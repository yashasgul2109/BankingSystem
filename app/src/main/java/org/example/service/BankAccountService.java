
package org.example.service;

import org.example.dto.WithdrawRequest;
import org.example.dto.TransferRequest;
import org.example.dto.DepositRequest;
import org.example.dto.AccountBalanceResponse;
import org.example.entity.BankAccount;
import org.example.entity.Transaction;
import org.example.repository.BankAccountRepository;
import org.example.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final TransactionRepository transactionRepository;

    public BankAccountService(
            BankAccountRepository bankAccountRepository,
            TransactionRepository transactionRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.transactionRepository = transactionRepository;
    }

    private void recordTransaction(
            Long accountId, String type, double amount, String description) {

        Transaction transaction = new Transaction();
        transaction.setAccountId(accountId);
        transaction.setType(type);
        transaction.setAmount(amount);
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setDescription(description);

        transactionRepository.save(transaction);
    }

    @Transactional
    public BankAccount withdraw(Long accountId, WithdrawRequest request) {

        BankAccount account = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        double amount = request.getAmount();

        if (amount <= 0) {
            throw new RuntimeException(
                    "Withdrawal amount must be greater than zero");
        }

        if (account.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance");
        }

        account.setBalance(account.getBalance() - amount);
        BankAccount savedAccount = bankAccountRepository.save(account);

        recordTransaction(
                accountId, "WITHDRAWAL", amount, "Cash withdrawal");

        return savedAccount;
    }

    @Transactional
    public BankAccount deposit(Long accountId, DepositRequest request) {

        BankAccount account = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        double amount = request.getAmount();

        if (amount <= 0) {
            throw new RuntimeException(
                    "Deposit amount must be greater than zero");
        }

        account.setBalance(account.getBalance() + amount);
        BankAccount savedAccount = bankAccountRepository.save(account);

        recordTransaction(
                accountId, "DEPOSIT", amount, "Cash deposit");

        return savedAccount;
    }

    @Transactional
    public BankAccount transfer(
            Long fromAccountId, TransferRequest request) {

        BankAccount sender = bankAccountRepository.findById(fromAccountId)
                .orElseThrow(() -> new RuntimeException(
                        "Sender account not found"));

        BankAccount receiver = bankAccountRepository
                .findById(request.getToAccountId())
                .orElseThrow(() -> new RuntimeException(
                        "Receiver account not found"));

        double amount = request.getAmount();

        if (amount <= 0) {
            throw new RuntimeException(
                    "Transfer amount must be greater than zero");
        }

        if (sender.getId().equals(receiver.getId())) {
            throw new RuntimeException(
                    "Cannot transfer to the same account");
        }

        if (sender.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance");
        }

        sender.setBalance(sender.getBalance() - amount);
        receiver.setBalance(receiver.getBalance() + amount);

        bankAccountRepository.save(sender);
        bankAccountRepository.save(receiver);

        recordTransaction(fromAccountId, "TRANSFER_SENT", amount,
                "Transfer to account " + receiver.getId());

        recordTransaction(receiver.getId(), "TRANSFER_RECEIVED", amount,
                "Transfer from account " + sender.getId());

        return sender;
    }

    public AccountBalanceResponse checkBalance(Long accountId) {

        BankAccount account = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        AccountBalanceResponse response = new AccountBalanceResponse();
        response.setAccountId(account.getId());
        response.setAccountNumber(account.getAccountNumber());
        response.setBalance(account.getBalance());

        return response;
    }
}
