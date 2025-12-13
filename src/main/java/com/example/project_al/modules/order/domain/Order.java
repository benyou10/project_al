package com.example.project_al.modules.order.domain;

import com.example.project_al.modules.user.domain.Buyer;
import com.example.project_al.shared.kernel.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order extends BaseEntity {

    @Column(name = "id_underlist")
    private String idUnderlist;

    @Column(name = "date_timestamp")
    private LocalDateTime dateTimestamp;

    @Column(name = "data_string", columnDefinition = "TEXT")
    private String dataString;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status = OrderStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
    private Buyer buyer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Column(name = "shipping_address")
    private String shippingAddress;

    @Column(name = "billing_address")
    private String billingAddress;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "tracking_number")
    private String trackingNumber;

    @PrePersist
    public void prePersist() {
        if (dateTimestamp == null) {
            dateTimestamp = LocalDateTime.now();
        }
    }

    public void order() {
        this.status = OrderStatus.PLACED;
        calculateTotal();
    }

    public void cancel() {
        if (this.status.canCancel()) {
            this.status = OrderStatus.CANCELLED;
            // Restore product quantities
            orderItems.forEach(item -> {
                item.getProduct().increaseQuantity(item.getQuantity());
            });
        } else {
            throw new RuntimeException("Order cannot be cancelled in current status: " + status);
        }
    }

    public void pay() {
        if (this.status.canPay()) {
            this.status = OrderStatus.PROCESSING;
            // Process payment logic here
        } else {
            throw new RuntimeException("Order cannot be paid in current status: " + status);
        }
    }

    public void confirm() {
        if (this.status == OrderStatus.PLACED) {
            this.status = OrderStatus.CONFIRMED;
        }
    }

    public void ship(String trackingNumber) {
        if (this.status == OrderStatus.CONFIRMED) {
            this.status = OrderStatus.SHIPPED;
            this.trackingNumber = trackingNumber;
        }
    }

    public void deliver() {
        if (this.status == OrderStatus.SHIPPED) {
            this.status = OrderStatus.DELIVERED;
        }
    }

    private void calculateTotal() {
        this.totalAmount = orderItems.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void addOrderItem(OrderItem item) {
        item.setOrder(this);
        orderItems.add(item);
        calculateTotal();
    }

    public void removeOrderItem(OrderItem item) {
        orderItems.remove(item);
        item.setOrder(null);
        calculateTotal();
    }
}