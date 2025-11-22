# GitHub Actions Workflows

## CI Workflows

### `ci-backend.yml`
- **Триггер:** Изменения в `services/` или `proto/`
- **Действия:** Тесты и сборка всех Go сервисов
- **Matrix:** 16 сервисов (character, inventory, movement, social, achievement, economy, support, reset, gameplay, admin, clan-war, companion, voice-chat, realtime-gateway, ws-lobby, matchmaking)

### `ci-client.yml`
- **Триггер:** Изменения в `client/`
- **Действия:** Валидация UE5 проекта

## CD Workflows

### `cd-deploy.yml`
- **Триггер:** Push в `main` или `develop`, или ручной запуск
- **Действия:**
  - Сборка Docker образов для всех сервисов
  - Push в GitHub Container Registry
  - Деплой в Kubernetes (staging/production)
- **Требуемые секреты:**
  - `KUBERNETES_CONFIG_STAGING` - kubeconfig для staging
  - `KUBERNETES_CONFIG_PRODUCTION` - kubeconfig для production

### `cd-argocd-sync.yml`
- **Триггер:** Push в `main` или `develop`, или ручной запуск
- **Действия:** Синхронизация ArgoCD приложения
- **Требуемые секреты:**
  - `ARGOCD_SERVER` - URL ArgoCD сервера
  - `ARGOCD_USERNAME` - Имя пользователя ArgoCD
  - `ARGOCD_PASSWORD` - Пароль ArgoCD

## Automation Workflows

### `concept-director-automation.yml`
- **Триггер:** По расписанию (каждые 6 часов) или вручную
- **Действия:** Автоматизация Concept Director с блокировками и телеметрией

### `concept-director-lock-cleanup.yml`
- **Триггер:** По расписанию (каждый час) или вручную
- **Действия:** Очистка устаревших блокировок

## Utility Workflows

### `label-sync.yml`
- **Триггер:** Ручной запуск
- **Действия:** Синхронизация labels из `.github/labels.json`

### `project-status-automation.yml`
- **Триггер:** При создании/обновлении Issues
- **Действия:** Автоматическое управление статусами в GitHub Project

### `auto-assign.yml`
- **Триггер:** При создании Issues
- **Действия:** Автоматическое назначение агентов на Issues

### `auto-close-issues.yml`
- **Триггер:** По расписанию (ежедневно)
- **Действия:** Автоматическое закрытие неактивных Issues

### `dependency-review.yml`
- **Триггер:** При создании Pull Requests
- **Действия:** Проверка зависимостей на уязвимости

## Настройка секретов

Для работы CD workflows необходимо добавить секреты в Settings → Secrets and variables → Actions:

- `KUBERNETES_CONFIG_STAGING` - base64 encoded kubeconfig для staging
- `KUBERNETES_CONFIG_PRODUCTION` - base64 encoded kubeconfig для production
- `ARGOCD_SERVER` - URL ArgoCD сервера (например: argocd.example.com)
- `ARGOCD_USERNAME` - Имя пользователя ArgoCD
- `ARGOCD_PASSWORD` - Пароль ArgoCD

## Использование

### Ручной деплой

```bash
# Через GitHub Actions UI:
# Actions → CD Deploy → Run workflow

# Или через GitHub CLI:
gh workflow run cd-deploy.yml -f environment=staging
```

### Проверка статуса деплоя

```bash
kubectl get pods -n necpgame
kubectl get services -n necpgame
kubectl get hpa -n necpgame
```

