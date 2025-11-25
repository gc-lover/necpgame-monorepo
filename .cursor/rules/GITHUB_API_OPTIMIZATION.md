# Оптимизация запросов к GitHub API

## Система очередей

Для массовых операций используй GitHub Actions Batch Processor:
- `.github/workflows/github-api-batch-processor.yml`
- `.github/GITHUB_API_QUEUE_SYSTEM.md`

Когда: массовая передача Issues (>10), обновление меток, комментарии к батчу.

## ОСНОВНЫЕ МЕТОДЫ

Главное: поиск и батчинг, не кэширование.

### 1. Поиск (ОБЯЗАТЕЛЬНО)

ВСЕГДА `mcp_github_search_issues` вместо множественных `issue_read`:

```javascript
// НЕПРАВИЛЬНО: 100 запросов
for (let i = 1; i <= 100; i++) {
  await mcp_github_issue_read({ issue_number: i });
}

// ПРАВИЛЬНО: 1 запрос
const result = await mcp_github_search_issues({
  query: 'is:issue is:open label:agent:content-writer',
  perPage: 100
});
```

### 2. Батчинг (ОБЯЗАТЕЛЬНО)

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

### 3. Кэширование (дополнительно)

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

### 1. Последовательные запросы с задержками

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
- Поиск: 500-1000ms

### 2. Группирование (батчинг)

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

### 3. Кэширование (ОБЯЗАТЕЛЬНО)

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

### 4. Поиск вместо множественных запросов

```javascript
// ПРАВИЛЬНО
const searchCache = new Map();
const SEARCH_TTL = 2 * 60 * 1000;

async function searchIssuesCached(query) {
  const cacheKey = `search:${query}`;
  if (searchCache.has(cacheKey)) {
    const cached = searchCache.get(cacheKey);
    if (Date.now() - cached.timestamp < SEARCH_TTL) {
      return cached.data;
    }
  }
  const searchResult = await mcp_github_search_issues({
    query: query,
    perPage: 100
  });
  searchCache.set(cacheKey, { data: searchResult, timestamp: Date.now() });
  searchResult.items.forEach(issue => {
    issueCache.set(issue.number, { data: issue, timestamp: Date.now() });
  });
  await delay(500);
  return searchResult;
}

const result = await searchIssuesCached('is:issue is:open label:agent:content-writer');
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
  const searchResult = await mcp_github_search_issues({
    query: 'is:issue is:open label:agent:idea-writer label:stage:idea',
    perPage: 100
  });
  searchResult.items.forEach(issue => {
    issueCache.set(issue.number, { data: issue, timestamp: Date.now() });
  });
  return searchResult.items;
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
| Поиск Issues | 500-1000ms | - |
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

- ОБЯЗАТЕЛЬНО: `mcp_github_search_issues` вместо множественных `issue_read`
- Определи: Batch Processor или прямые запросы
  - <3 Issues: прямые с задержками
  - 3-9 Issues: батчинг
  - >=10 Issues: GitHub Actions
- Группируй в батчи по 5-10
- Задержки между запросами (300-500ms)
- Задержки между батчами (1000ms)
- Последовательно, не параллельно
- Обрабатывай rate limit с повторами
- Кэширование в памяти (опционально)

function delay(ms) {
  return new Promise(resolve => setTimeout(resolve, ms));
}
