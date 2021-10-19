package com.zeit.hotrocket.appstartup.components.startupcomplete;

import android.app.Application;


public class StartupComponentBase {

    private static boolean isMainProcess;

    private static Application application;

    static boolean isMainProcess() {
        return isMainProcess;
    }

    static Application getApplication() {
        return application;
    }

    static void setIsMainProcess(boolean z) {
        isMainProcess = z;
    }

    static void setApplication(Application application) {
        StartupComponentBase.application = application;
    }
}
