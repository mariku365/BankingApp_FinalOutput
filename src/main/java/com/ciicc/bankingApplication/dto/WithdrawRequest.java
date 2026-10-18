package com.ciicc.bankingApplication.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class WithdrawRequest {
    private String accountNumber;
    private BigDecimal amount;
}
