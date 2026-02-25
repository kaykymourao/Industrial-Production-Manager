package com.kayky.backend.service;

import com.kayky.backend.domain.RawMaterial;
import com.kayky.backend.dto.RawMaterialRequest;
import com.kayky.backend.dto.RawMaterialResponse;
import com.kayky.backend.repository.ProductMaterialRepository;
import com.kayky.backend.repository.RawMaterialRepository;
import com.kayky.backend.service.exception.ConflictException;
import com.kayky.backend.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class RawMaterialService {

    private final RawMaterialRepository rawMaterialRepository;
    private final ProductMaterialRepository productMaterialRepository;

    public RawMaterialService(RawMaterialRepository rawMaterialRepository,
                              ProductMaterialRepository productMaterialRepository) {
        this.rawMaterialRepository = rawMaterialRepository;
        this.productMaterialRepository = productMaterialRepository;
    }

    @Transactional
    public RawMaterialResponse create(RawMaterialRequest request) {
        rawMaterialRepository.findByCode(request.getCode())
                .ifPresent(r -> { throw new ConflictException("error.rawMaterial.codeAlreadyExists"); });

        RawMaterial rawMaterial = new RawMaterial(
                request.getCode(),
                request.getName(),
                request.getStockQuantity(),
                request.getUnit()
        );

        return toResponse(rawMaterialRepository.save(rawMaterial));
    }

    @Transactional
    public RawMaterialResponse update(Long id, RawMaterialRequest request) {
        RawMaterial existing = rawMaterialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("error.rawMaterial.notFound"));

        rawMaterialRepository.findByCode(request.getCode())
                .filter(r -> !r.getId().equals(id))
                .ifPresent(r -> { throw new ConflictException("error.rawMaterial.codeAlreadyExists"); });

        existing.update(
                request.getCode(),
                request.getName(),
                request.getStockQuantity(),
                request.getUnit()
        );

        return toResponse(rawMaterialRepository.save(existing));
    }

    public RawMaterialResponse findById(Long id) {
        RawMaterial rawMaterial = rawMaterialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("error.rawMaterial.notFound"));

        return toResponse(rawMaterial);
    }

    // 🔥 PAGINAÇÃO + FILTRO
    public Page<RawMaterialResponse> findAll(String code, Pageable pageable) {

        Page<RawMaterial> page;

        if (code != null && !code.isBlank()) {
            page = rawMaterialRepository.findByCodeContainingIgnoreCase(code, pageable);
        } else {
            page = rawMaterialRepository.findAll(pageable);
        }

        return page.map(this::toResponse);
    }

    @Transactional
    public void delete(Long id) {
        RawMaterial existing = rawMaterialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("error.rawMaterial.notFound"));

        if (productMaterialRepository.existsByRawMaterial_Id(id)) {
            throw new ConflictException("error.rawMaterial.inUse");
        }

        rawMaterialRepository.delete(existing);
    }

    private RawMaterialResponse toResponse(RawMaterial rawMaterial) {
        return new RawMaterialResponse(
                rawMaterial.getId(),
                rawMaterial.getCode(),
                rawMaterial.getName(),
                rawMaterial.getStockQuantity(),
                rawMaterial.getUnit()
        );
    }
}