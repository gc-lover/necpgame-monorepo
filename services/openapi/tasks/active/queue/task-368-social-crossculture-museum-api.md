# Task ID: API-TASK-368
**Тип:** API Generation  
**Приоритет:** средний  
**Статус:** queued  
**Создано:** 2025-11-08 17:26  
**Создатель:** AI Brain Manager (GPT-5 Codex)  
**Зависимости:** API-TASK-366 (world crossculture season API), API-TASK-343 (mentorship-programs API), API-TASK-349 (npc-hiring-contracts API), API-TASK-346 (mentorship-effects API)

---

## 📋 Краткое описание

Создать OpenAPI `crossculture-museum.yaml` для social-service, описывающий управление цифровым музеем «Коды дружбы»: создание/модерация экспозиций гильдий, посещения, рейтинги, выдача предметов `network.woven-hands`.

---

## 🎯 Цель

Обеспечить UI и backend возможностью:
- гильдиям публиковать экспозиции (до 5 залов, 128 МБ ассетов);
- модерировать контент (pending → approved/rejected, модерационные заметки);
- отслеживать посещения, отзывы и выдачу сезонных наград;
- связать музей с общим календарём сезона.

---

## 📚 Источники

- `.BRAIN/06-tasks/active/CURRENT-WORK/active/2025-11-07-crossculture-easter-atlas.md` (раздел 3.6 и аналитика).
- `.BRAIN/02-gameplay/social/social-mechanics-overview.md` — принципы социальных структур.
- `.BRAIN/05-technical/backend/support/support-ticket-system.md` — модерационные процессы.
- `.BRAIN/05-technical/backend/notification-system.md` — уведомления для кураторов.
- `.BRAIN/06-tasks/active/CURRENT-WORK/open-questions.md` — подтверждённые решения по шаблонам.

---

## 📁 Целевая структура

- **Файл:** `api/v1/social/crossculture/museum.yaml`
- **Формат:** OpenAPI 3.0.3
- **Версия:** 1.0.0

```
api/
  v1/
    social/
      crossculture/
        museum.yaml
```

`info.x-microservice`:
```yaml
info:
  title: Crossculture Museum API
  version: 1.0.0
  description: Управление цифровым музеем Metropolis Threads
  x-microservice:
    name: social-service
    port: 8084
    domain: social
    basePath: /api/v1/social
    package: com.necp.social.crossculture.museum
```

---

## 🏗️ Архитектура

- **Backend:** social-service, интеграция с storage/CDN (S3), notification-service, analytics-service.
- **Frontend:** `modules/social/seasons`, `modules/world/events`, возможно `modules/ui/gallery`.
  - State: `useSocialStore` (`museumExhibits`, `moderationQueue`, `visitHistory`).
  - UI: `@shared/ui/ExhibitGrid`, `@shared/ui/ModerationQueue`, `@shared/forms/ExhibitForm`.
- **Kafka:** `social.crossculture.exhibit` (статусы), `social.crossculture.visit`.

---

## 🔧 План

1. Смоделировать сущности экспозиций (`social.crossculture_exhibits`) и посещений (аналитика).
2. Добавить endpoints для CRUD экспозиций, модерации, посещений, отзывов.
3. Описать правила ассетов (128 МБ, whitelist типов, хранение ссылок).
4. Задокументировать выдачу награды `network.woven-hands` после посещения всех залов.
5. Добавить метрики: созданные экспозиции, посещения, конверсия.
6. Подготовить события Kafka, уведомления кураторов.
7. Обновить mapping и документ `.BRAIN`.

---

## 🌐 Endpoints

1. `POST /api/v1/social/crossculture/museum/exhibits`
   - Создание экспозиции (гильдия).
   - Тело: `title`, `description`, `assets[]`, `curatorId`, `region`, `tags`.
   - Ответ: 201 Created (`Exhibit`), статус `PENDING`.

