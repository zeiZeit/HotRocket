package com.zeit.hotrocket.rocket;

import com.zeit.hotrocket.rocket.core.Task;
import com.zeit.hotrocket.rocket.core.Rocket;
import com.zeit.hotrocket.rocket.core.TaskQueue;
import com.zeit.hotrocket.logger.Logger;
import com.zeit.hotrocket.rocket.barrier.BarrierTask;
import com.zeit.hotrocket.rocket.barrier.Barriers;
import com.zeit.hotrocket.rocket.init.ContextHolder;

import java.util.List;

public class HotRocket {

    public static String TAG = "HotRocket";

    private static final HotRocketPreload ROCKET_PRELOAD = new HotRocketPreload();

    private static boolean hasPreload = false;

    private static boolean hasDoneHomeReadyTask = false;

    private static final Object mHomeReadyLock = new Object();

    private static boolean hasDoneHomeIdleTask = false;

    private static final Object mHomeIdleLock = new Object();

    private static boolean hasDoneUserIdleTask = false;

    private static final Object mUserIdleLock = new Object();

    private static HotRocketListener hotRocketListener;

    private static HotRocketTaskListener hotRocketTaskListener;

    /**
     * 预加载
     * @param config
     */
    public static void preload(HotRocketConfig config) {
        String str = TAG;
        Logger.i(str, "Rocket预加载开始 >>> " + config.processFullName);
        if (checkHasPreload(config, false)) {
            Logger.i(TAG, "Rocket已经预加载, 跳过本次预加载.");
        } else {
            Logger.i(TAG, "Rocket预加载完成.");
        }
    }

    /**
     * 火箭发射
     * @param config
     */
    public static void launch(HotRocketConfig config) {
        String str = TAG;
        Logger.i(str, "Rocket初始化 >>> " + config.processFullName);
        onInit();
        if (checkHasPreload(config, true)) {
            Logger.i(TAG, "Rocket已经预加载, 跳过本次创建.");
        } else {
            Logger.i(TAG, "Rocket创建完成.");
        }
        Logger.i(TAG, "Rocket开始执行.");
        onStart();
        runTask();
        Rocket rocket = getRocket();
        Logger.i(TAG, "Rocket执行同步部分完成.");
        if (rocket == null) {
            Logger.i(TAG, "Rocket全部执行完成(没有异步任务).");
            onFinish();
            return;
        }
        rocket.addTaskQueueListener(new TaskQueue.RocketListener() {
            @Override
            public void onRocketStop(Rocket rocket, List<Task> list) {
                rocket.removeTaskQueueListener(this);
                Logger.i(HotRocket.TAG, "Rocket全部执行完成.");
                HotRocket.onFinish();
            }
        });
    }

    public static void onHomeReady() {
        synchronized (mHomeReadyLock) {
            if (!hasDoneHomeReadyTask) {
                Logger.i(TAG, ">>>>>>>>>>>>>>>>> %s <<<<<<<<<<<<<<<<<", "onHomeReady");
                BarrierTask barrierTask = Barriers.mApp2HomeReadyTask;
                if (barrierTask != null) {
                    barrierTask.unLockBarrier();
                }
                hasDoneHomeReadyTask = true;
            }
        }
    }

    public static void onHomeIdle() {
        synchronized (mHomeIdleLock) {
            if (!hasDoneHomeIdleTask) {
                Logger.i(TAG, ">>>>>>>>>>>>>>>>> %s <<<<<<<<<<<<<<<<<", "onHomeIdle");
                BarrierTask barrierTask = Barriers.mHomeReady2HomeIdleTask;
                if (barrierTask != null) {
                    barrierTask.unLockBarrier();
                }
                hasDoneHomeIdleTask = true;
            }
        }
    }

    public static void onUserIdle() {
        synchronized (mUserIdleLock) {
            if (!hasDoneUserIdleTask) {
                Logger.i(TAG, ">>>>>>>>>>>>>>>>> %s <<<<<<<<<<<<<<<<<", "onUserIdle");
                BarrierTask barrierTask = Barriers.mHomeIdle2UserIdleTask;
                if (barrierTask != null) {
                    barrierTask.unLockBarrier();
                }
                hasDoneUserIdleTask = true;
            }
        }
    }

    public static void onFinish() {
        HotRocketListener listener = hotRocketListener;
        if (listener != null) {
            listener.onHotRocketFinish();
        }
    }

    public static void onTaskStart(String str) {
        HotRocketTaskListener rocketTaskListener = hotRocketTaskListener;
        if (rocketTaskListener != null) {
            rocketTaskListener.onTaskStart(str);
        }
    }

    public static void onTaskFinish(String str) {
        HotRocketTaskListener iVar = hotRocketTaskListener;
        if (iVar != null) {
            iVar.onTaskComplete(str);
        }
    }

    private static boolean checkHasPreload(HotRocketConfig hotRocketConfig, boolean z) {
        synchronized (HotRocket.class) {
            if (hasPreload) {
                return true;
            }
            ROCKET_PRELOAD.preload(hotRocketConfig);
            hasPreload = true;
            return false;
        }
    }

    private static void runTask() {
        List<HotRocketTask> rocketTasks = ROCKET_PRELOAD.getPreloadRocketTasks();
        if (rocketTasks != null) {
            for (HotRocketTask hotRocketTask : rocketTasks) {
                onTaskStart(hotRocketTask.getTaskName());
                if (ContextHolder.application!=null){
                    hotRocketTask.run(ContextHolder.application);
                }else {
                    Logger.e("HotRocket","!!!!!!-------------please init Rocket by HotRocketInit.preload(Application)-----------------!!!!!!");
                }
                onTaskFinish(hotRocketTask.getTaskName());
            }
        }
    }

    private static Rocket getRocket() {
        Rocket rocket = ROCKET_PRELOAD.getRocket();
        if (rocket != null) {
            rocket.addTaskListener(new Task.TaskListener() {

                @Override
                public void onTaskStart(Task task) {
                    if (!(task instanceof BarrierTask)) {
                        HotRocket.onTaskStart(task.name);
                    }
                }

                @Override
                public void onTaskComplete(Task task) {
                    if (!(task instanceof BarrierTask)) {
                        HotRocket.onTaskFinish(task.name);
                    }
                }
            });
            rocket.launch();
        }
        return rocket;
    }

    private static void onInit() {
        HotRocketListener listener = hotRocketListener;
        if (listener != null) {
            listener.onHotRocketInit();
        }
    }

    private static void onStart() {
        HotRocketListener listener = HotRocket.hotRocketListener;
        if (listener != null) {
            listener.onHotRocketStart();
        }
    }

    public static void setHotRocketListener(HotRocketListener listener) {
        hotRocketListener = listener;
    }

    public static void setHotRocketTaskListener(HotRocketTaskListener hotRocketTaskListener) {
        HotRocket.hotRocketTaskListener = hotRocketTaskListener;
    }
}
