package com.kayky.backend.service;

import com.kayky.backend.domain.Product;
import com.kayky.backend.domain.ProductMaterial;
import com.kayky.backend.domain.RawMaterial;
import com.kayky.backend.dto.ProductionSuggestionResponse;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ProductionPlanningEngineTest {

    @Test
    void should_prioritize_higher_price_and_resolve_conflicts_by_consuming_stock() {
        RawMaterial rm = rawMaterialWithId(1L);

        Map<Long, BigDecimal> stock = Map.of(1L, new BigDecimal("10"));

        Product a = productWithId(1L, "A", new BigDecimal("100"));
        a.replaceMaterials(List.of(new ProductMaterial(a, rm, new BigDecimal("6"))));

        Product b = productWithId(2L, "B", new BigDecimal("80"));
        b.replaceMaterials(List.of(new ProductMaterial(b, rm, new BigDecimal("5"))));

        ProductionPlanningEngine engine = new ProductionPlanningEngine();
        ProductionSuggestionResponse result = engine.suggest(List.of(a, b), stock);

        assertEquals(1, result.getItems().size());
        assertEquals("A", result.getItems().get(0).getProductCode());
        assertEquals(1, result.getItems().get(0).getQuantityToProduce());
        assertEquals(0, result.getTotalValue().compareTo(new BigDecimal("100")));
    }

    @Test
    void should_skip_product_when_stock_is_insufficient() {
        RawMaterial rm = rawMaterialWithId(1L);
        Map<Long, BigDecimal> stock = Map.of(1L, new BigDecimal("2"));

        Product p = productWithId(1L, "P", new BigDecimal("50"));
        p.replaceMaterials(List.of(new ProductMaterial(p, rm, new BigDecimal("3"))));

        ProductionPlanningEngine engine = new ProductionPlanningEngine();
        ProductionSuggestionResponse result = engine.suggest(List.of(p), stock);

        assertTrue(result.getItems().isEmpty());
        assertEquals(BigDecimal.ZERO, result.getTotalValue());
    }

    @Test
    void should_floor_division_when_quantity_per_unit_is_decimal() {
        RawMaterial rm = rawMaterialWithId(1L);
        Map<Long, BigDecimal> stock = Map.of(1L, new BigDecimal("10"));

        Product p = productWithId(1L, "P", new BigDecimal("7"));
        p.replaceMaterials(List.of(new ProductMaterial(p, rm, new BigDecimal("2.5"))));

        ProductionPlanningEngine engine = new ProductionPlanningEngine();
        ProductionSuggestionResponse result = engine.suggest(List.of(p), stock);

        assertEquals(1, result.getItems().size());
        assertEquals(4, result.getItems().get(0).getQuantityToProduce());
        assertEquals(0, result.getTotalValue().compareTo(new BigDecimal("28")));
    }

    private RawMaterial rawMaterialWithId(Long id) {
        return new RawMaterial("RM", "Raw", BigDecimal.ZERO, "UNIT") {
            @Override public Long getId() { return id; }
        };
    }

    private Product productWithId(Long id, String code, BigDecimal price) {
        return new Product(code, code, price) {
            @Override public Long getId() { return id; }
        };
    }
}