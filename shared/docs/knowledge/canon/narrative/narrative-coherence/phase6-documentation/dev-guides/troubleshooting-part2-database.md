# Troubleshooting (Part 2: Database & Performance)

**Версия:** 1.0.0
**Дата:** 2025-11-07 00:51
**Часть:** 2 из 3

---

## 🔴 ПРОБЛЕМА 6: Performance медленный

### Решения

**Добавить индексы:**
```sql
CREATE INDEX idx_quests_era_level ON quests(era, min_level);
CREATE INDEX idx_player_flags_lookup ON player_flags(character_id, flag_key);
```

**N+1 Problem:**
```java
// ХОРОШО
@EntityGraph(attributePaths = {"branches"})
List<Quest> findAllWithBranches();
```

**Кэширование:**
```java
@Cacheable(value = "questGraph", key = "#questId")
public Quest getQuest(String questId) {
    return questRepository.findById(questId).orElseThrow();
}
```

---

## 🔴 ПРОБЛЕМА 7: Slow Queries

### Диагностика

```sql
EXPLAIN ANALYZE SELECT * FROM quests WHERE era = '2020-2030';
```

### Решения

**Индексы + projections:**
```java
@Query("SELECT new QuestSummary(q.id, q.name) FROM Quest q")
List<QuestSummary> findSummaries();
```

---

## 🔴 ПРОБЛЕМА 8: Redis connection failed

### Решения

```bash
# Проверить Redis
redis-cli ping

# Запустить
redis-server
```

---

## ➡️ Продолжение

**Далее:** [Part 3 - WebSocket](./troubleshooting-part3-advanced.md)

---

## История изменений

- v1.0.0 (2025-11-07 00:51) - Database & Performance

