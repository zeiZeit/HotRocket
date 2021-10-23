package com.zeit.hotrocketdemo.tasks;

import android.content.Context;
import android.view.View;

import com.zeit.hotrocket.appinit.annotations.InitTask;
import com.zeit.hotrocket.rocket.core.utils.ThreadPoolUtils;


public class HomeUIPreInit implements InitTask {

    public static class RunnableC7578a implements Runnable {

        private Context f33670a;

        public void run() {
            HomeUIPreInit.m44424a(this.f33670a);
        }

        public RunnableC7578a(Context context) {
            this.f33670a = context;
        }
    }

    public static void m44424a(Context context) {
//        if (HomeViewCache.f33681a) {
//            PLog.m14194i("HomeUIPreInit", "home page already created, will not run HomeUIPreInit task");
//            return;
//        }
//        HomePagerAdapter.m44577x(HomePagerAdapter.m44578y(context));
        m44425b(context, "home_layout_key");
        m44425b(context, "default_home_layout_key");
    }

    /* renamed from: b */
    private static void m44425b(Context context, String str) {
//        if (HomeViewCache.f33681a) {
//            PLog.m14194i("HomeUIPreInit", "home page already created, will not inflate " + str);
//            return;
//        }
//        try {
//            PLog.m14194i("HomeUIPreInit", "preload layout start for layout:" + str);
//            View c = m44426c(context, str);
//            if (c != null) {
//                HomeViewCache.m44455e(str, c);
//            }
//            PLog.m14194i("HomeUIPreInit", "preload layout end for layout:" + str);
//        } catch (Exception e) {
//            PLog.m14191e("HomeUIPreInit", e);
//        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x0027  */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0030  */
    /* renamed from: c */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static View m44426c(Context r3, String r4) {
        /*
            int r0 = com.xunmeng.pinduoduo.p614b.NullPointerCrashHandler.m34835i(r4)
            r1 = 1550717738(0x5c6e132a, float:2.68048462E17)
            r2 = 1
            if (r0 == r1) goto L_0x001a
            r1 = 1649974572(0x62589d2c, float:9.9895554E20)
            if (r0 == r1) goto L_0x0010
            goto L_0x0024
        L_0x0010:
            java.lang.String r0 = "default_home_layout_key"
            boolean r4 = com.xunmeng.pinduoduo.p614b.NullPointerCrashHandler.m34820R(r4, r0)
            if (r4 == 0) goto L_0x0024
            r4 = 1
            goto L_0x0025
        L_0x001a:
            java.lang.String r0 = "home_layout_key"
            boolean r4 = com.xunmeng.pinduoduo.p614b.NullPointerCrashHandler.m34820R(r4, r0)
            if (r4 == 0) goto L_0x0024
            r4 = 0
            goto L_0x0025
        L_0x0024:
            r4 = -1
        L_0x0025:
            if (r4 == 0) goto L_0x0030
            if (r4 == r2) goto L_0x002b
            r3 = 0
            goto L_0x0034
        L_0x002b:
            android.view.View r3 = com.xunmeng.pinduoduo.app_default_home.widget.DefaultHomeLayoutFactory.createDefaultHomeLayout(r3)
            goto L_0x0034
        L_0x0030:
            android.view.View r3 = com.xunmeng.pinduoduo.home.widget.HomeLayoutFactory.createHomeLayout(r3)
        L_0x0034:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xunmeng.pinduoduo.home.HomeUIPreInit.m44426c(android.content.Context, java.lang.String):android.view.View");
    }

    @Override // com.xunmeng.pinduoduo.appinit.annotations.InitTask
    public void run(Context context) {
//        if (DefaultHomeAbTestUtil.m30294y()) {
//            C10617ai.m60220m().mo49880x(ThreadBiz.Home, "XmlLayoutPreloadRunnable", new RunnableC7578a(context));
//        } else {
//            C6055f.m35977e().mo30359h(new RunnableC7578a(context));
//        }
        ThreadPoolUtils.getInstance().submit(new RunnableC7578a(context));
    }
}
