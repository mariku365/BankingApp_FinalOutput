package com.ciicc.bankingApplication.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ciicc.bankingApplication.dto.BalanceInquiryRequest;
import com.ciicc.bankingApplication.dto.CreateAccountRequest;
import com.ciicc.bankingApplication.model.Account;
import com.ciicc.bankingApplication.repository.AccountRepository;
import com.ciicc.bankingApplication.util.GenerateAccountNumber;

@Service 
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public Account createAccount(CreateAccountRequest createAccountRequest){
        Account account = new Account();

        account.setAccountName(createAccountRequest.getAccountName());
        account.setBalance(createAccountRequest.getInitialDeposit());

        Account saved = accountRepository.save(account);
        
        saved.setAccountNumber(
            GenerateAccountNumber.generateAccountNumber(saved.getId())
        );
        return accountRepository.save(saved);
    }

    public Account getAccount(
        BalanceInquiryRequest balanceInquiryRequest
    ){
        return accountRepository
            .findByAccountNumber(balanceInquiryRequest.getAccountNumber())
            .orElseThrow(() -> 
                new RuntimeException("Account not found"));
    }


    public List<Account> listAccounts() {
        return accountRepository.findAll();
    }
}
