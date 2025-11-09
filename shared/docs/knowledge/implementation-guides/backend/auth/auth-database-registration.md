---
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 01:46
**api-readiness-notes:** Authentication System - Database & Registration. БД схема и регистрация. ~380 строк.
---

# Authentication System - Part 1: Database & Registration

**Статус:** approved  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-07 01:46  
**Приоритет:** КРИТИЧЕСКИЙ (MVP блокер!)  
**Автор:** AI Brain Manager

**Микрофича:** Auth database schema & registration flow  
**Размер:** ~380 строк ✅

**Родительский документ:** authentication-authorization-system.md (разбит на 3 части)  
**Связанные микрофичи:**
- [Part 2: Login & JWT Management](./auth-login-jwt.md)
- [Part 3: Authorization & Security](./auth-authorization-security.md)

---

## Краткое описание

**Authentication & Authorization System** - критически важная система для управления доступом игроков к игре. Без этой системы игра не может запуститься.

**Ключевые возможности:**
- ✅ Регистрация аккаунтов (email/password, OAuth)
- ✅ Login/Logout flow
- ✅ JWT Token management
- ✅ Password recovery
- ✅ Two-Factor Authentication
- ✅ Roles & Permissions
- ✅ Account linking

---

## Архитектура системы

### High-Level Flow

```
┌─────────────┐
│   CLIENT    │
└──────┬──────┘
       │
       │ 1. Register/Login
       ▼
┌──────────────────┐
│  Auth Service    │
│  - Validate      │
│  - Hash password │
│  - Generate JWT  │
└──────┬───────────┘
       │
       ├─→ PostgreSQL (accounts)
       ├─→ Redis (refresh tokens, blacklist)
       └─→ Session Manager (create session)
       
       │ 2. Access protected resources
       ▼
┌──────────────────┐
│  API Gateway     │
│  - Verify JWT    │
│  - Check roles   │
└──────┬───────────┘
       │
       ▼
┌──────────────────┐
│  Game Services   │
└──────────────────┘
```

---

## Database Schema

### Таблица `accounts`

```sql
CREATE TABLE accounts (
    -- Идентификация
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    
    -- Credentials
    email VARCHAR(255) UNIQUE NOT NULL,
    email_verified BOOLEAN DEFAULT FALSE,
    password_hash VARCHAR(255) NOT NULL, -- bcrypt hash
    
    -- OAuth (опционально)
    oauth_provider VARCHAR(50), -- google, steam, discord, twitch
    oauth_id VARCHAR(255), -- ID от провайдера
    oauth_data JSONB, -- Дополнительные данные от OAuth
    
    -- Профиль
    username VARCHAR(50) UNIQUE NOT NULL,
    display_name VARCHAR(100),
    
    -- 2FA
    two_factor_enabled BOOLEAN DEFAULT FALSE,
    two_factor_secret VARCHAR(100), -- TOTP secret
    backup_codes TEXT[], -- Резервные коды
    
    -- Security
    last_password_change TIMESTAMP,
    failed_login_attempts INTEGER DEFAULT 0,
    locked_until TIMESTAMP, -- Временная блокировка после попыток
    
    -- Статус
    status VARCHAR(20) DEFAULT 'ACTIVE', 
    -- ACTIVE, SUSPENDED, BANNED, DELETED
    
    ban_reason TEXT,
    banned_until TIMESTAMP, -- NULL = permanent
    banned_by UUID, -- Admin who banned
    banned_at TIMESTAMP,
    
    -- Timestamps
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_login_at TIMESTAMP,
    
    -- IP tracking (для security)
    registration_ip VARCHAR(45),
    last_login_ip VARCHAR(45),
    
    CONSTRAINT fk_account_banned_by FOREIGN KEY (banned_by) 
        REFERENCES accounts(id) ON DELETE SET NULL
);

CREATE INDEX idx_accounts_email ON accounts(email);
CREATE INDEX idx_accounts_username ON accounts(username);
CREATE INDEX idx_accounts_oauth ON accounts(oauth_provider, oauth_id);
CREATE INDEX idx_accounts_status ON accounts(status);
```

