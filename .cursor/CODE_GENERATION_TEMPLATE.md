# Шаблон для применения генерации кода из OpenAPI спецификаций

Этот документ содержит шаблоны для применения генерации кода ко всем сервисам.

## ⚠️ КРИТИЧЕСКИ ВАЖНО: SOLID и лимит 500 строк

**ПРОБЛЕМА:** `oapi-codegen` генерирует **один большой файл** (2000-3000 строк), что нарушает:
- ❌ SOLID принципы (Single Responsibility)
- ❌ Лимит файлов 500 строк
- ❌ Читаемость и поддерживаемость кода

**РЕШЕНИЕ:** Генерация в **несколько файлов** (`types.gen.go`, `server.gen.go`, `spec.gen.go`)

---

## Файлы шаблона

### 1. Makefile (с раздельной генерацией)

Создай файл `services/{service-name}-go/Makefile`:

```makefile
.PHONY: generate-api bundle-api clean verify-api check-deps install-deps generate-types generate-server generate-spec check-file-sizes

SERVICE_NAME := {service-name}
OAPI_CODEGEN := oapi-codegen
REDOCLY_CLI := npx -y @redocly/cli
ROUTER_TYPE := {chi-server|gorilla-server}

SPEC_DIR := ../../proto/openapi
API_DIR := pkg/api
SERVICE_SPEC := $(SPEC_DIR)/$(SERVICE_NAME).yaml
BUNDLED_SPEC := $(API_DIR)/$(SERVICE_NAME).bundled.yaml

# Split output files (SOLID compliance)
TYPES_FILE := $(API_DIR)/types.gen.go
SERVER_FILE := $(API_DIR)/server.gen.go
SPEC_FILE := $(API_DIR)/spec.gen.go

check-deps:
	@echo "Checking dependencies..."
	@command -v $(OAPI_CODEGEN) >/dev/null 2>&1 || { echo "❌ oapi-codegen not found. Install: go install github.com/oapi-codegen/oapi-codegen/v2/cmd/oapi-codegen@latest"; exit 1; }
	@command -v node >/dev/null 2>&1 || { echo "❌ node not found. Install Node.js"; exit 1; }
	@echo "✅ All dependencies are installed"

install-deps:
	@echo "Installing dependencies..."
	@go install github.com/oapi-codegen/oapi-codegen/v2/cmd/oapi-codegen@latest || true
	@echo "✅ Dependencies installed"

bundle-api: check-deps
	@echo "Bundling OpenAPI spec: $(SERVICE_SPEC)"
	@if [ ! -f "$(SERVICE_SPEC)" ]; then \
		echo "❌ OpenAPI spec not found: $(SERVICE_SPEC)"; \
		exit 1; \
	fi
	@mkdir -p $(API_DIR)
	@$(REDOCLY_CLI) bundle $(SERVICE_SPEC) -o $(BUNDLED_SPEC) || { echo "❌ Failed to bundle"; exit 1; }
	@echo "✅ Bundled spec: $(BUNDLED_SPEC)"

# Generate types separately (models only)
generate-types: bundle-api
	@echo "Generating types from: $(BUNDLED_SPEC)"
	@$(OAPI_CODEGEN) -package api -generate types -o $(TYPES_FILE) $(BUNDLED_SPEC) || { echo "❌ Failed to generate types"; exit 1; }
	@echo "✅ Generated types: $(TYPES_FILE) ($$(wc -l < $(TYPES_FILE) | tr -d ' ') lines)"

# Generate server interface separately
generate-server: bundle-api
	@echo "Generating server interface from: $(BUNDLED_SPEC)"
	@$(OAPI_CODEGEN) -package api -generate $(ROUTER_TYPE) -o $(SERVER_FILE) $(BUNDLED_SPEC) || { echo "❌ Failed to generate server"; exit 1; }
	@echo "✅ Generated server: $(SERVER_FILE) ($$(wc -l < $(SERVER_FILE) | tr -d ' ') lines)"

# Generate spec embedding
generate-spec: bundle-api
	@echo "Generating spec embedding from: $(BUNDLED_SPEC)"
	@$(OAPI_CODEGEN) -package api -generate spec -o $(SPEC_FILE) $(BUNDLED_SPEC) || { echo "❌ Failed to generate spec"; exit 1; }
	@echo "✅ Generated spec: $(SPEC_FILE) ($$(wc -l < $(SPEC_FILE) | tr -d ' ') lines)"

# Check file sizes (500 line limit)
check-file-sizes:
	@echo "Checking file sizes (max 500 lines)..."
	@for file in $(TYPES_FILE) $(SERVER_FILE) $(SPEC_FILE); do \
		if [ -f "$$file" ]; then \
			lines=$$(wc -l < "$$file" | tr -d ' '); \
			if [ $$lines -gt 500 ]; then \
				echo "⚠️  WARNING: $$file has $$lines lines (exceeds 500 line limit)"; \
			else \
				echo "✅ $$file: $$lines lines (OK)"; \
			fi; \
		fi; \
	done

# Generate all files
generate-api: generate-types generate-server generate-spec check-file-sizes
	@echo ""
	@echo "✅ Code generation complete!"
	@echo "Files generated:"
	@ls -lh $(API_DIR)/*.gen.go 2>/dev/null || true

verify-api: check-deps
	@echo "Verifying OpenAPI spec: $(SERVICE_SPEC)"
	@$(REDOCLY_CLI) lint $(SERVICE_SPEC) || { echo "❌ Spec validation failed"; exit 1; }
	@echo "✅ Spec is valid"

clean:
	@echo "Cleaning generated files"
	@rm -f $(BUNDLED_SPEC) $(TYPES_FILE) $(SERVER_FILE) $(SPEC_FILE)
	@echo "✅ Cleaned"
```

