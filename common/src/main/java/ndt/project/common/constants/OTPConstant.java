package ndt.project.common.constants;

public interface OTPConstant {

    // This parameter does not need to be modified.
    // It is used to format the Authentication header field and assign a value to the X-WSSE parameter.
    String WSSE_HEADER_FORMAT = "UsernameToken Username=\"%s\",PasswordDigest=\"%s\",Nonce=\"%s\",Created=\"%s\"";
    // This parameter does not need to be modified.
    // It is used to format the Authentication header field and assign a value to the Authorization parameter.
    String AUTH_HEADER_VALUE = "WSSE realm=\"SDP\",profile=\"UsernameToken\",type=\"Appkey\"";

    String SIGNATURE = "SMS OTP";
    String SSL = "SSL";
    String INSTANCE = "SHA-256";
    String CONTENT_TYPE = "Content-Type";
    String APPLICATION_BODY_TYPE = "application/x-www-form-urlencoded";
    String REQUEST_PROPERTY = "X-WSSE";
    String METHOD_POST = "POST";
    String NOT_CONFIRM_OTP = "false";
    String CONFIRMED_OTP = "true";
    String REDIS_OTP_KEY = "otp:";
    String REDIS_CONFIRM_MAIL_OR_PHONE_KEY = "confirmEmailOrPhone:";
}
