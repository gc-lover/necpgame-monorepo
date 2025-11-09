# Moderation Admin Feature
Центр модерации: репорты, санкции, производительность модераторов.

**OpenAPI:** moderation.yaml | **Роут:** /admin/moderation

**⭐ UI на shared библиотеке (`GameLayout`, `CompactCard`, `CyberpunkButton`).**

## Функционал
- Очередь кейсов с фильтрами по статусу/категории
- Статистика санкций (warnings, temporary/permanent bans, reinstated)
- Производительность модераторов (SLA, время решения)
- Быстрые действия: назначить кейс, создать санкцию, экспорт отчета

## Компоненты
- **ModerationPage** — SPA сетка 380px | flex | 320px
- **ModerationQueueCard** — список кейсов с цветовой индикацией
- **SanctionStatsCard** — ключевые показатели санкций
- **ModeratorPerformanceCard** — SLA и нагрузка по модераторам

## Примечания
- Шрифты 0.65–0.875rem, киберпанк тема
- Юнит-тесты добавлены (не запускались)
- Подготовлено к интеграции с `moderation-api`


