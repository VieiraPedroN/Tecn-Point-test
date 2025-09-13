package com.tecnpoint.tecnpoint.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

import com.tecnpoint.tecnpoint.enums.Category;

@Entity
@Table(name="products")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductVariation> productVariations;

    private Boolean available;

}