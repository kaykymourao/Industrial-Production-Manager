package com.kayky.backend.dto;

import java.math.BigDecimal;

public class ProductionMaterialConsumptionResponse {

    private Long rawMaterialId;
    private String rawMaterialCode;
    private String rawMaterialName;
    private BigDecimal consumedQuantity;
    private String unit;

    public ProductionMaterialConsumptionResponse(Long rawMaterialId,
                                                 String rawMaterialCode,
                                                 String rawMaterialName,
                                                 BigDecimal consumedQuantity,
                                                 String unit) {
        this.rawMaterialId = rawMaterialId;
        this.rawMaterialCode = rawMaterialCode;
        this.rawMaterialName = rawMaterialName;
        this.consumedQuantity = consumedQuantity;
        this.unit = unit;
    }

    public Long getRawMaterialId() { return rawMaterialId; }
    public String getRawMaterialCode() { return rawMaterialCode; }
    public String getRawMaterialName() { return rawMaterialName; }
    public BigDecimal getConsumedQuantity() { return consumedQuantity; }
    public String getUnit() { return unit; }
}