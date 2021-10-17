package com.zeit.hotrocket;


import com.zeit.hotrocket.appinit.annotations.ISwitcher;
import com.zeit.hotrocket.appinit.annotations.InitTask;
import com.zeit.hotrocket.appinit.annotations.PRIORITY;
import com.zeit.hotrocket.appinit.annotations.STAGE;
import com.zeit.hotrocket.appinit.annotations.THREAD;

import java.util.Set;

public interface PddRocketTask extends ISwitcher, InitTask {
    /* renamed from: i */
    String mo44813i();

    /* renamed from: j */
    Set<String> mo44814j();

    /* renamed from: k */
    PRIORITY mo44815k();

    /* renamed from: l */
    THREAD mo44816l();

    /* renamed from: m */
    STAGE mo44817m();
}
