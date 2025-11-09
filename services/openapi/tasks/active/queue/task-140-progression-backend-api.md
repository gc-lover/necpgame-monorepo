# Task ID: API-TASK-140
**Тип:** API Generation  
**Приоритет:** критический  
**Статус:** queued  
**Создано:** 2025-11-07 10:40  
**Создатель:** AI Agent  
**Зависимости:** API-TASK-127

---

## 📋 Краткое описание
**MVP блокер.** Нужна спецификация системы прогрессии: опыт, уровни, атрибуты, навыки, rebirth.

**Что нужно сделать:** описать API gameplay-service по `.BRAIN/05-technical/backend/progression-backend.md`.

---

## 🎯 Цель задания
Обеспечить сервис прокачки, который начисляет опыт, повышает уровни, распределяет атрибуты и отслеживает скиллы.

**Зачем это нужно:**
- Основной источник роста персонажа и разблокировки контента.  
- Интеграция с combat, quests, achievements, economy.  
- База для балансировки и сезонных систем.

---

## 📚 Источники информации

### Основной источник
**Путь:** `.BRAIN/05-technical/backend/progression-backend.md`  
**Версия:** v1.0.0 · **Статус:** ready · **Дата:** 2025-11-07  

**Ключевые элементы:**
- Experience pipelines, formulas, множители.  
- Attribute и skill point distribution, class progression.  
- Rebirth system (сброс уровня с бонусами).

### Дополнительные источники
- `.BRAIN/05-technical/backend/combat-session-backend.md` — источник exp.  
- `.BRAIN/05-technical/backend/quest-engine-backend.md` — награды за квесты.  
- `.BRAIN/05-technical/backend/achievement-system.md` — ачивки за уровни.  
- `.BRAIN/05-technical/backend/rebirth-design.md` — механика рестартов.

### Связанные документы
- `.BRAIN/02-gameplay/progression/level-curves.md` — формулы exp.  
- `.BRAIN/02-gameplay/classes/class-progression.md` — классовые бонусы.  
- `.BRAIN/05-technical/backend/save-system.md` — хранение данных.

---

## 📁 Целевая структура API
### Репозиторий: `API-SWAGGER`
**Целевой файл:** `api/v1/gameplay/progression/progression-engine.yaml`  
> ⚠️ Серверы: `https://api.necp.game/v1/gameplay` и `http://localhost:8080/api/v1/gameplay`.

**Тип:** OpenAPI 3.0.3 · **Версия:** v1

```
API-SWAGGER/
└── api/
    └── v1/
        └── gameplay/
            └── progression/
                └── progression-engine.yaml
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend
- **Микросервис:** gameplay-service  
- **Порт:** 8083  
- **API Base:** `/api/v1/gameplay/progression`  
- **Интеграции:** combat-service, quest-engine, achievement-system, economy-service (rebirth costs), analytics, notification-service.  
- **Комментарий:**
  ```yaml
  # Target Architecture:
  # - Microservice: gameplay-service (port 8083)
  # - API Base: /api/v1/gameplay/progression
  # - Dependencies: combat-service, quest-engine, achievement-system, economy-service, notification-service
  # - Frontend Module: modules/progression/leveling
  # - UI: LevelProgressBar, AttributePanel, SkillTree, RebirthModal
  # - Forms: AttributeAssignmentForm, SkillUpgradeForm, RebirthConfirmForm
  # - Hooks: useProgressionStore, useRealtime, useAnalytics
  ```

### OpenAPI требования
- `info.x-microservice`:
  ```yaml
  x-microservice:
    name: gameplay-service
    port: 8083
    domain: gameplay
    base-path: /api/v1/gameplay/progression
    directory: api/v1/gameplay/progression
    package: com.necpgame.gameplayservice
  ```
- `servers` как указано.  
- `x-websocket`: `wss://api.necp.game/v1/gameplay/progression/characters/{characterId}/stream` — уведомления о level up/skill up.

