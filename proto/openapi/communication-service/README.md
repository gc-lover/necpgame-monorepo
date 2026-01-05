# Communication Service - OpenAPI Specification

## 📋 **Назначение**

Communication Service предоставляет комплексную систему коммуникации для NECPGAME - enterprise-grade API для чата,
уведомлений, голосовых каналов и модерации контента. Сервис обеспечивает масштабируемую, безопасную и
высокопроизводительную коммуникацию между игроками в киберпанк MMOFPS RPG.

## 🎯 **Функциональность**

### **💬 Chat System (Чат)**

- **Многоуровневые каналы**: Глобальный, гильдейский, партийный и приватный чат
- **Rich Text Messages**: Поддержка форматирования, вложений и упоминаний
- **Real-time Delivery**: Мгновенная доставка сообщений через WebSocket
- **Message History**: Пагинированная история с поиском
- **Reactions**: Система реакций на сообщения

### **🔔 Notification System (Уведомления)**

- **Многоуровневые уведомления**: Система, социальные, достижения, торговля, бой, гильдии
- **Гибкие настройки**: Персонализация по типам и каналам доставки
- **Priority System**: Уровни приоритета от low до critical
- **Bulk Operations**: Массовые операции для UI оптимизации
- **Scheduled Delivery**: Отложенная доставка уведомлений

### **🎤 Voice Channels (Голосовые каналы)**

- **Spatial Audio**: Пространственное аудио для иммерсивного опыта
- **WebRTC Integration**: Современные стандарты для низкой латентности
- **Capacity Management**: Управление вместимостью каналов
- **Quality Settings**: Настраиваемый битрейт и качество звука
- **Real-time Status**: Статус участников в реальном времени

### **🛡️ Moderation System (Модерация)**

- **Content Reports**: Система жалоб на контент и пользователей
- **Automated Actions**: Автоматизированные действия по модерации
- **Audit Trail**: Полный аудит действий модераторов
- **Escalation System**: Эскалация серьезных нарушений
- **Evidence Management**: Управление доказательствами нарушений

## 📁 **Структура**

```
communication-service/
├── main.yaml           # Основная спецификация (этот файл)
├── README.md          # Эта документация
├── chat/              # Чат-специфичные расширения
├── notifications/     # Уведомления-специфичные расширения
├── voice/             # Голосовые-специфичные расширения
└── moderation/        # Модерация-специфичные расширения
```

## 🔗 **Зависимости**

### **Common Architecture (SOLID/DRY)**

