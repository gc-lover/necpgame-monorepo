# Task ID: API-TASK-101
**Тип:** API Generation  
**Приоритет:** critical  
**Статус:** completed  
**Создано:** 2025-11-09 18:24  
**Завершено:** 2025-11-09 20:37  
**Исполнитель:** АПИТАСК

---

## 📋 Краткое описание

Спецификация `gameplay-service` для системы прогрессии: уровни, опыт, распределение атрибутных и skill points, история и respec.

---

## ✅ Выполнено

- Создан основной контракт `progression-core.yaml` (≤ 400 строк) с эндпоинтами состояния, начисления опыта (batch), распределения очков, skill xp, истории, respec и синхронизации.
- Подготовлены файлы моделей:
  - `progression-core-models.yaml` — схемы уровня, пунктов, атрибутов, навыков и истории.
  - `progression-core-models-operations.yaml` — запросы/ответы, события `gameplay.progression.*`, батчевые payloadы.
- Добавлен `README.md` со структурой каталога.
- Описаны ограничения (капы атрибутов/навыков, rate limit на spend), интеграции с combat, quest, character, economy и notification сервисами.
- Валидация `validate-swagger.ps1` успешно пройдена.

---

## 🔗 Спецификации

- `api/v1/gameplay/progression/progression-core/progression-core.yaml`
- `api/v1/gameplay/progression/progression-core/progression-core-models.yaml`
- `api/v1/gameplay/progression/progression-core/progression-core-models-operations.yaml`

---

## 🧾 Источники

- `.BRAIN/05-technical/backend/progression-backend.md` v1.0.0
- `.BRAIN/02-gameplay/progression/progression-attributes.md`
- `.BRAIN/02-gameplay/progression/progression-skills.md`
- `.BRAIN/05-technical/backend/player-character-mgmt/character-management.md`
- `.BRAIN/05-technical/backend/quest-engine-backend.md`
- `.BRAIN/02-gameplay/combat/combat-shooting.md`

---

## 📈 Передано

- Gameplay Service (прогрессия и события)
- Character Service (синхронизация атрибутов и навыков)
- Notification Service (уведомления об уровне и milestone)
- Frontend Agent (модуль `modules/progression/core`, Orval клиент `@api/gameplay/progression`)

