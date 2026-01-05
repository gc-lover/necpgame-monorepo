# Guild Service - OpenAPI Specification

## 📋 **Назначение**

Guild Service предоставляет комплексную систему управления гильдиями/кланами для NECPGAME - enterprise-grade API для
создания, управления и взаимодействия гильдий в киберпанк MMOFPS RPG. Сервис обеспечивает масштабируемую, безопасную и
высокопроизводительную систему социальных организаций.

## 🎯 **Функциональность**

### **🏰 Guild Lifecycle (Жизненный цикл гильдий)**

- **Создание гильдий**: Регистрация с валидацией требований и начальным капиталом
- **Управление гильдиями**: Обновление настроек, описаний и конфигураций
- **Расформирование**: Безопасное закрытие гильдий с распределением активов
- **Иерархия уровней**: Прогрессия гильдий с опытом и репутацией

### **👥 Member Management (Управление членами)**

- **Ролевая система**: Лидеры, офицеры, ветераны, рядовые члены
- **Разрешения**: Гибкая система прав доступа и модерации
- **Активность**: Отслеживание вклада и последнего посещения
- **Приглашения**: Система инвайтов с персональными сообщениями

### **🎯 Recruitment System (Система рекрутинга)**

- **Приложения**: Формальные заявки на вступление в гильдию
- **Требования**: Минимальные уровни, репутация, рекомендации
- **Валидация**: Проверка соответствия критериям вступления
- **Одобрение**: Многоступенчатый процесс рассмотрения заявок

### **💰 Banking & Economy (Банковская система)**

- **Общая казна**: Совместное хранение ресурсов и валюты
- **Транзакции**: Полная история операций с аудитом
- **Налоги**: Автоматические сборы с членов гильдии
- **Распределение**: Система распределения доходов

### **🗺️ Territory Control (Контроль территорий)**

- **Захват зон**: PvP система контроля территорий
- **Ресурсы**: Генерация дохода от контролируемых зон
- **Оборона**: Система защиты территорий
- **Конфликты**: Межгильдейские войны и альянсы

### **📢 Social Features (Социальные возможности)**

- **Объявления**: Публичные и внутренние новости гильдии
- **События**: Организация рейдов, встреч и турниров
- **Календарь**: Планирование гильдейских активностей
- **История**: Архив достижений и событий

## 📁 **Структура**

```
guild-service/
├── main.yaml           # Основная спецификация (этот файл)
├── README.md          # Эта документация
├── management/        # Управление гильдиями
├── members/           # Членство и роли
├── recruitment/       # Рекрутинг и приложения
├── banking/           # Финансовая система
├── territory/         # Территории и PvP
└── social/            # Социальные возможности
```

## 🔗 **Зависимости**

### **Common Architecture (SOLID/DRY)**

