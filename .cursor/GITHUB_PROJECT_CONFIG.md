# GitHub Project Configuration

**Единый источник параметров проекта для всех агентов**

## [FORBIDDEN] EMOJI AND SPECIAL CHARACTERS ЗАПРЕТ

**КРИТИЧНО:** Запрещено использовать эмодзи и специальные Unicode символы в коде!

### Почему запрещено:

— [FORBIDDEN] Ломают выполнение скриптов на Windows
— [FORBIDDEN] Могут вызвать ошибки в терминале

- [FORBIDDEN] Создают проблемы с кодировкой
- [FORBIDDEN] Нарушают совместимость между ОС

### Что use вместо:

- [OK] `:smile:` вместо [EMOJI]
- [OK] `[FORBIDDEN]` вместо [FORBIDDEN]
- [OK] `[OK]` вместо [OK]
- [OK] `[ERROR]` вместо [ERROR]
- [OK] `[WARNING]` вместо [WARNING]

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

В командах агентов use эти значения:

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
- `python scripts/batch-optimize-openapi-struct-alignment.py` - оптимизация структур OpenAPI
- `python scripts/reorder-liquibase-columns.py` - оптимизация колонок БД

**Важно:** Если параметры проекта изменятся, обновить их здесь и во всех командах агентов.

## Field IDs

- **Status Field ID:** `239690516` (single_select)
- **Status Field Node ID:** `PVTSSF_lAHODCWAw84BIyiezg5JYxQ`
- **Agent Field ID:** `243899542` (single_select)
- **Agent Field Node ID:** `PVTSSF_lAHODCWAw84BIyiezg6JnJY`
- **Type Field ID:** `246469155` (single_select)
- **Check Field ID:** `246468990` (single_select)

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

## Type Option IDs

**Полный список опций поля Type (определяет тип технической задачи):**

```javascript
const TYPE_FIELD_ID = 246469155;
const TYPE_OPTIONS = {
  API: '66f88b2c',         // Создание OpenAPI спецификаций
  MIGRATION: 'd3702826',   // Создание БД миграций
  DATA: 'b06014a2',         // Импорт данных в БД
  BACKEND: '08174330',      // Написание backend кода
  UE5: 'd4d523a0',          // Разработка в Unreal Engine 5
};
```

**Правила простановки TYPE:**

- **API**: Задачи на создание/изменение OpenAPI спецификаций в enterprise-grade доменах
- **MIGRATION**: Задачи на создание Liquibase миграций схемы БД
- **DATA**: Задачи на импорт контента/NPC/квестов в БД
- **BACKEND**: Задачи на написание Go сервисов, генерацию кода из OpenAPI
- **UE5**: Задачи на разработку клиентской части в Unreal Engine

## Check Option IDs

**Поле Check (отслеживание проверки выполнения задачи):**

```javascript
const CHECK_FIELD_ID = 246468990;
const CHECK_OPTIONS = {
  NOT_CHECKED: '22932cc7',  // Задача не проверялась на выполнение (0)
  CHECKED: '4e8cf8f5',      // Задача проверена на выполнение (1)
};
```

**Правила простановки CHECK:**

- **0 (NOT_CHECKED)**: Задача взята в работу впервые, статус не проверялся
- **1 (CHECKED)**: Агент проверил актуальность задачи и её выполнение в проекте

**Алгоритм проверки:**

1. Взять задачу → CHECK = 0
2. Проанализировать задачу и проверить в проекте
3. Если задача сделана → передать следующему агенту
4. Если задача не сделана → CHECK = 1, выполнить работу
5. После выполнения → передать следующему агенту

## Project Details

- **Project Name:** NECPGAME Development
- **Project Node ID:** `PVT_kwHODCWAw84BIyie`
- **Project Number:** 1
- **Owner:** gc-lover

