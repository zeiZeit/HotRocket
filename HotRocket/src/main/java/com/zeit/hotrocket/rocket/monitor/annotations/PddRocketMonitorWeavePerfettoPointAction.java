package com.zeit.hotrocket.rocket.monitor.annotations;

public enum PddRocketMonitorWeavePerfettoPointAction {
    weave_Perfetto_startTrace("weave_Perfetto_startTrace"),
    weave_Perfetto_stopTrace("weave_Perfetto_stopTrace");
    
    private final String name;

    private PddRocketMonitorWeavePerfettoPointAction(String str) {
        this.name = str;
    }

    public String getName() {
        return this.name;
    }
}
