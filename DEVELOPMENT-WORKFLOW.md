# Воркфлоу разработки проекта NECPGAME

## Краткое описание

Этот документ описывает полный цикл разработки функциональности в проекте NECPGAME - от идеи до реализации на backend и frontend.

**Основные принципы:**
- **API First** - сначала проектируется API спецификация, затем реализуется backend и frontend
- **Микросервисная архитектура** - backend состоит из 6 независимых микросервисов
- **Модульная архитектура frontend** - фронтенд организован в feature-based модули

---

## Архитектура проекта

### Backend - Микросервисы

Backend проекта организован в виде микросервисов. Каждый микросервис отвечает за свою доменную область:

| Микросервис | Порт | Домен | Примеры API |
|------------|------|-------|-------------|
| **auth-service** | 8081 | Аутентификация | `/api/v1/auth/*` |
| **character-service** | 8082 | Персонажи | `/api/v1/characters/*`, `/api/v1/players/*` |
| **gameplay-service** | 8083 | Геймплей | `/api/v1/gameplay/*`, `/api/v1/combat/*` |
| **social-service** | 8084 | Социальное | `/api/v1/social/*`, `/api/v1/guilds/*` |
| **economy-service** | 8085 | Экономика | `/api/v1/economy/*`, `/api/v1/trade/*` |
| **world-service** | 8086 | Мир | `/api/v1/world/*`, `/api/v1/locations/*` |

**Инфраструктура:**
- **API Gateway** (8080) - единая точка входа для всех запросов
- **Service Discovery** (Eureka, 8761) - регистрация и обнаружение сервисов
- **Config Server** (8888) - централизованная конфигурация

**Генерация контрактов:**
- Используется скрипт `generate-openapi-microservices.ps1`
- Автоматическое определение целевого микросервиса из API спецификации
- Генерация в директорию `microservices/{service-name}/`

### Frontend - Модульная архитектура

Frontend организован в виде модулей, соответствующих доменам:

| Модуль | Директория | State Store | UI Components |
|--------|-----------|-------------|---------------|
| **Auth** | `features/auth/` | useAuthStore | LoginForm, RegisterForm |
| **Characters** | `features/characters/` | useCharactersStore | CharacterCard, CharacterList |
| **Gameplay** | `modules/gameplay/` | useGameplayStore | WeaponCard, AbilityButton |
| **Social** | `modules/social/` | useSocialStore | GuildCard, FriendCard, NPCCard |
| **Economy** | `modules/economy/` | useEconomyStore | TradeForm, AuctionCard |
| **World** | `modules/world/` | useWorldStore | LocationCard, EventCard |

**Библиотеки компонентов:**
- `@shared/ui` - базовые UI компоненты (кнопки, карточки, индикаторы)
- `@shared/forms` - готовые переиспользуемые формы
- `@shared/layouts` - layout компоненты для страниц
- `@shared/hooks` - общие хуки

**Генерация API клиента:**
- Используется Orval для генерации TypeScript клиента и React Query хуков
- API Gateway URL: `http://localhost:8080`
- Генерация в `src/api/generated/`

---

## Визуальная диаграмма воркфлоу

