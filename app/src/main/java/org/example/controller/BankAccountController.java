package org.example.controller;

import org.example.dto.WithdrawRequest;
import org.example.entity.BankAccount;
import org.example.service.BankAccountService;
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
}