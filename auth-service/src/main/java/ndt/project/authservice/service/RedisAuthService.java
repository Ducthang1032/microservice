package ndt.project.authservice.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ndt.project.authservice.config.AuthConfig;
import ndt.project.authservice.domain.SystemSetting;
import ndt.project.authservice.repository.SystemSettingRepository;
import ndt.project.common.constants.NumberConstants;
import ndt.project.common.utils.RedisKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisAuthService {

    private final RedisTemplate<String, String> redisTemplate;

    private final AuthConfig authConfig;

    private final CacheableService cacheableService;

    private final SystemSettingRepository systemSettingRepository;

    public void updateRedisConfirmMailOrPhone(String emailOrPhone, String value) {
        log.info("Start update status confirm OTP in redis for email or phone = {}, newStatus = {}", emailOrPhone, value);
        redisTemplate.opsForValue().set(
                RedisKeyUtil.redisConfirmMailOrPhoneKey(authConfig.getPrefixRedisKey(), emailOrPhone),
                value);
        log.info("SUCCESS update status confirm OTP in redis for email or phone = {}, newStatus = {}", emailOrPhone, value);
    }

    public void generateRedisOTP(String mailOrPhone, String otp) {
        log.info("Start save OTP to redis with email or phone = {}", mailOrPhone);
        redisTemplate.opsForValue().set(
                RedisKeyUtil.getOtpKey(authConfig.getPrefixRedisKey(), mailOrPhone),
                otp,
                authConfig.getOptRedisTimeOut(),
                TimeUnit.SECONDS);
        log.info("SUCCESS save OTP to redis with email or phone = {}", mailOrPhone);
    }

    public void countTimesPasswordFail(String userId) {
        try {
            log.info("Start count Times password fail with userId = {}", userId);
            String numberPasswordFail = redisTemplate.opsForValue()
                    .get(RedisKeyUtil.getTimesPasswordFailKey(authConfig.getPrefixRedisKey(), userId));

            numberPasswordFail = StringUtils.isBlank(numberPasswordFail) ? NumberConstants.STR_ONE
                    : String.valueOf(NumberUtils.toInt(numberPasswordFail) + NumberUtils.toInt(NumberConstants.STR_ONE));

            redisTemplate.opsForValue()
                    .set(RedisKeyUtil.getTimesPasswordFailKey(authConfig.getPrefixRedisKey(), userId),
                            numberPasswordFail, authConfig.getExpirationTimePasswordFail(), TimeUnit.MILLISECONDS);
            log.info("Finish count Times password fail with userId = {}", userId);
        } catch (Exception e) {
            log.error("FAILED while count Times password fail with userId = {}", userId, e);
        }

    }

    public boolean checkTimesPasswordFail(String userId) {
        log.info("Start check Times password fail with userId = {}", userId);
        String numberPasswordFail = redisTemplate.opsForValue()
                .get(RedisKeyUtil.getTimesPasswordFailKey(authConfig.getPrefixRedisKey(), userId));

        if (StringUtils.isBlank(numberPasswordFail)) return false;

        List<SystemSetting> systemSettings = systemSettingRepository.findAll();
        if (CollectionUtils.isEmpty(systemSettings)) return false;
        log.info("Finish check Times password fail with userId = {}", userId);
        return Objects.isNull(systemSettings.get(0).getMaxLoginFail())
                || NumberUtils.toInt(numberPasswordFail) >= systemSettings.get(0).getMaxLoginFail();
    }
}
