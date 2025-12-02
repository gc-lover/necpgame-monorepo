# Backend: Return Task

Возврат задачи предыдущему агенту если что-то не готово.

## 🚫 Причины возврата

### 1. OpenAPI спецификация отсутствует, невалидна или слишком большая

**Проверка:**
```bash
# Проверить существование файла
ls proto/openapi/{service-name}.yaml

# Валидировать спецификацию
npx -y @redocly/cli lint proto/openapi/{service-name}.yaml

# НОВАЯ ПРОВЕРКА: Проверить размер спецификации
wc -l proto/openapi/{service-name}.yaml

# Если >500 строк - проверить модульную структуру
ls proto/openapi/{service-name}/schemas/
ls proto/openapi/{service-name}/paths/
```

**Если проблема:**
- Update Status to `API Designer - Returned`
- Указать что именно не так:
  - Файл отсутствует
  - Спецификация невалидна (ошибки линтера)
  - **НОВОЕ:** Спецификация >500 строк и не разбита на модули

### 2. Архитектура не готова

**Проверка:**
- Нет архитектурного документа
- Непонятно как реализовывать
- Противоречия в требованиях

**Если проблема:**
- Update Status to `Architect - Returned`
- Указать какой документ нужен

### 3. Это не задача для Backend

**Проверка labels:**
- `ui`, `ux`, `client` → это UI/UX задача
- `canon`, `lore`, `quest` БЕЗ архитектуры → Content Writer

**Если проблема:**
- Update Status to правильному агенту
- Объяснить почему

### 4. Database миграции не готовы

**Проверка:**
- Нужны новые таблицы, но миграций нет
- Миграции не применены

**Если проблема:**
- Update Status to `Database - Returned`
- Указать какие миграции нужны

## WARNING Как вернуть задачу

### Шаблон возврата к API Designer (спецификация отсутствует/невалидна)

```javascript
// 1. Обновить статус
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

// 2. Добавить комментарий с причиной
mcp_github_add_issue_comment({
  owner: 'gc-lover',
  repo: 'necpgame-monorepo',
  issue_number: issue_number,
  body: 'WARNING **Task returned: OpenAPI spec issues**\n\n' +
        '**Problems found:**\n' +
        '- OpenAPI spec file not found: `proto/openapi/companion-service.yaml`\n' +
        '- OR: Spec validation failed (see errors below)\n\n' +
        '**Expected:**\n' +
        '- Valid OpenAPI 3.0 spec\n' +
        '- All endpoints described\n' +
        '- Request/Response schemas defined\n\n' +
        '**Correct agent:** API Designer\n\n' +
        '**Status updated:** `API Designer - Returned`\n\n' +
        'Issue: #' + issue_number
});
```

### Шаблон возврата к API Designer (спецификация >500 строк)

```javascript
// 1. Обновить статус
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

// 2. Добавить комментарий с причиной
mcp_github_add_issue_comment({
  owner: 'gc-lover',
  repo: 'necpgame-monorepo',
  issue_number: issue_number,
  body: 'WARNING **Task returned: OpenAPI spec too large (violates 500-line limit)**\n\n' +
        '**Problems found:**\n' +
        '- OpenAPI spec: XXX lines (exceeds 500-line limit)\n' +
        '- Not split into modules\n' +
        '- Generated code would violate SOLID principles\n\n' +
        '**Impact on code generation:**\n' +
        '- types.gen.go: ~XXX lines (would exceed limit)\n' +
        '- server.gen.go: ~YYY lines (would exceed limit)\n\n' +
        '**Expected:**\n' +
        '- Split spec into modules: `{service-name}/schemas/`, `{service-name}/paths/`\n' +
        '- Each module <500 lines\n' +
        '- Use `$ref` to link modules\n' +
        '- Main file uses `$ref` to import modules\n\n' +
        '**Example structure:**\n' +
        '```\n' +
        'proto/openapi/\n' +
        '├── {service-name}/\n' +
        '│   ├── schemas/\n' +
        '│   │   ├── domain1.yaml  # <500 lines\n' +
        '│   │   └── domain2.yaml  # <500 lines\n' +
        '│   └── paths/\n' +
        '│       ├── domain1.yaml  # <500 lines\n' +
        '│       └── domain2.yaml  # <500 lines\n' +
        '└── {service-name}.yaml    # Main file with $ref\n' +
        '```\n\n' +
        '**See documentation:**\n' +
        '- `.cursor/rules/agent-api-designer.mdc` (Splitting Large Specs)\n' +
        '- `.cursor/SOLID_CODE_GENERATION_GUIDE.md`\n\n' +
        '**Correct agent:** API Designer\n\n' +
        '**Status updated:** `API Designer - Returned`\n\n' +
        'Issue: #' + issue_number
});
```

### Шаблон возврата к Architect

```javascript
// 1. Обновить статус
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,
  updated_field: {
    id: 239690516,
    value: '96c824c5'  // STATUS_OPTIONS['Architect - Returned']
  }
});

