# Скрипты для локальной проверки инфраструктуры

Набор скриптов для валидации и проверки инфраструктуры NECPGAME локально.

## Доступные скрипты

### `check-local-infrastructure.sh`

Комплексная проверка всей локальной инфраструктуры:

- Проверка Docker и Docker Compose
- Проверка Kubernetes манифестов
- Проверка Go сервисов
- Проверка Dockerfile
- Проверка GitHub Actions workflows
- Проверка Observability конфигурации

**Использование:**

```bash
chmod +x scripts/local/check-local-infrastructure.sh
./scripts/local/check-local-infrastructure.sh
```

### `validate-k8s.sh`

Валидация всех Kubernetes манифестов через `kubectl --dry-run`.

**Требования:**

- Установленный `kubectl`

**Использование:**

```bash
chmod +x scripts/local/validate-k8s.sh
./scripts/local/validate-k8s.sh
```

### `validate-dockerfiles.sh`

Проверка наличия и валидности всех Dockerfile для Go сервисов.

**Использование:**

```bash
chmod +x scripts/local/validate-dockerfiles.sh
./scripts/local/validate-dockerfiles.sh
```

### `validate-ci-workflows.sh`

Валидация синтаксиса всех GitHub Actions workflows.

**Требования (опционально):**

- `yamllint` для полной валидации YAML
- Или Python3 для базовой проверки

**Использование:**

```bash
chmod +x scripts/local/validate-ci-workflows.sh
./scripts/local/validate-ci-workflows.sh
```

### `test-docker-build.sh`

Тестовая сборка всех Docker образов для проверки корректности Dockerfile.

**Требования:**

- Запущенный Docker daemon
- Достаточно места на диске

**Использование:**

```bash
chmod +x scripts/local/test-docker-build.sh
./scripts/local/test-docker-build.sh
```

## Быстрая проверка всего

```bash
# Сделать все скрипты исполняемыми
chmod +x scripts/local/*.sh

# Запустить комплексную проверку
./scripts/local/check-local-infrastructure.sh

# Валидация манифестов (если установлен kubectl)
./scripts/local/validate-k8s.sh

# Тестовая сборка образов (если нужна)
./scripts/local/test-docker-build.sh
```

## Что проверяется

1. **Docker и Docker Compose**
    - Установка и доступность
    - Валидность docker-compose.yml

2. **Kubernetes манифесты**
    - Синтаксис YAML
    - Корректность ресурсов
    - Наличие всех необходимых файлов

3. **Dockerfile**
    - Наличие для всех 17 сервисов
    - Корректность синтаксиса
    - Возможность сборки

4. **GitHub Actions**
    - Синтаксис YAML
    - Корректность workflow файлов

5. **Go сервисы**
    - Наличие main.go файлов
    - Количество сервисов

6. **Observability**
    - Конфигурации Prometheus, Loki, Grafana
    - K8s манифесты для мониторинга

## Следующие шаги после проверки

1. **Локальная разработка:**
   ```bash
   docker-compose up -d
   ```

2. **Заполнение секретов:**
    - См. `k8s/SECRETS_SETUP.md`
    - Обновить `k8s/secrets-common.yaml`

3. **Тестирование в локальном K8s:**
    - Установить minikube или kind
    - Применить манифесты: `kubectl apply -f k8s/`

4. **Проверка CI/CD:**
    - Создать тестовый PR
    - Проверить работу GitHub Actions


