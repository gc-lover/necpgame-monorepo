# Документационный аудит — 2025-11-08

## 1. Сценарии агентов
- `.BRAIN/МЕНЕДЖЕР.MD` — управление документооборотом `.BRAIN`.
- `.BRAIN/КЛИР.MD` — архивация и очистка материалов.
- `API-SWAGGER/ДУАПИТАСК.MD` — создание заданий для OpenAPI.
- `API-SWAGGER/АПИТАСК.MD` — генерация OpenAPI спецификаций.
- `BACK-GO/docs/БЭКТАСК.MD` — реализация backend по контрактам.
- `FRONT-WEB/ФРОНТТАСК.MD` — реализация frontend по контрактам.

## 2. Расширенные руководства и справочники
- `.BRAIN/06-tasks/active/` (`current-status.md`, `open-questions.md`, `TODO.md`), `06-tasks/config/readiness-tracker.yaml`, `brain-mapping.yaml`.
- `API-SWAGGER/` (`ARCHITECTURE.md`, `АПИТАСК-ARCHITECTURE.md`, `АПИТАСК-PROCESS.md`, `АПИТАСК-REQUIREMENTS.md`, `АПИТАСК-FAQ.md`, `АПИТАСК-FAQ-EXAMPLES.md`, `tasks/config/*.yaml|*.md`).
- `BACK-GO/docs/` (`БЭКТАСК-ARCHITECTURE.md`, `БЭКТАСК-BEST-PRACTICES.md`, `БЭКТАСК-FAQ.md`, `QUICK-START.md`, `DOCKER-SETUP.md`, `DOCKER-DEPLOYMENT.md`, `MANUAL-TEMPLATES.md`).
- `BACK-GO/OPENAPI-CONTRACT-ARCHITECTURE.md`, `FRONT-WEB/ARCHITECTURE.md`, `FRONT-WEB/docs/*`, `GLOBAL-RULES.md`, `DEVELOPMENT-WORKFLOW.md`.

## 3. Шаблоны и генераторы
- `.BRAIN/06-tasks/ideas/IDEA-TEMPLATE.md`, `.BRAIN/04-narrative/npc-lore/NPC-TEMPLATE.md`.
- `API-SWAGGER/tasks/templates/api-generation-task-template.md`, `api-generation-task-template-details.md`.
- `API-SWAGGER/api/v1/shared/` (`responses.yaml`, `pagination.yaml`, `security.yaml`).
- `BACK-GO/docs/MANUAL-TEMPLATES.md`, `BACK-GO/templates/*.mustache`, `BACK-GO/templates-standard/`.
- `FRONT-WEB/scripts/generate-api*.ps1|.sh`, `FRONT-WEB/docs/modules/*.md`, `FRONT-WEB/src/shared/ui/` (UI kit описания в README).

## 4. Найденные дубликаты и устаревшие ссылки
- `scripts/autocommit.ps1|.sh` содержатся в каждом репозитории; рационально ссылаться на единые правила в `GLOBAL-RULES.md`.
- `API-SWAGGER/scripts/validate-openapi.ps1` удалён, функцию выполняет `scripts/validate-swagger.ps1` (корень монорепозитория).
- Инструкции в `BACK-GO/docs/*`, `BACK-GO/templates/STRUCTURE.md`, `BACK-GO/docs/БЭКТАСК-FAQ.md`, `API-SWAGGER/README.md` обновлены — используются актуальные команды `validate-swagger`.
- FAQ `АПИТАСК-FAQ.md` и `АПИТАСК-FAQ-EXAMPLES.md` продолжают перекрывать содержание (вопросы vs. примеры).
- `BACK-GO/docs/БЭКТАСК-BEST-PRACTICES.md` и `БЭКТАСК-FAQ.md` частично дублируют пайплайн и tooling.
- `FRONT-WEB/QUICK-START.md`, `ФРОНТТАСК-QUICKSTART.md`, `ФРОНТТАСК-PROCESS.md` описывают идентичные шаги генерации API.

## 5. Рекомендации после консолидации
- Централизовать инструкции по `autocommit` и ссылаться на них из репозиториев.
- Свести FAQ/Best Practices в единые документы, вынести примеры и кейсы в приложения.
- Поддерживать актуальные ссылки на `GLOBAL-RULES.md` и `CORE.md` во всех вспомогательных файлах.
- Контролировать лимит 500 строк и при необходимости разбивать материалы на части (`*_0001.md`, `*_0002.md`).

