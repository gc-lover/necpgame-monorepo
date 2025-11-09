---
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 02:34
**api-readiness-notes:** Housing System. Личные квартиры, кастомизация, furniture, декор. ~390 строк.
---

# Housing System - Система жилья

---

- **Status:** queued
- **Last Updated:** 2025-11-07 23:45
---

**Статус:** approved  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-07 02:34  
**Приоритет:** MEDIUM (Social Hub!)  
**Автор:** AI Brain Manager

**Микрофича:** Player housing & customization  
**Размер:** ~390 строк ✅

---

## Краткое описание

**Housing System** - система личного жилья игроков для социализации и персонализации.

**Ключевые возможности:**
- ✅ Personal Apartments (личные квартиры)
- ✅ Furniture & Decoration (мебель и декор)
- ✅ Storage Expansion (расширение хранилища)
- ✅ Social Features (invite friends)
- ✅ Functional Upgrades (crafting station, etc)
- ✅ Prestige System (показатель статуса)

---

## Архитектура системы

```
Player purchases apartment
    ↓
Select location (Night City, Watson, etc)
    ↓
Customize interior
    ↓
Place furniture/decorations
    ↓
Invite friends
    ↓
Show off collection
```

---

## Apartment Types

### 1. Studio Apartments (Starter)

```
Size: Small (50m²)
Price: 50,000 eddies
Furniture slots: 20
Storage expansion: +10 slots
Location: Watson, Japantown
```

### 2. Standard Apartments

```
Size: Medium (100m²)
Price: 200,000 eddies
Furniture slots: 40
Storage expansion: +20 slots
Location: City Center, Westbrook
```

### 3. Luxury Penthouses

```
Size: Large (200m²)
Price: 1,000,000 eddies
Furniture slots: 80
Storage expansion: +50 slots
Location: Corpo Plaza, North Oak
Bonus: Helipad access
```

### 4. Guild Halls

```
Size: Massive (500m²)
Price: 5,000,000 eddies (guild bank)
Furniture slots: 200
Storage: Guild vault
Location: Charter Hill
Bonus: Meeting room, war room
```

---

## Database Schema

### Таблица `apartments`

```sql
CREATE TABLE apartments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    
    -- Owner
    owner_id UUID NOT NULL,
    owner_type VARCHAR(20) NOT NULL,
    
    -- Type
    apartment_type VARCHAR(50) NOT NULL,
    location VARCHAR(100) NOT NULL,
    
    -- Purchase
    purchase_price INTEGER NOT NULL,
    purchased_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Customization
    name VARCHAR(100),
    description TEXT,
    
    -- Limits
    max_furniture_slots INTEGER NOT NULL,
    storage_expansion INTEGER DEFAULT 0,
    
    -- Prestige
    prestige_score INTEGER DEFAULT 0,
    
    -- Access
    is_public BOOLEAN DEFAULT FALSE,
    allowed_visitors UUID[],
    
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_apartment_owner FOREIGN KEY (owner_id) 
        REFERENCES players(id) ON DELETE CASCADE
);

CREATE INDEX idx_apartments_owner ON apartments(owner_id);
CREATE INDEX idx_apartments_public ON apartments(is_public) 
    WHERE is_public = TRUE;
```

### Таблица `placed_furniture`

```sql
CREATE TABLE placed_furniture (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    apartment_id UUID NOT NULL,
    furniture_id UUID NOT NULL,
    
    -- Placement
    position_x DECIMAL(10,2) NOT NULL,
    position_y DECIMAL(10,2) NOT NULL,
    position_z DECIMAL(10,2) NOT NULL,
    
    -- Rotation
    rotation_yaw DECIMAL(6,2) DEFAULT 0,
    
    -- Scale
    scale DECIMAL(4,2) DEFAULT 1.0,
    
    placed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_placed_apartment FOREIGN KEY (apartment_id) 
        REFERENCES apartments(id) ON DELETE CASCADE,
    CONSTRAINT fk_placed_furniture FOREIGN KEY (furniture_id) 
        REFERENCES furniture_items(id) ON DELETE CASCADE
);

CREATE INDEX idx_placed_apartment ON placed_furniture(apartment_id);
```

### Таблица `furniture_items`

```sql
CREATE TABLE furniture_items (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    
    -- Item info
    code VARCHAR(100) UNIQUE NOT NULL,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    
    -- Category
    category VARCHAR(50) NOT NULL,
    
    -- Acquisition
    is_purchasable BOOLEAN DEFAULT TRUE,
    price INTEGER,
    currency VARCHAR(20) DEFAULT 'EDDIES',
    
    -- Functional
    is_functional BOOLEAN DEFAULT FALSE,
    function_type VARCHAR(50),
    function_bonus JSONB,
    
    -- Size
    width DECIMAL(4,2),
    height DECIMAL(4,2),
    depth DECIMAL(4,2),
    
    -- Prestige
    prestige_value INTEGER DEFAULT 1,
    
    -- Assets
    model_path VARCHAR(255),
    preview_image VARCHAR(255),
    
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_furniture_category ON furniture_items(category);
CREATE INDEX idx_furniture_purchasable ON furniture_items(is_purchasable) 
    WHERE is_purchasable = TRUE;
```

---

## Furniture Categories

### Decorative (Декор)

