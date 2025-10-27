package com.example.api_restaurant.controller;

import com.example.api_restaurant.entity.Dish;
import com.example.api_restaurant.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/dishes")
public class DishController {
    @Autowired
    private DishService dishService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getAll(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        // validate
        if (page < 0 || limit < 10) {
            return ResponseEntity.badRequest().body("Page and limit must be greater than 0");
        }
        if (!sortDir.equalsIgnoreCase("asc") && !sortDir.equalsIgnoreCase("desc")) {
            return ResponseEntity.badRequest().body("Sort direction must be 'asc' or 'desc'");
        }
        Page<Dish> dishes = dishService.findAll(keyword, categoryId, status, minPrice, maxPrice, sortBy, sortDir, page, limit);
        return ResponseEntity.ok(dishes);
    }
}
