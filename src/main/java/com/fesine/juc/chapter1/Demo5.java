package com.fesine.juc.chapter1;

/**
 * @description: threadLocal线程封闭实例
 * @author: fesine
 * @createTime:2020/3/12
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/3/12
 */
public class Demo5 {
    /**
     * threadLocal变量，每个线程一个副本互不干扰
     */
    private static ThreadLocal<String> value = new ThreadLocal<>();

    private void threadLocalTest(){
        //主线程设值
        value.set("主线程设值123");
        System.out.println("线程1执行之前，主线程取到的值：" + value.get());
        new Thread(()->{
            String v = value.get();
            System.out.println("线程1执行，取到的值：" + v);
            value.set("线程1设值456");
            v = value.get();
            System.out.println("线程1重新设值后，取到的值：" + v);
            System.out.println("线程1执行结束");
        }).start();
        try {
            //等待所有线程执行结束
            Thread.sleep(5000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("线程1执行结束后，主线程获取到的值："+value.get());
    }

    public static void main(String[] args) {
        new Demo5().threadLocalTest();
    }
}
