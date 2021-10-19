package com.zeit.hotrocket.rocket;

import android.content.Context;
import android.text.TextUtils;

import com.zeit.hotrocket.appinit.annotations.ISwitcher;
import com.zeit.hotrocket.appinit.annotations.InitTask;
import com.zeit.hotrocket.appinit.annotations.PRIORITY;
import com.zeit.hotrocket.appinit.annotations.PROCESS;
import com.zeit.hotrocket.appinit.annotations.STAGE;
import com.zeit.hotrocket.appinit.annotations.THREAD;
import com.zeit.hotrocket.logger.Logger;

import java.util.List;
import java.util.Set;

public class HotRocketStaticTask implements HotRocketTask {

    public String mName;

    public Set<String> mBarrierSet;

    public PRIORITY mPriority;

    public List<PROCESS> mProcessLs;

    public STAGE mStage;

    public THREAD mThread;

    public InitTask mInitTask;

    public String mTaskClazz;

    private Object mTask;

    private Object getRocketTask() {
        InitTask bVar = this.mInitTask;
        if (bVar != null) {
            return bVar;
        }
        if (this.mTask == null) {
            try {
                if (!TextUtils.isEmpty(this.mTaskClazz)) {
                    this.mTask = Class.forName(this.mTaskClazz).newInstance();
                }
            } catch (Throwable th) {
                Logger.e(HotRocket.TAG, th);
            }
        }
        if (this.mTask == null) {
            Logger.e(HotRocket.TAG, "Task [%s] must has 'runnable' or 'runnableTaskClass'", this.mName);
        }
        return this.mTask;
    }

    public HotRocketStaticTask(String str, Set<String> set, PRIORITY priority, List<PROCESS> list, STAGE stage, THREAD thread, InitTask bVar) {
        this.mName = str;
        this.mBarrierSet = set;
        this.mPriority = priority;
        this.mProcessLs = list;
        this.mStage = stage;
        this.mThread = thread;
        this.mInitTask = bVar;
    }

    public HotRocketStaticTask(String str, Set<String> set, PRIORITY priority, List<PROCESS> list, STAGE stage, THREAD thread, String str2) {
        this.mName = str;
        this.mBarrierSet = set;
        this.mPriority = priority;
        this.mProcessLs = list;
        this.mStage = stage;
        this.mThread = thread;
        this.mTaskClazz = str2;
    }

    @Override
    public boolean isOpen() {
        Object rocketTask = getRocketTask();
        if (rocketTask == null) {
            return false;
        }
        if (!(rocketTask instanceof ISwitcher)) {
            return true;
        }
        return ((ISwitcher) rocketTask).isOpen();
    }

    @Override
    public void run(Context context) {
        if (!isOpen()) {
            Logger.i(HotRocket.TAG, "Task [%s] switcher is off, did not run.", this.mName);
//            HotRocketSwitcherRecord.m52504a(this.mName, false);
            return;
        }
        Object o = getRocketTask();
        if (!(o instanceof InitTask)) {
            Logger.e(HotRocket.TAG, "Task [%s] must be instanceof 'InitTask'", this.mName);
            return;
        }
        Logger.i(HotRocket.TAG, "Task [%s] is running...", this.mName);
//        HotRocketSwitcherRecord.m52504a(this.mName, true);
        ((InitTask) o).run(context);
    }

    @Override
    public String getTaskName() {
        return this.mName;
    }

    @Override
    public Set<String> getBarriersSet() {
        return this.mBarrierSet;
    }

    @Override
    public PRIORITY getPriority() {
        return this.mPriority;
    }

    @Override
    public THREAD getThread() {
        return this.mThread;
    }

    @Override
    public STAGE getStage() {
        return this.mStage;
    }
}
