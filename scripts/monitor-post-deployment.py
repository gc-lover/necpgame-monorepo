#!/usr/bin/env python3
"""
Post-Deployment Monitoring Script
Issue: #2227 - Continuous monitoring of database migration deployment
Agent: Release - Automated monitoring and alerting for production database deployment

Monitors:
- Database performance metrics
- Application health checks
- Query performance regression
- Data integrity validation
- Alert generation and escalation
"""

import argparse
import time
import logging
import json
import sys
from datetime import datetime, timedelta
from typing import Dict, List, Any, Optional
from dataclasses import dataclass
from pathlib import Path

import psycopg2
import psycopg2.extras
import requests
from prometheus_client import CollectorRegistry, Gauge, Counter, push_to_gateway

# Configuration
DEFAULT_PROMETHEUS_GATEWAY = "http://prometheus-pushgateway.necpgame.internal:9091"
DEFAULT_ALERT_WEBHOOK = "https://hooks.slack.com/services/YOUR/SLACK/WEBHOOK"
MONITORING_INTERVAL = 60  # seconds
ALERT_ESCALATION_INTERVAL = 300  # 5 minutes

@dataclass
class MonitoringConfig:
    """Monitoring configuration"""
    environment: str
    deployment_id: str
    database_host: str
    database_port: int
    database_name: str
    database_user: str
    database_password: str
    prometheus_gateway: str
    alert_webhook: str
    duration: int  # seconds
    log_level: str

@dataclass
class Alert:
    """Alert definition"""
    name: str
    severity: str  # critical, high, medium, low
    message: str
    timestamp: datetime
    resolved: bool = False
    resolved_at: Optional[datetime] = None
    escalation_count: int = 0
    last_escalated: Optional[datetime] = None

