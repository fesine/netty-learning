package com.fesine.juc.lock;

import java.util.concurrent.TimeUnit;

/**
 * @description: 使用两个线程实现对变量i的自增操作
 * @author: fesine
 * @createTime:2020/3/18
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/3/18
 */
public class LockDemo2_1 {
    /**
     * 使用volatile关键字修饰，虽然可以保证变量可见性，但无法从根本上解决问题，因为i++操作不是原子性
     */
    private volatile int i;

    /**
     * 通过反编译class文件生成汇编语言，可以得知i++;操作不是原子性，分三步操作
     * 在多线程操作i++时，会导致变量i的值不同步
     * 当多线程共同更新共享资源是，需要判断临界区和竞态条件
     * i++;这段代码就是临界区，i++为竞态条件
     * Code:
     *        0: aload_0
     *        1: dup
     *        2: getfield      #2                  // Field i:I
     *        5: iconst_1
     *        6: iadd
     *        7: putfield      #2                  // Field i:I
     *       10: return
     */
    public  void add(){
        i++;
    }

    public static void main(String[] args) throws InterruptedException {
        LockDemo2_1 demo = new LockDemo2_1();
        for (int i = 0; i < 2; i++) {
            new Thread(()->{
                for (int j = 0; j < 10000; j++) {
                    demo.add();
                }
            }).start();
        }

        TimeUnit.SECONDS.sleep(2);
        System.out.println(demo.i);

    }
}
