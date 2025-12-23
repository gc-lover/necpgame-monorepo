<!-- Issue: #140890166 -->

# Economy Contracts and Deals System - Database Schema

## Обзор

Схема базы данных для системы контрактов и сделок, включающая контракты игроков, переговоры, эскроу, залоги, споры и лог
исполнения контрактов.

## ERD Диаграмма

```mermaid
erDiagram
    player_contracts ||--o{ contract_negotiations : "has"
    player_contracts ||--|| escrows : "has"
    player_contracts ||--o{ collaterals : "has"
    player_contracts ||--o| contract_disputes : "has"
    player_contracts ||--o{ contract_execution_log : "logs"
    character ||--o{ player_contracts : "initiates"
    character ||--o{ player_contracts : "receives"
    character ||--o{ contract_negotiations : "proposes"
    character ||--o{ collaterals : "provides"
    character ||--o{ contract_disputes : "initiates"

    player_contracts {
        uuid id PK
        contract_type contract_type
        uuid initiator_id FK
        uuid counterparty_id FK
        contract_status status
        jsonb terms
        jsonb initiator_assets
        jsonb counterparty_assets
        uuid escrow_id FK
        uuid initiator_collateral_id FK
        uuid counterparty_collateral_id FK
        timestamp deadline
        timestamp completed_at
        timestamp cancelled_at
        uuid dispute_id FK
        timestamp created_at
        timestamp updated_at
    }

    contract_negotiations {
        uuid id PK
        uuid contract_id FK
        jsonb proposal
        uuid proposer_id FK
        negotiation_status status
        timestamp created_at
        timestamp responded_at
    }

    escrows {
        uuid id PK
        uuid contract_id FK UNIQUE
        jsonb initiator_items
        jsonb counterparty_items
        decimal initiator_currency
        decimal counterparty_currency
        escrow_status status
        timestamp locked_at
        timestamp released_at
    }

    collaterals {
        uuid id PK
        uuid contract_id FK
        uuid player_id FK
        decimal amount
        decimal forfeited_amount
        collateral_status status
        timestamp locked_at
        timestamp released_at
    }

    contract_disputes {
        uuid id PK
        uuid contract_id FK UNIQUE
        uuid initiator_id FK
        text reason
        jsonb evidence
        jsonb ai_moderation_result
        dispute_decision decision
        jsonb escrow_distribution
        timestamp resolved_at
        timestamp created_at
    }

    contract_execution_log {
        uuid id PK
        uuid contract_id FK
        varchar action
        jsonb details
        timestamp executed_at
    }
```

## Описание таблиц

### player_contracts

Таблица контрактов игроков. Хранит информацию о контрактах между игроками.

**Ключевые поля:**

- `id`: UUID первичный ключ
- `contract_type`: Тип контракта (contract_type ENUM, NOT NULL)
- `initiator_id`: ID инициатора (FK к characters, NOT NULL)
- `counterparty_id`: ID контрагента (FK к characters, NOT NULL)
- `status`: Статус контракта (contract_status ENUM, NOT NULL, default: 'DRAFT')
- `terms`: Условия контракта (JSONB, default: {})
- `initiator_assets`: Активы инициатора (JSONB, default: {})
- `counterparty_assets`: Активы контрагента (JSONB, default: {})
- `escrow_id`: ID эскроу (FK к escrows, nullable)
- `initiator_collateral_id`: ID залога инициатора (FK к collaterals, nullable)
- `counterparty_collateral_id`: ID залога контрагента (FK к collaterals, nullable)
- `deadline`: Срок выполнения (TIMESTAMP, NOT NULL)
- `completed_at`: Время завершения (TIMESTAMP, nullable)
- `cancelled_at`: Время отмены (TIMESTAMP, nullable)
- `dispute_id`: ID спора (FK к contract_disputes, nullable)
- `created_at`: Время создания
- `updated_at`: Время последнего обновления

**Индексы:**

