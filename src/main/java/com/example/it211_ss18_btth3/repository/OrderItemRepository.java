package com.example.it211_ss18_btth3.repository;

import com.example.it211_ss18_btth3.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository
        extends JpaRepository<OrderItem, Long> {
}