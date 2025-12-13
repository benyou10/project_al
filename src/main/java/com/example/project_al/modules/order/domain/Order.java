// modules/order/domain/Order.java
package com.example.project_al.modules.order.domain;

import com.example.project_al.modules.catalog.domain.Product;
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
public class Order extends BaseEntity {

    @Column(name = "id_underlist")
    private String idUnderlist;  // From UML: Id_Underlist

    @Column(name = "date_created")
    private LocalDateTime dateCreated = LocalDateTime.now();  // From UML: dateItemssteps

    @Column(name = "order_data", columnDefinition = "TEXT")
    private String data;  // From UML: dataString

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private Buyer buyer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;
    @Id
    private Long id;

    // From UML: order()
    public void placeOrder() {
        this.status = OrderStatus.PLACED;
        this.dateCreated = LocalDateTime.now();
        calculateTotal();
    }

    // From UML: cancel()
    public void cancel() {
        if (this.status.canCancel()) {
            this.status = OrderStatus.CANCELLED;
            // Return stock to inventory
            orderItems.forEach(item -> {
                Product product = item.getProduct();
                product.setQuantity(product.getQuantity() + item.getQuantity());
            });
        }
    }

    // From UML: pay()
    public boolean pay(String paymentMethod) {
        if (this.status == OrderStatus.PLACED) {
            this.status = OrderStatus.PAID;
            // Process payment logic here
            return true;
        }
        return false;
    }

    private void calculateTotal() {
        this.totalAmount = orderItems.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}


