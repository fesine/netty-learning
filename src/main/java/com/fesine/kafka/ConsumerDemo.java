package com.fesine.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * @description: 类描述
 * @author: fesine
 * @createTime:2019/12/17
 * @update:修改内容
 * @author: fesine
 * @updateTime:2019/12/17
 */
public class ConsumerDemo {

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "test");
        //props.put("enable.auto.commit", "true");
        props.put("enable.auto.commit", "false");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");

        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
        consumer.subscribe(Arrays.asList("foo", "bar"));
        final int minBatchSize = 200;
        List<ConsumerRecord<String, String>> buffer = new ArrayList<>();
        while (true) {
            ConsumerRecords<String, String> records= consumer.poll(100);
            //for (ConsumerRecord<String, String> record : records) {
            //    System.out.printf("offset=%d,key=%s,value=%s%n", record.offset(), record.key(),
            //            record.value());
            //}
            for (ConsumerRecord<String, String> record : records) {
                buffer.add(record);
            }
            if (buffer.size() >= minBatchSize) {
                //此处可进行持久化处理
                //insertIntoDb(buffer);
                consumer.commitSync();
                buffer.clear();
            }

        }
    }
}
