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
- `ui-registration.md` - Экран регистрации OK ready
- `ui-character-creation.md` - Начальный интерфейс: выбор и создание персонажа OK ready
- `ui-game-start.md` - Начало игры и шутерная боевая петля OK ready
- `ui-main-game.md` - Основной игровой интерфейс (текстовая версия) OK ready

### Game Start Flow (Сценарий начала игры)
- `game-start-scenario.md` - Общий сценарий запуска игры OK ready
- `game-tutorial-flow.md` - Туториал и первые шаги игрока OK ready
- `game-first-quest-intro.md` - Первый квест и введение в игру OK ready

### Unique Starts (Уникальные старты)
- `game-start-unique-starts.md` - Общая структура уникальных стартов OK ready
- `game-start-by-origin.md` - Старты по происхождениям (Street Kid, Corpo, Nomad) OK ready
- `game-start-by-faction.md` - Старты по фракциям (Arasaka, Militech, Valentinos, Maelstrom, NCPD) OK ready
- `game-start-by-class.md` - Старты по классам (Solo, Netrunner, Techie) OK ready

### Content Generation (Контент и симуляции) → world-service, social-service, economy-service
- `content-generation/CONTENT-TEAM-GUIDE.md` - Руководство по массовой генерации контента OK ready
- `content-generation/city-life-population-algorithm.md` - Алгоритм наполнения городов NPC и инфраструктурой WARNING needs-work
- `content-generation/baseline/*.json` - Baseline пакеты городов (Watson, Westbrook, Shinjuku, Kreuzberg) для симуляций

### API Requirements (Требования к API)
- `api-requirements/mvp-endpoints.md` - Endpoints для MVP OK ready
- `api-requirements/mvp-data-models.md` - Модели данных для MVP OK ready
- `api-requirements/equipment-matrix-entities.md` - Equipment Matrix entities OK ready

### Shooter Attributes (характеристики шутера)
- `attributes-dnd-mapping.md` - [АРХИВ] сопоставление с D&D, не использовать
- `shooter-attributes.md` - Параметры точности, мобильности и стойкости для 3D-шутера WARNING in-review
- `ui-game-start.md` - UI потоки старта игры и боевой петли шутера OK ready

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
- OK План MVP (mvp-text-version-plan.md)
- OK Регистрация (ui-registration.md) - ready
- OK Создание персонажа (ui-character-creation.md) - ready
- OK Основной интерфейс (ui-main-game.md) - ready
- OK API требования для MVP (api-requirements/mvp-endpoints.md)
- OK Модели данных для MVP (api-requirements/mvp-data-models.md)
- OK Минимальный набор данных (mvp-initial-data.md)
- OK JSON данные для MVP (mvp-data-json/*.json)
- OK Детальные тексты и контент (mvp-content/*)

**Требует работы:**
- WARNING UI спецификация для MVP (ui-mvp-screens.md)
- WARNING Архитектура бекенда для MVP
- WARNING Архитектура фронтенда для MVP
- WARNING SQL скрипты для миграций БД

