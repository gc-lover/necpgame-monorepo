---
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 06:15
**api-readiness-notes:** Auction House Mechanics. Философия, механика ставок, автопродление, buyout. ~400 строк.
---

# Auction House Mechanics - Механика аукционов

**target-domain:** economy-auction  
**target-microservice:** economy-service (port 8085)  
**target-frontend-module:** modules/economy/auction-house

**Статус:** approved  
**Версия:** 1.0.0  
**Дата:** 2025-11-07 06:15  
**Приоритет:** высокий  
**Автор:** AI Brain Manager

**Микрофича:** Auction mechanics  
**Размер:** ~400 строк ✅

---
**API Tasks Status:**
- Status: created
- Tasks:
  - API-TASK-109: api/v1/economy/auction-house/auction-mechanics.yaml (2025-11-09)
- Last Updated: 2025-11-09 20:15

---

## Философия

**Auction House vs Player Market:**

**Auction House (конкурентные ставки):**
- Bidding system (повышение ставок)
- Таймер (24h, 48h, 72h)
- Автопродление при ставке
- Buyout option
- Комиссия 5%

**Player Market (фиксированная цена):**
- Instant buy
- Фиксированная цена
- Без таймера
- Комиссия 1%

---

## Механики

### 1. Создание аукциона

```typescript
createAuction(item, startingBid, buyoutPrice, duration) {
  // Validation
  if (startingBid < 100) return error;
  if (buyoutPrice && buyoutPrice <= startingBid) return error;
  
  // Lock item
  lockItem(item);
  
  // Create
  auction = {
    id: uuid(),
    sellerId: playerId,
    itemId: item.id,
    quantity: item.quantity,
    startingBid: startingBid,
    currentBid: startingBid,
    buyoutPrice: buyoutPrice,
    duration: duration, // 24h, 48h, 72h
    expiresAt: now + duration,
    status: "active",
    bidCount: 0
  };
  
  save(auction);
  return auction;
}
```

---

### 2. Ставка (Bidding)

```typescript
placeBid(auctionId, bidAmount) {
  auction = getAuction(auctionId);
  
  // Validation
  if (auction.status !== "active") return error;
  if (bidAmount <= auction.currentBid) return error;
  if (bidAmount < auction.currentBid * 1.05) return error; // Min +5%
  if (buyer.eurodollars < bidAmount) return error;
  
  // Reserve funds
  reserveFunds(buyer.id, bidAmount);
  
  // Return prev bidder funds
  if (auction.currentBidderId) {
    returnFunds(auction.currentBidderId, auction.currentBid);
  }
  
  // Update auction
  auction.previousBid = auction.currentBid;
  auction.currentBid = bidAmount;
  auction.currentBidderId = buyer.id;
  auction.bidCount++;
  
  // Auto-extend (если ставка в последние 5 мин)
  if (auction.expiresAt - now < 5min) {
    auction.expiresAt += 5min;
  }
  
  save(auction);
  
  // Notify
  notify(seller, "New bid: " + bidAmount);
  notify(prevBidder, "Outbid!");
  
  return success;
}
```

---

### 3. Buyout

```typescript
buyout(auctionId) {
  auction = getAuction(auctionId);
  
  if (!auction.buyoutPrice) return error;
  if (buyer.eurodollars < auction.buyoutPrice) return error;
  
  // Instant purchase
  completeSale(auction, auction.buyoutPrice, "buyout");
  
  return success;
}
```

---

### 4. Завершение аукциона

```typescript
// Cron job (каждую минуту)
processExpiredAuctions() {
  auctions = getExpiredAuctions();
  
  for (auction of auctions) {
    if (auction.currentBidderId) {
      // Есть победитель
      completeSale(auction, auction.currentBid, "won");
    } else {
      // Нет ставок
      auction.status = "expired";
      unlockItem(auction.itemId);
      notify(seller, "Auction expired");
    }
    save(auction);
  }
}

completeSale(auction, price, method) {
  BEGIN_TRANSACTION();
  
    // Деньги
    buyer.eurodollars -= price;
    seller.eurodollars += price * 0.95; // -5% fee
    
    // Предмет
    removeFromSeller(auction.itemId);
    addToBuyer(auction.itemId);
    
    // Auction
    auction.status = "sold";
    auction.soldPrice = price;
    auction.soldMethod = method;
    auction.soldAt = now;
    
    // History
    createTradeRecord(auction);
  
  COMMIT();
  
  notify(buyer, "Won auction!");
  notify(seller, "Auction sold: " + price);
}
```

---

## Связанные документы

- `.BRAIN/02-gameplay/economy/auction-house/auction-database.md` - Database (2/3)
- `.BRAIN/02-gameplay/economy/auction-house/auction-operations.md` - Operations (3/3)

---

## История изменений

- **v1.0.0 (2025-11-07 06:15)** - Микрофича 1/3 (split from economy-auction-house.md)
