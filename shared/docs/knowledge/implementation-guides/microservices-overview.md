# Микросервисная архитектура - Детальный обзор

**api-readiness:** not-applicable  
**api-readiness-check-date:** 2025-11-07
**api-readiness-notes:** Техническая документация, описывает архитектуру, не для создания API

**Статус:** approved  
**Версия:** 1.1.0  
**Дата создания:** 2025-11-07  
**Последнее обновление:** 2025-11-08  
**Приоритет:** критический

---

## Описание

Детальное описание микросервисной архитектуры backend систем NECPGAME. Документ фиксирует инфраструктуру, микросервисы, их взаимодействие и эксплуатацию полностью распределённого backend без монолита.

---

## Текущее состояние

**Статус:** 100% микросервисная архитектура  
**Monolith:** выведен из эксплуатации  
**Backend входные точки:** API Gateway `http://localhost:8080` (dev) и прямые сервисы `http://localhost:8081-8086` (dev), Production только через `https://api.necp.game/v1`

**Ключевые принципы:**
- Все бизнес-функции реализуются в отдельных микросервисах (auth, character, gameplay, social, economy, world)
- API Gateway маршрутизирует запросы к микросервисам и реализует cross-cutting функции
- Каждая OpenAPI спецификация обязана содержать `info.x-microservice` с целевым сервисом
- Прямой доступ к микросервисам на продакшне отсутствует, используется единый публичный домен `api.necp.game`

---

## Инфраструктурные компоненты

### 1. API Gateway (Spring Cloud Gateway)

**Порт:** 8080  
**Назначение:** Единая точка входа для всех клиентов

**Функции:**
- Маршрутизация запросов к микросервисам
- JWT валидация и авторизация
- Load Balancing между инстансами сервисов
- Circuit Breaker для устойчивости
- CORS конфигурация
- Rate Limiting
- Request/Response трансформация

**Пример маршрутизации:**
```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: auth-service
          uri: lb://AUTH-SERVICE
          predicates:
            - Path=/api/v1/auth/**
        - id: character-service
          uri: lb://CHARACTER-SERVICE
          predicates:
            - Path=/api/v1/characters/**
```

**Файл:** `BACK-GO/infrastructure/api-gateway/`

---

### 2. Service Discovery (Eureka Server)

**Порт:** 8761  
**Назначение:** Регистрация и обнаружение всех микросервисов

**Функции:**
- Автоматическая регистрация сервисов при старте
- Health checks (heartbeat)
- Service instance tracking
- Load balancing support
- Failover handling

**Dashboard:** http://localhost:8761

**Конфигурация сервиса:**
```yaml
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
  instance:
    prefer-ip-address: true
    lease-renewal-interval-in-seconds: 30
```

**Файл:** `BACK-GO/infrastructure/service-discovery/`

---

### 3. Config Server

**Порт:** 8888  
**Назначение:** Централизованное управление конфигурациями

**Функции:**
- Хранение конфигураций для всех сервисов
- Поддержка профилей (dev, test, prod)
- Динамическое обновление конфигураций
- Git backend для версионирования
- Encryption/Decryption секретов

**Структура конфигураций:**
```
config-repo/
├── application.yml           # Общие настройки
├── auth-service-dev.yml      # Auth service (dev)
├── auth-service-prod.yml     # Auth service (prod)
└── ...
```

**Файл:** `BACK-GO/infrastructure/config-server/`

---

## Микросервисы

### 1. Auth Service (Port 8081)

**API маршруты:** `/api/v1/auth/*`

**Ответственность:**
- Регистрация пользователей
- Аутентификация (login/logout)
- JWT token management (issue/refresh/validate)
- OAuth 2.0 integration (Google, GitHub)
- Password recovery
- Role management
- Account management

**Endpoints (9):**
- POST `/auth/register` - регистрация
- POST `/auth/login` - вход
- POST `/auth/logout` - выход
- POST `/auth/refresh` - обновление токена
- POST `/auth/password/forgot` - запрос сброса пароля
- POST `/auth/password/reset` - сброс пароля
- GET `/auth/roles` - получение ролей
- GET `/auth/oauth/{provider}/authorize` - OAuth redirect
- GET `/auth/oauth/{provider}/callback` - OAuth callback

