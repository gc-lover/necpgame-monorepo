# AI Algorithms Feature
Внутреннее SPA для мониторинга romance AI, NPC personality и decision engine.

**OpenAPI:** internal/ai-algorithms.yaml | **Роут:** /internal/ai-algorithms

## UI
- `AIAlgorithmsPage` — SPA (380 / flex / 320), фильтры, debug switch, запуск операций
- Компоненты:
  - `CompatibilityCard`
  - `DialogueGeneratorCard`
  - `TriggerPredictionCard`
  - `PersonalityCard`
  - `NPCDecisionCard`
  - `AlgorithmMetricsCard`

## Возможности
- Совместимость романтических пар, рекомендации и факторы
- Генерация диалогов с вариантами выбора
- Предсказание триггеров романтических событий
- Профили личности NPC (traits, quirks)
- Решения NPC и вероятности действий
- Метрики алгоритмов (latency, throughput, cache hit)
- Компактный cyberpunk дизайн, всё на одном экране

## Тесты
- Юнит-тесты в `components/__tests__`
- Написаны, **не запускались**


