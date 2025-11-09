---
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-09 01:30
**api-readiness-notes:** Проводка P2P трейда, схемы БД, события и антифрод перепроверены 2025-11-09 01:30 — документ готов для API economy-service.
**target-domain:** economy-trade  
**target-microservice:** economy-service (8085)  
**target-frontend-module:** modules/economy/trade
---
**API Tasks Status:**
- Status: created
- Tasks:
  - API-TASK-111: api/v1/economy/trade/trade-system.yaml (2025-11-09)
- Last Updated: 2025-11-09 20:35
---

# Trade System (P2P) - Система торговли между игроками

**Статус:** approved  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-07  
**Последнее обновление:** 2025-11-09 01:30  
**Приоритет:** высокий (критично для экономики)  
**Автор:** AI Brain Manager

---

## Краткое описание

**Trade System** - система для прямого обмена предметами и валютой между игроками (Player-to-Player trading). Обеспечивает безопасный обмен с защитой от мошенничества.

**Ключевые возможности:**
- ✅ Trade window (обмен 1-на-1)
- ✅ Trade offers (предложение/принятие/отклонение)
- ✅ Confirmation (двойное подтверждение)
- ✅ Trade history (аудит всех сделок)
- ✅ Trade restrictions (bind-on-pickup, bind-on-equip)
- ✅ Gold + items trade
- ✅ Distance check (игроки должны быть рядом)
- ✅ Anti-scam protection

---

## Микросервисная архитектура

**Ответственный микросервис:** economy-service  
**Порт:** 8085  
**API Gateway маршрут:** `/api/v1/economy/trade/*`  
**Статус:** 📋 В планах (Фаза 3)

**Взаимодействие с другими сервисами:**
- inventory-service (economy): проверка items, transfer
- character-service: получение данных игроков
- world-service: проверка расстояния между игроками

**Event Bus события:**
- Публикует: `trade:started`, `trade:completed`, `trade:cancelled`
- Подписывается: `character:moved` (проверка расстояния), `session:ended` (cancel trade)

**Circuit Breaker:**
- Используется для вызовов inventory-service (проверка items)
- Fallback: отмена трейда при недоступности inventory

---

## Database Schema

### Таблица `trade_sessions`

```sql
CREATE TABLE trade_sessions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    
    -- Участники
    initiator_character_id UUID NOT NULL,
    recipient_character_id UUID NOT NULL,
    
    -- Offers
    initiator_offer JSONB DEFAULT '{"items": [], "gold": 0}',
    -- {items: [{itemId: "uuid", quantity: 1}], gold: 1000}
    
    recipient_offer JSONB DEFAULT '{"items": [], "gold": 0}',
    
    -- Confirmations
    initiator_confirmed BOOLEAN DEFAULT FALSE,
    recipient_confirmed BOOLEAN DEFAULT FALSE,
    
    initiator_locked BOOLEAN DEFAULT FALSE, -- Locked after first confirm
    recipient_locked BOOLEAN DEFAULT FALSE,
    
    -- Status
    status VARCHAR(20) DEFAULT 'PENDING',
    -- PENDING, COMPLETED, CANCELLED, EXPIRED
    
    -- Location (для проверки distance)
    zone_id VARCHAR(100) NOT NULL,
    
    -- Timestamps
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP,
    expires_at TIMESTAMP NOT NULL, -- 5 minutes timeout
    
    -- Результат
    completion_reason VARCHAR(50),
    -- SUCCESS, CANCELLED_BY_INITIATOR, CANCELLED_BY_RECIPIENT, EXPIRED, ERROR
    
    CONSTRAINT fk_trade_initiator FOREIGN KEY (initiator_character_id) 
        REFERENCES characters(id) ON DELETE CASCADE,
    CONSTRAINT fk_trade_recipient FOREIGN KEY (recipient_character_id) 
        REFERENCES characters(id) ON DELETE CASCADE,
    CHECK (initiator_character_id != recipient_character_id)
);

CREATE INDEX idx_trade_initiator ON trade_sessions(initiator_character_id, status);
CREATE INDEX idx_trade_recipient ON trade_sessions(recipient_character_id, status);
CREATE INDEX idx_trade_expires ON trade_sessions(expires_at) WHERE status = 'PENDING';
```

### Таблица `trade_history`

```sql
CREATE TABLE trade_history (
    id BIGSERIAL PRIMARY KEY,
    trade_session_id UUID NOT NULL,
    
    -- Participants
    character_a_id UUID NOT NULL,
    character_b_id UUID NOT NULL,
    
    -- What was traded
    character_a_gave JSONB, -- {items: [...], gold: 1000}
    character_b_gave JSONB,
    
    -- Context
    zone_id VARCHAR(100),
    
    -- Timestamp
    traded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_trade_history_session FOREIGN KEY (trade_session_id) 
        REFERENCES trade_sessions(id) ON DELETE SET NULL
);

CREATE INDEX idx_trade_history_char_a ON trade_history(character_a_id, traded_at DESC);
CREATE INDEX idx_trade_history_char_b ON trade_history(character_b_id, traded_at DESC);
```

