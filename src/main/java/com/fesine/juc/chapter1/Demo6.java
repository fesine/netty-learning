package com.fesine.juc.chapter1;

import java.util.concurrent.locks.LockSupport;

/**
 * @description: 线程通信，演示suspend & resume使用
 * @author: fesine
 * @createTime:2020/3/11
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/3/11
 */
public class Demo6 {
    private Object baozidian = null;

    /**
     * 正确使用suspend & resume
     */
    private void suspendAndResumeTest() {
        //创建一个消费者去购买包子
        Thread consume = new Thread(() -> {
            if (baozidian == null) {
                System.out.println("1、没有包子，进入等待");
                Thread.currentThread().suspend();
            }
            System.out.println("3、买到包子，回家");
        });
        consume.start();

        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        baozidian = new Object();
        System.out.println("2、生产包子，通知消费者");
        consume.resume();
    }

    /**
     * 错误使用suspend & resume
     * suspend在resume之后调用
     */
    private void suspendAfterResumeTest() {
        //创建一个消费者去购买包子
        Thread consume = new Thread(() -> {
            if (baozidian == null) {
                System.out.println("1、没有包子，进入等待");
                try {
                    Thread.sleep(5000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Thread.currentThread().suspend();
            }
            System.out.println("3、买到包子，回家");
        });
        consume.start();

        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        baozidian = new Object();
        System.out.println("2、生产包子，通知消费者");
        consume.resume();
    }

    /**
     * 错误使用suspend & resume
     * 死锁状态
     */
    private void suspendAndResumeWithLockTest() {
        //创建一个消费者去购买包子
        Thread consume = new Thread(() -> {
            if (baozidian == null) {
                System.out.println("1、没有包子，进入等待");
                synchronized (this) {
                    Thread.currentThread().suspend();
                }
            }
            System.out.println("3、买到包子，回家");
        });
        consume.start();

        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        baozidian = new Object();
        System.out.println("2、生产包子，通知消费者");
        synchronized (this) {
            consume.resume();
        }
    }

    /**
     * 正确使用wait & notify
     * wait等待会解锁
     */
    private void waitAndNotifyTest() {
        //创建一个消费者去购买包子
        Thread consume = new Thread(() -> {
            if (baozidian == null) {
                synchronized (this) {
                    System.out.println("1、没有包子，进入等待");
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
            System.out.println("3、买到包子，回家");
        });
        consume.start();

        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        baozidian = new Object();
        System.out.println("2、生产包子，通知消费者");
        synchronized (this) {
            this.notify();
        }
    }

    /**
     * 错误使用wait & notify
     * 当notify在wait前执行时，会导致线程永久在等待状态，无法被唤醒
     */
    private void waitAfterNotifyTest() {
        //创建一个消费者去购买包子
        Thread consume = new Thread(() -> {
            if (baozidian == null) {
                try {
                    //拿到锁之前先等待
                    Thread.sleep(5000L);
                    synchronized (this) {
                        System.out.println("1、没有包子，进入等待");
                        this.wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
            System.out.println("3、买到包子，回家");
        });
        consume.start();

        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        baozidian = new Object();
        System.out.println("2、生产包子，通知消费者");
        synchronized (this) {
            this.notify();
        }
    }

    /**
     * 正确使用park & unpark
     */
    private void parkAndUnparkTest() {
        //创建一个消费者去购买包子
        Thread consume = new Thread(() -> {
            if (baozidian == null) {
                System.out.println("1、没有包子，进入等待");
                LockSupport.park();
            }
            System.out.println("3、买到包子，回家");
        });
        consume.start();

        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        baozidian = new Object();
        System.out.println("2、生产包子，通知消费者");
        LockSupport.unpark(consume);
    }

    /**
     * park 在 unpark 后面执行，不影响结果
     */
    private void parkAfterUnparkTest() {
        //创建一个消费者去购买包子
        Thread consume = new Thread(() -> {
            if (baozidian == null) {
                try {
                    Thread.sleep(5000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("1、没有包子，进入等待");
                LockSupport.park();
            }
            System.out.println("3、买到包子，回家");
        });
        consume.start();

        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        baozidian = new Object();
        System.out.println("2、生产包子，通知消费者");
        LockSupport.unpark(consume);
    }

    /**
     * 错误案例park & unpark
     * 不会释放锁
     */
    private void parkAndUnparkDeadLockTest() {
        //创建一个消费者去购买包子
        Thread consume = new Thread(() -> {
            if (baozidian == null) {
                System.out.println("1、没有包子，进入等待");
                synchronized (this){
                    LockSupport.park();
                }
            }
            System.out.println("3、买到包子，回家");
        });
        consume.start();

        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        baozidian = new Object();
        System.out.println("2、生产包子，通知消费者");
        synchronized (this){
            LockSupport.unpark(consume);
        }
    }

    public static void main(String[] args) {
        Demo6 demo6 = new Demo6();
        //demo6.suspendAndResumeTest();
        //demo6.suspendAndResumeWithLockTest();
        //demo6.suspendAfterResumeTest();
        //demo6.waitAndNotifyTest();
        //demo6.waitAfterNotifyTest();
        //demo6.parkAndUnparkTest();
        //demo6.parkAfterUnparkTest();
        demo6.parkAndUnparkDeadLockTest();
    }
}
