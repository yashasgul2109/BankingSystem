
package org.example.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountBalanceResponse {

    private Long accountId;
    private String accountNumber;
    private double balance;
}
