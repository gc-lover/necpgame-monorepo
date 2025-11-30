# Руководство по статусам и передаче Issues между агентами

**Единый справочник по управлению статусами и передаче задач между агентами**

## 📋 Содержание

1. [Основные концепции](#основные-концепции)
2. [Формат статусов](#формат-статусов)
3. [Жизненный цикл задачи](#жизненный-цикл-задачи)
4. [Передача задач между агентами](#передача-задач-между-агентами)
5. [Список всех агентов и их статусов](#список-всех-агентов-и-их-статусов)
6. [Техническая реализация](#техническая-реализация)
7. [Примеры](#примеры)

---

## Основные концепции

### Что такое Status?

**Status** - это поле в GitHub Project, которое определяет:
- **Какой агент** должен обработать задачу
- **Текущее состояние** задачи (Todo, In Progress, Blocked, Review, Returned)

### Почему Status, а не Labels?

- OK **Status** - единственный источник правды о текущем этапе задачи
- OK **Labels** - только функциональные метки (backend, ui, security и т.д.)
- OK **Status** автоматически определяет агента и этап работы
- OK Легко отслеживать прогресс задачи через Status

### Принципы работы со статусами

1. **Одна задача = один статус** - в каждый момент времени задача имеет только один статус
2. **Status определяет агента** - название агента в статусе показывает, кто должен работать
3. **Обязательное обновление** - статус ОБЯЗАТЕЛЬНО обновляется при каждом переходе
4. **Комментарий при передаче** - при передаче задачи ОБЯЗАТЕЛЬНО добавляется комментарий

---

## Формат статусов

### Формат: `{Agent Name} - {State}`

**Агенты:**
- Idea Writer
- Architect
- API Designer
- Database
- Backend
- Network
- Security
- DevOps
- Performance
- UE5
- UI/UX
- Content Writer
- QA
- Game Balance
- Release
- Stats

**Состояния (States):**

| State | Описание | Когда использовать |
|-------|----------|-------------------|
| **Todo** | Задача готова к началу работы | Задача передана агенту, ждет начала работы |
| **In Progress** | Задача в активной работе | Агент начал работу над задачей |
| **Blocked** | Задача заблокирована | Ожидание ответа, зависимость, технические проблемы |
| **Review** | Задача на проверке | Внутренняя ревизия перед передачей следующему агенту |
| **Returned** | Задача возвращена | Задача возвращена предыдущему агенту из-за проблем |
| **Done** | Задача завершена | Финальный статус, работа полностью завершена |

**Универсальные статусы:**
- `Todo` - универсальный статус для новых задач (без указания агента)
- `Done` - универсальный статус для завершенных задач

---

## Жизненный цикл задачи

### Типичный workflow задачи

```
1. Todo (новая задача)
   ↓
2. Idea Writer - Todo
   ↓
3. Idea Writer - In Progress
   ↓
4. Architect - Todo (передача)
   ↓
5. Architect - In Progress
   ↓
6. API Designer - Todo (передача)
   ↓
7. API Designer - In Progress
   ↓
8. Backend - Todo (передача)
   ↓
... (и так далее)
   ↓
N. Done (завершение)
```

### Возможные переходы

```
Todo → {Agent} - Todo → {Agent} - In Progress
                                ↓
                    ┌───────────┴───────────┐
                    ↓                       ↓
        {Agent} - Blocked      {Agent} - Review
                    ↓                       ↓
        {Agent} - In Progress  {NextAgent} - Todo
                                            ↓
                                    {NextAgent} - In Progress
                                            ↓
                                    ... → Done
```

### Возврат задачи

```
{Agent} - In Progress → {CorrectAgent} - Returned
                                 ↓
                    {CorrectAgent} - Todo (после исправления)
                                 ↓
                    {CorrectAgent} - In Progress
```

---

## Передача задач между агентами

### Правила передачи

1. **При завершении работы:**
   - Обновить статус: `{MyAgent} - In Progress` → `{NextAgent} - Todo`
   - Добавить комментарий к Issue с описанием выполненной работы

2. **При возврате задачи:**
   - Обновить статус: `{MyAgent} - In Progress` → `{CorrectAgent} - Returned`
   - Добавить комментарий с описанием проблемы

3. **При блокировке:**
   - Обновить статус: `{MyAgent} - In Progress` → `{MyAgent} - Blocked`
   - Добавить комментарий с описанием причины блокировки

### Стандартные маршруты передачи

#### Системные задачи (Idea Writer → Architect → API Designer → Backend → ...)

```
Idea Writer → Architect → Database/API Designer → Backend → Network → Security → DevOps → UE5 → QA → Release → Done
```

#### Контентные квесты

```
Idea Writer → Content Writer → Backend (импорт в БД) → QA → Release → Done
```

#### UI/UX задачи

```
Idea Writer → UI/UX Designer → UE5 → QA → Release → Done
```

### Определение следующего агента

Каждый агент должен знать, кому передавать задачу после завершения:

| Текущий агент | Следующий агент (системные) | Следующий агент (контент) |
|--------------|------------------------------|---------------------------|
| Idea Writer | Architect / UI/UX / Content Writer | - |
| Architect | Database / API Designer | - |
| Database | API Designer | - |
| API Designer | Backend | - |
| Backend | Network (системные) / QA (контент) | QA |
| Network | Security | - |
| Security | DevOps | - |
| DevOps | UE5 | - |
| UE5 | QA | - |
| QA | Game Balance / Release | Release |
| Game Balance | Release | - |
| Release | Done | Done |

---

## Список всех агентов и их статусов

### Полный список статусов

**Универсальные:**
- `Todo` - новая задача
- `Done` - завершенная задача

**Idea Writer:**
- `Idea Writer - Todo`
- `Idea Writer - In Progress`
- `Idea Writer - Blocked`
- `Idea Writer - Review`
- `Idea Writer - Returned`

**Architect:**
- `Architect - Todo`
- `Architect - In Progress`
- `Architect - Blocked`
- `Architect - Review`
- `Architect - Returned`

**API Designer:**
- `API Designer - Todo`
- `API Designer - In Progress`
- `API Designer - Blocked`
- `API Designer - Review`
- `API Designer - Returned`

**Database:**
- `Database - Todo`
- `Database - In Progress`
- `Database - Blocked`
- `Database - Review`
- `Database - Returned`

**Backend:**
- `Backend - Todo`
- `Backend - In Progress`
- `Backend - Blocked`
- `Backend - Review`
- `Backend - Returned`

**Network:**
- `Network - Todo`
- `Network - In Progress`
- `Network - Blocked`
- `Network - Review`
- `Network - Returned`

**Security:**
- `Security - Todo`
- `Security - In Progress`
- `Security - Blocked`
- `Security - Review`
- `Security - Returned`

**DevOps:**
- `DevOps - Todo`
- `DevOps - In Progress`
- `DevOps - Blocked`
- `DevOps - Review`
- `DevOps - Returned`

**Performance:**
- `Performance - Todo`
- `Performance - In Progress`
- `Performance - Blocked`
- `Performance - Review`
- `Performance - Returned`

**UE5:**
- `UE5 - Todo`
- `UE5 - In Progress`
- `UE5 - Blocked`
- `UE5 - Review`
- `UE5 - Returned`

**UI/UX:**
- `UI/UX - Todo`
- `UI/UX - In Progress`
- `UI/UX - Blocked`
- `UI/UX - Review`
- `UI/UX - Returned`

**Content Writer:**
- `Content Writer - Todo`
- `Content Writer - In Progress`
- `Content Writer - Blocked`
- `Content Writer - Review`
- `Content Writer - Returned`

**QA:**
- `QA - Todo`
- `QA - In Progress`
- `QA - Blocked`
- `QA - Review`
- `QA - Returned`

**Game Balance:**
- `Game Balance - Todo`
- `Game Balance - In Progress`
- `Game Balance - Blocked`
- `Game Balance - Review`
- `Game Balance - Returned`

**Release:**
- `Release - Todo`
- `Release - In Progress`
- `Release - Blocked`
- `Release - Review`

**Stats:**
- `Stats - Todo`
- `Stats - In Progress`
- `Stats - Blocked`
- `Stats - Review`
- `Stats - Returned`

### ID статусов

Все ID статусов находятся в `.cursor/GITHUB_PROJECT_CONFIG.md`

---

## Техническая реализация

### Обновление статуса

**Параметры:**
- `owner_type: 'user'`
- `owner: 'gc-lover'`
- `project_number: 1`
- `status_field_id: 239690516`

**Шаги:**

1. **Получить item_id из Project:**
```javascript
const items = await mcp_github_list_project_items({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  query: 'Status:"{Agent} - Todo"'
});
const project_item_id = items.items[0].id;  // внутренний ID для API
const issue_number = items.items[0].content.number;  // номер Issue для комментариев
```

2. **Обновить статус:**
```javascript
await mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,
  updated_field: {
    id: 239690516,  // STATUS_FIELD_ID (число!)
    value: '02b1119e'  // ID статуса из GITHUB_PROJECT_CONFIG.md
  }
});
```

3. **Добавить комментарий:**
```javascript
await mcp_github_add_issue_comment({
  owner: 'gc-lover',
  repo: 'necpgame-monorepo',
  issue_number: issue_number,  // номер Issue, не item_id!
  body: 'OK Work ready. Handed off to {NextAgent}\n\nIssue: #' + issue_number
});
```

### Получение ID статусов

Если нужного статуса нет в `.cursor/GITHUB_PROJECT_CONFIG.md`:

```javascript
const fields = await mcp_github_list_project_fields({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1
});

const statusField = fields.fields.find(f => f.id === 239690516);
const option = statusField.options.find(o => o.name === '{Agent} - In Progress');
const optionId = option.id;  // использовать в value
```

### Важные замечания

WARNING **КРИТИЧЕСКИ ВАЖНО:**
- `item_id` (project_item_id) - используется ТОЛЬКО для API вызовов
- Номер Issue (`#123`) - используется в комментариях, коммитах, сообщениях
- Никогда не показывай пользователю `item_id` - всегда используй номер Issue
- `id` поля - число (239690516), не строка
- `value` - id опции статуса (строка), из констант в GITHUB_PROJECT_CONFIG.md

---

## Примеры

### Пример 1: Передача от Backend к QA

```javascript
// 1. Найти задачу
const items = await mcp_github_list_project_items({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  query: 'Status:"Backend - In Progress"'
});

const task = items.items[0];
const project_item_id = task.id;
const issue_number = task.content.number;

// 2. Обновить статус
await mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,
  updated_field: {
    id: 239690516,
    value: '86ca422e'  // QA - Todo
  }
});

// 3. Добавить комментарий
await mcp_github_add_issue_comment({
  owner: 'gc-lover',
  repo: 'necpgame-monorepo',
  issue_number: issue_number,
  body: `OK Backend implementation ready. Handed off to QA

- All endpoints implemented
- Tests passing
- Ready for testing

Issue: #${issue_number}`
});
```

### Пример 2: Возврат задачи

```javascript
// 1. Найти задачу
const items = await mcp_github_list_project_items({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  query: 'Status:"Backend - In Progress"'
});

const task = items.items[0];
const project_item_id = task.id;
const issue_number = task.content.number;

// 2. Обновить статус (вернуть к API Designer)
await mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,
  updated_field: {
    id: 239690516,
    value: 'd0352ed3'  // API Designer - Returned
  }
});

// 3. Добавить комментарий
await mcp_github_add_issue_comment({
  owner: 'gc-lover',
  repo: 'necpgame-monorepo',
  issue_number: issue_number,
  body: `WARNING **Task returned: Missing OpenAPI specification**

**Missing:**
- OpenAPI spec for endpoint /api/v1/users/{id}
- Response schema for error cases

**Correct agent:** API Designer

**Status updated:** \`API Designer - Returned\`

Issue: #${issue_number}`
});
```

### Пример 3: Блокировка задачи

```javascript
// 1. Обновить статус
await mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,
  updated_field: {
    id: 239690516,
    value: '504999e1'  // Backend - Blocked
  }
});

// 2. Добавить комментарий
await mcp_github_add_issue_comment({
  owner: 'gc-lover',
  repo: 'necpgame-monorepo',
  issue_number: issue_number,
  body: `🔒 **Task blocked: Waiting for database migration**

**Reason:** Database migration #456 must be completed first

**Blocked by:** Issue #456

Issue: #${issue_number}`
});
```

---

## Чек-лист при передаче задачи

### Перед передачей задачи

- [ ] Работа полностью завершена
- [ ] Все требования из Issue выполнены
- [ ] Критерии приемки выполнены
- [ ] Код/документы соответствуют стандартам
- [ ] Коммиты созданы с префиксом агента
- [ ] Файлы содержат `Issue: #123` в начале

### При передаче задачи

- [ ] Статус обновлен: `{MyAgent} - In Progress` → `{NextAgent} - Todo`
- [ ] Добавлен комментарий с описанием выполненной работы
- [ ] Указан номер Issue в комментарии
- [ ] Указан PR (если применимо)

### При возврате задачи

- [ ] Статус обновлен: `{MyAgent} - In Progress` → `{CorrectAgent} - Returned`
- [ ] Добавлен комментарий с описанием проблемы
- [ ] Указано, что отсутствует/неправильно
- [ ] Указан правильный агент для исправления

---

## Ссылки

- [AGENT_COMMON_RULES.md](./AGENT_COMMON_RULES.md) - общие правила для агентов
- [GITHUB_PROJECT_CONFIG.md](./GITHUB_PROJECT_CONFIG.md) - конфигурация проекта и ID статусов
- [AGENT_TASK_RETURN.md](./AGENT_TASK_RETURN.md) - правила возврата задач
- [AGENT_TASK_DISCOVERY.md](./AGENT_TASK_DISCOVERY.md) - поиск задач

---

**Последнее обновление:** 2025-01-XX