// 2. Добавить комментарий
mcp_github_add_issue_comment({
  owner: 'gc-lover',
  repo: 'necpgame-monorepo',
  issue_number: issue_number,
  body: 'WARNING **Task returned: Architecture incomplete**\n\n' +
        '**Missing:**\n' +
        '- Architecture document not found\n' +
        '- Unclear how to implement the feature\n' +
        '- Need clarification on components interaction\n\n' +
        '**Expected:**\n' +
        '- Architecture diagram (Mermaid)\n' +
        '- Components description\n' +
        '- API endpoints (high-level)\n\n' +
        '**Correct agent:** Architect\n\n' +
        '**Status updated:** `Architect - Returned`\n\n' +
        'Issue: #' + issue_number
});
```

### Шаблон возврата к Database

```javascript
// 1. Обновить статус
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,
  updated_field: {
    id: 239690516,
    value: '4272fcd7'  // STATUS_OPTIONS['Database - Returned']
  }
});

// 2. Добавить комментарий
mcp_github_add_issue_comment({
  owner: 'gc-lover',
  repo: 'necpgame-monorepo',
  issue_number: issue_number,
  body: 'WARNING **Task returned: Database migrations missing**\n\n' +
        '**Missing:**\n' +
        '- Liquibase migration for new tables\n' +
        '- Need: users_achievements table\n' +
        '- Need: achievement_progress table\n\n' +
        '**Expected:**\n' +
        '- Liquibase migration files in `infrastructure/liquibase/migrations/`\n' +
        '- Migrations applied to dev DB\n\n' +
        '**Correct agent:** Database Engineer\n\n' +
        '**Status updated:** `Database - Returned`\n\n' +
        'Issue: #' + issue_number
});
```

## 📊 ID статусов для возврата

```javascript
const RETURN_STATUS_IDS = {
  'Idea Writer - Returned': 'ec26fd29',
  'Architect - Returned': '96c824c5',
  'API Designer - Returned': 'd0352ed3',
  'Database - Returned': '4272fcd7',
  'Backend - Returned': '40f37190',  // если другой Backend agent
  'Network - Returned': '1daf88e8',
  'Security - Returned': 'cb38d85c',
  'DevOps - Returned': '96b3e4b0',
  'UE5 - Returned': '855f4872',
  'UI/UX - Returned': '278add0a',
  'Content Writer - Returned': 'f4a7797e',
  'QA - Returned': '6ccc53b0'
};
```

## OK После возврата

1. **НЕ продолжай работу** над задачей
2. Возвращенный агент должен исправить проблемы
3. Задача вернется к тебе снова когда будет готова
4. Переключись на другую задачу из `Backend - Todo`

## 🔄 Лимит возвратов

**ВАЖНО:** Максимум **2 возврата** между одними агентами.

Если задача возвращается 3-й раз:
1. Update Status to `Backend - Blocked`
2. Создать отдельный Issue для обсуждения проблемы
3. Тэгнуть Architect для разрешения конфликта

