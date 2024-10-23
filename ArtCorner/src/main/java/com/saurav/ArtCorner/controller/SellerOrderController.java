package com.saurav.ArtCorner.controller;

import com.saurav.ArtCorner.model.Order;
import com.saurav.ArtCorner.model.OrderStatus;
import com.saurav.ArtCorner.model.Seller;
import com.saurav.ArtCorner.repository.OrderRepository;
import com.saurav.ArtCorner.service.OrderService;
import com.saurav.ArtCorner.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/seller/orders")
public class SellerOrderController {

    private final OrderService orderService;
    private final SellerService sellerService;
    private final OrderRepository orderRepository;

    @GetMapping()
    public ResponseEntity<List<Order>> getAllOrdersHandler(@RequestHeader("Authentication")String jwt)throws Exception
    {
        Seller seller=sellerService.getSellerProfile(jwt);
        List<Order>orders=orderRepository.findBySellerId(seller.getId());
        return new ResponseEntity<>(orders, HttpStatus.ACCEPTED);
    }

    @PatchMapping("/{orderID}/status/{orderStatus}")
    public ResponseEntity<Order> updateOrderHandler(@RequestHeader("Authentication")String jwt,
                                                    @PathVariable Long orderId,
                                                    @PathVariable OrderStatus orderStatus)throws Exception
    {
        Order order=orderService.updateOrderStatus(orderId,orderStatus);

        return new ResponseEntity<>(order,HttpStatus.ACCEPTED);
    }

}
