package com.zeit.hotrocket.rocket.core;

import java.util.Comparator;

public class TaskPriorityComparator implements Comparator<Task> {
    public int compare(Task task1, Task task2) {
        if (task2.isReady && !task1.isReady) {
            return 1;
        }
        if (task2.isReady || !task1.isReady) {
            return task2.mPriority2 - task1.mPriority2;
        }
        return -1;
    }
}
