package com.fesine.distributedlock.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.*;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @description: 类描述
 * @author: fesine
 * @createTime:2020/3/10
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/3/10
 */
public class DistributeLockByCurator {
    private  final String lockPath = "/distributed-lock";
    private String connectString;
    private RetryPolicy retry;
    private CuratorFramework client1;
    private CuratorFramework client2;

    @Before
    public void init(){
        connectString = "127.0.0.1:2181";
        retry = new ExponentialBackoffRetry(1000, 3);
        client1 = CuratorFrameworkFactory.newClient(connectString, 60000, 15000, retry);
        client2 = CuratorFrameworkFactory.newClient(connectString, 60000, 15000, retry);
        client1.start();
        client2.start();
    }

    @After
    public void close(){
        CloseableUtils.closeQuietly(client1);
        CloseableUtils.closeQuietly(client2);
    }

    @Test
    public void sharedLock() throws Exception {
        // InterProcessSemaphoreMutex：分布式排它锁
        final InterProcessLock lock1 = new InterProcessSemaphoreMutex(client1, lockPath);
        final InterProcessLock lock2 = new InterProcessSemaphoreMutex(client2, lockPath);
        new Thread(() ->{
                try {
                    lock1.acquire();
                    System.out.println("lock1 获取锁=========");
                    Thread.sleep(5000);
                    lock1.release();
                    System.out.println("lock1 释放锁=========");
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }).start();
        new Thread(() -> {
            try {
                lock2.acquire();
                System.out.println("lock2 获取锁=========");
                Thread.sleep(5000);
                lock2.release();
                System.out.println("lock2 释放锁=========");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        Thread.sleep(20000);
    }

    @Test
    public void sharedReentrantLock() throws Exception {
        // InterProcessMutex：分布式可重入排它锁，重入几次释放几次
        final InterProcessLock lock1 = new InterProcessMutex(client1, lockPath);
        final InterProcessLock lock2 = new InterProcessMutex(client2, lockPath);

        final CountDownLatch countDownLatch = new CountDownLatch(2);
        new Thread(() ->{
                try {
                    lock1.acquire();
                    System.out.println("lock1 获取锁=========");
                    lock1.acquire();
                    System.out.println("lock1 再次获取锁=========");
                    Thread.sleep(5000);
                    lock1.release();
                    System.out.println("lock1 释放锁=========");
                    lock1.release();
                    System.out.println("lock1 再次释放锁=========");
                    countDownLatch.countDown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }).start();
        new Thread(() -> {
            try {
                lock2.acquire();
                System.out.println("lock2 获取锁=========");
                lock2.acquire();
                System.out.println("lock2 再次获取锁=========");
                Thread.sleep(5000);
                lock2.release();
                System.out.println("lock2 释放锁=========");
                lock2.release();
                System.out.println("lock2 再次释放锁=========");
                countDownLatch.countDown();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        countDownLatch.await();
    }

    @Test
    public void sharedReentrantReadWriteLock() throws Exception {
        // InterProcessReadWriteLock：共享可重入读写锁,读锁和读锁不互斥，只要有写锁就互斥
        final InterProcessReadWriteLock lock1 = new InterProcessReadWriteLock(client1, lockPath);
        final InterProcessReadWriteLock lock2 = new InterProcessReadWriteLock(client2, lockPath);
        final InterProcessLock readLock1 = lock1.readLock();
        final InterProcessLock readLock2 = lock2.readLock();
        final CountDownLatch countDownLatch = new CountDownLatch(2);
        new Thread(() ->{
                try {
                    readLock1.acquire();
                    System.out.println("lock1 获取锁=========");
                    readLock1.acquire();
                    System.out.println("lock1 再次获取锁=========");
                    Thread.sleep(5000);
                    readLock1.release();
                    System.out.println("lock1 释放锁=========");
                    readLock1.release();
                    System.out.println("lock1 再次释放锁=========");
                    countDownLatch.countDown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }).start();
        new Thread(() -> {
            try {
                readLock2.acquire();
                System.out.println("lock2 获取锁=========");
                readLock2.acquire();
                System.out.println("lock2 再次获取锁=========");
                Thread.sleep(5000);
                readLock2.release();
                System.out.println("lock2 释放锁=========");
                readLock2.release();
                System.out.println("lock2 再次释放锁=========");
                countDownLatch.countDown();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        countDownLatch.await();
    }

    @Test
    public void semaphore() throws InterruptedException {
        // 创建一个信号量, Curator 以公平锁的方式进行实现,也可以分配多个信号量
        final InterProcessSemaphoreV2 semaphore = new InterProcessSemaphoreV2(client1, lockPath, 3);
        final CountDownLatch countDownLatch = new CountDownLatch(2);
        new Thread(() -> {
            try {
                //获取一个许可
                Collection<Lease> lease =semaphore.acquire(2);
                System.out.println("lock1 获取信号量=========");
                Thread.sleep(5000);
                semaphore.returnAll(lease);
                System.out.println("lock1 释放信号量=========");
                countDownLatch.countDown();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try {
                //获取一个许可
                Collection<Lease> lease = semaphore.acquire(1);
                System.out.println("lock2 获取信号量=========");
                Thread.sleep(5000);
                semaphore.returnAll(lease);
                System.out.println("lock2 释放信号量=========");
                countDownLatch.countDown();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        countDownLatch.await();
    }

    @Test
    public void multiLock() throws InterruptedException {

        //可重入锁
        final InterProcessLock lock1 = new InterProcessMutex(client1, lockPath);
        //不可重入锁
        final InterProcessLock lock2 = new InterProcessSemaphoreMutex(client2, lockPath);

        //创建多重锁对象
        final InterProcessLock lock = new InterProcessMultiLock(Arrays.asList(lock1, lock2));

        final CountDownLatch countDownLatch = new CountDownLatch(1);

        new Thread(
                ()->{
                    try {
                        //获取锁对象
                        lock.acquire();
                        //存在不可重入锁，所以整个InterProcessLock不可用
                        System.out.println(lock.acquire(2, TimeUnit.SECONDS));
                        // lock1是可重入锁，所以可重入
                        System.out.println(lock1.acquire(2, TimeUnit.SECONDS));
                        // lock2不可重入
                        System.out.println(lock2.acquire(2, TimeUnit.SECONDS));
                        countDownLatch.countDown();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        ).start();
        countDownLatch.await();
    }
}
