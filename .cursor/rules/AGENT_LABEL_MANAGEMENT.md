# Управление метками для агентов

## Общие правила

Каждый агент ОБЯЗАН управлять метками при работе с Issue:

1. **При начале работы** - добавить свою метку `agent:{agent-name}`
2. **При завершении работы** - удалить свою метку
3. **При переходе к следующему этапу** - добавить метку следующего агента

## Формат меток

### Метки агентов
- `agent:idea-writer` - Idea Writer
- `agent:architect` - Architect
- `agent:api-designer` - API Designer
- `agent:backend` - Backend Developer
- `agent:network` - Network Engineer
- `agent:devops` - DevOps
- `agent:performance` - Performance Engineer
- `agent:ue5` - UE5 Developer
- `agent:content-writer` - Content Writer
- `agent:qa` - QA/Testing
- `agent:release` - Release

### Метки этапов
- `stage:idea` - Idea stage
- `stage:design` - Design stage
- `stage:api-design` - API Design stage
- `stage:backend-dev` - Backend Development stage
- `stage:network` - Network stage
- `stage:infrastructure` - Infrastructure stage
- `stage:performance` - Performance stage
- `stage:client-dev` - Client Development stage
- `stage:content` - Content stage
- `stage:testing` - Testing stage
- `stage:release` - Release stage

### Метки возврата задач
- `returned` - Задача возвращена (требует внимания)
- `needs-review` - Требуется ручное вмешательство (неясная ситуация)

## Использование MCP GitHub

### ⚠️ ОБЯЗАТЕЛЬНО: Батчинг для массовых операций

**Перед обновлением меток ОБЯЗАТЕЛЬНО:**
1. **Для <3 Issues:** обновляй последовательно с задержками (300-500ms)
2. **Для 3-9 Issues:** используй батчинг (батчи по 5, задержки между запросами и батчами)
3. **Для >=10 Issues:** используй GitHub Actions Batch Processor (добавь метку `queue:batch-process`)
4. (Опционально) Используй кэшированную функцию для чтения Issue в рамках одной сессии (см. `.cursor/rules/GITHUB_MCP_CACHE_HELPER.md`)

### Добавление меток

```javascript
// ✅ ПРАВИЛЬНО: С кэшированием и задержками
const issueCache = new Map();

async function addLabels(issueNumber, newLabels) {
  // Читаем Issue из кэша
  let issue = issueCache.get(issueNumber);
  if (!issue || Date.now() - issue.timestamp > 5 * 60 * 1000) {
    issue = await mcp_github_issue_read({
      method: 'get',
      issue_number: issueNumber
    });
    issueCache.set(issueNumber, { data: issue, timestamp: Date.now() });
  } else {
    issue = issue.data;
  }
  
  const currentLabels = issue.labels.map(l => l.name);
  const allLabels = [...new Set([...currentLabels, ...newLabels])];
  
  await mcp_github_issue_write({
    method: 'update',
    owner: 'gc-lover',
    repo: 'necpgame-monorepo',
    issue_number: issueNumber,
    labels: allLabels
  });
  
  // Инвалидируем кэш
  issueCache.delete(issueNumber);
  await delay(300);
}
```

### Удаление меток

```javascript
// ✅ ПРАВИЛЬНО: С кэшированием
async function removeLabel(issueNumber, labelToRemove) {
  // Используем кэшированную функцию
  const issue = await getCachedIssue(issueNumber);
  
  const currentLabels = issue.labels.map(l => l.name);
  const newLabels = currentLabels.filter(l => l !== labelToRemove);
  
  await mcp_github_issue_write({
    method: 'update',
    owner: 'gc-lover',
    repo: 'necpgame-monorepo',
    issue_number: issueNumber,
    labels: newLabels
  });
  
  // Инвалидируем кэш
  issueCache.delete(issueNumber);
  await delay(300);
}
```

### Переход к следующему агенту

```javascript
// ✅ ПРАВИЛЬНО: С кэшированием
async function transferToNextAgent(issueNumber, myAgentLabel, nextAgentLabel, nextStageLabel) {
  // 1. Используем кэшированную функцию для чтения
  const issue = await getCachedIssue(issueNumber);
  const currentLabels = issue.labels.map(l => l.name);
  
  // 2. Удали свою метку агента
  const labelsWithoutMyAgent = currentLabels.filter(l => l !== myAgentLabel);
  
  // 3. Добавь метку следующего агента
  const nextAgentLabels = [nextAgentLabel, nextStageLabel];
  const allLabels = [...labelsWithoutMyAgent, ...nextAgentLabels];
  
  // 4. Обнови Issue
  await mcp_github_issue_write({
    method: 'update',
    owner: 'gc-lover',
    repo: 'necpgame-monorepo',
    issue_number: issueNumber,
    labels: allLabels
  });
  
  // 5. Инвалидируем кэш
  issueCache.delete(issueNumber);
  await delay(300);
  
  // 6. Добавь комментарий
  await mcp_github_add_issue_comment({
    owner: 'gc-lover',
    repo: 'necpgame-monorepo',
    issue_number: issueNumber,
    body: `✅ Задача передана агенту ${nextAgentLabel}`
  });
  
  await delay(300);
}
```

