# QA: Validate Result

Проверка готовности тестирования перед передачей Release/Game Balance.

## 📋 Чек-лист готовности

**QA готово когда:**

### Функциональное тестирование
- [ ] Все требования из Issue протестированы
- [ ] Acceptance criteria выполнены
- [ ] Happy path работает
- [ ] Edge cases проверены
- [ ] Error handling протестирован

### Тест-кейсы
- [ ] Тест-кейсы созданы и задокументированы
- [ ] Тест-кейсы покрывают все сценарии
- [ ] Воспроизводимые шаги описаны
- [ ] Ожидаемые результаты указаны

### Интеграция
- [ ] Backend API работает корректно
- [ ] Client (UE5) интегрирован с API
- [ ] Data flow между компонентами работает
- [ ] Аутентификация/авторизация работает

### Производительность (базовая)
- [ ] Нет критичных performance issues
- [ ] Response time приемлемый (<2s для обычных запросов)
- [ ] Нет memory leaks (базовая проверка)

### Баги
- [ ] **Все критичные баги исправлены**
- [ ] **Все major баги задокументированы**
- [ ] Minor баги задокументированы (но не блокируют релиз)
- [ ] Issues созданы для всех найденных багов

### Документация
- [ ] Результаты тестирования задокументированы
- [ ] Bug reports созданы (если есть баги)
- [ ] Test coverage report готов
- [ ] Комментарий с summary добавлен в Issue

## 🔍 Автоматические проверки

```bash
# 1. Backend тесты (если есть)
cd services/{service-name}-go
go test ./... -v

# 2. Integration тесты (если настроены)
docker-compose -f docker-compose.test.yml up --abort-on-container-exit

# 3. API health check
curl http://localhost:8080/health
curl http://localhost:8080/metrics

# 4. Client build (UE5, если применимо)
# Проверить что клиент компилируется
```

## OK Если всё готово

### Если нужна балансировка → Game Balance

```javascript
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,
  updated_field: [
    { id: 239690516, value: 'f75ad846' }, // Status: Todo
    { id: 243899542, value: '12e8fb71' }, // Agent: GameBalance
  ]
});

mcp_github_add_issue_comment({
  owner: 'gc-lover',
  repo: 'necpgame-monorepo',
  issue_number: issue_number,
  body: 'OK Testing complete. Handed off to Game Balance\n\n' +
        '**Test results:**\n' +
        '- All functional tests passed\n' +
        '- Integration verified\n' +
        '- No critical bugs found\n\n' +
        '**Needs balancing:**\n' +
        '- Weapon damage values\n' +
        '- Economy (prices/rewards)\n\n' +
        'Issue: #' + issue_number
});
```

### Если балансировка НЕ нужна → Release

```javascript
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,
  updated_field: [
    { id: 239690516, value: 'f75ad846' }, // Status: Todo
    { id: 243899542, value: 'f5878f68' }, // Agent: Release
  ]
});

mcp_github_add_issue_comment({
  owner: 'gc-lover',
  repo: 'necpgame-monorepo',
  issue_number: issue_number,
  body: 'OK Testing complete. Ready for Release\n\n' +
        '**Test results:**\n' +
        '- All functional tests passed OK\n' +
        '- Integration verified OK\n' +
        '- Performance acceptable OK\n' +
        '- No critical/major bugs found OK\n\n' +
        '**Test coverage:**\n' +
        '- Unit tests: X passed\n' +
        '- Integration tests: Y passed\n' +
        '- Manual testing: completed\n\n' +
        'Issue: #' + issue_number
});
```

## ❌ Если есть баги

**НЕ передавай дальше если есть критичные или блокирующие баги!**

### Вернуть Backend

