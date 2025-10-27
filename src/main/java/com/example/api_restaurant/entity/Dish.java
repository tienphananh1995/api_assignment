package com.example.api_restaurant.entity;

import com.example.api_restaurant.entity.helper.DishStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@Table(name = "dishes")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Mã món ăn (MN001)

    @Column(nullable = false, name = "name", length = 150, columnDefinition = "VARCHAR(150)")
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    @Column(name ="price", nullable = false, columnDefinition = "DECIMAL")
    private Double price;

    @Column(name = "start_date", nullable = false, columnDefinition = "DATETIME")
    private Date startDate;

    @Column(name = "last_modified", columnDefinition = "DATETIME")
    private Date lastModifiedDate;

    @Enumerated(EnumType.STRING)
    private DishStatus status;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonBackReference
    private Category category;
}
