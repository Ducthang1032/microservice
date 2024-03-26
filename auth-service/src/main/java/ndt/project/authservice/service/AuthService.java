package ndt.project.authservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ndt.project.authservice.config.AuthConfig;
import ndt.project.authservice.domain.SystemRole;
import ndt.project.authservice.domain.UserEntity;
import ndt.project.authservice.dto.*;
import ndt.project.authservice.repository.SystemRoleRepository;
import ndt.project.authservice.repository.UserRepository;
import ndt.project.authservice.security.CustomAuthenticationManager;
import ndt.project.authservice.security.JwtTokenProvider;
import ndt.project.common.constants.AuthoritiesConstants;
import ndt.project.common.constants.CommonConstant;
import ndt.project.common.constants.EmailConstants;
import ndt.project.common.dto.MetaDTO;
import ndt.project.common.dto.ResponseMetaData;
import ndt.project.common.enums.MetaData;
import ndt.project.common.enums.SystemRoleType;
import ndt.project.common.enums.UserStatus;
import ndt.project.common.utils.DateTimeUtil;
import ndt.project.common.utils.RedisKeyUtil;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final CustomAuthenticationManager customAuthenticationManager;

    private final RedisTemplate<String, String> redisTemplate;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;

    private final UserRepository userRepository;

    private final AuthConfig authConfig;

    private final PasswordEncoder passwordEncoder;

    public final SendMailService sendMailService;


    private final AESService aesService;

    private final MongoTemplate mongoTemplate;

    private final SystemRoleRepository systemRoleRepository;

    public final BCryptPasswordEncoder encoder;


    public LoginResponseDTO generateToken(UserLoginDTO userLogin, UserEntity userEntity, boolean isCreateUser, String deviceId) {
        log.info("Start generate token with username: {}", userLogin.getUsername());
        // Default role is user
        SystemRole cqRole = systemRoleRepository.findFirstById(userEntity.getRoleId());
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList(AuthoritiesConstants.ROLE_PREFIX.concat(cqRole.getRole()));

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(String.valueOf(userEntity.getId()), userLogin.getPassword(), grantedAuthorities);

        // Authentication manager authenticate the user, and use UserDetailsServiceImpl::loadUserByUsername() method to load the user.
        Authentication auth = customAuthenticationManager.authenticate(authToken);

        if (Objects.isNull(auth)) {
            log.info("Authenticate fail with user: {}", userEntity.getDisplayName());
            return null;
        }

        // Set info authentication to Security Context
        SecurityContextHolder.getContext().setAuthentication(auth);

        Date expirationTime = new Date(System.currentTimeMillis() + authConfig.getExpirationToken());
        String token = jwtTokenProvider.generateToken(auth, deviceId, expirationTime);
        removeOldTokenInDevice(userLogin.getDeviceId(), userEntity.getId());
        redisTemplate.opsForValue().set(
                RedisKeyUtil.getAccessTokenKey(authConfig.getPrefixRedisKey(), userEntity.getId(),
                        subSystem, userLogin.getDeviceId()),
                token,
                authConfig.getExpirationToken(),
                TimeUnit.MILLISECONDS);

        String refreshToken = generateRefreshToken(authToken, subSystem, userEntity.getId(), userLogin);
        redisTemplate.opsForValue().set(
                RedisKeyUtil.getRefreshTokenKey(authConfig.getPrefixRedisKey(), userEntity.getId(), subSystem, userLogin.getDeviceId()),
                refreshToken,
                authConfig.getExpirationRefreshToken(),
                TimeUnit.MILLISECONDS);

        redisTemplate.opsForValue().set(
                RedisKeyUtil.getSPhotonChatTokenKey(authConfig.getPrefixRedisKey(), userEntity.getId(),
                        subSystem, userLogin.getDeviceId()),
                tokenChat,
                authConfig.getExpirationToken(),
                TimeUnit.MILLISECONDS);
        updateTokenNotification(userEntity.getId(), userLogin.getDeviceId(), userLogin.getFcmToken());
        log.info("Finish generate token with username: {}, subSystem = {}", userLogin.getUsername(), subSystem);
        return new LoginResponseDTO(token, refreshToken, tokenChat, DateTimeUtil.convertDateToLocalDateTime(expirationTime),
                userEntity, aesService, obsHuawei, cacheableService.getRoleNameById(userEntity.getRoleId()), cacheableService);
    }

    private LoginResponseDTO generateToken(UserEntity userEntity, String deviceId, Long subSystem, boolean isCreateUser,
                                           String platform, String fcmToken, String apnsToken
    ) {
        log.info("Start generate token with userId: {}, subSystem = {}", userEntity.getId(), subSystem);
        SystemRole cqRole = systemRoleRepository.findFirstById(userEntity.getRoleId());
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList(AuthoritiesConstants.ROLE_PREFIX.concat(cqRole.getRole()));

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(String.valueOf(userEntity.getId()), null, grantedAuthorities);

        SecurityContextHolder.getContext().setAuthentication(authToken);
        Date expirationTime = new Date(System.currentTimeMillis() + authConfig.getExpirationToken());
        String token = jwtTokenProvider.generateToken(authToken, deviceId, subSystem, expirationTime);
        removeOldTokenInDevice(deviceId, subSystem, userEntity.getId());
        redisTemplate.opsForValue().set(
                RedisKeyUtil.getAccessTokenKey(authConfig.getPrefixRedisKey(), userEntity.getId(), subSystem, deviceId),
                token,
                authConfig.getExpirationToken(),
                TimeUnit.MILLISECONDS);

        String refreshToken = generateRefreshToken(authToken, subSystem, userEntity.getId(), deviceId,
                platform, apnsToken, fcmToken);
        redisTemplate.opsForValue().set(
                RedisKeyUtil.getRefreshTokenKey(authConfig.getPrefixRedisKey(), userEntity.getId(), subSystem, deviceId),
                refreshToken,
                authConfig.getExpirationRefreshToken(),
                TimeUnit.MILLISECONDS);

        redisTemplate.opsForValue().set(
                RedisKeyUtil.getSPhotonChatTokenKey(authConfig.getPrefixRedisKey(), userEntity.getId(), subSystem, deviceId),
                tokenChat,
                authConfig.getExpirationToken(),
                TimeUnit.MILLISECONDS);
        updateTokenNotification(userEntity.getId(), deviceId, fcmToken);
        log.info("Finish generate token with userId: {}, subSystem = {}", userEntity.getId(), subSystem);
        return new LoginResponseDTO(token, refreshToken, tokenChat, DateTimeUtil.convertDateToLocalDateTime(expirationTime),
                userEntity, aesService, obsHuawei, cacheableService.getRoleNameById(userEntity.getRoleId()), cacheableService);
    }

    private boolean refreshTokenInfoIsBlank(RefreshTokenInfo refreshTokenInfo) {
        return Objects.isNull(refreshTokenInfo)
                || Objects.isNull(refreshTokenInfo.getSubSystem())
                || StringUtils.isAnyBlank(refreshTokenInfo.getFcmToken(), refreshTokenInfo.getUserId(),
                refreshTokenInfo.getDeviceId(), refreshTokenInfo.getPlatform())
                || (NotificationPlatform.isIosPlatform(refreshTokenInfo.getPlatform())
                && StringUtils.isBlank(refreshTokenInfo.getApnsToken()));
    }

    private LoginResponseDTO generateToken(RefreshTokenInfo refreshTokenInfo) {
        log.info("Start generate token with refreshToken, userId: {}, deviceId = {}",
                refreshTokenInfo.getUserId(), refreshTokenInfo.getDeviceId());
        if (refreshTokenInfoIsBlank(refreshTokenInfo)) {
            log.warn("Authenticate fail");
            return null;
        }
        UserEntity user = userRepository.findDistinctFirstById(Long.valueOf(refreshTokenInfo.getUserId()));

        if (Objects.isNull(user) || !UserStatus.ACTIVE.getValue().equalsIgnoreCase(user.getStatus())) {
            log.warn("UserId = {} not exist in deviceId = {}", refreshTokenInfo.getUserId(), refreshTokenInfo.getDeviceId());
            return null;
        }

        SystemRole systemRole = systemRoleRepository.findFirstById(user.getRoleId());
        List<GrantedAuthority> grantedAuthorities =
                AuthorityUtils.commaSeparatedStringToAuthorityList(AuthoritiesConstants.ROLE_PREFIX.concat(systemRole.getRole()));

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(refreshTokenInfo.getUserId(), null, grantedAuthorities);
        String refreshToken = generateRefreshToken(authToken, user.getId(), deviceId,
                platform, apnsToken, fcmToken);
        SecurityContextHolder.getContext().setAuthentication(authToken);
        Date expirationTime = new Date(System.currentTimeMillis() + authConfig.getExpirationToken());
        String token = jwtTokenProvider.generateToken(authToken, refreshTokenInfo.getDeviceId(), expirationTime);

        log.info("Finish generate token with userId: {}", user.getId());
        return new LoginResponseDTO(token, DateTimeUtil.convertDateToLocalDateTime(expirationTime), user,
                aesService, systemRole);
    }

    private String generateRefreshToken(Authentication authToken, Long userId, String deviceId) {
        Date expirationTime = new Date(System.currentTimeMillis() + authConfig.getExpirationRefreshToken());
        RefreshTokenInfo refreshTokenInfo = new RefreshTokenInfo(String.valueOf(userId), deviceId);
        return jwtTokenProvider.generateRefreshToken(authToken, refreshTokenInfo, expirationTime);
    }

    private void removeOldTokenInDevice(String deviceId, Long myId) {
        log.info("Start remove old token in device with deviceId = {}, not owned by {}",
                deviceId, myId);
        String headerKeyAccessToken = authConfig.getPrefixRedisKey().concat(CommonConstant.ACCESS_TOKEN_KEY).concat(CommonConstant.COLON);
        String footerKeyAccessToken = CommonConstant.COLON.concat(deviceId);

        Set<String> accessTokenKeyList = redisTemplate.keys(
                headerKeyAccessToken.concat(CommonConstant.ASTERISK).concat(footerKeyAccessToken));
        if (!CollectionUtils.isEmpty(accessTokenKeyList))
            accessTokenKeyList.stream()
                    .map(item -> NumberUtils.toLong(item.replace(headerKeyAccessToken, StringUtils.EMPTY)
                            .replace(footerKeyAccessToken, StringUtils.EMPTY)))
                    .filter(userId -> !Objects.equals(myId, userId))
                    .forEach(userId -> logout(userId, deviceId, ));
        log.info("SUCCESS remove old token in device with deviceId = {}, not owned by {}",
                deviceId, myId);
    }

    private String generateRefreshToken(Authentication authToken, Long userId, UserLoginDTO userLoginDTO) {
        return generateRefreshToken(authToken, userId, userLoginDTO.getDeviceId());
    }

    @SneakyThrows
    public ResponseEntity<ResMDLogin> loginByRefreshToken(RefreshTokenReq refreshTokenReq) {
        if (Objects.isNull(refreshTokenReq) || StringUtils.isBlank(refreshTokenReq.getRefreshToken()))
            return ResponseEntity.badRequest().body(new ResMDLogin(new MetaDTO(MetaData.REFRESH_TOKEN_IS_MISSING)));

        Claims claims = Jwts.parser()
                .setSigningKey(authConfig.getKeySecret().getBytes())
                .parseClaimsJws(refreshTokenReq.getRefreshToken())
                .getBody();

        RefreshTokenInfo refreshTokenInfo = (new ObjectMapper()).readValue(claims.getSubject(), RefreshTokenInfo.class);
        if (Objects.isNull(refreshTokenInfo))
            return ResponseEntity.status(MetaData.BAD_REQUEST.getMetaCode()).body(new ResMDLogin(new MetaDTO(MetaData.UNAUTHORIZED)));

        LoginResponseDTO loginResponseDTO = generateToken(refreshTokenInfo);
        if (Objects.isNull(loginResponseDTO))
            return ResponseEntity.status(MetaData.BAD_REQUEST.getMetaCode())
                    .body(new ResMDLogin(new MetaDTO(MetaData.UNAUTHORIZED), null));
        return ResponseEntity.status(MetaData.SUCCESS.getMetaCode())
                .body(new ResMDLogin(new MetaDTO(MetaData.SUCCESS), loginResponseDTO));
    }

    public LoginResponseDTO saveUserRegisSuccess(UserRegisterDTO registerDTO) throws JsonProcessingException {
        log.info("Start create new user with email = {}", registerDTO.getEmail());

        UserEntity userEntity = new UserEntity(registerDTO, cacheableService.getRoleIdByRoleName(SystemRoleType.USER.getRoleName()));
        userEntity.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        userEntity.setLang(AuthoritiesConstants.DEFAULT_LANG);
        if (SubSystem.isPoemOrPoemHK(subSystem) && BooleanUtils.isFalse(registerDTO.getIsAgreeTC()))
            userEntity.setStatus(UserStatus.DUMMY_P3.getValue());
        userEntity = setCategoryAndListTRCodesForPoem(userEntity, registerDTO);

        redisAuthService.updateRedisConfirmMailOrPhone(userEntity.getEmail(), EmailConstants.NOT_CONFIRM_EMAIL, subSystem.toString());

        //Create user in SPhoton
        String emailRegister = registerDTO.getEmail();
        String emailRegisterSPhoton = new StringBuilder(subSystem.toString()).append(emailRegister).toString();
        String userNameSPhoton = (!emailRegister.contains(AuthConstant.AT_SIGN) ? emailRegister : new StringBuilder(emailRegister.substring(0, emailRegister.indexOf(AuthConstant.AT_SIGN))).toString());
        String chatId = sPhotonChatService.createSPhotonUser(new UserCreateChat(emailRegisterSPhoton, userEntity.getPassword(), userNameSPhoton), subSystem);
        if (StringUtils.isBlank(chatId)) return null;

        userEntity.setChatId(chatId);
        userRepository.save(userEntity);

        //Create user setting default for new user
        createNewUserSetting(userEntity.getId(), UserStatus.isDummyP3(userEntity.getStatus()));

        //Create user in MongoDB
        mongoTemplate.save(new CQUserMongo(userEntity));

        LoginResponseDTO response = generateToken(new UserLoginDTO(registerDTO), userEntity, true);
        userService.addVerifyUserPoemToGroupOfTR(userEntity);
        userService.addVerifyUserPoemToGroupPublic(userEntity);
        log.info("SUCCESS while create new user with userId = {}, email = {}, phone = {}, subSystem = {}", userEntity.getId(), registerDTO.getEmail(), registerDTO.getPhone(), subSystem);
        return response;
    }

    public void createNewUserSetting(Long userId, boolean isDummyP3) throws JsonProcessingException {
        log.info("Start create new user setting with userId = {}", userId);
        CQUserSetting newUserSetting = new CQUserSetting(userId, isDummyP3);
        userSettingRepository.save(newUserSetting);
        redisTemplate.delete(RedisKeyUtil.getUserSettingNotificationKey(authConfig.getPrefixRedisKey(), userId));
        redisTemplate.opsForValue().set(
                RedisKeyUtil.getUserSettingKey(authConfig.getPrefixRedisKey(), userId),
                (new ObjectMapper()).writeValueAsString(newUserSetting)
        );

        //Generate list topic setting of user and add to database
        List<CQUserTopic> userTopicList = cacheableService.mappingTopic(CommonConstant.TOPIC_CACHE_KEY_NAME).keySet().stream()
                .map(item -> new CQUserTopic(newUserSetting.getId(), item)).collect(Collectors.toList());

        userTopicRepository.saveAll(userTopicList);
        log.info("SUCCESS while create new user setting with userId = {}", userId);
    }

    public LoginResponseDTO loginEmailOtp(String email, LoginOtpDTO loginOtp, Long subSystem) {
        log.info("Start generate token with email user info: {}, subSystem = {}", email, subSystem);
        UserEntity user = userRepository.findOneByEmailIgnoreCaseAndSubSystem(email, subSystem);
        if (Objects.isNull(user) || !UserStatus.ACTIVE.getValue().equalsIgnoreCase(user.getStatus())) {
            log.warn("FAILED while generate token with email user info: {}, subSystem = {}", email, subSystem);
            return null;
        }

        LoginResponseDTO loginResponseDTO = generateToken(user, aesService.decryptDataWithoutUrl(loginOtp.getDeviceId()),
                subSystem, false, loginOtp.getPlatform(), loginOtp.getFcmToken(), loginOtp.getApnsToken());
        log.info("SUCCESS while generate token with email user info: {}, subSystem = {}", email, subSystem);
        return loginResponseDTO;
    }


    public ResponseEntity<ResponseMetaData> logout(Long userId, String deviceId, Long subSystem) {
        log.info("Start logout with user id = {}, device id = {}, subSystem = {}", userId, deviceId, subSystem);
        redisTemplate.delete(RedisKeyUtil.getRefreshTokenKey(authConfig.getPrefixRedisKey(), userId, subSystem, deviceId));
        redisTemplate.delete(RedisKeyUtil.getAccessTokenKey(authConfig.getPrefixRedisKey(), userId, subSystem, deviceId));
        String redisChatTokenKey = RedisKeyUtil.getSPhotonChatTokenKey(authConfig.getPrefixRedisKey(), userId, subSystem, deviceId);
        redisTemplate.delete(redisChatTokenKey);
        log.info("Finish logout with user id = {}, device id = {}, subSystem ={}", userId, deviceId, subSystem);
        return ResponseEntity.status(MetaData.SUCCESS.getMetaCode())
                .body(new ResponseMetaData(new MetaDTO(MetaData.SUCCESS), null));
    }

    public ResponseEntity<ResMDLogin> updateNewPassword(Long userId, Long subSystem, UpdateNewPassword updateNewPassword) {
        log.info("SUCCESS while update new password with userId = {}, subSystem = {}", userId, subSystem);

        if (redisAuthService.checkTimesPasswordFail(String.valueOf(userId), subSystem))
            return ResponseEntity.badRequest().body(new ResMDLogin(
                    new MetaDTO(MetaData.PASSWORD_FAIL_TOO_MANY_TIMES), null));

        UserEntity userInfo = userRepository.findDistinctFirstById(userId, subSystem);
        if (Objects.isNull(userInfo) || !UserStatus.ACTIVE.getValue().equalsIgnoreCase(userInfo.getStatus())) {
            log.warn("User not exist");
            return ResponseEntity.badRequest().body(new ResMDLogin(new MetaDTO(MetaData.USER_NOT_EXIST), null));
        }

        if (!encoder.matches(updateNewPassword.getCurrentPassword(), userInfo.getPassword())) {
            redisAuthService.countTimesPasswordFail(String.valueOf(userId), subSystem);
            log.warn("Password is incorrect");
            return ResponseEntity.badRequest().body(new ResMDLogin(new MetaDTO(MetaData.PASSWORD_INCORRECT), null));
        }
        String oldPassword = userInfo.getPassword();
        if (encoder.matches(updateNewPassword.getNewPassword(), oldPassword)
                || encoder.matches(updateNewPassword.getNewPassword(), userInfo.getPrePassword1())
                || encoder.matches(updateNewPassword.getNewPassword(), userInfo.getPrePassword2())) {
            log.warn("This password has already been used");
            return ResponseEntity.badRequest().body(new ResMDLogin(new MetaDTO(MetaData.PASSWORD_ALREADY_USED), null));
        }
        userInfo.setPrePassword2(userInfo.getPrePassword1());
        userInfo.setPrePassword1(oldPassword);
        userInfo.setPassword(encoder.encode(updateNewPassword.getNewPassword()));
        if (!userService.updatePasswordSPhotonUser(userInfo.getEmail(), oldPassword, userInfo.getPassword(), subSystem)) {
            log.warn("FAILED while update password with userId = {}, subSystem = {}", userInfo.getId(), subSystem);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(new ResMDLogin(new MetaDTO(MetaData.SERVICE_UNAVAILABLE), null));
        }
        userRepository.save(userInfo);
        userService.deleteOldToken(String.valueOf(userId), subSystem);
        LoginResponseDTO loginResponse = generateTokenWhenUpdatePassword(userInfo, updateNewPassword, subSystem);
        log.info("SUCCESS while update new password with userId = {}, subSystem = {}", userInfo.getId(), subSystem);
        return ResponseEntity.ok(new ResMDLogin(new MetaDTO(MetaData.SUCCESS), loginResponse));
    }

    private LoginResponseDTO generateTokenWhenUpdatePassword(UserEntity user, UpdateNewPassword updateNewPassword, Long subSystem) {
        return generateToken(user, updateNewPassword.getDeviceId(), subSystem, false,
                updateNewPassword.getPlatform(), updateNewPassword.getFcmToken(), updateNewPassword.getApnsToken());
    }

}
