# Player Character Management - Part 1: Creation & Deletion

**Статус:** approved  
**Версия:** 1.0.1  
**Дата:** 2025-11-07 02:22  
**api-readiness:** ready

[Навигация](./README.md) | [Part 2 →](./part2-switching-management.md)

---

- **Status:** queued
- **Last Updated:** 2025-11-08 02:24
---

## Краткое описание

**Player & Character Management System** - критически важная система для управления профилями игроков и их персонажами. Без этой системы игра не может запуститься.

**Ключевые возможности:**
- ✅ Player profiles (профили пользователей)
- ✅ Character creation/deletion
- ✅ Character switching (переключение между персонажами)
- ✅ Character slots (ограничение количества персонажей)
- ✅ Character data storage (attributes, skills, inventory IDs, progress)
- ✅ Character appearance (кастомизация внешности)
- ✅ Character naming (уникальные имена, валидация)
- ✅ Character restore (восстановление удаленных персонажей)

---

## Database Schema

### Таблица `players`

```sql
CREATE TABLE players (
    -- Идентификация
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    account_id UUID NOT NULL,
    
    -- Профиль игрока (account-wide данные)
    premium_currency INTEGER DEFAULT 0, -- Не привязано к персонажу
    total_playtime INTEGER DEFAULT 0, -- Суммарное время всех персонажей
    
    -- Settings (account-wide)
    ui_settings JSONB DEFAULT '{}',
    audio_settings JSONB DEFAULT '{}',
    graphics_settings JSONB DEFAULT '{}',
    
    -- Social
    friend_list UUID[] DEFAULT '{}',
    blocked_players UUID[] DEFAULT '{}',
    
    -- Preferences
    language VARCHAR(10) DEFAULT 'en',
    timezone VARCHAR(50) DEFAULT 'UTC',
    
    -- Timestamps
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_player_account FOREIGN KEY (account_id) 
        REFERENCES accounts(id) ON DELETE CASCADE,
    UNIQUE(account_id)
);

CREATE INDEX idx_players_account ON players(account_id);
```

### Таблица `characters`

```sql
CREATE TABLE characters (
    -- Идентификация
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    player_id UUID NOT NULL,
    account_id UUID NOT NULL, -- Денормализация для быстрого доступа
    
    -- Основная информация
    character_name VARCHAR(50) UNIQUE NOT NULL,
    
    -- Class & Origin
    class VARCHAR(50) NOT NULL, -- SOLO, NETRUNNER, TECHIE, FIXER, NOMAD
    origin VARCHAR(50) NOT NULL, -- CORPO, STREETKID, NOMAD, CUSTOM
    
    -- Level & Experience
    level INTEGER DEFAULT 1,
    experience BIGINT DEFAULT 0,
    experience_to_next_level BIGINT DEFAULT 1000,
    
    -- Attributes (базовые характеристики)
    attributes JSONB NOT NULL,
    -- {
    --   "body": 5,
    --   "reflexes": 5,
    --   "technical_ability": 5,
    --   "intelligence": 5,
    --   "cool": 5,
    --   "empathy": 5
    -- }
    
    -- Skills (навыки, уровни 0-100)
    skills JSONB NOT NULL DEFAULT '{}',
    -- {
    --   "hacking": 15,
    --   "shooting": 20,
    --   ...
    -- }
    
    -- Derived Stats (производные параметры)
    health INTEGER NOT NULL DEFAULT 100,
    max_health INTEGER NOT NULL DEFAULT 100,
    armor INTEGER DEFAULT 0,
    cyberpsychosis INTEGER DEFAULT 0,
    max_cyberpsychosis INTEGER DEFAULT 100,
    
    -- Currency
    eddies BIGINT DEFAULT 0, -- Основная валюта
    
    -- Appearance (внешность)
    appearance JSONB NOT NULL,
    -- {
    --   "bodyType": 1,
    --   "skinTone": 5,
    --   "hairStyle": 12,
    --   "hairColor": "#FF5733",
    --   "eyeColor": "#1E90FF",
    --   "tattoos": [1, 5, 8],
    --   "scars": [2],
    --   "implants_visible": ["mantis_blades", "optic_implant"]
    -- }
    
    -- World Position
    current_zone VARCHAR(100) NOT NULL DEFAULT 'nightCity.watson.character_creation',
    position_x DECIMAL(10,2) DEFAULT 0,
    position_y DECIMAL(10,2) DEFAULT 0,
    position_z DECIMAL(10,2) DEFAULT 0,
    
    -- Progress
    main_quest_progress INTEGER DEFAULT 0, -- Этап главного квеста
    completed_quests TEXT[] DEFAULT '{}',
    failed_quests TEXT[] DEFAULT '{}',
    
    -- Reputation (по фракциям)
    reputation JSONB DEFAULT '{}',
    -- {
    --   "arasaka": -50,
    --   "militech": 20,
    --   "nomads": 80,
    --   ...
    -- }
    
    -- Playtime
    playtime INTEGER DEFAULT 0, -- Секунды
    
    -- Character Status
    status VARCHAR(20) DEFAULT 'ACTIVE',
    -- ACTIVE, IN_COMBAT, AFK, DEAD, DELETED
    
    is_alive BOOLEAN DEFAULT TRUE,
    death_count INTEGER DEFAULT 0,
    last_death_at TIMESTAMP,
    
    -- Deletion (soft delete)
    deleted BOOLEAN DEFAULT FALSE,
    deleted_at TIMESTAMP,
    can_restore_until TIMESTAMP, -- 30 days grace period
    
    -- Timestamps
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_played_at TIMESTAMP,
    
    CONSTRAINT fk_character_player FOREIGN KEY (player_id) 
        REFERENCES players(id) ON DELETE CASCADE,
    CONSTRAINT fk_character_account FOREIGN KEY (account_id) 
        REFERENCES accounts(id) ON DELETE CASCADE
);

CREATE INDEX idx_characters_player ON characters(player_id);
CREATE INDEX idx_characters_account ON characters(account_id);
CREATE INDEX idx_characters_name ON characters(character_name);
CREATE INDEX idx_characters_zone ON characters(current_zone) WHERE deleted = FALSE;
CREATE INDEX idx_characters_deleted ON characters(deleted, can_restore_until);
```

