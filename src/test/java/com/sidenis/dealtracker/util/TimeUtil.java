package com.sidenis.dealtracker.util;

import java.sql.Date;
import java.sql.Timestamp;

public final class TimeUtil {
    private TimeUtil() {
    }

    public static Timestamp now() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static Timestamp toTimestamp(String s) {
        return new Timestamp(Date.valueOf(s).getTime());
    }

}
