# Loot System - Part 1: Loot Generation

**Статус:** approved  
**Версия:** 1.0.1  
**Дата:** 2025-11-07 02:18  
**api-readiness:** ready

[Навигация](./README.md) | [Part 2 →](./part2-advanced-loot.md)

---

## Краткое описание

**Loot System** - критически важная система для генерации и распределения добычи. Без этой системы в игре нет progression.

**Ключевые возможности:**
- ✅ Loot generation (из loot tables с весами)
- ✅ Loot drops (NPC death, containers)
- ✅ Loot distribution (solo, party, raid)
- ✅ Roll system (need/greed/pass)
- ✅ Personal vs shared loot
- ✅ Boss loot (гарантированный + RNG)

---

## Архитектура системы

### Loot Flow

```
NPC/Container Death/Open
    ↓
Generate Loot (from loot tables)
    ↓
Determine Loot Mode (personal/shared)
    ↓
IF PERSONAL:
    Each player gets own loot
    → Directly to inventory
    
IF SHARED:
    Create world drop
    → Players roll (need/greed/pass)
    → Winner gets item
    
Boss Loot:
    Guaranteed items for all
    + Random items (shared/rolled)
```

---

## Database Schema

### Таблица `loot_tables`

```sql
CREATE TABLE loot_tables (
    id VARCHAR(100) PRIMARY KEY,
    
    -- Название
    table_name VARCHAR(200) NOT NULL,
    
    -- Тип
    table_type VARCHAR(50) NOT NULL,
    -- NPC_LOOT, CONTAINER_LOOT, BOSS_LOOT, QUEST_REWARD
    
    -- Связь с источником
    source_id VARCHAR(100), -- NPC ID, Container ID, Boss ID
    
    -- Min/Max items to drop
    min_items INTEGER DEFAULT 1,
    max_items INTEGER DEFAULT 3,
    
    -- Currency
    min_eddies INTEGER DEFAULT 0,
    max_eddies INTEGER DEFAULT 100,
    
    -- Entries (items)
    entries JSONB NOT NULL,
    -- [
    --   {item_template_id: "weapon_pistol", weight: 10, min_qty: 1, max_qty: 1},
    --   {item_template_id: "ammo_pistol", weight: 50, min_qty: 10, max_qty: 50},
    --   ...
    -- ]
    
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_loot_tables_source ON loot_tables(source_id);
CREATE INDEX idx_loot_tables_type ON loot_tables(table_type);
```

### Таблица `world_drops`

```sql
CREATE TABLE world_drops (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    
    -- Location
    zone_id VARCHAR(100) NOT NULL,
    position_x DECIMAL(10,2) NOT NULL,
    position_y DECIMAL(10,2) NOT NULL,
    position_z DECIMAL(10,2) NOT NULL,
    
    -- Source
    source_type VARCHAR(50) NOT NULL,
    -- NPC_DEATH, CONTAINER_OPEN, PLAYER_DROP
    
    source_id VARCHAR(100), -- NPC ID, Container ID
    
    -- Items
    items JSONB NOT NULL,
    
    -- Ownership (для personal loot)
    owner_character_id UUID, -- Если personal loot
    party_id UUID, -- Если party loot
    
    -- Loot mode
    loot_mode VARCHAR(20) DEFAULT 'FREE_FOR_ALL',
    -- FREE_FOR_ALL, PERSONAL, PARTY_ROLL, MASTER_LOOTER
    
    -- Status
    status VARCHAR(20) DEFAULT 'ACTIVE',
    -- ACTIVE, LOOTED, EXPIRED
    
    -- Timestamps
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NOT NULL, -- Auto-cleanup after 5 minutes
    looted_at TIMESTAMP,
    
    CONSTRAINT fk_drop_owner FOREIGN KEY (owner_character_id) 
        REFERENCES characters(id) ON DELETE SET NULL,
    CONSTRAINT fk_drop_party FOREIGN KEY (party_id) 
        REFERENCES parties(id) ON DELETE SET NULL
);

CREATE INDEX idx_drops_zone ON world_drops(zone_id, status);
CREATE INDEX idx_drops_owner ON world_drops(owner_character_id, status);
CREATE INDEX idx_drops_party ON world_drops(party_id, status);
CREATE INDEX idx_drops_expires ON world_drops(expires_at) WHERE status = 'ACTIVE';
```

---

## Loot Generation

### Генерация лута из таблицы

