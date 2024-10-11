package com.saurav.ArtCorner.repository;

import com.saurav.ArtCorner.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart,Long> {
    Cart findByUserId(Long id);
}
