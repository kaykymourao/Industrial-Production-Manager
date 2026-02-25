package com.kayky.backend.service;

import com.kayky.backend.domain.Product;
import com.kayky.backend.domain.ProductMaterial;
import com.kayky.backend.domain.RawMaterial;
import com.kayky.backend.dto.*;
import com.kayky.backend.repository.ProductRepository;
import com.kayky.backend.repository.RawMaterialRepository;
import com.kayky.backend.service.exception.BadRequestException;
import com.kayky.backend.service.exception.ConflictException;
import com.kayky.backend.service.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final RawMaterialRepository rawMaterialRepository;

    public ProductService(ProductRepository productRepository, RawMaterialRepository rawMaterialRepository) {
        this.productRepository = productRepository;
        this.rawMaterialRepository = rawMaterialRepository;
    }

    public ProductResponse create(ProductRequest request) {
        productRepository.findByCode(request.getCode())
                .ifPresent(p -> { throw new ConflictException("error.product.codeAlreadyExists"); });

        Product product = new Product(request.getCode(), request.getName(), request.getPrice());
        List<ProductMaterial> materials = buildMaterials(product, request.getMaterials());
        product.replaceMaterials(materials);

        Product saved = productRepository.save(product);
        return toResponse(saved);
    }

    public ProductResponse update(Long id, ProductRequest request) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("error.product.notFound"));

        productRepository.findByCode(request.getCode())
                .filter(p -> !p.getId().equals(id))
                .ifPresent(p -> { throw new ConflictException("error.product.codeAlreadyExists"); });

        existing.updateBasicInfo(request.getCode(), request.getName(), request.getPrice());

        List<ProductMaterial> materials = buildMaterials(existing, request.getMaterials());
        existing.replaceMaterials(materials);

        Product saved = productRepository.save(existing);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public ProductResponse findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("error.product.notFound"));

        return toResponse(product);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> findAll() {
        return productRepository.findAll().stream().map(this::toResponse).toList();
    }

    private List<ProductMaterial> buildMaterials(Product product, List<ProductMaterialRequest> materialRequests) {
        Set<Long> usedRawMaterialIds = new HashSet<>();

        return materialRequests.stream().map(mr -> {
            Long rawMaterialId = mr.getRawMaterialId();

            if (!usedRawMaterialIds.add(rawMaterialId)) {
                throw new BadRequestException("error.product.duplicateMaterial", rawMaterialId);
            }

            RawMaterial rawMaterial = rawMaterialRepository.findById(rawMaterialId)
                    .orElseThrow(() -> new BadRequestException("error.rawMaterial.notFoundById", rawMaterialId));

            return new ProductMaterial(product, rawMaterial, mr.getQuantityPerUnit());
        }).toList();
    }

    private ProductResponse toResponse(Product product) {
        List<ProductMaterialResponse> materials = product.getMaterials().stream()
                .map(pm -> new ProductMaterialResponse(
                        pm.getRawMaterial().getId(),
                        pm.getRawMaterial().getCode(),
                        pm.getRawMaterial().getName(),
                        pm.getQuantityPerUnit()
                ))
                .toList();

        return new ProductResponse(
                product.getId(),
                product.getCode(),
                product.getName(),
                product.getPrice(),
                materials
        );
    }

    public void delete(Long id) {
        Product existing = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("error.product.notFound"));

        productRepository.delete(existing);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> findAll(String code, Pageable pageable) {

        Page<Product> page;

        if (code != null && !code.isBlank()) {
            page = productRepository.findByCodeContainingIgnoreCase(code, pageable);
        } else {
            page = productRepository.findAll(pageable);
        }

        return page.map(this::toResponse);
    }
}