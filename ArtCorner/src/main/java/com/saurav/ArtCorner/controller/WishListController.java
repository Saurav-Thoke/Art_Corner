package com.saurav.ArtCorner.controller;

import com.saurav.ArtCorner.exception.ProductException;
import com.saurav.ArtCorner.model.Product;
import com.saurav.ArtCorner.model.User;
import com.saurav.ArtCorner.model.WishList;
import com.saurav.ArtCorner.service.ProductService;
import com.saurav.ArtCorner.service.UserService;
import com.saurav.ArtCorner.service.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wishlist")
public class WishListController {

    private final WishListService wishListService;
    private final UserService userService;
    private final ProductService productService;

    @GetMapping()
    public ResponseEntity<WishList> getWishListByUserId(@RequestHeader("Authentication")String jwt) throws Exception {

        User user=userService.findUserByJwtToken(jwt);
        WishList wishList=wishListService.getWishListByUserId(user);
        return ResponseEntity.ok(wishList);

    }

    @PostMapping("/add-product/{productId}")
    public ResponseEntity<WishList> addProductToWishList(@PathVariable Long productId,@RequestHeader("Authentication")String jwt) throws Exception {
        Product product=productService.findProductById(productId);
        User user=userService.findUserByJwtToken(jwt);

        WishList updatedWishList=wishListService.addProductToWishList(user,product);
        return ResponseEntity.ok(updatedWishList);

    }
}


