#!/bin/bash

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"

echo "🔧 Installing Git hooks..."

if [ ! -d "$PROJECT_ROOT/.githooks" ]; then
  echo "❌ .githooks directory not found"
  exit 1
fi

git config core.hooksPath .githooks

chmod +x "$PROJECT_ROOT/.githooks/"*

echo "OK Git hooks installed successfully!"
echo ""
echo "Hooks installed:"
for hook in "$PROJECT_ROOT/.githooks/"*; do
  if [ -f "$hook" ]; then
    echo "  • $(basename "$hook")"
  fi
done
echo ""
echo "To uninstall: git config --unset core.hooksPath"

