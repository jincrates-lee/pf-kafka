package me.jincrates.pf.order.domain.core.event;

import lombok.Getter;
import me.jincrates.pf.order.domain.service.port.input.OrderEventListener;

@Getter
public enum OrderAction {
    ORDER_CREATED("주문 생성됨") {
        @Override
        public void process(OrderEvent event, OrderEventListener listener) {
            listener.orderCreated(new OrderCreatedEvent(event.getOrder(),
                event.getCreatedAt()));
        }
    },
    ORDER_CANCELLED("주문 취소됨") {
        @Override
        public void process(OrderEvent event, OrderEventListener listener) {
            listener.orderCancelled(new OrderCancelledEvent(event.getOrder(),
                event.getCreatedAt()));
        }
    },
    ORDER_COMPLETED("주문 완료됨") {
        @Override
        public void process(OrderEvent event, OrderEventListener listener) {
            listener.orderCompleted(new OrderCompletedEvent(event.getOrder(),
                event.getCreatedAt()));
        }
    },
    ;
    private final String value;

    OrderAction(String value) {
        this.value = value;
    }

    public abstract void process(OrderEvent event, OrderEventListener listener);
}
