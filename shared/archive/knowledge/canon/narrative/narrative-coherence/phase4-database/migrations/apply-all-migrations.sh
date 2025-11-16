#!/bin/bash
# Apply All Migrations Script
# Version: 1.0.0
# Date: 2025-11-07 00:27

set -e  # Exit on error

# Configuration
DB_NAME="${DB_NAME:-necpgame}"
DB_USER="${DB_USER:-postgres}"
DB_HOST="${DB_HOST:-localhost}"
DB_PORT="${DB_PORT:-5432}"

echo "========================================="
echo "NECPGAME - Quest System Migrations"
echo "========================================="
echo "Database: $DB_NAME"
echo "Host: $DB_HOST:$DB_PORT"
echo "User: $DB_USER"
echo "========================================="

# Функция для применения миграции
apply_migration() {
    local file=$1
    echo "Applying: $file"
    PGPASSWORD=$DB_PASSWORD psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -f "$file"
    if [ $? -eq 0 ]; then
        echo "✅ Success: $file"
    else
        echo "❌ Failed: $file"
        exit 1
    fi
}

# Применяем миграции в порядке
echo ""
echo "Step 1/5: Expanding quests table..."
apply_migration "001-expand-quests-table.sql"

echo ""
echo "Step 2/5: Creating quest branches..."
apply_migration "002-create-quest-branches.sql"

echo ""
echo "Step 3/5: Creating dialogue system..."
apply_migration "003-create-dialogue-system.sql"

echo ""
echo "Step 4/5: Creating player systems..."
apply_migration "004-create-player-systems.sql"

echo ""
echo "Step 5/5: Creating world state system..."
apply_migration "005-create-world-state-system.sql"

echo ""
echo "========================================="
echo "✅ ALL MIGRATIONS APPLIED SUCCESSFULLY!"
echo "========================================="
echo ""
echo "Next steps:"
echo "1. Verify tables: psql -d $DB_NAME -c '\dt quest*'"
echo "2. Check indexes: psql -d $DB_NAME -c '\di quest*'"
echo "3. Import quest data: run import scripts"
echo ""

