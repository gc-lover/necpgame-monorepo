# Лучшие практики работы с MCP GitHub

## Обзор

Этот файл содержит примеры правильного и неправильного использования MCP GitHub API для избежания rate limit.

## ⚠️ ВАЖНО: Основные методы оптимизации

**Главные методы избежания rate limit (работают всегда):**
1. **Использование поиска** - вместо множественных `issue_read` используй `search_issues`
2. **Батчинг** - группируй операции в батчи с задержками
3. **GitHub Actions** - для массовых операций (>10 Issues)

**Кэширование** - дополнительная оптимизация, работает только в рамках одного вызова агента.

## ✅ Правильные практики

### 1. Использование поиска (ГЛАВНОЕ!)

```javascript
// ✅ ПРАВИЛЬНО: Используем поиск вместо множественных запросов
async function findTasks(agentLabel) {
  // Один запрос поиска вместо 100 запросов
  const result = await mcp_github_search_issues({
    query: `is:issue is:open label:${agentLabel}`,
    perPage: 100
  });
  
  return result.items;
}
```

### 2. Кэширование (дополнительная оптимизация)

```javascript
// ✅ ПРАВИЛЬНО: Кэширование в рамках одной сессии
const issueCache = new Map();
const ISSUE_TTL = 5 * 60 * 1000;

async function getCachedIssue(issueNumber) {
  // Проверяем кэш (работает только в рамках одного вызова агента)
  if (issueCache.has(issueNumber)) {
    const cached = issueCache.get(issueNumber);
    if (Date.now() - cached.timestamp < ISSUE_TTL) {
      return cached.data; // Используем кэш
    }
  }
  
  const issue = await mcp_github_issue_read({ issue_number: issueNumber });
  issueCache.set(issueNumber, { data: issue, timestamp: Date.now() });
  await delay(300);
  return issue;
}
```

### 3. Последовательные запросы с задержками

```javascript
// ✅ ПРАВИЛЬНО: Последовательные запросы с задержками
async function updateMultipleIssues(issueNumbers, newLabels) {
  for (const issueNum of issueNumbers) {
    await mcp_github_issue_write({
      method: 'update',
      issue_number: issueNum,
      labels: newLabels
    });
    await delay(300); // Задержка между запросами
  }
}
```

### 4. Батчинг для массовых операций

```javascript
// ✅ ПРАВИЛЬНО: Батчинг для >=3 Issues
async function batchUpdate(updates) {
  if (updates.length >= 3) {
    const batchSize = 5;
    for (let i = 0; i < updates.length; i += batchSize) {
      const batch = updates.slice(i, i + batchSize);
      
      for (const update of batch) {
        await mcp_github_issue_write(update);
        await delay(300);
      }
      
      if (i + batchSize < updates.length) {
        await delay(1000); // Большая задержка между батчами
      }
    }
  } else {
    // Для <3 Issues - последовательно
    for (const update of updates) {
      await mcp_github_issue_write(update);
      await delay(300);
    }
  }
}
```

### 5. Обработка rate limit

```javascript
// ✅ ПРАВИЛЬНО: Обработка rate limit с повторами
async function safeApiCall(apiFunction) {
  for (let attempt = 1; attempt <= 3; attempt++) {
    try {
      return await apiFunction();
    } catch (error) {
      if (error.message?.includes('rate limit') || error.status === 403) {
        const waitTime = 60000; // 60 секунд
        console.log(`Rate limit, waiting ${waitTime}ms...`);
        await delay(waitTime);
        continue;
      }
      throw error;
    }
  }
}
```

### 6. Использование GitHub Actions для массовых операций

```javascript
// ✅ ПРАВИЛЬНО: Для >10 Issues используем GitHub Actions
if (issueNumbers.length > 10) {
  // Добавляем метку для автоматической обработки
  await mcp_github_issue_write({
    method: 'update',
    issue_number: firstIssueNumber,
    labels: ['queue:batch-process', ...existingLabels]
  });
  
  // GitHub Actions автоматически обработает батчами
  return 'Issues queued for batch processing';
}
```

## ❌ Неправильные практики

### 1. Отсутствие кэширования

```javascript
// ❌ НЕПРАВИЛЬНО: Каждый раз запрос к API
async function getIssue(issueNumber) {
  // Нет проверки кэша - каждый раз запрос!
  const issue = await mcp_github_issue_read({ issue_number: issueNumber });
  return issue;
}

// Вызов дважды = два запроса к API
const issue1 = await getIssue(123);
const issue2 = await getIssue(123); // Повторный запрос!
```

### 2. Параллельные запросы

```javascript
// ❌ НЕПРАВИЛЬНО: Параллельные запросы вызывают secondary rate limit
const issues = await Promise.all([
  mcp_github_issue_read({ issue_number: 1 }),
  mcp_github_issue_read({ issue_number: 2 }),
  mcp_github_issue_read({ issue_number: 3 }),
]);
```

