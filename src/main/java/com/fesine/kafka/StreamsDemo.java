package com.fesine.kafka;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStreamBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: 类描述
 * @author: fesine
 * @createTime:2019/12/17
 * @update:修改内容
 * @author: fesine
 * @updateTime:2019/12/17
 */
public class StreamsDemo {

    public static void main(String[] args) {
        Map<String, String> props = new HashMap<>();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "my-stream-processing-application");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        StreamsConfig config = new StreamsConfig(props);

        KStreamBuilder builder = new KStreamBuilder();
        builder.stream("my-input-topic").mapValues(value -> value.toString()).to("my" +
                "-output-topic");
        KafkaStreams streams = new KafkaStreams(builder, config);
        streams.start();


    }
}