### Таблица `account_roles`

```sql
CREATE TABLE account_roles (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    account_id UUID NOT NULL,
    
    -- Роль
    role VARCHAR(50) NOT NULL,
    -- PLAYER, MODERATOR, ADMIN, SUPER_ADMIN, CONTENT_CREATOR, TESTER
    
    -- Permissions (JSON array)
    permissions JSONB DEFAULT '[]',
    -- ["chat.moderate", "player.ban", "event.create", etc]
    
    -- Временные роли
    granted_until TIMESTAMP, -- NULL = permanent
    
    -- Аудит
    granted_by UUID,
    granted_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_role_account FOREIGN KEY (account_id) 
        REFERENCES accounts(id) ON DELETE CASCADE,
    CONSTRAINT fk_role_granted_by FOREIGN KEY (granted_by) 
        REFERENCES accounts(id) ON DELETE SET NULL,
    UNIQUE(account_id, role)
);

CREATE INDEX idx_roles_account ON account_roles(account_id);
CREATE INDEX idx_roles_role ON account_roles(role);
```

### Таблица `password_reset_tokens`

```sql
CREATE TABLE password_reset_tokens (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    account_id UUID NOT NULL,
    
    token VARCHAR(255) UNIQUE NOT NULL, -- Random secure token
    expires_at TIMESTAMP NOT NULL,
    used BOOLEAN DEFAULT FALSE,
    
    -- IP tracking
    requested_ip VARCHAR(45),
    used_ip VARCHAR(45),
    
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    used_at TIMESTAMP,
    
    CONSTRAINT fk_reset_account FOREIGN KEY (account_id) 
        REFERENCES accounts(id) ON DELETE CASCADE
);

CREATE INDEX idx_reset_token ON password_reset_tokens(token) 
    WHERE used = FALSE;
CREATE INDEX idx_reset_account ON password_reset_tokens(account_id);
CREATE INDEX idx_reset_expires ON password_reset_tokens(expires_at) 
    WHERE used = FALSE;
```

### Таблица `email_verification_tokens`

```sql
CREATE TABLE email_verification_tokens (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    account_id UUID NOT NULL,
    
    token VARCHAR(255) UNIQUE NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    verified BOOLEAN DEFAULT FALSE,
    
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    verified_at TIMESTAMP,
    
    CONSTRAINT fk_verify_account FOREIGN KEY (account_id) 
        REFERENCES accounts(id) ON DELETE CASCADE
);

CREATE INDEX idx_verify_token ON email_verification_tokens(token) 
    WHERE verified = FALSE;
```

### Таблица `login_history`

```sql
CREATE TABLE login_history (
    id BIGSERIAL PRIMARY KEY,
    account_id UUID NOT NULL,
    
    -- Event
    event_type VARCHAR(20) NOT NULL, 
    -- LOGIN_SUCCESS, LOGIN_FAILED, LOGOUT, PASSWORD_RESET
    
    -- Details
    ip_address VARCHAR(45),
    user_agent TEXT,
    location JSONB, -- {country, city, ...}
    
    -- Timestamp
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_login_history_account FOREIGN KEY (account_id) 
        REFERENCES accounts(id) ON DELETE CASCADE
);

CREATE INDEX idx_login_history_account ON login_history(account_id, created_at DESC);
CREATE INDEX idx_login_history_created ON login_history(created_at DESC);
```

---

## Registration Flow

### Email/Password Registration