```
ИДЕЯ/ЗАДАЧА
    ↓
┌─────────────────────────────────────────────────────┐
│ [1] МЕНЕДЖЕР (.BRAIN)                               │
│     - Создаёт/обновляет документы                   │
│     - Записывает идеи и концепции                   │
│     - Прорабатывает механики                        │
│     - Статус: draft → review → approved             │
│     - api-readiness: needs-work → ready             │
└─────────────────────────────────────────────────────┘
    ↓
    ↓ Документы со статусом approved/review
    ↓ api-readiness проверяется
    ↓
┌─────────────────────────────────────────────────────┐
│ [2] Brain Readiness Checker (опционально)           │
│     - Проверяет готовность документов               │
│     - Применяет критерии готовности                 │
│     - Обновляет readiness-tracker.yaml              │
│     - Обновляет метаданные документов               │
│     - api-readiness: ready/needs-work/blocked       │
└─────────────────────────────────────────────────────┘
    ↓
    ↓ Документы со статусом api-readiness: ready
    ↓
┌─────────────────────────────────────────────────────┐
│ [3] ДУАПИТАСК (API Task Creator)                    │
│     - Читает готовые документы из .BRAIN            │
│     - Создаёт полные задания для API Executor       │
│     - Сохраняет в tasks/active/queue/               │
│     - Обновляет brain-mapping.yaml                  │
│     - Статус: queued                                │
└─────────────────────────────────────────────────────┘
    ↓
    ↓ Задания в tasks/active/queue/
    ↓
┌─────────────────────────────────────────────────────┐
│ [4] АПИТАСК (API Executor)                          │
│     - Читает задания из queue/                      │
│     - Создаёт OpenAPI спецификацию                  │
│     - Сохраняет в API-SWAGGER/api/v1/              │
│     - Обновляет brain-mapping.yaml: completed       │
│     - СОЗДАЁТ запись в implementation-tracker.yaml  │
│     - api_status: completed                         │
│     - backend.status: not_started                   │
│     - frontend.status: not_started                  │
└─────────────────────────────────────────────────────┘
    ↓
    ↓ OpenAPI спецификация готова
    ↓ Запись в implementation-tracker создана
    ↓
    ├─────────────────────┬─────────────────────┐
    ↓                     ↓                     ↓
┌──────────────────┐ ┌──────────────────┐ ┌──────────────────┐
│ [5] БЭКТАСК      │ │ [6] ФРОНТТАСК    │ │ [Другие агенты]  │
│  (Backend Agent) │ │  (Frontend Agent)│ │                  │
└──────────────────┘ └──────────────────┘ └──────────────────┘
    ↓                     ↓
    │ ОБЯЗАТЕЛЬНО: Обновить implementation-tracker.yaml
    │ backend.status: "in_progress"
    │ backend.started: "YYYY-MM-DD HH:MM"
    ↓
    │ - Генерирует контракты из OpenAPI
    │ - Создаёт реализацию (Entities, Repos, Controllers, Services)
    │ - Создаёт миграции БД (Liquibase)
    │ - Создаёт seed данные
    │ - Тестирует API (curl/Postman)
    ↓
    │ ОБЯЗАТЕЛЬНО: Обновить implementation-tracker.yaml
    │ backend.status: "completed"
    │ backend.completed: "YYYY-MM-DD HH:MM"
    │ backend.commit: "hash"
    │ Делает коммит через autocommit
    ↓                     ↓
    │                     │ ОБЯЗАТЕЛЬНО: Обновить implementation-tracker.yaml
    │                     │ frontend.status: "in_progress"
    │                     │ frontend.started: "YYYY-MM-DD HH:MM"
    │                     ↓
    │                     │ - Генерирует TypeScript клиент (Orval)
    │                     │ - Создаёт React Query хуки
    │                     │ - Создаёт feature-based структуру
    │                     │ - Создаёт страницы (pages) для роутинга
    │                     │ - Настраивает роуты (React Router)
    │                     │ - Создаёт компоненты (с MUI)
    │                     │ - Тестирует интеграцию с бекендом
    │                     ↓
    │                     │ ОБЯЗАТЕЛЬНО: Обновить implementation-tracker.yaml
    │                     │ frontend.status: "completed"
    │                     │ frontend.completed: "YYYY-MM-DD HH:MM"
    │                     │ frontend.commit: "hash"
    │                     │ Делает коммит через autocommit
    └─────────────────────┴──→ ГОТОВАЯ ФИЧА ✅
```

---

## Детальное описание этапов

### Этап 1: МЕНЕДЖЕР (.BRAIN)

**Агент:** Brain Manager Agent  
**Документ:** `.BRAIN/МЕНЕДЖЕР.MD`  
**Роль:** Создание и управление документами с концепциями и механиками игры

