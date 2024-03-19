package me.jincrates.pf.order.domain.service.port.input;

import me.jincrates.pf.order.domain.core.event.OrderCancelledEvent;
import me.jincrates.pf.order.domain.core.event.OrderCompletedEvent;
import me.jincrates.pf.order.domain.core.event.OrderCreatedEvent;

public interface OrderEventListener {

    void orderCreated(OrderCreatedEvent event);

    void orderCancelled(OrderCancelledEvent event);

    void orderCompleted(OrderCompletedEvent event);
}
