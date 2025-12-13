package com.example.project_al.modules.user.application;

import com.example.project_al.modules.user.domain.*;
import com.example.project_al.modules.user.infrastructure.FollowRepository;
import com.example.project_al.modules.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final PasswordEncoder passwordEncoder;

    public User registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setNom(userDetails.getNom());
        user.setPhoneNumber(userDetails.getPhoneNumber());
        user.setSharestring(userDetails.getSharestring());
        user.setAddresses(userDetails.getAddresses());

        return userRepository.save(user);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findActiveByEmail(email);
    }

    public List<Buyer> getAllBuyers() {
        return userRepository.findAllBuyers();
    }

    public List<Seller> getAllSellers() {
        return userRepository.findAllSellers();
    }

    public Page<User> searchUsers(String keyword, Pageable pageable) {
        return userRepository.searchByNom(keyword, pageable);
    }

    public Follow followSeller(Long buyerId, Long sellerId) {
        User buyerUser = userRepository.findById(buyerId)
                .orElseThrow(() -> new RuntimeException("Buyer not found"));
        User sellerUser = userRepository.findById(sellerId)
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        if (!(buyerUser instanceof Buyer buyer)) {
            throw new RuntimeException("User is not a buyer");
        }
        if (!(sellerUser instanceof Seller seller)) {
            throw new RuntimeException("User is not a seller");
        }

        if (followRepository.existsByBuyerIdAndSellerId(buyerId, sellerId)) {
            throw new RuntimeException("Already following this seller");
        }

        Follow follow = Follow.builder()
                .buyer(buyer)
                .seller(seller)
                .build();

        buyer.follow(seller);
        userRepository.save(buyer);

        return followRepository.save(follow);
    }

    public void unfollowSeller(Long buyerId, Long sellerId) {
        if (!followRepository.existsByBuyerIdAndSellerId(buyerId, sellerId)) {
            throw new RuntimeException("Not following this seller");
        }

        User buyerUser = userRepository.findById(buyerId)
                .orElseThrow(() -> new RuntimeException("Buyer not found"));
        User sellerUser = userRepository.findById(sellerId)
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        if (buyerUser instanceof Buyer buyer && sellerUser instanceof Seller seller) {
            buyer.unfollow(seller);
            userRepository.save(buyer);
        }

        followRepository.deleteByBuyerIdAndSellerId(buyerId, sellerId);
    }

    public List<Buyer> getFollowers(Long sellerId) {
        return userRepository.findFollowersBySellerId(sellerId);
    }

    public Long countFollowers(Long sellerId) {
        return followRepository.countBySellerId(sellerId);
    }

    public void deactivateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setIsActive(false); // CHANGED: setActive(false) -> setIsActive(false)
        userRepository.save(user);
    }

    public void activateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setIsActive(true); // CHANGED: setActive(true) -> setIsActive(true)
        userRepository.save(user);
    }

    public boolean validateCredentials(String email, String password) {
        return userRepository.findByEmail(email)
                .map(user -> passwordEncoder.matches(password, user.getPassword()))
                .orElse(false);
    }

    public List<User> findByUserListId(Integer userListId) {
        return userRepository.findByUserListId(userListId);
    }
}