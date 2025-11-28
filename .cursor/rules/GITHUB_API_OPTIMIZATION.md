# Оптимизация запросов к GitHub API

## ⚠️ КРИТИЧНО: Secondary Rate Limit

**Search API лимит: 30 запросов/минуту.** При превышении GitHub блокирует ВСЕ типы запросов (REST, GraphQL, Search) на 1 час. 

**РЕШЕНИЕ: Используй правильный метод в зависимости от сценария** - см. `.cursor/rules/GITHUB_API_METHOD_SELECTION.md` для выбора между `list_issues` и `search_issues`.

## Система очередей

Для массовых операций используй GitHub Actions Batch Processor:
- `.github/workflows/github-api-batch-processor.yml`
- `.github/GITHUB_API_QUEUE_SYSTEM.md`

Когда: массовая передача Issues (>10), обновление меток, комментарии к батчу.

## ОСНОВНЫЕ МЕТОДЫ

Главное: выбор правильного метода и батчинг, не кэширование.

### 1. Выбор метода поиска (ОБЯЗАТЕЛЬНО)

**Два способа работы с Issues:**

#### Способ 1: `list_issues` с `labels` (РЕКОМЕНДУЕТСЯ)

**Используй когда:**
- Фильтрация по меткам (`agent:*`, `stage:*`)
- Фильтрация по статусу (`OPEN`, `CLOSED`)
- Работа с конкретным репозиторием
- Нужно получить все issues с определенными метками

**Преимущества:**
- Лимит: 5000 запросов/час (REST API) или 5000 очков/час (GraphQL)
- Нет secondary rate limit (не блокирует другие запросы)
- Фильтрация на стороне API

**Пример:**
```javascript
// Фильтрация по меткам агента и этапа
const result = await mcp_github_list_issues({
  owner: 'gc-lover',
  repo: 'necpgame-monorepo',
  labels: ['agent:idea-writer', 'stage:idea'],
  state: 'OPEN',
  perPage: 100
});
```

#### Способ 2: `search_issues` (ТОЛЬКО при необходимости)

**Используй ТОЛЬКО когда:**
- Нужен поиск по тексту в title/body
- Нужен поиск по датам (created, updated)
- Нужен поиск по assignee
- Нужен поиск по комбинации сложных условий
- Нужен поиск по нескольким репозиториям одновременно

**Ограничения:**
- Лимит: 30 запросов/мин (1800/час)
- При превышении блокирует ВСЕ типы запросов на 1 час (secondary rate limit)
- Задержка 2-3 секунды между запросами ОБЯЗАТЕЛЬНА

**Пример:**
```javascript
// Поиск по тексту в title (НЕ МОЖЕТ быть через list_issues)
const result = await mcp_github_search_issues({
  query: 'is:issue is:open repo:gc-lover/necpgame-monorepo "система транспорта" in:title',
  perPage: 100
});
await delay(2000); // ОБЯЗАТЕЛЬНО 2-3 сек задержка
```

### 2. Правила выбора метода

**ВСЕГДА используй `list_issues` если:**
- Фильтрация только по меткам → `list_issues` с `labels`
- Фильтрация только по статусу → `list_issues` с `state`
- Комбинация меток + статус → `list_issues` с `labels` и `state`
- Работа с одним репозиторием → `list_issues`

**Используй `search_issues` ТОЛЬКО если:**
- Нужен поиск по тексту (title/body) → `search_issues`
- Нужен поиск по датам → `search_issues`
- Нужен поиск по assignee → `search_issues`
- Нужен поиск по нескольким репозиториям → `search_issues`

### 3. Поиск задач агента (ОБЯЗАТЕЛЬНО)

ВСЕГДА `mcp_github_list_issues` с `labels` для поиска задач агента:

```javascript
// НЕПРАВИЛЬНО: 100 запросов
for (let i = 1; i <= 100; i++) {
  await mcp_github_issue_read({ issue_number: i });
}

// НЕПРАВИЛЬНО: использует Search API (лимит 30/мин)
const result = await mcp_github_search_issues({
  query: 'is:issue is:open label:agent:content-writer',
  perPage: 100
});

// ПРАВИЛЬНО: использует GraphQL (лимит 5000/час)
const result = await mcp_github_list_issues({
  owner: 'gc-lover',
  repo: 'necpgame-monorepo',
  labels: ['agent:content-writer'],
  state: 'OPEN',
  perPage: 100
});
```

### 4. Батчинг (ОБЯЗАТЕЛЬНО)

Для >=3 Issues:

```javascript
const batchSize = 5;
for (let i = 0; i < issues.length; i += batchSize) {
  const batch = issues.slice(i, i + batchSize);
  for (const issue of batch) {
    await updateIssue(issue);
    await delay(300);
  }
  if (i + batchSize < issues.length) {
    await delay(1000);
  }
}
```

