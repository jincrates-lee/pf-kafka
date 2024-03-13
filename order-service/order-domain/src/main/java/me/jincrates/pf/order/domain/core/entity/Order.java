package me.jincrates.pf.order.domain.core.entity;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import me.jincrates.pf.order.domain.core.valueobject.OrderId;
import me.jincrates.pf.order.domain.core.valueobject.OrderStatus;

@Slf4j
@Getter
public class Order {

    private OrderId id;
    private OrderStatus orderStatus;
    private int amount;

    @Builder
    public Order(OrderId id, OrderStatus orderStatus, int amount) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.amount = amount;
    }

    public void initialize() {
        this.id = new OrderId(UUID.randomUUID());
        this.orderStatus = OrderStatus.PENDING;
    }

    public void cancel() {
        if (!(orderStatus == OrderStatus.PENDING || orderStatus == OrderStatus.COMPLETED)) {
            log.warn("주문을 취소할 수 없는 상태입니다! status: {}", orderStatus);
            throw new RuntimeException("주문을 취소할 수 없는 상태입니다!");
        }
        orderStatus = OrderStatus.CANCELLED;
    }

}
