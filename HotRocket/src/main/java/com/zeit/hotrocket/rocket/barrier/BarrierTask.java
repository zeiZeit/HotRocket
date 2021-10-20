package com.zeit.hotrocket.rocket.barrier;


import com.zeit.hotrocket.rocket.HotRocket;
import com.zeit.hotrocket.appinit.annotations.PRIORITY;
import com.zeit.hotrocket.rocket.core.Task;
import com.zeit.hotrocket.logger.Logger;

import java.util.Set;
import java.util.concurrent.CountDownLatch;

/**
 * 通过任务跑完了先挂起，实现任务与任务之间的阻断
 */
public class BarrierTask extends Task {

    private CountDownLatch downLatch = new CountDownLatch(1);

    public BarrierTask(String name, Set<String> dependence) {
        super(name, PRIORITY.MIN.priority, dependence);
    }

    /**
     * 解锁Barrier
     */
    public void unLockBarrier() {
        this.downLatch.countDown();
        String str = HotRocket.TAG;
        Logger.i(str, "解锁Barrier[" + this.name + "].");
    }

    @Override
    public void runTask() {
        super.runTask();
        String str = HotRocket.TAG;
        Logger.i(str, "到达Barrier[" + this.name + "].");
        try {
            this.downLatch.await();
        } catch (InterruptedException e) {
            Logger.e(HotRocket.TAG, e);
        }
        String str2 = HotRocket.TAG;
        Logger.i(str2, "通过Barrier[" + this.name + "].");
    }
}
