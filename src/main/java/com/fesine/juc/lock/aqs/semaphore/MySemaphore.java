package com.fesine.juc.lock.aqs.semaphore;

import com.fesine.juc.lock.aqs.MyAqs;

/**
 * @description: 利用MyAqs实现自定义信号量
 * @author: fesine
 * @createTime:2020/4/10
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/4/10
 */
public class MySemaphore  {

    private MyAqs aqs = new MyAqs(){
        @Override
        public int tryAcquireShared() {
            //自旋锁，直到获取到信号量
            for(;;){
                //获取当前state的值
                int count = getState().get();
                //将当前state值-1
                int n = count - 1;
                //如果当前值小于等于0或者n小于0，获取失败
                if (count <= 0 || n < 0) {
                    return -1;
                }
                //利用cas方法设置最新的state值
                if (getState().compareAndSet(count, n)) {
                    return 1;
                }
            }
        }

        @Override
        public boolean tryReleaseShared() {
            return getState().incrementAndGet() >= 0;
        }
    };

    public MySemaphore(int count){
        aqs.getState().set(count);
    }

    public void acquire(){
        aqs.acquireShared();
    }

    public void release(){
        aqs.releaseShared();
    }
}
