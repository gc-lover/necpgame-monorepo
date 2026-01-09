---
trigger: model_decision
---

---
description: "API Designer rules: OpenAPI 3.0, Domain Separation, Struct Alignment, Ogen compatibility. Creates REST specs, enforced strictly."
globs: ["**/proto/openapi/**/*.yaml", "**/openapi*.yaml", "**/api-spec*.yaml"]
priority: 1
tags: ["api", "openapi", "spec", "design"]
version: "1.0"
---

# API Designer Agent Rules

## 1. Роль и Область ответственности

**Role:** Создание OpenAPI 3.0 спецификаций для REST API (ogen-compatible).
**Outputs:** `.yaml` файлы в `proto/openapi/`.
**NOT Responsible:**
- `.proto` файлы (Real-time/Voice/Sync -> Network Engineer).
- Backend implementation.
- Content generation.

## 2. 🏗️ Архитектура: Enterprise Domain Separation

**CRITICAL:** Все спецификации СТРОГО используют наследование (Inheritance) от Common Core. Дублирование полей ЗАПРЕЩЕНО.

### Структура Директорий
```text
proto/openapi/
├── common/                     # SOLID/DRY Foundation
│   ├── schemas/                # Shared Entities (Base, Game, Economy, Social)
│   ├── responses/              # Standard Responses (Success, Error)
│   ├── operations/crud.yaml    # Standard CRUD & Optimistic Locking
│   └── security/               # SecuritySchemes (BearerAuth)
├── {domain}-service/           # Service Specifications (<1000 lines)
│   ├── main.yaml               # Service Endpoint Definition
│   └── README.md
```

### Принципы Наследования (Inheritance Pattern)
Все сущности должны наследовать базовые поля из `common/schemas/`.

```yaml
# Пример сервисного объекта
CombatUnit:
  allOf:
    - $ref: '../common/schemas/game-entities.yaml#/CharacterEntity' # Inherit common (ID, Stats)
    - type: object
      required: [unit_type]
      properties:
        unit_type: {type: string, enum: ['infantry', 'mech']}       # Service-specific
```

### Основные Домены
1. **System:** `auth`, `session`, `profile` (Inherits: `infrastructure-entities.yaml`)
2. **Game:** `combat`, `ability`, `movement` (Inherits: `game-entities.yaml`)
3. **Economy:** `trading`, `auction`, `currency` (Inherits: `economy-entities.yaml`)
4. **Social:** `guild`, `friend`, `chat` (Inherits: `social-entities.yaml`)
5. **World:** `location`, `city` (Inherits: `game-entities.yaml`)

## 3. 🔧 Правила Проектирования (Design Rules)

### Strict Typing & Constraints
- **All Fields:** Must have `type`, `example`, `description`.
- **Strings:** Define `minLength`, `maxLength`, `pattern`.
- **Integers:** Define `minimum`, `maximum`, `format` (`int32`/`int64`).
- **Enums:** Use for all fixed sets of values.
- **Arrays:** Use `maxItems` where possible (Fixed-size arrays preferred).

### Performance: Struct Alignment (Backend Optimization)
**GOAL:** Save 30-50% memory in Go structs.
**Rule:** Order fields from Largest to Smallest size.

1. **Large (16-24B):** `string`, `array`, `object` ($ref), `time`
2. **Medium (8B):** `int64`, `float64`, `uint64`
3. **Small (4B):** `int32`, `float32`, `enum` (if int based)
4. **Byte (1B):** `boolean`, `byte`

**Nullable:** AVOID `nullable: true` (creates pointer). Use `default` values instead.

### Concurrency: Optimistic Locking
Для операций обновления (PUT/PATCH) обязательна проверка версии.
- Используйте `common/operations/crud.yaml#/UpdateRequest`.
- Поле `version` (int) инкрементируется при каждом изменении.

## 4. 🚀 Workflow

### Алгоритм работы
1. **НАЙТИ:** `Agent:"API" Status:"Todo"`
2. **ВЗЯТЬ:** Status → `In Progress` (`83d488e7`), Agent → `API` (`6aa5d9af`)
3. **АНАЛИЗ:**
   - Если Protocol == `OpenAPI 3.0` → Work.
   - Если Protocol == `Protobuf` (Real-time) → Pass to `Network` Agent.
4. **РАБОТАТЬ:**
   - Создать/Обновить YAML в `proto/openapi/{domain}/{service}/`.
   - Применить Domain Separation.
   - Валидировать (Validation).
5. **ПЕРЕДАТЬ:** Status → `Todo` (`f75ad846`), Agent → `Backend` (`1fc13998`).

### Инструменты Валидации (ОБЯЗАТЕЛЬНО)
```bash
# 1. Linting & Validation
redocly lint proto/openapi/{domain}/{service}/main.yaml
python scripts/validate-domains-openapi.py --domain {domain}

# 2. Optimization (Struct Alignment)
python scripts/batch-optimize-openapi-struct-alignment.py proto/openapi/{domain}/main.yaml
```

## 5. File Constraints & Logic
- **Source Limits:** YAML файлы исходников < 1000 строк. Если больше — разбивать на `$ref`.
- **Generated Files:** `openapi-bundled.yaml` и `oas_*_gen.go` МОГУТ быть большими (исключены из лимитов через `.githooks`).
- **Tools:** Используй `scripts/` для массовой генерации/валидации.
