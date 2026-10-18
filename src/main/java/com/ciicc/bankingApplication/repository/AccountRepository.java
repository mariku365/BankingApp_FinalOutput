package com.ciicc.bankingApplication.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ciicc.bankingApplication.model.Account;



public interface AccountRepository extends JpaRepository<Account, Long>{
    Optional<Account> findById(long id);
}
