package com.example.it211_ss18_btth3.repository;

import com.example.it211_ss18_btth3.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}