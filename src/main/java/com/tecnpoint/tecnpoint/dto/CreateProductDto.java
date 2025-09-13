package com.tecnpoint.tecnpoint.dto;

import java.util.List;

public record CreateProductDto(

        String name,

        String description,

        String category,

        List<CreateProductVariationDto> productVariations,

        Boolean available

) {
}
