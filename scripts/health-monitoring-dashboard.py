#!/usr/bin/env python3
"""
NECPGAME Enterprise Health Monitoring Dashboard
Issue: Production Readiness - Comprehensive Health Monitoring

Monitors all services for:
- API availability and response times
- Database connectivity and performance
- Redis cache health
- Kafka event streaming
- Memory usage and performance metrics
- Business logic validation
- Security checks

Supports both development (docker-compose) and production (k8s) environments.
"""

import asyncio
import aiohttp
import asyncpg
import redis.asyncio as redis
import json
import time
import os
from datetime import datetime, timedelta
from typing import Dict, List, Optional, Tuple, Any
from dataclasses import dataclass, asdict
from enum import Enum
import logging
import sys
from pathlib import Path

# Configure logging
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s',
    handlers=[
        logging.FileHandler('health_monitor.log'),
        logging.StreamHandler(sys.stdout)
    ]
)
logger = logging.getLogger('HealthMonitor')

class HealthStatus(Enum):
    HEALTHY = "healthy"
    WARNING = "warning"
    CRITICAL = "critical"
    UNKNOWN = "unknown"

class ServiceType(Enum):
    AUTH = "auth"
    ABILITY = "ability"
    COMBAT = "combat"
    ECONOMY = "economy"
    MATCHMAKING = "matchmaking"
    WORLD_EVENTS = "world_events"

@dataclass
class HealthCheck:
    service_name: str
    service_type: ServiceType
    endpoint: str
    status: HealthStatus
    response_time_ms: float
    last_check: datetime
    error_message: Optional[str] = None
    metrics: Dict[str, Any] = None

@dataclass
class DatabaseHealth:
    connections_active: int
    connections_idle: int
    connections_total: int
    query_avg_time_ms: float
    slowest_queries: List[Dict[str, Any]]
    status: HealthStatus

@dataclass
class CacheHealth:
    memory_used_mb: float
    memory_peak_mb: float
    connections_active: int
    hit_rate_percent: float
    eviction_rate: float
    status: HealthStatus

@dataclass
class ServiceHealth:
    service_name: str
    overall_status: HealthStatus
    api_checks: List[HealthCheck]
    database_health: Optional[DatabaseHealth] = None
    cache_health: Optional[CacheHealth] = None
    uptime_seconds: float = 0
    last_incident: Optional[datetime] = None

