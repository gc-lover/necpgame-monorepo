# 🎯 SOLID Code Generation Guide

## 📋 Проблема

**Текущая ситуация:**
- `oapi-codegen` генерирует **один большой файл** `api.gen.go` (2000-3000 строк)
- **Нарушение SOLID принципов** (Single Responsibility)
- **Превышение лимита файлов** (500 строк max)
- **Низкая читаемость** и поддерживаемость кода

**Проблемные сервисы:**

| Сервис | api.gen.go | Превышение | Статус |
|--------|------------|------------|--------|
| `voice-chat-service-go` | **2926** строк | 🔴 **5.9x** | Требует миграции |
| `housing-service-go` | **1869** строк | 🔴 **3.7x** | Требует миграции |
| `clan-war-service-go` | **1724** строки | 🔴 **3.4x** | Требует миграции |
| `companion-service-go` | **1329** строк | 🔴 **2.6x** | Требует миграции |
| `cosmetic-service-go` | **1191** строка | 🔴 **2.4x** | Требует миграции |

---

## ✅ Решение: Раздельная генерация

**Новый подход:**
- ❌ **Старый:** 1 файл `api.gen.go` (2000-3000 строк)
- ✅ **Новый:** 3 файла `types.gen.go`, `server.gen.go`, `spec.gen.go` (каждый <500 строк)

**Преимущества:**
- ✅ Соблюдение SOLID принципов
- ✅ Каждый файл <500 строк
- ✅ Лучшая читаемость и поддерживаемость
- ✅ Разделение ответственности (типы, сервер, спецификация)

---

## 📚 Для API Designer агента

### Правило: Разбивай большие спецификации

**Если OpenAPI спецификация >500 строк:**

1. **Создай модульную структуру:**

```
proto/openapi/
├── {service-name}/
│   ├── main.yaml                    # Основной файл
│   ├── schemas/
│   │   ├── {domain1}.yaml           # Схемы домена 1 (<500 строк)
│   │   ├── {domain2}.yaml           # Схемы домена 2 (<500 строк)
│   │   └── {domain3}.yaml           # Схемы домена 3 (<500 строк)
│   └── paths/
│       ├── {domain1}.yaml           # Endpoints домена 1 (<500 строк)
│       ├── {domain2}.yaml           # Endpoints домена 2 (<500 строк)
│       └── {domain3}.yaml           # Endpoints домена 3 (<500 строк)
└── {service-name}.yaml              # Главный файл с $ref ссылками
```

2. **Главный файл использует $ref:**

```yaml
# Issue: #123
openapi: 3.0.0
info:
  title: Voice Chat Service API
  version: 1.0.0

paths:
  /api/v1/channels:
    $ref: 'voice-chat-service/paths/channels.yaml#/paths/~1api~1v1~1channels'

components:
  schemas:
    Channel:
      $ref: 'voice-chat-service/schemas/channels.yaml#/components/schemas/Channel'
  
  # ALWAYS use common.yaml
  securitySchemes:
    $ref: 'common.yaml#/components/securitySchemes'
```

3. **Валидация:**

```bash
redocly lint proto/openapi/{service-name}.yaml
redocly bundle proto/openapi/{service-name}.yaml -o /tmp/bundled.yaml
```

**Подробнее:** `.cursor/rules/agent-api-designer.mdc` (секция "Splitting Large Specs")

---

## 🛠️ Для Backend Developer агента

### Правило: Используй раздельную генерацию

**Структура генерации:**

```
services/{service}-go/
├── pkg/api/
│   ├── types.gen.go              # <500 строк (модели, схемы)
│   ├── server.gen.go             # <500 строк (ServerInterface, роутер)
│   └── spec.gen.go               # <500 строк (embedded OpenAPI spec)
├── server/
│   ├── http_server.go            # Настройка сервера
│   ├── middleware.go             # Middleware
│   ├── handlers.go               # Реализация api.ServerInterface
│   ├── service.go                # Бизнес-логика
│   └── repository.go             # БД
├── Makefile                      # С раздельной генерацией
└── .gitignore                    # Игнорирует *.gen.go
```

