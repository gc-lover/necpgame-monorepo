# Биржа акций - Интеграция с геймплеем

**Статус:** approved  
**Версия:** 1.1.0  
**Дата создания:** 2025-11-06  
**Последнее обновление:** 2025-11-07 16:19  
**Приоритет:** высокий (Post-MVP)

**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-09 11:21
**api-readiness-notes:** Перепроверено 2025-11-09 11:21: интеграция с событиями, квестами, фракциями и шинами данных остаётся полной, блокеров нет.

---

## Краткое описание

Интеграция биржи акций с другими игровыми системами.

**Микрофича:** Квесты → Акции, Фракции → Акции, События → Акции

---

## 🎮 Квестовая интеграция

### Quest Outcomes → Stock Prices

**Примеры квестов:**

**1. "Corporate Espionage"**
```
Quest chain: Steal Militech secrets for Arasaka

Outcomes:
Success (Arasaka gets secrets):
→ ARSK: +10% (advantage gained)
→ MLTC: -8% (secrets stolen)

Failure (caught):
→ ARSK: -5% (scandal)
→ MLTC: +3% (defended)

Betray both (sell to Kang Tao):
→ ARSK: -12%
→ MLTC: -12%
→ KANG: +15%
```

**2. "Biotechnica Sabotage"**
```
Quest: Destroy Biotechnica lab

Before quest:
BIOT: 480 eddies

After quest:
BIOT: 336 eddies (-30%)

Recovery:
Week 1: 360 (+7% recovery)
Week 2: 384 (+7%)
Week 3: 410 (+7%)
Final: 432 eddies (-10% permanent)
```

### Investment Quests

**"Stock Market Tutorial"**
- Buy first stock
- Receive first dividend
- Sell for profit
- Reward: 1,000 eddies + broker fee discount

**"Insider Information"** (grey quest)
- NPC gives tip about upcoming event
- Player can act on info
- Risk: Insider trading detection
- Reward: Potential huge profit OR ban

---

## 🏢 Фракционная интеграция

### Faction Wars → Stocks

```
Corporate War: Arasaka vs Militech

Player chooses: Arasaka

Arasaka wins:
→ ARSK: +30%
→ Player's ARSK holdings: profit!

Militech wins:
→ MLTC: +30%
→ Player's choice was wrong, missed profit
```

### Reputation Benefits

```
High reputation with Arasaka:
- Access to preferred stock (ARSK-P)
- Insider tips (legal info)
- Broker fee discount (-10%)
```

---

## 🌍 World Events → Stocks

```
Global Event: "Energy Crisis"
→ PTRC: +30%
→ SVOL: +35%
→ All others: -5%

Global Event: "AI Breakthrough"
→ All tech stocks: +15%

Global Event: "War"
→ Defense stocks: +20%
→ Others: -10%
```

---

## 🧩 Событийная шина

- **Topic:** `economy.integration.events`
- Payload содержит: `eventId`, `eventType`, `severity`, `affectedEntities`, `timestamp`.
- Подписчики: `stock-events`, `currency-exchange`, `logistics`, `guild-system`.

---

## 🗄️ Мэппинг событий

```sql
CREATE TABLE event_stock_mapping (
    event_type VARCHAR(64) NOT NULL,
    event_subtype VARCHAR(64),
    corporation_id VARCHAR(100) NOT NULL,
    base_impact_percent DECIMAL(6,2) NOT NULL,
    metadata JSONB,
    PRIMARY KEY (event_type, event_subtype, corporation_id)
);
```

- Управляется дизайнерами через админ UI.
- Поддерживает приоритеты и overrides (например, временное отключение влияния).

---

## 🌐 API интеграций

| Endpoint | Метод | Назначение |
| --- | --- | --- |
| `/stocks/integration/event-hooks` | `POST` | Регистрация источника событий (квест/фракция) |
| `/stocks/integration/event-preview` | `POST` | Просмотр ожидаемого эффекта (what-if) |
| `/stocks/integration/event-override` | `PATCH` | Тонкая настройка impact (админ) |
| `/stocks/integration/journal` | `GET` | Журнал взаимосвязей событий и цен |

---

## 🔄 Связанные системы

- `quest-service`: передаёт outcome через webhooks.
- `faction-service`: статусы войн, территорий.
- `economy-events`: глобальные макро события.
- `news-feed`: внутриигровые новости о корпорациях.

---

## 🔗 Связанные документы

- `stock-events.md` - Детали влияния событий
- `../../../04-narrative/quest-system.md` - Квесты

---

## История изменений

- v1.1.0 (2025-11-07 16:19) - Добавлены шина событий, API и мэппинг, расширен список интеграций
- v1.0.0 (2025-11-06 21:45) - Создание документа об интеграции

