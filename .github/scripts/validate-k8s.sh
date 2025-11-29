#!/bin/bash
set -e

echo "🔍 Проверка Kubernetes deployments стандартов..."

ERRORS=0
TOTAL_ISSUES=0

find k8s -name "*-deployment.yaml" 2>/dev/null | grep -v -E "(prometheus|grafana|loki|tempo|alertmanager)" | sort | while read -r deployment; do
    service=$(basename "$deployment" | sed 's/-deployment.yaml//')
    
    if [ ! -f "$deployment" ]; then
        continue
    fi
    
    content=$(cat "$deployment")
    has_issues=false
    
    if ! echo "$content" | grep -q "runAsNonRoot:"; then
        echo "❌ $service: Отсутствует security context (pod)"
        has_issues=true
        TOTAL_ISSUES=$((TOTAL_ISSUES + 1))
    fi
    
    if ! echo "$content" | grep -q "allowPrivilegeEscalation: false"; then
        echo "❌ $service: Отсутствует security context (container)"
        has_issues=true
        TOTAL_ISSUES=$((TOTAL_ISSUES + 1))
    fi
    
    if ! echo "$content" | grep -q "readOnlyRootFilesystem: true"; then
        echo "❌ $service: Отсутствует readOnlyRootFilesystem"
        has_issues=true
        TOTAL_ISSUES=$((TOTAL_ISSUES + 1))
    fi
    
    if ! echo "$content" | grep -q "startupProbe:"; then
        echo "❌ $service: Отсутствует startup probe"
        has_issues=true
        TOTAL_ISSUES=$((TOTAL_ISSUES + 1))
    fi
    
    if ! echo "$content" | grep -q "livenessProbe:"; then
        echo "❌ $service: Отсутствует liveness probe"
        has_issues=true
        TOTAL_ISSUES=$((TOTAL_ISSUES + 1))
    fi
    
    if ! echo "$content" | grep -q "readinessProbe:"; then
        echo "❌ $service: Отсутствует readiness probe"
        has_issues=true
        TOTAL_ISSUES=$((TOTAL_ISSUES + 1))
    fi
    
    if ! echo "$content" | grep -q "prometheus.io/scrape"; then
        echo "❌ $service: Отсутствуют Prometheus annotations"
        has_issues=true
        TOTAL_ISSUES=$((TOTAL_ISSUES + 1))
    fi
    
    if ! echo "$content" | grep -q "emptyDir:" || ! echo "$content" | grep -q "/tmp"; then
        echo "❌ $service: Отсутствует tmp volume"
        has_issues=true
        TOTAL_ISSUES=$((TOTAL_ISSUES + 1))
    fi
    
    if [ "$has_issues" = false ]; then
        echo "✅ $service"
    fi
done

if [ $TOTAL_ISSUES -eq 0 ]; then
    echo ""
    echo "✅ Все deployments соответствуют стандартам!"
    exit 0
else
    echo ""
    echo "❌ Найдено $TOTAL_ISSUES проблем"
    exit 1
fi
