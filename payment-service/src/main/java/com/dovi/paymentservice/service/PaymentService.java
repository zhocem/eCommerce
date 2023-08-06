package com.dovi.paymentservice.service;

import com.dovi.paymentservice.model.PaymentRequest;

public interface PaymentService {
    Long doPayment(PaymentRequest paymentRequest);
}
