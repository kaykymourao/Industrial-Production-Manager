package com.kayky.backend.service;

import com.kayky.backend.dto.DashboardSummaryResponse;
import com.kayky.backend.repository.ProductRepository;
import com.kayky.backend.repository.RawMaterialRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional(readOnly = true)
public class DashboardService {

    private final ProductRepository productRepository;
    private final RawMaterialRepository rawMaterialRepository;
    private final ProductionPlanningEngine engine;

    public DashboardService(ProductRepository productRepository,
                            RawMaterialRepository rawMaterialRepository,
                            ProductionPlanningEngine engine) {
        this.productRepository = productRepository;
        this.rawMaterialRepository = rawMaterialRepository;
        this.engine = engine;
    }

    public DashboardSummaryResponse getSummary() {

        long totalProducts = productRepository.count();
        long totalRawMaterials = rawMaterialRepository.count();

        BigDecimal totalStock = rawMaterialRepository.findAll()
                .stream()
                .map(rm -> rm.getStockQuantity())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalProductPrices = productRepository.findAll()
                .stream()
                .map(p -> p.getPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal maxProductionValue = engine
                .suggest(productRepository.findAll(),
                        rawMaterialRepository.findAll()
                                .stream()
                                .collect(java.util.stream.Collectors.toMap(
                                        rm -> rm.getId(),
                                        rm -> rm.getStockQuantity()
                                )))
                .getTotalValue();

        return new DashboardSummaryResponse(
                totalProducts,
                totalRawMaterials,
                totalStock,
                totalProductPrices,
                maxProductionValue
        );
    }
}