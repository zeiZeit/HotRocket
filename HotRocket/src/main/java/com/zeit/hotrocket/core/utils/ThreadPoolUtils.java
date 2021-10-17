package com.zeit.hotrocket.core.utils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/*
 *  @文件名:   ThreadPoolUtils 访OkHttp Dispatcher写的
 *  @描述：    线程管理类，管理并发执行的任务。
 *  1. 默认同时最多执行10个任务，如有需求可以通过修改MAX_TASK值。
 *  2. 如果任务数量大于10个，就会放进readyTasks里面等待执行。
 *  3. 任务执行完成都会调用promoteAndExecute方法。
 *  4. 执行任务用的是线程池
 */
public class ThreadPoolUtils {
    // 线程池
    private ExecutorService executorService;
    // 准备执行的任务
    private final Deque<ThreadTask> readyTasks = new ArrayDeque<>();
    // 正在执行的任务
    private final Deque<ThreadTask> runningTasks = new ArrayDeque<>();
    // 队列默认并发任务个数最大值为10
    private static int MAX_TASK = 10;

    private static ScheduledExecutorService scheduledExecutorService ;


    private ThreadPoolUtils() {
    }

    public static ThreadPoolUtils getInstance() {

        return InstanceHolder.instance;
    }

    private static class InstanceHolder {
        public static ThreadPoolUtils instance = new ThreadPoolUtils();
    }

    protected ExecutorService executorService() {
        if (executorService == null) {
            synchronized (this) {
                if (executorService == null) {
                    executorService = new ThreadPoolExecutor(
                            0,
                            Integer.MAX_VALUE,
                            60,
                            TimeUnit.SECONDS,
                            // 1、直接提交队列：设置为SynchronousQueue队列，SynchronousQueue是一个特殊的BlockingQueue，
                            // 它没有容量，没执行一个插入操作就会阻塞，需要再执行一个删除操作才会被唤醒，
                            // 反之每一个删除操作也都要等待对应的插入操作
                            new SynchronousQueue<Runnable>(),
                            threadFactory()
                    );
                }
            }
        }
        return executorService;
    }

    /**
     * 执行任务
     */
    public void execute(Runnable runnable) {
        synchronized (this) {
            // 添加到准备的任务里面
            readyTasks.add(new ThreadTask(runnable,0l));
        }

        // 去执行
        promoteAndExecute();
    }

    /**
     * 执行任务
     */
    public void delayExecute(Runnable runnable,long delay) {
        if (scheduledExecutorService==null){
            scheduledExecutorService = Executors.newScheduledThreadPool(5);
        }
        scheduledExecutorService.schedule(runnable,delay,TimeUnit.MILLISECONDS);
    }

    private void promoteAndExecute() {
        // 如果没有准备的任务或者执行的任务大于任务数量
        synchronized (this) {
            if (readyTasks.size() <= 0 || runningTasks.size() >= MAX_TASK) {
                return;
            }
        }
        // 创建一个List
        List<ThreadTask> executableTasks = new ArrayList<>();
        synchronized (this) {
            for (ThreadTask readyTask : readyTasks) {
                // 如果任务数大于最大数量
                if (runningTasks.size() >= MAX_TASK) {
                    break;
                }
                // 移除
                readyTasks.remove(readyTask);
                // 添加到执行的任务集合
                runningTasks.add(readyTask);
                executableTasks.add(readyTask);
            }
        }

        for (ThreadTask executableCall : executableTasks) {
            executeOn(executableCall);
        }
    }

    private void executeOn(ThreadTask task) {
        boolean success = false;
        try {
            // 开线程后，立马到finally
            executorService().execute(task);
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 如果出现异常
            if (!success) {
                finishTask(task);
            }
        }

    }

    private void finishTask(Runnable task) {
        synchronized (this) {
            runningTasks.remove(task);
        }
        // 回去继续执行
        promoteAndExecute();
    }

    protected ThreadFactory threadFactory() {
        return new ThreadFactory() {
            @Override
            public Thread newThread(Runnable runnable) {
                Thread result = new Thread(runnable, "ThreadPoolUtils");
                result.setDaemon(false);
                return result;
            }
        };
    }

    /**
     * 这个比较少用，直接执行
     */
    public Future<?> submit(Runnable task) {
        return executorService().submit(task);
    }

    /**
     * 设置任务并发最大数量，如果要改变设置调用一次即可
     *
     * @param maxTask
     */
    public static void setMaxTask(int maxTask) {
        MAX_TASK = maxTask;
    }


    /**
     * 任务：使用静态代理
     */
    private static class ThreadTask implements Runnable {
        private Runnable runnable;
        private long delayTime;

        public ThreadTask(Runnable runnable,long delayTime) {
            this.runnable = runnable;
            this.delayTime = delayTime;
        }

        @Override
        public void run() {
            try {
                runnable.run();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // 从队列移除
                ThreadPoolUtils.getInstance().finishTask(this);
            }
        }

    }

}