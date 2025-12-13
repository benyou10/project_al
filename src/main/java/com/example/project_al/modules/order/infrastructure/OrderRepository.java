package com.example.project_al.modules.order.infrastructure;

import com.example.project_al.modules.order.domain.Order;
import com.example.project_al.modules.order.domain.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByBuyerId(Long buyerId);

    List<Order> findByStatus(OrderStatus status);

    @Query("SELECT o FROM Order o WHERE o.buyer.id = :buyerId AND o.dateTimestamp BETWEEN :startDate AND :endDate")
    List<Order> findByBuyerAndDateRange(@Param("buyerId") Long buyerId,
                                        @Param("startDate") LocalDateTime startDate,
                                        @Param("endDate") LocalDateTime endDate);

    @Query("SELECT o FROM Order o WHERE o.totalAmount > :minAmount")
    Page<Order> findOrdersAboveAmount(@Param("minAmount") Double minAmount, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.buyer.id = :buyerId ORDER BY o.dateTimestamp DESC")
    Page<Order> findByBuyerIdOrderByDateDesc(@Param("buyerId") Long buyerId, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.idUnderlist = :idUnderlist")
    List<Order> findByIdUnderlist(@Param("idUnderlist") String idUnderlist);

    @Query("SELECT o FROM Order o WHERE o.dateTimestamp >= :startDate")
    List<Order> findRecentOrders(@Param("startDate") LocalDateTime startDate);

    Long countByStatus(OrderStatus status);

    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.status = 'DELIVERED'")
    BigDecimal getTotalRevenue();
}