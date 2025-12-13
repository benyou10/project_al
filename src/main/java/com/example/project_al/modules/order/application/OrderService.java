package com.example.project_al.modules.order.application;

import com.example.project_al.modules.catalog.application.ProductService;
import com.example.project_al.modules.catalog.domain.Product;
import com.example.project_al.modules.order.domain.Order;
import com.example.project_al.modules.order.domain.OrderItem;
import com.example.project_al.modules.order.domain.OrderStatus;
import com.example.project_al.modules.order.infrastructure.OrderRepository;
import com.example.project_al.modules.user.domain.Buyer;
import com.example.project_al.modules.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductService productService;

    public Order createOrder(Order order) {
        // Validate buyer
        if (!(order.getBuyer() instanceof Buyer)) {
            throw new RuntimeException("Only buyers can create orders");
        }

        // Validate and reserve product quantities
        for (OrderItem item : order.getOrderItems()) {
            Product product = item.getProduct();
            if (product.getQuantity() < item.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }
            product.reduceQuantity(item.getQuantity());
            item.setUnitPrice(product.getPrice());
            item.calculateSubtotal();
        }

        order.order();
        return orderRepository.save(order);
    }

    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    public List<Order> findByBuyerId(Long buyerId) {
        return orderRepository.findByBuyerId(buyerId);
    }

    public Page<Order> findByBuyerIdPaged(Long buyerId, Pageable pageable) {
        return orderRepository.findByBuyerIdOrderByDateDesc(buyerId, pageable);
    }

    public List<Order> findByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    public Order updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        switch (status) {
            case CONFIRMED -> order.confirm();
            case CANCELLED -> order.cancel();
            case SHIPPED -> order.ship("TRACK" + orderId);
            case DELIVERED -> order.deliver();
            case PROCESSING -> order.pay();
            default -> throw new RuntimeException("Invalid status transition");
        }

        return orderRepository.save(order);
    }

    public Order addOrderItem(Long orderId, OrderItem item) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Validate product availability
        Product product = item.getProduct();
        if (product.getQuantity() < item.getQuantity()) {
            throw new RuntimeException("Insufficient stock for product: " + product.getName());
        }

        product.reduceQuantity(item.getQuantity());
        item.setUnitPrice(product.getPrice());
        item.setOrder(order);

        order.addOrderItem(item);
        return orderRepository.save(order);
    }

    public Order removeOrderItem(Long orderId, Long itemId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        OrderItem item = order.getOrderItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Order item not found"));

        // Restore product quantity
        item.getProduct().increaseQuantity(item.getQuantity());

        order.removeOrderItem(item);
        return orderRepository.save(order);
    }

    public List<Order> findRecentOrders(LocalDateTime startDate) {
        return orderRepository.findRecentOrders(startDate);
    }

    public BigDecimal getTotalRevenue() {
        return orderRepository.getTotalRevenue();
    }

    public Long countByStatus(OrderStatus status) {
        return orderRepository.countByStatus(status);
    }

    public Page<Order> findOrdersAboveAmount(Double minAmount, Pageable pageable) {
        return orderRepository.findOrdersAboveAmount(minAmount, pageable);
    }

    public List<Order> findByIdUnderlist(String idUnderlist) {
        return orderRepository.findByIdUnderlist(idUnderlist);
    }

    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Restore all product quantities
        order.getOrderItems().forEach(item -> {
            item.getProduct().increaseQuantity(item.getQuantity());
        });

        orderRepository.delete(order);
    }
}