**Статус:** ✅ Реализовано и работает

**БД:** Таблица `accounts` в PostgreSQL

**Файл:** `BACK-GO/microservices/auth-service/`

---

### 2. Character Service (Port 8082)

**API маршруты:** `/api/v1/characters/*`, `/api/v1/players/*`

**Ответственность:**
- Создание персонажей
- Управление персонажами (CRUD)
- Character slots
- Character selection
- Character attributes и stats
- Player profiles
- Character appearance
- Lifepath и origin системы

**Планируемые endpoints (~20):**
- GET `/characters` - список персонажей
- POST `/characters` - создание персонажа
- GET `/characters/{id}` - детали персонажа
- PUT `/characters/{id}` - обновление персонажа
- DELETE `/characters/{id}` - удаление персонажа
- POST `/characters/{id}/select` - выбор персонажа
- GET `/players/{id}` - профиль игрока
- ...

**Статус:** 📋 В планах (Фаза 2)

**БД:** Таблицы `players`, `characters`, `character_slots`

---

### 3. Gameplay Service (Port 8083)

**API маршруты:** `/api/v1/gameplay/*`

**Ответственность:**
- Боевая система (combat)
- Способности (abilities)
- Оружие (weapons)
- Импланты (implants)
- Cyberpsychosis механики
- Действия игрока (actions)
- Локации gameplay
- Combos и синергии

**Планируемые endpoints (~62):**
- Combat endpoints (6)
- Weapons endpoints (8)
- Abilities endpoints (7)
- Cyberpsychosis endpoints (21)
- Implants endpoints (10)
- Actions endpoints (4)
- Locations endpoints (6)

**Статус:** 📋 В планах (Фаза 2)

**БД:** Таблицы для combat, abilities, weapons, implants

---

### 4. Social Service (Port 8084)

**API маршруты:** `/api/v1/social/*`

**Ответственность:**
- Романсы и отношения с NPC
- NPC взаимодействия
- Гильдии/кланы
- Друзья (friends)
- Чат (chat)
- Party system
- Mail system
- Notifications
- События (events)

**Планируемые endpoints (~15):**
- Romances endpoints
- NPC interaction endpoints
- Guild endpoints
- Friends endpoints
- Chat endpoints
- Party endpoints
- Mail endpoints

**Статус:** 📋 В планах (Фаза 3)

**БД:** Таблицы для romances, npcs, guilds, friends, chat, mail

---

### 5. Economy Service (Port 8085)

**API маршруты:** `/api/v1/economy/*`

**Ответственность:**
- Инвентарь (inventory)
- Торговля (trading)
- Крафт (crafting)
- Валюты (currencies)
- Аукцион (auction house)
- Биржа (stock exchange)
- Лут (loot)

**Планируемые endpoints (~10):**
- Inventory endpoints
- Trading endpoints
- Crafting endpoints
- Currency endpoints
- Auction endpoints
- Market endpoints

**Статус:** 📋 В планах (Фаза 3)

**БД:** Таблицы для inventory, items, crafting, trading, auction

---

### 6. World Service (Port 8086)

**API маршруты:** `/api/v1/world/*`

**Ответственность:**
- Локации и зоны
- Мировые события
- Рейды
- Территории
- Global state
- Real-time синхронизация
- Zone management
- Player positions

**Планируемые endpoints (~10):**
- Locations endpoints
- World events endpoints
- Raids endpoints
- Territory endpoints
- Global state endpoints

**Статус:** 📋 В планах (Фаза 3)

**БД:** Таблицы для locations, world_events, raids, territories, global_state

---

## Межсервисное взаимодействие

### 1. REST через Feign Client

**Назначение:** Синхронные запросы между сервисами

**Пример:**
```java
@FeignClient(name = "AUTH-SERVICE")
public interface AuthServiceClient {
    @GetMapping("/validate-token")
    TokenValidationResponse validateToken(@RequestHeader("Authorization") String token);
}
```

