# Валидация меток агентов

## Автоматическая проверка workflow

GitHub Action `.github/workflows/validate-agent-labels.yml` автоматически проверяет метки при создании и редактировании Issues.

## Правила валидации

### 1. Только одна метка `agent:*` одновременно

**Ошибка:** Несколько меток агентов одновременно
```
❌ agent:content-writer + agent:backend
✅ agent:content-writer
```

**Действие:** Автоматически удаляются все метки агентов, кроме первой.

### 2. Соответствие `agent:*` и `stage:*`

**Ошибка:** Метка агента не соответствует метке этапа
```
❌ agent:content-writer + stage:backend-dev
✅ agent:content-writer + stage:content
```

**Действие:** Автоматически добавляется правильная метка `stage:*` и удаляются несовместимые.

### 3. Контентные квесты (canon, lore, quest)

**Правильный workflow:**
```
Idea Writer → Content Writer → Backend → QA
```

**Ошибки:**
- ❌ Контентные квесты через Architect
- ❌ Content Writer → QA (пропуск Backend для импорта в БД)

**Действие:** Автоматически удаляются несовместимые метки и добавляется комментарий с объяснением.

### 4. Системные задачи

**Правильный workflow:**
```
Idea Writer → Architect → Database → API Designer → Backend → Network → Security → DevOps → UE5 → QA → Game Balance → Release
```

### 5. Метка `returned`

**Предупреждение:** Если есть метка `returned`, должна быть метка агента для возврата задачи.

## Маппинг агентов и этапов

| Агент | Этап |
|-------|------|
| `agent:idea-writer` | `stage:idea` |
| `agent:content-writer` | `stage:content` |
| `agent:architect` | `stage:design` |
| `agent:database` | `stage:database` |
| `agent:api-designer` | `stage:api-design` |
| `agent:backend` | `stage:backend-dev` |
| `agent:network` | `stage:network` |
| `agent:security` | `stage:security` |
| `agent:devops` | `stage:infrastructure` |
| `agent:performance` | `stage:performance` |
| `agent:ue5` | `stage:client-dev` |
| `agent:qa` | `stage:testing` |
| `agent:game-balance` | `stage:balance` |
| `agent:release` | `stage:release` |

## Что происходит при нарушении

1. **Создается комментарий** в Issue с описанием нарушений
2. **Автоматически исправляются метки:**
   - Удаляются несовместимые метки агентов
   - Удаляются несовместимые метки этапов
   - Добавляются правильные метки этапов
3. **Предупреждения** создают комментарий, но не изменяют метки

## Примеры нарушений

### Пример 1: Несколько агентов одновременно
```
Метки: agent:content-writer, agent:backend
Результат: ❌ Ошибка - удаляется agent:backend, остается agent:content-writer
```

### Пример 2: Неправильный этап
```
Метки: agent:content-writer, stage:backend-dev
Результат: ❌ Ошибка - удаляется stage:backend-dev, добавляется stage:content
```

### Пример 3: Контентный квест через Architect
```
Метки: canon, agent:architect
Результат: ❌ Ошибка - удаляются agent:architect и stage:design
```

### Пример 4: Content Writer → QA (пропуск Backend)
```
Метки: canon, agent:content-writer, agent:qa
Результат: ❌ Ошибка - удаляется agent:qa, добавляется комментарий о необходимости Backend
```

## Как избежать нарушений

1. **Следуйте правилам workflow:**
   - Контентные квесты: Idea Writer → Content Writer → Backend → QA
   - Системные задачи: Idea Writer → Architect → Database → API Designer → Backend → ...

2. **Используйте правильные метки:**
   - Всегда добавляйте соответствующую метку `stage:*` при добавлении `agent:*`
   - Удаляйте свою метку агента при передаче следующему агенту

3. **Проверяйте документацию:**
   - `.cursor/rules/AGENT_LABEL_MANAGEMENT.md`
   - `.github/AGENT_WORKFLOW.md`

## Отключение валидации

Валидация работает автоматически для всех Issues. Если нужно временно отключить валидацию для конкретного Issue, добавьте метку `skip-validation` (не рекомендуется).

