package com.saurav.ArtCorner.controller;

import com.saurav.ArtCorner.model.*;
import com.saurav.ArtCorner.repository.SellerRepository;
import com.saurav.ArtCorner.response.ApiResponse;
import com.saurav.ArtCorner.response.PaymentLinkResponse;
import com.saurav.ArtCorner.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;
    private final UserService userService;
    private final SellerService sellerService;
    private final SellerReportService sellerReportService;
    private final OrderService orderService;
    private final SellerRepository sellerRepository;
    private final TransactionService transactionService;

    @GetMapping("/api/payment/{paymentId}")
    public ResponseEntity<ApiResponse> paymentSuccessHandler(
            @PathVariable String paymentId,
            @RequestParam String paymentLinkId,
            @RequestHeader("Authentication")String jwt)throws Exception
    {
        User user = userService.findUserByJwtToken(jwt);
        PaymentLinkResponse paymentLinkResponse;
        PaymentOrder paymentOrder = paymentService.getPaymentOderByPaymentId(paymentLinkId);

        boolean paymentSuccess = paymentService.proceedPaymentOrder(paymentOrder, paymentId, paymentLinkId);

        if (paymentSuccess) {
            for (Order order : paymentOrder.getOrders()) {

                transactionService.createTransaction(order);
                Seller seller = sellerRepository.findAll().getFirst();
                SellerReport report = sellerReportService.getSellerReport(seller);
                report.setTotalOrders(report.getTotalOrders() + 1);
                report.setTotalEarnings(report.getTotalEarnings() + order.getTotalSellingPrice());
                report.setTotalSales(report.getTotalSales() + order.getOrderItems().size());
                sellerReportService.updateSellerReport(report);
            }
        }
        ApiResponse res=new ApiResponse();
        res.setMessage("Payment Successful");

        return new ResponseEntity<>(res, HttpStatus.CREATED);

    }

}
