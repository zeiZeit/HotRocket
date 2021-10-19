package com.zeit.hotrocket.appstartup.components.startupcomplete;

public enum StartupStage {
    DEFAULT(0),
    COMPLETE(1),
    IDLE(2),
    USER_IDLE(3);
    
    private final int order;

    public boolean isAfterComplete() {
        if (this.order >= COMPLETE.order) {
            return true;
        }
        return false;
    }

    public boolean isAfterIdle() {
        if (this.order >= IDLE.order) {
            return true;
        }
        return false;
    }

    public boolean isAfterUserIdle() {
        if (this.order >= USER_IDLE.order) {
            return true;
        }
        return false;
    }

    private StartupStage(int i) {
        this.order = i;
    }
}