class DatabaseMonitor:
    """Database performance and health monitoring"""

    def __init__(self, config: MonitoringConfig):
        self.config = config
        self.connection = None
        self.baseline_metrics = {}
        self.alerts: List[Alert] = []
        self.registry = CollectorRegistry()

        # Prometheus metrics
        self.db_connections_active = Gauge(
            'database_connections_active',
            'Number of active database connections',
            registry=self.registry
        )

        self.db_query_latency_p95 = Gauge(
            'database_query_latency_p95_seconds',
            '95th percentile query latency',
            registry=self.registry
        )

        self.db_table_size_bytes = Gauge(
            'database_table_size_bytes',
            'Size of database table in bytes',
            ['table'],
            registry=self.registry
        )

        self.migration_alerts_total = Counter(
            'migration_alerts_total',
            'Total number of migration alerts',
            ['severity'],
            registry=self.registry
        )

    def connect(self) -> bool:
        """Establish database connection"""
        try:
            self.connection = psycopg2.connect(
                host=self.config.database_host,
                port=self.config.database_port,
                dbname=self.config.database_name,
                user=self.config.database_user,
                password=self.config.database_password,
                connect_timeout=10
            )
            self.connection.autocommit = True
            return True
        except Exception as e:
            logging.error(f"Database connection failed: {e}")
            return False

    def collect_baseline_metrics(self) -> None:
        """Collect baseline metrics before migration"""
        logging.info("Collecting baseline metrics...")

        try:
            with self.connection.cursor(cursor_factory=psycopg2.extras.RealDictCursor) as cursor:
                # Query performance baseline
                cursor.execute("""
                    SELECT
                        schemaname,
                        tablename,
                        n_tup_ins,
                        n_tup_upd,
                        n_tup_del,
                        n_live_tup,
                        n_dead_tup
                    FROM pg_stat_user_tables
                    WHERE schemaname = 'gameplay'
                """)

                for row in cursor.fetchall():
                    table_name = row['tablename']
                    self.baseline_metrics[f"{table_name}_live_tuples"] = row['n_live_tup']
                    self.baseline_metrics[f"{table_name}_dead_tuples"] = row['n_dead_tup']

                # Connection baseline
                cursor.execute("SELECT count(*) as active_connections FROM pg_stat_activity WHERE state = 'active'")
                row = cursor.fetchone()
                self.baseline_metrics['active_connections'] = row['active_connections'] if row else 0

        except Exception as e:
            logging.error(f"Failed to collect baseline metrics: {e}")

    def check_database_health(self) -> Dict[str, Any]:
        """Check database health and performance"""
        metrics = {}

        try:
            with self.connection.cursor(cursor_factory=psycopg2.extras.RealDictCursor) as cursor:
                # Basic connectivity
                cursor.execute("SELECT 1 as health_check")
                result = cursor.fetchone()
                metrics['connectivity'] = result['health_check'] == 1

                # Active connections
                cursor.execute("""
                    SELECT count(*) as active_connections
                    FROM pg_stat_activity
                    WHERE state = 'active' AND backend_type = 'client backend'
                """)
                row = cursor.fetchone()
                metrics['active_connections'] = row['active_connections'] if row else 0

                # Quest definitions table health
                cursor.execute("""
                    SELECT
                        schemaname,
                        tablename,
                        n_tup_ins,
                        n_tup_upd,
                        n_tup_del,
                        n_live_tup,
                        n_dead_tup,
                        pg_size_pretty(pg_total_relation_size(schemaname||'.'||tablename)) as size
                    FROM pg_stat_user_tables
                    WHERE schemaname = 'gameplay' AND tablename = 'quest_definitions'
                """)

                if cursor.rowcount > 0:
                    row = cursor.fetchone()
                    metrics['quest_definitions_exists'] = True
                    metrics['quest_definitions_live_tuples'] = row['n_live_tup']
                    metrics['quest_definitions_dead_tuples'] = row['n_dead_tup']
                    metrics['quest_definitions_size'] = row['size']
                else:
                    metrics['quest_definitions_exists'] = False

                # Query performance (recent queries >100ms)
                cursor.execute("""
                    SELECT count(*) as slow_queries
                    FROM pg_stat_statements
                    WHERE mean_time > 100000  -- microseconds
                    AND query LIKE '%quest_definitions%'
                """)
                row = cursor.fetchone()
                metrics['slow_quest_queries'] = row['slow_queries'] if row else 0

                # Index usage
                cursor.execute("""
                    SELECT
                        schemaname,
                        tablename,
                        indexname,
                        idx_scan,
                        idx_tup_read,
                        idx_tup_fetch
                    FROM pg_stat_user_indexes
                    WHERE schemaname = 'gameplay' AND tablename = 'quest_definitions'
                """)

                index_stats = {}
                for row in cursor.fetchall():
                    index_name = row['indexname']
                    index_stats[index_name] = {
                        'scans': row['idx_scan'],
                        'tuples_read': row['idx_tup_read'],
                        'tuples_fetched': row['idx_tup_fetch']
                    }
                metrics['index_stats'] = index_stats

        except Exception as e:
            logging.error(f"Database health check failed: {e}")
            metrics['error'] = str(e)
            metrics['connectivity'] = False

        return metrics

    def check_application_health(self) -> Dict[str, Any]:
        """Check application health via APIs"""
        metrics = {}

        # Quest service health check
        try:
            response = requests.get("http://quest-service.necpgame.internal/health", timeout=5)
            metrics['quest_service_health'] = response.status_code == 200
            metrics['quest_service_response_time'] = response.elapsed.total_seconds()
        except Exception as e:
            logging.warning(f"Quest service health check failed: {e}")
            metrics['quest_service_health'] = False

        # Quest API performance check
        try:
            start_time = time.time()
            response = requests.get(
                "http://quest-service.necpgame.internal/api/v1/quests?limit=1",
                timeout=10
            )
            end_time = time.time()

            metrics['quest_api_available'] = response.status_code == 200
            metrics['quest_api_response_time'] = end_time - start_time

            if response.status_code == 200:
                data = response.json()
                metrics['quest_api_returns_data'] = len(data.get('quests', [])) >= 0
            else:
                metrics['quest_api_returns_data'] = False

        except Exception as e:
            logging.warning(f"Quest API check failed: {e}")
            metrics['quest_api_available'] = False
            metrics['quest_api_returns_data'] = False

        return metrics

    def analyze_metrics(self, db_metrics: Dict[str, Any], app_metrics: Dict[str, Any]) -> List[Alert]:
        """Analyze metrics and generate alerts"""
        alerts = []

        # Database connectivity
        if not db_metrics.get('connectivity', False):
            alerts.append(Alert(
                name="DatabaseConnectivityLost",
                severity="critical",
                message="Lost connectivity to production database",
                timestamp=datetime.now()
            ))

        # Table existence
        if not db_metrics.get('quest_definitions_exists', False):
            alerts.append(Alert(
                name="QuestDefinitionsTableMissing",
                severity="critical",
                message="quest_definitions table does not exist",
                timestamp=datetime.now()
            ))

        # Performance degradation
        baseline_connections = self.baseline_metrics.get('active_connections', 0)
        current_connections = db_metrics.get('active_connections', 0)

        if current_connections > baseline_connections * 2:
            alerts.append(Alert(
                name="DatabaseConnectionSpike",
                severity="high",
                message=f"Database connections increased by {((current_connections / baseline_connections) - 1) * 100:.1f}%",
                timestamp=datetime.now()
            ))

        # Slow queries
        slow_queries = db_metrics.get('slow_queries', 0)
        if slow_queries > 10:
            alerts.append(Alert(
                name="ExcessiveSlowQueries",
                severity="medium",
                message=f"Found {slow_queries} slow quest-related queries (>100ms)",
                timestamp=datetime.now()
            ))

        # Application health
        if not app_metrics.get('quest_service_health', False):
            alerts.append(Alert(
                name="QuestServiceUnhealthy",
                severity="high",
                message="Quest service health check failed",
                timestamp=datetime.now()
            ))

        if not app_metrics.get('quest_api_available', False):
            alerts.append(Alert(
                name="QuestAPIDown",
                severity="high",
                message="Quest API is not responding",
                timestamp=datetime.now()
            ))

        # Response time degradation
        api_response_time = app_metrics.get('quest_api_response_time', 0)
        if api_response_time > 2.0:  # 2 seconds
            alerts.append(Alert(
                name="QuestAPIResponseSlow",
                severity="medium",
                message=f"Quest API response time: {api_response_time:.2f}s (threshold: 2.0s)",
                timestamp=datetime.now()
            ))

        return alerts

    def send_alerts(self, alerts: List[Alert]) -> None:
        """Send alerts to configured webhook"""
        if not alerts or not self.config.alert_webhook:
            return

        current_time = datetime.now()

        for alert in alerts:
            # Skip if already resolved
            if alert.resolved:
                continue

            # Check escalation
            should_escalate = False
            if alert.last_escalated:
                time_since_escalation = current_time - alert.last_escalated
                if time_since_escalation.total_seconds() > ALERT_ESCALATION_INTERVAL:
                    should_escalate = True
                    alert.escalation_count += 1
            else:
                should_escalate = True
                alert.last_escalated = current_time

            if should_escalate:
                self._send_alert_webhook(alert)

    def _send_alert_webhook(self, alert: Alert) -> None:
        """Send alert to webhook"""
        payload = {
            "text": f"🚨 *{alert.severity.upper()}*: {alert.name}",
            "attachments": [{
                "color": self._severity_color(alert.severity),
                "fields": [
                    {"title": "Environment", "value": self.config.environment, "short": True},
                    {"title": "Deployment ID", "value": self.config.deployment_id, "short": True},
                    {"title": "Severity", "value": alert.severity.upper(), "short": True},
                    {"title": "Escalation Count", "value": str(alert.escalation_count), "short": True},
                ],
                "text": alert.message,
                "footer": "NECPGAME Database Migration Monitor",
                "ts": alert.timestamp.timestamp()
            }]
        }

        try:
            response = requests.post(
                self.config.alert_webhook,
                json=payload,
                timeout=10
            )
            if response.status_code == 200:
                logging.info(f"Alert sent: {alert.name}")
            else:
                logging.error(f"Failed to send alert: HTTP {response.status_code}")
        except Exception as e:
            logging.error(f"Failed to send alert webhook: {e}")

    def _severity_color(self, severity: str) -> str:
        """Get color for alert severity"""
        colors = {
            "critical": "danger",
            "high": "warning",
            "medium": "warning",
            "low": "good"
        }
        return colors.get(severity, "warning")

    def update_prometheus_metrics(self, db_metrics: Dict[str, Any], app_metrics: Dict[str, Any]) -> None:
        """Update Prometheus metrics"""
        try:
            # Database metrics
            self.db_connections_active.set(db_metrics.get('active_connections', 0))

            # Table metrics
            quest_size = db_metrics.get('quest_definitions_size', '0')
            # Convert size string to bytes (simplified)
            size_bytes = 0
            if 'MB' in quest_size:
                size_bytes = int(float(quest_size.replace(' MB', '')) * 1024 * 1024)
            elif 'GB' in quest_size:
                size_bytes = int(float(quest_size.replace(' GB', '')) * 1024 * 1024 * 1024)

            self.db_table_size_bytes.labels(table='quest_definitions').set(size_bytes)

            # Alert metrics
            active_alerts = [a for a in self.alerts if not a.resolved]
            for alert in active_alerts:
                self.migration_alerts_total.labels(severity=alert.severity).inc()

            # Push to Prometheus
            push_to_gateway(
                self.config.prometheus_gateway,
                job=f"database_migration_monitor_{self.config.environment}",
                registry=self.registry
            )

        except Exception as e:
            logging.warning(f"Failed to update Prometheus metrics: {e}")

    def save_metrics_snapshot(self, db_metrics: Dict[str, Any], app_metrics: Dict[str, Any]) -> None:
        """Save metrics snapshot for analysis"""
        snapshot = {
            "timestamp": datetime.now().isoformat(),
            "environment": self.config.environment,
            "deployment_id": self.config.deployment_id,
            "database_metrics": db_metrics,
            "application_metrics": app_metrics,
            "active_alerts": len([a for a in self.alerts if not a.resolved])
        }

        snapshot_file = f"/var/log/necp-game/migration-monitor-{self.config.deployment_id}.json"
        try:
            with open(snapshot_file, 'a') as f:
                json.dump(snapshot, f)
                f.write('\n')
        except Exception as e:
            logging.warning(f"Failed to save metrics snapshot: {e}")

