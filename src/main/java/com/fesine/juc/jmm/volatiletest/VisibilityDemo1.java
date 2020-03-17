package com.fesine.juc.jmm.volatiletest;

import java.util.concurrent.TimeUnit;

/**
 * @description: 使用synchronized关键字实现
 * @author: fesine
 * @createTime:2020/3/17
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/3/17
 */
public class VisibilityDemo1 {
    private static boolean flag = true;

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            int i = 0;
            while (VisibilityDemo1.flag) {
                synchronized (VisibilityDemo1.class) {
                    i++;
                }
            }
            System.out.println(i);
        }).start();
        TimeUnit.SECONDS.sleep(2);
        //设置flag为false，终止上面的while循环
        VisibilityDemo1.flag = false;
        System.out.println("flag被设置为false了");
    }
}
