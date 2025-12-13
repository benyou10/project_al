package com.example.project_al.modules.catalog.infrastructure;

import com.example.project_al.modules.catalog.application.ProductService;
import com.example.project_al.modules.catalog.domain.Category;
import com.example.project_al.modules.catalog.domain.Product;
import com.example.project_al.shared.kernel.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Product Catalog", description = "APIs for managing products and categories")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @Operation(summary = "Create a new product")
    public ResponseEntity<ApiResponse<Product>> createProduct(@Valid @RequestBody Product product) {
        Product created = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Product created successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID")
    public ResponseEntity<ApiResponse<Product>> getProduct(@PathVariable Long id) {
        return productService.findById(id)
                .map(product -> ResponseEntity.ok(ApiResponse.success(product)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Product not found")));
    }

    @GetMapping("/sku/{sku}")
    @Operation(summary = "Get product by SKU")
    public ResponseEntity<ApiResponse<Product>> getProductBySku(@PathVariable String sku) {
        Optional<Product> product = productService.findById(Long.parseLong(sku));
        return product.map(value -> ResponseEntity.ok(ApiResponse.success(value)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Product not found")));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update product")
    public ResponseEntity<ApiResponse<Product>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody Product productDetails) {
        Product updated = productService.updateProduct(id, productDetails);
        return ResponseEntity.ok(ApiResponse.success(updated, "Product updated successfully"));
    }

    @GetMapping("/search")
    @Operation(summary = "Search products")
    public ResponseEntity<ApiResponse<Page<Product>>> searchProducts(
            @RequestParam String keyword,
            @Parameter(hidden = true) Pageable pageable) {
        Page<Product> products = productService.searchProducts(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    @GetMapping("/price-range")
    @Operation(summary = "Get products by price range")
    public ResponseEntity<ApiResponse<Page<Product>>> getProductsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice,
            @Parameter(hidden = true) Pageable pageable) {
        Page<Product> products = productService.findByPriceRange(minPrice, maxPrice, pageable);
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Get products by category")
    public ResponseEntity<ApiResponse<List<Product>>> getProductsByCategory(
            @PathVariable Long categoryId) {
        List<Product> products = productService.findByCategoryId(categoryId);
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    @GetMapping("/store/{storeId}")
    @Operation(summary = "Get products by store")
    public ResponseEntity<ApiResponse<List<Product>>> getProductsByStore(
            @PathVariable Long storeId) {
        List<Product> products = productService.findByStoreId(storeId);
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    @GetMapping("/available")
    @Operation(summary = "Get available products")
    public ResponseEntity<ApiResponse<List<Product>>> getAvailableProducts() {
        List<Product> products = productService.findAvailableProducts();
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    @PostMapping("/categories")
    @Operation(summary = "Create a new category")
    public ResponseEntity<ApiResponse<Category>> createCategory(@Valid @RequestBody Category category) {
        Category created = productService.createCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Category created successfully"));
    }

    @GetMapping("/categories/root")
    @Operation(summary = "Get root categories")
    public ResponseEntity<ApiResponse<List<Category>>> getRootCategories() {
        List<Category> categories = productService.getRootCategories();
        return ResponseEntity.ok(ApiResponse.success(categories));
    }

    @GetMapping("/categories/{parentId}/subcategories")
    @Operation(summary = "Get subcategories")
    public ResponseEntity<ApiResponse<List<Category>>> getSubCategories(@PathVariable Long parentId) {
        List<Category> categories = productService.getSubCategories(parentId);
        return ResponseEntity.ok(ApiResponse.success(categories));
    }

    @PutMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate product")
    public ResponseEntity<ApiResponse<String>> deactivateProduct(@PathVariable Long id) {
        productService.deactivateProduct(id);
        return ResponseEntity.ok(ApiResponse.success("Product deactivated successfully"));
    }

    @PutMapping("/{id}/activate")
    @Operation(summary = "Activate product")
    public ResponseEntity<ApiResponse<String>> activateProduct(@PathVariable Long id) {
        productService.activateProduct(id);
        return ResponseEntity.ok(ApiResponse.success("Product activated successfully"));
    }

    @PostMapping("/{id}/command")
    @Operation(summary = "Command a product")
    public ResponseEntity<ApiResponse<String>> commandProduct(@PathVariable Long id) {
        Product product = productService.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.command();
        return ResponseEntity.ok(ApiResponse.success("Product commanded successfully"));
    }
}