# Руководство по деплою NECPGAME

## Обзор

Проект использует Kubernetes для оркестрации микросервисов и GitHub Actions для CI/CD.

## Предварительные требования

- Kubernetes кластер (версия 1.24+)
- kubectl настроен и подключен к кластеру
- Доступ к GitHub Container Registry (ghcr.io)
- ArgoCD (опционально, для GitOps деплоя)

## Быстрый старт

### 1. Настройка секретов

Перед деплоем необходимо настроить секреты:

#### Kubernetes Secrets

```bash
# Создать namespace
kubectl create namespace necpgame

# Создать секреты для БД, Redis, JWT
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

Подробнее см. `k8s/SECRETS_SETUP.md`

#### GitHub Actions Secrets

Добавьте в Settings → Secrets and variables → Actions:

- `KUBERNETES_CONFIG_STAGING` - base64 encoded kubeconfig для staging
- `KUBERNETES_CONFIG_PRODUCTION` - base64 encoded kubeconfig для production
- `ARGOCD_SERVER` - URL ArgoCD сервера (опционально)
- `ARGOCD_USERNAME` - Имя пользователя ArgoCD (опционально)
- `ARGOCD_PASSWORD` - Пароль ArgoCD (опционально)

### 2. Деплой через kubectl

```bash
# Базовые ресурсы
kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/configmap-common.yaml
kubectl apply -f k8s/rbac-service-account.yaml
kubectl apply -f k8s/networkpolicy-default.yaml
kubectl apply -f k8s/resource-quota.yaml

# Сервисы
kubectl apply -f k8s/*-deployment.yaml

# Автомасштабирование и высокая доступность
kubectl apply -f k8s/hpa-services.yaml
kubectl apply -f k8s/pdb-services.yaml

# Ingress и мониторинг
kubectl apply -f k8s/ingress.yaml
kubectl apply -f k8s/servicemonitor-common.yaml
```

### 3. Деплой через GitHub Actions

#### Автоматический деплой

- **Staging:** Push в ветку `develop` автоматически деплоит в staging
- **Production:** Push в ветку `main` автоматически деплоит в production

#### Ручной деплой

1. Перейдите в Actions → CD Deploy
2. Нажмите "Run workflow"
3. Выберите environment (staging/production)
4. Опционально укажите конкретный сервис

### 4. Деплой через ArgoCD

```bash
# Применить ArgoCD Application
kubectl apply -f infrastructure/argocd/app-necpgame.yaml

# Синхронизация через GitHub Actions
# Автоматически при push в main/develop или вручную через workflow
```

## Проверка деплоя

```bash
# Статус подов
kubectl get pods -n necpgame

# Статус сервисов
kubectl get services -n necpgame

# Статус автомасштабирования
kubectl get hpa -n necpgame

# Логи сервиса
kubectl logs -f deployment/character-service-go -n necpgame

# Описание пода
kubectl describe pod <pod-name> -n necpgame
```

## Мониторинг

### Prometheus

```bash
# Порт-forward для доступа к Prometheus
kubectl port-forward -n monitoring svc/prometheus 9090:9090
# Откройте http://localhost:9090
```

### Grafana

```bash
# Порт-forward для доступа к Grafana
kubectl port-forward -n monitoring svc/grafana 3000:3000
# Откройте http://localhost:3000
# Логин: admin, Пароль: (см. секрет grafana-secrets)
```

## Откат деплоя

```bash
# Откат конкретного сервиса
kubectl rollout undo deployment/character-service-go -n necpgame

# Откат всех сервисов
kubectl rollout undo deployment -n necpgame --all
```

## Масштабирование

### Ручное масштабирование

```bash
# Увеличить количество реплик
kubectl scale deployment/character-service-go --replicas=3 -n necpgame
```

### Автоматическое масштабирование

HPA настроен для следующих сервисов:
- `character-service-go`: 1-5 реплик
- `realtime-gateway-go`: 2-10 реплик
- `ws-lobby-go`: 2-10 реплик

Масштабирование происходит автоматически на основе CPU и Memory.

## Обновление сервиса

```bash
# Обновить образ сервиса
kubectl set image deployment/character-service-go \
  character-service-go=ghcr.io/gc-lover/necpgame-character-service-go:latest \
  -n necpgame

# Или через GitHub Actions CD pipeline
```

## Troubleshooting

### Поды не запускаются

```bash
# Проверить события
kubectl get events -n necpgame --sort-by='.lastTimestamp'

# Проверить логи
kubectl logs <pod-name> -n necpgame

# Проверить описание
kubectl describe pod <pod-name> -n necpgame
```

### Проблемы с подключением к БД

```bash
# Проверить секреты
kubectl get secret database-secrets -n necpgame -o yaml

# Проверить подключение из пода
kubectl exec -it <pod-name> -n necpgame -- env | grep DATABASE
```

### Проблемы с метриками

```bash
# Проверить ServiceMonitor
kubectl get servicemonitor -n necpgame

# Проверить метки на сервисах
kubectl get svc -n necpgame --show-labels | grep prometheus
```

## Production Best Practices

1. **Secrets:** Используйте внешние системы управления секретами (HashiCorp Vault, AWS Secrets Manager)
2. **Мониторинг:** Настройте алерты в Prometheus/Grafana
3. **Backup:** Настройте регулярные бэкапы БД
4. **Security:** Регулярно обновляйте образы и проверяйте уязвимости
5. **Resource Limits:** Настройте правильные лимиты ресурсов для каждого сервиса
6. **Network Policies:** Используйте NetworkPolicy для изоляции трафика
7. **RBAC:** Минимизируйте права доступа сервисов

## Дополнительная документация

- `k8s/README.md` - Детальная документация по K8s манифестам и структуре
- `k8s/SECRETS_SETUP.md` - Настройка секретов
- `.github/workflows/README.md` - Документация по GitHub Actions workflows

