package com.derysuwandi.restapisimplebanking.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.derysuwandi.restapisimplebanking.dto.response.AccountAuthResponse;
import com.derysuwandi.restapisimplebanking.entity.Users;
import com.derysuwandi.restapisimplebanking.repo.UserRepository;
import com.derysuwandi.restapisimplebanking.utils.JWTUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;
import io.micrometer.common.util.StringUtils;

@Component
public class AuthFilter extends OncePerRequestFilter {
    private final String jwtSecretKey;
    private final String jwtExpired;
    private final UserRepository userRepository;
    private final Environment environment;

    public AuthFilter(Environment environment, UserRepository userRepository) {
        this.jwtSecretKey = environment.getProperty("app.auth.jwt-secret-key");
        this.jwtExpired = environment.getProperty("app.auth.jwt-expired");
        this.userRepository = userRepository;
        this.environment = environment;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (environment.acceptsProfiles(Profiles.of("test"))) {
            setUpMockSecurityContext();
            filterChain.doFilter(request, response);
        }else {
            String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (StringUtils.isEmpty(authorization)) {
                filterChain.doFilter(request, response);
                return;
            }
            String token = authorization.substring("Bearer".length() + 1);
            getAuthentication(request, token);
            filterChain.doFilter(request, response);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.AUTHORIZATION) == null || RequestMethod.OPTIONS.name().equals(request.getMethod());
    }

    private Authentication getAuthentication(HttpServletRequest request, String token) {
        try {
            DecodedJWT decodedJWT = JWTUtils.decodedJWT(token, jwtSecretKey, Long.parseLong(jwtExpired));
            if (decodedJWT.getExpiresAt().before(new Date())) {
                System.out.println("JWT Expired!");
                return null;
            } else {
                String id = decodedJWT.getSubject();

                Optional<Users> userOpt = userRepository.findByEmail(id);

                if (userOpt.isEmpty()) return null;

                AccountAuthResponse auth = AccountAuthResponse.builder()
                        .accountName(userOpt.get().getAccount().getAccountName())
                        .accountId(userOpt.get().getAccount().getId())
                        .email(userOpt.get().getEmail()).build();

                var authenticationToken = new UsernamePasswordAuthenticationToken(
                        auth, null, null);
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public static void setUpMockSecurityContext() {
        AccountAuthResponse auth = AccountAuthResponse.builder().accountId(1).build();
        var authenticationToken = new UsernamePasswordAuthenticationToken(
                auth, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
