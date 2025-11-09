# Developer Guide: Narrative System

**Версия:** 1.0.0  
**Дата:** 2025-11-07 00:00

**api-readiness:** not-applicable

---

## Краткое описание

Гайд для разработчиков по использованию системы сюжетной целостности и событийного графа.

---

## Архитектура системы

### Компоненты

1. **Quest Graph** - граф зависимостей квестов (550 узлов, 1200 связей)
2. **World State** - состояние мира (3 уровня: personal, server, faction)
3. **Event Triggers** - система триггеров и блокираторов
4. **Dynamic Content** - генерация контента на основе state

---

## Использование Quest Graph

### Проверка доступности квеста

```java
public boolean isQuestAvailable(String questId, UUID characterId) {
    Quest quest = questRepository.findById(questId);
    
    // 1. Check prerequisites
    for (String prereqId : quest.getRequiredQuests()) {
        if (!isQuestCompleted(prereqId, characterId)) {
            return false;
        }
    }
    
    // 2. Check flags
    for (Flag flag : quest.getRequiredFlags()) {
        if (!hasFlag(characterId, flag.getKey(), flag.getValue())) {
            return false;
        }
    }
    
    // 3. Check blocks
    if (isQuestBlocked(questId, characterId)) {
        return false;
    }
    
    // 4. Check level
    if (getCharacterLevel(characterId) < quest.getMinLevel()) {
        return false;
    }
    
    return true;
}
```

### Обработка выбора в квесте

```java
public void processQuestChoice(UUID characterId, String questId, String choiceId) {
    DialogueChoice choice = dialogueChoiceRepository.findByQuestAndChoice(questId, choiceId);
    
    // 1. Apply consequences
    applyReputationChanges(characterId, choice.getReputationChanges());
    setFlags(characterId, choice.getSetsFlags());
    unsetFlags(characterId, choice.getUnsetsFlags());
    
    // 2. Give/remove items
    giveItems(characterId, choice.getGivesItems());
    removeItems(characterId, choice.getRemovesItems());
    
    // 3. Unlock/block quests
    unlockQuests(characterId, choice.getUnlocksQuests());
    blockQuests(characterId, choice.getBlocksQuests());
    
    // 4. Audit trail
    savePlayerChoice(characterId, questId, choiceId, choice.getConsequences());
}
```

---

## World State Management

### Проверка состояния мира

```java
public WorldState getWorldState(UUID characterId, String serverId) {
    WorldState combined = new WorldState();
    
    // 1. Get personal state
    PersonalState personal = personalStateRepository.findByCharacter(characterId);
    
    // 2. Get server state
    ServerState server = serverStateRepository.findByServer(serverId);
    
    // 3. Get faction state (if character in faction)
    FactionState faction = null;
    if (characterHasFaction(characterId)) {
        faction = factionStateRepository.findByFaction(
            getCharacterFaction(characterId), serverId
        );
    }
    
    // 4. Combine with priorities
    combined.merge(server, Priority.HIGH);
    if (faction != null) {
        combined.merge(faction, Priority.MEDIUM);
    }
    combined.merge(personal, Priority.PERSONAL);
    
    return combined;
}
```

### Обновление серверного состояния (голосование)

```java
public void castWorldStateVote(UUID characterId, String serverId, 
                                String stateKey, Object voteValue) {
    // 1. Save vote
    WorldStateVote vote = new WorldStateVote();
    vote.setServerId(serverId);
    vote.setStateKey(stateKey);
    vote.setCharacterId(characterId);
    vote.setVoteValue(voteValue);
    vote.setWeight(calculateVoteWeight(characterId)); // Reputation based
    voteRepository.save(vote);
    
    // 2. Check threshold
    ServerWorldState serverState = serverStateRepository.findByServerAndKey(serverId, stateKey);
    int totalVotes = voteRepository.countByServerAndKey(serverId, stateKey);
    
    if (totalVotes >= serverState.getThresholdRequired()) {
        // 3. Apply change
        Object winningValue = calculateWinningVote(serverId, stateKey);
        serverState.setStateValue(winningValue);
        serverState.setStatus("ACTIVE");
        serverStateRepository.save(serverState);
        
        // 4. Trigger consequences
        triggerWorldStateChange(serverId, stateKey, winningValue);
    }
}
```

---

## Event Triggers

### Проверка триггеров событий