**Замены:**
- `{service-name}` - имя сервиса (например: `companion-service`, `inventory-service`)
- `{chi-server|gorilla-server}` - тип роутера:
  - `chi-server` - если используется `github.com/go-chi/chi/v5` (рекомендуется)
  - `gorilla-server` - если используется `github.com/gorilla/mux`

### 2. oapi-codegen.yaml (не используется при раздельной генерации)

**ВАЖНО:** При использовании раздельной генерации через `Makefile` файл `oapi-codegen.yaml` **НЕ нужен**.
Все параметры передаются через флаги `oapi-codegen` в `Makefile`.

Если всё же хочешь использовать `oapi-codegen.yaml`, создай файл `services/{service-name}-go/oapi-codegen.yaml`:

```yaml
# Issue: NOT USED - generation is configured via Makefile
# This file is kept for reference only
package: api
output-options:
  skip-prune: true
```

### 3. .gitignore

Создай файл `services/{service-name}-go/.gitignore` (если его нет):

```gitignore
# Generated OpenAPI bundled files (DO NOT commit)
*.bundled.yaml
*.merged.yaml

# Generated API code (multiple files for SOLID compliance)
pkg/api/types.gen.go
pkg/api/server.gen.go
pkg/api/spec.gen.go

# Legacy single file (if exists)
pkg/api/api.gen.go

# Binaries
*.exe
*.exe~
*.dll
*.so
*.dylib

# Test binary
*.test

# Output of the go coverage tool
*.out
coverage/

# Dependency directories
vendor/

# IDE
.idea/
.vscode/
*.swp
*.swo
*~
```

## 📊 Статистика генерации (проблемные сервисы)

**Сервисы с НАРУШЕНИЕМ лимита 500 строк:**

| Сервис | api.gen.go | Превышение | Нужна миграция |
|--------|------------|------------|----------------|
| `voice-chat-service-go` | **2926** строк | 🔴 **5.9x** | ✅ КРИТИЧНО |
| `housing-service-go` | **1869** строк | 🔴 **3.7x** | ✅ КРИТИЧНО |
| `clan-war-service-go` | **1724** строки | 🔴 **3.4x** | ✅ КРИТИЧНО |
| `companion-service-go` | **1329** строк | 🔴 **2.6x** | ✅ Высокий |
| `cosmetic-service-go` | **1191** строка | 🔴 **2.4x** | ✅ Высокий |
| `referral-service-go` | **1168** строк | 🔴 **2.3x** | ✅ Высокий |
| `world-service-go` | **1142** строки | 🔴 **2.3x** | ✅ Высокий |
| `maintenance-service-go` | **1000** строк | 🔴 **2.0x** | ✅ Средний |

**Все эти сервисы требуют миграции на раздельную генерацию!**

---

## 🔧 Разбиение больших OpenAPI спецификаций

**Если OpenAPI спецификация >500 строк**, её нужно разбить на модули:

### Структура разбиения (пример: `voice-chat-service`):

```
proto/openapi/
├── voice-chat-service/
│   ├── main.yaml                    # Основной файл (info, servers, tags)
│   ├── schemas/
│   │   ├── channels.yaml            # Схемы для каналов (< 500 строк)
│   │   ├── rooms.yaml               # Схемы для комнат (< 500 строк)
│   │   ├── participants.yaml        # Схемы для участников (< 500 строк)
│   │   └── settings.yaml            # Схемы для настроек (< 500 строк)
│   └── paths/
│       ├── channels.yaml            # Endpoints для каналов (< 500 строк)
│       ├── rooms.yaml               # Endpoints для комнат (< 500 строк)
│       └── participants.yaml        # Endpoints для участников (< 500 строк)
└── voice-chat-service.yaml          # Главный файл с $ref ссылками
```

