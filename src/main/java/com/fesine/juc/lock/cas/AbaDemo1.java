package com.fesine.juc.lock.cas;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @description: aba问题，重复操作 过期操作
 * @author: fesine
 * @createTime:2020/3/18
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/3/18
 */
public class AbaDemo1 {

    /**
     * 模拟充值、消费动作
     * 有3个线程给用户充值，当用户余额少于20元是，就充值20元
     * 有100个线程在消费，每次消费10元
     * 用户初始金额19元
     * AtomicStampedReference增加版本信息
     */
    static AtomicStampedReference<Integer> money = new AtomicStampedReference(19,0);

    public static void main(String[] args) {
        for (int i = 0; i < 3; i++) {
            int stamp = money.getStamp();
            new Thread(()->{
                while (true) {
                    int current;
                    do {
                        //获取内存中的值
                        current = money.getReference();
                    } while (!money.compareAndSet(current, current + 20, stamp, stamp+1));
                }
            }).start();
        }

        for (int i = 0; i < 100; i++) {
            new Thread(()->{
                while (true){
                    int stamp = money.getStamp();
                    int m = money.getReference();
                    if(m>10) {
                        if (money.compareAndSet(m, m - 10, stamp, stamp + 1)) {
                            System.out.println("消费10元，余额：" + money.getReference());
                        }
                    }
                    try {
                        TimeUnit.MILLISECONDS.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }).start();
        }
    }
}
