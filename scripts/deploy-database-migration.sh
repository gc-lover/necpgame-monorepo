#!/bin/bash
# Production Database Migration Deployment Script
# Issue: #2227 - Automated production deployment of quest_definitions table migration
# Agent: Release - Production deployment orchestration with monitoring and rollback

set -euo pipefail

# Configuration
DEPLOYMENT_ID="quest_definitions_migration_$(date +%Y%m%d_%H%M%S)"
LOG_FILE="/var/log/necp-game/database-migration-${DEPLOYMENT_ID}.log"
LOCK_FILE="/tmp/database-migration-${DEPLOYMENT_ID}.lock"
NOTIFICATION_WEBHOOK="${DATABASE_DEPLOYMENT_WEBHOOK:-}"

# Environment configuration
ENVIRONMENT="${DEPLOYMENT_ENV:-production}"
DATABASE_HOST="${DATABASE_HOST:-necpgame-production-db.internal}"
DATABASE_PORT="${DATABASE_PORT:-5432}"
DATABASE_NAME="${DATABASE_NAME:-necp_game}"
DATABASE_USER="${DATABASE_USER:-migration_user}"
DATABASE_PASSWORD="${DATABASE_PASSWORD:-}"

# Liquibase configuration
LIQUIBASE_HOME="${LIQUIBASE_HOME:-/opt/liquibase}"
CHANGELOG_FILE="${CHANGELOG_FILE:-infrastructure/liquibase/changelog.xml}"
DRIVER_PATH="${DRIVER_PATH:-/opt/liquibase/drivers/postgresql.jar}"

# Monitoring configuration
HEALTH_CHECK_INTERVAL="${HEALTH_CHECK_INTERVAL:-30}"
VALIDATION_TIMEOUT="${VALIDATION_TIMEOUT:-1800}"  # 30 minutes
ROLLBACK_TIMEOUT="${ROLLBACK_TIMEOUT:-900}"      # 15 minutes

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

# Logging functions
log_info() {
    echo -e "${BLUE}[$(date +%Y-%m-%d\ %H:%M:%S)]${NC} $1" | tee -a "$LOG_FILE"
}

log_success() {
    echo -e "${GREEN}[$(date +%Y-%m-%d\ %H:%M:%S)]${NC} $1" | tee -a "$LOG_FILE"
}

log_warning() {
    echo -e "${YELLOW}[$(date +%Y-%m-%d\ %H:%M:%S)]${NC} $1" | tee -a "$LOG_FILE"
}

log_error() {
    echo -e "${RED}[$(date +%Y-%m-%d\ %H:%M:%S)]${NC} $1" | tee -a "$LOG_FILE"
}

# Notification functions
send_notification() {
    local title="$1"
    local message="$2"
    local color="${3:-good}"

    if [[ -n "$NOTIFICATION_WEBHOOK" ]]; then
        curl -s -X POST "$NOTIFICATION_WEBHOOK" \
            -H 'Content-Type: application/json' \
            -d "{\"text\":\"${title}\",\"attachments\":[{\"text\":\"${message}\",\"color\":\"${color}\"}]}" \
            || log_warning "Failed to send notification"
    fi
}

# Pre-deployment checks
pre_deployment_checks() {
    log_info "Starting pre-deployment checks..."

    # Check for existing lock file
    if [[ -f "$LOCK_FILE" ]]; then
        log_error "Migration lock file exists: $LOCK_FILE"
        log_error "Another migration may be in progress"
        exit 1
    fi
    touch "$LOCK_FILE"

    # Validate environment
    if [[ "$ENVIRONMENT" != "staging" && "$ENVIRONMENT" != "production" ]]; then
        log_error "Invalid environment: $ENVIRONMENT"
        exit 1
    fi

    # Check database connectivity
    if ! check_database_connectivity; then
        log_error "Database connectivity check failed"
        exit 1
    fi

    # Validate Liquibase installation
    if [[ ! -x "$LIQUIBASE_HOME/liquibase" ]]; then
        log_error "Liquibase not found at: $LIQUIBASE_HOME/liquibase"
        exit 1
    fi

    # Check changelog file
    if [[ ! -f "$CHANGELOG_FILE" ]]; then
        log_error "Changelog file not found: $CHANGELOG_FILE"
        exit 1
    fi

    # Validate required scripts
    local required_scripts=(
        "scripts/validate-migration-results.py"
        "scripts/health-check-post-migration.py"
        "scripts/emergency-rollback.sh"
    )

    for script in "${required_scripts[@]}"; do
        if [[ ! -x "$script" ]]; then
            log_error "Required script not found: $script"
            exit 1
        fi
    done

    # Check available disk space (require 10GB free)
    local db_disk_free
    db_disk_free=$(df -BG /var/lib/postgresql | tail -1 | awk '{print $4}' | sed 's/G//')
    if [[ $db_disk_free -lt 10 ]]; then
        log_error "Insufficient disk space: ${db_disk_free}GB free, 10GB required"
        exit 1
    fi

    # Create pre-migration backup
    if ! create_pre_migration_backup; then
        log_error "Pre-migration backup failed"
        exit 1
    fi

    log_success "Pre-deployment checks completed"
}

