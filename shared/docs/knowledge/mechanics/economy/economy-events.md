---

- **Status:** queued
- **Last Updated:** 2025-11-07 00:18
---


# Экономика - Экономические события

**Статус:** approved  
**Версия:** 1.1.0  
**Дата создания:** 2025-11-06  
**Последнее обновление:** 2025-11-07 16:19  
**Приоритет:** средний (Post-MVP)

**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-09 03:32
**api-readiness-notes:** Перепроверено 2025-11-09 03:32; lifecycle событий, планировщики, REST/WS API и мониторинг остаются полными, блокеров для economy-service нет.

**target-domain:** economy  
**target-microservice:** economy-service (port 8085)  
**target-frontend-module:** modules/economy/events

---
**API Tasks Status:**
- Status: completed
- Tasks:
  - API-TASK-104: Economic Events API — `api/v1/economy/events/economic-events/economic-events.yaml`
    - Создано: 2025-11-09 19:12
    - Завершено: 2025-11-09 22:02
    - Доп. файлы: `economic-events-models.yaml`, `economic-events-models-operations.yaml`, `README.md`
    - Файл задачи: `API-SWAGGER/tasks/completed/2025-11-09/task-104-economy-events-api.md`
- Last Updated: 2025-11-09 22:02
---

---

## Краткое описание

Экономические события, влияющие на цены, валюты, рынки.

**Микрофича:** Economic events (кризисы, бум, инфляция, эмбарго)

---

## 📉 Типы событий

### 1. Economic Crisis (Кризис)

**Trigger:** Quest outcome, random event  
**Effect:** Все цены -10-20%

```
Event: "Night City Economic Crisis"
Duration: 2 weeks

Effects:
- All item prices: -15%
- Stock market: -20%
- Currency: EDDY weakens -10%
- Unemployment: +15%

Player impact:
- Goods cheaper (good for buying!)
- Stocks cheaper (buy opportunity!)
- Salary/income lower
```

### 2. Economic Boom (Бум)

**Trigger:** Victory in war, tech breakthrough  
**Effect:** Все цены +10-20%

```
Event: "Corporate War Victory Boom"
Duration: 1 month

Effects:
- All item prices: +15%
- Stock market: +25%
- Currency: EDDY strengthens +10%
- Employment: +20%
```

### 3. Inflation (Инфляция)

**Trigger:** Too much money in economy  
**Effect:** Цены растут постепенно

```
Event: "High Inflation Period"
Duration: 3 months

Effects:
- Prices increase 1% per week
- Total: +12% over 3 months
- Salaries increase slower (+8%)
- Real purchasing power decreases
```

### 4. Trade Embargo (Торговое эмбарго)

**Trigger:** Faction war, political event  
**Effect:** Ограничение торговли

```
Event: "Embargo on Soviet Goods"
Duration: Until war ends

Effects:
- No trade with Soviet regions
- Soviet goods price +50% (scarcity)
- Alternative suppliers +20% (demand)
```

### 5. Sanctions (Санкции)

**Trigger:** Political events  
**Effect:** Ограничения на корпорации

```
Event: "Sanctions on Arasaka"

Effects:
- ARSK stock: -30%
- Arasaka goods +25% (harder to get)
- Competitor stocks +10%
```

### 6. Tariffs (Тарифы)

**Trigger:** Political decisions  
**Effect:** Импортные налоги

```
Event: "Import Tariffs on Asian Goods"

Effects:
- Asian goods +15% (tariff added)
- Local alternatives +5% (demand shift)
- Asian stocks -8%
```

### 7. Corporate Scandals

**См:** `stock-exchange/stock-events.md`

### 8. Technological Breakthroughs

**См:** `stock-exchange/stock-events.md`

---

## 📊 Структура данных

