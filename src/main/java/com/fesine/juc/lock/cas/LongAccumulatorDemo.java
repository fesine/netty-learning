package com.fesine.juc.lock.cas;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAccumulator;

/**
 * @description: LongAdder增强版，可以不仅仅是自增，可通过实现接口策略，自定义实现
 * @author: fesine
 * @createTime:2020/3/18
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/3/18
 */
public class LongAccumulatorDemo {
    public static void main(String[] args) throws InterruptedException {
        //接口方法里第二个参数表示left初始值
        LongAccumulator accumulator = new LongAccumulator((left, right) -> left < right ? left :
                right, 0);
        for (int i = 0; i < 1000; i++) {
            int finaI = i;
            new Thread(()->{
                //此处传入的参数是上面接口中的right参数值
                accumulator.accumulate(finaI);
            }).start();
        }
        TimeUnit.SECONDS.sleep(2);
        System.out.println(accumulator.get());
    }
}
