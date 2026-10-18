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
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
// import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@NoArgsConstructor
@Getter 
@Setter
@Table(name = "accounts")
public class Account {
    
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;


    @Column(
        name = "account_number",
        unique = true,
        length = 20
    )
    private String accountNumber;

    @Column(
        name = "account_name",
        nullable = false
    )
    private String accountName;

    @Column(
        name = "balance",
        precision = 20,
        scale = 2
    )
    private BigDecimal balance = BigDecimal.ZERO;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @PrePersist
    private void prePersistBalance() {
        if (balance == null) {
            balance = BigDecimal.ZERO;
        }
    }
}