```java
@Service
public class LootService {
    
    @Autowired
    private LootTableRepository lootTableRepository;
    
    @Autowired
    private ItemTemplateRepository itemTemplateRepository;
    
    public LootResult generateLoot(String lootTableId, LootContext context) {
        // 1. Получить loot table
        LootTable table = lootTableRepository.findById(lootTableId)
            .orElseThrow(() -> new LootTableNotFoundException());
        
        // 2. Определить количество предметов
        int itemCount = random.nextInt(
            table.getMaxItems() - table.getMinItems() + 1
        ) + table.getMinItems();
        
        // 3. Сгенерировать предметы
        List<LootItem> items = new ArrayList<>();
        
        for (int i = 0; i < itemCount; i++) {
            LootItem item = rollItem(table, context);
            if (item != null) {
                items.add(item);
            }
        }
        
        // 4. Сгенерировать валюту
        int eddies = 0;
        if (table.getMaxEddies() > 0) {
            eddies = random.nextInt(
                table.getMaxEddies() - table.getMinEddies() + 1
            ) + table.getMinEddies();
            
            // Применить multipliers
            eddies = (int) (eddies * context.getEddiesMultiplier());
        }
        
        if (eddies > 0) {
            items.add(new LootItem("eddies", eddies));
        }
        
        return new LootResult(items);
    }
    
    private LootItem rollItem(LootTable table, LootContext context) {
        // 1. Получить entries
        List<LootEntry> entries = table.getEntries();
        
        // 2. Применить luck modifier
        entries = applyLuckModifier(entries, context.getLuckModifier());
        
        // 3. Weighted random selection
        int totalWeight = entries.stream()
            .mapToInt(LootEntry::getWeight)
            .sum();
        
        int roll = random.nextInt(totalWeight);
        int current = 0;
        
        for (LootEntry entry : entries) {
            current += entry.getWeight();
            if (roll < current) {
                // Выбран этот item
                int quantity = random.nextInt(
                    entry.getMaxQuantity() - entry.getMinQuantity() + 1
                ) + entry.getMinQuantity();
                
                return new LootItem(entry.getItemTemplateId(), quantity);
            }
        }
        
        return null;
    }
    
    private List<LootEntry> applyLuckModifier(List<LootEntry> entries, double luckMod) {
        // Luck увеличивает шанс редких предметов
        return entries.stream()
            .map(entry -> {
                ItemTemplate template = itemTemplateRepository
                    .findById(entry.getItemTemplateId()).get();
                
                int adjustedWeight = entry.getWeight();
                
                // Увеличиваем вес для редких предметов
                switch (template.getRarity()) {
                    case RARE:
                        adjustedWeight *= (1 + luckMod * 0.1);
                        break;
                    case EPIC:
                        adjustedWeight *= (1 + luckMod * 0.2);
                        break;
                    case LEGENDARY:
                        adjustedWeight *= (1 + luckMod * 0.3);
                        break;
                }
                
                return entry.withWeight(adjustedWeight);
            })
            .collect(Collectors.toList());
    }
}
```

---

## Solo Loot (личный лут)

```java
@Transactional
public void createSoloLoot(
    UUID characterId,
    String lootTableId,
    LootSource source
) {
    // 1. Сгенерировать лут
    Character character = characterRepository.findById(characterId).get();
    
    LootContext context = new LootContext();
    context.setKillerId(characterId);
    context.setKillerLevel(character.getLevel());
    context.setLuckModifier(getLuckModifier(character));
    
    LootResult loot = generateLoot(lootTableId, context);
    
    // 2. Создать world drop (personal)
    WorldDrop drop = new WorldDrop();
    drop.setZoneId(character.getCurrentZone());
    drop.setPosition(getCharacterPosition(characterId));
    drop.setSourceType(source.getType());
    drop.setSourceId(source.getId());
    drop.setItems(loot.getItems());
    drop.setOwnerCharacterId(characterId); // Personal ownership
    drop.setLootMode(LootMode.PERSONAL);
    drop.setExpiresAt(Instant.now().plus(Duration.ofMinutes(5)));
    
    worldDropRepository.save(drop);
    
    // 3. Уведомить игрока
    notificationService.send(
        getAccountId(characterId),
        new LootDroppedNotification(loot.getItems().size())
    );
    
    log.info("Created personal loot drop {} for character {}", drop.getId(), characterId);
}
```

---

## Party Loot (групповой лут)

```java
@Transactional
public void createPartyLoot(
    UUID partyId,
    String lootTableId,
    LootSource source,
    Vector3 position,
    String zoneId
) {
    // 1. Получить party
    Party party = partyRepository.findById(partyId)
        .orElseThrow(() -> new PartyNotFoundException());
    
    // 2. Определить loot mode
    LootMode lootMode = party.getLootMode();
    
    if (lootMode == LootMode.PERSONAL) {
        // Каждый участник получает свой лут
        for (UUID memberId : party.getMembers()) {
            createSoloLoot(memberId, lootTableId, source);
        }
        return;
    }
    
    // 3. Сгенерировать общий лут
    UUID leaderId = party.getLeaderId();
    Character leader = characterRepository.findById(leaderId).get();
    
    LootContext context = new LootContext();
    context.setKillerId(leaderId);
    context.setKillerLevel(getAveragePartyLevel(party));
    context.setLuckModifier(getAveragePartyLuck(party));
    
    LootResult loot = generateLoot(lootTableId, context);
    
    // 4. Создать shared world drop
    WorldDrop drop = new WorldDrop();
    drop.setZoneId(zoneId);
    drop.setPosition(position);
    drop.setSourceType(source.getType());
    drop.setSourceId(source.getId());
    drop.setItems(loot.getItems());
    drop.setPartyId(partyId);
    drop.setLootMode(lootMode);
    drop.setExpiresAt(Instant.now().plus(Duration.ofMinutes(5)));
    
    worldDropRepository.save(drop);
    
    // 5. Создать rolls для редких предметов
    for (LootItem item : loot.getItems()) {
        ItemTemplate template = itemTemplateRepository.findById(item.getTemplateId()).get();
        
        // Редкие предметы требуют roll
        if (template.getRarity().ordinal() >= Rarity.RARE.ordinal()) {
            createLootRoll(drop.getId(), item.getTemplateId(), partyId);
        }
    }
    
    // 6. Уведомить party
    notifyParty(partyId, new LootDroppedNotification(loot.getItems().size()));
    
    log.info("Created party loot drop {} for party {}", drop.getId(), partyId);
}
```

---

[Part 2: Advanced Loot →](./part2-advanced-loot.md)

---

## История изменений

- v1.0.1 (2025-11-07 02:18) - Создан с полным Java кодом (generation, solo, party)
- v1.0.0 (2025-11-07) - Создан