- **common/schemas/social-entities.yaml**: `UserProfileEntity`, `ChatChannelEntity`, `ChatMessageEntity`
- **common/schemas/common.yaml**: `BaseEntity`, `UUID`, `Timestamp`
- **common/responses/**: Стандартизированные ответы успеха/ошибки
- **common/security/**: JWT Bearer authentication

### **External Services**

- **user-profile-service**: Для профилей пользователей в чате
- **guild-service**: Для гильдейских каналов и разрешений
- **party-service**: Для партийных коммуникаций

## 📊 **Performance**

### **Response Times (P99)**

- **Health Check**: <1ms
- **Send Chat Message**: <20ms
- **Get Notifications**: <50ms (с пагинацией)
- **Join Voice Channel**: <25ms
- **Moderation Action**: <45ms

### **Throughput**

- **Chat Messages**: 10,000+ msg/sec peak
- **Notifications**: 5,000+ notifications/sec peak
- **Voice Channels**: 50,000+ concurrent connections
- **WebSocket Connections**: 100,000+ simultaneous

### **Scalability**

- **Horizontal Scaling**: Stateless design для легкого масштабирования
- **Database Sharding**: По user_id для оптимального распределения
- **Redis Caching**: 95% кэширование для снижения нагрузки на БД
- **CDN Integration**: Для статических ассетов (аватары, эмодзи)

### **Memory Usage**

- **Per User Session**: <50KB
- **Chat Message Cache**: <256 bytes per message
- **Notification Queue**: <512 bytes per notification
- **WebSocket Connection**: <1KB per connection

## 🚀 **Использование**

### **Валидация**

```bash
# Redocly linting
npx @redocly/cli lint main.yaml

# Bundle для проверки $ref
npx @redocly/cli bundle main.yaml -o bundled.yaml
```

### **Генерация Go кода**

```bash
# Генерация из bundled спецификации
ogen --target ../../services/communication-service-go/pkg/api \
     --package api --clean bundled.yaml
```

### **Документация**

```bash
# HTML документация
npx @redocly/cli build-docs main.yaml -o docs/index.html

# Swagger UI playground
npx @redocly/cli build-docs main.yaml --template swagger-ui -o docs/playground.html
```

## 🔧 **Backend Implementation Notes**

### **Database Design**

```sql
-- Chat messages (partitioned by channel_id)
CREATE TABLE chat_messages (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    channel_id UUID NOT NULL,
    sender_id UUID NOT NULL,
    content TEXT NOT NULL,
    sent_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    edited_at TIMESTAMPTZ,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE
) PARTITION BY HASH (channel_id);

-- Notifications (partitioned by user_id)
CREATE TABLE notifications (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    type VARCHAR(20) NOT NULL,
    title VARCHAR(100) NOT NULL,
    content TEXT NOT NULL,
    read BOOLEAN NOT NULL DEFAULT FALSE,
    priority VARCHAR(10) NOT NULL DEFAULT 'normal',
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
) PARTITION BY HASH (user_id);

-- Voice channels (with spatial indexing)
CREATE TABLE voice_channels (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(50) NOT NULL,
    type VARCHAR(20) NOT NULL,
    max_participants INTEGER NOT NULL DEFAULT 10,
    current_participants INTEGER NOT NULL DEFAULT 0,
    region VARCHAR(20) NOT NULL
);

-- Moderation reports
CREATE TABLE moderation_reports (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    reporter_id UUID NOT NULL,
    reported_content_type VARCHAR(20) NOT NULL,
    reported_content_id UUID NOT NULL,
    reason VARCHAR(30) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'pending',
    priority VARCHAR(10) NOT NULL DEFAULT 'medium',
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
```

### **WebSocket Implementation**

```go
// Real-time chat and voice status
type WebSocketHub struct {
    clients    map[*WebSocketClient]bool
    broadcast  chan []byte
    register   chan *WebSocketClient
    unregister chan *WebSocketClient
    rooms      map[string]map[*WebSocketClient]bool
}

func (h *WebSocketHub) run() {
    for {
        select {
        case client := <-h.register:
            h.clients[client] = true
            if room, exists := h.rooms[client.roomID]; exists {
                room[client] = true
            } else {
                h.rooms[client.roomID] = make(map[*WebSocketClient]bool)
                h.rooms[client.roomID][client] = true
            }

        case client := <-h.unregister:
            if _, ok := h.clients[client]; ok {
                delete(h.clients, client)
                if room, exists := h.rooms[client.roomID]; exists {
                    delete(room, client)
                    if len(room) == 0 {
                        delete(h.rooms, client.roomID)
                    }
                }
            }

        case message := <-h.broadcast:
            // Broadcast to all clients in the same room
            for client := range h.clients {
                select {
                case client.send <- message:
                default:
                    close(client.send)
                    delete(h.clients, client)
                }
            }
        }
    }
}
```

### **Redis Caching Strategy**

```go
// User notification settings cache
userNotifSettingsKey := fmt.Sprintf("user:%s:notif_settings", userID)

// Channel participant counts
channelParticipantsKey := fmt.Sprintf("channel:%s:participants", channelID)

// Recent chat messages (LRU cache)
chatHistoryKey := fmt.Sprintf("chat:%s:history", channelID)

// Voice channel metadata
voiceChannelKey := fmt.Sprintf("voice:%s:metadata", channelID)
```

### **Rate Limiting**

```go
// Chat message rate limiting
chatLimiter := tollbooster.NewLimiter(10, time.Minute) // 10 messages per minute

// Notification sending limits
notifLimiter := tollbooster.NewLimiter(100, time.Hour) // 100 notifications per hour

// Moderation action limits
moderationLimiter := tollbooster.NewLimiter(50, time.Hour) // 50 actions per hour
```

## 🔐 **Security Considerations**

### **Authentication**

- JWT Bearer tokens с expiration
- Service-to-service authentication для внутренних вызовов
- API key fallback для legacy integrations

### **Authorization**

- Role-based access control (RBAC)
- Channel-specific permissions
- Guild membership validation
- Content moderation permissions

### **Data Protection**

- End-to-end encryption для приватных сообщений
- Message content encryption at rest
- PII data minimization
- GDPR compliance для user data

### **Spam Prevention**

- Message rate limiting per user/channel
- Content filtering и moderation
- CAPTCHA integration для suspicious activity
- Automated bot detection

## 📈 **Monitoring & Observability**

### **Key Metrics**

```prometheus
# Chat system metrics
chat_messages_total{channel_type="guild"} 1250000
chat_active_connections 8500
chat_message_latency_p95 45

# Notification system metrics
notifications_sent_total{type="achievement"} 50000
notifications_delivery_rate 0.98
notifications_queue_size 150

# Voice system metrics
voice_channels_active 1200
voice_connections_total 45000
voice_audio_latency_p95 25

# Moderation metrics
moderation_reports_pending 45
moderation_actions_total{type="ban"} 1200
moderation_response_time_p95 30
```

### **Logging Strategy**

```json
{
  "timestamp": "2025-12-28T10:30:00Z",
  "level": "INFO",
  "service": "communication-service",
  "operation": "send_chat_message",
  "user_id": "123e4567-e89b-12d3-a456-426614174000",
  "channel_id": "987fcdeb-51a2-43d7-8f9e-123456789abc",
  "message_length": 150,
  "processing_time_ms": 12,
  "ip_address": "192.168.1.100",
  "user_agent": "NECPGAME-Client/1.0.0"
}
```

### **Health Checks**

```yaml
# Comprehensive health check endpoints
/health:
  status: "healthy"
  uptime: "15d 4h 23m"
  version: "1.2.3"
  dependencies:
    database: "healthy"
    redis: "healthy"
    websocket: "healthy"

/health/batch:
  overall_status: "healthy"
  services:
    chat: "healthy"
    notifications: "healthy"
    voice: "healthy"
    moderation: "healthy"

/health/ws:
  websocket_status: "healthy"
  active_connections: 8547
  message_rate: 1250
  latency_ms: 15
```

## 🎯 **API Design Principles**

### **SOLID/DRY Compliance**

- **Single Responsibility**: Каждый endpoint отвечает за одну операцию
- **Open/Closed**: Легкое расширение через common inheritance
- **DRY**: Переиспользование common schemas и responses
- **SOLID Inheritance**: Domain-specific entity extension

### **RESTful Design**

- **Resource-Based URLs**: `/chat/channels/{id}/messages`
- **HTTP Methods**: GET, POST, PUT, DELETE appropriately
- **Status Codes**: Корректное использование 200, 201, 204, 400, 403, 404
- **Content Negotiation**: JSON responses с правильными headers

### **Real-time Communication**

- **WebSocket Protocol**: Для real-time messaging и voice
- **Event-Driven**: Server-sent events для notifications
- **Connection Management**: Graceful handling of disconnects
- **Backpressure**: Prevention of message flooding

### **Error Handling**

- **Consistent Error Format**: Стандартизированные error responses
- **Detailed Error Messages**: Для debugging без sensitive data exposure
- **Graceful Degradation**: Fallback behavior при failures
- **Retry Logic**: Exponential backoff для transient failures

---

## 📞 **Контакты**

**Команда разработки Communication Service:**

- **Tech Lead**: @communication-tech-lead
- **Backend**: @communication-backend
- **Frontend**: @communication-frontend
- **DevOps**: @communication-devops

**Мониторинг и поддержка:**

- **SRE Team**: @platform-sre
- **Security**: @platform-security
- **Documentation**: docs@necpgame.com

---

*Этот сервис является частью enterprise-grade микросервисной архитектуры NECPGAME с фокусом на масштабируемость,
безопасность и производительность.*
