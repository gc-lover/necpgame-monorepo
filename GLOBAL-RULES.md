# GLOBAL RULES

## Главные правила
1. Соблюдай назначенную роль агента и передавай работу следующему звену по воркфлоу.
2. Поддерживай размер каждого файла ≤ 500 строк; при превышении создавай последовательности `_0001`, `_0002`, … и связывай их ссылками.
3. Перед публикацией OpenAPI спецификаций запускай `scripts\validate-swagger.ps1` и устраняй все несоответствия.
4. Следуй актуальной документации (`GLOBAL-RULES.md`, репозиторные `CORE.md`, действующие шаблоны) и обновляй её при изменениях процессов.
5. Соблюдай архитектурные требования: микросервисную карту, структуру каталогов `api/v1/<microservice>/<domain>/`, модульную фронтенд-архитектуру и согласованный стек.

## Микросервисная карта NECPGAME
| Сервис | Порт | Базовые домены | Хранилище | Ключевые интеграции |
| --- | --- | --- | --- | --- |
| api-gateway | 8080 | /api/v1/** | N/A | JWT, rate limiting, маршрутизация на все сервисы |
| auth-service | 8081 | /api/v1/auth/* | PostgreSQL (auth), Redis | Feign-клиенты всех сервисов, событие `account.created` |
| character-service | 8082 | /api/v1/characters/*, /api/v1/players/* | PostgreSQL (characters) | Читает токены auth, подписка на `account.created`, REST/event связи с gameplay и economy |
| gameplay-service | 8083 | /api/v1/gameplay/* | PostgreSQL (gameplay), Redis | REST/event взаимодействие с character, economy, social; публикует `combat.*`, `quest.*` |
| social-service | 8084 | /api/v1/social/* | PostgreSQL (social), Kafka | Слушает `combat.*`, `economy.*`, `world.*`, отправляет уведомления |
| economy-service | 8085 | /api/v1/economy/* | PostgreSQL (economy), Kafka outbox | Получает события боёв/квестов, взаимодействует с social |
| world-service | 8086 | /api/v1/world/* | PostgreSQL (world), Kafka | Принимает обновления gameplay/economy, публикует мировые события |
| narrative-service | 8087 | /api/v1/narrative/* | PostgreSQL (narrative), S3 | Использует данные персонажей и триггеры gameplay/social |
| admin-service | 8088 | /api/v1/admin/* | PostgreSQL (admin), Elasticsearch | Защищённые REST/Kafka вызовы ко всем доменам, аудит |

## OpenAPI: обязательные требования
- Каждый боевой файл (кроме `api/v1/shared/**`) обязан содержать `info.x-microservice`:
  - `name`: `auth-service` | `character-service` | `gameplay-service` | `social-service` | `economy-service` | `world-service` | `narrative-service` | `admin-service`
  - `port`: 8081–8088 (строго в соответствии с таблицей выше)
  - `domain`: `auth`, `characters`, `gameplay`, `social`, `economy`, `world`, `narrative`, `admin`
  - `base-path`: `/api/v1/<domain>/<subdomain?>`, отражает реальный каталог
  - `directory`: относительный путь `api/v1/<domain>/<subdomain?>`
- Список `servers` обязан содержать `https://api.necp.game/v1` (Production) и может включать `http://localhost:8080/api/v1` (Dev gateway).
- Файлы размещаются строго внутри каталога своего микросервиса: `api/v1/<domain>/...`.
- Paths остаются в основном файле; компоненты (`components.schemas`, `responses`, и т.д.) можно выносить с помощью `$ref`.
- Все ошибки используют общую модель из `api/v1/shared/common/responses.yaml`.
- Обновляй спецификацию до исправления style/lint ошибок — генераторы и бэкенд/фронтенд не должны дописывать контракты вручную.

### Скрипт проверки
```powershell
pwsh -NoProfile -File .\scripts\validate-swagger.ps1 -ApiDirectory API-SWAGGER\api\v1\
```
Скрипт блокирует ошибки `x-microservice`, отсутствующие `servers`, несогласованную структуру каталогов и другие нарушения. Исправь спецификации на месте до того, как переходить к генерации кода.

## Репозитории и их ядра
- `.BRAIN/CORE.md` — подготовка концептов, статусов, трекеров; связь с API через brain-mapping.
- `API-SWAGGER/CORE.md` — хранение спецификаций и заданий, единый источник контрактов.
- `BACK-GO/CORE.md` — реализация микросервисов на Java 21/Spring Boot по сгенерированным контрактам.
- `FRONT-WEB/CORE.md` — SPA на React + TypeScript с Orval/React Query, MUI и модульной архитектурой.
Каждый `CORE.md` расширяет глобальные правила конкретными деталями и описывает точки интеграции.

## Дополнительные материалы
- `.BRAIN`: [КЛИР-PROCEDURES.md](.BRAIN/КЛИР-PROCEDURES.md) — архивирование и очистка; [МЕНЕДЖЕР-CHECKLIST.md](.BRAIN/МЕНЕДЖЕР-CHECKLIST.md) — чеклист менеджера; [МЕНЕДЖЕР-EXAMPLES.md](.BRAIN/МЕНЕДЖЕР-EXAMPLES.md) — типовые ответы; [МЕНЕДЖЕР-PROCEDURES.md](.BRAIN/МЕНЕДЖЕР-PROCEDURES.md) — процедуры менеджера; [ARCHITECTURE.md](.BRAIN/ARCHITECTURE.md) — структура каталога.
- `API-SWAGGER`: [АПИТАСК-ARCHITECTURE.md](API-SWAGGER/АПИТАСК-ARCHITECTURE.md) — устройство каталога API; [АПИТАСК-PROCESS.md](API-SWAGGER/АПИТАСК-PROCESS.md) — пошаговый процесс; [АПИТАСК-REQUIREMENTS.md](API-SWAGGER/АПИТАСК-REQUIREMENTS.md) — критерии спецификаций; [АПИТАСК-FAQ-EXAMPLES.md](API-SWAGGER/АПИТАСК-FAQ-EXAMPLES.md) — примеры и команды; [АПИТАСК-FAQ.md](API-SWAGGER/АПИТАСК-FAQ.md) — типичные вопросы; [ARCHITECTURE.md](API-SWAGGER/ARCHITECTURE.md) — обзор репозитория.
- `BACK-GO`: [OPENAPI-CONTRACT-ARCHITECTURE.md](BACK-GO/OPENAPI-CONTRACT-ARCHITECTURE.md) — связь контрактов и реализации; [docs/БЭКТАСК-ARCHITECTURE.md](BACK-GO/docs/БЭКТАСК-ARCHITECTURE.md) — архитектура микросервисов; [docs/БЭКТАСК-BEST-PRACTICES.md](BACK-GO/docs/БЭКТАСК-BEST-PRACTICES.md) — практики разработки; [docs/БЭКТАСК-FAQ.md](BACK-GO/docs/БЭКТАСК-FAQ.md) — вопросы и решения; [docs/DOCKER-DEPLOYMENT.md](BACK-GO/docs/DOCKER-DEPLOYMENT.md) — деплой в Docker; [docs/DOCKER-SETUP.md](BACK-GO/docs/DOCKER-SETUP.md) — локальная БД; [docs/MANUAL-TEMPLATES.md](BACK-GO/docs/MANUAL-TEMPLATES.md) — шаблоны реализации; [docs/QUICK-START.md](BACK-GO/docs/QUICK-START.md) — быстрый старт.
- `FRONT-WEB`: [ФРОНТТАСК-ARCHITECTURE.md](FRONT-WEB/ФРОНТТАСК-ARCHITECTURE.md) — модульная схема SPA; [ФРОНТТАСК-FAQ.md](FRONT-WEB/ФРОНТТАСК-FAQ.md) — ответы по фронтенду; [ФРОНТТАСК-REQUIREMENTS.md](FRONT-WEB/ФРОНТТАСК-REQUIREMENTS.md) — требования к фичам; [ФРОНТТАСК-PROCESS.md](FRONT-WEB/ФРОНТТАСК-PROCESS.md) — рабочий процесс; [ФРОНТТАСК-QUICKSTART.md](FRONT-WEB/ФРОНТТАСК-QUICKSTART.md) — стартовые шаги; [API-GENERATION-README.md](FRONT-WEB/API-GENERATION-README.md) — генерация клиентов; [ARCHITECTURE.md](FRONT-WEB/ARCHITECTURE.md) — архитектура фронтенда.
- Общий контур: [DEVELOPMENT-WORKFLOW.md](DEVELOPMENT-WORKFLOW.md) — цепочка от идеи до релиза.

## Общий воркфлоу
1. `.BRAIN` формирует и утверждает документы (`draft → review → approved`, `api-readiness`).
2. `API-SWAGGER` создаёт задания, затем OpenAPI спецификации и синхронизирует `brain-mapping.yaml`.
3. `BACK-GO` генерирует контракты, пишет реализацию, обновляет `implementation-tracker.yaml`.
4. `FRONT-WEB` генерирует клиенты, создаёт модули, закрывает `implementation-tracker.yaml`.
5. `.BRAIN`/`API-SWAGGER` архивируют завершённое (Clear агент).

## Работа с шаблонами и размерами файлов
- Все шаблоны должны соответствовать актуальным правилам. Перед массовым созданием однотипных материалов убедись, что шаблон обновлён.
- При достижении лимита строк переноси часть содержимого в новую версию (`*_0001.md`, `*_0002.md`, …) и указывай ссылки между частями.
- Не дублируй информацию: вынеси общие блоки в `GLOBAL-RULES.md`, `CORE.md` или конкретный шаблон и ссылайся на них.

## Коммиты и автоматизация
- Используй `scripts/autocommit.ps1|.sh` из корня соответствующего репозитория, фиксируй логические блоки.
- Проверяй состояние линтеров/валидаторов перед коммитом.
- Любое изменение правил, архитектуры, workflow или шаблонов документируй сразу — устаревшие инструкции подлежат удалению в рамках этапа очистки.


