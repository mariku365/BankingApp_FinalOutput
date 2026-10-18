package com.ciicc.bankingApplication.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class TransferRequest {
    private String sourceAccountNumber;
    private String destinationAccountNumber;
    private BigDecimal amount;
}
