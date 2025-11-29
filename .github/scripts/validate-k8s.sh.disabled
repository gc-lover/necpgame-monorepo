#!/bin/bash
set -e

echo "🔍 Проверка Kubernetes deployments стандартов..."

ISSUES_FOUND=0
FAILED_SERVICES=()

for deployment in $(find k8s -name "*-deployment.yaml" 2>/dev/null | grep -v -E "(prometheus|grafana|loki|tempo|alertmanager)" | sort); do
    if [ ! -f "$deployment" ]; then
        continue
    fi
    
    service=$(basename "$deployment" | sed 's/-deployment.yaml//')
    content=$(cat "$deployment")
    service_issues=()
    
    if ! echo "$content" | grep -q "runAsNonRoot:"; then
        service_issues+=("Отсутствует security context (pod)")
    fi
    
    if ! echo "$content" | grep -q "allowPrivilegeEscalation: false"; then
        service_issues+=("Отсутствует security context (container)")
    fi
    
    if ! echo "$content" | grep -q "readOnlyRootFilesystem: true"; then
        service_issues+=("Отсутствует readOnlyRootFilesystem")
    fi
    
    if ! echo "$content" | grep -q "startupProbe:"; then
        service_issues+=("Отсутствует startup probe")
    fi
    
    if ! echo "$content" | grep -q "livenessProbe:"; then
        service_issues+=("Отсутствует liveness probe")
    fi
    
    if ! echo "$content" | grep -q "readinessProbe:"; then
        service_issues+=("Отсутствует readiness probe")
    fi
    
    if ! echo "$content" | grep -q "prometheus.io/scrape"; then
        service_issues+=("Отсутствуют Prometheus annotations")
    fi
    
    if ! echo "$content" | grep -q "emptyDir:" || ! echo "$content" | grep -q "/tmp"; then
        service_issues+=("Отсутствует tmp volume")
    fi
    
    if [ ${#service_issues[@]} -eq 0 ]; then
        echo "OK $service"
    else
        ISSUES_FOUND=1
        FAILED_SERVICES+=("$service")
        echo "❌ $service:"
        for issue in "${service_issues[@]}"; do
            echo "   - $issue"
            echo "::error file=$deployment::$service: $issue"
        done
    fi
done

echo ""

if [ $ISSUES_FOUND -eq 0 ]; then
    echo "OK Все deployments соответствуют стандартам!"
    exit 0
else
    echo "::error::Найдены Kubernetes deployments, не соответствующие стандартам проекта"
    echo ""
    echo "Сервисы с проблемами: ${FAILED_SERVICES[*]}"
    echo ""
    echo "Пожалуйста, обновите deployments согласно стандартам:"
    echo "- Security context (pod и container)"
    echo "- readOnlyRootFilesystem: true"
    echo "- Startup, liveness, readiness probes"
    echo "- Prometheus annotations"
    echo "- tmp volume для readOnlyRootFilesystem"
    exit 1
fi
