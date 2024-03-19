package me.jincrates.pf.order.messaging.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jincrates.pf.kafka.domain.TopicMessage;
import me.jincrates.pf.kafka.producer.KafkaProducer;
import me.jincrates.pf.order.domain.core.event.OrderAction;
import me.jincrates.pf.order.domain.core.event.OrderCancelledEvent;
import me.jincrates.pf.order.domain.service.port.output.OrderCancelledEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CancelledOrderKafkaPublisher implements OrderCancelledEventPublisher {

    private final KafkaProducer<OrderCancelledEvent> kafkaProducer;

    private String topic = "commerce.order";

    @Override
    public void publish(OrderCancelledEvent event) {
        String orderId = event.getOrder().getId().getValue().toString();
        log.info("Received OrderCancelledEvent for orderId: {}", orderId);

        try {
            TopicMessage<OrderCancelledEvent> message = TopicMessage.<OrderCancelledEvent>builder()
                .id(orderId)
                .action(OrderAction.ORDER_CANCELLED.name())
                .actionValue(OrderAction.ORDER_CANCELLED.getValue())
                .data(event)
                .build();
            kafkaProducer.send(topic, orderId, message);
        } catch (Exception ex) {
            log.error("주문 취소 카프카 메시지 전송 실패 orderId: {}", orderId);
        }
    }
}
