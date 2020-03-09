package com.fesine.distributedlock.zk;

import org.I0Itec.zkclient.IZkDataListener;

import java.util.concurrent.CountDownLatch;

/**
 * @description: 类描述
 * @author: fesine
 * @createTime:2020/3/9
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/3/9
 */
public class SimpleLock extends AbstractLock {
    private static final String NODE_NAME = "/test_simple_lock";

    private CountDownLatch countDownLatch;


    @Override
    public void releaseLock() {
        if(null != zkClient){
            zkClient.delete(NODE_NAME);
            zkClient.close();
            System.out.println(Thread.currentThread().getName()+"-释放锁成功");
        }

    }

    /**
     * 直接创建节点，如果创建成功，则说明获取锁，创建不成功则处理异常
     * @return
     */
    @Override
    public boolean tryLock() {
        if(null == zkClient){
            return false;
        }
        try {
            zkClient.createEphemeral(NODE_NAME);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    @Override
    public void waitLock() {
        IZkDataListener iZkDataListener = new IZkDataListener() {
            @Override
            public void handleDataChange(String s, Object o) throws Exception {

            }
            //节点删除回调
            @Override
            public void handleDataDeleted(String s) throws Exception {
                if(countDownLatch != null){
                    countDownLatch.countDown();
                }
            }
        };
        zkClient.subscribeDataChanges(NODE_NAME, iZkDataListener);
        if (zkClient.exists(NODE_NAME)) {
            countDownLatch = new CountDownLatch(1);
            try {
                countDownLatch.await();
                System.out.println(Thread.currentThread().getName() + " 等待获取锁...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        zkClient.unsubscribeDataChanges(NODE_NAME,iZkDataListener);

    }
}
