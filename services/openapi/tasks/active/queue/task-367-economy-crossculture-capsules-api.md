# Task ID: API-TASK-367
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-08 17:26  
**Создатель:** AI Brain Manager (GPT-5 Codex)  
**Зависимости:** API-TASK-366 (world crossculture season API), API-TASK-150 (trading-guilds API), API-TASK-256 (stock-exchange-dividends API), API-TASK-320 (player-orders-economy-index API)

---

## 📋 Краткое описание

Разработать OpenAPI `crossculture-capsules.yaml` для economy-service, описывающий рынок «Коллаб-капсулы»: каталог товаров, динамические цены, лимиты, покупки, выдачу сезонной валюты `thread-token` и отчётность.

---

## 🎯 Цель

Предоставить экономике структурированный API, чтобы:
- управлять ассортиментом сезонных капсул и эмоций;
- контролировать дневные лимиты, динамическое ценообразование, бонусы;
- регистрировать покупки и синхронизировать награды с inventory-service;
- предоставлять дашборду статистику продаж.

---

## 📚 Источники

- `.BRAIN/06-tasks/active/CURRENT-WORK/active/2025-11-07-crossculture-easter-atlas.md` (v1.0.0, ready).
- `.BRAIN/02-gameplay/economy/economy-overview.md` (структура валют и marketplace).
- `.BRAIN/02-gameplay/economy/economy-pricing-detailed.md` — методы динамического ценообразования.
- `.BRAIN/05-technical/backend/mail-system.md` — рассылки подтверждений.
- `.BRAIN/06-tasks/active/CURRENT-WORK/open-questions.md` — решения по токенам `thread-token`.

---

## 📁 Целевая структура

- **Файл:** `api/v1/economy/crossculture/capsules.yaml`
- **Формат:** OpenAPI 3.0.3
- **Версия:** 1.0.0

```
api/
  v1/
    economy/
      crossculture/
        capsules.yaml
```

`info.x-microservice`:
```yaml
info:
  title: Crossculture Capsule Market API
  version: 1.0.0
  description: Управление сезонным рынком капсул Metropolis Threads
  x-microservice:
    name: economy-service
    port: 8085
    domain: economy
    basePath: /api/v1/economy
    package: com.necp.economy.crossculture
```

---

## 🏗️ Архитектура

- **Backend:** economy-service (8085) + inventory-service (выдача предметов), world-service (хабы), social-service (эмоции/титулы), analytics-service.
- **Frontend:** `modules/economy/seasonal-market`, `modules/social/seasons`.
  - State: `useEconomyStore` (`seasonalCatalog`, `purchaseHistory`, `tokenBalance`).
  - UI: `@shared/ui/CapsuleCard`, `@shared/ui/TokenBalance`, `@shared/forms/CapsulePurchaseForm`.
- **Kafka:** `economy.crossculture.purchase`, `economy.crossculture.catalog-updated`.

---

## 🔧 План

1. Смоделировать сущности капсулы (id, регион, цена, лимиты, награды) из `.BRAIN`.
2. Описать REST endpoints: каталог, покупка, лимиты, отчёты, администрирование.
3. Добавить выдачу сезонной валюты `thread-token` и контроль дневных лимитов.
4. Зафиксировать механики динамического ценообразования (baseline, surge, discount).
5. Подготовить события Kafka и связь с inventory-service.
6. Обновить mapping и документ .BRAIN.

---

## 🌐 Endpoints

1. `GET /api/v1/economy/crossculture/capsules`
   - Каталог капсул (фильтры по региону, типу, доступности).
   - Пагинация, сортировка по популярности / цене.

2. `POST /api/v1/economy/crossculture/capsules/purchase`
   - Тело: `capsuleId`, `quantity`, `paymentMethod` (eddies/thread-token), `playerId`, `region`.
   - Проверяет лимиты, списывает токены, публикует `economy.crossculture.purchase`.

3. `GET /api/v1/economy/crossculture/capsules/{capsuleId}`
   - Детали капсулы: награды, лимиты, расписание, динамическая цена.

4. `GET /api/v1/economy/crossculture/purchases`
   - История покупок игрока (фильтр по периодам, типам).

5. `POST /api/v1/economy/crossculture/capsules/{capsuleId}/adjust-price`
   - Администрирование: обновление базовой цены, коэффициентов surge/discount.

6. `GET /api/v1/economy/crossculture/stats`
   - Метрики: `salesVolume`, `tokenSpent`, `topCapsules`, `regionsPerformance`.

7. `GET /api/v1/economy/crossculture/token-balance`
   - Текущее количество `thread-token` для игрока.

---

## 🧱 Модели

- `Capsule`: `capsuleId`, `name`, `region`, `category`, `basePrice`, `currentPrice`, `purchaseLimitDaily`, `rewardPackage`, `activeFrom`, `activeTo`.
- `PurchaseRequest`: `playerId`, `capsuleId`, `quantity`, `paymentMethod`, `region`, `clientVersion`.
- `RewardPackage`: `items[]`, `emotes[]`, `titles[]`, `tokenBonus`.
- `CapsuleStats`: `salesVolume`, `uniqueBuyers`, `tokensConsumed`, `avgBasket`, `conversionRate`.
- `TokenBalance`: `playerId`, `balance`, `dailyEarned`, `dailySpent`.

---

## 📊 Правила

- Лимит покупок: 3/день на капсулу; проверять при каждом запросе.
- Динамическая цена: `currentPrice = basePrice * demandCoefficient * regionModifier`.
- Токены: лимит 150 за сезон, конвертация из наград хабов.
- Покупки логируются в audit + отправка письма-подтверждения через mail-service.
- Возвраты не поддерживаются (422 при попытке).

---

## ✅ Acceptance Criteria

1. Создан `api/v1/economy/crossculture/capsules.yaml`, валиден по OpenAPI.
2. В `info.x-microservice` указан economy-service (8085).
3. Все endpoints документированы с кодами ошибок (`CAPSULE_LIMIT_EXCEEDED`, `CAPSULE_SOLD_OUT`, `TOKEN_CAP_REACHED`).
4. Описаны модели и бизнес-правила (лимиты, динамическая цена, токены).
5. Kafka события `economy.crossculture.purchase` и `economy.crossculture.catalog-updated` оформлены.
6. Используются общие схемы безопасности, ответов и ошибок.
7. Добавлены примеры запросов/ответов (включая успешную и отказанную покупку).
8. `brain-mapping.yaml` содержит запись для API-TASK-367.
9. `.BRAIN/2025-11-07-crossculture-easter-atlas.md` обновлён (API Tasks Status).
10. Подготовлена заметка о последующей генерации клиентов для economy-service и UI.

---

## ❓FAQ

- **Поддерживаются ли паки по регионам?** Да, регион указывается в моделе и фильтрах каталога.
- **Нужно ли объединять с market API?** Нет, сезонный рынок автономен; связь с основным marketplace — через аналитические отчёты.
- **Как учитывать промокоды?** В базовой версии не поддерживаются; указать в `Rejected Scenarios`.

---

После завершения — обновить mapping, .BRAIN документ и инициировать кодогенерацию.