- **common/schemas/social-entities.yaml**: `GuildEntity`, `GuildMemberEntity`
- **common/schemas/common.yaml**: `BaseEntity`, `UUID`, `Timestamp`
- **common/responses/**: Стандартизированные ответы успеха/ошибки
- **common/security/**: JWT Bearer authentication

### **External Services**

- **currency-service**: Для банковских транзакций и налогов
- **notification-service**: Для оповещений членов гильдии
- **achievement-service**: Для гильдейских достижений
- **territory-service**: Для контроля территорий

## 📊 **Performance**

### **Response Times (P99)**

- **Health Check**: <1ms
- **Get Guild Info**: <40ms (с кэшированием)
- **List Members**: <60ms (с пагинацией)
- **Bank Transaction**: <70ms (с валидацией баланса)
- **Territory Update**: <50ms (с гео-индексацией)

### **Throughput**

- **Guild Operations**: 5,000+ ops/sec peak
- **Member Queries**: 10,000+ queries/sec peak
- **Bank Transactions**: 2,000+ tx/sec peak
- **Territory Updates**: 1,000+ updates/sec peak

### **Scalability**

- **Database Sharding**: По guild_id для оптимального распределения
- **Redis Clustering**: Для кэширования гильдейских данных
- **Event-Driven**: Kafka для межгильдейских коммуникаций
- **CDN Integration**: Для эмблем и баннеров гильдий

### **Memory Usage**

- **Per Guild Cache**: <2KB (metadata + member list)
- **Bank Balance Cache**: <512 bytes per guild
- **Territory Data**: <1KB per controlled zone
- **Event Calendar**: <256 bytes per active event

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
ogen --target ../../services/guild-service-go/pkg/api \
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
-- Guilds table (main entity)
CREATE TABLE guilds (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(50) NOT NULL UNIQUE,
    tag VARCHAR(5) NOT NULL UNIQUE,
    description TEXT,
    leader_id UUID NOT NULL,
    level INTEGER NOT NULL DEFAULT 1,
    experience BIGINT NOT NULL DEFAULT 0,
    reputation INTEGER NOT NULL DEFAULT 0,
    member_count INTEGER NOT NULL DEFAULT 1,
    max_members INTEGER NOT NULL DEFAULT 50,
    is_recruiting BOOLEAN NOT NULL DEFAULT true,
    emblem_url TEXT,
    founded_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
) PARTITION BY HASH (id);

-- Guild members (membership tracking)
CREATE TABLE guild_members (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    guild_id UUID NOT NULL,
    user_id UUID NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'recruit',
    joined_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    contribution_points BIGINT NOT NULL DEFAULT 0,
    is_active BOOLEAN NOT NULL DEFAULT true,
    UNIQUE(guild_id, user_id)
) PARTITION BY HASH (guild_id);

-- Guild bank transactions
CREATE TABLE guild_bank_transactions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    guild_id UUID NOT NULL,
    type VARCHAR(20) NOT NULL,
    amount BIGINT NOT NULL,
    currency VARCHAR(20) NOT NULL DEFAULT 'credits',
    description TEXT,
    executed_by UUID NOT NULL,
    balance_after BIGINT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
) PARTITION BY HASH (guild_id);

-- Guild applications (recruitment)
CREATE TABLE guild_applications (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    guild_id UUID NOT NULL,
    user_id UUID NOT NULL,
    application_text TEXT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'pending',
    applied_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    reviewed_by UUID,
    reviewed_at TIMESTAMPTZ,
    UNIQUE(guild_id, user_id, status)
) PARTITION BY HASH (guild_id);
```

### **Redis Caching Strategy**

```go
// Guild metadata cache
guildMetaKey := fmt.Sprintf("guild:%s:meta", guildID)

// Member list cache (with pagination)
guildMembersKey := fmt.Sprintf("guild:%s:members:%d:%d", guildID, offset, limit)

// Bank balance cache
guildBankKey := fmt.Sprintf("guild:%s:bank", guildID)

// Territory control cache
guildTerritoryKey := fmt.Sprintf("guild:%s:territory", guildID)

// Active events cache
guildEventsKey := fmt.Sprintf("guild:%s:events:active", guildID)
```

### **Event-Driven Architecture**

```go
// Guild events via Kafka
type GuildEvent struct {
    GuildID    string    `json:"guild_id"`
    EventType  string    `json:"event_type"`  // member_joined, bank_transaction, territory_lost
    ActorID    string    `json:"actor_id"`
    TargetID   string    `json:"target_id,omitempty"`
    Data       any       `json:"data"`
    Timestamp  time.Time `json:"timestamp"`
}

// Event handlers
func handleGuildMemberJoined(event GuildEvent) {
    // Update member count cache
    // Send notifications to guild
    // Update activity metrics
}

func handleGuildBankTransaction(event GuildEvent) {
    // Update bank balance cache
    // Send transaction notifications
    // Update financial metrics
}
```

### **Territory Control System**

```go
type TerritoryController struct {
    redis  *redis.Client
    kafka  *kafka.Producer
    mutex  sync.RWMutex
    zones  map[string]*ControlledZone
}

type ControlledZone struct {
    ZoneID         string    `json:"zone_id"`
    GuildID        string    `json:"guild_id"`
    ControlPercent int       `json:"control_percent"`
    LastUpdated    time.Time `json:"last_updated"`
    DefenseLevel   int       `json:"defense_level"`
}

// Territory capture logic
func (tc *TerritoryController) CaptureZone(attackerGuildID, zoneID string) error {
    tc.mutex.Lock()
    defer tc.mutex.Unlock()

    zone, exists := tc.zones[zoneID]
    if !exists {
        return errors.New("zone not found")
    }

    // PvP capture logic here
    // Update control percentage
    // Send zone change events

    return tc.updateZoneControl(zoneID, attackerGuildID, newControlPercent)
}
```

### **Rate Limiting**

```go
// Guild creation limits
guildCreationLimiter := tollbooster.NewLimiter(1, time.Hour) // 1 guild per hour per user

// Application submission limits
applicationLimiter := tollbooster.NewLimiter(5, time.Hour) // 5 applications per hour

// Bank transaction limits
bankTransactionLimiter := tollbooster.NewLimiter(100, time.Hour) // 100 transactions per hour per guild

// Event creation limits
eventCreationLimiter := tollbooster.NewLimiter(10, time.Day) // 10 events per day per guild
```

## 🔐 **Security Considerations**

### **Guild Ownership**

- Leader-only operations: disband, transfer leadership
- Officer permissions: member management, bank withdrawals
- Member permissions: view operations, event participation
- Public permissions: view basic info, apply for membership

### **Bank Security**

- Multi-signature requirements for large transactions
- Audit trails for all financial operations
- Fraud detection and prevention
- Balance consistency checks

### **Recruitment Safety**

- Anti-spam measures for applications
- Identity verification for high-level guilds
- Background checks integration
- Automated risk assessment

### **Territory Protection**

- Anti-cheat measures for territory control
- Dispute resolution system
- Escalation procedures for conflicts
- Fair play enforcement

## 📈 **Monitoring & Observability**

### **Key Metrics**

```prometheus
# Guild system metrics
guilds_active_total 15420
guild_members_total 245680
guild_bank_transactions_total{type="deposit"} 45670

# Performance metrics
guild_query_duration_p95 45
guild_member_list_duration_p95 67
guild_bank_transaction_duration_p95 78

# Territory metrics
guild_territory_controlled_zones 1250
guild_territory_conflicts_total 890
guild_territory_capture_rate 0.15

# Social metrics
guild_events_created_total 5600
guild_announcements_posted_total 12340
guild_applications_processed_total 45600
```

### **Distributed Tracing**

```go
// Guild operation tracing
func createGuild(ctx context.Context, req CreateGuildRequest) (*Guild, error) {
    span, ctx := tracer.StartSpanFromContext(ctx, "guild.create")
    defer span.Finish()

    span.SetTag("guild.name", req.Name)
    span.SetTag("guild.founder", req.LeaderID)
    span.SetTag("operation", "create")

    // Implementation with tracing
    guild, err := s.guildRepo.Create(ctx, req)
    if err != nil {
        span.SetTag("error", true)
        span.LogFields(log.Error(err))
        return nil, err
    }

    span.SetTag("guild.id", guild.ID)
    return guild, nil
}
```

### **Health Checks**

```yaml
# Guild service health endpoints
/health:
  status: "healthy"
  guilds_active: 15420
  members_online: 45670
  territories_controlled: 1250

/health/bank:
  status: "healthy"
  total_balance: 125000000
  transactions_today: 12450
  suspicious_activities: 0

/health/territory:
  status: "healthy"
  zones_monitored: 500
  conflicts_active: 12
  capture_events_today: 67
```

## 🎯 **API Design Principles**

### **SOLID/DRY Compliance**

- **Single Responsibility**: Каждый endpoint отвечает за конкретную операцию гильдии
- **Open/Closed**: Легкое расширение через common inheritance
- **DRY**: Переиспользование GuildEntity и GuildMemberEntity
- **SOLID Inheritance**: Domain-specific entity extension

### **RESTful Design**

- **Resource-Based URLs**: `/guilds/{guildId}/members/{userId}`
- **HTTP Methods**: GET, POST, PUT, DELETE appropriately
- **Status Codes**: Корректное использование 200, 201, 204, 400, 403, 404
- **Content Negotiation**: JSON responses с правильными headers

### **Pagination & Filtering**

- **Cursor-Based**: Для больших списков членов и транзакций
- **Filter Support**: По роли, статусу, уровню, активности
- **Sorting Options**: По дате присоединения, вкладу, рангу
- **Efficient Queries**: Оптимизированные индексы и кэширование

### **Optimistic Locking**

- **Version Fields**: Для конкурентных обновлений гильдий
- **Conflict Resolution**: Автоматическое разрешение конфликтов
- **Audit Trails**: Полная история изменений
- **Data Consistency**: Гарантия целостности данных

---

## 📞 **Контакты**

**Команда разработки Guild Service:**

- **Tech Lead**: @guild-tech-lead
- **Backend**: @guild-backend
- **Database**: @guild-database
- **DevOps**: @guild-devops

**Мониторинг и поддержка:**

- **SRE Team**: @platform-sre
- **Security**: @platform-security
- **Game Balance**: @guild-balance

**Бизнес-аналитика:**

- **Product Manager**: @guild-product
- **Community Manager**: @guild-community
- **Economics**: @guild-economics

---

*Этот сервис является частью enterprise-grade микросервисной архитектуры NECPGAME с фокусом на социальные взаимодействия
и организацию сообщества игроков.*

