package me.jincrates.pf.order.messaging.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jincrates.kafka.producer.KafkaProducer;
import me.jincrates.pf.order.domain.core.event.OrderCancelledEvent;
import me.jincrates.pf.order.domain.service.port.output.OrderCancelledEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CancelledOrderKafkaPublisher implements OrderCancelledEventPublisher {

    private final KafkaProducer kafkaProducer;

    private String topic = "cancel-order-topic";

    @Override
    public void publish(OrderCancelledEvent event) {
        String orderId = event.getOrder().getId().getValue().toString();
        log.info("Received OrderCancelledEvent for orderId: {}", orderId);

        try {
            kafkaProducer.send(topic, orderId, event);
        } catch (Exception ex) {
            log.error("주문취소 카프카 메시지 전송 실패 orderId: {}", orderId);
        }
    }
}
