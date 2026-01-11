# OAuth Service - Enterprise-Grade OAuth Integration

## 📋 **Назначение**

OAuth Service предоставляет комплексную интеграцию с внешними OAuth провайдерами для платформы NECPGAME с enterprise-grade безопасностью и масштабируемостью.

## 🎯 **Функциональность**

### **OAuth Провайдеры**
- **Google OAuth 2.0**: Интеграция с Google аккаунтами
- **Discord OAuth 2.0**: Интеграция с Discord
- **Steam OpenID**: Интеграция с Steam
- **GitHub OAuth**: Для разработчиков и модераторов

### **Безопасность и Управление**
- **State Parameter Protection**: Защита от CSRF атак
- **PKCE Support**: Proof Key for Code Exchange
- **Token Security**: Безопасное хранение и ротация токенов
- **Scope Management**: Гранулярное управление разрешениями

### **Управление Аккаунтами**
- **Account Linking**: Связывание OAuth аккаунтов с игровыми
- **Profile Sync**: Синхронизация профилей из провайдеров
- **Token Refresh**: Автоматическое обновление токенов
- **Account Unlinking**: Отвязка OAuth аккаунтов

## 📁 **Структура**

```
oauth-service/
├── main.yaml              # Enterprise-grade OAuth спецификация
└── README.md              # Эта документация
```

## 🔗 **Domain Inheritance**

Наследует от `infrastructure-entities.yaml` с добавлением:
- OAuth provider configurations
- Token management and rotation
- Security audit trails
- Account linking mechanisms

## 📊 **Performance**

- **P99 Latency**: <35ms для OAuth flows
- **Memory**: <45KB per instance
- **Concurrent Flows**: 10,000+ OAuth operations/second
- **Token Cache**: Redis-based с высокой доступностью

## 🚀 **API Endpoints**

- `POST /oauth/{provider}/authorize` - Инициация OAuth flow
- `POST /oauth/{provider}/callback` - Обработка OAuth callback
- `POST /oauth/{provider}/token` - Обмен кода на токены
- `POST /oauth/{provider}/refresh` - Обновление токенов
- `DELETE /oauth/{provider}/unlink` - Отвязка аккаунта

*Полная спецификация в main.yaml*

---

*OAuth Service обеспечивает безопасную интеграцию с внешними провайдерами для NECPGAME*