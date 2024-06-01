package com.derysuwandi.restapisimplebanking.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountRegisterResponse {
    private String email;
    private Integer accountNumber;
    private String accountName;
    private double balance;
}
