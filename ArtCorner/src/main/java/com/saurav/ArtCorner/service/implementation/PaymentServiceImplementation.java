package com.saurav.ArtCorner.service.implementation;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.saurav.ArtCorner.model.*;
import com.saurav.ArtCorner.repository.OrderRepository;
import com.saurav.ArtCorner.repository.PaymentOrderRepository;
import com.saurav.ArtCorner.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class PaymentServiceImplementation implements PaymentService {

    private final PaymentOrderRepository paymentOrderRepository;
    private final OrderRepository orderRepository;

    private String apiKey="apiKey";
    private String apiSecret="apiSecrete";
    @Override
    public PaymentOrder createOrder(User user, Set<Order> orders) {
        Long amount=orders.stream().mapToLong(Order::getTotalSellingPrice).sum();

        PaymentOrder paymentOrder=new PaymentOrder();
        paymentOrder.setAmount(amount);
        paymentOrder.setUser(user);
        paymentOrder.setOrders(orders);
        return paymentOrderRepository.save(paymentOrder);
    }

    @Override
    public PaymentOrder getPaymentOrderById(Long orderId) throws Exception {
        return paymentOrderRepository.findById(orderId).orElseThrow(()->new Exception("Payment order not found"));
    }

    @Override
    public PaymentOrder getPaymentOderByPaymentId(String paymentId) throws Exception {
        PaymentOrder paymentOrder=paymentOrderRepository.findByPaymentLinkId(paymentId);
        if(paymentOrder==null)
        {
            throw new Exception("Payment order not found with provided payment link id");
        }
        return paymentOrder;
    }

    @Override
    public Boolean proceedPaymentOrder(PaymentOrder paymentOrder, String paymentId, String paymentLinkId) throws RazorpayException {
        if(paymentOrder.getStatus().equals(PaymentOrderStatus.PENDING))
        {
            RazorpayClient razorpay=new RazorpayClient(apiKey,apiSecret);
            Payment payment=razorpay.payments.fetch(paymentId);

            String status=payment.get("status");
            if(status.equals("captured"))
            {
                Set<Order> orders=paymentOrder.getOrders();
                for(Order order:orders)
                {
                    order.setPaymentStatus(PaymentStatus.COMPLETED);
                    orderRepository.save(order);
                }
                paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
                paymentOrderRepository.save(paymentOrder);
                return true;
            }
            paymentOrder.setStatus(PaymentOrderStatus.FAILED);
            paymentOrderRepository.save(paymentOrder);
            return false;
        }
        return false;
    }

    @Override
    public PaymentLink createRazorPayPaymentLink(User user, Long amount, Long orderId) throws Exception {

        amount=amount*100;
        try {
            RazorpayClient razorpay=new RazorpayClient(apiKey,apiSecret);
            JSONObject paymentLinkRequest=new JSONObject();
            paymentLinkRequest.put("amount",amount);
            paymentLinkRequest.put("currency","INR");

            JSONObject customer=new JSONObject();
            customer.put("name",user.getFullName());
            customer.put("email",user.getEmail());
            paymentLinkRequest.put("customer",customer);

            JSONObject notify=new JSONObject();
            notify.put("email",true);
            paymentLinkRequest.put("notify",notify);

            paymentLinkRequest.put("callback_url","http://localhost:3000/payment-success/"+orderId);
            paymentLinkRequest.put("callback_method","get");

            PaymentLink paymentLink=razorpay.paymentLink.create(paymentLinkRequest);

            String paymentLinkUrl=paymentLink.get("short_url");
            String paymentLinkId =paymentLink.get("id");

            return paymentLink;

        }catch (Exception e)
        {
            System.out.println(e.getMessage());
             throw new Exception(e.getMessage());
        }
    }
}
