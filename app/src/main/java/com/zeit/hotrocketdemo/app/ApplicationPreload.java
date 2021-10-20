package com.zeit.hotrocketdemo.app;

import android.app.Application;

import com.zeit.hotrocket.appstartup.components.startupcomplete.StartupCompleteComponent;
import com.zeit.hotrocket.logger.Logger;
import com.zeit.hotrocket.rocket.init.PddRocketInit;

public class ApplicationPreload {

    private static void m28738g(final Application application) {
        C10617ai.m60220m().mo49856D(ThreadBiz.Startup, "preloadStartup", new Runnable() {
            /* class com.xunmeng.pinduoduo.app.ApplicationPreload.RunnableC45703 */

            public void run() {
                PddRocketInit.preLoadPddRocket(application);
                StartupCompleteComponent.m32769c();
                Logger.i("ApplicationPreload", "preloadStartup");
            }
        });
    }



}
