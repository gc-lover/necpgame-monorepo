# Stock Integration Service

Сервис интеграции фондовой биржи с другими игровыми системами.

## Функционал

- **Quest Integration** - влияние корпоративных квестов на акции
- **Faction Events** - влияние фракционных войн на репутацию корпораций
- **World Events** - макроэкономические кризисы, технологические прорывы
- **Event Mapping** - настраиваемые правила маппинга событий на акции
- **Preview Simulation** - предварительный расчет влияния без применения
- **Event Journal** - журнал всех интеграционных событий

## API Endpoints

- `POST /stocks/integration/webhooks/quest` - webhook от quest-service
- `POST /stocks/integration/webhooks/faction` - webhook от faction-service
- `POST /stocks/integration/events/world` - мировые события
- `POST /stocks/integration/events/preview` - preview симуляция
- `GET /stocks/integration/mapping` - получить маппинги
- `GET /stocks/integration/journal` - журнал событий
- `POST /admin/stocks/integration/mapping` - создать/обновить маппинг (admin)
- `DELETE /admin/stocks/integration/mapping/{id}` - удалить маппинг (admin)

## Интеграции

- **quest-service** - webhooks при завершении квестов
- **faction-service** - webhooks при войнах
- **economy-events** - event bus `economy.integration.events`
- **news-feed** - трансляция новостей игрокам
- **stock-events-service** - применение влияния на акции

## Запуск

```bash
make generate-api
go run main.go
```

## Сборка

```bash
go build -o stock-integration-service .
```

## Docker

```bash
docker build -t stock-integration-service:latest .
docker run -p 8080:8080 stock-integration-service:latest
```

