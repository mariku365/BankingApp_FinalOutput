package com.ciicc.bankingApplication.service;

import org.springframework.stereotype.Service;

import com.ciicc.bankingApplication.dto.DepositRequest;
import com.ciicc.bankingApplication.dto.TransactionHistoryRequest;
import com.ciicc.bankingApplication.dto.TransferRequest;
import com.ciicc.bankingApplication.dto.WithdrawRequest;
import com.ciicc.bankingApplication.model.Account;
import com.ciicc.bankingApplication.model.Transaction;
import com.ciicc.bankingApplication.model.TransactionType;
import com.ciicc.bankingApplication.repository.AccountRepository;
import com.ciicc.bankingApplication.repository.TransactionRepository;
import com.ciicc.bankingApplication.util.GenerateTransactionNumber;

import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service 
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionService( 
            TransactionRepository transactionRepository,
            AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    public void deposit(DepositRequest depositRequest) {
        Account account = accountRepository
            .findByAccountNumber(depositRequest.getAccountNumber())
                .orElseThrow(() -> 
                new RuntimeException("Account not found"));

        BigDecimal amount = depositRequest.getAmount();

        if (amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new RuntimeException("Amount must be greater that zero.");
        }

        account.setBalance(account.getBalance().add(amount));

        accountRepository.save(account);

        saveTransaction(
            account,
            TransactionType.DEPOSIT,
            amount,
            account.getAccountNumber()
        );
    }



    public void withdraw (WithdrawRequest withdrawRequest) {
        Account account = accountRepository
            .findByAccountNumber(withdrawRequest.getAccountNumber())
                .orElseThrow(() -> 
                new RuntimeException("Account not found"));

        BigDecimal amount = withdrawRequest.getAmount();

        if (amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new RuntimeException("Amount must be greater that zero.");
        }

        if (account.getBalance().compareTo(amount) < 0){
            throw new RuntimeException("Insufficient funds.");
        }

        account.setBalance(account.getBalance().subtract(amount));

        accountRepository.save(account);

        saveTransaction(
            account,
            TransactionType.WITHDRAW,
            amount,
            account.getAccountNumber()
        );
    }


    @Transactional
    public void transfer(TransferRequest transferRequest) {
        String sourceAccountNumber = transferRequest.getSourceAccountNumber();
        String destinationAccountNumber = transferRequest.getDestinationAccountNumber();
        BigDecimal amount = transferRequest.getAmount();

            if(sourceAccountNumber.equals(destinationAccountNumber)){
                throw new RuntimeException("Source and destination accounts cannot be the same.");
            }

            if (amount.compareTo(BigDecimal.ZERO) <= 0){
                throw new RuntimeException("Amount must be greater that zero.");
            }

            Account sourceAccount = accountRepository
                .findByAccountNumber(sourceAccountNumber)
                .orElseThrow(() ->
                    new RuntimeException("Source account not found"));

            Account destinationAccount = accountRepository
                .findByAccountNumber(destinationAccountNumber)
                .orElseThrow(() ->
                    new RuntimeException("Destination account not found"));

            if (sourceAccount.getBalance().compareTo(amount) < 0){
                throw new RuntimeException("Insufficient balance.");
            }

            sourceAccount.setBalance(
                sourceAccount.getBalance().subtract(amount)
            );

            destinationAccount.setBalance(
                destinationAccount.getBalance().add(amount)
            );

            accountRepository.save(sourceAccount);
            accountRepository.save(destinationAccount);

            saveTransaction(
                sourceAccount,
                TransactionType.TRANSFER_OUT,
                amount,
                destinationAccountNumber
            );

            saveTransaction(
                destinationAccount,
                TransactionType.TRANSFER_IN,
                amount,
                sourceAccountNumber
            );
        }


    public List<Transaction> getTransactionHistory(TransactionHistoryRequest transactionHistoryRequest) {
        return transactionRepository.findByAccountNumber(transactionHistoryRequest.getAccountNumber());
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    private void saveTransaction(
        Account account,
        TransactionType transactionType,
        BigDecimal amount,
        String referenceAccount) {

    Transaction transaction = new Transaction();

    transaction.setTransactionReference(
        GenerateTransactionNumber.generateTransactionNumber()
    );
    transaction.setTransactionType(transactionType);
    transaction.setAccountNumber(account.getAccountNumber());
    transaction.setAmount(amount);
    transaction.setBalance(account.getBalance());
    transaction.setReferenceAccount(referenceAccount);

    transactionRepository.save(transaction);
}

}
