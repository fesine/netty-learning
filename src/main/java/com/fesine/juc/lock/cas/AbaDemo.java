package com.fesine.juc.lock.cas;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description: aba问题，重复操作 过期操作
 * @author: fesine
 * @createTime:2020/3/18
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/3/18
 */
public class AbaDemo {

    /**
     * 模拟充值、消费动作
     * 有3个线程给用户充值，当用户余额少于20元是，就充值20元
     * 有100个线程在消费，每次消费10元
     * 用户初始金额9元
     */
    static AtomicInteger money = new AtomicInteger(19);

    public static void main(String[] args) {
        for (int i = 0; i < 3; i++) {
            new Thread(()->{
                int current;
                do {
                    //获取内存中的值
                    current = money.get();
                } while (!money.compareAndSet(current, current + 20));
            }).start();
        }

        for (int i = 0; i < 100; i++) {
            new Thread(()->{
                while (true){
                    int m = money.get();
                    if(m>10){
                        if(money.compareAndSet(m, m - 10)){
                            System.out.println("消费10元，余额：" + money.get());
                            break;
                        }
                    }else {
                        break;
                    }

                }
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
