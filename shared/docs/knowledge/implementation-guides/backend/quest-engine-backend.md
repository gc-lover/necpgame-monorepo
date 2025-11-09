# Quest Engine Backend — бэкенд движка квестов

**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-09 01:03  
**api-readiness-notes:** Quest Engine backend перепроверен 2025-11-09 01:03 и обновлён 2025-11-09 23:30: state machine, диалоговый движок, shooter skill tests и branching готовы к постановке задач для gameplay-service.

---
**API Tasks Status:**
- Status: completed
- Tasks:
  - API-TASK-098: Quest Engine API — `api/v1/gameplay/quests/quest-engine/quest-engine.yaml`
    - Создано: 2025-11-09 17:28
    - Завершено: 2025-11-09 19:48
    - Доп. файлы: `quest-engine-models.yaml`, `quest-engine-models-operations.yaml`, `README.md`
    - Файл задачи: `API-SWAGGER/tasks/completed/2025-11-09/task-098-quest-engine-api.md`
- Last Updated: 2025-11-09 19:48
---

**target-domain:** gameplay-quests  
**target-microservice:** gameplay-service (port 8083)  
**target-frontend-module:** modules/quests

**Статус:** approved  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-07  
**Последнее обновление:** 2025-11-08 18:15  
**Приоритет:** критический (MVP блокер)  
**Ответственный:** Brain Manager

---

**Статус:** approved  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-07  
**Последнее обновление:** 2025-11-07 (обновлено для микросервисов)  
**Приоритет:** КРИТИЧЕСКИЙ (MVP блокер!)  
**Автор:** AI Brain Manager

---

## Краткое описание

**Quest Engine Backend** - движок для выполнения квестов с ветвлениями, диалогами и shooter skill tests. **БЕЗ ЭТОГО НЕТ КОНТЕНТА В ИГРЕ!**

**Микрофича:** Quest execution engine  
**Размер:** ~400 строк (соблюдает лимит!)

**Ключевые возможности:**
- ✅ Quest state machine (запуск, прогресс, завершение)
- ✅ Dialogue tree execution (nodes, choices)
- ✅ Skill test processing (deterministic shooter thresholds)
- ✅ Branch selection logic
- ✅ Condition evaluation (player flags, reputation, items)

---

## Микросервисная архитектура

**Ответственный микросервис:** gameplay-service  
**Порт:** 8083  
**API Gateway маршрут:** `/api/v1/gameplay/quests/*`  
**Статус:** 📋 В планах (Фаза 2)

**Взаимодействие с другими сервисами:**
- character-service: получение stats для skill checks, update flags
- economy-service: quest rewards (items, gold)
- social-service: NPC reputation updates

**Event Bus события:**
- Публикует: `quest:started`, `quest:objective-completed`, `quest:completed`, `quest:failed`
- Подписывается: `combat:enemy-killed`, `item:collected`, `npc:talked`

---

## Database Schema

### Таблица `quest_instances`

```sql
CREATE TABLE quest_instances (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    character_id UUID NOT NULL,
    quest_template_id VARCHAR(100) NOT NULL,
    
    -- State
    status VARCHAR(20) DEFAULT 'ACTIVE',
    -- ACTIVE, COMPLETED, FAILED, ABANDONED
    
    -- Current position
    current_branch_id VARCHAR(100),
    current_dialogue_node_id VARCHAR(100),
    
    -- Timestamps
    started_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP,
    
    CONSTRAINT fk_quest_instance_character FOREIGN KEY (character_id) 
        REFERENCES characters(id) ON DELETE CASCADE
);

CREATE INDEX idx_quest_instances_character ON quest_instances(character_id, status);
```

### Таблица `dialogue_state`

```sql
CREATE TABLE dialogue_state (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    quest_instance_id UUID NOT NULL,
    
    current_node_id VARCHAR(100) NOT NULL,
    visited_nodes TEXT[] DEFAULT '{}',
    
    -- Choices made
    choices_made JSONB DEFAULT '[]',
    -- [{node_id: "node_1", choice_id: "choice_a", result: "success"}]
    
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_dialogue_quest FOREIGN KEY (quest_instance_id) 
        REFERENCES quest_instances(id) ON DELETE CASCADE
);
```

### Таблица `skill_check_results`

```sql
CREATE TABLE skill_check_results (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    quest_instance_id UUID NOT NULL,
    dialogue_node_id VARCHAR(100) NOT NULL,
    
    -- Check parameters
    skill_name VARCHAR(50) NOT NULL,
    difficulty_class INTEGER NOT NULL,
    
    -- Roll
    dice_roll INTEGER NOT NULL, -- d20 or d100
    skill_modifier INTEGER DEFAULT 0,
    total_result INTEGER NOT NULL,
    
    -- Result
    success BOOLEAN NOT NULL,
    
    rolled_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_check_quest FOREIGN KEY (quest_instance_id) 
        REFERENCES quest_instances(id) ON DELETE CASCADE
);

CREATE INDEX idx_checks_quest ON skill_check_results(quest_instance_id);
```

---

## Quest Execution

### Start Quest

