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

    private static List<AbstractC5375a> f24309g;


    static void m32780a(final long j, final long j2, boolean z) {
        StartupLogger.m32758c("StartupComponent.Idle", "进程启动，初始化StartupIdleComponent", new Object[0]);
        HomeRenderInternal.m32763b(z);
        StartupCompleteComponent.m32773g(new StartupCompleteComponent.AbstractC5368a() {

            @Override
            public void mo28070a(boolean z) {
                StartupIdleComponent.m32783d(z);
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    public void run() {
                        StartupLogger.m32758c("StartupComponent.Idle", "开始监听IDLE，超时时间：%s ...", Long.valueOf(j));
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
            StartupLogger.m32758c("StartupComponent.Idle", "开始监听USER_IDLE，超时时间：%s ...", Long.valueOf(j));
        }
    }

    @Deprecated
    public static synchronized void m32782c(AbstractC5375a aVar) {
        synchronized (StartupIdleComponent.class) {
            if (StartupStageComponent.f24319a == StartupStage.USER_IDLE) {
                StartupLogger.m32758c("StartupComponent.Idle", "注册启动IDLE监听[%s]，直接回调启动USER_IDLE", aVar.getClass().getName());
                aVar.mo28075a(false);
                aVar.mo28076b(false);
                aVar.mo28077c(false);
            } else if (StartupStageComponent.f24319a == StartupStage.IDLE) {
                StartupLogger.m32758c("StartupComponent.Idle", "注册启动IDLE监听[%s]，直接回调启动IDLE，开始监听USER_IDLE...", aVar.getClass().getName());
                aVar.mo28075a(false);
                aVar.mo28076b(false);
                m32786h(aVar);
            } else if (StartupStageComponent.f24319a == StartupStage.COMPLETE) {
                StartupLogger.m32758c("StartupComponent.Idle", "注册启动IDLE监听[%s]，直接回调启动完成，开始监听IDLE/USER_IDLE...", aVar.getClass().getName());
                aVar.mo28075a(false);
                m32786h(aVar);
            } else {
                StartupLogger.m32758c("StartupComponent.Idle", "注册启动IDLE监听[%s], 开始监听...", aVar.getClass().getName());
                m32786h(aVar);
            }
        }
    }

    public static synchronized void m32783d(boolean z) {
        String str;
        synchronized (StartupIdleComponent.class) {
            if (StartupStageComponent.f24319a == StartupStage.DEFAULT) {
                StartupStageComponent.f24319a = StartupStage.COMPLETE;
                StringBuilder sb = new StringBuilder();
                sb.append("启动完成");
                if (z) {
                    str = "(超时)";
                } else {
                    str = "";
                }
                sb.append(str);
                sb.append(", 进行COMPLETE回调");
                StartupLogger.m32758c("StartupComponent.Idle", sb.toString(), new Object[0]);
                List<AbstractC5375a> list = f24309g;
                if (list != null && !list.isEmpty()) {
                    Iterator V = CrashHandlerLogger.m34824V(f24309g);
                    while (V.hasNext()) {
                        ((AbstractC5375a) V.next()).mo28075a(z);
                    }
                }
            }
        }
    }

    public static synchronized void m32784e(boolean z) {
        String str;
        synchronized (StartupIdleComponent.class) {
            if (StartupStageComponent.f24319a == StartupStage.COMPLETE) {
                StartupStageComponent.f24319a = StartupStage.IDLE;
                StringBuilder sb = new StringBuilder();
                sb.append("启动IDLE结束");
                if (z) {
                    str = "(超时)";
                } else {
                    str = "";
                }
                sb.append(str);
                sb.append(", 进行IDLE回调");
                StartupLogger.m32758c("StartupComponent.Idle", sb.toString(), new Object[0]);
                List<AbstractC5375a> list = f24309g;
                if (list != null && !list.isEmpty()) {
                    Iterator V = CrashHandlerLogger.m34824V(f24309g);
                    while (V.hasNext()) {
                        ((AbstractC5375a) V.next()).mo28076b(z);
                    }
                }
            }
        }
    }

    public static synchronized void m32785f(boolean z) {
        String str;
        synchronized (StartupIdleComponent.class) {
            if (StartupStageComponent.f24319a == StartupStage.IDLE) {
                StartupStageComponent.f24319a = StartupStage.USER_IDLE;
                StringBuilder sb = new StringBuilder();
                sb.append("启动USER_IDLE结束");
                if (z) {
                    str = "(超时)";
                } else {
                    str = "";
                }
                sb.append(str);
                sb.append(", 进行USER_IDLE回调");
                StartupLogger.m32758c("StartupComponent.Idle", sb.toString(), new Object[0]);
                List<AbstractC5375a> list = f24309g;
                if (list != null && !list.isEmpty()) {
                    Iterator V = CrashHandlerLogger.m34824V(f24309g);
                    while (V.hasNext()) {
                        ((AbstractC5375a) V.next()).mo28077c(z);
                    }
                    f24309g.clear();
                }
                f24309g = null;
            }
        }
    }

    private static void m32786h(AbstractC5375a aVar) {
        if (f24309g == null) {
            f24309g = new LinkedList();
        }
        f24309g.add(aVar);
    }

    public static abstract class AbstractC5375a {
        /* renamed from: a */
        public void mo28075a(boolean z) {
        }

        /* renamed from: b */
        public void mo28076b(boolean z) {
        }

        /* renamed from: c */
        public void mo28077c(boolean z) {
        }
    }
}
