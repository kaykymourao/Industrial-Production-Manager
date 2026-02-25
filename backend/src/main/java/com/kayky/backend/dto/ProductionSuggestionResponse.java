package com.kayky.backend.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

public class ProductionSuggestionResponse {

    private boolean applied;
    private int totalUnits;
    private BigDecimal totalValue;
    private List<ProductionSuggestionItemResponse> items = new ArrayList<>();
    private List<ProductionMaterialConsumptionResponse> consumptions = new ArrayList<>();
    private List<ProductionStockSnapshotResponse> stockSnapshot = new ArrayList<>();

    public ProductionSuggestionResponse(boolean applied,
                                        int totalUnits,
                                        BigDecimal totalValue,
                                        List<ProductionSuggestionItemResponse> items,
                                        List<ProductionMaterialConsumptionResponse> consumptions,
                                        List<ProductionStockSnapshotResponse> stockSnapshot) {
        this.applied = applied;
        this.totalUnits = totalUnits;
        this.totalValue = totalValue;
        this.items = items;
        this.consumptions = consumptions;
        this.stockSnapshot = stockSnapshot;
    }

    public boolean isApplied() { return applied; }
    public int getTotalUnits() { return totalUnits; }
    public BigDecimal getTotalValue() { return totalValue; }
    public List<ProductionSuggestionItemResponse> getItems() { return items; }
    public List<ProductionMaterialConsumptionResponse> getConsumptions() { return consumptions; }
    public List<ProductionStockSnapshotResponse> getStockSnapshot() { return stockSnapshot; }
}