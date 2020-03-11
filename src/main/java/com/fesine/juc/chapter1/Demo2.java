package com.fesine.juc.chapter1;

/**
 * @description: 线程状态切换
 * @author: fesine
 * @createTime:2020/3/11
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/3/11
 */
public class Demo2 {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("-----第一种状态切换，新建-运行-终止");
        Thread t1 = new Thread(()->{
            System.out.println("t1当前状态：" + Thread.currentThread().getState().toString());
            System.out.println("t1线程运行了");
        });
        System.out.println("没有调用start方法时，t1线程状态:"+t1.getState().toString());
        t1.start();
        //等2秒，等t1执行完成
        Thread.sleep(2000L);
        System.out.println("等待2秒后，再看t1的线程状态" + t1.getState().toString());
        System.out.println();
        System.out.println("-----第二种状态切换，新建-运行-等待-运行-终止（sleep方式）");

        Thread t2 = new Thread(()->{
            try {
                //将线程2移动到等待状态，1500毫秒后自动唤醒
                Thread.sleep(1500L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("t2当前状态：" + Thread.currentThread().getState().toString());
            System.out.println("t2线程运行了");
        });
        System.out.println("没有调用start方法时，t2线程状态:" + t2.getState().toString());
        t2.start();
        System.out.println("调用start方法时，t2线程状态:" + t2.getState().toString());
        //等待200毫秒，查看线程状态
        Thread.sleep(200L);
        System.out.println("等待200毫秒后，t2线程状态:" + t2.getState().toString());
        //等待3秒，等待线程执行完成
        Thread.sleep(3000L);
        System.out.println("等待3秒后，t2线程状态:" + t2.getState().toString());
        System.out.println();
        System.out.println("-----第三种状态切换，新建-运行-阻塞-运行-终止（加锁）");
        Thread t3 = new Thread(()->{
            synchronized (Demo2.class){
                System.out.println("t3当前状态：" + Thread.currentThread().getState().toString());
                System.out.println("t3线程运行了");
            }
        });
        synchronized (Demo2.class){
            System.out.println("没有调用start方法时，t3线程状态:" + t3.getState().toString());
            t3.start();
            System.out.println("调用start方法时，t3线程状态:" + t3.getState().toString());
            //等待200毫秒，查看t3线程状态，此时应该在阻塞
            Thread.sleep(200L);
            System.out.println("等待200毫秒后，t3线程状态:" + t3.getState().toString());
        }
        Thread.sleep(3000L);
        System.out.println("等待3秒后，t3线程状态:" + t3.getState().toString());
    }
}