**Входные данные:**
- Идея или задача от пользователя
- Существующие документы .BRAIN (для контекста)

**Что делает:**
1. Создаёт новые документы с описанием концепций/механик
2. Прорабатывает детали (механики, правила, примеры)
3. Записывает идеи в структурированном формате
4. Обновляет статус документа: draft → review → approved
5. Отмечает готовность к созданию API: api-readiness (needs-work → ready)
6. Перед массовым созданием однотипных документов создаёт шаблоны

**Выходные данные:**
- Документы .BRAIN со статусом approved/review
- Документы с api-readiness: ready (готовы к созданию API)

**Системы отслеживания:**
- Обновляет метаданные документов (status, api-readiness)
- Не обновляет трекеры напрямую

**Следующий этап:** Brain Readiness Checker (опционально) или ДУАПИТАСК

---

### Этап 2: Brain Readiness Checker (опционально)

**Агент:** Brain Readiness Checker Agent  
**Документ:** `.BRAIN/06-tasks/config/ЧЕКБРЕЙН.MD`  
**Роль:** Проверка готовности документов к созданию API задач

**Входные данные:**
- Документы .BRAIN с любым статусом
- Критерии готовности из readiness-check-guide.md

**Что делает:**
1. Применяет критерии готовности к документам
2. Определяет статус api-readiness (ready/needs-work/blocked/in-review/not-applicable)
3. Обновляет метаданные документов
4. Обновляет readiness-tracker.yaml
5. Создаёт отчёт о готовности с предложениями по доработке

**Выходные данные:**
- Обновлённые метаданные документов (api-readiness)
- Обновлённый readiness-tracker.yaml
- Отчёт о готовности документов

**Системы отслеживания:**
- Обновляет api-readiness в метаданных документов
- Обновляет readiness-tracker.yaml

**Следующий этап:** ДУАПИТАСК (для документов с api-readiness: ready)

---

### Этап 3: ДУАПИТАСК (API Task Creator)

**Агент:** API Task Creator Agent  
**Документ:** `API-SWAGGER/ДУАПИТАСК.MD`  
**Роль:** Создание заданий для API Executor из готовых документов .BRAIN

**Входные данные:**
- Документы .BRAIN с api-readiness: ready
- Шаблон задания (api-generation-task-template.md)
- Инструкция по созданию заданий (task-creation-guide.md)

**Что делает:**
1. Читает готовые документы из .BRAIN
2. Анализирует механики, правила, зависимости
3. Создаёт полное самодостаточное задание для API Executor
4. Сохраняет задание в tasks/active/queue/
5. Обновляет brain-mapping.yaml (связь .BRAIN → задание)
6. Обновляет документ .BRAIN (добавляет секцию отслеживания)

**Выходные данные:**
- Задание в tasks/active/queue/task-XXX-description.md
- Обновлённый brain-mapping.yaml (статус: queued)
- Обновлённый документ .BRAIN (секция API Tasks Status)

**Системы отслеживания:**
- Создаёт запись в brain-mapping.yaml (status: queued)
- Обновляет документ .BRAIN (API Tasks Status)

**Следующий этап:** АПИТАСК

---

### Этап 4: АПИТАСК (API Executor)

**Агент:** API Executor Agent  
**Документ:** `API-SWAGGER/АПИТАСК.MD`  
**Роль:** Создание OpenAPI спецификаций на основе заданий

**Входные данные:**
- Задание из tasks/active/queue/
- Правила API-SWAGGER (api-swagger-rules.mdc)
- Существующие API спецификации (для стиля)

**Что делает:**
1. Читает задание из queue/
2. Создаёт OpenAPI спецификацию (endpoints, модели, ошибки)
3. Использует общие компоненты из shared/common/
4. Соблюдает ограничение 400 строк на файл
5. Сохраняет API спецификацию в API-SWAGGER/api/v1/
6. Обновляет brain-mapping.yaml (status: completed)
7. **ОБЯЗАТЕЛЬНО**: Создаёт запись в implementation-tracker.yaml