### 5. Кэширование (дополнительно)

Кэш работает только в рамках одной сессии агента.

```javascript
const issueCache = new Map();
const ISSUE_TTL = 5 * 60 * 1000;

async function getCachedIssue(issueNumber) {
  if (issueCache.has(issueNumber)) {
    const cached = issueCache.get(issueNumber);
    if (Date.now() - cached.timestamp < ISSUE_TTL) {
      return cached.data;
    }
    issueCache.delete(issueNumber);
  }
  const issue = await mcp_github_issue_read({ issue_number: issueNumber });
  issueCache.set(issueNumber, { data: issue, timestamp: Date.now() });
  await delay(300);
  return issue;
}
```

TTL:
- Issues: 5-10 мин
- Поиск: 1-2 мин
- Project items: 2-3 мин

Шаблоны: `.cursor/rules/GITHUB_MCP_CACHE_HELPER.md`

## Правила оптимизации

### 1. Выбор метода по сценарию

**Сценарий: Поиск задач агента по меткам**
```javascript
// ✅ ПРАВИЛЬНО: list_issues (фильтрация по меткам)
const issues = await mcp_github_list_issues({
  owner: 'gc-lover',
  repo: 'necpgame-monorepo',
  labels: ['agent:idea-writer', 'stage:idea'],
  state: 'OPEN',
  perPage: 100
});
```

**Сценарий: Поиск по тексту в названии**
```javascript
// ✅ ПРАВИЛЬНО: search_issues (поиск по тексту)
const issues = await mcp_github_search_issues({
  query: 'is:issue is:open repo:gc-lover/necpgame-monorepo "транспорт" in:title',
  perPage: 100
});
await delay(2000); // ОБЯЗАТЕЛЬНО
```

**Сценарий: Поиск по дате создания**
```javascript
// ✅ ПРАВИЛЬНО: search_issues (поиск по дате)
const issues = await mcp_github_search_issues({
  query: 'is:issue is:open repo:gc-lover/necpgame-monorepo created:>2025-11-01',
  perPage: 100
});
await delay(2000); // ОБЯЗАТЕЛЬНО
```

### 2. Последовательные запросы с задержками

```javascript
// ПРАВИЛЬНО
const issues = [];
for (const num of [1, 2, 3]) {
  const issue = await mcp_github_issue_read({ issue_number: num });
  issues.push(issue);
  await delay(300);
}
```

Задержки:
- Одиночные: 200-300ms
- Массовые: 300-500ms
- list_issues: 200-300ms (GraphQL, лимит 5000/час)
- search_issues: НЕ ИСПОЛЬЗУЙ (лимит 30/мин, блокирует все запросы)

### 3. Группирование (батчинг)

```javascript
// ПРАВИЛЬНО
const issuesToUpdate = [1, 2, 3, 4, 5];
const searchResult = await mcp_github_search_issues({
  query: 'is:issue is:open label:agent:content-writer',
  perPage: 100
});

const batchSize = 5;
for (let i = 0; i < issuesToUpdate.length; i += batchSize) {
  const batch = issuesToUpdate.slice(i, i + batchSize);
  for (const issueNum of batch) {
    await mcp_github_issue_write({
      method: 'update',
      issue_number: issueNum,
      labels: ['agent:qa', 'stage:testing']
    });
    await delay(300);
  }
  if (i + batchSize < issuesToUpdate.length) {
    await delay(1000);
  }
}
```

### 4. Кэширование (ОБЯЗАТЕЛЬНО)

```javascript
const issueCache = new Map();
const ISSUE_TTL = 5 * 60 * 1000;

async function getCachedIssue(issueNumber) {
  if (issueCache.has(issueNumber)) {
    const cached = issueCache.get(issueNumber);
    const age = Date.now() - cached.timestamp;
    if (age < ISSUE_TTL) return cached.data;
    issueCache.delete(issueNumber);
  }
  const issue = await mcp_github_issue_read({ issue_number: issueNumber });
  issueCache.set(issueNumber, { data: issue, timestamp: Date.now() });
  await delay(300);
  return issue;
}
```

### 5. Поиск через list_issues (для меток)

