package com.kayky.backend.dto;

import java.math.BigDecimal;

public class DashboardSummaryResponse {

    private long totalProducts;
    private long totalRawMaterials;
    private BigDecimal totalStockQuantitySum;
    private BigDecimal totalProductsPriceSum;
    private BigDecimal maxProductionValueSuggestion;

    public DashboardSummaryResponse(long totalProducts,
                                    long totalRawMaterials,
                                    BigDecimal totalStockQuantitySum,
                                    BigDecimal totalProductsPriceSum,
                                    BigDecimal maxProductionValueSuggestion) {
        this.totalProducts = totalProducts;
        this.totalRawMaterials = totalRawMaterials;
        this.totalStockQuantitySum = totalStockQuantitySum;
        this.totalProductsPriceSum = totalProductsPriceSum;
        this.maxProductionValueSuggestion = maxProductionValueSuggestion;
    }

    public long getTotalProducts() { return totalProducts; }
    public long getTotalRawMaterials() { return totalRawMaterials; }
    public BigDecimal getTotalStockQuantitySum() { return totalStockQuantitySum; }
    public BigDecimal getTotalProductsPriceSum() { return totalProductsPriceSum; }
    public BigDecimal getMaxProductionValueSuggestion() { return maxProductionValueSuggestion; }
}