# MVP Content Feature
Модуль для контроля MVP контента: эндпоинты, модели данных, стартовые наборы и состояние текстовой версии.

**OpenAPI:** mvp-content.yaml | **Роут:** /meta/mvp-content

## UI
- `MVPContentPage` — SPA в фирменной сетке (380 / flex / 320)
- Карточки на `CompactCard` и `CyberpunkButton`
  - `EndpointsCard`
  - `ModelsCard`
  - `InitialDataCard`
  - `ContentOverviewCard`
  - `ContentStatusCard`
  - `TextVersionStateCard`
  - `MainUIDataCard`
  - `MVPHealthCard`

## Возможности
- Каталог MVP эндпоинтов, приоритеты, статус реализации
- Модели данных и поля
- Стартовые предметы, квесты, локации, NPC
- Обзор контента по периодам, процент готовности
- Статус систем MVP и health-check
- Отображение state упрощённой текстовой версии и данных основного UI

## Тесты
- Файлы в `components/__tests__` (не запускались по требованию)


