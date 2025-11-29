#!/bin/bash
set -e

echo "🔍 Проверка Dockerfile стандартов..."

ERRORS=0
TOTAL_ISSUES=0

find services -maxdepth 2 -name "Dockerfile" 2>/dev/null | sort | while read -r dockerfile; do
    service=$(echo "$dockerfile" | cut -d'/' -f2)
    
    if [ ! -f "$dockerfile" ]; then
        continue
    fi
    
    content=$(cat "$dockerfile")
    has_issues=false
    
    if ! echo "$content" | grep -q "FROM golang:1.24-alpine"; then
        echo "❌ $service: Не используется Go 1.24-alpine"
        has_issues=true
        TOTAL_ISSUES=$((TOTAL_ISSUES + 1))
    fi
    
    if ! echo "$content" | grep -q "AS builder" || ! echo "$content" | grep -q "FROM alpine:latest"; then
        echo "❌ $service: Отсутствует multi-stage build"
        has_issues=true
        TOTAL_ISSUES=$((TOTAL_ISSUES + 1))
    fi
    
    if ! echo "$content" | grep -q "USER "; then
        echo "❌ $service: Отсутствует security context (non-root user)"
        has_issues=true
        TOTAL_ISSUES=$((TOTAL_ISSUES + 1))
    fi
    
    if ! echo "$content" | grep -q "HEALTHCHECK"; then
        echo "❌ $service: Отсутствует health check"
        has_issues=true
        TOTAL_ISSUES=$((TOTAL_ISSUES + 1))
    fi
    
    if ! echo "$content" | grep -q "-extldflags"; then
        echo "❌ $service: Отсутствует статическая линковка"
        has_issues=true
        TOTAL_ISSUES=$((TOTAL_ISSUES + 1))
    fi
    
    if ! echo "$content" | grep -q "tzdata"; then
        echo "❌ $service: Отсутствует timezone data"
        has_issues=true
        TOTAL_ISSUES=$((TOTAL_ISSUES + 1))
    fi
    
    if [ "$has_issues" = false ]; then
        echo "OK $service"
    fi
done

if [ $TOTAL_ISSUES -eq 0 ]; then
    echo ""
    echo "OK Все Dockerfile соответствуют стандартам!"
    exit 0
else
    echo ""
    echo "❌ Найдено $TOTAL_ISSUES проблем"
    exit 1
fi
