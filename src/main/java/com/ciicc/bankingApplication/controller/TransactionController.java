package com.ciicc.bankingApplication.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ciicc.bankingApplication.dto.DepositRequest;
import com.ciicc.bankingApplication.dto.TransactionHistoryRequest;
import com.ciicc.bankingApplication.dto.TransferRequest;
import com.ciicc.bankingApplication.dto.WithdrawRequest;
import com.ciicc.bankingApplication.model.Transaction;
import com.ciicc.bankingApplication.service.TransactionService;

@RestController
@RequestMapping ("/api/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(
        @RequestBody DepositRequest depositRequest){

            transactionService.deposit(depositRequest);

            return ResponseEntity.ok("Deposit successful");
        }

    @PostMapping ("/withdraw")
    public ResponseEntity<String> withdraw(
        @RequestBody WithdrawRequest withdrawRequest){
            transactionService.withdraw(withdrawRequest);

            return ResponseEntity.ok("Withdrawal successful");
        }

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(
        @RequestBody TransferRequest transferRequest) {

            transactionService.transfer(transferRequest);

            return ResponseEntity.ok("Transfer successful");
        }

    @GetMapping 
    public ResponseEntity<List<Transaction>> getAllTransactions(){
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @GetMapping("/history/{accountNumber}")
    public ResponseEntity<List<Transaction>> getTransactionHistory(@PathVariable String accountNumber){
        TransactionHistoryRequest request = new TransactionHistoryRequest();
        request.setAccountNumber(accountNumber);
        return ResponseEntity.ok(transactionService.getTransactionHistory(request));
    }
}
