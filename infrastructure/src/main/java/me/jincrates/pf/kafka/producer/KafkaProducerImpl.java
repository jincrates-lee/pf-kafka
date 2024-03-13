package me.jincrates.pf.kafka.producer;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jincrates.pf.kafka.domain.TopicMessage;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducerImpl<T> implements KafkaProducer<T> {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void send(String topic, String key, TopicMessage<T> message) {
        log.info("Sending to topic = {}, from message: {}", topic, message);
        try {
            kafkaTemplate.send(topic, key, message);
        } catch (KafkaException ex) {
            log.error("Error on kafka producer with key: {}, message: {}, and exception: {}", key,
                message, ex.getMessage());
            throw new RuntimeException("카프카 메시지 발송 오류");
        }
    }

    @PreDestroy
    public void close() {
        if (kafkaTemplate != null) {
            log.info("Closing kafka producer!");
            kafkaTemplate.destroy();
        }
    }
}
