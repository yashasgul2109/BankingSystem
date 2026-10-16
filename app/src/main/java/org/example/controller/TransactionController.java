
package org.example.controller;

import org.example.dto.TransactionResponse;
import org.example.entity.Transaction;
import org.example.repository.TransactionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class TransactionController {

    private final TransactionRepository transactionRepository;

    public TransactionController(
            TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @GetMapping("/{accountId}/transactions")
    public List<TransactionResponse> getTransactionHistory(
            @PathVariable Long accountId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Transaction> transactionPage =
                transactionRepository
                        .findByAccountIdOrderByTimestampDesc(
                                accountId, pageable);

        return transactionPage.getContent().stream().map(transaction -> {
            TransactionResponse response = new TransactionResponse();

            response.setId(transaction.getId());
            response.setType(transaction.getType());
            response.setAmount(transaction.getAmount());
            response.setTimestamp(transaction.getTimestamp());
            response.setDescription(transaction.getDescription());

            return response;
        }).toList();
    }
}
