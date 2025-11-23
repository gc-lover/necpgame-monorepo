# Оптимизация запросов к GitHub API

## Система очередей на уровне GitHub

**Для массовых операций используй GitHub Actions Batch Processor:**
- `.github/workflows/github-api-batch-processor.yml` - автоматическая обработка батчами
- `.github/GITHUB_API_QUEUE_SYSTEM.md` - документация по использованию

**Преимущества:**
- Автоматическая обработка батчами
- Защита от rate limit
- Можно запускать вручную или по расписанию
- Не требует работы агента в реальном времени

**Когда использовать:**
- Массовая передача Issues в QA (>10 Issues)
- Обновление меток для множества Issues
- Добавление комментариев к батчу Issues

## Проблема

GitHub API имеет два типа лимитов:
1. **Primary Rate Limit**: 5000 запросов/час для аутентифицированных запросов
2. **Secondary Rate Limit**: защита от злоупотреблений, срабатывает при:
   - Слишком частых запросах
   - Параллельных запросах
   - Запросах из разных IP/сессий

## Правила оптимизации для агентов

### 1. Последовательные запросы с задержками

**❌ НЕПРАВИЛЬНО:**
```javascript
// Параллельные запросы - могут вызвать secondary rate limit
const issues = await Promise.all([
  mcp_github_issue_read({ issue_number: 1 }),
  mcp_github_issue_read({ issue_number: 2 }),
  mcp_github_issue_read({ issue_number: 3 }),
]);
```

**✅ ПРАВИЛЬНО:**
```javascript
// Последовательные запросы с задержкой
const issues = [];
for (const num of [1, 2, 3]) {
  const issue = await mcp_github_issue_read({ issue_number: num });
  issues.push(issue);
  
  // Задержка 200-500ms между запросами
  await new Promise(resolve => setTimeout(resolve, 300));
}
```

**Рекомендуемые задержки:**
- Одиночные запросы: 200-300ms
- Массовые операции (обновление меток): 300-500ms
- Поиск Issues: 500-1000ms

### 2. Группирование операций (батчинг)

**❌ НЕПРАВИЛЬНО:**
```javascript
// Множественные отдельные запросы
for (const issueNum of [1, 2, 3, 4, 5]) {
  await mcp_github_issue_write({
    method: 'update',
    issue_number: issueNum,
    labels: ['agent:qa', 'stage:testing']
  });
  await new Promise(resolve => setTimeout(resolve, 300));
}
```

**✅ ПРАВИЛЬНО:**
```javascript
// Группируй операции по типам
const issuesToUpdate = [1, 2, 3, 4, 5];

// Сначала читаем все Issues (один запрос на поиск)
const searchResult = await mcp_github_search_issues({
  query: `is:issue is:open label:agent:content-writer`,
  perPage: 100
});

// Затем обновляем батчами по 5-10 Issues
const batchSize = 5;
for (let i = 0; i < issuesToUpdate.length; i += batchSize) {
  const batch = issuesToUpdate.slice(i, i + batchSize);
  
  // Обновляем батч с задержкой между батчами
  for (const issueNum of batch) {
    await mcp_github_issue_write({
      method: 'update',
      issue_number: issueNum,
      labels: ['agent:qa', 'stage:testing']
    });
    await new Promise(resolve => setTimeout(resolve, 300));
  }
  
  // Большая задержка между батчами
  if (i + batchSize < issuesToUpdate.length) {
    await new Promise(resolve => setTimeout(resolve, 1000));
  }
}
```

### 3. Кэширование результатов

**✅ ПРАВИЛЬНО:**
```javascript
// Кэшируй результаты локально
const issueCache = new Map();

async function getCachedIssue(issueNumber) {
  // Проверяем кэш
  if (issueCache.has(issueNumber)) {
    const cached = issueCache.get(issueNumber);
    // Кэш действителен 5 минут
    if (Date.now() - cached.timestamp < 5 * 60 * 1000) {
      return cached.data;
    }
  }
  
  // Запрашиваем из API
  const issue = await mcp_github_issue_read({
    issue_number: issueNumber
  });
  
  // Сохраняем в кэш
  issueCache.set(issueNumber, {
    data: issue,
    timestamp: Date.now()
  });
  
  // Задержка после запроса
  await new Promise(resolve => setTimeout(resolve, 300));
  
  return issue;
}
```

### 4. Использование поиска вместо множественных запросов

**❌ НЕПРАВИЛЬНО:**
```javascript
// Множественные запросы для получения списка Issues
const issues = [];
for (let i = 1; i <= 100; i++) {
  try {
    const issue = await mcp_github_issue_read({ issue_number: i });
    if (issue.labels.some(l => l.name === 'agent:content-writer')) {
      issues.push(issue);
    }
  } catch (e) {
    // Issue не существует
  }
  await new Promise(resolve => setTimeout(resolve, 300));
}
```

**✅ ПРАВИЛЬНО:**
```javascript
// Один запрос поиска вместо 100 запросов
const searchResult = await mcp_github_search_issues({
  query: 'is:issue is:open label:agent:content-writer label:stage:content',
  perPage: 100,
  page: 1
});

// Используем результаты поиска
const issues = searchResult.items;
```

## Паттерны для агентов

### Content Writer: Массовая передача в QA

