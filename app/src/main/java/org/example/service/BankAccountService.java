package org.example.service;

import org.example.dto.WithdrawRequest;
import org.example.entity.BankAccount;
import org.example.repository.BankAccountRepository;
import org.springframework.stereotype.Service;

@Service
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;

    public BankAccountService(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    public BankAccount withdraw(Long accountId, WithdrawRequest request) {

        BankAccount account = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        double amount = request.getAmount();

        if (amount <= 0) {
            throw new RuntimeException("Withdrawal amount must be greater than zero");
        }

        if (account.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance");
        }

        account.setBalance(account.getBalance() - amount);

        return bankAccountRepository.save(account);
    }
}