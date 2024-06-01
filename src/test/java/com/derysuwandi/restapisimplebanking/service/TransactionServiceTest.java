package com.derysuwandi.restapisimplebanking.service;

import com.derysuwandi.restapisimplebanking.dto.request.TransactionRequest;
import com.derysuwandi.restapisimplebanking.dto.response.AccountAuthResponse;
import com.derysuwandi.restapisimplebanking.dto.response.TransactionResponse;
import com.derysuwandi.restapisimplebanking.exception.InfoLevelException;
import com.derysuwandi.restapisimplebanking.service.transaction.TransactionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class TransactionServiceTest {
    @Autowired
    private TransactionService transactionService;

    private String email = "test@gmail.com";
    private String accountName = "test";
    private Integer accountNumber = 123456;
    private Integer accountId = 1;

    @Nested
    public class DepositTest {
        @Test
        void testDepositFailedBalaceNullOrZero() {
            TransactionRequest transactionRequest = new TransactionRequest();
            assertThrows(InfoLevelException.class, () -> {
                transactionService.deposit(transactionRequest);
            });
        }

        @Test
        void testDepositFailedUnauthorized() {
            TransactionRequest transactionRequest = new TransactionRequest();
            transactionRequest.setBalance(10000L);
            SecurityContextHolder.getContext().setAuthentication(null);
            assertThrows(InfoLevelException.class, () -> {
                transactionService.deposit(transactionRequest);
            });
        }

        @Test
        void testDepositSuccess() {
            TransactionRequest transactionRequest = new TransactionRequest();
            transactionRequest.setBalance(10000L);

            AccountAuthResponse auth = AccountAuthResponse.builder().email(email).accountName(accountName).accountId(accountId).build();
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(auth, null, null));

            TransactionResponse response = transactionService.deposit(transactionRequest);
            System.out.println(response);

            Assertions.assertNotNull(response);
            Assertions.assertEquals(accountName, response.getAccountName());
        }
    }

    @Nested
    public class WithDrawTest {
        @Test
        void testWithdrawFailedBalaceNullOrZero() {
            TransactionRequest transactionRequest = new TransactionRequest();
            assertThrows(InfoLevelException.class, () -> {
                transactionService.withdraw(transactionRequest);
            });
        }

        @Test
        void testWithdrawFailedUnauthorized() {
            TransactionRequest transactionRequest = new TransactionRequest();
            transactionRequest.setBalance(10000L);
            SecurityContextHolder.getContext().setAuthentication(null);
            assertThrows(InfoLevelException.class, () -> {
                transactionService.withdraw(transactionRequest);
            });
        }

        @Test
        void testWithdrawInsufficientBalance() {
            TransactionRequest transactionRequest = new TransactionRequest();
            transactionRequest.setBalance(1000000L);

            AccountAuthResponse auth = AccountAuthResponse.builder().email(email).accountName(accountName).accountId(accountId).build();
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(auth, null, null));

            assertThrows(InfoLevelException.class, () -> {
                transactionService.withdraw(transactionRequest);
            });
        }

        @Test
        void testWithdrawSuccess() {
            TransactionRequest transactionRequest = new TransactionRequest();
            transactionRequest.setBalance(10000L);

            AccountAuthResponse auth = AccountAuthResponse.builder().email(email).accountName(accountName).accountId(accountId).build();
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(auth, null, null));

            TransactionResponse response = transactionService.withdraw(transactionRequest);
            System.out.println(response);

            Assertions.assertNotNull(response);
            Assertions.assertEquals(accountName, response.getAccountName());
        }
    }

}
