package com.tj.sophie.job;

/**
 * Created by mbp on 6/5/15.
 */
public class Logger {
    private static boolean enable = true;

    public static void debug(String format, Object... args) {
        String message = String.format(format, args);
        output("DEBUG", message);
    }

    public static boolean isEnable() {
        return enable;
    }

    public static void setEnable(boolean enable) {
        Logger.enable = enable;
    }

    private static void output(String level, String message) {
        if (!enable) {
            return;
        }
        System.out.println(String.format("%s %s", level, message));
    }
}