### 3. Множественные запросы вместо поиска

```javascript
// ❌ НЕПРАВИЛЬНО: 100 запросов вместо одного поиска
const issues = [];
for (let i = 1; i <= 100; i++) {
  try {
    const issue = await mcp_github_issue_read({ issue_number: i });
    if (issue.labels.some(l => l.name === 'agent:content-writer')) {
      issues.push(issue);
    }
  } catch (e) {}
  await delay(300);
}
```

### 4. Отсутствие задержек

```javascript
// ❌ НЕПРАВИЛЬНО: Нет задержек между запросами
async function updateIssues(issueNumbers) {
  for (const issueNum of issueNumbers) {
    await mcp_github_issue_write({
      method: 'update',
      issue_number: issueNum,
      labels: ['agent:qa']
    });
    // Нет задержки - может вызвать rate limit!
  }
}
```

### 5. Игнорирование rate limit

```javascript
// ❌ НЕПРАВИЛЬНО: Нет обработки ошибок rate limit
async function updateIssue(issueNumber) {
  try {
    await mcp_github_issue_write({
      method: 'update',
      issue_number: issueNumber,
      labels: ['agent:qa']
    });
  } catch (error) {
    // Игнорируем ошибку - плохо!
    console.error('Error:', error);
  }
}
```

### 6. Прямые запросы для массовых операций

```javascript
// ❌ НЕПРАВИЛЬНО: Прямые запросы для 50+ Issues
async function transferToQA(issueNumbers) {
  // 50 Issues = 50 запросов = может вызвать rate limit
  for (const issueNum of issueNumbers) {
    await mcp_github_issue_write({
      method: 'update',
      issue_number: issueNum,
      labels: ['agent:qa', 'stage:testing']
    });
    await delay(300);
  }
}
```

## Сравнение подходов

### Поиск задач агента

**❌ НЕПРАВИЛЬНО:**
```javascript
// Множественные запросы
const allIssues = [];
for (let i = 1; i <= 200; i++) {
  try {
    const issue = await mcp_github_issue_read({ issue_number: i });
    if (issue.labels.some(l => l.name === 'agent:content-writer')) {
      allIssues.push(issue);
    }
  } catch (e) {}
  await delay(300);
}
// = 200 запросов, ~60 секунд
```

**✅ ПРАВИЛЬНО:**
```javascript
// Один запрос поиска
const result = await mcp_github_search_issues({
  query: 'is:issue is:open label:agent:content-writer',
  perPage: 100
});
// = 1 запрос, ~0.5 секунды
```

### Обновление меток

**❌ НЕПРАВИЛЬНО:**
```javascript
// Параллельные запросы
await Promise.all(
  issueNumbers.map(num => 
    mcp_github_issue_write({
      method: 'update',
      issue_number: num,
      labels: ['agent:qa']
    })
  )
);
// = Может вызвать secondary rate limit
```

**✅ ПРАВИЛЬНО:**
```javascript
// Последовательные с задержками
for (const issueNum of issueNumbers) {
  await mcp_github_issue_write({
    method: 'update',
    issue_number: issueNum,
    labels: ['agent:qa']
  });
  await delay(300);
}
// = Безопасно, но медленно для больших батчей
```

**✅ ЕЩЕ ЛУЧШЕ (для >10 Issues):**
```javascript
// Используем GitHub Actions Batch Processor
if (issueNumbers.length > 10) {
  // Добавляем метку для автоматической обработки
  await mcp_github_issue_write({
    method: 'update',
    issue_number: issueNumbers[0],
    labels: ['queue:batch-process']
  });
}
```

## Чеклист перед работой с Issues

- [ ] Инициализирован кэш в памяти (Map/объект)
- [ ] Используется поиск вместо множественных `issue_read`
- [ ] Проверяется кэш перед каждым запросом
- [ ] Обновляется кэш после каждого запроса
- [ ] Добавлены задержки между запросами (300-500ms)
- [ ] Используется батчинг для >=3 Issues
- [ ] Используется GitHub Actions для >10 Issues
- [ ] Обрабатываются ошибки rate limit
- [ ] Запросы выполняются последовательно, не параллельно

## Рекомендации по частоте

| Операция | Задержка | Размер батча | Использовать Batch Processor |
|----------|----------|--------------|------------------------------|
| Чтение Issue | 200-300ms | - | Нет |
| Поиск Issues | 500-1000ms | - | Нет |
| Обновление 1-2 Issues | 300-500ms | - | Нет |
| Обновление 3-9 Issues | 300-500ms | 5 | Нет |
| Обновление 10+ Issues | - | - | Да |

## Дополнительные ресурсы

- `.cursor/rules/GITHUB_API_OPTIMIZATION.md` - полные правила оптимизации
- `.cursor/rules/GITHUB_MCP_CACHE_HELPER.md` - шаблоны кода для кэширования
- `.github/GITHUB_API_QUEUE_SYSTEM.md` - документация по Batch Processor


