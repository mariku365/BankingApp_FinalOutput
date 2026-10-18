package com.ciicc.bankingApplication.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter 
public class TransactionHistoryRequest {
    private String accountNumber;
    private String accountName;
    private BigDecimal balance;
}
