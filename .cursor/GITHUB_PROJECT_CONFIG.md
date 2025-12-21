# GitHub Project Configuration

**Единый источник параметров проекта для всех агентов**

## 🚫 ЗАПРЕТ ЭМОДЗИ И СПЕЦСИМВОЛОВ

**КРИТИЧНО:** Запрещено использовать эмодзи и специальные Unicode символы в коде!

### Почему запрещено:
- 🚫 Ломают выполнение скриптов на Windows
- 🚫 Могут вызвать ошибки в терминале
- 🚫 Создают проблемы с кодировкой
- 🚫 Нарушают совместимость между ОС

### Что использовать вместо:
- OK `:smile:` вместо 😀
- OK `[FORBIDDEN]` вместо 🚫
- OK `[OK]` вместо OK
- OK `[ERROR]` вместо ❌
- OK `[WARNING]` вместо WARNING

### Автоматическая проверка:
- Pre-commit hooks блокируют коммиты с эмодзи
- Git hooks проверяют staged файлы
- Исключения: `.cursor/rules/*` (документация), `.githooks/*`

## Project Parameters

Все агенты используют эти параметры для работы с GitHub Project через MCP:

- **Owner Type:** `user`
- **Owner:** `gc-lover`
- **Project Number:** `1`
- **Project Node ID:** `PVT_kwHODCWAw84BIyie`
- **Status Field ID:** `239690516`
- **Agent Field ID:** `243899542`
- **Repository:** `gc-lover/necpgame-monorepo`

## Usage in Commands

В командах агентов использовать эти значения:

```javascript
mcp_github_list_project_items({
  owner_type: 'user',        // из этого конфига
  owner: 'gc-lover',         // из этого конфига
  project_number: 1,         // из этого конфига
  query: 'Agent:"{Agent}" Status:"Todo"' // или добавь In Progress по необходимости
});
```

**Note:** Не используй `is:issue` в query - `list_project_items` работает только с issues. Не указывай `fields` -
вернутся все поля.

**Оптимизированные скрипты для агентов:**
- `python scripts/validate-domains-openapi.py` - валидация OpenAPI доменов
- `python scripts/generate-all-domains-go.py` - генерация enterprise-grade сервисов
- `python scripts/reorder-openapi-fields.py` - оптимизация структур OpenAPI
- `python scripts/reorder-liquibase-columns.py` - оптимизация колонок БД

**Важно:** Если параметры проекта изменятся, обновить их здесь и во всех командах агентов.

## Field IDs

- **Status Field ID:** `239690516` (single_select)
- **Status Field Node ID:** `PVTSSF_lAHODCWAw84BIyiezg5JYxQ`
- **Agent Field ID:** `243899542` (single_select)
- **Agent Field Node ID:** `PVTSSF_lAHODCWAw84BIyiezg6JnJY`

## Status Option IDs

**Полный список опций поля Status (единые для всех агентов):**

```javascript
const STATUS_FIELD_ID = 239690516;
const STATUS_OPTIONS = {
  Returned: 'c01c12e9',
  Todo: 'f75ad846',
  'In Progress': '83d488e7',
  Review: '55060662',
  Blocked: 'af634d5b',
  Done: '98236657',
};
```

**Использование:**

```javascript
updated_field: {
  id: STATUS_FIELD_ID,
  value: STATUS_OPTIONS['In Progress']
}
```

## Agent Option IDs

**Полный список опций поля Agent:**

```javascript
const AGENT_FIELD_ID = 243899542;
const AGENT_OPTIONS = {
  Idea: '8c3f5f11',
  Content: 'd3cae8d8',
  Backend: '1fc13998',
  Architect: 'd109c7f9',
  API: '6aa5d9af',
  DB: '1e745162',
  QA: '3352c488',
  Performance: 'd16ede50',
  Security: '12586c50',
  Network: 'c60ebab1',
  DevOps: '7e67a39b',
  'UI/UX': '98c65039',
  UE5: '56920475',
  GameBalance: '12e8fb71',
  Release: 'f5878f68',
};
```

**Использование (назначить агенту Backend и поставить в работу):**

```javascript
updated_field: [
  { id: STATUS_FIELD_ID, value: STATUS_OPTIONS['In Progress'] },
  { id: AGENT_FIELD_ID, value: AGENT_OPTIONS.Backend },
]
```

## Project Details

- **Project Name:** NECPGAME Development
- **Project Node ID:** `PVT_kwHODCWAw84BIyie`
- **Project Number:** 1
- **Owner:** gc-lover

