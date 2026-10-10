package org.example.service;

import org.example.dto.WithdrawRequest;
import org.example.dto.TransferRequest;
import org.springframework.transaction.annotation.Transactional;
import org.example.entity.BankAccount;
import org.example.dto.DepositRequest;
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
    
public BankAccount deposit(Long accountId, DepositRequest request) {

    BankAccount account = bankAccountRepository.findById(accountId)
            .orElseThrow(() -> new RuntimeException("Account not found"));


    double amount = request.getAmount();

    if (amount <= 0) {
        throw new RuntimeException("Deposit amount must be greater than zero");
    }

    account.setBalance(account.getBalance() + amount);

    return bankAccountRepository.save(account);
    }
    
@Transactional
public BankAccount transfer(Long fromAccountId, TransferRequest request) {


    BankAccount sender = bankAccountRepository.findById(fromAccountId)
            .orElseThrow(() -> new RuntimeException("Sender account not found"));

    BankAccount receiver = bankAccountRepository.findById(request.getToAccountId())
            .orElseThrow(() -> new RuntimeException("Receiver account not found"));

    double amount = request.getAmount();

    if (amount <= 0) {
        throw new RuntimeException("Transfer amount must be greater than zero");
    }

    if (sender.getId().equals(receiver.getId())) {
        throw new RuntimeException("Cannot transfer to the same account");
    }

    if (sender.getBalance() < amount) {
        throw new RuntimeException("Insufficient balance");
    }

    sender.setBalance(sender.getBalance() - amount);

    receiver.setBalance(receiver.getBalance() + amount);

    bankAccountRepository.save(sender);
    bankAccountRepository.save(receiver);

    return sender;
}

}