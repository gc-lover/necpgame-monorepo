# NECPGAME — Tech Stack One‑Pager (PC First, QUIC + WebSocket)

## Цели
- PC first ММОРПГ с лутер‑шутер петлёй в киберпанке
- Минимальные задержки в бою (QUIC/UDP), надёжная ММО‑синхронизация
- Эволюция от технодемки → MVP без смены ключевого стека

## Ключевые выборы
- Java 21, Spring Boot 3.3, SOLID
- Реалтайм: QUIC/UDP для боёв, WebSocket для лобби/соц
- Сериализация: Protobuf (gRPC межсервисно)
- Данные: PostgreSQL 15, Redis 7, Kafka 3.7, ClickHouse, OpenSearch, S3/MinIO
- Клиент: Unreal Engine 5.4, QUIC‑плагин, WebSocket, protobuf‑cpp

## Сервисы (логическая карта)
```mermaid
flowchart LR
  subgraph Edge
    CF[Cloudflare CDN/Edge]
    GW[Envoy API Gateway (HTTP/3)]
  end

  subgraph Realtime
    RTQ[Realtime QUIC Gateway\n(Armeria QUIC)]
    WSS[WebSocket Gateway\n(lobby/chat)]
  end

  subgraph Core
    AUTH[Auth/Keycloak]
    GAME[Gameplay]
    WORLD[World-State]
    COMBAT[Combat-Sim (instances)]
    INV[Inventory/Loot]
    ECON[Economy/Trade]
    SOC[Social/Chat/Party]
    ROM[Romance/Relationships]
    MM[Matchmaking]
    NOTIF[Notifications]
  end

  subgraph Data
    PG[(PostgreSQL 15\n mvp_core/mvp_meta)]
    RD[(Redis 7 Cluster)]
    KF[(Kafka 3.7)]
    CH[(ClickHouse)]
    OS[(OpenSearch)]
    S3[(S3/MinIO)]
  end

  CF --> GW -->|HTTP/3| WSS
  CF --> GW -->|HTTP/3| RTQ

  WSS --> SOC
  WSS --> NOTIF

  RTQ --> COMBAT
  COMBAT --> GAME
  GAME --> WORLD
  MM <--> COMBAT

  GAME --> PG
  WORLD --> PG
  INV --> PG
  ECON --> PG
  ROM --> PG

  ALL[[Microservices]] -. gRPC/proto .- GAME
  ALL -. outbox->Kafka .-> KF

  KF --> NOTIF
  KF --> CH
  CH --> OS
  S3 --> CH
  RD <--> MM
  RD <--> WSS
```

## Realtime протоколы
- QUIC/UDP: бой/инстансы, 60+ TPS, snapshot + delta, AOI, input‑replay
- WebSocket: лобби/чат/соц, бинарные Protobuf payload’ы

## Данные и БД
- PostgreSQL: нормализация до 3НФ, JSONB (метрики/конфиги), партиционирование
- Redis Cluster: кэш, сессии, очереди матчмейкинга
- Kafka: outbox‑паттерн (mvp_meta.outbox), события геймплея/уведомления
- ClickHouse: телеметрия/логирование, аналитика
- OpenSearch: поиск/фиды, трекинг предметов/заказов
- S3/MinIO: медиа, реплеи; архив в Glacier — отложенное решение

## Наблюдаемость и безопасность
- OpenTelemetry, Prometheus, Grafana, Loki, Tempo/Jaeger
- Keycloak 24 (OIDC), мTLS межсервисно, TLS/QUIC
- Сервер‑авторитет, rate limiting, аномалии; античит после технодемки

## Клиент (UE 5.4)
- QUIC клиент (MsQuic/Quiche), WebSocket резерв/соц
- Protobuf‑схемы с версионированием
- QoS: RTT/packet‑loss пробы, адаптивные частоты/пакеты

## Уровни реализации
- Технодемка: RT‑QUIC + Combat‑Sim 60+ TPS, MM, WSS лобби/чат, Inventory/Loot, наблюдаемость
- MVP: полный набор сервисов, Kafka‑интеграции, расширенная экономика/крафт/романтика, OpenSearch фиды

