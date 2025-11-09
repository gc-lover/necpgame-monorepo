# Система заказов — репутация и рейтинг (детально)

**Статус:** approved  \\n**Версия:** 1.0.0  \\n**Дата создания:** 2025-11-07  \\n**Последнее обновление:** 2025-11-08 18:52  \\n**Приоритет:** высокий

**api-readiness:** ready  \\n**api-readiness-check-date:** 2025-11-08 09:55  \\n**api-readiness-notes:** Рейтинги заказчиков/исполнителей финализированы: формулы, JSON схемы, REST/Kafka контракты и UX требования согласованы с social/economy/world сервисами.

---

## 1. Общая схема рейтингов

- **Рейтинги сторон:** отдельные шкалы для заказчика и исполнителя.
- **Состав:** качество выполнения, соблюдение сроков, коммуникация, количество жалоб.
- **Диапазон:** 0–100 (интегрируется в `social-service`).
- **Категории:** Bronze (0–39), Silver (40–64), Gold (65–84), Platinum (85–100).
- **Decay:** рейтинг снижается со временем без активности, минимальный уровень = 20% от исторического пика.

- **Status:** in_progress
- **Last Updated:** 2025-11-08 18:55

---

## 2. Метрики исполнителя

- **Completion Rate (CR):** процент успешно выполненных заказов.
- **Timeliness (TL):** соблюдение сроков (штраф за задержку, бонус за опережение).
- **Quality (QL):** оценки заказчиков (1–5) по качеству доказательств, отсутствию штрафов.
- **Reliability (RL):** число жалоб, отмен, предательств.
- **Complexity Bonus (CB):** коэф. за сложные заказы (вес трека).

### 2.1 Расчёт (набросок)

```
Score_executor = w_cr * CR + w_tl * TL + w_ql * QL + w_rl * RL + w_cb * CB
```

- **Глобальные веса (по умолчанию):** w_cr = 0.3, w_tl = 0.2, w_ql = 0.25, w_rl = 0.15, w_cb = 0.1.
- **Decay:** финальный результат × decay(t), где t — дни без заказов (например, 1% в день после 14 дней).
- **Normalization:** значения CR/TL/QL/RL/CB приводятся к шкале 0–100 перед применением весов.
- **Role modifiers:** отдельные веса для боевых/хакерских/логистических заказов (конфигурация в `social-service`).

---

## 3. Метрики заказчика

- **Payment Reliability (PR):** своевременность оплаты, отсутствие отказов.
- **Brief Quality (BQ):** полнота описания, ясность требований (оценка исполнителей).
- **Dispute Rate (DR):** доля жалоб, конфликтов, арбитражей.
- **Reward Fairness (RF):** соответствие оплаты сложности (аналитика `economy-service`).
- **Repeat Business (RB):** проц. исполнителей, которые повторно берут заказы у заказчика.

### 3.1 Расчёт (набросок)

```
Score_client = v_pr * PR + v_bq * BQ + v_dr * DR + v_rf * RF + v_rb * RB
```

- **Пример весов:** v_pr = 0.3, v_bq = 0.2, v_dr = 0.2, v_rf = 0.15, v_rb = 0.15.
- **Decay:** аналогично исполнителю.
- **Penalty overrides:** при арбитраже в пользу исполнителя PR и DR получают мгновенное штрафное значение.

---

## 4. Отзывы и оценки

- **Отзывы:** текст + оценка (1–5) по двум шкалам: «качество» и «коммуникация».
- **Флаги:** исполнитель/заказчик может поставить флаг (позитивный, нейтральный, негативный).
- **Верификация:** `social-service` проверяет на спам, токсичность.
- **Жалобы:** запускают арбитраж, влияют на рейтинг.

---

## 5. Категории и привилегии

- **Bronze:** базовый доступ.
- **Silver:** скидки на комиссии, доступ к дополнительным фильтрам.
- **Gold:** приоритетное размещение заказов, быстрые выплаты.
- **Platinum:** эксклюзивные заказы, прямые приглашения, участие в элитных аукционах.
- **Санкции:** низкий рейтинг → лимиты на создание заказов/участие.

---

## 6. Динамика рейтинга

- **Decay:** рейтинг снижается при бездействии; минимальное значение — 20% от исторического пика.
- **Boost:** успешные серии заказов дают временный буст.
- **Сезонные сбросы:** в конце сезона пленарные обновления, награды.
- **Фракционные влияния:** фракции могут повышать/понижать рейтинг в своих системах.

---

## 7. Арбитраж и безопасность

- **Dispute Handler:** нейтральная организация (NPC) разбирает споры.
- **Штрафы:** понижение рейтинга, финансовые санкции, бан.
- **Защита:** escrow, страховые фонды (`economy-service`).
- **Логи:** все действия записываются для аудита.

---

## 8. UX и визуализация

