# Workqueue Service

Сервис координирует работу Cursor AI Agents через единую очередь задач и каталог данных. Он пришёл на смену YAML‑очередям: все статусы, контент и процессы теперь управляются из PostgreSQL и доступны через REST API.

## Основные возможности
- **Очереди и задачи**. У каждой очереди (`/api/queues/{segment}`) есть элементы с приоритетами, историей состояний и блокировками. Агент может взять следующую задачу, обновить статус, закрепить/освободить элемент, посмотреть историю (`/api/queue-items/{id}`).
- **Агентские предпочтения**. `AgentPreferenceService` хранит конфигурацию сегментов, допустимых статусов и тайм-аутов. `AgentTaskService` выдаёт задачи с учётом активных статусов, fallback‑сегментов и блокировок.
- **Каталог знаний**. Подсистема `/api/content` зеркалирует знания из `shared/docs/knowledge` в БД: типы сущностей, статусы, секции, ссылки, локализации и историю правок.
- **Справочники и процессы**. `/api/reference/task-statuses`, `/api/process/*`, `/api/qa/plans`, `/api/release/runs`, `/api/analytics/schemas` дают агентам доступ к чеклистам, QA‑планам, релизным шагам и аналитическим схемам без чтения YAML.
- **Блокировки и тайм-ауты**. `/api/locks` управляет эксклюзивными lock’ами для очередей и элементов. По cron‑джобу `AgentTaskTimeoutJob` автоматически освобождает просроченные локации и откатывает статус задачи.
- **Аудит**. `ActivityLogService` фиксирует каждое изменение очереди, что позволяет синхронизировать `shared/trackers` и выполнять требования GLOBAL-RULES.

## Архитектура
- **Стек**: Spring Boot 3.2, Java 21, Spring Data JPA, Liquibase, PostgreSQL, Spring Security (`X-Agent-Role`), Springdoc OpenAPI, Actuator.
- **Слои**: `domain` (JPA-энтити), `repository` (интерфейсы Spring Data + кастомные запросы), `service` (бизнес-логика, валидация, транзакции), `web` (REST-контроллеры и DTO). Схема следует внутренней политике `policy:workqueue`, поэтому все правила формализованы прямо в БД (agent_briefs/reference_templates).
- **Модули данных**: очереди, активности, агентские предпочтения, enum‑справочники, контент (entities/sections/attributes/history), процессы (templates/checklists), QA (plans/reports), релизы (runs/steps/validations), аналитика (schemas/metrics).
- **Фоновая работа**: `@EnableScheduling` подключает `AgentTaskTimeoutJob`, который каждые `workqueue.timeout.poll-interval-ms` миллисекунд освобождает блокировки и откатывает статус задачи в `returnStatus`.

## База данных и миграции
- Схема описана в `src/main/resources/db/changelog`. Последовательность changeSet’ов 001‑022 создаёт ядро очередей, справочники (`enum_groups`, `enum_values`), контентные таблицы, процессы, QA, seed‑данные, связки `queue_item_templates`, `queue_item_artifacts` и таблицу `handoff_rules`.
- Liquibase запускается автоматически (контролируется `spring.liquibase.*`). Для выборочного прогрева используйте `WORKQUEUE_LIQUIBASE_CONTEXTS`.
- `ddl-auto: validate`, поэтому все изменения схемы проходят только через миграции.

## Хранение данных и бэкапы
- Контейнер `workqueue-db` монтирует локальную директорию `./.data/workqueue-db`. Создайте её перед первым запуском (`mkdir .data && mkdir .data/workqueue-db`). `docker-compose down` больше не удаляет файлы — чтобы сбросить БД, удаляйте директорию вручную.
- Используйте `docker-compose -f docker-compose.workqueue.yml down` (без `-v`), если нужно просто остановить сервисы. Это исключает случайное разрушение данных другими агентами.
- Для резервного копирования используйте стандартный `pg_dump`/`pg_restore`. Пример:
  - `docker exec workqueue-db pg_dump -U workqueue workqueue > .data/backups/workqueue-$(date +%s).sql`
  - `docker exec -i workqueue-db psql -U workqueue workqueue < .data/backups/workqueue-last.sql`
  Папка `.data/backups` не создаётся автоматически — добавьте её вручную и следите за retention.

