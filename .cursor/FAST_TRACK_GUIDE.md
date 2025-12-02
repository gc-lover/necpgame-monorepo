# ⚡ Fast Track для простых задач

**Ускоренный конвейер для небольших изменений**

## 🎯 Что такое Fast Track?

**Fast Track** - это сокращенный путь для простых задач, которые не требуют полного конвейера из 11 этапов.

### Обычный путь (11 этапов):
```
Idea → Architect → Database → API → Backend → Network → Security → 
DevOps → UE5 → QA → Release
```

### Fast Track (3-4 этапа):
```
Todo → Backend → QA → Release
```
или
```
Todo → UE5 → QA → Release
```

## ✅ Критерии для Fast Track

**Задача подходит для Fast Track если:**

### Размер изменений
- [ ] **<100 строк кода** (изменения, не считая тесты)
- [ ] **Один файл или модуль** (не затрагивает множество компонентов)
- [ ] **<2 часов работы** (реальное время разработки)

### Область изменений
- [ ] **НЕ требует архитектурных изменений**
- [ ] **НЕ требует изменений в БД** (новые таблицы, миграции)
- [ ] **НЕ требует изменений OpenAPI** (новые endpoints или схемы)
- [ ] **НЕ требует изменений в протоколе** (proto файлы, сетевая часть)
- [ ] **НЕ затрагивает security** (auth, permissions, encryption)

### Тип задачи
- [ ] **Bugfix** (исправление бага в существующем коде)
- [ ] **Рефакторинг** (улучшение кода без изменения функциональности)
- [ ] **Мелкая фича** (добавление простой функции в существующий модуль)
- [ ] **UI/UX улучшение** (мелкие изменения интерфейса)
- [ ] **Документация** (обновление README, комментариев)
- [ ] **Конфигурация** (изменение параметров, констант)

### Риски
- [ ] **Низкий риск** (не критичная функциональность)
- [ ] **Легко откатить** (изменения изолированы)
- [ ] **НЕ влияет на production критичные компоненты**

## 🚀 Как активировать Fast Track

### 1. Добавить label на Issue

```javascript
// При создании или обновлении Issue
labels: ['fast-track']
```

### 2. Проверить критерии

**ВАЖНО:** Не все задачи подходят для Fast Track!

Проверь каждый критерий выше. Если хотя бы один **НЕ выполнен** → используй обычный конвейер.

### 3. Определить путь

**Backend изменения:**
```
Todo → Backend - Todo → QA - Todo → Release - Todo
```

**Client изменения (UE5):**
```
Todo → UE5 - Todo → QA - Todo → Release - Todo
```

**Контент-квест (YAML):**
```
Todo → Content Writer - Todo → Backend - Todo (import) → QA - Todo → Release - Todo
```
**Примечание:** Контент-квесты уже используют короткий путь, Fast Track для них не нужен.

## 📋 Fast Track Workflow

### Backend Fast Track

```javascript
// 1. Взять задачу
mcp_github_list_project_items({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  query: 'Status:"Todo" label:fast-track label:backend'
});

// 2. Начать работу
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,
  updated_field: {
    id: 239690516,
    value: '7bc9d20f'  // STATUS_OPTIONS['Backend - In Progress']
  }
});

// 3. Реализовать изменения
// - Изменить код
// - Написать/обновить тесты
// - Проверить: go test ./...
// - Коммит: [backend] fix: описание

// 4. Передать QA напрямую (минуя Network, Security, DevOps)
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,
  updated_field: {
    id: 239690516,
    value: '86ca422e'  // STATUS_OPTIONS['QA - Todo']
  }
});

mcp_github_add_issue_comment({
  owner: 'gc-lover',
  repo: 'necpgame-monorepo',
  issue_number: issue_number,
  body: '✅ **Fast Track: Backend changes ready**\n\n' +
        '**Changes:**\n' +
        '- Fixed bug in companion service\n' +
        '- Updated unit tests\n' +
        '- 50 lines changed\n\n' +
        '**Fast Track reason:**\n' +
        '- Small bugfix (<100 lines)\n' +
        '- No architecture changes\n' +
        '- No DB/API/Protocol changes\n' +
        '- Low risk\n\n' +
        'Handed off to QA for fast testing\n\n' +
        'Issue: #' + issue_number
});
```

