package me.jincrates.pf.order.domain.service;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jincrates.pf.order.domain.core.OrderDomainService;
import me.jincrates.pf.order.domain.core.entity.Order;
import me.jincrates.pf.order.domain.core.event.OrderCancelledEvent;
import me.jincrates.pf.order.domain.core.event.OrderCreatedEvent;
import me.jincrates.pf.order.domain.core.valueobject.OrderId;
import me.jincrates.pf.order.domain.service.port.output.OrderRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderServiceHelper {

    private final OrderDomainService orderDomainService;
    private final OrderRepository orderRepository;

    @Transactional
    public OrderCreatedEvent persistOrder(Order order) {
        OrderCreatedEvent event = orderDomainService.createOrder(order);
        saveOrder(event.getOrder());
        return event;
    }

    @Transactional
    public OrderCancelledEvent cancelOrder(String orderId) {
        Order order = findOrder(orderId);
        OrderCancelledEvent event = orderDomainService.cancelOrder(order);
        updateOrder(event.getOrder());
        return event;
    }

    @Transactional(readOnly = true)
    public Order findOrder(String orderId) {
        return orderRepository.findById(new OrderId(UUID.fromString(orderId)))
            .orElseThrow(() -> {
                log.info("주문을 찾을 수 없습니다! id: {}", orderId);
                return new RuntimeException("주문을 찾을 수 없습니다!");
            });
    }

    private void saveOrder(Order order) {
        Order result = orderRepository.save(order);
        if (result == null) {
            log.warn("주문을 저장할 수 없습니다! orderId: {}", order.getId().getValue());
            throw new RuntimeException("주문을 저장할 수 없습니다!");
        }
        log.info("주문이 저장되었습니다. orderId: {}", result.getId().getValue());
    }


    private void updateOrder(Order order) {
        Order result = orderRepository.save(order);
        if (result == null) {
            log.warn("주문을 수정할 수 없습니다! orderId: {}", order.getId().getValue());
            throw new RuntimeException("주문을 저장할 수 없습니다!");
        }
        log.info("주문이 수정되었습니다. orderId: {}", result.getId().getValue());
    }
}
