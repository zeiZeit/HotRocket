package com.zeit.hotrocket.appinit.annotations;


public enum PROCESS {
    ALL("all"),
    MAIN("main"),
    TITAN("titan"),
    LIFECYCLE("lifecycle"),
    PATCH("patch"),
    SUPPORT("support"),
    MEEPO("meepo"),
    ASSISTS("assists"),
    XG_SERVICE_V4("xg_service_v4");
    
    private final String name;

    public boolean include(PROCESS process) {
        if (this == process || this == ALL) {
            return true;
        }
        return false;
    }

    private PROCESS(String str) {
        this.name = str;
    }

    public String getName() {
        return this.name;
    }
}
