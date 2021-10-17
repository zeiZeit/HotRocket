package com.zeit.hotrocket.p1087a;



import com.zeit.hotrocket.appinit.annotations.STAGE;
import com.zeit.hotrocket.core.utils.Constants;

import java.util.Set;


public class Barriers {

    public static final String f39765a = ("Barrier-" + STAGE.AppInit.name() + Constants.ACCEPT_TIME_SEPARATOR_SERVER + STAGE.HomeReadyInit);

    public static final String f39766b = ("Barrier-" + STAGE.HomeReadyInit.name() + Constants.ACCEPT_TIME_SEPARATOR_SERVER + STAGE.HomeIdleInit);

    public static final String f39767c = ("Barrier-" + STAGE.HomeIdleInit.name() + Constants.ACCEPT_TIME_SEPARATOR_SERVER + STAGE.UserIdleInit);

    public static BarrierTask f39768d = null;

    public static BarrierTask f39769e = null;

    public static BarrierTask f39770f = null;

    public static BarrierTask m52400g(Set<String> set) {
        BarrierTask aVar = new BarrierTask(f39765a, set);
        f39768d = aVar;
        return aVar;
    }

    public static BarrierTask m52401h(Set<String> set) {
        BarrierTask aVar = new BarrierTask(f39766b, set);
        f39769e = aVar;
        return aVar;
    }

    public static BarrierTask m52402i(Set<String> set) {
        BarrierTask aVar = new BarrierTask(f39767c, set);
        f39770f = aVar;
        return aVar;
    }
}
