package com.tecnpoint.tecnpoint.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tecnpoint.tecnpoint.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
