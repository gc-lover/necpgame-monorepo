# Руководство по миграции скриптов на Python

## 🚫 КРИТИЧНО: Только Python скрипты разрешены!

Начиная с этого момента, **ЗАПРЕЩЕНО** создавать новые скрипты на:
- ❌ Shell/Bash (.sh)
- ❌ PowerShell (.ps1)
- ❌ Batch (.bat/.cmd)
- ❌ Perl (.pl)
- ❌ Ruby (.rb)
- ❌ JavaScript (.js)

**ТОЛЬКО Python (.py) скрипты разрешены для новой разработки!**

## OK Разрешенные исключения (системная инфраструктура)

Эти скрипты остаются и поддерживаются:

### Git Hooks (`.githooks/*.sh`)
- `pre-commit` - защита от опасных команд
- `post-commit` - уведомления
- `pre-push` - финальные проверки

### Инфраструктура (`infrastructure/**/*.sh`)
- Docker сборка и развертывание
- Kubernetes манифесты
- Мониторинг и логирование

### Безопасность (`scripts/git-security/*.bat`)
- Активация защиты Git
- Блокировка опасных команд
- Системные инструменты безопасности

### Сборка (`scripts/linting/*`)
- Node.js инструменты для линтинга
- Пакетный менеджмент

## 🔄 Миграция существующих скриптов

### Приоритет миграции:

1. **Высокий приоритет** (мигрировать первыми):
   - `scripts/lint.sh` → `scripts/lint.py`
   - `scripts/generate-content-migrations.sh` → `scripts/generate-content-migrations.py`
   - `scripts/validate-backend-optimizations.sh` → `scripts/validate-backend-optimizations.py`

2. **Средний приоритет**:
   - `scripts/deploy/*.sh` → `scripts/deploy/*.py`
   - `scripts/db/*.sh` → `scripts/db/*.py`

3. **Низкий приоритет** (системные):
   - `scripts/local/*.sh` - оставить как есть
   - `scripts/testing/*.ps1` - конвертировать по мере необходимости

## 🛠️ Как создать новый Python скрипт

### Используй базовый фреймворк:

```python
#!/usr/bin/env python3
from scripts.framework import ScriptFramework

class MyScript(ScriptFramework):
    def add_script_args(self):
        self.parser.add_argument('--input', required=True, help='Input file')
        self.parser.add_argument('--output', help='Output file')

    def run(self):
        args = self.parse_args()

        # Твоя логика здесь
        self.logger.info(f"Processing {args.input}")

        if args.dry_run:
            self.logger.info("DRY RUN: бы не выполнил изменения")
            return

        # Выполняй работу...

if __name__ == "__main__":
    script = MyScript("My Script", "Description of what it does")
    script.main()
```

### Запуск:
```bash
python scripts/my_script.py --help
python scripts/my_script.py --input file.txt --verbose
python scripts/my_script.py --dry-run  # безопасный тест
```

## 📚 Возможности фреймворка

### Автоматически предоставляется:

- **Логирование**: структурированное с уровнями (DEBUG, INFO, WARNING, ERROR)
- **Обработка аргументов**: `--verbose`, `--dry-run`, `--config`
- **Валидация окружения**: проверка Python версии, наличия проекта
- **Обработка ошибок**: понятные сообщения и exit codes
- **Запуск команд**: `self.run_command(['git', 'status'])`
- **Работа с файлами**: `self.read_file()`, `self.write_file()`
- **Подтверждения**: `self.get_confirmation("Continue?")`

### Утилиты:
```bash
# Показать все Python скрипты
python scripts/framework.py --list-scripts

# Проверить синтаксис всех скриптов
python scripts/framework.py --validate-scripts
```

## 🔧 Конвертация существующих скриптов

### Пример: Конвертация Bash скрипта

**Было (Bash):**
```bash
#!/bin/bash
if [ -z "$1" ]; then
    echo "Usage: $0 <file>"
    exit 1
fi

echo "Processing $1..."
grep "pattern" "$1" > output.txt
echo "Done"
```

**Стало (Python):**
```python
#!/usr/bin/env python3
from scripts.framework import ScriptFramework
import re

class FileProcessor(ScriptFramework):
    def add_script_args(self):
        self.parser.add_argument('input_file', help='File to process')

    def run(self):
        args = self.parse_args()

        self.logger.info(f"Processing {args.input_file}")

        content = self.read_file(Path(args.input_file))
        matches = re.findall(r'pattern', content)

        output_path = Path("output.txt")
        self.write_file(output_path, '\n'.join(matches))

        self.logger.info(f"Found {len(matches)} matches, saved to {output_path}")

if __name__ == "__main__":
    script = FileProcessor("File Processor", "Find patterns in files")
    script.main()
```

## 🚨 Git Hook блокирует запрещенные скрипты

При попытке закоммитить `.sh`/`.ps1`/`.bat` файл в `scripts/`:

```
[BLOCKED] COMMIT BLOCKED: FORBIDDEN SCRIPT TYPE DETECTED!

SCRIPT LANGUAGE POLICY ENFORCEMENT:
• OK ALLOWED: .py (Python scripts)
• ❌ FORBIDDEN: .sh, .ps1, .bat, .cmd, .pl, .rb, .js

WHY THIS IS ENFORCED:
• Python is cross-platform and maintainable
• Shell scripts cause platform compatibility issues
• Python has better error handling and testing
• Single language reduces cognitive load
```

## 🎯 Следующие шаги

1. **Начать миграцию** с высокоприоритетных скриптов
2. **Использовать фреймворк** для всех новых скриптов
3. **Тестировать** конвертированные скрипты
4. **Удалять** старые скрипты после успешной миграции
5. **Обновлять документацию** с новыми Python командами

## 📞 Нужна помощь?

- Посмотри примеры в `scripts/*.py`
- Используй `python scripts/framework.py --help`
- Создай Issue для сложных конвертаций
