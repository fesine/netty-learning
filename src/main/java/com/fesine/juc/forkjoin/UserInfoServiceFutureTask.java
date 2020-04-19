package com.fesine.juc.forkjoin;

import com.alibaba.fastjson.JSONObject;

import java.util.concurrent.*;

/**
 * @description: 用户信息查询,使用countDownLatch实现
 * {"userId":"fesine","address":"hangzhou","age":18,"userName":"张三"}
 * @author: fesine
 * @createTime:2020/4/19
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/4/19
 */
public class UserInfoServiceFutureTask {

    //ExecutorService executorService = Executors.newCachedThreadPool();

    public static void main(String[] args) {
        UserInfoServiceFutureTask service = new UserInfoServiceFutureTask();
        service.getUserAllInfo();
    }

    /**
     * 串行查询信息
     * @return
     */
    private Object getUserAllInfo() {
        long startTime = System.currentTimeMillis();
        Callable<JSONObject> callable = () -> {
            long start = System.currentTimeMillis();
            JSONObject object = new JSONObject();
            object.put("userId", "fesine");
            object.put("userName", "张三");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("获取用户基本信息耗时为：" + (System.currentTimeMillis() - start));
            return object;
        };

        //FutureTask<JSONObject> userInfoFutureTask = new FutureTask<>(callable);
        MyFutureTask<JSONObject> userInfoFutureTask = new MyFutureTask<>(callable);
        new Thread(userInfoFutureTask).start();
        Callable<JSONObject> callable1 = () -> {
            long start = System.currentTimeMillis();
            JSONObject object = new JSONObject();
            object.put("userId", "fesine");
            object.put("age", 18);
            object.put("address", "hangzhou");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("获取用户基本扩展信息耗时为：" + (System.currentTimeMillis() - start));
            return object;
        };

        //FutureTask<JSONObject> userExtraInfoFutureTask = new FutureTask<>(callable1);
        MyFutureTask<JSONObject> userExtraInfoFutureTask = new MyFutureTask<>(callable1);
        new Thread(userExtraInfoFutureTask).start();
        JSONObject userInfo = new JSONObject();
        userInfo.putAll(userInfoFutureTask.get());
        userInfo.putAll(userExtraInfoFutureTask.get());
        long end = System.currentTimeMillis();
        System.out.println("获取用户所有信息耗时为：" + (end - startTime));
        System.out.println("用户所有信息：" + userInfo.toJSONString());
        return userInfo;
    }


}
