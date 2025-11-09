# Troubleshooting (Part 3: WebSocket & Advanced)

**Версия:** 1.0.0
**Дата:** 2025-11-07 00:52
**Часть:** 3 из 3

---

## 🔴 ПРОБЛЕМА 9: WebSocket не работает

### Решения

**CORS:**
```java
registry.addEndpoint("/ws/narrative")
    .setAllowedOrigins("http://localhost:3000")
    .withSockJS();
```

**Правильный topic:**
```java
messagingTemplate.convertAndSend(
    "/topic/server/" + serverId + "/world-state", event
);
```

---

## 🔴 ПРОБЛЕМА 10: Memory leak

### Решения

**Cache eviction:**
```java
@Cacheable(value = "quests", unless = "#result == null")
```

**Session timeout:**
```yaml
spring:
  session:
    timeout: 30m
```

---

## 🛠️ Debugging Tools

### SQL Logging

```yaml
logging:
  level:
    org.hibernate.SQL: DEBUG
```

### Redis Monitor

```bash
redis-cli monitor
```

---

## 🆘 Emergency Fixes

**Rollback миграций:**
```bash
psql -d necpgame -f rollback/005-rollback-world-state.sql
```

**Очистить cache:**
```bash
redis-cli FLUSHDB
```

---

## История изменений

- v1.0.0 (2025-11-07 00:52) - WebSocket & Advanced

