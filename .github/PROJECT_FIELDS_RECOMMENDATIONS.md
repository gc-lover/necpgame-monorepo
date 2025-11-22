# Рекомендации по кастомным полям в GitHub Project

## Текущие поля

### Стандартные поля (уже есть)
- OK **Title** - название задачи
- OK **Assignees** - назначенные пользователи
- OK **Status** - Todo / In Progress / Done
- OK **Labels** - метки (agent:*, stage:*, priority:*)
- OK **Linked pull requests** - связанные PR
- OK **Milestone** - вехи проекта
- OK **Repository** - репозиторий
- OK **Reviewers** - ревьюеры
- OK **Parent issue** - родительская задача
- OK **Sub-issues progress** - прогресс подзадач

### Кастомные поля (уже есть)
- OK **Developmen Stage** - этап разработки (idea-writer, architect, ...)

## Рекомендации по дополнительным полям

### 1. "Assigned Agent" - НЕ НУЖНО ❌

**Почему не нужно:**
- Уже есть метки `agent:*` (agent:idea-writer, agent:backend, ...)
- Дублирование информации
- Метки более гибкие (можно несколько агентов)

**Альтернатива:** Используйте метки `agent:*`

### 2. "Priority" - УЖЕ ЕСТЬ OK

**Где:** В метках `priority-high`, `priority-medium`, `priority-low`

**Рекомендация:** Оставить как есть, метки достаточно

### 3. "Component" - УЖЕ ЕСТЬ OK

**Где:** В метках `backend`, `client`, `infrastructure`, `protocol`, `game-design`

**Рекомендация:** Оставить как есть

### 4. "Blocked By" - ПОЛЕЗНО WARNING

**Тип:** Linked Issues (multiple)

**Зачем:**
- Показывает блокирующие задачи
- Помогает понять зависимости
- Автоматически обновлять статус

**Рекомендация:** Добавить, если есть задачи с зависимостями

### 5. "Estimated Time" - ОПЦИОНАЛЬНО WARNING

**Тип:** Number (часы)

**Зачем:**
- Планирование времени
- Оценка сложности
- Метрики производительности агентов

**Рекомендация:** Добавить, если нужны метрики

### 6. "Actual Time" - ОПЦИОНАЛЬНО WARNING

**Тип:** Number (часы)

**Зачем:**
- Отслеживание реального времени
- Сравнение с оценкой
- Метрики эффективности

**Рекомендация:** Добавить вместе с "Estimated Time"

### 7. "Branch" - ПОЛЕЗНО OK

**Тип:** Text

**Зачем:**
- Быстрая ссылка на ветку
- Автоматическое заполнение при создании ветки
- Навигация к коду

**Рекомендация:** Добавить для удобства

### 8. "Last Agent Activity" - ОПЦИОНАЛЬНО WARNING

**Тип:** Date

**Зачем:**
- Отслеживание последней активности агента
- Поиск застрявших задач
- Метрики времени на этапе

**Рекомендация:** Добавить, если нужен мониторинг

## Итоговые рекомендации

### Обязательные поля (уже есть)
1. OK **Developmen Stage** - этап разработки
2. OK **Status** - статус задачи
3. OK **Labels** - все метки (агенты, приоритеты, компоненты)

### Рекомендуемые дополнительные поля

1. **Branch** (Text)
   - Автоматически заполняется при создании ветки
   - Формат: `feature/issue-{number}-{desc}`
   - Полезно для быстрой навигации

2. **Blocked By** (Linked Issues, multiple)
   - Если есть задачи с зависимостями
   - Помогает отслеживать блокировки

### Опциональные поля (для метрик)

3. **Estimated Time** (Number, hours)
4. **Actual Time** (Number, hours)
5. **Last Agent Activity** (Date)

## Вывод

**Минимальный набор (рекомендуется):**
- OK Developmen Stage (уже есть)
- OK Status (уже есть)
- OK Labels (уже есть)
- ➕ Branch (добавить)

**Расширенный набор (если нужны метрики):**
- Все из минимального набора
- ➕ Blocked By
- ➕ Estimated Time
- ➕ Actual Time
- ➕ Last Agent Activity

**НЕ нужно:**
- ❌ Assigned Agent (дублирует метки)
- ❌ Priority (уже в метках)
- ❌ Component (уже в метках)

## Автоматизация заполнения полей

### Workflow для автоматического заполнения Branch

```yaml
- name: Update Branch Field
  uses: actions/github-script@v7
  with:
    script: |
      const branchName = `feature/issue-${context.payload.issue.number}-${context.payload.issue.title.toLowerCase().replace(/[^a-z0-9]+/g, '-').substring(0, 50)}`;
      
      # Обновить поле Branch в Project через GraphQL
```

### Workflow для отслеживания Last Agent Activity

```yaml
- name: Update Last Activity
  uses: actions/github-script@v7
  with:
    script: |
      # Обновить дату последней активности при коммите от агента
```

