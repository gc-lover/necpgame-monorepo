---

- **Status:** queued
- **Last Updated:** 2025-11-07 00:18
---


# Экономика - Контракты и сделки

**Статус:** approved  
**Версия:** 1.1.0  
**Дата создания:** 2025-11-06  
**Последнее обновление:** 2025-11-07 16:14  
**Приоритет:** средний (Post-MVP)

**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-09 03:22
**api-readiness-notes:** Перепроверено 2025-11-09 03:22: lifecycle контрактов, escrow и API остаются полными и готовы к задачам economy-service.

**target-domain:** economy  
**target-microservice:** economy-service (port 8085)  
**target-frontend-module:** modules/economy/contracts

---
**API Tasks Status:**
- Status: completed
- Tasks:
  - API-TASK-102: Economy Contracts API — `api/v1/economy/contracts/contracts.yaml`
    - Создано: 2025-11-09 18:40
    - Завершено: 2025-11-09 21:28
    - Доп. файлы: `contracts-models.yaml`, `contracts-models-operations.yaml`, `README.md`
    - Файл задачи: `API-SWAGGER/tasks/completed/2025-11-09/task-102-economy-contracts-api.md`
- Last Updated: 2025-11-09 21:28
---

---

## Краткое описание

Система контрактов между игроками для безопасных сделок.

**Микрофича:** Contracts (P2P, escrow, collateral, arbitration)

---

## 📝 Типы контрактов

### 1. Item Exchange Contract
**Обмен предметами**

```
Player A offers: Mantis Blades
Player B offers: 10,000 eddies

Contract terms:
- A gives Mantis Blades
- B gives 10,000 eddies
- Escrow: Both deposit
- Auto-execute on accept
```

### 2. Delivery Contract
**Доставка груза**

```
Client: Player A
Courier: Player B

Terms:
- Deliver 100 Health Boosters from NC to Tokyo
- Payment: 1,000 eddies
- Deadline: 24 hours
- Penalty: 50% if late
- Collateral: 500 eddies from courier
```

### 3. Crafting Contract
**Заказ на крафт**

```
Client: Player A
Crafter: Player B

Terms:
- Craft Legendary Rifle
- Materials: provided by client
- Payment: 5,000 eddies
- Deadline: 3 days
- Quality guaranteed
```

### 4. Service Contract
**Оказание услуг**

```
Client: Player A
Mercenary: Player B

Terms:
- Escort through Badlands (dangerous zone)
- Payment: 2,000 eddies
- Success bonus: +1,000 eddies
- Collateral: 1,000 eddies
```

---

## 🔒 Escrow System

**Механика:**
```
1. Contract created
2. Both parties deposit (escrow)
3. Terms fulfilled
4. Escrow released automatically
```

**Пример:**
```
Item Exchange:
Player A deposits: Mantis Blades (in escrow)
Player B deposits: 10,000 eddies (in escrow)

Both accept contract:
→ Auto-execute
→ A receives 10,000 eddies
→ B receives Mantis Blades

Escrow guarantees safety!
```

---

## 💰 Collateral (Залог)

**Зачем:**
- Guarantee исполнения
- Penalty за нарушение

**Пример:**
```
Delivery contract:
Collateral: 500 eddies (from courier)

Success: collateral returned
Failure: collateral lost
Late: partial collateral lost
```

---

## ⚖️ Dispute Resolution (Арбитраж)

**Если спор:**
```
1. Player raises dispute
2. Both sides present evidence
3. AI moderator reviews
4. Decision made (3-5 days)
5. Escrow distributed per decision
```

---

## 📊 Структура БД

```sql
CREATE TABLE player_contracts (
    id UUID PRIMARY KEY,
    
    contract_type VARCHAR(20) NOT NULL,
    
    creator_id UUID NOT NULL,
    contractor_id UUID NOT NULL,
    
    terms JSONB NOT NULL,
    
    escrow_creator JSONB,
    escrow_contractor JSONB,
    collateral DECIMAL(12,2) DEFAULT 0,
    
    status VARCHAR(20) DEFAULT 'PENDING',
    
    deadline TIMESTAMP,
    
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP,
    disputed BOOLEAN DEFAULT FALSE
);
```