```java
@Service
public class QuestEngine {
    
    @Transactional
    public QuestInstance startQuest(UUID characterId, String questTemplateId) {
        // 1. Load quest template (из JSON или DB)
        QuestTemplate template = questTemplateRepository.findById(questTemplateId)
            .orElseThrow(() -> new QuestNotFoundException());
        
        // 2. Check requirements
        if (!meetsRequirements(characterId, template.getRequirements())) {
            throw new QuestRequirementsNotMetException();
        }
        
        // 3. Create instance
        QuestInstance instance = new QuestInstance();
        instance.setCharacterId(characterId);
        instance.setQuestTemplateId(questTemplateId);
        instance.setCurrentBranchId(template.getStartBranchId());
        instance.setCurrentDialogueNodeId(template.getStartNodeId());
        instance.setStatus(QuestStatus.ACTIVE);
        
        questInstanceRepository.save(instance);
        
        // 4. Initialize dialogue state
        DialogueState dialogueState = new DialogueState();
        dialogueState.setQuestInstanceId(instance.getId());
        dialogueState.setCurrentNodeId(template.getStartNodeId());
        dialogueStateRepository.save(dialogueState);
        
        // 5. Publish event
        eventBus.publish(new QuestStartedEvent(instance.getId(), characterId, questTemplateId));
        
        return instance;
    }
}
```

### Process Dialogue Choice

```java
@Transactional
public DialogueResponse processChoice(
    UUID questInstanceId,
    UUID characterId,
    String choiceId
) {
    // 1. Get quest instance
    QuestInstance instance = questInstanceRepository.findById(questInstanceId).get();
    DialogueState state = dialogueStateRepository.findByQuestInstance(questInstanceId).get();
    
    // 2. Get current node
    DialogueNode node = getDialogueNode(instance.getQuestTemplateId(), state.getCurrentNodeId());
    
    // 3. Find choice
    DialogueChoice choice = node.getChoices().stream()
        .filter(c -> c.getId().equals(choiceId))
        .findFirst()
        .orElseThrow(() -> new InvalidChoiceException());
    
    // 4. Check conditions
    if (!evaluateConditions(characterId, choice.getConditions())) {
        throw new ConditionsNotMetException();
    }
    
    // 5. Skill check (если есть)
    if (choice.getSkillCheck() != null) {
        SkillCheckResult result = performSkillCheck(
            questInstanceId,
            characterId,
            choice.getSkillCheck()
        );
        
        if (!result.isSuccess()) {
            // Failed skill check, go to fail node
            String failNodeId = choice.getFailNodeId();
            state.setCurrentNodeId(failNodeId);
            state.getChoicesMade().add(new ChoiceRecord(node.getId(), choiceId, "FAILED"));
            dialogueStateRepository.save(state);
            
            return new DialogueResponse(
                getDialogueNode(instance.getQuestTemplateId(), failNodeId),
                result
            );
        }
    }
    
    // 6. Apply consequences
    applyConsequences(characterId, choice.getConsequences());
    
    // 7. Move to next node
    String nextNodeId = choice.getNextNodeId();
    state.setCurrentNodeId(nextNodeId);
    state.getVisitedNodes().add(node.getId());
    state.getChoicesMade().add(new ChoiceRecord(node.getId(), choiceId, "SUCCESS"));
    dialogueStateRepository.save(state);
    
    // 8. Check if quest completed
    DialogueNode nextNode = getDialogueNode(instance.getQuestTemplateId(), nextNodeId);
    if (nextNode.getType().equals("END")) {
        completeQuest(instance);
    }
    
    return new DialogueResponse(nextNode, null);
}
```

### Perform Skill Check

```java
private SkillCheckResult performSkillCheck(
    UUID questInstanceId,
    UUID characterId,
    SkillCheck skillCheck
) {
    Character character = characterRepository.findById(characterId).get();
    
    // 1. Get skill level
    int skillLevel = character.getSkills().getOrDefault(skillCheck.getSkillName(), 0);
    
    // 2. Get attribute modifier
    String relatedAttribute = getRelatedAttribute(skillCheck.getSkillName());
    int attributeValue = character.getAttributes().get(relatedAttribute);
    int attributeModifier = ShooterSkillScaling.calculate(attributeValue); // shooter skill scaling
    
    // 3. Roll dice
    int diceRoll = rollDice(skillCheck.getDiceType()); // d20 or d100
    
    // 4. Calculate total
    int total = diceRoll + skillLevel + attributeModifier + skillCheck.getModifier();
    
    // 5. Check success
    boolean success = total >= skillCheck.getDifficultyClass();
    
    // 6. Save result
    SkillCheckResult result = new SkillCheckResult();
    result.setQuestInstanceId(questInstanceId);
    result.setDialogueNodeId(skillCheck.getNodeId());
    result.setSkillName(skillCheck.getSkillName());
    result.setDifficultyClass(skillCheck.getDifficultyClass());
    result.setDiceRoll(diceRoll);
    result.setSkillModifier(skillLevel + attributeModifier);
    result.setTotalResult(total);
    result.setSuccess(success);
    
    skillCheckResultRepository.save(result);
    
    return result;
}
```

---

## API Endpoints

**POST `/api/v1/quests/start`** - начать квест
**GET `/api/v1/quests/active`** - активные квесты
**POST `/api/v1/quests/{id}/dialogue/choice`** - выбрать в диалоге
**GET `/api/v1/quests/{id}/dialogue/current`** - текущий диалог
**POST `/api/v1/quests/{id}/complete`** - завершить квест
**POST `/api/v1/quests/{id}/abandon`** - отказаться от квеста

---

## Связанные документы

- `.BRAIN/04-narrative/quests/quest-system.md` - Gameplay система квестов
- `.BRAIN/05-technical/backend/quest-progress-tracking.md` - Progress tracking (будет создан)
- `.BRAIN/05-technical/backend/quest-rewards.md` - Rewards distribution (будет создан)

---

## TODO

- [ ] Quest sharing (с party members)
- [ ] Quest hints system
- [ ] Quest tracker UI data

---

## История изменений

- **v1.0.0 (2025-11-07 05:30)** - Создан Quest Engine Backend (микрофича)
