package com.ciicc.bankingApplication.dto;

import java.math.BigDecimal;

import lombok.Data;
// import lombok.Getter;
// import lombok.Setter;

@Data  
public class CreateAccountRequest {
    private String accountName;
    private BigDecimal initialDeposit;
}
