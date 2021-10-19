package com.zeit.hotrocket.rocket.core;


import com.zeit.hotrocket.rocket.core.utils.Constants;
import com.zeit.hotrocket.rocket.core.utils.ObjectsUtil;

import java.util.HashSet;
import java.util.Set;

public class Task {

    private Set<String> mTaskSet1;

    private Set<String> mDependence;

    private final Object mLock1;

    public Rocket mRocket;

    public String name;

    public int mPriority1;

    public int mPriority2;

    public Thread mThread;

    public volatile boolean isReady;

    public volatile TaskRunStatus mStatus;

    private final Object mLock2;
    
    public interface TaskListener {
        void onTaskStart(Task cVar);
        
        void onTaskComplete(Task cVar);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Task cVar = (Task) obj;
        return this.mPriority1 == cVar.mPriority1 && ObjectsUtil.equals(this.mRocket, cVar.mRocket) && ObjectsUtil.equals(this.name, cVar.name) && ObjectsUtil.equals(this.mTaskSet1, cVar.mTaskSet1) && ObjectsUtil.equals(this.mThread, cVar.mThread) && this.mStatus == cVar.mStatus;
    }

    public Task(String name, int priority, Set<String> set) {
        this(name, priority, set, TaskRunStatus.WAITING);
    }

    public int hashCode() {
        return ObjectsUtil.hashCode(this.mRocket, this.name, Integer.valueOf(this.mPriority1), Integer.valueOf(this.mPriority2), this.mTaskSet1, this.mDependence, this.mThread, this.mStatus);
    }


    public boolean removeDependence(String name) {
        boolean remove;
        synchronized (this.mLock2) {
            remove = this.mDependence.remove(name);
        }
        return remove;
    }

    public String toString() {
        return this.name + Constants.COLON_SEPARATOR + this.mPriority2;
    }

    private Task(String name, int priority, Set<String> set, TaskRunStatus taskRunStatus) {
        this.mLock1 = new Object();
        this.mLock2 = new Object();
        this.name = name;
        this.mPriority1 = priority;
        this.mPriority2 = priority;
        this.mTaskSet1 = new HashSet(set);
        this.mDependence = new HashSet(set);
        this.mStatus = taskRunStatus;
    }

    public void startRun(TaskCompleteEmitter taskCompleteEmitter) {
        this.mThread = Thread.currentThread();
        runTask();
        taskCompleteEmitter.onTaskComplete();
        this.mThread = null;
    }

    protected void runTask() {
    }

    
    public Set<String> getDependence() {
        return this.mDependence;
    }
}
