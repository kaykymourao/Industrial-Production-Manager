package com.kayky.backend.dto;

import java.math.BigDecimal;
import java.util.List;

public class ProductResponse {

    private Long id;
    private String code;
    private String name;
    private BigDecimal price;
    private List<ProductMaterialResponse> materials;

    public ProductResponse(Long id, String code, String name, BigDecimal price, List<ProductMaterialResponse> materials) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.price = price;
        this.materials = materials;
    }

    public Long getId() { return id; }
    public String getCode() { return code; }
    public String getName() { return name; }
    public BigDecimal getPrice() { return price; }
    public List<ProductMaterialResponse> getMaterials() { return materials; }
}