# Economy Analytics Feature
Экономическая аналитика - графики, индикаторы, портфолио (TradingView / Bloomberg Terminal style).

**OpenAPI:** analytics.yaml | **Роут:** /game/economy-analytics

**⭐ ИСПОЛЬЗУЕТ shared/ библиотеку компонентов!**

## Функционал
- Графики: Line, Candlestick, OHLC, Volume
- Технические индикаторы (MA/EMA/RSI/MACD/Bollinger)
- Market Sentiment (bull/bear ratio, volume trend, momentum)
- Heat Map (лучшие/худшие активы)
- Portfolio analytics (P/L, ROI, diversification)
- Trade history (win rate, средняя прибыль)
- Alerts (price/volume)

## Компоненты
- **PriceChartCard** — использует `CompactCard`, `cyberpunkTokens`
- **TechnicalIndicatorsCard** — список индикаторов с сигналами
- **MarketSentimentCard** — bull/bear ratio, volume trends, momentum
- **HeatMapCard** — топ роста/падения (проценты/объем)
- **PortfolioAnalyticsCard** — ROI, profit/loss, диверсификация, топ активы
- **TradeHistoryCard** — статистика сделок и последние трейды
- **AlertsCard** — активные price alerts
- **EconomyAnalyticsPage** — использует `GameLayout`, `CyberpunkButton`, компактную сетку 380px | flex | 320px

## Вдохновение
TradingView, Bloomberg Terminal, EVE Online Market Analytics

**Соответствие:** SPA, компактный UI на одном экране, шрифты 0.65-0.875rem, киберпанк стиль.

