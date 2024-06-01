package com.derysuwandi.restapisimplebanking.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.derysuwandi.restapisimplebanking.entity.Users;

import java.util.Date;

public class JWTUtils {

    public static String createdJwtToken(Users user, String jwtSecretKey, long jwtExpired){
        return JWT.create().withSubject(user.getEmail())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtExpired))
                .sign(Algorithm.HMAC256(jwtSecretKey));
    }

    public static DecodedJWT decodedJWT(String token, String jwtSecretKey, long jwtExpired){
        return JWT.require(Algorithm.HMAC256(jwtSecretKey.getBytes()))
                .acceptExpiresAt(jwtExpired)
                .build()
                .verify(token);
    }
}
