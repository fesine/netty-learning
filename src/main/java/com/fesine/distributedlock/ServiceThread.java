package com.fesine.distributedlock;

/**
 * @description: 类描述
 * @author: fesine
 * @createTime:2020/2/28
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/2/28
 */
public class ServiceThread extends Thread {
    private Service service;

    public ServiceThread(Service service) {
        this.service = service;
    }

    @Override
    public void run() {
        service.seckill();
    }
}
