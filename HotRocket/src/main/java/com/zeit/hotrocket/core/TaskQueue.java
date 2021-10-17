package com.zeit.hotrocket.core;


import com.zeit.hotrocket.core.utils.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.PriorityBlockingQueue;

public class TaskQueue {

    public volatile boolean isQueueEmpty;

    public int mPriority;

    private Rocket mRocket;

    private Rocket.Config mConfig;

    private PriorityBlockingQueue<Task> mQueue;

    private Map<String, Task> mTaskMap;

    private List<Task> mTaskList;

    private final TaskDispatcher[] mTaskDispatchers;

    private final Object mTaskMapLock = new Object();

    private final Object f39839s = new Object();

    private final Vector<TaskQueueListener> taskQueueListeners = new Vector<>();

    private final Vector<Task.TaskListener> taskListeners = new Vector<>();


    public interface TaskQueueListener {

        void onRocketStop(Rocket rocket, List<Task> list);

        void onRocketStart(Rocket rocket);
    }

    private void onTaskComplete(Task task) {
        for (Object obj : this.taskListeners.toArray()) {
            ((Task.TaskListener) obj).onTaskComplete(task);
        }
    }

    // 把 等待状态且无依赖的任务 导入到工作队列
    private void checkTaskToWorkingQueue() {
        for (Map.Entry<String, Task> entry : this.mTaskMap.entrySet()) {
            if (entry.getValue().mStatus == TaskRunStatus.WAITING && entry.getValue().getDependence().isEmpty()) {
                entry.getValue().mStatus = TaskRunStatus.RUNNABLE;
                this.mQueue.put(entry.getValue());
            }
        }
    }

    private void stopDispatcher() {
        synchronized (this.mTaskDispatchers) {
            TaskDispatcher[] taskDispatchers = this.mTaskDispatchers;
            for (TaskDispatcher dispatcher : taskDispatchers) {
                if (dispatcher != null) {
                    dispatcher.interruptDispatcher();
                }
            }
            this.mRocket.mLog4Rocket.log("[Rocket队列] 全部任务执行完成，分发器即将全部停止 >>>>>>>>>>>>>>>>>>>>>>>");
        }
    }

    private void onRocketStart() {
        for (Object obj : this.taskQueueListeners.toArray()) {
            ((TaskQueueListener) obj).onRocketStart(this.mRocket);
        }
    }

    private void onRocketStop() {
        for (Object obj : this.taskQueueListeners.toArray()) {
            ((TaskQueueListener) obj).onRocketStop(this.mRocket, new ArrayList(this.mTaskList));
        }
    }

    private void onTaskStart(Task task) {
        for (Object obj : this.taskListeners.toArray()) {
            ((Task.TaskListener) obj).onTaskStart(task);
        }
    }

    //整理队列 1.移除相关依赖  2.把可以执行的任务放到队列
    public void organizeQueue(Task task) {
        synchronized (this.mTaskMapLock) {
            ArrayList<Task> arrayList = new ArrayList();
            for (Map.Entry<String, Task> entry : this.mTaskMap.entrySet()) {
                Task value = entry.getValue();
                if (value.mStatus == TaskRunStatus.WAITING && value.getDependence().contains(task.name)) {
                    value.removeDependence(task.name);
                    if (value.getDependence().isEmpty()) {
                        arrayList.add(value);
                        this.mRocket.mLog4Rocket.log("[Rocket队列] 任务 [%s] 减少依赖[%s]，进入可执行状态", value.name, task.name);
                    } else {
                        this.mRocket.mLog4Rocket.log("[Rocket队列] 任务 [%s] 减少依赖[%s]，还有依赖 %s", value.name, task.name, value.getDependence());
                    }
                }
            }
            Collections.sort(arrayList, new TaskPriorityComparator());
            for (Task task1 : arrayList) {
                task1.mStatus = TaskRunStatus.RUNNABLE;
                this.mQueue.put(task1);
            }
            this.mRocket.mLog4Rocket.log("[Rocket队列] 任务 [%s] 重整队列完成，新加可执行任务 %s，当前可执行队列 %s", task.name, arrayList, this.mQueue);
        }
    }


