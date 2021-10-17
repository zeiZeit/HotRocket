package com.zeit.hotrocket.core;


public class RocketLock {

    private boolean isLocking;

    private final Object mLock = new Object();

    public boolean isLocked() {
        boolean z;
        synchronized (this.mLock) {
            z = this.isLocking;
        }
        return z;
    }

    public void releaseLock() {
        synchronized (this.mLock) {
            this.isLocking = false;
            this.mLock.notifyAll();
        }
    }

    public void lock() {
        synchronized (this.mLock) {
            this.isLocking = true;
        }
    }

    public void letWait() throws InterruptedException {
        synchronized (this.mLock) {
            if (this.isLocking) {
                this.mLock.wait();
            }
        }
    }

    RocketLock() {
    }
}
