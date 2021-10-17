package com.zeit.hotrocket.core;


import com.zeit.hotrocket.core.utils.StringUtil;
import com.zeit.hotrocket.core.utils.Log4Rocket;

import java.util.List;


public class Rocket {

    public Log4Rocket mLog4Rocket;

    RocketLock mRocketLock;

    private boolean hasLaunched;

    private TaskQueue mTaskQueue;

    private Config mConfig;

    public static class Config {

        public String mProcessName = "Rocket4J";

        public Log4Rocket.Logger mLogger;

        public int mThreadPoolSize;

        private List<Task> mTaskList;

        public boolean isValid() {
            List<Task> list;
            if (StringUtil.isEmpty(this.mProcessName) || this.mThreadPoolSize <= 0 || (list = this.mTaskList) == null || list.isEmpty()) {
                return false;
            }
            return true;
        }

        public String toString() {
            return "Config{mName='" + this.mProcessName + '\'' + ", mLogger=" + this.mLogger + ", mThreadPoolSize=" + this.mThreadPoolSize + ", mTasks=" + this.mTaskList + '}';
        }

        public Config setThreadPoolSize(int i) {
            this.mThreadPoolSize = i;
            return this;
        }

        public Config setTaskList(List<Task> list) {
            this.mTaskList = list;
            return this;
        }

        public Config setProcessName(String name) {
            this.mProcessName = name;
            return this;
        }

        public Config setLogger(Log4Rocket.Logger aVar) {
            this.mLogger = aVar;
            return this;
        }

        public List<Task> getTaskList() {
            return this.mTaskList;
        }
    }

    public Rocket launch() {
        synchronized (this) {
            if (this.hasLaunched) {
                this.mLog4Rocket.log("Rocket has launched before.");
                return this;
            }
            this.mTaskQueue.startDispatchers();
            this.hasLaunched = true;
            return this;
        }
    }


    public static Rocket build(Config aVar) {
        return new Rocket(aVar);
    }


    public void releaseLock() {
        this.mRocketLock.releaseLock();
    }

    public void pause() {
        this.mRocketLock.lock();
    }


    public boolean isPaused() {
        return this.mRocketLock.isLocked();
    }

    public boolean isQueueEmpty() {
        return this.mTaskQueue.isQueueEmpty;
    }

    public Rocket addTaskQueueListener(TaskQueue.TaskQueueListener listener) {
        this.mTaskQueue.addTaskQueueListeners(listener);
        return this;
    }

    public Rocket removeTaskQueueListener(TaskQueue.TaskQueueListener taskQueueListener) {
        this.mTaskQueue.removeTaskQueueListener(taskQueueListener);
        return this;
    }

    public Rocket addTaskListener(Task.TaskListener aVar) {
        this.mTaskQueue.addTaskListener(aVar);
        return this;
    }

    public Rocket removeTaskListener(Task.TaskListener aVar) {
        this.mTaskQueue.removeTaskListener(aVar);
        return this;
    }

    private Rocket(Config aVar) {
        if (aVar == null || !aVar.isValid()) {
            throw new IllegalArgumentException(String.format("Config %s not valid.", aVar));
        }
        this.mConfig = aVar;
        this.hasLaunched = false;
        this.mRocketLock = new RocketLock();
        this.mLog4Rocket = new Log4Rocket(String.format("[%s]", aVar.mProcessName), aVar.mLogger);
        this.mTaskQueue = new TaskQueue(this, aVar);
    }
}
