package com.ciicc.bankingApplication.ui;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.ciicc.bankingApplication.service.AccountService;
import com.ciicc.bankingApplication.service.TransactionService;

import com.ciicc.bankingApplication.dto.BalanceInquiryRequest;
import com.ciicc.bankingApplication.dto.CreateAccountRequest;
import com.ciicc.bankingApplication.dto.DepositRequest;
import com.ciicc.bankingApplication.dto.TransactionHistoryRequest;
import com.ciicc.bankingApplication.dto.TransferRequest;
import com.ciicc.bankingApplication.dto.WithdrawRequest;
import com.ciicc.bankingApplication.model.Account;
import com.ciicc.bankingApplication.model.Transaction;


@Component 
public class ConsoleUI implements CommandLineRunner{
    
    private final AccountService accountService;
    private final TransactionService transactionService;

    public ConsoleUI(AccountService accountService, TransactionService transactionService){
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner userInput = new Scanner(System.in);
        boolean running = true;
        while (running){
            bankingMenu();

            System.out.print("Enter your choice: ");
            int choice = Integer.parseInt(userInput.nextLine());

            switch (choice){
                case 1 -> createAccount(userInput);
                case 2 -> balanceInquiry(userInput);
                case 3 -> listAccounts();
                case 4 -> deposit(userInput);
                // case 5 -> withdraw(userInput);
                // case 6 -> transfer(userInput);
                // case 7 -> transactionHistory(userInput);
                case 0 -> {
                    System.out.println("Exiting the application. Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
            
        }
    }

    public void bankingMenu(){
        System.out.println("================================");
        System.out.println("       BANKING APPLICATION");
        System.out.println("================================");
        System.out.println("1. Create Account");
        System.out.println("2. Balance Inquiry");
        System.out.println("3. List Accounts");
        System.out.println("4. Deposit");
        System.out.println("5. Withdraw");
        System.out.println("6. Transfer");
        System.out.println("7. Transaction History");
        System.out.println("0. Exit");
    }

    private void createAccount(Scanner userInput){
        System.out.println("================================");
        System.out.println("       ACCOUNT CREATION");
        System.out.println("================================");

        System.out.print("Enter account name: ");
        String accountName = userInput.nextLine();

        System.out.print("Enter initial deposit: ");
        BigDecimal initialDeposit = new BigDecimal(userInput.nextLine());

        CreateAccountRequest createAccountRequest = new CreateAccountRequest();
        createAccountRequest.setAccountName(accountName);
        createAccountRequest.setInitialDeposit(initialDeposit);

        Account account = accountService.createAccount(createAccountRequest);

        System.out.println("\nAccount Number: " + account.getAccountNumber());
        System.out.println("Account Name: " + account.getAccountName());
        System.out.printf("Initial Deposit: P%.2f%n\n", account.getBalance());

        System.out.println("Account created successfully!");
        
    }


    public void balanceInquiry(Scanner userInput){
        System.out.println("================================");
        System.out.println("       BALANCE INQUIRY");
        System.out.println("================================");

        System.out.print("Enter account number: ");
        String accountNumber = userInput.nextLine();

        BalanceInquiryRequest balanceInquiryRequest = new BalanceInquiryRequest();
        balanceInquiryRequest.setAccountNumber(accountNumber);

        Account account = accountService.getAccount(balanceInquiryRequest);
        String accountName = account.getAccountName();
        BigDecimal balance = account.getBalance();

        System.out.println("\nAccount Number: " + accountNumber);
        System.out.println("Account Name: " + accountName);
        System.out.printf("Current Balance: P%.2f%n\n", balance);
    }


    public void listAccounts(){
        List<Account> accounts = accountService.listAccounts();
        System.out.println("================================");
        System.out.println("       LIST OF ACCOUNTS");
        System.out.println("================================");
        for (Account account : accounts) {
            System.out.printf("Account Number: %s%n", account.getAccountNumber());
            System.out.printf("Account Name: %s%n", account.getAccountName());
            System.out.printf("Current Balance: P%.2f%n%n", account.getBalance());
        }
    }

    public void deposit(Scanner userInput){
        System.out.println("================================");
        System.out.println("            DEPOSIT");
        System.out.println("================================");

        System.out.print("Enter account number: ");
        String accountNumber = userInput.nextLine();

        BalanceInquiryRequest balanceInquiryRequest = new BalanceInquiryRequest();
        balanceInquiryRequest.setAccountNumber(accountNumber);

        Account accountBefore = accountService.getAccount(balanceInquiryRequest);
        BigDecimal previousBalance = accountBefore.getBalance();

        System.out.print("Enter deposit amount: ");
        BigDecimal amount = new BigDecimal(userInput.nextLine());

        DepositRequest depositRequest = new DepositRequest();

        depositRequest.setAccountNumber(accountNumber);
        depositRequest.setAmount(amount);

        transactionService.deposit(depositRequest);

        Account accountAfter = accountService.getAccount(balanceInquiryRequest);
        BigDecimal newBalance = accountAfter.getBalance();

        System.out.println("================================");
        System.out.println("         DEPOSIT SUMMARY");
        System.out.println("================================");
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Previous Balance: P" + previousBalance);
        System.out.printf("Deposit Amount: P%.2f%n\n", amount);
        System.out.println("New Balance: P" + newBalance);
        System.out.println("================================");
        System.out.println("Deposit successful!");
    }

}
