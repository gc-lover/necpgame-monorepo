# Система заказов — создание заказа (детально)

**Статус:** approved  \\n**Версия:** 1.0.0  \\n**Дата создания:** 2025-11-07  \\n**Последнее обновление:** 2025-11-08 09:53  \\n**Приоритет:** высокий

**api-readiness:** ready  \\n**api-readiness-check-date:** 2025-11-08 09:53  \\n**api-readiness-notes:** Мастер создания заказа финализирован: добавлены формулы бюджета, REST/JSON схемы, Kafka события, UX-поток и интеграции с экономикой/социальными сервисами.

---

## 1. Общий конвейер

- Подготовка брифа → валидация → оценка сложности → настройка публикации → выбор гарантий → публикация.
- Оркестрируется `social-service`, вызывает `economy-service` для расчётов и `npc-service` при адресных заказах.

- **Status:** in-progress
- **Last Updated:** 2025-11-08 16:55

---

## 2. Предусловия

- У игрока есть рейтинг ≥ Bronze и отсутствие активных санкций.
- Баланс кошелька покрывает минимальный депозит/escrow (`economy-service`).
- Если заказ корпоративный, нужна авторизация через `factions-service`.

---

## 3. Шаблоны заказов

- **Боевой:** операционные задачи, raid, escort.
- **Хакерский:** проникновение, защита, анализ сетей.
- **Экономический:** поставки, торговля, сбор ресурсов.
- **Социальный:** переговоры, infiltration, репутационные кампании.
- **Исследовательский:** разведка, анализ артефактов.
- Параметр `orderTemplate` задаёт предзаполненные поля и подсказки.

---

## 4. Параметры брифа

- **Цель:** краткое описание + детальные задачи.
- **Сроки:** дата старта, дедлайн, чекпоинты.
- **Бюджет:** базовая сумма, бонусы, штрафы.
- **Риски:** уровень опасности, зоны, правовой статус.
- **Команда:** количество слотов, требования к рейтингу.
- **Приватность:** публичный, пригласительный, закрытый.
- **Документы:** вложения (карты, схемы) — хранение в `content-service`.

---

## 5. Оценка сложности

- Сложность `ComplexityScore` формируется из факторов: уровень зоны, количество целей, требуемые навыки, риск конфликта с фракциями.
- Данные подтягиваются из `world-service` (территории) и `factions-service` (репутация).
- Результат влияет на рекомендованный бюджет и рейтинг допускаемых исполнителей.

---

## 6. Расчёт бюджета

- **Основная формула:**
```
BaseReward = ComplexityScore * RiskModifier * MarketIndex * TimeModifier
```
- `ComplexityScore` = Σ (вес фактора × значение) по зонам, навыкам, количеству этапов.
- `RiskModifier` (0.8–1.5) — угрозы (боевые/хакерские/политические).
- `MarketIndex` приходит из `economy-service` каждые 30 мин.
- `TimeModifier` (1.0–1.3) — дедлайны и чекпоинты.
- Escrow = `BaseReward * EscrowRate` (10–30%).
- Commission = `BaseReward * CommissionRate` (5–12%).
- Система сравнивает результат с медианой по типу заказа и предупреждает о завышении/занижении.

### 6.1 JSON схема бюджета

`schemas/social/player-order-budget.schema.json`:
```json
{
  "$id": "schemas/social/player-order-budget.schema.json",
  "type": "object",
  "required": ["complexityScore", "riskModifier", "marketIndex", "timeModifier", "baseReward", "escrow", "commission"],
  "properties": {
    "complexityScore": { "type": "number", "minimum": 0 },
    "riskModifier": { "type": "number", "minimum": 0.5, "maximum": 2.0 },
    "marketIndex": { "type": "number", "minimum": 0 },
    "timeModifier": { "type": "number", "minimum": 0.5, "maximum": 2.0 },
    "baseReward": { "type": "number", "minimum": 0 },
    "escrow": { "type": "number", "minimum": 0 },
    "commission": { "type": "number", "minimum": 0 }
  }
}
```

---

## 7. Режимы публикации

- **Публичный:** доступен всем, фильтр по рейтингу/специализации.
- **Пригласительный:** список конкретных исполнителей (игроки/NPC).
- **Закрытый:** доступен только внутри корпорации/клана.
- Для адресных публикаций используется `relationships-system` (доверие).

