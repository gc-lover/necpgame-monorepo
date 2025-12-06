# 🎯 Простое руководство для агентов

## 📋 Что такое Status?

**Status показывает КТО должен работать с задачей прямо сейчас**

```
"Backend - Todo" = Задача ДЛЯ Backend агента (он должен взять)
"Backend - In Progress" = Backend работает
"QA - Todo" = Задача передана QA (Backend закончил)
```

---

## 🔄 Простой алгоритм работы

### 1️⃣ НАЙТИ задачу

```javascript
mcp_github_list_project_items({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  query: 'Status:"{МойАгент} - Todo"'  // Например: "Backend - Todo"
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
  updated_field: {
    id: 239690516,  // STATUS_FIELD_ID
    value: '{id_опции}'  // из GITHUB_PROJECT_CONFIG.md
  }
});
```

### 3️⃣ РАБОТАТЬ

- Создавай файлы с `# Issue: #123` в начале
- Коммить с префиксом `[agent]`
- Пример: `[backend] feat: добавить API`

### 4️⃣ ЗАКОНЧИТЬ = Передать следующему

```javascript
// Меняй In Progress → {СледующийАгент} - Todo
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,
  updated_field: {
    id: 239690516,
    value: '{id_следующего_агента}'  // из GITHUB_PROJECT_CONFIG.md
  }
});

// ОБЯЗАТЕЛЬНО добавь комментарий
mcp_github_add_issue_comment({
  owner: 'gc-lover',
  repo: 'necpgame-monorepo',
  issue_number: issue_number,  // из list_project_items: content.number
  body: '✅ Work ready. Handed off to {NextAgent}\n\nIssue: #' + issue_number
});
```

---

## 🗺️ Карта передачи задач

```
Системные задачи:
Todo → Idea Writer → Architect → Database → API Designer → 
Backend → Network → Security → DevOps → UE5 → QA → Release → Done

Контент-квесты (canon/lore/quest):
Todo → Idea Writer → Content Writer → Backend (импорт) → QA → Release → Done

UI задачи:
Todo → Idea Writer → UI/UX Designer → UE5 → QA → Release → Done
```

---

## 📦 Конфигурация

**Все ID статусов:** `.cursor/GITHUB_PROJECT_CONFIG.md`

**Основные параметры:**
- owner_type: `'user'`
- owner: `'gc-lover'`
- project_number: `1`
- status_field_id: `239690516`

**Для Backend Developer:**
- Code gen: `ogen` (typed handlers, 90% faster)
- Гайд: `.cursor/ogen/README.md`
- Reference: `services/combat-combos-service-ogen-go/`

---

## 🚨 Важные правила

### ✅ ДЕЛАЙ:
1. **СРАЗУ** обновляй статус при взятии задачи (Todo → In Progress)
2. **ВСЕГДА** добавляй комментарий при передаче задачи
3. **ИСПОЛЬЗУЙ** константы из GITHUB_PROJECT_CONFIG.md
4. **ПИШИ** номер Issue (#123) в комментариях, НЕ item_id

### ❌ НЕ ДЕЛАЙ:
1. Не работай без обновления статуса
2. Не передавай задачу без комментария
3. Не используй item_id в комментариях (только #123)
4. Не создавай файлы >500 строк

---

## 🔍 Различие ID

| Что | Где используется | Пример |
|-----|------------------|---------|
| **item_id** | ТОЛЬКО в API вызовах | `140861824` |
| **Issue номер** | В комментариях, коммитах, сообщениях | `#123` |

**Правило:** Пользователь видит `#123`, API использует `item_id`

---

## 📝 Специфичные команды

Смотри `.cursor/commands/` для специфичных проверок:
- `backend-check-openapi.md` - проверить OpenAPI перед началом
- `backend-import-quest-to-db.md` - импорт квестов
- `qa-check-functionality-ready.md` - проверка готовности к QA
- И т.д.

---

## 🎓 Детали по агентам

Смотри `.cursor/rules/agent-{name}.mdc` для деталей конкретного агента

---

**Это всё что нужно знать! Следуй этим 4 шагам: Найти → Взять → Работать → Передать**

