package com.fesine.mq.simple;

/**
 * @description: 类描述
 * @author: fesine
 * @createTime:2020/8/3
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/8/3
 */
public class  ProduceClient {

    public static void main(String[] args) throws Exception {
        MqClient.produce("Hello world");
    }
}
