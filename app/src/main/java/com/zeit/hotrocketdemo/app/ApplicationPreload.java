package com.zeit.hotrocketdemo.app;

import android.app.Application;

import com.zeit.hotrocket.appstartup.components.startupcomplete.StartupCompleteComponent;
import com.zeit.hotrocket.logger.Logger;
import com.zeit.hotrocket.rocket.HotRocketTask;
import com.zeit.hotrocket.rocket.core.utils.ThreadPoolUtils;
import com.zeit.hotrocket.rocket.init.HotRocketInit;
import com.zeit.hotrocketdemo.tasks.HomeUIPreInit;
import com.zeit.hotrocketdemo.tasks.HotRocketTaskFactory;
import java.util.ArrayList;


public class ApplicationPreload {

    public static void preloadRocket(final Application application) {
        ArrayList<HotRocketTask> taskArrayList = HotRocketTaskFactory.createHotRocketTasks(application);
        HotRocketInit.preLoadHotRocket(application,taskArrayList);
        StartupCompleteComponent.preload();
        Logger.i("ApplicationPreload", "preloadStartup");
    }

    public static void preloadSplash(final Application application){
        ThreadPoolUtils.getInstance().execute(SplashSpeedManager.getRunnable(application));
    }

    public static void preloadApp(final Application application){
        preloadSplash(application);
        preloadRocket(application);
    }

}
