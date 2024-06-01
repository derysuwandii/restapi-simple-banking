package com.derysuwandi.restapisimplebanking.service;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.derysuwandi.restapisimplebanking.entity.Account;
import com.derysuwandi.restapisimplebanking.entity.Users;
import com.derysuwandi.restapisimplebanking.utils.JWTUtils;
import com.derysuwandi.restapisimplebanking.utils.GeneralUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UtilsTest {
    GeneralUtils generalUtils = new GeneralUtils();
    JWTUtils jwtUtils = new JWTUtils();

    @Value("${app.auth.jwt-secret-key}")
    private String jwtSecretKey;

    @Value("${app.auth.jwt-expired}")
    private long jwtExpired;

    @Test
    public void testRandomAccountNumber(){
        var result = generalUtils.randomAccountNumber();
        System.out.println(result);
        assertNotNull(result);
    }

    @Test
    public void testValidateEmailSuccess(){
        var result = generalUtils.isValidEmail("tes@gmail.com");

        assertEquals(true, result);
    }
    @Test
    public void testValidateEmailFailed(){
        var result = generalUtils.isValidEmail("tes");

        assertEquals(false, result);
    }

    @Test
    public void testCreatedJwtTokenSuccess(){
        Account account = new Account();
        account.setAccountNumber(123456);
        account.setBalance(500000);
        account.setAccountName("test");

        Users user = new Users();
        user.setEmail("test@gmail.com");
        user.setAccount(account);

        var result = jwtUtils.createdJwtToken(user, jwtSecretKey, jwtExpired);
        System.out.println(result);
        assertNotNull(result);
    }

    @Test
    public void testCreatedJwtTokenFailed(){
        Account account = new Account();
        account.setAccountNumber(123456);
        account.setBalance(500000);
        account.setAccountName("test");

        Users user = new Users();
        user.setEmail("test@gmail.com");
        user.setAccount(account);

        assertThrows(IllegalArgumentException.class, () -> {
            jwtUtils.createdJwtToken(user, null, jwtExpired);
        });
    }


    @Test
    public void testDecodedJWTSuccess(){
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGdtYWlsLmNvbSIsImV4cCI6MTcxNzE3MzgyNCwiaWF0IjoxNzE3MTcwMjI0fQ.qX4P1BSgL_fukb3EgWaGXGVMkGlWqdiohdoaXR41FIU";
        var result = jwtUtils.decodedJWT(token, jwtSecretKey, jwtExpired);
        assertNotNull(result);
    }

    @Test
    public void testDecodedJWTFailed(){
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGdtYWlsLmNvbSIsImV4cCI6MTcxNzE3MzgyNCwiaWF0IjoxNzE3MTcwMjI0fQ.qX4P1BSgL_fukb3EgWaGXGVMkGlWqdiohdoaXR41FIU";
        assertThrows(SignatureVerificationException.class, () -> {
            jwtUtils.decodedJWT(token, "tes", jwtExpired);
        });
    }
}
