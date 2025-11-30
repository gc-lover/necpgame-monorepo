# Шаблон для применения генерации кода из OpenAPI спецификаций

Этот документ содержит шаблоны для применения генерации кода ко всем сервисам.

## Файлы шаблона

### 1. Makefile

Создай файл `services/{service-name}-go/Makefile`:

```makefile
.PHONY: generate-api bundle-api clean verify-api

SERVICE_NAME := {service-name}
OAPI_CODEGEN := oapi-codegen
REDOCLY_CLI := npx -y @redocly/cli
ROUTER_TYPE := {chi-server|gorilla-server}

SPEC_DIR := ../../proto/openapi
API_DIR := pkg/api
SERVICE_SPEC := $(SPEC_DIR)/$(SERVICE_NAME).yaml
BUNDLED_SPEC := $(API_DIR)/$(SERVICE_NAME).bundled.yaml
OUTPUT_FILE := $(API_DIR)/api.gen.go

bundle-api:
	@echo "Bundling OpenAPI spec: $(SERVICE_SPEC)"
	@mkdir -p $(API_DIR)
	$(REDOCLY_CLI) bundle $(SERVICE_SPEC) -o $(BUNDLED_SPEC)

generate-api: bundle-api
	@echo "Generating Go code from: $(BUNDLED_SPEC)"
	$(OAPI_CODEGEN) -package api -generate types,$(ROUTER_TYPE) -o $(OUTPUT_FILE) $(BUNDLED_SPEC)
	@echo "Generated code: $(OUTPUT_FILE)"

verify-api:
	@echo "Verifying OpenAPI spec: $(SERVICE_SPEC)"
	$(REDOCLY_CLI) lint $(SERVICE_SPEC)

clean:
	@echo "Cleaning generated files"
	rm -f $(BUNDLED_SPEC) $(OUTPUT_FILE)
```

**Замены:**
- `{service-name}` - имя сервиса (например: `companion-service`, `inventory-service`)
- `{chi-server|gorilla-server}` - тип роутера:
  - `chi-server` - если используется `github.com/go-chi/chi/v5`
  - `gorilla-server` - если используется `github.com/gorilla/mux`

### 2. oapi-codegen.yaml

Создай файл `services/{service-name}-go/oapi-codegen.yaml`:

```yaml
package: api
generate:
  models: true
  strict-server: true
output: pkg/api/api.gen.go
```

### 3. .gitignore

Создай файл `services/{service-name}-go/.gitignore` (если его нет):

```gitignore
# Generated OpenAPI bundled files
*.bundled.yaml

# Generated API code (uncomment if you want to exclude generated code from git)
# pkg/api/api.gen.go

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

## Список сервисов для миграции

### Сервисы с одной спецификацией (приоритетные):

1. **companion-service-go** → `companion-service.yaml`
   - Использует: `gorilla/mux`
   - Router type: `gorilla-server`

2. **inventory-service-go** → `inventory-service.yaml`
   - Использует: `gorilla/mux`
   - Router type: `gorilla-server`

3. **housing-service-go** → `housing-service.yaml`
   - Использует: `gorilla/mux`
   - Router type: `gorilla-server`

4. **clan-war-service-go** → `clan-war-service.yaml`
   - Использует: `gorilla/mux`
   - Router type: `gorilla-server`

5. **movement-service-go** → `movement-service.yaml`
   - Использует: `gorilla/mux`
   - Router type: `gorilla-server`

6. **referral-service-go** → `referral-service.yaml`
   - Использует: `gorilla/mux`
   - Router type: `gorilla-server`

7. **voice-chat-service-go** → `voice-chat-service.yaml`
   - Использует: `gorilla/mux`
   - Router type: `gorilla-server`

8. **reset-service-go** → `reset-service.yaml` OK (уже готов)

### Сервисы с примененными шаблонами:

OK **companion-service-go** - Makefile, oapi-codegen.yaml, .gitignore созданы
OK **inventory-service-go** - Makefile, oapi-codegen.yaml, .gitignore созданы
OK **housing-service-go** - Makefile, oapi-codegen.yaml, .gitignore созданы
OK **clan-war-service-go** - Makefile, oapi-codegen.yaml, .gitignore созданы
OK **movement-service-go** - Makefile, oapi-codegen.yaml, .gitignore созданы
OK **referral-service-go** - Makefile, oapi-codegen.yaml, .gitignore созданы
OK **voice-chat-service-go** - Makefile, oapi-codegen.yaml, .gitignore созданы

**Следующие шаги для этих сервисов:**
1. Добавить зависимость `github.com/oapi-codegen/runtime` в `go.mod`
2. Сгенерировать код: `make generate-api`
3. Мигрировать handlers на использование `api.ServerInterface`
4. Обновить HTTP сервер для использования сгенерированного кода

### Сервисы с множественными спецификациями:

- **achievement-service-go** → `achievement-*.yaml` (множественные)
- **admin-service-go** → `admin-service.yaml` (большой файл)
- **battle-pass-service-go** → `battle-pass-*.yaml` (множественные)
- **character-service-go** → `character-*.yaml` (множественные)
- **economy-service-go** → `economy-*.yaml` (множественные)
- **feedback-service-go** → `feedback-*.yaml` (множественные)
- **gameplay-service-go** → `gameplay-*.yaml` (множественные)
- **leaderboard-service-go** → `leaderboard-*.yaml` (множественные)
- **social-service-go** → `social-*.yaml` (множественные)
- **support-service-go** → `support-*.yaml` (множественные)

## Скрипты для автоматизации

### Добавление зависимостей

Скрипт `scripts/add-codegen-deps.sh` автоматически добавляет зависимость `github.com/oapi-codegen/runtime` во все сервисы:

```bash
./scripts/add-codegen-deps.sh
```

### Валидация всех сервисов

Скрипт `scripts/validate-codegen.sh` проверяет все сервисы на наличие необходимых файлов и валидность OpenAPI спецификаций:

```bash
./scripts/validate-codegen.sh
```

## Процесс применения

Для каждого сервиса:

1. Создай `Makefile` по шаблону
2. Создай `oapi-codegen.yaml`
3. Создай или обнови `.gitignore`
4. Проверь наличие OpenAPI спецификации
5. Определи тип роутера
6. Сгенерируй код: `make generate-api`
7. Мигрируй handlers на использование `api.ServerInterface`
8. Обнови HTTP сервер для использования сгенерированного кода

## Примеры

См. `services/reset-service-go/` как пример полностью настроенного сервиса с генерацией.

