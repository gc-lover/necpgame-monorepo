# Global State Extended Feature
Командный центр глобального состояния (синхронизация, конфликты, снапшоты).

**OpenAPI:** global-state-extended.yaml | **Роут:** /technical/global-state-extended

## UI
- `GlobalStateExtendedPage` — SPA (380 / flex / 320) с фильтрами и метриками
- Компоненты:
  - `GlobalStateSummaryCard`
  - `StateComponentCard`
  - `SyncStatusCard`
  - `ConflictResolutionCard`
  - `StateSnapshotCard`
  - `OperationQueueCard`

## Возможности
- Сводка версий мирового состояния и активных сессий
- Мониторинг компонент (world/factions/economy/player/quests/combat)
- Статус синхронизации шардов и очереди мутаций
- Конфликты и стратегии разрешения
- Снапшоты, теги и возможность отката
- 3-колоночная сетка, мелкие шрифты (0.65–0.875rem), киберпанк стиль

## Тесты
- Юнит-тесты для карточек в `components/__tests__` — написаны, **не запускались**

