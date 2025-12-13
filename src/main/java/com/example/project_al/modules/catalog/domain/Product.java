// modules/catalog/domain/Product.java
package com.example.project_al.modules.catalog.domain;

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
public class Product extends BaseEntity {

    @Column(columnDefinition = "TEXT")
    private String description;  // From UML: Description.String

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;  // From UML: PriceFloat (using BigDecimal for accuracy)

    @Column(columnDefinition = "TEXT")
    private String options;  // From UML: options.String

    @Column(nullable = false)
    private Integer quantity;  // From UML: QuantityInt

    @Column(nullable = false)
    private String name;

    private String sku;
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private com.example.project_al.modules.stores.domain.Store store;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @Id
    private Long id;

    // From UML: command()
    public void markAsCommanded() {
        // Logic when product is commanded
        System.out.println("Product " + name + " has been commanded");
    }

    // From UML: echovec_option()
    public String echoOption() {
        return "Option: " + options;
    }

    // From UML: idspontedList()
    public List<String> getSponsoredList() {
        // Return list of sponsored attributes
        List<String> sponsored = new ArrayList<>();
        if (price.compareTo(new BigDecimal("100")) > 0) {
            sponsored.add("PREMIUM");
        }
        return sponsored;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}