```javascript
// ПРАВИЛЬНО: list_issues с кэшированием
const issuesCache = new Map();
const ISSUES_TTL = 2 * 60 * 1000;

async function getIssuesCached(agentLabel, stageLabel) {
  const cacheKey = `issues:${agentLabel}:${stageLabel}`;
  if (issuesCache.has(cacheKey)) {
    const cached = issuesCache.get(cacheKey);
    if (Date.now() - cached.timestamp < ISSUES_TTL) {
      return cached.data;
    }
  }
  const result = await mcp_github_list_issues({
    owner: 'gc-lover',
    repo: 'necpgame-monorepo',
    labels: [agentLabel, stageLabel].filter(Boolean),
    state: 'OPEN',
    perPage: 100
  });
  issuesCache.set(cacheKey, { data: result, timestamp: Date.now() });
  result.issues.forEach(issue => {
    issueCache.set(issue.number, { data: issue, timestamp: Date.now() });
  });
  await delay(300);
  return result;
}

const result = await getIssuesCached('agent:content-writer', 'stage:content');
const readyIssues = result.issues.filter(issue => 
  issue.labels.some(label => label.name.startsWith('ready:'))
);
```

## Паттерны для агентов

### Content Writer: передача в QA

```javascript
async function transferToQA(issueNumbers) {
  const batchSize = 5;
  for (let i = 0; i < issueNumbers.length; i += batchSize) {
    const batch = issueNumbers.slice(i, i + batchSize);
    for (const issueNum of batch) {
      const issue = await getCachedIssue(issueNum);
      const newLabels = issue.labels
        .map(l => l.name)
        .filter(l => l !== 'agent:content-writer')
        .concat(['agent:qa', 'stage:testing']);
      await mcp_github_issue_write({
        method: 'update',
        issue_number: issueNum,
        labels: newLabels
      });
      await delay(300);
    }
    if (i + batchSize < issueNumbers.length) {
      await delay(1000);
    }
  }
}
```

### Idea Writer: поиск задач

```javascript
async function findIdeaWriterTasks() {
  const result = await mcp_github_list_issues({
    owner: 'gc-lover',
    repo: 'necpgame-monorepo',
    labels: ['agent:idea-writer', 'stage:idea'],
    state: 'OPEN',
    perPage: 100
  });
  result.issues.forEach(issue => {
    issueCache.set(issue.number, { data: issue, timestamp: Date.now() });
  });
  const readyTasks = result.issues.filter(issue => 
    issue.labels.some(label => label.name.startsWith('ready:'))
  );
  return readyTasks.length > 0 ? readyTasks : result.issues;
}
```

## Обработка rate limit

```javascript
async function safeApiCall(apiFunction, retries = 3) {
  for (let attempt = 1; attempt <= retries; attempt++) {
    try {
      return await apiFunction();
    } catch (error) {
      if (error.message?.includes('rate limit')) {
        const resetMatch = error.message.match(/until (\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2})/);
        if (resetMatch) {
          const resetTime = new Date(resetMatch[1]);
          const waitTime = resetTime.getTime() - Date.now() + 5000;
          await delay(Math.max(waitTime, 60000));
          continue;
        }
      }
      if (attempt < retries) {
        const delay = Math.min(1000 * Math.pow(2, attempt), 10000);
        await new Promise(resolve => setTimeout(resolve, delay));
        continue;
      }
      throw error;
    }
  }
}
```

## Рекомендации по частоте

| Операция | Задержка | Размер батча |
|----------|----------|--------------|
| Чтение Issue | 200-300ms | - |
| Обновление Issue | 300-500ms | 5-10 |
| list_issues (GraphQL) | 200-300ms | - |
| search_issues (НЕ ИСПОЛЬЗУЙ) | - | - |
| Комментарии | 300-500ms | 5-10 |
| Метки | 300-500ms | 5-10 |
| Массовые | 500-1000ms | 5 |

## Система очередей vs прямые запросы

Используй GitHub Actions Batch Processor если:
- >10 Issues
- Операция асинхронна
- Нужна гарантия защиты от rate limit
- Операция повторяющаяся

Используй прямые MCP запросы если:
- <10 Issues
- Требуется немедленная обратная связь
- Операция уникальная
- Нужна интерактивная обработка

## Чеклист

- ОБЯЗАТЕЛЬНО: `mcp_github_list_issues` с `labels` вместо `search_issues` (обходит Search API лимит)
- НЕ ИСПОЛЬЗУЙ `search_issues` - лимит 30/мин, блокирует все запросы
- Определи: Batch Processor или прямые запросы
  - <3 Issues: прямые с задержками
  - 3-9 Issues: батчинг
  - >=10 Issues: GitHub Actions
- Группируй в батчи по 5-10
- Задержки между запросами (300-500ms)
- Задержки для list_issues (200-300ms) - GraphQL, лимит 5000/час
- Задержки между батчами (1000ms)
- Последовательно, не параллельно
- Обрабатывай rate limit с повторами
- Кэширование результатов list_issues (TTL: 1-2 мин) - ОБЯЗАТЕЛЬНО
- Используй метки `ready:*` для определения готовности (вместо комментариев)
- Используй метки `stage:*` для фильтрации (вместо Project статуса)

function delay(ms) {
  return new Promise(resolve => setTimeout(resolve, ms));
}
