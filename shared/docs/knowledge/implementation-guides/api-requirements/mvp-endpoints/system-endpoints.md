---
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 06:05
**api-readiness-notes:** MVP System Endpoints. Error handling, validation, health checks. ~120 строк.
---

# MVP System Endpoints - Системные endpoint'ы

**Статус:** approved  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-07  
**Последнее обновление:** 2025-11-07 (обновлено для микросервисов)  
**Приоритет:** КРИТИЧЕСКИЙ (MVP)  
**Автор:** AI Brain Manager

**Микрофича:** System endpoints  
**Размер:** ~120 строк ✅  

---

## Микросервисная архитектура

**System endpoints в каждом микросервисе:**
- auth-service (8081): `/actuator/health`, `/actuator/info`
- character-service (8082): `/actuator/health`
- gameplay-service (8083): `/actuator/health`
- social-service (8084): `/actuator/health`
- economy-service (8085): `/actuator/health`
- world-service (8086): `/actuator/health`

**API Gateway health:** http://localhost:8080/actuator/health  
**Eureka Dashboard:** http://localhost:8761

---

## Error Responses

### Standard Error Format

```json
{
  "success": false,
  "error": {
    "code": "ERROR_CODE",
    "message": "Human readable message",
    "details": {}
  }
}
```

### Error Codes

```
AUTH_001 - Invalid credentials
AUTH_002 - Token expired
AUTH_003 - Token invalid
CHAR_001 - Character not found
CHAR_002 - Max characters limit
QUEST_001 - Quest not available
QUEST_002 - Requirements not met
INV_001 - Item not found
INV_002 - Inventory full
COMBAT_001 - Combat session not found
TRADE_001 - Insufficient funds
```

---

## Validation

### Common Validation Rules

**UUIDs:**
- Format: `^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$`

**Strings:**
- name: 3-50 characters
- username: 3-20 characters, alphanumeric + underscore
- description: max 500 characters

**Numbers:**
- level: 1-100
- price: >= 0
- quantity: >= 1

---

## Health Checks

### GET /api/v1/health

**Response 200:**
```json
{
  "status": "UP",
  "components": {
    "database": "UP",
    "redis": "UP",
    "eventBus": "UP"
  }
}
```

---

## Связанные документы

- `.BRAIN/05-technical/api-requirements/mvp-endpoints/auth-endpoints.md` - Auth (микрофича 1/4)
- `.BRAIN/05-technical/api-requirements/mvp-endpoints/gameplay-endpoints.md` - Gameplay (микрофича 2/4)
- `.BRAIN/05-technical/api-requirements/mvp-endpoints/content-endpoints.md` - Content (микрофича 3/4)

---

## История изменений

- **v1.0.0 (2025-11-07 06:05)** - Микрофича 4/4: System Endpoints (split from mvp-endpoints.md)
