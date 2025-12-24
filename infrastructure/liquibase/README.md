# NECPGAME Liquibase Migrations

## Структура директорий

```
infrastructure/liquibase/
├── changelog.yaml                    # Главный changelog Liquibase
├── README.md                         # Эта документация
└── migrations/
    ├── schema/                      # DDL - создание схем и таблиц
    │   ├── V1_00__create_schemas.sql           # Создание основных схем
    │   ├── V1_50__content_quest_definitions_table.sql
    │   ├── V1_51__content_npc_definitions_table.sql
    │   ├── V1_52__content_dialogue_nodes_table.sql
    │   ├── V1_53__content_lore_entries_table.sql
    │   ├── V1_60__content_items_table.sql
    │   ├── V1_61__content_enemies_table.sql
    │   ├── V1_62__content_interactives_table.sql
    │   └── V1_70__project_documentation_table.sql
    │
    └── data/                        # DML - наполнение данными
        ├── gameplay/                # Игровые данные
        │   ├── quests/              # Миграции квестов
        │   │   ├── beatles-revival-london-2078-2093.yaml
        │   │   └── ... (1 файл = 1 квест)
        │   └── items/               # Предметы и оружие
        │       └── weapons/
        │
        ├── narrative/               # Повествовательные данные
        │   ├── npcs/                # NPC персонажи
        │   │   ├── common/
        │   │   ├── factions/
        │   │   ├── important/
        │   │   └── ... (1 файл = 1 NPC)
        │   └── dialogues/           # Диалоги
        │       └── ... (1 файл = 1 диалог)
        │
        ├── knowledge/               # Знания и лор
        │   ├── lore/                # Лор и история
        │   │   ├── characters/
        │   │   ├── factions/
        │   │   ├── locations/
        │   │   └── ... (1 файл = 1 лор запись)
        │   ├── culture/             # Культура и общество
        │   └── enemies/             # Враги и противники
        │
        └── project/                 # Проектная документация
            └── documentation/       # Вся документация (mechanics, implementation, design)
                └── ... (1 файл = 1 документ)
```

## Схемы базы данных

- **gameplay.** - Игровые механики (квесты, предметы, прогрессия)
- **narrative.** - Повествование (NPC, диалоги, события)
- **knowledge.** - Знания (лор, враги, интерактивы, культура)
- **project.** - Проектная документация (архитектура, дизайн, механики, реализации)

## Порядок выполнения миграций

1. **Schema migrations** (V1_00__ до V1_70__) - создание таблиц
2. **Data migrations** - наполнение данными

## Генерация миграций данных

### SOLID архитектура (рекомендуемый подход):

```bash
# Запуск всех генераторов через SOLID архитектуру
python scripts/generate-all-content-migrations-solid.py

# Отдельные генераторы (рефакторированные по SOLID)
python scripts/generate-quests-migrations-refactored.py
python scripts/generate-npcs-migrations-refactored.py
python scripts/generate-dialogues-migrations-refactored.py
python scripts/generate-lore-migrations-refactored.py
python scripts/generate-enemies-migrations-refactored.py
python scripts/generate-interactives-migrations-refactored.py
python scripts/generate-items-migrations-refactored.py
python scripts/generate-culture-migrations-refactored.py
python scripts/generate-documentation-migrations-refactored.py
```

### Особенности генерации:

- **1 YAML файл = 1 миграция**: Каждый файл из knowledge/ создает отдельную миграцию
- **Версионность**: Изменения файлов отслеживаются по MD5 хэшу
- **Checksum-safe**: Новые миграции создаются при изменениях, старые не перезаписываются
- **Читаемые имена**: `data_{type}_{filename}_{hash}_{timestamp}.yaml`
- **Liquibase-compatible**: Все миграции имеют правильный формат для Liquibase

### Формат именования файлов:

```
data_{type}_{filename}_{hash}_{timestamp}.yaml
```

Примеры:
- `data_quest_beatles-revival-london-2078-2093_a1b2c3d4_20251224003015.yaml`
- `data_npc_jackie-welles_e5f6g7h8_20251224003020.yaml`
- `data_lore_main-story-overview_i9j0k1l2_20251224003025.yaml`

Где:
- `data_{type}` - тип контента (quest, npc, lore, etc.)
- `{filename}` - имя исходного YAML файла
- `{hash}` - MD5 хэш файла (8 символов) для версионности
- `{timestamp}` - время генерации в формате YYYYMMDDHHMMSS

## Источники данных

- **knowledge/canon/** - Канонический контент игры
- **knowledge/content/** - Игровой контент и ассеты
- **YAML файлы** преобразуются в Liquibase миграции

## Типы поддерживаемых данных

- ✅ **Квесты** - quest definitions с наградами, целями, требованиями
- ✅ **NPC** - персонажи с характеристиками, диалогами, фракциями
- ✅ **Диалоги** - узлы диалогов с условиями и действиями
- ✅ **Лор** - записи знаний по категориям (персонажи, фракции, локации)
- ✅ **Предметы** - оружие, броня, расходники с характеристиками
- ✅ **Враги** - противники с характеристиками и поведением
- ✅ **Интерактивы** - объекты мира с взаимодействиями
- ✅ **Культура** - культурные аспекты и общество игры
- ✅ **Документация** - ВСЯ проектная документация (mechanics, implementation, design, analysis)

## Особенности

- **Mirror approach**: Структура миграций повторяет структуру исходных YAML файлов
- **1:1 mapping**: Каждый YAML файл → одна миграция
- **Отслеживание**: Легко понять, какие файлы уже обработаны
- **Обновление**: При изменении YAML → пересоздание миграции
- **JSONB поля**: Гибкое хранение сложных данных (награды, требования, метаданные)
