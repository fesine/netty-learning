package com.fesine.juc.chapter1;

/**
 * @description: 通过标志位中止线程
 * @author: fesine
 * @createTime:2020/3/11
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/3/11
 */
public class Demo4 {
    private static volatile boolean flag=true;

    public static void main(String[] args) throws InterruptedException {
        new Thread(()->{
            while (flag) {
                try {
                    System.out.println("运行中");
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        //3秒后，修改标识位为false，代表不再运行
        Thread.sleep(3000L);
        flag = false;
        System.out.println("线程运行结束");
    }
}
