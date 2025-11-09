---
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 01:46
**api-readiness-notes:** Authentication System - Login & JWT. Login flow, JWT tokens, password recovery, 2FA. ~380 строк.
---

# Authentication System - Part 2: Login & JWT Management

**Статус:** approved  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-07 01:46  
**Приоритет:** КРИТИЧЕСКИЙ (MVP блокер!)  
**Автор:** AI Brain Manager

**Микрофича:** Login flow, JWT tokens, password recovery, 2FA  
**Размер:** ~380 строк ✅

**Родительский документ:** authentication-authorization-system.md (разбит на 3 части)  
**Связанные микрофичи:**
- [Part 1: Database & Registration](./auth-database-registration.md)
- [Part 3: Authorization & Security](./auth-authorization-security.md)

---

## Login Flow

### Email/Password Login

```java
public LoginResponse login(LoginRequest request) {
    Account account = accountRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new InvalidCredentialsException());
    
    if (account.getLockedUntil() != null && 
        Instant.now().isBefore(account.getLockedUntil())) {
        long minutesLeft = Duration.between(
            Instant.now(), account.getLockedUntil()
        ).toMinutes();
        throw new AccountLockedException(
            "Account locked. Try again in " + minutesLeft + " minutes"
        );
    }
    
    if (!passwordEncoder.matches(request.getPassword(), account.getPasswordHash())) {
        account.setFailedLoginAttempts(account.getFailedLoginAttempts() + 1);
        
        if (account.getFailedLoginAttempts() >= 5) {
            account.setLockedUntil(Instant.now().plus(Duration.ofMinutes(15)));
            accountRepository.save(account);
            logLoginEvent(account.getId(), LoginEventType.LOGIN_FAILED);
            
            throw new AccountLockedException(
                "Too many failed attempts. Account locked for 15 minutes"
            );
        }
        
        accountRepository.save(account);
        logLoginEvent(account.getId(), LoginEventType.LOGIN_FAILED);
        throw new InvalidCredentialsException();
    }
    
    if (account.getStatus() == AccountStatus.BANNED) {
        if (account.getBannedUntil() != null && 
            Instant.now().isAfter(account.getBannedUntil())) {
            account.setStatus(AccountStatus.ACTIVE);
            account.setBannedUntil(null);
            accountRepository.save(account);
        } else {
            throw new AccountBannedException(
                account.getBanReason(),
                account.getBannedUntil()
            );
        }
    }
    
    if (account.isTwoFactorEnabled()) {
        if (request.getTwoFactorCode() == null) {
            return new LoginResponse(
                null, null, null, null,
                true,
                "Two-factor authentication required"
            );
        }
        
        if (!verifyTwoFactorCode(account, request.getTwoFactorCode())) {
            throw new InvalidTwoFactorCodeException();
        }
    }
    
    account.setFailedLoginAttempts(0);
    account.setLockedUntil(null);
    account.setLastLoginAt(Instant.now());
    account.setLastLoginIp(getClientIp());
    accountRepository.save(account);
    
    String accessToken = jwtService.generateAccessToken(account);
    String refreshToken = jwtService.generateRefreshToken(account);
    
    redis.opsForValue().set(
        "refresh_token:" + account.getId(),
        refreshToken,
        7, TimeUnit.DAYS
    );
    
    SessionResponse session = sessionManager.createSession(
        account.getId(),
        null,
        getClientIp(),
        getUserAgent(),
        getClientVersion()
    );
    
    logLoginEvent(account.getId(), LoginEventType.LOGIN_SUCCESS);
    
    return new LoginResponse(
        accessToken,
        refreshToken,
        session.getSessionToken(),
        account.toDTO()
    );
}
```

---

## JWT Token Management

### Token Structure

**Access Token (15 минут TTL):**
```json
{
  "sub": "account-uuid",
  "type": "access",
  "roles": ["PLAYER"],
  "permissions": ["game.play", "chat.send"],
  "iat": 1699296000,
  "exp": 1699296900
}
```

**Refresh Token (7 дней TTL):**
```json
{
  "sub": "account-uuid",
  "type": "refresh",
  "iat": 1699296000,
  "exp": 1699900800
}
```

### Token Generation

```java
@Service
public class JwtService {
    
    @Value("${jwt.secret}")
    private String jwtSecret;
    
    @Value("${jwt.access.expiration}")
    private long accessTokenExpiration = 900;
    
    @Value("${jwt.refresh.expiration}")
    private long refreshTokenExpiration = 604800;
    
    public String generateAccessToken(Account account) {
        List<String> roles = getRoles(account.getId());
        List<String> permissions = getPermissions(account.getId());
        
        return Jwts.builder()
            .setSubject(account.getId().toString())
            .claim("type", "access")
            .claim("roles", roles)
            .claim("permissions", permissions)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration * 1000))
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact();
    }
    
    public String generateRefreshToken(Account account) {
        return Jwts.builder()
            .setSubject(account.getId().toString())
            .claim("type", "refresh")
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration * 1000))
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact();
    }
    
    public Claims validateToken(String token) {
        try {
            return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        } catch (ExpiredJwtException e) {
            throw new TokenExpiredException();
        } catch (JwtException e) {
            throw new InvalidTokenException();
        }
    }
}
```

