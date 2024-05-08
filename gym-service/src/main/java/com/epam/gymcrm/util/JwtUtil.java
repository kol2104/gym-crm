package com.epam.gymcrm.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.epam.gymcrm.exception.AuthenticationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Collection;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${jwt.key}")
    private String secret;

    @Value("${jwt.exp}")
    private long exp;

    public String generateToken(String username, Collection<? extends GrantedAuthority> authorities) {
        return JWT.create()
                .withClaim("sub", username)
                .withClaim("roles", authorities.toString())
                .withExpiresAt(Instant.now().plusSeconds(exp))
                .sign(Algorithm.HMAC256(secret));
    }

    public boolean isTokenInvalid(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return true;
        }
        token = getPureToken(token);
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                    .acceptExpiresAt(exp)
                    .build();
            verifier.verify(token);
            return false;
        } catch (JWTVerificationException exception) {
            return true;
        }
    }

    public String getUserUsername(String token) {
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

            return claims.get("sub").asString();
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
