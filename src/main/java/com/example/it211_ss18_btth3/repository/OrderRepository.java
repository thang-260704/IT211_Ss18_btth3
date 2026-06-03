package com.example.it211_ss18_btth3.repository;

import com.example.it211_ss18_btth3.entity.Order;
import com.example.it211_ss18_btth3.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUser(User user);
}