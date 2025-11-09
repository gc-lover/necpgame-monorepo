# Task ID: API-TASK-148
**Тип:** API Generation | **Приоритет:** средний | **Статус:** queued
**Создано:** 2025-11-07 10:58 | **Создатель:** AI Agent | **Зависимости:** API-TASK-138

---

## 📋 Описание

Создать JSON Schema для квестов. Валидация структуры квестовых данных.

---

## 📚 Источник

**Документ:** `.BRAIN/05-technical/api-structures/quests-json-schema.md` (v1.0.0, ready)

**Содержит:** Полная JSON схема для квестов с skill-checks, диалогами, ветвлениями.

---

## 📁 Целевой файл

`api/schemas/quest-schema.json`

---

## ✅ Задача

Создать JSON Schema definition для валидации quest data. Использовать в OpenAPI как components/schemas.

**Схемы:** Quest, DialogueNode, SkillCheck, Branch, Reward, Condition

---

**Источник:** `.BRAIN/05-technical/api-structures/quests-json-schema.md`


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

