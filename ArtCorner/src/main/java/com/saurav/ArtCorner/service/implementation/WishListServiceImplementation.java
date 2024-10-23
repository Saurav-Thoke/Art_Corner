package com.saurav.ArtCorner.service.implementation;

import com.saurav.ArtCorner.model.Product;
import com.saurav.ArtCorner.model.User;
import com.saurav.ArtCorner.model.WishList;
import com.saurav.ArtCorner.repository.WishListRepository;
import com.saurav.ArtCorner.service.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WishListServiceImplementation implements WishListService {

    WishListRepository wishListRepository;
    @Override
    public WishList createWishList(User user) {
        WishList wishList=new WishList();
        wishList.setUser(user);
        return wishListRepository.save(wishList);
    }

    @Override
    public WishList getWishListByUserId(User user) {
        WishList wishList= wishListRepository.findByUserId(user.getId());
        if(wishList==null)
        {
            wishList=createWishList(user);
        }
        return wishList;
    }

    @Override
    public WishList addProductToWishList(User user, Product product) {
        WishList wishList=getWishListByUserId(user);
        if(wishList.getProducts().contains(product))
        {
            wishList.getProducts().remove(product);
        }
        else
        {
            wishList.getProducts().add(product);
        }
        return wishListRepository.save(wishList);
    }
}
