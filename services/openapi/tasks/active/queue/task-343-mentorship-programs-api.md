# Task ID: API-TASK-343
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-08 19:10  
**Создатель:** AI Task Creator Agent  
**Зависимости:** none

---

## 📋 Краткое описание

Создать спецификацию `Mentorship Programs API`, описывающую каталог программ наставничества, расписания и контент обучения для игроков и NPC.  
**Целевой файл:** `api/v1/social/mentorship/programs.yaml`

---

## 🎯 Цель задания

Предоставить social-service REST API, которое:
- регистрирует и управляет программами наставничества (типы, цели, требования, расписание, контент);
- поддерживает различные форматы обучения (теория, практика, VR-курсы, групповые занятия);
- отслеживает прогресс, аттестации и награды (очки опыта, достижения, доступы);
- обеспечивает интеграции с content-service (учебные материалы), gameplay-service (прирост навыков) и economy-service (оплаты, гранты);
- предоставляет UI все необходимые данные для модулей наставничества и академий.

---

## 📚 Источники информации

### Основной документ

**Репозиторий:** `.BRAIN`  
**Путь:** `.BRAIN/02-gameplay/social/mentorship-system-детально.md`  
**Версия:** 1.0.0  
**Дата обновления:** 2025-11-08 10:20  
**Статус документа:** approved (api-readiness: ready)

**Ключевые разделы:**
- Разделы 2–5: типы наставничества, пайплайн, форматы обучения, навыковые треки и награды.  
- Раздел 8: учебный контент и интеграция с content-service.  
- Раздел 12: использование документа и связь с другими системами.  
- Раздел 13: REST макеты (`POST /social/mentorship/programs`, `GET /social/mentorship/schedule/{programId}`) и JSON схемы.  
- Раздел 14–15: Kafka события и метрики (LessonCompletionRate, MentorSatisfactionScore).

### Дополнительные источники

- `.BRAIN/02-gameplay/social/mentorship-world-impact-детально.md` — влияние программ на мир, метрики и события.  
- `.BRAIN/02-gameplay/progression/progression-skills.md` — связь навыков и прогресса.  
- `.BRAIN/05-technical/content-generation/mentorship-content-pipeline.md` — требования к загрузке и модерации контента.  
- `.BRAIN/02-gameplay/social/relationships-system-детально.md` — влияние наставничества на доверие и репутацию.

---

## 📁 Целевая структура API

**Репозиторий:** `API-SWAGGER`  
**Файл:** `api/v1/social/mentorship/programs.yaml`  
**Тип:** OpenAPI 3.0.3 (YAML)

**Структура директории:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── social/
            └── mentorship/
                ├── components/
                │   ├── schemas/
                │   ├── responses/
                │   └── examples/
                └── programs.yaml
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend:
- **Микросервис:** social-service (port 8084)  
- **Интеграции:** content-service (учебные материалы), gameplay-service (навыки, XP), economy-service (гранты, оплаты), notification-service (напоминания), analytics-service (LessonCompletionRate).  
- **Kafka:** `social.mentorship.lesson.completed`, `social.mentorship.contract.signed`, `social.mentorship.contract.terminated`.

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend:
- **Модуль:** modules/social/mentorship  
- **State Store:** `useSocialStore(mentorshipPrograms)`  
- **UI:** `MentorshipDashboard`, `ProgramDetailsCard`, `MentorshipScheduleBoard`, `LessonProgressTracker`, `MentorReputationBadge`  
- **Формы:** `ProgramCreationForm`, `LessonPlanForm`, `MentorshipEnrollmentForm`  
- **Layouts:** `MentorshipLayout`, `AcademyProgramsLayout`  
- **Hooks:** `useMentorshipPrograms`, `useMentorshipProgram`, `useMentorshipSchedule`, `useMentorshipEnrollment`

**Комментарий в YAML:**
```
# Target Architecture:
# - Microservice: social-service (port 8084)
# - Frontend Module: modules/social/mentorship
# - State Store: useSocialStore(mentorshipPrograms)
# - UI: MentorshipDashboard, ProgramDetailsCard, MentorshipScheduleBoard, LessonProgressTracker, MentorReputationBadge
# - Forms: ProgramCreationForm, LessonPlanForm, MentorshipEnrollmentForm
# - Layouts: MentorshipLayout, AcademyProgramsLayout
# - Hooks: useMentorshipPrograms, useMentorshipProgram, useMentorshipSchedule, useMentorshipEnrollment
# - Events: social.mentorship.lesson.completed, social.mentorship.contract.signed, social.mentorship.contract.terminated
# - API Base: /api/v1/social/mentorship/*
```

---

## ✅ Детальный план

1. **Определить модель программы:** тип, цели, требования по репутации/уровню, длительность, формат, награды.  
2. **Проработать расписание:** уроки, блоки, форматы (theory, practice, VR), требования к контенту.  
3. **Определить процессы записи:** заявки, подтверждения, квоты, ожидание.  
4. **Спроектировать схемы:** `MentorshipProgram`, `MentorshipLesson`, `MentorshipSchedule`, `MentorshipEnrollmentRequest`, `MentorshipProgress`, `MentorReputation`.  
5. **Разработать эндпоинты для CRUD программ, расписаний, записей, прогресса и отзывов.**  
6. **Документировать интеграции:** ссылки на контент (content-service), навыки (gameplay-service), платежи (economy-service).  
7. **Задокументировать Kafka события (lesson.completed, contract.signed/terminated) и их влияние.**  
8. **Добавить примеры:** индивидуальная программа, корпоративный курс, VR-практикум, групповое обучение, расписание недели.  
9. **Использовать shared security/responses/pagination, вынести схемы/примеры в компоненты, соблюдать лимит 400 строк.**  
10. **Прогнать `scripts/validate-swagger.ps1`, обновить README в каталоге.**

