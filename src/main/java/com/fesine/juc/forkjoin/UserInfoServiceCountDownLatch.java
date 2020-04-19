package com.fesine.juc.forkjoin;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @description: 用户信息查询,使用countDownLatch实现
 * {"userId":"fesine","address":"hangzhou","age":18,"userName":"张三"}
 * @author: fesine
 * @createTime:2020/4/19
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/4/19
 */
public class UserInfoServiceCountDownLatch {

    ExecutorService executorService = Executors.newCachedThreadPool();

    public static void main(String[] args) throws InterruptedException {
        UserInfoServiceCountDownLatch service = new UserInfoServiceCountDownLatch();
        service.getUserAllInfo();
    }

    /**
     * 串行查询信息
     * @return
     */
    private Object getUserAllInfo() throws InterruptedException {
        long startTime = System.currentTimeMillis();
        CountDownLatch countDownLatch = new CountDownLatch(2);
        ArrayList<JSONObject> values = new ArrayList<>();
        executorService.submit(()->{
            long start = System.currentTimeMillis();
            JSONObject object = new JSONObject();
            object.put("userId", "fesine");
            object.put("userName", "张三");
            values.add(object);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("获取用户基本信息耗时为：" + (System.currentTimeMillis() - start));
            countDownLatch.countDown();
        });
        executorService.submit(()->{
            long start = System.currentTimeMillis();
            JSONObject object = new JSONObject();
            object.put("userId", "fesine");
            object.put("age", 18);
            object.put("address", "hangzhou");
            values.add(object);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("获取用户基本扩展信息耗时为：" + (System.currentTimeMillis() - start));
            countDownLatch.countDown();
        });
        countDownLatch.await();
        JSONObject userInfo = new JSONObject();
        for (JSONObject value :values){
            userInfo.putAll(value);
        }
        long end = System.currentTimeMillis();
        System.out.println("获取用户所有信息耗时为：" + (end - startTime));
        System.out.println("用户所有信息：" + userInfo.toJSONString());
        return userInfo;
    }


}
