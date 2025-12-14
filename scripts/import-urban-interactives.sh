#!/bin/bash
# Issue: #1857
# Script to import urban interactive objects to database

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
DB_HOST="${DB_HOST:-localhost}"
DB_PORT="${DB_PORT:-5432}"
DB_NAME="${DB_NAME:-necp_game}"
DB_USER="${DB_USER:-postgres}"
DB_PASSWORD="${DB_PASSWORD:-password}"

# Export password for psql
export PGPASSWORD="$DB_PASSWORD"

# Check if SQL file exists
SQL_FILE="$SCRIPT_DIR/import-urban-interactives.sql"
if [ ! -f "$SQL_FILE" ]; then
    echo "❌ Error: SQL file not found: $SQL_FILE"
    exit 1
fi

echo "🚀 Starting urban interactives import..."
echo "📊 Database: $DB_HOST:$DB_PORT/$DB_NAME"
echo "📁 SQL file: $SQL_FILE"

# Check database connection
echo "🔍 Testing database connection..."
if ! psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -c "SELECT 1;" >/dev/null 2>&1; then
    echo "❌ Database connection failed"
    echo "   Please check your database configuration:"
    echo "   - DB_HOST: $DB_HOST"
    echo "   - DB_PORT: $DB_PORT"
    echo "   - DB_NAME: $DB_NAME"
    echo "   - DB_USER: $DB_USER"
    exit 1
fi

echo "OK Database connection successful"

# Run the import
echo "📤 Executing import script..."
psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -f "$SQL_FILE"

echo ""
echo "🎉 Urban interactives import completed successfully!"
echo "📋 Check the output above for import statistics"
echo ""
echo "🔍 To verify the import, you can run:"
echo "   psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -c \"SELECT object_id, name, category FROM gameplay.interactive_objects WHERE object_id IN ('street_terminal', 'ar_billboard', 'access_door', 'delivery_drone', 'garbage_chute', 'security_camera') ORDER BY object_id;\""
