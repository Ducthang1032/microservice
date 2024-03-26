package ndt.project.common.utils;


import ndt.project.common.constants.RegexConstants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class EmailUtils {
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile(RegexConstants.EMAIL_REGEX, Pattern.CASE_INSENSITIVE);

    public static boolean isEmail(final String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }
}
