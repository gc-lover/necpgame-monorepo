# API Generation Task Template

> Обязательно сверяйся с `GLOBAL-RULES.md`, `API-SWAGGER/CORE.md`, `tasks/config/task-creation-guide.md` и `tasks/config/checklist.md`.  
> Размер задания ≤ 500 строк. При превышении создавай продолжения (`task-XXX-description_0001.md`).

## Metadata
- **Task ID:** API-TASK-XXX  
- **Type:** API Generation | API Update | API Validation  
- **Priority:** low | medium | high | critical  
- **Status:** queued  
- **Created:** YYYY-MM-DD HH:MM  
- **Author:** API Task Creator Agent  
- **Dependencies:** none | API-TASK-YYY  

## Summary
Кратко опиши цель задания (1–2 предложения): какую спецификацию создать/обновить, почему это важно.

## Source Documents
| Поле | Значение |
| --- | --- |
| Repository | `.BRAIN` |
| Path | `.BRAIN/.../document.md` |
| Version | vX.Y.Z |
| Status | approved |
| API readiness | ready (YYYY-MM-DD HH:MM) |

**Key points:** перечисли 3–5 фактов из документа, которые должны попасть в API (правила, ограничения, события).  
**Related docs:** список дополнительных источников и ссылок.

## Target Architecture (⚠️ Обязательно)
- **Microservice:** [auth-service | character-service | gameplay-service | social-service | economy-service | world-service | narrative-service | admin-service]  
- **Port:** [8081–8088]  
- **Domain:** [auth | characters | gameplay | social | economy | world | narrative | admin]  
- **API directory:** `api/v1/<domain>/<feature>/filename.yaml`  
- **base-path:** `/api/v1/<domain>/<feature>`  
- **Java package:** `com.necpgame.<service>`  
- **Frontend module:** `modules/<module>/<feature>`  
- **Shared UI/Form components:** перечисли компоненты из `@shared/ui`, `@shared/forms`, layouts, хуки.

> Все значения должны соответствовать таблице микросервисов в `GLOBAL-RULES.md`.

## Scope of Work
Опиши ключевые действия (4–6 шагов), например:
1. Проанализировать исходный документ.
2. Определить набор endpoints.
3. Описать модели данных и связи.
4. Добавить схемы ошибок/валидаторов.
5. Подготовить examples, security requirements.
6. Запустить `validate-swagger.ps1`.

## Endpoints
Перечисли endpoints с целевыми методами и короткими описаниями. Для каждого укажи:
- Method & path (например, `POST /api/v1/gameplay/combat/shoot`)
- Назначение
- Основные параметры/валидацию
- Ответы (200, 400, 404, 409 и т.д.)

## Data Models
Перечисли требуемые `components.schemas`, `responses`, `parameters`, `securitySchemes`.  
Используй PascalCase для моделей, snake_case для свойств. Укажи обязательные поля, enum, примеры.

## Integrations & Events
- REST зависимости (вызовы других сервисов).
- Kafka/WebSocket события (publish/subscribe).
- Связь с существующими спецификациями.

## Acceptance Criteria
1. Спецификация создана/обновлена по указанному пути и ≤ 500 строк.  
2. `info.x-microservice` и `servers` заполнены корректно.  
3. Все endpoints/models из документа отражены.  
4. Использованы общие компоненты (`api/v1/shared/common/*`).  
5. Добавлены примеры, описания, коды ошибок.  
6. `pwsh -NoProfile -File ..\scripts\validate-swagger.ps1 -ApiSpec ...` проходит без ошибок.  
7. `tasks/config/brain-mapping.yaml` обновлён (или соответствующая часть `_000X.yaml`).  
8. Документ `.BRAIN` обновлён (секция API Tasks Status).  
9. Связанные задания/спецификации перечислены и соблюдают зависимости.

## FAQ / Notes
- [Вопрос] → [Ответ]  
- Особые условия, риски, ограничения.

## Change Log
- YYYY-MM-DD HH:MM — Задание создано (Agent)  
- YYYY-MM-DD HH:MM — Проверено (Reviewer)

