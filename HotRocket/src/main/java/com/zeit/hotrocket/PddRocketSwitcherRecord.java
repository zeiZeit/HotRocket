package com.zeit.hotrocket;

import java.util.LinkedHashSet;
import java.util.Set;

/* renamed from: com.xunmeng.pinduoduo.rocket.f */
public class PddRocketSwitcherRecord {

    /* renamed from: d */
    private static final Object f39852d = new Object();

    /* renamed from: e */
    private static final Object f39853e = new Object();

    /* renamed from: f */
    private static Set<String> f39854f;

    /* renamed from: g */
    private static Set<String> f39855g;

    /* renamed from: a */
    public static void m52504a(String str, boolean z) {
        if (z) {
            synchronized (f39852d) {
                if (f39854f == null) {
                    f39854f = new LinkedHashSet();
                }
                f39854f.add(str);
            }
            return;
        }
        synchronized (f39853e) {
            if (f39855g == null) {
                f39855g = new LinkedHashSet();
            }
            f39855g.add(str);
        }
    }

    /* renamed from: b */
    public static Set<String> m52505b() {
        LinkedHashSet linkedHashSet;
        synchronized (f39852d) {
            if (f39854f != null) {
                linkedHashSet = new LinkedHashSet(f39854f);
            } else {
                linkedHashSet = null;
            }
        }
        if (linkedHashSet == null) {
            return new LinkedHashSet(0);
        }
        return linkedHashSet;
    }

    /* renamed from: c */
    public static Set<String> m52506c() {
        LinkedHashSet linkedHashSet;
        synchronized (f39853e) {
            if (f39855g != null) {
                linkedHashSet = new LinkedHashSet(f39855g);
            } else {
                linkedHashSet = null;
            }
        }
        if (linkedHashSet == null) {
            return new LinkedHashSet(0);
        }
        return linkedHashSet;
    }
}
