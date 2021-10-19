package com.zeit.hotrocket.appstartup.components.startupcomplete;

import com.zeit.hotrocket.appstartup.components.StartupLogger;

public class StartupStageComponent {

    public static StartupStage f24319a = StartupStage.DEFAULT;

    public static void m32794c(final StartupStageListener hVar) {
        StartupLogger.m32758c("StartupComponent.Stage", "注册监听启动STAGE状态[%s]", hVar.getClass().getName());
        StartupIdleComponent.m32782c(new StartupIdleComponent.AbstractC5375a() {

            @Override
            public void mo28075a(boolean z) {
                hVar.mo22063c(z);
            }

            @Override
            public void mo28076b(boolean z) {
                hVar.mo22064d(z);
            }

            @Override
            public void mo28077c(boolean z) {
                hVar.mo22065e(z);
            }
        });
    }

    public static void m32793b(StartupStageComponentConfig gVar) {
        StartupLogger.m32758c("StartupComponent.Stage", "进程启动，初始化StartupStageComponent: " + gVar, new Object[0]);
        StartupComponentBase.m32777b(gVar.f24321a);
        StartupComponentBase.m32776a(gVar.f24322b);
        StartupIdleComponent.m32780a(gVar.f24324d, gVar.f24325e, gVar.f24326f);
        StartupCompleteComponent.m32770d(gVar.f24323c);
    }
}
