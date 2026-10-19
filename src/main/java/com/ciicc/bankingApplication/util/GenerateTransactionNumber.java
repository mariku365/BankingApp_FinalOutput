package com.ciicc.bankingApplication.util;

public class GenerateTransactionNumber {
    public static String generateTransactionNumber(){
        String transactionNumber = "TXN" + System.currentTimeMillis();
        return transactionNumber;
    }
}