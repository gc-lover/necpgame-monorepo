---

# MVP Backend Architecture

**Статус:** ready  
**Версия:** 1.0.0  
**Дата:** 2025-11-08  
**Ответственный:** Backend Guild  
**Связанные документы:** `mvp-frontend-architecture.md`, `2025-11-08-gameplay-backend-sync.md`, `database/schema.md`

**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-08 12:20  
**api-readiness-notes:** Сервисы и порты синхронизированы с текущей микросервисной архитектурой и Production URL.

---

## 1. Архитектурный обзор

- Стиль: микросервисная архитектура, контейнеризация (Docker, Kubernetes dev cluster).
- Интеграция: REST (Sync) + Kafka (Async), PostgreSQL, Redis, Keycloak.
- Сервисы MVP: `api-gateway`, `auth-service`, `character-service`, `gameplay-service`, `social-service`, `economy-service`, `world-service`.

## 2. Модули и ответственность

| Сервис | Порт | Хранилище | Ответственность |
|--------|------|-----------|-----------------|
| api-gateway | 8080 | N/A | BFF, маршрутизация, rate limiting, JWT валидация |
| auth-service | 8081 | PostgreSQL (`auth` schema) | Регистрация, OAuth2, refresh токены |
| character-service | 8082 | PostgreSQL (`mvp_core`) | Аккаунты, персонажи, инвентарь |
| gameplay-service | 8083 | PostgreSQL (`mvp_core`), Redis | Боевые подсистемы, баллистика, навыки |
| social-service | 8084 | PostgreSQL (`mvp_core`), Kafka | Система заказов, рейтинги, арбитраж |
| economy-service | 8085 | PostgreSQL (`mvp_core`), Kafka | Крафт, материалы, escrow |
| world-service | 8086 | PostgreSQL (`mvp_core`), Kafka | `city.unrest`, события мира |

## 3. Интеграции и потоки

- REST: Frontend ↔ `api-gateway` ↔ сервисы.
- Kafka Topics: см. `2025-11-08-gameplay-backend-sync.md` + `world.unrest.updates`.
- Outbox Pattern: `mvp_meta.outbox` + Debezium коннектор.

## 4. Observability

- Логи: OpenTelemetry → Loki.
- Метрики: Prometheus + Grafana дашборд `mvp-backend`.
- Трейсинг: Jaeger (`traceId` из gateway).

## 5. Безопасность

- Keycloak issuer, JWT → сервисы проверяют через `jwks`.
- RBAC на уровне gateway и сервисов.
- Шифрование секретов через Vault dev instance.

## 6. CI/CD

- GitHub Actions: билд (Gradle), тесты, контейнеризация (Buildx).
- ArgoCD dev → apply манифестов.

## 7. Чек-лист готовности

- [x] Описаны сервисы, порты, ответственности.
- [x] Зафиксированы интеграции и Kafka.
- [x] Обозначены observability и безопасность.
- [ ] Обновить диаграмму в `docs/architecture/backend-mvp.drawio` (backend team).

---

**Следующее действие:** синхронизировать архитектуру с `BACK-JAVA` кодовой базой и обновить Helm чарты.
