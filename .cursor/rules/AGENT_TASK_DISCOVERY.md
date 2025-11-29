# Как агентам находить свои задачи

## Механизм поиска задач

Агенты в Cursor не имеют автоматического доступа к списку задач. Они работают по запросу пользователя или могут искать задачи через MCP GitHub.

## Способ 1: Поиск через MCP GitHub (рекомендуется)

### WARNING ОБЯЗАТЕЛЬНО: Использование поиска

**КРИТИЧЕСКИ ВАЖНО:** ВСЕГДА используй `mcp_github_search_issues` вместо множественных `mcp_github_issue_read`. Это главный метод избежания rate limit.

**Перед поиском задач ОБЯЗАТЕЛЬНО:**
1. **Используй поиск** - `mcp_github_search_issues` вместо множественных `mcp_github_issue_read`
2. (Опционально) Инициализируй кэш в памяти для повторных запросов в рамках одной сессии (см. `.cursor/rules/GITHUB_MCP_CACHE_HELPER.md`)

### Для Idea Writer агента

```javascript
// OK ПРАВИЛЬНО: Используй поиск с кэшированием
const issueCache = new Map();
const searchCache = new Map();
const SEARCH_TTL = 2 * 60 * 1000; // 2 минуты

async function findIdeaWriterTasks() {
  const query = 'is:issue is:open label:agent:idea-writer';
  const cacheKey = `search:${query}`;
  
  // Проверяем кэш поиска
  if (searchCache.has(cacheKey)) {
    const cached = searchCache.get(cacheKey);
    if (Date.now() - cached.timestamp < SEARCH_TTL) {
      return cached.data.items;
    }
  }
  
  // Используем поиск вместо множественных запросов
  const result = await mcp_github_search_issues({
    query: query,
    perPage: 100
  });
  
  // Кэшируем результаты
  searchCache.set(cacheKey, { data: result, timestamp: Date.now() });
  result.items.forEach(issue => {
    issueCache.set(issue.number, { data: issue, timestamp: Date.now() });
  });
  
  return result.items;
}
```

**Команда для агента:**
```
@agent-idea-writer Найди все открытые задачи для тебя через MCP GitHub и покажи список
```

**Что делать агенту:**
1. **ОБЯЗАТЕЛЬНО:** Инициализируй кэш в памяти сессии
2. Используй `mcp_github_search_issues` с запросом:
   - `query: 'is:issue is:open label:agent:idea-writer'`
   - `perPage: 100`
3. Кэшируй результаты поиска
4. Проверь Project статус через `mcp_github_list_project_items` (с кэшированием):
   - Фильтр: `Development Stage = idea-writer state:open`
   - Кэш TTL: 2-3 минуты
5. Покажи пользователю список найденных задач
6. Спроси, с какой задачей работать

### Для Backend Developer агента

```javascript
// Фильтр: метка agent:backend И статус open И Development Stage = backend-dev
```

**Команда для агента:**
```
@backend Найди все задачи для бекенд разработки и покажи список
```

**Что делать агенту:**
1. Используй `mcp_github_list_issues` с фильтром:
   - `labels: ['agent:backend']`
   - `state: 'OPEN'`
2. Проверь, что есть OpenAPI спецификация (от API Designer)
3. Покажи список задач с приоритетами

### Для всех агентов

**Общий алгоритм поиска:**

1. **Определи свои метки:**
   - Idea Writer: `agent:idea-writer`
   - Architect: `agent:architect`
   - API Designer: `agent:api-designer`
   - Backend: `agent:backend`
   - Network: `agent:network`
   - DevOps: `agent:devops`
   - Performance: `agent:performance`
   - UE5: `agent:ue5`
   - QA: `agent:qa`
   - Release: `agent:release`
   - Stats: `agent:stats`

2. **ОБЯЗАТЕЛЬНО: Используй поиск с кэшированием:**
   ```javascript
   // OK ПРАВИЛЬНО: Поиск с кэшированием
   const issueCache = new Map();
   const searchCache = new Map();
   const SEARCH_TTL = 2 * 60 * 1000;
   
   async function searchMyTasks(agentLabel) {
     const query = `is:issue is:open label:${agentLabel}`;
     const cacheKey = `search:${query}`;
     
     // Проверяем кэш
     if (searchCache.has(cacheKey)) {
       const cached = searchCache.get(cacheKey);
       if (Date.now() - cached.timestamp < SEARCH_TTL) {
         return cached.data.items;
       }
     }
     
     // Используем поиск (НЕ list_issues для множественных запросов)
     const result = await mcp_github_search_issues({
       query: query,
       perPage: 100
     });
     
     // Кэшируем
     searchCache.set(cacheKey, { data: result, timestamp: Date.now() });
     result.items.forEach(issue => {
       issueCache.set(issue.number, { data: issue, timestamp: Date.now() });
     });
     
     return result.items;
   }
   ```
   
   **❌ НЕПРАВИЛЬНО:**
   ```javascript
   // Множественные запросы вместо поиска
   const issues = await mcp_github_list_issues({
     owner: 'gc-lover',
     repo: 'necpgame-monorepo',
     labels: ['agent:idea-writer'],
     state: 'OPEN'
   });
   ```

