# Шаблоны комментариев при передаче Issues

**Стандартные шаблоны комментариев для передачи задач между агентами**

## Основные шаблоны

### ✅ Передача задачи следующему агенту

```markdown
✅ {Work type} ready. Handed off to {NextAgent}

{Optional: Краткое описание выполненной работы}

{Optional: Ссылки на созданные файлы/PR}

PR: #{number} (if applicable)
Issue: #{number}
```

**Примеры:**

```markdown
✅ Architecture ready. Handed off to Database

- Architecture document created
- Components defined
- Database requirements specified

Issue: #123
```

```markdown
✅ OpenAPI spec ready. Handed off to Backend

- All endpoints described
- Request/Response schemas defined
- Spec validated

PR: #456
Issue: #123
```

```markdown
✅ Backend implementation ready. Handed off to Network

- All endpoints implemented
- Tests passing
- Ready for network optimization

PR: #789
Issue: #123
```

---

### ⚠️ Возврат задачи предыдущему агенту

```markdown
⚠️ **Task returned: {reason}**

**Missing:**
- {what_is_missing_1}
- {what_is_missing_2}

**Issues found:**
- {issue_1}
- {issue_2}

**Correct agent:** {Agent Name}

**Status updated:** `{CorrectAgent} - Returned`

Issue: #{number}
```

**Примеры:**

```markdown
⚠️ **Task returned: Missing OpenAPI specification**

**Missing:**
- OpenAPI spec for endpoint /api/v1/users/{id}
- Response schema for error cases
- Authentication requirements

**Correct agent:** API Designer

**Status updated:** `API Designer - Returned`

Issue: #123
```

```markdown
⚠️ **Task returned: Architecture document incomplete**

**Missing:**
- Component interaction diagrams
- Data synchronization strategy
- Performance requirements

**Correct agent:** Architect

**Status updated:** `Architect - Returned`

Issue: #123
```

---

### 🔒 Блокировка задачи

```markdown
🔒 **Task blocked: {reason}**

**Reason:** {detailed_reason}

**Blocked by:** {Issue/PR/dependency}

**Next steps:** {what_needs_to_happen}

Issue: #{number}
```

**Примеры:**

```markdown
🔒 **Task blocked: Waiting for database migration**

**Reason:** Database migration #456 must be completed first

**Blocked by:** Issue #456

**Next steps:** Wait for migration completion, then continue

Issue: #123
```

```markdown
🔒 **Task blocked: Waiting for clarification**

**Reason:** Requirements unclear for endpoint behavior

**Blocked by:** Waiting for product owner response

**Next steps:** Need clarification on error handling strategy

Issue: #123
```

---

### 📝 Начало работы над задачей

```markdown
🟢 **Starting work on Issue #{number}**

**Plan:**
- {step_1}
- {step_2}
- {step_3}

**Estimated time:** {time_estimate}

Issue: #{number}
```

**Пример:**

```markdown
🟢 **Starting work on Issue #123**

**Plan:**
- Create OpenAPI specification
- Define all endpoints
- Add request/response schemas
- Validate spec

**Estimated time:** 2-3 hours

Issue: #123
```

---

### ✅ Завершение работы (для Release/Done)

```markdown
✅ **Task completed: Issue #{number}**

**Summary:**
- {completed_item_1}
- {completed_item_2}
- {completed_item_3}

**Status:** `Done`

Issue: #{number}
```

**Пример:**

```markdown
✅ **Task completed: Issue #123**

**Summary:**
- Feature implemented
- Tests passing
- Documentation updated
- Deployed to production

**Status:** `Done`

Issue: #123
```

---

## Специфичные шаблоны для агентов

### Idea Writer → Architect

```markdown
✅ Idea and concept ready. Handed off to Architect

- Idea document created
- Game mechanics described
- Lore and narrative defined

Issue: #{number}
```

### Architect → Database / API Designer

```markdown
✅ Architecture ready. Handed off to {Database/API Designer}

- Architecture document created
- Components defined
- Technical requirements specified

Issue: #{number}
```

### API Designer → Backend

```markdown
✅ OpenAPI spec ready. Handed off to Backend

- All endpoints described
- Request/Response schemas defined
- Spec validated with swagger-cli

PR: #{number}
Issue: #{number}
```

### Backend → Network (системные задачи)

```markdown
✅ Backend implementation ready. Handed off to Network

- All endpoints implemented
- Tests passing
- Ready for network optimization

PR: #{number}
Issue: #{number}
```

### Backend → QA (контент-квесты)

```markdown
✅ Backend ready (import completed). Handed off to QA

- Quest content imported to database
- API endpoints tested
- Ready for QA testing

Issue: #{number}
```

### Content Writer → Backend (импорт в БД)

```markdown
✅ Quest content ready. Handed off to Backend for DB import

- YAML file created and validated
- Lore and dialogues written
- Structure matches architecture

**Next step:** Import to database via POST /api/v1/gameplay/quests/content/reload

Issue: #{number}
```

### UE5 → QA

```markdown
✅ Client implementation ready. Handed off to QA

- UI components implemented
- API integration completed
- Game mechanics working

PR: #{number}
Issue: #{number}
```

### QA → Release

```markdown
✅ Testing complete. Handed off to Release

- All test cases passed
- Bugs documented and fixed
- Ready for release

Issue: #{number}
```

### QA → Backend / UE5 (возврат с багами)

```markdown
⚠️ **Task returned: Bugs found during testing**

**Bugs found:**
- Bug 1: {description}
- Bug 2: {description}

**Correct agent:** {Backend/UE5}

**Status updated:** `{Backend/UE5} - Returned`

Issue: #{number}
```

---

## Эмодзи для статусов

Используйте эмодзи для визуального выделения:

- ✅ - Успешно завершено / Передача
- ⚠️ - Предупреждение / Возврат
- 🔒 - Блокировка
- 🟢 - Начало работы
- 📝 - Заметка / План
- 🐛 - Баг
- 🔧 - Исправление
- 📚 - Документация
- 🚀 - Релиз

---

## Важные замечания

### Всегда указывайте:

1. **Номер Issue** в формате `Issue: #123`
2. **Номер PR** (если применимо) в формате `PR: #456`
3. **Статус** при возврате задачи: `Status updated: \`{Agent} - Returned\``

### Никогда не используйте:

- ❌ `item_id` (project_item_id) в комментариях
- ❌ Внутренние ID для API вызовов
- ❌ Ссылки на сгенерированные файлы без контекста

### Форматирование:

- Используйте Markdown для структурирования
- Выделяйте важную информацию жирным текстом
- Используйте списки для перечисления
- Код/технические термины в обратных кавычках

---

**См. также:**
- [STATUS_HANDOFF_GUIDE.md](./STATUS_HANDOFF_GUIDE.md) - руководство по статусам
- [AGENT_COMMON_RULES.md](./AGENT_COMMON_RULES.md) - общие правила
