package com.kayky.backend.repository;

import com.kayky.backend.domain.ProductMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductMaterialRepository extends JpaRepository<ProductMaterial, Long> {
    boolean existsByRawMaterial_Id(Long rawMaterialId);
}