### Пример главного файла `voice-chat-service.yaml`:

```yaml
# Issue: #123
openapi: 3.0.0
info:
  title: Voice Chat Service API
  version: 1.0.0
  description: Voice chat management for NECP Game

servers:
  - url: http://localhost:8154
    description: Local development

tags:
  - name: channels
    description: Channel management
  - name: rooms
    description: Room management
  - name: participants
    description: Participant management

paths:
  # Channels endpoints
  /api/v1/voice-chat/channels:
    $ref: 'voice-chat-service/paths/channels.yaml#/paths/~1api~1v1~1voice-chat~1channels'
  
  # Rooms endpoints
  /api/v1/voice-chat/rooms:
    $ref: 'voice-chat-service/paths/rooms.yaml#/paths/~1api~1v1~1voice-chat~1rooms'

components:
  schemas:
    # Import schemas from separate files
    Channel:
      $ref: 'voice-chat-service/schemas/channels.yaml#/components/schemas/Channel'
    Room:
      $ref: 'voice-chat-service/schemas/rooms.yaml#/components/schemas/Room'
    Participant:
      $ref: 'voice-chat-service/schemas/participants.yaml#/components/schemas/Participant'
  
  # Common components from common.yaml
  securitySchemes:
    $ref: 'common.yaml#/components/securitySchemes'
  
  responses:
    $ref: 'common.yaml#/components/responses'

security:
  - BearerAuth: []
```

### Пример модуля `paths/channels.yaml`:

```yaml
# Issue: #123
# Module: Channels endpoints
paths:
  /api/v1/voice-chat/channels:
    get:
      tags: [channels]
      summary: List all channels
      operationId: listChannels
      parameters:
        - $ref: '../../../common.yaml#/components/parameters/PageParam'
        - $ref: '../../../common.yaml#/components/parameters/LimitParam'
      responses:
        '200':
          description: List of channels
          content:
            application/json:
              schema:
                type: object
                properties:
                  channels:
                    type: array
                    items:
                      $ref: '../schemas/channels.yaml#/components/schemas/Channel'
                  pagination:
                    $ref: '../../../common.yaml#/components/schemas/PaginationResponse'
        '400':
          $ref: '../../../common.yaml#/components/responses/BadRequest'
        '401':
          $ref: '../../../common.yaml#/components/responses/Unauthorized'
```

---

## Список сервисов для миграции

### 🔴 КРИТИЧНЫЕ (нужна миграция СЕЙЧАС):

1. **voice-chat-service-go** (2926 строк) - разбить спецификацию + раздельная генерация
2. **housing-service-go** (1869 строк) - раздельная генерация
3. **clan-war-service-go** (1724 строки) - раздельная генерация

### 🟡 ВЫСОКИЙ ПРИОРИТЕТ:

4. **companion-service-go** (1329 строк)
5. **cosmetic-service-go** (1191 строка)
6. **referral-service-go** (1168 строк)
7. **world-service-go** (1142 строки)

### 🟢 СРЕДНИЙ ПРИОРИТЕТ:

8. **maintenance-service-go** (1000 строк)
9. Остальные сервисы с api.gen.go >500 строк

## 🛠️ Скрипты для автоматизации

### 1. Миграция на раздельную генерацию

Скрипт `scripts/migrate-to-split-generation.sh` автоматически мигрирует все сервисы:

```bash
./scripts/migrate-to-split-generation.sh [service-name]

# Примеры:
./scripts/migrate-to-split-generation.sh voice-chat-service-go    # Один сервис
./scripts/migrate-to-split-generation.sh                          # Все сервисы
```

**Что делает скрипт:**
- Обновляет `Makefile` для раздельной генерации
- Удаляет старый `oapi-codegen.yaml` (если есть)
- Обновляет `.gitignore` для новых файлов
- Генерирует код в 3 файла: `types.gen.go`, `server.gen.go`, `spec.gen.go`
- Проверяет размеры файлов (макс 500 строк)

### 2. Добавление зависимостей

Скрипт `scripts/add-codegen-deps.sh` автоматически добавляет зависимость `github.com/oapi-codegen/runtime`:

```bash
./scripts/add-codegen-deps.sh
```

### 3. Валидация всех сервисов

Скрипт `scripts/validate-codegen.sh` проверяет все сервисы:

```bash
./scripts/validate-codegen.sh
```

**Что проверяет:**
- Наличие `Makefile` с раздельной генерацией
- Валидность OpenAPI спецификаций
- Размеры сгенерированных файлов (макс 500 строк)
- Структуру файлов в `pkg/api/`

---

## 📋 Процесс применения

