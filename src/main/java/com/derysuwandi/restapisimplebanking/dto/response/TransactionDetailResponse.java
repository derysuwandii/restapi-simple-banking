package com.derysuwandi.restapisimplebanking.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class TransactionDetailResponse {
    private String transactionType;
    private double amount;
    private Date createdDate;

}
