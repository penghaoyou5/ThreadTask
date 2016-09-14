package com.threadmytest;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.concurrent.ArrayBlockingQueue;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    /**
     * 还是通过实用代码好理解呀
     * AsyncTask封装思想
     * 开启子线程操作为主线   同时为了方便 提供线程执行进度的主线程回调与 返回值的主线程回调
     *
     * http://www.58maisui.com/2016/06/22/a-297/
     */
    public void testUseAsyncTask(){
        MyAsyncTask asyncTask = new MyAsyncTask();
        /**
         * 看这个方法 每一个MyAsyncTask实例只能调用一次
         *
         * 但是AsyncTask可以new多个内部有静态变量的线程池来维护
         */
        asyncTask.execute(1,3,4);


        /**
         *  进行任务的取消并设置是否能中断
         */
        asyncTask.cancel(true);
    }



    public class MyAsyncTask extends AsyncTask<Integer,String,MainActivity>{

        /**
         *线程开启前调用
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         *
         * 拿到execut传过来的值 进行子线程操作
         * @param params
         * @return
         */
        @Override
        protected MainActivity doInBackground(Integer... params) {
            publishProgress("");
            return null;
        }

        /**
         * 获取进度值进行回调操作
         * @param values
         */
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }


        /**
         * 使用返回值
         * @param mainActivity
         */
        @Override
        protected void onPostExecute(MainActivity mainActivity) {
            super.onPostExecute(mainActivity);
        }
        /**
         * onPreExecute –> doInBackground –> publishProgress –> onProgressUpdate –> onPostExecute。
         */
    }


}
