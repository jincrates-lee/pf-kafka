package me.jincrates.pf.order.domain.service.mapper;

import me.jincrates.pf.order.domain.core.entity.Order;
import me.jincrates.pf.order.domain.service.dto.OrderRequest;
import me.jincrates.pf.order.domain.service.dto.OrderResponse;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public Order toDomain(OrderRequest request) {
        return Order.builder()
            .amount(request.price())
            .build();
    }

    public OrderResponse toResponse(Order domain) {
        return new OrderResponse(
            domain.getId().getValue().toString(),
            domain.getOrderStatus(),
            domain.getAmount()
        );
    }
}
