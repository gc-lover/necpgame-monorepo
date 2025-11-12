# Loot System - Part 2: Advanced Loot

**Статус:** approved  
**Версия:** 1.0.1  
**Дата:** 2025-11-07 02:19  
**api-readiness:** ready

[← Part 1](./part1-loot-generation.md) | [Навигация](./README.md)

---

- **Status:** queued
- **Last Updated:** 2025-11-08 01:52
---

## Loot Roll System (продолжение)

### Roll Tables Schema

```sql
CREATE TABLE loot_rolls (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    world_drop_id UUID NOT NULL,
    item_template_id VARCHAR(100) NOT NULL,
    
    -- Party context
    party_id UUID NOT NULL,
    
    -- Rolls
    rolls JSONB DEFAULT '[]',
    -- [
    --   {character_id: "uuid", roll_type: "NEED", roll_value: 85},
    --   {character_id: "uuid", roll_type: "GREED", roll_value: 42},
    --   {character_id: "uuid", roll_type: "PASS", roll_value: null},
    --   ...
    -- ]
    
    -- Winner
    winner_character_id UUID,
    winner_roll_type VARCHAR(10),
    winner_roll_value INTEGER,
    
    -- Status
    status VARCHAR(20) DEFAULT 'PENDING',
    -- PENDING, COMPLETED, EXPIRED
    
    -- Timestamps
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP,
    expires_at TIMESTAMP NOT NULL, -- 60 seconds to roll
    
    CONSTRAINT fk_roll_drop FOREIGN KEY (world_drop_id) 
        REFERENCES world_drops(id) ON DELETE CASCADE,
    CONSTRAINT fk_roll_party FOREIGN KEY (party_id) 
        REFERENCES parties(id) ON DELETE CASCADE,
    CONSTRAINT fk_roll_winner FOREIGN KEY (winner_character_id) 
        REFERENCES characters(id) ON DELETE SET NULL
);

CREATE INDEX idx_rolls_drop ON loot_rolls(world_drop_id);
CREATE INDEX idx_rolls_party ON loot_rolls(party_id, status);
CREATE INDEX idx_rolls_expires ON loot_rolls(expires_at) WHERE status = 'PENDING';
```

---

## Smart Loot (умный лут)

### Smart Loot Algorithm

**Цель:** Повысить шансы получить нужные предметы для класса

```java
@Service
public class SmartLootService {
    
    public List<LootItem> applySmartLoot(
        List<LootItem> baseItems,
        Character character
    ) {
        // 1. Получить класс и spec
        String characterClass = character.getCharacterClass();
        String spec = character.getSpecialization();
        
        // 2. Для каждого item, проверить relevance
        return baseItems.stream()
            .map(item -> {
                ItemTemplate template = getTemplate(item.getTemplateId());
                
                // Если item подходит классу, увеличить шанс
                if (isRelevantForClass(template, characterClass, spec)) {
                    // Increase quantity or add duplicate roll
                    return upgradeRelevantItem(item, template);
                }
                
                return item;
            })
            .collect(Collectors.toList());
    }
    
    private boolean isRelevantForClass(
        ItemTemplate template,
        String characterClass,
        String spec
    ) {
        // Check if item has stats/bonuses relevant to class
        Map<String, Integer> stats = template.getStats();
        
        // Для Netrunner: предметы с Intelligence, Hacking
        if (characterClass.equals("Netrunner")) {
            return stats.containsKey("intelligence") || 
                   stats.containsKey("hacking_power");
        }
        
        // Для Solo: предметы с Reflexes, Combat stats
        if (characterClass.equals("Solo")) {
            return stats.containsKey("reflexes") || 
                   stats.containsKey("damage");
        }
        
        // ... etc для других классов
        
        return false;
    }
}
```

---

## Boss Loot (продолжение)

### Guaranteed Boss Loot

