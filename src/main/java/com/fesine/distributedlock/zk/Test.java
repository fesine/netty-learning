package com.fesine.distributedlock.zk;

/**
 * @description: 类描述
 * @author: fesine
 * @createTime:2020/3/9
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/3/9
 */
public class Test {
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(new LockRunnable()).start();
        }
    }
}
