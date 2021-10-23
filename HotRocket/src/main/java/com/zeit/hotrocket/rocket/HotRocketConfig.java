package com.zeit.hotrocket.rocket;

import android.text.TextUtils;

import java.util.ArrayList;

public class HotRocketConfig {

    public String processFullName;

    public boolean rocket_main_thread_check;

    public long rocket_busy_threshold;

    public int thread_pool_size;

    public String packageName;

    public ArrayList<HotRocketTask> mTask ;

    public HotRocketTaskFactoryInterceptor factoryInterceptor;

    public static final class Config {

        private String processFullName;

        private String packageName;

        private boolean rocket_main_thread_check = true;

        private long rocket_busy_threshold = 50;

        private int thread_pool_size = 2;

        private HotRocketTaskFactoryInterceptor taskFactoryInterceptor;

        public ArrayList<HotRocketTask> getTask() {
            return task;
        }

        public void setTask(ArrayList<HotRocketTask> task) {
            this.task = task;
        }

        public ArrayList<HotRocketTask> task;

        public static Config getConfig() {
            return new Config();
        }

        public HotRocketConfig build() {
            HotRocketConfig config = new HotRocketConfig();
            if (!TextUtils.isEmpty(this.processFullName)) {
                config.processFullName = this.processFullName;
                config.rocket_main_thread_check = this.rocket_main_thread_check;
                config.thread_pool_size = this.thread_pool_size;
                config.rocket_busy_threshold = this.rocket_busy_threshold;
                config.factoryInterceptor = this.taskFactoryInterceptor;
                config.packageName = this.packageName;
                config.mTask = this.task;
                return config;
            }
            throw new IllegalStateException("HotRocketConfig TextUtils.isEmpty(this.mProcessName)");
        }

        private Config() {
        }

        public Config setRunInProcessName(String str) {
            this.processFullName = str;
            return this;
        }

        public Config setCheckMainThreadBusy(boolean z) {
            this.rocket_main_thread_check = z;
            return this;
        }

        public Config setBusyThreshold(long j) {
            this.rocket_busy_threshold = j;
            return this;
        }

        public Config setThreadPoolSize(int i) {
            this.thread_pool_size = i;
            return this;
        }

        public String getPackageName() {
            return packageName;
        }

        public Config setPackageName(String packageName) {
            this.packageName = packageName;
            return this;
        }
    }

    private HotRocketConfig() {
    }
}
