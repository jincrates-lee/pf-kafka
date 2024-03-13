package me.jincrates.kafka.producer;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class KafkaProducerImpl implements KafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void send(String topic, String key, Object message) {
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
