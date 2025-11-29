# Issues для создания

## Миграция сервисов на oapi-codegen

### 1. [Backend] Мигрировать admin-service-go на использование oapi-codegen

**Описание:**
Сервис `admin-service-go` имеет `oapi-codegen.yaml` и OpenAPI спецификации, но не использует сгенерированный код через `HandlerFromMux`. Необходимо мигрировать на использование `oapi-codegen`.

**Детали:**
- Сервис: `admin-service-go`
- OpenAPI спецификации: `admin-auth-service.yaml`, `admin-content-service.yaml`, `admin-dashboard-service.yaml`, `admin-economy-service.yaml`, `admin-players-service.yaml`
- Текущее состояние: есть `oapi-codegen.yaml`, есть `Makefile`, но handlers не используют `HandlerFromMux`
- Роутер: `gorilla/mux` (использовать `gorilla-server`)

**Метки:** `agent:backend`, `stage:backend-dev`, `backend`, `migration`, `oapi-codegen`

---

### 2. [Backend] Мигрировать cosmetic-service-go на использование oapi-codegen

**Описание:**
Сервис `cosmetic-service-go` имеет `oapi-codegen.yaml` и OpenAPI спецификацию, но не использует сгенерированный код через `HandlerFromMux`. Необходимо мигрировать на использование `oapi-codegen`.

**Детали:**
- Сервис: `cosmetic-service-go`
- OpenAPI спецификация: `cosmetic-service.yaml`
- Текущее состояние: есть `oapi-codegen.yaml`, но handlers не используют `HandlerFromMux`

**Метки:** `agent:backend`, `stage:backend-dev`, `backend`, `migration`, `oapi-codegen`

---

### 3. [Backend] Мигрировать economy-service-go на использование oapi-codegen

**Описание:**
Сервис `economy-service-go` имеет `oapi-codegen.yaml` и множество OpenAPI спецификаций, но не использует сгенерированный код через `HandlerFromMux`. Необходимо мигрировать на использование `oapi-codegen`.

**Детали:**
- Сервис: `economy-service-go`
- OpenAPI спецификации: `admin-economy-service.yaml`, `economy-*-service.yaml` (множество спецификаций)
- Текущее состояние: есть `oapi-codegen.yaml`, но handlers не используют `HandlerFromMux`

**Метки:** `agent:backend`, `stage:backend-dev`, `backend`, `migration`, `oapi-codegen`

---

### 4. [Backend] Мигрировать world-service-go на использование oapi-codegen

**Описание:**
Сервис `world-service-go` имеет `oapi-codegen.yaml` и OpenAPI спецификации, но не использует сгенерированный код через `HandlerFromMux`. Необходимо мигрировать на использование `oapi-codegen`.

**Детали:**
- Сервис: `world-service-go`
- OpenAPI спецификации: `world-events-core-service.yaml`, `world-events-scheduler-service.yaml`, `world-events-analytics-service.yaml`, `world-seasonal-seasons-service.yaml`
- Текущее состояние: есть `oapi-codegen.yaml`, но handlers не используют `HandlerFromMux`

**Метки:** `agent:backend`, `stage:backend-dev`, `backend`, `migration`, `oapi-codegen`

---

## Issues для API Designer (сервисы без OpenAPI спецификаций)

### 5. [API Designer] Создать OpenAPI спецификацию для combat-ai-service-go

**Описание:**
Сервис `combat-ai-service-go` не имеет OpenAPI спецификации, но уже использует `HandlerFromMux` (возможно, есть спецификация, но не найдена). Необходимо создать или найти OpenAPI спецификацию.

**Детали:**
- Сервис: `combat-ai-service-go`
- Текущее состояние: использует `HandlerFromMux`, но спецификация не найдена

**Метки:** `agent:api-designer`, `stage:api-design`, `api-design`, `openapi`

---

### 6. [API Designer] Создать OpenAPI спецификацию для combat-damage-service-go

**Описание:**
Сервис `combat-damage-service-go` не имеет OpenAPI спецификации. Необходимо создать OpenAPI спецификацию для этого сервиса.

**Детали:**
- Сервис: `combat-damage-service-go`
- Текущее состояние: нет OpenAPI спецификации, не использует `oapi-codegen`

