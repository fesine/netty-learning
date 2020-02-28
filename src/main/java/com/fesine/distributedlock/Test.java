package com.fesine.distributedlock;

/**
 * @description: 类描述
 * @author: fesine
 * @createTime:2020/2/28
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/2/28
 */
public class Test {
    public static void main(String[] args) {
        Service service = new Service();
        for (int i = 0; i < 50 ; i++) {
            ServiceThread thread = new ServiceThread(service);
            thread.start();
        }
    }
}
