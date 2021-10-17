package com.zeit.hotrocket.logger;


import com.zeit.hotrocket.logger.impl.ILoggerImpl;

public class ILoggerInit {
    public static void init(Class<? extends ILoggerImpl> cls) {
        Logger.getLogger().setClazz(cls);
    }
}
