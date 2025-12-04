#!/bin/bash
# Issue: #1606
# Автоматическое исправление struct alignment через fieldalignment

set -e

echo "🔧 Fixing struct alignment..."

# Игнорируем generated файлы
find services -name "*.go" \
  -not -path "*/pkg/api/*" \
  -not -name "*_gen.go" \
  -not -name "*_test.go" \
  -exec fieldalignment -fix {} \;

echo "OK Struct alignment fixed"