**Выходные данные:**
- OpenAPI спецификация в api/v1/.../file.yaml
- Обновлённый brain-mapping.yaml (status: completed)
- **Запись в implementation-tracker.yaml** (api_status: completed, backend/frontend: not_started)

**Системы отслеживания:**
- Обновляет brain-mapping.yaml (status: completed)
- **ОБЯЗАТЕЛЬНО создаёт запись в implementation-tracker.yaml**

**Следующий этап:** БЭКТАСК и ФРОНТТАСК

---

### Этап 5: БЭКТАСК (Backend Agent)

**Агент:** Backend Agent  
**Документ:** `BACK-GO/docs/БЭКТАСК.MD`  
**Роль:** Создание backend реализации на Java Spring Boot

**Входные данные:**
- OpenAPI спецификация из API-SWAGGER/api/v1/
- Запись в implementation-tracker.yaml (backend.status: not_started)

**Что делает:**

**При НАЧАЛЕ работы (ОБЯЗАТЕЛЬНО):**
1. Открывает implementation-tracker.yaml
2. Находит запись для API (по api_path)
3. Обновляет:
   - backend.status: "in_progress"
   - backend.started: "YYYY-MM-DD HH:MM" (реальное время!)
   - backend.agent: "Backend Agent"

**Основная работа:**
4. **Определяет целевой микросервис** из API спецификации (x-microservice)
5. Генерирует контракты в целевой микросервис:
   - Использует скрипт: `generate-openapi-microservices.ps1`
   - Генерирует в: `microservices/{service-name}/src/main/java/`
   - Создаёт: DTOs, API Interfaces, Service Interfaces
6. Создаёт реализацию вручную в микросервисе:
   - Entities, Repositories, Controllers, ServiceImpl
   - В директории: `microservices/{service-name}/`
7. Создаёт Liquibase миграции для БД микросервиса
8. Создаёт seed данные (с проверкой существования)
9. Тестирует API через API Gateway (port 8080)

**При ЗАВЕРШЕНИИ работы (ОБЯЗАТЕЛЬНО):**
10. Обновляет implementation-tracker.yaml:
   - backend.status: "completed" (или "failed")
   - backend.completed: "YYYY-MM-DD HH:MM"
   - backend.commit: "хэш коммита"
   - backend.notes: "микросервис: {service-name}" (указать сервис)
11. Делает коммит через autocommit

**Выходные данные:**
- Backend код в `BACK-GO/microservices/{service-name}/src/main/java/`
- Liquibase миграции в `microservices/{service-name}/src/main/resources/db/changelog/`
- Обновлённый implementation-tracker.yaml (backend.status: completed)
- Коммит в git

**Микросервисы:**
- auth-service (8081) - аутентификация
- character-service (8082) - персонажи
- gameplay-service (8083) - геймплей
- social-service (8084) - социальное
- economy-service (8085) - экономика
- world-service (8086) - мир

**Системы отслеживания:**
- **ОБЯЗАТЕЛЬНО обновляет implementation-tracker.yaml** (при начале и завершении)

**Следующий этап:** ФРОНТТАСК (может работать параллельно или после)

---

### Этап 6: ФРОНТТАСК (Frontend Agent)

**Агент:** Frontend Agent  
**Документ:** `FRONT-WEB/ФРОНТТАСК.MD`  
**Роль:** Создание frontend реализации на React + TypeScript

**⚠️ ВАЖНО:** Фронтенд разрабатывается **ПОСЛЕ** бекенда! Проверь, что backend.status: completed.

**Входные данные:**
- OpenAPI спецификация из API-SWAGGER/api/v1/
- Запись в implementation-tracker.yaml (frontend.status: not_started)
- **Готовый бекенд** (backend.status: completed)

**Что делает:**

