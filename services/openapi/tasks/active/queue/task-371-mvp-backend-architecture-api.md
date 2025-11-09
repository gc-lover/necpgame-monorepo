# Task ID: API-TASK-371
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** queued
**Создано:** 2025-11-08 19:25
**Создатель:** GPT-5 Codex (API Task Creator)
**Зависимости:** API-TASK-177, API-TASK-180

---

## 📋 Краткое описание

Подготовить OpenAPI-спецификацию `MVP Backend Architecture`, описывающую REST API для управления сведениями о микросервисах MVP, их интеграциях, наблюдаемости, безопасности и CI/CD.

**Что нужно сделать:** Создать `api/v1/technical/mvp-backend-architecture.yaml`, отразив структуры данных по микросервиса́м, интеграционным потокам, observability стеку и операционным процедурам из документа `.BRAIN/05-technical/architecture/mvp-backend-architecture.md`.

---

## 🎯 Цель задания

Предоставить админскому микросервису единый контракт, через который команды и инструменты смогут получать актуальную архитектурную информацию MVP backend.

**Зачем это нужно:**
- Синхронизировать сведения о микросервисах (порты, хранилища, ответственность) с инфраструктурными инструментами.
- Дать DevOps и Observability командам API для доступа к конфигурациям мониторинга, логирования и безопасности.
- Поддержать автоматизацию CI/CD, проверку соответствия и обновление документации без прямой работы с `.BRAIN`.

---

## 📚 Источники информации

### Основной документ

**Репозиторий:** `.BRAIN`
**Путь:** `.BRAIN/05-technical/architecture/mvp-backend-architecture.md`
**Версия:** v1.0.0 (2025-11-08)
**Статус:** approved, api-readiness: ready

**Что важно:**
- Таблица сервисов, портов, хранилищ и ответственности (секция 2).
- Интеграции (REST, Kafka, Outbox) и observability стек (секции 3-4).
- Безопасность (Keycloak, RBAC, Vault), CI/CD (GitHub Actions, ArgoCD) и чек-листы.

### Дополнительные источники

- `.BRAIN/05-technical/architecture/mvp-frontend-architecture.md` — взаимные ссылки и согласование портов.
- `.BRAIN/05-technical/global-state/global-state-operations.md` — интеграционные каналы world-service.
- `.BRAIN/05-technical/backend/notification-system.md` — пример admin-service API стека.
- `.BRAIN/06-tasks/active/CURRENT-WORK/active/backend-audit-compact.md` — статус готовности сервисов.

### Связанные задания

- `task-177-backend-audit-complete-api.md` — технический аудит backend.
- `task-180-api-technical-summary-api.md` — сводка технических API.

---

## 📁 Целевая структура API

### Репозиторий: `API-SWAGGER`

**Целевой файл:** `api/v1/technical/mvp-backend-architecture.yaml`
> ⚠️ Ограничить файл ≤400 строк, при необходимости вынести схемы в `api/v1/technical/components/mvp-backend-architecture-schemas.yaml`.
**API версия:** v1 (semantic version 1.0.0)
**Тип:** OpenAPI 3.0.3 (при необходимости дополнить AsyncAPI ссылками на Kafka каналы)

**Структура директории:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── technical/
            ├── mvp-backend-architecture.yaml
            └── components/
                └── mvp-backend-architecture-schemas.yaml (опционально)
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend (микросервис)

- **Микросервис:** admin-service
- **Порт:** 8088
- **API Base Path:** `/api/v1/technical/*`
- **Домен:** техническая документация, наблюдаемость, управление архитектурой
- **Зависимости:**
  - auth-service (Keycloak issuer, auth metadata)
  - infra-observability (Prometheus, Grafana, Loki, Jaeger)
  - CI/CD pipeline (GitHub Actions, ArgoCD)
  - analytics-service (для статистики состояния сервисов)

### Frontend (модули)

- **Фронтенд модуль:** `modules/admin/architecture`
- **State Store:** `useAdminStore (architectureCatalog)`
- **UI компоненты (@shared/ui):** ArchitectureServiceTable, IntegrationGraph, ObservabilityChecklist, SecurityPolicyCard
- **Формы (@shared/forms):** ArchitectureSyncForm, ServiceAnnotationForm
- **Hooks (@shared/hooks):** usePolling, useDebounce, useDownload
- **Layouts (@shared/layouts):** AdminDashboardLayout

**Комментарий:** В начале OpenAPI указать блок архитектуры (см. пример в шаблоне), перечислить микросервис, модули и UI компоненты.

### OpenAPI требования

- Заполнить `info.x-microservice` (name=admin-service, port=8088, domain=admin, base-path=/api/v1/technical, package=com.necpgame.adminservice).
- В `servers` оставить только gateway URL (`https://api.necp.game/v1`, `http://localhost:8080/api/v1`).
- Подключить общие компоненты: `shared/common/security.yaml`, `shared/common/responses.yaml`, `shared/common/pagination.yaml`.

---

## ✅ Детальный план

### Шаг 1: Анализ архитектуры
- Выписать из документа список сервисов, портов, хранилищ, ответственности.
- Определить поля для модели `ServiceOverview`, включая интеграцию с Kafka и хранилищами.
- Зафиксировать контрольные флаги (observability, security, ciCd).

