
// modules/user/domain/Seller.java
package com.example.project_al.modules.user.domain;

import com.example.project_al.modules.stores.domain.Store;
import com.example.project_al.modules.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sellers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Seller extends User {

    @Column(name = "seller_type")
    private String type = "INDIVIDUAL";  // From UML: type = defaultValue

    @Column(name = "store_name")
    private String storeName;  // attributeAsType

    @Column(name = "business_registration")
    private String businessRegistration;  // attributeAsType

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    private List<Store> stores = new ArrayList<>();

    @Override
    public String getUserType() {
        return "SELLER";
    }
}
