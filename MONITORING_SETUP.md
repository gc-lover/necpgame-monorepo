# Мониторинг NECP Game Services

## Обзор

Система мониторинга включает Prometheus, Grafana, Loki и AlertManager для полного наблюдения за сервисами MMOFPS RPG.

## Архитектура

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Services      │───▶│   Prometheus    │───▶│     Grafana     │
│   (Metrics)     │    │   (Metrics)     │    │  (Dashboards)   │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                        │                        │
         ▼                        ▼                        ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│    Promtail     │───▶│      Loki       │───▶│     Grafana     │
│   (Log ship)    │    │   (Log agg)     │    │   (Log view)    │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                                       │
                                                       ▼
                                            ┌─────────────────┐
                                            │  AlertManager   │
                                            │   (Alerts)      │
                                            └─────────────────┘
```

## Запуск мониторинга

### 1. Запустить основную систему
```bash
docker-compose up -d
```

### 2. Запустить мониторинг
```bash
docker-compose -f docker-compose.monitoring.yml up -d
```

### 3. Проверить статус
```bash
docker-compose -f docker-compose.monitoring.yml ps
```

## Доступ к интерфейсам

- **Grafana**: http://localhost:3000
  - Логин: `admin`
  - Пароль: `admin123`

- **Prometheus**: http://localhost:9090

- **AlertManager**: http://localhost:9093

- **Loki**: http://localhost:3100

## Dashboards

### Основной Dashboard
- **NECP Game Services Overview** - общая статистика всех сервисов
- Метрики: health status, request rate, response time, goroutines, memory

### Метрики сбора

Prometheus собирает метрики с:
- `/metrics` endpoint каждого сервиса
- Go runtime метрики (goroutines, memory, GC)
- HTTP request metrics (rate, duration, status codes)

## Логирование

Loki агрегирует логи из всех контейнеров:
- Структурированные JSON логи
- Поиск по сервисам и уровням логирования
- Корреляция логов с метриками

## Алерты

AlertManager настроен для отправки уведомлений:
- Service down alerts
- High error rate alerts
- Memory/CPU threshold alerts

## Настройка алертов

### В AlertManager (`infrastructure/monitoring/alertmanager.yml`)
```yaml
route:
  group_by: ['alertname']
  group_wait: 10s
  group_interval: 10s
  repeat_interval: 1h
  receiver: 'email'
```

### Правила алертов (добавить в Prometheus)
```yaml
groups:
- name: service-alerts
  rules:
  - alert: ServiceDown
    expr: up == 0
    for: 5m
    labels:
      severity: critical
    annotations:
      summary: "Service {{ $labels.job }} is down"
```

## Производительность

### Рекомендации по ресурсам
- **Prometheus**: 2GB RAM, 20GB disk
- **Grafana**: 1GB RAM, 5GB disk
- **Loki**: 2GB RAM, 50GB disk
- **Promtail**: 256MB RAM

### Оптимизация
- Настройте retention политики
- Используйте persistent volumes
- Мониторьте disk usage

## Troubleshooting

### Метрики не собираются
```bash
# Проверить endpoint
curl http://localhost:9200/metrics

# Проверить конфигурацию Prometheus
docker logs necpgame-prometheus
```

### Логи не появляются
```bash
# Проверить Promtail
docker logs necpgame-promtail

# Проверить Loki
docker logs necpgame-loki
```

### Dashboard пустой
```bash
# Проверить datasource в Grafana
# Configuration → Data Sources → Prometheus
```

## Расширение

### Добавление новых метрик
1. Добавить метрики в сервис
2. Обновить `prometheus.yml`
3. Перезапустить Prometheus

### Добавление новых алертов
1. Добавить правила в `prometheus.yml`
2. Настроить receivers в `alertmanager.yml`

### Кастомные dashboards
1. Создать JSON dashboard
2. Поместить в `infrastructure/monitoring/grafana/provisioning/dashboards/`
3. Перезапустить Grafana

## Производственные рекомендации

1. **Безопасность**
   - Настроить аутентификацию в Grafana
   - Использовать HTTPS
   - Ограничить доступ к портам

2. **Масштабирование**
   - Настроить кластер Prometheus
   - Использовать внешнее хранилище для Loki
   - Настроить load balancing

3. **Backup**
   - Резервное копирование Grafana dashboards
   - Backup Prometheus data
   - Архивация логов
