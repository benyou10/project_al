package com.example.project_al.modules.stores.infrastructure;

import com.example.project_al.modules.stores.domain.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    Optional<Store> findByNomStore(String nomStore);

    List<Store> findBySellerId(Long sellerId);

    @Query("SELECT s FROM Store s WHERE s.nomStore LIKE %:keyword%")
    Page<Store> searchByNomStore(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT s FROM Store s WHERE s.idScore > :minScore")
    List<Store> findHighScoreStores(@Param("minScore") Integer minScore);

    @Query("SELECT s FROM Store s WHERE s.grade = :grade")
    List<Store> findByGrade(@Param("grade") String grade);

    @Query("SELECT s FROM Store s WHERE s.seller.id = :sellerId AND s.isActive = true") // CHANGED: active -> isActive
    List<Store> findActiveBySellerId(@Param("sellerId") Long sellerId);

    boolean existsByNomStore(String nomStore);

    // NEW METHODS to add:
    List<Store> findByIsActiveTrue();

    @Query("SELECT s FROM Store s WHERE s.seller.id = :sellerId AND s.isActive = true")
    List<Store> findBySellerIdAndIsActiveTrue(@Param("sellerId") Long sellerId);

    @Query("SELECT s FROM Store s WHERE s.isActive = true")
    Page<Store> findActiveStores(Pageable pageable);
}