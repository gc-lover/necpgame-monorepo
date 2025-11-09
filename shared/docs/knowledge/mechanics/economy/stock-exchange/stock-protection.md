# Биржа акций - Защита от манипуляций

**Статус:** approved  
**Версия:** 1.1.0  
**Дата создания:** 2025-11-06  
**Последнее обновление:** 2025-11-07 16:19  
**Приоритет:** высокий (Post-MVP)

**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-09 11:21
**api-readiness-notes:** Перепроверено 2025-11-09 11:21: circuit breakers, детекция инсайдов, структуры данных и API остаются консистентными, готово к задачам.

---

## Краткое описание

Защита биржи от манипуляций и мошенничества.

**Микрофича:** Anti-manipulation, circuit breakers, insider trading detection

---

## 🛡️ Механизмы защиты

### 1. Circuit Breakers (Остановка торгов)

**Trigger:** Падение/рост ≥ 15% за 1 час

**Действия:**
```
ARSK: 1,000 → 850 (-15%) in 30 minutes
→ CIRCUIT BREAKER TRIGGERED!
→ Trading HALTED for 15 minutes
→ Cooldown period
→ Trading resumes

Purpose: Prevent panic selling/buying
```

### 2. Price Limits (Ценовые лимиты)

**Daily limits:**
```
Max change per day: ±20%

If ARSK opens @ 1,000:
Max price: 1,200 (+20%)
Min price: 800 (-20%)

If price hits limit: trading paused до next day
```

### 3. Insider Trading Detection

**Flags:**
- Buying before positive quest outcomes
- Selling before negative quest outcomes
- Unusual timing patterns

**Penalty:**
- Investigation
- Profit confiscation
- Ban from stock exchange

---

## 🗄️ Структуры данных

```sql
CREATE TABLE surveillance_alerts (
    id UUID PRIMARY KEY,
    alert_type VARCHAR(32) NOT NULL, -- INSIDER, SPOOFING, WASH_TRADE
    corporation_id VARCHAR(100),
    player_id UUID,
    severity VARCHAR(10) NOT NULL, -- LOW/MED/HIGH
    trigger_details JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(16) NOT NULL DEFAULT 'OPEN'
);

CREATE TABLE enforcement_actions (
    id UUID PRIMARY KEY,
    player_id UUID NOT NULL,
    action_type VARCHAR(20) NOT NULL, -- WARNING | SUSPENSION | BAN | CONFISCATION
    reason TEXT NOT NULL,
    issued_by UUID NOT NULL,
    issued_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP
);
```

---

## 🔍 Алгоритмы обнаружения

- **Spoofing:** быстрые крупные заявки, отменённые < 2 секунд; сравнение заявок/исполнений.
- **Wash trading:** покупка и продажа между связанными аккаунтами; проверка IP/гильдий.
- **Pump & dump:** рост цены > 20% без новостей + связанная активность гильдий.
- **Quest leak:** сделки до того, как событие стало публичным; сопоставление времени квеста.

---

## 🌐 API & админ панели

| Endpoint | Метод | Назначение |
| --- | --- | --- |
| `/stocks/protection/alerts` | `GET` | Список активных алертов (фильтры) |
| `/stocks/protection/alerts/{id}` | `PATCH` | Обновить статус, добавить комментарий |
| `/stocks/protection/enforcement` | `POST` | Создать дисциплинарное действие |
| `/stocks/protection/enforcement/{id}` | `GET` | История действий по игроку |

Админ UI: дашборд подозрительных активностей, heatmap по тикерам.

---

## 📈 Мониторинг

- Метрики: `AlertRate`, `FalsePositiveRate`, `CircuitBreakerCount`, `AverageHaltDuration`.
- PagerDuty: высокое количество алертов за 5 мин → возможная атака.
- Логи: все manual overrides в `surveillance_audit`.

---

## 🔄 Интеграции

- `anti-cheat-system`: обмен данными о подозрительных аккаунтах.
- `guild-system`: блокировка гильдий за коллективные манипуляции.
- `economy-events`: исключение легитимных событий из сигналов.
- `notification-service`: уведомления администраторам и игрокам о санкциях.

---

## 🔗 Связанные документы

- `stock-trading.md`

---

## История изменений

- v1.1.0 (2025-11-07 16:19) - Добавлены БД, детекция манипуляций, REST API, мониторинг и интеграции
- v1.0.0 (2025-11-06 21:45) - Создание документа о защите

