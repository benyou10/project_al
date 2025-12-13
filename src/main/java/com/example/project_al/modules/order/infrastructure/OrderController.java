package com.example.project_al.modules.order.infrastructure;

import com.example.project_al.modules.order.application.OrderService;
import com.example.project_al.modules.order.domain.Order;
import com.example.project_al.modules.order.domain.OrderItem;
import com.example.project_al.modules.order.domain.OrderStatus;
import com.example.project_al.shared.kernel.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Order Management", description = "APIs for managing orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Create a new order")
    public ResponseEntity<ApiResponse<Order>> createOrder(@Valid @RequestBody Order order) {
        Order created = orderService.createOrder(order);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Order created successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID")
    public ResponseEntity<ApiResponse<Order>> getOrder(@PathVariable Long id) {
        return orderService.findById(id)
                .map(order -> ResponseEntity.ok(ApiResponse.success(order)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Order not found")));
    }

    @GetMapping("/buyer/{buyerId}")
    @Operation(summary = "Get orders by buyer")
    public ResponseEntity<ApiResponse<List<Order>>> getOrdersByBuyer(@PathVariable Long buyerId) {
        List<Order> orders = orderService.findByBuyerId(buyerId);
        return ResponseEntity.ok(ApiResponse.success(orders));
    }

    @GetMapping("/buyer/{buyerId}/paged")
    @Operation(summary = "Get orders by buyer (paged)")
    public ResponseEntity<ApiResponse<Page<Order>>> getOrdersByBuyerPaged(
            @PathVariable Long buyerId,
            @Parameter(hidden = true) Pageable pageable) {
        Page<Order> orders = orderService.findByBuyerIdPaged(buyerId, pageable);
        return ResponseEntity.ok(ApiResponse.success(orders));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get orders by status")
    public ResponseEntity<ApiResponse<List<Order>>> getOrdersByStatus(@PathVariable OrderStatus status) {
        List<Order> orders = orderService.findByStatus(status);
        return ResponseEntity.ok(ApiResponse.success(orders));
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update order status")
    public ResponseEntity<ApiResponse<Order>> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status) {
        Order updated = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success(updated, "Order status updated successfully"));
    }

    @PostMapping("/{orderId}/items")
    @Operation(summary = "Add item to order")
    public ResponseEntity<ApiResponse<Order>> addOrderItem(
            @PathVariable Long orderId,
            @Valid @RequestBody OrderItem item) {
        Order updated = orderService.addOrderItem(orderId, item);
        return ResponseEntity.ok(ApiResponse.success(updated, "Item added to order successfully"));
    }

    @DeleteMapping("/{orderId}/items/{itemId}")
    @Operation(summary = "Remove item from order")
    public ResponseEntity<ApiResponse<Order>> removeOrderItem(
            @PathVariable Long orderId,
            @PathVariable Long itemId) {
        Order updated = orderService.removeOrderItem(orderId, itemId);
        return ResponseEntity.ok(ApiResponse.success(updated, "Item removed from order successfully"));
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = "Cancel order")
    public ResponseEntity<ApiResponse<Order>> cancelOrder(@PathVariable Long id) {
        Order cancelled = orderService.updateOrderStatus(id, OrderStatus.CANCELLED);
        return ResponseEntity.ok(ApiResponse.success(cancelled, "Order cancelled successfully"));
    }

    @PostMapping("/{id}/pay")
    @Operation(summary = "Pay for order")
    public ResponseEntity<ApiResponse<Order>> payOrder(@PathVariable Long id) {
        Order paid = orderService.updateOrderStatus(id, OrderStatus.PROCESSING);
        return ResponseEntity.ok(ApiResponse.success(paid, "Order payment processed successfully"));
    }

    @GetMapping("/recent")
    @Operation(summary = "Get recent orders")
    public ResponseEntity<ApiResponse<List<Order>>> getRecentOrders(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate) {
        List<Order> orders = orderService.findRecentOrders(startDate);
        return ResponseEntity.ok(ApiResponse.success(orders));
    }

    @GetMapping("/revenue")
    @Operation(summary = "Get total revenue")
    public ResponseEntity<ApiResponse<BigDecimal>> getTotalRevenue() {
        BigDecimal revenue = orderService.getTotalRevenue();
        return ResponseEntity.ok(ApiResponse.success(revenue));
    }

    @GetMapping("/stats/status")
    @Operation(summary = "Get order statistics by status")
    public ResponseEntity<ApiResponse<Long>> getOrderCountByStatus(@RequestParam OrderStatus status) {
        Long count = orderService.countByStatus(status);
        return ResponseEntity.ok(ApiResponse.success(count));
    }

    @GetMapping("/amount-above")
    @Operation(summary = "Get orders above amount")
    public ResponseEntity<ApiResponse<Page<Order>>> getOrdersAboveAmount(
            @RequestParam Double minAmount,
            @Parameter(hidden = true) Pageable pageable) {
        Page<Order> orders = orderService.findOrdersAboveAmount(minAmount, pageable);
        return ResponseEntity.ok(ApiResponse.success(orders));
    }

    @GetMapping("/underlist/{idUnderlist}")
    @Operation(summary = "Get orders by id underlist")
    public ResponseEntity<ApiResponse<List<Order>>> getOrdersByIdUnderlist(@PathVariable String idUnderlist) {
        List<Order> orders = orderService.findByIdUnderlist(idUnderlist);
        return ResponseEntity.ok(ApiResponse.success(orders));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete order")
    public ResponseEntity<ApiResponse<String>> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok(ApiResponse.success("Order deleted successfully"));
    }
}