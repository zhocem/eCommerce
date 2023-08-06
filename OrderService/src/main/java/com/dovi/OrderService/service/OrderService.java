package com.dovi.OrderService.service;

import com.dovi.OrderService.model.OrderRequest;
import com.dovi.OrderService.model.OrderResponse;

public interface OrderService {
    long placeOrder(OrderRequest orderRequest);

    OrderResponse getOrderDetails(long orderId);
}
