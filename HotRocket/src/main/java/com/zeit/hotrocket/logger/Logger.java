package com.zeit.hotrocket.logger;

import android.util.Log;

import com.zeit.hotrocket.logger.impl.AndroidLogImpl;
import com.zeit.hotrocket.logger.impl.ILoggerImpl;

public class Logger {

    private static volatile Logger mLogger;

    private Class<? extends ILoggerImpl> mClazz;

    private ILoggerImpl mLoggerImpl;

    static Logger getLogger() {
        if (mLogger == null) {
            mLogger = new Logger();
        }
        return mLogger;
    }

    private static ILoggerImpl buildLoggerImpl() {
        Class<? extends ILoggerImpl> cls = getLogger().mClazz;
        if (cls == null) {
            return null;
        }
        try {
            return (ILoggerImpl) cls.newInstance();
        } catch (Exception e) {
            Log.e("Hot.Logger", "", e);
            return null;
        }
    }

    public static void v(String str, String str2) {
        getLoggerImpl().getLogger().v(str, str2);
    }

    public static void v(String str, String str2, Object... objArr) {
        getLoggerImpl().getLogger().v(str, str2, objArr);
    }

    public static void v(String str, Throwable th) {
        getLoggerImpl().getLogger().v(str, th);
    }

    public static void v(String str, String str2, Throwable th) {
        getLoggerImpl().getLogger().v(str, str2, th);
    }

    public static void d(String str, String str2) {
        getLoggerImpl().getLogger().d(str, str2);
    }

    public static void d(String str, String str2, Object... objArr) {
        getLoggerImpl().getLogger().d(str, str2, objArr);
    }

    public static void d(String str, Throwable th) {
        getLoggerImpl().getLogger().d(str, th);
    }

    public static void d(String str, String str2, Throwable th) {
        getLoggerImpl().getLogger().v(str, str2, th);
    }

    public static void i(String str, String str2) {
        getLoggerImpl().getLogger().i(str, str2);
    }

    public static void i(String str, String str2, Object... objArr) {
        getLoggerImpl().getLogger().i(str, str2, objArr);
    }

    public static void i(String str, Throwable th) {
        getLoggerImpl().getLogger().i(str, th);
    }

    public static void i(String str, String str2, Throwable th) {
        getLoggerImpl().getLogger().i(str, str2, th);
    }

    public static void w(String str, String str2) {
        getLoggerImpl().getLogger().w(str, str2);
    }

    public static void w(String str, String str2, Object... objArr) {
        getLoggerImpl().getLogger().w(str, str2, objArr);
    }

    public static void w(String str, Throwable th) {
        getLoggerImpl().getLogger().w(str, th);
    }

    public static void w(String str, String str2, Throwable th) {
        getLoggerImpl().getLogger().w(str, str2, th);
    }

    public static void e(String str, String str2) {
        getLoggerImpl().getLogger().e(str, str2);
    }

    public static void e(String str, String str2, Object... objArr) {
        getLoggerImpl().getLogger().e(str, str2, objArr);
    }

    public static void e(String str, Throwable th) {
        getLoggerImpl().getLogger().e(str, th);
    }

    public static void e(String str, String str2, Throwable th) {
        getLoggerImpl().getLogger().e(str, str2, th);
    }


    private static ILoggerImpl getLoggerImpl() {
        ILoggerImpl logger = getLogger().mLoggerImpl;
        if (logger == null) {
            logger = buildLoggerImpl();
            getLogger().mLoggerImpl = logger;
        }
        if (logger == null) {
            return new AndroidLogImpl();
        }
        return logger;
    }

    private Logger() {
    }


    public void setClazz(Class<? extends ILoggerImpl> cls) {
        this.mClazz = cls;
    }
}