```javascript
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,
  updated_field: [
    { id: 239690516, value: 'c01c12e9' }, // Status: Returned
    { id: 243899542, value: '1fc13998' }, // Agent: Backend
  ]
});

mcp_github_add_issue_comment({
  owner: 'gc-lover',
  repo: 'necpgame-monorepo',
  issue_number: issue_number,
  body: 'WARNING **Testing failed: Critical bugs found**\n\n' +
        '**Critical bugs:**\n' +
        '1. API returns 500 on valid request (Bug #XXX)\n' +
        '2. Data loss when updating user profile (Bug #YYY)\n\n' +
        '**Blocking issues:**\n' +
        '- Cannot proceed to release with these bugs\n' +
        '- Backend needs to fix critical issues\n\n' +
        '**Test results:**\n' +
        '- Functional tests: 80% passed, 20% failed\n' +
        '- Integration tests: FAILED\n\n' +
        '**Correct agent:** Backend Developer\n\n' +
        '**Status updated:** Returned (Agent: Backend)\n\n' +
        'Issue: #' + issue_number
});
```

### Вернуть UE5

```javascript
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,
  updated_field: [
    { id: 239690516, value: 'c01c12e9' }, // Status: Returned
    { id: 243899542, value: '56920475' }, // Agent: UE5
  ]
});

mcp_github_add_issue_comment({
  owner: 'gc-lover',
  repo: 'necpgame-monorepo',
  issue_number: issue_number,
  body: 'WARNING **Testing failed: Client bugs found**\n\n' +
        '**Critical bugs:**\n' +
        '1. UI not displaying data correctly (Bug #XXX)\n' +
        '2. Client crash on specific action (Bug #YYY)\n\n' +
        '**Test results:**\n' +
        '- Backend API: works OK\n' +
        '- Client integration: FAILED ❌\n\n' +
        '**Correct agent:** UE5 Developer\n\n' +
        '**Status updated:** Returned (Agent: UE5)\n\n' +
        'Issue: #' + issue_number
});
```

## 📊 ID (Status/Agent)

- Status: Todo `f75ad846`, In Progress `83d488e7`, Review `55060662`, Blocked `af634d5b`, Returned `c01c12e9`, Done `98236657`
- Agent: Backend `1fc13998`, UE5 `56920475`, GameBalance `12e8fb71`, Release `f5878f68`, QA `3352c488`, API `6aa5d9af`, DB `1e745162`, Network `c60ebab1`, Security `12586c50`, DevOps `7e67a39b`, UI/UX `98c65039`, Content `d3cae8d8`, Idea `8c3f5f11`, Architect `d109c7f9`, Performance `d16ede50`

## 🐛 Создание Bug Issues

**Для каждого найденного бага создай отдельный Issue:**

```javascript
mcp_github_issue_write({
  method: 'create',
  owner: 'gc-lover',
  repo: 'necpgame-monorepo',
  title: '[BUG] API returns 500 on valid companion request',
  body: '**Описание:**\n' +
        'API endpoint `/api/v1/companions` возвращает 500 при валидном запросе\n\n' +
        '**Шаги воспроизведения:**\n' +
        '1. Отправить GET `/api/v1/companions?user_id=123`\n' +
        '2. Получить 500 Internal Server Error\n\n' +
        '**Ожидаемое поведение:**\n' +
        'Должен вернуть 200 OK с списком компаньонов\n\n' +
        '**Actual behavior:**\n' +
        '500 Internal Server Error\n\n' +
        '**Logs:**\n' +
        '```\npanic: runtime error: nil pointer dereference\n```\n\n' +
        '**Severity:** Critical\n\n' +
        'Related to Issue: #' + original_issue_number,
  labels: ['bug', 'backend', 'priority-high']
});
```

## 🔄 Review (опционально)

Для сложного тестирования можешь использовать:

```javascript
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,
  updated_field: {
    id: 239690516,
    value: 'e7fc0d6e'  // STATUS_OPTIONS['QA - Review']
  }
});
```

После review → передать дальше или вернуть с багами.

## WARNING Важно

**QA НЕ валидирует YAML квесты!**
- Content Writer валидирует YAML самостоятельно
- QA тестирует только после импорта квеста в БД

