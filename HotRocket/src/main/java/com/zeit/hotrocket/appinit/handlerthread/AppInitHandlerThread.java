package com.zeit.hotrocket.appinit.handlerthread;

import android.os.Handler;
import android.os.HandlerThread;

import com.zeit.hotrocket.core.utils.ThreadPoolUtils;

@Deprecated
public class AppInitHandlerThread {

    private final boolean mFlag = false;

    private HandlerThread mHandlerThread;

    private Handler mHandler;

    public static class Holder {
        public static AppInitHandlerThread instance = new AppInitHandlerThread();
    }

    private void init() {
        if (this.mHandlerThread == null) {
            HandlerThread handlerThread = new HandlerThread("Startup#Pdd.InitThread", 10);
            this.mHandlerThread = handlerThread;
            handlerThread.start();
        }
        if (this.mHandler == null) {
            this.mHandler = new Handler(this.mHandlerThread.getLooper());
        }
    }

    public void mo27974b(Runnable runnable) {
        if (this.mFlag) {
            ThreadPoolUtils.getInstance().execute(runnable);
            return;
        }
        init();
        this.mHandler.post(runnable);
    }

    public void mo27975c(Runnable runnable, long millisecond) {
        if (this.mFlag) {
            ThreadPoolUtils.getInstance().delayExecute(runnable,millisecond);
            return;
        }
        init();
        this.mHandler.postDelayed(runnable, millisecond);
    }


    public static AppInitHandlerThread getInstance() {
        return Holder.instance;
    }
}
