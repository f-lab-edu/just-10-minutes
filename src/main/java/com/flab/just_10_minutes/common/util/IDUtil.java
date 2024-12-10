package com.flab.just_10_minutes.common.util;

import java.util.UUID;

public class IDUtil {

    public static String issueCustomUid() {
        return issueUUID().toString();
    }

    public static String issueOrderId() {
        return issueUUID().toString();
    }

    public static String issueEventId() {
        return issueUUID().toString();
    }

    public static String issueNotificationId() {
        return issueUUID().toString();
    }

    public static UUID issueUUID() {
        return UUID.randomUUID();
    }
}
