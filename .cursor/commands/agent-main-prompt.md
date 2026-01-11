# Agent Main Prompt Command

## Обзор
Главная команда для работы CURSOR AI Агентов в проекте NECPGAME. Определяет полный workflow для автономной работы агентов.

## Основной промпт агентов

```
БЕРИ КАЖДУЮ ЗАДАЧУ И ДОВОДИ ДО СОСТОЯНИЯ DONE!

Для поиска задач используй КОМБИНИРОВАННЫЙ подход: @.cursor/MCP_GITHUB_GUIDE.md
1. GH CLI для быстрого поиска открытых задач
2. MCP GitHub для получения деталей и обновления статусов

ВАЖНО: MCP GitHub Projects API может не возвращать поля Status/Agent в list_project_items.
Используй GH CLI для поиска, затем MCP для получения item_id и обновления статусов.

КРИТИЧНО: ОБЯЗАТЕЛЬНО ПРОВЕРЬ РЕАЛЬНОЕ СОСТОЯНИЕ РЕПОЗИТОРИЯ перед взятием задачи!
- Проверь статус Issue через MCP GitHub (может быть закрыта, даже если в Projects статус Todo)
- Проверь код в репозитории - возможно задача уже выполнена
- Статусы в Projects могут быть устаревшими и не совпадать с реальностью!
- ЕСЛИ задача уже реализована в коде или Issue закрыта - ОБЯЗАТЕЛЬНО синхронизировать статус на Done и закрыть Issue (если открыта)!
- ТОЛЬКО ПОСЛЕ проверки актуальности и подтверждения что задача требует работы - изменить статус с "Todo" на "In Progress" через MCP GitHub

ТЫ ДОЛЖЕН ДОВЕСТИ КАЖДУЮ ЗАДАЧУ ДО СОСТОЯНИЯ DONE!

КРИТИЧНО: После завершения работы над задачей ОБЯЗАТЕЛЬНО делать коммит! Одна задача = один коммит!
Формат коммита: [agent] {type}: {desc}\n\nRelated Issue: #{number}

Все роли агентов: @.cursor/rules/agent-api-designer.mdc @.cursor/rules/agent-architect.mdc @.cursor/rules/agent-autonomy.mdc @.cursor/rules/agent-backend.mdc @.cursor/rules/agent-content-writer.mdc @.cursor/rules/agent-database.mdc @.cursor/rules/agent-devops.mdc @.cursor/rules/agent-game-balance.mdc @.cursor/rules/agent-idea-writer.mdc @.cursor/rules/agent-network.mdc @.cursor/rules/agent-performance.mdc @.cursor/rules/agent-qa.mdc @.cursor/rules/agent-release.mdc @.cursor/rules/agent-security.mdc @.cursor/rules/agent-ue5.mdc @.cursor/rules/agent-ui-ux-designer.mdc @.cursor/rules/always.mdc @.cursor/rules/linter-emoji-ban.mdc

Базовый сценарий: @.cursor/AGENT_QUICK_START.md
Изменение статусов: @.cursor/MCP_GITHUB_GUIDE.md
Backend оптимизации: @.cursor/BACKEND_OPTIMIZATION_CHECKLIST.md
Content workflow: @.cursor/CONTENT_WORKFLOW.md
Performance enforcement: @.cursor/PERFORMANCE_ENFORCEMENT.md

Цель:
1. Брать КАЖДУЮ задачу
2. Качественно выполнить задачу согласно роли агента
3. Передать задачу следующему агенту по workflow (если требуется)
4. Довести задачу ДО DONE состояния
5. Изменить статус задачи через MCP GitHub

Глобальная цель:
1. Реализовать первоклассную игру MMOFPS с элементами RPG уровня WORLD OF WARCRAFT
2. Постепенно решить ВСЕ задачи проекта
3. Довести ВСЕ задачи до состояния DONE

СТРОГО ЗАПРЕЩЕНО:
- Создавать мусорные файлы (отчеты, рапорты, summary)
- Оставлять задачи незавершенными
- Не доводить задачи до DONE
- Не использовать MCP GitHub для изменения статусов

Можешь работать с 1-5 задачами параллельно, но КАЖДУЮ ДОВОДИ ДО DONE!
```

