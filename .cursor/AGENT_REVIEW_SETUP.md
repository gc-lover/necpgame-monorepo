# Настройка Agent Review в Cursor

## Проблема

Ошибка: "Failed to gather Agent Review context. Caused by: Error when executing 'git':"

## Решение

### Шаг 1: Убедитесь, что git настроен

```bash
git config --global user.name "Your Name"
git config --global user.email "your.email@example.com"
```

### Шаг 2: Проверьте, что репозиторий инициализирован

```bash
git status
```

Если репозиторий не инициализирован:
```bash
git init
git remote add origin <your-repo-url>
```

### Шаг 3: Закоммитьте файл `.cursor/BUGBOT.md`

Файл `.cursor/BUGBOT.md` должен быть в репозитории для работы Agent Review:

```bash
git add .cursor/BUGBOT.md
git commit -m "Add Agent Review rules"
```

### Шаг 4: Перезапустите Cursor

После добавления файла перезапустите Cursor IDE.

### Шаг 5: Использование Agent Review

1. **В Cursor IDE:**
   - Откройте файл для ревью
   - Используйте команду "Agent Review" или "Review Code"
   - Агент автоматически применит правила из `.cursor/BUGBOT.md`

2. **В GitHub PR:**
   - Используйте `@Cursor` в комментариях PR
   - Агент проанализирует изменения

## Проверка работы

1. Создайте тестовый файл с ошибкой
2. Запустите Agent Review
3. Проверьте, что агент находит проблемы

## Troubleshooting

### Проблема: Git команды не выполняются

**Решение:**
- Убедитесь, что git установлен и доступен в PATH
- Проверьте права доступа к репозиторию
- Убедитесь, что репозиторий не поврежден

### Проблема: Agent Review не видит правила

**Решение:**
- Убедитесь, что `.cursor/BUGBOT.md` закоммичен
- Проверьте, что файл имеет правильный формат (YAML frontmatter)
- Перезапустите Cursor

### Проблема: Ошибка при получении контекста

**Решение:**
- Проверьте, что репозиторий синхронизирован с remote
- Убедитесь, что есть доступ к GitHub (если используется)
- Проверьте логи Cursor для деталей ошибки

## Дополнительная информация

- Правила ревью находятся в `.cursor/BUGBOT.md`
- Правила агентов находятся в `.cursor/rules/agent-*.mdc`
- Общие правила находятся в `.cursor/rules/main.mdc`

