package com.zeit.hotrocket.appstartup.components.startupcomplete;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import com.zeit.hotrocket.appstartup.components.StartupLogger;
import com.zeit.hotrocket.logger.CrashHandlerLogger;


public class UserIdleInternal {


    public static final long[] f24333c = {1024, 1024, 1024, 512, 512, 256, 256, 128, 128, 64, 64, 32, 32, 16, 16, 16, 16};

    public final Handler mHandler;

    public boolean hasComplete = false;

    private boolean hasStart = false;

    public interface Listener {
        void complete(boolean z);
    }

    public void checkMessage(final int i, final Listener listener) {
        if (this.hasComplete) {
            StartupLogger.d("StartupComponent.User.Idle", "already callback by timeout, stop looping.", new Object[0]);
        } else if (i >= f24333c.length) {
            StartupLogger.d("StartupComponent.User.Idle", "stop observing UserIdle, callback for UserIdle.", new Object[0]);
            listener.complete(false);
            this.hasComplete = true;
        } else {
            StartupLogger.d("StartupComponent.User.Idle", "checking main thread message delay... ", new Object[0]);
            final long elapsedRealtime = SystemClock.elapsedRealtime();
            this.mHandler.post(new Runnable() {

                public void run() {
                    String str;
                    final long elapsedRealtime = SystemClock.elapsedRealtime();
                    Object[] objArr = new Object[5];
                    objArr[0] = Long.valueOf(elapsedRealtime - elapsedRealtime);
                    if (elapsedRealtime - elapsedRealtime > 16) {
                        str = "Restart";
                    } else {
                        str = "OK";
                    }
                    objArr[1] = str;
                    objArr[2] = Long.valueOf(CrashHandlerLogger.m34829c(UserIdleInternal.f24333c, i));
                    objArr[3] = Integer.valueOf(i);
                    objArr[4] = Integer.valueOf(UserIdleInternal.f24333c.length - 1);
                    StartupLogger.d("StartupComponent.User.Idle", "current main thread message delay: %sms(%s), continue checking after %sms(%s/%s)...", objArr);
                    UserIdleInternal.this.mHandler.postDelayed(new Runnable() {

                        public void run() {
                            int i = 0;
                            UserIdleInternal iVar = UserIdleInternal.this;
                            if (elapsedRealtime - elapsedRealtime > 16) {
                                i = 0;
                            } else {
                                i = i + 1;
                            }
                            iVar.checkMessage(i, listener);
                        }
                    }, CrashHandlerLogger.m34829c(UserIdleInternal.f24333c, i));
                }
            });
        }
    }

    public UserIdleInternal(Handler handler) {
        this.mHandler = handler;
    }

    public boolean addListener(long delay, final Listener listener) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new IllegalStateException("UserIdleInternal observeUserIdle must run in main thread.");
        } else if (this.hasStart) {
            return false;
        } else {
            this.hasStart = true;
            StartupLogger.i("StartupComponent.User.Idle", "start observing UserIdle, timeout(%s)...", Long.valueOf(delay));
            this.mHandler.postDelayed(new Runnable() {

                public void run() {
                    if (!UserIdleInternal.this.hasComplete) {
                        StartupLogger.i("StartupComponent.User.Idle", "stop observing UserIdle, callback for timeout.", new Object[0]);
                        listener.complete(true);
                        UserIdleInternal.this.hasComplete = true;
                        return;
                    }
                    StartupLogger.i("StartupComponent.User.Idle", "already callback by UserIdle, ignore.", new Object[0]);
                }
            }, delay);
            checkMessage(0, listener);
            return true;
        }
    }
}
