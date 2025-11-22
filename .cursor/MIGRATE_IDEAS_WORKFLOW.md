# Workflow миграции существующих идей в Issues

## Обзор

Этот workflow позволяет Idea Writer агенту переносить существующие идеи из `knowledge/analysis/tasks/ideas/` в GitHub Issues для работы через новый автоматизированный процесс.

## Структура существующих идей

Идеи хранятся в формате YAML:
- **Путь:** `knowledge/analysis/tasks/ideas/*.yaml`
- **Формат:** YAML с секциями `metadata`, `summary`, `content`
- **Примеры:**
  - `2025-11-07-IDEA-subtle-media-collabs.yaml`
  - `2025-11-03-IDEA-events-brainstorm.yaml`
  - `2025-11-03-IDEA-procedural-equipment-matrix.yaml`

## Способы миграции

### Способ 1: Через Idea Writer агента (рекомендуется)

#### Одна идея

1. Откройте чат Cursor (`Ctrl+L`)
2. Введите команду:
   ```
   @agent-idea-writer Прочитай идею из knowledge/analysis/tasks/ideas/2025-11-07-IDEA-subtle-media-collabs.yaml и создай GitHub Issue для её проработки
   ```

3. Агент:
   - Прочитает YAML файл
   - Извлечет метаданные и контент
   - Создаст GitHub Issue через MCP
   - Обновит исходный файл со ссылкой на Issue

#### Несколько идей

```
@agent-idea-writer Найди все идеи в knowledge/analysis/tasks/ideas/ со статусом draft и создай Issues для каждой. Начни с первых 5 идей.
```

### Способ 2: Через GitHub Actions (автоматизация)

1. Откройте: https://github.com/gc-lover/necpgame-monorepo/actions
2. Выберите workflow: **Migrate Ideas to Issues**
3. Нажмите **Run workflow**
4. Укажите путь к файлу идеи:
   ```
   knowledge/analysis/tasks/ideas/2025-11-07-IDEA-subtle-media-collabs.yaml
   ```
5. Нажмите **Run workflow**

Workflow автоматически:
- Распарсит YAML файл
- Создаст GitHub Issue
- Обновит исходный файл со ссылкой на Issue

### Способ 3: Массовая миграция через скрипт

Создайте скрипт для обработки всех идей:

```bash
# Найти все идеи со статусом draft
find knowledge/analysis/tasks/ideas/ -name "*.yaml" -type f | while read file; do
  # Проверить статус в YAML
  status=$(grep "status: draft" "$file" || echo "")
  if [ -n "$status" ]; then
    echo "Processing: $file"
    # Запустить workflow или использовать агента
  fi
done
```

## Структура создаваемого Issue

### Заголовок
```
[Agent] {title из metadata}
```

### Тело Issue

```markdown
## Описание идеи

{summary.problem}

**Цель:** {summary.goal}
**Суть:** {summary.essence}

### Ключевые моменты
{summary.key_points как список}

## Детали
{content.sections преобразованные в Markdown}

## Критерии приемки
- [ ] Идея полностью описана текстом
- [ ] Лор проработан и согласован
- [ ] Игровая механика описана понятно
- [ ] Все текстовые материалы готовы
- [ ] Связанные системы определены

## Метаданные
- **Источник:** {путь к файлу}
- **Статус:** {metadata.status}
- **Приоритет:** {metadata.priority}
- **Теги:** {metadata.tags}
```

### Метки

Автоматически добавляются:
- `agent:idea-writer`
- `stage:idea`
- `game-design`
- `priority-{high/medium/low}` (на основе metadata.priority)

Дополнительно из metadata.tags:
- Если есть `narrative` или `lore` → `game-design`
- Если есть `combat` → `backend`
- И т.д.

## Обновление исходного файла

После создания Issue исходный YAML файл обновляется:

```yaml
metadata:
  related_documents:
    - id: github-issue-{number}
      title: GitHub Issue #{number}
      link: https://github.com/.../issues/{number}
      relation: migrated_to
  status: in_progress  # если был draft
```

## Примеры миграции

