# Relationship Service - OpenAPI Specification

## 📋 **Назначение**

Relationship Service предоставляет комплексную систему управления социальными связями между персонажами в NECPGAME -
enterprise-grade API для дружбы, романтики, соперничества, менторства и репутационной системы в киберпанк MMOFPS RPG.
Сервис обеспечивает масштабируемую, приватную и высокопроизводительную систему социальных отношений.

## 🎯 **Функциональность**

### **🤝 Relationship Management (Управление отношениями)**

- **Многоуровневые отношения**: Дружба, романтика, соперничество, менторство, бизнес, вражда
- **Динамическая интенсивность**: Уровни от -10 до +10 с эволюцией со временем
- **Социальная история**: Отслеживание взаимодействий и развития отношений
- **Приватность**: Контролируемая видимость отношений для других игроков

### **👥 Friendship System (Система дружбы)**

- **Friend Requests**: Формальные запросы дружбы с персональными сообщениями
- **Friendship Levels**: Уровни близости от 1 до 10
- **Social Circles**: Группировка друзей по категориям
- **Activity Tracking**: Отслеживание совместной активности

### **❤️ Romance System (Романтическая система)**

- **Romantic Proposals**: Предложения свиданий, отношений, помолвок
- **Relationship Status**: Статусы от dating до married
- **Privacy Controls**: Уровни приватности отношений
- **Anniversary Tracking**: Отслеживание важных дат

### **⚔️ Rivalry System (Система соперничества)**

- **Rivalry Declaration**: Публичное объявление соперничества
- **Intensity Levels**: От minor до deadly
- **Conflict Tracking**: Отслеживание активных конфликтов
- **Resolution Mechanisms**: Пути разрешения соперничества

### **🎓 Mentorship System (Система менторства)**

- **Mentorship Contracts**: Формальные соглашения с условиями
- **Progress Tracking**: Отслеживание уроков и улучшений навыков
- **Skill Development**: Передача навыков от ментора к ученику
- **Payment Integration**: Монетизация менторства

### **🏆 Reputation System (Репутационная система)**

- **Multi-Category Reputation**: Торговля, бой, социум, лидерство, ремесло
- **Evidence-Based**: Репутация основана на задокументированных событиях
- **Dynamic Scoring**: Автоматический расчет и обновление
- **Social Influence**: Влияние на социальные взаимодействия

### **🌐 Social Network Analysis (Анализ социальной сети)**

- **Network Mapping**: Картирование социальных связей
- **Influence Calculation**: Расчет социального влияния
- **Connection Paths**: Поиск путей связи между персонажами
- **Network Density**: Анализ плотности социальных связей

## 📁 **Структура**

```
relationship-service/
├── main.yaml           # Основная спецификация (этот файл)
├── README.md          # Эта документация
├── relationships/     # Управление отношениями
├── friendships/       # Дружеские связи
├── romance/          # Романтические отношения
├── rivalries/        # Соперничество
├── mentorship/       # Менторство
├── reputation/       # Репутационная система
└── social-network/   # Социальный граф
```

## 🔗 **Зависимости**

### **Common Architecture (SOLID/DRY)**

