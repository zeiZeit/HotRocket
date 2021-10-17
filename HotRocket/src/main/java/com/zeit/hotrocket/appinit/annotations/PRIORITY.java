package com.zeit.hotrocket.appinit.annotations;

public enum PRIORITY {
    UNKNOWN(0),
    DEFAULT(0),
    P1(1),
    P2(2),
    P3(3),
    P4(4),
    MAX(5),
    P_1(-1),
    P_2(-2),
    P_3(-3),
    P_4(-4),
    MIN(-5);
    
    public final int priority;

    private PRIORITY(int i) {
        this.priority = i;
    }
}
