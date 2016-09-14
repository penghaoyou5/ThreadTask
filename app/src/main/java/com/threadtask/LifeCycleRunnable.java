package com.threadtask;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * Created by bjhl-penghaoyou on 16/9/13.
 *
 */
public abstract class LifeCycleRunnable<T> implements Runnable {


    private static InternalHandler sHandler;

    private static Handler getHandler() {
        synchronized (LifeCycleRunnable.class) {
            if (sHandler == null) {
                sHandler = new InternalHandler();
            }
            return sHandler;
        }
    }

    private static class InternalHandler extends Handler {
        public InternalHandler() {
            super(Looper.getMainLooper());
        }
        public void handleMessage(Message msg) {
            if(msg.obj instanceof ExecuteStatus) {
                ExecuteStatus executeStatus = (ExecuteStatus) msg.obj;
                switch (executeStatus.status){
                    case PENDING:
                        executeStatus.cycleRunnable.onStart();
                        break;
                    case FINISHED:
                        executeStatus.cycleRunnable.onFinish();
                        break;
                }
            }
            else if (msg.obj instanceof Result){
                Result result = (Result)msg.obj;
                result.cycleRunnable.onPullFinish(result.data);
            }
        }
    }


    private volatile Status mStatus = Status.PENDING;

    /**
     * 生命周期状态
     */
    public enum Status {

        PENDING,

        RUNNING,

        FINISHED,
    }

    public static class ExecuteStatus{
        public ExecuteStatus(Status status, LifeCycleRunnable cycleRunnable) {
            this.status = status;
            this.cycleRunnable = cycleRunnable;
        }

        public Status status;
        public LifeCycleRunnable cycleRunnable;
    }


    public Status getmStatus() {
        return mStatus;
    }

    private void setmStatus(Status mStatus) {
        this.mStatus = mStatus;
    }


    /**
     * 为了执行生命周期被真正调用的方法
     */
    public Runnable runnable = new Runnable() {
        @Override
        public void run() {

            getHandler().obtainMessage(0,new ExecuteStatus(Status.PENDING,LifeCycleRunnable.this)).sendToTarget();

            setmStatus(Status.RUNNING);
            LifeCycleRunnable.this.run();
            setmStatus(Status.FINISHED);

            getHandler().obtainMessage(0,new ExecuteStatus(Status.FINISHED,LifeCycleRunnable.this)).sendToTarget();
        }
    };

    public static class Result{
        public Object[] data;
        public LifeCycleRunnable cycleRunnable;
    }

    /**
     * 执行刚开始的回调
     */
    public void onStart(){}


    /**
     * 使运行在主线程中的方法
     */
    public void pullRunUIThread(T... objects){
        Result result  = new Result();
        result.data = objects;
        result.cycleRunnable = this;
        getHandler().obtainMessage(0,result).sendToTarget();
    }


    /**
     * 调用运行的回调
     * @param objects
     */
    public void onPullFinish(T... objects){}


    /**
     * 执行完成的回调
     */
    public void onFinish(){}
}
