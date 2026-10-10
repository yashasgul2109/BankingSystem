package org.example.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferRequest {

    private Long toAccountId;

    private double amount;
}