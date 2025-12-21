# [TARGET] Простое руководство для агентов

## [SYMBOL] Что такое Status и Agent?

— **Status** — стадия задачи: `Todo`, `In Progress`, `Review`, `Blocked`, `Returned`, `Done`.
- **Agent** - кто отвечает сейчас: `Idea`, `Content`, `Backend`, `Architect`, `API`, `DB`, `QA`, `Performance`,
  `Security`, `Network`, `DevOps`, `UI/UX`, `UE5`, `GameBalance`, `Release`.

Examples:

- `Status: Todo + Agent: Backend` → Backend должен взять.
- `Status: In Progress + Agent: Backend` → Backend работает.
- `Status: Todo + Agent: QA` → задача передана QA.

---

## [SYMBOL] Простой алгоритм работы

### 1️⃣ НАЙТИ задачу

```javascript
mcp_github_list_project_items({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  query: 'Agent:"{МойАгент}" Status:"Todo"'  // Например: Agent:"Backend"
});
```

### 2️⃣ ВЗЯТЬ задачу = СРАЗУ обновить статус

```javascript
// СРАЗУ меняй Todo → In Progress
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,  // из list_project_items
  updated_field: [
    { id: 239690516, value: '83d488e7' }, // Status: In Progress
    { id: 243899542, value: '{id_моего_агента}' } // Agent: из GITHUB_PROJECT_CONFIG.md
  ]
});
```

### 3️⃣ РАБОТАТЬ

    - **НЕ МУСОРИТЬ В КОРНЕ ПРОЕКТА!**
- **OpenAPI:** `proto/openapi/{domain}/` (enterprise-grade домены!)
- **Go сервисы:** `services/{service}-go/`
- **Контент:** `knowledge/canon/` (YAML квесты, лор)
- **Скрипты:** `scripts/` (автоматизация, оптимизация)

**Оптимизированные скрипты (ТОЛЬКО PYTHON!):**
- `python scripts/validate-domains-openapi.py` - валидация OpenAPI доменов
- `python scripts/generate-all-domains-go.py` - генерация enterprise-grade сервисов
- `python scripts/batch-optimize-openapi-struct-alignment.py` - оптимизация структур OpenAPI
- `python scripts/reorder-liquibase-columns.py` - оптимизация колонок БД

**КРИТИЧНО:** Forbidden создавать новые .sh/.ps1/.bat скрипты!
- [OK] Используй: `scripts/core/base_script.py` (базовый фреймворк)
- [ERROR] Forbidden: .sh, .ps1, .bat, .cmd, .pl, .rb, .js
- Git hooks блокируют коммиты с запрещенными типами скриптов
    - Корень только для: `README.md`, `CHANGELOG*.md`, основные конфиги
    - НЕ создавать промежуточные/тестовые файлы в корне!
    - **OpenAPI ДОМЕНЫ (КРИТИЧНО! Новая enterprise-grade архитектура):**
      - `system-domain/` (553 файла) - инфраструктура, сервисы
      - `specialized-domain/` (157 файлов) - игровые механики
      - `social-domain/` (91 файл) - социальные функции
      - `economy-domain/` (31 файл) - экономика
      - `world-domain/` (57 файлов) - игровой мир
      - Остальные 10 доменов для специализированных функций
    - **СТРОГО соблюдать структуру knowledge/ (КРИТИЧНО!):**
      - `knowledge/analysis/` - аналитика и исследования
      - `knowledge/canon/` - канонический лор (YAML квесты, NPC, диалоги)
      - `knowledge/content/` - игровые ассеты (враги, интерактивы, квесты)
      - `knowledge/design/` - дизайн-документы UI/UX
      - `knowledge/implementation/` - техническая реализация
      - `knowledge/mechanics/` - игровые механики
      - **ЗАПРЕЩЕНО:** создавать файлы напрямую в `knowledge/` (только в поддиректориях!)
- Создавай файлы с `# Issue: #123` в начале
- Коммить с префиксом `[agent]`
- Пример: `[backend] feat: добавить API`

### 4️⃣ ЗАКОНЧИТЬ = Передать следующему

