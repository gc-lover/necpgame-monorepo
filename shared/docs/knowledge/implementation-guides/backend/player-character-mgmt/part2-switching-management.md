# Player Character Management - Part 2: Switching & Management

**Статус:** approved  
**Версия:** 1.0.1  
**Дата:** 2025-11-07 02:23  
**api-readiness:** ready

[← Part 1](./part1-creation-deletion.md) | [Навигация](./README.md)

---

- **Status:** queued
- **Last Updated:** 2025-11-08 02:08
---

## Character Switching

### Switch Character

```java
@Transactional
public SwitchCharacterResponse switchCharacter(
    UUID accountId,
    UUID newCharacterId
) {
    // 1. Получить текущую session
    Session session = sessionService.getCurrentSession(accountId);
    
    if (session == null) {
        throw new NoActiveSessionException();
    }
    
    UUID currentCharacterId = session.getCharacterId();
    
    // 2. Сохранить состояние текущего персонажа
    if (currentCharacterId != null) {
        saveCharacterState(currentCharacterId);
    }
    
    // 3. Проверить новый персонаж
    Character newCharacter = characterRepository.findById(newCharacterId)
        .orElseThrow(() -> new CharacterNotFoundException());
    
    if (!newCharacter.getAccountId().equals(accountId)) {
        throw new UnauthorizedAccessException();
    }
    
    if (newCharacter.isDeleted()) {
        throw new CharacterDeletedException();
    }
    
    // 4. Обновить session
    session.setCharacterId(newCharacterId);
    sessionRepository.save(session);
    
    // 5. Загрузить состояние нового персонажа
    loadCharacterState(newCharacterId);
    
    // 6. Обновить last_played_at
    newCharacter.setLastPlayedAt(Instant.now());
    characterRepository.save(newCharacter);
    
    // 7. Логировать
    log.info("Account {} switched from character {} to {}", 
        accountId, currentCharacterId, newCharacterId);
    
    // 8. Опубликовать событие
    eventBus.publish(new CharacterSwitchedEvent(
        accountId,
        currentCharacterId,
        newCharacterId
    ));
    
    return new SwitchCharacterResponse(newCharacter.toDTO());
}
```

---

## Character Stats Calculation

### Recalculate Stats

```java
@Transactional
public void recalculateCharacterStats(UUID characterId) {
    Character character = characterRepository.findById(characterId).get();
    
    // 1. Base stats from attributes
    Map<String, Integer> attributes = character.getAttributes();
    
    int baseHealth = 100 + (attributes.get("body") * 10);
    int baseArmor = attributes.get("body") * 2;
    int baseCyberpsychosis = attributes.get("empathy") * 5;
    
    // 2. Bonuses from equipment
    List<CharacterItem> equipment = itemRepository
        .findByCharacterAndStorage(characterId, StorageType.EQUIPPED);
    
    int equipmentHealth = 0;
    int equipmentArmor = 0;
    
    for (CharacterItem item : equipment) {
        ItemTemplate template = itemTemplateRepository
            .findById(item.getItemTemplateId()).get();
        
        Map<String, Integer> stats = template.getStats();
        
        if (stats != null) {
            equipmentHealth += stats.getOrDefault("health", 0);
            equipmentArmor += stats.getOrDefault("armor", 0);
        }
    }
    
    // 3. Bonuses from buffs
    List<ActiveBuff> buffs = buffRepository.findActiveByCharacter(characterId);
    
    int buffHealth = buffs.stream()
        .mapToInt(buff -> buff.getStats().getOrDefault("health", 0))
        .sum();
    
    int buffArmor = buffs.stream()
        .mapToInt(buff -> buff.getStats().getOrDefault("armor", 0))
        .sum();
    
    // 4. Calculate totals
    int totalMaxHealth = baseHealth + equipmentHealth + buffHealth;
    int totalArmor = baseArmor + equipmentArmor + buffArmor;
    
    // 5. Update character
    character.setMaxHealth(totalMaxHealth);
    character.setArmor(totalArmor);
    
    // Clamp current health
    if (character.getHealth() > totalMaxHealth) {
        character.setHealth(totalMaxHealth);
    }
    
    characterRepository.save(character);
    
    log.debug("Recalculated stats for character {}: HP={}, Armor={}", 
        characterId, totalMaxHealth, totalArmor);
}
```

---

## Character Data Management

### Save Character State

