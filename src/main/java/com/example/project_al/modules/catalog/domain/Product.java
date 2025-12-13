package com.example.project_al.modules.catalog.domain;

import com.example.project_al.modules.stores.domain.Store;
import com.example.project_al.shared.kernel.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product extends BaseEntity {

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "options", columnDefinition = "TEXT")
    private String options;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "sku", unique = true)
    private String sku;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();

    // REMOVED: private Boolean active = true; // This is already in BaseEntity

    @Column(name = "average_rating")
    private Double averageRating = 0.0;

    public void command() {
        System.out.println("Product " + name + " has been commanded");
    }

    public String echovecOption() {
        return "Echovec option: " + options;
    }

    public List<String> idspontedList() {
        List<String> sponsoredList = new ArrayList<>();
        if (price.compareTo(new BigDecimal("50")) > 0) {
            sponsoredList.add("SPONSORED");
        }
        return sponsoredList;
    }

    public void operations3() {
        System.out.println("Product operations3 executed");
    }

    public void reduceQuantity(Integer amount) {
        if (this.quantity < amount) {
            throw new RuntimeException("Insufficient stock for product: " + name);
        }
        this.quantity -= amount;
    }

    public void increaseQuantity(Integer amount) {
        this.quantity += amount;
    }
}