check_database_connectivity() {
    log_info "Checking database connectivity..."

    local connection_string="postgresql://$DATABASE_USER:$DATABASE_PASSWORD@$DATABASE_HOST:$DATABASE_PORT/$DATABASE_NAME"

    if command -v pg_isready &> /dev/null; then
        if pg_isready -h "$DATABASE_HOST" -p "$DATABASE_PORT" -U "$DATABASE_USER" -d "$DATABASE_NAME" &> /dev/null; then
            log_success "Database connectivity verified"
            return 0
        fi
    fi

    # Fallback: try psql connection
    if command -v psql &> /dev/null; then
        if PGPASSWORD="$DATABASE_PASSWORD" psql -h "$DATABASE_HOST" -p "$DATABASE_PORT" -U "$DATABASE_USER" -d "$DATABASE_NAME" -c "SELECT 1;" &> /dev/null; then
            log_success "Database connectivity verified"
            return 0
        fi
    fi

    log_error "Database connectivity check failed"
    return 1
}

create_pre_migration_backup() {
    log_info "Creating pre-migration backup..."

    local backup_file="/var/backups/database/pre-migration-${DEPLOYMENT_ID}.sql"

    # Create backup directory if it doesn't exist
    mkdir -p "$(dirname "$backup_file")"

    if command -v pg_dump &> /dev/null; then
        if PGPASSWORD="$DATABASE_PASSWORD" pg_dump \
            -h "$DATABASE_HOST" \
            -p "$DATABASE_PORT" \
            -U "$DATABASE_USER" \
            -d "$DATABASE_NAME" \
            --format=custom \
            --compress=9 \
            --file="$backup_file"; then

            log_success "Pre-migration backup created: $backup_file"
            return 0
        fi
    fi

    log_error "Failed to create pre-migration backup"
    return 1
}

# Execute migration
execute_migration() {
    log_info "Starting database migration..."

    send_notification "🚀 Database Migration Started" \
        "Environment: $ENVIRONMENT\nDeployment ID: $DEPLOYMENT_ID\nTables: quest_definitions" \
        "warning"

    local start_time
    start_time=$(date +%s)

    # Set Liquibase properties
    export LIQUIBASE_COMMAND_URL="jdbc:postgresql://$DATABASE_HOST:$DATABASE_PORT/$DATABASE_NAME"
    export LIQUIBASE_COMMAND_USERNAME="$DATABASE_USER"
    export LIQUIBASE_COMMAND_PASSWORD="$DATABASE_PASSWORD"
    export LIQUIBASE_COMMAND_DRIVER="org.postgresql.Driver"
    export LIQUIBASE_COMMAND_CLASSPATH="$DRIVER_PATH"

    # Execute migration
    if "$LIQUIBASE_HOME/liquibase" \
        --changeLogFile="$CHANGELOG_FILE" \
        --labels="quest_definitions" \
        update \
        >> "$LOG_FILE" 2>&1; then

        local end_time
        end_time=$(date +%s)
        local duration=$((end_time - start_time))

        log_success "Migration completed successfully in ${duration}s"

        send_notification "✅ Database Migration Completed" \
            "Environment: $ENVIRONMENT\nDuration: ${duration}s\nStatus: SUCCESS" \
            "good"

        return 0
    else
        log_error "Migration failed"

        send_notification "❌ Database Migration Failed" \
            "Environment: $ENVIRONMENT\nDeployment ID: $DEPLOYMENT_ID\nCheck logs: $LOG_FILE" \
            "danger"

        return 1
    fi
}

# Post-migration validation
post_migration_validation() {
    log_info "Starting post-migration validation..."

    local validation_start
    validation_start=$(date +%s)

    # Run validation script
    if python3 scripts/validate-migration-results.py \
        --env "$ENVIRONMENT" \
        --timeout "$VALIDATION_TIMEOUT" \
        >> "$LOG_FILE" 2>&1; then

        log_success "Migration validation passed"
    else
        log_error "Migration validation failed"
        return 1
    fi

    # Health check
    if python3 scripts/health-check-post-migration.py \
        --env "$ENVIRONMENT" \
        --interval "$HEALTH_CHECK_INTERVAL" \
        --duration 300 \
        >> "$LOG_FILE" 2>&1; then

        log_success "Health checks passed"
    else
        log_error "Health checks failed"
        return 1
    fi

    local validation_end
    validation_end=$(date +%s)
    local validation_duration=$((validation_end - validation_start))

    log_success "Post-migration validation completed in ${validation_duration}s"
    return 0
}

