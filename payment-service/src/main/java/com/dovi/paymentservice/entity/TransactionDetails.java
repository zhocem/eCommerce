package com.dovi.paymentservice.entity;

import com.dovi.paymentservice.model.PaymentMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "TRANSACTION_DETAILS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long orderId;
    private PaymentMode paymentMode;
    private String referenceNumber;
    private Instant paymentDate;
    private String paymentStatus;
    private long amount;
}
