package com.fesine.mq.simple;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @description: 类描述
 * @author: fesine
 * @createTime:2020/8/3
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/8/3
 */
public class Broker {
    /**
     * 队列存储消息的最大数量
     */
    private final static int MAX_SIZE = 3;
    /**
     * 保存消息的容器
     */
    private static ArrayBlockingQueue<String> messageQueue = new ArrayBlockingQueue<>(MAX_SIZE);

    public static void produce(String msg){
        if (messageQueue.offer(msg)) {
            System.out.println("成功向消息处理中心投递消息:" + msg + "，当前暂存的消息数量是：" + messageQueue.size());
        }else{
            System.out.println("消息处理中心内暂存的消息达到最大负荷，不能继续放入消息！");
        }
        System.out.println("=====================");
    }

    public static String consume(){
        String msg = messageQueue.poll();
        if (msg != null) {
            System.out.println("已经消费消息：" + msg + "，当前暂存的消息数量是：" + messageQueue.size());
        }else{
            System.out.println("消息处理中心内没有消息可提供消费！");
        }
        System.out.println("=====================");
        return msg;

    }


}