    public Task getTaskFromQueue() throws InterruptedException {
        Task task = this.mQueue.take();
        onTaskStart(task);
        task.mStatus = TaskRunStatus.RUNNING;
        this.mTaskList.add(task);
        this.mRocket.mLog4Rocket.log("[Rocket队列] 任务 [%s] 出队，当前可执行队列 %s", task.name, this.mQueue);
        return task;
    }


    public void completeTask(Task task) {
        synchronized (this.f39839s) {
            task.mStatus = TaskRunStatus.COMPLETE;
            onTaskComplete(task);
        }
    }

    public void checkHasNextTask() {
        boolean isQueueEmpty;
        Iterator<Map.Entry<String, Task>> it = this.mTaskMap.entrySet().iterator();
        while (true) {
            if (it.hasNext()) {
                if (it.next().getValue().mStatus != TaskRunStatus.COMPLETE) {
                    isQueueEmpty = false;
                    break;
                }
            } else {
                isQueueEmpty = true;
                break;
            }
        }
        if (isQueueEmpty) {
            this.isQueueEmpty = true;
            stopDispatcher();
            onRocketStop();
        }
    }


    public void addTaskQueueListeners(TaskQueueListener listener) {
        this.taskQueueListeners.add(listener);
    }

    public void removeTaskQueueListener(TaskQueueListener listener) {
        this.taskQueueListeners.remove(listener);
    }


    public void addTaskListener(Task.TaskListener listener) {
        this.taskListeners.add(listener);
    }


    public void removeTaskListener(Task.TaskListener taskListener) {
        this.taskListeners.remove(taskListener);
    }

    TaskQueue(Rocket rocket, Rocket.Config mConfig) {
        this.mRocket = rocket;
        this.mConfig = mConfig;
        this.isQueueEmpty = false;
        this.mTaskMap = new HashMap();
        for (Task task : this.mConfig.getTaskList()) {
            task.mRocket = this.mRocket;
            this.mTaskMap.put(task.name, task);
            if (this.mPriority < task.mPriority1) {
                this.mPriority = task.mPriority1;
            }
        }
        this.mPriority++;
        this.mQueue = new PriorityBlockingQueue<>(this.mTaskMap.size(), new TaskPriorityComparator());
        checkTaskToWorkingQueue();
        this.mTaskList = Collections.synchronizedList(new ArrayList());
        this.mTaskDispatchers = new TaskDispatcher[this.mConfig.mThreadPoolSize];
        this.mRocket.mLog4Rocket.log("[Rocket队列] 初始化完成\n当前可执行队列：%s\n所有任务：%s", this.mQueue, this.mConfig.getTaskList());
    }

    public void startDispatchers() {
        onRocketStart();
        synchronized (this.mTaskDispatchers) {
            this.mRocket.mLog4Rocket.log("[Rocket队列] 开始，开启所有分发器 >>>>>>>>>>>>>>>>>>>>>>>");
            for (int i = 0; i < this.mTaskDispatchers.length; i++) {
                TaskDispatcher taskDispatcher = new TaskDispatcher(this.mRocket, this);
                this.mTaskDispatchers[i] = taskDispatcher;
                taskDispatcher.setName("Startup#Rocket" + this.mConfig.mProcessName + Constants.ACCEPT_TIME_SEPARATOR_SERVER + i);
                taskDispatcher.start();
            }
        }
    }


    public static class C9066b implements TaskQueueListener {
        @Override
        public void onRocketStop(Rocket rocket, List<Task> list) {

        }

        @Override
        public void onRocketStart(Rocket rocket) {

        }
    }
}