```java
@Transactional
public void saveCharacterState(UUID characterId) {
    Character character = characterRepository.findById(characterId).get();
    
    // 1. Сохранить основные данные (уже в БД через JPA)
    characterRepository.save(character);
    
    // 2. Сохранить inventory (already saved via inventory service)
    
    // 3. Сохранить quest progress
    questProgressRepository.flush();
    
    // 4. Сохранить position
    // (already in character table)
    
    // 5. Сохранить session data
    sessionService.updateSessionState(characterId);
    
    // 6. Создать snapshot (опционально)
    if (shouldTakeSnapshot(character)) {
        snapshotService.takeSnapshot(characterId, SnapshotReason.AUTO_SAVE);
    }
    
    log.debug("Saved state for character {}", characterId);
}
```

### Load Character State

```java
@Transactional(readOnly = true)
public CharacterFullState loadCharacterState(UUID characterId) {
    // 1. Загрузить character
    Character character = characterRepository.findById(characterId)
        .orElseThrow(() -> new CharacterNotFoundException());
    
    // 2. Загрузить inventory
    List<CharacterItem> inventory = itemRepository
        .findByCharacter(characterId);
    
    // 3. Загрузить equipment
    EquipmentSlots equipment = equipmentSlotsRepository
        .findByCharacter(characterId).orElseThrow();
    
    // 4. Загрузить quest progress
    List<QuestProgress> quests = questProgressRepository
        .findByCharacter(characterId);
    
    // 5. Загрузить buffs
    List<ActiveBuff> buffs = buffRepository
        .findActiveByCharacter(characterId);
    
    // 6. Собрать полное состояние
    CharacterFullState state = new CharacterFullState();
    state.setCharacter(character);
    state.setInventory(inventory);
    state.setEquipment(equipment);
    state.setQuests(quests);
    state.setBuffs(buffs);
    
    return state;
}
```

---

## Character Slots Management

### Purchase Slot

```java
@Transactional
public PurchaseSlotResponse purchaseCharacterSlot(UUID accountId) {
    // 1. Получить slots
    CharacterSlots slots = slotsRepository.findByAccount(accountId)
        .orElseGet(() -> createDefaultSlots(accountId));
    
    // 2. Проверить limit
    if (slots.getTotalSlots() >= slots.getMaxSlots()) {
        throw new MaxSlotsReachedException(
            "Maximum " + slots.getMaxSlots() + " character slots allowed"
        );
    }
    
    // 3. Проверить стоимость
    int cost = SLOT_PRICE;
    
    Player player = playerRepository.findByAccount(accountId).get();
    
    if (player.getPremiumCurrency() < cost) {
        throw new InsufficientPremiumCurrencyException();
    }
    
    // 4. Списать валюту
    player.setPremiumCurrency(player.getPremiumCurrency() - cost);
    playerRepository.save(player);
    
    // 5. Добавить слот
    slots.setTotalSlots(slots.getTotalSlots() + 1);
    slots.setPremiumSlotsPurchased(slots.getPremiumSlotsPurchased() + 1);
    slotsRepository.save(slots);
    
    log.info("Account {} purchased character slot for {} premium currency", 
        accountId, cost);
    
    return new PurchaseSlotResponse(slots.getTotalSlots(), player.getPremiumCurrency());
}
```

---

## API Endpoints Summary

### Player
- **GET** `/api/v1/players/profile` - профиль игрока
- **PUT** `/api/v1/players/settings` - настройки

### Characters
- **POST** `/api/v1/characters` - создать персонажа
- **GET** `/api/v1/characters` - список персонажей
- **GET** `/api/v1/characters/{id}` - данные персонажа
- **DELETE** `/api/v1/characters/{id}` - удалить (soft)
- **POST** `/api/v1/characters/{id}/restore` - восстановить
- **POST** `/api/v1/characters/switch` - переключиться

### Slots
- **GET** `/api/v1/characters/slots` - информация о слотах
- **POST** `/api/v1/characters/slots/purchase` - купить слот

---

## Связанные документы

- [Authentication System](../authentication-authorization/README.md)
- [Session Management](../session-management/README.md)
- [Inventory System](../inventory-system/README.md)

---

[← Назад к навигации](./README.md)

---

## История изменений

- v1.0.1 (2025-11-07 02:23) - Создан с полным Java кодом (switching, stats, slots)
- v1.0.0 (2025-11-07) - Создан
