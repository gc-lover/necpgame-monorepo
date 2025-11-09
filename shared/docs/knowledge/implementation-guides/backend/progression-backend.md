---
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-09 02:47
**api-readiness-notes:** Перепроверено 2025-11-09 02:47: REST `/api/v1/gameplay/progression/*`, события `character:level-up`/`skill-leveled`, схемы БД и расчёт EXP подтверждены; готово к постановке задач gameplay-service.
---

**API Tasks Status:**
- Status: completed
- Tasks:
  - API-TASK-101: Progression Core API — `api/v1/gameplay/progression/progression-core/progression-core.yaml`
    - Создано: 2025-11-09 18:24
    - Завершено: 2025-11-09 20:37
    - Доп. файлы: `progression-core-models.yaml`, `progression-core-models-operations.yaml`, `README.md`
    - Файл задачи: `API-SWAGGER/tasks/completed/2025-11-09/task-101-progression-core-api.md`
- Last Updated: 2025-11-09 20:37
---

# Progression System Backend - Backend системы прогрессии

**Статус:** approved  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-07  
**Последнее обновление:** 2025-11-07 (обновлено для микросервисов)  
**Приоритет:** КРИТИЧЕСКИЙ (MVP блокер!)  
**Автор:** AI Brain Manager

---

## Краткое описание

**Progression Backend** - backend для системы прокачки персонажей. **БЕЗ ЭТОГО НЕТ ПРОГРЕССИИ!**

**Микрофича:** Level up & skill progression  
**Размер:** ~400 строк (соблюдает лимит!)

**Ключевые возможности:**
- ✅ Experience calculation & award
- ✅ Level up logic
- ✅ Attribute points distribution
- ✅ Skill experience tracking
- ✅ Skill level up rewards

---

## Микросервисная архитектура

**Ответственный микросервис:** gameplay-service  
**Порт:** 8083  
**API Gateway маршрут:** `/api/v1/gameplay/progression/*`  
**Статус:** 📋 В планах (Фаза 2)

**Взаимодействие с другими сервисами:**
- character-service: обновление level, attributes, skills
- achievement-service (world): проверка level achievements

**Event Bus события:**
- Публикует: `character:level-up`, `character:skill-leveled`, `character:attribute-increased`
- Подписывается: `combat:enemy-killed` (experience), `quest:completed` (experience), `skill:used` (skill exp)

---

## Database Schema

```sql
CREATE TABLE character_progression (
    character_id UUID PRIMARY KEY,
    
    -- Level
    level INTEGER DEFAULT 1,
    experience BIGINT DEFAULT 0,
    experience_to_next_level BIGINT DEFAULT 1000,
    
    -- Points
    unspent_attribute_points INTEGER DEFAULT 0,
    unspent_skill_points INTEGER DEFAULT 0,
    
    -- Totals
    total_experience_earned BIGINT DEFAULT 0,
    total_attribute_points_spent INTEGER DEFAULT 0,
    total_skill_points_spent INTEGER DEFAULT 0,
    
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_progression_character FOREIGN KEY (character_id) 
        REFERENCES characters(id) ON DELETE CASCADE
);
```

### Таблица `skill_experience`

```sql
CREATE TABLE skill_experience (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    character_id UUID NOT NULL,
    
    skill_name VARCHAR(50) NOT NULL,
    current_level INTEGER DEFAULT 0,
    experience INTEGER DEFAULT 0,
    experience_to_next_level INTEGER DEFAULT 100,
    
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_skill_exp_character FOREIGN KEY (character_id) 
        REFERENCES characters(id) ON DELETE CASCADE,
    UNIQUE(character_id, skill_name)
);

CREATE INDEX idx_skill_exp_character ON skill_experience(character_id);
```

---

## Award Experience