### Пример 1: Идея пасхалок

**Файл:** `knowledge/analysis/tasks/ideas/2025-11-07-IDEA-subtle-media-collabs.yaml`

**Команда:**
```
@agent-idea-writer Прочитай идею из knowledge/analysis/tasks/ideas/2025-11-07-IDEA-subtle-media-collabs.yaml и создай GitHub Issue
```

**Результат:**
- Issue создан с заголовком: `[Agent] Новые скрытые коллаборации и пасхалки`
- Метки: `agent:idea-writer`, `stage:idea`, `game-design`, `priority-medium`
- Исходный файл обновлен со ссылкой на Issue

### Пример 2: Идея мировых событий

**Файл:** `knowledge/analysis/tasks/ideas/2025-11-03-IDEA-events-brainstorm.yaml`

**Команда:**
```
@agent-idea-writer Мигрируй идею из knowledge/analysis/tasks/ideas/2025-11-03-IDEA-events-brainstorm.yaml в GitHub Issue
```

## Проверка миграции

После миграции проверьте:

1. **Issue создан:**
   - Откройте: https://github.com/gc-lover/necpgame-monorepo/issues
   - Найдите Issue с заголовком `[Agent] {название идеи}`

2. **Issue в Project:**
   - Откройте: https://github.com/users/gc-lover/projects/1
   - Issue должен быть в колонке с статусом `idea-writer`

3. **Исходный файл обновлен:**
   - Проверьте `metadata.related_documents` в YAML файле
   - Должна быть ссылка на созданный Issue

## Работа с мигрированными идеями

После миграции работайте с идеей через Issue:

1. **Idea Writer агент:**
   ```
   @agent-idea-writer Проработай Issue #{number} - разработай детальную концепцию
   ```

2. **Автоматические переходы:**
   - После выполнения критериев приемки Issue автоматически перейдет на этап `architect`
   - Workflow обновит статус в Project

## Troubleshooting

### Проблема: Агент не может прочитать YAML файл

**Решение:**
- Убедитесь, что путь к файлу правильный
- Проверьте, что файл существует и доступен
- Попробуйте указать полный путь от корня проекта

### Проблема: Issue создан, но не в Project

**Решение:**
- Подождите 1-2 минуты (workflow обрабатывает автоматически)
- Проверьте GitHub Actions: https://github.com/gc-lover/necpgame-monorepo/actions
- Убедитесь, что workflow `project-status-automation.yml` запустился

### Проблема: Исходный файл не обновлен

**Решение:**
- Проверьте права доступа к файлу
- Убедитесь, что агент имеет права на запись
- Попробуйте обновить файл вручную, добавив ссылку на Issue

## Список идей для миграции

Текущие идеи в `knowledge/analysis/tasks/ideas/`:

1. `2025-11-07-IDEA-subtle-media-collabs.yaml` - Скрытые коллаборации
2. `2025-11-07-IDEA-subliminal-easter-network.yaml` - Скрытая сеть пасхалок
3. `2025-11-07-IDEA-hybrid-media-references.yaml` - Гибридные медиа-ссылки
4. `2025-11-07-IDEA-immersive-media-cheese.yaml` - Иммерсивные медиа-пасхалки
5. `2025-11-07-IDEA-reactive-media-puzzles.yaml` - Реактивные медиа-головоломки
6. `2025-11-07-IDEA-passive-media-easter-eggs.yaml` - Пассивные медиа-пасхалки
7. `2025-11-07-IDEA-crossculture-easter-atlas.yaml` - Кросс-культурный атлас
8. `2025-11-07-IDEA-urban-legend-easter-eggs.yaml` - Городские легенды
9. `2025-11-03-IDEA-events-brainstorm.yaml` - Мировые события
10. `2025-11-03-IDEA-procedural-equipment-matrix.yaml` - Процедурная матрица экипировки

## Следующие шаги

1. Начните миграцию с одной идеи для тестирования
2. Проверьте, что workflow работает корректно
3. Мигрируйте остальные идеи по одной или пакетами
4. После миграции работайте с идеями через Issues и автоматизированный workflow