- По `(initiator_id, status)` для контрактов инициатора
- По `(counterparty_id, status)` для контрактов контрагента
- По `(contract_type, status)` для фильтрации по типу
- По `(status, deadline)` для активных контрактов (WHERE status IN ('ACTIVE', 'ESCROW_PENDING'))

**Constraints:**

- CHECK (initiator_id != counterparty_id): Инициатор и контрагент должны быть разными

### contract_negotiations

Таблица переговоров по контрактам. Хранит предложения и переговоры по контрактам.

**Ключевые поля:**

- `id`: UUID первичный ключ
- `contract_id`: ID контракта (FK к player_contracts, NOT NULL)
- `proposal`: Предложение по контракту (JSONB, default: {})
- `proposer_id`: ID предложившего (FK к characters, NOT NULL)
- `status`: Статус предложения (negotiation_status ENUM, NOT NULL, default: 'pending')
- `created_at`: Время создания
- `responded_at`: Время ответа (TIMESTAMP, nullable)

**Индексы:**

- По `(contract_id, status)` для предложений по контракту
- По `proposer_id` для предложений игрока

### escrows

Таблица эскроу для контрактов. Хранит информацию о заблокированных активах.

**Ключевые поля:**

- `id`: UUID первичный ключ
- `contract_id`: ID контракта (FK к player_contracts, NOT NULL, UNIQUE)
- `initiator_items`: Предметы инициатора (JSONB, default: [])
- `counterparty_items`: Предметы контрагента (JSONB, default: [])
- `initiator_currency`: Валюта инициатора (DECIMAL(20,2), NOT NULL, default: 0.00)
- `counterparty_currency`: Валюта контрагента (DECIMAL(20,2), NOT NULL, default: 0.00)
- `status`: Статус эскроу (escrow_status ENUM, NOT NULL, default: 'locked')
- `locked_at`: Время блокировки
- `released_at`: Время освобождения (TIMESTAMP, nullable)

**Индексы:**

- По `contract_id` для эскроу контракта
- По `status` для фильтрации по статусу

**Constraints:**

- UNIQUE(contract_id): Один эскроу на контракт

### collaterals

Таблица залогов для контрактов. Хранит информацию о залогах сторон.

**Ключевые поля:**

- `id`: UUID первичный ключ
- `contract_id`: ID контракта (FK к player_contracts, NOT NULL)
- `player_id`: ID игрока (FK к characters, NOT NULL)
- `amount`: Сумма залога (DECIMAL(20,2), NOT NULL, default: 0.00)
- `forfeited_amount`: Сумма удержанного залога (DECIMAL(20,2), NOT NULL, default: 0.00)
- `status`: Статус залога (collateral_status ENUM, NOT NULL, default: 'locked')
- `locked_at`: Время блокировки
- `released_at`: Время освобождения (TIMESTAMP, nullable)

**Индексы:**

- По `contract_id` для залогов контракта
- По `(player_id, status)` для залогов игрока

### contract_disputes

Таблица споров по контрактам. Хранит информацию о спорах и арбитраже.

**Ключевые поля:**

- `id`: UUID первичный ключ
- `contract_id`: ID контракта (FK к player_contracts, NOT NULL, UNIQUE)
- `initiator_id`: ID инициатора спора (FK к characters, NOT NULL)
- `reason`: Причина спора (TEXT, NOT NULL)
- `evidence`: Доказательства (JSONB, default: {})
- `ai_moderation_result`: Результат AI-модерации (JSONB, nullable)
- `decision`: Решение по спору (dispute_decision ENUM, NOT NULL, default: 'pending')
- `escrow_distribution`: Распределение эскроу (JSONB, nullable)
- `resolved_at`: Время разрешения (TIMESTAMP, nullable)
- `created_at`: Время создания

**Индексы:**

- По `contract_id` для спора контракта
- По `initiator_id` для споров инициатора
- По `(decision, resolved_at)` для разрешенных споров (WHERE decision != 'pending')

**Constraints:**

- UNIQUE(contract_id): Один спор на контракт

### contract_execution_log

