# Локальное зеркало GitHub Issues

## Обзор

Локальное зеркало позволяет работать с Issues офлайн, избегая rate limit GitHub API.

## Структура

```
.local/issues/
├── cache/              # Кэш Issues (JSON файлы)
│   ├── issues/         # Отдельные Issues по номеру
│   └── index.json      # Индекс всех Issues
├── pending/            # Ожидающие синхронизации изменения
│   ├── updates/        # Обновления Issues
│   ├── comments/       # Новые комментарии
│   └── labels/         # Изменения меток
└── sync/               # История синхронизаций
    └── sync-log.json   # Лог синхронизаций
```

## Использование

### Синхронизация с GitHub

```bash
# Полная синхронизация (скачать все Issues)
node scripts/github-issues/sync-from-github.js

# Синхронизация изменений (только обновления)
node scripts/github-issues/sync-from-github.js --incremental

# Отправка локальных изменений в GitHub
node scripts/github-issues/sync-to-github.js
```

### Работа с локальными Issues

```bash
# Поиск Issues локально
node scripts/github-issues/search-local.js "label:agent:content-writer"

# Обновление Issue локально
node scripts/github-issues/update-local.js 123 --labels "agent:qa,stage:testing"

# Добавление комментария локально
node scripts/github-issues/add-comment-local.js 123 "Готово к QA"
```

## Автоматическая синхронизация

GitHub Actions автоматически синхронизирует Issues:
- Каждые 6 часов: полная синхронизация
- При изменении Issue: инкрементальная синхронизация
- Перед отправкой изменений: проверка конфликтов

## Преимущества

- OK Работа офлайн без API запросов
- OK Нет rate limit при локальной работе
- OK Быстрый поиск и фильтрация
- OK История изменений
- OK Батчинг при синхронизации

