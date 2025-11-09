# Inventory System - Part 1: Core System

**Статус:** approved  
**Версия:** 1.0.1  
**Дата:** 2025-11-07 02:15  
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-09 01:30  
**api-readiness-notes:** Перепроверено 2025-11-09 01:30. Core storage, equipment, схемы БД и процессы готовы для API economy-service.
**Последнее обновление:** 2025-11-09 01:30  
**target-domain:** economy-inventory  
**target-microservice:** economy-service (8085)  
**target-frontend-module:** modules/economy/inventory

---
**API Tasks Status:**
- Status: completed
- Tasks:
  - API-TASK-100: Inventory Core API — `api/v1/economy/inventory/inventory-core/inventory-core.yaml`
    - Создано: 2025-11-09 18:05
    - Завершено: 2025-11-09 20:22
    - Доп. файлы: `inventory-core-models.yaml`, `inventory-core-models-operations.yaml`, `README.md`
    - Файл задачи: `API-SWAGGER/tasks/completed/2025-11-09/task-100-inventory-core-api.md`
- Last Updated: 2025-11-09 20:22
---

[Навигация](./README.md) | [Part 2 →](./part2-advanced-features.md)

---

## Краткое описание

**Inventory System** - критически важная система для управления предметами игроков. Без этой системы игра не может работать.

**Ключевые возможности:**
- ✅ Inventory slots (ограниченное количество слотов)
- ✅ Item stacking (складывание однотипных предметов)
- ✅ Weight/Encumbrance system (вес и перегрузка)
- ✅ Item pickup/drop
- ✅ Item use/consume
- ✅ Equipment slots (weapon, armor, implants)
- ✅ Bank/Stash storage (дополнительное хранилище)
- ✅ Transfer items (trade, mail, auction)

---

## Архитектура системы

### Inventory Structure

```
CHARACTER
    │
    ├── BACKPACK (Inventory)
    │   ├─ Slot 1: Weapon (stackable: no)
    │   ├─ Slot 2: Ammo x250 (stackable: yes)
    │   ├─ Slot 3: Medikit x5 (stackable: yes)
    │   ├─ ...
    │   └─ Slot 50: (empty)
    │
    ├── EQUIPMENT (Equipped items)
    │   ├─ Weapon Slot 1: Mantis Blades
    │   ├─ Weapon Slot 2: Pistol
    │   ├─ Armor: Corpo Suit
    │   ├─ Implants: [Kerenzikov, Sandevistan, ...]
    │   └─ Cyberware: [Optical Implant, Gorilla Arms, ...]
    │
    └── BANK/STASH (Shared storage)
        ├─ Slot 1: Materials x999
        ├─ Slot 2: Rare Item
        ├─ ...
        └─ Slot 100: (empty)
```

---

## Database Schema

### Таблица `character_inventory`

```sql
CREATE TABLE character_inventory (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    character_id UUID NOT NULL,
    
    -- Инвентарь
    max_slots INTEGER DEFAULT 50,
    current_weight DECIMAL(10,2) DEFAULT 0,
    max_weight DECIMAL(10,2) DEFAULT 200.00, -- Зависит от Body attribute
    
    -- Timestamps
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_inventory_character FOREIGN KEY (character_id) 
        REFERENCES characters(id) ON DELETE CASCADE,
    UNIQUE(character_id)
);
```

### Таблица `character_items`

```sql
CREATE TABLE character_items (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    character_id UUID NOT NULL,
    
    -- Item reference
    item_template_id VARCHAR(100) NOT NULL, -- ID шаблона предмета
    
    -- Location
    storage_type VARCHAR(20) NOT NULL DEFAULT 'BACKPACK',
    -- BACKPACK, EQUIPPED, BANK, STASH
    
    slot_index INTEGER, -- Позиция в инвентаре (0-49 for backpack, 0-99 for bank)
    equipment_slot VARCHAR(50), -- Если EQUIPPED: WEAPON_1, WEAPON_2, ARMOR, etc
    
    -- Quantity (для stackable items)
    quantity INTEGER DEFAULT 1,
    
    -- Durability (для equipment)
    current_durability INTEGER DEFAULT 100,
    max_durability INTEGER DEFAULT 100,
    
    -- Binding
    bind_type VARCHAR(20) DEFAULT 'NONE',
    -- NONE, BIND_ON_PICKUP, BIND_ON_EQUIP, BIND_TO_ACCOUNT
    
    is_bound BOOLEAN DEFAULT FALSE,
    bound_at TIMESTAMP,
    
    -- Modifiers (для уникальных предметов)
    modifiers JSONB, -- {damage_bonus: +10, crit_chance: +5%}
    
    -- Enchantments / Upgrades
    enchantments JSONB DEFAULT '[]',
    upgrade_level INTEGER DEFAULT 0,
    
    -- Acquisition
    acquired_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    acquired_from VARCHAR(100), -- LOOT, CRAFT, TRADE, QUEST, PURCHASE
    
    -- Trading
    is_tradeable BOOLEAN DEFAULT TRUE,
    is_sellable BOOLEAN DEFAULT TRUE,
    is_deletable BOOLEAN DEFAULT TRUE,
    
    -- Timestamps
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_item_character FOREIGN KEY (character_id) 
        REFERENCES characters(id) ON DELETE CASCADE
);

CREATE INDEX idx_items_character ON character_items(character_id);
CREATE INDEX idx_items_template ON character_items(item_template_id);
CREATE INDEX idx_items_storage ON character_items(character_id, storage_type);
CREATE INDEX idx_items_slot ON character_items(character_id, storage_type, slot_index);
```

### Таблица `item_templates`

