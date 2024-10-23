package com.saurav.ArtCorner.service;

import com.razorpay.PaymentLink;
import com.razorpay.RazorpayException;
import com.saurav.ArtCorner.model.Order;
import com.saurav.ArtCorner.model.PaymentOrder;
import com.saurav.ArtCorner.model.User;

import java.util.Set;

public interface PaymentService {
    PaymentOrder createOrder(User user, Set<Order> orders);
    PaymentOrder getPaymentOrderById(Long orderId) throws Exception;
    PaymentOrder getPaymentOderByPaymentId(String paymentId) throws Exception;
    Boolean proceedPaymentOrder(PaymentOrder paymentOrder,String paymentId,String paymentLinkId) throws RazorpayException;
    PaymentLink createRazorPayPaymentLink(User user,Long amount,Long orderId) throws Exception;
}
