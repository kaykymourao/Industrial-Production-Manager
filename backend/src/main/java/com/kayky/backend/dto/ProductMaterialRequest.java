package com.kayky.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class ProductMaterialRequest {

    @NotNull
    private Long rawMaterialId;

    @NotNull
    @Positive
    private BigDecimal quantityPerUnit;

    public Long getRawMaterialId() {
        return rawMaterialId;
    }

    public BigDecimal getQuantityPerUnit() {
        return quantityPerUnit;
    }
}