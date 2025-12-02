# QA: Return to Developer

Возврат задачи разработчику если найдены баги или функционал не готов.

## 🚫 Причины возврата

### 1. Функционал не работает (Backend)

**Проверка:**
- API endpoints не работают
- Возвращает ошибки на валидных запросах
- Data flow нарушен
- Аутентификация не работает

**Если проблема:**
- Update Status to `Backend - Returned`
- Создать Bug Issues для каждой проблемы

### 2. Функционал не работает (Client)

**Проверка:**
- UI не отображается
- Client crash
- Интеграция с API не работает
- Игровая механика broken

**Если проблема:**
- Update Status to `UE5 - Returned`
- Создать Bug Issues

### 3. Функционал не готов

**Проверка:**
- Не все endpoints реализованы
- Не все features из Issue
- Acceptance criteria не выполнены

**Если проблема:**
- Update Status to `Backend - Returned` или `UE5 - Returned`

### 4. Это контентный квест (YAML)

**Проверка:**
- Labels: `canon`, `lore`, `quest`
- Только YAML файл, нет реализации

**Если проблема:**
- Update Status to `Content Writer - Returned`
- QA не тестирует YAML напрямую

### 5. OpenAPI проблемы

**Проверка:**
- API не соответствует OpenAPI спецификации
- Endpoints отсутствуют
- Request/Response не соответствуют схеме

**Если проблема:**
- Update Status to `API Designer - Returned`
- Указать несоответствия

## WARNING Как вернуть задачу

### Шаблон возврата к Backend

```javascript
// 1. Создать Bug Issues
const bug_issue = await mcp_github_issue_write({
  method: 'create',
  owner: 'gc-lover',
  repo: 'necpgame-monorepo',
  title: '[BUG] API endpoint returns 500 on valid request',
  body: '**Found during QA testing of Issue #' + original_issue + '**\n\n' +
        '**Problem:**\n' +
        'API endpoint `/api/v1/companions` returns 500 Internal Server Error\n\n' +
        '**Steps to reproduce:**\n' +
        '1. Send GET request to `/api/v1/companions?user_id=123`\n' +
        '2. Observe 500 error\n\n' +
        '**Expected:**\n' +
        'Should return 200 OK with companion list\n\n' +
        '**Actual:**\n' +
        '500 Internal Server Error\n\n' +
        '**Logs:**\n' +
        '```\npanic: nil pointer dereference\n```\n\n' +
        '**Severity:** Critical',
  labels: ['bug', 'backend', 'priority-high']
});

// 2. Обновить статус
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,
  updated_field: {
    id: 239690516,
    value: '40f37190'  // STATUS_OPTIONS['Backend - Returned']
  }
});

// 3. Добавить комментарий
mcp_github_add_issue_comment({
  owner: 'gc-lover',
  repo: 'necpgame-monorepo',
  issue_number: issue_number,
  body: 'WARNING **Testing failed: Critical bugs found**\n\n' +
        '**Critical bugs:**\n' +
        '- API returns 500 on valid request → Bug #' + bug_issue.number + '\n' +
        '- Data not persisting to database → Bug #YYY\n\n' +
        '**Test results:**\n' +
        '- Functional tests: 75% passed, 25% failed ❌\n' +
        '- Integration tests: FAILED ❌\n' +
        '- Cannot proceed to release\n\n' +
        '**Blocking issues:**\n' +
        '- These bugs block the release\n' +
        '- Backend must fix before re-submission to QA\n\n' +
        '**Correct agent:** Backend Developer\n\n' +
        '**Status updated:** `Backend - Returned`\n\n' +
        'Issue: #' + issue_number
});
```

### Шаблон возврата к UE5

