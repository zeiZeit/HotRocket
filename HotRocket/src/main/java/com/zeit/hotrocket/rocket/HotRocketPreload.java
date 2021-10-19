package com.zeit.hotrocket.rocket;

import android.app.Application;
import android.text.TextUtils;


import com.zeit.hotrocket.appinit.annotations.PROCESS;
import com.zeit.hotrocket.appinit.annotations.STAGE;
import com.zeit.hotrocket.appinit.annotations.THREAD;
import com.zeit.hotrocket.logger.Logger;
import com.zeit.hotrocket.rocket.core.Task;
import com.zeit.hotrocket.rocket.core.Rocket;
import com.zeit.hotrocket.rocket.core.utils.Log4Rocket;
import com.zeit.hotrocket.rocket.core.utils.ThreadUtil;
import com.zeit.hotrocket.rocket.barrier.BarrierTask;
import com.zeit.hotrocket.rocket.barrier.Barriers;
import com.zeit.hotrocket.rocket.init.ContextHolder;
import com.zeit.hotrocket.rocket.p1088b.p1089a.MainThreadBusyStateControllerPlugin;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

class HotRocketPreload {

    /**
     * 各个阶段里边不同thread下执行的任务
     */
    private HashMap<STAGE, Map<THREAD, Set<String>>> mStageTaskMap = new HashMap<>();

    private List<HotRocketTask> mRocketTaskList;

    private Rocket mRocket;

    private List<BarrierTask> getBarrierTask() {
        Set<String> set;
        LinkedList linkedList = new LinkedList();
        HashSet hashSet = new HashSet();
        Map<THREAD, Set<String>> map = this.mStageTaskMap.get(STAGE.AppInit);
        if (!(map == null || (set = map.get(THREAD.BACKGROUND)) == null)) {
            hashSet = (HashSet) set;
        }
        linkedList.add(Barriers.createAppInitBarrierTask(hashSet));
        HashSet hashSet2 = new HashSet();
        hashSet2.add(Barriers.mAppInit2HomeReadyInit);
        Map<THREAD, Set<String>> homeReadyInitMap = this.mStageTaskMap.get(STAGE.HomeReadyInit);
        if (homeReadyInitMap != null) {
            Set<String> backgroundTaskSet = homeReadyInitMap.get(THREAD.BACKGROUND);
            Set<String> mainThreadTaskSet = homeReadyInitMap.get(THREAD.MAIN);
            if (backgroundTaskSet != null) {
                hashSet2.addAll(backgroundTaskSet);
            }
            if (mainThreadTaskSet != null) {
                hashSet2.addAll(mainThreadTaskSet);
            }
        }
        linkedList.add(Barriers.createHomeReadyInitBarrierTask(hashSet2));
        HashSet hashSet3 = new HashSet();
        hashSet3.add(Barriers.mHomeReadyInit2HomeIdleInit);
        Map<THREAD, Set<String>> map3 = this.mStageTaskMap.get(STAGE.HomeIdleInit);
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
        linkedList.add(Barriers.createHomeIdleInitBarrierTask(hashSet3));
        return linkedList;
    }

    //获取App init阶段 且不是在主线程中运行的任务
    private List<HotRocketTask> getAppInitAndNotMainThreadTask(List<HotRocketTask> list) {
        LinkedList linkedList = new LinkedList();
        for (HotRocketTask hotRocketTask : list) {
            if (!(STAGE.AppInit.equals(hotRocketTask.getStage()) && THREAD.MAIN.equals(hotRocketTask.getThread()))) {
                break;
            }
            linkedList.add(hotRocketTask);
            add2StageTaskMap(hotRocketTask.getStage(), hotRocketTask.getThread(), hotRocketTask.getTaskName());
        }
        return linkedList;
    }

