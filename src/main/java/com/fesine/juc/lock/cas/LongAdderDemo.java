package com.fesine.juc.lock.cas;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * @description: jdk8中针对单变量自增做了升级，比较三者之间的性能
 * @author: fesine
 * @createTime:2020/3/18
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/3/18
 */
public class LongAdderDemo {
    private long count = 0;

    /**
     * 使用同步代码块方式
     */
    private void testSync(){
        for (int i = 0; i < 3; i++) {
            new Thread(()->{
                long startTime = System.currentTimeMillis();
                while (System.currentTimeMillis() - startTime < 2000) {
                    synchronized (this){
                        ++count;
                    }
                }
                long endTime = System.currentTimeMillis();
                System.out.println("syncThread spend:"+(endTime-startTime)+", v="+count);
            }).start();
        }
    }

    /**
     * 使用传统并发类方式
     */
    AtomicLong acount = new AtomicLong(0);
    private void testAtomic(){
        for (int i = 0; i < 3; i++) {
            new Thread(()->{
                long startTime = System.currentTimeMillis();
                while (System.currentTimeMillis() - startTime < 2000) {
                    acount.incrementAndGet();
                }
                long endTime = System.currentTimeMillis();
                System.out.println("atomicThread spend:"+(endTime-startTime)+", v="+acount.get());
            }).start();
        }
    }

    /**
     * jdk8 LongAdder高级使用
     * 其底层也是unsafe操作
     * 其原理是创建一个一定长度cell[]的数组，一个或多个线程操作数组中的一个元素，减少锁冲突
     * 取值时将数组中的值累计得出结果
     * 适用于频繁更新但读取比较少的情况
     * 因为累计操作时比较消耗性能
     */
    LongAdder lcount = new LongAdder();
    private void testLongAdder(){
        for (int i = 0; i < 3; i++) {
            new Thread(()->{
                long startTime = System.currentTimeMillis();
                while (System.currentTimeMillis() - startTime < 2000) {
                    lcount.increment();
                }
                long endTime = System.currentTimeMillis();
                System.out.println("longAdderThread spend:"+(endTime-startTime)+", v="+lcount.sum());
            }).start();
        }
    }

    public static void main(String[] args) {
        LongAdderDemo demo = new LongAdderDemo();
        demo.testSync();
        demo.testAtomic();
        demo.testLongAdder();
    }
}
