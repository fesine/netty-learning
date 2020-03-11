package com.fesine.juc.chapter1;

/**
 * @description: stop强制中断线程，破坏线程安全
 * @author: fesine
 * @createTime:2020/3/11
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/3/11
 */
public class Demo3 {
    public static void main(String[] args) throws InterruptedException {
        StopThread stopThread = new StopThread();
        stopThread.start();
        //休眠1秒，确保i变量自增成功
        Thread.sleep(1000L);
        //执行stop方法，破坏线程安全
        //stopThread.stop();
        //使用interrupt方法，正常中断
        stopThread.interrupt();
        while (stopThread.isAlive()) {
            //确保线程已经中止
        }
        //打印i和j的值
        stopThread.print();
    }
}
