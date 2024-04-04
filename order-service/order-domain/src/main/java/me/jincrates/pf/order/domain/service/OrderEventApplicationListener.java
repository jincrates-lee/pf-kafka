package me.jincrates.pf.order.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jincrates.pf.order.domain.core.event.OrderCancelledEvent;
import me.jincrates.pf.order.domain.core.event.OrderCompletedEvent;
import me.jincrates.pf.order.domain.core.event.OrderCreatedEvent;
import me.jincrates.pf.order.domain.service.port.output.OrderEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventApplicationListener {

    //    private final OrderCreatedEventPublisher orderCreatedEventPublisher;
//    private final OrderCancelledEventPublisher orderCancelledEventPublisher;
//    private final OrderCompletedEventPublisher orderCompletedEventPublisher;
    private final OrderEventPublisher orderEventPublisher;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void process(OrderCreatedEvent event) {
        log.info("주문 생성 이벤트(OrderCreatedEvent) AFTER_COMMIT - orderId: {}",
            event.getOrder().getId().getValue());
//        orderCreatedEventPublisher.publish(event);
        orderEventPublisher.publish(event);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void process(OrderCancelledEvent event) {
        log.info("주문 취소 이벤트(OrderCancelledEvent) AFTER_COMMIT - orderId: {}",
            event.getOrder().getId().getValue());
//        orderCancelledEventPublisher.publish(event);
        orderEventPublisher.publish(event);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void process(OrderCompletedEvent event) {
        log.info("주문 완료 이벤트(OrderCompletedEvent) AFTER_COMMIT - orderId: {}",
            event.getOrder().getId().getValue());
//        orderCompletedEventPublisher.publish(event);
        orderEventPublisher.publish(event);
    }
}
