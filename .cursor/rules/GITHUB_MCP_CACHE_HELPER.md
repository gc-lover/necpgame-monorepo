# Шаблоны кода для кэширования MCP GitHub

## Обзор

Этот файл содержит готовые шаблоны кода для агентов, которые работают с MCP GitHub.

## ⚠️ ВАЖНО: Ограничения кэширования

**Кэш работает только в рамках одного вызова агента (одна сессия чата):**
- ✅ Работает: когда агент делает несколько запросов к одному Issue в рамках одной сессии
- ❌ НЕ работает: между разными вызовами агентов (`@agent-idea-writer` → `@agent-architect`)
- ❌ НЕ работает: между перезапусками Cursor

**Основные методы оптимизации (работают всегда):**
1. **Использование поиска** вместо множественных `issue_read` - это главное!
2. **Батчинг** для массовых операций - это главное!
3. **Кэширование** - дополнительная оптимизация для повторных запросов в одной сессии

## Инициализация кэша

```javascript
// Инициализируй кэш в начале работы агента
const issueCache = new Map();
const searchCache = new Map();
const projectCache = new Map();

// TTL для разных типов данных
const ISSUE_TTL = 5 * 60 * 1000; // 5 минут
const SEARCH_TTL = 2 * 60 * 1000; // 2 минуты
const PROJECT_TTL = 3 * 60 * 1000; // 3 минуты
```

## 1. Чтение Issue с кэшированием

```javascript
async function getCachedIssue(issueNumber) {
  // ОБЯЗАТЕЛЬНО: Проверяем кэш ПЕРЕД запросом
  if (issueCache.has(issueNumber)) {
    const cached = issueCache.get(issueNumber);
    const age = Date.now() - cached.timestamp;
    
    if (age < ISSUE_TTL) {
      return cached.data;
    }
    
    issueCache.delete(issueNumber);
  }
  
  // Запрашиваем из API только если нет в кэше
  const issue = await mcp_github_issue_read({
    method: 'get',
    owner: 'gc-lover',
    repo: 'necpgame-monorepo',
    issue_number: issueNumber
  });
  
  // ОБЯЗАТЕЛЬНО: Сохраняем в кэш
  issueCache.set(issueNumber, {
    data: issue,
    timestamp: Date.now()
  });
  
  await delay(300);
  
  return issue;
}
```

## 2. Поиск Issues с кэшированием

```javascript
async function searchIssuesCached(query, options = {}) {
  const { perPage = 100, page = 1 } = options;
  const cacheKey = `search:${query}:${perPage}:${page}`;
  
  // Проверяем кэш поиска
  if (searchCache.has(cacheKey)) {
    const cached = searchCache.get(cacheKey);
    if (Date.now() - cached.timestamp < SEARCH_TTL) {
      return cached.data;
    }
  }
  
  // Запрашиваем через поиск
  const searchResult = await mcp_github_search_issues({
    query: query,
    perPage: perPage,
    page: page
  });
  
  // Кэшируем результаты поиска
  searchCache.set(cacheKey, {
    data: searchResult,
    timestamp: Date.now()
  });
  
  // Кэшируем каждое Issue отдельно
  if (searchResult.items) {
    searchResult.items.forEach(issue => {
      issueCache.set(issue.number, {
        data: issue,
        timestamp: Date.now()
      });
    });
  }
  
  await delay(500);
  
  return searchResult;
}
```

## 3. Получение Project items с кэшированием

```javascript
async function getCachedProjectItems(projectNumber, query, fields) {
  const cacheKey = `project:${projectNumber}:${query}:${fields?.join(',') || 'all'}`;
  
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
  
  projectCache.set(cacheKey, {
    data: items,
    timestamp: Date.now()
  });
  
  await delay(500);
  
  return items;
}
```

## 4. Батчинг обновлений Issues

