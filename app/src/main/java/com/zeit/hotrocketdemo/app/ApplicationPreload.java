package com.zeit.hotrocketdemo.app;

import android.app.Application;

import com.zeit.hotrocket.appstartup.components.startupcomplete.StartupCompleteComponent;
import com.zeit.hotrocket.logger.Logger;
import com.zeit.hotrocket.rocket.core.utils.ThreadPoolUtils;
import com.zeit.hotrocket.rocket.init.HotRocketInit;



public class ApplicationPreload {

    public static void preloadRocket(final Application application) {
        ThreadPoolUtils.getInstance().execute(new Runnable() {

            public void run() {
                HotRocketInit.preLoadHotRocket(application);
                StartupCompleteComponent.m32769c();
                Logger.i("ApplicationPreload", "preloadStartup");
            }
        });
//        C10617ai.m60220m().mo49856D(ThreadBiz.Startup, "preloadStartup", );
    }




}
