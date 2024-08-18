package com.flab.just_10_minutes.Util.Common;

import java.util.UUID;

public class IDUtil {

    public static String issueCustomUid() {
        return issueUUID().toString();
    }

    public static String issueOrderId() {
        return issueUUID().toString();
    }

    private static UUID issueUUID() {
        return UUID.randomUUID();
    }
}
