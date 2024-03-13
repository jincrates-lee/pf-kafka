package me.jincrates.pf.order.domain.core.valueobject;

public enum OrderStatus {
    PENDING("대기"),
    COMPLETED("완료"),
    CANCELLED("취소"),
    FAILED("실패"),
    ;

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