### UE5 Fast Track

```javascript
// Аналогично, но:
// - Status: UE5 - In Progress
// - Коммит: [ue5] fix: описание
// - Передать: QA - Todo
```

## ⚠️ Когда НЕ использовать Fast Track

### ❌ НЕ подходит:
- Новые features с архитектурой
- Изменения в БД (новые таблицы, индексы)
- Изменения в OpenAPI (новые endpoints)
- Изменения в security (auth, permissions)
- Изменения в протоколе (proto файлы)
- Большие рефакторинги (>100 строк)
- Критичные компоненты (auth, payment, etc.)

### ✅ Используй обычный конвейер:
```
Idea → Architect → Database → API → Backend → Network → Security → 
DevOps → UE5 → QA → Release
```

## 📊 Метрики Fast Track

**Отслеживай эффективность:**

```markdown
## Fast Track метрики

| Метрика | Цель | Текущее |
|---------|------|---------|
| Время выполнения | <1 день | - |
| % успешных (без возврата) | >90% | - |
| % задач на Fast Track | 20-30% | - |
```

**Если Fast Track не работает (много возвратов):**
- Проверь критерии отбора
- Возможно задачи слишком сложные
- Уточни критерии

## 🎯 Примеры Fast Track задач

### ✅ Хорошие примеры:

**1. Bugfix:**
```
Исправить опечатку в error message
- 1 файл, 5 строк
- Нет изменений в логике
```

**2. Рефакторинг:**
```
Извлечь магическое число в константу
- 1 файл, 10 строк
- Нет изменений в поведении
```

**3. Мелкая фича:**
```
Добавить поле "last_seen" к API response
- 1 файл, 20 строк
- Просто добавить поле из БД (уже существует)
```

**4. UI улучшение:**
```
Изменить цвет кнопки
- 1 файл, 3 строки CSS
- Нет изменений в логике
```

### ❌ Плохие примеры:

**1. НЕ Fast Track:**
```
Добавить новую таблицу companions
- Нужна архитектура
- Нужны миграции БД
- Нужен OpenAPI
→ Используй обычный конвейер
```

**2. НЕ Fast Track:**
```
Изменить алгоритм аутентификации
- Критичный компонент (security)
- Нужна проверка Security Agent
→ Используй обычный конвейер
```

## 🔧 Настройка Fast Track label

**Добавить label в репозиторий (если нет):**

```javascript
// Создать label через GitHub API
// (обычно делается вручную в GitHub UI или через admin скрипты)
```

**Label свойства:**
- **Name:** `fast-track`
- **Color:** `#0E8A16` (зеленый)
- **Description:** "Small changes, accelerated pipeline (3-4 steps instead of 11)"

## 📈 SLA для Fast Track

**Ускоренные сроки:**

| Этап | Обычный SLA | Fast Track SLA |
|------|-------------|----------------|
| Backend/UE5 | 3 дня | **4 часа** |
| QA | 1 день | **2 часа** |
| Release | 1 день | **1 час** |
| **ИТОГО** | 5 дней | **<1 день** |

**Цель:** Завершить Fast Track задачу за **рабочий день** (8 часов).

## ✅ Чек-лист перед Fast Track

Перед добавлением `fast-track` label, проверь:

- [ ] Изменения <100 строк
- [ ] Один модуль/файл
- [ ] НЕ требует архитектуры
- [ ] НЕ требует БД изменений
- [ ] НЕ требует OpenAPI изменений
- [ ] НЕ затрагивает security
- [ ] Низкий риск
- [ ] Легко откатить

**Если все ✅ → добавляй `fast-track` label и работай!**

**Если хоть один ❌ → используй обычный конвейер.**

