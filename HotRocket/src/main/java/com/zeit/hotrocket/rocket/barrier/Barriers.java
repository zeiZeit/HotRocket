package com.zeit.hotrocket.rocket.barrier;



import com.zeit.hotrocket.appinit.annotations.STAGE;
import com.zeit.hotrocket.rocket.core.utils.Constants;

import java.util.Set;


public class Barriers {

    public static final String mAppInit2HomeReadyInit = ("Barrier-" + STAGE.AppInit.name() + Constants.ACCEPT_TIME_SEPARATOR_SERVER + STAGE.HomeReadyInit);

    public static final String mHomeReadyInit2HomeIdleInit = ("Barrier-" + STAGE.HomeReadyInit.name() + Constants.ACCEPT_TIME_SEPARATOR_SERVER + STAGE.HomeIdleInit);

    public static final String mHomeIdleInit2UserIdleInit = ("Barrier-" + STAGE.HomeIdleInit.name() + Constants.ACCEPT_TIME_SEPARATOR_SERVER + STAGE.UserIdleInit);

    public static BarrierTask mApp2HomeReadyTask = null;

    public static BarrierTask mHomeReady2HomeIdleTask = null;

    public static BarrierTask mHomeIdle2UserIdleTask = null;

    public static BarrierTask createAppInitBarrierTask(Set<String> set) {
        BarrierTask barrierTask = new BarrierTask(mAppInit2HomeReadyInit, set);
        mApp2HomeReadyTask = barrierTask;
        return barrierTask;
    }

    public static BarrierTask createHomeReadyInitBarrierTask(Set<String> set) {
        BarrierTask barrierTask = new BarrierTask(mHomeReadyInit2HomeIdleInit, set);
        mHomeReady2HomeIdleTask = barrierTask;
        return barrierTask;
    }

    public static BarrierTask createHomeIdleInitBarrierTask(Set<String> set) {
        BarrierTask barrierTask = new BarrierTask(mHomeIdleInit2UserIdleInit, set);
        mHomeIdle2UserIdleTask = barrierTask;
        return barrierTask;
    }
}
