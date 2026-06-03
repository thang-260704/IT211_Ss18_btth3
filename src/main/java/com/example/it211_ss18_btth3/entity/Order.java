package com.example.it211_ss18_btth3.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Người đặt hàng
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime createdDate;

    private String status;

    private BigDecimal totalMoney;
}