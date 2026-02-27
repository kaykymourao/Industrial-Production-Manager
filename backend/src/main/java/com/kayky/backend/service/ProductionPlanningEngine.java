package com.kayky.backend.service;

import com.kayky.backend.domain.Product;
import com.kayky.backend.domain.ProductMaterial;
import com.kayky.backend.dto.ProductionSuggestionItemResponse;
import com.kayky.backend.dto.ProductionSuggestionResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Component
public class ProductionPlanningEngine {

    public ProductionSuggestionResponse suggest(List<Product> products,
                                                Map<Long, BigDecimal> stockByRawMaterialId) {

        Map<Long, BigDecimal> availableStock = new HashMap<>(stockByRawMaterialId);

        List<Product> ordered = new ArrayList<>(products);
        ordered.sort(Comparator.comparing(Product::getPrice).reversed());

        List<ProductionSuggestionItemResponse> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (Product product : ordered) {
            int maxUnits = calculateMaxUnits(product, availableStock);
            if (maxUnits <= 0) continue;

            consumeStock(product, maxUnits, availableStock);

            BigDecimal itemTotal = product.getPrice()
                    .multiply(BigDecimal.valueOf(maxUnits));

            total = total.add(itemTotal);

            items.add(new ProductionSuggestionItemResponse(
                    product.getId(),
                    product.getCode(),
                    product.getName(),
                    maxUnits,
                    product.getPrice(),
                    itemTotal
            ));
        }

        int totalUnits = items.stream()
                .mapToInt(i -> i.getQuantityToProduce())
                .sum();

        // 🔥 NOVO CONSTRUTOR
        return new ProductionSuggestionResponse(
                false,
                totalUnits,
                total,
                items,
                List.of(),
                List.of()
        );
    }

    private int calculateMaxUnits(Product product,
                                  Map<Long, BigDecimal> availableStock) {

        List<ProductMaterial> materials = product.getMaterials();
        if (materials == null || materials.isEmpty()) return 0;

        int max = Integer.MAX_VALUE;

        for (ProductMaterial pm : materials) {

            Long rawMaterialId = pm.getRawMaterial().getId();
            BigDecimal stock = availableStock
                    .getOrDefault(rawMaterialId, BigDecimal.ZERO);

            BigDecimal qtyPerUnit = pm.getQuantityPerUnit();
            if (qtyPerUnit == null ||
                qtyPerUnit.compareTo(BigDecimal.ZERO) <= 0) {
                return 0;
            }

            int possibleUnits = stock
                    .divide(qtyPerUnit, 0, RoundingMode.FLOOR)
                    .intValue();

            max = Math.min(max, possibleUnits);
            if (max == 0) return 0;
        }

        return max;
    }

    private void consumeStock(Product product,
                              int units,
                              Map<Long, BigDecimal> availableStock) {

        for (ProductMaterial pm : product.getMaterials()) {

            Long rawMaterialId = pm.getRawMaterial().getId();
            BigDecimal stock = availableStock
                    .getOrDefault(rawMaterialId, BigDecimal.ZERO);

            BigDecimal consumption = pm.getQuantityPerUnit()
                    .multiply(BigDecimal.valueOf(units));

            availableStock.put(rawMaterialId,
                    stock.subtract(consumption));
        }
    }
}