// modules/user/application/UserService.java
package com.example.project_al.modules.user.application;

import com.example.project_al.modules.user.domain.*;
import com.example.project_al.modules.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public User registerUser(User user) {
        return userRepository.save(user);
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<Buyer> getAllBuyers() {
        return userRepository.findAllBuyers();
    }

    public List<Seller> getAllSellers() {
        return userRepository.findAllSellers();
    }

    public Buyer followSeller(Long buyerId, Long sellerId) {
        Buyer buyer = (Buyer) findById(buyerId);
        Seller seller = (Seller) findById(sellerId);

        buyer.follow(seller);
        return userRepository.save(buyer);
    }
}