# Currency Exchange Feature
Валютная биржа - 12 региональных валют, arbitrage, leverage trading (Forex / EVE Online механики).

**OpenAPI:** currency-exchange.yaml | **Роут:** /game/currency-exchange

## Функционал
- **12 региональных валют:**
  - NCRD (Night City Dollar) - основная валюта
  - EURO (Euro Zone)
  - YUAN (Pacific Rim)
  - EBUCK (Nomad territories)
  - CORPO (Corporate scrip)
  - + 7 других валют
- **Currency pairs:**
  - Major (основные пары)
  - Minor (второстепенные)
  - Exotic (экзотические)
- **Real-time курсы:** постоянное обновление
- **Механики торговли:**
  - Currency conversion (обмен)
  - Arbitrage opportunities (арбитраж)
  - Hedging (хеджирование рисков)
  - Carry trade (процентные ставки)
  - Leverage trading (кредитное плечо до 10x)
- **Spread & commission:** 0.1-2% spread, 0.05% commission
- **Historical rates:** история курсов (1h, 24h, 7d, 30d, 1y)

## Компоненты
- **CurrencyPairCard** - карточка валютной пары (курс, изменение 24h, spread, volume)
- **CurrencyExchangePage** - список пар, курсы, механики торговли

## Механики
- Обмен валют
- Арбитраж между парами
- Leverage до 10x
- Риск-менеджмент (hedging)
- Carry trade прибыль

**Соответствие:** Полная реализация на основе OpenAPI, React Query хуки, MUI компоненты.

