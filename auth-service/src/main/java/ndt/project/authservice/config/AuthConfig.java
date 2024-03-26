package ndt.project.authservice.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Getter
@Component
public class AuthConfig {

    @Value("${app.rest-template.connect-timeout}")
    private Integer restTemplateConnectTimeout;

    @Value("${app.security.expiration-token}")
    private Long expirationToken;

    @Value("${app.security.expiration-refresh-token}")
    private Long expirationRefreshToken;

    @Value("${app.security.key-secret}")
    private String keySecret;

    @Value("${app.security.ignore-auth-api}")
    private String[] ignoreAuthApi;

    @Value("${spring.redis.prefix-key}")
    private String prefixRedisKey;

    @Value("${app.security.email-linkedIn-api}")
    private String emailLinkedInApi;

    @Value("${app.security.user-facebook-api}")
    private String UserFacebookApi;

    @Value("${app.security.user-google-api}")
    private String userGoogleApi;

    @Value("${mail.server.host}")
    private String mailServerHost;

    @Value("${mail.server.port}")
    private String mailServerPort;

    @Value("${mail.server.protocol}")
    private String mailServerProtocol;

    @Value("${mail.server.username}")
    private String mailServerUsername;

    @Value("${mail.server.sender-name}")
    private String mailServerSenderName;

    @Value("${mail.server.password}")
    private String mailServerPassword;

    @Value("${app.cipher.encrypt-key}")
    private String cipherSecretKey;

    @Value("${spring.redis.otp-time-out}")
    private Long optRedisTimeOut;

    @Value("${otp-phone.url}")
    private String otpPhoneUrl;

    @Value("${otp-phone.app-key}")
    private String otpPhoneAppKey;

    @Value("${otp-phone.app-secret}")
    private String otpPhoneAppSecret;

    @Value("${otp-phone.sender}")
    private String otpPhoneSender;

    @Value("${otp-phone.template-id}")
    private String otpPhoneTemplateId;

    @Value("${app.threadPool.max-pool-size}")
    private int maxPoolSize;

    @Value("${app.threadPool.core-pool-size}")
    private int corePoolSize;

    @Value("${app.threadPool.keep-alive-time}")
    private int threadPoolKeepAliveTime;

    @Value("${app.threadPool.thread-name-prefix}")
    private String threadNamePrefix;

    @Value("${app.security.sphoton-apis.create-user}")
    private String createUserSPhotonApi;

    @Value("${app.security.sphoton-apis.invite-member}")
    private String inviteMemberSPhotonApi;

    @Value("${app.security.sphoton-apis.logout}")
    private String logoutSPhotonApi;

    @Value("${app.security.sphoton-apis.revoke-all-session}")
    private String revokeAllSessionSPhotonApi;

    @Value("${app.security.sphoton-apis.login}")
    private String loginSPhotonApi;

    @Value("${app.security.sphoton-apis.update-password}")
    private String updatePasswordSPhoTonApi;

    @Value("${app.security.expiration-time-password-fail}")
    private Long expirationTimePasswordFail;

    @Value("${app.security.apple-jwk-api}")
    private String appleJwkApi;

    @Value("${app.security.expire-time-confirm-otp-or-password}")
    private Long expireTimeConfirmOtpOrPassword;

    @Value("${app.security.expire-time-confirm-qr-code}")
    private Long expireTimeConfirmQRCode;

    @Value("${app.security.sphoton-expiration-token}")
    private Long expireSphotonToken;

    @Value("${app.security.sphoton-apis.create-direct-message-channel}")
    private String createDirectMessage;

    @Value("${app.security.sphoton-apis.create-post}")
    private String createPostsChat;

    @Value("${app.security.sphoton-team-id-p3}")
    private List<String> sPhotonTeamIdP3;

    @Value("${app.country-code-allow}")
    private Set<String> listCountryCodeAllow;
}
