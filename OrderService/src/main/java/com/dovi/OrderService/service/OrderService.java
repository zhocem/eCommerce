package com.dovi.OrderService.service;

import com.dovi.OrderService.model.OrderRequest;

public interface OrderService {
    long placeOrder(OrderRequest orderRequest);
}