```javascript
// 1. Создать Bug Issues
const bug_issue = await mcp_github_issue_write({
  method: 'create',
  owner: 'gc-lover',
  repo: 'necpgame-monorepo',
  title: '[BUG] Client crashes when opening companion menu',
  body: '**Found during QA testing of Issue #' + original_issue + '**\n\n' +
        '**Problem:**\n' +
        'UE5 client crashes when user opens companion menu\n\n' +
        '**Steps to reproduce:**\n' +
        '1. Launch client\n' +
        '2. Login as test user\n' +
        '3. Click "Companions" button\n' +
        '4. Client crashes\n\n' +
        '**Expected:**\n' +
        'Companion menu should open\n\n' +
        '**Actual:**\n' +
        'Client crashes with segfault\n\n' +
        '**Crash log:**\n' +
        '```\nSegmentation fault at 0x00000000\n```\n\n' +
        '**Environment:**\n' +
        '- UE5 version: 5.7\n' +
        '- Build: Debug\n\n' +
        '**Severity:** Critical',
  labels: ['bug', 'client', 'ue5', 'priority-high']
});

// 2. Обновить статус
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,
  updated_field: {
    id: 239690516,
    value: '855f4872'  // STATUS_OPTIONS['UE5 - Returned']
  }
});

// 3. Добавить комментарий
mcp_github_add_issue_comment({
  owner: 'gc-lover',
  repo: 'necpgame-monorepo',
  issue_number: issue_number,
  body: 'WARNING **Testing failed: Client bugs found**\n\n' +
        '**Critical bugs:**\n' +
        '- Client crashes on companion menu → Bug #' + bug_issue.number + '\n' +
        '- UI elements not rendering → Bug #YYY\n\n' +
        '**Test results:**\n' +
        '- Backend API: works correctly OK\n' +
        '- Client functionality: FAILED ❌\n' +
        '- Integration: Cannot test due to crashes\n\n' +
        '**Blocking issues:**\n' +
        '- Crashes prevent further testing\n' +
        '- UE5 must fix critical bugs\n\n' +
        '**Correct agent:** UE5 Developer\n\n' +
        '**Status updated:** `UE5 - Returned`\n\n' +
        'Issue: #' + issue_number
});
```

### Шаблон возврата к Content Writer

```javascript
// Обновить статус
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,
  updated_field: {
    id: 239690516,
    value: 'f4a7797e'  // STATUS_OPTIONS['Content Writer - Returned']
  }
});

// Добавить комментарий
mcp_github_add_issue_comment({
  owner: 'gc-lover',
  repo: 'necpgame-monorepo',
  issue_number: issue_number,
  body: 'WARNING **Task returned: Content quest not for QA**\n\n' +
        '**Reason:**\n' +
        '- This is content quest (YAML file)\n' +
        '- QA does NOT validate YAML quests\n' +
        '- Content Writer validates YAML themselves\n\n' +
        '**Workflow for content quests:**\n' +
        'Idea Writer → Content Writer → Backend (import) → QA (test after import)\n\n' +
        '**Current state:**\n' +
        '- YAML not imported to DB yet\n' +
        '- Backend needs to import first\n\n' +
        '**Correct agent:** Content Writer (to complete YAML)\n\n' +
        '**Status updated:** `Content Writer - Returned`\n\n' +
        'Issue: #' + issue_number
});
```

### Шаблон возврата к API Designer

```javascript
// Обновить статус
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,
  updated_field: {
    id: 239690516,
    value: 'd0352ed3'  // STATUS_OPTIONS['API Designer - Returned']
  }
});

// Добавить комментарий
mcp_github_add_issue_comment({
  owner: 'gc-lover',
  repo: 'necpgame-monorepo',
  issue_number: issue_number,
  body: 'WARNING **Testing failed: API spec mismatch**\n\n' +
        '**Problems found:**\n' +
        '- OpenAPI spec says endpoint returns CompanionList\n' +
        '- Actual API returns different structure\n' +
        '- Missing fields in response: level, skills\n\n' +
        '**OpenAPI spec issues:**\n' +
        '1. Response schema does not match implementation\n' +
        '2. Missing required fields in spec\n' +
        '3. Endpoint paths inconsistent\n\n' +
        '**Action needed:**\n' +
        '- Update OpenAPI spec to match implementation\n' +
        '- OR fix implementation to match spec\n\n' +
        '**Correct agent:** API Designer\n\n' +
        '**Status updated:** `API Designer - Returned`\n\n' +
        'Issue: #' + issue_number
});
```

## 📊 ID статусов для возврата

```javascript
const RETURN_STATUS_IDS = {
  'Backend - Returned': '40f37190',
  'UE5 - Returned': '855f4872',
  'API Designer - Returned': 'd0352ed3',
  'Content Writer - Returned': 'f4a7797e'
};
```

## 🐛 Severity levels для багов

```markdown
**Critical:**
- System crash
- Data loss
- Security vulnerability
- Cannot use main functionality

**Major:**
- Feature не работает
- Incorrect results
- Performance issues
- UI broken

**Minor:**
- UI glitches
- Typos
- Non-critical errors
- UX issues
```

## OK После возврата

1. **НЕ продолжай тестирование** этой задачи
2. Developer должен исправить баги
3. Задача вернется к QA когда будет исправлена
4. Переключись на другую задачу из `QA - Todo`

## 🔄 Лимит возвратов

**ВАЖНО:** Максимум **2 возврата** между QA и разработчиком.

Если задача возвращается 3-й раз:
1. Update Status to `QA - Blocked`
2. Создать встречу для обсуждения проблем
3. Привлечь tech lead для помощи
4. Возможно нужен рефакторинг или изменение архитектуры

