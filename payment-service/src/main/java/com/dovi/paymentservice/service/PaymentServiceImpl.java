package com.dovi.paymentservice.service;

import com.dovi.paymentservice.entity.TransactionDetails;
import com.dovi.paymentservice.model.PaymentMode;
import com.dovi.paymentservice.model.PaymentRequest;
import com.dovi.paymentservice.model.PaymentResponse;
import com.dovi.paymentservice.repository.TransactionDetailsRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Log4j2
public class PaymentServiceImpl implements PaymentService {

    private final TransactionDetailsRepository transactionDetailsRepository;

    public PaymentServiceImpl(TransactionDetailsRepository transactionDetailsRepository) {
        this.transactionDetailsRepository = transactionDetailsRepository;
    }

    @Override
    public Long doPayment(PaymentRequest paymentRequest) {
        log.info("Recording payment details: {}", paymentRequest);

        TransactionDetails transaction = TransactionDetails.builder()
                .paymentDate(Instant.now())
                .paymentMode(String.valueOf(paymentRequest.getPaymentMode()))
                .paymentStatus("SUCCESS")
                .orderId(paymentRequest.getOrderId())
                .amount(paymentRequest.getAmount())
                .referenceNumber(paymentRequest.getReferenceNumber())
                .build();

        transactionDetailsRepository.save(transaction);

        log.info("Transaction {} completed", transaction.getId());

        return transaction.getId();
    }

    @Override
    public PaymentResponse getPaymentDetailsByOrderId(String orderId) {
        log.info("Getting payment details for order id {}", orderId);
        TransactionDetails transactionDetails = transactionDetailsRepository.findByOrderId(Long.parseLong(orderId));

        return PaymentResponse.builder()
                .paymentId(transactionDetails.getId())
                .orderId(transactionDetails.getOrderId())
                .status(transactionDetails.getPaymentStatus())
                .amount(transactionDetails.getAmount())
                .paymentMode(PaymentMode.valueOf(transactionDetails.getPaymentMode()))
                .paymentDate(transactionDetails.getPaymentDate())
                .build();
    }
}
