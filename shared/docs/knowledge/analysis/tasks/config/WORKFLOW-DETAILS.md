# Детальный воркфлоу агентов NECPGAME

**Версия:** 1.0.0  
**Обновлено:** 2025-11-08 23:49  
**Ответственный:** Brain Manager

---

## 1. Цепочка документов → API → реализация

1. `.BRAIN` формирует и утверждает документы (Manager + профильные авторы).
2. Brain Readiness Checker оценивает готовность и обновляет `readiness-tracker.yaml`.
3. ДУАПИТАСК получает записи со статусом `ready`, создает задания в `API-SWAGGER/tasks/active/queue/` и обновляет `brain-mapping.yaml`.
4. АПИТАСК пишет/обновляет OpenAPI спецификации, валидирует через `scripts/validate-swagger.ps1`.
5. БЭКТАСК генерирует код, реализует бизнес-логику, обновляет `implementation-tracker.yaml` (backend).
6. ФРОНТТАСК генерирует клиентов Orval, обновляет фронтенд, закрывает `implementation-tracker.yaml` (frontend).
7. КЛИР архивирует завершенные работы и очищает активные трекеры/списки.

---

## 2. Обязанности менеджера по этапам

| Этап | Действия |
| --- | --- |
| Подготовка документов | Структура ≤ 500 строк, заполнены метаданные, ссылки на микросервис и фронтенд модуль. |
| Readiness | Проверка по `STATUSES-GUIDE.md`, запись в `readiness-tracker.yaml`, фиксация времени. |
| Постановка задач | Обновление `TODO.md`, `current-status.md`, постановка вопросов. |
| Передача в API | Создание/обновление раздела `API Tasks` в документе, уведомление ДУАПИТАСК. |
| Реализация | Мониторинг `implementation-tracker.yaml`, синхронизация приоритетов. |
| Архивация | После закрытия backend/frontend инициировать Clear-процедуры. |

---

## 3. Форматы трекеров и обязательные поля

### readiness-tracker.yaml
- `path`, `version`, `status`, `priority`, `checked`, `checker`, `api_target.microservice`, `api_target.directory`, `api_target.frontend_module`, `notes`.

### brain-mapping.yaml (API-SWAGGER)
- `source`, `version`, `task_id`, `task_file`, `api_path`, `status`, `created`, `priority`, `notes`.

### implementation-tracker.yaml
- `api_path`, `task_id`, `api_status`, `backend`, `frontend`, `brain_source`, `priority`, `notes`.

---

## 4. Управление очередью задач

1. Формируем список в `TODO.md` по приоритету (Critical → High → Medium → Low).
2. Для активных направлений ведем `current-status.md` (краткий прогресс, ссылки на документы).
3. Вопросы и блокеры фиксируем в `open-questions.md` (с ответственными).
4. После подготовки документа переводим его в `ready`, обновляем трекер и уведомляем ДУАПИТАСК.
5. После создания задания проверяем `brain-mapping.yaml`, при необходимости корректируем.

---

## 5. Временные метки и версии

- Всегда берём время из системы (`powershell -Command "Get-Date -Format 'yyyy-MM-dd HH:mm'"`).
- Одна проверка → одно значение времени, используем его во всех связанных файлах.
- Версии документов повышаем семантически: `MAJOR.MINOR.PATCH` (структурные изменения → MINOR, правки текста → PATCH).

---

## 6. Инструменты

- **Ручные коммиты:** используй `git add`, `git commit`, `git push` или MCP для фиксации изменений.
- **Локальные скрипты:** `scripts/validate-swagger.ps1`.
- **Генераторы:** backend (`BACK-GO/scripts/generate-openapi-microservices.ps1`), frontend (`FRONT-WEB/scripts/generate-api-orval.ps1`). Запускаются профильными агентами, но менеджер проверяет готовность спецификаций.
- **Проверка:** перед передачей задания убедиться, что OpenAPI каталог существует в `API-SWAGGER/api/v1/...`.

---

## 7. Архивация и контроль

- После завершения реализации создаём запись для КЛИР в `TODO.md`/`current-status.md`.
- Проверяем, что все трекеры обновлены (`readiness-tracker`, `brain-mapping`, `implementation-tracker`).
- Архивируем документы/трекеры в `06-tasks/archive/...` с указанием дат.

---

## 8. Эскалация вопросов

- Используем `open-questions.md` с тегами `#backend`, `#frontend`, `#api`, `#lore` и указанием ответственного.
- Критичные блокеры помечаем `[!]` в `TODO.md` и дублируем в разделе `blocked` файла `current-status.md`.

---

## 9. Контроль качества

- Все новые документы соответствуют шаблону из `ARCHITECTURE.md`.
- При обнаружении устаревших ссылок обновляем или фиксируем вопрос.
- Любое изменение структуры сопровождается ссылкой на соответствующие правила (GLOBAL-RULES, CORE, сценарии агентов).

