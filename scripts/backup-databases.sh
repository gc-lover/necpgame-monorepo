#!/bin/bash

# NECP Game Database Backup Script
# Создание резервных копий PostgreSQL и Redis

set -e

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

# Configuration
BACKUP_DIR=${BACKUP_DIR:-"./backups"}
TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
KEEP_BACKUPS=${KEEP_BACKUPS:-7}

# Database credentials (from environment or defaults)
POSTGRES_HOST=${POSTGRES_HOST:-"localhost"}
POSTGRES_PORT=${POSTGRES_PORT:-"5432"}
POSTGRES_USER=${POSTGRES_USER:-"necpg"}
POSTGRES_PASSWORD=${POSTGRES_PASSWORD:-"necpg"}
POSTGRES_DB=${POSTGRES_DB:-"necpg"}

REDIS_HOST=${REDIS_HOST:-"localhost"}
REDIS_PORT=${REDIS_PORT:-"6379"}

echo "💾 NECP Game Database Backup"
echo "============================"
echo "Timestamp: $TIMESTAMP"
echo "Backup directory: $BACKUP_DIR"
echo ""

# Create backup directory
mkdir -p "$BACKUP_DIR"
cd "$BACKUP_DIR"

# Function to backup PostgreSQL
backup_postgres() {
    echo -e "${BLUE}📊 Backing up PostgreSQL...${NC}"

    local backup_file="postgres_backup_$TIMESTAMP.sql.gz"

    # Use docker exec to backup from container
    if docker ps | grep -q necpgame-postgres; then
        echo "Using Docker container backup..."
        docker exec necpgame-postgres-1 pg_dumpall -U "$POSTGRES_USER" | gzip > "$backup_file"
    else
        echo "Using direct database connection..."
        export PGPASSWORD="$POSTGRES_PASSWORD"
        pg_dumpall -h "$POSTGRES_HOST" -p "$POSTGRES_PORT" -U "$POSTGRES_USER" | gzip > "$backup_file"
    fi

    local file_size=$(du -h "$backup_file" | cut -f1)
    echo -e "${GREEN}OK PostgreSQL backup created: $backup_file (${file_size})${NC}"
    echo "$backup_file"
}

# Function to backup Redis
backup_redis() {
    echo -e "${BLUE}🔴 Backing up Redis...${NC}"

    local backup_file="redis_backup_$TIMESTAMP.rdb.gz"

    # Use docker exec to backup from container
    if docker ps | grep -q necpgame-redis; then
        echo "Using Docker container backup..."
        docker exec necpgame-redis-1 redis-cli SAVE
        docker cp necpgame-redis-1:/data/dump.rdb - > "${backup_file%.gz}"
        gzip "${backup_file%.gz}"
    else
        echo "Using direct Redis connection..."
        redis-cli -h "$REDIS_HOST" -p "$REDIS_PORT" SAVE
        cp /var/lib/redis/dump.rdb "${backup_file%.gz}"
        gzip "${backup_file%.gz}"
    fi

    local file_size=$(du -h "$backup_file" | cut -f1)
    echo -e "${GREEN}OK Redis backup created: $backup_file (${file_size})${NC}"
    echo "$backup_file"
}

# Function to cleanup old backups
cleanup_old_backups() {
    echo -e "${BLUE}🧹 Cleaning up old backups...${NC}"

    local postgres_count=$(ls postgres_backup_*.sql.gz 2>/dev/null | wc -l)
    local redis_count=$(ls redis_backup_*.rdb.gz 2>/dev/null | wc -l)

    echo "Found $postgres_count PostgreSQL backups, $redis_count Redis backups"

    # Keep only last N backups
    ls -t postgres_backup_*.sql.gz 2>/dev/null | tail -n +$((KEEP_BACKUPS + 1)) | xargs -r rm -f
    ls -t redis_backup_*.rdb.gz 2>/dev/null | tail -n +$((KEEP_BACKUPS + 1)) | xargs -r rm -f

    local deleted_postgres=$((postgres_count - $(ls postgres_backup_*.sql.gz 2>/dev/null | wc -l)))
    local deleted_redis=$((redis_count - $(ls redis_backup_*.rdb.gz 2>/dev/null | wc -l)))

    if [ $deleted_postgres -gt 0 ] || [ $deleted_redis -gt 0 ]; then
        echo -e "${YELLOW}🗑️  Cleaned up $deleted_postgres PostgreSQL and $deleted_redis Redis old backups${NC}"
    else
        echo "No old backups to clean up"
    fi
}

# Function to show backup info
show_backup_info() {
    echo ""
    echo "📁 Backup Directory Contents:"
    echo "----------------------------"

    ls -lh *.gz 2>/dev/null || echo "No backup files found"

    echo ""
    echo "💾 Disk Usage:"
    echo "--------------"
    du -sh . 2>/dev/null || echo "Unable to calculate disk usage"
}

# Main backup process
echo "Starting backup process..."

# Backup PostgreSQL
postgres_backup=$(backup_postgres)

# Backup Redis
redis_backup=$(backup_redis)

# Cleanup old backups
cleanup_old_backups

# Show results
show_backup_info

echo ""
echo "🎉 Backup completed successfully!"
echo "PostgreSQL backup: $postgres_backup"
echo "Redis backup: $redis_backup"
echo ""
echo "💡 Tips:"
echo "  - Store backups in secure location"
echo "  - Test restore procedures regularly"
echo "  - Monitor backup sizes and growth"

# Create backup manifest
cat > "backup_manifest_$TIMESTAMP.txt" << EOF
NECP Game Database Backup Manifest
==================================
Timestamp: $TIMESTAMP
PostgreSQL Backup: $postgres_backup
Redis Backup: $redis_backup

System Information:
- Hostname: $(hostname)
- User: $(whoami)
- Docker: $(docker --version 2>/dev/null | head -1 || echo "Not available")

Backup Configuration:
- Keep backups: $KEEP_BACKUPS days
- Backup directory: $BACKUP_DIR
EOF

echo -e "${GREEN}📋 Backup manifest created: backup_manifest_$TIMESTAMP.txt${NC}"
