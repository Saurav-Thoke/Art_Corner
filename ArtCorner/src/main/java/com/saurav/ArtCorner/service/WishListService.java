package com.saurav.ArtCorner.service;

import com.saurav.ArtCorner.model.Product;
import com.saurav.ArtCorner.model.User;
import com.saurav.ArtCorner.model.WishList;

public interface WishListService {

    WishList createWishList(User user);
    WishList getWishListByUserId(User user);
    WishList addProductToWishList(User user, Product product);
}
