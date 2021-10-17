package com.zeit.hotrocket;

import android.app.Application;
import android.text.TextUtils;


import com.zeit.hotrocket.appinit.annotations.PROCESS;
import com.zeit.hotrocket.appinit.annotations.STAGE;
import com.zeit.hotrocket.appinit.annotations.THREAD;
import com.zeit.hotrocket.core.Task;
import com.zeit.hotrocket.core.Rocket;
import com.zeit.hotrocket.core.utils.Log4Rocket;
import com.zeit.hotrocket.core.utils.ThreadUtil;
import com.zeit.hotrocket.p1087a.BarrierTask;
import com.zeit.hotrocket.p1087a.Barriers;
import com.zeit.hotrocket.p1088b.p1089a.MainThreadBusyStateControllerPlugin;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

class PddRocketPreload {

    /* renamed from: d */
    private HashMap<STAGE, Map<THREAD, Set<String>>> f39842d = new HashMap<>();

    /* renamed from: e */
    private List<PddRocketTask> mRocketTaskList;

    private Rocket mRocket;

    /* renamed from: h */
    private List<BarrierTask> m52486h() {
        Set<String> set;
        LinkedList linkedList = new LinkedList();
        HashSet hashSet = new HashSet();
        Map<THREAD, Set<String>> map = this.f39842d.get(STAGE.AppInit);
        if (!(map == null || (set = map.get(THREAD.BACKGROUND)) == null)) {
            hashSet = (HashSet) set;
        }
        linkedList.add(Barriers.m52400g(hashSet));
        HashSet hashSet2 = new HashSet();
        hashSet2.add(Barriers.f39765a);
        Map<THREAD, Set<String>> map2 = this.f39842d.get(STAGE.HomeReadyInit);
        if (map2 != null) {
            Set<String> set2 = map2.get(THREAD.BACKGROUND);
            Set<String> set3 = map2.get(THREAD.MAIN);
            if (set2 != null) {
                hashSet2.addAll(set2);
            }
            if (set3 != null) {
                hashSet2.addAll(set3);
            }
        }
        linkedList.add(Barriers.m52401h(hashSet2));
        HashSet hashSet3 = new HashSet();
        hashSet3.add(Barriers.f39766b);
        Map<THREAD, Set<String>> map3 = this.f39842d.get(STAGE.HomeIdleInit);
        if (map3 != null) {
            Set<String> set4 = map3.get(THREAD.BACKGROUND);
            Set<String> set5 = map3.get(THREAD.MAIN);
            if (set4 != null) {
                hashSet3.addAll(set4);
            }
            if (set5 != null) {
                hashSet3.addAll(set5);
            }
        }
        linkedList.add(Barriers.m52402i(hashSet3));
        return linkedList;
    }

    /* renamed from: j */
    private List<PddRocketTask> m52488j(List<PddRocketTask> list) {
        LinkedList linkedList = new LinkedList();
        for (PddRocketTask pddRocketTask : list) {
            if (!(STAGE.AppInit.equals(pddRocketTask.mo44817m()) && THREAD.MAIN.equals(pddRocketTask.mo44816l()))) {
                break;
            }
            linkedList.add(pddRocketTask);
            m52496r(pddRocketTask.mo44817m(), pddRocketTask.mo44816l(), pddRocketTask.mo44813i());
        }
        return linkedList;
    }

    /* renamed from: k */
    private List<Task> m52489k(Application application, List<PddRocketTask> list) {
        LinkedList linkedList = new LinkedList();
        for (PddRocketTask pddRocketTask : list) {
            if (STAGE.AppInit.equals(pddRocketTask.mo44817m()) && THREAD.BACKGROUND.equals(pddRocketTask.mo44816l())) {
                linkedList.add(m52493o(application, pddRocketTask, null));
                m52496r(pddRocketTask.mo44817m(), pddRocketTask.mo44816l(), pddRocketTask.mo44813i());
            }
        }
        return linkedList;
    }

    /* renamed from: l */
    private List<Task> m52490l(Application application, List<PddRocketTask> list) {
        LinkedList linkedList = new LinkedList();
        for (PddRocketTask pddRocketTask : list) {
            if (STAGE.HomeReadyInit.equals(pddRocketTask.mo44817m())) {
                linkedList.add(m52493o(application, pddRocketTask, Barriers.f39765a));
                m52496r(pddRocketTask.mo44817m(), pddRocketTask.mo44816l(), pddRocketTask.mo44813i());
            }
        }
        return linkedList;
    }

    /* renamed from: m */
    private List<Task> m52491m(Application application, List<PddRocketTask> list) {
        LinkedList linkedList = new LinkedList();
        for (PddRocketTask pddRocketTask : list) {
            if (STAGE.HomeIdleInit.equals(pddRocketTask.mo44817m())) {
                linkedList.add(m52493o(application, pddRocketTask, Barriers.f39766b));
                m52496r(pddRocketTask.mo44817m(), pddRocketTask.mo44816l(), pddRocketTask.mo44813i());
            }
        }
        return linkedList;
    }

    /* renamed from: n */
    private List<Task> m52492n(Application application, List<PddRocketTask> list) {
        LinkedList linkedList = new LinkedList();
        for (PddRocketTask pddRocketTask : list) {
            if (STAGE.UserIdleInit.equals(pddRocketTask.mo44817m())) {
                linkedList.add(m52493o(application, pddRocketTask, Barriers.f39767c));
                m52496r(pddRocketTask.mo44817m(), pddRocketTask.mo44816l(), pddRocketTask.mo44813i());
            }
        }
        return linkedList;
    }

