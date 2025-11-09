---
title: "Диалоговый файл — шаблон"
version: "1.0.0"
status: "approved"
updated: "2025-11-07"
author: "Brain Manager"
api-readiness: "ready"
**api-readiness-check-date:** "2025-11-07 17:45"
api-readiness-notes: "Шаблон диалогов NPC/квестов с состояниями, узлами, проверками и метаданными."
target-domain: "narrative"
---

---

# {{display-name}}

**ID диалога:** `dialogue-{{slug}}`  
**Тип:** npc | quest | ambient  
**Статус:** draft | review | approved  
**Версия:** 1.0.0  
**Дата создания:** YYYY-MM-DD  
**Последнее обновление:** YYYY-MM-DD  
**Приоритет:** medium  
**Связанные документы:** `../npc-lore/...`, `../quests/...`  
**target-microservice:** narrative-service (port 8087)  
**target-frontend-module:** modules/narrative/{{module}}  
**api-readiness:** in-review  
**api-readiness-check-date:** YYYY-MM-DD HH:mm
**api-readiness-notes:** Краткое пояснение статуса проверки.

---

## 1. Контекст и цели
- NPC / квест / событие, основная роль.
- Ключевые сюжетные моменты и желаемый эмоциональный тон.
- Интеграции с системами (репутация, события, мир).

## 2. Состояния и условия

| Состояние | Описание | Триггеры | Используемые флаги |
|-----------|----------|----------|---------------------|
| base | Базовое состояние | пример | `rep.corp.example` |
| ... | ... | ... | ... |

- Сопутствующие флаги/репутации.
- D&D проверки и связанные квестовые узлы.
- Внешние события, влияющие на диалог.

## 3. Структура диалога

### 3.1 Приветствия/entry точки

| Состояние | Реплика NPC | Условия | Ответы игрока |
|-----------|-------------|---------|---------------|
| base | «...», — {{npc}} | default | `["Ответ1", "Ответ2"]` |

### 3.2 Узлы

```yaml
- node-id: example
  label: Описание узла
  entry-condition: state == "base"
  player-options:
    - option-id: sample
      text: "Команда игрока"
      requirements:
        - type: stat-check
          stat: Persuasion
          dc: 18
      npc-response: "..."
      outcomes:
        success: { effect: "grant_flag", flag: "flag.example" }
        failure: { effect: "apply_penalty", reputation: -3 }
```

### 3.3 Таблица проверок D&D

| Узел | Тип проверки | DC | Модификаторы | Успех | Провал | Крит. успех | Крит. провал |
|------|--------------|----|--------------|-------|--------|-------------|--------------|

### 3.4 Реакции на события
- **Событие:** `world.event.example`
  - **Условие:** ...
  - **Реплика:** ...
  - **Последствия:** ...

## 4. Награды и последствия
- Репутация/флаги.
- Предметы/контракты.
- Связь с world-state/quest-branching.

## 5. Связанные материалы
- `../npc-lore/...`
- `../quests/...`
- `../../02-gameplay/...`

## 6. История изменений
- YYYY-MM-DD — описание апдейта.
# Шаблон диалогового файла

**ID диалога:** `dialogue-[npc-or-quest-id]`  
**Тип:** npc | quest | event | romance  
**Статус:** draft  
**Версия:** 0.1.0  
**Дата создания:** YYYY-MM-DD  
**Последнее обновление:** YYYY-MM-DD HH:MM  
**Приоритет:** medium  
**Связанные документы:** `[список путей через запятую]`  
**target-domain:** narrative  
**target-microservice:** narrative-service (port 8087)  
**target-frontend-module:** modules/narrative/quests  
**api-readiness:** in-review  
**api-readiness-check-date:** YYYY-MM-DD HH:MM
**api-readiness-notes:** Краткое описание состояния готовности

---

## 1. Контекст и цели

