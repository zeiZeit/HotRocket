package com.zeit.hotrocketdemo.tasks;

import android.app.Application;

import com.zeit.hotrocket.appinit.annotations.PRIORITY;
import com.zeit.hotrocket.appinit.annotations.PROCESS;
import com.zeit.hotrocket.appinit.annotations.STAGE;
import com.zeit.hotrocket.appinit.annotations.THREAD;
import com.zeit.hotrocket.rocket.HotRocketConfig;
import com.zeit.hotrocket.rocket.HotRocketProcessInstance;
import com.zeit.hotrocket.rocket.HotRocketStaticTask;
import com.zeit.hotrocket.rocket.HotRocketTask;
import com.zeit.hotrocket.rocket.HotRocketTaskFactoryInterceptor;
import com.zeit.hotrocket.rocket.core.utils.ListUtil;
import com.zeit.hotrocket.rocket.core.utils.SetUtil;
import com.zeit.hotrocket.rocket.init.ContextHolder;
import com.zeit.hotrocket.rocket.init.ProcessNameUtil;

import java.util.ArrayList;
import java.util.List;

public class HotRocketTaskFactory {
    public static ArrayList<HotRocketTask> createHotRocketTasks(Application application) {
        String processName = ProcessNameUtil.getCurrentProcessName();
        PROCESS process = HotRocketProcessInstance.getPROCESS(application,processName);
        ArrayList<HotRocketTask> taskArrayList = new ArrayList();
        if (process != null) {
            createTasks(taskArrayList, process);
        }
        return taskArrayList;
    }


    protected static void createTasks(List<HotRocketTask> list, PROCESS process) {
        HotRocketStaticTask hotRocketStaticTask = null;
        if (process == PROCESS.MAIN) {
            list.add(new HotRocketStaticTask("app_home_pre_init", SetUtil.toHashSet(new String[0]), PRIORITY.MAX, ListUtil.toArrayList(PROCESS.MAIN), STAGE.AppInit, THREAD.MAIN, new HomeUIPreInit()));
//            hotRocketStaticTask = new HotRocketStaticTask("UserIdleCommonInitTask", SetUtil.toHashSet(new String[0]), PRIORITY.MIN, ListUtil.toArrayList(PROCESS.MAIN, PROCESS.TITAN, PROCESS.LIFECYCLE, PROCESS.PATCH, PROCESS.SUPPORT, PROCESS.MEEPO, PROCESS.ASSISTS, PROCESS.XG_SERVICE_V4), STAGE.UserIdleInit, THREAD.BACKGROUND, "com.xunmeng.pinduoduo.appstartup.appinit.UserIdleCommonInitTask");
        } else if (process == PROCESS.LIFECYCLE) {

        } else if (process == PROCESS.PATCH) {

        } else if (process == PROCESS.SUPPORT) {

        } else if (process == PROCESS.ASSISTS) {

        }  else {
            return;
        }
//        list.add(hotRocketStaticTask);
    }
}