# Emergency rollback
emergency_rollback() {
    log_error "Initiating emergency rollback..."

    send_notification "🚨 Emergency Rollback Initiated" \
        "Environment: $ENVIRONMENT\nDeployment ID: $DEPLOYMENT_ID\nReason: Migration failure" \
        "danger"

    local rollback_start
    rollback_start=$(date +%s)

    # Execute rollback script
    if ./scripts/emergency-rollback.sh \
        --deployment-id "$DEPLOYMENT_ID" \
        --reason "migration_failure" \
        --timeout "$ROLLBACK_TIMEOUT" \
        >> "$LOG_FILE" 2>&1; then

        local rollback_end
        rollback_end=$(date +%s)
        local rollback_duration=$((rollback_end - rollback_start))

        log_success "Emergency rollback completed in ${rollback_duration}s"

        send_notification "✅ Emergency Rollback Completed" \
            "Environment: $ENVIRONMENT\nDuration: ${rollback_duration}s\nStatus: SUCCESS" \
            "warning"

        return 0
    else
        log_error "Emergency rollback failed"

        send_notification "💀 Emergency Rollback Failed" \
            "Environment: $ENVIRONMENT\nDeployment ID: $DEPLOYMENT_ID\nManual intervention required!" \
            "danger"

        return 1
    fi
}

# Cleanup
cleanup() {
    log_info "Performing cleanup..."

    # Remove lock file
    rm -f "$LOCK_FILE"

    # Archive log file
    local archive_dir="/var/log/necp-game/archive"
    mkdir -p "$archive_dir"
    mv "$LOG_FILE" "$archive_dir/"

    log_success "Cleanup completed"
}

# Monitoring and alerting
start_monitoring() {
    log_info "Starting post-deployment monitoring..."

    # Start background monitoring script
    nohup ./scripts/monitor-post-deployment.sh \
        --deployment-id "$DEPLOYMENT_ID" \
        --env "$ENVIRONMENT" \
        --duration 86400 \
        >> "$LOG_FILE" 2>&1 &

    local monitor_pid=$!
    echo "$monitor_pid" > "/tmp/monitoring-${DEPLOYMENT_ID}.pid"

    log_success "Post-deployment monitoring started (PID: $monitor_pid)"
}

# Main deployment function
main() {
    log_info "Starting database migration deployment"
    log_info "Environment: $ENVIRONMENT"
    log_info "Deployment ID: $DEPLOYMENT_ID"
    log_info "Target: quest_definitions table migration"

    # Trap for cleanup on exit
    trap cleanup EXIT

    # Execute deployment phases
    if pre_deployment_checks && \
       execute_migration && \
       post_migration_validation; then

        log_success "Database migration deployment completed successfully!"

        # Start monitoring
        start_monitoring

        send_notification "🎉 Database Migration Deployment Complete" \
            "Environment: $ENVIRONMENT\nDeployment ID: $DEPLOYMENT_ID\nStatus: SUCCESS\nMonitoring: Active" \
            "good"

        exit 0
    else
        log_error "Database migration deployment failed!"

        # Attempt rollback
        if emergency_rollback; then
            log_info "Rollback completed successfully"
        else
            log_error "Rollback also failed - manual intervention required"
            exit 1
        fi

        exit 1
    fi
}

# Argument parsing
while [[ $# -gt 0 ]]; do
    case $1 in
        --env|--environment)
            ENVIRONMENT="$2"
            shift 2
            ;;
        --dry-run)
            DRY_RUN=true
            shift
            ;;
        --help)
            echo "Usage: $0 [OPTIONS]"
            echo "Options:"
            echo "  --env ENVIRONMENT    Target environment (staging|production)"
            echo "  --dry-run           Perform dry run without actual deployment"
            echo "  --help              Show this help message"
            exit 0
            ;;
        *)
            log_error "Unknown option: $1"
            exit 1
            ;;
    esac
done

# Dry run mode
if [[ "${DRY_RUN:-false}" == "true" ]]; then
    log_info "DRY RUN MODE - No actual changes will be made"
    export DRY_RUN=true
fi

# Validate required environment variables
if [[ -z "$DATABASE_PASSWORD" ]]; then
    log_error "DATABASE_PASSWORD environment variable not set"
    exit 1
fi

# Run main deployment
main "$@"
