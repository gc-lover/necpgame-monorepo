# 🚀 Production Database Deployment Guide: Quest Definitions Migration

## Executive Summary

This comprehensive guide provides production deployment procedures for the `quest_definitions` table migration to NECPGAME production databases. This is a critical infrastructure component affecting all quest-related functionality across the platform.

**Migration Type:** Schema Change with Data Migration  
**Risk Level:** HIGH (Database Schema Modification)  
**Downtime Window:** ZERO (Online Migration with Rollback)  
**Rollback Time:** <15 minutes  
**Affected Systems:** All quest services, content management, player progression  

## 📋 Pre-Deployment Checklist

### Phase 1: Environment Preparation (Week -2)

#### [ ] Database Infrastructure Validation
- [ ] Production PostgreSQL cluster (v14+) verified and accessible
- [ ] Liquibase v4.20+ installed on deployment servers
- [ ] Database connection pooling configured (max 50 connections)
- [ ] Database backup systems operational (automated daily backups)
- [ ] Point-in-time recovery (PITR) enabled with 5-minute granularity
- [ ] Read replica lag monitoring (<30 seconds acceptable)

#### [ ] Security Prerequisites
- [ ] Database credentials rotated for deployment
- [ ] TLS 1.3 encryption enabled for all database connections
- [ ] Network policies restrict database access to authorized services
- [ ] Audit logging enabled for DDL operations
- [ ] Database user permissions validated (minimal required privileges)

#### [ ] Application Readiness
- [ ] All microservices tested with new schema
- [ ] Database connection strings updated in configuration
- [ ] Application startup scripts validated
- [ ] Health check endpoints configured for new tables
- [ ] Monitoring dashboards updated for new metrics

### Phase 2: Migration Preparation (Week -1)

#### [ ] Migration Script Validation
- [ ] Liquibase changelog files syntax validated
- [ ] Rollback scripts tested in staging environment
- [ ] Pre-migration and post-migration scripts prepared
- [ ] Data integrity validation queries prepared
- [ ] Performance impact assessment completed

#### [ ] Data Preparation
- [ ] Quest content files validated against schema
- [ ] Data transformation scripts tested
- [ ] Foreign key relationships verified
- [ ] Index creation impact assessed
- [ ] Table size estimates calculated

#### [ ] Monitoring Setup
- [ ] Database performance monitoring configured
- [ ] Application metrics collection enabled
- [ ] Alert thresholds established
- [ ] Log aggregation configured
- [ ] Incident response procedures documented

## 🎯 Deployment Strategy

### Online Migration Approach

#### Architecture Overview
```
Production Environment
├── Primary Database (Read-Write)
│   ├── Online Migration Execution
│   ├── Continuous Health Monitoring
│   └── Real-time Rollback Capability
│
├── Read Replicas (Read-Only)
│   ├── Isolated Testing Environment
│   ├── Performance Validation
│   └── Gradual Traffic Migration
│
├── Staging Environment (Pre-Production)
    ├── Full Migration Dry-Run
    ├── Load Testing Validation
    └── Application Integration Testing
```

#### Deployment Phases

##### Phase 1: Staging Environment Validation (Day -1)
```bash
# 1. Create staging environment snapshot
pg_dump production_db > staging_snapshot.sql
createdb staging_db
psql staging_db < staging_snapshot.sql

# 2. Execute migration in staging
cd infrastructure/liquibase
liquibase --changeLogFile=changelog.xml --url=jdbc:postgresql://staging-db:5432/staging_db update

# 3. Validate migration results
./scripts/validate-migration-results.py --env staging

# 4. Run application integration tests
./scripts/integration-test-suite.py --env staging --test db-migration
```

##### Phase 2: Production Dry-Run (Day 0 Morning)
```bash
# 1. Create production read replica for testing
aws rds create-db-instance-read-replica \
  --db-instance-identifier necpgame-dry-run \
  --source-db-instance-identifier necpgame-production

# 2. Execute migration on read replica
liquibase --changeLogFile=changelog.xml --url=jdbc:postgresql://dry-run-db:5432/dry_run_db update

# 3. Performance validation
./scripts/performance-validation.py --env dry-run --duration 3600

# 4. Application compatibility testing
./scripts/application-compatibility-test.py --env dry-run
```

