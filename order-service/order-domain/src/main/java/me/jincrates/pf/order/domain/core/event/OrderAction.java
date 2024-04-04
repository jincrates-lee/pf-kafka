package me.jincrates.pf.order.domain.core.event;

import lombok.Getter;

@Getter
public enum OrderAction {
    EMPTY("없음"),
    ORDER_CREATED("주문 생성됨"),
    ORDER_CANCELLED("주문 취소됨"),
    ORDER_COMPLETED("주문 완료됨"),
    ;
    private final String value;

    OrderAction(String value) {
        this.value = value;
    }
}