**Метки:** `agent:api-designer`, `stage:api-design`, `api-design`, `openapi`

---

### 7. [API Designer] Создать OpenAPI спецификацию для combat-hacking-service-go

**Описание:**
Сервис `combat-hacking-service-go` не имеет OpenAPI спецификации. Необходимо создать OpenAPI спецификацию для этого сервиса.

**Детали:**
- Сервис: `combat-hacking-service-go`
- Текущее состояние: нет OpenAPI спецификации, не использует `oapi-codegen`

**Метки:** `agent:api-designer`, `stage:api-design`, `api-design`, `openapi`

---

### 8. [API Designer] Создать OpenAPI спецификацию для combat-implants-core-service-go

**Описание:**
Сервис `combat-implants-core-service-go` не имеет OpenAPI спецификации, но уже использует `HandlerFromMux`. Необходимо создать OpenAPI спецификацию.

**Детали:**
- Сервис: `combat-implants-core-service-go`
- Текущее состояние: использует `HandlerFromMux`, но спецификация не найдена

**Метки:** `agent:api-designer`, `stage:api-design`, `api-design`, `openapi`

---

### 9. [API Designer] Создать OpenAPI спецификацию для combat-implants-maintenance-service-go

**Описание:**
Сервис `combat-implants-maintenance-service-go` не имеет OpenAPI спецификации. Необходимо создать OpenAPI спецификацию для этого сервиса.

**Детали:**
- Сервис: `combat-implants-maintenance-service-go`
- Текущее состояние: нет OpenAPI спецификации, не использует `oapi-codegen`

**Метки:** `agent:api-designer`, `stage:api-design`, `api-design`, `openapi`

---

### 10. [API Designer] Создать OpenAPI спецификацию для combat-implants-stats-service-go

**Описание:**
Сервис `combat-implants-stats-service-go` не имеет OpenAPI спецификации. Необходимо создать OpenAPI спецификацию для этого сервиса.

**Детали:**
- Сервис: `combat-implants-stats-service-go`
- Текущее состояние: нет OpenAPI спецификации, не использует `oapi-codegen`

**Метки:** `agent:api-designer`, `stage:api-design`, `api-design`, `openapi`

---

### 11. [API Designer] Создать OpenAPI спецификацию для combat-sandevistan-service-go

**Описание:**
Сервис `combat-sandevistan-service-go` не имеет OpenAPI спецификации. Необходимо создать OpenAPI спецификацию для этого сервиса.

**Детали:**
- Сервис: `combat-sandevistan-service-go`
- Текущее состояние: нет OpenAPI спецификации, не использует `oapi-codegen`

**Метки:** `agent:api-designer`, `stage:api-design`, `api-design`, `openapi`

---

### 12. [API Designer] Создать OpenAPI спецификации для stock-* сервисов

**Описание:**
Все stock-* сервисы не имеют OpenAPI спецификаций. Необходимо создать OpenAPI спецификации для всех stock сервисов.

**Сервисы:**
- `stock-analytics-charts-service-go`
- `stock-analytics-tools-service-go`
- `stock-dividends-service-go`
- `stock-events-service-go`
- `stock-futures-service-go`
- `stock-indices-service-go`
- `stock-integration-service-go`
- `stock-margin-service-go`
- `stock-options-service-go`
- `stock-protection-service-go` (уже использует `HandlerFromMux`)

**Детали:**
- Текущее состояние: нет OpenAPI спецификаций, большинство не используют `oapi-codegen`
- `stock-protection-service-go` уже использует `HandlerFromMux`, но спецификация не найдена

**Метки:** `agent:api-designer`, `stage:api-design`, `api-design`, `openapi`, `stock`

---

## Резюме

**Миграция на oapi-codegen (4 сервиса):**
1. admin-service-go
2. cosmetic-service-go
3. economy-service-go
4. world-service-go

**Создание OpenAPI спецификаций (17 сервисов):**
1. combat-ai-service-go
2. combat-damage-service-go
3. combat-hacking-service-go
4. combat-implants-core-service-go
5. combat-implants-maintenance-service-go
6. combat-implants-stats-service-go
7. combat-sandevistan-service-go
8-17. Все stock-* сервисы (10 сервисов)

