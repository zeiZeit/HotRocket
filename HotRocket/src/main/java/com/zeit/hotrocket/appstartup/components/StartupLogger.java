package com.zeit.hotrocket.appstartup.components;

import android.util.Log;

import com.zeit.hotrocket.logger.CrashHandlerLogger;


public class StartupLogger {

    private static ILogger logger = new ILogger() {

        @Override
        public void d(String str, String str2) {
            Log.d(str, str2);
        }

        @Override
        public void i(String str, String str2) {
            Log.i(str, str2);
        }
    };

    public interface ILogger {
        void d(String str, String str2);

        void i(String str, String str2);
    }

    public static void d(String str, String str2, Object... objArr) {
        if (logger != null) {
            if (objArr != null && objArr.length > 0) {
                str2 = CrashHandlerLogger.m34780h(str2, objArr);
            }
            logger.d(str, str2);
        }
    }

    public static void i(String str, String str2, Object... objArr) {
        if (logger != null) {
            if (objArr != null && objArr.length > 0) {
                str2 = CrashHandlerLogger.m34780h(str2, objArr);
            }
            logger.i(str, str2);
        }
    }

    public static void setLogger(ILogger iLogger) {
        logger = iLogger;
    }
}