### Token Refresh

```java
@PostMapping("/auth/refresh")
public LoginResponse refreshToken(@RequestBody RefreshTokenRequest request) {
    Claims claims = jwtService.validateToken(request.getRefreshToken());
    
    if (!claims.get("type").equals("refresh")) {
        throw new InvalidTokenException("Not a refresh token");
    }
    
    UUID accountId = UUID.fromString(claims.getSubject());
    
    String storedToken = (String) redis.opsForValue().get(
        "refresh_token:" + accountId
    );
    
    if (storedToken == null || !storedToken.equals(request.getRefreshToken())) {
        throw new InvalidTokenException("Refresh token revoked or not found");
    }
    
    Account account = accountRepository.findById(accountId)
        .orElseThrow(() -> new AccountNotFoundException());
    
    if (account.getStatus() != AccountStatus.ACTIVE) {
        throw new AccountNotActiveException();
    }
    
    String newAccessToken = jwtService.generateAccessToken(account);
    
    return new LoginResponse(
        newAccessToken,
        request.getRefreshToken(),
        null,
        null
    );
}
```

---

## Password Recovery

### Request Password Reset

```java
@PostMapping("/auth/forgot-password")
public MessageResponse requestPasswordReset(@RequestBody EmailRequest request) {
    Account account = accountRepository.findByEmail(request.getEmail())
        .orElse(null);
    
    if (account == null) {
        return new MessageResponse(
            "If this email exists, you will receive a password reset link"
        );
    }
    
    String resetToken = generateSecureRandomToken();
    
    PasswordResetToken token = new PasswordResetToken();
    token.setAccountId(account.getId());
    token.setToken(resetToken);
    token.setExpiresAt(Instant.now().plus(Duration.ofHours(1)));
    token.setRequestedIp(getClientIp());
    
    resetTokenRepository.save(token);
    
    emailService.sendPasswordResetEmail(
        account.getEmail(),
        account.getUsername(),
        resetToken
    );
    
    return new MessageResponse(
        "If this email exists, you will receive a password reset link"
    );
}
```

### Reset Password

```java
@PostMapping("/auth/reset-password")
public MessageResponse resetPassword(@RequestBody ResetPasswordRequest request) {
    PasswordResetToken token = resetTokenRepository.findByToken(request.getToken())
        .orElseThrow(() -> new InvalidTokenException("Invalid or expired token"));
    
    if (Instant.now().isAfter(token.getExpiresAt())) {
        throw new TokenExpiredException();
    }
    
    if (token.isUsed()) {
        throw new TokenAlreadyUsedException();
    }
    
    Account account = accountRepository.findById(token.getAccountId())
        .orElseThrow(() -> new AccountNotFoundException());
    
    String newPasswordHash = passwordEncoder.encode(request.getNewPassword());
    account.setPasswordHash(newPasswordHash);
    account.setLastPasswordChange(Instant.now());
    accountRepository.save(account);
    
    token.setUsed(true);
    token.setUsedAt(Instant.now());
    token.setUsedIp(getClientIp());
    resetTokenRepository.save(token);
    
    redis.delete("refresh_token:" + account.getId());
    
    return new MessageResponse("Password reset successful");
}
```

---

## Two-Factor Authentication (2FA)

### Enable 2FA

```java
@PostMapping("/auth/2fa/enable")
public TwoFactorResponse enable2FA(@AuthenticatedUser Account account) {
    String secret = generateTOTPSecret();
    List<String> backupCodes = generateBackupCodes(10);
    
    redis.opsForValue().set(
        "2fa_setup:" + account.getId(),
        new TwoFactorSetup(secret, backupCodes),
        10, TimeUnit.MINUTES
    );
    
    String issuer = "NECPGAME";
    String qrCodeData = "otpauth://totp/" + issuer + ":" + account.getEmail() + 
                        "?secret=" + secret + 
                        "&issuer=" + issuer;
    
    return new TwoFactorResponse(
        secret,
        qrCodeData,
        backupCodes,
        "Scan QR code and enter verification code"
    );
}
```

### Verify and Activate 2FA

```java
@PostMapping("/auth/2fa/verify")
public MessageResponse verify2FA(
    @AuthenticatedUser Account account,
    @RequestBody VerifyTwoFactorRequest request
) {
    TwoFactorSetup setup = (TwoFactorSetup) redis.opsForValue().get(
        "2fa_setup:" + account.getId()
    );
    
    if (setup == null) {
        throw new TwoFactorSetupNotFoundException();
    }
    
    if (!verifyTOTPCode(setup.getSecret(), request.getCode())) {
        throw new InvalidTwoFactorCodeException();
    }
    
    account.setTwoFactorEnabled(true);
    account.setTwoFactorSecret(setup.getSecret());
    account.setBackupCodes(setup.getBackupCodes().toArray(new String[0]));
    accountRepository.save(account);
    
    redis.delete("2fa_setup:" + account.getId());
    
    return new MessageResponse("Two-factor authentication enabled successfully");
}
```

---

## Связанные документы

- [Part 1: Database & Registration](./auth-database-registration.md)
- [Part 3: Authorization & Security](./auth-authorization-security.md)