2. `GET /api/v1/social/crossculture/museum/exhibits`
   - Список экспозиций (фильтр `status`, `guildId`, `region`).
   - Пагинация, сортировка по популярности.

3. `PATCH /api/v1/social/crossculture/museum/exhibits/{exhibitId}`
   - Обновление экспозиции (до модерации).

4. `POST /api/v1/social/crossculture/museum/exhibits/{exhibitId}/moderate`
   - Модератор утверждает/отклоняет экспозицию.
   - Тело: `status` (APPROVED/REJECTED), `notes`.

5. `POST /api/v1/social/crossculture/museum/exhibits/{exhibitId}/visit`
   - Регистрация посещения игроком; возвращает `visitId`, прогресс награды.

6. `GET /api/v1/social/crossculture/museum/visits`
   - История посещений игрока/гильдии; используется UI и аналитикой.

7. `POST /api/v1/social/crossculture/museum/exhibits/{exhibitId}/feedback`
   - Оставить отзыв/оценку (1–5), комментарий.

8. `GET /api/v1/social/crossculture/museum/analytics`
   - Метрики: количество экспозиций, посещение, рейтинг, модерационные SLA.

---

## 🧱 Модели

- `Exhibit`: `exhibitId`, `guildId`, `curatorId`, `title`, `description`, `assets[]`, `status`, `region`, `createdAt`, `updatedAt`, `moderationNotes`.
- `ExhibitAsset`: `assetId`, `type`, `url`, `sizeBytes`, `checksum`.
- `ModerationRequest`: `status`, `moderatorId`, `notes`.
- `VisitRecord`: `visitId`, `playerId`, `exhibitId`, `visitedAt`, `duration`, `rewardGranted`.
- `Feedback`: `feedbackId`, `playerId`, `exhibitId`, `rating`, `comment`, `createdAt`.
- `MuseumAnalytics`: `totalExhibits`, `pendingCount`, `approvedCount`, `visits`, `uniqueVisitors`, `averageRating`.

---

## 📊 Правила

- Лимит: до 5 экспозиций на гильдию; проверять при создании.
- Ассеты: поддерживаемые типы (`image/*`, `video/mp4`, `audio/mpeg`, `application/json`), размер ≤128 МБ.
- SLA модерации: 24 часа; API должно отдавать `moderationDeadline`.
- Посещение всех залов выдает предмет `network.woven-hands` (через inventory-service).
- Флаг `autoFlag` при 3 отклонениях подряд.
- Уведомления: `MUSEUM_EXHIBIT_APPROVED`, `MUSEUM_EXHIBIT_REJECTED`.

---

## ✅ Acceptance Criteria

1. Создан файл `api/v1/social/crossculture/museum.yaml`, валиден.
2. `info.x-microservice` указывает social-service (8084) и пакет `com.necp.social.crossculture.museum`.
3. Задокументированы все перечисленные endpoints и модели данных.
4. Учтены лимиты экспозиций, размер ассетов, SLA модерации.
5. Kafka события `social.crossculture.exhibit` и `social.crossculture.visit` описаны.
6. Добавлены прометей-метрики `museum_exhibits_total`, `museum_visits_total`, `museum_moderation_sla_violation_total`.
7. Примеры: создание экспозиции, модерация, визит (включая награду).
8. Используются общие схемы безопасности и ответов.
9. `brain-mapping.yaml` обновлён записью с API-TASK-368.
10. `.BRAIN/2025-11-07-crossculture-easter-atlas.md` содержит обновлённый статус задач (API-TASK-366/367/368).

---

## ❓FAQ

- **Откуда берутся ассеты?** Хранятся во внешнем хранилище (S3); API возвращает подписанные URL.
- **Можно ли использовать этот API вне сезона?** Да, но параметры должны поддерживать `seasonId`.
- **Нужно ли объединять с guild bank?** Нет, финансовая логика покрывается economy-service; здесь только экспозиции и социальные награды.

---

После реализации — обновить mapping, документ .BRAIN и инициировать генерацию клиентов.

