package com.saurav.ArtCorner.repository;

import com.saurav.ArtCorner.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {
}
