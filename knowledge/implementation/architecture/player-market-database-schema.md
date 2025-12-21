<!-- Issue: #140876083 -->
# Player Market - Database Schema

## Обзор

Схема базы данных для Player Market, включающая историю сделок, отзывы продавцов, статистику продавцов, избранных продавцов и подписки. Интегрируется с системой market_listings для обеспечения полного цикла торговли.

## ERD Диаграмма

```mermaid
erDiagram
    market_listings ||--o{ market_trade_history : "completed_as"
    market_trade_history ||--o| seller_reviews : "has"
    character ||--o{ market_trade_history : "buys"
    character ||--o{ market_trade_history : "sells"
    character ||--o{ seller_reviews : "receives"
    character ||--o{ seller_reviews : "writes"
    character ||--o| seller_statistics : "has"
    character ||--o{ seller_favorites : "favorites"
    character ||--o{ seller_subscriptions : "subscribes"

    market_listings {
        uuid id PK
        uuid seller_id FK
        uuid item_id
        decimal price
        integer quantity
        varchar status
        timestamp expires_at
        timestamp created_at
        timestamp sold_at
        timestamp updated_at
    }

    market_trade_history {
        uuid id PK
        uuid listing_id FK
        uuid buyer_id FK
        uuid seller_id FK
        uuid item_id
        integer quantity
        decimal price_per_unit
        decimal total_price
        decimal commission
        decimal seller_received
        timestamp completed_at
        timestamp created_at
    }

    seller_reviews {
        uuid id PK
        uuid trade_id FK
        uuid seller_id FK
        uuid buyer_id FK
        integer rating
        text comment
        boolean is_positive
        boolean reported
        timestamp created_at
        timestamp updated_at
    }

    seller_statistics {
        uuid id PK
        uuid seller_id FK UNIQUE
        integer total_sales
        decimal total_revenue
        decimal average_rating
        integer positive_reviews
        integer negative_reviews
        integer total_reviews
        integer items_sold
        timestamp last_sale_at
        timestamp last_update
        timestamp created_at
    }

    seller_favorites {
        uuid id PK
        uuid buyer_id FK
        uuid seller_id FK
        timestamp created_at
    }

    seller_subscriptions {
        uuid id PK
        uuid subscriber_id FK
        uuid seller_id FK
        boolean notify_on_new_listings
        boolean notify_on_price_drops
        timestamp created_at
        timestamp updated_at
    }
```

## Описание таблиц

### market_trade_history

Таблица истории сделок на рынке. Хранит информацию о всех завершенных сделках на игровом рынке.

**Ключевые поля:**
- `listing_id`: ID объявления из market_listings (FK)
- `buyer_id`: ID покупателя (FK к characters)
- `seller_id`: ID продавца (FK к characters)
- `item_id`: ID предмета
- `quantity`: Количество проданных предметов (INTEGER, > 0)
- `price_per_unit`: Цена за единицу товара (DECIMAL(15,2), > 0)
- `total_price`: Общая стоимость сделки (DECIMAL(15,2), > 0)
- `commission`: Комиссия рынка (DECIMAL(15,2), >= 0)
- `seller_received`: Сумма, полученная продавцом (DECIMAL(15,2), >= 0)
- `completed_at`: Время завершения сделки

**Индексы:**
- По `listing_id` для связи с объявлением
- По `(buyer_id, completed_at DESC)` для истории покупок покупателя
- По `(seller_id, completed_at DESC)` для истории продаж продавца
- По `(item_id, completed_at DESC)` для истории по предмету
- По `completed_at DESC` для последних сделок

### seller_reviews

Таблица отзывов продавцов. Хранит отзывы покупателей о продавцах после завершения сделок.

**Ключевые поля:**
- `trade_id`: ID сделки из market_trade_history (FK)
- `seller_id`: ID продавца (FK к characters)
- `buyer_id`: ID покупателя (FK к characters)
- `rating`: Рейтинг продавца (INTEGER, 1-5)
- `comment`: Текстовый комментарий (nullable)
- `is_positive`: Флаг положительного отзыва (BOOLEAN)
- `reported`: Флаг жалобы на отзыв (BOOLEAN)
- `created_at`: Время создания отзыва
- `updated_at`: Время последнего обновления

