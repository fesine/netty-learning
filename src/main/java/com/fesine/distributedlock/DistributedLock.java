package com.fesine.distributedlock;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.exceptions.JedisException;

import java.util.List;
import java.util.UUID;

/**
 * @description: 使用redis实现分布式锁
 * @author: fesine
 * @createTime:2020/2/28
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/2/28
 */
public class DistributedLock {
    private final JedisPool jedisPool;

    public DistributedLock(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    /**
     * 加锁
     * @param lockName 锁的key
     * @param acquireTimeout 获取超时时间
     * @param timeout 锁的超时时间
     * @return
     */
    public String lockWithTimeout(String lockName, long acquireTimeout, long timeout) {
        Jedis conn = null;
        String retIdentifier = null;
        try {
            // 获取连接
            conn = jedisPool.getResource();
            String identifier = UUID.randomUUID().toString();
            // 锁名
            String lockKey = "lock:" + lockName;
            // 超时时间，上锁后超过此时间自动释放锁
            int lockExpire = (int) (timeout/1000);
            // 获取锁的超时时间，超过这个时间则自动放弃获取锁
            long end = acquireTimeout + System.currentTimeMillis();
            while (System.currentTimeMillis() < end){
                //上锁成功
                if(conn.setnx(lockKey, identifier) == 1){
                    //设置当前锁的超时时间
                    conn.expire(lockKey,lockExpire);
                    retIdentifier = identifier;
                    return retIdentifier;
                }
                // -1表示没有设置超时时间，为key设置一个超时时间
                if(conn.ttl(lockKey) == -1){
                    //设置当前锁的超时时间
                    conn.expire(lockKey, lockExpire);
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        } catch (JedisException e) {
            e.printStackTrace();
        } finally {
            if(conn != null){
                conn.close();
            }
        }
        return retIdentifier;
    }

    /**
     * 释放锁
     * @param lockName 锁的key
     * @param identifier 释放锁的标识
     * @return
     */
    public boolean releaseLock(String lockName,String identifier){
        Jedis conn = null;
        String lockKey = "lock:"+lockName;
        boolean retFlag = false;
        try {
            conn = jedisPool.getResource();
            while (true){
                // 监控lock，准备开启事务
                conn.watch(lockKey);
                //通过前面返回的value值判断是不是该锁，如果是该值，则删除，释放锁
                if (identifier.equals(conn.get(lockKey))) {
                    Transaction transaction = conn.multi();
                    transaction.del(lockKey);
                    List<Object> result = transaction.exec();
                    if(result == null){
                        continue;
                    }
                    retFlag = true;
                }
                conn.unwatch();
                System.out.println(Thread.currentThread().getName() + " 释放了锁");
                break;
            }
        } catch (JedisException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return retFlag;
    }
}
