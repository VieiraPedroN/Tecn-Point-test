package com.tecnpoint.tecnpoint.repositories;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import com.tecnpoint.tecnpoint.entities.ProductVariation;

@Repository
public interface ProductVariationRepository extends JpaRepository<ProductVariation, Long> {

    @Query("select pv from ProductVariation pv where pv.product.id = :productId and pv.id = :productVariationId")
    Optional<ProductVariation> findByProductIdAdProductVariationId(@Param("productId") Long productId, @Param("productVariationId") Long productVariationId);

}