3. **Проверь Project статус с кэшированием:**
   ```javascript
   // OK ПРАВИЛЬНО: Project items с кэшированием
   const projectCache = new Map();
   const PROJECT_TTL = 3 * 60 * 1000;
   
   async function getCachedProjectItems(projectNumber, query, fields) {
     const cacheKey = `project:${projectNumber}:${query}`;
     
     if (projectCache.has(cacheKey)) {
       const cached = projectCache.get(cacheKey);
       if (Date.now() - cached.timestamp < PROJECT_TTL) {
         return cached.data;
       }
     }
     
     const items = await mcp_github_list_project_items({
       owner_type: 'user',
       owner: 'gc-lover',
       project_number: projectNumber,
       query: query,
       fields: fields
     });
     
     projectCache.set(cacheKey, { data: items, timestamp: Date.now() });
     return items;
   }
   ```

4. **Отфильтруй готовые задачи:**
   - Исключи задачи со статусом `closed`
   - Исключи задачи на других этапах
   - Приоритизируй задачи без метки `in-progress`

## Способ 2: Работа по запросу пользователя

Пользователь может явно указать задачу:

```
@agent-idea-writer Работай с Issue #9
```

В этом случае:
1. **ОБЯЗАТЕЛЬНО:** Проверь кэш перед запросом
2. Прочитай Issue #9 через MCP GitHub (используй кэшированную функцию)
3. Проверь, что задача для тебя (метка `agent:idea-writer`)
4. Проверь статус задачи (не обработана ли уже)
5. Начни работу

**Пример с кэшированием:**
```javascript
async function getCachedIssue(issueNumber) {
  if (issueCache.has(issueNumber)) {
    const cached = issueCache.get(issueNumber);
    if (Date.now() - cached.timestamp < 5 * 60 * 1000) {
      return cached.data; // Используем кэш
    }
  }
  
  const issue = await mcp_github_issue_read({
    method: 'get',
    issue_number: issueNumber
  });
  
  issueCache.set(issueNumber, { data: issue, timestamp: Date.now() });
  return issue;
}
```

## Способ 3: Автоматическое уведомление (через GitHub Actions)

GitHub Actions автоматически:
- Добавляет метки при создании Issue
- Обновляет Project статус
- Комментирует в Issue, когда задача готова для агента

**Агент должен:**
1. Проверить комментарии в Issue
2. Если есть комментарий "Ready for {agent-name}" → задача готова
3. Начать работу

## Структура задачи для агента

Когда агент находит задачу, он должен проверить:

1. **Метки:**
   - Есть ли метка `agent:{agent-name}`?
   - Есть ли метка `stage:{stage-name}`?

2. **Project статус:**
   - Правильный ли `Development Stage`?
   - Открыта ли задача?

3. **Содержимое Issue:**
   - Есть ли описание задачи?
   - Есть ли критерии приемки?
   - Есть ли связанные документы?

4. **Готовность:**
   - Завершена ли предыдущая стадия?
   - Есть ли все необходимые входные данные?

## Примеры команд для поиска задач

### Idea Writer
```
@agent-idea-writer Покажи все открытые задачи для Idea Writer из GitHub Project
```

### Backend Developer
```
@backend Найди все задачи для бекенд разработки, где есть OpenAPI спецификация
```

### Architect
```
@architect Покажи все задачи на этапе архитектуры, которые ждут обработки
```

## Автоматизация через GitHub Actions

Workflow `project-status-automation.yml` автоматически:
- Добавляет Issues в Project
- Устанавливает `Development Stage` на основе меток
- Комментирует в Issue, когда задача готова

**Агент должен проверять комментарии:**
- Если есть комментарий от `github-actions[bot]` с текстом "Ready for {agent}" → задача готова

## Рекомендации

1. **ОБЯЗАТЕЛЬНО: Использование поиска (главное!):**
   - **ВСЕГДА** используй `mcp_github_search_issues` вместо множественных `mcp_github_issue_read`
   - Это главный метод избежания rate limit
   - Один запрос поиска заменяет 100+ запросов `issue_read`

2. **ОБЯЗАТЕЛЬНО: Батчинг для массовых операций:**
   - Для >=3 Issues используй батчинг с задержками
   - Для >=10 Issues используй GitHub Actions Batch Processor

3. **Опционально: Кэширование (дополнительная оптимизация):**
   - Кэш работает только в рамках одного вызова агента
   - Полезен для повторных запросов к одному Issue в рамках одной сессии
   - См. `.cursor/rules/GITHUB_MCP_CACHE_HELPER.md` для шаблонов кода

2. **Всегда проверяй статус перед началом работы:**
   - Не обрабатывай уже обработанные задачи
   - Проверяй, что задача на правильном этапе

3. **Используй MCP GitHub для поиска:**
   - Не полагайся только на упоминание пользователя
   - Активно ищи свои задачи
   - **ВСЕГДА используй поиск, а не множественные запросы**

4. **Сообщай пользователю о найденных задачах:**
   - Покажи список задач
   - Предложи приоритеты
   - Спроси, с какой начать

5. **Помечай обработанные задачи:**
   - Обновляй статус в Project
   - Комментируй в Issue
   - Переходи к следующему этапу
   - Инвалидируй кэш после обновления

## Дополнительные ресурсы

- `.cursor/rules/GITHUB_MCP_CACHE_HELPER.md` - шаблоны кода для кэширования
- `.cursor/rules/GITHUB_MCP_BEST_PRACTICES.md` - примеры правильного использования
- `.cursor/rules/GITHUB_API_OPTIMIZATION.md` - полные правила оптимизации