## API-обзор
- `GET /api/agents`, `/api/agents/{id}`, `/api/agents/role/{role}` — каталог агентов.
- `GET /api/agents/next-task`, `POST /api/agents/next-task/accept`, `POST /api/agents/next-task/release` — подбор, приём и возврат задач.
- `GET /api/queues/{segment}` — очереди сегмента, `GET /api/queues/{segment}/items` — фильтр по статусу и исполнителю.
- `GET/PATCH /api/queue-items/{id}` — детализация и обновление записи (payload, metadata, назначение).
- `POST /api/ingest/tasks` — единая точка ввода задач: валидирует `TaskIngestionRequest`, сохраняет артефакт, логирует событие и сопоставляет шаблоны в `queue_item_templates`.
- `GET /api/ingest/tasks/schema` — JSON Schema запроса ingest (`contracts/task-ingestion-request.schema.json`), удобно для генерации клиентов и CI-валидаций.
- `POST /api/agents/tasks/claim` — подбор задачи с учётом `agent_preferences`, приоритета и фильтров; ответ включает TTL, инструкции, чеклисты и ссылки на шаблоны из БД, при конфликтах возвращаются коды (`active_task_exists`, `no_tasks`).
- `POST /api/agents/tasks/{itemId}/submit` — загрузка отчёта и артефактов (multipart), валидация по шаблонам сегмента, обновление статуса и автоматическое создание handoff-задачи следующему сегменту на основе `handoff_rules`.
- `GET /api/agents/tasks/items/{itemId}` — чтение детали очереди для агента с включёнными instructions и submitContract (для активной задачи из 409).
- `PUT /api/reference/templates/{code}` — синхронизация шаблонов из YAML/Markdown; `GET /api/reference/templates/{code}` — выдача шаблона агентам и Claim API.
- `POST /api/content/entities`, `PUT /api/content/entities/{id}` — создание/обновление типизированных записей каталога знаний (Concept Director → Vision Manager pipeline, политика `policy:content`).
- `POST /api/quests`, `PUT /api/quests`, `GET /api/quests/{contentId}` — Vision Manager фиксирует структурированные данные квестов (quest_data/stages/rewards/branches/effects) поверх существующего `content_entity`.
- `POST /api/items`, `PUT /api/items`, `GET /api/items/{contentId}` — Typed‑CRUD для предметов: категория/слот/редкость, weapon/armor stats, потребляемые эффекты, мод-слоты и требования к компонентам пишутся в `item_data`, `weapon_stats`, `armor_stats`, `consumable_effects`, `item_mod_slots`, `item_component_requirements` с полной валидацией DTO.
- `POST /api/npcs`, `PUT /api/npcs`, `GET /api/npcs/{contentId}` — хранение профилей NPC: поведенческие/фракционные привязки, расписание, инвентарь и ссылки на диалоги с валидацией и логированием в `npc_data`, `npc_schedule_entries`, `npc_inventory_items`, `npc_dialogue_links`.
- `POST /api/world/locations`, `PUT /api/world/locations`, `GET /api/world/locations/{contentId}` + `POST /api/world/events`, `PUT /api/world/events`, `GET /api/world/events/{contentId}` — typed-CRUD для локаций и мировых событий: регионы, биомы, связи между локациями, spawn-пойнты, события с требованиями и наградами, все данные пишутся в `world_location_data`, `world_location_links`, `world_spawn_points`, `world_event_data`, `world_event_requirements`.
- **Контент → Очереди.** После `POST/PUT` к `content/entities`, `quests`, `items`, `npcs`, `world/locations`, `world/events` сервис автоматически создаёт элемент очереди следующего сегмента (Vision → API → Backend → Frontend и т.д.) и фиксирует событие `content.queue_enqueued` в ActivityLog. В payload нового queue-item’a лежат REST-ссылки (`knowledgeRefs`) вида `/api/content/entities/{id}` и типовая карточка (`/api/items/{id}` и т.п.), чтобы агенты работали только через API.
- `POST /api/locks` и `DELETE /api/locks/{token}` — блокировки по очереди/элементу, `POST /api/locks/cleanup` — ручное удаление просроченных локаций.
- Каталоги: `/api/content/entities`, `/api/analytics/schemas`, `/api/process/templates`, `/api/process/checklists`, `/api/qa/plans`, `/api/release/runs`, `/api/reference/task-statuses`.
- `GET /api/knowledge/docs`, `/api/knowledge/docs/{code}` — просмотр мигрированных `knowledge/**` файлов напрямую из БД (см. Knowledge Importer).
- OpenAPI UI: `/swagger-ui/index.html`, json: `/v3/api-docs`.

