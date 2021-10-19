package com.zeit.hotrocket.appstartup.components;

import android.util.Log;

import com.zeit.hotrocket.logger.CrashHandlerLogger;


public class StartupLogger {

    private static AbstractC5360a f24290d = new AbstractC5360a() {

        @Override
        public void mo24755b(String str, String str2) {
            Log.d(str, str2);
        }

        @Override
        public void mo24756c(String str, String str2) {
            Log.i(str, str2);
        }
    };

    public interface AbstractC5360a {
        void mo24755b(String str, String str2);

        void mo24756c(String str, String str2);
    }

    public static void m32757b(String str, String str2, Object... objArr) {
        if (f24290d != null) {
            if (objArr != null && objArr.length > 0) {
                str2 = CrashHandlerLogger.m34780h(str2, objArr);
            }
            f24290d.mo24755b(str, str2);
        }
    }

    public static void m32758c(String str, String str2, Object... objArr) {
        if (f24290d != null) {
            if (objArr != null && objArr.length > 0) {
                str2 = CrashHandlerLogger.m34780h(str2, objArr);
            }
            f24290d.mo24756c(str, str2);
        }
    }

    public static void m32756a(AbstractC5360a aVar) {
        f24290d = aVar;
    }
}
