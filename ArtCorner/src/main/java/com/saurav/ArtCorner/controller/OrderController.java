package com.saurav.ArtCorner.controller;

import com.saurav.ArtCorner.model.*;
import com.saurav.ArtCorner.response.PaymentLinkResponse;
import com.saurav.ArtCorner.service.CartService;
import com.saurav.ArtCorner.service.OrderService;
import com.saurav.ArtCorner.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;
    private final CartService cartService;

    @PostMapping()
    public ResponseEntity<PaymentLinkResponse> createOrderHandler(
            @RequestBody Address shippingAddress,
            @RequestParam PaymentMethod paymentMethod,
            @RequestHeader("Authorization")String jwt)throws Exception
    {
        User user=userService.findUserByJwtToken(jwt);
        Cart cart=cartService.findUserCart(user);
        Set<Order> orders=orderService.createOrder(user,shippingAddress,cart);

        PaymentLinkResponse res=new PaymentLinkResponse();
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Order>> userOrderHistoryHandler(
            @RequestHeader("Authorization") String jwt)throws Exception
    {
        User user=userService.findUserByJwtToken(jwt);
        List<Order> orders=orderService.usersOrderHistory(user.getId());
        return new ResponseEntity<>(orders,HttpStatus.ACCEPTED);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long orderId,@RequestHeader("Authorization")String jwt)throws Exception
    {
        User user=userService.findUserByJwtToken(jwt);
        Order orders=orderService.findOrderById(orderId);
        return new ResponseEntity<>(orders,HttpStatus.ACCEPTED);
    }

    @GetMapping("/item/{orderItemId}")
    public ResponseEntity<OrderItem> getOrderItemById(
            @PathVariable Long orderItemId,@RequestHeader("Authorization") String jwt)throws Exception
    {
        User user=userService.findUserByJwtToken(jwt);
        OrderItem orderItem=orderService.getOrderItemById(orderItemId);
        return new ResponseEntity<>(orderItem,HttpStatus.ACCEPTED);
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<Order> cancelOrder(
            @PathVariable Long orderId,
            @RequestHeader("Authorization")String jwt)throws Exception
    {
        User user=userService.findUserByJwtToken(jwt);
        Order order=orderService.cancelOrder(orderId,user);


        return ResponseEntity.ok(order);
    }

}
