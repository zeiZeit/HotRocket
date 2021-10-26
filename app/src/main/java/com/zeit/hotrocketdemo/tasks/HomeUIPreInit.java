package com.zeit.hotrocketdemo.tasks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.zeit.hotrocket.appinit.annotations.InitTask;
import com.zeit.hotrocket.logger.Logger;
import com.zeit.hotrocket.rocket.core.utils.ThreadPoolUtils;
import com.zeit.hotrocketdemo.R;
import com.zeit.hotrocketdemo.home.HomeViewCache;


public class HomeUIPreInit implements InitTask {

    public static class RunnableC7578a implements Runnable {

        private Context mContext;

        public void run() {
            HomeUIPreInit.preInitHomeUI(this.mContext);
        }

        public RunnableC7578a(Context context) {
            this.mContext = context;
        }
    }

    public static void preInitHomeUI(Context context) {
        if (HomeViewCache.hasInit) {
            Logger.d("HomeUIPreInit", "home page already created, will not run HomeUIPreInit task");
            return;
        }
//        HomePagerAdapter.m44577x(HomePagerAdapter.m44578y(context));
        createHomeUI(context, "home_layout_key");
//        createHomeUI(context, "default_home_layout_key");
    }


    private static void createHomeUI(Context context, String str) {
        if (HomeViewCache.hasInit) {
            Logger.d("HomeUIPreInit", "home page already created, will not inflate " + str);
            return;
        }
        try {
            Logger.d("HomeUIPreInit", "preload layout start for layout:" + str);
            View view = createView(context, str);
            if (view != null) {
                HomeViewCache.saveUI(str, view);
            }
            Logger.d("HomeUIPreInit", "preload layout end for layout:" + str);
        } catch (Exception e) {
            Logger.d("HomeUIPreInit", e);
        }
    }

    private static View createView(Context context, String key) {
        View view = null;
        if (key!=null&&key.equals("home_layout_key")){
            view = LayoutInflater.from(context)
                    .inflate(R.layout.activity_main, null, false);
            return view;
        }

        throw new UnsupportedOperationException("Method not decompiled: com.xunmeng.pinduoduo.home.HomeUIPreInit.m44426c(android.content.Context, java.lang.String):android.view.View");
    }

    @Override
    public void run(Context context) {
//        if (DefaultHomeAbTestUtil.m30294y()) {
//            C10617ai.m60220m().mo49880x(ThreadBiz.Home, "XmlLayoutPreloadRunnable", new RunnableC7578a(context));
//        } else {
//            C6055f.m35977e().mo30359h(new RunnableC7578a(context));
//        }
        ThreadPoolUtils.getInstance().submit(new RunnableC7578a(context));
    }
}
