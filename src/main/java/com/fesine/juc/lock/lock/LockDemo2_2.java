package com.fesine.juc.lock.lock;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

/**
 * @description: 使用两个线程实现对变量i的自增操作，
 * @author: fesine
 * @createTime:2020/3/18
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/3/18
 */
public class LockDemo2_2 {
    /**
     * 使用volatile关键字修饰，虽然可以保证变量可见性，但无法从根本上解决问题，因为i++操作不是原子性
     * 使用unsafe类进行cas操作
     */
    private volatile int value;

    /**
     * 使用unsafe类直接操作内存，通过内存地址修改对象
     */
    static Unsafe unsafe;

    private static Long valueOffset;

    static {
        //Unsafe类无法直接创建，只能通过反射
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            //private static final Unsafe theUnsafe = new Unsafe(); 为私有 不可变属性
            field.setAccessible(true);
            unsafe = (Unsafe) field.get(null);
            //获取到value属性的偏移量（用于确定value在内存中的具体地址）
            valueOffset = unsafe.objectFieldOffset(LockDemo2_2.class.getDeclaredField("value"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
        //i++;
        //cas+循环重试
        int current;
        do {
            /**
             * 1、获取当前内存地址中的值
             * 2、将当前内存地址中的值+1，并且在+1操作后，比较当前操作的值和内存地址中的值是否匹配
             * 3、如果不匹配，则操作失败，继续获取内存地址值
             * 如果大量操作不成功，会大量占用cpu执行时间
             */
            current = unsafe.getIntVolatile(this, valueOffset);
        } while (!unsafe.compareAndSwapInt(this, valueOffset, current, current + 1));
    }

    public static void main(String[] args) throws InterruptedException {
        LockDemo2_2 demo = new LockDemo2_2();
        for (int i = 0; i < 2; i++) {
            new Thread(()->{
                for (int j = 0; j < 10000; j++) {
                    demo.add();
                }
            }).start();
        }

        TimeUnit.SECONDS.sleep(2);
        System.out.println(demo.value);

    }
}
