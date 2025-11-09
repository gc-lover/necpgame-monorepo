# Incident Response Feature
Оперативный центр инцидентов: регистрация, эскалация, таймлайн и RCA.

**OpenAPI:** technical/incident-response.yaml | **Роут:** /technical/incident-response

## UI
- `IncidentResponsePage` — SPA (380 / flex / 320), фильтры severity/war-room, auto notify toggle
- Компоненты:
  - `IncidentCard`
  - `EscalationCard`
  - `TimelineCard`
  - `RcaCard`
  - `OnCallCard`

## Возможности
- Мониторинг активных инцидентов (severity, статус, commander)
- Эскалации по уровням и каналам
- Таймлайн действий (детекция, митигейшн, коммуникация)
- Пост-инцидент RCA с корректирующими действиями
- On-call информация и war-room каналы
- Компактная cyberpunk сетка на одном экране

## Тесты
- Юнит-тесты для карточек в `components/__tests__`
- Написаны, **не запускались**


