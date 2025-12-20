# Quest Import Guide

## Issue: #50

Скрипты для импорта контентных квестов из YAML файлов в базу данных через API.

## Требования

- Python 3.x (для конвертации YAML → JSON)
- PowerShell (для Windows)
- Запущенный `gameplay-service-go` на `http://localhost:8083`
- База данных PostgreSQL с примененными миграциями (таблица `gameplay.quest_definitions`)

## Использование

### Импорт одного квеста

```powershell
.\scripts\import-quest.ps1 -QuestFile "knowledge/canon/lore/timeline-author/quests/america/las-vegas/2020-2029/quest-001-strip.yaml"
```

### Импорт всех квестов из директории

```powershell
.\scripts\import-quests-batch.ps1 -QuestDir "knowledge/canon/lore/timeline-author/quests/america/las-vegas/2020-2029"
```

### С кастомным API URL и токеном

```powershell
.\scripts\import-quest.ps1 `
    -QuestFile "knowledge/canon/lore/timeline-author/quests/america/las-vegas/2020-2029/quest-001-strip.yaml" `
    -ApiUrl "http://localhost:8083/api/v1/gameplay/quests/content/reload" `
    -AuthToken "your-jwt-token"
```

## Структура YAML квеста

Скрипт ожидает YAML файл со следующей структурой:

```yaml
metadata:
  id: quest-vegas-2029-strip
  title: 'Лас-Вегас 2020-2029 — Прогулка по Стрипу'

summary:
  goal: "Описание цели квеста"
  essence: "Краткое описание квеста"

quest_definition:
  quest_type: side
  level_min: 3
  level_max: null
  requirements:
    required_quests: []
    required_flags: []
  objectives:
    - id: welcome_sign_photo
      text: "Сделать фото у знака"
      type: interact
  rewards:
    xp: 1500
    currency: 0
```

## API Endpoint

**POST** `/api/v1/gameplay/quests/content/reload`

**Request Body:**

```json
{
  "quest_id": "quest-vegas-2029-strip",
  "yaml_content": {
    "metadata": {...},
    "summary": {...},
    "quest_definition": {...}
  }
}
```

**Response:**

```json
{
  "quest_id": "quest-vegas-2029-strip",
  "message": "Quest imported successfully",
  "imported_at": "2025-12-06T11:30:00Z"
}
```

## Обработка в Handler

Handler (`ReloadQuestContent`) извлекает из YAML:

- `quest_id` из `metadata.id`
- `title` из `summary.goal` или `metadata.title`
- `description` из `summary.essence`
- `quest_type` из `quest_definition.quest_type`
- `level_min`, `level_max` из `quest_definition`
- `requirements`, `objectives`, `rewards` из `quest_definition`
- Полный YAML сохраняется в `content_data` (JSONB)

## Проверка импорта

После импорта можно проверить через SQL:

```sql
SELECT quest_id, title, quest_type, level_min, level_max, is_active
FROM gameplay.quest_definitions
WHERE quest_id = 'quest-vegas-2029-strip';
```

## Troubleshooting

### Ошибка: "Python is not installed"

Установите Python 3.x и добавьте в PATH.

### Ошибка: "Quest file not found"

Проверьте путь к файлу. Используйте абсолютный путь или относительный от корня репозитория.

### Ошибка: "Failed to import quest (HTTP 500)"

- Проверьте, что сервис запущен
- Проверьте логи сервиса
- Убедитесь, что база данных доступна
- Проверьте, что миграции применены

### Ошибка: "questRepository not initialized"

Убедитесь, что сервис запущен с подключением к БД (переменная окружения `DATABASE_URL`).