```java
@Service
public class AuthService {
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private EmailService emailService;
    
    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        validateRegistration(request);
        
        if (accountRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException();
        }
        
        if (accountRepository.existsByUsername(request.getUsername())) {
            throw new UsernameAlreadyTakenException();
        }
        
        String passwordHash = passwordEncoder.encode(request.getPassword());
        
        Account account = new Account();
        account.setEmail(request.getEmail());
        account.setPasswordHash(passwordHash);
        account.setUsername(request.getUsername());
        account.setDisplayName(request.getDisplayName());
        account.setRegistrationIp(getClientIp());
        account.setStatus(AccountStatus.ACTIVE);
        account.setEmailVerified(false);
        
        account = accountRepository.save(account);
        
        AccountRole playerRole = new AccountRole();
        playerRole.setAccountId(account.getId());
        playerRole.setRole(Role.PLAYER);
        playerRole.setPermissions(Role.PLAYER.getDefaultPermissions());
        roleRepository.save(playerRole);
        
        String verificationToken = generateVerificationToken();
        EmailVerificationToken token = new EmailVerificationToken();
        token.setAccountId(account.getId());
        token.setToken(verificationToken);
        token.setExpiresAt(Instant.now().plus(Duration.ofHours(24)));
        verificationTokenRepository.save(token);
        
        emailService.sendVerificationEmail(
            account.getEmail(),
            account.getUsername(),
            verificationToken
        );
        
        log.info("New account registered: {}", account.getId());
        
        return new RegisterResponse(
            account.getId(),
            "Account created! Please check your email to verify."
        );
    }
    
    private void validateRegistration(RegisterRequest request) {
        if (!isValidEmail(request.getEmail())) {
            throw new InvalidEmailException();
        }
        
        if (request.getPassword().length() < 8) {
            throw new WeakPasswordException("Password must be at least 8 characters");
        }
        
        if (!hasUpperCase(request.getPassword()) || 
            !hasLowerCase(request.getPassword()) || 
            !hasDigit(request.getPassword())) {
            throw new WeakPasswordException(
                "Password must contain uppercase, lowercase, and digit"
            );
        }
        
        if (request.getUsername().length() < 3 || request.getUsername().length() > 20) {
            throw new InvalidUsernameException("Username must be 3-20 characters");
        }
        
        if (!isAlphanumeric(request.getUsername())) {
            throw new InvalidUsernameException("Username must be alphanumeric");
        }
    }
}
```

### OAuth Registration/Login

```java
@Service
public class OAuthService {
    
    public LoginResponse loginWithOAuth(OAuthProvider provider, String oauthCode) {
        OAuthTokenResponse tokenResponse = provider.exchangeCodeForToken(oauthCode);
        OAuthUserInfo userInfo = provider.getUserInfo(tokenResponse.getAccessToken());
        
        Account account = accountRepository
            .findByOAuthProviderAndId(provider.name(), userInfo.getId())
            .orElseGet(() -> createOAuthAccount(provider, userInfo));
        
        if (account.getStatus() == AccountStatus.BANNED) {
            throw new AccountBannedException(account.getBanReason());
        }
        
        String accessToken = jwtService.generateAccessToken(account);
        String refreshToken = jwtService.generateRefreshToken(account);
        
        SessionResponse session = sessionManager.createSession(
            account.getId(),
            null,
            getClientIp(),
            getUserAgent()
        );
        
        logLoginEvent(account.getId(), LoginEventType.LOGIN_SUCCESS);
        
        return new LoginResponse(
            accessToken,
            refreshToken,
            session.getSessionToken(),
            account.toDTO()
        );
    }
    
    private Account createOAuthAccount(OAuthProvider provider, OAuthUserInfo userInfo) {
        Account account = new Account();
        account.setOauthProvider(provider.name());
        account.setOauthId(userInfo.getId());
        account.setEmail(userInfo.getEmail());
        account.setEmailVerified(true);
        account.setUsername(generateUniqueUsername(userInfo.getUsername()));
        account.setDisplayName(userInfo.getDisplayName());
        account.setPasswordHash(generateRandomPassword());
        account.setRegistrationIp(getClientIp());
        
        account = accountRepository.save(account);
        assignRole(account.getId(), Role.PLAYER);
        
        return account;
    }
}
```

---

## Связанные документы

- [Part 2: Login & JWT Management](./auth-login-jwt.md)
- [Part 3: Authorization & Security](./auth-authorization-security.md)
- [Session Management System](../session-management-system.md)

