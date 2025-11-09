# Биржа акций - Система дивидендов

**Статус:** approved  
**Версия:** 1.1.0  
**Дата создания:** 2025-11-06  
**Последнее обновление:** 2025-11-07 16:19  
**Приоритет:** высокий (Post-MVP)

**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-09 11:21
**api-readiness-notes:** Перепроверено 2025-11-09 11:21: расписания, DRIP, налоги и REST/событийные интерфейсы остаются консистентными, блокеров нет.

---

## Краткое описание

Система выплаты дивидендов акционерам в NECPGAME.

**Микрофича:** Дивиденды (расчет, выплата, реинвестирование)

---

## 💰 Что такое дивиденды?

**Дивиденд (Dividend)** — выплата части прибыли корпорации владельцам акций.

**Зачем:**
- Пассивный доход для игроков
- Incentive держать акции долгосрочно
- Reward за investment

---

## 📅 Частота выплат

### Quarterly Dividends (Ежеквартальные)

**Для большинства корпораций:**
```
Q1: January - March   → Payout: April 1
Q2: April - June      → Payout: July 1
Q3: July - September  → Payout: October 1
Q4: October - December → Payout: January 1
```

**Пример:**
```
Arasaka Corporation (ARSK)
Quarterly dividend: 10 eddies per share

Player owns: 100 shares
Receives: 1,000 eddies каждый квартал
Annual: 4,000 eddies (4 × 1,000)
```

### Annual Dividends (Ежегодные)

**Для некоторых корпораций:**
```
Payout: January 1 (yearly)

Example: SovOil (SVOL)
Annual dividend: 25 eddies per share

Player owns: 50 shares
Receives: 1,250 eddies once per year
```

---

## 💵 Расчет дивидендов

### Formula

```
dividend_per_share = (corporate_profit × payout_ratio) / shares_outstanding

Где:
- corporate_profit: прибыль корпорации за период
- payout_ratio: % прибыли на дивиденды (обычно 40-60%)
- shares_outstanding: общее количество акций
```

### Пример расчета

```
Arasaka Q4 2025:
Profit: 25 billion eddies
Payout ratio: 40%
Shares outstanding: 2.5 billion

Dividend per share:
(25B × 0.40) / 2.5B = 10B / 2.5B = 4 eddies

Player owns 100 shares:
Receives: 100 × 4 = 400 eddies (Q4 dividend)
```

### Типы дивидендов

**Fixed Dividend (Фиксированный):**
- Для Preferred Stock
- Не зависит от прибыли
- Гарантированный

```
Arasaka Preferred (ARSK-P)
Fixed dividend: 15 eddies per share (quarterly)
Guaranteed даже если profit падает
```

**Variable Dividend (Переменный):**
- Для Common Stock
- Зависит от прибыли
- Может меняться каждый квартал

```
Arasaka Common (ARSK)
Q1: 10 eddies (profit was good)
Q2: 12 eddies (profit increased!)
Q3: 8 eddies (profit decreased)
Q4: 10 eddies (back to normal)
```

---

## 📆 Важные даты

### Ex-Dividend Date

**Определение:** Последний день для покупки акций чтобы получить дивиденды

**Механика:**
```
Dividend timeline:
- Declaration Date: Dec 1 (объявлено)
- Ex-Dividend Date: Dec 15 (граница)
- Record Date: Dec 17 (фиксация владельцев)
- Payment Date: Jan 1 (выплата)

Правило:
Buy before Dec 15 → GET dividend ✅
Buy on/after Dec 15 → NO dividend ❌
```

**Пример:**
```
Arasaka declares dividend: 10 eddies (Dec 1)
Ex-dividend: Dec 15

Player A buys 100 ARSK on Dec 14:
→ Eligible for dividend ✅
→ Receives: 1,000 eddies on Jan 1

Player B buys 100 ARSK on Dec 15:
→ NOT eligible ❌
→ Next dividend: April 1 (Q1)

Price behavior:
Dec 14: 1,000 eddies (includes dividend value)
Dec 15: 990 eddies (drops by ~dividend amount)
```

---

## 💸 Выплата дивидендов

### Механика выплаты

**Automatic payout:**
```
Payment Date: January 1, 09:00 AM

System calculates:
- Who owns shares on Record Date
- How many shares each player owns
- Total dividend per player

Automatic transfer:
→ Dividends добавляются в wallet
→ Notification: "You received 4,000 eddies dividend from ARSK"
→ Mail: "Dividend Payment Receipt"
```

