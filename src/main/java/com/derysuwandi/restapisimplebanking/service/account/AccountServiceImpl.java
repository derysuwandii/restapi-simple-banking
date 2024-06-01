package com.derysuwandi.restapisimplebanking.service.account;

import com.derysuwandi.restapisimplebanking.dto.request.AccountRegisterRequest;
import com.derysuwandi.restapisimplebanking.dto.request.LoginRequest;
import com.derysuwandi.restapisimplebanking.dto.response.*;
import com.derysuwandi.restapisimplebanking.entity.Account;
import com.derysuwandi.restapisimplebanking.entity.Transaction;
import com.derysuwandi.restapisimplebanking.entity.Users;
import com.derysuwandi.restapisimplebanking.exception.InfoLevelException;
import com.derysuwandi.restapisimplebanking.repo.AccountRepository;
import com.derysuwandi.restapisimplebanking.repo.UserRepository;
import com.derysuwandi.restapisimplebanking.utils.JWTUtils;
import com.derysuwandi.restapisimplebanking.utils.GeneralUtils;
import io.micrometer.common.util.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final String jwtSecretKey;
    private final String jwtExpired;

    public AccountServiceImpl(AccountRepository accountRepository, UserRepository userRepository, Environment environment) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.jwtSecretKey =  environment.getProperty("app.auth.jwt-secret-key");
        this.jwtExpired =  environment.getProperty("app.auth.jwt-expired");
    }

    @Override
    public LoginResponse login(LoginRequest req) {
        if (!GeneralUtils.isValidEmail(req.getEmail()))
            throw new InfoLevelException("Email is not valid!");
        if (StringUtils.isBlank(req.getPassword()))
            throw new InfoLevelException("Password is required!");

        Optional<Users> userOpt = userRepository.findByEmail(req.getEmail());
        if (userOpt.isEmpty() || !new BCryptPasswordEncoder().matches(req.getPassword(), userOpt.get().getPassword()))
            throw new InfoLevelException("Invalid email or password!");

        return LoginResponse.builder()
                .accountNumber(userOpt.get().getAccount().getAccountNumber())
                .accountName(userOpt.get().getAccount().getAccountName())
                .email(userOpt.get().getEmail())
                .token(JWTUtils.createdJwtToken(userOpt.get(), jwtSecretKey, Long.parseLong(jwtExpired)))
                .build();
    }

    @Override
    public AccountRegisterResponse accountRegister(AccountRegisterRequest req) {
        if (!GeneralUtils.isValidEmail(req.getEmail()))
            throw new InfoLevelException("Email is not valid!");
        if (StringUtils.isBlank(req.getAccountName()))
            throw new InfoLevelException("Account Name is required!");
        if (StringUtils.isBlank(req.getPassword()) || req.getPassword().length() < 6)
            throw new InfoLevelException("Password is required and minimal 6 character!");
        if (StringUtils.isBlank(req.getConfirmPassword()))
            throw new InfoLevelException("Confirm Password is required!");
        if(!req.getPassword().equals(req.getConfirmPassword()))
            throw new InfoLevelException("Password and Confirm Password are not the same!");

        final Optional<Users> userOpt = userRepository.findByEmail(req.getEmail());
        if(userOpt.isPresent())
            throw new InfoLevelException("Email already registered!");

        Account acc = new Account();
        acc.setAccountNumber(GeneralUtils.randomAccountNumber());
        acc.setAccountName(req.getAccountName());

        Users user = new Users();
        user.setEmail(req.getEmail());
        user.setPassword(new BCryptPasswordEncoder().encode(req.getPassword()));
        user.setAccount(acc);
        Users savedUser = userRepository.save(user);

        acc.setId(savedUser.getAccount().getId());
        acc.setUser(savedUser);
        accountRepository.save(acc);

        return AccountRegisterResponse.builder()
                .email(savedUser.getEmail())
                .accountName(savedUser.getAccount().getAccountName())
                .accountNumber(savedUser.getAccount().getAccountNumber())
                .balance(savedUser.getAccount().getBalance())
                .build();
    }

    @Override
    public AccountDetailResponse accountDetail() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null)
            throw new InfoLevelException("Unauthorized");

        AccountAuthResponse accountDetail = (AccountAuthResponse) authentication.getPrincipal();
        Optional<Account> accountOpt = accountRepository.findById(accountDetail.getAccountId());

        if (accountOpt.isEmpty())
            throw new InfoLevelException("Account not found");
        Account account = accountOpt.get();

        List<TransactionDetailResponse> transactionDetailResponseList = new ArrayList<>();
        for (Transaction transaction: account.getTransactions()){
            transactionDetailResponseList.add(TransactionDetailResponse.builder()
                    .transactionType(transaction.getTransactionType())
                    .amount(transaction.getAmount())
                    .createdDate(transaction.getCreatedDate())
                    .build());
        }

        AccountDetailResponse response = AccountDetailResponse.builder()
                .accountName(account.getAccountName())
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .transaction(transactionDetailResponseList)
                .build();

        return response;
    }
}
