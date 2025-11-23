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

### Проблема: Cursor не может выполнить git команды

**Возможные причины и решения:**

#### 1. Git не в PATH для Cursor

**Решение:**
- Убедитесь, что git установлен и доступен в системном PATH
- Перезапустите Cursor после установки/обновления git
- Проверьте в терминале Cursor: `git --version` должен работать

#### 2. Проблемы с правами доступа на Windows

**Решение:**
- Убедитесь, что Cursor запущен с правами, достаточными для выполнения git команд
- Проверьте, что папка `.git` не заблокирована антивирусом
- Попробуйте запустить Cursor от имени администратора (если нужно)

#### 3. Репозиторий в нестабильном состоянии

**Решение:**
```bash
# Проверьте состояние репозитория
git status
git fsck

# Если есть проблемы - попробуйте:
git gc --prune=now
```

#### 4. Проблемы с line endings (CRLF/LF) на Windows

**Решение:**
```bash
# Настройте git для правильной работы с line endings
git config core.autocrlf true

# Или для проекта:
git config core.autocrlf input
```

#### 5. Незакоммиченные изменения мешают

**Решение:**
- Закоммитьте или stash незакоммиченные изменения
- Agent Review может требовать "чистое" состояние репозитория

```bash
# Закоммитьте изменения
git add .
git commit -m "WIP: temporary commit for Agent Review"

# Или используйте stash
git stash
# После ревью: git stash pop
```

### Проверьте логи Cursor

1. Откройте Developer Tools в Cursor:
   - `Help` → `Toggle Developer Tools` (или `Ctrl+Shift+I`)
2. Перейдите на вкладку `Console`
3. Проверьте ошибки git при попытке Agent Review
4. Ищите сообщения типа "Error when executing 'git'"

### Проверьте права доступа

```bash
# Проверьте, что можете выполнять git команды
git --version
git status
git log --oneline -5

# Проверьте, что git доступен из Cursor терминала
# Откройте терминал в Cursor и выполните те же команды
```

### Проверьте репозиторий

```bash
# Убедитесь, что репозиторий не поврежден
git fsck

# Проверьте remote
git remote -v

# Проверьте, что ветка настроена правильно
git branch -vv
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


