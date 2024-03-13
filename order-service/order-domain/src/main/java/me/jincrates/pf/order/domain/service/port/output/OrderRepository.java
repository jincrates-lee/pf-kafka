package me.jincrates.pf.order.domain.service.port.output;

import java.util.Optional;
import me.jincrates.pf.order.domain.core.entity.Order;
import me.jincrates.pf.order.domain.core.valueobject.OrderId;

public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findById(OrderId id);
}
