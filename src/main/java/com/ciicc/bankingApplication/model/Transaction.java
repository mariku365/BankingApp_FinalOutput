package com.ciicc.bankingApplication.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity 
public class Transaction {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
        name = "transaction_reference",
        nullable = false,
        unique = true,
        length = 20
    )
    private String transactionReference;

    @Column(
        name = "account_number",
        nullable = false,
        length = 10
    )
    private String accountNumber;

    @Column(
        name = "transaction_type",
        nullable = false,
        length = 20
    )
    private String transactionType;

    @Column (
        name = "amount",
        nullable = false,
        precision = 20,
        scale = 2
    )
    private BigDecimal amount;

    @Column (
        name = "balance",
        nullable = false,
        precision = 20,
        scale = 2
    )
    private BigDecimal balance;

    @Column(
        name = "reference_account",
        nullable = false,
        length = 255
    )
    private String referenceAccount;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
