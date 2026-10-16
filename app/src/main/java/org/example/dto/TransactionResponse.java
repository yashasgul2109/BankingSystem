package org.example.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TransactionResponse {

    private Long id;
    private String type;
    private double amount;
    private LocalDateTime timestamp;
    private String description;
}