    //获取AppInit阶段 且在Background中运行的任务
    private List<Task> getAppInitAndBackgroundTask(Application application, List<HotRocketTask> list) {
        LinkedList linkedList = new LinkedList();
        for (HotRocketTask hotRocketTask : list) {
            if (STAGE.AppInit.equals(hotRocketTask.getStage()) && THREAD.BACKGROUND.equals(hotRocketTask.getThread())) {
                linkedList.add(buildTask(application, hotRocketTask, null));
                add2StageTaskMap(hotRocketTask.getStage(), hotRocketTask.getThread(), hotRocketTask.getTaskName());
            }
        }
        return linkedList;
    }

    //获取在HomeReadyInit阶段的任务
    private List<Task> getHomeReadyInitTask(Application application, List<HotRocketTask> list) {
        LinkedList linkedList = new LinkedList();
        for (HotRocketTask hotRocketTask : list) {
            if (STAGE.HomeReadyInit.equals(hotRocketTask.getStage())) {
                linkedList.add(buildTask(application, hotRocketTask, Barriers.mAppInit2HomeReadyInit));
                add2StageTaskMap(hotRocketTask.getStage(), hotRocketTask.getThread(), hotRocketTask.getTaskName());
            }
        }
        return linkedList;
    }

    //获取在HomeIdleInit阶段的任务
    private List<Task> getHomeIdleInitTask(Application application, List<HotRocketTask> list) {
        LinkedList linkedList = new LinkedList();
        for (HotRocketTask hotRocketTask : list) {
            if (STAGE.HomeIdleInit.equals(hotRocketTask.getStage())) {
                linkedList.add(buildTask(application, hotRocketTask, Barriers.mHomeReadyInit2HomeIdleInit));
                add2StageTaskMap(hotRocketTask.getStage(), hotRocketTask.getThread(), hotRocketTask.getTaskName());
            }
        }
        return linkedList;
    }

    //获取在UserIdleInit阶段运行的任务
    private List<Task> getUserIdleInitTask(Application application, List<HotRocketTask> list) {
        LinkedList linkedList = new LinkedList();
        for (HotRocketTask hotRocketTask : list) {
            if (STAGE.UserIdleInit.equals(hotRocketTask.getStage())) {
                linkedList.add(buildTask(application, hotRocketTask, Barriers.mHomeIdleInit2UserIdleInit));
                add2StageTaskMap(hotRocketTask.getStage(), hotRocketTask.getThread(), hotRocketTask.getTaskName());
            }
        }
        return linkedList;
    }

    private String getHotRocketTaskName2String(Collection<HotRocketTask> collection) {
        StringBuilder sb = new StringBuilder();
        for (HotRocketTask hotRocketTask : collection) {
            sb.append(hotRocketTask.getTaskName());
            sb.append(", ");
        }
        return sb.toString();
    }

    private static String getTaskName2String(Collection<Task> collection) {
        StringBuilder sb = new StringBuilder();
        for (Task cVar : collection) {
            sb.append(cVar.name);
            sb.append(", ");
        }
        return sb.toString();
    }

    private void add2StageTaskMap(STAGE stage, THREAD thread, String str) {
        Map<THREAD, Set<String>> map = this.mStageTaskMap.get(stage);
        if (map == null) {
            map = new HashMap<>();
            this.mStageTaskMap.put(stage, map);
        }
        Set<String> set = map.get(thread);
        if (set == null) {
            set = new HashSet<>();
            map.put(thread, set);
        }
        set.add(str);
    }

    HotRocketPreload() {
    }

