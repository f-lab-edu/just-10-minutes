package com.flab.just_10_minutes.util.common;

import java.util.concurrent.TimeUnit;

public class TimeUtil {

    public static long convertMinutesToMilliSeconds(final Integer minutes) {
        return TimeUnit.MILLISECONDS.convert(minutes, TimeUnit.MINUTES);
    }

    public static long fetchCurrentTimeMillis() {
        return System.currentTimeMillis();
    }
}
