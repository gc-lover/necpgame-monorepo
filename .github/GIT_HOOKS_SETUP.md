# Git Hooks Setup

## Установка

Проект использует Git hooks для проверки качества кода перед коммитом.

### Автоматическая установка

**Linux/Mac:**
```bash
./scripts/install-git-hooks.sh
```

**Windows:**
```cmd
scripts\install-git-hooks.bat
```

### Ручная установка

```bash
git config core.hooksPath .githooks
chmod +x .githooks/*
```

## Настроенные хуки

### Pre-commit

Проверяет размер файлов перед коммитом.

**Что проверяется:**
- Максимум **600 строк** на файл (настраивается в `.github/file-size-config.sh`)
- Только файлы в staging area (готовые к коммиту)
- Типы файлов: `.go`, `.cpp`, `.h`, `.yaml`, `.md`, `.sql`, и др.

**Исключения:**
- `*.gen.go` - сгенерированные файлы
- `*.pb.go` - protobuf файлы
- `vendor/*` - зависимости
- `node_modules/*` - npm пакеты
- `*/pkg/api/api.gen.go` - API сгенерированный код

**Обход проверки (не рекомендуется):**
```bash
git commit --no-verify
```

## Конфигурация

Все настройки в одном месте: `.github/file-size-config.sh`

```bash
export MAX_LINES=600                    # Максимум строк на файл
export FILE_EXTENSIONS=(...)            # Проверяемые типы файлов
export EXCLUDED_PATTERNS=(...)          # Исключения из проверки
```

Эта же конфигурация используется в:
- Git pre-commit hook
- GitHub Actions workflow

## Troubleshooting

### Hook не запускается

```bash
chmod +x .githooks/pre-commit
git config core.hooksPath .githooks
```

### Ошибка "permission denied" на Windows

- Запустите Git Bash от имени администратора
- Или используйте `install-git-hooks.bat`

### Git команды зависают на Windows

Если git команды зависают, это может быть связано с проблемами WSL2 bash:

1. **Проблема:** WSL2 bash не работает (ошибка подключения диска WSL2)
2. **Решение:** Убедитесь, что Git использует Git Bash вместо WSL2 bash

```bash
# Проверьте, какой bash используется
where bash.exe

# Должен показать путь к Git Bash: C:\Program Files\Git\bin\bash.exe
# Если показывает WSL путь - проблема в WSL2

# Исправление: Настройте Git для использования Git Bash
git config --global core.editor "'C:/Program Files/Git/bin/bash.exe' -c 'EDITOR=\"$EDITOR\" exec \"$EDITOR\" \"$@\"'"
```

Если проблема сохраняется, hooks настроены для автоматического обхода при ошибках. Они не должны блокировать git команды.

### Проверка установленных хуков

```bash
git config core.hooksPath
ls -la .githooks/
```

### Временное отключение hooks

```bash
git config --unset core.hooksPath
```

## Удаление

```bash
git config --unset core.hooksPath
```

