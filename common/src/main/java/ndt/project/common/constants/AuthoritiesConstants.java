package ndt.project.common.constants;

import java.util.Arrays;
import java.util.List;

public interface AuthoritiesConstants {
    String ADMIN = "ADMIN";
    String USER = "USER";
    String ANONYMOUS = "ANONYMOUS";
    String ROLE_PREFIX = "ROLE_";
    String AUTHORITIES = "authorities";
    String FACEBOOK_OBJECT_NAME = "me";
    String DEFAULT_LANG = "English";
    String BEARER_TOKEN = "BearerToken";
    String TOKEN = "Token: ";
    String SUB_SYSTEM = "sub_system";
    String IS_VERIFY_OTP = "is_verify_otp";
    String CHECK_TOKEN_PARAM = "token";
    String IS_DECRYPT_DEVICE_ID = "is_decrypt_device_id";
    List<String> ACTUATOR_HEALTH_URL_LIST = Arrays.asList("/actuator/health", "/v1/actuator/health");
}
