package com.kayky.backend.dto;

import java.math.BigDecimal;

public class ProductMaterialResponse {

    private Long rawMaterialId;
    private String rawMaterialCode;
    private String rawMaterialName;
    private BigDecimal quantityPerUnit;

    public ProductMaterialResponse(Long rawMaterialId, String rawMaterialCode, String rawMaterialName, BigDecimal quantityPerUnit) {
        this.rawMaterialId = rawMaterialId;
        this.rawMaterialCode = rawMaterialCode;
        this.rawMaterialName = rawMaterialName;
        this.quantityPerUnit = quantityPerUnit;
    }

    public Long getRawMaterialId() { return rawMaterialId; }
    public String getRawMaterialCode() { return rawMaterialCode; }
    public String getRawMaterialName() { return rawMaterialName; }
    public BigDecimal getQuantityPerUnit() { return quantityPerUnit; }
}