package com.example.api_restaurant.service;

import com.example.api_restaurant.entity.Category;
import com.example.api_restaurant.entity.Dish;
import com.example.api_restaurant.entity.helper.DishStatus;
import com.example.api_restaurant.repository.CategoryRepository;
import com.example.api_restaurant.repository.DishRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DishService {
    @Autowired
    DishRepository dishRepository;

    @Autowired
    CategoryRepository categoryRepository;

    public Page<Dish> findAll(String keyword, Long categoryId, String status, Double minPrice, Double maxPrice, String sortBy, String sortDir, int page, int limit) {
        Specification<Dish> spec = (root, query, criteriaBuilder) -> {
            // lưu danh sách lọc
            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();
            // xử lý logic để thêm trường cần lọc.
            if (keyword != null && !keyword.isEmpty()) {
                predicates.add(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
                                "%" + keyword.toLowerCase() + "%")
                );
            }
            if (categoryId != null) {
                predicates.add(
                        criteriaBuilder.equal(root.get("categoryId"),
                                categoryId));
            }
            if (status != null && !status.isEmpty()) {
                predicates.add(
                        criteriaBuilder.equal(root.get("status"),
                                status));
            }
            if (minPrice != null) {
                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(root.get("price"),
                                minPrice));
            }
            if (maxPrice != null) {
                predicates.add(
                        criteriaBuilder.lessThanOrEqualTo(root.get("price"),
                                maxPrice));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        Sort sort = Sort.by(sortBy != null ? sortBy : "id");
        sort = "asc".equalsIgnoreCase(sortDir) ? sort.ascending() : sort.descending();
        // Phân trang
        Pageable pageable = PageRequest.of(page, limit, sort);
        Page<Dish> dishPage = dishRepository.findAll(spec, pageable);
        return dishPage.map(dish -> {
            Dish response = new Dish();
            response.setId(dish.getId());
            response.setName(dish.getName());
            response.setPrice(dish.getPrice());
            response.setDescription(dish.getDescription());
            response.setPrice(dish.getPrice());
            response.setStartDate(dish.getStartDate());
            response.setStatus(DishStatus.valueOf(dish.getStatus().name()));
            response.setCategory(dish.getCategory() != null ? dish.getCategory() : categoryRepository.findById(dish.getCategory().getId()).get());
            return response;
        });
    }

//  get dish info
    public Dish getDishById(Long id) {
        Dish dish = dishRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dish not found with id: " + id));
        Dish response = new Dish();
        response.setId(dish.getId());
        response.setName(dish.getName());
        response.setPrice(dish.getPrice());
        response.setDescription(dish.getDescription());
        response.setPrice(dish.getPrice());
        response.setImageUrl(dish.getImageUrl());
        response.setStartDate(dish.getStartDate());
        response.setLastModifiedDate(dish.getLastModifiedDate());
        response.setStatus(DishStatus.valueOf(dish.getStatus().name()));
        response.setCategory(dish.getCategory());
        return response;
    }

//    create dish
    public Dish saveDish(Dish dish) {
        //check category
        Category category = categoryRepository.findById(dish.getCategory().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category not found with id: " + dish.getCategory().getId()));

        Dish response = new Dish();
        response.setName(dish.getName());
        response.setPrice(dish.getPrice());
        response.setDescription(dish.getDescription());
        response.setStartDate(dish.getStartDate());
        response.setStatus(DishStatus.valueOf(dish.getStatus().name()));
        response.setCategory(category);
        response.setImageUrl(dish.getImageUrl());
        response.setStartDate(dish.getStartDate());

        Dish savedDish = dishRepository.save(dish);
        Dish savedResponse = new Dish();
        savedResponse.setId(savedDish.getId());
        savedResponse.setName(savedDish.getName());
        savedResponse.setPrice(savedDish.getPrice());
        savedResponse.setDescription(savedDish.getDescription());
        savedResponse.setStartDate(savedDish.getStartDate());
        savedResponse.setImageUrl(savedDish.getImageUrl());
        savedResponse.setLastModifiedDate(savedDish.getLastModifiedDate());
        savedResponse.setStatus(DishStatus.valueOf(savedDish.getStatus().name()));
        savedResponse.setCategory(category);
        return savedResponse;
    }

//    update dish
}
