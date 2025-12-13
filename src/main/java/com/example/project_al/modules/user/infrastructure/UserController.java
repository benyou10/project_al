// modules/user/infrastructure/UserController.java
package com.example.project_al.modules.user.infrastructure;

import com.example.project_al.modules.user.application.UserService;
import com.example.project_al.modules.user.domain.Buyer;
import com.example.project_al.modules.user.domain.Seller;
import com.example.project_al.modules.user.domain.User;
import com.example.project_al.shared.kernel.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "APIs for managing users, buyers, and sellers")
public class UserController {

    private final UserService userService;

    @PostMapping("/register/buyer")
    @Operation(summary = "Register a new buyer")
    public ResponseEntity<ApiResponse<Buyer>> registerBuyer(@RequestBody Buyer buyer) {
        Buyer registered = (Buyer) userService.registerUser(buyer);
        return ResponseEntity.ok(ApiResponse.success(registered, "Buyer registered successfully"));
    }

    @PostMapping("/register/seller")
    @Operation(summary = "Register a new seller")
    public ResponseEntity<ApiResponse<Seller>> registerSeller(@RequestBody Seller seller) {
        Seller registered = (Seller) userService.registerUser(seller);
        return ResponseEntity.ok(ApiResponse.success(registered, "Seller registered successfully"));
    }

    @PostMapping("/{buyerId}/follow/{sellerId}")
    @Operation(summary = "Buyer follows a seller")
    public ResponseEntity<ApiResponse<Buyer>> followSeller(
            @PathVariable Long buyerId,
            @PathVariable Long sellerId) {
        Buyer buyer = userService.followSeller(buyerId, sellerId);
        return ResponseEntity.ok(ApiResponse.success(buyer, "Successfully followed seller"));
    }

    @GetMapping("/buyers")
    @Operation(summary = "Get all buyers")
    public ResponseEntity<ApiResponse<List<Buyer>>> getAllBuyers() {
        List<Buyer> buyers = userService.getAllBuyers();
        return ResponseEntity.ok(ApiResponse.success(buyers));
    }
}