### State machine

| State | Описание | Триггеры перехода |
| --- | --- | --- |
| `DRAFT` | Создан инициатором, контрагент ещё не приглашён | `invite_contractor()` |
| `NEGOTIATION` | Обе стороны редактируют условия | `propose_terms()` / `accept_terms()` |
| `ESCROW_PENDING` | Требуется внести залог/эскроу | `deposit_escrow()` завершено обеими сторонами |
| `ACTIVE` | Контракт исполняется | `mark_deliverable_submitted()` |
| `COMPLETED` | Условия закрыты, эскроу распределён | `confirm_completion()` обеими сторонами или арбитром |
| `CANCELLED` | Анулирован по взаимному согласию до выполнения | `cancel_contract()` |
| `DISPUTED` | Запущено разбирательство | `raise_dispute()` → `resolve_dispute()` ведёт к `COMPLETED`/`CANCELLED` |

---

## 🌐 API контракты (economy-service)

| Endpoint | Метод | Назначение | Основные поля |
| --- | --- | --- | --- |
| `/contracts` | `POST` | Создать контракт | `contractType`, `terms`, `collateral`, `deadline`, `invitedContractorId` |
| `/contracts/{id}` | `GET` | Получить детали контракта | включает state machine snapshot, escrow balances |
| `/contracts/{id}/proposals` | `POST` | Обновить условия во время переговоров | `proposalVersion`, `termsDelta`, `message` |
| `/contracts/{id}/accept` | `POST` | Принять актуальные условия | требует двухфакторного подтверждения |
| `/contracts/{id}/escrow/deposit` | `POST` | Внести залог / эскроу | `walletId`, `amount`, optional `items[]` |
| `/contracts/{id}/deliverables` | `POST` | Загрузить подтверждение исполнения | ссылки на инвентарь, трекинг доставки |
| `/contracts/{id}/complete` | `POST` | Подтвердить завершение | `rating`, `feedback` |
| `/contracts/{id}/dispute` | `POST` | Открыть спор | `reasonCode`, `evidenceUrls`, `comment` |
| `/contracts/{id}/timeline` | `GET` | Audit trail для арбитража | события, подписи, файловые ссылки |

**Integration events (Kafka / `economy.contracts.*`):**
- `created`, `proposal_submitted`, `escrow_locked`, `deliverable_submitted`, `completed`, `dispute_opened`, `dispute_resolved`.
- Подписчики: `inventory-service` (резервы предметов), `reputation-service` (оценки), `notification-service` (push/почта), `analytics-service` (экономический мониторинг).

---

## ✅ Валидации и лимиты

- **Eligibility:** аккаунт 30 lvl+, KYC-complete для контрактов с валютой > 5,000 eddies.
- **Collateral caps:** максимум 200% от стоимости сделки; система автоматически оценивает предметы через `valuation-service`.
- **Time limits:** активные negotiations истекают через 48 часов без активности; delivery контракт ≤ 7 дней.
- **Concurrency:** максимум 10 активных контрактов на аккаунт (конфигурируемо), 3 открытых спора одновременно.
- **Fraud checks:** античит сервис верифицирует повторяющиеся отмены, аномально высокие collateral, пересечение IP.

---

## 🔔 Уведомления и UX сигналы

- In-game HUD: баннер о новых предложениях / необходимости внести эскроу.
- Mobile push + e-mail: для изменений статуса (accept, escrow locked, dispute).
- Contract timeline доступен из social UI, поддерживает комментирование промежуточных шагов.

---

## 🔄 Интеграции и зависимости

- `inventory-service`: резерв предметов до завершения контракта.
- `wallet-service`: блокировка/возврат валюты в эскроу.
- `reputation-service`: рейтинг исполнителей, штраф за споры.
- `logistics-service`: трекинг поставок для delivery контрактов.
- `social-service`: уведомления, чаты по контракту.

---

## 🔗 Связанные документы

- `economy-overview.md`

---

## История изменений

- v1.1.0 (2025-11-07 16:14) - Добавлены lifecycle, API, интеграции, валидации, обновлены статусы/метаданные
- v1.0.0 (2025-11-06 22:00) - Создание документа о контрактах