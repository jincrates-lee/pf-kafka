package me.jincrates.pf.order.messaging.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jincrates.pf.kafka.domain.TopicMessage;
import me.jincrates.pf.kafka.producer.KafkaProducer;
import me.jincrates.pf.order.domain.core.event.OrderAction;
import me.jincrates.pf.order.domain.core.event.OrderCreatedEvent;
import me.jincrates.pf.order.domain.service.port.output.OrderCreatedEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateOrderKafkaPublisher implements OrderCreatedEventPublisher {

    private final KafkaProducer kafkaProducer;

    private String topic = "commerce.order";

    @Override
    public void publish(OrderCreatedEvent event) {
        String orderId = event.getOrder().getId().getValue().toString();
        log.info("주문 생성 이벤트(OrderCreatedEvent)를 카프카 프로듀서에 전송합니다. orderId: {}", orderId);

        try {
            TopicMessage message = TopicMessage.builder()
                .id(orderId)
                .action(OrderAction.ORDER_CREATED.name())
                .actionValue(OrderAction.ORDER_CREATED.getValue())
                .data(event)
                .build();
            kafkaProducer.send(topic, orderId, message);
        } catch (Exception ex) {
            log.error("주문 생성 카프카 메시지 전송 실패 orderId: {}", orderId);
        }
    }
}
