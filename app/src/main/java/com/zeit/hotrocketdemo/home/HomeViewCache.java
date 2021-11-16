package com.zeit.hotrocketdemo.home;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.zeit.hotrocket.logger.CrashHandlerLogger;
import com.zeit.hotrocket.logger.Logger;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HomeViewCache {

    public static volatile boolean hasInit;

    private static Map<String, View> mViewMap = new ConcurrentHashMap(10);

    public static void saveUI(String str, View view) {
        CrashHandlerLogger.mapPut(mViewMap, str, view);
    }

    public static View getView(Context context, String str, int i, int i2) {
        View view = (View) CrashHandlerLogger.mapGet(mViewMap, str);
        Logger.d("HomeViewCache", "getViewCache layout=" + str + " view=" + view);
        if (view != null) {
            mViewMap.remove(str);
            if (view.getLayoutParams() == null) {
                view.setLayoutParams(new ViewGroup.LayoutParams(i, i2));
            }
            try {
                Logger.d("HomeViewCache", "setContext start for layout:" + str);
                setViewContext(view, context);
                Logger.d("HomeViewCache", "setContext end for layout:" + str);
            } catch (Exception e) {
                Logger.d("HomeViewCache", "exception when setting context :");
                Logger.d("HomeViewCache", e);
                return null;
            }
        }
        return view;
    }

    public static void onCreate() {
        Logger.d("HomeViewCache", "onCreate");
        hasInit = true;
    }

    public static void onDestroy() {
        Logger.d("HomeViewCache", "onDestroy");
        hasInit = false;
        clearMap();
    }

    private static void clearMap() {
        mViewMap.clear();
    }


    private static void setContext(View view, Context context) throws NoSuchFieldException, IllegalAccessException, NullPointerException {
        Field declaredField = View.class.getDeclaredField("mContext");
        declaredField.setAccessible(true);
        declaredField.set(view, context);
    }

    public static void m44453c(int i, View view) {
        saveUI(String.valueOf(i), view);
    }

    public static View m44454d(Context context, int i, int i2, int i3) {
        return getView(context, String.valueOf(i), i2, i3);
    }

    private static void setViewContext(View view, Context context) throws NoSuchFieldException, IllegalAccessException, NullPointerException {
        setContext(view, context);
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int childCount = viewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                setViewContext(viewGroup.getChildAt(i), context);
            }
        }
    }
}