Таблица лога исполнения контрактов. Хранит историю действий по контрактам.

**Ключевые поля:**

- `id`: UUID первичный ключ
- `contract_id`: ID контракта (FK к player_contracts, NOT NULL)
- `action`: Действие (VARCHAR(255), NOT NULL)
- `details`: Детали действия (JSONB, default: {})
- `executed_at`: Время выполнения

**Индексы:**

- По `(contract_id, executed_at DESC)` для лога контракта
- По `executed_at DESC` для временных запросов

## ENUM типы

### contract_type

- `item_exchange`: Обмен предметами
- `delivery`: Доставка
- `crafting`: Крафт
- `service`: Сервис

### contract_status

- `DRAFT`: Черновик
- `NEGOTIATION`: Переговоры
- `ESCROW_PENDING`: Ожидание эскроу
- `ACTIVE`: Активен
- `COMPLETED`: Завершен
- `CANCELLED`: Отменен
- `DISPUTED`: Спор

### negotiation_status

- `pending`: Ожидает ответа
- `accepted`: Принято
- `rejected`: Отклонено

### escrow_status

- `locked`: Заблокировано
- `released`: Освобождено
- `distributed`: Распределено

### collateral_status

- `locked`: Заблокировано
- `released`: Освобождено
- `forfeited`: Удержано

### dispute_decision

- `pending`: Ожидает решения
- `initiator_wins`: Победа инициатора
- `counterparty_wins`: Победа контрагента
- `partial`: Частичное решение

## Constraints и валидация

### CHECK Constraints

- `player_contracts.initiator_id != counterparty_id`: Инициатор и контрагент должны быть разными

### Foreign Keys

- `player_contracts.initiator_id` → `mvp_core.character.id` (ON DELETE CASCADE)
- `player_contracts.counterparty_id` → `mvp_core.character.id` (ON DELETE CASCADE)
- `player_contracts.escrow_id` → `economy.escrows.id` (ON DELETE SET NULL)
- `player_contracts.initiator_collateral_id` → `economy.collaterals.id` (ON DELETE SET NULL)
- `player_contracts.counterparty_collateral_id` → `economy.collaterals.id` (ON DELETE SET NULL)
- `player_contracts.dispute_id` → `economy.contract_disputes.id` (ON DELETE SET NULL)
- `contract_negotiations.contract_id` → `economy.player_contracts.id` (ON DELETE CASCADE)
- `contract_negotiations.proposer_id` → `mvp_core.character.id` (ON DELETE CASCADE)
- `escrows.contract_id` → `economy.player_contracts.id` (ON DELETE CASCADE)
- `collaterals.contract_id` → `economy.player_contracts.id` (ON DELETE CASCADE)
- `collaterals.player_id` → `mvp_core.character.id` (ON DELETE CASCADE)
- `contract_disputes.contract_id` → `economy.player_contracts.id` (ON DELETE CASCADE)
- `contract_disputes.initiator_id` → `mvp_core.character.id` (ON DELETE CASCADE)
- `contract_execution_log.contract_id` → `economy.player_contracts.id` (ON DELETE CASCADE)

### Unique Constraints

- `escrows(contract_id)`: Один эскроу на контракт
- `contract_disputes(contract_id)`: Один спор на контракт

## Оптимизация запросов

### Частые запросы

1. **Получение активных контрактов игрока:**
   ```sql
   SELECT * FROM economy.player_contracts 
   WHERE (initiator_id = $1 OR counterparty_id = $1) 
   AND status IN ('ACTIVE', 'ESCROW_PENDING') 
   ORDER BY deadline ASC;
   ```
   Использует индексы `(initiator_id, status)` и `(counterparty_id, status)`.

2. **Получение переговоров по контракту:**
   ```sql
   SELECT * FROM economy.contract_negotiations 
   WHERE contract_id = $1 AND status = 'pending' 
   ORDER BY created_at DESC;
   ```
   Использует индекс `(contract_id, status)`.