---

## 8. Валидация и проверки

- Проверка полноты полей + антисамоповторы.
- Юридический фильтр (запрет на запрещённые действия) — `world-service` + `social-service` политика.
- Проверка бюджета на минимальные/максимальные пороги.
- Скрининг токсичных формулировок (`content-service`).
- Сверка санкций и ограничений с `factions-service`.
- Логирование ошибок в `telemetry-service` для аналитики.

---

## 9. Гарантии и страхование

- Заказчик выбирает уровень страховки (базовая, расширенная, премиум).
- Связь с `economy-service` для расчёта комиссий и гарантий.
- Дополнительные гарантии дают бонус к рейтингу заказчика.
- Страховой полис хранится в `economy/insurance`, доступен арбитражу.

---

## 10. UX-поток

- Многошаговый мастер: Overview → Details → Budget → Publication → Review.
- Подсказки по заполнению, индикатор готовности брифа.
- Просмотр симуляции бюджета и рекомендованных исполнителей.
- Возможность сохранения черновиков (`content-service`).

---

## 11. Интеграции

- `player-orders-system-детально.md` — общий процесс.
- `player-orders-reputation-детально.md` — влияние брифа на рейтинг.
- `relationships-system-детально.md` — адресные приглашения.
- `npc-hiring-system-детально.md` — подключение NPC для выполнения.

---

## 12. Следующие шаги

- Реализация эндпоинтов и генерация клиентов (social/economy/world).
- Настройка мониторинга бюджета и escrow на проде.
- Подготовка справочника для UI (подсказки, тексты ошибок).

---

## 13. REST API

| Метод | Путь | Описание |
|-------|------|----------|
| `POST` | `/social/player-orders` | Создание заказа (`PlayerOrderCreateRequest`) |
| `POST` | `/social/player-orders/estimate` | Расчёт бюджета (`PlayerOrderEstimateRequest`) |
| `POST` | `/social/player-orders/validate` | Полная валидация брифа (`PlayerOrderValidationRequest`) |
| `POST` | `/social/player-orders/{orderId}/publish` | Публикация, блокировка escrow |
| `POST` | `/social/player-orders/{orderId}/draft` | Сохранение черновика |

**JSON схемы:**  
- `schemas/social/player-order-create.schema.json`  
- `schemas/social/player-order-estimate.schema.json`  
- `schemas/social/player-order-validation.schema.json`  
- `schemas/social/player-order-publish.schema.json`

---

## 14. Kafka события

| Topic | Producer | Payload |
|-------|----------|---------|
| `social.player-orders.draft.saved` | social-service | `{ orderId, ownerId, updatedAt }` |
| `social.player-orders.validation.failed` | social-service | `{ orderId, errors[], timestamp }` |
| `social.player-orders.published` | social-service | `{ orderId, template, publishedAt }` |
| `economy.player-orders.escrow.locked` | economy-service | `{ orderId, escrowAmount, insuranceTier }` |

- Подписчики: telemetry-service, monitoring-service, factions-service, quest-service.

---

## 15. UX / UI

- Макеты: `figma://ui/player-orders/create-wizard`.
- Компоненты: `OrderCreateWizard`, `BudgetSimulator`, `OrderValidationSummary`.
- Автодополнение: `relationships-service` (адресные приглашения), `npc-service` (NPC брокеры).
- Экспорт JSON: `scripts/export-player-orders.ps1`.

---

## 16. Проверка и согласование

- Продукт: workshop 2025-11-08 09:20 — подтверждён мастер.
- UI/UX: PR `FW-PO-CREATION-021` — макеты утверждены.
- Economy: формулы бюджета/escrow согласованы (meeting 2025-11-08 09:30).
- Security: валидаторы санкций подтверждены (ticket `SEC-PO-016`).
- QA чеклист:  
  - [x] JSON схемы валидированы `schema-test`.  
  - [x] Kafka payloadы задокументированы.  
  - [x] Документ < 400 строк, readiness-трекер обновлён.

---

## 17. История изменений

- 2025-11-08 — добавлены формулы, REST/JSON схемы, Kafka события и UX требования; статус `approved`, готовность `ready`.
- 2025-11-07 — базовый процесс создания заказа.