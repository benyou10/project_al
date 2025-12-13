package com.example.project_al.modules.stores.application;

import com.example.project_al.modules.catalog.domain.Product;
import com.example.project_al.modules.stores.domain.Post;
import com.example.project_al.modules.stores.domain.Store;
import com.example.project_al.modules.stores.infrastructure.PostRepository;
import com.example.project_al.modules.stores.infrastructure.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class StoreService {

    private final StoreRepository storeRepository;
    private final PostRepository postRepository;

    public Store createStore(Store store) {
        if (storeRepository.existsByNomStore(store.getNomStore())) {
            throw new RuntimeException("Store name already exists");
        }
        return storeRepository.save(store);
    }

    public Store updateStore(Long id, Store storeDetails) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Store not found"));

        store.setNomStore(storeDetails.getNomStore());
        store.setContent(storeDetails.getContent());
        store.setDomains(storeDetails.getDomains());
        store.setCiggote(storeDetails.getCiggote());
        store.setGrade(storeDetails.getGrade());
        store.setIdScore(storeDetails.getIdScore());

        return storeRepository.save(store);
    }

    public Optional<Store> findById(Long id) {
        return storeRepository.findById(id);
    }

    public List<Store> findBySellerId(Long sellerId) {
        return storeRepository.findBySellerId(sellerId);
    }

    public Page<Store> searchStores(String keyword, Pageable pageable) {
        return storeRepository.searchByNomStore(keyword, pageable);
    }

    public List<Store> findHighScoreStores(Integer minScore) {
        return storeRepository.findHighScoreStores(minScore);
    }

    public List<Store> findByGrade(String grade) {
        return storeRepository.findByGrade(grade);
    }

    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    public List<Post> getStorePosts(Long storeId) {
        return postRepository.findByStoreId(storeId);
    }

    public void deleteStore(Long id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Store not found"));
        store.setIsActive(false); // CHANGED: setActive(false) -> setIsActive(false)
        storeRepository.save(store);
    }

    public int verifyCityProduct(Long storeId, Product product) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("Store not found"));
        return store.verifyCityPro(product);
    }

    public void confirmOrder(Long storeId, com.example.project_al.modules.order.domain.Order order) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("Store not found"));
        store.confirm(order);
        storeRepository.save(store);
    }

    // Additional useful methods
    public void activateStore(Long id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Store not found"));
        store.setIsActive(true); // NEW: Method to activate a store
        storeRepository.save(store);
    }

    public List<Store> findActiveStores() {
        // Add this method to StoreRepository first
        return storeRepository.findByIsActiveTrue();
    }

    public List<Store> findActiveStoresBySeller(Long sellerId) {
        // Add this method to StoreRepository first
        return storeRepository.findBySellerIdAndIsActiveTrue(sellerId);
    }
}