### Makefile команды:

```bash
make generate-api      # Генерация всех 3 файлов
make generate-types    # Только types.gen.go
make generate-server   # Только server.gen.go
make generate-spec     # Только spec.gen.go
make check-file-sizes  # Проверка лимита 500 строк
make verify-api        # Валидация OpenAPI spec
make clean             # Удаление сгенерированных файлов
```

### Миграция существующего сервиса:

**Windows (PowerShell):**
```powershell
# Один сервис
.\scripts\migrate-to-split-generation.ps1 -ServiceName voice-chat-service

# Все проблемные сервисы
.\scripts\migrate-to-split-generation.ps1
```

**Linux/macOS:**
```bash
# Один сервис
./scripts/migrate-to-split-generation.sh voice-chat-service

# Все проблемные сервисы
./scripts/migrate-to-split-generation.sh
```

**Скрипт автоматически:**
1. Создает новый `Makefile` с раздельной генерацией
2. Обновляет `.gitignore`
3. Удаляет старый `oapi-codegen.yaml` (не нужен)
4. Генерирует код в 3 файла
5. Проверяет размеры файлов

### Чек-лист после миграции:

- [ ] 3 файла созданы: `types.gen.go`, `server.gen.go`, `spec.gen.go`
- [ ] Каждый файл <500 строк (`make check-file-sizes`)
- [ ] Старый `api.gen.go` удален
- [ ] `.gitignore` обновлен
- [ ] Код компилируется: `go build ./...`
- [ ] Тесты проходят: `go test ./...`
- [ ] Docker образ собирается

**Подробнее:** `.cursor/rules/agent-backend.mdc` (секция "Генерация кода из OpenAPI")

---

## 🔧 Шаблоны и примеры

### Полный шаблон Makefile:

См. `.cursor/CODE_GENERATION_TEMPLATE.md` - полный шаблон с:
- Раздельной генерацией
- Проверкой зависимостей
- Валидацией размеров файлов
- Примерами использования

### Пример handlers.go:

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

func NewHandlers(service Service) *Handlers {
    return &Handlers{service: service}
}

