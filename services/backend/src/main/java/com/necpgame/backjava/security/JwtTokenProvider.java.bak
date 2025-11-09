package com.necpgame.backjava.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.UUID;

/**
 * JWT Token Provider для создания и валидации JWT токенов
 */
@Slf4j
@Component
public class JwtTokenProvider {
    
    @Value("${jwt.secret:necpgame-secret-key-for-jwt-token-generation-change-in-production}")
    private String jwtSecret;
    
    @Value("${jwt.expiration:86400000}") // 24 hours in milliseconds
    private long jwtExpirationMs;
    
    private SecretKey key;
    
    @PostConstruct
    public void init() {
        // Create a secure key from the secret
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }
    
    /**
     * Создать JWT токен для аккаунта
     */
    public String createToken(UUID accountId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);
        
        return Jwts.builder()
                .setSubject(accountId.toString())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }
    
    /**
     * Получить дату истечения токена
     */
    public OffsetDateTime getTokenExpiration() {
        return OffsetDateTime.now().plusSeconds(jwtExpirationMs / 1000);
    }
    
    /**
     * Получить Account ID из токена
     */
    public UUID getAccountIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        
        return UUID.fromString(claims.getSubject());
    }
    
    /**
     * Валидация токена
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty");
        }
        return false;
    }
}

