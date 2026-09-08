package com.pedro.delivery_api.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.pedro.delivery_api.entity.User;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class TokenConfig {

    private String secret = "secret";

    public String generateToken(User user) {

    Algorithm algorithm = Algorithm.HMAC256(secret);
    return JWT.create()
            .withClaim("userId", user.getId())
            .withSubject(user.getEmail())
            .withExpiresAt(Instant.now().plusSeconds(86400))
            .withIssuedAt(Instant.now())
            .sign(algorithm);

    }
}
