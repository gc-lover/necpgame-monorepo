# Локальное зеркало GitHub Issues

## Обзор

Система локального зеркала позволяет работать с Issues офлайн, избегая rate limit GitHub API. Все изменения сохраняются локально и синхронизируются с GitHub батчами.

## Архитектура

```
.local/issues/
├── cache/              # Кэш Issues (НЕ коммитится)
│   ├── issues/         # Отдельные Issues по номеру (#123.json)
│   └── index.json      # Индекс всех Issues
├── pending/            # Ожидающие синхронизации изменения (коммитится)
│   ├── updates/        # Обновления Issues
│   ├── comments/       # Новые комментарии
│   └── labels/         # Изменения меток
└── sync/               # История синхронизаций (НЕ коммитится)
    └── sync-log.json   # Лог синхронизаций
```

## Использование

### 1. Первоначальная синхронизация

```bash
# Скачать все Issues из GitHub
node scripts/github-issues/sync-from-github.js

# Или только обновления (инкрементальная синхронизация)
node scripts/github-issues/sync-from-github.js --incremental
```

### 2. Работа с локальными Issues

#### Поиск Issues

```bash
# Поиск по тексту
node scripts/github-issues/search-local.js "content-writer"

# Поиск по меткам
node scripts/github-issues/search-local.js "" --labels="agent:content-writer,stage:content"
```

#### Обновление Issue локально

```bash
# Обновить метки
node scripts/github-issues/update-local.js 123 --labels="agent:qa,stage:testing"

# Обновить заголовок
node scripts/github-issues/update-local.js 123 --title="Новый заголовок"

# Изменить статус
node scripts/github-issues/update-local.js 123 --state="closed"
```

#### Добавление комментария локально

```bash
node scripts/github-issues/add-comment-local.js 123 "OK Готово к QA"
```

### 3. Синхронизация изменений в GitHub

```bash
# Отправить все локальные изменения в GitHub
node scripts/github-issues/sync-to-github.js
```

## Автоматическая синхронизация

### GitHub Actions

Workflow `.github/workflows/issues-sync.yml` автоматически:

1. **Каждые 6 часов**: синхронизирует Issues из GitHub (инкрементально)
2. **При ручном запуске**: можно запустить синхронизацию вручную
3. **При отправке**: синхронизирует локальные изменения в GitHub

### Настройка

Workflow использует `GITHUB_TOKEN` из secrets (автоматически доступен в Actions).

## Интеграция с агентами

### Content Writer: Массовая передача в QA

```bash
# 1. Найти все готовые Issues
node scripts/github-issues/search-local.js "" --labels="agent:content-writer,stage:content" > ready-issues.txt

# 2. Обновить метки локально для всех Issues
cat ready-issues.txt | while read issue_num; do
  node scripts/github-issues/update-local.js $issue_num --labels="agent:qa,stage:testing"
done

# 3. Синхронизировать с GitHub (батчами)
node scripts/github-issues/sync-to-github.js
```

### Idea Writer: Поиск задач

```bash
# Найти все задачи idea-writer
node scripts/github-issues/search-local.js "" --labels="agent:idea-writer,stage:idea"
```

## Преимущества

### OK Работа офлайн
- Не требует постоянного доступа к GitHub API
- Можно работать без интернета

### OK Нет rate limit
- Локальные операции не используют API
- Синхронизация батчами с задержками

### OK Быстрый поиск
- Поиск по локальным файлам мгновенный
- Фильтрация по меткам без API запросов

### OK История изменений
- Все изменения сохраняются локально
- Можно откатить изменения

### OK Батчинг при синхронизации
- Все изменения отправляются батчами
- Автоматическая защита от rate limit

## Workflow для агентов

### 1. Content Writer: Передача в QA

```bash
# Найти готовые Issues
ISSUES=$(node scripts/github-issues/search-local.js "" --labels="agent:content-writer,stage:content" | grep "^#" | cut -d' ' -f1 | cut -d'#' -f2)

# Обновить метки локально
for issue in $ISSUES; do
  node scripts/github-issues/update-local.js $issue --labels="agent:qa,stage:testing"
  node scripts/github-issues/add-comment-local.js $issue "OK Контентный квест готов к тестированию"
done

# Синхронизировать
node scripts/github-issues/sync-to-github.js
```

### 2. Idea Writer: Создание Issues

```bash
# После создания Issue в GitHub, синхронизировать
node scripts/github-issues/sync-from-github.js --incremental
```

## Мониторинг

### Проверка статуса синхронизации

```bash
# Количество Issues в кэше
ls -1 .local/issues/cache/issues/*.json | wc -l

# Количество pending изменений
ls -1 .local/issues/pending/updates/*.json 2>/dev/null | wc -l
ls -1 .local/issues/pending/comments/*.json 2>/dev/null | wc -l
```

### Логи синхронизации

GitHub Actions workflow логирует все операции синхронизации.

## Обработка конфликтов

Если Issue был изменен в GitHub после локального изменения:

1. При синхронизации проверяется `updated_at`
2. Если GitHub версия новее - локальное изменение отменяется
3. Можно вручную разрешить конфликт

## Рекомендации

### Частота синхронизации
- **Из GitHub**: каждые 6 часов (автоматически)
- **В GitHub**: после завершения работы агента
- **Вручную**: при необходимости

### Размер кэша
- Кэш Issues может быть большим (1000+ Issues)
- Рекомендуется периодически очищать старые Issues (закрытые >30 дней)

### Безопасность
- Кэш Issues содержит публичные данные
- Не хранить секреты в комментариях Issues
- `.local/issues/cache/` не коммитится в репозиторий

## Troubleshooting

### Кэш устарел

```bash
# Полная пересинхронизация
rm -rf .local/issues/cache/
node scripts/github-issues/sync-from-github.js
```

### Pending изменения не отправляются

```bash
# Проверить pending файлы
ls -la .local/issues/pending/*/

# Попробовать синхронизацию снова
node scripts/github-issues/sync-to-github.js
```

### Rate limit при синхронизации

Скрипты автоматически ждут 60 секунд при rate limit и продолжают работу.

