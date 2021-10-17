package com.zeit.hotrocket;

import android.text.TextUtils;

/* renamed from: com.xunmeng.pinduoduo.rocket.b */
public class PddRocketConfig {

    /* renamed from: a */
    public String f39771a;

    /* renamed from: b */
    public boolean f39772b;

    /* renamed from: c */
    public long f39773c;

    /* renamed from: d */
    public int f39774d;

    /* renamed from: e */
    public PddRocketTaskFactoryInterceptor f39775e;

    /* renamed from: com.xunmeng.pinduoduo.rocket.b$a */
    /* compiled from: PddRocketConfig */
    public static final class C9053a {

        /* renamed from: g */
        private String f39776g;

        /* renamed from: h */
        private boolean f39777h = true;

        /* renamed from: i */
        private long f39778i = 50;

        /* renamed from: j */
        private int f39779j = 2;

        /* renamed from: k */
        private PddRocketTaskFactoryInterceptor f39780k;

        /* renamed from: a */
        public static C9053a m52403a() {
            return new C9053a();
        }

        /* renamed from: f */
        public PddRocketConfig mo44827f() {
            PddRocketConfig bVar = new PddRocketConfig();
            if (!TextUtils.isEmpty(this.f39776g)) {
                bVar.f39771a = this.f39776g;
                bVar.f39772b = this.f39777h;
                bVar.f39774d = this.f39779j;
                bVar.f39773c = this.f39778i;
                bVar.f39775e = this.f39780k;
                return bVar;
            }
            throw new IllegalStateException("PddRocketConfig TextUtils.isEmpty(this.mProcessName)");
        }

        private C9053a() {
        }

        /* renamed from: b */
        public C9053a mo44823b(String str) {
            this.f39776g = str;
            return this;
        }

        /* renamed from: c */
        public C9053a mo44824c(boolean z) {
            this.f39777h = z;
            return this;
        }

        /* renamed from: d */
        public C9053a mo44825d(long j) {
            this.f39778i = j;
            return this;
        }

        /* renamed from: e */
        public C9053a mo44826e(int i) {
            this.f39779j = i;
            return this;
        }
    }

    private PddRocketConfig() {
    }
}
