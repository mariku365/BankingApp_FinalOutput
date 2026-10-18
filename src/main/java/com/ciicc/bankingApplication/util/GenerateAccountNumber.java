package com.ciicc.bankingApplication.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GenerateAccountNumber {

    public static String generateAccountNumber(Long id) {
        String prefix = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyHH"));

        return "ACC-" + prefix + "-" + String.format("%06d", id);
    }
}
