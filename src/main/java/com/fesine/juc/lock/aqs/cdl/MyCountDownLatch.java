package com.fesine.juc.lock.aqs.cdl;

import com.fesine.juc.lock.aqs.MyAqs;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description: 自定义实现countDownLatch
 * @author: fesine
 * @createTime:2020/4/10
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/4/10
 */
public class MyCountDownLatch {

    MyAqs aqs = new MyAqs(){
        @Override
        public int tryAcquireShared() {
            //如果非等于0，说明还有线程没准备就绪，需要等待
            return getState().get() == 0? 1:-1;
        }

        @Override
        public boolean tryReleaseShared() {
            //如果非等于0，说明还有线程没有准备就绪，则不会通知继续执行
            return getState().decrementAndGet() == 0;
        }
    };

    public MyCountDownLatch(int count) {
        aqs.setState(new AtomicInteger(count));
    }

    public void await(){
        aqs.acquireShared();
    }

    public void countDown(){
        aqs.releaseShared();
    }

}
