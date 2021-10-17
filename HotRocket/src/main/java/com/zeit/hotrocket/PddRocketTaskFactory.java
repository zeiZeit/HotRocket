package com.zeit.hotrocket;

import com.zeit.hotrocket.appinit.annotations.PRIORITY;
import com.zeit.hotrocket.appinit.annotations.PROCESS;
import com.zeit.hotrocket.appinit.annotations.STAGE;
import com.zeit.hotrocket.core.utils.ListUtil;
import com.zeit.hotrocket.core.utils.SetUtil;

import java.util.ArrayList;
import java.util.List;

/* renamed from: com.xunmeng.pinduoduo.rocket.g */
public class PddRocketTaskFactory {
    /* renamed from: a */
    static List<PddRocketTask> m52507a(PROCESS process, PddRocketConfig bVar) {
        ArrayList<PddRocketTask> arrayList = new ArrayList();
        m52508b(arrayList, process);
        PddRocketTaskFactoryInterceptor hVar = bVar.f39775e;
        if (hVar == null) {
            return arrayList;
        }
        ArrayList arrayList2 = new ArrayList();
        for (PddRocketTask pddRocketTask : arrayList) {
            if (!hVar.mo44910a(pddRocketTask)) {
                arrayList2.add(pddRocketTask);
            }
        }
        return arrayList2;
    }

    /* renamed from: b */
    private static void m52508b(List<PddRocketTask> list, PROCESS process) {
        PddRocketStaticTask pddRocketStaticTask = null;
        if (process == PROCESS.MAIN) {
            list.add(new PddRocketStaticTask("app_home_pre_init", SetUtil.toHashSet(new String[0]), PRIORITY.MAX, ListUtil.toArrayList(PROCESS.MAIN), STAGE.AppInit, THREAD.MAIN, new HomeUIPreInit()));
            pddRocketStaticTask = new PddRocketStaticTask("UserIdleCommonInitTask", SetUtil.toHashSet(new String[0]), PRIORITY.MIN, ListUtil.toArrayList(PROCESS.MAIN, PROCESS.TITAN, PROCESS.LIFECYCLE, PROCESS.PATCH, PROCESS.SUPPORT, PROCESS.MEEPO, PROCESS.ASSISTS, PROCESS.XG_SERVICE_V4), STAGE.UserIdleInit, THREAD.BACKGROUND, "com.xunmeng.pinduoduo.appstartup.appinit.UserIdleCommonInitTask");
        } else if (process == PROCESS.TITAN) {

        } else if (process == PROCESS.LIFECYCLE) {

        } else if (process == PROCESS.PATCH) {

        } else if (process == PROCESS.SUPPORT) {

        } else if (process == PROCESS.ASSISTS) {

        } else if (process == PROCESS.XG_SERVICE_V4) {

        } else {
            return;
        }
        list.add(pddRocketStaticTask);
    }
}
