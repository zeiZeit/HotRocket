package com.zeit.hotrocket.p1087a;


import com.zeit.hotrocket.PddRocket;
import com.zeit.hotrocket.appinit.annotations.PRIORITY;
import com.zeit.hotrocket.core.Task;
import com.zeit.hotrocket.logger.Logger;

import java.util.Set;
import java.util.concurrent.CountDownLatch;

public class BarrierTask extends Task {

    private CountDownLatch downLatch = new CountDownLatch(1);

    public BarrierTask(String str, Set<String> set) {
        super(str, PRIORITY.MIN.priority, set);
    }

    public void mo44821a() {
        this.downLatch.countDown();
        String str = PddRocket.TAG;
        Logger.i(str, "解锁Barrier[" + this.name + "].");
    }

    @Override
    public void runTask() {
        super.runTask();
        String str = PddRocket.TAG;
        Logger.i(str, "到达Barrier[" + this.name + "].");
        try {
            this.downLatch.await();
        } catch (InterruptedException e) {
            Logger.e(PddRocket.TAG, e);
        }
        String str2 = PddRocket.TAG;
        Logger.i(str2, "通过Barrier[" + this.name + "].");
    }
}
