package com.derysuwandi.restapisimplebanking.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionResponse {
    private Integer accountNumber;
    private String accountName;
    private double saldo;
}
