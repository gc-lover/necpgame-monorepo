#!/bin/bash
# Issue: #1868
# Script to import Jackie Welles NPC profile to database

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
SQL_FILE="$SCRIPT_DIR/import-jackie-welles-npc.sql"
if [ ! -f "$SQL_FILE" ]; then
    echo "❌ Error: SQL file not found: $SQL_FILE"
    exit 1
fi

echo "🚀 Starting Jackie Welles NPC import..."
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
echo "📤 Executing NPC import script..."
psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -f "$SQL_FILE"

echo ""
echo "🎉 Jackie Welles NPC import completed successfully!"
echo "📋 Check the output above for import statistics"
echo ""
echo "🔍 To verify the import, you can run:"
echo "   psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -c \"SELECT npc_id, title, version, is_active FROM narrative.npc_definitions WHERE npc_id = 'jackie-welles-street-partner';\""