3. **Получение эскроу контракта:**
   ```sql
   SELECT * FROM economy.escrows 
   WHERE contract_id = $1;
   ```
   Использует индекс `contract_id`.

4. **Получение залогов контракта:**
   ```sql
   SELECT * FROM economy.collaterals 
   WHERE contract_id = $1;
   ```
   Использует индекс `contract_id`.

5. **Получение спора контракта:**
   ```sql
   SELECT * FROM economy.contract_disputes 
   WHERE contract_id = $1;
   ```
   Использует индекс `contract_id`.

6. **Получение лога исполнения контракта:**
   ```sql
   SELECT * FROM economy.contract_execution_log 
   WHERE contract_id = $1 
   ORDER BY executed_at DESC 
   LIMIT 100;
   ```
   Использует индекс `(contract_id, executed_at DESC)`.

## Миграции

### Применение миграций:

```bash
liquibase update --changelog-file=infrastructure/liquibase/changelog.yaml
```

## Соответствие архитектуре

Схема БД полностью соответствует архитектуре из
`knowledge/implementation/architecture/economy-contracts-system-architecture.yaml`:

- [OK] Все таблицы из архитектуры созданы
- [OK] Все поля соответствуют описанию
- [OK] Индексы оптимизированы для частых запросов
- [OK] Constraints обеспечивают целостность данных
- [OK] Foreign Keys настроены с CASCADE/SET NULL для автоматической очистки
- [OK] Интеграция с существующими таблицами (characters)

## Особенности реализации

### Контракты

Система контрактов включает:

- **Типы контрактов**: item_exchange, delivery, crafting, service
- **Жизненный цикл**: DRAFT → NEGOTIATION → ESCROW_PENDING → ACTIVE → COMPLETED/CANCELLED/DISPUTED
- **Условия**: terms (JSONB) для гибких условий контракта
- **Активы**: initiator_assets и counterparty_assets (JSONB) для предметов и валюты
- **Сроки**: deadline для контроля сроков выполнения
- **Связи**: escrow_id, collateral_id, dispute_id для интеграции с другими таблицами

### Переговоры

Система переговоров включает:

- **Предложения**: proposal (JSONB) для гибких предложений
- **Статусы**: pending, accepted, rejected для управления предложениями
- **Временные метки**: created_at и responded_at для отслеживания времени

### Эскроу

Система эскроу включает:

- **Предметы**: initiator_items и counterparty_items (JSONB массивы) для блокировки предметов
- **Валюта**: initiator_currency и counterparty_currency для блокировки валюты
- **Статусы**: locked, released, distributed для управления эскроу
- **Временные метки**: locked_at и released_at для отслеживания времени

### Залоги

Система залогов включает:

- **Суммы**: amount для суммы залога, forfeited_amount для удержанной суммы
- **Статусы**: locked, released, forfeited для управления залогами
- **Временные метки**: locked_at и released_at для отслеживания времени

### Споры

Система споров включает:

- **Причины**: reason для описания причины спора
- **Доказательства**: evidence (JSONB) для сбора доказательств
- **AI-модерация**: ai_moderation_result (JSONB) для результатов AI-модерации
- **Решения**: decision для решения по спору (pending, initiator_wins, counterparty_wins, partial)
- **Распределение**: escrow_distribution (JSONB) для распределения эскроу согласно решению

### Лог исполнения

Система лога включает:

- **Действия**: action для описания действия
- **Детали**: details (JSONB) для деталей действия
- **Временные метки**: executed_at для времени выполнения

### Интеграция с другими системами

Система контрактов интегрируется с:

- **Characters**: через initiator_id, counterparty_id для участников контрактов
- **Inventory Service**: через initiator_items и counterparty_items для блокировки предметов
- **Wallet Service**: через initiator_currency и counterparty_currency для блокировки валюты
- **Reputation Service**: через события для обновления репутации
- **Logistics Service**: через contract_type 'delivery' для доставки
- **Crafting Service**: через contract_type 'crafting' для крафта
- **Notification Service**: через события для уведомлений

