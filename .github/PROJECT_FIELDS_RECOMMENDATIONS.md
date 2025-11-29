# Рекомендации по кастомным полям и веткам в GitHub Project

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

### Рекомендуемые поля

1. **Branch** (Text) OK
   - Автоматически заполняется при создании ветки
   - Формат: `feature/issue-{number}-{desc}`
   - Полезно для быстрой навигации

### Опциональные поля (для метрик)

2. **Blocked By** (Linked Issues, multiple) - блокирующие задачи
3. **Estimated Time** (Number, hours) - оценка времени
4. **Actual Time** (Number, hours) - реальное время
5. **Last Agent Activity** (Date) - последняя активность

### НЕ нужно

- ❌ **Assigned Agent** - дублирует метки `agent:*`
- ❌ **Priority** - уже есть в метках `priority-*`
- ❌ **Component** - уже есть в метках `backend`, `client`, etc.

## Стратегия веток

### Рекомендуемый подход

**Одна ветка на одну задачу (Issue)**

```
feature/issue-{number}-{short-description}
```

**Примеры:**
- `feature/issue-42-inventory-system`
- `feature/issue-55-character-creation`

### Как это работает

1. **Создание ветки:**
   - Автоматически при добавлении метки `ready-for-dev`
   - Или при переходе к этапу `stage:idea`
   - Workflow: `.github/workflows/auto-create-branch.yml`

2. **Работа агентов:**
   - Все агенты работают в одной ветке последовательно
   - Каждый агент делает коммиты с префиксом: `[agent-name]`
   - Примеры:
     - `[idea-writer] Add quest concept`
     - `[architect] Design system architecture`
     - `[backend] Implement service`

3. **Pull Request:**
   - Создается когда все этапы завершены
   - Или когда нужен промежуточный ревью
   - Связан с Issue через `Closes #42`

4. **Merge:**
   - PR → `develop` → тестирование → `main`
   - Используется squash merge для чистоты

### НЕ рекомендуется

**Отдельные ветки для каждого агента:**
- Слишком много веток
- Много мерджей между этапами
- Сложно отслеживать прогресс
- Избыточно для работы в одном IDE

## Итоговые рекомендации

**Минимальный набор полей:**
- OK Developmen Stage (уже есть)
- OK Status (уже есть)
- OK Labels (уже есть)
- ➕ Branch (добавить)

**Стратегия веток:**
- Одна ветка на Issue OK
- Префиксы агентов в коммитах OK
- Автоматическое создание OK

## Автоматизация

### Workflow для автоматического заполнения Branch

```yaml
- name: Update Branch Field
  uses: actions/github-script@v7
  with:
    script: |
      const branchName = `feature/issue-${context.payload.issue.number}-${context.payload.issue.title.toLowerCase().replace(/[^a-z0-9]+/g, '-').substring(0, 50)}`;
      
      # Обновить поле Branch в Project через GraphQL
```

