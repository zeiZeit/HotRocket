package com.zeit.hotrocket.rocket;


import com.zeit.hotrocket.appinit.annotations.ISwitcher;
import com.zeit.hotrocket.appinit.annotations.InitTask;
import com.zeit.hotrocket.appinit.annotations.PRIORITY;
import com.zeit.hotrocket.appinit.annotations.STAGE;
import com.zeit.hotrocket.appinit.annotations.THREAD;

import java.util.Set;

public interface HotRocketTask extends ISwitcher, InitTask {
    String getTaskName();

    Set<String> getBarriersSet();

    PRIORITY getPriority();

    THREAD getThread();

    STAGE getStage();
}