    private Rocket buildRocket(Application application, HotRocketConfig pddRocketConfig, List<HotRocketTask> list, PROCESS process) {
        List<Task> appInitAndBackgroundTasks = getAppInitAndBackgroundTask(application, list);
        String TAG = HotRocket.TAG;
        Logger.i(TAG, "进程[" + process + "]的[" + STAGE.AppInit + "/" + THREAD.BACKGROUND + "]任务列表(" + appInitAndBackgroundTasks.size() + "): " + getTaskName2String(appInitAndBackgroundTasks));
        LinkedList linkedList = new LinkedList(appInitAndBackgroundTasks);
        List<Task> homeReadyInitTasks = getHomeReadyInitTask(application, list);
        Logger.i(TAG, "进程[" + process + "]的[" + STAGE.HomeReadyInit + "]任务列表(" + homeReadyInitTasks.size() + "): " + getTaskName2String(homeReadyInitTasks));
        linkedList.addAll(homeReadyInitTasks);
        List<Task> homeIdleInitTasks = getHomeIdleInitTask(application, list);
        Logger.i(TAG, "进程[" + process + "]的[" + STAGE.HomeIdleInit + "]任务列表(" + homeIdleInitTasks.size() + "): " + getTaskName2String(homeIdleInitTasks));
        linkedList.addAll(homeIdleInitTasks);
        List<Task> userIdleInitTasks = getUserIdleInitTask(application, list);
        Logger.i(TAG, "进程[" + process + "]的[" + STAGE.UserIdleInit + "]任务列表(" + userIdleInitTasks.size() + "): " + getTaskName2String(userIdleInitTasks));
        linkedList.addAll(userIdleInitTasks);
        linkedList.addAll(getBarrierTask());
        if (linkedList.isEmpty()) {
            return null;
        }
        Rocket rocket = Rocket.build(new Rocket.Config()
                .setProcessName(process.getName())
                .setLogger(str -> Logger.d(HotRocket.TAG, str))
                .setThreadPoolSize(pddRocketConfig.thread_pool_size)
                .setTaskList(linkedList));

        if (pddRocketConfig.rocket_main_thread_check) {
            new MainThreadBusyStateControllerPlugin().openCheckMainThreadBusy(rocket, Long.valueOf(pddRocketConfig.rocket_busy_threshold));
        }
        return rocket;
    }

    private List<HotRocketTask> createHotRocketTasks(PROCESS process, HotRocketConfig hotRocketConfig) {
        List<HotRocketTask> hotRocketTasks = HotRocketTaskFactory.createHotRocketTasks(process, hotRocketConfig);
        Logger.i(HotRocket.TAG, "进程[" + process + "]的启动任务列表(" + hotRocketTasks.size() + "): " + getHotRocketTaskName2String(hotRocketTasks));
        return hotRocketTasks;
    }

    private Task buildTask(final Application application, final HotRocketTask hotRocketTask, String barrierName) {
        if (!TextUtils.isEmpty(barrierName)) {
            hotRocketTask.getBarriersSet().add(barrierName);
        }
        return new Task(hotRocketTask.getTaskName(), hotRocketTask.getPriority().priority, hotRocketTask.getBarriersSet()) {

            @Override
            public void runTask() {
                super.runTask();
                if (THREAD.MAIN.equals(hotRocketTask.getThread())) {
                    // 主线程里边的任务
                    ThreadUtil.executeRunnable(new Runnable() {
                        public void run() {
                            hotRocketTask.run(application);
                        }
                    });
                } else {
                    hotRocketTask.run(application);
                }
            }
        };
    }


    public void preload(HotRocketConfig hotRocketConfig) {
        if (ContextHolder.application==null){
            Logger.e("HotRocket","!!!!!!-------------please init Rocket by HotRocketInit.preload(Application)-----------------!!!!!!");
            return;
        }
        PROCESS process = HotRocketProcessInstance.getPROCESS(ContextHolder.application, hotRocketConfig.processFullName);
        if (process != null) {
            List<HotRocketTask> hotRocketTaskList = createHotRocketTasks(process, hotRocketConfig);
            this.mRocketTaskList = getAppInitAndNotMainThreadTask(hotRocketTaskList);
            String str = HotRocket.TAG;
            Logger.i(str, "进程[" + process + "]的[" + STAGE.AppInit + "/" + THREAD.MAIN + "]任务列表(" + this.mRocketTaskList.size() + "): " + getHotRocketTaskName2String(this.mRocketTaskList));
            this.mRocket = buildRocket(ContextHolder.application, hotRocketConfig, hotRocketTaskList, process);
        }
    }

    public List<HotRocketTask> getPreloadRocketTasks() {
        return this.mRocketTaskList;
    }

    public Rocket getRocket() {
        return this.mRocket;
    }
}
