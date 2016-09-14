package com.threadtask;

import java.util.ArrayDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by bjhl-penghaoyou on 16/9/13
 * 线程放入执行的方法
 * 线程取消的方法
 * 进行线程执行的状态改变
 */
public class ThreadExecuteClass {

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final int KEEP_ALIVE = 1;

    private static final ThreadFactory sThreadFactory = new ThreadFactory() {

        //AtomicInteger，一个提供原子操作的Integer的类
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "ThreadExecuteClass #" + mCount.getAndIncrement());
        }
    };

    private static final BlockingQueue<Runnable> sPoolWorkQueue =
            new LinkedBlockingQueue<Runnable>(128);


    public static final Executor THREAD_POOL_EXECUTOR
            = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE,
            TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory);

    public static void execute(LifeCycleRunnable lifeCycleRunnable){
        executeOnExecutor(THREAD_POOL_EXECUTOR,lifeCycleRunnable);
    }

    public static synchronized void executeOnExecutor(Executor exec,LifeCycleRunnable lifeCycleRunnable){
        exec.execute(lifeCycleRunnable.runnable);
    }
}
