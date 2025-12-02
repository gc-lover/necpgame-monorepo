# Backend: Validate Result

Проверка готовности бекенда перед передачей следующему этапу.

## 📋 Чек-лист готовности

**Backend готов когда:**

### Код
- [ ] API реализован, все endpoints из OpenAPI работают
- [ ] Handlers реализуют `api.ServerInterface` полностью
- [ ] Бизнес-логика в отдельных файлах (service.go, repository.go)
- [ ] SOLID принципы соблюдены (handlers.go, middleware.go, http_server.go разделены)
- [ ] Каждый файл <500 строк (включая сгенерированные!)
- [ ] Issue указан в начале каждого файла (`// Issue: #123`)

### Генерация кода (НОВАЯ ПРОВЕРКА)
- [ ] Используется раздельная генерация (3 файла: types.gen.go, server.gen.go, spec.gen.go)
- [ ] Каждый сгенерированный файл <500 строк
- [ ] Старый api.gen.go удален (если был)
- [ ] Makefile обновлен с generate-types, generate-server, generate-spec
- [ ] .gitignore обновлен (игнорирует *.gen.go, *.bundled.yaml)

### Тесты
- [ ] Unit тесты написаны (coverage >70%)
- [ ] Integration тесты для критичных endpoints
- [ ] Тесты пройдены: `go test ./...`

### Качество кода
- [ ] Код компилируется: `go build ./...`
- [ ] Линтер пройден: `golangci-lint run` (если настроен)
- [ ] Зависимости обновлены: `go mod tidy`

### Инфраструктура
- [ ] Health checks настроены (`/health`)
- [ ] Metrics endpoint настроен (`/metrics`)
- [ ] Структурированное логирование (JSON format)
- [ ] Docker образ собирается: `docker build -t service:test .`

### OpenAPI соответствие
- [ ] Все endpoints из OpenAPI спецификации реализованы
- [ ] Request/Response типы соответствуют спецификации
- [ ] Валидация входных данных настроена

### Документация
- [ ] README обновлен (если нужно)
- [ ] Коммиты с префиксом `[backend]`
- [ ] Комментарии в коде для сложной логики

## 🔍 Автоматические проверки

```bash
# 1. Проверка размеров сгенерированных файлов (НОВАЯ ПРОВЕРКА)
cd services/{service-name}-go
make check-file-sizes

# Ожидаемый вывод:
# ✅ pkg/api/types.gen.go: 350 lines (OK)
# ✅ pkg/api/server.gen.go: 280 lines (OK)
# ✅ pkg/api/spec.gen.go: 120 lines (OK)

# Если есть ⚠️ WARNING - файл превышает 500 строк!
# Действие: Вернись к API Designer, разбей спецификацию на модули

# 2. Компиляция
go build ./...

# 3. Тесты
go test ./... -cover

# 4. Зависимости
go mod tidy
go mod verify

# 5. Docker build
docker build -t {service-name}:test .

# 6. OpenAPI валидация (если есть изменения)
make verify-api
```

## ✅ Если всё готово

**Системная задача:**
```javascript
// Update Status to Network - Todo
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,
  updated_field: {
    id: 239690516,
    value: '944246f3'  // STATUS_OPTIONS['Network - Todo']
  }
});

// Добавить комментарий
mcp_github_add_issue_comment({
  owner: 'gc-lover',
  repo: 'necpgame-monorepo',
  issue_number: issue_number,
  body: '✅ Backend ready. Handed off to Network\n\n' +
        '**Completed:**\n' +
        '- API implemented and tested\n' +
        '- Health checks configured\n' +
        '- Docker image builds successfully\n\n' +
        'Issue: #' + issue_number
});
```

**Контент-квест (labels `canon`, `lore`, `quest`):**
```javascript
// Update Status to QA - Todo
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,
  updated_field: {
    id: 239690516,
    value: '86ca422e'  // STATUS_OPTIONS['QA - Todo']
  }
});

// Добавить комментарий
mcp_github_add_issue_comment({
  owner: 'gc-lover',
  repo: 'necpgame-monorepo',
  issue_number: issue_number,
  body: '✅ Backend ready (YAML imported to DB). Handed off to QA\n\n' +
        '**Quest import completed:**\n' +
        '- YAML imported via POST /api/v1/gameplay/quests/content/reload\n' +
        '- Data validated in quest_definitions table\n\n' +
        'Issue: #' + issue_number
});
```

## ❌ Если НЕ готово

**Исправь проблемы перед передачей!**

### Если сгенерированные файлы >500 строк:

**Причина:** OpenAPI спецификация слишком большая и не разбита на модули

**Действие:**
1. Верни задачу API Designer: Update Status to `API Designer - Returned`
2. Укажи проблему в комментарии:

```javascript
mcp_github_add_issue_comment({
  owner: 'gc-lover',
  repo: 'necpgame-monorepo',
  issue_number: issue_number,
  body: '⚠️ **Code generation produces files >500 lines**\n\n' +
        '**Generated files:**\n' +
        '- types.gen.go: XXX lines (exceeds limit)\n' +
        '- server.gen.go: YYY lines (exceeds limit)\n\n' +
        '**Root cause:** OpenAPI spec too large (not split into modules)\n\n' +
        '**Action required (API Designer):**\n' +
        '- Split spec into modules: `{service-name}/schemas/`, `{service-name}/paths/`\n' +
        '- Each module <500 lines\n' +
        '- Use `$ref` to link modules\n\n' +
        '**See:** `.cursor/rules/agent-api-designer.mdc` (Splitting Large Specs)\n\n' +
        'Issue: #' + issue_number
});
```

### Для других проблем:

Оставь статус `Backend - In Progress` и продолжи работу.

## 🔄 Review (опционально)

Можешь поставить статус на `Backend - Review` для финальной проверки:

```javascript
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,
  updated_field: {
    id: 239690516,
    value: '8b8c3ffb'  // STATUS_OPTIONS['Backend - Review']
  }
});
```

После review → передать дальше (Network/QA).

