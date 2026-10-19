package com.ciicc.bankingApplication.controller;

import org.springframework.web.bind.annotation.RestController;

// import com.ciicc.bankingApplication.model.Account;
import com.ciicc.bankingApplication.repository.AccountRepository;

import lombok.RequiredArgsConstructor;

// import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;



@RestController 
@RequiredArgsConstructor
@RequestMapping ("/api/accounts")
public class AccountController {

    private final AccountRepository accountRepository;


    @GetMapping("/accounts")
    public ResponseEntity<?> getAllProducts(){
        return ResponseEntity.ok(accountRepository.findAll());
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<?> findById(@PathVariable long id){
        return ResponseEntity.ok(accountRepository.findById(id));
    }
}
