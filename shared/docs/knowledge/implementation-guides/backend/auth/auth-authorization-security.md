---
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 01:46
**api-readiness-notes:** Authentication System - Authorization & Security. Roles, permissions, API endpoints, security. ~250 строк.
---

# Authentication System - Part 3: Authorization & Security

**Статус:** approved  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-07 01:46  
**Приоритет:** КРИТИЧЕСКИЙ (MVP блокер!)  
**Автор:** AI Brain Manager

**Микрофича:** Authorization, permissions, security  
**Размер:** ~250 строк ✅

**Родительский документ:** authentication-authorization-system.md (разбит на 3 части)  
**Связанные микрофичи:**
- [Part 1: Database & Registration](./auth-database-registration.md)
- [Part 2: Login & JWT Management](./auth-login-jwt.md)

---

## Authorization (Roles & Permissions)

### Role Definitions

```java
public enum Role {
    PLAYER(List.of(
        "game.play",
        "chat.send",
        "trade.execute",
        "guild.join"
    )),
    
    MODERATOR(List.of(
        "game.play",
        "chat.send",
        "chat.moderate",
        "player.mute",
        "player.kick"
    )),
    
    ADMIN(List.of(
        "game.play",
        "chat.moderate",
        "player.ban",
        "player.unban",
        "event.create",
        "world.manage",
        "economy.adjust"
    )),
    
    SUPER_ADMIN(List.of(
        "*"
    ));
    
    private final List<String> defaultPermissions;
    
    Role(List<String> defaultPermissions) {
        this.defaultPermissions = defaultPermissions;
    }
    
    public List<String> getDefaultPermissions() {
        return defaultPermissions;
    }
}
```

### Check Permission

```java
@Service
public class PermissionService {
    
    public boolean hasPermission(UUID accountId, String permission) {
        List<AccountRole> roles = roleRepository.findByAccountId(accountId);
        
        for (AccountRole role : roles) {
            if (role.getGrantedUntil() != null && 
                Instant.now().isAfter(role.getGrantedUntil())) {
                continue;
            }
            
            if (role.getRole().equals("SUPER_ADMIN")) {
                return true;
            }
            
            if (role.getPermissions().contains(permission)) {
                return true;
            }
        }
        
        return false;
    }
}
```

### @RequiresPermission Annotation

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresPermission {
    String value();
}

@Aspect
@Component
public class PermissionAspect {
    
    @Autowired
    private PermissionService permissionService;
    
    @Around("@annotation(requiresPermission)")
    public Object checkPermission(
        ProceedingJoinPoint joinPoint,
        RequiresPermission requiresPermission
    ) throws Throwable {
        Account account = SecurityContextHolder.getAccount();
        
        if (!permissionService.hasPermission(
            account.getId(),
            requiresPermission.value()
        )) {
            throw new InsufficientPermissionsException(
                "Required permission: " + requiresPermission.value()
            );
        }
        
        return joinPoint.proceed();
    }
}
```

---

## API Endpoints

### Authentication

**POST `/api/v1/auth/register`** - регистрация  
**POST `/api/v1/auth/login`** - вход  
**POST `/api/v1/auth/logout`** - выход  
**POST `/api/v1/auth/refresh`** - обновить access token

### Password Management

**POST `/api/v1/auth/forgot-password`** - запросить reset  
**POST `/api/v1/auth/reset-password`** - сбросить пароль  
**POST `/api/v1/auth/change-password`** - изменить пароль

### Email Verification

**POST `/api/v1/auth/verify-email`** - подтвердить email  
**POST `/api/v1/auth/resend-verification`** - переотправить письмо

### Two-Factor Authentication

**POST `/api/v1/auth/2fa/enable`** - включить 2FA  
**POST `/api/v1/auth/2fa/verify`** - подтвердить 2FA  
**POST `/api/v1/auth/2fa/disable`** - выключить 2FA

### OAuth

**GET `/api/v1/auth/oauth/{provider}`** - начать OAuth flow  
**GET `/api/v1/auth/oauth/{provider}/callback`** - callback от провайдера

### Account Management

**GET `/api/v1/auth/account`** - получить информацию  
**PUT `/api/v1/auth/account`** - обновить профиль  
**DELETE `/api/v1/auth/account`** - удалить аккаунт

---

## Security Best Practices

### Password Security
- **Хэширование:** BCrypt (cost factor 12+)
- **Минимальная длина:** 8 символов
- **Требования:** uppercase + lowercase + digit + special char
- **Password history:** Не разрешать повторное использование последних 3 паролей

### Token Security
- **Access Token TTL:** 15 минут
- **Refresh Token TTL:** 7 дней
- **Token Storage:** Redis
- **Token Blacklist:** При logout добавлять в blacklist

### Rate Limiting
- **Login attempts:** 5 попыток за 15 минут
- **Password reset:** 3 запроса за час
- **Account creation:** 3 аккаунта с одного IP за сутки

### Account Security
- **Email verification:** Обязательная перед доступом
- **2FA:** Опциональная, рекомендуется
- **IP tracking:** Для аудита и security
- **Session management:** Интеграция с Session Manager

---

## Микросервисная архитектура

**Домен:** `auth`  
**Микросервис:** Auth Service  
**Зависимости:**
- Session Manager (создание сессий)
- Email Service (верификация, reset)
- Redis (токены, blacklist)
- PostgreSQL (accounts, roles, tokens)

---

## Связанные документы

- [Part 1: Database & Registration](./auth-database-registration.md)
- [Part 2: Login & JWT Management](./auth-login-jwt.md)
- [Session Management System](../session-management-system.md)
- [MVP Auth Endpoints](../../api-requirements/mvp-endpoints/auth-endpoints.md)

