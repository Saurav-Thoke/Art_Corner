package com.saurav.ArtCorner.controller;

import com.razorpay.PaymentLink;
import com.saurav.ArtCorner.model.*;
import com.saurav.ArtCorner.repository.PaymentOrderRepository;
import com.saurav.ArtCorner.repository.SellerRepository;
import com.saurav.ArtCorner.response.PaymentLinkResponse;
import com.saurav.ArtCorner.service.*;
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
    private final SellerService sellerService;
    private final SellerReportService sellerReportService;
    private final Seller seller;
    private final SellerRepository sellerRepository;
    private final PaymentService paymentService;
    private final PaymentOrderRepository paymentOrderRepository;

    @PostMapping()
    public ResponseEntity<PaymentLinkResponse> createOrderHandler(
            @RequestBody Address shippingAddress,
            @RequestParam PaymentMethod paymentMethod,
            @RequestHeader("Authorization")String jwt)throws Exception
    {
        User user=userService.findUserByJwtToken(jwt);
        Cart cart=cartService.findUserCart(user);
        Set<Order> orders=orderService.createOrder(user,shippingAddress,cart);

        PaymentOrder paymentOrder=paymentService.createOrder(user,orders);

        PaymentLinkResponse res=new PaymentLinkResponse();

        if(paymentMethod.equals(PaymentMethod.RAZORPAY))
        {
            PaymentLink paymentLink=paymentService.createRazorPayPaymentLink(user,paymentOrder.getAmount(), paymentOrder.getId());
            String paymentUrl=paymentLink.get("short_url");
            String paymentUrlId=paymentLink.get("id");

            res.setPayment_link_url(paymentUrl);
            paymentOrder.setPaymentLinkId(paymentUrlId);
            paymentOrderRepository.save(paymentOrder);
        }

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

        Seller seller=sellerService.getSellerById(sellerRepository.findAll().getFirst().getId());
        SellerReport report=sellerReportService.getSellerReport(seller);
        report.setCanceledOrders(report.getCanceledOrders()+1);
        report.setTotalRefund(report.getTotalRefund()+order.getTotalSellingPrice());
        sellerReportService.updateSellerReport(report);


        return ResponseEntity.ok(order);
    }

}
