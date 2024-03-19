package me.jincrates.pf.order.dataacess.mapper;

import me.jincrates.pf.order.dataacess.entity.OrderEntity;
import me.jincrates.pf.order.domain.core.entity.Order;
import me.jincrates.pf.order.domain.core.valueobject.OrderId;
import org.springframework.stereotype.Component;

@Component
public class OrderDataAccessMapper {

    public OrderEntity toEntity(Order domain) {
        return OrderEntity.builder()
            .id(domain.getId().getValue())
            .orderStatus(domain.getOrderStatus())
            .amount(domain.getAmount())
            .build();
    }

    public Order toDomain(OrderEntity entity) {
        return Order.builder()
            .id(new OrderId(entity.getId()))
            .orderStatus(entity.getOrderStatus())
            .amount(entity.getAmount())
            .build();
    }
}
