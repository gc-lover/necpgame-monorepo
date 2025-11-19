# Kubernetes манифесты для NECPGAME

Манифесты для развертывания Go сервисов в Kubernetes кластере.

## Структура

- `namespace.yaml` - Namespace `necpgame`
- `realtime-gateway-go-deployment.yaml` - Deployment и Service для QUIC Gateway
- `ws-lobby-go-deployment.yaml` - Deployment и Service для WebSocket Lobby
- `matchmaking-go-deployment.yaml` - Deployment и Service для Matchmaking

## Развертывание

### Создание namespace

```bash
kubectl apply -f namespace.yaml
```

### Развертывание сервисов

```bash
kubectl apply -f realtime-gateway-go-deployment.yaml
kubectl apply -f ws-lobby-go-deployment.yaml
kubectl apply -f matchmaking-go-deployment.yaml
```

### Проверка статуса

```bash
kubectl get pods -n necpgame
kubectl get services -n necpgame
```

## Особенности

### Health Checks

Все сервисы используют `/metrics` endpoint для health checks:
- `livenessProbe` - проверка каждые 30 секунд
- `readinessProbe` - проверка каждые 10 секунд

### Ресурсы

Настроены requests и limits:
- Memory: 64Mi requests, 256Mi limits
- CPU: 100m requests, 500m limits

### Метрики

Все сервисы экспортируют метрики на порту `9090` через `/metrics` endpoint.

### Логирование

Логи структурированы в JSON формате и собираются через Promtail.

## Интеграция с ArgoCD

Манифесты готовы для использования с ArgoCD (см. `infrastructure/argocd/app-necpgame.yaml`).

