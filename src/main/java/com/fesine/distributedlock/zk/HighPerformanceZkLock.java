package com.fesine.distributedlock.zk;

import org.I0Itec.zkclient.IZkDataListener;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @description: 类描述
 * @author: fesine
 * @createTime:2020/3/9
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/3/9
 */
public class HighPerformanceZkLock extends AbstractLock {

    private static final String PATH = "/highPerformance_zklock";
    //当前节点路径
    private String currentPath;

    //前一个节点的路径
    private String beforePath;

    private CountDownLatch countDownLatch = null;

    public HighPerformanceZkLock() {
        if (!zkClient.exists(PATH)) {
            zkClient.createPersistent(PATH);
        }
    }

    @Override
    public void releaseLock() {
        if (zkClient != null) {
            zkClient.delete(currentPath);
            zkClient.close();
            //System.out.println(Thread.currentThread().getName() + "-释放锁成功");
        }

    }

    @Override
    public boolean tryLock() {
        //如果currentPath为空则尝试第一次加锁，第一次加锁赋值currentPath
        if (null == currentPath || "".equals(currentPath)) {
            //在path下面创建一个临时的顺序节点
            currentPath = zkClient.createEphemeralSequential(PATH + "/", "lock");
            System.out.println(Thread.currentThread().getName()+"-currentPath:"+currentPath);
        }
        List<String> childrens = zkClient.getChildren(PATH);
        Collections.sort(childrens);
        if (currentPath.equals(PATH + "/" + childrens.get(0))) {
            return true;
        }else{
            //如果不是排名第一，则获取前面的节点名称，并赋值给beforePath
            int pathLength = PATH.length();
            int wz = Collections.binarySearch(childrens, currentPath.substring(pathLength + 1));
            beforePath = PATH + "/" + childrens.get(wz - 1);
            System.out.println(Thread.currentThread().getName() + "-beforePath:" + beforePath);
        }
        return false;
    }

    @Override
    public void waitLock() {
        IZkDataListener iZkDataListener = new IZkDataListener() {
            @Override
            public void handleDataChange(String s, Object o) throws Exception {

            }

            @Override
            public void handleDataDeleted(String s) throws Exception {
                if(countDownLatch != null){
                    countDownLatch.countDown();
                }
            }
        };
        zkClient.subscribeDataChanges(beforePath, iZkDataListener);
        if (zkClient.exists(beforePath)) {
            countDownLatch = new CountDownLatch(1);
            try {
                countDownLatch.await();
                System.out.println(Thread.currentThread().getName() + " 等待获取锁...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        zkClient.unsubscribeDataChanges(beforePath,iZkDataListener);

    }
}
