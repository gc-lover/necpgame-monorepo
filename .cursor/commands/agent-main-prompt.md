# Agent Main Prompt Command

## Обзор
Главная команда для работы CURSOR AI Агентов в проекте NECPGAME. Определяет полный workflow для автономной работы агентов.

## Основной промпт агентов

```
БЕРИ КАЖДУЮ ЗАДАЧУ И ДОВОДИ ДО СОСТОЯНИЯ DONE!

Для поиска задач используй MCP GITHUB: @.cursor/MCP_GITHUB_GUIDE.md
Ищи задачи со статусом TODO в GitHub Projects с обязательным фильтром Status:"Todo" в запросе.

КРИТИЧНО: ОБЯЗАТЕЛЬНО ПРОВЕРЬ РЕАЛЬНОЕ СОСТОЯНИЕ РЕПОЗИТОРИЯ перед взятием задачи!
- Проверь статус Issue через MCP GitHub (может быть закрыта, даже если в Projects статус Todo)
- Проверь код в репозитории - возможно задача уже выполнена
- Статусы в Projects могут быть устаревшими и не совпадать с реальностью!

СРАЗУ КАК БЕРЕШЬ ЗАДАЧУ ТЫ ДОЛЖЕН ИЗМЕНИТЬ СТАТУС С "TODO" НА "IN PROGRESS" через MCP GitHub.

ТЫ ДОЛЖЕН ДОВЕСТИ КАЖДУЮ ЗАДАЧУ ДО СОСТОЯНИЯ DONE!

Все роли агентов: @.cursor/rules/agent-api-designer.mdc @.cursor/rules/agent-architect.mdc @.cursor/rules/agent-autonomy.mdc @.cursor/rules/agent-backend.mdc @.cursor/rules/agent-content-writer.mdc @.cursor/rules/agent-database.mdc @.cursor/rules/agent-devops.mdc @.cursor/rules/agent-game-balance.mdc @.cursor/rules/agent-idea-writer.mdc @.cursor/rules/agent-network.mdc @.cursor/rules/agent-performance.mdc @.cursor/rules/agent-qa.mdc @.cursor/rules/agent-release.mdc @.cursor/rules/agent-security.mdc @.cursor/rules/agent-ue5.mdc @.cursor/rules/agent-ui-ux-designer.mdc @.cursor/rules/always.mdc @.cursor/rules/linter-emoji-ban.mdc

Базовый сценарий: @.cursor/AGENT_QUICK_START.md
Изменение статусов: @.cursor/AGENT_STATUS_CHANGE_GUIDE.md
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

#### 1.1. Поиск задач
```javascript
// ОБЯЗАТЕЛЬНО использовать фильтр Status:"Todo" для поиска незавершенных задач
// Поиск всех задач со статусом Todo
await mcp_github_list_project_items({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  query: 'Status:"Todo"'
});

// Поиск задач конкретного агента со статусом Todo
// Пример для Backend агента
await mcp_github_list_project_items({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  query: 'Agent:"Backend" Status:"Todo"'
});

// Важно: используй кавычки для значений с пробелами (например, "In Progress")
// Правильно: Status:"Todo", Status:"In Progress"
// Неправильно: Status:Todo, Status:In Progress
```

#### 1.2. ОБЯЗАТЕЛЬНАЯ проверка актуальности задачи
**КРИТИЧНО: Статусы задач в Projects могут не совпадать с реальностью!**
**ПЕРЕД взятием задачи в работу агент ОБЯЗАН:**

1. **Проверить статус Issue через MCP GitHub** - убедиться что Issue не закрыта
   ```javascript
   // Проверка статуса Issue
   const issue = await mcp_github_issue_read({
     owner: 'gc-lover',
     repo: 'necpgame-monorepo',
     issue_number: issueNumber,
     method: 'get'
   });
   
   // Если Issue закрыта - задача уже выполнена, не брать в работу!
   if (issue.state === 'closed') {
     // Задача уже закрыта, пропустить
     return;
   }
   ```

2. **Проверить содержимое задачи** - прочитать полное описание и требования

3. **Проверить реальное состояние репозитория** - проверить код/файлы в репозитории
   - Найти файлы, которые должны быть изменены
   - Убедиться что требуемый функционал еще не реализован
   - Проверить что изменения действительно нужны

4. **Оценить актуальность** - возможно задача уже выполнена, устарела или решена другим способом

5. **ТОЛЬКО ЕСЛИ задача актуальна и Issue открыта** - брать её в работу

**Признаки неактуальной задачи:**
- Issue закрыта (`state === 'closed'`)
- Код уже реализован в репозитории (файлы существуют, функционал работает)
- Требования противоречат текущему состоянию проекта
- Задача дублирует существующую функциональность
- Изменения уже внесены другим способом

**Важно:** Статус в Projects (Todo/In Progress/Done) может быть устаревшим!
Всегда проверяй реальное состояние Issue и кода в репозитории!

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
| API спецификации | API Designer | `agent-api-designer.mdc` |
| Архитектура, дизайн | Architect | `agent-architect.mdc` |
| Backend код, Go сервисы | Backend | `agent-backend.mdc` |
| Контент, квесты, лор | Content Writer | `agent-content-writer.mdc` |
| Идеи, концепции | Idea Writer | `agent-idea-writer.mdc` |

### 3. Выполнение задачи
- Следовать правилам выбранного агента
- Применять валидацию из `common-validation.md`
- Соблюдать ограничения из `always.mdc`
- Избегать создания мусорных файлов

### 4. Передача задачи
```javascript
// Передача следующему агенту
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

// Комментарий о передаче
await mcp_github_add_issue_comment({
  owner: 'gc-lover',
  repo: 'necpgame-monorepo',
  issue_number: issueNumber,
  body: '[OK] Work completed. Handed off to {NextAgent}. Issue: #{number}'
});
```

## Правила качества

### ✅ ДОЛЖНО быть
- MCP GitHub для всех операций с задачами
- **ОБЯЗАТЕЛЬНАЯ проверка актуальности задачи перед взятием**
- Смена статуса TODO → In Progress при взятии
- Выбор правильной роли агента
- Качественное выполнение требований
- Передача следующему агенту с комментарием

### ❌ ЗАПРЕЩЕНО
- Создание отчетов, рапортов, summary файлов
- Использование GitHub CLI для изменения статусов
- Работа без смены статуса на In Progress
- Оставление задач без передачи следующему агенту
- Нарушение правил размещения файлов

## Лимиты и ограничения

- **Максимум задач за раз:** 5
- **Файлы:** Только необходимые для реализации
- **Коммиты:** Формат `[agent] {type}: {desc}`
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
- `@.cursor/MCP_GITHUB_GUIDE.md` - работа с GitHub через MCP
- `@.cursor/AGENT_STATUS_CHANGE_GUIDE.md` - смена статусов задач
- `@.cursor/commands/github-integration.md` - команды GitHub
- `@.cursor/commands/common-validation.md` - валидация кода