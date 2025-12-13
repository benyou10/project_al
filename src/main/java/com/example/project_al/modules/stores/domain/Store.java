// modules/store/domain/Store.java
package com.example.project_al.modules.stores.domain;

import com.example.project_al.modules.catalog.domain.Product;
import com.example.project_al.modules.user.domain.Seller;
import com.example.project_al.shared.kernel.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "stores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Store extends BaseEntity {

    @Column(name = "id_score")
    private Integer idScore;  // From UML: Id_Score:int

    @Column(name = "nom_store", nullable = false)
    private String nomStore;  // From UML: Nom_Store<String

    @Column(name = "city")
    private String city;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Post> posts = new ArrayList<>();  // From UML: store_postsList=Posts>

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Seller seller;

    @Column(name = "domains")
    private String domains;  // From UML: +domains<String

    @Column(name = "ciggote")
    private String ciggote;  // From UML: +ciggote<String

    @Column(name = "grade")
    private String grade;  // From UML: -Grade<String
    @Id
    private Long id;

    // From UML: + verify_city_pro(Product)int
    public int verifyCityProduct(Product product) {
        // Logic to verify if product is available in store's city
        // Return some verification score
        return product.getQuantity() > 0 ? 100 : 0;
    }

    // From UML: +confirm(order)
    public boolean confirmOrder(com.example.project_al.modules.order.domain.Order order) {
        // Logic to confirm order
        return order != null && order.getOrderItems().stream()
                .allMatch(item -> item.getProduct().getQuantity() >= item.getQuantity());
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}

