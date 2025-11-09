# Production Chains Feature
Производственные цепочки: стадии, задания, оптимизация и аналитика.

**OpenAPI:** production-chains.yaml | **Роут:** /game/production-chains

**⭐ ИСПОЛЬЗУЕТ shared/ библиотеку компонентов!**

## Функционал
- Обзор цепочек (weapons / armor / implants)
- Детализация стадий (inputs/outputs, длительность)
- Активные задания и прогресс
- Анализ прибыльности, ROI, рекомендации
- AI оптимизация производственных цепочек

## Компоненты
- **ChainsOverviewCard** — список цепочек, статус (optimal / bottleneck)
- **ChainStagesCard** — стадии цепочки, входы/выходы
- **ProductionJobsCard** — активные задания, прогресс, facility
- **ProfitabilityCard** — profit/cycle, ROI, рекомендации
- **OptimizationCard** — AI-тips, ожидаемый прирост
- **ProductionChainsPage** — использует `GameLayout`, `CyberpunkButton`, `cyberpunkTokens`, сетка 380px | flex | 320px

## Вдохновение
Factorio, EVE Online Industry, Satisfactory supply chains

**Соответствие:** SPA, компактный UI, шрифты 0.65-0.875rem, киберпанк стиль.


