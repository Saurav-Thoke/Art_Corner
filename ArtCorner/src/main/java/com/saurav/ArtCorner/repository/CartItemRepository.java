package com.saurav.ArtCorner.repository;

import com.saurav.ArtCorner.model.Cart;
import com.saurav.ArtCorner.model.CartItem;
import com.saurav.ArtCorner.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {

    CartItem findByCartAndProduct(Cart cart, Product product);
}
