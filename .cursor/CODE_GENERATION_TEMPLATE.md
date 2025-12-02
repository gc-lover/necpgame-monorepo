# Шаблон для применения генерации кода из OpenAPI спецификаций

Этот документ содержит шаблоны для применения генерации кода ко всем сервисам.

## WARNING КРИТИЧЕСКИ ВАЖНО: SOLID и лимит 500 строк

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
ROUTER_TYPE := chi-server  # OK СТАНДАРТ для новых сервисов (НЕ меняй на gorilla!)

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
	@echo "OK All dependencies are installed"

install-deps:
	@echo "Installing dependencies..."
	@go install github.com/oapi-codegen/oapi-codegen/v2/cmd/oapi-codegen@latest || true
	@echo "OK Dependencies installed"

bundle-api: check-deps
	@echo "Bundling OpenAPI spec: $(SERVICE_SPEC)"
	@if [ ! -f "$(SERVICE_SPEC)" ]; then \
		echo "❌ OpenAPI spec not found: $(SERVICE_SPEC)"; \
		exit 1; \
	fi
	@mkdir -p $(API_DIR)
	@$(REDOCLY_CLI) bundle $(SERVICE_SPEC) -o $(BUNDLED_SPEC) || { echo "❌ Failed to bundle"; exit 1; }
	@echo "OK Bundled spec: $(BUNDLED_SPEC)"

# Generate types separately (models only)
generate-types: bundle-api
	@echo "Generating types from: $(BUNDLED_SPEC)"
	@$(OAPI_CODEGEN) -package api -generate types -o $(TYPES_FILE) $(BUNDLED_SPEC) || { echo "❌ Failed to generate types"; exit 1; }
	@echo "OK Generated types: $(TYPES_FILE) ($$(wc -l < $(TYPES_FILE) | tr -d ' ') lines)"

# Generate server interface separately
generate-server: bundle-api
	@echo "Generating server interface from: $(BUNDLED_SPEC)"
	@$(OAPI_CODEGEN) -package api -generate $(ROUTER_TYPE) -o $(SERVER_FILE) $(BUNDLED_SPEC) || { echo "❌ Failed to generate server"; exit 1; }
	@echo "OK Generated server: $(SERVER_FILE) ($$(wc -l < $(SERVER_FILE) | tr -d ' ') lines)"

# Generate spec embedding
generate-spec: bundle-api
	@echo "Generating spec embedding from: $(BUNDLED_SPEC)"
	@$(OAPI_CODEGEN) -package api -generate spec -o $(SPEC_FILE) $(BUNDLED_SPEC) || { echo "❌ Failed to generate spec"; exit 1; }
	@echo "OK Generated spec: $(SPEC_FILE) ($$(wc -l < $(SPEC_FILE) | tr -d ' ') lines)"

# Check file sizes (500 line limit)
check-file-sizes:
	@echo "Checking file sizes (max 500 lines)..."
	@for file in $(TYPES_FILE) $(SERVER_FILE) $(SPEC_FILE); do \
		if [ -f "$$file" ]; then \
			lines=$$(wc -l < "$$file" | tr -d ' '); \
			if [ $$lines -gt 500 ]; then \
				echo "WARNING  WARNING: $$file has $$lines lines (exceeds 500 line limit)"; \
			else \
				echo "OK $$file: $$lines lines (OK)"; \
			fi; \
		fi; \
	done

# Generate all files
generate-api: generate-types generate-server generate-spec check-file-sizes
	@echo ""
	@echo "OK Code generation complete!"
	@echo "Files generated:"
	@ls -lh $(API_DIR)/*.gen.go 2>/dev/null || true

verify-api: check-deps
	@echo "Verifying OpenAPI spec: $(SERVICE_SPEC)"
	@$(REDOCLY_CLI) lint $(SERVICE_SPEC) || { echo "❌ Spec validation failed"; exit 1; }
	@echo "OK Spec is valid"

clean:
	@echo "Cleaning generated files"
	@rm -f $(BUNDLED_SPEC) $(TYPES_FILE) $(SERVER_FILE) $(SPEC_FILE)
	@echo "OK Cleaned"
```

**Замены:**
- `{service-name}` - имя сервиса (например: `companion-service`, `inventory-service`)
- `ROUTER_TYPE` уже установлен в `chi-server` - **это ЕДИНСТВЕННЫЙ стандарт!**

**Типы роутеров:**
- `chi-server` OK **ЕДИНСТВЕННЫЙ СТАНДАРТ** - используй для ВСЕХ сервисов
  - Зависимость: `github.com/go-chi/chi/v5`
  - Современный, легковесный, быстрый
- `gorilla-server` ❌ **ЗАПРЕЩЕН** - НЕ используется в проекте
  - Deprecated и больше не поддерживается
  - Если найден в существующем сервисе → **ОБЯЗАТЕЛЬНО мигрируй на Chi!**
  - См. `.cursor/rules/agent-backend.mdc` секция "Миграция с Gorilla на Chi"

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

---

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

## OK Чек-лист миграции

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

## 📚 Структура после миграции

```
services/{service-name}-go/
├── Makefile                   # С раздельной генерацией
├── .gitignore
├── pkg/api/
│   ├── types.gen.go          # <500 строк
│   ├── server.gen.go         # <500 строк
│   └── spec.gen.go           # <500 строк
├── server/
│   ├── http_server.go
│   ├── handlers.go           # Реализация api.ServerInterface
│   └── service.go
└── go.mod
```

**Примеры кода:** См. `.cursor/rules/agent-backend.mdc`

