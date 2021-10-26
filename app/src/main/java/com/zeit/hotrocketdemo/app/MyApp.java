package com.zeit.hotrocketdemo.app;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.zeit.hotrocket.appstartup.components.StartupLogger;
import com.zeit.hotrocket.appstartup.components.startupcomplete.StartupStageComponent;
import com.zeit.hotrocket.appstartup.components.startupcomplete.StartupStageComponentConfig;
import com.zeit.hotrocket.logger.Logger;
import com.zeit.hotrocket.rocket.HotRocket;
import com.zeit.hotrocket.rocket.init.HotRocketInit;
import com.zeit.hotrocket.rocket.init.ProcessNameUtil;
import com.zeit.hotrocket.rocket.init.StartupStageComponentConfigUpdater;

public class MyApp extends Application {

    private String packageName;
    private String processName;
    private boolean isMainProcess;
    private Application application;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Logger.d(HotRocket.TAG,"MyApp attachBaseContext");
        this.processName = ProcessNameUtil.getCurrentProcessName();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.d(HotRocket.TAG,"MyApp onCreate");
        initProcessStartTime();
        if (isMainProcess){
            ApplicationPreload.preloadApp(this.application);
            HotRocketInit.launch(this);
            initStartupComponent(this);
        }
    }

    private void initProcessStartTime() {
        this.application = this;
        String packageName2 = getApplication().getPackageName();
        this.packageName = packageName2;
        if (TextUtils.equals(this.processName, packageName2)) {
            this.isMainProcess = true;
        }
    }

    public Application getApplication() {
        return this.application;
    }


    private void initStartupComponent(Application application) {
        StartupLogger.setLogger(new StartupLogger.ILogger() {

            @Override
            public void d(String str, String str2) {
                Logger.d(str, str2);
            }

            @Override
            public void i(String str, String str2) {
                Logger.i(str, str2);
            }
        });
        StartupStageComponentConfigUpdater.init(this.packageName);
        StartupStageComponent.init(StartupStageComponentConfig.config.get()
                .setAppliaction(getApplication())
                .setIsMainProcess(this.isMainProcess)
                .setStartupCompleteTimeoutMillis(StartupStageComponentConfigUpdater.getAppStartupCompleteTimeout())
                .setStartupIdleTimeoutMillis(StartupStageComponentConfigUpdater.getStartupIdleTimeoutMillis())
                .setStartupUserIdleTimeoutMillis(StartupStageComponentConfigUpdater.getStartupUserIdleTimeoutMillis())
                .setObserveHomeRender(StartupStageComponentConfigUpdater.isObserveHomeRender())
                .setHomeActivityName("com.zeit.hotrocketdemo.MainActivity")
                .build());
    }



}
