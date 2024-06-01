package com.derysuwandi.restapisimplebanking.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AccountDetailResponse {
    private Integer accountNumber;
    private String accountName;
    private double balance;
    private List<TransactionDetailResponse> transaction;

}
