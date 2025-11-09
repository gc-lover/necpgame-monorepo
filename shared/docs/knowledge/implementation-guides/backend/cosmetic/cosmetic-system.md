---
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 02:30
**api-readiness-notes:** Cosmetic System. Skins, emotes, персонализация, cosmetic shop. ~380 строк.
---

# Cosmetic System - Система косметики

---

- **Status:** queued
- **Last Updated:** 2025-11-07 22:45
---

**Статус:** approved  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-07 02:30  
**Приоритет:** HIGH (Monetization!)  
**Автор:** AI Brain Manager

**Микрофича:** Cosmetics & personalization  
**Размер:** ~380 строк ✅

---

## Краткое описание

**Cosmetic System** - система косметических предметов для персонализации (skins, emotes, titles).

**Ключевые возможности:**
- ✅ Character Skins (скины персонажа)
- ✅ Weapon Skins (скины оружия)
- ✅ Emotes (эмоции/жесты)
- ✅ Titles (звания)
- ✅ Name Plates (таблички имени)
- ✅ Victory Poses (позы победы)

---

## Типы косметики

### 1. Character Skins (Скины персонажа)

```
Rarity Tiers:
- Common: Basic recolors
- Uncommon: Different textures
- Rare: New outfits
- Epic: Unique designs
- Legendary: Animated effects
```

**Примеры:**
- "Corpo Executive" - деловой костюм
- "Street Samurai" - самурай стиль
- "Netrunner Elite" - киберпанк хакер
- "Nomad Warrior" - кочевник

---

### 2. Weapon Skins

```
Types:
- Pistols (10+ skins each weapon)
- Rifles (10+ skins)
- SMGs (10+ skins)
- Shotguns (10+ skins)
- Melee (10+ skins)
```

**Features:**
- Color schemes
- Material changes (chrome, gold, carbon)
- Particle effects (legendary only)
- Sound effects (legendary only)

---

### 3. Emotes

```
Categories:
- Greetings (wave, salute, bow)
- Victory (dance, flex, celebrate)
- Taunts (mock, laugh, provoke)
- Social (sit, lean, smoke)
```

**Примеры:**
- "Victory Dance" - танец победы
- "Corpo Salute" - корпоративное приветствие
- "Street Wave" - уличный жест
- "Netrunner Taunt" - насмешка хакера

---

### 4. Titles

```
Sources:
- Achievements (earned)
- Battle Pass (seasonal)
- Events (limited time)
- Leaderboard (rank-based)
- Purchase (premium)
```

**Примеры:**
- "Legend of Night City" (achievement)
- "Season 1 Champion" (battle pass)
- "Top 100 Global" (leaderboard)
- "The Immortal" (premium)

---

### 5. Name Plates

```
Features:
- Background design
- Border style
- Animated effects
- Color schemes
```

**Rarity:**
- Common: Simple backgrounds
- Rare: Glowing borders
- Epic: Animated backgrounds
- Legendary: Particle effects

---

## Database Schema

### Таблица `cosmetic_items`

```sql
CREATE TABLE cosmetic_items (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    
    -- Identification
    code VARCHAR(100) UNIQUE NOT NULL,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    
    -- Type
    cosmetic_type VARCHAR(50) NOT NULL,
    category VARCHAR(50),
    
    -- Rarity
    rarity VARCHAR(20) DEFAULT 'COMMON',
    
    -- Acquisition
    is_purchasable BOOLEAN DEFAULT TRUE,
    price INTEGER,
    currency VARCHAR(20) DEFAULT 'PREMIUM',
    
    -- Exclusive
    is_exclusive BOOLEAN DEFAULT FALSE,
    exclusive_source VARCHAR(100),
    
    -- Limited
    is_limited BOOLEAN DEFAULT FALSE,
    available_from TIMESTAMP,
    available_until TIMESTAMP,
    
    -- Assets
    preview_image VARCHAR(255),
    model_path VARCHAR(255),
    animation_path VARCHAR(255),
    
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_cosmetics_type ON cosmetic_items(cosmetic_type);
CREATE INDEX idx_cosmetics_purchasable ON cosmetic_items(is_purchasable) 
    WHERE is_purchasable = TRUE;
```

### Таблица `player_cosmetics`

```sql
CREATE TABLE player_cosmetics (
    player_id UUID NOT NULL,
    cosmetic_id UUID NOT NULL,
    
    -- Acquisition
    acquired_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    source VARCHAR(50),
    
    -- Usage
    times_used INTEGER DEFAULT 0,
    last_used_at TIMESTAMP,
    
    PRIMARY KEY (player_id, cosmetic_id),
    
    CONSTRAINT fk_player_cosmetic_player FOREIGN KEY (player_id) 
        REFERENCES players(id) ON DELETE CASCADE,
    CONSTRAINT fk_player_cosmetic_item FOREIGN KEY (cosmetic_id) 
        REFERENCES cosmetic_items(id) ON DELETE CASCADE
);

CREATE INDEX idx_player_cosmetics_player ON player_cosmetics(player_id);
```

### Таблица `player_equipped_cosmetics`

