package com.derysuwandi.restapisimplebanking.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountRegisterRequest {
    private String email;
    private String password;
    private String confirmPassword;
    private String accountName;
}
