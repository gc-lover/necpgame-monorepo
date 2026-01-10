#!/usr/bin/env python3
"""
NECPGAME Simple Health Check Script
Production-ready health monitoring without external dependencies

Checks all services for basic availability and response times.
"""

import json
import time
import urllib.request
import urllib.error
import socket
import os
from datetime import datetime
from typing import Dict, List, Optional, Any
from dataclasses import dataclass, asdict
from enum import Enum

class HealthStatus(Enum):
    HEALTHY = "healthy"
    WARNING = "warning"
    CRITICAL = "critical"
    UNKNOWN = "unknown"

@dataclass
class HealthCheck:
    service_name: str
    endpoint: str
    status: HealthStatus
    response_time_ms: float
    status_code: Optional[int] = None
    error_message: Optional[str] = None
    timestamp: str = ""

@dataclass
class ServiceHealth:
    service_name: str
    overall_status: HealthStatus
    checks: List[HealthCheck]
    uptime_percentage: float = 100.0
    total_checks: int = 0
    successful_checks: int = 0

class NECPGAMESimpleHealthChecker:
    """Simple health checker for NECPGAME services"""

    def __init__(self):
        self.services = self._load_service_config()
        self.timeout_seconds = 10
        self.results_history: List[Dict[str, Any]] = []

    def _load_service_config(self) -> Dict[str, Dict[str, Any]]:
        """Load service configuration"""
        return {
            "auth-service": {
                "host": os.getenv("AUTH_SERVICE_HOST", "localhost"),
                "port": int(os.getenv("AUTH_SERVICE_PORT", "8080")),
                "endpoints": ["/health", "/auth/health"]
            },
            "ability-service": {
                "host": os.getenv("ABILITY_SERVICE_HOST", "localhost"),
                "port": int(os.getenv("ABILITY_SERVICE_PORT", "8081")),
                "endpoints": ["/health", "/ability/health"]
            },
            "combat-service": {
                "host": os.getenv("COMBAT_SERVICE_HOST", "localhost"),
                "port": int(os.getenv("COMBAT_SERVICE_PORT", "8084")),
                "endpoints": ["/health", "/combat/health"]
            },
            "economy-service": {
                "host": os.getenv("ECONOMY_SERVICE_HOST", "localhost"),
                "port": int(os.getenv("ECONOMY_SERVICE_PORT", "8083")),
                "endpoints": ["/health", "/economy/health"]
            },
            "matchmaking-service": {
                "host": os.getenv("MATCHMAKING_SERVICE_HOST", "localhost"),
                "port": int(os.getenv("MATCHMAKING_SERVICE_PORT", "8082")),
                "endpoints": ["/health", "/matchmaking/health"]
            }
        }

    def check_service(self, service_name: str, config: Dict[str, Any]) -> ServiceHealth:
        """Check health of a single service"""
        service_health = ServiceHealth(service_name=service_name, overall_status=HealthStatus.UNKNOWN, checks=[])

        for endpoint in config["endpoints"]:
            url = f"http://{config['host']}:{config['port']}{endpoint}"
            check = self._check_endpoint(url, service_name, endpoint)
            service_health.checks.append(check)

        # Calculate overall status
        if service_health.checks:
            critical_count = sum(1 for c in service_health.checks if c.status == HealthStatus.CRITICAL)
            warning_count = sum(1 for c in service_health.checks if c.status == HealthStatus.WARNING)
            healthy_count = sum(1 for c in service_health.checks if c.status == HealthStatus.HEALTHY)

            if critical_count > 0:
                service_health.overall_status = HealthStatus.CRITICAL
            elif warning_count > 0:
                service_health.overall_status = HealthStatus.WARNING
            elif healthy_count > 0:
                service_health.overall_status = HealthStatus.HEALTHY

        return service_health

    def _check_endpoint(self, url: str, service_name: str, endpoint: str) -> HealthCheck:
        """Check a single endpoint"""
        check = HealthCheck(
            service_name=service_name,
            endpoint=url,
            status=HealthStatus.UNKNOWN,
            response_time_ms=0.0,
            timestamp=datetime.now().isoformat()
        )

        try:
            start_time = time.time()

            # Create request with timeout
            req = urllib.request.Request(url)
            req.add_header('User-Agent', 'NECPGAME-HealthCheck/1.0')

            with urllib.request.urlopen(req, timeout=self.timeout_seconds) as response:
                response_time = (time.time() - start_time) * 1000
                check.response_time_ms = response_time
                check.status_code = response.getcode()

                # Check response
                if response.getcode() == 200:
                    if response_time < 500:  # Healthy threshold
                        check.status = HealthStatus.HEALTHY
                    elif response_time < 2000:  # Warning threshold
                        check.status = HealthStatus.WARNING
                        check.error_message = ".1f"
                    else:  # Critical threshold
                        check.status = HealthStatus.CRITICAL
                        check.error_message = ".1f"
                else:
                    check.status = HealthStatus.CRITICAL
                    check.error_message = f"HTTP {response.getcode()}"

        except urllib.error.HTTPError as e:
            check.status = HealthStatus.CRITICAL
            check.status_code = e.code
            check.error_message = f"HTTP {e.code}: {e.reason}"
        except urllib.error.URLError as e:
            check.status = HealthStatus.CRITICAL
            check.error_message = f"Connection error: {e.reason}"
        except socket.timeout:
            check.status = HealthStatus.CRITICAL
            check.error_message = f"Timeout after {self.timeout_seconds}s"
        except Exception as e:
            check.status = HealthStatus.CRITICAL
            check.error_message = f"Unexpected error: {str(e)}"

        return check

    def check_tcp_port(self, host: str, port: int) -> bool:
        """Check if TCP port is open"""
        try:
            sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            sock.settimeout(5)
            result = sock.connect_ex((host, port))
            sock.close()
            return result == 0
        except:
            return False

    def run_full_check(self) -> Dict[str, Any]:
        """Run comprehensive health check"""
        print("🔍 NECPGAME Health Check Starting...")
        start_time = time.time()

        results = {}
        for service_name, config in self.services.items():
            print(f"  Checking {service_name}...")

            # Check TCP connectivity first
            port_open = self.check_tcp_port(config["host"], config["port"])
            if not port_open:
                results[service_name] = ServiceHealth(
                    service_name=service_name,
                    overall_status=HealthStatus.CRITICAL,
                    checks=[HealthCheck(
                        service_name=service_name,
                        endpoint=f"{config['host']}:{config['port']}",
                        status=HealthStatus.CRITICAL,
                        response_time_ms=0,
                        error_message="Port not accessible",
                        timestamp=datetime.now().isoformat()
                    )]
                )
                continue

            # Check service health
            service_health = self.check_service(service_name, config)
            results[service_name] = service_health

            # Print status
            status_emoji = {
                HealthStatus.HEALTHY: "✅",
                HealthStatus.WARNING: "⚠️",
                HealthStatus.CRITICAL: "❌",
                HealthStatus.UNKNOWN: "❓"
            }
            print(f"    {status_emoji[service_health.overall_status]} {service_health.overall_status.value}")

        # Calculate summary
        summary = self._calculate_summary(results)

        # Generate report
        report = {
            "timestamp": datetime.now().isoformat(),
            "check_duration_seconds": time.time() - start_time,
            "services": {name: asdict(health) for name, health in results.items()},
            "summary": summary
        }

        # Save report
        with open("health_check_report.json", "w") as f:
            json.dump(report, f, indent=2)

        # Print summary
        self._print_summary(summary)

        return report

    def _calculate_summary(self, results: Dict[str, ServiceHealth]) -> Dict[str, Any]:
        """Calculate health summary"""
        total = len(results)
        healthy = sum(1 for h in results.values() if h.overall_status == HealthStatus.HEALTHY)
        warning = sum(1 for h in results.values() if h.overall_status == HealthStatus.WARNING)
        critical = sum(1 for h in results.values() if h.overall_status == HealthStatus.CRITICAL)

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
            "health_percentage": round((healthy / total * 100), 1) if total > 0 else 0
        }

    def _print_summary(self, summary: Dict[str, Any]):
        """Print human-readable summary"""
        print("\n📊 Health Check Summary:")
        print(f"   Total Services: {summary['total_services']}")
        print(f"   Healthy: {summary['healthy_services']} ✅")
        print(f"   Warning: {summary['warning_services']} ⚠️")
        print(f"   Critical: {summary['critical_services']} ❌")
        print(".1f"
        status_emoji = {
            "healthy": "🟢",
            "warning": "🟡",
            "critical": "🔴",
            "unknown": "⚪"
        }
        print(f"   Overall Status: {status_emoji.get(summary['overall_status'], '❓')} {summary['overall_status'].upper()}")

        if summary['critical_services'] > 0:
            print("\n🚨 Critical Issues Found!")
        elif summary['warning_services'] > 0:
            print("\n⚠️ Performance Issues Detected")

def main():
    """Main entry point"""
    import argparse

    parser = argparse.ArgumentParser(description="NECPGAME Simple Health Checker")
    parser.add_argument("--output", type=str, help="Output file path (default: health_check_report.json)")
    parser.add_argument("--quiet", action="store_true", help="Suppress console output")

    args = parser.parse_args()

    checker = NECPGAMESimpleHealthChecker()
    report = checker.run_full_check()

    if args.output:
        output_file = args.output
    else:
        output_file = "health_check_report.json"

    print(f"\n📄 Report saved to: {output_file}")

    # Exit with appropriate code
    summary = report["summary"]
    if summary["critical_services"] > 0:
        exit(1)  # Critical issues
    elif summary["warning_services"] > 0:
        exit(2)  # Warnings present
    else:
        exit(0)  # All healthy

if __name__ == "__main__":
    main()