### Таблица `character_slots`

```sql
CREATE TABLE character_slots (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    account_id UUID NOT NULL,
    
    -- Слоты
    total_slots INTEGER DEFAULT 3, -- Базовое количество
    used_slots INTEGER DEFAULT 0,
    
    -- Premium slots (купленные)
    premium_slots_purchased INTEGER DEFAULT 0,
    max_slots INTEGER DEFAULT 5, -- Максимум (3 базовых + 2 premium)
    
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_slots_account FOREIGN KEY (account_id) 
        REFERENCES accounts(id) ON DELETE CASCADE,
    UNIQUE(account_id)
);
```

---

## Character Creation Flow

### Создание персонажа

```java
@Service
public class CharacterService {
    
    @Autowired
    private CharacterRepository characterRepository;
    
    @Autowired
    private CharacterSlotService slotService;
    
    @Autowired
    private EventBus eventBus;
    
    @Transactional
    public CharacterResponse createCharacter(
        UUID accountId,
        CreateCharacterRequest request
    ) {
        // 1. Проверить доступные слоты
        if (!slotService.hasAvailableSlot(accountId)) {
            throw new NoAvailableSlotsException(
                "All character slots are used. Delete a character or purchase more slots."
            );
        }
        
        // 2. Валидация имени
        validateCharacterName(request.getName());
        
        // 3. Проверить уникальность имени (global)
        if (characterRepository.existsByName(request.getName())) {
            throw new CharacterNameTakenException(
                "Character name is already taken"
            );
        }
        
        // 4. Получить player_id
        UUID playerId = getOrCreatePlayer(accountId);
        
        // 5. Создать персонажа
        Character character = new Character();
        character.setPlayerId(playerId);
        character.setAccountId(accountId);
        character.setCharacterName(request.getName());
        character.setClass(request.getCharacterClass());
        character.setOrigin(request.getOrigin());
        
        // 6. Установить начальные атрибуты (зависят от класса)
        Map<String, Integer> initialAttributes = getInitialAttributes(
            request.getCharacterClass(),
            request.getOrigin()
        );
        character.setAttributes(initialAttributes);
        
        // 7. Начальные навыки (зависят от origin)
        Map<String, Integer> initialSkills = getInitialSkills(request.getOrigin());
        character.setSkills(initialSkills);
        
        // 8. Внешность
        character.setAppearance(request.getAppearance());
        
        // 9. Начальная позиция (зависит от origin)
        String startZone = getStartZone(request.getOrigin());
        Vector3 startPosition = getStartPosition(request.getOrigin());
        character.setCurrentZone(startZone);
        character.setPositionX(startPosition.x);
        character.setPositionY(startPosition.y);
        character.setPositionZ(startPosition.z);
        
        // 10. Начальная валюта (зависит от origin)
        long startEddies = getStartEddies(request.getOrigin());
        character.setEddies(startEddies);
        
        // 11. Сохранить
        character = characterRepository.save(character);
        
        // 12. Обновить used slots
        slotService.incrementUsedSlots(accountId);
        
        // 13. Создать начальный инвентарь
        inventoryService.createInitialInventory(
            character.getId(),
            request.getCharacterClass(),
            request.getOrigin()
        );
        
        // 14. Дать начальные квесты
        questService.giveStartingQuests(character.getId(), request.getOrigin());
        
        // 15. Логировать
        log.info("Character created: {} for account {}", character.getId(), accountId);
        
        // 16. Опубликовать событие
        eventBus.publish(new CharacterCreatedEvent(
            character.getId(),
            accountId,
            character.getCharacterName(),
            request.getCharacterClass()
        ));
        
        // 17. Snapshot
        snapshotService.takeSnapshot(
            character.getId(),
            SnapshotReason.CHARACTER_CREATED
        );
        
        return character.toDTO();
    }
    
    private void validateCharacterName(String name) {
        // Length
        if (name.length() < 3 || name.length() > 20) {
            throw new InvalidCharacterNameException(
                "Name must be 3-20 characters"
            );
        }
        
        // Characters (alphanumeric + spaces + hyphens)
        if (!name.matches("^[a-zA-Z0-9 -]+$")) {
            throw new InvalidCharacterNameException(
                "Name can only contain letters, numbers, spaces, and hyphens"
            );
        }
        
        // Banned words
        if (containsBannedWords(name)) {
            throw new InvalidCharacterNameException(
                "Name contains inappropriate content"
            );
        }
    }
}
```