## Плейбуки агентов
- `services/workqueue-service/docs/agents/concept-director-playbook.md` — подготовка знаний, ingest, claim/submit и проверка auto-handoff Vision.
- `services/workqueue-service/docs/agents/vision-manager-playbook.md` — строгое `claim → submit`, обновление `/api/content/entities/{id}` и типизированных CRUD, контроль `content.queue_enqueued`.
- `services/workqueue-service/docs/agents/api-task-architect-playbook.md` — API-first, подготовка и валидация OpenAPI, генерация клиентов/стабов, строгие конвенции.
Обе инструкции используют только REST (ingest разрешён исключительно Concept Director, прямой SQL запрещён).

## Каталог знаний и REST-доступ
- **Структура БД.** Лор/контент больше не живёт в YAML-очередях: change-set `011-create-content-core` создаёт `content_entities` + связанные таблицы (`entity_tags`, `entity_topics`, `entity_links`). Специализированные домены развиваются отдельными миграциями:
  - `012-create-quest-tables` — `quest_data`, `quest_stages`, `quest_rewards`, `quest_objectives` и т.п.
  - `013-create-skill-tables`, `014-create-item-tables`, `015-create-npc-tables`, `016-create-world-tables` — предметы, оружие, NPC, локации и их характеристики.
  - `017-create-process-tables` — QA/процессные шаблоны, release-run’ы и checklist’ы.
- **Представление в API.** `GET /api/content/entities` и `GET /api/content/entities/{code}` возвращают любую запись (canon, mechanics, content, implementation). Дополнительные эндпоинты дают типизированный вид: `/api/quests/{code}`, `/api/items/{code}`, `/api/npcs/{code}`, `/api/world/locations/{code}` и т.д. Все они собирают данные из таблиц выше, поэтому агенту больше не нужно читать файлы из `knowledge/`.
- **Шаблоны и схемы.** `knowledge-entry-template`, `knowledge-schema`, `structure-guidelines` живут в `reference_templates` (миграция `026-migrate-knowledge-to-db`). Claim отдаёт их через `instructions.templates[]`, а `GET /api/reference/templates/{code}` возвращает Markdown/JSON-вариант.
- **Глоссарий и ссылки.** Все записи `knowledge-glossary` проиндексированы в `content_entities` (тип `knowledge_reference`). Выдача — `GET /api/content/entities?type=knowledge_reference`, обновление — обычный `PUT /api/content/entities/{id}`.
- **Knowledge Importer.** Свежий `workqueue.knowledge-import.enabled=true` включает сканирование каталога `knowledge/**`: каждое `.yaml/.yml/.md` попадает в таблицу `knowledge_documents` с кодом, категорией и checksum. Точки доступа `/api/knowledge/docs` позволяют агентам ссылаться на REST вместо файлов.
- **Миграция из файлов.** YAML из `knowledge/*.yaml` переносится в БД и REST (Concept Director создаёт `POST /api/content/entities`, Vision Manager дорабатывает `PUT`). `knowledgeRefs` в ingest/submit обязаны ссылаться на `/api/reference/templates/*`, `/api/content/*` или `/api/knowledge/docs/*`; файловые пути служат только для архивов.

## Безопасность
- Аутентификация по ключу отключена. Используется только заголовок роли:
- Заголовки:
  - `X-Agent-Role` — ключ роли (`agents.role_key`), единственный обязательный заголовок.
- Аутентификация отключена в `local` профиле (`application-local.yml`).
- `ApiKeyAuthenticationFilter` аутентифицирует по роли и подставляет `AgentPrincipal` в `SecurityContext`.
- **Политика доступа к БД**:
  1. PostgreSQL-доступ выдаётся только сервису (уникальный user/password, не распространяем агентам/CLI).
  2. Контейнер БД не пробрасывается наружу — доступ исключительно из docker-сети или через SSH-tunnel для миграций/операторов.
  3. Все операции агентов выполняются через REST (ingest/claim/submit, справочники, reference templates). Любые скрипты поверх API допускаются, но прямой SQL запрещён.

## Конфигурация

