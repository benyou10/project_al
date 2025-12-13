// modules/user/domain/User.java
package com.example.project_al.modules.user.domain;

import com.example.project_al.shared.kernel.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class User extends BaseEntity {

    @Column(name = "user_id", unique = true)
    private Integer userListId;  // From UML: User_Listnt

    @Column(nullable = false)
    private String nom;  // From UML: Non.String

    @Column(unique = true, nullable = false)
    private String email;  // From UML: EmailString

    @Column(name = "phone_number")
    private String phoneNumber;  // From UML: phone_number.String

    private String sharestring;  // From UML: sharestring.String

    @ElementCollection
    @CollectionTable(name = "user_addresses", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "address")
    private List<String> addresses = new ArrayList<>();  // From UML: adresses.String

    @Column(nullable = false)
    private String password;

    public abstract String getUserType();

    // From UML: Sign_Internal Password
    public boolean signIn(String password) {
        return this.password.equals(password);
    }
}


