package ndt.project.common.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum SendEmailOtpType {
    FORGOT_PASSWORD("forgot_password"),
    REGISTER("register"),
    PIN_LOGIN_OTP("login");

    private final String value;

    SendEmailOtpType(String value) {
        this.value = value;
    }

    public static boolean typeIsExist(String type) {
        return Arrays.stream(SendEmailOtpType.values()).anyMatch(item -> item.value.equalsIgnoreCase(type));
    }
}
