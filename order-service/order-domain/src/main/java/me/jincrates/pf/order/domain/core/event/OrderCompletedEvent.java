package me.jincrates.pf.order.domain.core.event;

import java.time.LocalDateTime;
import me.jincrates.pf.order.domain.core.entity.Order;

public class OrderCompletedEvent extends OrderEvent {

    public OrderCompletedEvent(Order order, LocalDateTime createdAt) {
        super(order, createdAt);
    }
}