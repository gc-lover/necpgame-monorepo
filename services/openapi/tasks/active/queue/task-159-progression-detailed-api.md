# Task ID: API-TASK-159
**Тип:** API Generation | **Приоритет:** средний | **Статус:** queued
**Создано:** 2025-11-07 11:22 | **Создатель:** AI Agent | **Зависимости:** API-TASK-140

---

## 📋 Описание

Создать API для детализации системы прокачки (4 документа). Attributes matrix, skills mapping, skills-classes.

---

## 📚 Источники (4 документа)

- `.BRAIN/02-gameplay/progression/progression-attributes.md` (v1.0.0)
- `.BRAIN/02-gameplay/progression/progression-attributes-matrix.md` (v1.0.0)
- `.BRAIN/02-gameplay/progression/progression-skills-classes.md` (v1.0.0)
- `.BRAIN/02-gameplay/progression/progression-skills-mapping.md` (v1.0.0)

**Ключевые механики:**
- Attributes: 9 атрибутов (STR, DEX, CON, INT, WIS, CHA, TECH, COOL, LUCK)
- Attributes matrix: стартовые бонусы по классам, рост/капы
- Skills-classes: классовые модификаторы навыков
- Skills mapping: соответствия навыков к предметам и имплантам

---

## 📁 Целевая структура

```
api/v1/progression/
├── attributes.yaml
├── attributes-matrix.yaml
├── skills-classes.yaml
└── skills-mapping.yaml
```

---

## 🏗️ Целевая архитектура

### Backend (микросервис):

**Микросервис:** gameplay-service  
**Порт:** 8083  
**API пути:** /api/v1/progression/attributes/*, /api/v1/progression/skills/*

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend (модуль):

**Модуль:** progression  
**Путь:** modules/progression/attributes  
**State Store:** useProgressionStore (attributes, skillModifiers, mapping)

### Frontend (библиотеки):

**UI компоненты (@shared/ui):**
- StatBlock, AttributeDisplay, SkillTree, MatrixTable

**Готовые формы (@shared/forms):**
- AttributeAssignmentForm

**Layouts (@shared/layouts):**
- GameLayout

**Хуки (@shared/hooks):**
- useCharacter (для current attributes)

---

## ✅ Endpoints

1. **GET /api/v1/progression/attributes** - Список атрибутов
2. **GET /api/v1/progression/attributes/formulas** - Формулы производных
3. **GET /api/v1/progression/attributes/matrix** - Матрица по классам
4. **GET /api/v1/progression/skills/class-modifiers** - Классовые модификаторы
5. **GET /api/v1/progression/skills/mapping** - Маппинг к предметам

**Models:** Attribute, AttributeMatrix, SkillModifier, SkillMapping

---

**Источники:** 4 progression документа

