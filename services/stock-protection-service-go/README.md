# Stock Protection Service

Сервис защиты фондовой биржи от манипуляций и контроля рисков.

## Функционал

- **Circuit Breakers** - автоматическая остановка торгов при >15% изменении цены
- **Surveillance Alerts** - детекция манипуляций (insider trading, spoofing, wash trading, pump & dump)
- **Enforcement Actions** - санкции к нарушителям (предупреждения, баны, конфискация)
- **Price Limits** - ±20% ограничения дневных изменений
- **Admin Controls** - ручное управление остановками и санкциями

## API Endpoints

- `GET /stocks/{stock_id}/circuit-breaker` - статус circuit breaker
- `GET /stocks/protection/alerts` - список алертов
- `GET /stocks/protection/alerts/{id}` - детали алерта
- `PATCH /stocks/protection/alerts/{id}` - обновить статус алерта
- `GET /stocks/protection/enforcement` - список санкций
- `POST /stocks/protection/enforcement` - создать санкцию
- `POST /admin/stocks/protection/circuit-breaker/trigger` - активировать breaker (admin)
- `POST /admin/stocks/protection/circuit-breaker/resume` - возобновить торги (admin)

## Генерация кода

```bash
make generate-api
```

## Запуск

```bash
go run .
```

## Порты

- **8080** - HTTP API
- **8081** - Метрики Prometheus

## Интеграции

- `anti-cheat-service` - передача данных о нарушителях
- `guild-system` - блокировка гильдейских операций
- `notification-service` - уведомления о санкциях
- `economy-events` - фильтрация легитимных новостей

