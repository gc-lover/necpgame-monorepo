# UI Systems Feature
Центр управления UI потоками: логин, выбор сервера, создание персонажей, HUD, настройки.

**OpenAPI:** technical/ui-systems.yaml | **Роут:** /technical/ui-systems

## UI
- `UISystemsPage` — SPA (380 / flex / 320), фильтры, действия, компактные панели
- Компоненты:
  - `LoginScreenCard`
  - `ServerListCard`
  - `CharacterCreationFlowCard`
  - `AppearanceOptionsCard`
  - `CharacterSelectCard`
  - `HUDOverviewCard`
  - `UIFeaturesCard`
  - `UISettingsCard`

## Возможности
- Данные экрана входа и rotating tips
- Список серверов с статусами, ping, населением
- Flow создания персонажа, appearance, character select
- HUD overview, UI features, настройки с пресетами и accessibility
- 3-колоночная сетка, мелкие шрифты (0.65–0.875rem), киберпанк стиль

## Тесты
- Юнит-тесты для карточек в `components/__tests__`
- Написаны, **не запускались** (требование)

