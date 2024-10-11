package com.saurav.ArtCorner.service;

import com.saurav.ArtCorner.model.Cart;
import com.saurav.ArtCorner.model.CartItem;
import com.saurav.ArtCorner.model.Product;
import com.saurav.ArtCorner.model.User;

public interface CartService {
    public CartItem addCartItem(User user, Product product,int quantity);
    public Cart findUserCart(User user);
}
