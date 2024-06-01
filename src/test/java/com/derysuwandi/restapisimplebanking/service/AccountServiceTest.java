package com.derysuwandi.restapisimplebanking.service;

import com.derysuwandi.restapisimplebanking.dto.request.AccountRegisterRequest;
import com.derysuwandi.restapisimplebanking.dto.request.LoginRequest;
import com.derysuwandi.restapisimplebanking.dto.response.AccountAuthResponse;
import com.derysuwandi.restapisimplebanking.dto.response.AccountDetailResponse;
import com.derysuwandi.restapisimplebanking.dto.response.AccountRegisterResponse;
import com.derysuwandi.restapisimplebanking.dto.response.LoginResponse;
import com.derysuwandi.restapisimplebanking.exception.InfoLevelException;
import com.derysuwandi.restapisimplebanking.service.account.AccountService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountServiceTest {
    @Autowired
    private AccountService accountService;

    @Nested
    public class RegisterTest {
        @Test
        void testRegisterFailedRequired() {
            AccountRegisterRequest request = AccountRegisterRequest.builder().build();
            //VALIDATE FORMAT EMAIL
            request.setEmail("tes");
            assertThrows(InfoLevelException.class, () -> {
                accountService.accountRegister(request);
            });

            //VALIDATE ACCOUNT NAME CANNOT BE NULL
            request.setEmail("test@gmail.com");
            assertThrows(InfoLevelException.class, () -> {
                accountService.accountRegister(request);
            });

            //VALIDATE PASSWORD CANNOT BE NULL
            request.setAccountName("test");
            assertThrows(InfoLevelException.class, () -> {
                accountService.accountRegister(request);
            });

            //VALIDATE PASSWORD MINIMAL 6 CHARACTER
            request.setPassword("test");
            assertThrows(InfoLevelException.class, () -> {
                accountService.accountRegister(request);
            });

            //VALIDATE CONFIRM PASSWORD CANNOT BE NULL
            request.setPassword("test123");
            assertThrows(InfoLevelException.class, () -> {
                accountService.accountRegister(request);
            });

            //VALIDATE Password and Confirm Password are not the same
            request.setConfirmPassword("test");
            assertThrows(InfoLevelException.class, () -> {
                accountService.accountRegister(request);
            });
        }

        @Test
        void testRegisterSuccess() {
            String accountName = "Koi";
            String email = "test@gmail.com";
            String password = "123123123";
            String confirmPassword = "123123123";
            long balance = 500000;

            AccountRegisterRequest request = AccountRegisterRequest.builder()
                    .accountName(accountName)
                    .email(email)
                    .password(password)
                    .confirmPassword(confirmPassword)
                    .build();

            AccountRegisterResponse response = accountService.accountRegister(request);

            System.out.println(response);
            Assertions.assertNotNull(response);
            Assertions.assertEquals(accountName, response.getAccountName());
            Assertions.assertEquals(email, response.getEmail());
            Assertions.assertEquals(balance, response.getBalance());
        }

        @Test
        void testRegisterFailedDuplicateEmail() {
            AccountRegisterRequest request = AccountRegisterRequest.builder()
                    .email(UUID.randomUUID() + "@gmail.com")
                    .accountName("test")
                    .password("test123")
                    .confirmPassword("test123")
                    .build();

            accountService.accountRegister(request);

            assertThrows(InfoLevelException.class, () -> {
                accountService.accountRegister(request);
            });
        }
    }

    @Nested
    public class LoginAndDetailTest {
        String email = "test@gmail.com";
        String password = "123123123";
        String accountName = "Koi";
        Integer accountId = 1;

        @Test
        void testLoginFailedRequired() {
            LoginRequest request = new LoginRequest();
            //VALIDATE EMAIL CANNOT BE NULL
            assertThrows(InfoLevelException.class, () -> {
                accountService.login(request);
            });

            //VALIDATE EMAIL NOT VALID
            request.setEmail("tes");
            assertThrows(InfoLevelException.class, () -> {
                accountService.login(request);
            });

            //VALIDATE PASSWORD CANNOT BE NULL
            request.setEmail(email);
            assertThrows(InfoLevelException.class, () -> {
                accountService.login(request);
            });
        }

        @Test
        void testLoginFailedNotFound() {
            String emailF = "test2482974@gmail.com";
            String passwordF = "123123123";

            LoginRequest request = new LoginRequest();
            request.setEmail(emailF);
            request.setPassword(passwordF);
            assertThrows(InfoLevelException.class, () -> {
                accountService.login(request);
            });
        }

        @Test
        void testLoginSuccess() {
            LoginRequest request = new LoginRequest();
            request.setEmail(email);
            request.setPassword(password);

            LoginResponse response = accountService.login(request);

            System.out.println(response);
            Assertions.assertNotNull(response);
            Assertions.assertEquals(email, response.getEmail());
            Assertions.assertEquals(accountName, response.getAccountName());
        }

        @Test
        void getDetailFailedUnauthorized() {
            SecurityContextHolder.getContext().setAuthentication(null);
            assertThrows(InfoLevelException.class, () -> {
                accountService.accountDetail();
            });
        }

        @Test
        void getDetailSuccess() {
            AccountAuthResponse auth = AccountAuthResponse.builder()
                    .accountName(accountName)
                    .accountId(accountId)
                    .email(email).build();
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(auth, null, null));
            AccountDetailResponse response = accountService.accountDetail();
            System.out.println(response);
            assertNotNull(response);
        }
    }

}
