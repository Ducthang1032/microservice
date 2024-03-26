package ndt.project.authservice.service;


import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ndt.project.authservice.config.AuthConfig;
import ndt.project.authservice.domain.UserEntity;
import ndt.project.authservice.dto.ConfirmEmailDTO;
import ndt.project.common.constants.EmailConstants;
import ndt.project.common.dto.MetaDTO;
import ndt.project.common.dto.ResponseMetaData;
import ndt.project.common.enums.MetaData;
import ndt.project.common.utils.NumberUtil;
import ndt.project.common.utils.RedisKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.internet.MimeMessage;
import java.util.Objects;

import static ndt.project.common.constants.CommonConstant.*;


@Service
@Slf4j
@RequiredArgsConstructor
public class SendMailService {

    public final JavaMailSender emailSender;

    public final RedisTemplate<String, String> redisTemplate;

    private final AuthConfig authConfig;

    private final SpringTemplateEngine templateEngine;

    private final MessageSource messageSource;

    private final RedisAuthService redisAuthService;

    @SneakyThrows
    public void sendMail(String subject, String content, String mailTo) {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MULTIPART, UTF8_ENCODING);
        message.setContent(content, MESSAGE_TYPE);
        helper.setFrom(authConfig.getMailServerUsername(), authConfig.getMailServerSenderName());
        helper.setSubject(subject);
        helper.setTo(mailTo);
        this.emailSender.send(message);
    }

    public MetaDTO sendOTP(UserEntity userEntity) {
        if (checkExistOTP(userEntity.getEmail())) return new MetaDTO(MetaData.SEND_REQUEST_TOO_MANY_TIMES);
        String otp = NumberUtil.generateOTP();
        Context context = new Context();
        context.setVariable("displayName", userEntity.getDisplayName());
        context.setVariable("otp", otp);
        String content = templateEngine.process("mail/otpEmail", context);
        String subject = messageSource.getMessage("email.activation.title", null, null);

        log.info("Save isNotConfirm in redis for email {}", userEntity.getEmail());
        redisAuthService.updateRedisConfirmMailOrPhone(userEntity.getEmail(), EmailConstants.NOT_CONFIRM_EMAIL);

        log.info("Save OTP in redis for email {}", userEntity.getEmail());
        redisAuthService.generateRedisOTP(userEntity.getEmail(), otp);

        log.info("Start send OTP to email {}", userEntity.getEmail());
        sendMail(subject, content, userEntity.getEmail());
        return null;
    }

    public MetaDTO sendOTP(String email) {
        if (checkExistOTP(email)) return new MetaDTO(MetaData.SEND_REQUEST_TOO_MANY_TIMES);
        String otp = NumberUtil.generateOTP();
        Context context = new Context();
        context.setVariable("displayName", "Sir/Madam");
        context.setVariable("otp", otp);
        String content = templateEngine.process("mail/otpEmail", context);
        String subject = messageSource.getMessage("email.activation.title", null, null);

        log.info("Save isNotConfirm in redis for email {}", email);
        redisAuthService.updateRedisConfirmMailOrPhone(email, EmailConstants.NOT_CONFIRM_EMAIL);

        log.info("Save OTP in redis for email {}", email);
        redisAuthService.generateRedisOTP(email, otp);

        log.info("Start send OTP to email {}", email);
        sendMail(subject, content, email);
        return null;
    }

    public boolean checkExistOTP(String email) {
        String oldOTP = redisTemplate.opsForValue().get(
                RedisKeyUtil.getOtpKey(authConfig.getPrefixRedisKey(), email));
        return StringUtils.isNotBlank(oldOTP);
    }

    public ResponseEntity<ResponseMetaData> confirmEmail(ConfirmEmailDTO confirmEmailDTO,
                                                         ValidationService validationService, UserService userService) {
        UserEntity userEntity = userService.getUserByEmail(confirmEmailDTO.getEmail());
        MetaDTO metaDTO = validationService.validationUserWithType(confirmEmailDTO.getType(), userEntity);
        if (Objects.nonNull(metaDTO)) {
            log.warn("FAILED confirm email with email = {}", confirmEmailDTO.getEmail());
            ResponseEntity.ok().body(new ResponseMetaData(new MetaDTO(
                    MetaData.REGISTER_GENERAL_MESSAGE.getMetaCode(),
                    String.format(MetaData.REGISTER_GENERAL_MESSAGE.getMessage(), confirmEmailDTO.getEmail()))));
        }

        if (Objects.nonNull(userEntity))
            metaDTO = sendOTP(userEntity);
        else
            metaDTO = sendOTP(confirmEmailDTO.getEmail());

        if (Objects.nonNull(metaDTO))
            return ResponseEntity.status(MetaData.BAD_REQUEST.getMetaCode())
                    .body(new ResponseMetaData(metaDTO));

        log.info("SUCCESS confirm email with email = {} = {}", confirmEmailDTO.getEmail());
        return ResponseEntity.ok().body(new ResponseMetaData(new MetaDTO(
                MetaData.REGISTER_GENERAL_MESSAGE.getMetaCode(),
                String.format(MetaData.REGISTER_GENERAL_MESSAGE.getMessage(), confirmEmailDTO.getEmail()))));
    }
}