```sql
CREATE TABLE item_templates (
    id VARCHAR(100) PRIMARY KEY,
    
    -- Basic info
    item_name VARCHAR(200) NOT NULL,
    item_type VARCHAR(50) NOT NULL,
    -- WEAPON, ARMOR, CONSUMABLE, MATERIAL, QUEST_ITEM, CYBERWARE, IMPLANT, etc
    
    item_subtype VARCHAR(50), -- PISTOL, RIFLE, MELEE, etc
    
    -- Rarity
    rarity VARCHAR(20) DEFAULT 'COMMON',
    -- COMMON, UNCOMMON, RARE, EPIC, LEGENDARY, UNIQUE
    
    -- Stacking
    is_stackable BOOLEAN DEFAULT FALSE,
    max_stack_size INTEGER DEFAULT 1,
    
    -- Weight
    weight DECIMAL(6,2) DEFAULT 1.00,
    
    -- Value
    vendor_price INTEGER DEFAULT 0,
    
    -- Requirements
    required_level INTEGER DEFAULT 1,
    required_attributes JSONB, -- {body: 5, reflexes: 8}
    
    -- Stats (для equipment)
    stats JSONB, -- {damage: 50, armor: 100, etc}
    
    -- Effects (для consumables)
    effects JSONB, -- [{type: "HEAL", value: 50}]
    
    -- Icon
    icon_url VARCHAR(500),
    
    -- Description
    description TEXT,
    lore_text TEXT,
    
    -- Flags
    is_unique BOOLEAN DEFAULT FALSE,
    is_quest_item BOOLEAN DEFAULT FALSE,
    
    -- Binding
    bind_on_pickup BOOLEAN DEFAULT FALSE,
    bind_on_equip BOOLEAN DEFAULT FALSE,
    
    -- Trading
    is_tradeable BOOLEAN DEFAULT TRUE,
    is_sellable BOOLEAN DEFAULT TRUE,
    is_deletable BOOLEAN DEFAULT TRUE,
    
    -- Durability
    has_durability BOOLEAN DEFAULT FALSE,
    base_durability INTEGER DEFAULT 100,
    
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_templates_type ON item_templates(item_type);
CREATE INDEX idx_templates_rarity ON item_templates(rarity);
```

---

## Item Pickup

### Подбор предмета с земли

```java
@Service
public class InventoryService {
    
    @Transactional
    public PickupResponse pickupItem(
        UUID characterId,
        String itemTemplateId,
        int quantity
    ) {
        // 1. Получить инвентарь
        CharacterInventory inventory = inventoryRepository
            .findByCharacter(characterId)
            .orElseThrow(() -> new InventoryNotFoundException());
        
        // 2. Получить шаблон предмета
        ItemTemplate template = itemTemplateRepository.findById(itemTemplateId)
            .orElseThrow(() -> new ItemTemplateNotFoundException());
        
        // 3. Проверить вес
        double totalWeight = template.getWeight() * quantity;
        if (inventory.getCurrentWeight() + totalWeight > inventory.getMaxWeight()) {
            throw new OverweightException(
                "Cannot carry more weight. Drop some items or increase Body attribute."
            );
        }
        
        // 4. Проверить requirements
        if (!meetsRequirements(characterId, template)) {
            throw new RequirementsNotMetException(
                "You don't meet the requirements for this item"
            );
        }
        
        // 5. Если stackable, попробовать добавить к существующему стеку
        if (template.isStackable()) {
            Optional<CharacterItem> existingStack = findExistingStack(
                characterId,
                itemTemplateId
            );
            
            if (existingStack.isPresent()) {
                CharacterItem item = existingStack.get();
                
                int newQuantity = item.getQuantity() + quantity;
                if (newQuantity > template.getMaxStackSize()) {
                    // Стек переполнен, создаем новый
                    int overflow = newQuantity - template.getMaxStackSize();
                    item.setQuantity(template.getMaxStackSize());
                    itemRepository.save(item);
                    
                    // Создать новый стек для overflow
                    return pickupItem(characterId, itemTemplateId, overflow);
                } else {
                    item.setQuantity(newQuantity);
                    itemRepository.save(item);
                    
                    return new PickupResponse(item.getId(), "Added to existing stack");
                }
            }
        }
        
        // 6. Найти свободный слот
        int freeSlot = findFreeSlot(characterId, StorageType.BACKPACK);
        if (freeSlot == -1) {
            throw new InventoryFullException("No free inventory slots");
        }
        
        // 7. Создать предмет
        CharacterItem item = new CharacterItem();
        item.setCharacterId(characterId);
        item.setItemTemplateId(itemTemplateId);
        item.setStorageType(StorageType.BACKPACK);
        item.setSlotIndex(freeSlot);
        item.setQuantity(quantity);
        item.setAcquiredFrom("LOOT");
        
        // Bind-on-pickup
        if (template.isBindOnPickup()) {
            item.setBindType(BindType.BIND_ON_PICKUP);
            item.setBound(true);
            item.setBoundAt(Instant.now());
        }
        
        item = itemRepository.save(item);
        
        // 8. Обновить вес
        inventory.setCurrentWeight(inventory.getCurrentWeight() + totalWeight);
        inventoryRepository.save(inventory);
        
        // 9. Логировать
        log.info("Character {} picked up {} x{}", characterId, itemTemplateId, quantity);
        
        // 10. Уведомить
        notificationService.send(
            getAccountId(characterId),
            new ItemPickedUpNotification(template.getItemName(), quantity)
        );
        
        return new PickupResponse(item.getId(), "Item picked up");
    }
}
```

---

[Part 2: Advanced Features →](./part2-advanced-features.md)

---

## История изменений

- v1.0.1 (2025-11-07 02:15) - Создан с полным Java кодом (схемы, pickup logic)
- v1.0.0 (2025-11-07) - Создан

