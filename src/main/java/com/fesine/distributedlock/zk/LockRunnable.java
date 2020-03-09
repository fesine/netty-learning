package com.fesine.distributedlock.zk;

/**
 * @description: 类描述
 * @author: fesine
 * @createTime:2020/3/9
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/3/9
 */
public class LockRunnable implements Runnable {
    @Override
    public void run() {
        //AbstractLock zkLock = new SimpleLock();
        AbstractLock zkLock = new HighPerformanceZkLock();
        zkLock.getLock();
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        zkLock.releaseLock();
    }
}
