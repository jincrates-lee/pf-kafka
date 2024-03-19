package me.jincrates.pf.order.messaging.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jincrates.pf.kafka.consumer.KafkaConsumer;
import me.jincrates.pf.kafka.domain.TopicMessage;
import me.jincrates.pf.order.domain.core.event.OrderAction;
import me.jincrates.pf.order.domain.core.event.OrderEvent;
import me.jincrates.pf.order.domain.service.port.input.CommerceOrderResponseListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderMessageKafkaListener implements KafkaConsumer<String> {

    private final CommerceOrderResponseListener commerceOrderResponseListener;

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
                processOrderResponse(topicMessage.getAction(), event);
            } catch (Exception ex) {
                log.error("주문 처리 중 오류 발생. 메시지 keys: {}. 예외 정보: {}",
                    keys, ex.getMessage());
            }
        });
    }

    private void processOrderResponse(String action, OrderEvent event) {
        OrderAction orderAction;
        try {
            orderAction = OrderAction.valueOf(action);
            log.info("{} 처리 중, 주문 ID: {}", orderAction.getValue(),
                event.getOrder().getId().getValue().toString());
            orderAction.process(event, commerceOrderResponseListener);
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException("잘못된 액션입니다. action: " + action
                + ", orderId: " + event.getOrder().getId().getValue().toString(), ex);
        }
    }
}