##### Phase 3: Production Deployment (Day 0 Evening)
```bash
# 1. Enable maintenance mode announcement
kubectl apply -f deployment/maintenance-announcement.yaml

# 2. Pause non-critical background jobs
kubectl scale deployment background-job-processor --replicas=0

# 3. Execute production migration
liquibase --changeLogFile=changelog.xml --url=jdbc:postgresql://production-db:5432/production_db update

# 4. Validate migration success
./scripts/validate-migration-results.py --env production

# 5. Resume background jobs
kubectl scale deployment background-job-processor --replicas=5

# 6. Disable maintenance mode
kubectl delete -f deployment/maintenance-announcement.yaml
```

##### Phase 4: Post-Deployment Validation (Day 1)
```bash
# 1. 24-hour production monitoring
kubectl apply -f monitoring/production-monitoring.yaml

# 2. Performance baseline comparison
./scripts/performance-baseline-comparison.py --before --after

# 3. Data integrity validation
./scripts/data-integrity-validation.py --comprehensive

# 4. Application health verification
./scripts/application-health-check.py --continuous
```

## 📊 Migration Details

### Schema Changes Overview

#### New Tables Created
```sql
-- Main quest definitions table
CREATE TABLE gameplay.quest_definitions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title VARCHAR(200) NOT NULL,
    description TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'active'
        CHECK (status IN ('active', 'completed', 'archived')),
    level_min INTEGER NOT NULL CHECK (level_min >= 1 AND level_min <= 100),
    level_max INTEGER NOT NULL CHECK (level_max >= 1 AND level_max <= 100),
    rewards JSONB,
    objectives JSONB,
    metadata JSONB,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Supporting indexes and constraints
CREATE INDEX idx_quest_definitions_status ON gameplay.quest_definitions(status);
CREATE INDEX idx_quest_definitions_level_range ON gameplay.quest_definitions(level_min, level_max);
CREATE INDEX idx_quest_definitions_created_at ON gameplay.quest_definitions(created_at DESC);
CREATE INDEX idx_quest_definitions_active ON gameplay.quest_definitions(level_min, level_max)
    WHERE status = 'active';
CREATE INDEX idx_quest_definitions_rewards_gin ON gameplay.quest_definitions USING GIN (rewards);
CREATE INDEX idx_quest_definitions_objectives_gin ON gameplay.quest_definitions USING GIN (objectives);
```

#### Performance Impact Assessment
- **Table Size Estimate:** 50MB initial, 500MB with full content
- **Index Size Estimate:** 150MB (including GIN indexes)
- **Query Performance:** <5ms for common queries, <10ms for complex JSONB searches
- **Memory Impact:** +50MB database cache requirement
- **Connection Impact:** No change in connection requirements

### Data Migration Strategy

#### Content Import Process
```sql
-- Step 1: Bulk insert quest definitions
INSERT INTO gameplay.quest_definitions (title, description, level_min, level_max, rewards, objectives, metadata)
SELECT
    content.title,
    content.description,
    content.level_min,
    content.level_max,
    content.rewards::jsonb,
    content.objectives::jsonb,
    content.metadata::jsonb
FROM temp_quest_import content;

-- Step 2: Update search indexes
REINDEX INDEX idx_quest_definitions_rewards_gin;
REINDEX INDEX idx_quest_definitions_objectives_gin;

-- Step 3: Validate data integrity
SELECT COUNT(*) as total_quests FROM gameplay.quest_definitions;
SELECT COUNT(*) as invalid_levels FROM gameplay.quest_definitions
WHERE level_min > level_max OR level_min < 1 OR level_max > 100;
```

#### Rollback Strategy
```sql
-- Emergency rollback procedure
BEGIN;

-- Drop new table (safe rollback)
DROP TABLE IF EXISTS gameplay.quest_definitions CASCADE;

-- Restore backup if needed
-- (Automated by Liquibase rollback command)

COMMIT;
```

