# Перемещенные файлы проекта

## Важное уведомление

По правилам проекта корень должен оставаться чистым. Все файлы были перемещены в соответствующие директории.

## Где найти файлы

### Git Security Scripts (системная блокировка git команд)
Расположение: `scripts/git-security/`

- `ACTIVATE-ABSOLUTE-PROTECTION.bat` - Активация абсолютной защиты
- `install-system-git-blocker.bat` - Установка системного блокировщика
- `uninstall-system-git-blocker.bat` - Удаление системного блокировщика
- `test-system-blocking.bat` - Тестирование блокировки команд
- `SYSTEM-GIT-BLOCKING-README.md` - Документация по блокировке
- `demo-strict-security.txt` - Демо файл с примерами сообщений

### ESLint Configuration (линтинг и проверка кода)
Расположение: `scripts/linting/`

- `package.json` - Конфигурация зависимостей ESLint
- `package-lock.json` - Lock файл зависимостей

## Как использовать

### Для работы с git security:
```bash
cd scripts/git-security
# Запустить нужный скрипт
```

### Для работы с ESLint:
```bash
cd scripts/linting
npm install
npm run lint
```

## ESLint интеграция

ESLint настроен в корне проекта (`.eslintrc.js`) и ссылается на конфигурацию в `scripts/linting/`.
Для запуска линтинга используйте команды из `scripts/linting/package.json`.

## Правило чистого корня

Согласно `.cursor/rules/always.mdc`, в корне проекта разрешены только:
- `README.md`
- `CHANGELOG*.md`
- Основные конфигурационные файлы (`.gitignore`, `.eslintrc.js`, etc.)
- `prompt.md`

Все остальные файлы должны быть в соответствующих директориях.
