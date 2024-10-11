package com.saurav.ArtCorner.service.implementation;

import com.saurav.ArtCorner.model.Cart;
import com.saurav.ArtCorner.model.CartItem;
import com.saurav.ArtCorner.model.Product;
import com.saurav.ArtCorner.model.User;
import com.saurav.ArtCorner.repository.CartItemRepository;
import com.saurav.ArtCorner.repository.CartRepository;
import com.saurav.ArtCorner.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImplementation implements CartService {
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;

    @Override
    public CartItem addCartItem(User user, Product product, int quantity) {
        Cart cart=findUserCart(user);
        CartItem isPresent=cartItemRepository.findByCartAndProduct(cart,product);
        if(isPresent==null)
        {
            CartItem cartItem=new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUserId(user.getId());
            int totalPrice=quantity*product.getSellingPrice();
            cartItem.setSellingPrice(totalPrice);
            cartItem.setMrpPrice(quantity*product.getMrpPrice());
            cart.getCartItems().add(cartItem);
            cartItem.setCart(cart);
            return cartItemRepository.save(cartItem);
        }
        return isPresent;
    }

    @Override
    public Cart findUserCart(User user) {
        Cart cart=cartRepository.findByUserId(user.getId());
        int totalPrice=0;
        int totalDiscountedPrice=0;
        int totalItem=0;

        for(CartItem cartItem:cart.getCartItems())
        {
            totalPrice+=cartItem.getMrpPrice();
            totalDiscountedPrice+=cartItem.getSellingPrice();
            totalItem+=cartItem.getQuantity();
        }
        cart.setTotalMrpPrice(totalPrice);
        cart.setTotalItem(totalItem);
        cart.setDiscount(calculateDiscountPercentage(totalPrice,totalDiscountedPrice));
        cart.setTotalSellingPrice(totalDiscountedPrice);
        return cart;
    }
    public int calculateDiscountPercentage(int mrpPrice, int sellingPrice) {
        if(mrpPrice<=0)
        {
            return 0;
        }
        double discount=mrpPrice-sellingPrice;
        double discountPercentage=(discount/mrpPrice)*100;
        return (int)discountPercentage;
    }
}
