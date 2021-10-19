package com.zeit.hotrocket.appstartup.components.startupcomplete;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.zeit.hotrocket.appstartup.components.StartupLogger;

public class HomeRenderInternal {

    public static boolean f24291a;

    private static boolean f24292g;

    private static boolean f24293h;

    private static Application.ActivityLifecycleCallbacks f24294i;

    private static AbstractC5363a f24295j;

    private static boolean f24296k;

    public interface AbstractC5363a {
        void mo28060a();
    }

    static void m32763b(boolean z) {
        f24296k = z;
        if (!z) {
            StartupLogger.m32758c("StartupComponent.HomeRender", "HomeRenderInternal init disabled.", new Object[0]);
        } else if (Looper.myLooper() == Looper.getMainLooper()) {
            f24294i = new SimpleActivityLifecycleCallback() {

                @Override
                public void onActivityCreated(Activity activity, Bundle bundle) {
                    if (m34820R("com.xunmeng.pinduoduo.ui.activity.HomeActivity", activity.getClass().getName())) {
                        StartupLogger.m32758c("StartupComponent.HomeRender", "HomeRenderInternal监听到首页[HomeActivity]创建.", new Object[0]);
                        HomeRenderInternal.f24291a = true;
                    }
                }
            };
            StartupComponentBase.m32779d().registerActivityLifecycleCallbacks(f24294i);
            StartupLogger.m32758c("StartupComponent.HomeRender", "HomeRenderInternal init enabled, 开始监听首页创建...", new Object[0]);
        } else {
            throw new IllegalStateException("HomeRenderInternal init must run in main thread.");
        }
    }

    static void m32764c() {
        if (f24296k) {
            if (Looper.myLooper() != Looper.getMainLooper()) {
                throw new IllegalStateException("HomeRenderInternal clean must run in main thread.");
            } else if (!f24293h) {
                f24293h = true;
                if (f24294i != null) {
                    StartupComponentBase.m32779d().unregisterActivityLifecycleCallbacks(f24294i);
                    f24294i = null;
                }
                f24295j = null;
                StartupLogger.m32758c("StartupComponent.HomeRender", "HomeRenderInternal clean.", new Object[0]);
            }
        }
    }

    static void m32765d(AbstractC5363a aVar) {
        if (!f24296k) {
            aVar.mo28060a();
        } else if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new IllegalStateException("HomeRenderInternal observeIdleWrapHomeRender must run in main thread.");
        } else if (!f24293h) {
            if (!f24291a) {
                StartupLogger.m32758c("StartupComponent.HomeRender", "HomeRenderInternal没有首页，不监听直接回调.", new Object[0]);
                aVar.mo28060a();
            } else if (f24292g) {
                StartupLogger.m32758c("StartupComponent.HomeRender", "HomeRenderInternal监听到首页已经绘制，直接回调.", new Object[0]);
                aVar.mo28060a();
            } else {
                StartupLogger.m32758c("StartupComponent.HomeRender", "HomeRenderInternal首页创建但未绘制，开始监听绘制...", new Object[0]);
                f24295j = aVar;
            }
        }
    }

    public static void m32766e() {
        if (f24296k) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                m32767f();
            } else {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    public void run() {
                        HomeRenderInternal.m32767f();
                    }
                });
            }
        }
    }

    public static void m32767f() {
        if (!f24293h) {
            StartupLogger.m32758c("StartupComponent.HomeRender", "HomeRenderInternal收到外部通知首页绘制完成消息.", new Object[0]);
            f24292g = true;
            AbstractC5363a aVar = f24295j;
            if (aVar != null) {
                aVar.mo28060a();
            }
        }
    }

    public static boolean m34820R(String str, Object obj) {
        if (str == null) {
            return false;
        } else if (obj == null) {
            return false;
        } else {
            return str.equals(obj);
        }
    }


}
