# Configuration Management Feature
Центр управления конфигурациями: версии сервисов, секреты, среды и hot reload.

**OpenAPI:** technical/configuration-management.yaml | **Роут:** /technical/configuration-management

## UI
- `ConfigurationManagementPage` — SPA (380 / flex / 320), фильтры сервиса/окружения, auto reload toggle
- Компоненты:
  - `ServiceConfigCard`
  - `SecretsCard`
  - `EnvironmentSummaryCard`
  - `ReloadControlCard`

## Возможности
- Просмотр конфигурации сервисов, версии и ключей
- Метаданные секретов с датами обновления
- Сводка окружений (services, overrides, drift alerts)
- Управление hot reload и быстрые действия
- Киберпанк сетка под один экран

## Тесты
- Юнит-тесты для карточек в `components/__tests__`
- Написаны, **не запускались**


