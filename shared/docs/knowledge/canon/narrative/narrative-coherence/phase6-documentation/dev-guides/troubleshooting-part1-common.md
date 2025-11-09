# Troubleshooting (Part 1: Common Problems)

**Версия:** 1.0.0
**Дата:** 2025-11-07 00:51
**Часть:** 1 из 3

---

## Навигация

- **Part 1:** Common Problems (этот файл)
- **Part 2:** [Database & Performance](./troubleshooting-part2-database.md)
- **Part 3:** [WebSocket & Advanced](./troubleshooting-part3-advanced.md)

---

## 🔴 ПРОБЛЕМА 1: Миграции не применяются

### Симптомы
```
ERROR: relation "quests" does not exist
ERROR: column "has_branches" does not exist
```

### Решения

**Базовая таблица не существует:**
```sql
CREATE TABLE quests (
    id VARCHAR(100) PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    created_at TIMESTAMP NOT NULL
);
```

**Нет прав:**
```sql
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO your_user;
```

---

## 🔴 ПРОБЛЕМА 2: Quest graph не загружается

### Симптомы
```
FileNotFoundException: quest-dependencies-full.json
```

### Решения

**JSON не найден:**
```bash
cd .BRAIN/.../export
python convert-quest-graph.py
cp export/*.json BACK-JAVA/src/main/resources/data/narrative/
```

---

## 🔴 ПРОБЛЕМА 3: JSONB не работает

### Решения

**Добавить dependency:**
```xml
<dependency>
    <groupId>com.vladmihalcea</groupId>
    <artifactId>hibernate-types-55</artifactId>
    <version>2.21.1</version>
</dependency>
```

**Добавить @TypeDef:**
```java
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Quest {
    @Type(type = "jsonb")
    private Map<String, Object> requiredFlags;
}
```

---

## 🔴 ПРОБЛЕМА 4: Quest не доступен

### Диагностика

```java
log.debug("Prerequisites: {}", checkPrerequisites(quest, characterId));
log.debug("Flags: {}", checkRequiredFlags(quest, characterId));
log.debug("Blocked: {}", isQuestBlocked(questId, characterId));
```

### Решения

**Установить флаги для теста:**
```sql
INSERT INTO player_flags (character_id, flag_key, flag_value)
VALUES ('xxx', 'test_flag', 'true'::jsonb);
```

---

## 🔴 ПРОБЛЕМА 5: Dialogue choice не сохраняется

### Решение

**Добавить @Transactional:**
```java
@Transactional
public QuestChoiceResult processChoice(...) {
    // Все изменения в одной транзакции
}
```

---

## ➡️ Продолжение

**Далее:** [Part 2 - Database & Performance](./troubleshooting-part2-database.md)

---

## История изменений

- v1.0.0 (2025-11-07 00:51) - Common problems

