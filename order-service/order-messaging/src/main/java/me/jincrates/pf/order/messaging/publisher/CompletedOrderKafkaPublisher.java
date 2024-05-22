package me.jincrates.pf.order.messaging.publisher;

import io.github.springwolf.core.asyncapi.annotations.AsyncOperation;
import io.github.springwolf.core.asyncapi.annotations.AsyncPublisher;
import io.github.springwolf.plugins.kafka.asyncapi.annotations.KafkaAsyncOperationBinding;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jincrates.pf.kafka.domain.TopicMessage;
import me.jincrates.pf.kafka.producer.KafkaProducer;
import me.jincrates.pf.order.domain.core.event.OrderAction;
import me.jincrates.pf.order.domain.core.event.OrderCompletedEvent;
import me.jincrates.pf.order.domain.service.port.output.OrderCompletedEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CompletedOrderKafkaPublisher implements OrderCompletedEventPublisher {

    private final KafkaProducer kafkaProducer;

    private String topic = "commerce.order";

    @AsyncPublisher(
        operation = @AsyncOperation(
            channelName = "commerce.order",
            description = "More details for the outgoing topic"
        )
    )
    @KafkaAsyncOperationBinding  // Kafka 태깅이 붙음
    @Override
    public void publish(OrderCompletedEvent event) {
        String orderId = event.getOrder().getId().getValue().toString();
        log.info("주문 완료 이벤트(OrderCompletedEvent)를 카프카 프로듀서에 전송합니다. orderId: {}", orderId);

        try {
            TopicMessage message = TopicMessage.builder()
                .id(orderId)
                .action(OrderAction.ORDER_COMPLETED.name())
                .actionValue(OrderAction.ORDER_COMPLETED.getValue())
                .data(event)
                .build();
            kafkaProducer.send(topic, orderId, message);
        } catch (Exception ex) {
            log.error("주문 완료 카프카 메시지 전송 실패 orderId: {}", orderId);
        }
    }
}
