package ndt.project.common.utils;


import io.micrometer.common.util.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import java.security.SecureRandom;
import java.util.Objects;
import java.util.regex.Pattern;

public final class NumberUtil {

    /**
     * Generate random 6 numbers
     */
    public static String generateOTP() {
        // random 6 number
        SecureRandom secureRandom = new SecureRandom();
        int otp = 100000 + secureRandom.nextInt(900000);
        return String.valueOf(otp);
    }

    public static boolean isNumeric(String strNum) {
        Pattern pattern = Pattern.compile("\\d+");
        if (StringUtils.isBlank(strNum)) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }

    public static Long parseLong(String parseValue, Long defaultValue) {
        if (StringUtils.isBlank(parseValue))
            return defaultValue;

        return Long.valueOf(parseValue);
    }

    public static Long convertSecondToMinute(Long secondNum) {
        return secondNum / 60;
    }

    public static Long convertPositiveToNegative(Long number) {
        return number * -1;
    }

    public static boolean isPositiveInteger(Integer input) {
        return Objects.nonNull(input) && (input > NumberUtils.INTEGER_ZERO);
    }

    public static boolean isNonPositiveInteger(Integer input) {
        return !isPositiveInteger(input);
    }
}