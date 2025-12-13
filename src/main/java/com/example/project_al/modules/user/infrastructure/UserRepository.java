package com.example.project_al.modules.user.infrastructure;

import com.example.project_al.modules.user.domain.Buyer;
import com.example.project_al.modules.user.domain.Seller;
import com.example.project_al.modules.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE TYPE(u) = Buyer")
    List<Buyer> findAllBuyers();

    @Query("SELECT u FROM User u WHERE TYPE(u) = Seller")
    List<Seller> findAllSellers();

    @Query("SELECT b FROM Buyer b JOIN b.followingList s WHERE s.id = :sellerId")
    List<Buyer> findFollowersBySellerId(@Param("sellerId") Long sellerId);

    @Query("SELECT u FROM User u WHERE u.nom LIKE %:keyword%")
    Page<User> searchByNom(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.email = :email AND u.isActive = true") // CHANGED: active -> isActive
    Optional<User> findActiveByEmail(@Param("email") String email);

    List<User> findByUserListId(Integer userListId);

    // Add these methods if needed:
    List<User> findByIsActiveTrue();

    @Query("SELECT u FROM User u WHERE u.isActive = true AND TYPE(u) = Buyer")
    List<Buyer> findActiveBuyers();

    @Query("SELECT u FROM User u WHERE u.isActive = true AND TYPE(u) = Seller")
    List<Seller> findActiveSellers();
}