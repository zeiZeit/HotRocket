>HotRocket ！！一款Android 冷启动 任务分发组件 

#### 原理

把启动任务按启动阶段，运行进程，运行线程，分轻重急缓进行划分。
任务类型可分为：

1. application init 且运行在主线程的任务
2. application init 且运行在background的任务

此为第一阶段，为application启动阶段，在此阶段可以做大量的预加载任务

3. MainActivity第一次doframe可见后，视为HomeReady阶段，此阶段可做延迟加载的任务。
4. MainActivity利用主线程空闲时段运行任务，主要用到了handlerIdle ，即称此阶段为homeIdle阶段
5. UserIdle阶段

