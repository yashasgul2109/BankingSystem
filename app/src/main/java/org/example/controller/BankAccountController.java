package org.example.controller;

import org.example.dto.WithdrawRequest;
import org.example.dto.DepositRequest;
import org.example.entity.BankAccount;
import org.example.service.BankAccountService;
import org.example.dto.TransferRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class BankAccountController {

    private final BankAccountService bankAccountService;

    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @PostMapping("/{accountId}/withdraw")
    public BankAccount withdraw(
            @PathVariable Long accountId,
            @RequestBody WithdrawRequest request
    ) {
        return bankAccountService.withdraw(accountId, request);
    }
    @PostMapping("/{accountId}/deposit")
public BankAccount deposit(
        @PathVariable Long accountId,
        @RequestBody DepositRequest request) {

    return bankAccountService.deposit(accountId, request);
}

@PostMapping("/{accountId}/transfer")
public BankAccount transfer(
        @PathVariable Long accountId,
        @RequestBody TransferRequest request) {

    return bankAccountService.transfer(accountId, request);
}

}