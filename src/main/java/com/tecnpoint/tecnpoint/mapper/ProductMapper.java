package com.tecnpoint.tecnpoint.mapper;

import java.util.List;
import com.tecnpoint.tecnpoint.dto.*;
import com.tecnpoint.tecnpoint.entities.*;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "productVariations", qualifiedByName = "mapProductVariationToRecoveryProductVariationDto")
    RecoveryProductDto mapProductToRecoveryProductDto(Product product);

    @Named("mapProductVariationToRecoveryProductVariationDto")
    @IterableMapping(qualifiedByName = "mapProductVariationToRecoveryProductVariationDto")
    List<RecoveryProductVariationDto> mapProductVariationToRecoveryProductVariationDto(List<ProductVariation> productVariations);

    @Named("mapProductVariationToRecoveryProductVariationDto")
    RecoveryProductVariationDto mapProductVariationToRecoveryProductVariationDto(ProductVariation productVariation);

    ProductVariation mapCreateProductVariationDtoToProductVariation(CreateProductVariationDto createProductVariationDto);

}
