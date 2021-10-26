package com.zeit.hotrocketdemo.app;

import android.app.Application;

import com.zeit.hotrocket.logger.Logger;


public class SplashSpeedManager {

    public static Runnable getRunnable(final Application application) {
        return new Runnable() {
            public void run() {
                try {
                    Class.forName("com.zeit.hotrocketdemo.SplashActivity");
                    Class.forName("com.zeit.hotrocketdemo.HomeActivity");
                    Logger.d("ApplicationPreload", "9preload.HomeActivity");
                } catch (ClassNotFoundException unused) {
                }
//                MigrationTask.m35882a();
//                UTManager.m61309a();
//                UTKV.m61458h(application);
//                EventTrackerImpl.m8634c();
                Logger.d("ApplicationPreload", "10preload.EventTrackerImpl");
            }
        };
    }
}