## Структура работы агента

### 1. Поиск и взятие задачи

#### 1.1. Поиск задач (КОМБИНИРОВАННЫЙ ПОДХОД)

**ВАЖНО: MCP GitHub Projects API не всегда корректно фильтрует задачи по Status/Agent.**
**Используй комбинированный подход: GH CLI для поиска + MCP для обновления.**

**Шаг 1: Поиск через GH CLI (быстрый просмотр)**
```bash
# Поиск открытых задач по префиксу в названии
gh issue list --repo gc-lover/necpgame-monorepo --state open --limit 30 --json number,title,state

# Поиск задач с определённым префиксом (например, [Backend])
gh issue list --repo gc-lover/necpgame-monorepo --state open | grep "\[Backend\]"

# Поиск задач по лейблу (если используются)
gh issue list --repo gc-lover/necpgame-monorepo --state open --label "agent:backend"
```

**Шаг 2: Получение деталей через MCP GitHub**
```javascript
// После нахождения задачи через GH CLI (например, #2296)
const issueNumber = 2296;

// 1. Получить Issue детали
const issue = await mcp_github_issue_read({
  owner: 'gc-lover',
  repo: 'necpgame-monorepo',
  issue_number: issueNumber,
  method: 'get'
});

// 2. Найти задачу в проекте по номеру Issue
// ВАЖНО: query `number:${issueNumber}` может не работать, особенно для закрытых Issues
// Используй альтернативный подход через GraphQL, если query не работает

// Попытка 1: Использовать query (может не работать для закрытых Issues)
let projectItems = await mcp_github_list_project_items({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  query: `number:${issueNumber}`,
  fields: []
});

let item = projectItems.items.find(item => item.content?.number === issueNumber);

// Попытка 2: Если query не сработал, использовать GraphQL через GH CLI
if (!item) {
  // Используй GH CLI для поиска item_id через GraphQL
  // gh api graphql -f query='query { user(login: "gc-lover") { projectV2(number: 1) { items(first: 100, after: $cursor) { nodes { id content { ... on Issue { number title } } } pageInfo { hasNextPage endCursor } } } } }'
  // Затем найди item с content.number === issueNumber и используй его id
  // Для больших проектов может потребоваться pagination
}

// Попытка 3: Если GraphQL не доступен, перебирай items без фильтра
if (!item) {
  projectItems = await mcp_github_list_project_items({
    owner_type: 'user',
    owner: 'gc-lover',
    project_number: 1,
    query: '',  // Пустой query = все items
    per_page: 200
  });
  
  // Перебирай items и используй get_project_item для каждого, чтобы получить content.number
  // Это неэффективно, но работает, если других способов нет
  for (const candidateItem of projectItems.items) {
    const itemDetails = await mcp_github_get_project_item({
      owner_type: 'user',
      owner: 'gc-lover',
      project_number: 1,
      item_id: candidateItem.id,
      fields: []
    });
    // Проверь itemDetails на наличие content.number (если доступно)
    // или сравнивай title с issue.title
  }
}

// 3. Если задача найдена - получить её поля (Status, Agent)
if (item) {
  const itemDetails = await mcp_github_get_project_item({
    owner_type: 'user',
    owner: 'gc-lover',
    project_number: 1,
    item_id: item.id,
    fields: []
  });
  
  // Извлечь статус и агента из полей
  const statusField = itemDetails.fields.find(f => f.id === '239690516');
  const currentStatus = statusField?.value;
}
```

**ВАЖНО: Если query `number:${issueNumber}` не работает (особенно для закрытых Issues):**

Используй GraphQL через GH CLI для надежного поиска item_id:
```bash
# Найти item_id по номеру Issue через GraphQL
gh api graphql -f query='query($cursor: String) { user(login: "gc-lover") { projectV2(number: 1) { items(first: 100, after: $cursor) { nodes { id content { ... on Issue { number title } } } pageInfo { hasNextPage endCursor } } } } }' --jq '.data.user.projectV2.items.nodes[] | select(.content.number == ISSUE_NUMBER) | .id'

# Для больших проектов может потребоваться pagination через cursor
```

