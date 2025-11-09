# Mentorship Extended Feature
Расширенная система наставничества с легендарными наставниками, уроками и graduation.

**OpenAPI:** mentorship-extended.yaml | **Роут:** /game/mentorship-extended

## UI
- `MentorshipExtendedPage` — SPA (380 / flex / 320) с фильтрами по типу и легендарности
- Карточки на `CompactCard` и `ProgressBar`:
  - `MentorExtendedCard`
  - `MentorshipRelationshipCard`
  - `MentorshipLessonCard`
  - `MentorshipAbilityCard`
  - `GraduationStatusCard`
  - `MentorshipSummaryCard`

## Возможности
- Каталог наставников (тип, rank, legendary status, уникальные способности)
- Активные отношения: bond/trust, прогресс уроков
- Доступные уроки, сложность, требования, награды
- Передача способностей и легендарные навыки
- Graduation прогресс и требования
- Быстрые действия: поиск наставника, unlock abilities, legendary программы
- Малые шрифты (0.65–0.875rem), киберпанк цветовая схема

## Тесты
- Юнит-тесты для всех карточек (`components/__tests__`) — написаны, **не запускались**

