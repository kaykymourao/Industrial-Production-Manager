package com.kayky.backend.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(
        name = "raw_material",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_raw_material_code", columnNames = "code")
        }
)
public class RawMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String code;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(name = "stock_quantity", nullable = false, precision = 19, scale = 4)
    private BigDecimal stockQuantity;

    @Column(nullable = false, length = 20)
    private String unit;

    protected RawMaterial() {
        // JPA only
    }

    public RawMaterial(String code, String name, BigDecimal stockQuantity, String unit) {
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

    public void update(String code, String name, BigDecimal stockQuantity, String unit) {
        this.code = code;
        this.name = name;
        this.stockQuantity = stockQuantity;
        this.unit = unit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RawMaterial that)) return false;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    public void setStockQuantity(BigDecimal stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
}