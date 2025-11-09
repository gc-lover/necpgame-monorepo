# Anti-Cheat Admin Feature
Админский центр анти-чита: репорты, баны, апелляции.

**OpenAPI:** anti-cheat.yaml | **Роут:** /admin/anti-cheat

**⭐ Сборка на shared библиотеке (`GameLayout`, `CompactCard`, `CyberpunkButton`).**

## Функционал
- Просмотр репортов по читам (фильтры: статус, серьезность)
- Метрики детекта (auto-ban, паттерны, очередь ревью)
- Статистика банов и апелляций
- Быстрые действия: репорт, назначение ревьюера, экспорт логов

## Компоненты
- **AntiCheatPage** — SPA сетка 380px | flex | 320px
- **ReportSummaryCard** — список репортов с severity
- **BanOverviewCard** — активные баны, апелляции, авто/ручные
- **DetectionStatsCard** — ключевые метрики detection
- **AppealsQueueCard** — очередь апелляций

## Примечания
- Шрифты 0.65–0.875rem, киберпанк стиль
- Тесты добавлены (не запускались)
- Готово к интеграции с `anti-cheat-api` (React Query hooks)