**Ожидаемый результат:** черновик схем `ServiceOverview`, `IntegrationChannel`, `ObservabilityProfile`, `SecurityPolicy`, `CICDPipeline`.

### Шаг 2: Проектирование эндпоинтов
- `GET /technical/architecture/mvp/services` — список сервисов с фильтрами (`domain`, `storage`, `status`).
- `GET /technical/architecture/mvp/services/{serviceId}` — детали сервиса, зависимости, метрики.
- `GET /technical/architecture/mvp/integrations` — REST/Kafka/Outbox каналы.
- `GET /technical/architecture/mvp/observability` — метрики, логи, трассировка.
- `GET /technical/architecture/mvp/security` — политики безопасности.
- `GET /technical/architecture/mvp/ci-cd` — пайплайны, ArgoCD приложения.
- `POST /technical/architecture/mvp/services:sync` — (опционально) триггер синхронизации документации (RBAC `architecture-admin`).

**Ожидаемый результат:** секция `paths` с методами, параметрами, примерами ответов.

### Шаг 3: Определение моделей и компонентов
- Описать схемы `ServiceOverview`, `ServiceDetail`, `IntegrationChannel`, `KafkaTopic`, `ObservabilityProfile`, `SecurityPolicy`, `CICDPipeline`, `SyncRequest`, `SyncStatus`.
- Указать поля: `serviceId`, `port`, `responsibilities`, `storage`, `restEndpoints`, `kafkaTopics`, `monitoringDashboards`, `alerts`, `ciPipelines`.
- Добавить `x-frontend` аннотации (какие компоненты UI потребляют модель).

### Шаг 4: Безопасность и аудиторские требования
- Наследовать `securitySchemes` (`bearerAuth`).
- Определить роли: `architecture-view`, `architecture-admin`.
- Задокументировать требования аудита (`updatedBy`, `sourceCommit`, `syncTimestamp`).

### Шаг 5: Примеры, расширения, ссылки
- Добавить `examples` для каждого эндпоинта (успешный ответ + ошибка).
- Вставить `x-integration` ссылки на Prometheus/Grafana и GitHub repos.
- Зафиксировать `x-monitoring` (SLO: 99.5% uptime admin-service), `x-governance` (review board, checklist).

### Шаг 6: Валидация и чеклист
- Прогнать `scripts/validate-swagger.ps1 api/v1/technical/mvp-backend-architecture.yaml`.
- Проверить чек-лист (`tasks/config/checklist.md`) — блоки архитектуры, безопасности, валидации.
- Убедиться, что файл ≤400 строк (вынести схемы при необходимости).

---

## 📏 Критерии приёмки (12)

1. Файл `api/v1/technical/mvp-backend-architecture.yaml` создан и проходит `scripts/validate-swagger.ps1` без ошибок.
2. Заполнен `info.x-microservice` с данными `admin-service (8088)` и base-path `/api/v1/technical`.
3. `servers` содержит только gateway URL (prod + localhost через gateway).
4. `GET /technical/architecture/mvp/services` возвращает список сервисов с фильтрами и пагинацией (`shared/common/pagination.yaml`).
5. `ServiceOverview` включает поля `serviceId`, `name`, `port`, `storage`, `responsibilities`, `integrationLevel`, `status`.
6. `IntegrationChannel` описывает REST, Kafka, Outbox (тип, endpoint/topic, частота, SLA).
7. `ObservabilityProfile` содержит метрики, дашборды, алерты (`prometheus`, `grafana`, `jaeger`, `loki`).
8. `SecurityPolicy` фиксирует Keycloak issuer, JWT audience, RBAC роли, Vault secret path.
9. `CICDPipeline` описывает GitHub Actions, Buildx, ArgoCD приложения и окружения.
10. `POST /technical/architecture/mvp/services:sync` доступен только ролям `architecture-admin`, возвращает `202 Accepted` и объект `SyncStatus`.
11. Все ошибки используют стандартные ответы из `shared/common/responses.yaml` (400, 401, 403, 404, 409, 500).
12. В задании указаны расширения `x-frontend`, `x-monitoring`, `x-governance` и ссылки на исходные `.BRAIN` документы.

---

## ❓ FAQ

**В: Нужно ли включать CRUD для редактирования архитектуры?**  
О: Нет, текущий контракт read-only + ручной `sync`. Изменения архитектуры происходят через отдельный процесс ревью, а API публикует только синхронизированные данные.

**В: Как синхронизировать информацию с реальной конфигурацией?**  
О: `POST /technical/architecture/mvp/services:sync` запускает бекенд-процесс (GitOps), который считывает `docs/architecture/backend-mvp.drawio` и Helm чарты. Описание процесса добавить в документацию.

**В: Нужны ли WebSocket каналы?**  
О: Пока нет. Обновления архитектуры редки и обслуживаются polling/refresh. При необходимости добавить AsyncAPI в следующих версиях.

**В: Где хранится связь с frontend архитектурой?**  
О: Через `x-integrations.frontendArchitectureDoc` указать ссылку на `api/v1/technical/mvp-frontend-architecture.yaml` (будущая задача) и `.BRAIN/05-technical/architecture/mvp-frontend-architecture.md`.

**В: Как учитывать нестандартные сервисы или временные компоненты?**  
О: Использовать массив `extensions` в `ServiceDetail` (описание, срок, владелец). Сервис без SLA отмечается полем `slaDefined = false`.
