package com.kayky.backend.service;

import com.kayky.backend.domain.Product;
import com.kayky.backend.domain.ProductMaterial;
import com.kayky.backend.domain.RawMaterial;
import com.kayky.backend.dto.ProductionSuggestionResponse;
import com.kayky.backend.repository.ProductRepository;
import com.kayky.backend.repository.RawMaterialRepository;
import com.kayky.backend.service.ProductionPlanningEngine;

import com.kayky.backend.dto.ProductionMaterialConsumptionResponse;
import com.kayky.backend.dto.ProductionStockSnapshotResponse;
import com.kayky.backend.dto.ProductionMaterialConsumptionResponse;
import com.kayky.backend.dto.ProductionStockSnapshotResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ProductionPlanningService {

    private final ProductRepository productRepository;
    private final RawMaterialRepository rawMaterialRepository;
    private final ProductionPlanningEngine productionPlanningEngine;

    public ProductionPlanningService(ProductRepository productRepository, RawMaterialRepository rawMaterialRepository, ProductionPlanningEngine productionPlanningEngine) {
        this.productRepository = productRepository;
        this.rawMaterialRepository = rawMaterialRepository;
        this.productionPlanningEngine = productionPlanningEngine;
    }

    @Transactional(readOnly = true)
    public ProductionSuggestionResponse suggestProductionPlan() {

        Map<Long, BigDecimal> stock = loadCurrentStockByRawMaterialId();
        List<Product> products = productRepository.findAll();

        var engineResult = productionPlanningEngine.suggest(products, stock);

        int totalUnits = engineResult.getItems()
                .stream()
                .mapToInt(i -> i.getQuantityToProduce())
                .sum();

        return new ProductionSuggestionResponse(
                false,
                totalUnits,
                engineResult.getTotalValue(),
                engineResult.getItems(),
                List.of(),
                List.of()
        );
    }
    
    private Map<Long, BigDecimal> loadCurrentStockByRawMaterialId() {
        return rawMaterialRepository.findAll().stream()
                .collect(Collectors.toMap(RawMaterial::getId, RawMaterial::getStockQuantity));
    }

    @Transactional
    public ProductionSuggestionResponse applyProductionPlan() {

        Map<Long, BigDecimal> stockBefore = loadCurrentStockByRawMaterialId();
        List<Product> products = productRepository.findAll();

        var engineResult = productionPlanningEngine.suggest(products, stockBefore);

        if (engineResult.getItems().isEmpty()) {
            return new ProductionSuggestionResponse(
                    false, 0, engineResult.getTotalValue(),
                    engineResult.getItems(), List.of(), List.of()
            );
        }

        Map<Long, RawMaterial> rmById = rawMaterialRepository.findAll()
                .stream()
                .collect(Collectors.toMap(RawMaterial::getId, rm -> rm));

        Map<Long, BigDecimal> totalConsumed = new HashMap<>();

        for (var item : engineResult.getItems()) {
            Product product = products.stream()
                    .filter(p -> p.getId().equals(item.getProductId()))
                    .findFirst()
                    .orElse(null);

            if (product == null) continue;

            for (ProductMaterial pm : product.getMaterials()) {
                Long rmId = pm.getRawMaterial().getId();

                BigDecimal consumed = pm.getQuantityPerUnit()
                        .multiply(BigDecimal.valueOf(item.getQuantityToProduce()));

                totalConsumed.merge(rmId, consumed, BigDecimal::add);
            }
        }

        List<ProductionMaterialConsumptionResponse> consumptions = new ArrayList<>();
        List<ProductionStockSnapshotResponse> snapshots = new ArrayList<>();

        for (var entry : totalConsumed.entrySet()) {
            Long rmId = entry.getKey();
            BigDecimal consumed = entry.getValue();

            RawMaterial rm = rmById.get(rmId);
            if (rm == null) continue;

            BigDecimal before = rm.getStockQuantity();
            BigDecimal after = before.subtract(consumed);
            if (after.compareTo(BigDecimal.ZERO) < 0) after = BigDecimal.ZERO;

            rm.setStockQuantity(after);

            consumptions.add(new ProductionMaterialConsumptionResponse(
                    rm.getId(),
                    rm.getCode(),
                    rm.getName(),
                    consumed,
                    rm.getUnit()
            ));

            snapshots.add(new ProductionStockSnapshotResponse(
                    rm.getId(),
                    rm.getCode(),
                    rm.getName(),
                    before,
                    after,
                    rm.getUnit()
            ));
        }

        rawMaterialRepository.saveAll(rmById.values());

        int totalUnits = engineResult.getItems()
                .stream()
                .mapToInt(i -> i.getQuantityToProduce())
                .sum();

        return new ProductionSuggestionResponse(
                true,
                totalUnits,
                engineResult.getTotalValue(),
                engineResult.getItems(),
                consumptions,
                snapshots
        );
    }
}