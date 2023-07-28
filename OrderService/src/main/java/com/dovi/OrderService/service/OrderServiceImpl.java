package com.dovi.OrderService.service;

import com.dovi.OrderService.entity.Order;
import com.dovi.OrderService.model.OrderRequest;
import com.dovi.OrderService.repository.OrderRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Log4j2
public class OrderServiceImpl implements OrderService {

    OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public long placeOrder(OrderRequest orderRequest) {
        // order service -> save the data with status order created
        // product service -> block products (reduce the quantity)
        // payment service -> payment -> success Complete, else cancelled
        log.info("Placing the order request: {}", orderRequest);

        Order order = Order.builder()
                .amount(orderRequest.getTotalAmount())
                .status("CREATED")
                .productId(orderRequest.getProductId())
                .date(Instant.now())
                .quantity(orderRequest.getQuantity())
                .build();

        order = orderRepository.save(order);

        log.info("Order placed successfully with id: {}", order.getId());
        return order.getId();
    }
}
