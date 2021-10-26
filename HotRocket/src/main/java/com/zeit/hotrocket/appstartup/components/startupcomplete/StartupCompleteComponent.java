package com.zeit.hotrocket.appstartup.components.startupcomplete;

import android.app.Activity;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import com.zeit.hotrocket.appstartup.components.StartupLogger;
import com.zeit.hotrocket.logger.CrashHandlerLogger;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class StartupCompleteComponent {

    public static boolean hasDoFrame;

    public static boolean complete;

    private static List<Callback> callbackList;

    private static Handler mHandler;

    private static final Object mLock = new Object();

    static SimpleActivityLifecycleCallback simpleActivityLifecycleCallback;


    public interface Callback {
        void onComplete(boolean z);
    }

    public static void preload() {
        synchronized (mLock) {
            if (mHandler == null) {
                StartupLogger.i("StartupComponent.Complete", "StartupCompleteComponent预加载线程", new Object[0]);
                mHandler = getHandler();
            }
        }
    }

    static void init(long startupCompleteTimeoutMillis,String splashActivity) {
        final Handler handler;
        StartupLogger.i("StartupComponent.Complete", "进程启动，初始化StartupCompleteComponent", new Object[0]);
        synchronized (mLock) {
            if (mHandler == null) {
                StartupLogger.i("StartupComponent.Complete", "StartupCompleteComponent初始化线程", new Object[0]);
                mHandler = getHandler();
            }
            handler = mHandler;
        }
        if (!StartupComponentBase.isMainProcess()) {
            StartupLogger.i("StartupComponent.Complete", "非主进程启动，直接发送启动完成HomeReady通知...", new Object[0]);
            sendEmptyMessage(0, handler);
            return;
        }
        StartupLogger.i("StartupComponent.Complete", CrashHandlerLogger.m34780h("主进程启动，开始监听第一个页面创建，超时时间%sms...", Long.valueOf(startupCompleteTimeoutMillis)), new Object[0]);
        sendEmptyMessage(startupCompleteTimeoutMillis, handler);

        simpleActivityLifecycleCallback = new SimpleActivityLifecycleCallback() {
            @Override
            public void onActivityResumed(Activity activity) {
                super.onActivityResumed(activity);
                final String simpleName = activity.getClass().getSimpleName();
                if (StartupCompleteComponent.complete || StartupCompleteComponent.hasDoFrame) {
                    StartupLogger.i("StartupComponent.Complete", CrashHandlerLogger.m34780h("创建页面[%s]，启动完成HomeReady通知已经完成或正在排队中，忽略.", simpleName), new Object[0]);
                    if (!StartupCompleteComponent.hasDoFrame) {
                        StartupComponentBase.getApplication().unregisterActivityLifecycleCallbacks(this);
                    }
                } else if (CrashHandlerLogger.m34820R(splashActivity, activity.getClass().getName())) {
                    StartupLogger.i("StartupComponent.Complete", "创建闪屏页面[splash]，忽略.", new Object[0]);
                } else {
                    StartupLogger.i("StartupComponent.Complete", CrashHandlerLogger.m34780h("创建第一个非闪屏页面[%s]，等待页面绘制...", simpleName), new Object[0]);
                    activity.getWindow().getDecorView().post(new Runnable() {

                        public void run() {
                            if (StartupCompleteComponent.complete || StartupCompleteComponent.hasDoFrame) {
                                StartupLogger.i("StartupComponent.Complete", CrashHandlerLogger.m34780h("页面[%s]第一次DoFrame完成，启动完成HomeReady通知已经完成或正在排队中，忽略.", simpleName), new Object[0]);
                                if (!StartupCompleteComponent.hasDoFrame) {
                                    unregisterActivityLifecycleCallbacks();
                                    return;
                                }
                                return;
                            }
                            StartupLogger.i("StartupComponent.Complete", CrashHandlerLogger.m34780h("页面[%s]第一次DoFrame完成，解除监听后续页面，启动完成HomeReady通知排队中...", simpleName), new Object[0]);
                            unregisterActivityLifecycleCallbacks();
                            StartupCompleteComponent.hasDoFrame = true;
                            new Handler(Looper.getMainLooper()).post(new Runnable() {

                                public void run() {
                                    if (StartupCompleteComponent.complete) {
                                        StartupLogger.i("StartupComponent.Complete", CrashHandlerLogger.m34780h("页面[%s]绘制完成，启动完成HomeReady通知已经完成，忽略.", simpleName), new Object[0]);
                                        return;
                                    }
                                    StartupLogger.i("StartupComponent.Complete", CrashHandlerLogger.m34780h("页面[%s]绘制完成，开始发送启动完成HomeReady通知...", simpleName), new Object[0]);
                                    StartupCompleteComponent.sendEmptyMessage(0, handler);
                                }
                            });
                        }
                    });
                }
            }
        };
        StartupComponentBase.getApplication().registerActivityLifecycleCallbacks(simpleActivityLifecycleCallback);
    }


    private static void unregisterActivityLifecycleCallbacks(){
        if (StartupComponentBase.getApplication()!=null&&simpleActivityLifecycleCallback!=null){
            StartupComponentBase.getApplication().unregisterActivityLifecycleCallbacks(simpleActivityLifecycleCallback);
        }
    }

    public static void complete(boolean hasDelay, Handler handler) {
        String str;
        synchronized (mLock) {
            if (!complete) {
                complete = true;
                mHandler = null;
                handler.getLooper().quit();
                Object[] objArr = new Object[1];
                if (hasDelay) {
                    str = "(启动超时)";
                } else {
                    str = "";
                }
                objArr[0] = str;
                StartupLogger.i("StartupComponent.Complete", CrashHandlerLogger.m34780h("启动完成HomeReady通知完毕%s.", objArr), new Object[0]);
                List<Callback> list = callbackList;
                if (list != null && !list.isEmpty()) {
                    Iterator V = CrashHandlerLogger.m34824V(callbackList);
                    while (V.hasNext()) {
                        ((Callback) V.next()).onComplete(hasDelay);
                    }
                    callbackList.clear();
                }
                callbackList = null;
            }
        }
    }

    public static void sendEmptyMessage(long startupCompleteTimeoutMillis, Handler handler) {
        synchronized (mLock) {
            if (!complete) {
                if (startupCompleteTimeoutMillis > 0) {
                    handler.sendEmptyMessageDelayed(2, startupCompleteTimeoutMillis);
                } else {
                    handler.sendEmptyMessage(1);
                }
            }
        }
    }

    @Deprecated
    public static void addCallback(Callback callback) {
        synchronized (mLock) {
            if (complete) {
                StartupLogger.i("StartupComponent.Complete", "注册启动完成HomeReady监听[%s], 启动已经完成，直接回调", callback.getClass().getName());
                callback.onComplete(false);
            } else {
                StartupLogger.i("StartupComponent.Complete", "注册启动完成HomeReady监听[%s], 开始监听...", callback.getClass().getName());
                if (callbackList == null) {
                    callbackList = new LinkedList();
                }
                callbackList.add(callback);
            }
        }
    }

    private static Handler getHandler() {
        HandlerThread handlerThread = new HandlerThread("Startup#StartupComponent.Complete");
        handlerThread.start();
        return new Handler(handlerThread.getLooper()) {
            public void handleMessage(Message message) {
                boolean hasDelay                                                                           ;
                if (message.what == 2) {
                    hasDelay = true;
                } else {
                    hasDelay = false;
                }
                StartupCompleteComponent.complete(hasDelay, this);
            }
        };
    }
}
