#!/bin/bash
# Валидация Kubernetes манифестов локально

set -e

echo "🔍 Валидация Kubernetes манифестов..."

if ! command -v kubectl &> /dev/null; then
    echo "⚠️  kubectl не установлен. Установите kubectl для валидации."
    echo "   Используйте: kubectl --dry-run=client для валидации без кластера"
    exit 1
fi

ERRORS=0

echo "Проверка базовых ресурсов..."
for file in k8s/namespace.yaml k8s/configmap-common.yaml k8s/secrets-common.yaml; do
    if [ -f "$file" ]; then
        kubectl apply --dry-run=client -f "$file" > /dev/null 2>&1 || {
            echo "❌ Ошибка в $file"
            ERRORS=$((ERRORS + 1))
        }
    fi
done

echo "Проверка Deployment манифестов..."
for file in k8s/*-deployment.yaml; do
    if [ -f "$file" ]; then
        kubectl apply --dry-run=client -f "$file" > /dev/null 2>&1 || {
            echo "❌ Ошибка в $file"
            ERRORS=$((ERRORS + 1))
        }
    fi
done

echo "Проверка Observability манифестов..."
for file in k8s/prometheus-deployment.yaml k8s/loki-deployment.yaml k8s/grafana-deployment.yaml k8s/tempo-deployment.yaml; do
    if [ -f "$file" ]; then
        kubectl apply --dry-run=client -f "$file" > /dev/null 2>&1 || {
            echo "❌ Ошибка в $file"
            ERRORS=$((ERRORS + 1))
        }
    fi
done

if [ $ERRORS -eq 0 ]; then
    echo "✅ Все Kubernetes манифесты валидны!"
    exit 0
else
    echo "❌ Найдено $ERRORS ошибок в манифестах"
    exit 1
fi


