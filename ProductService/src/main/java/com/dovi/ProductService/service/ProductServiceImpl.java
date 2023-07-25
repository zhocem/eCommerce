package com.dovi.ProductService.service;

import com.dovi.ProductService.entity.Product;
import com.dovi.ProductService.model.ProductRequest;
import com.dovi.ProductService.model.ProductResponse;
import com.dovi.ProductService.repository.ProductRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public long addProduct(ProductRequest productRequest) {
        log.info("Adding Product...");

        Product product = Product.builder()
                .productName(productRequest.getName())
                .price(productRequest.getPrice())
                .quantity(productRequest.getQuantity())
                .build();

        productRepository.save(product);
        log.info("Product created...");
        return product.getProductId();
    }

    @Override
    public ProductResponse getProductById(long productId) {
        log.info("Getting product for productId: {}", productId);
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product witn given id not found"));
        ProductResponse productResponse = new ProductResponse();
        BeanUtils.copyProperties(product, productResponse);
        return productResponse;

    }


}
