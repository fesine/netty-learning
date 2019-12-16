package com.fesine.semaphore;

/**
 * @description: 类描述
 * @author: fesine
 * @createTime:2019/12/16
 * @update:修改内容
 * @author: fesine
 * @updateTime:2019/12/16
 */
public class VolatileDemo {

    /**
     * 线程间感知修改共享变量，需要使用volatile修饰
     */
    private volatile static boolean isOver =false;

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(()->{
            while (!isOver){

            }
            System.out.println("线程已经感知到isOver为true，线程正常返回！");
        });
        thread.start();
        Thread.sleep(500);
        isOver = true;
        System.out.println("isOver值为true");
    }
}