---

## Trade Flow

### 1. Initiate Trade

```java
@Service
public class TradeService {
    
    @Transactional
    public TradeResponse initiateTrade(UUID initiatorId, UUID recipientId) {
        // 1. Валидация
        if (initiatorId.equals(recipientId)) {
            throw new CannotTradeWithSelfException();
        }
        
        // 2. Проверить, что оба онлайн
        if (!sessionService.isOnline(initiatorId) || 
            !sessionService.isOnline(recipientId)) {
            throw new PlayerOfflineException();
        }
        
        // 3. Проверить distance (должны быть рядом)
        double distance = locationService.getDistance(initiatorId, recipientId);
        if (distance > 10) { // 10 meters
            throw new TooFarAwayException("Players must be within 10 meters to trade");
        }
        
        // 4. Проверить, не в другой trade уже
        if (tradeRepository.existsActiveTrade(initiatorId) ||
            tradeRepository.existsActiveTrade(recipientId)) {
            throw new AlreadyInTradeException();
        }
        
        // 5. Создать trade session
        TradeSession trade = new TradeSession();
        trade.setInitiatorCharacterId(initiatorId);
        trade.setRecipientCharacterId(recipientId);
        trade.setZoneId(getZone(initiatorId));
        trade.setExpiresAt(Instant.now().plus(Duration.ofMinutes(5)));
        
        trade = tradeRepository.save(trade);
        
        // 6. Уведомить получателя
        webSocketService.sendToPlayer(
            getAccountId(recipientId),
            new TradeRequestMessage(
                trade.getId(),
                getCharacterName(initiatorId),
                5 * 60 // seconds to accept
            )
        );
        
        log.info("Trade initiated: {} (initiator: {}, recipient: {})", 
            trade.getId(), initiatorId, recipientId);
        
        return new TradeResponse(trade.getId(), "Trade request sent");
    }
}
```

### 2. Add Item/Gold to Offer

```java
@Transactional
public void addItemToTrade(UUID tradeId, UUID characterId, UUID itemId, int quantity) {
    // 1. Получить trade
    TradeSession trade = tradeRepository.findById(tradeId)
        .orElseThrow(() -> new TradeNotFoundException());
    
    if (trade.getStatus() != TradeStatus.PENDING) {
        throw new TradeNotActiveException();
    }
    
    // 2. Определить, кто добавляет
    boolean isInitiator = trade.getInitiatorCharacterId().equals(characterId);
    boolean isRecipient = trade.getRecipientCharacterId().equals(characterId);
    
    if (!isInitiator && !isRecipient) {
        throw new NotInTradeException();
    }
    
    // 3. Проверить, что не locked
    if ((isInitiator && trade.isInitiatorLocked()) ||
        (isRecipient && trade.isRecipientLocked())) {
        throw new TradeLockedExcept ion("Cannot modify offer after confirmation");
    }
    
    // 4. Получить item
    CharacterItem item = itemRepository.findById(itemId)
        .orElseThrow(() -> new ItemNotFoundException());
    
    if (!item.getCharacterId().equals(characterId)) {
        throw new UnauthorizedItemAccessException();
    }
    
    // 5. Проверить, можно ли торговать
    if (item.isBound()) {
        throw new CannotTradeBoundItemException();
    }
    
    ItemTemplate template = itemTemplateRepository.findById(item.getItemTemplateId()).get();
    if (!template.isTradeable()) {
        throw new ItemNotTradeableException();
    }
    
    // 6. Проверить количество
    if (quantity > item.getQuantity()) {
        throw new InsufficientQuantityException();
    }
    
    // 7. Добавить в offer
    TradeOffer offer = isInitiator ? trade.getInitiatorOffer() : trade.getRecipientOffer();
    offer.addItem(itemId, quantity);
    
    if (isInitiator) {
        trade.setInitiatorOffer(offer);
        trade.setInitiatorConfirmed(false); // Reset confirmation
    } else {
        trade.setRecipientOffer(offer);
        trade.setRecipientConfirmed(false);
    }
    
    tradeRepository.save(trade);
    
    // 8. Уведомить другого игрока
    UUID otherId = isInitiator ? trade.getRecipientCharacterId() : trade.getInitiatorCharacterId();
    webSocketService.sendToPlayer(
        getAccountId(otherId),
        new TradeOfferUpdatedMessage(
            trade.getId(),
            getCharacterName(characterId),
            "added",
            template.getItemName(),
            quantity
        )
    );
    
    log.info("Item added to trade {}: {} x{}", tradeId, itemId, quantity);
}

@Transactional
public void addGoldToTrade(UUID tradeId, UUID characterId, long amount) {
    // Similar to addItemToTrade, but for gold
    // ...
}
```

