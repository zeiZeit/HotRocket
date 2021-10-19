package com.zeit.hotrocket.rocket.p1088b.p1089a;


import com.zeit.hotrocket.rocket.core.Task;
import com.zeit.hotrocket.rocket.core.Rocket;
import com.zeit.hotrocket.rocket.core.TaskQueue;

import java.util.List;

public class MainThreadBusyStateControllerPlugin {
    public void openCheckMainThreadBusy(Rocket rocket, Object... objArr) {
        if (objArr == null || objArr[0] == null) {
            throw new IllegalArgumentException("PauseWhenMainThreadBusy.apply params == null || params[0] == null");
        }
        final CheckMainThreadDispatcher dispatcher = new CheckMainThreadDispatcher(rocket, ((Long) objArr[0]).longValue());
        dispatcher.setName("Startup#Rocket-PauseWhenMainThreadBusy");
        dispatcher.start();
        dispatcher.checkMainThreadBusy();
        final Task.TaskListener taskListener = new Task.TaskListener() {

            @Override
            public void onTaskComplete(Task cVar) {
                dispatcher.checkMainThreadBusy();
            }

            @Override
            public void onTaskStart(Task cVar) {
            }
        };
        rocket.addTaskListener(taskListener);
        rocket.addTaskQueueListener(new TaskQueue.RocketListener() {
            @Override
            public void onRocketStop(Rocket rocket, List<Task> list) {
                super.onRocketStop(rocket, list);
                rocket.removeTaskQueueListener(this);
                rocket.removeTaskListener(taskListener);
                dispatcher.stopSelf();
            }
        });
    }
}
