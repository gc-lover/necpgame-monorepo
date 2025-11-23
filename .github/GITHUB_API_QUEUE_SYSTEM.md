# Система очередей для GitHub API

## Обзор

Система очередей позволяет обрабатывать массовые операции с GitHub Issues батчами, избегая secondary rate limit.

## Компоненты

### 1. GitHub Actions Workflow

**Файл:** `.github/workflows/github-api-batch-processor.yml`

Автоматически обрабатывает Issues батчами с задержками.

### 2. Правила оптимизации

**Файлы:**
- `.cursor/rules/GITHUB_API_OPTIMIZATION.md` - правила оптимизации запросов
- `.cursor/rules/GITHUB_MCP_CACHE_HELPER.md` - шаблоны кода для кэширования
- `.cursor/rules/GITHUB_MCP_BEST_PRACTICES.md` - примеры правильного использования

Содержат правила, примеры и шаблоны кода для агентов.

## Использование

### Ручной запуск через GitHub Actions

1. Перейди в **Actions** → **GitHub API Batch Processor**
2. Нажми **Run workflow**
3. Заполни параметры:
   - **Operation**: тип операции
   - **Query**: поисковый запрос GitHub
   - **Batch size**: размер батча (по умолчанию 5)
   - **Delay ms**: задержка между запросами (по умолчанию 500ms)

### Доступные операции

#### 1. `update-labels`
Обновляет метки Issues батчами.

**Параметры:**
- `query`: поисковый запрос (например, `is:issue is:open label:agent:content-writer`)
- `new_labels`: новые метки через запятую (например, `agent:qa,stage:testing`)
- `batch_size`: размер батча (по умолчанию 5)
- `delay_ms`: задержка между запросами (по умолчанию 500ms)

**Пример:**
```
Operation: update-labels
Query: is:issue is:open label:agent:content-writer label:stage:content
New labels: agent:qa,stage:testing
Batch size: 5
Delay ms: 500
```

#### 2. `add-comments`
Добавляет комментарии к Issues батчами.

**Параметры:**
- `query`: поисковый запрос
- `comment_template`: шаблон комментария (может использовать `{issue_number}`, `{timestamp}`)
- `batch_size`: размер батча
- `delay_ms`: задержка между запросами

**Пример:**
```
Operation: add-comments
Query: is:issue is:open label:agent:content-writer
Comment template: ✅ Контентный квест готов к тестированию\n\nОбработано: {timestamp}
Batch size: 5
Delay ms: 500
```

#### 3. `transfer-to-qa`
Передает Issues в QA батчами.

**Параметры:**
- `query`: поисковый запрос
- `next_agent`: метка следующего агента (по умолчанию `agent:qa`)
- `batch_size`: размер батча
- `delay_ms`: задержка между запросами

**Пример:**
```
Operation: transfer-to-qa
Query: is:issue is:open label:agent:content-writer label:stage:content
Next agent: agent:qa
Batch size: 5
Delay ms: 500
```

#### 4. `transfer-to-next-stage`
Передает Issues следующему этапу батчами.

**Параметры:**
- `query`: поисковый запрос
- `next_agent`: метка следующего агента
- `batch_size`: размер батча
- `delay_ms`: задержка между запросами

## Автоматический запуск

### По расписанию

Workflow запускается автоматически каждые 6 часов (cron: `0 */6 * * *`).

### При изменении меток

Workflow запускается автоматически при добавлении метки `queue:batch-process` к Issue.

## Примеры использования

### Массовая передача в QA

```yaml
Operation: transfer-to-qa
Query: is:issue is:open label:agent:content-writer label:stage:content
Next agent: agent:qa
Batch size: 5
Delay ms: 500
```

### Обновление меток для готовых задач

```yaml
Operation: update-labels
Query: is:issue is:open label:agent:idea-writer label:stage:idea
New labels: agent:architect,stage:design
Batch size: 5
Delay ms: 500
```

### Добавление комментариев о готовности

```yaml
Operation: add-comments
Query: is:issue is:open label:agent:content-writer
Comment template: |
  ✅ Контентный квест готов к тестированию
  
  Файл готов: проверен и готов к QA.
  Обработано: {timestamp}
Batch size: 5
Delay ms: 500
```

## Рекомендации

### Размер батча
- **5-10 Issues** - оптимальный размер для большинства операций
- **Меньше 5** - для критичных операций
- **Больше 10** - не рекомендуется (может вызвать rate limit)

### Задержки
- **300-500ms** - между запросами
- **1000ms** - между батчами
- **60000ms** - при ошибке rate limit

### Поисковые запросы
- Используй точные запросы для фильтрации
- Ограничивай количество результатов (max 100)
- Используй метки для точной фильтрации

## Мониторинг

### Логи GitHub Actions
- Проверяй логи выполнения workflow
- Следи за ошибками rate limit
- Проверяй количество обработанных Issues

### Метрики
- Количество обработанных Issues
- Время выполнения
- Количество ошибок

## Интеграция с агентами

Агенты могут использовать эту систему через:

1. **Ручной запуск**: через GitHub Actions UI
2. **Автоматический запуск**: при добавлении метки `queue:batch-process` к Issue
3. **По расписанию**: каждые 6 часов
4. **Через repository_dispatch**: программный запуск через API

**Важно для агентов:**
- Для <3 Issues используй прямые MCP запросы с кэшированием
- Для 3-9 Issues используй батчинг с задержками
- Для >=10 Issues используй GitHub Actions Batch Processor
- **ОБЯЗАТЕЛЬНО** используй кэширование в памяти сессии (см. `.cursor/rules/GITHUB_MCP_CACHE_HELPER.md`)

## Обработка ошибок

### Rate Limit
- Workflow автоматически ждет 60 секунд при ошибке 403
- Продолжает обработку после ожидания

### Ошибки Issues
- Логирует ошибку и продолжает обработку
- Не останавливает весь батч

## Безопасность

- Использует `GITHUB_TOKEN` из secrets
- Требует права `issues: write` и `pull-requests: write`
- Ограничивает количество запросов через батчинг