### Frontend
- **Модуль:** `modules/progression/leveling`.  
- **State Store:** `useProgressionStore` (`level`, `experience`, `attributes`, `skills`, `rebirth`).  
- **UI:** LevelProgressBar, AttributePanel, SkillTreeView, RebirthModal, ExperienceHistory.  
- **Формы:** AttributeAssignmentForm, SkillUpgradeForm, RebirthConfirmForm.  
- **Хуки:** useRealtime, useAnalytics, useCharacter.  
- **Layouts:** GameLayout (профиль персонажа).

---

## ✅ Что нужно сделать

### Шаг 1. Анализ
- Разложить формулы exp, уровней, skill exp.  
- Определить лимиты атрибутов, классовые ветки, rebirth bonuses.  
- Требования к audit log (история начислений).

### Шаг 2. Endpoints
1. **POST `/api/v1/gameplay/progression/experience/award`** — начисление exp (service token).  
2. **POST `/api/v1/gameplay/progression/skills/experience`** — skill exp.  
3. **POST `/api/v1/gameplay/progression/level-up`** — подтверждение повышения уровня.  
4. **POST `/api/v1/gameplay/progression/attributes/assign`** — распределение атрибутов.  
5. **POST `/api/v1/gameplay/progression/skills/upgrade`** — повышение скилла.  
6. **POST `/api/v1/gameplay/progression/rebirth`** — запуск rebirth.  
7. **GET `/api/v1/gameplay/progression/characters/{characterId}`** — состояние прогрессии.  
8. **GET `/api/v1/gameplay/progression/level-requirements`** — таблица exp уровней.  
9. **GET `/api/v1/gameplay/progression/history`** — журнал начислений/изменений.  
10. **GET `/api/v1/gameplay/progression/classes/{classId}`** — классовые ветки и перки.

### Шаг 3. Модели
- `ProgressionSnapshot`, `ExperienceAwardRequest`, `LevelUpResult`, `AttributeAssignmentRequest`, `SkillUpgradeRequest`, `RebirthRequest`, `RebirthResult`, `LevelRequirement`, `ProgressionHistoryEntry`.  
- Ошибки: `ProgressionError` (`VAL_NOT_ENOUGH_POINTS`, `VAL_MAX_LEVEL`, `BIZ_REBIRTH_LOCKED`, `BIZ_SKILL_LOCKED`).  
- WebSocket payload: `levelUp`, `skillUp`, `attributeAssigned`, `rebirthStarted`.

### Шаг 4. OpenAPI оформление
- `paths` с примерами, параметры (`characterId`, `classId`).  
- В `components` вынести схемы, enum классов, атрибутов, skill types.  
- `security`: `BearerAuth` (для игрока), `ServiceToken` для внутренних начислений.  
- Примеры: exp award, level up response, attribute assignment, rebirth.

### Шаг 5. Проверки
- `scripts/validate-swagger.ps1 -ApiDirectory API-SWAGGER/api/v1/gameplay/progression/`.  
- Проверка лимитов файла, README.  
- Обновить brain-mapping, `.BRAIN`, README `gameplay/progression`.

---

## 🔍 Критерии приемки
1. `info.x-microservice` = `gameplay-service`, порт `8083`, домен `gameplay`.  
2. Все маршруты под `/api/v1/gameplay/progression`.  
3. Поддерживаются exp award, level up, attributes, skills, rebirth, history.  
4. WebSocket события описаны.  
5. Ошибки используют общую модель `Error`.  
6. Примеры покрывают ключевые сценарии.  
7. Валидаторы проходят без ошибок.  
8. Обновлены brain-mapping и `.BRAIN`.  
9. README каталога содержит описание API.  
10. Ограничения (max level, rebirth cooldown) указаны.  
11. Internal endpoints защищены `ServiceToken`.

---

## FAQ
- **Можно ли выдать exp вручную?** Через internal endpoint с `ServiceToken`.  
- **Как пересчитать уровни после rebirth?** Endpoint rebirth возвращает новую базовую кривую.  
- **Поддерживаются мультиклассы?** Да, в `class progression` описать структуру веток.  
- **Нужны ли offline начисления?** Предусмотреть batch sync (`history`).  
- **Интеграция с achievements?** Возвращать события `levelUp` для world-service.

---

**Источник:** `.BRAIN/05-technical/backend/progression-backend.md` (v1.0.0, ready)

