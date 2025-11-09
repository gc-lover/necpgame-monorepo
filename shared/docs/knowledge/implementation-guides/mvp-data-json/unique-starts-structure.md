# Структура данных для уникальных стартов

**Статус:** draft  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-05  
**Последнее обновление:** 2025-11-05  

---

## Общее описание

Описание структуры данных для всех 45 уникальных стартов (3 происхождения × 5 фракций × 3 класса).

**Примечание:** Полный JSON файл с данными для всех 45 комбинаций будет создан позже, так как это большой объем данных. Сейчас создана структура и примеры.

---

## Структура данных

### Формат записи старта

```json
{
  "id": "start-{origin}-{faction}-{class}",
  "origin": "street_kid|corpo|nomad",
  "faction": "arasaka|militech|valentinos|maelstrom|ncpd",
  "class": "solo|netrunner|techie",
  "startingLocation": {
    "id": "loc-{location-id}",
    "name": "Название локации",
    "description": "Описание локации"
  },
  "startingNpc": {
    "id": "npc-{npc-id}",
    "name": "Имя NPC",
    "role": "fixer|trader|quest_giver|guard|citizen|enemy",
    "description": "Описание NPC"
  },
  "firstQuest": {
    "id": "quest-start-{origin}-{faction}-{class}",
    "name": "Название квеста",
    "description": "Описание квеста"
  },
  "introDialogue": {
    "greeting": "Приветствие NPC",
    "options": [
      {
        "id": "option-id",
        "text": "Текст опции",
        "response": "Ответ NPC"
      }
    ]
  },
  "startingReputation": {
    "faction-id": "integer"
  },
  "startingEquipment": {
    "base": ["item-id-1", "item-id-2"],
    "additional": ["item-id-3"]
  }
}
```

---

## Матрица комбинаций

### Все 45 комбинаций:

**Происхождения:**
1. Street Kid
2. Corpo
3. Nomad

**Фракции:**
1. Arasaka
2. Militech
3. Valentinos
4. Maelstrom
5. NCPD

**Классы:**
1. Solo
2. Netrunner
3. Techie

**Всего:** 3 × 5 × 3 = **45 комбинаций**

---

## TODO

**TODO:** Создать полный JSON файл с данными для всех 45 комбинаций - требуется для загрузки в БД.

**TODO:** Добавить детальные описания для каждой комбинации - требуется для полноты.

---

## История изменений

- v1.0.0 (2025-11-05) - Создание структуры данных для уникальных стартов

