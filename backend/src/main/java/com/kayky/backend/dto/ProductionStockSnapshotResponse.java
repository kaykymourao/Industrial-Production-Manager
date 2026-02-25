package com.kayky.backend.dto;

import java.math.BigDecimal;

public class ProductionStockSnapshotResponse {

    private Long rawMaterialId;
    private String rawMaterialCode;
    private String rawMaterialName;
    private BigDecimal before;
    private BigDecimal after;
    private String unit;

    public ProductionStockSnapshotResponse(Long rawMaterialId,
                                           String rawMaterialCode,
                                           String rawMaterialName,
                                           BigDecimal before,
                                           BigDecimal after,
                                           String unit) {
        this.rawMaterialId = rawMaterialId;
        this.rawMaterialCode = rawMaterialCode;
        this.rawMaterialName = rawMaterialName;
        this.before = before;
        this.after = after;
        this.unit = unit;
    }

    public Long getRawMaterialId() { return rawMaterialId; }
    public String getRawMaterialCode() { return rawMaterialCode; }
    public String getRawMaterialName() { return rawMaterialName; }
    public BigDecimal getBefore() { return before; }
    public BigDecimal getAfter() { return after; }
    public String getUnit() { return unit; }
}