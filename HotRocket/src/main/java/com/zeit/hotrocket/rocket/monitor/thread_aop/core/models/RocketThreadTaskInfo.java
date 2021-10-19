package com.zeit.hotrocket.rocket.monitor.thread_aop.core.models;

import java.io.Serializable;

public class RocketThreadTaskInfo implements Serializable {
    private Object taskObj;

    public RocketThreadTaskInfo(Object obj) {
        this.taskObj = obj;
    }

    public void setTaskObj(Object obj) {
        this.taskObj = obj;
    }

    public Object getTaskObj() {
        return this.taskObj;
    }
}
