package com.dovi.OrderService.external.client;

import com.dovi.OrderService.exception.CustomException;
import com.dovi.ProductService.model.ProductResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@CircuitBreaker(name = "external", fallbackMethod = "fallback")
@FeignClient(name = "PRODUCT-SERVICE/products")
public interface ProductService {

    @PutMapping("/reduceQuantity/{id}")
    ResponseEntity<Void> reduceQuantity(@PathVariable("id") long productId, @RequestParam long quantity);

    @GetMapping("/{id}")
    ResponseEntity<ProductResponse> getProductDetails(@PathVariable("id") long productId);

    default void fallback(Exception e) {
        throw new CustomException("Product Service is not available", "UNVAILABLE", 500);
    }
}