```sql
CREATE TABLE player_equipped_cosmetics (
    player_id UUID PRIMARY KEY,
    
    -- Equipped items
    character_skin_id UUID,
    weapon_skin_id UUID,
    title_id UUID,
    name_plate_id UUID,
    
    -- Favorite emotes (quick access)
    emote_slot_1 UUID,
    emote_slot_2 UUID,
    emote_slot_3 UUID,
    emote_slot_4 UUID,
    
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_equipped_player FOREIGN KEY (player_id) 
        REFERENCES players(id) ON DELETE CASCADE
);
```

---

## Cosmetic Shop

### Shop Rotation

```java
@Service
public class CosmeticShopService {
    
    @Scheduled(cron = "0 0 0 * * *") // Daily rotation
    public void rotateDailyShop() {
        // Select 6 items for daily shop
        List<CosmeticItem> dailyItems = selectDailyItems();
        
        // Save rotation
        ShopRotation rotation = new ShopRotation();
        rotation.setRotationDate(LocalDate.now());
        rotation.setItems(dailyItems.stream()
            .map(CosmeticItem::getId)
            .collect(Collectors.toList()));
        
        shopRotationRepository.save(rotation);
        
        // Notify players
        announceShopRotation(dailyItems);
    }
    
    private List<CosmeticItem> selectDailyItems() {
        List<CosmeticItem> pool = cosmeticRepository
            .findByIsPurchasableTrue();
        
        // Select mix of rarities
        List<CosmeticItem> selection = new ArrayList<>();
        selection.addAll(selectByRarity(pool, Rarity.COMMON, 2));
        selection.addAll(selectByRarity(pool, Rarity.RARE, 2));
        selection.addAll(selectByRarity(pool, Rarity.EPIC, 1));
        selection.addAll(selectByRarity(pool, Rarity.LEGENDARY, 1));
        
        return selection;
    }
}
```

---

## Purchase Cosmetic

```java
public void purchaseCosmetic(UUID playerId, UUID cosmeticId) {
    CosmeticItem cosmetic = cosmeticRepository.findById(cosmeticId)
        .orElseThrow();
    
    // Check if already owned
    if (playerOwnsCosmetic(playerId, cosmeticId)) {
        throw new AlreadyOwnedException();
    }
    
    // Check if purchasable
    if (!cosmetic.isPurchasable()) {
        throw new NotPurchasableException();
    }
    
    // Check currency
    if (!hasCurrency(playerId, cosmetic.getCurrency(), cosmetic.getPrice())) {
        throw new InsufficientCurrencyException();
    }
    
    // Deduct currency
    deductCurrency(playerId, cosmetic.getCurrency(), cosmetic.getPrice());
    
    // Grant cosmetic
    PlayerCosmetic playerCosmetic = new PlayerCosmetic();
    playerCosmetic.setPlayerId(playerId);
    playerCosmetic.setCosmeticId(cosmeticId);
    playerCosmetic.setSource("PURCHASE");
    
    playerCosmeticRepository.save(playerCosmetic);
    
    // Notify
    notificationService.send(playerId, 
        new CosmeticUnlockedNotification(cosmetic));
    
    // Analytics
    analyticsService.trackCosmeticPurchase(playerId, cosmeticId, cosmetic.getPrice());
    
    log.info("Cosmetic purchased: player={}, cosmetic={}", playerId, cosmeticId);
}
```

---

## Equip Cosmetic

```java
public void equipCosmetic(UUID playerId, UUID cosmeticId, String slot) {
    // Verify ownership
    if (!playerOwnsCosmetic(playerId, cosmeticId)) {
        throw new NotOwnedException();
    }
    
    CosmeticItem cosmetic = cosmeticRepository.findById(cosmeticId)
        .orElseThrow();
    
    // Get equipped cosmetics
    PlayerEquippedCosmetics equipped = getOrCreateEquipped(playerId);
    
    // Equip to appropriate slot
    switch (cosmetic.getCosmeticType()) {
        case "CHARACTER_SKIN" -> equipped.setCharacterSkinId(cosmeticId);
        case "WEAPON_SKIN" -> equipped.setWeaponSkinId(cosmeticId);
        case "TITLE" -> equipped.setTitleId(cosmeticId);
        case "NAME_PLATE" -> equipped.setNamePlateId(cosmeticId);
        case "EMOTE" -> equipEmote(equipped, cosmeticId, slot);
    }
    
    equipped.setUpdatedAt(Instant.now());
    equippedRepository.save(equipped);
    
    // Update usage stats
    incrementUsageCount(playerId, cosmeticId);
}
```

---

## API Endpoints

**GET `/api/v1/cosmetics/shop/daily`** - daily shop

**GET `/api/v1/cosmetics/owned`** - owned cosmetics

**POST `/api/v1/cosmetics/purchase`** - purchase cosmetic

**POST `/api/v1/cosmetics/equip`** - equip cosmetic

**GET `/api/v1/cosmetics/equipped`** - equipped cosmetics

---

## Связанные документы

- [Battle Pass System](../battle-pass/battle-pass-system.md)
- [Achievement System](../achievement/achievement-core.md)
- [Premium Currency](../../economy/premium-currency.md)
