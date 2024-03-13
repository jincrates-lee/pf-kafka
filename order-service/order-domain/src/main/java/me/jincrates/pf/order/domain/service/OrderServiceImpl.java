package me.jincrates.pf.order.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jincrates.pf.order.domain.core.entity.Order;
import me.jincrates.pf.order.domain.core.event.OrderCancelledEvent;
import me.jincrates.pf.order.domain.core.event.OrderCreatedEvent;
import me.jincrates.pf.order.domain.service.dto.OrderRequest;
import me.jincrates.pf.order.domain.service.dto.OrderResponse;
import me.jincrates.pf.order.domain.service.mapper.OrderMapper;
import me.jincrates.pf.order.domain.service.port.input.OrderService;
import me.jincrates.pf.order.domain.service.port.output.OrderCancelledEventPublisher;
import me.jincrates.pf.order.domain.service.port.output.OrderCreatedEventPublisher;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final OrderServiceHelper orderServiceHelper;
    private final OrderCreatedEventPublisher orderCreatedEventPublisher;
    private final OrderCancelledEventPublisher orderCancelledEventPublisher;

    @Override
    public OrderResponse createOrder(OrderRequest request) {
        Order order = orderMapper.toDomain(request);
        OrderCreatedEvent event = orderServiceHelper.persistOrder(order);
        orderCreatedEventPublisher.publish(event);
        return orderMapper.toResponse(event.getOrder());
    }

    @Override
    public void cancelOrder(String orderId) {
        OrderCancelledEvent event = orderServiceHelper.cancelOrder(orderId);
        orderCancelledEventPublisher.publish(event);
    }
}
