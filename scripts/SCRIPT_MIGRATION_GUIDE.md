# Руководство по миграции скриптов на Python

## [FORBIDDEN] КРИТИЧНО: Только Python скрипты разрешены!

Начиная с этого момента, **ЗАПРЕЩЕНО** создавать новые скрипты на:
- [ERROR] Shell/Bash (.sh)
- [ERROR] PowerShell (.ps1)
- [ERROR] Batch (.bat/.cmd)
- [ERROR] Perl (.pl)
- [ERROR] Ruby (.rb)
- [ERROR] JavaScript (.js)

**ТОЛЬКО Python (.py) скрипты разрешены для новой разработки!**

## [OK] Разрешенные исключения (системная инфраструктура)

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

## [SYMBOL] OK РЕФАКТОРИНГ ЗАВЕРШЕН - НОВАЯ АРХИТЕКТУРА

### Архитектура по SOLID принципам:

#### 🎯 **Принципы SOLID реализованы:**
- **Single Responsibility**: Каждый класс делает только одно дело
- **Open/Closed**: Легко расширять без изменения существующего кода
- **Liskov Substitution**: Все наследники совместимы с базовыми классами
- **Interface Segregation**: Минимальные интерфейсы для каждого компонента
- **Dependency Inversion**: Зависимости инжектируются через конструкторы

#### 🏗️ **Новая структура скриптов:**

```
scripts/
├── core/                          # Базовые компоненты
│   ├── __init__.py
│   ├── base_script.py            # Базовый класс для всех скриптов
│   ├── config.py                 # Управление конфигурацией
│   ├── file_manager.py           # Работа с файлами
│   ├── logger.py                 # Логирование
│   └── command_runner.py         # Запуск команд
├── openapi/                      # Работа с OpenAPI
│   ├── __init__.py
│   └── openapi_manager.py        # Управление OpenAPI спецификациями
├── sql/                          # Работа с SQL
│   ├── __init__.py
│   └── liquibase_processor.py    # Обработка Liquibase миграций
├── validation/                   # Валидация
│   ├── __init__.py
│   ├── base_validator.py         # Базовый валидатор
│   └── openapi_validator.py      # Валидатор OpenAPI
├── generation/                   # Генерация кода
│   ├── __init__.py
│   └── go_service_generator.py   # Генератор Go сервисов
└── [скрипты]                     # Конкретные скрипты
```

#### 📊 **Рефакторенные скрипты:**

| Скрипт | Статус | Описание |
|--------|--------|----------|
| `batch-optimize-openapi-struct-alignment.py` | OK | Массовый оптимизатор OpenAPI |
| `fix-all-openapi-warnings.py` | OK | Исправление всех OpenAPI warnings |
| `fix-common-refs.py` | OK | Исправление общих ссылок |
| `fix-openapi-warnings.py` | OK | Исправление OpenAPI warnings |
| `generate-all-domains-go.py` | OK | Генератор Go сервисов |
| `reorder-liquibase-columns.py` | OK | Оптимизация порядка колонок SQL |
| `reorder-openapi-fields.py` | OK | Оптимизация порядка полей OpenAPI |
| `validate-kafka-schemas.py` | ⏳ | Валидация Kafka схем |
| `validate-all-migrations.py` | ⏳ | Валидация всех миграций |
| `validate-domains-openapi.py` | ⏳ | Валидация OpenAPI доменов |
| `framework.py` | ⏳ | Фреймворк скриптов |

#### ⚙️ **Глобальный конфиг проекта:**

Создан `project-config.yaml` с настройками:
- Ограничения на размер файлов (1000 строк)
- Запрещенные расширения файлов
- Настройки OpenAPI, базы данных, валидации
- Параметры логирования и производительности

## [TRANSPORT]️ Как создать новый Python скрипт

### Используй новую архитектуру:

```python
#!/usr/bin/env python3
from scripts.core.base_script import BaseScript
from scripts.openapi.openapi_manager import OpenAPIManager

class MyScript(BaseScript):
    def __init__(self):
        super().__init__("my-script", "Description of what it does")
        self.openapi_manager = OpenAPIManager(
            self.file_manager, self.command_runner, self.logger
        )

    def add_script_args(self):
        self.parser.add_argument('--input', required=True, help='Input file')
        self.parser.add_argument('--output', help='Output file')

    def run(self):
        args = self.parse_args()

        # Используй компоненты
        self.logger.info(f"Processing {args.input}")

        if args.dry_run:
            self.logger.info("DRY RUN: no changes will be made")
            return

        # Твоя логика здесь...

if __name__ == "__main__":
    script = MyScript()
    script.main()
```

### Запуск:
```bash
python scripts/my_script.py --help
python scripts/my_script.py --input file.txt --verbose
python scripts/my_script.py --dry-run  # безопасный тест
```

## [BOOK] Возможности новой архитектуры

### Автоматически предоставляется:

- **Логирование**: структурированное с уровнями (DEBUG, INFO, WARNING, ERROR)
- **Обработка аргументов**: `--verbose`, `--dry-run`, `--config`
- **Валидация окружения**: проверка Python версии, наличия проекта
- **Обработка ошибок**: понятные сообщения и exit codes
- **Запуск команд**: `self.command_runner.run(['git', 'status'])`
- **Работа с файлами**: `self.file_manager.read_yaml()`, `write_yaml()`
- **OpenAPI операции**: `self.openapi_manager.validate_with_redocly()`
- **SQL обработка**: специализированные процессоры для разных типов SQL

### Специализированные менеджеры:
- **OpenAPIManager**: валидация, оптимизация, генерация
- **LiquibaseProcessor**: обработка SQL миграций
- **GoServiceGenerator**: генерация Go кода

### Утилиты:
```bash
# Показать все Python скрипты
python scripts/framework.py --list-scripts

# Проверить синтаксис всех скриптов
python scripts/framework.py --validate-scripts

# Проверить конфиг
python -c "from scripts.core.config import ConfigManager; print(ConfigManager().load_config())"
```

## [SYMBOL] Конвертация существующих скриптов

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

## [ALERT] Git Hook блокирует запрещенные скрипты

При попытке закоммитить `.sh`/`.ps1`/`.bat` файл в `scripts/`:

```
[BLOCKED] COMMIT BLOCKED: FORBIDDEN SCRIPT TYPE DETECTED!

SCRIPT LANGUAGE POLICY ENFORCEMENT:
• [OK] ALLOWED: .py (Python scripts)
• [ERROR] FORBIDDEN: .sh, .ps1, .bat, .cmd, .pl, .rb, .js

WHY THIS IS ENFORCED:
• Python is cross-platform and maintainable
• Shell scripts cause platform compatibility issues
• Python has better error handling and testing
• Single language reduces cognitive load
```

## [TARGET] Следующие шаги

1. **Начать миграцию** с высокоприоритетных скриптов
2. **Использовать фреймворк** для всех новых скриптов
3. **Тестировать** конвертированные скрипты
4. **Удалять** старые скрипты после успешной миграции
5. **Обновлять документацию** с новыми Python командами

## [SYMBOL] Нужна помощь?

- Посмотри примеры в `scripts/*.py`
- Используй `python scripts/framework.py --help`
- Создай Issue для сложных конвертаций
