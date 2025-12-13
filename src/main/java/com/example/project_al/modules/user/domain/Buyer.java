// modules/user/domain/Buyer.java
package com.example.project_al.modules.user.domain;

import com.example.project_al.modules.catalog.domain.Product;
import com.example.project_al.modules.user.domain.Seller;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "buyers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Buyer extends User {

    @ManyToMany
    @JoinTable(
            name = "buyer_wishlist",
            joinColumns = @JoinColumn(name = "buyer_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> wishlist = new ArrayList<>();  // From UML: wish_listList<Products

    @ManyToMany
    @JoinTable(
            name = "buyer_following",
            joinColumns = @JoinColumn(name = "buyer_id"),
            inverseJoinColumns = @JoinColumn(name = "seller_id")
    )
    private Set<User> following = new HashSet<>();  // From UML: FollowingList<User>
    @Id
    private Long id;

    // From UML: Follow<User>
    public void follow(User userToFollow) {
        if (userToFollow instanceof Seller) {
            following.add(userToFollow);
        }
    }

    public void unfollow(User userToUnfollow) {
        following.remove(userToUnfollow);
    }

    @Override
    public String getUserType() {
        return "BUYER";
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}