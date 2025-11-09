# Performance Tuning (Part 2: Application)

**Версия:** 1.0.0
**Дата:** 2025-11-07 00:53
**Часть:** 2 из 3

---

## Redis Caching

```java
@Cacheable(value = "questGraph", key = "#questId")
public Quest getQuest(String questId) {
    return questRepository.findById(questId).orElseThrow();
}
```

**TTL:**
- questGraph: 24 hours
- worldState: 5 minutes

---

## Query Optimization

**Projections:**
```java
@Query("SELECT new QuestSummary(q.id, q.name) FROM Quest q")
List<QuestSummary> findSummaries();
```

**Batch:**
```java
List<Quest> quests = questRepository.findAllById(questIds);
```

---

## Async Processing

```java
@Async
public CompletableFuture<Void> savePlayerChoice(...) {
    // Audit trail async
}
```

---

## ➡️ Продолжение

**Далее:** [Part 3 - Advanced](./performance-part3-advanced.md)

---

## История изменений

- v1.0.0 (2025-11-07 00:53) - Application optimization

