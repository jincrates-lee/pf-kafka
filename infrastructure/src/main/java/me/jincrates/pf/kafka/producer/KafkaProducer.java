package me.jincrates.pf.kafka.producer;

import me.jincrates.pf.kafka.domain.TopicMessage;

public interface KafkaProducer {

    void send(String topic, String key, TopicMessage message);
}
