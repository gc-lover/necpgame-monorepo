---
title: "Название идеи"
theme: "тема" # combat | progression | economy | social | lore | narrative | technical | other
status: "recorded" # recorded | in-development | completed | rejected
priority: "medium" # high | medium | low
date: "YYYY-MM-DD"
author: "User"
---

**Статус:** template  
**Версия:** 2.0.0  
**Последнее обновление:** 2025-11-09 14:36  

**api-readiness:** not-applicable  
**api-readiness-check-date:** 2025-11-09 04:18  
**api-readiness-notes:** Перепроверено 2025-11-09 04:18: шаблон соответствует `GLOBAL-RULES.md`; заполненный документ должен ссылаться на целевые микросервисы и модули.

# [Название идеи]

> Используй `GLOBAL-RULES.md`, `.BRAIN/CORE.md`, `IDEAL-PIPELINE/README.md`. Объём каждого файла ≤ 500 строк; при превышении создавай продолжения (`*_0001.md`) и фиксируй ссылки в обеих частях. Проверяй лимит скриптом `IDEAL-PIPELINE/scripts/check-file-limits.ps1`.

**Статус:** recorded  
**Тема:** [тема]  
**Приоритет:** [high/medium/low]  
**Дата создания:** YYYY-MM-DD  
**Автор:** User

## Краткое описание
[1–2 предложения о сути идеи]

## Детальное описание
[Подробное описание механики/сценария/системы]

## Архитектура и интеграции
- **Target microservice:** [auth-service / character-service / gameplay-service / social-service / economy-service / world-service / narrative-service / admin-service] (см. `GLOBAL-RULES.md`)  
- **API directory:** `api/v1/<domain>/<feature>.yaml` (директория обязана совпадать с `info.x-microservice.directory`)  
- **Frontend module:** `FRONT-WEB/src/features/<module>/<feature>`  
- **Связанные события и интеграции:** [REST/gRPC/Kafka/Redis/Telemetry]; укажи producer/consumer и микросервис.
- **Данные и миграции:** [какие таблицы/seed нужны, ссылка на Data & Balancing агента]

## Обоснование
[Почему идея важна, какие цели закрывает]

## Связь с существующими системами
[Как идея влияет на текущие документы, OpenAPI, реализованные фичи]

## Примеры использования
[Примерные сценарии, пользовательские истории]

## Требования и риски
- [ ] Требование 1  
- [ ] Требование 2  
- [ ] Потенциальный риск / ограничение

## Трекинг и проверки
- **Метаданные:** статус (`draft/review/approved`), версия, временные метки обновляй через `powershell -Command "Get-Date -Format 'yyyy-MM-dd HH:mm'"`.
- **Очереди:** добавь/обнови запись в `06-tasks/queues/<status>.md`; при смене статуса перенеси её вручную.
- **Readiness:** заполненный документ обязан ссылаться на чеклист `IDEAL-PIPELINE/checklists/idea-to-api.md`; отметь результат в `06-tasks/config/readiness-tracker.yaml`.
- **Лимит строк:** запускай `IDEAL-PIPELINE/scripts/check-file-limits.ps1 -Path <file>` до отправки на проверку.

## Pipeline & Handovers
| Этап | Обязательные действия | Валидаторы | Передача |
| --- | --- | --- | --- |
| Vision Manager | Заполнить разделы шаблона, указать microservice/API/Frontend, перечислить зависимости | `check-file-limits.ps1` | Запись в readiness queue, Activity Log |
| Readiness Reviewer | Проверить полноту, архитектуру, лимит строк, статусы | `check-readiness-entry.ps1`, `check-file-limits.ps1` | Обновить `api-readiness` → `ready`, уведомить API Task Creator |
| API Task Creator | Создать задание по `api-task-template.md`, связать с brain-mapping | `validate-api-task.ps1` | Переместить в `API-SWAGGER/tasks/queues/queued.md` |
| OpenAPI Executor | Обновить/создать спецификацию, использовать shared компоненты | `validate-swagger.ps1`, `check-spec-structure.ps1` | Статус `api.completed`, уведомить Backend |
| Backend Implementer | Генерация контрактов, реализация, миграции, tests | `check-backend-structure.ps1`, `mvn test` | `backend.completed`, выдать frontend brief |
| Frontend Implementer | Orval клиенты, UI, тесты, accessibility | `check-frontend-structure.ps1`, `npm run test` | `frontend.completed`, уведомить QA |
| QA Agent | Тест-план, прогон тестов, дефекты | `run-qa-suite.ps1`, `check-defect-register.ps1` | `qa.completed`, отчёт DevOps |
| DevOps Agent | Подготовка релиза, мониторинг | `run-deploy-dryrun.ps1`, `check-release-notes.ps1` | `release.released`, communication log |
| Refactor Agent | Оценить архитектурные долги, предложить задачи | `check-architecture-health.ps1`, `check-directory-structure.ps1` | Добавить задачи в backlog, обновить Activity Log |

## Контрольные вопросы перед передачей
- [ ] Все зависимости и влияния на микросервисы перечислены.
- [ ] Указаны необходимые данные, миграции, события и аналитика.
- [ ] Связанные решения/блокеры отражены (Decision Log, blockers).
- [ ] Обновлены readiness queue, tracker и Activity Log.
- [ ] Добавлены ссылки на релевантные agent briefs (`IDEAL-PIPELINE/templates/agent-briefs/*.md`).

## Вопросы для проработки
- [ ] Вопрос 1  
- [ ] Вопрос 2  
- [ ] Вопрос 3

## Заметки
[Дополнительные ссылки, источники вдохновения, материалы]

## История изменений
- YYYY-MM-DD — Идея записана  
- YYYY-MM-DD — Обновление/уточнение