### Массовое обновление меток (>=3 Issues)

```javascript
// ✅ ПРАВИЛЬНО: Батчинг для >=3 Issues
async function batchUpdateLabels(issueNumbers, newLabels) {
  if (issueNumbers.length >= 10) {
    // Для >10 Issues используем GitHub Actions Batch Processor
    await mcp_github_issue_write({
      method: 'update',
      issue_number: issueNumbers[0],
      labels: ['queue:batch-process', ...newLabels]
    });
    return 'Issues queued for batch processing';
  }
  
  // Для 3-9 Issues используем батчинг
  const batchSize = 5;
  for (let i = 0; i < issueNumbers.length; i += batchSize) {
    const batch = issueNumbers.slice(i, i + batchSize);
    
    for (const issueNum of batch) {
      const issue = await getCachedIssue(issueNum);
      const currentLabels = issue.labels.map(l => l.name);
      const allLabels = [...new Set([...currentLabels, ...newLabels])];
      
      await mcp_github_issue_write({
        method: 'update',
        issue_number: issueNum,
        labels: allLabels
      });
      
      issueCache.delete(issueNum);
      await delay(300);
    }
    
    if (i + batchSize < issueNumbers.length) {
      await delay(1000);
    }
  }
}
```

## Workflow переходов

### Системные задачи (требуют архитектуры):
```
idea-writer → architect → api-designer → backend → network → ue5 → qa → release
     ↓            ↓             ↓            ↓         ↓        ↓      ↓       ↓
agent:idea   agent:arch   agent:api    agent:back  agent:net agent:ue agent:qa agent:rel
```

### Контентные задачи (НЕ требуют архитектуры):
```
idea-writer → content-writer → qa → release
     ↓              ↓           ↓       ↓
agent:idea    agent:content  agent:qa agent:rel
```

## Возврат задач

### Когда использовать метку `returned`

Используй метку `returned` когда:
- Задача не готова для работы (отсутствуют входные данные)
- Задача неправильного типа (контентная vs системная)
- Задача должна быть передана другому агенту

### Когда использовать метку `needs-review`

Используй метку `needs-review` когда:
- Неясно, кому должна быть передана задача
- Требуется ручное вмешательство для определения правильного агента
- Ситуация не описана в правилах

### Процесс возврата

1. **Удали свои метки агента:**
   - `agent:{agent-name}`
   - `stage:{stage-name}`

2. **Добавь метку возврата:**
   - `returned` - для явного возврата
   - `needs-review` - если неясно, кому передать

3. **Добавь метку правильного агента** (если известна):
   - `agent:{correct-agent}`
   - `stage:{correct-stage}`

4. **Добавь комментарий** с объяснением причины возврата

**Подробнее:** см. `.cursor/rules/AGENT_TASK_RETURN.md`

## Важно

- **ВСЕГДА** добавляй свою метку при начале работы
- **ВСЕГДА** удаляй свою метку при завершении работы
- **ВСЕГДА** добавляй метку следующего агента при переходе
- **ВСЕГДА** проверяй готовность входных данных перед началом работы
- **ВСЕГДА** возвращай задачу, если входные данные не готовы
- **НЕ** удаляй другие метки (приоритет, категория и т.д.)
- **НЕ** начинай работу без необходимых входных данных
- **ИСПОЛЬЗУЙ** MCP GitHub для управления метками
- **ОБЯЗАТЕЛЬНО** используй кэширование перед чтением Issues
- **ОБЯЗАТЕЛЬНО** используй батчинг для >=3 Issues
- **ОБЯЗАТЕЛЬНО** используй GitHub Actions для >10 Issues

## Дополнительные ресурсы

- `.cursor/rules/GITHUB_MCP_CACHE_HELPER.md` - шаблоны кода для кэширования
- `.cursor/rules/GITHUB_MCP_BEST_PRACTICES.md` - примеры правильного использования
- `.cursor/rules/GITHUB_API_OPTIMIZATION.md` - полные правила оптимизации

