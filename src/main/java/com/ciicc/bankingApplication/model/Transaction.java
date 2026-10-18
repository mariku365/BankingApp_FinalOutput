package com.ciicc.bankingApplication.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter 
@NoArgsConstructor
@Table (name = "transactions")
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
        length = 20
    )
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    @Column(
        name = "transaction_type",
        nullable = false
    )
    private TransactionType transactionType;

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

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
