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

    public static class RunnableCreateHomeUI implements Runnable {

        private Context mContext;

        public void run() {
            HomeUIPreInit.preInitHomeUI(this.mContext);
        }

        public RunnableCreateHomeUI(Context context) {
            this.mContext = context;
        }
    }

    public static void preInitHomeUI(Context context) {
        if (HomeViewCache.hasInit) {
            Logger.d("HomeUIPreInit", "home page already created, will not run HomeUIPreInit task");
            return;
        }
        createHomeUI(context, "home_layout_key");
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
        }
        return view;
    }

    @Override
    public void run(Context context) {
        ThreadPoolUtils.getInstance().submit(new RunnableCreateHomeUI(context));
    }
}
