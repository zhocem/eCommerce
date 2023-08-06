package com.dovi.paymentservice.service;

import com.dovi.paymentservice.model.PaymentRequest;
import com.dovi.paymentservice.model.PaymentResponse;

public interface PaymentService {
    Long doPayment(PaymentRequest paymentRequest);

    PaymentResponse getPaymentDetailsByOrderId(String orderId);
}
