package com.saurav.ArtCorner.service.implementation;

import com.saurav.ArtCorner.model.*;
import com.saurav.ArtCorner.repository.AddressRepository;
import com.saurav.ArtCorner.repository.OrderItemRepository;
import com.saurav.ArtCorner.repository.OrderRepository;
import com.saurav.ArtCorner.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImplementation implements OrderService {

    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private  final OrderItemRepository orderItemRepository;
    @Override
    public Set<Order> createOrder(User user, Address shippingAddress, Cart cart) {
        if(!user.getAddresses().contains(shippingAddress))
        {
            user.getAddresses().add(shippingAddress);
        }
        Address address=addressRepository.save(shippingAddress);

        Map<Long,List<CartItem>> itemsBySeller=cart.getCartItems().stream()
                .collect(Collectors.groupingBy(item->item.getProduct()
                        .getSellerId()));
        Set<Order> orders=new HashSet<>();

        for(Map.Entry<Long,List<CartItem>> entry:itemsBySeller.entrySet())
        {
            Long sellerId=entry.getKey();
            List<CartItem> items=entry.getValue();
            int totalOrderPrice=items.stream().mapToInt(CartItem::getSellingPrice).sum();
            int totalItem=items.stream().mapToInt(CartItem::getQuantity).sum();

            Order createdOrder=new Order();
            createdOrder.setUser(user);
            createdOrder.setTotalMrpPrice(totalOrderPrice);
            createdOrder.setTotalSellingPrice(totalOrderPrice);
            createdOrder.setTotalItem(totalItem);
            createdOrder.setShippingAddress(address);
            createdOrder.setOrderStatus(OrderStatus.PENDING);
            createdOrder.getPaymentDetails().setStatus(PaymentStatus.PENDING);

            Order savedOrder=orderRepository.save(createdOrder);
            orders.add(savedOrder);

            List<OrderItem> orderItems=new ArrayList<>();
            for(CartItem item:items)
            {
                OrderItem orderItem=new OrderItem();
                orderItem.setOrder(savedOrder);
                orderItem.setMrpPrice(item.getMrpPrice());
                orderItem.setProduct(item.getProduct());
                orderItem.setQuantity(item.getQuantity());
                orderItem.setUserId(item.getUserId());
                orderItem.setSellingPrice(item.getSellingPrice());
                savedOrder.getOrderItems().add(orderItem);

                OrderItem savedOrderItem=orderItemRepository.save(orderItem);
                orderItems.add(savedOrderItem);
            }
        }

        return orders;
    }

    @Override
    public Order findOrderById(Long id) throws Exception {
        return orderRepository.findById(id).orElseThrow(()->new Exception("Order not found"));
    }

    @Override
    public List<Order> usersOrderHistory(Long userId) {
        return orderRepository.findByUserId(userId);
    }


    @Override
    public Order updateOrderStatus(Long orderId, OrderStatus orderStatus) throws Exception {
        Order order=findOrderById(orderId);
        order.setOrderStatus(orderStatus);
        return orderRepository.save(order);
    }

    @Override
    public Order cancelOrder(Long orderId, User user) throws Exception {
        Order order=findOrderById(orderId);

        if(!user.getId().equals(order.getUser().getId()))
        {
            throw new Exception("You do not have access to this order");
        }
        order.setOrderStatus(OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }

    @Override
    public OrderItem getOrderItemById(Long id) throws Exception {
        return orderItemRepository.findById(id).orElseThrow(()->new Exception("Order item do not exist"));
    }
}
