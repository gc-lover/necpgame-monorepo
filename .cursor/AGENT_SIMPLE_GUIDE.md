# [TARGET] Простое руководство для агентов

## [SYMBOL] Что такое Status, Agent, Type и Check?

— **Status** — стадия задачи: `Todo`, `In Progress`, `Review`, `Blocked`, `Returned`, `Done`.

- **Agent** - кто отвечает сейчас: `Idea`, `Content`, `Backend`, `Architect`, `API`, `DB`, `QA`, `Performance`,
  `Security`, `Network`, `DevOps`, `UI/UX`, `UE5`, `GameBalance`, `Release`.
- **Type** - тип технической задачи: `API`, `MIGRATION`, `DATA`, `BACKEND`, `UE5`.
- **Check** - проверка выполнения: `0` (не проверялась), `1` (проверена).

Examples:

- `Status: Todo + Agent: Backend + Type: API + Check: 0` → API задача для Backend, не проверялась.
- `Status: In Progress + Agent: Backend + Type: BACKEND + Check: 1` → Backend работает над кодом, задача проверена.
- `Status: Todo + Agent: QA + Type: DATA + Check: 1` → QA тестирует импорт данных.

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

### 2️⃣ ВЗЯТЬ задачу = СРАЗУ обновить статус, тип и проверку

**Используй скрипт для автоматического обновления полей:**

```bash
# Автоматически обновляет Status + Agent + Type + Check
python scripts/update-github-fields.py --item-id {item_id} --type {TYPE} --check 0
```

**ИЛИ через MCP (если скрипт недоступен):**

```javascript
mcp_github_update_project_item({
    owner_type: 'user',
    owner: 'gc-lover',
    project_number: 1,
    item_id: project_item_id,  // из list_project_items
    updated_field: [
        {id: 239690516, value: '83d488e7'}, // Status: In Progress
        {id: 243899542, value: '{id_моего_агента}'}, // Agent: из GITHUB_PROJECT_CONFIG.md
        {id: '246469155', value: '{type_option_id}'}, // Type: API/MIGRATION/DATA/BACKEND/UE5
        {id: '246468990', value: '22932cc7'} // Check: 0 (не проверялась)
    ]
});
```

**Правила простановки Type:**

- **API**: `--type API` - Создание OpenAPI спецификаций
- **MIGRATION**: `--type MIGRATION` - Создание БД миграций
- **DATA**: `--type DATA` - Импорт данных в БД
- **BACKEND**: `--type BACKEND` - Написание Go кода
- **UE5**: `--type UE5` - Разработка в UE5

### 3️⃣ ПРОВЕРИТЬ задачу = Обновить Check поле

**После анализа выполнения задачи:**

```bash
# Обнови Check на 1 (задача проверена)
python scripts/update-github-fields.py --item-id {item_id} --check 1
```

**ИЛИ через MCP:**

```javascript
mcp_github_update_project_item({
    owner_type: 'user',
    owner: 'gc-lover',
    project_number: 1,
    item_id: project_item_id,
    updated_field: [
        {id: '246468990', value: '4e8cf8f5'} // Check: 1 (проверена)
    ]
});
```

**Логика проверки:**

- Если задача **сделана** → передай следующему агенту
- Если задача **не сделана** → выполняй работу
- **ВСЕГДА** ставь Check = 1 после проверки!

### 4️⃣ РАБОТАТЬ

    - **НЕ МУСОРИТЬ В КОРНЕ ПРОЕКТА!**

- **OpenAPI:** `proto/openapi/{domain}/` (enterprise-grade домены!)
- **Go сервисы:** `services/{service}-go/`
- **Контент:** `knowledge/canon/` (YAML квесты, лор)
- **Скрипты:** `scripts/` (автоматизация, оптимизация)

**Оптимизированные скрипты (ТОЛЬКО PYTHON!):**

- `python scripts/validate-domains-openapi.py --domain {domain}` - валидация OpenAPI домена перед генерацией
- `python scripts/generate-all-domains-go.py --parallel 3 --memory-pool` - параллельная генерация всех enterprise-grade сервисов
- `python scripts/batch-optimize-openapi-struct-alignment.py proto/openapi/{domain}/main.yaml` - оптимизация struct alignment для performance
- `python scripts/reorder-liquibase-columns.py infrastructure/liquibase/migrations/{file}.sql` - оптимизация порядка колонок БД
- `python scripts/validate-all-migrations.py` - валидация всех enterprise-grade миграций
- `python scripts/update-github-fields.py --item-id {id} --type {TYPE} --check {0|1}` - управление полями GitHub Project

**КРИТИЧНО:** Forbidden создавать новые .sh/.ps1/.bat скрипты!

- [OK] Используй: `scripts/core/base_script.py` (базовый фреймворк)
- [ERROR] Forbidden: .sh, .ps1, .bat, .cmd, .pl, .rb, .js
- Git hooks блокируют коммиты с запрещенными типами скриптов
    - Корень только для: `README.md`, `CHANGELOG*.md`, основные конфиги
        - НЕ создавать промежуточные/тестовые файлы в корне!
        - **OpenAPI ДОМЕНЫ (КРИТИЧНО! Новая enterprise-grade архитектура):**
            - Все enterprise-grade домены (see .cursor/DOMAIN_REFERENCE.md)
            - Основные: system, specialized, social, economy, world domains
            - Специализированные: arena, cosmetic, cyberpunk, faction, etc.
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

### 5️⃣ ЗАКОНЧИТЬ = Передать следующему

```javascript
// Меняй In Progress → Status: Todo + Agent: {СледующийАгент}
mcp_github_update_project_item({
    owner_type: 'user',
    owner: 'gc-lover',
    project_number: 1,
    item_id: project_item_id,
    updated_field: [
        {id: 239690516, value: 'f75ad846'}, // Status: Todo
        {id: 243899542, value: '{id_следующего_агента}'}, // Agent: next
        {id: TYPE_FIELD_ID, value: '{new_type_option_id}'}, // Type: обнови если изменился тип работы
        {id: CHECK_FIELD_ID, value: '1'} // Check: остается 1 (уже проверена)
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

1. **СРАЗУ** обновляй статус, Type и Check при взятии задачи
2. **ОБЯЗАТЕЛЬНО** проверяй выполнение задачи перед работой (Check = 1)
3. **ВСЕГДА** обновляй Agent при передаче (назначай следующего)
4. **ВСЕГДА** добавляй комментарий при передаче задачи
5. **ИСПОЛЬЗУЙ** константы из GITHUB_PROJECT_CONFIG.md (включая TYPE и CHECK)
6. **ПИШИ** номер Issue (#123) в комментариях, НЕ item_id
7. **ПРОСТАВЛЯЙ** правильный Type исходя из типа работы

### [ERROR] НЕ ДЕЛАЙ:

1. Не работай без обновления всех полей (Status, Agent, Type, Check)
2. Не передавай задачу без комментария
3. Не используй item_id в комментариях (только #123)
4. Не создавай файлы >1000 строк
5. **НЕ** ставь Check = 1 без реальной проверки выполнения задачи

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

