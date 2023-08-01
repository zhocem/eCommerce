package com.dovi.ProductService.service;

import com.dovi.ProductService.model.ProductRequest;
import com.dovi.ProductService.model.ProductResponse;

public interface ProductService {
    long addProduct(ProductRequest productRequest);

    ProductResponse getProductById(long productId);

  void reduceQuantity(long productId, long quantity);
}