Затем используй найденный item_id с MCP GitHub для получения деталей и обновления статусов.

**Детальный workflow:** См. `@.cursor/MCP_GITHUB_GUIDE.md`

#### 1.2. ОБЯЗАТЕЛЬНАЯ проверка актуальности задачи
**КРИТИЧНО: Статусы задач в Projects могут не совпадать с реальностью!**
**ПЕРЕД взятием задачи в работу агент ОБЯЗАН:**

1. **Проверить статус Issue через MCP GitHub** - получить актуальное состояние Issue
   ```javascript
   // Проверка статуса Issue
   const issue = await mcp_github_issue_read({
     owner: 'gc-lover',
     repo: 'necpgame-monorepo',
     issue_number: issueNumber,
     method: 'get'
   });
   ```

2. **Проверить содержимое задачи** - прочитать полное описание и требования

3. **Проверить реальное состояние репозитория** - проверить код/файлы в репозитории
   - Найти файлы, которые должны быть изменены
   - Убедиться что требуемый функционал еще не реализован
   - Проверить что изменения действительно нужны

4. **Оценить актуальность** - возможно задача уже выполнена, устарела или решена другим способом

5. **КРИТИЧНО: Синхронизировать статусы Issue и Projects с реальным состоянием кода!**
   ```javascript
   // Определить реальное состояние задачи
   const issueClosed = (issue.state === 'closed');
   const codeImplemented = taskAlreadyImplemented; // Проверка кода из пункта 3
   // Получить текущий статус из Projects (из результата list_project_items)
   const projectStatus = currentProjectItem.fields.find(f => f.id === '239690516')?.value;
   
   // Сценарий 1: Issue закрыта, но статус в Projects не Done
   // Обновить статус в Projects на Done
   if (issueClosed && projectStatus !== '98236657') { // 98236657 = Done
     await mcp_github_update_project_item({
       owner_type: 'user',
       owner: 'gc-lover',
       project_number: 1,
       item_id: itemId,
       updated_field: {
         id: '239690516', // Status field
         value: '98236657' // Done
       }
     });
     
     await mcp_github_add_issue_comment({
       owner: 'gc-lover',
       repo: 'necpgame-monorepo',
       issue_number: issueNumber,
       body: '[OK] Issue already closed. Status synchronized to Done. Issue: #{number}'
     });
     
     return; // Задача уже закрыта, не брать в работу
   }
   
   // Сценарий 2: Код реализован, но Issue открыта или статус в Projects неправильный
   // Обновить статус на Done и закрыть Issue
   if (codeImplemented && (!issueClosed || projectStatus !== '98236657')) { // 98236657 = Done
     // Обновить статус в Projects на Done
     await mcp_github_update_project_item({
       owner_type: 'user',
       owner: 'gc-lover',
       project_number: 1,
       item_id: itemId,
       updated_field: {
         id: '239690516', // Status field
         value: '98236657' // Done
       }
     });
     
     // Закрыть Issue, если она открыта
     if (!issueClosed) {
       await mcp_github_issue_write({
         method: 'update',
         owner: 'gc-lover',
         repo: 'necpgame-monorepo',
         issue_number: issueNumber,
         state: 'closed',
         state_reason: 'completed'
       });
     }
     
     await mcp_github_add_issue_comment({
       owner: 'gc-lover',
       repo: 'necpgame-monorepo',
       issue_number: issueNumber,
       body: '[OK] Task already implemented in code. Status updated to Done. Issue: #{number}'
     });
     
     return; // Задача уже выполнена, не брать в работу
   }
   
   // Сценарий 3: Issue закрыта и код реализован - все синхронизировано
   if (issueClosed && codeImplemented) {
     // Проверить статус в Projects
     if (projectStatus !== '98236657') { // 98236657 = Done
       await mcp_github_update_project_item({
         owner_type: 'user',
         owner: 'gc-lover',
         project_number: 1,
         item_id: itemId,
         updated_field: {
           id: '239690516', // Status field
           value: '98236657' // Done
         }
       });
     }
     return; // Задача уже выполнена, не брать в работу
   }
   ```

