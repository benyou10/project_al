package com.example.project_al.modules.catalog.application;

import com.example.project_al.modules.catalog.domain.Category;
import com.example.project_al.modules.catalog.domain.Product;
import com.example.project_al.modules.catalog.infrastructure.CategoryRepository;
import com.example.project_al.modules.catalog.infrastructure.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public Product createProduct(Product product) {
        if (product.getSku() != null && productRepository.findBySku(product.getSku()).isPresent()) {
            throw new RuntimeException("SKU already exists");
        }
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product productDetails) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setQuantity(productDetails.getQuantity());
        product.setOptions(productDetails.getOptions());
        product.setSku(productDetails.getSku());
        product.setImageUrl(productDetails.getImageUrl());

        if (productDetails.getCategory() != null) {
            product.setCategory(productDetails.getCategory());
        }

        return productRepository.save(product);
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public Page<Product> searchProducts(String keyword, Pageable pageable) {
        return productRepository.searchProducts(keyword, pageable);
    }

    public Page<Product> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        return productRepository.findByPriceRange(minPrice, maxPrice, pageable);
    }

    public List<Product> findByCategoryId(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    public List<Product> findByStoreId(Long storeId) {
        return productRepository.findByStoreId(storeId);
    }

    public List<Product> findAvailableProducts() {
        return productRepository.findAvailableProducts();
    }

    public Category createCategory(Category category) {
        if (categoryRepository.findByName(category.getName()).isPresent()) {
            throw new RuntimeException("Category name already exists");
        }
        return categoryRepository.save(category);
    }

    public List<Category> getRootCategories() {
        return categoryRepository.findRootCategories();
    }

    public List<Category> getSubCategories(Long parentId) {
        return categoryRepository.findByParentId(parentId);
    }

    public void reduceProductQuantity(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.reduceQuantity(quantity);
        productRepository.save(product);
    }

    public void increaseProductQuantity(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.increaseQuantity(quantity);
        productRepository.save(product);
    }

    public void deactivateProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setIsActive(false); // Changed from setActive to setIsActive
        productRepository.save(product);
    }

    public void activateProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setIsActive(true); // Changed from setActive to setIsActive
        productRepository.save(product);
    }

    // Additional useful methods
    public Page<Product> findActiveProducts(Pageable pageable) {
        // We need to add this method to the repository
        return productRepository.findByIsActiveTrue(pageable);
    }

    public List<Product> findProductsByStoreAndCategory(Long storeId, Long categoryId) {
        return productRepository.findByStoreIdAndCategoryId(storeId, categoryId);
    }
}