package com.saurav.ArtCorner.service.implementation;

import com.saurav.ArtCorner.model.CartItem;
import com.saurav.ArtCorner.model.User;
import com.saurav.ArtCorner.repository.CartItemRepository;
import com.saurav.ArtCorner.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemServiceImplementation implements CartItemService {

    private final CartItemRepository cartItemRepository;
    @Override
    public CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws Exception {
        CartItem item=findCartItemByIt(id);
        User cartItemUser=item.getCart().getUser();
        if(cartItemUser.getId().equals(userId))
        {
            item.setQuantity(cartItem.getQuantity());
            item.setMrpPrice(item.getQuantity()*item.getProduct().getMrpPrice());
            item.setSellingPrice(item.getQuantity()*item.getProduct().getSellingPrice());
            return cartItemRepository.save(item);
        }
        throw new Exception("you can not update this cart item");
    }

    @Override
    public void removeCartItem(Long userId, Long cartItemId) throws Exception {
        CartItem item=findCartItemByIt(cartItemId);
        User cartItemUser=item.getCart().getUser();
        if(cartItemUser.getId().equals(userId))
        {
            cartItemRepository.delete(item);
        }
        else
        {
            throw new Exception("you can not delete this item");
        }
    }

    @Override
    public CartItem findCartItemByIt(Long id) throws Exception {

        return cartItemRepository.findById(id).orElseThrow(()->new Exception("cart item not found with id"+id));
    }
}
