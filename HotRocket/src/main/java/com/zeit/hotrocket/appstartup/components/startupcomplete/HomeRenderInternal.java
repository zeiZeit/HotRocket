package com.zeit.hotrocket.appstartup.components.startupcomplete;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.zeit.hotrocket.appstartup.components.StartupLogger;

public class HomeRenderInternal {

    public static boolean homeActivityCreate;

    private static boolean f24292g;

    private static boolean f24293h;

    private static Application.ActivityLifecycleCallbacks activityLifecycleCallbacks;

    private static AbstractC5363a f24295j;

    private static boolean isObserveHomeRender;

    public interface AbstractC5363a {
        void mo28060a();
    }

    static void init(boolean isObserveHomeRender) {
        HomeRenderInternal.isObserveHomeRender = isObserveHomeRender;
        if (!isObserveHomeRender) {
            StartupLogger.i("StartupComponent.HomeRender", "HomeRenderInternal init disabled.", new Object[0]);
        } else if (Looper.myLooper() == Looper.getMainLooper()) {
            activityLifecycleCallbacks = new SimpleActivityLifecycleCallback() {

                @Override
                public void onActivityCreated(Activity activity, Bundle bundle) {
                    if (safeEquals("com.xunmeng.pinduoduo.ui.activity.HomeActivity", activity.getClass().getName())) {
                        StartupLogger.i("StartupComponent.HomeRender", "HomeRenderInternal监听到首页[HomeActivity]创建.", new Object[0]);
                        HomeRenderInternal.homeActivityCreate = true;
                    }
                }
            };
            StartupComponentBase.getApplication().registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
            StartupLogger.i("StartupComponent.HomeRender", "HomeRenderInternal init enabled, 开始监听首页创建...", new Object[0]);
        } else {
            throw new IllegalStateException("HomeRenderInternal init must run in main thread.");
        }
    }

    static void m32764c() {
        if (isObserveHomeRender) {
            if (Looper.myLooper() != Looper.getMainLooper()) {
                throw new IllegalStateException("HomeRenderInternal clean must run in main thread.");
            } else if (!f24293h) {
                f24293h = true;
                if (activityLifecycleCallbacks != null) {
                    StartupComponentBase.getApplication().unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks);
                    activityLifecycleCallbacks = null;
                }
                f24295j = null;
                StartupLogger.i("StartupComponent.HomeRender", "HomeRenderInternal clean.", new Object[0]);
            }
        }
    }

    static void m32765d(AbstractC5363a aVar) {
        if (!isObserveHomeRender) {
            aVar.mo28060a();
        } else if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new IllegalStateException("HomeRenderInternal observeIdleWrapHomeRender must run in main thread.");
        } else if (!f24293h) {
            if (!homeActivityCreate) {
                StartupLogger.i("StartupComponent.HomeRender", "HomeRenderInternal没有首页，不监听直接回调.", new Object[0]);
                aVar.mo28060a();
            } else if (f24292g) {
                StartupLogger.i("StartupComponent.HomeRender", "HomeRenderInternal监听到首页已经绘制，直接回调.", new Object[0]);
                aVar.mo28060a();
            } else {
                StartupLogger.i("StartupComponent.HomeRender", "HomeRenderInternal首页创建但未绘制，开始监听绘制...", new Object[0]);
                f24295j = aVar;
            }
        }
    }

    public static void m32766e() {
        if (isObserveHomeRender) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                homeRenderComplete();
            } else {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    public void run() {
                        HomeRenderInternal.homeRenderComplete();
                    }
                });
            }
        }
    }

    public static void homeRenderComplete() {
        if (!f24293h) {
            StartupLogger.i("StartupComponent.HomeRender", "HomeRenderInternal收到外部通知首页绘制完成消息.", new Object[0]);
            f24292g = true;
            AbstractC5363a aVar = f24295j;
            if (aVar != null) {
                aVar.mo28060a();
            }
        }
    }

    public static boolean safeEquals(String str, Object obj) {
        if (str == null) {
            return false;
        } else if (obj == null) {
            return false;
        } else {
            return str.equals(obj);
        }
    }


}
