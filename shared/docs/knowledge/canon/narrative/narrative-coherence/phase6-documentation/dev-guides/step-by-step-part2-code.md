# Step-by-Step Backend Setup (Part 2: Backend Code)

**Версия:** 1.0.0  
**Дата:** 2025-11-07 00:48  
**Часть:** 2 из 3

---

## Навигация

- **Part 1:** [Setup & Database](./step-by-step-part1-setup.md)
- **Part 2:** Backend Code (этот файл)
- **Part 3:** [Testing & Deploy](./step-by-step-part3-testing.md)

---

## 📋 STEP 6: Entities (30 минут)

### 6.1 Quest.java

**Файл:** `BACK-JAVA/src/main/java/com/necpgame/narrative/entity/Quest.java`

**См. детальный код в:** `backend-integration-complete.md`

**Ключевые поля:**
```java
@Entity
@Table(name = "quests")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Quest {
    @Id
    private String id;
    private String name;
    private String description;
    
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private List<String> requiredQuests;
    
    // ... остальные поля
}
```

### 6.2 Создать остальные

**Аналогично:**
1. QuestBranch.java
2. DialogueNode.java
3. DialogueChoice.java
4. PlayerFlag.java
5. PlayerWorldState.java
6. ServerWorldState.java
7. TerritoryControl.java

**✅ Checkpoint:** 8 entities созданы

---

## 📋 STEP 7: Repositories (15 минут)

### QuestRepository.java

```java
@Repository
public interface QuestRepository extends JpaRepository<Quest, String> {
    
    @Query("SELECT q FROM Quest q WHERE q.era = :era")
    List<Quest> findByEra(@Param("era") String era);
    
    List<Quest> findByType(QuestType type);
}
```

**Аналогично:** 5 остальных repositories

**✅ Checkpoint:** 6 repositories

---

## 📋 STEP 8: Services (45 минут)

### QuestGraphService.java

**Основные методы:**
- `loadQuestGraph()` - загрузка из JSON
- `isQuestAvailable()` - проверка доступности
- `getAvailableQuests()` - список квестов
- `processChoice()` - обработка выбора

**См. полный код:** `backend-integration-complete.md`

**✅ Checkpoint:** Services работают

---

## 📋 STEP 9: Controllers (30 минут)

### QuestController.java

**Endpoints:**
```java
GET /api/v1/narrative/quests/available
GET /api/v1/narrative/quests/{questId}
POST /api/v1/narrative/quests/{questId}/choice
```

**См. полный код:** `backend-integration-complete.md`

**✅ Checkpoint:** REST API готов

---

## ➡️ Продолжение

**Следующий шаг:** [Part 3 - Testing](./step-by-step-part3-testing.md)

---

## История изменений

- v1.0.0 (2025-11-07 00:48) - Part 2 (Backend Code)

