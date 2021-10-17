package com.zeit.hotrocket.core;

import java.util.concurrent.CountDownLatch;


public class TaskCompleteEmitterImpl implements TaskCompleteEmitter {


    private CountDownLatch countDownLatch = new CountDownLatch(1);

    @Override
    public void onTaskComplete() {
        this.countDownLatch.countDown();
    }


    public void waitTaskComplete() throws InterruptedException {
        this.countDownLatch.await();
    }
}
