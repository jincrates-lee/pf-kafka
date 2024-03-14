package me.jincrates.pf.order.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jincrates.pf.order.domain.core.event.OrderCancelledEvent;
import me.jincrates.pf.order.domain.core.event.OrderCreatedEvent;
import me.jincrates.pf.order.domain.service.port.output.OrderCancelledEventPublisher;
import me.jincrates.pf.order.domain.service.port.output.OrderCreatedEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventApplicationListener {

    private final OrderCreatedEventPublisher orderCreatedEventPublisher;
    private final OrderCancelledEventPublisher orderCancelledEventPublisher;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void process(OrderCreatedEvent event) {
        orderCreatedEventPublisher.publish(event);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void process(OrderCancelledEvent event) {
        orderCancelledEventPublisher.publish(event);
    }
}
