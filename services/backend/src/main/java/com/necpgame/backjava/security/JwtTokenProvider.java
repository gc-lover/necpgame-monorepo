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
 * JWT Token Provider РґР»СЏ СЃРѕР·РґР°РЅРёСЏ Рё РІР°Р»РёРґР°С†РёРё JWT С‚РѕРєРµРЅРѕРІ
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
     * РЎРѕР·РґР°С‚СЊ JWT С‚РѕРєРµРЅ РґР»СЏ Р°РєРєР°СѓРЅС‚Р°
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
     * РџРѕР»СѓС‡РёС‚СЊ РґР°С‚Сѓ РёСЃС‚РµС‡РµРЅРёСЏ С‚РѕРєРµРЅР°
     */
    public OffsetDateTime getTokenExpiration() {
        return OffsetDateTime.now().plusSeconds(jwtExpirationMs / 1000);
    }
    
    /**
     * РџРѕР»СѓС‡РёС‚СЊ Account ID РёР· С‚РѕРєРµРЅР°
     */
    public UUID getAccountIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        
        return UUID.fromString(claims.getSubject());
    }
    
    /**
     * Р’Р°Р»РёРґР°С†РёСЏ С‚РѕРєРµРЅР°
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
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