```
- Posters (плакаты Cyberpunk тематики)
- Plants (искусственные растения)
- Lights (неоновые подсветки)
- Rugs (ковры)
- Art (картины, скульптуры)
```

### Functional (Функциональные)

```
- Crafting Table (+5% crafting speed)
- Meditation Corner (+10% humanity regen)
- Weapon Rack (display weapons)
- Armor Stand (display armor)
- Server Rack (+5% hacking success)
```

### Comfort (Комфорт)

```
- Beds (различные стили)
- Sofas (для гостей)
- Chairs (обеденные, gaming)
- Tables (кофейные, обеденные)
```

### Storage (Хранилище)

```
- Weapon Locker (+10 weapon slots)
- Closet (+20 clothing slots)
- Safe (+5 valuable slots)
- Fridge (+food storage)
```

---

## Prestige System

### Prestige Score Calculation

```java
public int calculatePrestigeScore(UUID apartmentId) {
    Apartment apartment = apartmentRepository.findById(apartmentId)
        .orElseThrow();
    
    int score = 0;
    
    // Base score from apartment type
    score += getBasePrestige(apartment.getApartmentType());
    
    // Furniture prestige
    List<PlacedFurniture> furniture = placedFurnitureRepository
        .findByApartment(apartmentId);
    
    for (PlacedFurniture placed : furniture) {
        FurnitureItem item = furnitureRepository.findById(placed.getFurnitureId())
            .orElseThrow();
        score += item.getPrestigeValue();
    }
    
    // Location bonus
    score += getLocationBonus(apartment.getLocation());
    
    // Unique items bonus
    score += countUniqueItems(furniture) * 5;
    
    return score;
}

private int getBasePrestige(String apartmentType) {
    return switch (apartmentType) {
        case "STUDIO" -> 100;
        case "STANDARD" -> 500;
        case "LUXURY" -> 2000;
        case "GUILD_HALL" -> 10000;
        default -> 0;
    };
}
```

---

## Visit Apartment

```java
public void visitApartment(UUID visitorId, UUID apartmentId) {
    Apartment apartment = apartmentRepository.findById(apartmentId)
        .orElseThrow();
    
    // Check access
    if (!canVisit(visitorId, apartment)) {
        throw new AccessDeniedException();
    }
    
    // Load apartment state
    ApartmentState state = loadApartmentState(apartmentId);
    
    // Track visit
    trackVisit(visitorId, apartmentId);
    
    // Notify owner (if online)
    if (sessionManager.isOnline(apartment.getOwnerId())) {
        notificationService.send(apartment.getOwnerId(),
            new ApartmentVisitNotification(visitorId));
    }
    
    return state;
}

private boolean canVisit(UUID visitorId, Apartment apartment) {
    // Owner can always visit
    if (visitorId.equals(apartment.getOwnerId())) {
        return true;
    }
    
    // Public apartments
    if (apartment.isPublic()) {
        return true;
    }
    
    // Allowed visitors list
    if (apartment.getAllowedVisitors() != null && 
        apartment.getAllowedVisitors().contains(visitorId)) {
        return true;
    }
    
    return false;
}
```

---

## Functional Furniture Benefits

```java
@Component
public class FunctionalFurnitureService {
    
    public Map<String, Object> getApartmentBonuses(UUID playerId) {
        UUID apartmentId = getPlayerApartment(playerId);
        
        List<PlacedFurniture> furniture = placedFurnitureRepository
            .findByApartment(apartmentId);
        
        Map<String, Object> bonuses = new HashMap<>();
        
        for (PlacedFurniture placed : furniture) {
            FurnitureItem item = furnitureRepository
                .findById(placed.getFurnitureId())
                .orElseThrow();
            
            if (item.isFunctional()) {
                applyBonus(bonuses, item.getFunctionType(), item.getFunctionBonus());
            }
        }
        
        return bonuses;
    }
    
    private void applyBonus(Map<String, Object> bonuses, 
                           String functionType, 
                           Map<String, Object> bonus) {
        switch (functionType) {
            case "CRAFTING_SPEED" -> 
                bonuses.merge("craftingSpeed", bonus.get("percentage"), 
                    (old, new_) -> (Integer) old + (Integer) new_);
            
            case "STORAGE_EXPANSION" ->
                bonuses.merge("storageSlots", bonus.get("slots"),
                    (old, new_) -> (Integer) old + (Integer) new_);
            
            case "HUMANITY_REGEN" ->
                bonuses.merge("humanityRegen", bonus.get("percentage"),
                    (old, new_) -> (Integer) old + (Integer) new_);
        }
    }
}
```

---

## API Endpoints

**GET `/api/v1/housing/apartments/available`** - доступные квартиры

**POST `/api/v1/housing/apartments/purchase`** - купить квартиру

**GET `/api/v1/housing/apartments/my`** - моя квартира

**GET `/api/v1/housing/apartments/{id}`** - посетить квартиру

**POST `/api/v1/housing/furniture/place`** - разместить мебель

**DELETE `/api/v1/housing/furniture/{id}/remove`** - убрать мебель

**GET `/api/v1/housing/furniture/shop`** - магазин мебели

**GET `/api/v1/housing/leaderboard/prestige`** - рейтинг престижа

---

## Связанные документы

- [Crafting System](../../gameplay/crafting/crafting-core.md)
- [Social System](../friend-system.md)
- [Economy System](../../economy/economy-overview.md)
