---
**api-readiness:** needs-work  
**api-readiness-check-date:** 2025-11-09 09:42
**api-readiness-notes:** Перепроверено 2025-11-09 09:42. Не хватает схем запросов/ответов с кодами ошибок, описания авторизации, WebSocket/Event потоков, валидации, интеграции с БД и KPI; требуется детализация перед постановкой задач.
---

# Player Market API - API endpoints

**Статус:** in-review  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-07  
**Дата обновления:** 2025-11-09 09:42  
**Приоритет:** высокий  
**Автор:** AI Brain Manager

**Микрофича:** API endpoints  
**Размер:** ~230 строк ✅

---

- **Status:** in-review
- **Last Updated:** 2025-11-09 09:42
---

## API Endpoints

### POST /api/v1/market/listings

**Request:**
```json
{
  "characterId": "uuid",
  "itemId": "uuid",
  "quantity": 1,
  "pricePerUnit": 6500
}
```

**Response 200:**
```json
{
  "success": true,
  "listingId": "uuid",
  "expiresAt": "datetime"
}
```

---

### GET /api/v1/market/listings

**Query Params:**
```
?search=mantis
&type=weapon
&quality=epic
&minPrice=5000
&maxPrice=10000
&city=nightCity
&sortBy=price_asc
&page=1
&limit=50
```

**Response 200:**
```json
{
  "listings": [
    {
      "id": "uuid",
      "seller": {
        "id": "uuid",
        "username": "CyberNinja92",
        "rating": 4.8,
        "totalSales": 148
      },
      "item": {
        "id": "mantis_blades_epic",
        "name": "Mantis Blades",
        "quality": "epic",
        "level": 20
      },
      "quantity": 1,
      "pricePerUnit": 6500,
      "totalPrice": 6500,
      "createdAt": "datetime",
      "views": 45
    }
  ],
  "total": 123,
  "page": 1,
  "pages": 3
}
```

---

### POST /api/v1/market/listings/{id}/buy

**Request:**
```json
{
  "characterId": "uuid",
  "quantity": 1
}
```

**Response 200:**
```json
{
  "success": true,
  "trade": {
    "id": "uuid",
    "itemReceived": {
      "id": "mantis_blades_epic",
      "quantity": 1
    },
    "paidAmount": 6500
  }
}
```

---

### DELETE /api/v1/market/listings/{id}

**Response 200:**
```json
{
  "success": true,
  "itemReturned": true
}
```

---

### POST /api/v1/market/reviews

**Request:**
```json
{
  "sellerId": "uuid",
  "tradeId": "uuid",
  "rating": 5,
  "comment": "Fast delivery, great seller!"
}
```

---

### GET /api/v1/market/sellers/{id}/reviews

**Response 200:**
```json
{
  "seller": {
    "id": "uuid",
    "username": "string",
    "averageRating": 4.8,
    "totalReviews": 148
  },
  "reviews": [
    {
      "rating": 5,
      "comment": "string",
      "createdAt": "datetime"
    }
  ]
}
```

---

## Безопасность

**Anti-Fraud:**
- Price validation (market average ±200%)
- Rate limiting (max 10 listings/hour)
- Captcha для bulk operations
- Trade history audit

**Anti-Scam:**
- Seller reputation system
- Review system
- Blacklist
- Admin moderation tools

---

## Связанные документы

- `.BRAIN/02-gameplay/economy/player-market/player-market-core.md` - Core (микрофича 1/4)
- `.BRAIN/02-gameplay/economy/player-market/player-market-database.md` - Database (микрофича 2/4)
- `.BRAIN/02-gameplay/economy/player-market/player-market-analytics.md` - Analytics (микрофича 4/4)

---

## История изменений

- **v1.0.0 (2025-11-07 06:10)** - Микрофича 3/4: Player Market API (split from economy-player-market.md)