## Ссылки
- Подробный стек: `knowledge/implementation/TECH-STACK.yaml`
- Схемы БД (MVP): `knowledge/implementation/database/schema.yaml`
- Романтическая БД: `knowledge/implementation/database/romance-database-schema.sql`
# NECPGAME — Tech Stack One‑Pager (PC First, QUIC + WebSocket)\n\n## Цели\n- PC first ММОРПГ с лутер‑шутер петлёй в киберпанке\n- Минимальные задержки в бою (QUIC/UDP), надёжная ММО‑синхронизация\n- Эволюция от технодемки → MVP без смены ключевого стека\n\n## Ключевые выборы\n- Java 21, Spring Boot 3.3, SOLID\n- Реалтайм: QUIC/UDP для боёв, WebSocket для лобби/соц\n- Сериализация: Protobuf (gRPC межсервисно)\n- Данные: PostgreSQL 15, Redis 7, Kafka 3.7, ClickHouse, OpenSearch, S3/MinIO\n- Клиент: Unreal Engine 5.4, QUIC‑плагин, WebSocket, protobuf‑cpp\n\n## Сервисы (логическая карта)\n```mermaid\nflowchart LR\n  subgraph Edge\n    CF[Cloudflare CDN/Edge]\n    GW[Envoy API Gateway (HTTP/3)]\n  end\n\n  subgraph Realtime\n    RTQ[Realtime QUIC Gateway]\\n(Netty Incubator QUIC)\n    WSS[WebSocket Gateway]\\n(lobby/chat)\n  end\n\n  subgraph Core\n    AUTH[Auth/Keycloak]\n    GAME[Gameplay]\n    WORLD[World-State]\n    COMBAT[Combat-Sim (instances)]\n    INV[Inventory/Loot]\n    ECON[Economy/Trade]\n    SOC[Social/Chat/Party]\n    ROM[Romance/Relationships]\n    MM[Matchmaking]\n    NOTIF[Notifications]\n  end\n\n  subgraph Data\n    PG[(PostgreSQL 15\\n mvp_core/mvp_meta)]\n    RD[(Redis 7 Cluster)]\n    KF[(Kafka 3.7)]\n    CH[(ClickHouse)]\n    OS[(OpenSearch)]\n    S3[(S3/MinIO)]\n  end\n\n  CF --> GW -->|HTTP/3| WSS\n  CF --> GW -->|HTTP/3| RTQ\n\n  WSS --> SOC\n  WSS --> NOTIF\n\n  RTQ --> COMBAT\n  COMBAT --> GAME\n  GAME --> WORLD\n  MM <--> COMBAT\n\n  GAME --> PG\n  WORLD --> PG\n  INV --> PG\n  ECON --> PG\n  ROM --> PG\n\n  ALL[[Microservices]] -. gRPC/proto .- GAME\n  ALL -. outbox->Kafka .-> KF\n\n  KF --> NOTIF\n  KF --> CH\n  CH --> OS\n  S3 --> CH\n  RD <--> MM\n  RD <--> WSS\n```\n\n## Realtime протоколы\n- QUIC/UDP: бой/инстансы, 60+ TPS, snapshot + delta, AOI, input‑replay\n- WebSocket: лобби/чат/соц, бинарные Protobuf payload’ы\n\n## Данные и БД\n- PostgreSQL: нормализация до 3НФ, JSONB (метрики/конфиги), партиционирование\n- Redis Cluster: кэш, сессии, очереди матчмейкинга\n- Kafka: outbox‑паттерн (mvp_meta.outbox), события геймплея/уведомления\n- ClickHouse: телеметрия/логирование, аналитика\n- OpenSearch: поиск/фиды, трекинг предметов/заказов\n- S3/MinIO: медиа, реплеи; архив в Glacier — отложенное решение\n\n## Наблюдаемость и безопасность\n- OpenTelemetry, Prometheus, Grafana, Loki, Tempo/Jaeger\n- Keycloak 24 (OIDC), мTLS межсервисно, TLS/QUIC\n- Сервер‑авторитет, rate limiting, аномалии; античит после технодемки\n\n## Клиент (UE 5.4)\n- QUIC клиент (MsQuic/Quiche), WebSocket резерв/соц\n- Protobuf‑схемы с версионированием\n- QoS: RTT/packet‑loss пробы, адаптивные частоты/пакеты\n\n## Уровни реализации\n- Технодемка: RT‑QUIC + Combat‑Sim 60+ TPS, MM, WSS лобби/чат, Inventory/Loot, наблюдаемость\n- MVP: полный набор сервисов, Kafka‑интеграции, расширенная экономика/крафт/романтика, OpenSearch фиды\n\n## Ссылки\n- Подробный стек: `knowledge/implementation/TECH-STACK.yaml`\n- Схемы БД (MVP): `knowledge/implementation/database/schema.yaml`\n- Романтическая БД: `knowledge/implementation/database/romance-database-schema.sql`\n*** End Patch``` }}} ***!

