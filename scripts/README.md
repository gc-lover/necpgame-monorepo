# Scripts

Утилиты для работы с проектом.

## check_workflow_status.py

Скрипт для проверки статуса GitHub Actions workflow runs и jobs.

### Установка зависимостей

```bash
pip install requests
```

### Настройка

Установите переменную окружения с GitHub токеном:

```bash
# Windows (CMD)
set GITHUB_TOKEN=your_token_here

# Windows (PowerShell)
$env:GITHUB_TOKEN="your_token_here"

# Linux/Mac
export GITHUB_TOKEN=your_token_here
```

Или передайте токен через параметр `--token`.

### Использование

```bash
# Показать последние workflow runs
python scripts/check_workflow_status.py --latest

# Показать runs для конкретного коммита
python scripts/check_workflow_status.py --commit 6001e3813

# Показать runs для конкретного workflow
python scripts/check_workflow_status.py --workflow ci-backend.yml

# Показать детали конкретного run
python scripts/check_workflow_status.py --run-id 12345678

# Показать jobs для конкретного run
python scripts/check_workflow_status.py --jobs 12345678

# Вывести в формате JSON
python scripts/check_workflow_status.py --latest --json
```

### Пример вывода

```
📋 Found 5 workflow run(s):

────────────────────────────────────────────────────────────────────────────────────────────
🔄 Backend CI
   Status: ✅ SUCCESS
   Run ID: 12345678
   Commit: 6001e38 on develop
   Created: 2025-11-30T00:09:10Z
   URL: https://github.com/gc-lover/necpgame-monorepo/actions/runs/12345678
────────────────────────────────────────────────────────────────────────────────────────────

🔧 Jobs for run 12345678 (3 total):

────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────
⚙️  discover-services
   Status: ✅ SUCCESS
   Job ID: 87654321
   Started: 2025-11-30T00:09:15Z
   Completed: 2025-11-30T00:09:25Z
   Steps (3):
     • Checkout: ✅ SUCCESS
     • Discover changed Go services: ✅ SUCCESS
     • Post actions: ✅ SUCCESS
────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────
```

