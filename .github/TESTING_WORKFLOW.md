# Тестирование Workflow автоматизации агентов

## Созданная тестовая задача

**Issue #9:** [Agent] Тестовая задача: Система уведомлений в игре
- **URL:** https://github.com/gc-lover/necpgame-monorepo/issues/9
- **Этап:** idea-writer
- **Метки:** `agent:idea-writer`, `stage:idea`, `game-design`, `priority-medium`

## Что должно произойти автоматически

### 1. GitHub Actions Workflows

После создания Issue должны запуститься:

#### `project-status-automation.yml`
- ✅ Добавит Issue в GitHub Project #1
- ✅ Установит статус "Development Stage" = `idea-writer`
- ✅ Создаст автоматический комментарий о назначении агента

#### `agent-workflow.yml`
- ✅ Определит агента по меткам (`agent:idea-writer`)
- ✅ Обновит метки при необходимости

#### `auto-create-branch.yml` (если добавить метку `ready-for-dev`)
- ✅ Создаст ветку `feature/issue-9-система-уведомлений`
- ✅ Обновит поле "Branch" в Project

### 2. Проверка работы

**Через 1-2 минуты после создания Issue:**

1. **Проверьте GitHub Project:**
   - Откройте: https://github.com/users/gc-lover/projects/1
   - Issue #9 должна быть в колонке с соответствующим статусом

2. **Проверьте GitHub Actions:**
   - Откройте: https://github.com/gc-lover/necpgame-monorepo/actions
   - Должны быть запущенные workflows для Issue #9

3. **Проверьте Issue:**
   - Должен быть автоматический комментарий от бота
   - Статус должен быть обновлен

## Тестирование полного цикла

### Шаг 1: Idea Writer агент работает

1. Откройте Issue #9 в Cursor
2. Используйте команду: `@agent-idea-writer Разработай концепцию системы уведомлений`
3. Агент должен:
   - Прочитать Issue
   - Создать детальную концепцию
   - Обновить Issue с результатами

### Шаг 2: Переход к следующему этапу

После выполнения всех критериев приемки:

1. Отметьте все чекбоксы в Issue:
   ```
   - [x] Описана концепция визуального дизайна уведомлений
   - [x] Определены типы уведомлений
   - [x] Описана система приоритетов
   - [x] Создан лор для системы уведомлений
   - [x] Описаны настройки персонализации
   ```

2. **Автоматически:**
   - `agent-stage-transition.yml` переведет Issue на этап `architect`
   - Обновятся метки: `agent:architect`, `stage:design`
   - Создастся комментарий о переходе

### Шаг 3: Architect агент работает

1. Откройте Issue #9
2. Используйте: `@agent-architect Структурируй концепцию из Issue #9`
3. Агент должен создать архитектурную структуру

### Шаг 4: Создание ветки

Когда задача готова к разработке:

1. Добавьте метку `ready-for-dev` к Issue
2. **Автоматически:**
   - `auto-create-branch.yml` создаст ветку `feature/issue-9-система-уведомлений`
   - Обновит поле "Branch" в Project

### Шаг 5: Работа агентов в ветке

1. Переключитесь на созданную ветку
2. Агенты будут работать последовательно:
   - Backend Dev → Network → UE5 Dev → QA → Release

## Проверка логов

Если что-то не работает:

1. **GitHub Actions логи:**
   - https://github.com/gc-lover/necpgame-monorepo/actions
   - Проверьте последние запуски workflows

2. **Проверьте метки Issue:**
   - Должны быть: `agent:idea-writer`, `stage:idea`

3. **Проверьте Project:**
   - Issue должна быть в Project с правильным статусом

## Следующие шаги

1. Подождите 1-2 минуты для обработки workflows
2. Проверьте GitHub Project и Actions
3. Начните работу с Idea Writer агентом через Cursor
4. Протестируйте переходы между этапами

## Полезные ссылки

- Issue #9: https://github.com/gc-lover/necpgame-monorepo/issues/9
- Project #1: https://github.com/users/gc-lover/projects/1
- Actions: https://github.com/gc-lover/necpgame-monorepo/actions
- Workflow файлы: `.github/workflows/`

