# Task ID: API-TASK-096
**Тип:** API Generation  
**Приоритет:** high  
**Статус:** completed  
**Создано:** 2025-11-09 16:40  
**Завершено:** 2025-11-09 19:17  
**Исполнитель:** АПИТАСК

---

## 📋 Краткое описание

Спецификация `social-service` для управления отношениями игрока с NPC, включая многослойные шкалы (affinity, trust, loyalty, mood, romance), аудит взаимодействий, мировые события и модерацию.

---

## ✅ Выполнено

- Создан основной контракт `npc-relationships.yaml` (≤ 400 строк) с эндпоинтами статуса отношений, пакетных корректировок, логов взаимодействий, романтики, истории, мировых событий, модерации и метрик.
- Подготовлены файлы моделей:
  - `npc-relationships-models.yaml` — параметры, базовые схемы слоёв отношений, классовых и фракционных модификаторов, событий, метрик и кейсов модерации.
  - `npc-relationships-models-operations.yaml` — запросы/ответы REST операций, payload Kafka топиков и определения очередей.
- Добавлен `README.md` со структурой каталога.
- Примеры включают статус NPC с детализацией доверия и романтики, ответы истории и события мира.
- Описаны Kafka payloadы `social.npc-relationship.changed`, `social.npc-romance.state`, `world.npc-relationship.event`, а также очереди `npc-relationship-review`, `npc-romance-validation`.
- Валидация `validate-swagger.ps1` выполнена без ошибок.

---

## 🔗 Спецификации

- `api/v1/social/npc-relationships/npc-relationships.yaml`
- `api/v1/social/npc-relationships/npc-relationships-models.yaml`
- `api/v1/social/npc-relationships/npc-relationships-models-operations.yaml`

---

## 🧾 Источники

- `.BRAIN/02-gameplay/social/npc-relationships-system-детально.md` v1.0.0
- `.BRAIN/02-gameplay/social/personal-npc-tool.md`
- `.BRAIN/02-gameplay/world/world-state/player-impact-systems.md`
- `.BRAIN/02-gameplay/social/npc-hiring-world-impact-детально.md`
- `.BRAIN/02-gameplay/economy/economy-contracts.md`
- `.BRAIN/03-lore/_03-lore/factions/factions-overview-детально.md`
- `.BRAIN/05-technical/backend/social-service-overview.md`

---

## 📈 Передано

- Social Service (core отношения и модерация)
- Gameplay Service (миссии и условия романтики)
- Frontend Agent (модули `modules/social/npc-relations`, `modules/world/insights`)

