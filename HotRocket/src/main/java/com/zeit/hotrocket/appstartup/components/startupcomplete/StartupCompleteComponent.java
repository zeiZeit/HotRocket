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

    public static boolean f24297a;

    public static boolean f24298b;

    private static List<AbstractC5368a> f24299h;

    private static Handler mHandler;

    private static final Object mLock = new Object();

    static SimpleActivityLifecycleCallback simpleActivityLifecycleCallback;


    public interface AbstractC5368a {
        void mo28070a(boolean z);
    }

    public static void preload() {
        synchronized (mLock) {
            if (mHandler == null) {
                StartupLogger.i("StartupComponent.Complete", "StartupCompleteComponent预加载线程", new Object[0]);
                mHandler = m32774k();
            }
        }
    }

    static void m32770d(long j) {
        final Handler handler;
        StartupLogger.i("StartupComponent.Complete", "进程启动，初始化StartupCompleteComponent", new Object[0]);
        synchronized (mLock) {
            if (mHandler == null) {
                StartupLogger.i("StartupComponent.Complete", "StartupCompleteComponent初始化线程", new Object[0]);
                mHandler = m32774k();
            }
            handler = mHandler;
        }
        if (!StartupComponentBase.isMainProcess()) {
            StartupLogger.i("StartupComponent.Complete", "非主进程启动，直接发送启动完成HomeReady通知...", new Object[0]);
            m32772f(0, handler);
            return;
        }
        StartupLogger.i("StartupComponent.Complete", CrashHandlerLogger.m34780h("主进程启动，开始监听第一个页面创建，超时时间%sms...", Long.valueOf(j)), new Object[0]);
        m32772f(j, handler);

        simpleActivityLifecycleCallback = new SimpleActivityLifecycleCallback() {
            @Override
            public void onActivityResumed(Activity activity) {
                super.onActivityResumed(activity);
                final String simpleName = activity.getClass().getSimpleName();
                if (StartupCompleteComponent.f24298b || StartupCompleteComponent.f24297a) {
                    StartupLogger.i("StartupComponent.Complete", CrashHandlerLogger.m34780h("创建页面[%s]，启动完成HomeReady通知已经完成或正在排队中，忽略.", simpleName), new Object[0]);
                    if (!StartupCompleteComponent.f24297a) {
                        StartupComponentBase.getApplication().unregisterActivityLifecycleCallbacks(this);
                    }
                } else if (CrashHandlerLogger.m34820R("com.xunmeng.pinduoduo.ui.activity.MainFrameActivity", activity.getClass().getName())) {
                    StartupLogger.i("StartupComponent.Complete", "创建闪屏页面[MainFrameActivity]，忽略.", new Object[0]);
                } else {
                    StartupLogger.i("StartupComponent.Complete", CrashHandlerLogger.m34780h("创建第一个非闪屏页面[%s]，等待页面绘制...", simpleName), new Object[0]);
                    activity.getWindow().getDecorView().post(new Runnable() {

                        public void run() {
                            if (StartupCompleteComponent.f24298b || StartupCompleteComponent.f24297a) {
                                StartupLogger.i("StartupComponent.Complete", CrashHandlerLogger.m34780h("页面[%s]第一次DoFrame完成，启动完成HomeReady通知已经完成或正在排队中，忽略.", simpleName), new Object[0]);
                                if (!StartupCompleteComponent.f24297a) {
                                    unregisterActivityLifecycleCallbacks();
                                    return;
                                }
                                return;
                            }
                            StartupLogger.i("StartupComponent.Complete", CrashHandlerLogger.m34780h("页面[%s]第一次DoFrame完成，解除监听后续页面，启动完成HomeReady通知排队中...", simpleName), new Object[0]);
                            unregisterActivityLifecycleCallbacks();
                            StartupCompleteComponent.f24297a = true;
                            new Handler(Looper.getMainLooper()).post(new Runnable() {

                                public void run() {
                                    if (StartupCompleteComponent.f24298b) {
                                        StartupLogger.i("StartupComponent.Complete", CrashHandlerLogger.m34780h("页面[%s]绘制完成，启动完成HomeReady通知已经完成，忽略.", simpleName), new Object[0]);
                                        return;
                                    }
                                    StartupLogger.i("StartupComponent.Complete", CrashHandlerLogger.m34780h("页面[%s]绘制完成，开始发送启动完成HomeReady通知...", simpleName), new Object[0]);
                                    StartupCompleteComponent.m32772f(0, handler);
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

    public static void m32771e(boolean z, Handler handler) {
        String str;
        synchronized (mLock) {
            if (!f24298b) {
                f24298b = true;
                mHandler = null;
                handler.getLooper().quit();
                Object[] objArr = new Object[1];
                if (z) {
                    str = "(启动超时)";
                } else {
                    str = "";
                }
                objArr[0] = str;
                StartupLogger.i("StartupComponent.Complete", CrashHandlerLogger.m34780h("启动完成HomeReady通知完毕%s.", objArr), new Object[0]);
                List<AbstractC5368a> list = f24299h;
                if (list != null && !list.isEmpty()) {
                    Iterator V = CrashHandlerLogger.m34824V(f24299h);
                    while (V.hasNext()) {
                        ((AbstractC5368a) V.next()).mo28070a(z);
                    }
                    f24299h.clear();
                }
                f24299h = null;
            }
        }
    }

    public static void m32772f(long j, Handler handler) {
        synchronized (mLock) {
            if (!f24298b) {
                if (j > 0) {
                    handler.sendEmptyMessageDelayed(2, j);
                } else {
                    handler.sendEmptyMessage(1);
                }
            }
        }
    }

    @Deprecated
    public static void m32773g(AbstractC5368a aVar) {
        synchronized (mLock) {
            if (f24298b) {
                StartupLogger.i("StartupComponent.Complete", "注册启动完成HomeReady监听[%s], 启动已经完成，直接回调", aVar.getClass().getName());
                aVar.mo28070a(false);
            } else {
                StartupLogger.i("StartupComponent.Complete", "注册启动完成HomeReady监听[%s], 开始监听...", aVar.getClass().getName());
                if (f24299h == null) {
                    f24299h = new LinkedList();
                }
                f24299h.add(aVar);
            }
        }
    }

    private static Handler m32774k() {
        HandlerThread handlerThread = new HandlerThread("Startup#StartupComponent.Complete");
        handlerThread.start();
        return new Handler(handlerThread.getLooper()) {
            public void handleMessage(Message message) {
                boolean z;
                if (message.what == 2) {
                    z = true;
                } else {
                    z = false;
                }
                StartupCompleteComponent.m32771e(z, this);
            }
        };
    }
}
