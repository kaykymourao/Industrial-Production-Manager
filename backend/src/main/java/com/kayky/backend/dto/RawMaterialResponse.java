package com.kayky.backend.dto;

import java.math.BigDecimal;

public class RawMaterialResponse {

    private Long id;
    private String code;
    private String name;
    private BigDecimal stockQuantity;
    private String unit;

    public RawMaterialResponse(Long id, String code, String name, BigDecimal stockQuantity, String unit) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.stockQuantity = stockQuantity;
        this.unit = unit;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getStockQuantity() {
        return stockQuantity;
    }

    public String getUnit() {
        return unit;
    }
}