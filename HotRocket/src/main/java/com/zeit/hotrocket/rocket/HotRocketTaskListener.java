package com.zeit.hotrocket.rocket;


public interface HotRocketTaskListener {
    void onTaskStart(String str);

    void onTaskComplete(String str);
}