```java
public void checkEventTriggers(UUID characterId, String questId) {
    Quest completedQuest = questRepository.findById(questId);
    
    // 1. Get quest triggers
    List<QuestTrigger> triggers = triggerRepository.findByTriggerQuest(questId);
    
    for (QuestTrigger trigger : triggers) {
        // 2. Check timing
        if (trigger.getTiming() == Timing.IMMEDIATE) {
            unlockQuest(characterId, trigger.getUnlockedQuestId());
        } 
        else if (trigger.getTiming() == Timing.NEXT_ERA) {
            scheduleQuestUnlock(characterId, trigger.getUnlockedQuestId(), 
                                getCurrentEra() + 1);
        }
        else if (trigger.getTiming() == Timing.CONDITIONAL) {
            // 3. Check conditions
            if (checkConditions(characterId, trigger.getConditions())) {
                unlockQuest(characterId, trigger.getUnlockedQuestId());
            }
        }
    }
}
```

---

## Dynamic Content Generation

### Генерация случайного квеста

```java
public Quest generateDynamicQuest(UUID characterId, String serverId) {
    WorldState worldState = getWorldState(characterId, serverId);
    
    // 1. Get quest pool
    List<DynamicQuestTemplate> pool = dynamicQuestRepository.findAvailable();
    
    // 2. Calculate probabilities
    Map<DynamicQuestTemplate, Double> probabilities = new HashMap<>();
    for (DynamicQuestTemplate template : pool) {
        double baseProbability = template.getBaseProbability();
        
        // Apply modifiers based on world state
        double modified = applyProbabilityModifiers(
            baseProbability, template.getModifiers(), worldState
        );
        
        probabilities.put(template, modified);
    }
    
    // 3. Weight random selection
    DynamicQuestTemplate selected = weightedRandom(probabilities);
    
    // 4. Generate quest instance
    return instantiateQuest(selected, characterId, worldState);
}

private double applyProbabilityModifiers(double base, 
                                         Map<String, Double> modifiers, 
                                         WorldState state) {
    double result = base;
    
    for (Map.Entry<String, Double> modifier : modifiers.entrySet()) {
        String condition = modifier.getKey();
        Double change = modifier.getValue();
        
        if (state.matches(condition)) {
            result += change;
        }
    }
    
    return Math.max(0.0, Math.min(1.0, result)); // Clamp to [0, 1]
}
```

---

## Performance Considerations

### Кэширование

**Quest Graph:**
- Cache quest dependencies in Redis
- TTL: 1 hour
- Key: `quest:deps:{questId}`

**World State:**
- Cache server state in Redis
- TTL: 5 minutes
- Key: `world:state:{serverId}`

**Player Flags:**
- Cache in character session
- Invalidate on flag change

### Индексы

**Critical queries:**
```sql
-- Quest availability check
CREATE INDEX idx_player_flags_character_key ON player_flags(character_id, flag_key);

-- World state lookup
CREATE INDEX idx_server_world_state_server_key ON server_world_state(server_id, state_key);

-- Quest dependencies
CREATE INDEX idx_quest_branches_quest ON quest_branches(quest_id);
```

---

## API Endpoints

### Quest Availability
```
GET /api/v1/quests/available?characterId={uuid}
Response: [{"questId": "MQ-2020-001", "available": true, ...}]
```

### Quest Choice
```
POST /api/v1/quests/{questId}/choice
Body: {"characterId": "uuid", "choiceId": "A1"}
Response: {"success": true, "consequences": {...}}
```

### World State
```
GET /api/v1/world-state?characterId={uuid}&serverId={id}
Response: {"personal": {...}, "server": {...}, "faction": {...}}
```

---

## Testing

### Unit Tests
```java
@Test
public void testQuestAvailability_withPrerequisites() {
    // Given
    UUID characterId = createTestCharacter();
    completeQuest(characterId, "MQ-2020-001");
    
    // When
    boolean available = questService.isQuestAvailable("MQ-2020-002", characterId);
    
    // Then
    assertTrue(available);
}

@Test
public void testQuestBlocked_afterMutualExclusion() {
    // Given
    UUID characterId = createTestCharacter();
    makeChoice(characterId, "MQ-2020-005", "choice_netwatch");
    
    // When
    boolean available = questService.isQuestAvailable("SQ-2030-VB-001", characterId);
    
    // Then
    assertFalse(available); // Voodoo quest blocked
}
```

---

## Linked Documents

- [Architecture](../../../phase3-event-matrix/architecture.md)
- [Quest Tables](../../../phase4-database/tables/quest-tables-extended.sql)
- [World State Tables](../../../phase4-database/tables/world-state-tables.sql)
- [API Integration](./api-integration.md)

---

## История изменений

- v1.0.0 (2025-11-07 00:00) - Developer guide created