**Использование:**
```java
@Service
public class CharacterService {
    private final AuthServiceClient authClient;
    
    public Character createCharacter(CreateCharacterRequest request, String token) {
        // Валидация токена через auth-service
        TokenValidationResponse validation = authClient.validateToken(token);
        if (!validation.isValid()) {
            throw new UnauthorizedException();
        }
        // Создание персонажа
        return characterRepository.save(new Character(...));
    }
}
```

---

### 2. Circuit Breaker (Resilience4j)

**Назначение:** Устойчивость к отказам других сервисов

**Пример:**
```java
@CircuitBreaker(name = "authService", fallbackMethod = "validateTokenFallback")
public TokenValidationResponse validateToken(String token) {
    return authServiceClient.validateToken(token);
}

public TokenValidationResponse validateTokenFallback(String token, Exception e) {
    // Fallback логика - проверка токена локально
    return localTokenValidator.validate(token);
}
```

**Конфигурация:**
```yaml
resilience4j:
  circuitbreaker:
    instances:
      authService:
        failure-rate-threshold: 50
        wait-duration-in-open-state: 10s
        permitted-number-of-calls-in-half-open-state: 3
```

---

### 3. Event-driven коммуникация (Message Queue)

**Назначение:** Асинхронная коммуникация для событий

**Technology:** Kafka или RabbitMQ

**Пример событий:**
```
PLAYER_REGISTERED -> auth-service публикует
                  -> character-service подписан (создать slots)
                  -> notification-service подписан (welcome email)

ENEMY_KILLED -> gameplay-service публикует
             -> economy-service подписан (generate loot)
             -> character-service подписан (add experience)

TRADE_COMPLETED -> economy-service публикует
                -> notification-service подписан (notify players)
                -> analytics-service подписан (track metrics)
```

**Пример publisher:**
```java
@Service
public class AuthEventPublisher {
    private final KafkaTemplate<String, PlayerRegisteredEvent> kafka;
    
    public void publishPlayerRegistered(String accountId) {
        PlayerRegisteredEvent event = new PlayerRegisteredEvent(accountId, LocalDateTime.now());
        kafka.send("player.registered", event);
    }
}
```

**Пример consumer:**
```java
@Service
public class CharacterEventListener {
    @KafkaListener(topics = "player.registered")
    public void handlePlayerRegistered(PlayerRegisteredEvent event) {
        // Создать character slots для нового игрока
        characterSlotService.createSlotsForPlayer(event.getAccountId());
    }
}
```

---

## Data Storage Strategy

### Database per Service Pattern

**Цель:** Каждый микросервис имеет свою БД (или schema)

**Текущая реализация (Фаза 1):**
- Все сервисы используют одну PostgreSQL БД
- Разные schemas для разных сервисов

**Планируемая реализация (Фаза 4):**
```
auth_db (PostgreSQL)
  └─ auth-service

character_db (PostgreSQL)
  └─ character-service

gameplay_db (PostgreSQL)
  └─ gameplay-service

social_db (PostgreSQL)
  └─ social-service

economy_db (PostgreSQL)
  └─ economy-service

world_db (PostgreSQL)
  └─ world-service

cache_db (Redis)
  └─ все сервисы (shared cache)
```

---

### Distributed Transactions

**Проблема:** Нельзя использовать @Transactional между сервисами

**Решения:**

**1. Saga Pattern (Orchestration)**
- Координатор управляет последовательностью операций
- Компенсирующие транзакции при ошибках

**2. Saga Pattern (Choreography)**
- Сервисы слушают события и реагируют
- Нет центрального координатора

**3. Eventual Consistency**
- Принятие eventual consistency вместо strict consistency
- Подходит для большинства MMORPG операций

**Пример Saga (Trade между игроками):**
```
1. Economy service: remove item from Player A → success
2. Economy service: add item to Player B → success
3. Economy service: transfer money → FAILURE
4. Compensate: return item to Player A
5. Notify players: trade failed
```

---

## Deployment

### Docker Compose

