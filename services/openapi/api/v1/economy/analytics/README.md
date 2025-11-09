## Economy Analytics API

- `analytics.yaml` — REST и WebSocket контракты: графики, свечи, объёмы, портфели, heat maps, алерты, sentiment, стримы.
- `analytics-models.yaml` — базовые структуры данных: параметры запросов, точки графиков, свечи, портфельные метрики, алерты.
- `analytics-models-operations.yaml` — payload запросов/ответов, Kafka события `economy.analytics.*`, описания стримов.

Каждый файл ≤ 400 строк, структура удовлетворяет требованиям API-SWAGGER.