```javascript
async function batchUpdateIssues(updates, options = {}) {
  const {
    batchSize = 5,
    delayBetweenRequests = 300,
    delayBetweenBatches = 1000
  } = options;
  
  // Если меньше 3 Issues - обновляем последовательно
  if (updates.length < 3) {
    for (const update of updates) {
      await mcp_github_issue_write({
        method: 'update',
        owner: 'gc-lover',
        repo: 'necpgame-monorepo',
        issue_number: update.issue_number,
        ...update.data
      });
      
      // Обновляем кэш оптимистично
      if (issueCache.has(update.issue_number)) {
        const cached = issueCache.get(update.issue_number);
        Object.assign(cached.data, update.data);
      }
      
      await delay(delayBetweenRequests);
    }
    return;
  }
  
  // Если >=3 Issues - используем батчинг
  for (let i = 0; i < updates.length; i += batchSize) {
    const batch = updates.slice(i, i + batchSize);
    
    for (const update of batch) {
      try {
        await mcp_github_issue_write({
          method: 'update',
          owner: 'gc-lover',
          repo: 'necpgame-monorepo',
          issue_number: update.issue_number,
          ...update.data
        });
        
        // Обновляем кэш
        if (issueCache.has(update.issue_number)) {
          const cached = issueCache.get(update.issue_number);
          Object.assign(cached.data, update.data);
        }
        
        await delay(delayBetweenRequests);
      } catch (error) {
        if (error.message?.includes('rate limit')) {
          await handleRateLimit(error);
          continue;
        }
        throw error;
      }
    }
    
    if (i + batchSize < updates.length) {
      await delay(delayBetweenBatches);
    }
  }
}
```

## 5. Обработка rate limit

```javascript
async function handleRateLimit(error) {
  const retryAfter = error.headers?.['retry-after'] || error.headers?.['x-ratelimit-reset'];
  
  if (retryAfter) {
    const waitTime = parseInt(retryAfter) * 1000;
    console.log(`Rate limit exceeded. Waiting ${waitTime}ms...`);
    await delay(waitTime + 1000);
    return;
  }
  
  const resetMatch = error.message?.match(/until (\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2})/);
  if (resetMatch) {
    const resetTime = new Date(resetMatch[1]);
    const waitTime = resetTime.getTime() - Date.now() + 5000;
    console.log(`Rate limit exceeded. Waiting until ${resetTime.toISOString()}`);
    await delay(Math.max(waitTime, 60000));
    return;
  }
  
  await delay(60000);
}

async function safeApiCall(apiFunction, retries = 3) {
  for (let attempt = 1; attempt <= retries; attempt++) {
    try {
      return await apiFunction();
    } catch (error) {
      if (error.message?.includes('rate limit') || error.status === 403) {
        await handleRateLimit(error);
        if (attempt < retries) {
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

## 6. Утилита задержки

```javascript
function delay(ms) {
  return new Promise(resolve => setTimeout(resolve, ms));
}
```

## 7. Полный пример использования

```javascript
// Инициализация
const issueCache = new Map();
const searchCache = new Map();
const ISSUE_TTL = 5 * 60 * 1000;
const SEARCH_TTL = 2 * 60 * 1000;

function delay(ms) {
  return new Promise(resolve => setTimeout(resolve, ms));
}

// Поиск задач агента
async function findMyTasks(agentLabel) {
  const query = `is:issue is:open label:${agentLabel}`;
  
  // Используем кэшированный поиск
  const result = await searchIssuesCached(query);
  
  return result.items || [];
}

// Чтение Issue
async function readIssue(issueNumber) {
  return await getCachedIssue(issueNumber);
}

// Обновление меток
async function updateLabels(issueNumber, newLabels) {
  const issue = await getCachedIssue(issueNumber);
  
  const update = {
    issue_number: issueNumber,
    data: {
      labels: newLabels
    }
  };
  
  await batchUpdateIssues([update]);
  
  // Инвалидируем кэш для этого Issue
  issueCache.delete(issueNumber);
}
```

## Рекомендации

1. **Всегда инициализируй кэш** в начале работы агента
2. **Всегда проверяй кэш** перед запросом к API
3. **Всегда обновляй кэш** после успешного запроса
4. **Используй поиск** вместо множественных `issue_read`
5. **Используй батчинг** для массовых операций (>3 Issues)
6. **Обрабатывай rate limit** с автоматическими повторами