**При НАЧАЛЕ работы (ОБЯЗАТЕЛЬНО):**
1. Проверяет, что backend.status: completed
2. Открывает implementation-tracker.yaml
3. Находит запись для API (по api_path)
4. Обновляет:
   - frontend.status: "in_progress"
   - frontend.started: "YYYY-MM-DD HH:MM" (реальное время!)
   - frontend.agent: "Frontend Agent"

**Основная работа:**
5. **Определяет целевой модуль** из API спецификации (комментарии о модуле)
6. Генерирует TypeScript клиент и React Query хуки (Orval)
   - Использует API Gateway URL: `http://localhost:8080`
   - Создаёт хуки в: `src/api/generated/{category}/`
7. Создаёт модульную структуру:
   - Модуль: `modules/{module}/{feature}/`
   - Использует библиотеки: `@shared/ui`, `@shared/forms`, `@shared/layouts`
8. Создаёт страницы (pages) для роутинга
9. Настраивает роуты в app/router.tsx (React Router)
10. Создаёт компоненты используя UI Kit (@shared/ui)
11. Интегрирует с state store (use{Module}Store)
12. Интегрирует с сгенерированными хуками (useQuery, useMutation)
13. Добавляет защищённые роуты (если требуется)
14. Тестирует интеграцию с бекендом через API Gateway

**При ЗАВЕРШЕНИИ работы (ОБЯЗАТЕЛЬНО):**
13. Обновляет implementation-tracker.yaml:
   - frontend.status: "completed" (или "failed")
   - frontend.completed: "YYYY-MM-DD HH:MM"
   - frontend.commit: "хэш коммита"
   - frontend.notes: "примечания" (опционально)
14. Делает коммит через autocommit

**Выходные данные:**
- Frontend код в FRONT-WEB/src/
- Сгенерированные хуки в src/api/generated/
- Обновлённый implementation-tracker.yaml (frontend.status: completed)
- Коммит в git

**Системы отслеживания:**
- **ОБЯЗАТЕЛЬНО обновляет implementation-tracker.yaml** (при начале и завершении)

**Результат:** ГОТОВАЯ ФИЧА ✅

---

## Системы отслеживания

### 1. readiness-tracker.yaml

**Расположение:** `.BRAIN/06-tasks/config/readiness-tracker.yaml`

**Назначение:** Отслеживание готовности документов .BRAIN к созданию API задач

**Кто обновляет:**
- МЕНЕДЖЕР: обновляет api-readiness в метаданных документов
- Brain Readiness Checker: обновляет api-readiness и readiness-tracker.yaml

**Статусы:**
- `ready` - готов к созданию задачи API
- `needs-work` - нужна доработка
- `blocked` - заблокирован зависимостями
- `in-review` - проверяется на готовность
- `not-applicable` - не предназначен для создания API

---

### 2. brain-mapping.yaml

**Расположение:** `API-SWAGGER/tasks/config/brain-mapping.yaml`

**Назначение:** Отслеживание связей .BRAIN документов → задания API → API спецификации

**Кто обновляет:**
- ДУАПИТАСК: создаёт запись (status: queued)
- АПИТАСК: обновляет статус (status: completed)

**Статусы:**
- `queued` - задание в очереди
- `assigned` - задание назначено
- `in_progress` - задание в работе
- `completed` - задание завершено
- `failed` - задание провалилось

---

### 3. implementation-tracker.yaml

**Расположение:** `.BRAIN/06-tasks/config/implementation-tracker.yaml`

**Назначение:** Отслеживание реализации backend и frontend для каждого API

**Кто обновляет:**
- **АПИТАСК**: создаёт запись (api_status: completed, backend/frontend: not_started)
- **БЭКТАСК**: обновляет backend.status (in_progress → completed)
- **ФРОНТТАСК**: обновляет frontend.status (in_progress → completed)

**Статусы:**
- `not_started` - реализация ещё не начата
- `in_progress` - реализация в процессе
- `completed` - реализация завершена успешно
- `failed` - реализация провалилась

---

## Таблица переходов статусов

### Документ .BRAIN

