package com.kayky.backend.web;

import com.kayky.backend.dto.ProductionSuggestionResponse;
import com.kayky.backend.service.ProductionPlanningService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/production")
public class ProductionController {

    private final ProductionPlanningService productionPlanningService;

    public ProductionController(ProductionPlanningService productionPlanningService) {
        this.productionPlanningService = productionPlanningService;
    }

    @PostMapping("/suggest")
    public ProductionSuggestionResponse suggest() {
        return productionPlanningService.suggestProductionPlan();
    }

    @PostMapping("/apply")
    public ProductionSuggestionResponse apply() {
        return productionPlanningService.applyProductionPlan();
    }
}