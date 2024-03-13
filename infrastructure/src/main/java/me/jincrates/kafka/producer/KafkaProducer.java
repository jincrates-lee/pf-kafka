package me.jincrates.kafka.producer;

public interface KafkaProducer {

    void send(String topic, String key, Object message);
}
