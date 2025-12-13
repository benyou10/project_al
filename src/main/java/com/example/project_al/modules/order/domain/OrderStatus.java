package com.example.project_al.modules.order.domain;

public enum OrderStatus {
    PENDING,
    PLACED,
    CONFIRMED,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    CANCELLED,
    REFUNDED;

    public boolean canCancel() {
        return this == PENDING || this == PLACED;
    }

    public boolean canPay() {
        return this == PLACED || this == CONFIRMED;
    }
}