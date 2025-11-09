# Технические задания

**api-readiness:** not-applicable  
**api-readiness-check-date:** 2025-11-06 18:10
**api-readiness-notes:** Служебный файл-индекс раздела технических требований. Сам не содержит механик для API. Дочерние документы имеют свои статусы готовности.

Этот раздел содержит технические задания, требования к API, схемы БД и архитектурные решения.

## Архитектурный контекст

- Backend проекта полностью мигрирован на микросервисы; монолитные решения недопустимы.
- Production-доступ ко всем сервисам осуществляется через единый gateway `https://api.necp.game/v1` и `wss://api.necp.game/v1`.
- Все OpenAPI спецификации обязаны содержать корректный блок `info.x-microservice` (name, port, domain, base-path, package) для целевого микросервиса.

## Структура

### MVP (Минимальная версия продукта)
- `mvp-text-version-plan.md` - План и требования для MVP текстовой версии
- `ui-mvp-screens.md` - Детальное описание всех экранов MVP (требует создания)

### UI/UX (Интерфейсы)
- `ui-registration.md` - Экран регистрации ✅ ready
- `ui-character-creation.md` - Начальный интерфейс: выбор и создание персонажа ✅ ready
- `ui-game-start.md` - Начало игры и шутерная боевая петля ✅ ready
- `ui-main-game.md` - Основной игровой интерфейс (текстовая версия) ✅ ready

### Game Start Flow (Сценарий начала игры)
- `game-start-scenario.md` - Общий сценарий запуска игры ✅ ready
- `game-tutorial-flow.md` - Туториал и первые шаги игрока ✅ ready
- `game-first-quest-intro.md` - Первый квест и введение в игру ✅ ready

### Unique Starts (Уникальные старты)
- `game-start-unique-starts.md` - Общая структура уникальных стартов ✅ ready
- `game-start-by-origin.md` - Старты по происхождениям (Street Kid, Corpo, Nomad) ✅ ready
- `game-start-by-faction.md` - Старты по фракциям (Arasaka, Militech, Valentinos, Maelstrom, NCPD) ✅ ready
- `game-start-by-class.md` - Старты по классам (Solo, Netrunner, Techie) ✅ ready

### Content Generation (Контент и симуляции) → world-service, social-service, economy-service
- `content-generation/CONTENT-TEAM-GUIDE.md` - Руководство по массовой генерации контента ✅ ready
- `content-generation/city-life-population-algorithm.md` - Алгоритм наполнения городов NPC и инфраструктурой ⚠️ needs-work
- `content-generation/baseline/*.json` - Baseline пакеты городов (Watson, Westbrook, Shinjuku, Kreuzberg) для симуляций

### API Requirements (Требования к API)
- `api-requirements/mvp-endpoints.md` - Endpoints для MVP ✅ ready
- `api-requirements/mvp-data-models.md` - Модели данных для MVP ✅ ready
- `api-requirements/equipment-matrix-entities.md` - Equipment Matrix entities ✅ ready

### Shooter Attributes (характеристики шутера)
- `attributes-dnd-mapping.md` - [АРХИВ] сопоставление с D&D, не использовать
- `shooter-attributes.md` - Параметры точности, мобильности и стойкости для 3D-шутера ⚠️ in-review
- `ui-game-start.md` - UI потоки старта игры и боевой петли шутера ✅ ready

### Database (База данных)
- `database/schema.md` - Схема БД для MVP (требует создания)
- `database/migrations.md` - Миграции для MVP (требует создания)
- `mvp-initial-data.md` - Минимальный набор данных для MVP (требует создания)

### Architecture (Архитектура)
- `architecture/mvp-backend-architecture.md` - Архитектура бекенда для MVP (требует создания)
- `architecture/mvp-frontend-architecture.md` - Архитектура фронтенда для MVP (требует создания)

## Связь с API-SWAGGER

Технические документы должны ссылаться на соответствующие API спецификации в репозитории `API-SWAGGER`.

## Статус MVP

**Текущий этап:** Документация MVP + Контент

**Готово:**
- ✅ План MVP (mvp-text-version-plan.md)
- ✅ Регистрация (ui-registration.md) - ready
- ✅ Создание персонажа (ui-character-creation.md) - ready
- ✅ Основной интерфейс (ui-main-game.md) - ready
- ✅ API требования для MVP (api-requirements/mvp-endpoints.md)
- ✅ Модели данных для MVP (api-requirements/mvp-data-models.md)
- ✅ Минимальный набор данных (mvp-initial-data.md)
- ✅ JSON данные для MVP (mvp-data-json/*.json)
- ✅ Детальные тексты и контент (mvp-content/*)

**Требует работы:**
- ⚠️ UI спецификация для MVP (ui-mvp-screens.md)
- ⚠️ Архитектура бекенда для MVP
- ⚠️ Архитектура фронтенда для MVP
- ⚠️ SQL скрипты для миграций БД

