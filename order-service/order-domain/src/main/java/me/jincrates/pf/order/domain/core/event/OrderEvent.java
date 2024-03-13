package me.jincrates.pf.order.domain.core.event;

import java.time.LocalDateTime;
import lombok.Getter;
import me.jincrates.pf.order.domain.core.entity.Order;

@Getter
public abstract class OrderEvent implements DomainEvent<Order> {

    private final Order order;
    private final LocalDateTime createdAt;

    public OrderEvent(Order order, LocalDateTime createdAt) {
        this.order = order;
        this.createdAt = createdAt;
    }
}
