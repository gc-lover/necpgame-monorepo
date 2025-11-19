# Observability Stack (Prometheus, Grafana, Loki, Tempo)

Запуск:
```bash
cd infrastructure/docker/observability
docker compose up -d
```

Панель Grafana: http://localhost:3000 (admin/admin)

Prometheus уже настроен на сбор метрик Envoy: host.docker.internal:9901 (/stats/prometheus).

Дальше:
- Подключить Java OTel агент к сервисам (JAVA_TOOL_OPTIONS c otel‑exporter‑otlp).
- Добавить promtail для отправки логов в Loki.