| Этап | Статус документа | api-readiness | Кто обновляет |
|------|-----------------|---------------|---------------|
| 1 | draft | needs-work | МЕНЕДЖЕР |
| 2 | review | in-review | МЕНЕДЖЕР |
| 3 | review | ready | Brain Readiness Checker |
| 4 | approved | ready | МЕНЕДЖЕР |

---

### Задание API

| Этап | brain-mapping status | Кто обновляет |
|------|---------------------|---------------|
| 1 | queued | ДУАПИТАСК |
| 2 | in_progress | АПИТАСК (опционально) |
| 3 | completed | АПИТАСК |

---

### Реализация API

| Этап | api_status | backend.status | frontend.status | Кто обновляет |
|------|-----------|---------------|-----------------|---------------|
| 1 | completed | not_started | not_started | АПИТАСК |
| 2 | completed | in_progress | not_started | БЭКТАСК (начало) |
| 3 | completed | completed | not_started | БЭКТАСК (завершение) |
| 4 | completed | completed | in_progress | ФРОНТТАСК (начало) |
| 5 | completed | completed | completed | ФРОНТТАСК (завершение) |

---

## Точки синхронизации

### 1. МЕНЕДЖЕР → Brain Readiness Checker

**Условие перехода:** Документ имеет статус approved или review

**Проверка:**
- Документ достаточно детализирован?
- Описаны все механики?
- Нет блокирующих TODO?

---

### 2. Brain Readiness Checker → ДУАПИТАСК

**Условие перехода:** api-readiness: ready

**Проверка:**
- Статус документа: approved/review
- api-readiness: ready
- Критичные зависимости завершены

---

### 3. ДУАПИТАСК → АПИТАСК

**Условие перехода:** Задание создано в tasks/active/queue/

**Проверка:**
- Задание полное и самодостаточное
- Все секции заполнены
- Задание прошло чеклист

---

### 4. АПИТАСК → БЭКТАСК

**Условие перехода:** API спецификация создана, запись в implementation-tracker.yaml создана

**Проверка:**
- API спецификация валидна (OpenAPI 3.0.3)
- brain-mapping.yaml обновлён (status: completed)
- implementation-tracker.yaml создана запись (backend.status: not_started)

---

### 5. БЭКТАСК → ФРОНТТАСК

**Условие перехода:** backend.status: completed

**Проверка:**
- Бекенд реализован и протестирован
- API endpoints доступны и работают
- implementation-tracker.yaml обновлён (backend.status: completed)

---

## Обязательные проверки

### МЕНЕДЖЕР (перед завершением работы)

- [ ] Документ имеет статус approved или review
- [ ] api-readiness определён (ready/needs-work/etc.)
- [ ] Все критичные детали проработаны
- [ ] Нет блокирующих TODO
- [ ] Метаданные обновлены (если применимо)
- [ ] Коммит сделан

---

### Brain Readiness Checker (перед завершением проверки)

- [ ] Все критерии готовности применены
- [ ] Метаданные документов обновлены (api-readiness)
- [ ] readiness-tracker.yaml обновлён
- [ ] Получено РЕАЛЬНОЕ время из системы
- [ ] Коммит сделан

---

### ДУАПИТАСК (перед созданием задания)

- [ ] Документ имеет api-readiness: ready
- [ ] Задание создано по шаблону
- [ ] Все секции заполнены
- [ ] Задание прошло чеклист
- [ ] brain-mapping.yaml обновлён (status: queued)
- [ ] Документ .BRAIN обновлён (секция API Tasks Status)
- [ ] Коммит сделан

---

### АПИТАСК (перед завершением работы)

- [ ] API спецификация создана
- [ ] API валидна (OpenAPI 3.0.3)
- [ ] Используются общие компоненты из shared/common/
- [ ] Размер файла ≤ 400 строк
- [ ] brain-mapping.yaml обновлён (status: completed)
- [ ] **implementation-tracker.yaml запись создана**
- [ ] Коммит сделан