| Переменная                                        | Назначение                                         | Значение по умолчанию                                                                          |
| ------------------------------------------------- | -------------------------------------------------- | ---------------------------------------------------------------------------------------------- |
| `WORKQUEUE_DB_URL`                                | JDBC строка PostgreSQL                             | `jdbc:postgresql://localhost:8442/workqueue`                                                   |
| `WORKQUEUE_DB_USERNAME` / `WORKQUEUE_DB_PASSWORD` | Доступ к БД                                        | `workqueue`                                                                                    |
| `WORKQUEUE_HTTP_PORT`                             | порт HTTP                                          | `8088`                                                                                         |
| `SECURITY_API_ENABLED`                            | включить security.api.enabled                      | `true`                                                                                          |
| `workqueue.timeout.poll-interval-ms`              | период фонового снятия локаций                     | `60000`                                                                                        |
| `WORKQUEUE_REPO_ROOT`                             | Базовый путь для проверки `knowledgeRefs`          | `../..`                                                                                        |
| `WORKQUEUE_INGESTION_ROLE`                        | Агент, от имени которого логируются ingest-события | `concept-director`                                                                             |
| `WORKQUEUE_ALLOWED_SEGMENTS`                      | Сегменты пайплайна (через запятую)                 | `concept,vision,api,backend,frontend,qa,release,analytics,community,security,data,ux,refactor` |
| `WORKQUEUE_ARTIFACTS_PATH`                        | Каталог хранения артефактов submit (`.data`)       | `.data/artifacts`                                                                              |
| `workqueue.knowledge-import.enabled`              | Включить импорт `knowledge/**` в БД                | `false`                                                                                        |
| `workqueue.knowledge-import.root-path`            | Каталог знаний относительно `repo-root`            | `knowledge`                                                                                   |

В docker-compose сценарии (`docker-compose.workqueue.yml`) сервис слушает порт `8090` (`WORKQUEUE_HTTP_PORT=8090`) и пробрасывается как `8090:8090`.

Локальный профиль (`spring.profiles.active=local`) перенастраивает БД на `localhost:5442` и отключает security. Артефакты сабмитов сохраняются в каталоге `WORKQUEUE_ARTIFACTS_PATH` (по умолчанию `.data/artifacts/<itemId>/...`), рекомендуется периодически очищать устаревшие файлы согласно политике хранения.

## Разработка и запуск
1. Установите Java 21, Maven 3.9+, PostgreSQL 14+.
2. Создайте БД `workqueue`, выдайте права пользователю `workqueue:workqueue`.
3. Запустите сервис:
   - Maven: `mvn spring-boot:run -Dspring-boot.run.profiles=local`.
   - JAR: `mvn clean package && java -jar target/workqueue-service-1.0.0.jar`.
   - Docker Compose: `docker-compose -f docker-compose.workqueue.yml up -d` (порт `8090`).
4. Тесты: `mvn clean verify`.
5. Docker: `docker build -t necpgame/workqueue-service .` и `docker run -p 8088:8088 --env-file .env necpgame/workqueue-service`.

Actuator метрики доступны без авторизации на `/actuator/**`.

## Windows-safe примеры вызова API (curl)

- Claim (PowerShell):
  $body = '{"segments":["concept"],"priorityFloor":1}'
  curl.exe -s -i -X POST http://localhost:8090/api/agents/tasks/claim `
    -H "X-Agent-Role: concept-director" `
    -H "Content-Type: application/json" `
    --data-binary $body

- Claim (CMD):
  cd /d C:\NECPGAME
  echo {"segments":["concept"],"priorityFloor":1}>claim.json
  curl.exe -s -i -X POST http://localhost:8090/api/agents/tasks/claim -H "X-Agent-Role: concept-director" -H "Content-Type: application/json" --data-binary @claim.json
  del claim.json

- Получить инструкции активной задачи (если Claim вернул 409 и activeItemId):
  curl.exe -s -i "http://localhost:8090/api/agents/tasks/items/<activeItemId>" -H "X-Agent-Role: concept-director"

- Submit (общая форма — уточнять по instructions.submitContract):
  1) Создайте файл payload.json:
     @'
     {
       "notes":"Concept brief",
       "artifacts":[{"url":"https://example.com/brief.md"}],
       "metadata":"{ \"handoff\": { \"nextSegment\":\"vision\" } }"
     }
     '@ | Out-File -Encoding utf8 payload.json
  2) Отправьте multipart:
     curl.exe -s -i -X POST "http://localhost:8090/api/agents/tasks/<itemId>/submit" ^
       -H "X-Agent-Role: concept-director" ^
       -H "Content-Type: multipart/form-data" ^
       -F "payload=@payload.json;type=application/json"
     del payload.json
 
