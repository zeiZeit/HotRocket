package com.zeit.hotrocket.rocket;

import com.zeit.hotrocket.appinit.annotations.PRIORITY;
import com.zeit.hotrocket.appinit.annotations.PROCESS;
import com.zeit.hotrocket.appinit.annotations.STAGE;
import com.zeit.hotrocket.appinit.annotations.THREAD;
import com.zeit.hotrocket.rocket.core.utils.ListUtil;
import com.zeit.hotrocket.rocket.core.utils.SetUtil;

import java.util.ArrayList;
import java.util.List;

public class HotRocketTaskFactory {
    static List<HotRocketTask> createHotRocketTasks(PROCESS process, HotRocketConfig hotRocketConfig) {
        ArrayList<HotRocketTask> taskArrayList = new ArrayList();
        m52508b(taskArrayList, process);
        HotRocketTaskFactoryInterceptor factoryInterceptor = hotRocketConfig.factoryInterceptor;
        if (factoryInterceptor == null) {
            return taskArrayList;
        }
        ArrayList arrayList2 = new ArrayList();
        for (HotRocketTask hotRocketTask : taskArrayList) {
            if (!factoryInterceptor.mo44910a(hotRocketTask)) {
                arrayList2.add(hotRocketTask);
            }
        }
        return arrayList2;
    }

    private static void m52508b(List<HotRocketTask> list, PROCESS process) {
        HotRocketStaticTask pddRocketStaticTask = null;
        if (process == PROCESS.MAIN) {
//            list.add(new PddRocketStaticTask("app_home_pre_init", SetUtil.toHashSet(new String[0]), PRIORITY.MAX, ListUtil.toArrayList(PROCESS.MAIN), STAGE.AppInit, THREAD.MAIN, new HomeUIPreInit()));
            pddRocketStaticTask = new HotRocketStaticTask("UserIdleCommonInitTask", SetUtil.toHashSet(new String[0]), PRIORITY.MIN, ListUtil.toArrayList(PROCESS.MAIN, PROCESS.TITAN, PROCESS.LIFECYCLE, PROCESS.PATCH, PROCESS.SUPPORT, PROCESS.MEEPO, PROCESS.ASSISTS, PROCESS.XG_SERVICE_V4), STAGE.UserIdleInit, THREAD.BACKGROUND, "com.xunmeng.pinduoduo.appstartup.appinit.UserIdleCommonInitTask");
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
