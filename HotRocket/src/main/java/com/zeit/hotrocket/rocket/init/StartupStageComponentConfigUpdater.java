package com.zeit.hotrocket.rocket.init;


import com.zeit.hotrocket.appstartup.components.startupcomplete.StartupStageComponent;
import com.zeit.hotrocket.appstartup.components.startupcomplete.StartupStageListener;
import com.zeit.hotrocket.logger.Logger;
import com.zeit.hotrocket.rocket.core.utils.ThreadPoolUtils;

public class StartupStageComponentConfigUpdater {
    public static void init(String packageName) {
        StartupStageComponent.setListener(new StartupStageListener() {

            @Override
            public void onHomeReady(boolean isTimeout) {
                String str;
                Object[] objArr = new Object[2];
                if (isTimeout) {
                    str = "(超时)";
                } else {
                    str = "";
                }
                objArr[0] = str;
                objArr[1] = ProcessNameUtil.getCurrentProcessName();
                Logger.d("StartupComponent.Messenger", "启动进入页面可见阶段(首页或落地页首帧绘制完成)%s, 进程: %s", objArr);
                if (ProcessNameUtil.isMainProcess(packageName)) {
//                    MessageCenter.m35854b().mo30283h(new Message0("msg_titan_lazyload_main_process_startup_complete"));
                   Logger.d("StartupComponent.Messenger", "发送启动完成通知[%s]完毕.", "msg_titan_lazyload_main_process_startup_complete");
                }
            }

            @Override
            public void onIdle(boolean isTimeout) {
                String str;
                Object[] objArr = new Object[2];
                if (isTimeout) {
                    str = "(超时)";
                } else {
                    str = "";
                }
                objArr[0] = str;
                objArr[1] = ProcessNameUtil.getCurrentProcessName();
               Logger.d("StartupComponent.Messenger", "启动进入IDLE阶段(主线程短暂空闲)%s, 进程: %s", objArr);
               StartupStageComponentConfigUpdater.m28824b();
                
            }

            @Override
            public void onUserIdle(boolean isTimeout) {
                String str;
                Object[] objArr = new Object[2];
                if (isTimeout) {
                    str = "(超时)";
                } else {
                    str = "";
                }
                objArr[0] = str;
                objArr[1] = ProcessNameUtil.getCurrentProcessName();
               Logger.d("StartupComponent.Messenger", "启动进入USER_IDLE阶段(主线程完全空闲)%s, 进程: %s", objArr);
            }
        });
    }

    public static boolean isObserveHomeRender() {
//        return StartupAbHelper.m38617c("ab_app_startup_component_observe_home_render_5650", false, true);
        return true;
    }

    public static void m28824b() {
        ThreadPoolUtils.getInstance().execute(new Runnable() {
            public void run() {
                StartupStageComponentConfigUpdater.m28826d();
                StartupStageComponentConfigUpdater.m28828f();
                StartupStageComponentConfigUpdater.m28830h();
            }
        });
    }

    /*
    * App 启动超时时间
    * 后续改成可配置的
    *  */
    public static long getAppStartupCompleteTimeout() {
        return 5000;
    }

    public static void m28826d() {

        Logger.d("StartupComponent.Messenger", "启动完成或者接收到Config回调, coldStartUp.cold_startup_complete_component_delay_millis没有变化: ");
    }

    public static long getStartupIdleTimeoutMillis() {
        return 10000;
    }

    public static void m28828f() {
        Logger.d("StartupComponent.Messenger", "启动完成或者接收到Config回调, coldStartUp.cold_startup_idle_component_delay_millis没有变化: ");
    }

    public static long getStartupUserIdleTimeoutMillis() {
        return 30000;
    }

    public static void m28830h() {

        Logger.d("StartupComponent.Messenger", "启动完成或者接收到Config回调, coldStartUp.cold_startup_user_idle_component_delay_millis没有变化: ");
    }
}
