package ndt.project.common.utils;


import ndt.project.common.constants.CommonConstant;
import ndt.project.common.constants.OTPConstant;

public class RedisKeyUtil {
    public static String redisConfirmPasswordKey(String prefixRedisKey, Long userId) {
        return prefixRedisKey.concat(CommonConstant.REDIS_CONFIRM_PASSWORD_KEY)
                .concat(CommonConstant.COLON).concat(String.valueOf(userId));
    }

    public static String redisConfirmMailOrPhoneKey(String prefixRedisKey, String emailOrPhone) {
        return redisConfirmMailOrPhoneKey(prefixRedisKey, emailOrPhone
        );
    }

    public static String redisConfirmMailOrPhoneKey(String prefixRedisKey, String emailOrPhone, String subSystem) {
        return prefixRedisKey.concat(OTPConstant.REDIS_CONFIRM_MAIL_OR_PHONE_KEY).concat(subSystem)
                .concat(CommonConstant.COLON).concat(emailOrPhone);
    }

    public static String getUserExistKey(String prefixRedisKey, Long userId) {
        return prefixRedisKey.concat(CommonConstant.CQ_USER_EXIST_KEY)
                .concat(CommonConstant.COLON).concat(String.valueOf(userId));
    }

    public static String getUserSettingKey(String prefixRedisKey, Long userId) {
        return prefixRedisKey.concat(CommonConstant.CQ_USER_SETTING_KEY).concat(String.valueOf(userId));
    }

    public static String getUserSettingNotificationKey(String prefixRedisKey, Long userId) {
        return prefixRedisKey.concat(CommonConstant.CQ_USER_SETTING_NOTIFICATION_KEY).concat(String.valueOf(userId));
    }

    public static String getFcmTokenKey(String prefixRedisKey, Long userId, String deviceId) {
        return prefixRedisKey.concat(CommonConstant.FCM_TOKEN_KEY).concat(String.valueOf(userId))
                .concat(CommonConstant.COLON).concat(deviceId);
    }

    public static String getPoem3TokenKey(String prefixRedisKey, String userId, String p3Token) {
        return prefixRedisKey.concat(CommonConstant.POEM3_TOKEN_KEY).concat(userId).concat(p3Token);
    }

    public static String getPoem3TokenPushNotiKey(String prefixRedisKey, Long userId, String deviceId) {
        return prefixRedisKey.concat(CommonConstant.POEM3_TOKEN_PUSH_NOTI_KEY)
                .concat(CommonConstant.COLON).concat(String.valueOf(userId))
                .concat(CommonConstant.COLON).concat(deviceId);
    }

    public static String getAccessTokenKey(String prefixRedisKey, Long userId, String deviceId) {
        return prefixRedisKey.concat(CommonConstant.ACCESS_TOKEN_KEY)
                .concat(CommonConstant.COLON).concat(String.valueOf(userId)).concat(CommonConstant.COLON).concat(deviceId);
    }

    public static String getAccessTokenKeyListByUserId(String prefixRedisKey, String userId) {
        return prefixRedisKey.concat(CommonConstant.ACCESS_TOKEN_KEY)
                .concat(CommonConstant.COLON).concat(userId).concat(CommonConstant.ASTERISK);
    }

    public static String getOtpKey(String prefixRedisKey, String emailOrPhone) {
        return prefixRedisKey.concat(OTPConstant.REDIS_OTP_KEY).concat(CommonConstant.COLON).concat(emailOrPhone);
    }

    public static String getRefreshTokenKey(String prefixRedisKey, Long userId, String deviceId) {
        return prefixRedisKey.concat(CommonConstant.REFRESH_TOKEN_KEY)
                .concat(CommonConstant.COLON).concat(String.valueOf(userId)).concat(CommonConstant.COLON).concat(deviceId);
    }

    public static String getRefreshTokenKeyListByUserId(String prefixRedisKey, String userId) {
        return prefixRedisKey.concat(CommonConstant.REFRESH_TOKEN_KEY)
                .concat(CommonConstant.COLON).concat(userId).concat(CommonConstant.ASTERISK);
    }

    public static String getSPhotonChatTokenKey(String prefixRedisKey, Long userId, String deviceId) {
        return prefixRedisKey.concat(CommonConstant.SPHOTON_CHAT_TOKEN_KEY)
                .concat(CommonConstant.COLON).concat(String.valueOf(userId)).concat(CommonConstant.COLON).concat(deviceId);
    }

