package com.zeit.hotrocket.appstartup.components.startupcomplete;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import com.zeit.hotrocket.appstartup.components.StartupLogger;
import com.zeit.hotrocket.logger.CrashHandlerLogger;


public class UserIdleInternal {


    public static final long[] f24333c = {1024, 1024, 1024, 512, 512, 256, 256, 128, 128, 64, 64, 32, 32, 16, 16, 16, 16};

    public final Handler f24334a;

    public boolean f24335b = false;

    private boolean f24336f = false;

    public interface AbstractC5381a {
        void mo28074a(boolean z);
    }

    public void mo28087e(final int i, final AbstractC5381a aVar) {
        if (this.f24335b) {
            StartupLogger.d("StartupComponent.User.Idle", "already callback by timeout, stop looping.", new Object[0]);
        } else if (i >= f24333c.length) {
            StartupLogger.d("StartupComponent.User.Idle", "stop observing UserIdle, callback for UserIdle.", new Object[0]);
            aVar.mo28074a(false);
            this.f24335b = true;
        } else {
            StartupLogger.d("StartupComponent.User.Idle", "checking main thread message delay... ", new Object[0]);
            final long elapsedRealtime = SystemClock.elapsedRealtime();
            this.f24334a.post(new Runnable() {

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
                    UserIdleInternal.this.f24334a.postDelayed(new Runnable() {

                        public void run() {
                            int i = 0;
                            UserIdleInternal iVar = UserIdleInternal.this;
                            if (elapsedRealtime - elapsedRealtime > 16) {
                                i = 0;
                            } else {
                                i = i + 1;
                            }
                            iVar.mo28087e(i, aVar);
                        }
                    }, CrashHandlerLogger.m34829c(UserIdleInternal.f24333c, i));
                }
            });
        }
    }

    public UserIdleInternal(Handler handler) {
        this.f24334a = handler;
    }

    public boolean mo28086d(long j, final AbstractC5381a aVar) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new IllegalStateException("UserIdleInternal observeUserIdle must run in main thread.");
        } else if (this.f24336f) {
            return false;
        } else {
            this.f24336f = true;
            StartupLogger.i("StartupComponent.User.Idle", "start observing UserIdle, timeout(%s)...", Long.valueOf(j));
            this.f24334a.postDelayed(new Runnable() {

                public void run() {
                    if (!UserIdleInternal.this.f24335b) {
                        StartupLogger.i("StartupComponent.User.Idle", "stop observing UserIdle, callback for timeout.", new Object[0]);
                        aVar.mo28074a(true);
                        UserIdleInternal.this.f24335b = true;
                        return;
                    }
                    StartupLogger.i("StartupComponent.User.Idle", "already callback by UserIdle, ignore.", new Object[0]);
                }
            }, j);
            mo28087e(0, aVar);
            return true;
        }
    }
}
