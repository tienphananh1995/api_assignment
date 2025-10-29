package com.example.api_restaurant.service;

import com.example.api_restaurant.entity.Category;
import com.example.api_restaurant.entity.helper.DishStatus;
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
import java.util.*;

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
        // 1. Kiểm tra categoryId bắt buộc
        if (dish.getCategory() == null || dish.getCategory().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "categoryId là bắt buộc");
        }

        // 2. Kiểm tra category tồn tại
        Category category = categoryRepository.findById(dish.getCategory().getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Category not found with id: " + dish.getCategory().getId()
                ));

        // 3. Gán category đã validate
        dish.setCategory(category);

        // 4. Server tự động gán các trường
        Date now = new Date();
        dish.setStartDate(now);
        dish.setImageUrl(dish.getImageUrl());
        dish.setLastModifiedDate(now);
        dish.setCategory(dish.getCategory());
        dish.setPrice(dish.getPrice());
        dish.setDescription(dish.getDescription());
        dish.setName(dish.getName());
        dish.setStatus(DishStatus.ON_SALE);

        // 5. Lưu → JPA tự sinh id
        Dish savedDish = dishRepository.save(dish);

        // 6. Trả về chính entity đã lưu (id đã có)
        return savedDish;
    }

//    update dish
    public Dish updateDish(long id, Dish updates) {
        // 1. Tìm món ăn cũ
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Món ăn không tồn tại"));

        // 2. Kiểm tra trạng thái DELETED
        if (dish.getStatus() == DishStatus.DELETED) {
            throw new IllegalStateException("Không thể cập nhật món ăn đã bị xóa");
        }

        // 3. CẬP NHẬT CHỈ CÁC TRƯỜNG ĐƯỢC PHÉP
        if (updates.getName() != null) {
            if (updates.getName().trim().isEmpty()) {
                throw new IllegalArgumentException("Tên món ăn không được để trống");
            }
            if (updates.getName().trim().length() <= 7) {
                throw new IllegalArgumentException("Tên món ăn phải dài hơn 7 ký tự");
            }
            dish.setName(updates.getName().trim());
        }

        if (updates.getDescription() != null) {
            if (updates.getDescription().length() > 500) {
                throw new IllegalArgumentException("Mô tả không được vượt quá 500 ký tự");
            }
            dish.setDescription(updates.getDescription());
        }

        if (updates.getImageUrl() != null) {
            if (!updates.getImageUrl().matches("^https?://.+")) {
                throw new IllegalArgumentException("URL ảnh không hợp lệ");
            }
            dish.setImageUrl(updates.getImageUrl());
        }

        if (updates.getPrice() != null) {
            if (updates.getPrice() <= 0) {
                throw new IllegalArgumentException("Giá phải lớn hơn 0");
            }
            dish.setPrice(updates.getPrice());
        }

        if (updates.getCategory() != null && updates.getCategory().getId() != null) {
            Category category = categoryRepository.findById(updates.getCategory().getId())
                    .orElseThrow(() -> new RuntimeException("Category không tồn tại"));
            dish.setCategory(category);
        }

        if (updates.getStatus() != null) {
            if (updates.getStatus() == DishStatus.DELETED) {
                throw new IllegalArgumentException("Không thể đặt trạng thái DELETED qua update");
            }
            if (updates.getStatus() != DishStatus.ON_SALE && updates.getStatus() != DishStatus.STOPPED) {
                throw new IllegalArgumentException("Trạng thái chỉ có thể là ON_SALE hoặc STOPPED");
            }
            dish.setStatus(updates.getStatus());
        }
        if (updates.getStartDate() != null) {
            Date newStartDate = updates.getStartDate();

            // Validation: không cho ngày trong tương lai (tùy chọn)
            if (newStartDate.after(new Date())) {
                throw new IllegalArgumentException("startDate không được là ngày trong tương lai");
            }

            // Validation: không trước quá xa (ví dụ: 10 năm)
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.YEAR, -10);
            if (newStartDate.before(cal.getTime())) {
                throw new IllegalArgumentException("startDate không hợp lệ (quá cũ)");
            }

            dish.setStartDate(newStartDate);
        }

        // 4. Cập nhật thời gian sửa (server tự động)
        dish.setLastModifiedDate(new Date());

        // 5. Lưu entity cũ đã cập nhật
        return dishRepository.save(dish); // Trả về chính dish đã sửa
    }

    public void deleteDish(long id) {
        // 1. Kiểm tra tồn tại
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Món ăn không tồn tại với id: " + id
                ));

        // 2. Kiểm tra trạng thái
        if (dish.getStatus() == DishStatus.DELETED) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Món ăn đã bị xóa từ trước (trạng thái DELETED)"
            );
        }

        // 3. Cập nhật trạng thái + thời gian
        dish.setStatus(DishStatus.DELETED);
        dish.setLastModifiedDate(new Date());

        // 4. Lưu
        dishRepository.save(dish);
    }
}
