package com.ciicc.bankingApplication.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ciicc.bankingApplication.model.Account;

@Repository 
public interface AccountRepository extends JpaRepository<Account, Long>{
    Optional<Account> findById(long id);

    Optional<Account> findByAccountNumber(String accountNumber);
}