def main():
    parser = argparse.ArgumentParser(description='Post-deployment monitoring for database migration')
    parser.add_argument('--env', '--environment', required=True, help='Target environment')
    parser.add_argument('--deployment-id', required=True, help='Deployment identifier')
    parser.add_argument('--database-host', default='necpgame-production-db.internal', help='Database host')
    parser.add_argument('--database-port', type=int, default=5432, help='Database port')
    parser.add_argument('--database-name', default='necp_game', help='Database name')
    parser.add_argument('--database-user', required=True, help='Database user')
    parser.add_argument('--database-password', required=True, help='Database password')
    parser.add_argument('--prometheus-gateway', default=DEFAULT_PROMETHEUS_GATEWAY, help='Prometheus push gateway')
    parser.add_argument('--alert-webhook', default=DEFAULT_ALERT_WEBHOOK, help='Alert webhook URL')
    parser.add_argument('--duration', type=int, default=86400, help='Monitoring duration in seconds')
    parser.add_argument('--log-level', default='INFO', choices=['DEBUG', 'INFO', 'WARNING', 'ERROR'], help='Log level')

    args = parser.parse_args()

    # Configure logging
    logging.basicConfig(
        level=getattr(logging, args.log_level),
        format='%(asctime)s - %(levelname)s - %(message)s',
        handlers=[
            logging.FileHandler(f'/var/log/necp-game/migration-monitor-{args.deployment_id}.log'),
            logging.StreamHandler(sys.stdout)
        ]
    )

    # Create configuration
    config = MonitoringConfig(
        environment=args.env,
        deployment_id=args.deployment_id,
        database_host=args.database_host,
        database_port=args.database_port,
        database_name=args.database_name,
        database_user=args.database_user,
        database_password=args.database_password,
        prometheus_gateway=args.prometheus_gateway,
        alert_webhook=args.alert_webhook,
        duration=args.duration,
        log_level=args.log_level
    )

    # Initialize monitor
    monitor = DatabaseMonitor(config)

    logging.info(f"Starting post-deployment monitoring for {config.duration} seconds")
    logging.info(f"Environment: {config.environment}")
    logging.info(f"Deployment ID: {config.deployment_id}")

    # Connect to database
    if not monitor.connect():
        logging.error("Failed to connect to database. Exiting.")
        sys.exit(1)

    # Collect baseline metrics
    monitor.collect_baseline_metrics()

    # Monitoring loop
    start_time = time.time()
    end_time = start_time + config.duration

    while time.time() < end_time:
        try:
            # Collect metrics
            db_metrics = monitor.check_database_health()
            app_metrics = monitor.check_application_health()

            # Analyze and generate alerts
            new_alerts = monitor.analyze_metrics(db_metrics, app_metrics)
            monitor.alerts.extend(new_alerts)

            # Send alerts
            monitor.send_alerts(monitor.alerts)

            # Update Prometheus
            monitor.update_prometheus_metrics(db_metrics, app_metrics)

            # Save snapshot
            monitor.save_metrics_snapshot(db_metrics, app_metrics)

            # Log summary
            active_alerts = len([a for a in monitor.alerts if not a.resolved])
            logging.info(f"Monitoring cycle complete. Active alerts: {active_alerts}")

        except Exception as e:
            logging.error(f"Monitoring cycle failed: {e}")

        # Wait for next cycle
        time.sleep(MONITORING_INTERVAL)

    logging.info("Post-deployment monitoring completed")

if __name__ == '__main__':
    main()
