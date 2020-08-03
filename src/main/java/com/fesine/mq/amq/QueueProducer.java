package com.fesine.mq.amq;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @description: 类描述
 * @author: fesine
 * @createTime:2020/8/3
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/8/3
 */
public class QueueProducer {

    public static final String USERNAME = ActiveMQConnection.DEFAULT_USER;

    public static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;

    public static final String BROKER_URL = ActiveMQConnection.DEFAULT_BROKER_URL;

    public static void main(String[] args) {
        //创建连接工厂
        ConnectionFactory connectionFactory =
                new ActiveMQConnectionFactory(USERNAME, PASSWORD, BROKER_URL);

        try {
            //创建连接
            Connection connection = connectionFactory.createConnection();
            //启动连接
            connection.start();
             //创建会话
            Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            //创建队列，需要指定队列名称，消息生产者和消费者将根据它来发送、接收对应的消息
            Queue myTestQueue = session.createQueue("activemq-queue-test1");
            //创建生产者
            MessageProducer producer = session.createProducer(myTestQueue);
            //创建一个消息对象
            TextMessage message = session.createTextMessage("测试点对点的一条消息");
            //生产者发送消息
            producer.send(message);
            //提交事务
            session.commit();
            //关闭资源
            session.close();
            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    //public void sendMessageJMS20(ConnectionFactory connectionFactory, Queue queue, String text) {
    //    try(JMSContext context = connectionFactory.createContext();){
    //        context.createProducer().sent(queue,text);
    //    }catch ()
    //}

}