```javascript
// Меняй In Progress → Status: Todo + Agent: {СледующийАгент}
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,
  updated_field: [
    { id: 239690516, value: 'f75ad846' }, // Status: Todo
    { id: 243899542, value: '{id_следующего_агента}' } // Agent: next
  ]
});

// ОБЯЗАТЕЛЬНО добавь комментарий
mcp_github_add_issue_comment({
  owner: 'gc-lover',
  repo: 'necpgame-monorepo',
  issue_number: issue_number,  // из list_project_items: content.number
  body: '[OK] Work ready. Handed off to {NextAgent}\n\nIssue: #' + issue_number
});
```

---

## [SYMBOL]️ Карта передачи задач

```
Системные задачи (Agent поле):
Idea → Architect → DB → API → Backend → Network → Security → DevOps → UE5 → QA → Release

Контент-квесты (Agent поле):
Idea → Content → Backend (импорт) → QA → Release

UI задачи (Agent поле):
Idea → UI/UX → UE5 → QA → Release

Status всегда: Todo → In Progress → Review/Returned/Blocked → Todo (следующий агент) → Done
```

**Детали контентного workflow:** `.cursor/CONTENT_WORKFLOW.md`

---

## [SYMBOL] Конфигурация

**Все ID статусов:** `.cursor/GITHUB_PROJECT_CONFIG.md`

**Основные параметры:**

- owner_type: `'user'`
- owner: `'gc-lover'`
- project_number: `1`
- status_field_id: `239690516`
- agent_field_id: `243899542`

**Контентный workflow:** `.cursor/CONTENT_WORKFLOW.md`

**Для Backend Developer:**

- Code gen: `ogen` (typed handlers, 90% faster)
- Гайд: `.cursor/ogen/README.md`
- Reference: `services/combat-combos-service-ogen-go/`
- Контентный workflow: `.cursor/CONTENT_WORKFLOW.md`

---

## [FORBIDDEN] EMOJI AND SPECIAL CHARACTERS ЗАПРЕТ

**КРИТИЧНО:** Запрещено использовать эмодзи и специальные Unicode символы в коде!

### Почему запрещено:
- [FORBIDDEN] Ломают выполнение скриптов на Windows
- [FORBIDDEN] Могут вызывать ошибки в терминале
- [FORBIDDEN] Создают проблемы с кодировкой
- [FORBIDDEN] Нарушают совместимость между ОС

### Что use вместо:
- [OK] `:smile:` вместо [EMOJI]
- [OK] `[FORBIDDEN]` вместо [FORBIDDEN]
- [OK] `[OK]` вместо [OK]
- [OK] `[ERROR]` вместо [ERROR]
- [OK] `[WARNING]` вместо [WARNING]

### Автоматическая проверка:
- Pre-commit hooks блокируют коммиты с эмодзи
- Git hooks проверяют staged файлы
- Исключения: `.cursor/rules/*` (документация), `.githooks/*`

---

## [ALERT] Важные правила

### [OK] ДЕЛАЙ:

1. **СРАЗУ** обновляй статус при взятии задачи (Todo → In Progress)
2. **ВСЕГДА** обновляй Agent при передаче (назначай следующего)
3. **ВСЕГДА** добавляй комментарий при передаче задачи
4. **ИСПОЛЬЗУЙ** константы из GITHUB_PROJECT_CONFIG.md
5. **ПИШИ** номер Issue (#123) в комментариях, НЕ item_id

### [ERROR] НЕ ДЕЛАЙ:

1. Не работай без обновления статуса
2. Не передавай задачу без комментария
3. Не используй item_id в комментариях (только #123)
4. Не создавай файлы >1000 строк

---

## [SEARCH] Различие ID

| Что             | Где используется                     | Пример      |
|-----------------|--------------------------------------|-------------|
| **item_id**     | ТОЛЬКО в API вызовах                 | `140861824` |
| **Issue номер** | В комментариях, коммитах, сообщениях | `#123`      |

**Правило:** Пользователь видит `#123`, API использует `item_id`

---

## [NOTE] Специфичные команды

Смотри `.cursor/commands/` для специфичных проверок:

- `backend-check-openapi.md` - проверить OpenAPI перед началом
- `backend-import-quest-to-db.md` - импорт квестов
- `qa-check-functionality-ready.md` - проверка готовности к QA
- И т.д.

---

## [SYMBOL] Детали по агентам

Смотри `.cursor/rules/agent-{name}.mdc` для деталей конкретного агента

---

**Это всё что нужно знать! Следуй этим 4 шагам: Найти → Взять → Работать → Передать**

**Контентный workflow:** `.cursor/CONTENT_WORKFLOW.md`