Пример с knowledgeRefs (по возможности используйте REST‑ссылки `/api/...`). Поле `metadata` — строка с вложенным JSON:
 
@'
{
  "notes": "Concept brief",
  "artifacts": [{ "url": "/api/knowledge/docs/structure-guidelines" }],
  "metadata": "{ \"knowledgeRefs\": [\"/api/content/entities/\"], \"handoff\": { \"nextSegment\": \"vision\" } }"
}
'@ | Out-File -Encoding utf8 payload.json
curl.exe -s -i -X POST "http://localhost:8090/api/agents/tasks/<itemId>/submit" ^
  -H "X-Agent-Role: concept-director" ^
  -H "Content-Type: multipart/form-data" ^
  -F "payload=@payload.json;type=application/json"
del payload.json

## Готовые PowerShell‑скрипты (Concept Director)
Расположение: `scripts/ps/workqueue/*`
- `claim.ps1` — Claim задачи (параметры: `-BaseUrl`, `-Role`, `-Segments`, `-PriorityFloor`)
- `get-item.ps1` — получить детали задачи и инструкции: `-ItemId`
- `ingest.ps1` — создать задачу: `-SourceId`, `-Title`, `-Summary`, `-Topic` (segment=concept, handoff на vision)
- `submit.ps1` — Submit multipart: `-ItemId`, `-Notes`, `-ArtifactUrl`, `-ArtifactTitle`, `-NextSegment`, `-KnowledgeRefs @("/api/...")`
- `run-concept.ps1` — автосценарий: Claim → (при 204) Ingest → Claim → (при 409) GET item → Submit
  Пример запуска:
  - `powershell -NoProfile -ExecutionPolicy Bypass -File scripts/ps/workqueue/run-concept.ps1 -BaseUrl http://localhost:8090 -Role concept-director`
  Сценарий использует только `X-Agent-Role`, читает `submitContract` через `GET /api/agents/tasks/items/{itemId}` и формирует корректный multipart.

## Контракты игровых сущностей (MMORPG лутер‑шутер)
Добавлены REST‑заглушки для валидации DTO и генерации OpenAPI‑схем. Все эндпоинты принимают `POST .../validate` с JSON‑телом и возвращают `204 No Content`.

- Core: `/api/contracts/core/*` — `player-profile/validate`, `character/validate`, `skill-tree/validate`, `progress-state/validate`
- Economy: `/api/contracts/economy/*` — `currency-balance/validate`, `transaction/validate`, `vendor-offer/validate`
- Items: `/api/contracts/items/*` — `inventory-item-instance/validate`, `loadout/validate`, `item-template/validate`, `affix-roll/validate`
- Equipment: `/api/contracts/equipment/*` — `weapon-template/validate`, `weapon-mod/validate`, `ammo/validate`, `armor-template/validate`, `cyberware/validate`, `perk-chip/validate`
- Crafting: `/api/contracts/crafting/*` — `consumable/validate`, `blueprint/validate`, `upgrade-request/validate`
- Social: `/api/contracts/social/*` — `dialogue-graph/validate`, `faction-profile/validate`, `party/validate`, `guild/validate`, `season-pass/validate`, `achievement/validate`, `telemetry/validate`
- World: `/api/contracts/world/*` — `poi/validate`, `hack-node/validate`
- Combat: `/api/contracts/combat/*` — `npc-ai-profile/validate`, `status-effect/validate`, `combat-formula/validate`
- Loot: `/api/contracts/loot/*` — `loot-table/validate`, `drop-rule/validate`, `affix-rule/validate`
- Ops: `/api/contracts/ops/*` — `security-flag/validate`, `run-report/validate`

Схемы доступны в OpenAPI UI `/swagger-ui/index.html` и JSON `/v3/api-docs`.

### Доменные сервисы и хранилище (ядро)
- Добавлены таблицы: `player_profiles`, `player_profile_bans`, `player_profile_preferences`, `characters`, `character_reputation` (change-set `030-create-core-game-tables`).
- Репозитории: `PlayerProfileRepository`, `CharacterRepository`.
- Сервисы: `PlayerProfileService.upsert`, `CharacterService.upsert`.

