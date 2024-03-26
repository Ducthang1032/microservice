package ndt.project.authservice.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ndt.project.authservice.config.AuthConfig;
import ndt.project.authservice.domain.UserEntity;
import ndt.project.authservice.dto.UserLoginDTO;
import ndt.project.authservice.dto.UserRegisterDTO;
import ndt.project.authservice.repository.UserRepository;
import ndt.project.common.constants.CommonConstant;
import ndt.project.common.constants.OTPConstant;
import ndt.project.common.dto.MetaDTO;
import ndt.project.common.dto.ResponseMetaData;
import ndt.project.common.enums.LoginType;
import ndt.project.common.enums.MetaData;
import ndt.project.common.enums.UserStatus;
import ndt.project.common.utils.RedisKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    public final SendMailService sendMailService;

    public final BCryptPasswordEncoder encoder;

    private final AuthConfig authConfig;

    private final AESService aesService;

    private final MongoTemplate mongoTemplate;

    private final UserRepository userRepository;

    private final RedisTemplate<String, String> redisTemplate;

    public UserEntity checkLogin(UserLoginDTO userLogin) {
        List<UserEntity> userList = new ArrayList<>();
        if (userLogin.getType().equalsIgnoreCase(LoginType.EMAIL.getValue()))
            userList = userRepository.findAllByEmailIgnoreCase(StringUtils.lowerCase(userLogin.getUsername()));

        if (CollectionUtils.isEmpty(userList))
            return null;

        return userList.stream().findFirst().orElse(null);
    }

    public MetaDTO checkEmailAndPhoneBeforeRegister(UserRegisterDTO userRegister) {
        if (StringUtils.isNotBlank(userRegister.getEmail())) {
            if (!isEmailOrPhoneVerified(userRegister.getEmail())) {
                log.warn("Email = {} is not verified", userRegister.getEmail());
                return new MetaDTO(MetaData.ACCESS_DENIED);
            }
            if (Objects.nonNull(userRepository.findOneByEmailIgnoreCase(userRegister.getEmail()))) {
                log.warn("Email = {} is existing", userRegister.getEmail());
                return new MetaDTO(MetaData.EMAIL_EXIST);
            }
        }
        return null;
    }

    public MetaDTO checkEmailBeforeRegisterP3(String email) {
        if (Strings.isBlank(email)) {
            log.warn("Email is missing");
            return new MetaDTO(MetaData.EMAIL_MISSING);
        }
        if (Objects.nonNull(getUserByEmail(email))) {
            log.warn("Email = {} is existing", email);
            return new MetaDTO(MetaData.EMAIL_EXIST);
        }
        return null;
    }

    public UserEntity getUserByEmailOrPhone(UserNewPasswordDTO userNewPasswordDTO) {
        UserEntity user;
        if (StringUtils.isNotBlank(userNewPasswordDTO.getEmail()))
            user = userRepository.findOneByEmailIgnoreCase(userNewPasswordDTO.getEmail());
        else user = userRepository.findOneByPhone(userNewPasswordDTO.getPhone());

        if (Objects.isNull(user) || !UserStatus.ACTIVE.getValue().equalsIgnoreCase(user.getStatus())) {
            log.warn("Email {} is not existing in subSystem = {}", userNewPasswordDTO.getEmail());
            return null;
        }
        return user;
    }

    public UserEntity getUserByEmail(String email) {
        return userRepository.findOneByEmailIgnoreCase(email);
    }

    public boolean updatePasswordAndDeleteOldToken(UserEntity userEntity, UserNewPasswordDTO userNewPasswordDTO, String phoneOrEmail) {
        log.info("Start update password and delete old token with userId = {}", userEntity.getId());
        String oldPassword = userEntity.getPassword();
        userEntity.setPassword(encoder.encode(userNewPasswordDTO.getNewPassword()));
        redisAuthService.updateRedisConfirmMailOrPhone(phoneOrEmail, EmailConstants.NOT_CONFIRM_EMAIL.toString());
        userRepository.save(userEntity);
        deleteOldToken(String.valueOf(userEntity.getId()));
        log.info("SUCCESS update password with userId = {}", userEntity.getId());
        return true;
    }

    public void deleteOldToken(String userId) {
        log.info("Start delete old token with userId = {}", userId);

        Set<String> refreshTokenKeyList = redisTemplate.keys(
                RedisKeyUtil.getRefreshTokenKeyListByUserId(authConfig.getPrefixRedisKey(), userId));
        if (!CollectionUtils.isEmpty(refreshTokenKeyList))
            redisTemplate.delete(refreshTokenKeyList);

        Set<String> accessTokenKeyList = redisTemplate.keys(
                RedisKeyUtil.getAccessTokenKeyListByUserId(authConfig.getPrefixRedisKey(), userId));
        if (!CollectionUtils.isEmpty(accessTokenKeyList))
            redisTemplate.delete(accessTokenKeyList);
        log.info("SUCCESS delete old token with userId = {}", userId);
    }

    public boolean verifyOTP(String emailOrPhone, String otpEncrypt, String subSystem) {
        String redisOtpKey = RedisKeyUtil.getOtpKey(authConfig.getPrefixRedisKey(), emailOrPhone);
        String redisConfirmKey = RedisKeyUtil.redisConfirmMailOrPhoneKey(authConfig.getPrefixRedisKey(), emailOrPhone);

        if (aesService.decryptDataWithoutUrl(otpEncrypt).equals(redisTemplate.opsForValue().get(redisOtpKey))
                && OTPConstant.NOT_CONFIRM_OTP.equals(redisTemplate.opsForValue().get(redisConfirmKey))) {
            redisTemplate.opsForValue().set(
                    redisConfirmKey, OTPConstant.CONFIRMED_OTP,
                    authConfig.getExpireTimeConfirmOtpOrPassword(), TimeUnit.MILLISECONDS);
            redisTemplate.delete(redisOtpKey);
            return true;
        }
        return false;
    }

    public boolean verifyLoginOTP(String emailOrPhone, String otpEncrypt) {
        String redisOtpKey = RedisKeyUtil.getOtpKey(authConfig.getPrefixRedisKey(), emailOrPhone);
        if (!aesService.decryptDataWithoutUrl(otpEncrypt).equals(redisTemplate.opsForValue().get(redisOtpKey)))
            return false;

        redisTemplate.delete(redisOtpKey);
        return true;
    }

    public boolean isEmailOrPhoneVerified(String emailOrPhone) {
        String redisConfirmKey = RedisKeyUtil.redisConfirmMailOrPhoneKey(authConfig.getPrefixRedisKey(), emailOrPhone);
        return OTPConstant.CONFIRMED_OTP.equals(redisTemplate.opsForValue().get(redisConfirmKey));
    }

    public ResponseEntity<ResponseMetaData> confirmPassword(Long userId, ConfirmPassword confirmPassword) {
        log.info("SUCCESS while confirm password with userId = {}", userId);

        if (redisAuthService.checkTimesPasswordFail(String.valueOf(userId)))
            return ResponseEntity.badRequest().body(new ResponseMetaData(
                    new MetaDTO(MetaData.PASSWORD_FAIL_TOO_MANY_TIMES)));

        String currentPasswordDecrypt = aesService.decryptDataWithoutUrl(confirmPassword.getCurrentPassword());
        UserEntity userInfo = userRepository.findDistinctFirstById(userId);
        if (Objects.isNull(userInfo) || !UserStatus.ACTIVE.getValue().equalsIgnoreCase(userInfo.getStatus())) {
            log.warn("UserId = {} not exist in subSystem = {}", userId);
            return ResponseEntity.badRequest().body(new ResponseMetaData(new MetaDTO(MetaData.USER_NOT_EXIST)));
        }

        if (!encoder.matches(currentPasswordDecrypt, userInfo.getPassword())) {
            redisAuthService.countTimesPasswordFail(String.valueOf(userId));
            log.warn("Password is incorrect with userId = {}", userId);
            return ResponseEntity.badRequest().body(new ResponseMetaData(new MetaDTO(MetaData.PASSWORD_INCORRECT)));
        }

        redisTemplate.opsForValue().set(
                RedisKeyUtil.redisConfirmPasswordKey(authConfig.getPrefixRedisKey(), userId),
                CommonConstant.CONFIRMED, authConfig.getExpireTimeConfirmOtpOrPassword(), TimeUnit.MILLISECONDS);

        log.info("SUCCESS while confirm password with userId = {}", userInfo.getId());
        return ResponseEntity.ok(new ResponseMetaData(new MetaDTO(MetaData.SUCCESS)));
    }

    @Transactional
    public ResponseEntity<ResponseMetaData> deleteUserAccount(Long userId) {
        log.info("Start delete account with userId = {}", userId);
        UserEntity account = userRepository.findDistinctFirstById(userId);
        if (Objects.isNull(account)) {
            log.warn("UserId = {} not exist ", userId);
            return ResponseEntity.badRequest().body(new ResponseMetaData(new MetaDTO(MetaData.NOT_FOUND)));
        }
        account.setStatus(UserStatus.REMOVED.getValue());
        userRepository.save(account);
        redisTemplate.delete(RedisKeyUtil.getUserStatusKey(authConfig.getPrefixRedisKey(), account.getId()));
        deleteOldToken(String.valueOf(userId));
        log.info("FINISH delete accountId = {} with userId = {}", account.getId(), userId);
        return ResponseEntity.ok(new ResponseMetaData(new MetaDTO(MetaData.SUCCESS)));
    }

    public void addVerifyUserPoemToGroupPublic(UserEntity user) {
        if (Objects.isNull(user)) return;
        log.info("Start auto join Verify user to public group with userId = {}", user.getId());
        List<CQGroupPost> groupPosts = mongoCQGroupPostRepository
                .findAllBySubSystemAndStatusInAndPrivacy(user.getSubSystem(), CommonConstant.GROUP_STATUS_ACTIVE_AND_REPORTED, GroupPrivacy.PUBLIC.getValue());
        MemberGroup member = new MemberGroup(user.getId(), GroupRole.MEMBER, MemberGroupStatus.ACTIVE);
        groupPosts.forEach(item -> autoJoinGroupWithUserP3(item, member, user.getStatus()));
        log.info("SUCCESS auto join Verify user to public group with userId = {}", user.getId());
    }

    private Map<Long, CQFollowRelation> addFollowRelation(Long myId, List<Long> listTRIdAndMe) {
        log.info("Start follow list user with myId = {}", myId);
        if (CollectionUtils.isEmpty(listTRIdAndMe) || Objects.isNull(myId)) return new HashMap<>();
        Map<Long, CQFollowRelation> followRelationMap =
                mongoCQFollowRelationRepository.getCQFollowRelationByIdIn(listTRIdAndMe).stream()
                        .collect(Collectors.toMap(CQFollowRelation::getId, Function.identity()));
        CQFollowerDetail meAsFollower = new CQFollowerDetail(String.valueOf(myId));
        List<CQFollowingDetail> followingDetails = new ArrayList<>();
        for (Long id : listTRIdAndMe) {
            if (id.equals(myId)) continue;
            //Add me to list follower of other
            CQFollowRelation followRelation = followRelationMap.getOrDefault(id, new CQFollowRelation(id))
                    .formatFollowRelation();
            followRelation.getFollowers().add(meAsFollower);
            followRelation.setFollowers(ObjectUtil.distinctByLastKey(followRelation.getFollowers(), CQFollowerDetail::getId));
            followRelationMap.put(id, followRelation);

            //Add other to my following list
            followingDetails.add(new CQFollowingDetail(String.valueOf(id), true));
        }
        //Add list following to my following list
        CQFollowRelation myFollowRelation = followRelationMap.getOrDefault(myId, new CQFollowRelation(myId))
                .formatFollowRelation();
        myFollowRelation.getFollowings().addAll(followingDetails);
        myFollowRelation.setFollowings(ObjectUtil.distinctByLastKey(myFollowRelation.getFollowings(), CQFollowingDetail::getId));
        followRelationMap.put(myId, myFollowRelation);

        log.info("SUCCESS follow list user with clientId = {}", myId);
        return followRelationMap;
    }
}
