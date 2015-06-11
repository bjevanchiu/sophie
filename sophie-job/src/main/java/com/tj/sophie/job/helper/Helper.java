package com.tj.sophie.job.helper;

/**
 * Created by mbp on 6/10/15.
 */
public final class Helper {

    public static <T extends Object> boolean equals(T x, T y) {
        if (x == null || y == null) {
            return false;
        }
        return x == y;
    }

    public static boolean isNullOrEmpty(String x) {
        return x == null || x.trim().isEmpty();
    }

    public static boolean equalsIgnoreCase(String x, String y) {
        if (x == null || y == null) {
            return false;
        }
        return x.equalsIgnoreCase(y);
    }
}
