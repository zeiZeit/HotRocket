package com.zeit.hotrocket.appstartup.components.startupcomplete;

import android.app.Application;


public class StartupComponentBase {

    private static boolean f24307e;

    private static Application application;

    static boolean m32778c() {
        return f24307e;
    }

    static Application m32779d() {
        return application;
    }

    static void m32776a(boolean z) {
        f24307e = z;
    }

    static void m32777b(Application application) {
        StartupComponentBase.application = application;
    }
}
