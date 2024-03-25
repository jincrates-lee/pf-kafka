package me.jincrates.pf.order.messaging.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jincrates.pf.kafka.consumer.KafkaConsumer;
import me.jincrates.pf.kafka.domain.TopicMessage;
import me.jincrates.pf.order.domain.core.event.OrderCancelledEvent;
import me.jincrates.pf.order.domain.core.event.OrderCompletedEvent;
import me.jincrates.pf.order.domain.core.event.OrderCreatedEvent;
import me.jincrates.pf.order.domain.core.event.OrderEvent;
import me.jincrates.pf.order.domain.service.port.input.OrderEventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderMessageKafkaListener implements KafkaConsumer<String> {

    private final OrderEventListener orderEventListener;

    @Override
    @KafkaListener(topics = "commerce.order", groupId = "commerce-order-group-id")
    public void receive(
        @Payload List<String> messages,
        @Header(KafkaHeaders.RECEIVED_KEY) List<String> keys,
        @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
        @Header(KafkaHeaders.OFFSET) List<Long> offsets
    ) {
        log.info(
            "주문 토픽(commerce.order)에서 {}건을 수신하였습니다. keys: {}, partitions: {}, offsets: {}",
            messages.size(), keys.toString(), partitions.toString(), offsets.toString());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        messages.forEach(message -> {
            try {
                TopicMessage topicMessage = objectMapper.readValue(message, TopicMessage.class);
                OrderEvent event = objectMapper.convertValue(topicMessage.getData(),
                    OrderEvent.class);
                processOrderEvent(topicMessage.getAction(), event);
            } catch (Exception ex) {
                log.error("주문 처리 중 오류 발생. 메시지 keys: {}. 예외 정보: {}",
                    keys, ex.getMessage());
            }
        });
    }


    private void processOrderEvent(String action, OrderEvent event) {
        switch (action) {
            case "PENDING":
                log.info("대기 중인 주문 처리 중. 주문 ID: {}", event.getOrder().getId().getValue());
                orderEventListener.orderCreated(new OrderCreatedEvent(event.getOrder(),
                    event.getCreatedAt()));
                break;
            case "COMPLETED":
                log.info("완료 주문 처리 중. 주문 ID: {}", event.getOrder().getId().getValue());
                orderEventListener.orderCompleted(new OrderCompletedEvent(event.getOrder(),
                    event.getCreatedAt()));
                break;
            case "CANCELLED":
                log.info("취소 주문 처리 중. 주문 ID: {}", event.getOrder().getId().getValue());
                orderEventListener.orderCancelled(new OrderCancelledEvent(event.getOrder(),
                    event.getCreatedAt()));
                break;
            case "FAILED":
                log.info("실패 주문 처리 중. 주문 ID: {}", event.getOrder().getId().getValue());
                break;
            default:
                log.error("알 수 없는 주문 상태입니다! 주문 ID: {}", event.getOrder().getId().getValue());
                break;
        }
    }
}
