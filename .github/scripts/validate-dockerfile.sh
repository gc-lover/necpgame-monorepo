#!/bin/bash
set -e

echo "🔍 Проверка Dockerfile стандартов..."

ISSUES_FOUND=0
FAILED_SERVICES=()

for dockerfile in $(find services -maxdepth 2 -name "Dockerfile" 2>/dev/null | sort); do
    if [ ! -f "$dockerfile" ]; then
        continue
    fi
    
    service=$(echo "$dockerfile" | cut -d'/' -f2)
    content=$(cat "$dockerfile")
    service_issues=()
    
    if ! echo "$content" | grep -q "FROM golang:1.24-alpine"; then
        service_issues+=("Не используется Go 1.24-alpine")
    fi
    
    if ! echo "$content" | grep -q "AS builder" || ! echo "$content" | grep -q "FROM alpine:latest"; then
        service_issues+=("Отсутствует multi-stage build")
    fi
    
    if ! echo "$content" | grep -q "USER "; then
        service_issues+=("Отсутствует security context (non-root user)")
    fi
    
    if ! echo "$content" | grep -q "HEALTHCHECK"; then
        service_issues+=("Отсутствует health check")
    fi
    
    if ! echo "$content" | grep -q "-extldflags"; then
        service_issues+=("Отсутствует статическая линковка")
    fi
    
    if ! echo "$content" | grep -q "tzdata"; then
        service_issues+=("Отсутствует timezone data")
    fi
    
    if [ ${#service_issues[@]} -eq 0 ]; then
        echo "✅ $service"
    else
        ISSUES_FOUND=1
        FAILED_SERVICES+=("$service")
        echo "❌ $service:"
        for issue in "${service_issues[@]}"; do
            echo "   - $issue"
            echo "::error file=$dockerfile::$service: $issue"
        done
    fi
done

echo ""

if [ $ISSUES_FOUND -eq 0 ]; then
    echo "✅ Все Dockerfile соответствуют стандартам!"
    exit 0
else
    echo "::error::Найдены Dockerfile, не соответствующие стандартам проекта"
    echo ""
    echo "Сервисы с проблемами: ${FAILED_SERVICES[*]}"
    echo ""
    echo "Пожалуйста, обновите Dockerfile согласно стандартам:"
    echo "- Использовать Go 1.24-alpine"
    echo "- Multi-stage build с alpine:latest"
    echo "- Security context (non-root user)"
    echo "- Health check"
    echo "- Статическая линковка (-extldflags)"
    echo "- Timezone data (tzdata)"
    exit 1
fi