---

## Character Deletion

### Soft Delete

```java
@Transactional
public void deleteCharacter(UUID characterId, UUID accountId) {
    // 1. Найти персонажа
    Character character = characterRepository.findById(characterId)
        .orElseThrow(() -> new CharacterNotFoundException());
    
    // 2. Проверить ownership
    if (!character.getAccountId().equals(accountId)) {
        throw new UnauthorizedAccessException();
    }
    
    // 3. Проверить, не удален ли уже
    if (character.isDeleted()) {
        throw new CharacterAlreadyDeletedException();
    }
    
    // 4. Soft delete
    character.setDeleted(true);
    character.setDeletedAt(Instant.now());
    character.setCanRestoreUntil(Instant.now().plus(Duration.ofDays(30)));
    character.setStatus(CharacterStatus.DELETED);
    
    characterRepository.save(character);
    
    // 5. Освободить слот
    slotService.decrementUsedSlots(accountId);
    
    // 6. Логировать
    log.info("Character {} deleted (soft) by account {}", characterId, accountId);
    
    // 7. Опубликовать событие
    eventBus.publish(new CharacterDeletedEvent(
        characterId,
        accountId,
        character.getCharacterName()
    ));
}
```

### Character Restore

```java
@Transactional
public CharacterResponse restoreCharacter(UUID characterId, UUID accountId) {
    // 1. Найти персонажа
    Character character = characterRepository.findById(characterId)
        .orElseThrow(() -> new CharacterNotFoundException());
    
    // 2. Проверить, можно ли восстановить
    if (!character.isDeleted()) {
        throw new CharacterNotDeletedException();
    }
    
    if (Instant.now().isAfter(character.getCanRestoreUntil())) {
        throw new RestoreWindowExpiredException(
            "Cannot restore character after 30 days"
        );
    }
    
    // 3. Проверить слоты
    if (!slotService.hasAvailableSlot(accountId)) {
        throw new NoAvailableSlotsException();
    }
    
    // 4. Восстановить
    character.setDeleted(false);
    character.setDeletedAt(null);
    character.setCanRestoreUntil(null);
    character.setStatus(CharacterStatus.ACTIVE);
    
    characterRepository.save(character);
    
    // 5. Занять слот
    slotService.incrementUsedSlots(accountId);
    
    // 6. Логировать
    log.info("Character {} restored by account {}", characterId, accountId);
    
    // 7. Опубликовать событие
    eventBus.publish(new CharacterRestoredEvent(characterId, accountId));
    
    return character.toDTO();
}
```

### Permanent Delete (Cleanup Job)

```java
@Scheduled(cron = "0 0 3 * * *") // 3 AM every day
public void cleanupExpiredDeletedCharacters() {
    Instant threshold = Instant.now();
    
    List<Character> expiredCharacters = characterRepository
        .findDeletedAndExpired(threshold);
    
    log.info("Found {} expired deleted characters for permanent deletion", 
        expiredCharacters.size());
    
    for (Character character : expiredCharacters) {
        // Hard delete
        permanentlyDeleteCharacter(character.getId());
    }
}

@Transactional
private void permanentlyDeleteCharacter(UUID characterId) {
    Character character = characterRepository.findById(characterId).get();
    
    // 1. Удалить связанные данные (каскадом)
    // - character_inventory (FK CASCADE)
    // - character_items (FK CASCADE)
    // - quest_progress (FK CASCADE)
    // - etc.
    
    // 2. Удалить персонажа
    characterRepository.delete(character);
    
    log.info("Character {} permanently deleted", characterId);
}
```

---

## API Endpoints

### Character Management
- **POST** `/api/v1/characters` - создать персонажа
- **GET** `/api/v1/characters` - список персонажей account
- **GET** `/api/v1/characters/{id}` - данные персонажа
- **DELETE** `/api/v1/characters/{id}` - удалить персонажа (soft)
- **POST** `/api/v1/characters/{id}/restore` - восстановить персонажа

---

[Part 2: Switching & Management →](./part2-switching-management.md)

---

## История изменений

- v1.0.1 (2025-11-07 02:22) - Создан с полным Java кодом (schemas, creation, deletion, restore)
- v1.0.0 (2025-11-07) - Создан
