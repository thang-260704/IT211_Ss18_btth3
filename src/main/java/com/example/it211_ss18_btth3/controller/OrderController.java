package com.example.it211_ss18_btth3.controller;

import com.example.it211_ss18_btth3.dto.CreateOrderRequest;
import com.example.it211_ss18_btth3.dto.UpdateOrderStatusRequest;
import com.example.it211_ss18_btth3.entity.Order;
import com.example.it211_ss18_btth3.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(
            OrderService orderService
    ) {
        this.orderService = orderService;
    }

    // CUSTOMER đặt hàng
    @PostMapping
    public Order createOrder(
            Authentication authentication,
            @Valid @RequestBody CreateOrderRequest request
    ) {
        return orderService.createOrder(
                authentication.getName(),
                request
        );
    }

    // CUSTOMER xem lịch sử đơn của mình
    @GetMapping("/my")
    public List<Order> getMyOrders(
            Authentication authentication
    ) {
        return orderService.getMyOrders(
                authentication.getName()
        );
    }

    // STAFF / ADMIN xem tất cả đơn
    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    // STAFF cập nhật trạng thái đơn
    @PutMapping("/{id}/status")
    public Order updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateOrderStatusRequest request
    ) {
        return orderService.updateStatus(
                id,
                request.getStatus()
        );
    }
}