**UI:**
```
┌─────────────────────────────────────┐
│ 💰 DIVIDEND RECEIVED                │
├─────────────────────────────────────┤
│                                     │
│ Arasaka Corporation (ARSK)          │
│ Q4 2025 Dividend                    │
│                                     │
│ Your shares: 100                    │
│ Dividend per share: 10 eddies       │
│ ─────────────────────────────────── │
│ Total received: 1,000 eddies        │
│                                     │
│ Paid to your wallet ✓               │
│                                     │
│ [View Portfolio] [Reinvest]         │
└─────────────────────────────────────┘
```

---

## 🔄 Реинвестирование дивидендов (DRIP)

### DRIP (Dividend Reinvestment Plan)

**Описание:** Автоматическая покупка акций на дивиденды

**Механика:**
```
Player enables DRIP for ARSK:

Quarterly dividend: 1,000 eddies
ARSK price: 1,000 eddies

→ System automatically buys: 1 share
→ Fractional shares: 0 (округление вниз)
→ Remaining: 0 eddies (недостаточно для еще одной)

Next quarter:
Owns 101 shares now
Dividend: 101 × 10 = 1,010 eddies
→ Buys 1 more share
→ Owns 102 shares

Compounding effect! 📈
```

**Настройки:**
```
DRIP Options:
☑ Reinvest all dividends (automatic)
☐ Reinvest only ARSK dividends
☐ Reinvest only if price < 1,050 (conditional)
☐ Disable (receive cash)
```

---

## 📊 Yield расчет

### Dividend Yield

**Formula:**
```
yield = (annual_dividend / current_price) × 100%
```

**Примеры:**
```
Arasaka (ARSK):
Price: 1,000 eddies
Annual dividend: 40 eddies
Yield: 40 / 1,000 = 4.0%

SovOil (SVOL):
Price: 380 eddies
Annual dividend: 25 eddies
Yield: 25 / 380 = 6.6% (high yield!)

Tsunami (TSUN):
Price: 150 eddies
Annual dividend: 0 eddies
Yield: 0% (growth stock)
```

### Yield vs Growth

**High Yield Stocks:**
- Mature companies
- Стабильный бизнес
- Медленный рост цены
- Для passive income

**Growth Stocks:**
- Молодые companies
- Быстрый рост
- Нет или низкие дивиденды
- Для capital appreciation

---

## 📊 Структура данных

### Dividend Schedules

```sql
CREATE TABLE dividend_schedules (
    id SERIAL PRIMARY KEY,
    corporation_id VARCHAR(100) NOT NULL,
    
    -- Schedule
    frequency VARCHAR(20) NOT NULL, -- "QUARTERLY", "ANNUAL"
    dividend_per_share DECIMAL(12,2) NOT NULL,
    
    -- Dates
    declaration_date DATE NOT NULL,
    ex_dividend_date DATE NOT NULL,
    record_date DATE NOT NULL,
    payment_date DATE NOT NULL,
    
    -- Type
    dividend_type VARCHAR(20) NOT NULL, -- "CASH", "STOCK" (for future)
    is_special BOOLEAN DEFAULT FALSE, -- One-time special dividend
    
    -- Status
    status VARCHAR(20) NOT NULL DEFAULT 'DECLARED', -- DECLARED, PAID, CANCELLED
    
    -- Meta
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_dividend_corporation FOREIGN KEY (corporation_id) REFERENCES stock_corporations(id) ON DELETE CASCADE
);

CREATE INDEX idx_dividend_schedules_corporation ON dividend_schedules(corporation_id);
CREATE INDEX idx_dividend_schedules_payment ON dividend_schedules(payment_date);
CREATE INDEX idx_dividend_schedules_status ON dividend_schedules(status);
```

### Dividend Payments

```sql
CREATE TABLE dividend_payments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    
    -- Dividend
    schedule_id INTEGER NOT NULL,
    corporation_id VARCHAR(100) NOT NULL,
    
    -- Player
    player_id UUID NOT NULL,
    shares_owned INTEGER NOT NULL,
    dividend_per_share DECIMAL(12,2) NOT NULL,
    total_amount DECIMAL(12,2) NOT NULL,
    
    -- DRIP
    reinvested BOOLEAN DEFAULT FALSE,
    shares_purchased INTEGER DEFAULT 0,
    
    -- Time
    paid_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_payment_schedule FOREIGN KEY (schedule_id) REFERENCES dividend_schedules(id),
    CONSTRAINT fk_payment_corporation FOREIGN KEY (corporation_id) REFERENCES stock_corporations(id),
    CONSTRAINT fk_payment_player FOREIGN KEY (player_id) REFERENCES characters(id) ON DELETE CASCADE
);

CREATE INDEX idx_dividend_payments_player ON dividend_payments(player_id, paid_at DESC);
CREATE INDEX idx_dividend_payments_corporation ON dividend_payments(corporation_id, paid_at DESC);
```