---

### БЭКТАСК (перед началом работы)

- [ ] API спецификация существует и валидна
- [ ] API содержит x-microservice или путь позволяет определить микросервис
- [ ] implementation-tracker.yaml запись существует
- [ ] **implementation-tracker.yaml обновлён (backend.status: in_progress)**

### БЭКТАСК (перед завершением работы)

- [ ] Определён целевой микросервис из API спецификации
- [ ] Контракты сгенерированы в целевой микросервис (`generate-openapi-microservices.ps1`)
- [ ] Реализация создана в микросервисе (Entities, Repos, Controllers, Services)
- [ ] Liquibase миграции созданы для микросервиса
- [ ] Seed данные созданы
- [ ] API протестирован через API Gateway (port 8080)
- [ ] **implementation-tracker.yaml обновлён (backend.status: completed, notes: микросервис)**
- [ ] Коммит сделан

---

### ФРОНТТАСК (перед началом работы)

- [ ] API спецификация существует и валидна
- [ ] API содержит комментарии о целевом модуле и UI компонентах
- [ ] **Бекенд реализован (backend.status: completed)**
- [ ] implementation-tracker.yaml запись существует
- [ ] **implementation-tracker.yaml обновлён (frontend.status: in_progress)**

### ФРОНТТАСК (перед завершением работы)

