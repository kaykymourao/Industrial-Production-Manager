package com.kayky.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public class RawMaterialRequest {

    @NotBlank
    private String code;

    @NotBlank
    private String name;

    @NotNull
    @PositiveOrZero
    private BigDecimal stockQuantity;

    @NotBlank
    private String unit;

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