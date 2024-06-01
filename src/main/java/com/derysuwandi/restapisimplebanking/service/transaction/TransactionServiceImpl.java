package com.derysuwandi.restapisimplebanking.service.transaction;

import com.derysuwandi.restapisimplebanking.constant.Constant;
import com.derysuwandi.restapisimplebanking.dto.request.TransactionRequest;
import com.derysuwandi.restapisimplebanking.dto.response.AccountAuthResponse;
import com.derysuwandi.restapisimplebanking.dto.response.TransactionResponse;
import com.derysuwandi.restapisimplebanking.entity.Account;
import com.derysuwandi.restapisimplebanking.entity.Transaction;
import com.derysuwandi.restapisimplebanking.exception.InfoLevelException;
import com.derysuwandi.restapisimplebanking.repo.AccountRepository;
import com.derysuwandi.restapisimplebanking.repo.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    @Override
    public TransactionResponse deposit(TransactionRequest req) {
        if(req.getBalance()== null || req.getBalance() == 0)
            throw new InfoLevelException("Balance must be more than 0");

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null)
            throw new InfoLevelException("Unauthorized");

        AccountAuthResponse accountDetail = (AccountAuthResponse) authentication.getPrincipal();

        final Account account = accountRepository.findById(accountDetail.getAccountId())
                .orElseThrow(() -> new InfoLevelException("Account not found"));

        Transaction transaction = new Transaction();
        transaction.setAmount(req.getBalance());
        transaction.setTransactionType(Constant.TransactionType.dp);
        transaction.setAccount(account);
        transactionRepository.save(transaction);

        account.setBalance(account.getBalance()+req.getBalance());
        account.setUpdatedDate(new Date());
        Account updatedAccount = accountRepository.save(account);

        return TransactionResponse.builder()
                .accountName(updatedAccount.getAccountName())
                .saldo(updatedAccount.getBalance())
                .accountNumber(updatedAccount.getAccountNumber())
                .build();
    }

    @Transactional
    @Override
    public TransactionResponse withdraw(TransactionRequest req) {
        if(req.getBalance()== null || req.getBalance() == 0)
            throw new InfoLevelException("Balance must be more than 0");

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null)
            throw new InfoLevelException("Unauthorized");

        AccountAuthResponse accountDetail = (AccountAuthResponse) authentication.getPrincipal();

        final Account account = accountRepository.findById(accountDetail.getAccountId())
                .orElseThrow(() -> new InfoLevelException("Account not found!"));

        if(req.getBalance() > account.getBalance())
            throw new InfoLevelException("Insufficient Balance!");

        Transaction transaction = new Transaction();
        transaction.setAmount(req.getBalance());
        transaction.setTransactionType(Constant.TransactionType.wd);
        transaction.setAccount(account);
        transactionRepository.save(transaction);

        account.setBalance(account.getBalance()-req.getBalance());
        account.setUpdatedDate(new Date());
        Account updatedAccount = accountRepository.save(account);

        return TransactionResponse.builder()
                .accountName(updatedAccount.getAccountName())
                .saldo(updatedAccount.getBalance())
                .accountNumber(updatedAccount.getAccountNumber())
                .build();
    }
}