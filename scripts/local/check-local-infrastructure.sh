#!/bin/bash
# Комплексная проверка локальной инфраструктуры

set -e

echo "[ROCKET] Проверка локальной инфраструктуры NECPGAME"
echo "=============================================="
echo ""

ERRORS=0

echo "1️⃣ Проверка Docker..."
if command -v docker &> /dev/null; then
    echo "[OK] Docker установлен: $(docker --version)"
    if docker ps > /dev/null 2>&1; then
        echo "[OK] Docker daemon работает"
    else
        echo "[ERROR] Docker daemon не запущен"
        ERRORS=$((ERRORS + 1))
    fi
else
    echo "[ERROR] Docker не установлен"
    ERRORS=$((ERRORS + 1))
fi
echo ""

echo "2️⃣ Проверка Docker Compose..."
if [ -f "docker-compose.yml" ]; then
    echo "[OK] docker-compose.yml найден"
    if command -v docker-compose &> /dev/null || docker compose version > /dev/null 2>&1; then
        echo "[OK] Docker Compose доступен"
        echo "   Проверка синтаксиса..."
        if docker-compose config > /dev/null 2>&1 || docker compose config > /dev/null 2>&1; then
            echo "[OK] docker-compose.yml валиден"
        else
            echo "[ERROR] Ошибка в docker-compose.yml"
            ERRORS=$((ERRORS + 1))
        fi
    else
        echo "[WARNING]  Docker Compose не найден"
    fi
else
    echo "[ERROR] docker-compose.yml не найден"
    ERRORS=$((ERRORS + 1))
fi
echo ""

echo "3️⃣ Проверка Kubernetes манифестов..."
if command -v kubectl &> /dev/null; then
    echo "[OK] kubectl установлен: $(kubectl version --client --short 2>/dev/null || echo 'установлен')"
    if [ -d "k8s" ]; then
        echo "[OK] Директория k8s/ существует"
        MANIFEST_COUNT=$(find k8s -name "*.yaml" -o -name "*.yml" | wc -l)
        echo "   Найдено манифестов: $MANIFEST_COUNT"
    else
        echo "[ERROR] Директория k8s/ не найдена"
        ERRORS=$((ERRORS + 1))
    fi
else
    echo "[WARNING]  kubectl не установлен (не критично для локальной разработки)"
fi
echo ""

echo "4️⃣ Проверка Go сервисов..."
if command -v go &> /dev/null; then
    echo "[OK] Go установлен: $(go version)"
    SERVICE_COUNT=$(find services -name "main.go" -type f | wc -l)
    echo "   Найдено сервисов: $SERVICE_COUNT"
else
    echo "[WARNING]  Go не установлен"
fi
echo ""

echo "5️⃣ Проверка Dockerfile..."
DOCKERFILE_COUNT=$(find services -name "Dockerfile" -type f | wc -l)
echo "   Найдено Dockerfile: $DOCKERFILE_COUNT"
if [ $DOCKERFILE_COUNT -lt 17 ]; then
    echo "[WARNING]  Ожидается 17 Dockerfile, найдено $DOCKERFILE_COUNT"
fi
echo ""

echo "6️⃣ Проверка GitHub Actions..."
if [ -d ".github/workflows" ]; then
    WORKFLOW_COUNT=$(find .github/workflows -name "*.yml" -o -name "*.yaml" | wc -l)
    echo "[OK] Найдено workflows: $WORKFLOW_COUNT"
else
    echo "[ERROR] Директория .github/workflows не найдена"
    ERRORS=$((ERRORS + 1))
fi
echo ""

echo "7️⃣ Проверка Observability конфигурации..."
OBSERVABILITY_FILES=(
    "infrastructure/observability/prometheus/prometheus.yml"
    "infrastructure/observability/loki/loki-config.yml"
    "infrastructure/observability/grafana/provisioning"
    "k8s/prometheus-deployment.yaml"
    "k8s/loki-deployment.yaml"
    "k8s/grafana-deployment.yaml"
)

OBS_COUNT=0
for file in "${OBSERVABILITY_FILES[@]}"; do
    if [ -f "$file" ] || [ -d "$file" ]; then
        OBS_COUNT=$((OBS_COUNT + 1))
    fi
done
echo "   Найдено конфигураций: $OBS_COUNT/${#OBSERVABILITY_FILES[@]}"
echo ""

echo "=============================================="
if [ $ERRORS -eq 0 ]; then
    echo "[OK] Локальная инфраструктура готова!"
    echo ""
    echo "Следующие шаги:"
    echo "  1. Запустить docker-compose up для локальной разработки"
    echo "  2. Заполнить секреты в k8s/secrets-common.yaml (см. k8s/SECRETS_SETUP.md)"
    echo "  3. Протестировать деплой в локальный K8s кластер (minikube/kind)"
    exit 0
else
    echo "[ERROR] Найдено $ERRORS критических проблем"
    exit 1
fi


