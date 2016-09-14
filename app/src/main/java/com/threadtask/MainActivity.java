package com.threadtask;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * 设计并实现一个异步任务队列，能够监听每一个任务被执行的生命周期
 * <p/>
 * 使用handler实现 参考AsyncTask
 * <p/>
 * 1.有生命周期标示的任务类
 * <p/>
 * 2.线程主类
 * <p/>
 * 2.线程池取数据执行
 */
public class MainActivity extends AppCompatActivity {
    TextView tv_1,tv_2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testThreadDemo();
            }
        });

        tv_1 = (TextView) findViewById(R.id.tv_1);
        tv_2 = (TextView) findViewById(R.id.tv_2);
    }

    private void testThreadDemo() {



        testTv1();
        testTv2();
    }

    private void testTv1() {
        ThreadExecuteClass.execute(new LifeCycleRunnable() {


            @Override
            public void onStart() {
                tv_1.setText("开始");
            }

            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                tv_1.setText("1000毫秒 t1 结束");
            }
        });
    }

    private void testTv2() {
        ThreadExecuteClass.execute(new LifeCycleRunnable<String>() {

            @Override
            public void onStart() {
                tv_2.setText("开始");
            }

            @Override
            public void run() {
                try {
                    int i = 0;
                    while (i < 5) {
                        i++;
                        Thread.sleep(500);
                        pullRunUIThread(i * 500 + "毫秒 t2");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onPullFinish(String... objects) {
                tv_2.setText(objects[0]);
            }
        });
    }
}
