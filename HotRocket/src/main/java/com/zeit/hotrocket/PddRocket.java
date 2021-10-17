package com.zeit.hotrocket;

import com.xunmeng.pinduoduo.basekit.BaseApplication;
import com.zeit.hotrocket.core.Task;
import com.zeit.hotrocket.core.Rocket;
import com.zeit.hotrocket.core.TaskQueue;
import com.zeit.hotrocket.logger.Logger;
import com.zeit.hotrocket.p1087a.BarrierTask;
import com.zeit.hotrocket.p1087a.Barriers;

import java.util.List;

/* renamed from: com.xunmeng.pinduoduo.rocket.a */
public class PddRocket {

    /* renamed from: a */
    public static String TAG = "PddRocket";

    /* renamed from: l */
    private static final PddRocketPreload ROCKET_PRELOAD = new PddRocketPreload();

    /* renamed from: m */
    private static boolean f39755m = false;

    /* renamed from: n */
    private static boolean f39756n = false;

    /* renamed from: o */
    private static final Object f39757o = new Object();

    /* renamed from: p */
    private static boolean f39758p = false;

    /* renamed from: q */
    private static final Object f39759q = new Object();

    /* renamed from: r */
    private static boolean f39760r = false;

    /* renamed from: s */
    private static final Object f39761s = new Object();

    /* renamed from: t */
    private static PddRocketListener f39762t;

    /* renamed from: u */
    private static PddRocketTaskListener f39763u;

    /* renamed from: b */
    public static void m52380b(PddRocketConfig bVar) {
        String str = TAG;
        Logger.i(str, "Rocket预加载开始 >>> " + bVar.f39771a);
        if (m52390v(bVar, false)) {
            Logger.i(TAG, "Rocket已经预加载, 跳过本次预加载.");
        } else {
            Logger.i(TAG, "Rocket预加载完成.");
        }
    }

    /* renamed from: c */
    public static void m52381c(PddRocketConfig bVar) {
        String str = TAG;
        Logger.i(str, "Rocket初始化 >>> " + bVar.f39771a);
        m52393y();
        if (m52390v(bVar, true)) {
            Logger.i(TAG, "Rocket已经预加载, 跳过本次创建.");
        } else {
            Logger.i(TAG, "Rocket创建完成.");
        }
        Logger.i(TAG, "Rocket开始执行.");
        m52394z();
        m52391w();
        Rocket x = m52392x();
        Logger.i(TAG, "Rocket执行同步部分完成.");
        if (x == null) {
            Logger.i(TAG, "Rocket全部执行完成(没有异步任务).");
            m52386h();
            return;
        }
        x.addTaskQueueListener(new TaskQueue.C9066b() {
            /* class com.xunmeng.pinduoduo.rocket.PddRocket.C90501 */

            @Override // com.xunmeng.pinduoduo.rocket.core.TaskQueue.C9066b, com.xunmeng.pinduoduo.rocket.core.TaskQueue.AbstractC9065a
            /* renamed from: a */
            public void onRocketStop(Rocket rocket, List<Task> list) {
                rocket.removeTaskQueueListener(this);
                Logger.i(PddRocket.TAG, "Rocket全部执行完成.");
                PddRocket.m52386h();
            }
        });
    }

    /* renamed from: d */
    public static void m52382d() {
        synchronized (f39757o) {
            if (!f39756n) {
                Logger.i(TAG, ">>>>>>>>>>>>>>>>> %s <<<<<<<<<<<<<<<<<", "onHomeReady");
                BarrierTask aVar = Barriers.f39768d;
                if (aVar != null) {
                    aVar.mo44821a();
                }
                f39756n = true;
            }
        }
    }

    /* renamed from: e */
    public static void m52383e() {
        synchronized (f39759q) {
            if (!f39758p) {
                Logger.i(TAG, ">>>>>>>>>>>>>>>>> %s <<<<<<<<<<<<<<<<<", "onHomeIdle");
                BarrierTask aVar = Barriers.f39769e;
                if (aVar != null) {
                    aVar.mo44821a();
                }
                f39758p = true;
            }
        }
    }

    /* renamed from: f */
    public static void m52384f() {
        synchronized (f39761s) {
            if (!f39760r) {
                Logger.i(TAG, ">>>>>>>>>>>>>>>>> %s <<<<<<<<<<<<<<<<<", "onUserIdle");
                BarrierTask aVar = Barriers.f39770f;
                if (aVar != null) {
                    aVar.mo44821a();
                }
                f39760r = true;
            }
        }
    }

    /* renamed from: h */
    public static void m52386h() {
        PddRocketListener cVar = f39762t;
        if (cVar != null) {
            cVar.mo24805d();
        }
    }

    /* renamed from: j */
    public static void m52388j(String str) {
        PddRocketTaskListener iVar = f39763u;
        if (iVar != null) {
            iVar.mo24801b(str);
        }
    }

    /* renamed from: k */
    public static void m52389k(String str) {
        PddRocketTaskListener iVar = f39763u;
        if (iVar != null) {
            iVar.mo24802c(str);
        }
    }

    /* renamed from: v */
    private static boolean m52390v(PddRocketConfig bVar, boolean z) {
        synchronized (PddRocket.class) {
            if (f39755m) {
                return true;
            }
            ROCKET_PRELOAD.mo44906a(bVar);
            f39755m = true;
            return false;
        }
    }

    /* renamed from: w */
    private static void m52391w() {
        List<PddRocketTask> b = ROCKET_PRELOAD.mo44907b();
        if (b != null) {
            for (PddRocketTask pddRocketTask : b) {
                m52388j(pddRocketTask.mo44813i());
                pddRocketTask.run(BaseApplication.m35441d());
                m52389k(pddRocketTask.mo44813i());
            }
        }
    }

    /* renamed from: x */
    private static Rocket m52392x() {
        Rocket rocket = ROCKET_PRELOAD.getRocket();
        if (rocket != null) {
            rocket.addTaskListener(new Task.TaskListener() {
                /* class com.xunmeng.pinduoduo.rocket.PddRocket.C90512 */

                @Override // com.xunmeng.pinduoduo.rocket.core.C9063c.AbstractC9064a
                /* renamed from: a */
                public void onTaskStart(Task cVar) {
                    if (!(cVar instanceof BarrierTask)) {
                        PddRocket.m52388j(cVar.name);
                    }
                }

                @Override // com.xunmeng.pinduoduo.rocket.core.C9063c.AbstractC9064a
                /* renamed from: b */
                public void onTaskComplete(Task cVar) {
                    if (!(cVar instanceof BarrierTask)) {
                        PddRocket.m52389k(cVar.name);
                    }
                }
            });
            rocket.launch();
        }
        return rocket;
    }

    /* renamed from: y */
    private static void m52393y() {
        PddRocketListener cVar = f39762t;
        if (cVar != null) {
            cVar.mo24803b();
        }
    }

    /* renamed from: z */
    private static void m52394z() {
        PddRocketListener cVar = f39762t;
        if (cVar != null) {
            cVar.mo24804c();
        }
    }

    /* renamed from: g */
    public static void m52385g(PddRocketListener cVar) {
        f39762t = cVar;
    }

    /* renamed from: i */
    public static void m52387i(PddRocketTaskListener iVar) {
        f39763u = iVar;
    }
}
