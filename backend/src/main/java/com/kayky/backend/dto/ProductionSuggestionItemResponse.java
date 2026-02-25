package com.kayky.backend.dto;

import java.math.BigDecimal;

public class ProductionSuggestionItemResponse {
    private final Long productId;
    private final String productCode;
    private final String productName;
    private final int quantityToProduce;
    private final BigDecimal unitPrice;
    private final BigDecimal totalValue;

    public ProductionSuggestionItemResponse(Long productId, String productCode, String productName,
                                            int quantityToProduce, BigDecimal unitPrice, BigDecimal totalValue) {
        this.productId = productId;
        this.productCode = productCode;
        this.productName = productName;
        this.quantityToProduce = quantityToProduce;
        this.unitPrice = unitPrice;
        this.totalValue = totalValue;
    }

    public Long getProductId() { return productId; }
    public String getProductCode() { return productCode; }
    public String getProductName() { return productName; }
    public int getQuantityToProduce() { return quantityToProduce; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public BigDecimal getTotalValue() { return totalValue; }
}