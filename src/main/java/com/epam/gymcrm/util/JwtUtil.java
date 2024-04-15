package com.epam.gymcrm.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.epam.gymcrm.auth.Authentication;
import com.epam.gymcrm.exception.AuthenticationException;
import com.epam.gymcrm.model.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${jwt.key}")
    private String secret;

    @Value("${jwt.exp}")
    private long exp;

    public String generateToken(Authentication authentication) {
        return JWT.create()
                .withClaim("sub", authentication.username())
                .withClaim("role", authentication.role().toString())
                .withExpiresAt(Instant.now().plusSeconds(exp))
                .sign(Algorithm.HMAC256(secret));
    }

    public Authentication validateToken(String token) {
        if (token == null) {
            throw new AuthenticationException();
        }
        token = getPureToken(token);
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                    .acceptExpiresAt(exp)
                    .build();
            DecodedJWT decodedJWT = verifier.verify(token);
            Map<String, Claim> claims = decodedJWT.getClaims();

            return Authentication.builder()
                    .username(claims.get("sub").asString())
                    .role(claims.get("role").as(Role.class))
                    .build();
        } catch (JWTVerificationException exception) {
            throw new AuthenticationException();
        }
    }

    private String getPureToken(String token) {
        if (token.contains(" ")) {
            return token.split(" ")[1];
        }
        return token;
    }
}