```sql
CREATE TABLE economic_events (
    id UUID PRIMARY KEY,
    
    event_type VARCHAR(50) NOT NULL,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP,
    
    effects JSONB NOT NULL, -- Price modifiers, restrictions
    affected_regions JSONB,
    affected_sectors JSONB,
    
    severity VARCHAR(20), -- "MINOR", "MAJOR", "SEVERE"
    
    is_active BOOLEAN DEFAULT TRUE,
    
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```

### Event lifecycle

| Stage | Описание | Источник |
| --- | --- | --- |
| `PLANNED` | Событие создано (ручное или из симуляции), ожидает окна запуска | Scheduler (`event_planner`) |
| `ANNOUNCED` | Игрокам показан тизер, активируются пред-эффекты | `POST /economy/events/{id}/announce` |
| `ACTIVE` | Эффекты применены к ценам / рынкам | Scheduler по `start_date` |
| `COOLDOWN` | Пост-эффекты (возврат цен, компенсации) | `event_engine` |
| `ARCHIVED` | Событие завершено, доступна аналитика | `PATCH /economy/events/{id}` |

---

## 🛠️ Планирование и триггеры

- **Sources:** квестовые результаты, симуляция макроэкономики, ручной запуск геймдизайнера, cron-кампании.
- **Scheduler:** сервис `economic-event-scheduler` (Quartz) обрабатывает `start_date`, `end_date`, `warning_time`.
- **Stackable:** одновременно активны ≤ 3 глобальных события и ≤ 5 региональных.
- **Cool-down:** минимальный перерыв между событиями одного типа — 7 дней (конфигурируемо).

---

## 🌐 API (economy-service)

| Endpoint | Метод | Назначение | Примечания |
| --- | --- | --- | --- |
| `/economy/events` | `GET` | Получить список активных/предстоящих событий | фильтры `status`, `region`, `sector` |
| `/economy/events/{id}` | `GET` | Детали события, применённые эффекты | включает историю изменений |
| `/economy/events` | `POST` | Создать событие (геймдизайн) | требует `economy_admin` роли |
| `/economy/events/{id}` | `PATCH` | Обновить даты/эффекты | под подписью двух админов |
| `/economy/events/{id}/announce` | `POST` | Публиковать анонс игрокам | запускает уведомления |
| `/economy/events/{id}/cancel` | `POST` | Аварийно отменить | откатывает эффекты, пишет в аудит |
| `/economy/events/feed` | `WS` | Реал-тайм поток изменений | для UI/аналитики |

**Event bus (`economy.events.*`):** `created`, `announced`, `activated`, `effect_applied`, `effect_rolled_back`, `archived`.

---

## ✅ Контроль и мониторинг

- **Metrics:** `PriceDeviation%`, `PlayerSentiment`, `TransactionVolume`, `EventUptime`.
- **Alerting:** PagerDuty при отклонении цен > прогноз +10% или при провале отката эффектов.
- **Audit trail:** каждая правка события фиксируется в `economic_event_audit` с подписью пользователя.

---

## 🔔 Коммуникация с игроками

- UI баннер в экономическом модуле + рассылка для игроков, вложенных в затронутый сектор.
- Push/Email за `warning_time` до старта, повтор при активации и завершении.
- Встроенные советы (какие товары выгодно скупать, какие зоны избегать).

---

## 🔄 Интеграции

- `pricing-engine`: изменение базовых цен и расчёт скидок/надбавок.
- `stock-exchange`: автоматические коррекции индексов и заморозка торгов при severe событиях.
- `currency-exchange`: корректировка курсов и расширение спредов.
- `quest-service`: события, запускаемые сюжетными ветками.
- `analytics-service`: отчёты для экономистов и гильдий.

---

---

## 🔗 Связанные документы

- `stock-exchange/stock-events.md` - События акций
- `economy-world-impact.md` - Влияние на мир

---

## История изменений

- v1.1.0 (2025-11-07 16:19) - Добавлены lifecycle, планировщик, REST/WS API, мониторинг, коммуникации и интеграции
- v1.0.0 (2025-11-06 22:00) - Создание документа об экономических событиях