```javascript
async function transferToQA(issueNumbers) {
  const batchSize = 5;
  const delayBetweenRequests = 300; // ms
  const delayBetweenBatches = 1000; // ms
  
  for (let i = 0; i < issueNumbers.length; i += batchSize) {
    const batch = issueNumbers.slice(i, i + batchSize);
    
    // Обрабатываем батч
    for (const issueNum of batch) {
      // Читаем Issue (используем кэш если возможно)
      const issue = await getCachedIssue(issueNum);
      
      // Обновляем метки
      const newLabels = issue.labels
        .map(l => l.name)
        .filter(l => l !== 'agent:content-writer')
        .concat(['agent:qa', 'stage:testing']);
      
      await mcp_github_issue_write({
        method: 'update',
        issue_number: issueNum,
        labels: newLabels
      });
      
      // Задержка между запросами
      await new Promise(resolve => setTimeout(resolve, delayBetweenRequests));
    }
    
    // Большая задержка между батчами
    if (i + batchSize < issueNumbers.length) {
      await new Promise(resolve => setTimeout(resolve, delayBetweenBatches));
    }
  }
}
```

### Idea Writer: Поиск задач

```javascript
async function findIdeaWriterTasks() {
  // Используем поиск вместо множественных запросов
  const searchResult = await mcp_github_search_issues({
    query: 'is:issue is:open label:agent:idea-writer label:stage:idea',
    perPage: 100,
    page: 1
  });
  
  // Кэшируем результаты
  searchResult.items.forEach(issue => {
    issueCache.set(issue.number, {
      data: issue,
      timestamp: Date.now()
    });
  });
  
  return searchResult.items;
}
```

## Обработка ошибок rate limit

```javascript
async function safeApiCall(apiFunction, retries = 3) {
  for (let attempt = 1; attempt <= retries; attempt++) {
    try {
      return await apiFunction();
    } catch (error) {
      if (error.message?.includes('rate limit')) {
        // Извлекаем время сброса из ошибки
        const resetMatch = error.message.match(/until (\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2})/);
        if (resetMatch) {
          const resetTime = new Date(resetMatch[1]);
          const waitTime = resetTime.getTime() - Date.now() + 5000; // +5 секунд запас
          
          console.log(`Rate limit exceeded. Waiting until ${resetTime.toISOString()}`);
          await new Promise(resolve => setTimeout(resolve, waitTime));
          continue;
        }
      }
      
      // Другие ошибки - пробуем с экспоненциальной задержкой
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

## Рекомендации по частоте запросов

| Операция | Задержка между запросами | Размер батча |
|----------|-------------------------|--------------|
| Чтение Issue | 200-300ms | - |
| Обновление Issue | 300-500ms | 5-10 Issues |
| Поиск Issues | 500-1000ms | - |
| Добавление комментариев | 300-500ms | 5-10 комментариев |
| Обновление меток | 300-500ms | 5-10 Issues |
| Массовые операции | 500-1000ms | 5 Issues |

## Мониторинг лимитов

Перед началом массовых операций проверяй лимиты:

```javascript
async function checkRateLimit() {
  // GitHub API возвращает заголовки с информацией о лимитах
  // X-RateLimit-Limit: 5000
  // X-RateLimit-Remaining: 4500
  // X-RateLimit-Reset: timestamp
  
  // MCP GitHub сервер может не предоставлять эти заголовки напрямую
  // Но можно отслеживать ошибки rate limit и делать паузы
}
```

## Когда использовать систему очередей vs прямые запросы

### Используй GitHub Actions Batch Processor если:
- ✅ Нужно обработать **>10 Issues** одновременно
- ✅ Операция может быть выполнена асинхронно
- ✅ Нужна гарантия защиты от rate limit
- ✅ Операция повторяющаяся (можно автоматизировать)

### Используй прямые MCP запросы если:
- ✅ Нужно обработать **<10 Issues**
- ✅ Требуется немедленная обратная связь
- ✅ Операция уникальная и не повторяется
- ✅ Нужна интерактивная обработка

### Примеры

**Используй Batch Processor:**
- Массовая передача 50+ квестов в QA
- Обновление меток для всех Issues агента
- Добавление комментариев к батчу готовых задач

**Используй прямые запросы:**
- Обработка одной задачи
- Проверка статуса Issue
- Добавление комментария к одной задаче

## Чеклист перед массовыми операциями

- [ ] Определи: использовать Batch Processor или прямые запросы
- [ ] Используй поиск вместо множественных запросов
- [ ] Группируй операции в батчи по 5-10
- [ ] Добавляй задержки между запросами (300-500ms)
- [ ] Добавляй большие задержки между батчами (1000ms)
- [ ] Кэшируй результаты локально
- [ ] Обрабатывай ошибки rate limit с повторными попытками
- [ ] Делай операции последовательно, не параллельно

## Примеры для разных агентов

### Content Writer
- При передаче в QA: батчи по 5 Issues, задержка 300ms между запросами, 1000ms между батчами
- При проверке готовности: используй поиск, кэшируй результаты

### Idea Writer
- При поиске задач: один запрос поиска вместо множественных
- При создании Issues: задержка 500ms между созданиями
- При обновлении меток: батчи по 5, задержка 300ms

### QA Agent
- При проверке Issues: используй поиск, кэшируй результаты
- При обновлении статусов: батчи по 5, задержка 300ms

