package com.zeit.hotrocket.core.utils;

import java.util.ArrayList;
import java.util.List;

public class ThrowableUtil {
    public static String formatThrowable(Throwable th) {
        return String.format("ErrorMessage: %s\nStackTrace: %s", String.valueOf(th), getStackTrace(th));
    }


    public static List<String> getStackTrace(Throwable th) {
        StackTraceElement[] stackTrace;
        ArrayList arrayList = new ArrayList();
        if (!(th == null || (stackTrace = th.getStackTrace()) == null || stackTrace.length == 0)) {
            int length = stackTrace.length;
            for (StackTraceElement stackTraceElement : stackTrace) {
                arrayList.add(String.valueOf(stackTraceElement));
            }
        }
        return arrayList;
    }
}
