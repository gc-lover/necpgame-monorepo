# Настройка и Troubleshooting Agent Review в Cursor

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

### Шаг 4: Настройте upstream для ветки

Если вы на ветке `develop`:
```bash
git branch --set-upstream-to=origin/develop develop
```

Если вы на ветке `main`:
```bash
git branch --set-upstream-to=origin/main main
```

### Шаг 5: Перезапустите Cursor

После всех изменений полностью перезапустите Cursor IDE.

## Использование Agent Review

1. **В Cursor IDE:**
   - Откройте файл для ревью
   - Используйте команду "Agent Review" или "Review Code"
   - Агент автоматически применит правила из `.cursor/BUGBOT.md`

2. **В GitHub PR:**
   - Используйте `@Cursor` в комментариях PR
   - Агент проанализирует изменения

## Troubleshooting

### Проблема: "Failed to gather Agent Review context. Caused by: Error when executing 'git':"

**Решения:**

1. **Проверьте git в PATH:**
   ```bash
   git --version
   ```

2. **Проверьте конфигурацию git:**
   ```bash
   git config --global user.name
   git config --global user.email
   ```

3. **Закоммитьте незакоммиченные изменения:**
   ```bash
   git status
   git add .
   git commit -m "WIP: temporary commit"
   ```

4. **Проверьте upstream для ветки:**
   ```bash
   git branch -vv
   ```

5. **Проверьте синхронизацию с remote:**
   ```bash
   git fetch origin
   git status
   ```

### Проблема: Git команды не выполняются

**Решение:**
- Убедитесь, что git установлен и доступен в PATH
- Проверьте права доступа к репозиторию
- Убедитесь, что репозиторий не поврежден
- На Windows: проверьте, что git.exe доступен в системном PATH

### Проблема: Agent Review не видит правила

**Решение:**
- Убедитесь, что `.cursor/BUGBOT.md` закоммичен
- Проверьте, что файл имеет правильный формат (YAML frontmatter с `alwaysApply: true`)
- Перезапустите Cursor

### Проблема: Ошибка при получении контекста

**Решение:**
- Проверьте, что репозиторий синхронизирован с remote (`git fetch origin`)
- Убедитесь, что есть доступ к GitHub
- Проверьте логи Cursor (Help → Toggle Developer Tools → Console)
- Попробуйте закоммитить все изменения перед ревью

### Проблема: Проблемы с line endings (CRLF/LF) на Windows

**Решение:**
```bash
git config core.autocrlf true
# Или для проекта:
git config core.autocrlf input
```

### Альтернативное решение: Использование через GitHub PR

Если Agent Review не работает локально, используйте ревью через GitHub:

1. Создайте Pull Request
2. Используйте `@Cursor` в комментариях PR
3. Cursor проанализирует изменения через GitHub API

## Дополнительная информация

- Правила ревью: `.cursor/BUGBOT.md`
- Правила агентов: `.cursor/rules/agent-*.mdc`
- Общие правила: `.cursor/rules/main.mdc`