- **NPC / Квест:** краткое описание роли персонажа или сцены.
- **Стадия прогресса:** стартовая встреча, середина задания, финал, реакция на событие.
- **Краткий синопсис:** один абзац о цели диалога и ключевых эмоциональных точках.

## 2. Состояния и условия

| Состояние | Описание | Триггеры | Используемые флаги |
|-----------|----------|----------|---------------------|
| base      | Базовое поведение | Всегда | `flag.example` |
| trusted   | Высокая репутация | `reputation >= 40` | `rep.corp.arasaka` |
| hostile   | Низкая репутация | `reputation <= -20` | `rep.gang.maelstrom` |

- **Репутация:** укажите пороги и ссылки на `02-gameplay/social/reputation-formulas.md`.
- **Мировые события:** связь с `02-gameplay/world/events/...` (какие идентификаторы событий влияют на реплики).
- **Проверки D&D:** список узлов из `quest-dnd-checks.md`, которые активируют альтернативные ветки.

## 3. Структура диалога

### 3.1. Приветствия

| Состояние | Реплика NPC | Условия | Ответы игрока |
|-----------|-------------|---------|---------------|
| base      | «Привет, чомбата. Я [Имя]. Чем могу помочь?» | default | `["Мне нужна работа", "Расскажи о себе", "Пока"]` |
| trusted   | «Рад тебя видеть снова. У меня есть кое-что особенное.» | `rep >= 40` | `["О заданиях", "Что нового?", "Позже"]` |
| hostile   | «Не задерживайся. У меня мало времени.» | `rep <= -20` | `["Мне нужна помощь", "Я ухожу"]` |

### 3.2. Узлы диалога

```
- node-id: node-1
  label: Первичное знакомство
  entry-condition: state == "base"
  player-options:
    - option-id: opt-1
      text: "Мне нужна работа"
      requirements: []
      npc-response: "Есть контракт. Проверка Persuasion DC 15."
      outcomes:
        success: { effect: "grant_contract", reputation: +5 }
        failure: { effect: "delay_offer", reputation: 0 }
        critical-success: { effect: "bonus_reward", reputation: +8 }
        critical-failure: { effect: "cooldown", reputation: -5 }
    - option-id: opt-2
      text: "Расскажи о себе"
      npc-response: "[краткая биография]"
      outcomes: { default: { effect: "unlock_codex_entry" } }
```

Используйте YAML-подобную структуру для однозначного маппинга в будущие API.

### 3.3. Ветвление по проверкам

| Узел | Тип проверки | DC | Модификаторы | Успех | Провал | Крит. успех | Крит. провал |
|------|--------------|----|--------------|-------|--------|-------------|--------------|
| node-1.opt-1 | Persuasion | 15 | `+2` костюм | Открывается контракт | Отложенный оффер | Бонус награда | Кулдаун 1 ч |

### 3.4. Реакции на события

- **Событие:** `world.event.blackwall_breach`
  - **Условие:** событие активировано, игрок завершил `main/042-black-barrier-heist`
  - **Реплика:** «Небо мерцает, чомбата. Blackwall снова звенит.»
  - **Последствия:** `reputation.vdboys +5`, доступ к ветке `node-blackwall`

## 4. Награды и последствия

- **Репутация:** перечислите изменения по фракциям.
- **Предметы/валюта:** опишите награды («корпорат-сет Mk1», `+500 eddies`).
- **Флаги:** добавьте список флагов/переменных, которые устанавливаются после сцены.
- **Влияние на world-state:** ссылка на таблицы из `quest-branching-database`.

## 5. Связанные материалы

- NPC лор: `04-narrative/npc-lore/important/[npc].md`
- Квест: `04-narrative/quests/[path]/[quest-id].md`
- Системы: `02-gameplay/social/reputation-formulas.md`, `02-gameplay/world/events/world-events-framework.md`
- UI: `05-technical/ui/dialogue-system.md` (если существует)

## 6. История изменений

- YYYY-MM-DD HH:MM — создан файл на основе шаблона.

