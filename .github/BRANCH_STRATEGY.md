# Стратегия веток для работы агентов

## Анализ ситуации

### Текущая ситуация
- Все агенты работают в одном Cursor IDE на одном компьютере
- Агенты работают последовательно (один за другим) или параллельно
- Нужна четкая структура для отслеживания изменений

### Вопрос: Нужны ли отдельные ветки для каждого агента?

**Ответ: НЕТ, но нужна структурированная система веток**

## Рекомендуемая стратегия веток

### Основные ветки

```
main (master)
  └─ production-ready код
  └─ защищена, только через PR
  └─ автоматический деплой

develop
  └─ интеграционная ветка
  └─ все фичи объединяются сюда
  └─ тестирование перед релизом
```

### Ветки для работы агентов

```
feature/issue-{number}-{short-description}
  └─ одна ветка на одну задачу (Issue)
  └─ все агенты работают в одной ветке последовательно
  └─ пример: feature/issue-42-inventory-system
```

### Структура коммитов

Каждый агент делает коммиты с префиксом своего имени:

```
[idea-writer] Add quest concept and lore
[architect] Design inventory system architecture
[api-designer] Create OpenAPI spec for inventory
[backend] Implement inventory service
[network] Configure gRPC endpoints
[ue5] Add inventory UI in Unreal Engine
[qa] Add integration tests
```

### Альтернатива: Ветки по этапам (НЕ рекомендуется)

```
feature/issue-42-idea
feature/issue-42-architect
feature/issue-42-api
feature/issue-42-backend
...
```

**Почему НЕ рекомендуется:**
- Слишком много веток
- Сложно отслеживать прогресс
- Много мерджей между этапами
- Если агенты работают в одном IDE, это избыточно

## Рекомендуемый FLOW

### Сценарий 1: Последовательная работа агентов

```
1. Создается Issue #42 "Inventory System"
2. Создается ветка: feature/issue-42-inventory-system
3. Idea Writer работает → коммиты [idea-writer]...
4. Architect работает → коммиты [architect]...
5. API Designer работает → коммиты [api-designer]...
6. Backend работает → коммиты [backend]...
7. Network работает → коммиты [network]...
8. UE5 работает → коммиты [ue5]...
9. QA работает → коммиты [qa]...
10. Создается PR в develop
11. После ревью → merge в develop
12. После тестирования → merge в main
```

### Сценарий 2: Параллельная работа (DevOps, Performance)

```
feature/issue-42-inventory-system (основная ветка)
  ├─ [backend] Implement service
  ├─ [devops] Add Docker config (параллельно)
  └─ [performance] Optimize queries (параллельно)
```

## Правила работы с ветками

### 1. Создание ветки
- Одна ветка = одна задача (Issue)
- Название: `feature/issue-{number}-{short-desc}`
- Создается автоматически при начале работы над Issue

### 2. Коммиты
- Префикс агента в каждом коммите: `[agent-name]`
- Описательные сообщения
- Маленькие, логичные коммиты

### 3. Pull Request
- Создается когда все этапы завершены
- Или когда нужен ревью на промежуточном этапе
- Связан с Issue через `Closes #42`

### 4. Merge
- Все PR идут в `develop`
- После тестирования → `main`
- Используется squash merge для чистоты истории

## Автоматизация через GitHub Actions

### Workflow для создания ветки

```yaml
on:
  issues:
    types: [opened, labeled]

jobs:
  create-branch:
    if: contains(github.event.issue.labels.*.name, 'ready-for-dev')
    steps:
      - name: Create feature branch
        run: |
          git checkout -b feature/issue-${{ github.event.issue.number }}-$(echo "${{ github.event.issue.title }}" | tr '[:upper:]' '[:lower:]' | tr ' ' '-' | head -c 50)
```

### Workflow для проверки коммитов

```yaml
on:
  pull_request:
    types: [opened, synchronize]

jobs:
  check-commits:
    steps:
      - name: Check commit prefixes
        run: |
          # Проверяем, что коммиты имеют префиксы агентов
          git log --oneline ${{ github.event.pull_request.base.sha }}..${{ github.event.pull_request.head.sha }} | grep -E '\[(idea-writer|architect|api-designer|backend|network|devops|performance|ue5|qa|release)\]'
```

## Преимущества подхода

1. **Простота** - одна ветка на задачу, легко отслеживать
2. **История** - видно кто что делал по префиксам коммитов
3. **Гибкость** - можно работать параллельно если нужно
4. **Чистота** - меньше веток, проще управление
5. **Автоматизация** - легко автоматизировать создание и проверку

## Недостатки альтернативных подходов

### Отдельные ветки для каждого агента
- ❌ Слишком много веток
- ❌ Много мерджей
- ❌ Сложно отслеживать прогресс
- ❌ Избыточно для работы в одном IDE

### Одна ветка для всех задач
- ❌ Сложно отслеживать отдельные фичи
- ❌ Конфликты между задачами
- ❌ Невозможно работать параллельно

## Рекомендации

1. **Используйте одну ветку на Issue** - `feature/issue-{number}-{desc}`
2. **Префиксы в коммитах** - `[agent-name]` для отслеживания
3. **PR в develop** - после завершения всех этапов
4. **Автоматизация** - создание веток и проверка коммитов
5. **Чистота истории** - squash merge для чистоты

