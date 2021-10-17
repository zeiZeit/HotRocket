package com.zeit.hotrocket;

import android.content.Context;
import android.text.TextUtils;

import com.zeit.hotrocket.appinit.annotations.ISwitcher;
import com.zeit.hotrocket.appinit.annotations.InitTask;
import com.zeit.hotrocket.appinit.annotations.PRIORITY;
import com.zeit.hotrocket.appinit.annotations.PROCESS;
import com.zeit.hotrocket.appinit.annotations.STAGE;
import com.zeit.hotrocket.appinit.annotations.THREAD;
import com.zeit.hotrocket.logger.Logger;

import java.util.List;
import java.util.Set;

public class PddRocketStaticTask implements PddRocketTask {

    /* renamed from: a */
    public String f39744a;

    /* renamed from: b */
    public Set<String> f39745b;

    /* renamed from: c */
    public PRIORITY f39746c;

    /* renamed from: d */
    public List<PROCESS> f39747d;

    /* renamed from: e */
    public STAGE f39748e;

    /* renamed from: f */
    public THREAD f39749f;

    /* renamed from: g */
    public InitTask f39750g;

    /* renamed from: h */
    public String f39751h;

    /* renamed from: n */
    private Object f39752n;

    /* renamed from: o */
    private Object m52368o() {
        InitTask bVar = this.f39750g;
        if (bVar != null) {
            return bVar;
        }
        if (this.f39752n == null) {
            try {
                if (!TextUtils.isEmpty(this.f39751h)) {
                    this.f39752n = Class.forName(this.f39751h).newInstance();
                }
            } catch (Throwable th) {
                Logger.e(PddRocket.TAG, th);
            }
        }
        if (this.f39752n == null) {
            Logger.e(PddRocket.TAG, "Task [%s] must has 'runnable' or 'runnableTaskClass'", this.f39744a);
        }
        return this.f39752n;
    }

    public PddRocketStaticTask(String str, Set<String> set, PRIORITY priority, List<PROCESS> list, STAGE stage, THREAD thread, InitTask bVar) {
        this.f39744a = str;
        this.f39745b = set;
        this.f39746c = priority;
        this.f39747d = list;
        this.f39748e = stage;
        this.f39749f = thread;
        this.f39750g = bVar;
    }

    public PddRocketStaticTask(String str, Set<String> set, PRIORITY priority, List<PROCESS> list, STAGE stage, THREAD thread, String str2) {
        this.f39744a = str;
        this.f39745b = set;
        this.f39746c = priority;
        this.f39747d = list;
        this.f39748e = stage;
        this.f39749f = thread;
        this.f39751h = str2;
    }

    @Override // com.xunmeng.pinduoduo.appinit.annotations.ISwitcher
    /* renamed from: on */
    public boolean isOpen() {
        Object o = m52368o();
        if (o == null) {
            return false;
        }
        if (!(o instanceof ISwitcher)) {
            return true;
        }
        return ((ISwitcher) o).isOpen();
    }

    @Override // com.xunmeng.pinduoduo.appinit.annotations.InitTask
    public void run(Context context) {
        if (!isOpen()) {
            Logger.i(PddRocket.TAG, "Task [%s] switcher is off, did not run.", this.f39744a);
            PddRocketSwitcherRecord.m52504a(this.f39744a, false);
            return;
        }
        Object o = m52368o();
        if (!(o instanceof InitTask)) {
            Logger.e(PddRocket.TAG, "Task [%s] must be instanceof 'InitTask'", this.f39744a);
            return;
        }
        Logger.i(PddRocket.TAG, "Task [%s] is running...", this.f39744a);
        PddRocketSwitcherRecord.m52504a(this.f39744a, true);
        ((InitTask) o).run(context);
    }

    @Override // com.xunmeng.pinduoduo.rocket.PddRocketTask
    /* renamed from: i */
    public String mo44813i() {
        return this.f39744a;
    }

    @Override // com.xunmeng.pinduoduo.rocket.PddRocketTask
    /* renamed from: j */
    public Set<String> mo44814j() {
        return this.f39745b;
    }

    @Override // com.xunmeng.pinduoduo.rocket.PddRocketTask
    /* renamed from: k */
    public PRIORITY mo44815k() {
        return this.f39746c;
    }

    @Override // com.xunmeng.pinduoduo.rocket.PddRocketTask
    /* renamed from: l */
    public THREAD mo44816l() {
        return this.f39749f;
    }

    @Override // com.xunmeng.pinduoduo.rocket.PddRocketTask
    /* renamed from: m */
    public STAGE mo44817m() {
        return this.f39748e;
    }
}
