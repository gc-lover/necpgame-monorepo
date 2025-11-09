# Step-by-Step Backend Setup (Part 3: Testing & Deploy)

**Версия:** 1.0.0  
**Дата:** 2025-11-07 00:48  
**Часть:** 3 из 3

---

## Навигация

- **Part 1:** [Setup & Database](./step-by-step-part1-setup.md)
- **Part 2:** [Backend Code](./step-by-step-part2-code.md)
- **Part 3:** Testing & Deploy (этот файл)

---

## 📋 STEP 10: Configuration (10 минут)

### application.yml

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/necpgame
    username: postgres
    password: ${DB_PASSWORD}
    hikari:
      maximum-pool-size: 20
  
  redis:
    host: localhost
    port: 6379
  
  jpa:
    hibernate:
      ddl-auto: validate
```

**✅ Checkpoint:** Config готов

---

## 📋 STEP 11: Testing (30 минут)

### 11.1 Запустить

```bash
./mvnw spring-boot:run
```

### 11.2 Тестировать API

```bash
# Available quests
curl "http://localhost:8080/api/v1/narrative/quests/available?characterId=test-uuid"

# Quest details
curl "http://localhost:8080/api/v1/narrative/quests/MQ-2020-001?characterId=test-uuid"

# Make choice
curl -X POST "http://localhost:8080/api/v1/narrative/quests/MQ-2020-001/choice" \
  -H "Content-Type: application/json" \
  -d '{"characterId": "test-uuid", "nodeId": 2, "choiceId": "A1"}'
```

**✅ Checkpoint:** API работает

---

## 📋 STEP 12-14: См. Part 2

**Подробности:** [Part 2 - Backend Code](./step-by-step-part2-code.md)

---

## 🎊 ФИНАЛЬНЫЙ CHECKLIST

- [x] PostgreSQL running
- [x] 5 миграций применены
- [x] 13 таблиц созданы
- [x] JSON экспортированы
- [x] Dependencies добавлены
- [x] 8 Entities созданы
- [x] 6 Repositories созданы
- [x] Services созданы
- [x] Controllers созданы
- [x] Redis configured
- [x] API tested

**ВСЁ ГОТОВО! 🚀**

---

## История изменений

- v1.0.0 (2025-11-07 00:48) - Part 3 (Testing & Deploy)