- [ ] Определён целевой модуль из API спецификации
- [ ] TypeScript клиент и хуки сгенерированы (Orval)
- [ ] Модульная структура создана (`modules/{module}/{feature}/`)
- [ ] Использованы компоненты из UI Kit (@shared/ui, @shared/forms)
- [ ] Интеграция с state store (use{Module}Store)
- [ ] Страницы (pages) созданы
- [ ] Роуты настроены (React Router)
- [ ] Интеграция с API Gateway протестирована (http://localhost:8080)
- [ ] **implementation-tracker.yaml обновлён (frontend.status: completed, notes: модуль)**
- [ ] Коммит сделан

---

## Ссылки на документы агентов

### Основные агенты

- **МЕНЕДЖЕР:** [.BRAIN/МЕНЕДЖЕР.MD](.BRAIN/МЕНЕДЖЕР.MD)
- **Brain Readiness Checker:** [.BRAIN/06-tasks/config/ЧЕКБРЕЙН.MD](.BRAIN/06-tasks/config/ЧЕКБРЕЙН.MD)
- **ДУАПИТАСК:** [API-SWAGGER/ДУАПИТАСК.MD](API-SWAGGER/ДУАПИТАСК.MD)
- **АПИТАСК:** [API-SWAGGER/АПИТАСК.MD](API-SWAGGER/АПИТАСК.MD)
- **БЭКТАСК:** [BACK-GO/docs/БЭКТАСК.MD](BACK-GO/docs/БЭКТАСК.MD)
- **ФРОНТТАСК:** [FRONT-WEB/ФРОНТТАСК.MD](FRONT-WEB/ФРОНТТАСК.MD)

### Справочные материалы

- **Статусы:** [.BRAIN/06-tasks/config/STATUSES-GUIDE.md](.BRAIN/06-tasks/config/STATUSES-GUIDE.md)
- **Детали воркфлоу:** [.BRAIN/06-tasks/config/WORKFLOW-DETAILS.md](.BRAIN/06-tasks/config/WORKFLOW-DETAILS.md)
- **Шаблоны:** [.BRAIN/06-tasks/config/TEMPLATES-GUIDE.md](.BRAIN/06-tasks/config/TEMPLATES-GUIDE.md)
- **Реестр агентов:** [.BRAIN/06-tasks/config/AGENTS-REGISTRY.md](.BRAIN/06-tasks/config/AGENTS-REGISTRY.md)

### Системы отслеживания

- **readiness-tracker.yaml:** [.BRAIN/06-tasks/config/readiness-tracker.yaml](.BRAIN/06-tasks/config/readiness-tracker.yaml)
- **brain-mapping.yaml:** [API-SWAGGER/tasks/config/brain-mapping.yaml](API-SWAGGER/tasks/config/brain-mapping.yaml)
- **implementation-tracker.yaml:** [.BRAIN/06-tasks/config/implementation-tracker.yaml](.BRAIN/06-tasks/config/implementation-tracker.yaml)

---

## Примеры полного прохождения

### Пример: Создание API для системы романтических отношений

**1. МЕНЕДЖЕР создаёт документ:**
- Файл: `.BRAIN/02-gameplay/social/romance-system.md`
- Статус: draft → review → approved
- api-readiness: needs-work → ready

**2. Brain Readiness Checker проверяет:**
- Применяет критерии готовности
- Обновляет api-readiness: ready
- Обновляет readiness-tracker.yaml

**3. ДУАПИТАСК создаёт задание:**
- Задание: `tasks/active/queue/task-065-romance-system-api.md`
- brain-mapping.yaml: status queued

**4. АПИТАСК создаёт API:**
- API: `api/v1/gameplay/social/romance-system.yaml`
- brain-mapping.yaml: status completed
- implementation-tracker.yaml: запись создана

**5. БЭКТАСК реализует backend:**
- implementation-tracker.yaml: backend.status in_progress
- Генерирует контракты
- Создаёт реализацию
- implementation-tracker.yaml: backend.status completed

**6. ФРОНТТАСК реализует frontend:**
- implementation-tracker.yaml: frontend.status in_progress
- Генерирует хуки
- Создаёт компоненты
- implementation-tracker.yaml: frontend.status completed

**Результат:** Готовая фича романтической системы ✅

---

## Часто задаваемые вопросы

### Когда документ готов к созданию API задачи?

Документ готов, когда:
- Статус документа: approved или review
- api-readiness: ready
- Достаточно деталей для создания API
- Нет блокирующих TODO
- Критичные зависимости завершены

---

### Что делать, если бекенд провалился (backend.status: failed)?

1. Проверь, в чём проблема (см. backend.notes в implementation-tracker.yaml)
2. Исправь проблему
3. Обнови backend.status обратно на in_progress
4. После исправления обнови на completed

---

### Можно ли делать фронтенд параллельно с бекендом?

Нет. Фронтенд разрабатывается **ПОСЛЕ** бекенда.

**Причина:** Фронтенд зависит от готового API бекенда для тестирования интеграции.

**Проверка:** Перед началом работы фронтенд-агент должен убедиться, что backend.status: completed.

---

### Как узнать, какие API готовы к реализации?

Проверь `implementation-tracker.yaml`:
- api_status: completed
- backend.status: not_started ← готов к реализации бекенда
- frontend.status: not_started ← готов к реализации фронтенда (если backend: completed)

---

### Что делать, если API спецификация изменилась после реализации?

1. Обнови API спецификацию через АПИТАСК
2. Перегенерируй контракты на бекенде (БЭКТАСК)
3. Перегенерируй клиент на фронтенде (ФРОНТТАСК)
4. Обнови implementation-tracker.yaml (примечание о перегенерации)

---

## Принципы проекта

1. **API First:** Сначала API спецификация, потом реализация
2. **SOLID, DRY, KISS:** Соблюдать во всех документах и коде
3. **Не хардкодить:** Всё хранить в БД
4. **Миграции БД:** Использовать Liquibase для версионирования схемы
5. **Тестирование:** Покрытие тестами не менее 50%
6. **Коммиты:** Обязательно коммитить после завершения работы
7. **Отслеживание:** Обязательно обновлять трекеры статусов

---

**Версия документа:** 1.1.0  
**Дата последнего обновления:** 2025-11-08
**Изменения в версии 1.1.0:**
- Добавлена информация о микросервисной архитектуре backend
- Добавлена информация о модульной архитектуре frontend
- Обновлены чеклисты для БЭКТАСК и ФРОНТТАСК
- Добавлены таблицы микросервисов и модулей