**Индексы:**
- По `(seller_id, rating, created_at DESC)` для отзывов продавца
- По `(buyer_id, created_at DESC)` для отзывов покупателя
- По `trade_id` для связи со сделкой
- По `(rating, seller_id)` для рейтинга продавца
- По `reported` для жалоб на отзывы

### seller_statistics

Таблица статистики продавцов. Хранит агрегированную статистику продавцов для быстрого доступа.

**Ключевые поля:**
- `seller_id`: ID продавца (FK к characters, UNIQUE)
- `total_sales`: Общее количество продаж (INTEGER, >= 0)
- `total_revenue`: Общий доход продавца (DECIMAL(20,2), >= 0)
- `average_rating`: Средний рейтинг продавца (DECIMAL(3,2), 0-5)
- `positive_reviews`: Количество положительных отзывов (INTEGER, >= 0)
- `negative_reviews`: Количество отрицательных отзывов (INTEGER, >= 0)
- `total_reviews`: Общее количество отзывов (INTEGER, >= 0)
- `items_sold`: Количество проданных предметов (INTEGER, >= 0)
- `last_sale_at`: Время последней продажи (nullable)
- `last_update`: Время последнего обновления статистики

**Индексы:**
- По `seller_id` для поиска статистики продавца
- По `(average_rating DESC, total_reviews DESC)` для топ продавцов
- По `total_revenue DESC` для сортировки по доходу
- По `total_sales DESC` для сортировки по продажам

### seller_favorites

Таблица избранных продавцов. Хранит информацию о продавцах, добавленных в избранное покупателями.

**Ключевые поля:**
- `buyer_id`: ID покупателя (FK к characters)
- `seller_id`: ID продавца (FK к characters)
- `created_at`: Время добавления в избранное

**Индексы:**
- По `buyer_id` для избранных покупателя
- По `seller_id` для покупателей, добавивших продавца в избранное

### seller_subscriptions

Таблица подписок на продавцов. Хранит информацию о подписках покупателей на продавцов для получения уведомлений.

**Ключевые поля:**
- `subscriber_id`: ID подписчика (FK к characters)
- `seller_id`: ID продавца (FK к characters)
- `notify_on_new_listings`: Уведомлять о новых объявлениях (BOOLEAN)
- `notify_on_price_drops`: Уведомлять о снижении цен (BOOLEAN)
- `created_at`: Время создания подписки
- `updated_at`: Время последнего обновления подписки

**Индексы:**
- По `subscriber_id` для подписок подписчика
- По `seller_id` для подписчиков продавца

## Constraints и валидация

### CHECK Constraints

- `market_trade_history.quantity`: Должно быть > 0
- `market_trade_history.price_per_unit`: Должно быть > 0
- `market_trade_history.total_price`: Должно быть > 0
- `market_trade_history.commission`: Должно быть >= 0
- `market_trade_history.seller_received`: Должно быть >= 0
- `seller_reviews.rating`: Должно быть >= 1 и <= 5
- `seller_statistics.total_sales`: Должно быть >= 0
- `seller_statistics.total_revenue`: Должно быть >= 0
- `seller_statistics.average_rating`: Должно быть >= 0 и <= 5
- `seller_statistics.positive_reviews`: Должно быть >= 0
- `seller_statistics.negative_reviews`: Должно быть >= 0
- `seller_statistics.total_reviews`: Должно быть >= 0
- `seller_statistics.items_sold`: Должно быть >= 0

### Foreign Keys

- `market_trade_history.listing_id` → `economy.market_listings.id` (ON DELETE CASCADE)
- `market_trade_history.buyer_id` → `mvp_core.character.id` (ON DELETE CASCADE)
- `market_trade_history.seller_id` → `mvp_core.character.id` (ON DELETE CASCADE)
- `seller_reviews.trade_id` → `economy.market_trade_history.id` (ON DELETE CASCADE)
- `seller_reviews.seller_id` → `mvp_core.character.id` (ON DELETE CASCADE)
- `seller_reviews.buyer_id` → `mvp_core.character.id` (ON DELETE CASCADE)
- `seller_statistics.seller_id` → `mvp_core.character.id` (ON DELETE CASCADE)
- `seller_favorites.buyer_id` → `mvp_core.character.id` (ON DELETE CASCADE)
- `seller_favorites.seller_id` → `mvp_core.character.id` (ON DELETE CASCADE)
- `seller_subscriptions.subscriber_id` → `mvp_core.character.id` (ON DELETE CASCADE)
- `seller_subscriptions.seller_id` → `mvp_core.character.id` (ON DELETE CASCADE)

