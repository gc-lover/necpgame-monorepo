# Inventory System - Part 2: Advanced Features

**Статус:** approved  
**Версия:** 1.0.1  
**Дата:** 2025-11-07 02:16  
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-08 23:39  
**api-readiness-notes:** Advanced механики (перегруз, auto-loot, качество, ограничения) описаны полно.

[← Part 1](./part1-core-system.md) | [Навигация](./README.md)

---

## Weight & Encumbrance

### Система перегрузки

**Механика:**
```
Current Weight / Max Weight

< 50%:  Normal (100% speed)
50-75%: Slightly Encumbered (90% speed)
75-90%: Encumbered (75% speed, -10% dodge)
90-100%: Heavily Encumbered (50% speed, -20% dodge, cannot sprint)
> 100%: Overweight (cannot move, cannot pickup items)
```

**Max Weight calculation:**
```java
public double calculateMaxWeight(Character character) {
    int body = character.getAttributes().get("body");
    
    // Base: 50kg + Body * 15kg
    double baseWeight = 50.0 + (body * 15.0);
    
    // Bonuses from perks
    double perkBonus = getPerkBonus(character, "CARRY_WEIGHT");
    
    // Bonuses from equipment
    double equipmentBonus = getEquipmentBonus(character, "CARRY_WEIGHT");
    
    return baseWeight + perkBonus + equipmentBonus;
}
```

---

## Auto-Sort & Auto-Loot

### Auto-Sort

```java
public void autoSortInventory(UUID characterId) {
    List<CharacterItem> items = itemRepository
        .findByCharacterAndStorage(characterId, StorageType.BACKPACK);
    
    // Сортировка:
    // 1. Quest items first
    // 2. By rarity (Legendary → Common)
    // 3. By type (Weapons, Armor, Consumables, Materials)
    
    items.sort(Comparator
        .comparing(CharacterItem::isQuestItem).reversed()
        .thenComparing(item -> getRarity(item).ordinal(), Comparator.reverseOrder())
        .thenComparing(item -> getItemType(item).ordinal())
    );
    
    // Reassign slot indexes
    for (int i = 0; i < items.size(); i++) {
        items.get(i).setSlotIndex(i);
    }
    
    itemRepository.saveAll(items);
}
```

### Auto-Loot Settings

```java
public class AutoLootSettings {
    private boolean enabled = false;
    private int minRarity = Rarity.UNCOMMON.ordinal(); // Auto-pickup Uncommon+
    private List<ItemType> autoPickupTypes = List.of(
        ItemType.QUEST_ITEM,
        ItemType.MATERIAL
    );
    private boolean skipIfOverweight = true;
}
```

---

## Item Quality & Tiers

### Quality Tiers (для Weapons/Armor)

```
DAMAGED    - 50% stats
WORN       - 75% stats
NORMAL     - 100% stats
FINE       - 110% stats
MASTERWORK - 125% stats
LEGENDARY  - 150% stats
```

**Implementation:**
```java
public Map<String, Integer> getItemStats(CharacterItem item) {
    ItemTemplate template = getTemplate(item);
    Map<String, Integer> baseStats = template.getStats();
    
    // Apply quality modifier
    double qualityMultiplier = getQualityMultiplier(item.getQuality());
    
    // Apply upgrade level bonus
    double upgradeBonus = item.getUpgradeLevel() * 0.05; // +5% per level
    
    // Apply enchantments
    double enchantmentBonus = calculateEnchantmentBonus(item);
    
    double totalMultiplier = qualityMultiplier * (1.0 + upgradeBonus + enchantmentBonus);
    
    return baseStats.entrySet().stream()
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            e -> (int) (e.getValue() * totalMultiplier)
        ));
}
```

---

## Inventory Limits

### Slot Limits

**Base:**
- Backpack: 50 slots
- Bank: 100 slots (shared across account)
- Equipment: 15-20 slots

**Upgrades:**
```java
public int getMaxInventorySlots(Character character) {
    int baseSlots = 50;
    
    // Upgrades from perks
    int perkSlots = getPerkBonus(character, "INVENTORY_SLOTS");
    
    // Premium bonus
    int premiumSlots = character.isPremium() ? 10 : 0;
    
    return baseSlots + perkSlots + premiumSlots;
}
```

---

## API Endpoints Summary

### Core
- **GET** `/api/v1/inventory` - получить инвентарь
- **POST** `/api/v1/inventory/pickup` - подобрать предмет
- **POST** `/api/v1/inventory/drop` - выбросить предмет
- **POST** `/api/v1/inventory/use` - использовать предмет
- **POST** `/api/v1/inventory/sort` - автосортировка

### Equipment
- **POST** `/api/v1/equipment/equip` - надеть предмет
- **POST** `/api/v1/equipment/unequip` - снять предмет
- **GET** `/api/v1/equipment` - получить экипировку

### Bank
- **GET** `/api/v1/bank` - получить содержимое банка
- **POST** `/api/v1/bank/deposit` - положить в банк
- **POST** `/api/v1/bank/withdraw` - взять из банка

### Transfer
- **POST** `/api/v1/inventory/transfer` - передать предмет

### Maintenance
- **POST** `/api/v1/items/{itemId}/repair` - починить предмет
- **POST** `/api/v1/items/{itemId}/upgrade` - улучшить предмет

---

## Performance Optimization

**Caching:**
```java
@Cacheable(value = "inventory", key = "#characterId")
public List<CharacterItem> getInventory(UUID characterId) {
    return itemRepository.findByCharacterAndStorage(
        characterId, 
        StorageType.BACKPACK
    );
}
```

**Batch Operations:**
```java
@Transactional
public void pickupMultipleItems(UUID characterId, List<LootDrop> drops) {
    // Process all pickups in single transaction
    List<CharacterItem> newItems = new ArrayList<>();
    
    for (LootDrop drop : drops) {
        CharacterItem item = createItemFromDrop(characterId, drop);
        newItems.add(item);
    }
    
    itemRepository.saveAll(newItems);
    
    // Single weight update
    updateInventoryWeight(characterId);
}
```

---

## Связанные документы

- [Loot System](../loot-system/README.md)
- [Equipment System](../../../02-gameplay/equipment/)
- [Trading System](../../../02-gameplay/economy/)

---

[← Назад к навигации](./README.md)

---

## История изменений

- v1.0.1 (2025-11-07 02:16) - Создан с полным Java кодом (weight, auto-sort, quality, API)
- v1.0.0 (2025-11-07) - Создан

