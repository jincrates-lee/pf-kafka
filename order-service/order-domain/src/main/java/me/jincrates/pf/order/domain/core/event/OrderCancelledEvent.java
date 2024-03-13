package me.jincrates.pf.order.domain.core.event;

import java.time.LocalDateTime;
import me.jincrates.pf.order.domain.core.entity.Order;

public class OrderCancelledEvent extends OrderEvent {

    public OrderCancelledEvent(Order order, LocalDateTime createdAt) {
        super(order, createdAt);
    }
}