### 3. Confirm Trade

```java
@Transactional
public void confirmTrade(UUID tradeId, UUID characterId) {
    // 1. Получить trade
    TradeSession trade = tradeRepository.findById(tradeId).get();
    
    boolean isInitiator = trade.getInitiatorCharacterId().equals(characterId);
    
    // 2. First confirmation → Lock offer
    if (isInitiator) {
        trade.setInitiatorConfirmed(true);
        trade.setInitiatorLocked(true);
    } else {
        trade.setRecipientConfirmed(true);
        trade.setRecipientLocked(true);
    }
    
    tradeRepository.save(trade);
    
    // 3. Уведомить
    UUID otherId = isInitiator ? trade.getRecipientCharacterId() : trade.getInitiatorCharacterId();
    webSocketService.sendToPlayer(
        getAccountId(otherId),
        new TradeConfirmedMessage(
            getCharacterName(characterId) + " confirmed the trade"
        )
    );
    
    // 4. Если оба подтвердили → Execute trade
    if (trade.isInitiatorConfirmed() && trade.isRecipientConfirmed()) {
        executeTrade(trade);
    }
}
```

### 4. Execute Trade

```java
@Transactional
private void executeTrade(TradeSession trade) {
    try {
        UUID initiatorId = trade.getInitiatorCharacterId();
        UUID recipientId = trade.getRecipientCharacterId();
        
        TradeOffer initiatorOffer = trade.getInitiatorOffer();
        TradeOffer recipientOffer = trade.getRecipientOffer();
        
        // 1. Валидация (финальная проверка)
        validateTradeExecution(trade);
        
        // 2. Передать items initiator → recipient
        for (TradeItem item : initiatorOffer.getItems()) {
            transferItem(initiatorId, recipientId, item.getItemId(), item.getQuantity());
        }
        
        // 3. Передать gold initiator → recipient
        if (initiatorOffer.getGold() > 0) {
            transferGold(initiatorId, recipientId, initiatorOffer.getGold());
        }
        
        // 4. Передать items recipient → initiator
        for (TradeItem item : recipientOffer.getItems()) {
            transferItem(recipientId, initiatorId, item.getItemId(), item.getQuantity());
        }
        
        // 5. Передать gold recipient → initiator
        if (recipientOffer.getGold() > 0) {
            transferGold(recipientId, initiatorId, recipientOffer.getGold());
        }
        
        // 6. Обновить статус
        trade.setStatus(TradeStatus.COMPLETED);
        trade.setCompletedAt(Instant.now());
        trade.setCompletionReason("SUCCESS");
        tradeRepository.save(trade);
        
        // 7. Записать в history
        tradeHistoryRepository.save(new TradeHistory(
            trade.getId(),
            initiatorId,
            recipientId,
            initiatorOffer.toJSON(),
            recipientOffer.toJSON(),
            trade.getZoneId()
        ));
        
        // 8. Уведомить обоих
        notificationService.send(getAccountId(initiatorId), 
            new TradeCompletedNotification("Trade completed successfully"));
        notificationService.send(getAccountId(recipientId), 
            new TradeCompletedNotification("Trade completed successfully"));
        
        // 9. Опубликовать событие
        eventBus.publish(new TradeCompletedEvent(
            trade.getId(),
            initiatorId,
            recipientId
        ));
        
        log.info("Trade {} completed successfully", trade.getId());
        
    } catch (Exception e) {
        // Откатить trade
        trade.setStatus(TradeStatus.CANCELLED);
        trade.setCompletionReason("ERROR: " + e.getMessage());
        tradeRepository.save(trade);
        
        log.error("Trade {} failed: {}", trade.getId(), e.getMessage());
        
        throw new TradeExecutionFailedException(e.getMessage());
    }
}
```

---

## API Endpoints

**POST `/api/v1/trade/initiate`** - начать торговлю
**POST `/api/v1/trade/{id}/add-item`** - добавить предмет
**POST `/api/v1/trade/{id}/add-gold`** - добавить золото
**POST `/api/v1/trade/{id}/remove-item`** - убрать предмет
**POST `/api/v1/trade/{id}/confirm`** - подтвердить
**POST `/api/v1/trade/{id}/cancel`** - отменить
**GET `/api/v1/trade/history`** - история сделок

---

## Связанные документы

- `.BRAIN/05-technical/backend/inventory-system.md` - Inventory
- `.BRAIN/02-gameplay/economy/economy-trading.md` - Trading mechanics

---

## История изменений

- **v1.0.0 (2025-11-07 05:20)** - Создан документ Trade System