## 📈 Monitoring & Alerting Setup

### Database Performance Metrics

#### Key Performance Indicators (KPIs)
- **Query Latency:** P95 <50ms for quest queries
- **Connection Pool Utilization:** <80% of max connections
- **Index Hit Rate:** >95% for quest-related queries
- **Table Bloat:** <10% table bloat after migration
- **Replication Lag:** <30 seconds for read replicas

#### Critical Alerts
```yaml
groups:
  - name: database.migration.critical
    rules:
      # Migration Failure
      - alert: DatabaseMigrationFailed
        expr: liquibase_migration_status{status="failed"} == 1
        for: 1m
        labels:
          severity: critical
          category: database
          impact: migration-failure
        annotations:
          summary: "Database migration failed"
          description: "Liquibase migration failed for quest_definitions table"
          runbook: "https://wiki.necpgame.com/database/migration-failure"

      # Performance Degradation
      - alert: DatabaseQueryLatencyHigh
        expr: histogram_quantile(0.95, rate(postgres_query_duration_seconds_bucket[5m])) > 0.1
        for: 5m
        labels:
          severity: high
          category: performance
        annotations:
          summary: "Database query latency high"
          description: "P95 query latency >100ms after migration"

      # Connection Pool Exhaustion
      - alert: DatabaseConnectionPoolExhausted
        expr: postgres_connection_pool_utilization > 0.9
        for: 2m
        labels:
          severity: high
          category: resources
        annotations:
          summary: "Database connection pool exhausted"
          description: "Connection pool utilization >90%"

  - name: database.migration.warning
    rules:
      # Index Build Slow
      - alert: DatabaseIndexBuildSlow
        expr: postgres_index_build_duration_seconds > 300
        for: 1m
        labels:
          severity: warning
          category: maintenance
        annotations:
          summary: "Database index build slow"
          description: "Index creation taking longer than 5 minutes"

      # Replication Lag Increase
      - alert: DatabaseReplicationLagHigh
        expr: postgres_replication_lag_seconds > 60
        for: 5m
        labels:
          severity: warning
          category: replication
        annotations:
          summary: "Database replication lag high"
          description: "Replication lag >60 seconds after migration"
```

### Application Metrics

#### Quest Service Metrics
```yaml
# Prometheus metrics for quest services
quest_service_metrics:
  - quest_query_duration_seconds
  - quest_cache_hit_ratio
  - quest_import_success_rate
  - quest_validation_errors_total

# Custom business metrics
business_metrics:
  - active_quests_total
  - quest_completion_rate
  - average_quest_duration_seconds
  - quest_abandonment_rate
```

### Log Aggregation Setup

#### Structured Logging Configuration
```yaml
# Fluent Bit configuration for database logs
[SERVICE]
    Flush         5
    Log_Level     info
    Daemon        off

[INPUT]
    Name              tail
    Path              /var/log/postgresql/postgresql.log
    Parser            postgres_log
    Tag               database.production.*

[FILTER]
    Name              grep
    Match             database.production.*
    Regex             (ERROR|FATAL|quest_definitions|migration)

[OUTPUT]
    Name              opensearch
    Match             database.production.*
    Host              logs.necpgame.internal
    Port              9200
    Index             database-production-%Y.%m.%d
    Type              database-log
```

## 🚨 Rollback Procedures

### Automated Rollback (Primary Method)
```bash
# Immediate rollback using Liquibase
liquibase --changeLogFile=changelog.xml rollback --rollbackTag production_deployment_2026_01_06

# Alternative: Manual rollback script
./scripts/emergency-rollback.sh --reason "migration_failure" --timestamp $(date +%s)
```

### Manual Rollback (Fallback Method)
```sql
-- Step 1: Stop application traffic
UPDATE system_settings SET maintenance_mode = true;

-- Step 2: Drop new table
DROP TABLE IF EXISTS gameplay.quest_definitions CASCADE;

-- Step 3: Restore from backup if needed
-- (Use pg_restore from pre-migration backup)

-- Step 4: Restart applications
UPDATE system_settings SET maintenance_mode = false;
```

