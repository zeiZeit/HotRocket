package com.zeit.hotrocket.core;

import android.os.Process;

import com.zeit.hotrocket.core.utils.Log4Rocket;
import com.zeit.hotrocket.core.utils.ThrowableUtil;


public class TaskDispatcher extends Thread {

    private TaskQueue mTaskQueue;

    private Rocket mRocket;

    private volatile boolean mIsInterrupt = false;

    public void interruptDispatcher() {
        this.mIsInterrupt = true;
        interrupt();
    }

    public void run() {
        String status;
        Process.setThreadPriority(10);
        while (true) {
            try {
                this.mRocket.mLog4Rocket.log("[Rocket分发器][%s] Taking下一个任务...", getName());
                Task task = this.mTaskQueue.getTaskFromQueue();
                if (task.isReady) {
                    this.mRocket.mLog4Rocket.log("[Rocket分发器][%s] 任务：[%s] 需要立即执行", getName(), task.name);
                } else {
                    Log4Rocket log4Rocket = this.mRocket.mLog4Rocket;
                    String threadName = getName();
                    String taskName = task.name;
                    if (this.mRocket.mRocketLock.isLocked()) {
                        status = "Paused";
                    } else {
                        status = "Resumed";
                    }
                    log4Rocket.log("[Rocket分发器][%s] 任务：[%s] 等待执行，当前rocket状态：%s", threadName, taskName, status);
                    this.mRocket.mRocketLock.letWait();
                }
                this.mRocket.mLog4Rocket.log("[Rocket分发器][%s] 任务 [%s] 进入执行状态.", getName(), task.name);
                TaskCompleteEmitterImpl taskCompleteEmitter = new TaskCompleteEmitterImpl();
                task.startRun(taskCompleteEmitter);
                taskCompleteEmitter.waitTaskComplete();
                this.mRocket.mLog4Rocket.log("[Rocket分发器][%s] 任务 [%s] 进入完成状态", getName(), task.name);
                this.mTaskQueue.completeTask(task);
                this.mTaskQueue.organizeQueue(task);
                this.mTaskQueue.checkHasNextTask();
            } catch (InterruptedException e2) {
                if (this.mIsInterrupt) {
                    this.mRocket.mLog4Rocket.log("[Rocket分发器][%s] 退出.", getName());
                    return;
                }
                this.mRocket.mLog4Rocket.log("[Rocket分发器][%s] 不退出，但是发生了阻断异常:%s", getName(), ThrowableUtil.formatThrowable(e2));
            }
        }
    }

    TaskDispatcher(Rocket rocket, TaskQueue taskQueue) {
        this.mRocket = rocket;
        this.mTaskQueue = taskQueue;
    }
}
