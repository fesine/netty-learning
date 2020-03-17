package com.fesine.juc.jmm.volatiletest;

import java.util.concurrent.TimeUnit;

/**
 * @description: 使用volatile关键字实现，不使用缓存
 * @author: fesine
 * @createTime:2020/3/17
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/3/17
 */
public class VisibilityDemo2 {
    private  volatile boolean flag = true;

    public static void main(String[] args) throws InterruptedException {
        VisibilityDemo2 demo = new VisibilityDemo2();
        new Thread(() -> {
            int i = 0;
            while (demo.flag) {
                    i++;
            }
            System.out.println(i);
        }).start();
        TimeUnit.SECONDS.sleep(2);
        //设置flag为false，终止上面的while循环
        demo.flag = false;
        System.out.println("flag被设置为false了");
    }
}