### Для API Designer агента:

**Если спецификация >500 строк:**

1. **Разбей спецификацию на модули** (schemas/, paths/)
2. **Создай главный файл** с `$ref` ссылками
3. **Каждый модуль max 500 строк**
4. **Используй `common.yaml`** для общих компонентов
5. **Валидируй:** `redocly lint {service-name}.yaml`

### Для Backend Developer агента:

**Миграция существующего сервиса:**

1. **Запусти скрипт миграции:**
   ```bash
   cd services/{service-name}-go
   ../../scripts/migrate-to-split-generation.sh
   ```

2. **Сгенерируй код:**
   ```bash
   make generate-api
   ```

3. **Проверь размеры файлов:**
   ```bash
   make check-file-sizes
   ```

4. **Обнови импорты в handlers:**
   ```go
   // Было (старая структура):
   import "github.com/your-org/necpgame/services/{service}-go/pkg/api"
   
   // Стало (новая структура - ничего не меняется!):
   import "github.com/your-org/necpgame/services/{service}-go/pkg/api"
   
   // Все типы остаются в пакете api:
   var req api.CreateChannelRequest
   ```

5. **Обнови HTTP сервер** (если нужно):
   ```go
   // В server/http_server.go
   handler := handlers.NewHandlers(service)
   
   // Используй сгенерированный роутер
   api.HandlerWithOptions(handler, api.ChiServerOptions{
       BaseURL:    "/api/v1",
       BaseRouter: router,
   })
   ```

6. **Удали старые файлы:**
   ```bash
   rm -f pkg/api/api.gen.go  # Старый монолитный файл
   ```

7. **Обнови `.gitignore`:**
   ```gitignore
   # Новая структура
   pkg/api/types.gen.go
   pkg/api/server.gen.go
   pkg/api/spec.gen.go
   ```

8. **Коммит:**
   ```bash
   git add Makefile .gitignore
   git commit -m "[backend] refactor: migrate to split code generation for SOLID compliance

   - Split api.gen.go (2926 lines) into 3 files: types.gen.go, server.gen.go, spec.gen.go
   - Each file now <500 lines (SOLID compliance)
   - Updated Makefile for separate generation
   - Updated .gitignore for new structure

   Related Issue: #123"
   ```

---

## ✅ Чек-лист миграции

**Для каждого сервиса проверь:**

- [ ] `Makefile` обновлен (generate-types, generate-server, generate-spec)
- [ ] `.gitignore` обновлен (types.gen.go, server.gen.go, spec.gen.go)
- [ ] Старый `oapi-codegen.yaml` удален (или помечен как неиспользуемый)
- [ ] Генерация работает: `make generate-api`
- [ ] Размеры файлов проверены: `make check-file-sizes`
- [ ] Все файлы <500 строк
- [ ] Код компилируется: `go build ./...`
- [ ] Тесты проходят: `go test ./...`
- [ ] Docker образ собирается: `docker build -t test .`
- [ ] Старый `api.gen.go` удален
- [ ] Коммит создан с правильным префиксом `[backend]`

---

## 📚 Примеры

### Правильная структура после миграции:

```
services/{service-name}-go/
├── Makefile                        # С раздельной генерацией
├── .gitignore                      # Игнорирует *.gen.go и *.bundled.yaml
├── pkg/
│   └── api/
│       ├── types.gen.go           # <500 строк (модели)
│       ├── server.gen.go          # <500 строк (интерфейс сервера)
│       └── spec.gen.go            # <500 строк (embedded spec)
├── server/
│   ├── http_server.go             # Настройка сервера
│   ├── middleware.go              # Middleware
│   ├── handlers.go                # Реализация api.ServerInterface
│   ├── service.go                 # Бизнес-логика
│   └── repository.go              # БД
├── Dockerfile
└── go.mod
```

### Пример handlers.go (использование сгенерированных типов):

```go
// Issue: #123
package server

import (
    "net/http"
    "github.com/your-org/necpgame/services/{service}-go/pkg/api"
)

type Handlers struct {
    service Service
}

// NewHandlers создает handlers с DI
func NewHandlers(service Service) *Handlers {
    return &Handlers{service: service}
}

// Реализация api.ServerInterface (сгенерированный интерфейс)
func (h *Handlers) ListChannels(w http.ResponseWriter, r *http.Request, params api.ListChannelsParams) {
    // Используй сгенерированные типы
    channels, err := h.service.ListChannels(r.Context(), params)
    if err != nil {
        respondError(w, http.StatusInternalServerError, err.Error())
        return
    }
    
    // Используй сгенерированные response типы
    response := api.ListChannelsResponse{
        Channels: channels,
    }
    
    respondJSON(w, http.StatusOK, response)
}
```

