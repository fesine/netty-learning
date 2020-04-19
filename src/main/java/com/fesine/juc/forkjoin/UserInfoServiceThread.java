package com.fesine.juc.forkjoin;

import com.alibaba.fastjson.JSONObject;

/**
 * @description: 使用多线程进行用户信息查询
 * {"userId":"fesine","address":"hangzhou","age":18,"userName":"张三"}
 * @author: fesine
 * @createTime:2020/4/19
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/4/19
 */
public class UserInfoServiceThread {

    public static void main(String[] args) {
        UserInfoServiceThread service = new UserInfoServiceThread();
        service.getUserAllInfo();
    }

    /**
     * 并行查询，直接使用多线程无法获取结果
     * @return
     */
    private Object getUserAllInfo(){
        long start = System.currentTimeMillis();
        new Thread(()->{
            JSONObject userInfo = getUserInfo();
        }).start();
        new Thread(()->{
            JSONObject userExtraInfo = getUserExtraInfo();
        }).start();

        long end = System.currentTimeMillis();
        System.out.println("获取用户所有信息耗时为：" + (end - start));
        //System.out.println("用户所有信息：" + result.toJSONString());
        return null;
    }

    /**
     * 模拟获取用户基本信息
     * @return
     */
    private JSONObject getUserInfo(){
        long start = System.currentTimeMillis();
        JSONObject object = new JSONObject();
        object.put("userId", "fesine");
        object.put("userName", "张三");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("获取用户基本信息耗时为："+(System.currentTimeMillis()-start));
        return object;
    }

    /**
     * 模拟获取用户扩展信息
     * @return
     */
    private JSONObject getUserExtraInfo(){
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
    }

}
