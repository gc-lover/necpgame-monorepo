# Kubernetes манифесты для NECPGAME

Манифесты для развертывания Go сервисов в Kubernetes кластере.

## Структура

### Базовые ресурсы
- `namespace.yaml` - Namespace `necpgame`
- `configmap-common.yaml` - ConfigMap с общими настройками
- `secrets-common.yaml` - Secrets для БД, Redis, JWT
- `rbac-service-account.yaml` - ServiceAccount, Role, RoleBinding
- `networkpolicy-default.yaml` - NetworkPolicy для сетевой безопасности
- `ingress.yaml` - Ingress для внешнего доступа
- `servicemonitor-common.yaml` - ServiceMonitor для Prometheus Operator

### Сервисы
- `character-service-go-deployment.yaml` - Character Service
- `inventory-service-go-deployment.yaml` - Inventory Service
- `movement-service-go-deployment.yaml` - Movement Service
- `social-service-go-deployment.yaml` - Social Service
- `achievement-service-go-deployment.yaml` - Achievement Service
- `economy-service-go-deployment.yaml` - Economy Service
- `support-service-go-deployment.yaml` - Support Service
- `reset-service-go-deployment.yaml` - Reset Service
- `gameplay-service-go-deployment.yaml` - Gameplay Service
- `realtime-gateway-go-deployment.yaml` - QUIC Gateway
- `ws-lobby-go-deployment.yaml` - WebSocket Lobby
- `matchmaking-go-deployment.yaml` - Matchmaking Service

## Развертывание

### Создание namespace

```bash
kubectl apply -f namespace.yaml
```

### Развертывание инфраструктуры

```bash
kubectl apply -f namespace.yaml
kubectl apply -f configmap-common.yaml
kubectl apply -f secrets-common.yaml
kubectl apply -f rbac-service-account.yaml
kubectl apply -f networkpolicy-default.yaml
kubectl apply -f servicemonitor-common.yaml
kubectl apply -f ingress.yaml
```

### Развертывание сервисов

```bash
kubectl apply -f character-service-go-deployment.yaml
kubectl apply -f inventory-service-go-deployment.yaml
kubectl apply -f movement-service-go-deployment.yaml
kubectl apply -f social-service-go-deployment.yaml
kubectl apply -f achievement-service-go-deployment.yaml
kubectl apply -f economy-service-go-deployment.yaml
kubectl apply -f support-service-go-deployment.yaml
kubectl apply -f reset-service-go-deployment.yaml
kubectl apply -f gameplay-service-go-deployment.yaml
kubectl apply -f realtime-gateway-go-deployment.yaml
kubectl apply -f ws-lobby-go-deployment.yaml
kubectl apply -f matchmaking-go-deployment.yaml
```

Или все сразу:

```bash
kubectl apply -f .
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

Настроены requests и limits для всех сервисов:
- HTTP сервисы: Memory 128Mi requests, 512Mi limits; CPU 100m requests, 500m limits
- Gateway сервисы: Memory 64Mi requests, 256Mi limits; CPU 100m requests, 500m limits

### Метрики

Все сервисы экспортируют метрики через `/metrics` endpoint на разных портах:
- character-service-go: 9092
- inventory-service-go: 9090
- movement-service-go: 9091
- social-service-go: 9094
- achievement-service-go: 9095
- economy-service-go: 9096
- support-service-go: 9097
- reset-service-go: 9098
- gameplay-service-go: 9093
- realtime-gateway-go: 9090
- ws-lobby-go: 9090
- matchmaking-go: 9090

### Безопасность

- **RBAC**: ServiceAccount с минимальными правами доступа
- **NetworkPolicy**: 
  - default-deny-all: блокирует весь трафик по умолчанию
  - allow-service-metrics: разрешает доступ к метрикам для Prometheus
  - allow-ingress-traffic: разрешает входящий трафик от Ingress
- **Secrets**: Все секреты хранятся в Kubernetes Secrets (требуют заполнения перед деплоем)

### Логирование

Логи структурированы в JSON формате и собираются через Promtail.

## Интеграция с ArgoCD

Манифесты готовы для использования с ArgoCD (см. `infrastructure/argocd/app-necpgame.yaml`).

