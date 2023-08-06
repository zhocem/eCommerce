package com.dovi.OrderService.service;

import com.dovi.OrderService.entity.Order;
import com.dovi.OrderService.exception.CustomException;
import com.dovi.OrderService.external.client.PaymentService;
import com.dovi.OrderService.external.client.ProductService;
import com.dovi.OrderService.external.request.PaymentRequest;
import com.dovi.OrderService.model.OrderRequest;
import com.dovi.OrderService.model.OrderResponse;
import com.dovi.OrderService.repository.OrderRepository;
import com.dovi.ProductService.model.ProductResponse;
import com.dovi.paymentservice.model.PaymentResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Service
@Log4j2
public class OrderServiceImpl implements OrderService {

    OrderRepository orderRepository;
    ProductService productService;
    PaymentService paymentService;

    RestTemplate restTemplate;

    public OrderServiceImpl(
            OrderRepository orderRepository,
            ProductService productService,
            PaymentService paymentService,
            RestTemplate restTemplate) {
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.paymentService = paymentService;
        this.restTemplate = restTemplate;
    }

    @Override
    public long placeOrder(OrderRequest orderRequest) {

        //Order Entity -> Save the data with Status Order Created
        //Product Service - Block Products (Reduce the Quantity)
        //Payment Service -> Payments -> Success-> COMPLETE, Else
        //CANCELLED

        log.info("Placing Order Request: {}", orderRequest);

        productService.reduceQuantity(orderRequest.getProductId(), orderRequest.getQuantity());

        log.info("Creating Order with Status CREATED");
        Order order = Order.builder()
                .amount(orderRequest.getTotalAmount())
                .status("CREATED")
                .productId(orderRequest.getProductId())
                .date(Instant.now())
                .quantity(orderRequest.getQuantity())
                .build();

        order = orderRepository.save(order);

        log.info("Calling Payment Service to complete the payment");
        PaymentRequest paymentRequest
                = PaymentRequest.builder()
                .orderId(order.getId())
                .paymentMode(orderRequest.getPaymentMode())
                .amount(orderRequest.getTotalAmount())
                .build();

        String orderStatus = null;
        try {
            paymentService.doPayment(paymentRequest);
            log.info("Payment done Successfully. Changing the Oder status to PLACED");
            orderStatus = "PLACED";
        } catch (Exception e) {
            log.error("Error occurred in payment. Changing order status to PAYMENT_FAILED");
            orderStatus = "PAYMENT_FAILED";
        }

        order.setStatus(orderStatus);
        orderRepository.save(order);

        log.info("Order Places successfully with Order Id: {}", order.getId());
        return order.getId();
    }

    @Override
    public OrderResponse getOrderDetails(long orderId) {
        log.info("Get order details for order Id {}", orderId);

        Order order = orderRepository
                .findById(orderId).
                orElseThrow(
                        () -> new CustomException("No Details found for this order id: " + orderId, "NOT_FOUND", 404)
                );

        log.info("Fetching product details using Product Service for product Id {}", order.getProductId());

        ProductResponse productResponse = restTemplate.getForObject("http://PRODUCT-SERVICE/products/" + order.getProductId(), ProductResponse.class);

        log.info("Getting payment information from the payment service for order id {}", orderId);

        PaymentResponse paymentResponse = restTemplate.getForObject("http://PAYMENT-SERVICE/payments/order/" + orderId, PaymentResponse.class);


        return OrderResponse.builder()
                .orderId(order.getId())
                .date(order.getDate())
                .amount(order.getAmount())
                .status(order.getStatus())
                .productDetails(
                        OrderResponse.ProductDetails
                                .builder()
                                .productName(productResponse.getProductName())
                                .productId(productResponse.getProductId())
                                .build()
                )
                .paymentDetails(
                        OrderResponse.PaymentDetails
                                .builder()
                                .paymentId(paymentResponse.getPaymentId())
                                .status(paymentResponse.getStatus())
                                .paymentDate(paymentResponse.getPaymentDate())
                                .paymentMode(paymentResponse.getPaymentMode())
                                .build()
                )
                .build();
    }


}
