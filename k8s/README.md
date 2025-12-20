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
- `hpa-services.yaml` - HorizontalPodAutoscaler для автомасштабирования
- `pdb-services.yaml` - PodDisruptionBudget для высокой доступности
- `resource-quota.yaml` - ResourceQuota и LimitRange для управления ресурсами

### Observability (namespace: monitoring)

- `monitoring-namespace.yaml` - Namespace для observability стека
- `prometheus-deployment.yaml` - Prometheus для метрик
- `loki-deployment.yaml` - Loki для логов
- `grafana-deployment.yaml` - Grafana для визуализации
- `tempo-deployment.yaml` - Tempo для трейсинга
- `promtail-daemonset.yaml` - Promtail для сбора логов с нод

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
- `admin-service-go-deployment.yaml` - Admin Service
- `clan-war-service-go-deployment.yaml` - Clan War Service
- `companion-service-go-deployment.yaml` - Companion Service
- `voice-chat-service-go-deployment.yaml` - Voice Chat Service
- `realtime-gateway-go-deployment.yaml` - QUIC Gateway
- `ws-lobby-go-deployment.yaml` - WebSocket Lobby
- `matchmaking-go-deployment.yaml` - Matchmaking Service

## Развертывание

### Создание namespace

```bash
kubectl apply -f namespace.yaml
```

### Настройка Secrets

**WARNING ВАЖНО:** Перед деплоем необходимо заполнить реальные значения в Secrets!

См. `k8s/SECRETS_SETUP.md` для подробных инструкций.

Рекомендуемый способ (безопасный):

```bash
kubectl create secret generic database-secrets \
  --from-literal=url="postgresql://necpgame:ПАРОЛЬ@postgres:5432/necpgame?sslmode=disable" \
  --from-literal=password="ПАРОЛЬ" \
  --namespace=necpgame

kubectl create secret generic redis-secrets \
  --from-literal=url="redis://:ПАРОЛЬ@redis:6379" \
  --from-literal=password="ПАРОЛЬ" \
  --namespace=necpgame

kubectl create secret generic jwt-secrets \
  --from-literal=secret="JWT_SECRET" \
  --from-literal=issuer="http://keycloak:8080/realms/necpgame" \
  --namespace=necpgame
```

### Развертывание observability стека (опционально)

```bash
kubectl apply -f monitoring-namespace.yaml
kubectl apply -f prometheus-deployment.yaml
kubectl apply -f loki-deployment.yaml
kubectl apply -f grafana-deployment.yaml
kubectl apply -f tempo-deployment.yaml
kubectl apply -f promtail-daemonset.yaml
```

**Примечание:** Для Grafana нужно создать Secret с паролем:

```bash
kubectl create secret generic grafana-secrets \
  --from-literal=admin-password="ВАШ_ПАРОЛЬ" \
  --namespace=monitoring
```

### Развертывание инфраструктуры

```bash
kubectl apply -f namespace.yaml
kubectl apply -f configmap-common.yaml
# Secrets уже созданы выше через kubectl create secret
kubectl apply -f rbac-service-account.yaml
kubectl apply -f networkpolicy-default.yaml
kubectl apply -f resource-quota.yaml
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
       kubectl apply -f admin-service-go-deployment.yaml
       kubectl apply -f clan-war-service-go-deployment.yaml
       kubectl apply -f companion-service-go-deployment.yaml
       kubectl apply -f voice-chat-service-go-deployment.yaml
       kubectl apply -f housing-service-go-deployment.yaml
kubectl apply -f realtime-gateway-go-deployment.yaml
kubectl apply -f ws-lobby-go-deployment.yaml
kubectl apply -f matchmaking-go-deployment.yaml
```

### Развертывание автомасштабирования и высокой доступности

```bash
kubectl apply -f hpa-services.yaml
kubectl apply -f pdb-services.yaml
```

Или все сразу (кроме secrets):

```bash
kubectl apply -f namespace.yaml
kubectl apply -f configmap-common.yaml
kubectl apply -f rbac-service-account.yaml
kubectl apply -f networkpolicy-default.yaml
kubectl apply -f resource-quota.yaml
kubectl apply -f servicemonitor-common.yaml
kubectl apply -f ingress.yaml
kubectl apply -f *-deployment.yaml
kubectl apply -f hpa-services.yaml
kubectl apply -f pdb-services.yaml
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
- admin-service-go: 9100
- clan-war-service-go: 9092
- companion-service-go: 9099
- voice-chat-service-go: 9101
- realtime-gateway-go: 9090
- ws-lobby-go: 9090
- matchmaking-go: 9090

### Автомасштабирование

- **HPA (HorizontalPodAutoscaler)**: Автоматическое масштабирование на основе CPU/Memory
    - character-service-go: 1-5 реплик
    - realtime-gateway-go: 2-10 реплик
    - ws-lobby-go: 2-10 реплик

### Высокая доступность

- **PDB (PodDisruptionBudget)**: Гарантирует минимальное количество доступных подов при обновлениях
    - character-service-go: minAvailable 1
    - realtime-gateway-go: minAvailable 1
    - ws-lobby-go: minAvailable 1

### Управление ресурсами

- **ResourceQuota**: Ограничивает общее использование ресурсов в namespace
    - CPU: 10 requests, 20 limits
    - Memory: 20Gi requests, 40Gi limits
- **LimitRange**: Устанавливает дефолтные лимиты для контейнеров
    - Default: 500m CPU, 512Mi Memory
    - DefaultRequest: 100m CPU, 128Mi Memory
    - Max: 2 CPU, 4Gi Memory
    - Min: 50m CPU, 64Mi Memory

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

## Дополнительная документация

- `DEPLOYMENT.md` - Полное руководство по деплою (включая GitHub Actions и ArgoCD)
- `SECRETS_SETUP.md` - Настройка секретов

