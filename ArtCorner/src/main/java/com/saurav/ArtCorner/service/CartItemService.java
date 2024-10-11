package com.saurav.ArtCorner.service;

import com.saurav.ArtCorner.model.CartItem;

public interface CartItemService {

    CartItem updateCartItem(Long userId,Long id,CartItem cartItem) throws Exception;
    void removeCartItem(Long userId,Long cartItemId) throws Exception;
    CartItem findCartItemByIt(Long id) throws Exception;
}
