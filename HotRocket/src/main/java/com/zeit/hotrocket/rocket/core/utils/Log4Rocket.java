package com.zeit.hotrocket.rocket.core.utils;

public class Log4Rocket {

    private Logger mLogger;

    private String TAG;
    
    public interface Logger {
        void log(String str);
    }

    public void log(String str) {
        Logger logger = this.mLogger;
        if (logger != null) {
            logger.log(this.TAG.concat(str));
        }
    }

    public void log(String str, Object obj) {
        Logger logger = this.mLogger;
        if (logger != null) {
            logger.log(this.TAG.concat(String.format(str, toString(obj))));
        }
    }

    public void log(String str, Object obj, Object obj2) {
        Logger logger = this.mLogger;
        if (logger != null) {
            logger.log(this.TAG.concat(String.format(str, toString(obj), toString(obj2))));
        }
    }


    public void log(String str, Object obj, Object obj2, Object obj3) {
        Logger logger = this.mLogger;
        if (logger != null) {
            logger.log(this.TAG.concat(String.format(str, toString(obj), toString(obj2), toString(obj3))));
        }
    }

    public Log4Rocket(String str, Logger logger) {
        this.mLogger = logger;
        this.TAG = str;
    }

    private static String toString(Object obj) {
        return String.valueOf(obj);
    }
}
