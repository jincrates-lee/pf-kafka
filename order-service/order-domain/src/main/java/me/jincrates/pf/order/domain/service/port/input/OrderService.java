package me.jincrates.pf.order.domain.service.port.input;

import me.jincrates.pf.order.domain.service.dto.OrderRequest;
import me.jincrates.pf.order.domain.service.dto.OrderResponse;

public interface OrderService {

    OrderResponse createOrder(OrderRequest request);

    void cancelOrder(String orderId);
}
