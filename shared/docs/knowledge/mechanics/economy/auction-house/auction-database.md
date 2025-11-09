---
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 06:15
**api-readiness-notes:** Auction House Database. БД schema, UI/UX, примеры использования. ~280 строк.
---

# Auction House Database - База данных

**target-domain:** economy-auction  
**target-microservice:** economy-service (port 8085)  
**target-frontend-module:** modules/economy/auction-house

**Статус:** approved  
**Версия:** 1.0.0  
**Дата:** 2025-11-07 06:15  
**Приоритет:** высокий  
**Автор:** AI Brain Manager

**Микрофича:** Database & UI  
**Размер:** ~280 строк ✅

---
**API Tasks Status:**
- Status: created
- Tasks:
  - API-TASK-108: api/v1/economy/auction-house/auction-database.yaml (2025-11-09)
- Last Updated: 2025-11-09 20:05

---

## Database Schema

### auctions

```sql
CREATE TABLE auctions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    seller_id UUID NOT NULL,
    item_id UUID NOT NULL,
    
    quantity INTEGER NOT NULL,
    starting_bid INTEGER NOT NULL,
    current_bid INTEGER NOT NULL,
    buyout_price INTEGER,
    
    current_bidder_id UUID,
    bid_count INTEGER DEFAULT 0,
    
    duration_hours INTEGER NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    
    status VARCHAR(20) DEFAULT 'active',
    -- active, sold, expired, cancelled
    
    sold_price INTEGER,
    sold_method VARCHAR(20), -- won, buyout
    sold_at TIMESTAMP,
    
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_auction_seller FOREIGN KEY (seller_id) 
        REFERENCES players(id) ON DELETE CASCADE
);

CREATE INDEX idx_auctions_status ON auctions(status);
CREATE INDEX idx_auctions_expires ON auctions(expires_at) WHERE status = 'active';
CREATE INDEX idx_auctions_item ON auctions(item_id);
```

---

### auction_bids

```sql
CREATE TABLE auction_bids (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    auction_id UUID NOT NULL,
    bidder_id UUID NOT NULL,
    
    bid_amount INTEGER NOT NULL,
    
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_bid_auction FOREIGN KEY (auction_id) 
        REFERENCES auctions(id) ON DELETE CASCADE,
    CONSTRAINT fk_bid_bidder FOREIGN KEY (bidder_id) 
        REFERENCES players(id) ON DELETE CASCADE
);

CREATE INDEX idx_bids_auction ON auction_bids(auction_id, created_at DESC);
CREATE INDEX idx_bids_bidder ON auction_bids(bidder_id);
```

---

## UI/UX

### Browse Auctions

```
┌──────────────────────────────────────────────────────┐
│ AUCTION HOUSE                  [My Auctions] [My Bids]│
├──────────────────────────────────────────────────────┤
│ Search: [___________]  [🔍]     Sort: [Ending Soon]  │
│                                                       │
│ ┌────────────────────────────────────────────────┐  │
│ │ [Icon] Legendary Cyberarm                      │  │
│ │        Current Bid: 15,000 ed (12 bids)        │  │
│ │        Buyout: 25,000 ed                       │  │
│ │        Ends in: 2h 35m                         │  │
│ │        [Place Bid] [Buyout]                    │  │
│ └────────────────────────────────────────────────┘  │
│                                                       │
└──────────────────────────────────────────────────────┘
```

---

## Связанные документы

- `.BRAIN/02-gameplay/economy/auction-house/auction-mechanics.md` - Mechanics (1/3)
- `.BRAIN/02-gameplay/economy/auction-house/auction-operations.md` - Operations (3/3)

---

## История изменений

- **v1.0.0 (2025-11-07 06:15)** - Микрофича 2/3 (split from economy-auction-house.md)
