package com.derysuwandi.restapisimplebanking.controller;

import com.derysuwandi.restapisimplebanking.dto.request.TransactionRequest;
import com.derysuwandi.restapisimplebanking.exception.JsonResponse;
import com.derysuwandi.restapisimplebanking.service.transaction.TransactionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transaction")
public class TransactionRestController {
    private final TransactionService transactionService;

    public TransactionRestController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/deposit")
    public JsonResponse<?> deposit(@RequestBody TransactionRequest req) {
        return JsonResponse.ok(transactionService.deposit(req));
    }

    @PostMapping("/withdraw")
    public JsonResponse<?> withdraw(@RequestBody TransactionRequest req) {
        return JsonResponse.ok(transactionService.withdraw(req));
    }
}
