package com.kayky.backend.repository;

import com.kayky.backend.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByCode(String code);

    Page<Product> findByCodeContainingIgnoreCase(String code, Pageable pageable);

}