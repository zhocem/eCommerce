package com.dovi.OrderService.external.client;


import com.dovi.OrderService.exception.CustomException;
import com.dovi.OrderService.external.request.PaymentRequest;
import com.dovi.paymentservice.model.PaymentResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CircuitBreaker(name = "external", fallbackMethod = "fallback")
@FeignClient("PAYMENT-SERVICE/payments")
public interface PaymentService {

    @PostMapping
    ResponseEntity<Long> doPayment(@RequestBody PaymentRequest paymentRequest);

    @GetMapping("/order/{orderId}")
    ResponseEntity<PaymentResponse> getPaymentDetails(@PathVariable("orderId") long orderId);

    default void fallback(Exception e) {
        throw new CustomException("Payment Service is not available", "UNVAILABLE", 500);
    }
}