---

## 📆 Lifecycle выплаты

| Этап | Описание | Сервис |
| --- | --- | --- |
| `DECLARED` | Корпорация объявляет дивиденд | `corporate-management-service` |
| `SCHEDULED` | Создан график, настроены даты | `dividend-service` |
| `NOTICE_SENT` | Игрокам отправлено уведомление | `notification-service` |
| `EX_DIVIDEND` | Фиксация владельцев | `dividend-snapshot job` |
| `PAID` | Выплата завершена / DRIP исполнен | `dividend-service` + `wallet-service` |
| `RECONCILED` | Подтверждены суммы, обновлена отчётность | `tax-service` |

---

## 🌐 API дивидендов

| Endpoint | Метод | Назначение |
| --- | --- | --- |
| `/stocks/dividends/schedule` | `GET` | Расписание и статусы выплат |
| `/stocks/dividends/history` | `GET` | История выплат игрока |
| `/stocks/dividends/{scheduleId}` | `GET` | Детали конкретной выплаты |
| `/stocks/dividends/{scheduleId}/drip` | `POST` | Настройки DRIP (вкл/условия) |
| `/stocks/admin/dividends` | `POST` | Админ создание расписания |
| `/stocks/admin/dividends/{id}` | `PATCH` | Изменение payout ratio, дат |

**Event bus (`economy.dividends.*`):** `declared`, `snapshot_completed`, `payout_started`, `payout_completed`, `drip_executed`, `tax_withheld`.

---

## ⚖️ Налоги и комплаенс

- Удержание налогов по ставке региона игрока (10-25%), выводится отдельной строкой в логе.
- Налоговые отчёты генерируются ежеквартально (`tax-service` → `/tax/reports/dividends`).
- Проверка мульти-аккаунтов: сопоставление IP/устройств, ограничения на получение бонусных дивидендов.

---

## 📈 Аналитика и UI

- Yield chart: динамика доходности по корпорациям, сравнение с индексами.
- DRIP калькулятор: прогноз роста позиции при реинвестировании.
- Notifications: HUD + мобильные push при объявлении, ex-date, выплате.

---

## 🔄 Интеграции

- `wallet-service`: кредитование средств, возврат при отмене.
- `analytics-service`: статистика выплат для корпораций и игроков.
- `stock-exchange-overview`: обновление метрик доходности.
- `economy-events`: корректировка payout ratio при кризисах.

---

## 🎯 Примеры дивидендов

### Пример 1: Regular Quarterly

```
Arasaka (ARSK) - Q4 2025 Dividend

Declaration: December 1
Ex-Dividend: December 15
Record: December 17
Payment: January 1

Dividend: 10 eddies per share

Player owns 100 shares (bought Nov 1):
→ Eligible ✅ (owned before ex-dividend)
→ Receives: 1,000 eddies on January 1
→ Notification sent
```

### Пример 2: Special Dividend

```
Militech (MLTC) - Special Dividend

Reason: Won Corporate War, huge profit

Regular quarterly: 9 eddies
Special dividend: 15 eddies (one-time!)
Total: 24 eddies per share

Player owns 80 shares:
Regular: 720 eddies
Special: 1,200 eddies
Total: 1,920 eddies (huge payout!)
```

### Пример 3: DRIP Compounding

```
Year 1:
Owns: 100 ARSK @ 1,000
Dividends: 40 eddies/year
DRIP enabled

Q1: 10 eddies × 100 = 1,000 → buy 1 share (now 101)
Q2: 10 eddies × 101 = 1,010 → buy 1 share (now 102)
Q3: 10 eddies × 102 = 1,020 → buy 1 share (now 103)
Q4: 10 eddies × 103 = 1,030 → buy 1 share (now 104)

Year 2:
Owns: 104 shares (started with 100!)
Dividends: 40 × 1.04 = 41.6 per share effect

Compounding каждый квартал!
After 10 years: ~148 shares (from 100)
```

---

## 🔗 Связанные документы

- `stock-exchange-overview.md` - Обзор
- `stock-corporations.md` - Корпорации
- `stock-trading.md` - Торговля

---

## История изменений

- v1.1.0 (2025-11-07 16:19) - Добавлены lifecycle выплат, REST API, налоги, аналитика и интеграции
- v1.0.0 (2025-11-06 21:45) - Создание документа о дивидендах, расчеты и календарь
  - Описана система DRIP (реинвестирование)
  - Описана структура БД (2 таблицы)

