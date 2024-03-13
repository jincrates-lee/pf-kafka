package me.jincrates.pf.order.domain.core;

import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import me.jincrates.pf.order.domain.core.entity.Order;
import me.jincrates.pf.order.domain.core.event.OrderCancelledEvent;
import me.jincrates.pf.order.domain.core.event.OrderCreatedEvent;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderDomainServiceImpl implements OrderDomainService {

    @Override
    public OrderCreatedEvent createOrder(Order order) {
        order.initialize();
        return new OrderCreatedEvent(order, LocalDateTime.now());
    }

    @Override
    public OrderCancelledEvent cancelOrder(Order order) {
        order.cancel();
        return new OrderCancelledEvent(order, LocalDateTime.now());
    }
}
