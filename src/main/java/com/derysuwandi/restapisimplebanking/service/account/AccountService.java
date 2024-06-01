package com.derysuwandi.restapisimplebanking.service.account;

import com.derysuwandi.restapisimplebanking.dto.request.AccountRegisterRequest;
import com.derysuwandi.restapisimplebanking.dto.request.LoginRequest;
import com.derysuwandi.restapisimplebanking.dto.response.AccountDetailResponse;
import com.derysuwandi.restapisimplebanking.dto.response.AccountRegisterResponse;
import com.derysuwandi.restapisimplebanking.dto.response.LoginResponse;

public interface AccountService {
    LoginResponse login(LoginRequest req);
    AccountRegisterResponse accountRegister(AccountRegisterRequest req);
    AccountDetailResponse accountDetail();
}
