package com.zeit.hotrocket.appstartup.components.startupcomplete;

import android.app.Application;

public class StartupStageComponentConfig {

    public Application application;

    public boolean isMainProcess;

    public long startupCompleteTimeoutMillis;

    public long startupIdleTimeoutMillis;

    public String HomeActivityName;
    public String SplashActivityName;

    public long startupUserIdleTimeoutMillis;

    public boolean observeHomeRender;

    public static final class config {

        private Application application;

        private boolean isMainProcess;

        private long startupCompleteTimeoutMillis;

        private long startupIdleTimeoutMillis;

        private long startupUserIdleTimeoutMillis;

        private boolean observeHomeRender;


        private String HomeActivityName;

        private String SplashActivityName;

        public static config get() {
            return new config();
        }

        public StartupStageComponentConfig build() {
            StartupStageComponentConfig config = new StartupStageComponentConfig();
            config.application = this.application;
            config.startupCompleteTimeoutMillis = this.startupCompleteTimeoutMillis;
            config.startupIdleTimeoutMillis = this.startupIdleTimeoutMillis;
            config.startupUserIdleTimeoutMillis = this.startupUserIdleTimeoutMillis;
            config.observeHomeRender = this.observeHomeRender;
            config.isMainProcess = this.isMainProcess;
            config.HomeActivityName = this.HomeActivityName;
            config.SplashActivityName = this.SplashActivityName;
            return config;
        }

        private config() {
        }

        public config setAppliaction(Application application) {
            this.application = application;
            return this;
        }

        public config setIsMainProcess(boolean z) {
            this.isMainProcess = z;
            return this;
        }

        public config setStartupCompleteTimeoutMillis(long j) {
            this.startupCompleteTimeoutMillis = j;
            return this;
        }

        public config setStartupIdleTimeoutMillis(long j) {
            this.startupIdleTimeoutMillis = j;
            return this;
        }

        public config setStartupUserIdleTimeoutMillis(long j) {
            this.startupUserIdleTimeoutMillis = j;
            return this;
        }

        public config setObserveHomeRender(boolean z) {
            this.observeHomeRender = z;
            return this;
        }

        public String getHomeActivityName() {
            return HomeActivityName;
        }

        public config setHomeActivityName(String homeActivityName) {
            HomeActivityName = homeActivityName;
            return this;
        }

        public String getSplashActivityName() {
            return SplashActivityName;
        }

        public config setSplashActivityName(String splashActivityName) {
            SplashActivityName = splashActivityName;
            return this;
        }
    }

    public String toString() {
        return "StartupStageComponentConfig{isMainProcess=" + this.isMainProcess + ", startupCompleteTimeoutMillis=" + this.startupCompleteTimeoutMillis +
                ", startupIdleTimeoutMillis=" + this.startupIdleTimeoutMillis + ", startupUserIdleTimeoutMillis=" + this.startupUserIdleTimeoutMillis +
                ", observeHomeRender=" + this.observeHomeRender + '}';
    }
}