```java
@Service
public class ProgressionService {
    
    @Transactional
    public void awardExperience(UUID characterId, long amount, String source) {
        CharacterProgression prog = progressionRepository.findById(characterId).get();
        Character character = characterRepository.findById(characterId).get();
        
        // 1. Add experience
        prog.setExperience(prog.getExperience() + amount);
        prog.setTotalExperienceEarned(prog.getTotalExperienceEarned() + amount);
        
        // 2. Check level up
        while (prog.getExperience() >= prog.getExperienceToNextLevel()) {
            levelUp(character, prog);
        }
        
        progressionRepository.save(prog);
        
        // 3. Notify
        notificationService.send(getAccountId(characterId),
            new ExperienceGainedNotification(amount, source));
    }
    
    private void levelUp(Character character, CharacterProgression prog) {
        // 1. Increase level
        int newLevel = prog.getLevel() + 1;
        prog.setLevel(newLevel);
        character.setLevel(newLevel);
        
        // 2. Deduct exp
        prog.setExperience(prog.getExperience() - prog.getExperienceToNextLevel());
        
        // 3. Calculate next level exp requirement
        long nextLevelExp = calculateExpRequirement(newLevel + 1);
        prog.setExperienceToNextLevel(nextLevelExp);
        
        // 4. Award points
        prog.setUnspentAttributePoints(prog.getUnspentAttributePoints() + 1);
        prog.setUnspentSkillPoints(prog.getUnspentSkillPoints() + 2);
        
        // 5. Recalculate derived stats
        recalculateStats(character);
        
        characterRepository.save(character);
        
        // 6. Notify
        notificationService.send(getAccountId(character.getId()),
            new LevelUpNotification(newLevel));
        
        // 7. Publish event
        eventBus.publish(new LevelUpEvent(character.getId(), newLevel));
        
        log.info("Character {} leveled up to {}", character.getId(), newLevel);
    }
    
    private long calculateExpRequirement(int level) {
        // Exponential formula
        return (long) (1000 * Math.pow(1.5, level - 1));
    }
}
```

---

## Spend Attribute Points

```java
@Transactional
public void spendAttributePoint(UUID characterId, String attributeName) {
    CharacterProgression prog = progressionRepository.findById(characterId).get();
    Character character = characterRepository.findById(characterId).get();
    
    // 1. Check points
    if (prog.getUnspentAttributePoints() <= 0) {
        throw new NoAttributePointsException();
    }
    
    // 2. Get current value
    Map<String, Integer> attributes = character.getAttributes();
    int currentValue = attributes.get(attributeName);
    
    // 3. Check cap
    int maxValue = 20; // Hard cap
    if (currentValue >= maxValue) {
        throw new AttributeCapReachedException();
    }
    
    // 4. Increase attribute
    attributes.put(attributeName, currentValue + 1);
    character.setAttributes(attributes);
    
    // 5. Spend point
    prog.setUnspentAttributePoints(prog.getUnspentAttributePoints() - 1);
    prog.setTotalAttributePointsSpent(prog.getTotalAttributePointsSpent() + 1);
    
    // 6. Recalculate derived stats
    recalculateStats(character);
    
    characterRepository.save(character);
    progressionRepository.save(prog);
    
    log.info("Character {} increased {} to {}", characterId, attributeName, currentValue + 1);
}
```

---

## Award Skill Experience

```java
@Transactional
public void awardSkillExperience(UUID characterId, String skillName, int amount) {
    SkillExperience skillExp = skillExpRepository
        .findByCharacterAndSkill(characterId, skillName)
        .orElseGet(() -> createSkillExp(characterId, skillName));
    
    // 1. Add exp
    skillExp.setExperience(skillExp.getExperience() + amount);
    
    // 2. Check skill level up
    while (skillExp.getExperience() >= skillExp.getExperienceToNextLevel()) {
        skillLevelUp(characterId, skillExp);
    }
    
    skillExpRepository.save(skillExp);
}

private void skillLevelUp(UUID characterId, SkillExperience skillExp) {
    int newLevel = skillExp.getCurrentLevel() + 1;
    
    if (newLevel > 100) {
        return; // Max skill level
    }
    
    skillExp.setCurrentLevel(newLevel);
    skillExp.setExperience(skillExp.getExperience() - skillExp.getExperienceToNextLevel());
    skillExp.setExperienceToNextLevel(calculateSkillExpReq(newLevel + 1));
    
    // Update character skills map
    Character character = characterRepository.findById(characterId).get();
    Map<String, Integer> skills = character.getSkills();
    skills.put(skillExp.getSkillName(), newLevel);
    character.setSkills(skills);
    characterRepository.save(character);
    
    // Notify
    if (newLevel % 10 == 0) { // Every 10 levels
        notificationService.send(getAccountId(characterId),
            new SkillLevelUpNotification(skillExp.getSkillName(), newLevel));
    }
}
```

---

## API Endpoints

**GET `/api/v1/progression`** - прогрессия персонажа
**POST `/api/v1/progression/attributes/spend`** - потратить attribute point
**POST `/api/v1/progression/skills/spend`** - потратить skill point
**GET `/api/v1/progression/skills`** - все навыки и их опыт

---

## Связанные документы

- `.BRAIN/02-gameplay/progression/progression-attributes.md` - Attributes system
- `.BRAIN/02-gameplay/progression/progression-skills.md` - Skills system

---

## История изменений

- **v1.0.0 (2025-11-07 05:30)** - Создан Progression Backend (микрофича)

