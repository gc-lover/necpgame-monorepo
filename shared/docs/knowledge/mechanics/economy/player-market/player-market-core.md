---
**api-readiness:** needs-work  
**api-readiness-check-date:** 2025-11-09 09:48
**api-readiness-notes:** Перепроверено 2025-11-09 09:48. Требуется детализировать бизнес-правила (ограничения, статусы сделок, комиссии), описать интеграцию с экономикой/инвентарём, события, очереди, схемы данных и связать с API/БД перед постановкой задач.
---

# Player Market Core - Основа P2P торговли

**Статус:** in-review  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-07  
**Дата обновления:** 2025-11-09 09:48  
**Приоритет:** высокий  
**Автор:** AI Brain Manager

**Микрофича:** P2P market mechanics  
**Размер:** ~400 строк ✅

---

- **Status:** in-review
- **Last Updated:** 2025-11-09 09:48
---

## Краткое описание

Player Market - P2P торговая площадка между игроками (не аукцион!).

**Отличия от Auction House:**
- Фиксированная цена (не ставки)
- Быстрая купля-продажа
- Личный контакт через чат
- Репутация продавца
- Меньше комиссия (1% vs 5%)

---

## Источники вдохновения

**EVE Online** - Player Market контракты (buy/sell orders)  
**Path of Exile** - Trade board с фильтрами  
**Warframe** - Player-to-player торговля  
**Lost Ark** - Market board с репутацией

---

## Философия

**P2P торговля:**
- Игрок выставляет товар по цене
- Другие игроки покупают напрямую
- Мгновенная сделка (instant buy)
- Опционально: торг через чат

**Социальный элемент:**
- Репутация продавца/покупателя
- Отзывы и рейтинги
- Любимые продавцы
- Blacklist

---

## Механики

### 1. Создание объявления

```typescript
createListing(item, price, quantity) {
  // 1. Проверки
  if (!hasItem(item, quantity)) return error;
  if (price < MIN_PRICE || price > MAX_PRICE) return error;
  
  // 2. Lock item (нельзя использовать пока в продаже)
  lockItem(item, quantity);
  
  // 3. Создать listing
  listing = {
    id: uuid(),
    sellerId: playerId,
    itemId: item.id,
    quantity: quantity,
    pricePerUnit: price,
    totalPrice: price * quantity,
    status: "active",
    expiresAt: now + 7days
  };
  
  save(listing);
  return listing;
}
```

**Лимиты:**
- Max 20 активных объявлений одновременно
- Срок действия: 7 дней
- Min цена: 1 eurodollar
- Max цена: 999,999,999 eurodollars

---

### 2. Поиск и фильтры

```typescript
search(filters) {
  query = buildQuery({
    itemName: filters.search,
    itemType: filters.type,
    quality: filters.quality,
    minPrice: filters.minPrice,
    maxPrice: filters.maxPrice,
    minLevel: filters.minLevel,
    maxLevel: filters.maxLevel,
    sellerId: filters.sellerId,
    city: filters.city
  });
  
  results = executeQuery(query)
    .orderBy(filters.sortBy)
    .paginate(filters.page, 50);
  
  return results;
}
```

**Фильтры:**
- Поиск по названию (текст)
- Тип предмета (weapon, armor, consumable)
- Качество (common, rare, epic, legendary)
- Диапазон цен (min-max)
- Уровень предмета (min-max)
- Продавец (по ID или username)
- Город (nightCity, tokyo, london)

**Сортировка:**
- По цене (возрастание/убывание)
- По дате (новые/старые)
- По популярности (views)
- По рейтингу продавца

---

### 3. Покупка

```typescript
buyListing(listingId, quantity) {
  listing = getListing(listingId);
  
  // 1. Проверки
  if (listing.status !== "active") return error;
  if (quantity > listing.quantity) return error;
  if (buyer.eurodollars < listing.totalPrice) return error;
  
  // 2. Transaction (atomic)
  BEGIN_TRANSACTION();
  
    // Деньги
    buyer.eurodollars -= listing.totalPrice;
    seller.eurodollars += listing.totalPrice * 0.99; // -1% комиссия
    
    // Предмет
    removeFromSeller(listing.itemId, quantity);
    addToBuyer(listing.itemId, quantity);
    
    // Listing
    if (quantity === listing.quantity) {
      listing.status = "sold";
    } else {
      listing.quantity -= quantity;
    }
    
    // Trade history
    createTradeRecord({
      buyerId: buyer.id,
      sellerId: seller.id,
      listingId: listing.id,
      itemId: listing.itemId,
      quantity: quantity,
      price: listing.totalPrice
    });
  
  COMMIT();
  
  // 3. Уведомления
  notify(seller, "Your item sold!");
  notify(buyer, "Purchase successful!");
  
  return success;
}
```

---

## Связанные документы

- `.BRAIN/02-gameplay/economy/player-market/player-market-database.md` - Database (микрофича 2/4)
- `.BRAIN/02-gameplay/economy/player-market/player-market-api.md` - API (микрофича 3/4)
- `.BRAIN/02-gameplay/economy/player-market/player-market-analytics.md` - Analytics (микрофича 4/4)

---

## История изменений

- **v1.0.0 (2025-11-07 06:10)** - Микрофича 1/4: Player Market Core (split from economy-player-market.md)
