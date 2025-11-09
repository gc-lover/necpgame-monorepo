# Task ID: API-TASK-383
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-08 22:00  
**Автор:** API Task Creator Agent  
**Зависимости:** API-TASK-319 (player-orders world impact API), API-TASK-320 (player-orders economy index API), API-TASK-321 (player-orders news API)

---

## Summary

Создать спецификацию `orders-lifecycle.yaml` для `social-service`, описывающую полный цикл заказов игроков: создание, заявки исполнителей, управление статусами, завершение, отзыв и арбитраж. Контракт обеспечивает синхронизацию с экономикой/миром и реализует требования `.BRAIN/06-tasks/active/CURRENT-WORK/active/2025-11-08-gameplay-backend-sync.md`.

---

## Source Documents

| Поле | Значение |
| --- | --- |
| Repository | `.BRAIN` |
| Path | `.BRAIN/06-tasks/active/CURRENT-WORK/active/2025-11-08-gameplay-backend-sync.md` |
| Version | v1.0.0 |
| Status | ready |
| API readiness | ready (2025-11-08) |

**Key points:**
- Требуются REST endpoints `/social/orders` для CRUD операций, заявок и переходов статусов (accept → in_progress → completed → dispute).
- Интеграция с escrow (`economy-service`), world impact и уведомлениями (`notification-service`).
- Журнал событий (timeline) и публикация `social.orders.lifecycle` в Kafka.
- Поддержка фильтрации по заказчику, исполнителю, фракции и связанным боевым событиям.
- Требуется безопасность `BearerAuth`, роль-ориентированные разрешения (`X-Order-Role`), полноценные ответы ошибок.

**Related docs:**
- `.BRAIN/02-gameplay/social/player-orders-system-детально.md` (v1.0.0, approved)
- `.BRAIN/02-gameplay/social/player-orders-world-impact-детально.md`
- `.BRAIN/05-technical/backend/notification-system.md`
- `.BRAIN/05-technical/backend/support/support-ticket-system.md` (арбитраж)

---

## Target Architecture

- **Microservice:** social-service  
- **Port:** 8084  
- **Domain:** social/orders  
- **API directory:** `api/v1/social/orders/orders-lifecycle.yaml`  
- **base-path:** `/api/v1/social/orders`  
- **Java package:** `com.necpgame.socialservice.orders.lifecycle`  
- **Frontend module:** `modules/social/player-orders/lifecycle` (store: `useSocialStore`)  
- **UI components:** `OrderBoard`, `ApplicationList`, `OrderTimeline`, `StatusTransitionModal`, `DisputePanel`  
- **Shared libs:** `@shared/ui/Stepper`, `@shared/forms/OrderStatusForm`, `@shared/hooks/useRealtimeChannel`

---

## Scope of Work

1. Систематизировать все шаги lifecycle (создание, заявки, статусы, завершение, отзыв, арбитраж) из источников.
2. Определить структуру OpenAPI: info, security, servers, tags, paths, components, callbacks/events.
3. Описать CRUD endpoints для заказов, заявок, статусов, отзывов и арбитража с примерами.
4. Сформировать схемы данных (OrderCreateRequest, OrderSummary, Application, StatusTransition, Review, TimelineEntry).
5. Задокументировать фильтры и пагинацию (использовать `shared/common/pagination.yaml`).
6. Зафиксировать Kafka события и интеграции с economy/world services.
7. Проверить спецификацию валидатором, обновить трекеры и .BRAIN документ.

---

## Endpoints

- **POST `/social/orders`** — создаёт заказ (параметры escrow, дедлайны, требования, связанный combatId); возвращает `OrderSummary`.
- **GET `/social/orders`** — поиск заказов по заказчику, статусу, типу, фракции; пагинация, сортировка по дедлайну/наградe.
- **GET `/social/orders/{orderId}`** — детальная информация (включая escrow, активные заявки, историю статусов).
- **POST `/social/orders/{orderId}/applications`** — заявка исполнителя; проверяет репутацию, доступность, возвращает `OrderApplication`.
- **GET `/social/orders/{orderId}/applications`** — список заявок с фильтрами по статусу (pending/accepted/declined).
- **POST `/social/orders/{orderId}/status`** — изменение статуса (accept, in_progress, completed, disputed, cancelled) с контролем разрешений.
- **POST `/social/orders/{orderId}/complete`** — финализация заказа; передаёт данные в economy-service и world-service.
- **POST `/social/orders/{orderId}/review`** — обмен отзывами; публикует `social.player-orders.review`.
- **POST `/social/orders/{orderId}/dispute`** — создание арбитража (связь с support-ticket-system).
- **GET `/social/orders/{orderId}/timeline`** — хронология событий (создание, заявки, статусы, арбитраж, world impact); поддерживает курсоры.

