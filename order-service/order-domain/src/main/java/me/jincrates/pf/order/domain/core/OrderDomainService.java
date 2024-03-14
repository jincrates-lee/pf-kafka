package me.jincrates.pf.order.domain.core;

import me.jincrates.pf.order.domain.core.entity.Order;
import me.jincrates.pf.order.domain.core.event.OrderCancelledEvent;
import me.jincrates.pf.order.domain.core.event.OrderCompletedEvent;
import me.jincrates.pf.order.domain.core.event.OrderCreatedEvent;

public interface OrderDomainService {

    OrderCreatedEvent createOrder(Order order);

    OrderCancelledEvent cancelOrder(Order order);

    OrderCompletedEvent completeOrder(Order order);
}