- **Профиль:** карта рейтингов, диаграммы, отзывы, история.
- **Виджеты:** уровень, значки, предупреждения.
- **Leaderboard:** топ исполнителей/заказчиков по типам заказов.
- **Награды:** медали, титулы, косметика.

---

## 9. Интеграции

- `player-orders-system-детально.md` — общий процесс заказов.
- `relationships-system-детально.md` — доверие и социальные эффекты.
- `npc-hiring-system-детально.md` — рейтинги NPC, выступающих исполнителями.
- `economy-service`, `social-service` — вычисление рейтингов, decay, арбитраж.
- `visual-style-assets-детально.md` — визуальные бейджи и титулы.
- `city-life-population-algorithm.md` — влияние рейтингов на доступность заказов в городах.

---

## 10. Использование

- Применять для API расчёта рейтингов, отображения в UI, арбитража.
- Подготовить UX (профили, графики, уведомления).
- Балансировать веса, decay, пороги категорий.
- Связать с наградами и доступом к эксклюзивным заказам.
- Экспортировать JSON через `scripts/export-player-orders-ratings.ps1`.

---

## 11. Формулы и JSON схемы

- `PlayerOrderRatingCalculation` (`schemas/social/player-order-rating.schema.json`):
```json
{
  "$id": "schemas/social/player-order-rating.schema.json",
  "type": "object",
  "required": ["score", "category", "decayApplied", "metrics"],
  "properties": {
    "score": { "type": "number", "minimum": 0, "maximum": 100 },
    "category": { "type": "string", "enum": ["bronze", "silver", "gold", "platinum"] },
    "decayApplied": { "type": "number" },
    "metrics": {
      "type": "object",
      "properties": {
        "CR": { "type": "number" },
        "TL": { "type": "number" },
        "QL": { "type": "number" },
        "RL": { "type": "number" },
        "CB": { "type": "number" }
      }
    }
  }
}
```

- `PlayerOrderReview` (`schemas/social/player-order-review.schema.json`) — текст + оценки + флаги.
- `PlayerOrderPenalty` (`schemas/social/player-order-penalty.schema.json`) — штрафы, санкции.
- `PlayerOrderCategoryThresholds` (`schemas/social/player-order-category.schema.json`) — конфигурация порогов.

---

## 12. REST API

| Метод | Путь | Описание |
|-------|------|----------|
| `GET` | `/social/player-orders/ratings/{playerId}` | Возвращает рейтинг исполнителя/заказчика |
| `POST` | `/social/player-orders/ratings/recalculate` | Пересчёт рейтинга (batch/job) |
| `POST` | `/social/player-orders/reviews` | Создание отзыва |
| `GET` | `/social/player-orders/reviews/{orderId}` | Список отзывов по заказу |
| `POST` | `/social/player-orders/penalties` | Применение санкций/штрафов |
| `GET` | `/social/player-orders/categories` | Конфигурация категорий, привилегии |

- API включает параметры фильтрации (тип заказа, период, категория).
- Для batch пересчёта используется `jobId`, статус отслеживается через `/jobs/{jobId}`.

---

## 13. Kafka события

| Topic | Producer | Payload |
|-------|----------|---------|
| `social.player-orders.rating.updated` | social-service | `{ playerId, role, score, category, updatedAt }` |
| `social.player-orders.review.created` | social-service | `{ orderId, reviewerId, targetId, rating, tags }` |
| `social.player-orders.penalty.applied` | social-service | `{ playerId, role, penaltyType, delta, reason }` |
| `economy.player-orders.reward.adjusted` | economy-service | `{ orderId, delta, reason }` |

Подписчики: UI (notification-service), telemetry, rating-service, factions-service, quest-service.

---

## 14. UX / UI

- Макеты: `figma://ui/player-orders/profile` (рейтинг, графики).
- Компоненты: `PlayerOrderRatingCard`, `RatingTrendChart`, `ReviewList`, `PenaltyBanner`.
- Badges: визуальные бейджи из `visual-style-assets-детально.md` (asset ID `ASSET-BADGE-PO-*`).
- Нотификации: отправляются через notification-service при изменении категории/штрафах.

---

## 15. Проверка и согласование

- Продукт: заседание 2025-11-08 09:40 — веса и decay подтверждены.
- UI: PR `FW-PO-RATINGS-017` — карточки рейтингов и виджеты утверждены.
- Economy: согласованы коэффициенты Reward Fairness (meeting 2025-11-08 09:45).
- Security: анти-манипуляция рейтингами (ticket `SEC-PO-018`).
- QA чеклист:  
  - [x] JSON схемы валидированы `schema-test`.  
  - [x] Kafka payloadы описаны.  
  - [x] Документ < 400 строк, readiness-трекер обновлён.

---

## 16. История изменений

- 2025-11-08 — добавлены формулы, JSON схемы, REST/Kafka контракты, UX требования; статус `approved`, готовность `ready`.
- 2025-11-07 — базовые метрики рейтингов.

