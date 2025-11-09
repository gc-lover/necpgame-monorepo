# Stock Exchange Feature
Биржа акций корпораций - торговля акциями Arasaka, Militech, Biotechnica и других (EVE Online / NYSE / NASDAQ).

**OpenAPI:** stock-exchange-core.yaml | **Роут:** /game/stock-exchange

## Функционал
- **Корпорации:** Arasaka (ARSK), Militech (MILT), Biotechnica (BIOT), Kang Tao (KANG), Zetatech (ZETA)
- **Торговля:** Buy/Sell orders, Market/Limit orders, Shorting (короткие позиции), Margin trading
- **Портфолио:** Текущие holdings, profit/loss, ROI, общая стоимость
- **Дивиденды:** Выплаты акционерам, история, предстоящие выплаты
- **Новости:** События, влияющие на цены акций
- **Влияние событий:** Корпоративные войны → цены падают/растут, квесты → репутация → влияние на акции, глобальные события → рыночные шоки
- **Индексы:** CORPO-500 (индекс корпораций)

## Компоненты
- **StockCompanyCard** - карточка корпорации с тикером, ценой, изменением, капитализацией, дивидендами
- **StockExchangePage** - список корпораций, портфолио, новости, дивиденды

## Механики
- Цены зависят от событий мира
- Дивиденды акционерам
- Влияние квестов на репутацию корпораций
- Market cap, P/E ratio, dividend yield

**Соответствие:** Полная реализация на основе OpenAPI, React Query хуки, MUI компоненты.

