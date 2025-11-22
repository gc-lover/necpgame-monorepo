# Troubleshooting Agent Review в Cursor

## Проблема: "Failed to gather Agent Review context. Caused by: Error when executing 'git':"

## Решение

### ✅ Шаг 1: Проверьте git конфигурацию

```bash
git config --global user.name "Your Name"
git config --global user.email "your.email@example.com"
```

### ✅ Шаг 2: Настройте upstream для ветки

Если вы на ветке `develop`:
```bash
git branch --set-upstream-to=origin/develop develop
```

Если вы на ветке `main`:
```bash
git branch --set-upstream-to=origin/main main
```

### ✅ Шаг 3: Убедитесь, что файл `.cursor/BUGBOT.md` существует

Файл должен быть в репозитории:
```bash
ls .cursor/BUGBOT.md
```

Если файла нет - он уже создан, нужно закоммитить:
```bash
git add .cursor/BUGBOT.md
git commit -m "Add Agent Review rules"
```

### ✅ Шаг 4: Проверьте синхронизацию с remote

```bash
git fetch origin
git status
```

Должно показать корректное состояние без ошибок.

### ✅ Шаг 5: Перезапустите Cursor

После всех изменений полностью перезапустите Cursor IDE.

## Проверка работы

1. Откройте любой файл в проекте
2. Используйте команду "Agent Review" или "Review Code"
3. Агент должен проанализировать код по правилам из `.cursor/BUGBOT.md`

## Если проблема сохраняется

### Проверьте логи Cursor

1. Откройте Developer Tools в Cursor (Help → Toggle Developer Tools)
2. Проверьте консоль на ошибки git
3. Проверьте, что git доступен в PATH

### Проверьте права доступа

```bash
# Проверьте, что можете выполнять git команды
git --version
git status
git log --oneline -5
```

### Проверьте репозиторий

```bash
# Убедитесь, что репозиторий не поврежден
git fsck
git remote -v
```

## Дополнительная информация

- Правила ревью: `.cursor/BUGBOT.md`
- Правила агентов: `.cursor/rules/agent-*.mdc`
- Общие правила: `.cursor/rules/main.mdc`

