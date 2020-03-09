package com.fesine.distributedlock.zk;

import org.I0Itec.zkclient.ZkClient;

/**
 * @description: 类描述
 * @author: fesine
 * @createTime:2020/3/9
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/3/9
 */
public abstract class AbstractLock {
    public static final String ZK_ADDRESS = "127.0.0.1:2181";
    public static final int SESSION_TIMEOUT=10000;

    protected ZkClient zkClient = new ZkClient(ZK_ADDRESS, SESSION_TIMEOUT);

    public void getLock(){
        String threadName = Thread.currentThread().getName();
        if(tryLock()){
            System.out.println(threadName+"-获取锁成功");
        }else{
            System.out.println(threadName+"-获取锁失败");
            waitLock();
            //递归获取锁
            getLock();
        }
    }

    public abstract void releaseLock();

    public abstract boolean tryLock();

    public abstract void waitLock();

}
