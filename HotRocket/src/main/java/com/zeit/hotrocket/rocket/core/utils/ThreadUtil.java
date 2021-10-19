package com.zeit.hotrocket.rocket.core.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class ThreadUtil {

    private static Handler mHandler = new Handler(Looper.getMainLooper());

    public interface OnResultListener {
        void onResult(boolean z);
    }

    public static void isMainThreadBusy(final long j, final OnResultListener onResultListener) {
        Handler handler = new Handler(Looper.getMainLooper()) {
            public void handleMessage(Message message) {
                boolean z;
                super.handleMessage(message);
                long longValue = ((Long) message.obj).longValue();
                if (longValue > 0) {
                    long currentTimeMillis = System.currentTimeMillis() - longValue;
                    OnResultListener listener = onResultListener;
                    if (listener != null) {
                        if (currentTimeMillis > j) {
                            z = true;
                        } else {
                            z = false;
                        }
                        listener.onResult(z);
                        return;
                    }
                    return;
                }
                throw new IllegalStateException("ThreadUtil.isMainThreadBusy startTime <= 0");
            }
        };
        Message obtainMessage = handler.obtainMessage();
        obtainMessage.obj = Long.valueOf(System.currentTimeMillis());
        handler.sendMessage(obtainMessage);
    }


    public static void executeRunnable(Runnable runnable) {
        mHandler.post(runnable);
    }
}
