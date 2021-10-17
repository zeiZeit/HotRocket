package com.zeit.hotrocket.p1088b.p1089a;


import com.zeit.hotrocket.core.Task;
import com.zeit.hotrocket.core.Rocket;
import com.zeit.hotrocket.core.TaskQueue;

import java.util.List;

public class MainThreadBusyStateControllerPlugin {
    public void mo44832a(Rocket aVar, Object... objArr) {
        if (objArr == null || objArr[0] == null) {
            throw new IllegalArgumentException("PauseWhenMainThreadBusy.apply params == null || params[0] == null");
        }
        final CheckMainThreadDispatcher dispatcher = new CheckMainThreadDispatcher(aVar, ((Long) objArr[0]).longValue());
        dispatcher.setName("Startup#Rocket-PauseWhenMainThreadBusy");
        dispatcher.start();
        dispatcher.mo44828b();
        final Task.TaskListener r0 = new Task.TaskListener() {

            @Override
            public void onTaskComplete(Task cVar) {
                dispatcher.mo44828b();
            }

            @Override
            public void onTaskStart(Task cVar) {
            }
        };
        aVar.addTaskListener(r0);
        aVar.addTaskQueueListener(new TaskQueue.C9066b() {
            @Override
            public void onRocketStop(Rocket rocket, List<Task> list) {
                super.onRocketStop(rocket, list);
                rocket.removeTaskQueueListener(this);
                rocket.removeTaskListener(r0);
                dispatcher.stopSelf();
            }
        });
    }
}