**Файл:** `BACK-GO/docker-compose-microservices.yml`

**Сервисы:**
```yaml
services:
  postgres:
    ports: 5433:5432
  
  eureka-server:
    ports: 8761:8761
    depends_on: postgres
  
  config-server:
    ports: 8888:8888
    depends_on: eureka-server
  
  api-gateway:
    ports: 8080:8080
    depends_on:
      - eureka-server
      - config-server
  
  auth-service:
    ports: 8081:8081
    depends_on:
      - postgres
      - eureka-server
      - config-server
  
  # Другие микросервисы...
```

**Запуск:**
```bash
cd BACK-GO
docker-compose -f docker-compose-microservices.yml up -d
```

**Порядок запуска:**
1. PostgreSQL (5433) - ~10 сек
2. Eureka Server (8761) - ~30 сек
3. Config Server (8888) - ~20 сек
4. API Gateway (8080) - ~30 сек
5. Микросервисы (8081+) - ~40 сек каждый

**Время полного запуска:** ~2-3 минуты

---

### Kubernetes (Планируется для Production)

**Планируемая структура:**
```
necpgame-cluster/
├── ingress-controller (NGINX)
├── api-gateway (3 replicas)
├── eureka-server (3 replicas)
├── config-server (2 replicas)
├── auth-service (5 replicas)
├── character-service (5 replicas)
├── gameplay-service (10 replicas) # Самый нагруженный
├── social-service (3 replicas)
├── economy-service (3 replicas)
└── world-service (5 replicas)
```

**Преимущества:**
- Auto-scaling по нагрузке
- Self-healing при падении pods
- Rolling updates без downtime
- Resource limits и requests
- Service mesh (Istio) для observability

---

## Мониторинг и Observability

### Планируемые инструменты

**1. Distributed Tracing (Zipkin/Jaeger)**
- Трейсинг запросов через все микросервисы
- Визуализация latency
- Поиск bottlenecks

**2. Metrics (Prometheus + Grafana)**
- Сбор метрик со всех сервисов
- Dashboards для мониторинга
- Алерты при проблемах

**3. Logging (ELK Stack)**
- Centralized logging
- Log aggregation
- Search и analysis

**4. Health Checks**
- Spring Boot Actuator
- Liveness и Readiness probes
- Custom health indicators

---

## Миграционная Roadmap

### ✅ Фаза 1: Infrastructure + Auth (Завершена)
- ✅ API Gateway
- ✅ Eureka Server
- ✅ Config Server
- ✅ Auth Service (9 endpoints)
- **Время:** 3 часа
- **Commits:** 3

### 📋 Фаза 2: Character + Gameplay (~80 endpoints)
- Character Service (8082)
- Gameplay Service (8083)
- **Оценка:** 6-8 часов
- **Endpoints:** ~80

### 📋 Фаза 3: Social + Economy + World (~35 endpoints)
- Social Service (8084)
- Economy Service (8085)
- World Service (8086)
- **Оценка:** 4-6 часов
- **Endpoints:** ~35

### 📋 Фаза 4: Database per Service
- Разделение БД для полной изоляции
- Data migration scripts
- **Оценка:** 4 часа

### 📋 Фаза 5: Production Ready
- Kubernetes deployment
- Monitoring и logging
- CI/CD pipelines
- **Оценка:** 1-2 недели

**Итого:** ~1-2 недели полная миграция

---

## Связанные документы

- [БЭКТАСК-MICROSERVICES.md](../../BACK-GO/docs/БЭКТАСК-MICROSERVICES.md) - руководство для Backend Agent
- [MICROSERVICES-FINAL-STATUS.md](../../BACK-GO/MICROSERVICES-FINAL-STATUS.md) - текущий статус миграции
- [backend-architecture-overview.md](./backend-architecture-overview.md) - обзор всей бэкенд архитектуры
- [ARCHITECTURE.md](../ARCHITECTURE.md) - общая архитектура .BRAIN

---

## История изменений

- v1.0.0 (2025-11-07) - Создан документ с детальным описанием микросервисной архитектуры

