
// modules/user/domain/Follow.java (Optional - for tracking follow relationships)
package com.example.project_al.modules.user.domain;

import com.example.project_al.modules.user.domain.Buyer;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "follows")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "follower_id")
    private Buyer follower;

    @ManyToOne
    @JoinColumn(name = "following_id")
    private Seller following;

    private LocalDateTime followedAt = LocalDateTime.now();
}