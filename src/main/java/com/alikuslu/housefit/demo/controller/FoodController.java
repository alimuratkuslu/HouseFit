package com.alikuslu.housefit.demo.controller;

import com.alikuslu.housefit.demo.dto.FoodDto;
import com.alikuslu.housefit.demo.service.EdamamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/foods")
public class FoodController {

    private final EdamamService edamamService;

    public FoodController(EdamamService edamamService) {
        this.edamamService = edamamService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<FoodDto>> searchFoods(@RequestParam String query) {
        return ResponseEntity.ok(edamamService.searchFoods(query));
    }
}
