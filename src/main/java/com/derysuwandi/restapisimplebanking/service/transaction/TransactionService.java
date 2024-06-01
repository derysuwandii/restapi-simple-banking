package com.derysuwandi.restapisimplebanking.service.transaction;

import com.derysuwandi.restapisimplebanking.dto.request.TransactionRequest;
import com.derysuwandi.restapisimplebanking.dto.response.TransactionResponse;

public interface TransactionService {
    TransactionResponse deposit(TransactionRequest req);

    TransactionResponse withdraw(TransactionRequest req);

}
