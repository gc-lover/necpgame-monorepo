#!/bin/bash
# Apply all Liquibase migrations to PostgreSQL container
# Issue: #50

set -e

CONTAINER_NAME="${CONTAINER_NAME:-necpgame-postgres}"
DATABASE="${DATABASE:-necpgame}"
USER="${USER:-postgres}"
PASSWORD="${PASSWORD:-postgres}"
USE_LIQUIBASE="${USE_LIQUIBASE:-false}"

echo "[ROCKET] Applying all Liquibase migrations..."
echo ""

# Check if container exists
if ! docker ps -a --format "{{.Names}}" | grep -q "^${CONTAINER_NAME}$"; then
    echo "[ERROR] Container '${CONTAINER_NAME}' not found!"
    echo "[IDEA] Start PostgreSQL container first:"
    echo "   cd infrastructure/docker/postgres"
    echo "   docker compose up -d"
    exit 1
fi

# Check if container is running
if ! docker ps --format "{{.Names}}" | grep -q "^${CONTAINER_NAME}$"; then
    echo "[WARNING]  Container '${CONTAINER_NAME}' is not running. Starting..."
    docker start "${CONTAINER_NAME}"
    sleep 3
fi

# Wait for PostgreSQL to be ready
echo "⏳ Waiting for PostgreSQL to be ready..."
for i in {1..30}; do
    if docker exec "${CONTAINER_NAME}" pg_isready -U "${USER}" > /dev/null 2>&1; then
        echo "[OK] PostgreSQL is ready"
        break
    fi
    sleep 1
done

if [ $i -eq 30 ]; then
    echo "[ERROR] PostgreSQL failed to start"
    exit 1
fi

if [ "$USE_LIQUIBASE" = "true" ]; then
    echo ""
    echo "[SYMBOL] Using Liquibase container..."
    
    COMPOSE_FILE="infrastructure/docker/postgres/docker-compose.migrations.yml"
    if [ ! -f "$COMPOSE_FILE" ]; then
        echo "[ERROR] docker-compose.migrations.yml not found"
        exit 1
    fi
    
    docker compose -f "$COMPOSE_FILE" run --rm liquibase update
else
    echo ""
    echo "[NOTE] Applying migrations via Liquibase CLI..."
    
    CHANGELOG_FILE="infrastructure/liquibase/changelog.yaml"
    if [ ! -f "$CHANGELOG_FILE" ]; then
        echo "[ERROR] Changelog file not found: $CHANGELOG_FILE"
        exit 1
    fi
    
    if ! command -v liquibase &> /dev/null; then
        echo "[WARNING]  Liquibase CLI not found. Install it or set USE_LIQUIBASE=true"
        echo "[IDEA] Install: https://docs.liquibase.com/tools/home.html"
        echo ""
        echo "Alternative: Use Docker Compose with Liquibase:"
        echo "   docker compose -f infrastructure/docker/postgres/docker-compose.migrations.yml up liquibase"
        exit 1
    fi
    
    export LIQUIBASE_COMMAND_URL="jdbc:postgresql://localhost:5432/${DATABASE}"
    export LIQUIBASE_COMMAND_USERNAME="${USER}"
    export LIQUIBASE_COMMAND_PASSWORD="${PASSWORD}"
    export LIQUIBASE_COMMAND_CHANGELOG_FILE="${CHANGELOG_FILE}"
    
    liquibase update
    
    if [ $? -eq 0 ]; then
        echo ""
        echo "[OK] All migrations applied successfully!"
    else
        echo ""
        echo "[ERROR] Migration failed!"
        exit 1
    fi
fi

echo ""
echo "[SYMBOL] Checking migration status..."
docker exec "${CONTAINER_NAME}" psql -U "${USER}" -d "${DATABASE}" -c "
SELECT 
    COUNT(*) as total_changesets,
    MAX(EXECUTEDAT) as last_migration
FROM databasechangelog;
"

echo ""
echo "[OK] Done!"