    public static String getTimesPasswordFailKey(String prefixRedisKey, String userId) {
        return prefixRedisKey.concat(CommonConstant.CQ_CHECK_PASSWORD_FAIL)
                .concat(CommonConstant.COLON).concat(userId);
    }

    public static String getSpammingRequestKey(String prefixRedisKey, String redisKey, String uri, String method) {
        return prefixRedisKey.concat(CommonConstant.CQ_SPAM_REQUEST).concat(redisKey)
                .concat(CommonConstant.COLON).concat(uri).concat(CommonConstant.COLON).concat(method);
    }

    public static String getCheckSpamNotificationKey(String prefixRedisKey, Long userId, Long userIdReceive,
                                                     String notiTemp, String objectId) {
        return prefixRedisKey.concat(CommonConstant.CQ_CHECK_SPAM_NOTIFICATION).concat(String.valueOf(userId))
                .concat(CommonConstant.COLON).concat(String.valueOf(userIdReceive))
                .concat(CommonConstant.COLON).concat(notiTemp)
                .concat(CommonConstant.COLON).concat(objectId);
    }

    public static String getLastActiveAtOfUserKey(String prefixRedisKey, String userId, String subSystem) {
        return prefixRedisKey.concat(CommonConstant.USER_LAST_ACTIVE_AT_KEY).concat(subSystem)
                .concat(CommonConstant.COLON).concat(userId);
    }

    public static String redisConfirmQRCodeKey(String prefixRedisKey, String fcmToken, String deviceId) {
        return prefixRedisKey.concat(CommonConstant.REDIS_CONFIRM_QR_CODE_KEY)
                .concat(CommonConstant.COLON).concat(deviceId).concat(CommonConstant.COLON).concat(fcmToken);
    }

    public static String getUserStatusKey(String prefixRedisKey, Long userId) {
        return prefixRedisKey.concat(CommonConstant.CQ_USER_STATUS_KEY)
                .concat(CommonConstant.COLON).concat(String.valueOf(userId));
    }

    public static String redisPointOneDayOfUser(String prefixRedisKey, Long userId) {
        return prefixRedisKey.concat(CommonConstant.REDIS_ADD_POINT)
                .concat(CommonConstant.COLON).concat(String.valueOf(userId));
    }

    public static String getAdminLmsTokenKey(String prefixRedisKey) {
        return prefixRedisKey.concat(CommonConstant.LEARNING_ADMIN_TOKEN_KEY);
    }

    public static String getUserLmsTokenKey(String prefixRedisKey, Long userId) {
        return prefixRedisKey.concat(CommonConstant.LEARNING_USER_TOKEN_KEY)
                .concat(CommonConstant.COLON).concat(String.valueOf(userId));
    }

    public static String getRecentCommentsKey(String prefixRedisKey, Long userId, String commentId) {
        return prefixRedisKey.concat(CommonConstant.RECENT_COMMENTS_KEY)
                .concat(CommonConstant.COLON).concat(String.valueOf(userId)).concat(CommonConstant.COLON).concat(commentId);
    }

    public static String getRecentMessagesKey(String prefixRedisKey, Long userId, String postId) {
        return prefixRedisKey.concat(CommonConstant.RECENT_MESSAGES_KEY)
                .concat(CommonConstant.COLON).concat(String.valueOf(userId))
                .concat(CommonConstant.COLON).concat(postId);
    }

    public static String redisCreateGroups(String prefixRedisKey, Long userId, String groupId) {
        return prefixRedisKey.concat(CommonConstant.RECENT_GROUPS_KEY)
                .concat(CommonConstant.COLON).concat(String.valueOf(userId))
                .concat(CommonConstant.COLON).concat(groupId);
    }

    public static String getRecentPostKey(String prefixRedisKey, Long userId, String postId) {
        return prefixRedisKey.concat(CommonConstant.RECENT_POSTS_KEY)
                        .
                concat(CommonConstant.COLON).concat(String.valueOf(userId)).concat(CommonConstant.COLON).concat(postId);
    }

    public static String getUserSubSystemsKey(String prefixRedisKey, Long userId) {
        return prefixRedisKey.concat(CommonConstant.CQ_USER_SUBSYSTEM_KEY)
                .concat(CommonConstant.COLON).concat(String.valueOf(userId));
    }
}
