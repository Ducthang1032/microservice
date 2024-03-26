package ndt.project.common.constants;

public interface RegexConstants {
    String EMAIL_REGEX = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
    String SPHOTON_USERNAME_REGEX = "[^a-zA-Z0-9._-]";

    /**
     * (?=.{8,}): Password must contain at least 8 characters
     * (?=.*[0-9]): a digit must occur at least once
     * (?=.*[a-z]): a lower case letter must occur at least once
     * (?=.*[A-Z]): an upper case letter must occur at least once
     * (?=.*[!"#$%&'()*+,\\./:;<>=?\[@\]_`{}|~^-]): a special character must occur at least once
     * List of special characters: !"#$%&'()*+,\./:;<>=?[@]_`{}|~^-
     */
    String PASSWORD_REGEX = "^.*(?=.{8,})(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!\"#$%&'()*+,\\\\./:;<>=?\\[@\\]_`{}|~^-]).*$";
    String GENDER_REGEX = "[mfoMFO]";
    String HASHTAG_REGEX = "#[\\w]*";

    //'\\w' matches any word character (equivalent to [a-zA-Z0-9_])
    //'-' matches the character '-'
    //'\\u4e00-\\u9fa5' matches a single character in the range between '一' (index 19968) and '龥' (index 40869)
    String HASHTAG_REGEX_POEM = "#[\\w-\\u4e00-\\u9fa5]*";
    /**
     * ^ : asserts position at start of a line <p>
     * \w : matches any word character (equivalent to [a-zA-Z0-9_]) <p>
     * \u4e00-\u9fa5 : matches a single character in the range between '一' (index 19968) and '龥' (index 40869) <p>
     * {4,30} : matches input between 4 and 30 characters <p>
     * $ : asserts position at the end of a line
     */
    String USERNAME_REGEX_POEM = "^[\\w\\u4e00-\\u9fa5]{4,30}$";
    String PREFIX_HASHTAG = "{{";
    String SUPFIX_HASHTAG = "}}";
    String GET_URL_REGEX = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
    String PHONE_REGEX = "\\+(9[976]\\d|8[987530]\\d|6[987]\\d|5[90]\\d|42\\d|3[875]\\d|\n" +
            "2[98654321]\\d|9[8543210]|8[6421]|6[6543210]|5[87654321]|\n" +
            "4[987654310]|3[9643210]|2[70]|7|1)\\d{1,14}$";
    String GET_GROUPS_ID_REGEX = "^\\/groups\\/([a-zA-Z0-9-_]+)(\\/?).*";
    String GET_POSTS_ID_REGEX = ".*\\/posts\\/([a-zA-Z0-9-_]+)(\\/?).*";
    String GET_RESOURCES_ID_REGEX = ".*\\/resources\\/([a-zA-Z0-9-_]+)(\\/?).*";
    String GET_COMMENT_ID_REGEX = ".*\\/comment\\/([a-zA-Z0-9-_]+)(\\/?).*";
    String DATE_REGEX = "^(0?[1-9]|[12][0-9]|3[01])\\/(0?[1-9]|1[012])\\/\\d{4}$";

    // date format is yyyy-mm-dd
    String YYYYMMDD_HYPHEN_REGEX = "^(\\d{4})-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])$";

    // date format is yyyy-mm-dd hh:mm:ss (2022-10-27 09:00:00)
    String YYYYMMDD_HHMMSS_REGEX = "[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1]) (2[0-3]|[01][0-9]):[0-5][0-9]:[0-5][0-9]";

    String MATCH_A_SINGLE_CHARACTER = "[^a-zA-Z0-9]";

    String SPECIAL_CHARACTER_REGEX = "/*!@#$%^&*()\\\"{}_[]|\\\\?/<>,.";

    String FILE_NAME_IN_CONTENT_DISPOSITION_REGEX = "(?i)^.*filename=\"?([^\"]+)\"?.*$";

    String DOT_REGEX = "\\.";

}
