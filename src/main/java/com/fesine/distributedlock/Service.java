package com.fesine.distributedlock;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @description: 测试分布式锁
 * @author: fesine
 * @createTime:2020/2/28
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/2/28
 */
public class Service {

    private static JedisPool pool = null;

    private DistributedLock lock = new DistributedLock(pool);
    int n= 500;
    static {
        JedisPoolConfig config = new JedisPoolConfig();
        //设置最大连接数
        config.setMaxTotal(200);
        // 设置最大空闲数
        config.setMaxIdle(8);
        //设置最在等待时间
        config.setMaxWaitMillis(1000 * 100);
        //设置borrow一个jedis实例时，是否需要验证，若为true，则所有的jedis实例都是可用的
        config.setTestOnBorrow(true);
        pool = new JedisPool(config, "10.211.55.130", 6379, 300);
    }

    public void seckill(){
        //返回锁的value值，供释放锁时进行判断
        String identifier = lock.lockWithTimeout("resource", 5000, 1000);
        System.out.println(Thread.currentThread().getName() + " 获取了锁");
        System.out.println(n--);
        lock.releaseLock("resource", identifier);
    }
}
