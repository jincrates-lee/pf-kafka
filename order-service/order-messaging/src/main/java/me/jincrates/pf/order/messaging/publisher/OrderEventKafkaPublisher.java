package me.jincrates.pf.order.messaging.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jincrates.pf.kafka.domain.TopicMessage;
import me.jincrates.pf.kafka.producer.KafkaProducer;
import me.jincrates.pf.order.domain.core.event.OrderAction;
import me.jincrates.pf.order.domain.core.event.OrderCancelledEvent;
import me.jincrates.pf.order.domain.core.event.OrderCompletedEvent;
import me.jincrates.pf.order.domain.core.event.OrderCreatedEvent;
import me.jincrates.pf.order.domain.core.event.OrderEvent;
import me.jincrates.pf.order.domain.service.port.output.OrderEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventKafkaPublisher implements OrderEventPublisher {

    private final KafkaProducer kafkaProducer;

    private String topic = "commerce.order";

    // TODO: 이벤트 전략을 통해 action 처리하도록 수정할 수 있을 것 같다.
    @Override
    public void publish(OrderEvent event) {
        String orderId = event.getOrder().getId().getValue().toString();
        log.info("주문 이벤트(OrderEvent)를 카프카 프로듀서에 전송합니다. orderId: {}", orderId);

        try {
            OrderAction action = retrieveAction(event);
            if (OrderAction.EMPTY.equals(action)) {
                throw new IllegalArgumentException(
                    "잘못된 주문 액션입니다. orderId=" + orderId + " action=" + action);
            }

            TopicMessage message = TopicMessage.builder()
                .id(orderId)
                .action(action.name())
                .actionValue(action.getValue())
                .data(event)
                .build();
            kafkaProducer.send(topic, orderId, message);
        } catch (Exception ex) {
            log.error("주문 이벤트 카프카 메시지 전송 실패 orderId: {}", orderId, ex);
        }
    }

    private OrderAction retrieveAction(OrderEvent event) {
        OrderAction action = OrderAction.EMPTY;
        if (event instanceof OrderCreatedEvent) {
            action = OrderAction.ORDER_CREATED;
        } else if (event instanceof OrderCompletedEvent) {
            action = OrderAction.ORDER_COMPLETED;
        } else if (event instanceof OrderCancelledEvent) {
            action = OrderAction.ORDER_CANCELLED;
        }
        return action;
    }
}