### Partial Rollback Scenarios

#### Scenario 1: Data Corruption Detected
```bash
-- Isolate corrupted data
UPDATE gameplay.quest_definitions SET status = 'archived'
WHERE id IN (SELECT id FROM corrupted_quests_audit);

-- Trigger data repair job
SELECT trigger_data_repair_job('quest_definitions', 'corruption_detected');
```

#### Scenario 2: Performance Issues
```bash
-- Drop problematic indexes temporarily
DROP INDEX IF EXISTS idx_quest_definitions_rewards_gin;
DROP INDEX IF EXISTS idx_quest_definitions_objectives_gin;

-- Rebuild with different configuration
CREATE INDEX CONCURRENTLY idx_quest_definitions_rewards_gin_opt
    ON gameplay.quest_definitions USING GIN (rewards jsonb_path_ops);
```

#### Scenario 3: Application Compatibility Issues
```bash
-- Enable feature flag to disable new functionality
UPDATE feature_flags SET enabled = false WHERE feature = 'quest_definitions_api';

-- Deploy application rollback
kubectl rollout undo deployment quest-service --to-revision=5
```

## 🧪 Validation Procedures

### Pre-Migration Validation
```bash
# 1. Schema compatibility check
./scripts/schema-compatibility-check.py --target production

# 2. Application readiness validation
./scripts/application-readiness-check.py --service quest-service

# 3. Data integrity pre-check
./scripts/data-integrity-precheck.py --source staging
```

### Post-Migration Validation
```bash
# 1. Schema validation
psql -h production-db -d production_db -f scripts/validate-schema.sql

# 2. Data integrity validation
./scripts/validate-data-integrity.py --comprehensive

# 3. Performance validation
./scripts/validate-performance-impact.py --baseline-comparison

# 4. Application integration testing
./scripts/integration-test-post-migration.py --full-suite
```

### Continuous Monitoring Validation
```bash
# 24-hour post-deployment monitoring
kubectl apply -f monitoring/post-deployment-validation.yaml

# Automated validation checks every 5 minutes
kubectl apply -f monitoring/continuous-validation-cronjob.yaml
```

## 📋 Success Criteria

### Functional Validation
- [ ] Quest definitions table exists and accessible
- [ ] All indexes created and operational
- [ ] Foreign key constraints validated
- [ ] Trigger functions working correctly
- [ ] Application can read/write quest data
- [ ] Content import scripts execute successfully

### Performance Validation
- [ ] Query performance within acceptable ranges (<50ms P95)
- [ ] Database connection pool utilization normal
- [ ] Index hit rates >95%
- [ ] Replication lag <30 seconds
- [ ] Memory usage within allocated limits

### Data Integrity Validation
- [ ] Row count matches expected values
- [ ] No orphaned or invalid records
- [ ] JSONB data properly formatted and accessible
- [ ] Check constraints working correctly
- [ ] Referential integrity maintained

### Application Validation
- [ ] Quest services start successfully
- [ ] API endpoints respond correctly
- [ ] Health checks pass
- [ ] Error rates within acceptable limits
- [ ] User-facing functionality works as expected

## 📞 Communication Plan

### Internal Communications

#### Development Team
- **Daily Updates:** Migration progress and any blockers
- **Technical Details:** Schema changes and performance impacts
- **Post-Mortem:** Lessons learned and improvement opportunities

#### Operations Team
- **Readiness Updates:** Database preparation status
- **Go-Live Notification:** Exact timing and expected impact
- **Incident Reports:** Real-time status during migration

#### Security Team
- **Access Changes:** New database permissions and access patterns
- **Audit Requirements:** Compliance with data handling policies
- **Security Validation:** Encryption and access control verification

### External Communications

#### Player-Facing
- **Maintenance Window:** Scheduled downtime notification
- **Feature Announcements:** New quest system capabilities
- **Status Updates:** Real-time migration status via status page

#### Partner Communications
- **API Changes:** New quest data structures and endpoints
- **Performance Expectations:** Expected improvements in quest loading
- **Integration Updates:** Changes to third-party quest integrations

