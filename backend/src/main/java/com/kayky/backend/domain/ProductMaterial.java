package com.kayky.backend.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "product_material")
public class ProductMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "raw_material_id", nullable = false)
    private RawMaterial rawMaterial;

    @Column(name = "quantity_per_unit", nullable = false, precision = 19, scale = 4)
    private BigDecimal quantityPerUnit;

    protected ProductMaterial() {
        // JPA only
    }

    public ProductMaterial(Product product, RawMaterial rawMaterial, BigDecimal quantityPerUnit) {
        this.product = product;
        this.rawMaterial = rawMaterial;
        this.quantityPerUnit = quantityPerUnit;
    }

    public Long getId() { return id; }
    public Product getProduct() { return product; }
    public RawMaterial getRawMaterial() { return rawMaterial; }
    public BigDecimal getQuantityPerUnit() { return quantityPerUnit; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductMaterial that)) return false;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 31;
    }
}