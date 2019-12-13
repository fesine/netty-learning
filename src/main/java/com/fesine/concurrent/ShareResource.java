package com.fesine.concurrent;

/**
 * @description: 类描述
 * @author: fesine
 * @createTime:2019/12/13
 * @update:修改内容
 * @author: fesine
 * @updateTime:2019/12/13
 */
public class ShareResource {

    private String name;
    private String gender;

    /**
     * 模拟生产者向共享资源对象中存储数据
     * @param name
     * @param gender
     */
    public void push(String name,String gender){
        try {
            Thread.sleep(10L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.name=name;
        this.gender=gender;
    }

    /**
     * 模拟消费者从共享资源中取出数据
     */
    public void popup(){
        try {
            Thread.sleep(10L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(this.name+"-"+this.gender);
    }
}
