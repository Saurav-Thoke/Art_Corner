package com.saurav.ArtCorner.controller;

import com.saurav.ArtCorner.model.Cart;
import com.saurav.ArtCorner.model.CartItem;
import com.saurav.ArtCorner.model.Product;
import com.saurav.ArtCorner.model.User;
import com.saurav.ArtCorner.request.AddItemRequest;
import com.saurav.ArtCorner.response.ApiResponse;
import com.saurav.ArtCorner.service.CartItemService;
import com.saurav.ArtCorner.service.CartService;
import com.saurav.ArtCorner.service.ProductService;
import com.saurav.ArtCorner.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final CartItemService cartItemService;
    private final UserService userService;
    private final ProductService productService;

    @GetMapping("/user-cart")
    public ResponseEntity<Cart> findUserCartHandler(
            @RequestHeader("Authorization") String jwt) throws Exception
    {
        User user=userService.findUserByJwtToken(jwt);
        Cart cart=cartService.findUserCart(user);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PutMapping("/add")
    public ResponseEntity<CartItem> addItemToCart(@RequestBody AddItemRequest req,
                                                  @RequestHeader("Authorization") String jwt) throws Exception {
        User user=userService.findUserByJwtToken(jwt);
        Product product=productService.findProductById(req.getProductId());

        CartItem item=cartService.addCartItem(user,product,req.getQuantity());
        ApiResponse res=new ApiResponse();
        res.setMessage("Item added to cart successfully");

        return new ResponseEntity<>(item,HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/item/{cartItemId}")
    public ResponseEntity<ApiResponse> deleteCartItemHandler(
            @PathVariable Long cartItemId,
            @RequestHeader("Authorization") String jwt)throws Exception
    {
        User user=userService.findUserByJwtToken(jwt);
        cartItemService.removeCartItem(user.getId(),cartItemId);
        ApiResponse res=new ApiResponse();
        res.setMessage("Item removed from cart");

        return new ResponseEntity<>(res,HttpStatus.ACCEPTED);
    }

    @PutMapping("/item/{cartItemId}")
    public ResponseEntity<CartItem> updateCartItemHandler(
            @PathVariable Long cartItemId,
            @RequestBody CartItem cartItem,
            @RequestHeader("Authorization")String jwt)throws Exception
    {
        User user=userService.findUserByJwtToken(jwt);
        CartItem updatedCartItem=null;
        if(cartItem.getQuantity()>0)
        {
            updatedCartItem=cartItemService.updateCartItem(user.getId(),cartItemId,cartItem);
        }

        return new ResponseEntity<>(updatedCartItem,HttpStatus.ACCEPTED);
    }
}
