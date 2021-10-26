package com.zeit.hotrocket.appstartup.components.startupcomplete;

import com.zeit.hotrocket.appstartup.components.StartupLogger;

public class StartupStageComponent {

    public static StartupStage stage = StartupStage.DEFAULT;

    public static void setListener(final StartupStageListener listener) {
        StartupLogger.i("StartupComponent.Stage", "注册监听启动STAGE状态[%s]", listener.getClass().getName());
        StartupIdleComponent.setObserver(new StartupIdleComponent.Observer() {

            @Override
            public void onHomeReady(boolean z) {
                listener.onHomeReady(z);
            }

            @Override
            public void onIdle(boolean z) {
                listener.onIdle(z);
            }

            @Override
            public void onUserIdle(boolean z) {
                listener.onUserIdle(z);
            }
        });
    }

    //application 里边调用
    public static void init(StartupStageComponentConfig config) {
        StartupLogger.i("StartupComponent.Stage", "进程启动，初始化StartupStageComponent: " + config, new Object[0]);
        StartupComponentBase.setApplication(config.application);
        StartupComponentBase.setIsMainProcess(config.isMainProcess);
        StartupIdleComponent.init(config.startupIdleTimeoutMillis, config.startupUserIdleTimeoutMillis, config.observeHomeRender,config.HomeActivityName);
        StartupCompleteComponent.init(config.startupCompleteTimeoutMillis,config.SplashActivityName);
    }
}