6. **ТОЛЬКО ЕСЛИ задача актуальна, Issue открыта и код не реализован** - брать её в работу

**Признаки неактуальной задачи (не брать в работу):**
- Issue закрыта (`state === 'closed'`) - ОБЯЗАТЕЛЬНО синхронизировать статус в Projects на Done
- Код уже реализован в репозитории (файлы существуют, функционал работает) - ОБЯЗАТЕЛЬНО обновить статус на Done и закрыть Issue
- Требования противоречат текущему состоянию проекта
- Задача дублирует существующую функциональность
- Изменения уже внесены другим способом

**Важно:** Статус в Projects (Todo/In Progress/Done) может быть устаревшим!
Всегда проверяй реальное состояние Issue и кода в репозитории!
**ОБЯЗАТЕЛЬНО синхронизировать статусы:**
- Если Issue закрыта → статус в Projects должен быть Done
- Если код реализован → статус в Projects должен быть Done, Issue должна быть закрыта
- Если и Issue закрыта, и код реализован → статус в Projects должен быть Done

#### 1.3. Взятие задачи в работу
```javascript
// Взятие задачи (In Progress) - ТОЛЬКО после проверки актуальности
await mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: itemId,
  updated_field: {
    id: '239690516', // Status field
    value: '83d488e7' // In Progress
  }
});
```

### 2. Определение роли агента
На основе содержимого задачи выбрать подходящую роль:

| Тип задачи | Агент | Правило |
|------------|-------|---------|
| API спецификации (OpenAPI 3.0) | API Designer | `agent-api-designer.mdc` |
| Архитектура, дизайн системы | Architect | `agent-architect.mdc` |
| Backend код, Go сервисы | Backend | `agent-backend.mdc` |
| Контент, квесты, лор, NPC, диалоги | Content Writer | `agent-content-writer.mdc` |
| Идеи, концепции, лор | Idea Writer | `agent-idea-writer.mdc` |
| Схемы БД, миграции, индексы | Database | `agent-database.mdc` |
| Docker, Kubernetes, CI/CD | DevOps | `agent-devops.mdc` |
| Балансировка, формулы | Game Balance | `agent-game-balance.mdc` |
| Envoy, gRPC, Protocol Buffers | Network | `agent-network.mdc` |
| Профилирование, оптимизация | Performance | `agent-performance.mdc` |
| Тестирование, баги | QA | `agent-qa.mdc` |
| Release notes, деплой | Release | `agent-release.mdc` |
| Безопасность, anti-cheat | Security | `agent-security.mdc` |
| UE5 клиент, C++ | UE5 | `agent-ue5.mdc` |
| UI/UX дизайн, wireframes | UI/UX Designer | `agent-ui-ux-designer.mdc` |

### 3. Выполнение задачи
- Следовать правилам выбранного агента
- Применять валидацию из `common-validation.md`
- Соблюдать ограничения из `always.mdc`
- Избегать создания мусорных файлов

### 4. Передача задачи или завершение

#### 4.1. Передача следующему агенту
Если задача требует работы следующего агента:

**КРИТИЧНО: ОБЯЗАТЕЛЬНО сделать коммит перед передачей задачи!**

```bash
# 1. Сделать коммит с изменениями (ОБЯЗАТЕЛЬНО!)
git add .
git commit -m "[agent] {type}: {desc}

Related Issue: #{number}"

# Примеры:
# git commit -m "[API] feat: Add OpenAPI specification for auth service\n\nRelated Issue: #1234"
# git commit -m "[Backend] refactor: Optimize database queries\n\nRelated Issue: #5678"
```

```javascript
// 2. Передача следующему агенту (после коммита!)
await mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: itemId,
  updated_field: [
    {
      id: '239690516', // Status field
      value: 'f75ad846' // Todo
    },
    {
      id: '243899542', // Agent field
      value: '{next_agent_id}' // ID следующего агента
    }
  ]
});

// 3. Комментарий о передаче
await mcp_github_add_issue_comment({
  owner: 'gc-lover',
  repo: 'necpgame-monorepo',
  issue_number: issueNumber,
  body: '[OK] Work completed. Handed off to {NextAgent}. Issue: #{number}'
});
```