### Роли агентов и зоны ответственности
- Concept Director: high-level контентные контракты (диалоги, фракции, сезонный план), `contracts/social/*`.
- Vision Manager: конкретизация контента (лут‑таблицы, дроп‑правила, формулы), `contracts/loot/*`, `contracts/combat/*`, world POI/hack‑ноды.
- API Task Architect: OpenAPI/интеграции, требования к экономике/магазинам, схемы DTO.
- Backend Implementer: реализация сервисов/репозиториев, миграции БД, интеграция `core` домена.
- Frontend Implementer: UI‑контракты и маппинг DTO, проверка `contracts/*/validate`.
- QA Engineer: валидация контрактов через `contracts/*/validate`, формирование тестовых payload и сценариев пайплайна.

## Валидация, шаблоны и артефакты
- Этапы проверки и чеклисты развёрнуты прямо в БД: Claim API возвращает `instructions.brief`, а также список `templates[]`. Обязанности/чеклисты берутся из таблиц `agent_briefs` и `reference_templates`, поэтому агент всегда видит актуальные требования без внешних файлов.
- Шаблоны обновляются вызовом `PUT /api/reference/templates/{code}`. Любой клиент (curl, HTTPie) может загрузить JSON/Markdown — сервис пересчитает hash и начнёт раздавать новый контент при следующем claim.
- Для сегмента `api` при submit обязательны `metadata.openapiVersion`, `metadata.specPath` (JSON) и артефакт/spec-файл `.yaml/.yml/.json` — см. `OpenApiSubmissionValidator`.
- Файлы сабмитов сохраняются в `.data/artifacts/<queueItemId>/`. Рекомендованный retention — 90 дней: перенесите архивы во внешнее хранилище или удалите вручную/cron’ом, чтобы не захламлять рабочую машину. Ссылочные артефакты (`artifacts[].url`) остаются в `queue_item_artifacts`.

### Сегментные валидаторы submit
- Vision (`validator:vision`): `metadata.handoff.nextSegment` должен быть `api`.
- Backend (`validator:backend`): `metadata.buildSuccess=true`, `metadata.commit` обязателен.
- Frontend (`validator:frontend`): `metadata.buildSuccess=true`, `metadata.artifactUrl` указывает на `.zip`.
- API (`validator:openapi`): см. требования выше к версии и spec‑артефакту.

Ошибки валидаторов возвращают расширенный `requirements[]`, включающий `policy:workqueue`, `agent-brief:<segment>`, `template:<code>`, а также `validator:<segment>`.

## Legacy
- YAML/PowerShell пайплайн удалён. Все новые задачи заводятся через `POST /api/ingest/tasks` (Concept Director), а остальные агенты работают исключительно через `claim → submit`. Исторические YAML-файлы остаются только в архиве `knowledge/*` и не участвуют в принятии решений.

## Пострелизный мониторинг
- Micrometer/Actuator публикуют `/actuator/prometheus`; собирайте метрики `workqueue_submission_success_total`, `workqueue_submission_failed_total`, `workqueue_validator_duration_seconds`, `workqueue_ingest_duration_seconds`, `workqueue_queue_backlog`, `workqueue_active_locks_total`, `workqueue_activity_log_lag_seconds`.
- Рекомендуемые алерты: всплески ошибок submit, превышение SLA валидаторов, рост бэклога, задержка аудита, утечки локаций.
- Перед релизом снимайте `pg_dump`, после выката держите включённым `WORKQUEUE_INGESTION_ENABLED`. В случае инцидента всё равно используйте REST API — fallback YAML пайплайна больше нет.

## Тестирование
- `mvn test` запускает unit + integration набор. Покрыты сервисные классы (`TaskIngestionServiceTest`, `TaskSubmissionServiceTest`, `AgentTaskServiceTest`) и MockMvc-интеграция (`TaskEndpointsIntegrationTest`), проверяющая ingest → claim → submit → handoff, multipart upload, ошибки авторства и автоматическую маршрутизацию.
- `TaskEndpointsIntegrationTest` содержит `ingestionLoadSmokeTest` (серия из 10 последовательно создаваемых задач) и negative-сценарии для security (`submission.not_owner`), что закрывает требования лоад/SEC-тестов из раздела 9 TODO.
- Для локального запуска интеграции используется H2 (in-memory) с Liquibase — зависимость `com.h2database:h2` подключена в тестовом scope, дополнительных сервисов не требуется.

