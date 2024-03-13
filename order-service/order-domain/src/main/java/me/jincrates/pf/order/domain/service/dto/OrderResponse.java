package me.jincrates.pf.order.domain.service.dto;

import me.jincrates.pf.order.domain.core.valueobject.OrderStatus;

public record OrderResponse(
    String id,
    OrderStatus orderStatus,
    int amount
) {

}
