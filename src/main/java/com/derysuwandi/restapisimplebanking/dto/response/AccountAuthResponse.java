package com.derysuwandi.restapisimplebanking.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountAuthResponse {
    private String email;
    private String accountName;
    private Integer accountId;

}
