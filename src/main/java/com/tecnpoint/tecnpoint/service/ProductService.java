package com.tecnpoint.tecnpoint.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tecnpoint.tecnpoint.dto.*;
import com.tecnpoint.tecnpoint.entities.*;
import com.tecnpoint.tecnpoint.enums.Category;
import com.tecnpoint.tecnpoint.mapper.*;
import com.tecnpoint.tecnpoint.repositories.*;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductVariationRepository productVariationRepository;

    @Autowired
    private ProductMapper productMapper;

    public RecoveryProductDto createProduct(CreateProductDto createProductDto) {

        List<ProductVariation> productVariations =  createProductDto.productVariations().stream()
                .map(productVariationDto -> productMapper.mapCreateProductVariationDtoToProductVariation(productVariationDto))
                .toList();

        Product product = Product.builder()
                .name(createProductDto.name())
                .description(createProductDto.description())
                .category(Category.valueOf(createProductDto.category().toUpperCase()))
                .productVariations(productVariations)
                .available(createProductDto.available())
                .build();

        if (!product.getAvailable() && product.getProductVariations().stream().anyMatch(ProductVariation::getAvailable)) {
            throw new RuntimeException("A variação de tamanho não pode estar disponível se o produto estiver indisponível.");
        }

        productVariations.forEach(productVariation -> productVariation.setProduct(product));

        Product productSaved = productRepository.save(product);

        return productMapper.mapProductToRecoveryProductDto(productSaved);
    }

    public RecoveryProductDto createProductVariation(Long productId, CreateProductVariationDto createProductVariationDto) {

        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Produto não encontrado."));

        ProductVariation productVariation = productMapper.mapCreateProductVariationDtoToProductVariation(createProductVariationDto);

        productVariation.setProduct(product);
        ProductVariation productVariationSaved = productVariationRepository.save(productVariation);

        product.getProductVariations().add(productVariationSaved);
        productRepository.save(product);

        return productMapper.mapProductToRecoveryProductDto(productVariationSaved.getProduct());
    }

    public List<RecoveryProductDto> getProducts() {
        List<Product> products = productRepository.findAll();

        return products.stream().map(product -> productMapper.mapProductToRecoveryProductDto(product)).toList();
    }

    public RecoveryProductDto getProductById(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Produto não encontrado."));

        return productMapper.mapProductToRecoveryProductDto(product);
    }

    public RecoveryProductDto updateProductPart(Long productId, UpdateProductDto updateProductDto) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Produto não encontrado."));

        if (updateProductDto.name() != null) {
            product.setName(updateProductDto.name());
        }
        if (updateProductDto.description() != null) {
            product.setDescription(updateProductDto.description());
        }
        if (updateProductDto.available() != null) {
            product.setAvailable(updateProductDto.available());

            if (!product.getAvailable()) {
                product.getProductVariations().forEach(productVariation -> productVariation.setAvailable(false));
            }
        }

        return productMapper.mapProductToRecoveryProductDto(productRepository.save(product));
    }

    public RecoveryProductDto updateProductVariation(Long productId, Long productVariationId, UpdateProductVariationDto updateProductVariationDto) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Produto não encontrado."));

        ProductVariation productVariation = product.getProductVariations().stream()
                .filter(productVariationInProduct -> productVariationInProduct.getId().equals(productVariationId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Variação de produto não encontrada."));

        if (updateProductVariationDto.sizeName() != null) {
            productVariation.setSizeName(updateProductVariationDto.sizeName());
        }
        if (updateProductVariationDto.description() != null) {
            productVariation.setDescription(updateProductVariationDto.description());
        }
        if (updateProductVariationDto.available() != null) {

            if (updateProductVariationDto.available() && !productVariation.getProduct().getAvailable()) {
                throw new RuntimeException("A variação de tamanho não pode estar disponível se o produto estiver indisponível.");
            }
            productVariation.setAvailable(updateProductVariationDto.available());
        }
        if (updateProductVariationDto.price() != null) {
            productVariation.setPrice(updateProductVariationDto.price());
        }

        Product productSaved = productRepository.save(product);

        return productMapper.mapProductToRecoveryProductDto(productSaved);
    }

    public void deleteProductId(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new RuntimeException("Produto não encontrado.");
        }
        productRepository.deleteById(productId);
    }

    public void deleteProductVariationById(Long productId, Long productVariationId) {
        ProductVariation productVariation = productVariationRepository
                .findByProductIdAdProductVariationId(productId, productVariationId)
                .orElseThrow(() -> new RuntimeException("Variação de produto não encontrada para o produto em questão."));

        productVariationRepository.deleteById(productVariation.getId());
    }
}
