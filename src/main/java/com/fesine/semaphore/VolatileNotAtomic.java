package com.fesine.semaphore;

/**
 * @description: count++ count--不是原子操作，要保证原子操作必须使用synchronized修饰
 * @author: fesine
 * @createTime:2019/12/16
 * @update:修改内容
 * @author: fesine
 * @updateTime:2019/12/16
 */
public class VolatileNotAtomic {
    private static volatile long count=0L;
    private static final int NUMBER=10000;

    public static void main(String[] args) {
        Thread subThread = new SubThread();
        subThread.start();
        for (int i = 0; i < NUMBER; i++) {
            synchronized (subThread){
                count++;
            }
        }
        while (subThread.isAlive()) {

        }
        System.out.println("count最后的值为："+count);

    }

    private static class SubThread extends Thread{

        @Override
        public void run() {
            for (int i = 0; i < NUMBER; i++) {
                synchronized (this){
                    count--;
                }
            }
        }
    }
}