- **common/schemas/social-entities.yaml**: `RelationshipEntity`, `FriendshipEntity`, `ReputationEntity`
- **common/schemas/common.yaml**: `BaseEntity`, `UUID`, `Timestamp`
- **common/responses/**: Стандартизированные ответы успеха/ошибки
- **common/security/**: JWT Bearer authentication

### **External Services**

- **user-profile-service**: Для профилей пользователей в отношениях
- **notification-service**: Для уведомлений о изменениях в отношениях
- **achievement-service**: Для достижений связанных с отношениями
- **currency-service**: Для монетизации менторства

## 📊 **Performance**

### **Response Times (P99)**

- **Health Check**: <1ms
- **Get Relationships**: <60ms (с пагинацией)
- **Create Relationship**: <80ms (с валидацией)
- **Reputation Query**: <30ms (с кэшированием)
- **Social Network Analysis**: <100ms (с ограничением глубины)
- **Mentorship Update**: <50ms (с прогресс-трекингом)

### **Throughput**

- **Relationship Operations**: 8,000+ ops/sec peak
- **Reputation Updates**: 15,000+ updates/sec peak
- **Friendship Queries**: 12,000+ queries/sec peak
- **Social Graph Traversals**: 3,000+ traversals/sec peak

### **Scalability**

- **Graph Database Sharding**: По user_id для социальных связей
- **Redis Clustering**: Для кэширования репутации и отношений
- **Event-Driven Updates**: Kafka для распространения изменений
- **CDN Integration**: Для публичных профилей отношений

### **Memory Usage**

- **Per User Relationship Cache**: <5KB (активные отношения)
- **Reputation Cache**: <2KB per user
- **Social Graph Cache**: <10KB per user network
- **Mentorship Progress**: <1KB per active mentorship

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
ogen --target ../../services/relationship-service-go/pkg/api \
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
-- Relationships table (core social connections)
CREATE TABLE relationships (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    character_id UUID NOT NULL,
    target_id UUID NOT NULL,
    type VARCHAR(20) NOT NULL,
    level INTEGER NOT NULL CHECK (level >= -10 AND level <= 10),
    description TEXT,
    established_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    last_interaction_at TIMESTAMPTZ,
    interaction_count INTEGER NOT NULL DEFAULT 0,
    flags TEXT[] DEFAULT '{}',
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE(character_id, target_id, type)
) PARTITION BY HASH (character_id);

-- Friendships table (specialized friendship tracking)
CREATE TABLE friendships (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    friend_id UUID NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'pending',
    friendship_level INTEGER NOT NULL DEFAULT 1 CHECK (friendship_level >= 1 AND friendship_level <= 10),
    requested_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    accepted_at TIMESTAMPTZ,
    blocked_at TIMESTAMPTZ,
    UNIQUE(user_id, friend_id)
) PARTITION BY HASH (user_id);

-- Reputation table (evidence-based reputation)
CREATE TABLE reputation_events (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    subject_id UUID NOT NULL,
    target_id UUID NOT NULL,
    category VARCHAR(30) NOT NULL,
    score INTEGER NOT NULL CHECK (score >= -100 AND score <= 100),
    evidence_count INTEGER NOT NULL DEFAULT 1,
    last_updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    is_public BOOLEAN NOT NULL DEFAULT true,
    UNIQUE(subject_id, target_id, category)
) PARTITION BY HASH (subject_id);

-- Mentorship contracts
CREATE TABLE mentorship_contracts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    mentor_id UUID NOT NULL,
    mentee_id UUID NOT NULL,
    contract_terms JSONB NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'active',
    started_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    completed_at TIMESTAMPTZ,
    progress_data JSONB DEFAULT '{}',
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE(mentor_id, mentee_id, status)
) PARTITION BY HASH (mentor_id);
```

### **Graph Database Integration**

```go
// Social graph using Neo4j or similar
type SocialGraphManager struct {
    driver neo4j.Driver
    redis  *redis.Client
}

func (sgm *SocialGraphManager) GetRelationshipNetwork(userID string, depth int) (*SocialNetwork, error) {
    query := `
        MATCH path = (u:User {id: $userId})-[r:RELATIONSHIP*1..%d]-(connected:User)
        WHERE r.is_active = true
        RETURN path, length(path) as depth
        ORDER BY depth
        LIMIT $limit
    `

    // Execute graph traversal
    result, err := sgm.driver.NewSession().Run(query, map[string]interface{}{
        "userId": userID,
        "depth":  depth,
        "limit":  100,
    })

    // Process results into social network structure
    return sgm.buildSocialNetwork(result)
}
```

### **Reputation Engine**

```go
type ReputationEngine struct {
    redis *redis.Client
    kafka *kafka.Producer
    db    *sql.DB
}

func (re *ReputationEngine) CalculateReputation(userID string) (*ReputationScore, error) {
    // Get all reputation events for user
    events, err := re.getReputationEvents(userID)
    if err != nil {
        return nil, err
    }

    // Calculate category scores
    scores := make(map[string]*ReputationCategory)
    for _, event := range events {
        if scores[event.Category] == nil {
            scores[event.Category] = &ReputationCategory{}
        }
        scores[event.Category].Score += event.Score
        scores[event.Category].EvidenceCount++
    }

    // Calculate overall score with weights
    overallScore := re.calculateOverallScore(scores)

    return &ReputationScore{
        UserID:    userID,
        Overall:   overallScore,
        Categories: scores,
    }, nil
}
```

### **Relationship Evolution System**

```go
type RelationshipEvolver struct {
    redis *redis.Client
    rules map[string]*EvolutionRule
}

type EvolutionRule struct {
    MinInteractions  int
    MaxLevel        int
    DecayRate       float64
    BoostEvents     []string
}

func (re *RelationshipEvolver) EvolveRelationship(rel *Relationship, event string) error {
    rule := re.rules[rel.Type]

    // Apply evolution based on event type
    switch event {
    case "positive_interaction":
        rel.Level = min(rel.Level+1, rule.MaxLevel)
        rel.LastInteractionAt = time.Now()
        rel.InteractionCount++
    case "negative_interaction":
        rel.Level = max(rel.Level-1, -10)
    case "time_decay":
        rel.Level = int(float64(rel.Level) * rule.DecayRate)
    }

    // Auto-terminate if level drops too low
    if rel.Level <= -10 {
        rel.IsActive = false
    }

    return re.saveRelationship(rel)
}
```

### **Privacy & Consent Management**

```go
type PrivacyManager struct {
    redis *redis.Client
    db    *sql.DB
}

func (pm *PrivacyManager) CheckRelationshipVisibility(rel *Relationship, viewerID string) (bool, error) {
    // Check relationship privacy settings
    if rel.Flags.Contains("public") {
        return true, nil
    }

    if rel.Flags.Contains("private") {
        return rel.CharacterID == viewerID || rel.TargetID == viewerID, nil
    }

    if rel.Flags.Contains("friends") {
        return pm.areFriends(rel.CharacterID, viewerID) || pm.areFriends(rel.TargetID, viewerID), nil
    }

    return false, nil
}
```

### **Rate Limiting**

```go
// Relationship creation limits
relationshipCreationLimiter := tollbooster.NewLimiter(50, time.Hour) // 50 relationships per hour

// Friend request limits
friendRequestLimiter := tollbooster.NewLimiter(20, time.Hour) // 20 friend requests per hour

// Reputation event limits
reputationEventLimiter := tollbooster.NewLimiter(100, time.Hour) // 100 reputation events per hour

// Mentorship proposal limits
mentorshipProposalLimiter := tollbooster.NewLimiter(5, time.Day) // 5 proposals per day
```

## 🔐 **Security Considerations**

### **Relationship Privacy**

- Granular privacy controls for each relationship type
- Consent-based visibility settings
- Social graph access restrictions
- PII protection in relationship data

### **Reputation Integrity**

- Evidence-based reputation changes only
- Fraud detection for artificial reputation inflation
- Audit trails for all reputation modifications
- Dispute resolution mechanisms

### **Consent Management**

- Explicit consent for romantic relationships
- Opt-in/opt-out for social features
- Harassment prevention through relationship blocking
- Safe termination procedures

### **Data Protection**

- Encrypted storage of sensitive relationship data
- GDPR compliance for personal relationship information
- Data minimization principles
- Secure deletion of terminated relationships

## 📈 **Monitoring & Observability**

### **Key Metrics**

```prometheus
# Relationship system metrics
relationships_total{type="friendship"} 256000
relationships_active_total 184000
friendship_requests_pending 1250

# Reputation system metrics
reputation_events_total{category="trading"} 45000
reputation_scores_average 127.5
reputation_fraud_attempts_total 23

# Social network metrics
social_connections_average 47.2
social_influence_top_percentile 892.5
social_network_traversals_total 156000

# Mentorship metrics
mentorship_contracts_active 3200
mentorship_completion_rate 0.78
mentorship_skill_improvements_total 125000

# Performance metrics
relationship_query_duration_p95 65
reputation_calculation_duration_p95 35
social_graph_traversal_duration_p95 95
```

### **Distributed Tracing**

```go
// Relationship operation tracing
func createRelationship(ctx context.Context, req CreateRelationshipRequest) (*Relationship, error) {
    span, ctx := tracer.StartSpanFromContext(ctx, "relationship.create")
    defer span.Finish()

    span.SetTag("relationship.type", req.Type)
    span.SetTag("character.id", req.CharacterID)
    span.SetTag("target.id", req.TargetID)
    span.SetTag("operation", "create")

    // Validate privacy and consent
    if err := pm.ValidateRelationshipConsent(ctx, req); err != nil {
        span.SetTag("error", true)
        span.LogFields(log.Error(err))
        return nil, err
    }

    // Implementation with tracing
    rel, err := s.relRepo.Create(ctx, req)
    if err != nil {
        span.SetTag("error", true)
        span.LogFields(log.Error(err))
        return nil, err
    }

    span.SetTag("relationship.id", rel.ID)
    span.SetTag("relationship.level", rel.Level)

    // Notify affected parties
    if err := s.notifyRelationshipCreated(ctx, rel); err != nil {
        span.LogFields(log.Error(err)) // Non-critical error
    }

    return rel, nil
}
```

### **Health Checks**

```yaml
# Relationship service health endpoints
/health:
  status: "healthy"
  relationships_active: 184000
  friendships_pending: 1250
  reputation_events_today: 5600

/health/reputation:
  status: "healthy"
  reputation_calculations_today: 45600
  fraud_detection_active: true
  evidence_validation_rate: 0.99

/health/social-network:
  status: "healthy"
  graph_nodes: 1250000
  graph_edges: 8750000
  traversal_cache_hit_rate: 0.89

/health/mentorship:
  status: "healthy"
  active_contracts: 3200
  completion_rate: 0.78
  average_contract_duration_days: 67
```

## 🎯 **API Design Principles**

### **SOLID/DRY Compliance**

- **Single Responsibility**: Каждый endpoint отвечает за конкретный аспект отношений
- **Open/Closed**: Легкое расширение через common inheritance
- **DRY**: Переиспользование RelationshipEntity и ReputationEntity
- **SOLID Inheritance**: Domain-specific entity extension

### **RESTful Design**

- **Resource-Based URLs**: `/relationships/{relationshipId}/mentorship/{mentorshipId}`
- **HTTP Methods**: GET, POST, PUT, DELETE appropriately
- **Status Codes**: Корректное использование 200, 201, 204, 400, 403, 404, 409
- **Content Negotiation**: JSON responses с правильными headers

### **Privacy-First Architecture**

- **Consent-Based**: Все отношения требуют явного согласия
- **Granular Privacy**: Детальный контроль видимости
- **Data Minimization**: Только необходимые данные хранятся
- **Right to Deletion**: Полное удаление отношений по запросу

### **Graph-Based Social Features**

- **Relationship Graph**: Эффективное хранение и запросы связей
- **Traversal Optimization**: Ограниченная глубина для производительности
- **Real-time Updates**: Event-driven обновления социальной сети
- **Caching Strategy**: Многоуровневое кэширование для быстрого доступа

---

## 📞 **Контакты**

**Команда разработки Relationship Service:**

- **Tech Lead**: @relationship-tech-lead
- **Backend**: @relationship-backend
- **Database**: @relationship-database
- **DevOps**: @relationship-devops

**Мониторинг и поддержка:**

- **SRE Team**: @platform-sre
- **Security**: @platform-security
- **Privacy**: @platform-privacy

**Бизнес-аналитика:**

- **Product Manager**: @relationship-product
- **Community Manager**: @relationship-community
- **Psychology**: @relationship-psychology

---

*Этот сервис является частью enterprise-grade микросервисной архитектуры NECPGAME с фокусом на глубокие социальные
взаимодействия и репутационную систему.*
