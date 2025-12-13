
// modules/order/domain/OrderStatus.java
package com.example.project_al.modules.order.domain;

public enum OrderStatus {
    PENDING,
    PLACED,
    PAID,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    CANCELLED;

    public boolean canCancel() {
        return this == PENDING || this == PLACED;
    }
}