    /* renamed from: p */
    private String m52494p(Collection<PddRocketTask> collection) {
        StringBuilder sb = new StringBuilder();
        for (PddRocketTask pddRocketTask : collection) {
            sb.append(pddRocketTask.mo44813i());
            sb.append(", ");
        }
        return sb.toString();
    }

    /* renamed from: q */
    private static String m52495q(Collection<Task> collection) {
        StringBuilder sb = new StringBuilder();
        for (Task cVar : collection) {
            sb.append(cVar.name);
            sb.append(", ");
        }
        return sb.toString();
    }

    /* renamed from: r */
    private void m52496r(STAGE stage, THREAD thread, String str) {
        Map<THREAD, Set<String>> map = this.f39842d.get(stage);
        if (map == null) {
            map = new HashMap<>();
            this.f39842d.put(stage, map);
        }
        Set<String> set = map.get(thread);
        if (set == null) {
            set = new HashSet<>();
            map.put(thread, set);
        }
        set.add(str);
    }

    PddRocketPreload() {
    }

    /* renamed from: g */
    private Rocket m52485g(Application application, PddRocketConfig bVar, List<PddRocketTask> list, PROCESS process) {
        List<Task> k = m52489k(application, list);
        String str = PddRocket.TAG;
        com.zeit.hotrocket.logger.Logger.i(str, "进程[" + process + "]的[" + STAGE.AppInit + "/" + THREAD.BACKGROUND + "]任务列表(" + k.size() + "): " + m52495q(k));
        LinkedList linkedList = new LinkedList(k);
        List<Task> l = m52490l(application, list);
        String str2 = PddRocket.TAG;
        com.zeit.hotrocket.logger.Logger.i(str2, "进程[" + process + "]的[" + STAGE.HomeReadyInit + "]任务列表(" + l.size() + "): " + m52495q(l));
        linkedList.addAll(l);
        List<Task> m = m52491m(application, list);
        String str3 = PddRocket.TAG;
        com.zeit.hotrocket.logger.Logger.i(str3, "进程[" + process + "]的[" + STAGE.HomeIdleInit + "]任务列表(" + m.size() + "): " + m52495q(m));
        linkedList.addAll(m);
        List<Task> n = m52492n(application, list);
        String str4 = PddRocket.TAG;
        com.zeit.hotrocket.logger.Logger.i(str4, "进程[" + process + "]的[" + STAGE.UserIdleInit + "]任务列表(" + n.size() + "): " + m52495q(n));
        linkedList.addAll(n);
        linkedList.addAll(m52486h());
        if (linkedList.isEmpty()) {
            return null;
        }
        Rocket c = Rocket.build(new Rocket.Config().setProcessName(process.getName()).setLogger(new Log4Rocket.Logger() {
            /* class com.xunmeng.pinduoduo.rocket.PddRocketPreload.C90671 */

            @Override // com.xunmeng.pinduoduo.rocket.core.p1090a.Log4Rocket.AbstractC9059a
            /* renamed from: b */
            public void log(String str) {
                com.zeit.hotrocket.logger.Logger.d(PddRocket.TAG, str);
            }
        }).setThreadPoolSize(bVar.f39774d).setTaskList(linkedList));
        if (bVar.f39772b) {
            new MainThreadBusyStateControllerPlugin().mo44832a(c, Long.valueOf(bVar.f39773c));
        }
        return c;
    }

    /* renamed from: i */
    private List<PddRocketTask> m52487i(PROCESS process, PddRocketConfig bVar) {
        List<PddRocketTask> a = PddRocketTaskFactory.m52507a(process, bVar);
        String str = PddRocket.TAG;
        com.zeit.hotrocket.logger.Logger.i(str, "进程[" + process + "]的启动任务列表(" + a.size() + "): " + m52494p(a));
        return a;
    }

    /* renamed from: o */
    private Task m52493o(final Application application, final PddRocketTask pddRocketTask, String str) {
        if (!TextUtils.isEmpty(str)) {
            pddRocketTask.mo44814j().add(str);
        }
        return new Task(pddRocketTask.mo44813i(), pddRocketTask.mo44815k().priority, pddRocketTask.mo44814j()) {
            /* class com.xunmeng.pinduoduo.rocket.PddRocketPreload.C90682 */

            @Override // com.xunmeng.pinduoduo.rocket.core.C9063c
            /* renamed from: d */
            public void runTask() {
                super.runTask();
                if (THREAD.MAIN.equals(pddRocketTask.mo44816l())) {
                    ThreadUtil.executeRunnable(new Runnable() {
                        /* class com.xunmeng.pinduoduo.rocket.PddRocketPreload.C90682.RunnableC90691 */

                        public void run() {
                            pddRocketTask.run(application);
                        }
                    });
                } else {
                    pddRocketTask.run(application);
                }
            }
        };
    }

    /* access modifiers changed from: package-private */
    /* renamed from: a */
    public void mo44906a(PddRocketConfig bVar) {
        PROCESS a = PddRocketProcessInstance.m52502a(BaseApplication.m35441d(), bVar.f39771a);
        if (a != null) {
            List<PddRocketTask> i = m52487i(a, bVar);
            this.mRocketTaskList = m52488j(i);
            String str = PddRocket.TAG;
            com.zeit.hotrocket.logger.Logger.i(str, "进程[" + a + "]的[" + STAGE.AppInit + "/" + THREAD.MAIN + "]任务列表(" + this.mRocketTaskList.size() + "): " + m52494p(this.mRocketTaskList));
            this.mRocket = m52485g(BaseApplication.m35441d(), bVar, i, a);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: b */
    public List<PddRocketTask> mo44907b() {
        return this.mRocketTaskList;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: c */
    public Rocket getRocket() {
        return this.mRocket;
    }
}
