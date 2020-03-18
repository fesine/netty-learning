package com.fesine.juc.lock.cas;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description: 并发原子类demo
 * @author: fesine
 * @createTime:2020/3/18
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/3/18
 */
public class AtomicTest {

    public static void main(String[] args) throws InterruptedException {
        AtomicInteger i = new AtomicInteger(0);
        for (int j = 0; j < 2; j++) {
            for (int k = 0; k < 10000; k++) {
                new Thread(()->{
                    i.incrementAndGet();
                }).start();
            }
        }
        TimeUnit.SECONDS.sleep(2);
        System.out.println(i.get());
    }
}