// Реализация api.ServerInterface (из server.gen.go)
func (h *Handlers) ListChannels(w http.ResponseWriter, r *http.Request, params api.ListChannelsParams) {
    // Используй типы из types.gen.go
    channels, err := h.service.ListChannels(r.Context(), params)
    if err != nil {
        respondError(w, http.StatusInternalServerError, err.Error())
        return
    }
    
    // Response типы из types.gen.go
    response := api.ListChannelsResponse{
        Channels: channels,
    }
    
    respondJSON(w, http.StatusOK, response)
}
```

**Важно:** Импорт не меняется! Все типы остаются в пакете `api`.

---

## 📊 Статистика и приоритеты

### 🔴 КРИТИЧНЫЕ (миграция СРОЧНО):

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

---

## 🎓 Обучение агентов

### Основные правила:

1. **API Designer:**
   - ✅ Разбивай спецификации >500 строк на модули
   - ✅ Используй `$ref` для связи между модулями
   - ✅ Всегда используй `common.yaml` для общих компонентов
   - ❌ НЕ создавай монолитные спецификации >500 строк

2. **Backend Developer:**
   - ✅ Используй раздельную генерацию (3 файла)
   - ✅ Используй Chi роутер для НОВЫХ сервисов (`chi-server`)
   - ✅ Проверяй размеры файлов: `make check-file-sizes`
   - ✅ Удаляй старый `api.gen.go` после миграции
   - ❌ НЕ используй старый подход (1 файл)
   - ❌ НЕ используй Gorilla для новых сервисов (deprecated)

3. **Оба агента:**
   - ✅ Каждый файл максимум 500 строк
   - ✅ Соблюдай SOLID принципы
   - ✅ Используй скрипты для автоматизации
   - ❌ НЕ игнорируй лимит файлов

---

## 📁 Файлы для изучения

### Шаблоны:
- `.cursor/CODE_GENERATION_TEMPLATE.md` - полный шаблон с примерами

### Правила агентов:
- `.cursor/rules/agent-api-designer.mdc` - правила для API Designer
- `.cursor/rules/agent-backend.mdc` - правила для Backend Developer

### Скрипты:
- `scripts/migrate-to-split-generation.sh` - Linux/macOS
- `scripts/migrate-to-split-generation.ps1` - Windows
- `scripts/add-codegen-deps.sh` - добавление зависимостей
- `scripts/validate-codegen.sh` - валидация всех сервисов

---

## ✅ Чек-лист для агентов

### API Designer:

При создании новой спецификации:
- [ ] Проверил размер спецификации
- [ ] Если >500 строк → разбил на модули
- [ ] Использовал `$ref` для связи между модулями
- [ ] Использовал `common.yaml` для общих компонентов
- [ ] Валидировал: `redocly lint {service-name}.yaml`

### Backend Developer:

При генерации кода:
- [ ] Использовал раздельную генерацию (3 файла)
- [ ] Проверил размеры: `make check-file-sizes`
- [ ] Все файлы <500 строк
- [ ] Удалил старый `api.gen.go`
- [ ] Обновил `.gitignore`
- [ ] Код компилируется и тесты проходят

---

## 🚀 Быстрый старт

### Для нового сервиса:

1. **API Designer:** Создай спецификацию (<500 строк или разбей на модули)
2. **Backend Developer:** Используй шаблон из `.cursor/CODE_GENERATION_TEMPLATE.md`
3. **Backend Developer:** `make generate-api` → проверь размеры файлов
4. **Backend Developer:** Реализуй handlers

### Для существующего сервиса:

1. **Backend Developer:** `.\scripts\migrate-to-split-generation.ps1 -ServiceName {service}`
2. **Backend Developer:** Проверь результат: `make check-file-sizes`
3. **Backend Developer:** Удали старый `api.gen.go`
4. **Backend Developer:** Коммит с префиксом `[backend]`

---

## 💡 Полезные команды

```bash
# Проверка размеров всех api.gen.go
Get-ChildItem -Recurse -Filter "api.gen.go" | ForEach-Object { [PSCustomObject]@{File=$_.FullName; Lines=(Get-Content $_.FullName).Count} } | Sort-Object Lines -Descending

# Миграция одного сервиса
.\scripts\migrate-to-split-generation.ps1 -ServiceName voice-chat-service

# Миграция всех проблемных сервисов
.\scripts\migrate-to-split-generation.ps1

# Проверка размеров после генерации
cd services\{service-name}-go
make check-file-sizes

# Валидация OpenAPI спецификации
make verify-api
```

---

## 📞 Помощь

**Если возникли проблемы:**

1. **Проверь зависимости:**
   ```bash
   oapi-codegen version
   node --version
   ```

2. **Проверь структуру:**
   ```bash
   ls -la services/{service-name}-go/pkg/api/
   ```

3. **Проверь логи генерации:**
   ```bash
   make generate-api 2>&1 | tee generation.log
   ```

4. **Изучи документацию:**
   - `.cursor/CODE_GENERATION_TEMPLATE.md`
   - `.cursor/rules/agent-backend.mdc`
   - `.cursor/rules/agent-api-designer.mdc`

---

## 🎯 Итог

**Цель:** Соблюдение SOLID и лимита 500 строк через раздельную генерацию кода.

**Результат:** Каждый сгенерированный файл <500 строк, лучшая читаемость и поддерживаемость кода.

**Для агентов:** Всегда используйте новый подход с раздельной генерацией!

