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
                case 5 -> withdraw(userInput);
                case 6 -> transfer(userInput);
                case 7 -> transactionHistory(userInput);
                case 0 -> {
                    System.out.println("Exiting the application. Goodbye!\n");
                    running = false;
                    System.exit(0);
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

        if (initialDeposit.compareTo(BigDecimal.ZERO) < 0) {
            System.out.println("Initial deposit cannot be less than zero.");
            return;
        }

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

        try {
            Account account = accountService.getAccount(balanceInquiryRequest);

            String accountName = account.getAccountName();
            BigDecimal balance = account.getBalance();

            System.out.println("\nAccount Number: " + accountNumber);
            System.out.println("Account Name: " + accountName);
            System.out.printf("Current Balance: P%.2f%n\n", balance);
        } catch (Exception e){
            System.out.println("Account does not exist.");
        }

    }


    public void listAccounts(){
        List<Account> accounts = accountService.listAccounts();
        System.out.println("================================");
        System.out.println("       LIST OF ACCOUNTS");
        System.out.println("================================");
        for (Account account : accounts) {
            System.out.printf("Account Number: %s%n", account.getAccountNumber());
            System.out.printf("Account Name: %s%n", account.getAccountName());
            System.out.printf("Current Balance: P%.2f%n%n\n", account.getBalance());
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

        try {
            Account accountBefore = accountService.getAccount(balanceInquiryRequest);
            BigDecimal previousBalance = accountBefore.getBalance();

            System.out.print("Enter deposit amount: ");
            BigDecimal amount = new BigDecimal(userInput.nextLine());

            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("Deposit amount must be greater than zero.");
                return;
            }

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
            System.out.println("Deposit successful!\n");
        } catch (Exception e){
            System.out.println("Account does not exist.");
        }
        
    }

    public void withdraw(Scanner userInput){
        System.out.println("================================");
        System.out.println("            WITHDRAW");
        System.out.println("================================");

        System.out.print("Enter account number: ");
        String accountNumber = userInput.nextLine();

        BalanceInquiryRequest balanceInquiryRequest = new BalanceInquiryRequest();
        balanceInquiryRequest.setAccountNumber(accountNumber);
        
        try {
            Account accountBefore = accountService.getAccount(balanceInquiryRequest);
            BigDecimal previousBalance = accountBefore.getBalance();

            System.out.print("Enter withdrawal amount: ");
            BigDecimal amount = new BigDecimal(userInput.nextLine());

            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("Withdrawal amount must be greater than zero.");
                return;
            }

            WithdrawRequest withdrawRequest = new WithdrawRequest();
            withdrawRequest.setAccountNumber(accountNumber);
            withdrawRequest.setAmount(amount);

            transactionService.withdraw(withdrawRequest);

            Account accountAfter = accountService.getAccount(balanceInquiryRequest);
            BigDecimal newBalance = accountAfter.getBalance();

            System.out.println("================================");
            System.out.println("       WITHDRAWAL SUMMARY");
            System.out.println("================================");
            System.out.println("Account Number: " + accountNumber);
            System.out.println("Previous Balance: P" + previousBalance);
            System.out.printf("Withdrawal Amount: P%.2f%n\n", amount);
            System.out.println("New Balance: P" + newBalance);
            System.out.println("================================");
            System.out.println("Withdrawal successful!\n");
        } catch (Exception e){
            System.out.println("Account does not exist.");
        }
        
    }

    public void transfer(Scanner userInput){
        System.out.println("================================");
        System.out.println("            TRANSFER");
        System.out.println("================================");

        System.out.print("Enter source account number: ");
        String sourceAccountNumber = userInput.nextLine();

        System.out.print("Enter destination account number: ");
        String destinationAccountNumber = userInput.nextLine();

        List<Account> accounts = accountService.listAccounts();

        boolean sourceExists = accounts.stream()
        .anyMatch(account ->
                account.getAccountNumber().equals(sourceAccountNumber));
        
        boolean destinationExists = accounts.stream()
        .anyMatch(account ->
                account.getAccountNumber().equals(destinationAccountNumber));
        
        if (!sourceExists) {
            System.out.println("Source Account does not exist.\n");
            return;
        }

        if (!destinationExists) {
            System.out.println("Destination account does not exist.\n");
            return;
        }

        BalanceInquiryRequest sourceBalanceInquiryRequest = new BalanceInquiryRequest();
        sourceBalanceInquiryRequest.setAccountNumber(sourceAccountNumber);

        BalanceInquiryRequest destinationBalanceInquiryRequest = new BalanceInquiryRequest();
        destinationBalanceInquiryRequest.setAccountNumber(destinationAccountNumber);

        System.out.print("Enter transfer amount: ");
        BigDecimal amount = new BigDecimal(userInput.nextLine());
        
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Transfer amount must be greater than zero.");
            return;
        }

        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setSourceAccountNumber(sourceAccountNumber);
        transferRequest.setDestinationAccountNumber(destinationAccountNumber);
        transferRequest.setAmount(amount);

        if (sourceAccountNumber.equals(destinationAccountNumber)){
            System.out.println("Source and destination accounts cannot be the same.");
            return;
        }

        transactionService.transfer(transferRequest);

        System.out.println("================================");
        System.out.println("         TRANSFER SUMMARY");
        System.out.println("================================");
        System.out.println("\nFrom Account: " + sourceAccountNumber);
        System.out.println("To Account: " + destinationAccountNumber);
        System.out.printf("Transfer Amount: P%.2f%n\n", amount);
        System.out.println("================================");
        System.out.println("Transfer successful!\n");
    }

    public void transactionHistory(Scanner userInput){
        System.out.println("================================");
        System.out.println("     TRANSACTION HISTORY");
        System.out.println("================================");

        System.out.print("Enter account number: ");
        String accountNumber = userInput.nextLine();

        BalanceInquiryRequest balanceInquiryRequest = new BalanceInquiryRequest();
        balanceInquiryRequest.setAccountNumber(accountNumber);

        try {
            accountService.getAccount(balanceInquiryRequest);
        } catch (RuntimeException e) {
            System.out.println("Account does not exist.");
            return;
        }

        TransactionHistoryRequest transactionHistoryRequest = new TransactionHistoryRequest();
        transactionHistoryRequest.setAccountNumber(accountNumber);

        List<Transaction> transactions = transactionService.getTransactionHistory(transactionHistoryRequest);

        System.out.println("\nTransaction History for Account: " + accountNumber);
        System.out.println("================================================\n");

        if (transactions == null || transactions.isEmpty()) {
            System.out.println("No transactions found for this account.");
            return;
        }

        for (Transaction transaction : transactions) {
            System.out.printf("Transaction Reference: %s%n", transaction.getTransactionReference());
            System.out.printf("Transaction Type: %s%n", transaction.getTransactionType());
            System.out.printf("Amount: P%.2f%n", transaction.getAmount());
            
            if (transaction.getReferenceAccount() != null
                && !transaction.getReferenceAccount().isBlank()) {
            System.out.println("Reference Account: "
                    + transaction.getReferenceAccount());
            } 
            
            System.out.printf("Balance After Transaction: P%.2f%n",
                transaction.getBalance());

            System.out.printf("Date: %s%n%n\n", transaction.getCreatedAt());
        }
    }
}
