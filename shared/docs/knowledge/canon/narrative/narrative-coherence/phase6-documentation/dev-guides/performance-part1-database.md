# Performance Tuning (Part 1: Database)

**Версия:** 1.0.0
**Дата:** 2025-11-07 00:52
**Часть:** 1 из 3

---

## Навигация

- **Part 1:** Database (этот файл)
- **Part 2:** [Application](./performance-part2-application.md)
- **Part 3:** [Advanced](./performance-part3-advanced.md)

---

## 🎯 Targets

| Operation | Target |
|-----------|--------|
| Get quests | < 100ms |
| Quest details | < 50ms |
| Process choice | < 200ms |

---

## 🚀 Database Optimization

### Индексы (критично!)

```sql
CREATE INDEX idx_quests_era_level ON quests(era, min_level, max_level);
CREATE INDEX idx_player_flags_character_key ON player_flags(character_id, flag_key);
CREATE INDEX idx_quests_tags_gin ON quests USING GIN(tags);
```

### Партиционирование

```sql
CREATE TABLE player_quest_choices_2025_01 PARTITION OF player_quest_choices
    FOR VALUES FROM ('2025-01-01') TO ('2025-02-01');
```

### Connection Pool

```yaml
spring:
  datasource:
    hikari:
      maximum-pool-size: 200
      minimum-idle: 20
```

---

## ➡️ Продолжение

**Далее:** [Part 2 - Application](./performance-part2-application.md)

---

## История изменений

- v1.0.0 (2025-11-07 00:52) - Database optimization

