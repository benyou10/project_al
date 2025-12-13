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
@Builder
public class Store extends BaseEntity {

    @Column(name = "id_score")
    private Integer idScore;

    @Column(name = "nom_store", nullable = false)
    private String nomStore;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Post> storePosts = new ArrayList<>();

    @Column(name = "content")
    private String content;

    @Column(name = "domains")
    private String domains;

    @Column(name = "ciggote")
    private String ciggote;

    @Column(name = "grade")
    private String grade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private Seller seller;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Product> products = new ArrayList<>();

    // REMOVE THIS LINE if it exists:
    // private Boolean active = true; // This is already in BaseEntity as isActive

    public int verifyCityPro(Product product) {
        // Implémentation de la vérification
        return product != null ? 100 : 0;
    }

    public void confirm(com.example.project_al.modules.order.domain.Order order) {
        // Implémentation de la confirmation
        System.out.println("Order confirmed: " + order.getId());
    }

    public void operation3() {
        System.out.println("Store operation3 executed");
    }
}