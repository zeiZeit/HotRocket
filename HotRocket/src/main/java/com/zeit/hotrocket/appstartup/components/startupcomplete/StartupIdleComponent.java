package com.zeit.hotrocket.appstartup.components.startupcomplete;

import android.os.Handler;
import android.os.Looper;
import android.os.MessageQueue;
import com.zeit.hotrocket.appstartup.components.StartupLogger;
import com.zeit.hotrocket.logger.CrashHandlerLogger;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class StartupIdleComponent {

    private static List<Observer> f24309g;


    static void init(final long j, final long j2, boolean observeHomeRender) {
        StartupLogger.i("StartupComponent.Idle", "进程启动，初始化StartupIdleComponent", new Object[0]);
        HomeRenderInternal.init(observeHomeRender);
        StartupCompleteComponent.m32773g(new StartupCompleteComponent.AbstractC5368a() {

            @Override
            public void mo28070a(boolean z) {
                StartupIdleComponent.m32783d(z);
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    public void run() {
                        StartupLogger.i("StartupComponent.Idle", "开始监听IDLE，超时时间：%s ...", Long.valueOf(j));
                        final UserIdleInternal iVar = new UserIdleInternal(handler);
                        handler.postDelayed(new Runnable() {

                            public void run() {
                                HomeRenderInternal.m32764c();
                                StartupIdleComponent.m32784e(true);
                                StartupIdleComponent.m32781b(j2, iVar);
                            }
                        }, j);
                        HomeRenderInternal.m32765d(new HomeRenderInternal.AbstractC5363a() {
                            @Override
                            public void mo28060a() {
                                Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
                                    public boolean queueIdle() {
                                        HomeRenderInternal.m32764c();
                                        StartupIdleComponent.m32784e(false);
                                        StartupIdleComponent.m32781b(j2, iVar);
                                        return false;
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
    }

    public static void m32781b(long j, UserIdleInternal iVar) {
        if (iVar.mo28086d(j, new UserIdleInternal.AbstractC5381a() {

            @Override
            public void mo28074a(boolean z) {
                StartupIdleComponent.m32785f(z);
            }
        })) {
            StartupLogger.i("StartupComponent.Idle", "开始监听USER_IDLE，超时时间：%s ...", Long.valueOf(j));
        }
    }

    @Deprecated
    public static synchronized void setObserver(Observer observer) {
        synchronized (StartupIdleComponent.class) {
            if (StartupStageComponent.stage == StartupStage.USER_IDLE) {
                StartupLogger.i("StartupComponent.Idle", "注册启动IDLE监听[%s]，直接回调启动USER_IDLE", observer.getClass().getName());
                observer.mo28075a(false);
                observer.mo28076b(false);
                observer.mo28077c(false);
            } else if (StartupStageComponent.stage == StartupStage.IDLE) {
                StartupLogger.i("StartupComponent.Idle", "注册启动IDLE监听[%s]，直接回调启动IDLE，开始监听USER_IDLE...", observer.getClass().getName());
                observer.mo28075a(false);
                observer.mo28076b(false);
                m32786h(observer);
            } else if (StartupStageComponent.stage == StartupStage.COMPLETE) {
                StartupLogger.i("StartupComponent.Idle", "注册启动IDLE监听[%s]，直接回调启动完成，开始监听IDLE/USER_IDLE...", observer.getClass().getName());
                observer.mo28075a(false);
                m32786h(observer);
            } else {
                StartupLogger.i("StartupComponent.Idle", "注册启动IDLE监听[%s], 开始监听...", observer.getClass().getName());
                m32786h(observer);
            }
        }
    }

    public static synchronized void m32783d(boolean z) {
        String str;
        synchronized (StartupIdleComponent.class) {
            if (StartupStageComponent.stage == StartupStage.DEFAULT) {
                StartupStageComponent.stage = StartupStage.COMPLETE;
                StringBuilder sb = new StringBuilder();
                sb.append("启动完成");
                if (z) {
                    str = "(超时)";
                } else {
                    str = "";
                }
                sb.append(str);
                sb.append(", 进行COMPLETE回调");
                StartupLogger.i("StartupComponent.Idle", sb.toString(), new Object[0]);
                List<Observer> list = f24309g;
                if (list != null && !list.isEmpty()) {
                    Iterator V = CrashHandlerLogger.m34824V(f24309g);
                    while (V.hasNext()) {
                        ((Observer) V.next()).mo28075a(z);
                    }
                }
            }
        }
    }

    public static synchronized void m32784e(boolean z) {
        String str;
        synchronized (StartupIdleComponent.class) {
            if (StartupStageComponent.stage == StartupStage.COMPLETE) {
                StartupStageComponent.stage = StartupStage.IDLE;
                StringBuilder sb = new StringBuilder();
                sb.append("启动IDLE结束");
                if (z) {
                    str = "(超时)";
                } else {
                    str = "";
                }
                sb.append(str);
                sb.append(", 进行IDLE回调");
                StartupLogger.i("StartupComponent.Idle", sb.toString(), new Object[0]);
                List<Observer> list = f24309g;
                if (list != null && !list.isEmpty()) {
                    Iterator V = CrashHandlerLogger.m34824V(f24309g);
                    while (V.hasNext()) {
                        ((Observer) V.next()).mo28076b(z);
                    }
                }
            }
        }
    }

    public static synchronized void m32785f(boolean z) {
        String str;
        synchronized (StartupIdleComponent.class) {
            if (StartupStageComponent.stage == StartupStage.IDLE) {
                StartupStageComponent.stage = StartupStage.USER_IDLE;
                StringBuilder sb = new StringBuilder();
                sb.append("启动USER_IDLE结束");
                if (z) {
                    str = "(超时)";
                } else {
                    str = "";
                }
                sb.append(str);
                sb.append(", 进行USER_IDLE回调");
                StartupLogger.i("StartupComponent.Idle", sb.toString(), new Object[0]);
                List<Observer> list = f24309g;
                if (list != null && !list.isEmpty()) {
                    Iterator V = CrashHandlerLogger.m34824V(f24309g);
                    while (V.hasNext()) {
                        ((Observer) V.next()).mo28077c(z);
                    }
                    f24309g.clear();
                }
                f24309g = null;
            }
        }
    }

    private static void m32786h(Observer aVar) {
        if (f24309g == null) {
            f24309g = new LinkedList();
        }
        f24309g.add(aVar);
    }

    public static abstract class Observer {
        public void mo28075a(boolean z) {
        }

        public void mo28076b(boolean z) {
        }

        public void mo28077c(boolean z) {
        }
    }
}