### Unique Constraints

- `seller_reviews(trade_id, buyer_id)`: Один отзыв на сделку от покупателя
- `seller_statistics(seller_id)`: Одна статистика на продавца
- `seller_favorites(buyer_id, seller_id)`: Один продавец в избранном покупателя
- `seller_subscriptions(subscriber_id, seller_id)`: Одна подписка на продавца

## Оптимизация запросов

### Частые запросы

1. **Получение истории покупок покупателя:**
   ```sql
   SELECT * FROM economy.market_trade_history 
   WHERE buyer_id = $1 
   ORDER BY completed_at DESC 
   LIMIT 50;
   ```
   Использует индекс `(buyer_id, completed_at DESC)`.

2. **Получение истории продаж продавца:**
   ```sql
   SELECT * FROM economy.market_trade_history 
   WHERE seller_id = $1 
   ORDER BY completed_at DESC 
   LIMIT 50;
   ```
   Использует индекс `(seller_id, completed_at DESC)`.

3. **Получение отзывов продавца:**
   ```sql
   SELECT * FROM economy.seller_reviews 
   WHERE seller_id = $1 
   ORDER BY rating DESC, created_at DESC;
   ```
   Использует индекс `(seller_id, rating, created_at DESC)`.

4. **Получение статистики продавца:**
   ```sql
   SELECT * FROM economy.seller_statistics 
   WHERE seller_id = $1;
   ```
   Использует UNIQUE constraint `(seller_id)`.

5. **Поиск топ продавцов:**
   ```sql
   SELECT * FROM economy.seller_statistics 
   WHERE total_reviews >= 10 
   ORDER BY average_rating DESC, total_reviews DESC 
   LIMIT 100;
   ```
   Использует индекс `(average_rating DESC, total_reviews DESC)`.

## Миграции

### Существующие миграции:
- `V1_51__economy_trading_markets_auctions_tables.sql` - базовые таблицы (market_listings)
- `V1_59__player_market_tables.sql` - полная схема Player Market

### Применение миграций:
```bash
liquibase update --changelog-file=infrastructure/liquibase/changelog.yaml
```

## Соответствие архитектуре

Схема БД полностью соответствует архитектуре из `knowledge/mechanics/economy/player-market/player-market-database.yaml`:
- [OK] Все таблицы из архитектуры созданы
- [OK] Все поля соответствуют описанию
- [OK] Индексы оптимизированы для частых запросов
- [OK] Constraints обеспечивают целостность данных
- [OK] Foreign Keys настроены с CASCADE для автоматической очистки
- [OK] Интеграция с существующими таблицами (market_listings)

## Особенности реализации

### История сделок

Система истории сделок включает:
- **Связь с объявлением**: `listing_id` для связи с market_listings
- **Детали сделки**: количество, цена за единицу, общая стоимость
- **Комиссия**: расчет комиссии рынка
- **Доход продавца**: сумма, полученная продавцом (total_price - commission)

### Отзывы продавцов

Система отзывов включает:
- **Рейтинг**: оценка от 1 до 5
- **Комментарий**: текстовый отзыв (nullable)
- **Положительность**: флаг положительного отзыва
- **Жалобы**: флаг жалобы на отзыв
- **Уникальность**: один отзыв на сделку от покупателя

### Статистика продавцов

Система статистики включает:
- **Продажи**: общее количество продаж и проданных предметов
- **Доход**: общий доход продавца
- **Рейтинг**: средний рейтинг и количество отзывов
- **Последняя продажа**: время последней продажи
- **Агрегация**: автоматическое обновление при новых сделках

### Избранные продавцы

Система избранных включает:
- **Быстрый доступ**: список избранных продавцов покупателя
- **Уникальность**: один продавец в избранном покупателя

### Подписки на продавцов

Система подписок включает:
- **Уведомления о новых объявлениях**: опция уведомлений
- **Уведомления о снижении цен**: опция уведомлений
- **Управление подписками**: создание и обновление подписок

### Интеграция с market_listings

Player Market интегрирован с market_listings через:
- `market_trade_history.listing_id`: Связь с объявлением
- Автоматическое обновление статистики при завершении сделок
- Связь отзывов со сделками для контекста


