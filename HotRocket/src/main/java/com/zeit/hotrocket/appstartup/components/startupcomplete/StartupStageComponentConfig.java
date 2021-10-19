package com.zeit.hotrocket.appstartup.components.startupcomplete;

import android.app.Application;

public class StartupStageComponentConfig {

    public Application f24321a;

    public boolean f24322b;

    public long f24323c;

    public long f24324d;


    public long f24325e;

    public boolean f24326f;

    public static final class C5377a {

        private Application f24327i;

        private boolean f24328j;

        private long f24329k;

        private long f24330l;

        private long f24331m;

        private boolean f24332n;

        public static C5377a m32798a() {
            return new C5377a();
        }

        public StartupStageComponentConfig mo28085h() {
            StartupStageComponentConfig gVar = new StartupStageComponentConfig();
            gVar.f24321a = this.f24327i;
            gVar.f24323c = this.f24329k;
            gVar.f24324d = this.f24330l;
            gVar.f24325e = this.f24331m;
            gVar.f24326f = this.f24332n;
            gVar.f24322b = this.f24328j;
            return gVar;
        }

        private C5377a() {
        }

        public C5377a mo28079b(Application application) {
            this.f24327i = application;
            return this;
        }

        public C5377a mo28080c(boolean z) {
            this.f24328j = z;
            return this;
        }

        public C5377a mo28081d(long j) {
            this.f24329k = j;
            return this;
        }

        public C5377a mo28082e(long j) {
            this.f24330l = j;
            return this;
        }

        public C5377a mo28083f(long j) {
            this.f24331m = j;
            return this;
        }

        public C5377a mo28084g(boolean z) {
            this.f24332n = z;
            return this;
        }
    }

    public String toString() {
        return "StartupStageComponentConfig{isMainProcess=" + this.f24322b + ", startupCompleteTimeoutMillis=" + this.f24323c + ", startupIdleTimeoutMillis=" + this.f24324d + ", startupUserIdleTimeoutMillis=" + this.f24325e + ", observeHomeRender=" + this.f24326f + '}';
    }
}
