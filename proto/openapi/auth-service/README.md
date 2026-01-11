# Auth Service - Enterprise-Grade Authentication & Authorization

## 📋 **Назначение**

Auth Service предоставляет комплексную систему аутентификации и авторизации для платформы NECPGAME. Сервис построен на принципах SOLID/DRY с domain inheritance от infrastructure entities.

## 🎯 **Функциональность**

### **Аутентификация**
- **Email/Password**: Стандартная аутентификация с валидацией сложности пароля
- **OAuth Integration**: Google, Discord, Steam провайдеры
- **Multi-Factor Authentication**: Поддержка TOTP для повышенной безопасности
- **Email Verification**: Обязательная верификация email при регистрации

### **Управление Сессиями**
- **JWT Tokens**: Access и refresh токены с автоматической ротацией
- **Session Monitoring**: Отслеживание активных сессий пользователя
- **Device Management**: Возможность отзыва сессий с конкретных устройств
- **Security Audit**: Полный аудит всех аутентификационных операций

### **Безопасность**
- **Rate Limiting**: Защита от brute force атак
- **Account Lockout**: Автоматическая блокировка при множественных неудачных попытках
- **Password Policies**: Строгие требования к сложности паролей
- **Audit Trail**: Полное логирование всех операций безопасности

## 📁 **Структура**

```
auth-service/
├── main.yaml              # Enterprise-grade спецификация с domain inheritance
└── README.md              # Эта документация
```

## 🔗 **Domain Inheritance - Infrastructure Entities**

### **Наследуемые Сущности**

#### **UserAccountEntity** → UserAccount
```yaml
# Автоматически наследует 15+ полей:
- id, created_at, updated_at, version
- username, email, password_hash
- account_status, registration_method
- last_login_at, login_count
- two_factor_enabled, recovery_email

# Добавляет auth-specific поля:
- auth_provider_data (OAuth IDs)
- last_password_change
- password_reset_tokens
```

#### **SessionEntity** → Session
```yaml
# Автоматически наследует 10+ полей:
- id, created_at, updated_at, version
- user_id, session_token, ip_address
- user_agent, expires_at, is_active
- security_level, session_metadata

# Добавляет auth-specific поля:
- oauth_provider
- mfa_verified
```

#### **AuditLogEntity** → AuthAuditLog
```yaml
# Автоматически наследует 15+ полей:
- id, created_at, updated_at, version
- event_type, actor_id, resource_type
- action, severity, ip_address
- success, error_message, metadata

# Добавляет auth-specific поля:
- user_id
- operation_type (login, logout, register, etc.)
```

## 🚀 **API Endpoints**

### **Аутентификация**
- `POST /auth/login` - Вход в систему
- `POST /auth/register` - Регистрация пользователя
- `POST /auth/verify-email` - Верификация email
- `POST /auth/logout` - Выход из системы
- `POST /auth/refresh` - Обновление токенов

### **Восстановление Пароля**
- `POST /auth/forgot-password` - Запрос сброса пароля
- `POST /auth/reset-password` - Сброс пароля

### **Управление Сессиями**
- `GET /sessions` - Список активных сессий
- `POST /sessions/{id}/revoke` - Отзыв сессии

### **OAuth Интеграция**
- `GET /oauth/{provider}/authorize` - Инициация OAuth
- `GET/POST /oauth/{provider}/callback` - OAuth callback

## 📊 **Performance**

- **P99 Latency**: <50ms для всех endpoints
- **Memory per Instance**: <50KB baseline
- **Concurrent Users**: 10,000+ поддержка
- **Rate Limiting**: 100 req/min per IP
- **Session Management**: Redis с 5min TTL

## 🔒 **Безопасность**

### **Аутентификация**
- **JWT RS256** с ротацией refresh токенов
- **bcrypt** хеширование паролей
- **Progressive delay** на неудачных попытках
- **Account lockout** после 5 неудач

### **Авторизация**
- **Role-Based Access Control** (RBAC)
- **OAuth 2.0** стандарты
- **MFA Support** для повышенной безопасности
- **Session Security** с device fingerprinting

### **Мониторинг**
- **Failed Login Tracking** с IP и user agent
- **Audit Logging** всех security events
- **Rate Limiting** на всех endpoints
- **Suspicious Activity Detection**

## 🧪 **Валидация и Тестирование**

### **Pre-Commit Checks**
```bash
# Lint спецификации
npx @redocly/cli lint main.yaml

# Генерация Go кода
ogen --target /tmp/codegen --package api --clean main.yaml

# Запуск тестов
go test ./...
```

### **Enterprise Requirements**
- [x] **Domain Inheritance**: Использует infrastructure-entities
- [x] **Zero Duplication**: Нет повторяющихся полей
- [x] **Strict Security**: JWT, bcrypt, rate limiting
- [x] **Audit Trail**: Полное логирование операций
- [x] **Health Endpoints**: 4 типа health проверок
- [x] **Error Handling**: Стандартизированные ошибки
- [x] **Documentation**: OpenAPI 3.0 спецификация

## 🔗 **Зависимости**

### **Внешние Сервисы**
- **Email Service**: Отправка verification emails
- **Redis**: Session storage и caching
- **PostgreSQL**: User accounts и audit logs

### **Внутренние Сервисы**
- **User Profile Service**: Extended user data
- **Notification Service**: Security alerts
- **Audit Service**: Centralized audit logging

## 📈 **Мониторинг**

### **Метрики**
- `auth_login_attempts_total` - Общее число попыток входа
- `auth_login_success_rate` - Процент успешных входов
- `auth_active_sessions` - Активных сессий
- `auth_failed_attempts` - Неудачных попыток
- `auth_oauth_usage` - Использование OAuth провайдеров

### **Alerts**
- Login failure rate > 5%
- P95 latency > 100ms
- Active sessions > 50,000
- Memory usage > 80%

## 🚀 **Использование**

### **Валидация**
```bash
npx @redocly/cli lint proto/openapi/auth-service/main.yaml
```

### **Генерация Go кода**
```bash
ogen --target ../../services/auth-service-go/pkg/api \
  --package api --clean proto/openapi/auth-service/main.yaml
```

### **Документация**
```bash
npx @redocly/cli build-docs proto/openapi/auth-service/main.yaml \
  -o docs/index.html
```

## 📞 **Поддержка**

- **Security Team**: security@necpgame.com
- **DevOps**: devops@necpgame.com
- **Architecture**: architecture@necpgame.com

**Все вопросы по Auth Service направлять в #auth-service Slack канал**

---

*Auth Service обеспечивает enterprise-grade безопасность для миллиона+ пользователей NECPGAME*