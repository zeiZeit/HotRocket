package com.zeit.hotrocket.rocket.init;

import android.app.Application;
import android.os.SystemClock;
import android.text.TextUtils;

import com.zeit.hotrocket.appstartup.components.startupcomplete.StartupStageComponent;
import com.zeit.hotrocket.appstartup.components.startupcomplete.StartupStageListener;
import com.zeit.hotrocket.logger.Logger;
import com.zeit.hotrocket.rocket.HotRocket;
import com.zeit.hotrocket.rocket.HotRocketConfig;
import com.zeit.hotrocket.rocket.HotRocketListener;
import com.zeit.hotrocket.rocket.HotRocketTaskListener;

import java.io.File;

public class PddRocketInit {

    public static class Config {

        private static volatile HotRocketConfig mHotRocketConfig;

        private static final Object mLock = new Object();

        /* 1 - 5*/
        public static int getSize(int i) {
            return Math.max(1, Math.min(5, i));
        }

        private static int getConfigThreadPoolSize(Application application) {
            try {
                File file = new File(application.getFilesDir(), "pdd_rocket");
                if (!file.exists() || !file.isDirectory()) {
                    String str = HotRocket.TAG;
                    Logger.d(str, "[PddRocketInit][Config][Rocket][ThreadPoolSize] Can not find dir " + file.getAbsolutePath() + ", return default: " + 2);
                    return 2;
                }
                File file2 = new File(file, "thread_pool_size_config");
                if (!file2.exists() || !file2.isFile()) {
                    String str2 = HotRocket.TAG;
                    Logger.d(str2, "[PddRocketInit][Config][Rocket][ThreadPoolSize] Can not find file " + file2.getAbsolutePath() + ", return default: " + 2);
                    return 2;
                }
                int length = (int) file2.length();
                int a = getSize(length);
                String str3 = HotRocket.TAG;
                Logger.d(str3, "[PddRocketInit][Config][Rocket][ThreadPoolSize] Read file len: " + length + ", return: " + a);
                return a;
            } catch (Throwable th) {
                Logger.d(HotRocket.TAG, th.getLocalizedMessage(), th);
                return 2;
            }
        }

        public static void updatingThreadPoolSizeAfterStartup(final Application application) {
            Logger.d(HotRocket.TAG, "[PddRocketInit][Config][Rocket][ThreadPoolSize] Updating thread pool size on startup finished...");
            updateThreadPoolSizeConfig(application);
        }

        public static HotRocketConfig buildRocketConfig(Application application) {
            HotRocketConfig hotRocketConfig;
            synchronized (mLock) {
                if (mHotRocketConfig == null) {
                    long elapsedRealtime = SystemClock.elapsedRealtime();
                    HotRocketConfig.Config config = HotRocketConfig.Config.getConfig();
                    //以下可配置
                    config.setCheckMainThreadBusy(true); //检测主线程繁忙
                    config.setBusyThreshold(50);         //判断主线程繁忙的阈值 默认50ms未响应即为繁忙
                    config.setThreadPoolSize(3); //getConfigThreadPoolSize(application)  几个任务分发器，默认2个
                    config.setRunInProcessName(RuntimeInfo.f5705c); //传入当前进程全名
                    mHotRocketConfig = config.build();
                    String str = HotRocket.TAG;
                    Logger.d(str, "[PddRocketInit][Config][Rocket] initRocketConfig cost " + (SystemClock.elapsedRealtime() - elapsedRealtime) + "ms, threadPoolSize: " + mHotRocketConfig.thread_pool_size + ", isPauseIfMainThreadBusy: " + mHotRocketConfig.rocket_main_thread_check + ", mainThreadBusyThresholdMillis: " + mHotRocketConfig.rocket_busy_threshold);
                }
                hotRocketConfig = mHotRocketConfig;
            }
            return hotRocketConfig;
        }

        public static void updateThreadPoolSizeConfig(final Application application) {
        }
    }

    static void launch(final Application application) {
        HotRocket.setHotRocketTaskListener(new HotRocketTaskListener() {

            @Override
            public void onTaskStart(String str) {
                Logger.d(HotRocket.TAG, "Rocket任务 [%s] 开始...", str);
            }

            @Override
            public void onTaskComplete(String str) {
                Logger.d(HotRocket.TAG, "Rocket任务 [%s] 完成.", str);
            }
        });
        HotRocket.setHotRocketListener(new HotRocketListener() {

            @Override
            public void onHotRocketInit() {

            }

            @Override
            public void onHotRocketStart() {

            }

            @Override
            public void onHotRocketFinish() {

            }
        });
        //开始执行
        HotRocket.launch(PddRocketInit.Config.buildRocketConfig(application));
        StartupStageComponent.m32794c(new StartupStageListener() {

            @Override
            public void mo22063c(boolean z) {
                HotRocket.onHomeReady();
            }

            @Override
            public void mo22064d(boolean z) {
                HotRocket.onHomeIdle();
            }

            @Override
            public void mo22065e(boolean z) {
                HotRocket.onUserIdle();
            }
        });
    }


    static void preLoadPddRocket(Application application) {
        ContextHolder.application = application;
        HotRocket.preload(PddRocketInit.Config.buildRocketConfig(application));
    }
}