Каждый путь использует `BearerAuth`, заголовки `X-Player-Id`, `X-Order-Role` (`customer`, `executor`, `arbiter`), возвращает ошибки через `shared/common/responses.yaml`, логирует `traceId`.

---

## Data Models

- `OrderCreateRequest` — orderType, title, description, rewardPackage, deadline, accessLevel, reputationRequirements, linkedCombatId, escrowDeposit.
- `OrderSummary` — orderId, customerId, status, reward, escrowStatus, reputationImpact, linkedEvents[], createdAt, updatedAt.
- `OrderListResponse` — массив `OrderSummary` + `PaginationMeta`.
- `OrderApplicationRequest` / `OrderApplication` — applicantId, bidValue, availabilityWindow, coverLetter, skills[], squadMembers[], status.
- `OrderStatusUpdateRequest` — newStatus, reasonCode, progressPercent, proofLinks[], referenceIds (combat session, crafting job).
- `OrderCompletionRequest` — deliverables[], verificationMode, worldImpactIntent, analyticsTraceId.
- `OrderReviewRequest` / `OrderReview` — reviewerId, targetId, rating, tags[], feedback, disputeFlag.
- `OrderTimelineEntry` — eventType, timestamp, actorId, details (structured payload), references (applicationId, ticketId, worldImpactId).
- `DisputeTicket` (reference schema) — ticketId, reason, attachments[], resolutionStatus.
- Enumerations: `OrderStatus`, `OrderAccessLevel`, `OrderApplicationStatus`, `OrderDisputeReason`.

---

## Integrations & Events

- **Economy-service:**  
  - REST `POST /economy/player-orders/{orderId}/escrow` (existing spec) — упомянуть зависимость;  
  - Callback `escrow.locked` → обновляет статус заказа.
- **World-service:** `POST /world/impact/simulate` (API-TASK-385) — передача world impact при завершении.
- **Gameplay-service:** `POST /combat/ballistics/simulate` (API-TASK-382) — для заказов типа combat; хранить ссылку `linkedCombatId`.
- **Kafka:**  
  - `social.orders.lifecycle` — каждое изменение статуса (payload: orderId, fromStatus, toStatus, actorId, timestamp, reasonCode);  
  - `social.player-orders.application` — создание/обновление заявок;  
  - `economy.player-orders.escrow` — подписка для обновления escrow;  
  - `world.player-orders.impact` — подписка для отражения мировых эффектов;  
  - `social.player-orders.review` — публикация отзывов.
- **Notifications:** REST hook к `notification-service` (push/email), webhooks для `modules/social/orders`.

---

## Acceptance Criteria

1. Файл `api/v1/social/orders/orders-lifecycle.yaml` создан (≤ 400 строк) с корректным `info.x-microservice` и `servers`.
2. Все указанные endpoints описаны, содержат параметры, примеры запросов/ответов, коды ошибок.
3. Схемы данных покрывают заявки, статусы, отзывы, timeline, escrow ссылки и world impact.
4. Задокументирован заголовок `X-Order-Role`, включены правила доступа и ответы `403/409/422`.
5. Пагинация и фильтры подключают `shared/common/pagination.yaml`.
6. Kafka события (`social.orders.lifecycle`, `social.player-orders.application`, `social.player-orders.review`) описаны с payload и ключевыми полями.
7. Указаны зависимости на economy-service и world-service, ссылки на связанные спецификации.
8. Добавлены примеры, отражающие статусы accept/in_progress/completed/disputed из `.BRAIN` документа.
9. `validate-swagger.ps1 -ApiSpec api/v1/social/orders/orders-lifecycle.yaml` выполняется без ошибок.
10. Обновлены `brain-mapping.yaml`, `.BRAIN/.../2025-11-08-gameplay-backend-sync.md`, readiness-checklist.
11. FAQ содержит ответы на типовые вопросы (арбитраж, массовые обновления, скрытые заказы).

---

## FAQ / Notes

- **Как обрабатывать закрытые (stealth) заказы?** Использовать `accessLevel=stealth`, требовать заголовок `X-Order-Role=arbiter` или `customer`. Ответ `404` маскирует существование заказа для других пользователей.
- **Нужен ли bulk-режим обновления статусов?** Нет, batch сценарии идут через отдельный административный API; текущий контракт только для единичных переходов.
- **Как подключаются world impact результаты?** Endpoint `/social/orders/{orderId}/complete` принимает `worldImpactIntent`, а фактическое влияние публикуется world-service; зафиксируйте ссылку `worldImpactId`.
- **Можно ли отзывать заявки?** Добавьте query `action=withdraw` в `POST .../applications` (описать как режим с телом payload `withdrawReason`).
- **Как логируются арбитражные решения?** После закрытия тикета support-service отправляет webhook; спецификация должна описать callback `support.ticket.resolved`.

---

## Change Log

- 2025-11-08 22:00 — Задание создано (API Task Creator Agent)


