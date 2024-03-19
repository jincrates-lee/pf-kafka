package me.jincrates.pf.order.messaging.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jincrates.pf.kafka.domain.TopicMessage;
import me.jincrates.pf.kafka.producer.KafkaProducer;
import me.jincrates.pf.order.domain.core.event.OrderCompletedEvent;
import me.jincrates.pf.order.domain.service.port.output.OrderCompletedEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CompletedOrderKafkaPublisher implements OrderCompletedEventPublisher {

    private final KafkaProducer<OrderCompletedEvent> kafkaProducer;

    private String topic = "commerce.order";

    @Override
    public void publish(OrderCompletedEvent event) {
        String orderId = event.getOrder().getId().getValue().toString();
        log.info("Received OrderCompletedEvent for orderId: {}", orderId);

        try {
            TopicMessage<OrderCompletedEvent> message = TopicMessage.<OrderCompletedEvent>builder()
                .id(orderId)
                .action("ORDER_COMPLETED")
                .actionValue("주문 완료됨")
                .data(event)
                .build();
            kafkaProducer.send(topic, orderId, message);
        } catch (Exception ex) {
            log.error("주문 완료 카프카 메시지 전송 실패 orderId: {}", orderId);
        }
    }
}