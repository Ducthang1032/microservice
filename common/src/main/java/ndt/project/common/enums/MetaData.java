package ndt.project.common.enums;

import lombok.Getter;

@Getter
public enum MetaData {
    //Successful responses
    SUCCESS(200, "Success"),
    NO_CONTENT(204, "No Content"),
    UNSUPPORTED_MEDIA_TYPE(415, "Unsupported media type"),

    // Redirection messagesY
    MULTIPLE_CHOICE(300, "Multiple Choice"),

    BAD_CREDENTIALS(301, "Bad Credentials"),

    // Client error responses
    BAD_REQUEST(400, "Bad request"),
    UNAUTHORIZED(401, "Unauthorized"),
    PAYMENT_REQUIRED(402, "Payment Required"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not Found"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    ACTION_NOT_ALLOWED(406, "Action Not Allowed"),
    REQUEST_TIMEOUT(407, "Request Timeout"),
    USERNAME_OR_PASSWORD_INCORRECT(408, "Incorrect username or password."),
    DOB_INVALID(409, "Dob is wrong format"),
    TOKEN_EXPIRED(410, "Token expired"),
    TOKEN_INVALID(411, "Token invalid"),
    HTTP_MEDIA_TYPE_NOT_SUPPORTED(412, "Http Media Type Not Supported"),
    INPUT_ENCODING_DATA_INVALID(413, "Http Version Not Supported"),
    ACCESS_DENIED(4810, "Access is denied"),
    SEND_REQUEST_TOO_MANY_TIMES(414, "Send request too many times"),
    EMAIL_EXIST(415, "Email is exist"),
    REFRESH_TOKEN_IS_MISSING(416, "Refresh token is missing"),
    PASSWORD_INCORRECT(417, "Incorrect username or password."),
    PASSWORD_FAIL_TOO_MANY_TIMES(418, "Password failed more than the specified number of times"),
    REGISTER_GENERAL_MESSAGE(419, "We have sent you the OTP code via %s. Please take a look."),
    PASSWORD_INVALID(420, "Password must contain at least 8 characters, at least one lowercase letter, one uppercase letter, one numeric digit, and one special character"),
    EMAIL_INVALID(421, "Email is invalid"),
    EMAIL_NOT_EXIST(422, "Email is not exist"),

    // Server error responses
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    BAD_GATEWAY(502, "Bad Gateway"),
    SERVICE_UNAVAILABLE(503, "Service Unavailable"),
    GATEWAY_TIMEOUT(504, "Gateway Timeout");

    private final Integer metaCode;
    private final String message;

    MetaData(Integer metaCode, String message) {
        this.metaCode = metaCode;
        this.message = message;
    }

}
