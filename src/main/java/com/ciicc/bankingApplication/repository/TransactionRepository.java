package com.ciicc.bankingApplication.repository;

import java.util.List;
// import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ciicc.bankingApplication.model.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
    
    List<Transaction> findByAccountNumber(String accountNumber);
}
