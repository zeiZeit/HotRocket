package com.zeit.hotrocket.rocket.p1088b.p1089a;

import android.os.Process;
import com.zeit.hotrocket.rocket.core.Rocket;
import com.zeit.hotrocket.rocket.core.utils.Log4Rocket;
import com.zeit.hotrocket.rocket.core.utils.ThreadUtil;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

public class CheckMainThreadDispatcher extends Thread {

    public volatile boolean isMainThreadBusy;

    private LinkedBlockingQueue<Boolean> booleanLinkedBlockingQueue = new LinkedBlockingQueue<>();

    private volatile boolean isQuit = false;

    /*
    * 用于判断主线程是否繁忙的 阈值
    * */
    private long mDelayTime;

    private volatile boolean isRunning;

    private Rocket mRocket;

    public void stopSelf() {
        this.isQuit = true;
        interrupt();
    }


    public void checkMainThreadBusy() {
        String str;
        if (this.isRunning || this.isQuit) {
            Log4Rocket log4Rocket = this.mRocket.mLog4Rocket;
            StringBuilder sb = new StringBuilder();
            sb.append("[Rocket控制器] 控制器");
            if (this.isRunning) {
                str = "正在运行";
            } else if (this.isQuit) {
                str = "已经退出";
            } else {
                str = "发生错误";
            }
            sb.append(str);
            sb.append("，忽略检查主线程忙碌状态");
            log4Rocket.log(sb.toString());
            return;
        }
        this.mRocket.pause();
        this.mRocket.mLog4Rocket.log("[Rocket控制器] Rocket暂停，检查主线程是否繁忙...");
        this.booleanLinkedBlockingQueue.offer(true);
    }

    public void run() {
        Process.setThreadPriority(10);
        while (true) {
            try {
                this.booleanLinkedBlockingQueue.take();
                this.isRunning = true;
                final CountDownLatch countDownLatch = new CountDownLatch(1);
                ThreadUtil.isMainThreadBusy(this.mDelayTime, isMainThreadBusy -> {
                    CheckMainThreadDispatcher.this.isMainThreadBusy = isMainThreadBusy;
                    countDownLatch.countDown();
                });
                countDownLatch.await();
                if (!this.isMainThreadBusy || this.mRocket.isQueueEmpty() || !this.mRocket.isPaused()) {
                    this.mRocket.releaseLock();
                    this.isRunning = false;
                    this.mRocket.mLog4Rocket.log("[Rocket控制器] 主线程空闲，Rocket恢复执行");
                } else {
                    this.booleanLinkedBlockingQueue.offer(false);
                    this.mRocket.mLog4Rocket.log("[Rocket控制器] 主线程繁忙，继续检查...");
                }
            } catch (InterruptedException unused) {
                if (this.isQuit) {
                    this.mRocket.releaseLock();
                    this.isRunning = false;
                    this.mRocket.mLog4Rocket.log("[Rocket控制器] 控制器退出");
                    return;
                }
            }
        }
    }

    CheckMainThreadDispatcher(Rocket aVar, long j) {
        this.mRocket = aVar;
        this.isRunning = false;
        this.isMainThreadBusy = false;
        this.mDelayTime = j;
    }
}
