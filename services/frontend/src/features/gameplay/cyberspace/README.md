# Cyberspace Feature
Киберпространство - полноценный режим игры с хабами, аренами, PvE-зонами и глубокими зонами.

**OpenAPI:** cyberspace-core.yaml | **Роут:** /game/cyberspace

## Функционал
- Вход/выход из киберпространства (требуется кибердека)
- 3 уровня доступа: Basic, Medium, Advanced (Netrunner)
- Типы зон: Hub, Arena, PvE Zone, Deep Zone, Custom
- Навигация между зонами
- Уникальный аватар в киберпространстве
- PvP/PvE активности

## Структура
- **CyberspaceZoneCard** - карточка зоны
- **CyberspacePage** - основная страница с управлением

**Соответствие:** Полная реализация на основе OpenAPI, React Query хуки, MUI компоненты.

