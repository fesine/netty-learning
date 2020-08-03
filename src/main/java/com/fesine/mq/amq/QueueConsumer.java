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
public class QueueConsumer {

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
            final Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            //创建队列，需要指定队列名称，消息生产者和消费者将根据它来发送、接收对应的消息
            Queue myTestQueue = session.createQueue("activemq-queue-test1");
            //创建消费者
            MessageConsumer consumer = session.createConsumer(myTestQueue);
            consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    try {
                        TextMessage textMessage = (TextMessage) message;
                        System.out.println(textMessage.getText());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                    //提交事务
                    try {
                        session.commit();
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            });

            Thread.sleep(100*1000);
            //关闭资源
            session.close();
            connection.close();
        } catch (JMSException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