```java
@Transactional
public void createBossLoot(
    UUID bossId,
    UUID killerId,
    UUID partyId,
    Vector3 position,
    String zoneId
) {
    // 1. Получить boss loot table
    String bossLootTableId = "boss_" + bossId;
    
    // 2. Гарантированные items для всех
    List<String> guaranteedItems = getBossGuaranteedItems(bossId);
    
    if (partyId != null) {
        Party party = partyRepository.findById(partyId).get();
        
        // Каждый член party получает гарантированный лут
        for (UUID memberId : party.getMembers()) {
            for (String itemId : guaranteedItems) {
                inventoryService.addItem(memberId, itemId, 1, "BOSS_GUARANTEED");
            }
        }
    } else {
        // Solo kill
        for (String itemId : guaranteedItems) {
            inventoryService.addItem(killerId, itemId, 1, "BOSS_GUARANTEED");
        }
    }
    
    // 3. Случайный boss loot (shared, с rollами)
    if (partyId != null) {
        createPartyLoot(partyId, bossLootTableId, 
            new LootSource("BOSS", bossId.toString()), position, zoneId);
    } else {
        createSoloLoot(killerId, bossLootTableId, 
            new LootSource("BOSS", bossId.toString()));
    }
    
    // 4. Опубликовать achievement
    achievementService.checkBossKillAchievement(killerId, bossId);
    
    log.info("Created boss loot for boss {}, killer {}, party {}", 
        bossId, killerId, partyId);
}
```

---

## Anti-Duplicate System

### Prevent Duplicate Legendaries

```java
@Service
public class LootDuplicateChecker {
    
    public boolean hasSimilarLegendary(UUID characterId, String itemTemplateId) {
        ItemTemplate template = itemTemplateRepository.findById(itemTemplateId).get();
        
        if (template.getRarity() != Rarity.LEGENDARY) {
            return false; // Only check for legendaries
        }
        
        // Проверить инвентарь + bank
        List<CharacterItem> allItems = itemRepository
            .findAllByCharacter(characterId);
        
        return allItems.stream()
            .anyMatch(item -> item.getItemTemplateId().equals(itemTemplateId));
    }
    
    public void applyBadLuckProtection(LootResult loot, UUID characterId) {
        // Если игрок давно не получал legendary, повысить шанс
        Instant lastLegendary = getLastLegendaryDropTime(characterId);
        
        if (lastLegendary == null || 
            Duration.between(lastLegendary, Instant.now()).toDays() > 7) {
            
            // Bad luck protection: гарантированный legendary каждые 7 дней
            loot.getItems().add(rollGuaranteedLegendary());
            
            log.info("Bad luck protection activated for character {}", characterId);
        }
    }
}
```

---

## API Endpoints Summary

### Loot Management
- **GET** `/api/v1/loot/drops/nearby` - лут рядом
- **POST** `/api/v1/loot/pickup` - подобрать
- **GET** `/api/v1/loot/history` - история лута

### Loot Rolls
- **POST** `/api/v1/loot/rolls/{rollId}/submit` - сделать roll
- **GET** `/api/v1/loot/rolls/active` - активные роллы

### Auto-Loot
- **GET** `/api/v1/loot/settings` - настройки
- **PUT** `/api/v1/loot/settings` - обновить настройки

---

## Performance Optimization

**Cleanup Job:**
```java
@Scheduled(fixedDelay = 60000) // Every minute
public void cleanupExpiredDrops() {
    List<WorldDrop> expired = worldDropRepository
        .findExpired(Instant.now());
    
    worldDropRepository.deleteAll(expired);
    
    log.info("Cleaned up {} expired loot drops", expired.size());
}
```

**Caching:**
```java
@Cacheable("lootTables")
public LootTable getLootTable(String tableId) {
    return lootTableRepository.findById(tableId)
        .orElseThrow();
}
```

---

## Связанные документы

- [Inventory System](../inventory-system/README.md)
- [Combat System](../../../02-gameplay/combat/)
- [Party System](../../party-system.md)

---

[← Назад к навигации](./README.md)

---

## История изменений

- v1.0.1 (2025-11-07 02:19) - Создан с полным Java кодом (rolls, boss loot, smart loot, anti-duplicate)
- v1.0.0 (2025-11-07) - Создан