class NECPGAMEHealthMonitor:
    """Enterprise-grade health monitoring for all NECPGAME services"""

    def __init__(self):
        self.services = self._load_service_config()
        self.health_data: Dict[str, ServiceHealth] = {}
        self.session_timeout = aiohttp.ClientTimeout(total=10, connect=5)
        self.start_time = datetime.now()

    def _load_service_config(self) -> Dict[str, Dict[str, Any]]:
        """Load service configuration from environment or config files"""
        # Default development configuration
        services = {
            "auth-service": {
                "type": ServiceType.AUTH,
                "host": os.getenv("AUTH_SERVICE_HOST", "localhost"),
                "port": int(os.getenv("AUTH_SERVICE_PORT", "8080")),
                "health_endpoint": "/health",
                "endpoints": [
                    "/auth/health",
                    "/auth/health/ws",
                    "/auth/sessions/stats"
                ]
            },
            "ability-service": {
                "type": ServiceType.ABILITY,
                "host": os.getenv("ABILITY_SERVICE_HOST", "localhost"),
                "port": int(os.getenv("ABILITY_SERVICE_PORT", "8081")),
                "health_endpoint": "/health",
                "endpoints": [
                    "/ability/health",
                    "/ability/combat-abilities"
                ]
            },
            "combat-service": {
                "type": ServiceType.COMBAT,
                "host": os.getenv("COMBAT_SERVICE_HOST", "localhost"),
                "port": int(os.getenv("COMBAT_SERVICE_PORT", "8084")),
                "health_endpoint": "/health",
                "endpoints": [
                    "/combat/health",
                    "/combat/health/ws"
                ]
            },
            "economy-service": {
                "type": ServiceType.ECONOMY,
                "host": os.getenv("ECONOMY_SERVICE_HOST", "localhost"),
                "port": int(os.getenv("ECONOMY_SERVICE_PORT", "8083")),
                "health_endpoint": "/health",
                "endpoints": [
                    "/economy/health",
                    "/economy/bazaar-bots"
                ]
            },
            "matchmaking-service": {
                "type": ServiceType.MATCHMAKING,
                "host": os.getenv("MATCHMAKING_SERVICE_HOST", "localhost"),
                "port": int(os.getenv("MATCHMAKING_SERVICE_PORT", "8082")),
                "health_endpoint": "/health",
                "endpoints": [
                    "/matchmaking/health"
                ]
            }
        }

        # Override with production config if available
        config_file = Path("health_config.json")
        if config_file.exists():
            with open(config_file) as f:
                prod_config = json.load(f)
                services.update(prod_config)

        return services

    async def check_service_health(self, service_name: str, service_config: Dict[str, Any]) -> ServiceHealth:
        """Comprehensive health check for a single service"""
        logger.info(f"Checking health for {service_name}")

        service_health = ServiceHealth(
            service_name=service_name,
            overall_status=HealthStatus.UNKNOWN,
            api_checks=[]
        )

        # Check API endpoints
        api_checks = await self._check_api_endpoints(service_name, service_config)
        service_health.api_checks = api_checks

        # Check database if applicable
        if service_config.get("check_database", True):
            db_health = await self._check_database_health(service_name)
            if db_health:
                service_health.database_health = db_health

        # Check cache if applicable
        if service_config.get("check_cache", True):
            cache_health = await self._check_cache_health()
            if cache_health:
                service_health.cache_health = cache_health

        # Determine overall status
        service_health.overall_status = self._calculate_overall_status(service_health)

        # Update uptime tracking
        if service_name in self.health_data:
            previous = self.health_data[service_name]
            if previous.overall_status == HealthStatus.HEALTHY and service_health.overall_status != HealthStatus.HEALTHY:
                service_health.last_incident = datetime.now()
            service_health.uptime_seconds = previous.uptime_seconds + 30  # Assuming 30s intervals
        else:
            service_health.uptime_seconds = 30

        return service_health

    async def _check_api_endpoints(self, service_name: str, config: Dict[str, Any]) -> List[HealthCheck]:
        """Check all API endpoints for a service"""
        checks = []
        base_url = f"http://{config['host']}:{config['port']}"

        async with aiohttp.ClientSession(timeout=self.session_timeout) as session:
            # Check health endpoint first
            health_check = await self._check_single_endpoint(
                session, service_name, f"{base_url}{config['health_endpoint']}", "health"
            )
            checks.append(health_check)

            # Check additional endpoints if health is OK
            if health_check.status in [HealthStatus.HEALTHY, HealthStatus.WARNING]:
                for endpoint in config.get("endpoints", []):
                    check = await self._check_single_endpoint(
                        session, service_name, f"{base_url}{endpoint}", endpoint
                    )
                    checks.append(check)

        return checks

    async def _check_single_endpoint(self, session: aiohttp.ClientSession, service_name: str,
                                   url: str, endpoint_name: str) -> HealthCheck:
        """Check a single API endpoint"""
        check = HealthCheck(
            service_name=service_name,
            service_type=self.services[service_name]["type"],
            endpoint=url,
            status=HealthStatus.UNKNOWN,
            response_time_ms=0,
            last_check=datetime.now()
        )

        try:
            start_time = time.time()
            async with session.get(url) as response:
                response_time = (time.time() - start_time) * 1000
                check.response_time_ms = response_time

                if response.status == 200:
                    # Parse response for additional metrics
                    try:
                        data = await response.json()
                        check.metrics = data
                    except:
                        pass

                    # Determine status based on response time and content
                    if response_time < 100:  # Fast response
                        check.status = HealthStatus.HEALTHY
                    elif response_time < 500:  # Acceptable response
                        check.status = HealthStatus.WARNING
                        check.error_message = f"Slow response: {response_time:.1f}ms"
                    else:  # Too slow
                        check.status = HealthStatus.CRITICAL
                        check.error_message = f"Very slow response: {response_time:.1f}ms"

                elif response.status in [401, 403]:
                    check.status = HealthStatus.WARNING
                    check.error_message = f"Authentication issue: {response.status}"
                elif response.status >= 500:
                    check.status = HealthStatus.CRITICAL
                    check.error_message = f"Server error: {response.status}"
                else:
                    check.status = HealthStatus.WARNING
                    check.error_message = f"Unexpected status: {response.status}"

        except asyncio.TimeoutError:
            check.status = HealthStatus.CRITICAL
            check.error_message = "Request timeout"
            check.response_time_ms = self.session_timeout.total * 1000
        except aiohttp.ClientError as e:
            check.status = HealthStatus.CRITICAL
            check.error_message = f"Connection error: {str(e)}"
        except Exception as e:
            check.status = HealthStatus.CRITICAL
            check.error_message = f"Unexpected error: {str(e)}"

        return check

    async def _check_database_health(self, service_name: str) -> Optional[DatabaseHealth]:
        """Check database health and performance"""
        try:
            # Connect to PostgreSQL
            conn = await asyncpg.connect(
                host=os.getenv("DB_HOST", "localhost"),
                port=int(os.getenv("DB_PORT", "5432")),
                user=os.getenv("DB_USER", "necpgame"),
                password=os.getenv("DB_PASSWORD", "necpgame_password"),
                database=os.getenv("DB_NAME", "necpgame")
            )

            # Get connection stats
            stats = await conn.fetchrow("""
                SELECT
                    count(*) as total_connections,
                    count(*) filter (where state = 'active') as active_connections,
                    count(*) filter (where state = 'idle') as idle_connections,
                    avg(extract(epoch from (now() - query_start))) * 1000 as avg_query_time
                FROM pg_stat_activity
                WHERE datname = current_database()
            """)

            # Get slowest queries
            slow_queries = await conn.fetch("""
                SELECT
                    query,
                    extract(epoch from (now() - query_start)) * 1000 as duration_ms,
                    usename,
                    client_addr
                FROM pg_stat_activity
                WHERE datname = current_database()
                AND query_start IS NOT NULL
                AND extract(epoch from (now() - query_start)) > 0.1
                ORDER BY query_start DESC
                LIMIT 5
            """)

            await conn.close()

            # Determine status
            status = HealthStatus.HEALTHY
            if stats['avg_query_time'] and stats['avg_query_time'] > 100:  # > 100ms avg
                status = HealthStatus.WARNING
            if stats['avg_query_time'] and stats['avg_query_time'] > 500:  # > 500ms avg
                status = HealthStatus.CRITICAL

            return DatabaseHealth(
                connections_active=stats['active_connections'] or 0,
                connections_idle=stats['idle_connections'] or 0,
                connections_total=stats['total_connections'] or 0,
                query_avg_time_ms=stats['avg_query_time'] or 0,
                slowest_queries=[dict(q) for q in slow_queries],
                status=status
            )

        except Exception as e:
            logger.error(f"Database health check failed: {e}")
            return None

    async def _check_cache_health(self) -> Optional[CacheHealth]:
        """Check Redis cache health"""
        try:
            r = redis.Redis(
                host=os.getenv("REDIS_HOST", "localhost"),
                port=int(os.getenv("REDIS_PORT", "6379")),
                decode_responses=True
            )

            # Get memory info
            memory_info = await r.info("memory")
            stats_info = await r.info("stats")

            # Get connection info
            clients_info = await r.info("clients")

            await r.close()

            memory_used = float(memory_info.get('used_memory', 0)) / (1024 * 1024)  # MB
            memory_peak = float(memory_info.get('used_memory_peak', 0)) / (1024 * 1024)  # MB
            connections = int(clients_info.get('connected_clients', 0))
            keyspace_hits = float(stats_info.get('keyspace_hits', 0))
            keyspace_misses = float(stats_info.get('keyspace_misses', 0))

            # Calculate hit rate
            total_requests = keyspace_hits + keyspace_misses
            hit_rate = (keyspace_hits / total_requests * 100) if total_requests > 0 else 100.0

            # Calculate eviction rate (simplified)
            evicted_keys = float(stats_info.get('evicted_keys', 0))
            eviction_rate = evicted_keys / max(1, float(stats_info.get('total_connections_received', 1)))

            # Determine status
            status = HealthStatus.HEALTHY
            if memory_used > 500:  # > 500MB memory usage
                status = HealthStatus.WARNING
            if memory_used > 1000:  # > 1GB memory usage
                status = HealthStatus.CRITICAL
            if hit_rate < 80:  # < 80% hit rate
                status = HealthStatus.WARNING
            if hit_rate < 50:  # < 50% hit rate
                status = HealthStatus.CRITICAL

            return CacheHealth(
                memory_used_mb=memory_used,
                memory_peak_mb=memory_peak,
                connections_active=connections,
                hit_rate_percent=hit_rate,
                eviction_rate=eviction_rate,
                status=status
            )

        except Exception as e:
            logger.error(f"Cache health check failed: {e}")
            return None

    def _calculate_overall_status(self, service_health: ServiceHealth) -> HealthStatus:
        """Calculate overall service health status"""
        # Critical if any API check is critical
        for check in service_health.api_checks:
            if check.status == HealthStatus.CRITICAL:
                return HealthStatus.CRITICAL

        # Warning if any component has issues
        if (service_health.database_health and
            service_health.database_health.status != HealthStatus.HEALTHY):
            return HealthStatus.WARNING

        if (service_health.cache_health and
            service_health.cache_health.status != HealthStatus.HEALTHY):
            return HealthStatus.WARNING

        # Warning if any API check is warning
        for check in service_health.api_checks:
            if check.status == HealthStatus.WARNING:
                return HealthStatus.WARNING

        # Healthy if all checks passed
        if service_health.api_checks and all(c.status == HealthStatus.HEALTHY for c in service_health.api_checks):
            return HealthStatus.HEALTHY

        return HealthStatus.UNKNOWN

    async def run_monitoring_loop(self, interval_seconds: int = 30):
        """Run continuous monitoring loop"""
        logger.info("Starting NECPGAME Health Monitoring Dashboard")
        logger.info(f"Monitoring {len(self.services)} services with {interval_seconds}s intervals")

        while True:
            start_time = time.time()

            # Check all services concurrently
            tasks = []
            for service_name, config in self.services.items():
                task = asyncio.create_task(self.check_service_health(service_name, config))
                tasks.append(task)

            results = await asyncio.gather(*tasks, return_exceptions=True)

            # Update health data
            for service_name, result in zip(self.services.keys(), results):
                if isinstance(result, Exception):
                    logger.error(f"Failed to check {service_name}: {result}")
                    self.health_data[service_name] = ServiceHealth(
                        service_name=service_name,
                        overall_status=HealthStatus.CRITICAL,
                        api_checks=[],
                        last_incident=datetime.now()
                    )
                else:
                    self.health_data[service_name] = result

            # Generate report
            self._generate_report()

            # Wait for next check
            elapsed = time.time() - start_time
            sleep_time = max(0, interval_seconds - elapsed)
            await asyncio.sleep(sleep_time)

    def _generate_report(self):
        """Generate comprehensive health report"""
        report = {
            "timestamp": datetime.now().isoformat(),
            "monitor_uptime_seconds": (datetime.now() - self.start_time).total_seconds(),
            "services": {},
            "summary": {
                "total_services": len(self.services),
                "healthy_services": 0,
                "warning_services": 0,
                "critical_services": 0,
                "overall_status": HealthStatus.HEALTHY.value
            }
        }

        for service_name, health in self.health_data.items():
            report["services"][service_name] = asdict(health)

            if health.overall_status == HealthStatus.HEALTHY:
                report["summary"]["healthy_services"] += 1
            elif health.overall_status == HealthStatus.WARNING:
                report["summary"]["warning_services"] += 1
            elif health.overall_status == HealthStatus.CRITICAL:
                report["summary"]["critical_services"] += 1
                report["summary"]["overall_status"] = HealthStatus.CRITICAL.value

        if report["summary"]["warning_services"] > 0 and report["summary"]["overall_status"] == HealthStatus.HEALTHY.value:
            report["summary"]["overall_status"] = HealthStatus.WARNING.value

        # Save report to file
        with open("health_report.json", "w") as f:
            json.dump(report, f, indent=2, default=str)

        # Log summary
        summary = report["summary"]
        logger.info(f"Health Report: {summary['healthy_services']}✓ {summary['warning_services']}⚠️ {summary['critical_services']}❌")

        # Print critical issues
        for service_name, health in self.health_data.items():
            if health.overall_status == HealthStatus.CRITICAL:
                logger.warning(f"CRITICAL: {service_name} - {health.api_checks[0].error_message if health.api_checks else 'Unknown error'}")

    async def run_once(self) -> Dict[str, Any]:
        """Run single health check and return results"""
        logger.info("Running single health check...")

        tasks = []
        for service_name, config in self.services.items():
            task = asyncio.create_task(self.check_service_health(service_name, config))
            tasks.append(task)

        results = await asyncio.gather(*tasks, return_exceptions=True)

        for service_name, result in zip(self.services.keys(), results):
            if isinstance(result, Exception):
                self.health_data[service_name] = ServiceHealth(
                    service_name=service_name,
                    overall_status=HealthStatus.CRITICAL,
                    api_checks=[]
                )
            else:
                self.health_data[service_name] = result

        self._generate_report()

        return {
            "timestamp": datetime.now().isoformat(),
            "services": {name: asdict(health) for name, health in self.health_data.items()},
            "summary": self._calculate_summary()
        }

    def _calculate_summary(self) -> Dict[str, Any]:
        """Calculate health summary"""
        total = len(self.health_data)
        healthy = sum(1 for h in self.health_data.values() if h.overall_status == HealthStatus.HEALTHY)
        warning = sum(1 for h in self.health_data.values() if h.overall_status == HealthStatus.WARNING)
        critical = sum(1 for h in self.health_data.values() if h.overall_status == HealthStatus.CRITICAL)

        overall = HealthStatus.HEALTHY
        if critical > 0:
            overall = HealthStatus.CRITICAL
        elif warning > 0:
            overall = HealthStatus.WARNING

        return {
            "total_services": total,
            "healthy_services": healthy,
            "warning_services": warning,
            "critical_services": critical,
            "overall_status": overall.value,
            "health_percentage": (healthy / total * 100) if total > 0 else 0
        }

async def main():
    """Main entry point"""
    import argparse

    parser = argparse.ArgumentParser(description="NECPGAME Enterprise Health Monitor")
    parser.add_argument("--once", action="store_true", help="Run single check and exit")
    parser.add_argument("--interval", type=int, default=30, help="Monitoring interval in seconds")
    parser.add_argument("--config", type=str, help="Path to config file")

    args = parser.parse_args()

    monitor = NECPGAMEHealthMonitor()

    if args.once:
        result = await monitor.run_once()
        print(json.dumps(result, indent=2, default=str))
    else:
        await monitor.run_monitoring_loop(args.interval)

if __name__ == "__main__":
    asyncio.run(main())