## 🎯 Risk Mitigation

### High-Risk Scenarios

#### Scenario 1: Migration Failure
**Probability:** Medium (15%)  
**Impact:** High (Service Downtime)  
**Mitigation:**
- Comprehensive staging environment testing
- Automated rollback procedures
- Database backup validation
- Multiple rollback strategies

#### Scenario 2: Performance Degradation
**Probability:** Low (5%)  
**Impact:** Medium (User Experience)  
**Mitigation:**
- Detailed performance impact assessment
- Query optimization and index tuning
- Connection pool sizing
- Real-time performance monitoring

#### Scenario 3: Data Corruption
**Probability:** Low (3%)  
**Impact:** Critical (Data Loss)  
**Mitigation:**
- Point-in-time recovery capability
- Data integrity validation scripts
- Comprehensive backup strategy
- Automated corruption detection

### Contingency Plans

#### Plan A: Standard Migration (Primary)
- Liquibase-controlled schema migration
- Automated validation and monitoring
- Immediate rollback capability
- Comprehensive post-migration testing

#### Plan B: Phased Migration (Alternative)
- Deploy to read replicas first
- Gradual traffic migration
- Extended monitoring period
- Slower but safer approach

#### Plan C: Emergency Rollback (Worst Case)
- Immediate traffic redirection
- Automated schema rollback
- Database restoration from backup
- Full system recovery procedures

## 📚 Documentation Updates

### Post-Migration Documentation
- [ ] Update database schema documentation
- [ ] Create troubleshooting guides for new tables
- [ ] Document performance characteristics
- [ ] Update backup and recovery procedures

### Operational Runbooks
- [ ] Migration execution procedures
- [ ] Monitoring and alerting playbooks
- [ ] Incident response procedures
- [ ] Performance optimization guides

### API Documentation
- [ ] Update quest service API documentation
- [ ] Document new query patterns and indexes
- [ ] Create data model documentation
- [ ] Update integration guides

## 📈 Performance Benchmarks

### Pre-Migration Baselines
- **Current Quest Queries:** P95 = 45ms, P99 = 120ms
- **Database Connections:** Average 150 active connections
- **Table Size:** Quest-related tables = 2.3GB
- **Index Hit Rate:** 94.2%

### Post-Migration Targets
- **Quest Definition Queries:** P95 <50ms, P99 <100ms
- **Complex JSONB Queries:** P95 <75ms, P99 <150ms
- **Bulk Import Performance:** 1000+ quests/minute
- **Index Hit Rate:** >95%
- **Connection Pool Utilization:** <70%

### Scaling Projections
- **10k Active Players:** P95 <60ms, connections <300
- **50k Active Players:** P95 <80ms, connections <800
- **100k Active Players:** P95 <100ms, connections <1500

## 🎉 Success Metrics

### Technical Success Metrics
- Migration execution time <30 minutes
- Zero data loss during migration
- All post-migration validations pass
- Application restart time <5 minutes
- Performance degradation <10%

### Business Success Metrics
- Quest loading time improvement >20%
- Player quest completion rate increase >5%
- New quest content deployment time reduction >50%
- System availability >99.9% during migration window

---

## Deployment Command Summary

```bash
# Pre-deployment validation
./scripts/pre-deployment-validation.sh

# Execute migration
liquibase --changeLogFile=changelog.xml --url=jdbc:postgresql://production-db:5432/production_db update

# Post-deployment validation
./scripts/post-deployment-validation.sh

# Emergency rollback (if needed)
liquibase --changeLogFile=changelog.xml rollback --rollbackTag production_deployment_2026_01_06
```

**Deployment Lead:** Release Agent (#f5878f68)  
**Database Lead:** Database Agent (#2210)  
**Application Lead:** Backend Agent (#2220)  
**Rollback Coordinator:** DevOps Agent (#7e67a39b)  

**Status:** ✅ READY FOR PRODUCTION DEPLOYMENT  
**Estimated Duration:** 4 hours (including validation)  
**Rollback Window:** <15 minutes  
**Risk Assessment:** MEDIUM (Comprehensive mitigation in place)
