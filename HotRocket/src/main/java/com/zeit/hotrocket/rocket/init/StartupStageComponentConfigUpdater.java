package com.zeit.hotrocket.rocket.init;


import com.zeit.hotrocket.appstartup.components.startupcomplete.StartupStageComponent;
import com.zeit.hotrocket.appstartup.components.startupcomplete.StartupStageListener;

public class StartupStageComponentConfigUpdater {
    static void m28823a() {
        StartupStageComponent.setListener(new StartupStageListener() {
            /* class com.xunmeng.pinduoduo.app.StartupStageComponentConfigUpdater.C45981 */

            @Override // com.xunmeng.pinduoduo.appstartup.components.startupcomplete.StartupStageListener
            /* renamed from: c */
            public void mo22063c(boolean z) {
                String str;
                Object[] objArr = new Object[2];
                if (z) {
                    str = "(超时)";
                } else {
                    str = "";
                }
                objArr[0] = str;
                objArr[1] = ProcessNameUtil.getCurrentProcessName();
                PLog.m14196i("StartupComponent.Messenger", "启动进入页面可见阶段(首页或落地页首帧绘制完成)%s, 进程: %s", objArr);
                if (RuntimeInfo.m7323h()) {
                    MessageCenter.m35854b().mo30283h(new Message0("msg_titan_lazyload_main_process_startup_complete"));
                    PLog.m14196i("StartupComponent.Messenger", "发送启动完成通知[%s]完毕.", "msg_titan_lazyload_main_process_startup_complete");
                }
            }

            @Override // com.xunmeng.pinduoduo.appstartup.components.startupcomplete.StartupStageListener
            /* renamed from: d */
            public void mo22064d(boolean z) {
                String str;
                Object[] objArr = new Object[2];
                if (z) {
                    str = "(超时)";
                } else {
                    str = "";
                }
                objArr[0] = str;
                objArr[1] = RuntimeInfo.f5705c;
                PLog.m14196i("StartupComponent.Messenger", "启动进入IDLE阶段(主线程短暂空闲)%s, 进程: %s", objArr);
                StartupStageComponentConfigUpdater.m28824b();
                Apollo.m28528i().mo24659t("coldStartUp.cold_startup_complete_component_delay_millis", new AbstractC4545g() {
                    /* class com.xunmeng.pinduoduo.app.StartupStageComponentConfigUpdater.C45981.C45991 */

                    @Override // com.xunmeng.pinduoduo.apollo.p494c.AbstractC4545g
                    /* renamed from: b */
                    public void mo7553b(String str, String str2, String str3) {
                        StartupStageComponentConfigUpdater.m28824b();
                    }
                });
            }

            @Override // com.xunmeng.pinduoduo.appstartup.components.startupcomplete.StartupStageListener
            /* renamed from: e */
            public void mo22065e(boolean z) {
                String str;
                Object[] objArr = new Object[2];
                if (z) {
                    str = "(超时)";
                } else {
                    str = "";
                }
                objArr[0] = str;
                objArr[1] = RuntimeInfo.f5705c;
                PLog.m14196i("StartupComponent.Messenger", "启动进入USER_IDLE阶段(主线程完全空闲)%s, 进程: %s", objArr);
            }
        });
    }

    /* renamed from: i */
    public static boolean m28831i() {
        return StartupAbHelper.m38617c("ab_app_startup_component_observe_home_render_5650", false, true);
    }

    /* renamed from: b */
    public static void m28824b() {
        C6055f.m35977e().mo30359h(new Runnable() {
            /* class com.xunmeng.pinduoduo.app.StartupStageComponentConfigUpdater.RunnableC46002 */

            public void run() {
                StartupStageComponentConfigUpdater.m28826d();
                StartupStageComponentConfigUpdater.m28828f();
                StartupStageComponentConfigUpdater.m28830h();
            }
        });
    }

    /* renamed from: c */
    public static long m28825c() {
        return MMKVHelper.m38616b().getLong("coldStartUp.cold_startup_complete_component_delay_millis", 5000);
    }

    /* renamed from: d */
    public static void m28826d() {
        long f = C5973b.m35556f(Apollo.m28528i().mo24661v("coldStartUp.cold_startup_complete_component_delay_millis", String.valueOf(5000L)), 5000);
        long j = MMKVHelper.m38616b().getLong("coldStartUp.cold_startup_complete_component_delay_millis", 5000);
        if (f != j) {
            PLog.m14194i("StartupComponent.Messenger", "启动完成或者接收到Config回调, coldStartUp.cold_startup_complete_component_delay_millis变化: " + j + " -> " + f);
            MMKVHelper.m38616b().putLong("coldStartUp.cold_startup_complete_component_delay_millis", f);
            return;
        }
        PLog.m14194i("StartupComponent.Messenger", "启动完成或者接收到Config回调, coldStartUp.cold_startup_complete_component_delay_millis没有变化: " + j);
    }

    /* renamed from: e */
    public static long m28827e() {
        return MMKVHelper.m38616b().getLong("coldStartUp.cold_startup_idle_component_delay_millis", 10000);
    }

    /* renamed from: f */
    public static void m28828f() {
        long f = C5973b.m35556f(Apollo.m28528i().mo24661v("coldStartUp.cold_startup_idle_component_delay_millis", String.valueOf(10000L)), 10000);
        long j = MMKVHelper.m38616b().getLong("coldStartUp.cold_startup_idle_component_delay_millis", 10000);
        if (f != j) {
            PLog.m14194i("StartupComponent.Messenger", "启动完成或者接收到Config回调, coldStartUp.cold_startup_idle_component_delay_millis变化: " + j + " -> " + f);
            MMKVHelper.m38616b().putLong("coldStartUp.cold_startup_idle_component_delay_millis", f);
            return;
        }
        PLog.m14194i("StartupComponent.Messenger", "启动完成或者接收到Config回调, coldStartUp.cold_startup_idle_component_delay_millis没有变化: " + j);
    }

    /* renamed from: g */
    public static long m28829g() {
        return MMKVHelper.m38616b().getLong("coldStartUp.cold_startup_user_idle_component_delay_millis", 30000);
    }

    /* renamed from: h */
    public static void m28830h() {
        long f = C5973b.m35556f(Apollo.m28528i().mo24661v("coldStartUp.cold_startup_user_idle_component_delay_millis", String.valueOf(30000L)), 30000);
        long j = MMKVHelper.m38616b().getLong("coldStartUp.cold_startup_user_idle_component_delay_millis", 30000);
        if (f != j) {
            PLog.m14194i("StartupComponent.Messenger", "启动完成或者接收到Config回调, coldStartUp.cold_startup_user_idle_component_delay_millis变化: " + j + " -> " + f);
            MMKVHelper.m38616b().putLong("coldStartUp.cold_startup_user_idle_component_delay_millis", f);
            return;
        }
        PLog.m14194i("StartupComponent.Messenger", "启动完成或者接收到Config回调, coldStartUp.cold_startup_user_idle_component_delay_millis没有变化: " + j);
    }
}
