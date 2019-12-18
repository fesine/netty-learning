package com.fesine.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * @description: 类描述
 * @author: fesine
 * @createTime:2019/12/17
 * @update:修改内容
 * @author: fesine
 * @updateTime:2019/12/17
 */
public class ProducerDemo {

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("acks", "all");
        properties.put("retries", 0);
        properties.put("batch.size", 16384);
        properties.put("linger.ms", 1);
        properties.put("buffer.memory", 33554432);
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization" +
                ".StringSerializer");
        Producer<String, String> producer = new KafkaProducer<String, String>(properties);
        for (int i = 0; i < 100; i++) {
            producer.send(new ProducerRecord<String, String>("my-topic", Integer.toString(i),
                    Integer.toString(i)));

        }
        producer.close();
    }
}