**Правило:** Одна задача = один коммит. Все изменения задачи должны быть в одном коммите перед передачей следующему агенту.

#### 4.2. Завершение задачи (Done)
Если задача полностью выполнена и не требует работы других агентов:

**КРИТИЧНО: ОБЯЗАТЕЛЬНО сделать коммит перед обновлением статуса на Done!**

```bash
# 1. Сделать коммит с изменениями (ОБЯЗАТЕЛЬНО!)
git add .
git commit -m "[agent] {type}: {desc}

Related Issue: #{number}"

# Примеры:
# git commit -m "[Backend] feat: Add user authentication service\n\nRelated Issue: #1234"
# git commit -m "[API] docs: Update OpenAPI specification\n\nRelated Issue: #5678"
# git commit -m "[Database] fix: Correct migration timestamp\n\nRelated Issue: #9012"
```

```javascript
// 2. Завершение задачи (после коммита!)
await mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: itemId,
  updated_field: {
    id: '239690516', // Status field
    value: '98236657' // Done
  }
});

// 3. Закрыть Issue
await mcp_github_issue_write({
  method: 'update',
  owner: 'gc-lover',
  repo: 'necpgame-monorepo',
  issue_number: issueNumber,
  state: 'closed',
  state_reason: 'completed'
});

// 4. Комментарий о завершении
await mcp_github_add_issue_comment({
  owner: 'gc-lover',
  repo: 'necpgame-monorepo',
  issue_number: issueNumber,
  body: '[OK] Task completed. Status: Done. Issue: #{number}'
});
```

**Правило:** Одна задача = один коммит. Все изменения задачи должны быть в одном коммите перед обновлением статуса на Done.

## Правила качества

### ✅ ДОЛЖНО быть
- MCP GitHub для всех операций с задачами
- **ОБЯЗАТЕЛЬНАЯ проверка актуальности задачи перед взятием**
- **Если задача уже реализована в коде - обновить статус на Done и закрыть Issue**
- Смена статуса TODO → In Progress при взятии
- Выбор правильной роли агента
- Качественное выполнение требований
- **ОБЯЗАТЕЛЬНЫЙ коммит после завершения работы над задачей (одна задача = один коммит)**
- Передача следующему агенту с комментарием

### ❌ ЗАПРЕЩЕНО
- Создание отчетов, рапортов, summary файлов
- Использование GitHub CLI для изменения статусов
- Работа без смены статуса на In Progress
- Оставление задач без передачи следующему агенту
- Нарушение правил размещения файлов
- Обновление статуса задачи на Done/Todo без коммита изменений
- Объединение изменений нескольких задач в один коммит

## Лимиты и ограничения

- **Максимум задач за раз:** 5
- **Файлы:** Только необходимые для реализации
- **Коммиты:** Формат `[agent] {type}: {desc}\n\nRelated Issue: #{number}`
- **Правило коммитов:** Одна задача = один коммит. Коммит ОБЯЗАТЕЛЕН перед обновлением статуса на Done/Todo
- **Валидация:** Обязательна перед передачей

## Команды валидации

### Перед передачей задачи
```bash
# Валидация результата агента
/{agent}-validate-result #{number}

# Общая валидация
python scripts/validation/validate-emoji-ban.py .
python scripts/openapi/validate-domains-openapi.py
```

### Проверка архитектуры
```bash
# Для архитектурных задач
/architect-validate-architecture #{number}

# Для backend задач
/backend-validate-optimizations #{number}
```

## Ссылки на документацию

- `@.cursor/AGENT_QUICK_START.md` - быстрый старт агентов
- `@.cursor/MCP_GITHUB_GUIDE.md` - работа с GitHub через MCP (поиск, статусы, workflow)
- `@.cursor/GITHUB_PROJECT_FIELD_IDS.md` - Field IDs для Projects
- `@.cursor/commands/common-validation.md` - валидация кода