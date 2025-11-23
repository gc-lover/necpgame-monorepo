#!/bin/bash
# Валидация GitHub Actions workflows

set -e

echo "🔍 Валидация GitHub Actions workflows..."

if ! command -v yamllint &> /dev/null; then
    echo "⚠️  yamllint не установлен. Установите для валидации YAML."
    echo "   pip install yamllint"
    echo "   Продолжаю без yamllint..."
fi

ERRORS=0

for workflow in .github/workflows/*.yml .github/workflows/*.yaml; do
    if [ -f "$workflow" ]; then
        if command -v yamllint &> /dev/null; then
            if ! yamllint "$workflow" > /dev/null 2>&1; then
                echo "❌ Ошибка в $workflow"
                ERRORS=$((ERRORS + 1))
            else
                echo "✅ $(basename $workflow)"
            fi
        else
            if ! python3 -c "import yaml; yaml.safe_load(open('$workflow'))" 2>/dev/null; then
                echo "❌ Ошибка синтаксиса YAML в $workflow"
                ERRORS=$((ERRORS + 1))
            else
                echo "✅ $(basename $workflow)"
            fi
        fi
    fi
done

if [ $ERRORS -eq 0 ]; then
    echo "✅ Все workflows валидны!"
    exit 0
else
    echo "❌ Найдено $ERRORS ошибок в workflows"
    exit 1
fi