---

## 🔌 Эндпоинты

1. **POST `/social/mentorship/programs`** — создание программы (администраторы/менторы).  
2. **GET `/social/mentorship/programs/{programId}`** — детальная информация (описание, требования, прогресс).  
3. **GET `/social/mentorship/programs`** — каталог с фильтрами (тип, сложность, формат, академия, доступность).  
4. **PATCH `/social/mentorship/programs/{programId}`** — обновление (`lessons`, `requirements`, `rewards`).  
5. **DELETE `/social/mentorship/programs/{programId}`** — деактивация (soft-delete с audit).  
6. **GET `/social/mentorship/programs/{programId}/schedule`** — расписание занятий.  
7. **POST `/social/mentorship/programs/{programId}/enroll`** — подача заявки / зачисление (с проверкой квот).  
8. **GET `/social/mentorship/programs/{programId}/progress`** — прогресс ученика/группы.  
9. **GET `/social/mentorship/programs/{programId}/content`** — ссылки на учебные материалы (контент-сервис).  
10. **POST `/social/mentorship/programs/{programId}/feedback`** — отзывы учеников о программе и уроках.

---

## 🧱 Модели данных

- **MentorshipProgram** — `programId`, `title`, `description`, `type`, `format`, `duration`, `difficulty`, `requirements[]`, `rewards[]`, `academyId`, `mentorId`, `status`.  
- **MentorshipLesson** — `lessonId`, `title`, `lessonType`, `mode`, `objectives`, `duration`, `contentRefs[]`, `xpAwards`.  
- **MentorshipSchedule** — календарь занятий (`startAt`, `endAt`, `timezone`, `slots[]`, `constraints`).  
- **MentorshipEnrollmentRequest** — `programId`, `participantId`, `role`, `motivation`, `paymentInfo`, `requestedStart`.  
- **MentorshipProgress** — `participantId`, `completedLessons`, `xpEarned`, `skillDelta`, `certifications`.  
- **MentorReputation** — `mentorId`, `rating`, `reviews[]`, `successRate`, `complaints`, `badges`.  
- **MentorshipFeedback** — `feedbackId`, `lessonId`, `rating`, `comment`, `submittedAt`.  
- **PaginatedMentorshipPrograms** — стандартная пагинация.  
- **MentorshipProgramFilter** — фильтры каталога (тип, формат, уровень навыков, академия, теги).

---

## 📏 Принципы и правила

- OpenAPI 3.0.3; файл ≤400 строк (схемы/примеры выносим в `components`).  
- Использовать `shared/common/security.yaml`, `shared/common/responses.yaml`, `shared/common/pagination.yaml`.  
- Ошибки с `x-error-code`: `VAL_MENTORSHIP_PROGRAM_INVALID`, `BIZ_MENTORSHIP_PROGRAM_NOT_FOUND`, `BIZ_MENTORSHIP_CAPACITY_EXCEEDED`, `BIZ_MENTORSHIP_ENROLLMENT_CONFLICT`, `INT_MENTORSHIP_CONTENT_UNAVAILABLE`.  
- `info.description` должен ссылаться на `.BRAIN` источники, UX подтверждения и связанные сервисы.  
- Добавить `tags`: `Mentorship`, `Programs`, `Schedule`, `Progress`, `Reputation`.  
- Указать связи с API доверия/репутации и контрактов наставничества.

---

## ✅ Критерии приемки

1. Файл `api/v1/social/mentorship/programs.yaml` создан/актуализирован и проходит `scripts/validate-swagger.ps1`.  
2. В начале файла присутствует `Target Architecture` блок.  
3. Описаны все указанные эндпоинты, схемы и примеры.  
4. Подключены общие компоненты безопасности/ответов/пагинации.  
5. Добавлены примеры программ (индивидуальная, корпоративная, VR-группа).  
6. Документированы требования к контенту и интеграции с content-service, gameplay-service, economy-service.  
7. Указаны Kafka события и метрики (LessonCompletionRate, MentorSatisfactionScore).  
8. README в `social/mentorship` обновлён (в рамках реализации).  
9. Добавлена запись в `brain-mapping.yaml`.  
10. Task статус синхронизирован в `.BRAIN` документе.  
11. Обозначены зависимости от `mentorship/contracts.yaml`, `relationships/status.yaml`, `player-orders/ratings.yaml`.

---

## ❓ FAQ

**Q:** Нужно ли хранить историю изменений программ?  
A: Да, предусмотреть `audit` и статусное поле (`draft`, `active`, `archived`); history может быть вынесена в отдельный ресурс или Kafka (вне scope).  

**Q:** Как обрабатывать контент, созданный игроками?  
A: Использовать ссылки на content-service (`contentId`, `contentType`); API возвращает только метаданные и статус модерации.  

**Q:** Поддерживаем ли автопродление программ?  
A: Да, добавить флаги `recurring`, `recurrenceRule`, а также расписание следующего цикла.  

**Q:** Требуется ли поддержка оффлайн-уроков?  
A: Да, включить `deliveryMode` (`online`, `onsite`, `hybrid`) и поля адреса/локации.  

---

**Следующие шаги исполнителя:** реализовать OpenAPI-файл, вынести компоненты, описать интеграции, подготовить примеры и прогнать линтеры.

