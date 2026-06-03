package com.example.it211_ss18_btth3.service;

import com.example.it211_ss18_btth3.dto.CreateOrderRequest;
import com.example.it211_ss18_btth3.dto.OrderItemRequest;
import com.example.it211_ss18_btth3.entity.Order;
import com.example.it211_ss18_btth3.entity.OrderItem;
import com.example.it211_ss18_btth3.entity.Product;
import com.example.it211_ss18_btth3.entity.User;
import com.example.it211_ss18_btth3.repository.OrderItemRepository;
import com.example.it211_ss18_btth3.repository.OrderRepository;
import com.example.it211_ss18_btth3.repository.ProductRepository;
import com.example.it211_ss18_btth3.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public OrderService(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            ProductRepository productRepository,
            UserRepository userRepository
    ) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public Order createOrder(
            String email,
            CreateOrderRequest request
    ) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Không tìm thấy user"
                        )
                );

        Order order = Order.builder()
                .user(user)
                .createdDate(LocalDateTime.now())
                .status("PENDING")
                .totalMoney(BigDecimal.ZERO)
                .build();

        orderRepository.save(order);

        BigDecimal totalMoney = BigDecimal.ZERO;

        for (OrderItemRequest itemRequest : request.getItems()) {
            Product product = productRepository.findById(
                    itemRequest.getProductId()
            ).orElseThrow(() ->
                    new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Không tìm thấy sản phẩm id: "
                                    + itemRequest.getProductId()
                    )
            );

            BigDecimal priceBuy = product.getPrice();

            BigDecimal itemTotal =
                    priceBuy.multiply(
                            BigDecimal.valueOf(
                                    itemRequest.getQuantity()
                            )
                    );

            totalMoney = totalMoney.add(itemTotal);

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(itemRequest.getQuantity())
                    .priceBuy(priceBuy)
                    .build();

            orderItemRepository.save(orderItem);
        }

        order.setTotalMoney(totalMoney);

        return orderRepository.save(order);
    }

    public List<Order> getMyOrders(
            String email
    ) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Không tìm thấy user"
                        )
                );

        return orderRepository.findByUser(user);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order updateStatus(
            Long id,
            String status
    ) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Không tìm thấy đơn hàng"
                        )
                );

        order.setStatus(status);

        return orderRepository.save(order);
    }
}