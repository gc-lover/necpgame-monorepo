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

## Использование MCP GitHub

### Добавление меток

```javascript
await mcp_github_issue_write({
  method: 'update',
  owner: 'gc-lover',
  repo: 'necpgame-monorepo',
  issue_number: 9,
  labels: ['agent:idea-writer', 'stage:idea', ...existing_labels]
});
```

### Удаление меток

```javascript
// Получи текущие метки
const issue = await mcp_github_issue_read({
  owner: 'gc-lover',
  repo: 'necpgame-monorepo',
  issue_number: 9,
  method: 'get'
});

// Удали свою метку
const currentLabels = issue.labels.map(l => l.name);
const newLabels = currentLabels.filter(l => l !== 'agent:idea-writer');

// Обнови Issue
await mcp_github_issue_write({
  method: 'update',
  owner: 'gc-lover',
  repo: 'necpgame-monorepo',
  issue_number: 9,
  labels: newLabels
});
```

### Переход к следующему агенту

```javascript
// 1. Получи текущие метки
const issue = await mcp_github_issue_read({...});
const currentLabels = issue.labels.map(l => l.name);

// 2. Удали свою метку агента
const labelsWithoutMyAgent = currentLabels.filter(l => l !== 'agent:idea-writer');

// 3. Добавь метку следующего агента
const nextAgentLabels = ['agent:architect', 'stage:design'];
const allLabels = [...labelsWithoutMyAgent, ...nextAgentLabels];

// 4. Обнови Issue
await mcp_github_issue_write({
  method: 'update',
  owner: 'gc-lover',
  repo: 'necpgame-monorepo',
  issue_number: 9,
  labels: allLabels
});

// 5. Добавь комментарий
await mcp_github_add_issue_comment({
  owner: 'gc-lover',
  repo: 'necpgame-monorepo',
  issue_number: 9,
  body: 'OK Задача передана агенту @agent:architect'
});
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

## Важно

- **ВСЕГДА** добавляй свою метку при начале работы
- **ВСЕГДА** удаляй свою метку при завершении работы
- **ВСЕГДА** добавляй метку следующего агента при переходе
- **НЕ** удаляй другие метки (приоритет, категория и т.д.)
- **ИСПОЛЬЗУЙ** MCP GitHub для управления метками

