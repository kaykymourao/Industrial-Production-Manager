package com.kayky.backend.web;

import com.kayky.backend.dto.RawMaterialRequest;
import com.kayky.backend.dto.RawMaterialResponse;
import com.kayky.backend.service.RawMaterialService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/raw-materials")
public class RawMaterialController {

    private final RawMaterialService rawMaterialService;

    public RawMaterialController(RawMaterialService rawMaterialService) {
        this.rawMaterialService = rawMaterialService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RawMaterialResponse create(@Valid @RequestBody RawMaterialRequest request) {
        return rawMaterialService.create(request);
    }

    @PutMapping("/{id}")
    public RawMaterialResponse update(@PathVariable Long id,
                                      @Valid @RequestBody RawMaterialRequest request) {
        return rawMaterialService.update(id, request);
    }

    @GetMapping("/{id}")
    public RawMaterialResponse findById(@PathVariable Long id) {
        return rawMaterialService.findById(id);
    }

    // 🔥 PAGINAÇÃO + FILTRO
    @GetMapping
    public Page<RawMaterialResponse> findAll(
            @RequestParam(required = false) String code,
            Pageable pageable
    ) {
        return rawMaterialService.findAll(code, pageable);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        rawMaterialService.delete(id);
    }
}