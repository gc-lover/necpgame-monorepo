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

## Project Status Automation

### `project-status-automation.yml`
- **Триггер:** При создании/обновлении Issues, комментариях, PR
- **Действия:**
  - Автоматическое добавление новых Issues в GitHub Project при создании
  - Установка начального статуса "Todo" для новых Issues
  - Проверка выполнения всех критериев приемки (чекбоксы) при комментариях
  - Автоматические переходы между статусами при выполнении всех чекбоксов
  - Уведомление о переходе через комментарий в Issue
- **Использует:** Project Status поле (Single Select) для отслеживания этапа задачи
- **Логика переходов:** Определяет следующего агента на основе текущего статуса и меток задачи

### `github-api-batch-processor.yml`
- **Триггер:** Ручной запуск или по расписанию (каждые 6 часов)
- **Действия:**
  - Массовая обработка Issues по фильтру статуса
  - Операции: обновление статуса, добавление комментариев, переход к следующему статусу, анализ и авто-обновление статусов
- **Операции:**
  - `update-status` - обновить статус на указанный
  - `add-comments` - добавить комментарии
  - `transfer-to-next-status` - передать к следующему агенту
  - `analyze-and-update-status` - проанализировать Issues и автоматически определить правильный статус
- **Параметры:**
  - `status_filter` - фильтр по статусу (используйте "All" для analyze-and-update-status)
  - `operation` - тип операции
  - `batch_size` - размер батча (по умолчанию: 5)

## Automation Workflows

### `migrate-ideas-to-issues.yml`
- **Триггер:** Ручной запуск
- **Действия:** Миграция идей из YAML файлов в GitHub Issues
- **Параметры:**
  - `idea_file` - путь к YAML файлу с идеей
  - `create_issue` - создавать ли Issue
- **Автоматически:** Issue добавляется в Project через `project-status-automation.yml`

## Utility Workflows

### `dependency-review.yml`
- **Триггер:** При создании Pull Requests
- **Действия:** Проверка зависимостей на уязвимости

### `check-file-size.yml`
- **Триггер:** Push в `main` или `develop`, или Pull Request
- **Действия:** Проверка размера файлов (максимум 500 строк, настраивается в `.github/file-size-config.sh`)

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

### Массовая обработка Issues

```bash
# Анализ и автоматическое обновление статусов для всех Issues:
gh workflow run github-api-batch-processor.yml \
  -f operation=analyze-and-update-status \
  -f status_filter="All" \
  -f batch_size=10 \
  -f delay_ms=1000

# Обновить статус для всех задач с определенным статусом:
gh workflow run github-api-batch-processor.yml \
  -f operation=update-status \
  -f status_filter="Backend - Todo" \
  -f new_status="Backend - In Progress" \
  -f batch_size=5
```

### Миграция идеи в Issue

```bash
gh workflow run migrate-ideas-to-issues.yml \
  -f idea_file="knowledge/analysis/tasks/ideas/2025-11-07-IDEA-subtle-media-collabs.yaml" \
  -f create_issue=true
```

### Проверка статуса деплоя

```bash
kubectl get pods -n necpgame
kubectl get services -n necpgame
kubectl get hpa -n necpgame
```

## Статусы в Project

Все workflows работают через поле **Status** в GitHub Project, которое определяет:
- Текущего агента (например: "Backend", "QA")
- Этап работы (например: "Todo", "In Progress", "Review")
- Готовность к переходу к следующему этапу

Формат статуса: `{Agent} - {State}` (например: "Backend - Todo", "QA - Review")
