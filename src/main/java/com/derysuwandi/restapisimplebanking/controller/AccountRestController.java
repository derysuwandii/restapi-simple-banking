package com.derysuwandi.restapisimplebanking.controller;

import com.derysuwandi.restapisimplebanking.dto.request.AccountRegisterRequest;
import com.derysuwandi.restapisimplebanking.dto.request.LoginRequest;
import com.derysuwandi.restapisimplebanking.exception.JsonResponse;
import com.derysuwandi.restapisimplebanking.service.account.AccountService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
public class AccountRestController {
    private final AccountService accountService;

    public AccountRestController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/register")
    public JsonResponse<?> register(@RequestBody AccountRegisterRequest req) {
        return JsonResponse.ok(accountService.accountRegister(req));
    }

    @PostMapping("/login")
    public JsonResponse<?> login(@RequestBody LoginRequest req) {
        return JsonResponse.ok(accountService.login(req));
    }

    @GetMapping("/my-balance")
    public JsonResponse<?> myBalance() {
        return JsonResponse.ok(accountService.accountDetail());
    }
}
