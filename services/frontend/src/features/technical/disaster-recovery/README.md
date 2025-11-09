# Disaster Recovery Feature
Командный центр аварийного восстановления: статус DR, планы бэкапов, цели failover и журнал инцидентов.

**OpenAPI:** technical/disaster-recovery.yaml | **Роут:** /technical/disaster-recovery

## UI
- `DisasterRecoveryPage` — SPA (380 / flex / 320), фильтры региона/режима, auto failover toggle
- Компоненты:
  - `DrStatusCard`
  - `BackupPlanCard`
  - `FailoverTargetsCard`
  - `EmergencyActionsCard`
  - `IncidentLogCard`
  - `QuestAvailabilityCard`

## Возможности
- Мониторинг DR готовности (RPO/RTO, failover статус)
- Планы бэкапа с расписанием и ретеншеном
- Цели failover с емкостью и задержкой
- Журнал инцидентов и предупреждения
- Быстрые emergency действия (backup, restore, failover)
- Компактная сетка в одном экране, киберпанк стиль

## Тесты
- Юнит-тесты для карточек в `components/__tests__`
- Написаны, **не запускались**

