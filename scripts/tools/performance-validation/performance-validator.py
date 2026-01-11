#!/usr/bin/env python3
"""
Performance Validation Framework for ogen Services
Issue: #143576311 - [QA] ogen Migration: Comprehensive Testing Strategy & Performance Validation Framework
"""

import asyncio
import aiohttp
import time
import statistics
import json
import sys
from typing import Dict, List, Any
from dataclasses import dataclass
from pathlib import Path
import argparse
import logging

# Configure logging
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')
logger = logging.getLogger(__name__)

@dataclass
class PerformanceMetrics:
    """Performance metrics for a single endpoint"""
    endpoint: str
    method: str
    total_requests: int
    successful_requests: int
    failed_requests: int
    avg_response_time: float
    median_response_time: float
    min_response_time: float
    max_response_time: float
    p95_response_time: float
    p99_response_time: float
    requests_per_second: float
    error_rate: float

@dataclass
class ValidationResult:
    """Validation result for performance targets"""
    endpoint: str
    target_latency: float
    actual_latency: float
    passed: bool
    deviation_percent: float

class OgenPerformanceValidator:
    """Performance validation framework for ogen services"""

    def __init__(self, base_url: str = "http://localhost:8080"):
        self.base_url = base_url.rstrip('/')
        self.session = None

    async def __aenter__(self):
        self.session = aiohttp.ClientSession()
        return self

    async def __aexit__(self, exc_type, exc_val, exc_tb):
        if self.session:
            await self.session.close()

    async def measure_endpoint(self, endpoint: str, method: str = "GET",
                             payload: Dict = None, headers: Dict = None,
                             num_requests: int = 100, concurrency: int = 10) -> PerformanceMetrics:
        """Measure performance of a single endpoint"""

        url = f"{self.base_url}{endpoint}"
        response_times = []

        async def single_request():
            start_time = time.time()
            try:
                if method.upper() == "GET":
                    async with self.session.get(url, headers=headers) as response:
                        await response.text()
                        end_time = time.time()
                        response_times.append(end_time - start_time)
                        return response.status == 200
                elif method.upper() == "POST":
                    async with self.session.post(url, json=payload, headers=headers) as response:
                        await response.text()
                        end_time = time.time()
                        response_times.append(end_time - start_time)
                        return response.status == 200
                else:
                    raise ValueError(f"Unsupported method: {method}")
            except Exception as e:
                logger.error(f"Request failed: {e}")
                end_time = time.time()
                response_times.append(end_time - start_time)
                return False

        # Execute requests with concurrency control
        semaphore = asyncio.Semaphore(concurrency)
        start_time = time.time()

        async def bounded_request():
            async with semaphore:
                return await single_request()

        tasks = [bounded_request() for _ in range(num_requests)]
        results = await asyncio.gather(*tasks)

        total_time = time.time() - start_time

        successful_requests = sum(1 for r in results if r)
        failed_requests = len(results) - successful_requests

        if response_times:
            sorted_times = sorted(response_times)
            metrics = PerformanceMetrics(
                endpoint=endpoint,
                method=method,
                total_requests=num_requests,
                successful_requests=successful_requests,
                failed_requests=failed_requests,
                avg_response_time=statistics.mean(response_times) * 1000,  # Convert to ms
                median_response_time=statistics.median(response_times) * 1000,
                min_response_time=min(response_times) * 1000,
                max_response_time=max(response_times) * 1000,
                p95_response_time=sorted_times[int(len(sorted_times) * 0.95)] * 1000,
                p99_response_time=sorted_times[int(len(sorted_times) * 0.99)] * 1000,
                requests_per_second=num_requests / total_time,
                error_rate=(failed_requests / num_requests) * 100
            )
        else:
            metrics = PerformanceMetrics(
                endpoint=endpoint,
                method=method,
                total_requests=num_requests,
                successful_requests=0,
                failed_requests=num_requests,
                avg_response_time=0,
                median_response_time=0,
                min_response_time=0,
                max_response_time=0,
                p95_response_time=0,
                p99_response_time=0,
                requests_per_second=0,
                error_rate=100
            )

        return metrics

    def validate_performance_targets(self, metrics: PerformanceMetrics,
                                   target_latency: float = 50.0,
                                   max_error_rate: float = 1.0) -> ValidationResult:
        """Validate performance against targets"""

        actual_latency = metrics.p95_response_time  # Use P95 for conservative validation
        deviation_percent = ((actual_latency - target_latency) / target_latency) * 100

        passed = (actual_latency <= target_latency and
                 metrics.error_rate <= max_error_rate)

        return ValidationResult(
            endpoint=metrics.endpoint,
            target_latency=target_latency,
            actual_latency=actual_latency,
            passed=passed,
            deviation_percent=deviation_percent
        )

    def generate_report(self, metrics: List[PerformanceMetrics],
                       validations: List[ValidationResult]) -> str:
        """Generate comprehensive performance report"""

        report = []
        report.append("=" * 80)
        report.append("OGEN SERVICE PERFORMANCE VALIDATION REPORT")
        report.append("=" * 80)
        report.append(f"Generated: {time.strftime('%Y-%m-%d %H:%M:%S')}")
        report.append(f"Service URL: {self.base_url}")
        report.append("")

        # Summary
        total_endpoints = len(metrics)
        passed_validations = sum(1 for v in validations if v.passed)
        failed_validations = total_endpoints - passed_validations

        report.append("SUMMARY")
        report.append("-" * 40)
        report.append(f"Total Endpoints Tested: {total_endpoints}")
        report.append(f"Passed Validations: {passed_validations}")
        report.append(f"Failed Validations: {failed_validations}")
        report.append(".1f")
        report.append("")

        # Detailed Results
        report.append("DETAILED RESULTS")
        report.append("-" * 40)

        for i, (metric, validation) in enumerate(zip(metrics, validations)):
            status = "PASS" if validation.passed else "FAIL"
            report.append(f"\n{i+1}. {metric.endpoint} ({metric.method})")
            report.append(f"   Status: {status}")
            report.append(f"   Target Latency: {validation.target_latency:.1f}ms")
            report.append(f"   Actual P95 Latency: {validation.actual_latency:.1f}ms")
            report.append(f"   Deviation: {validation.deviation_percent:+.1f}%")
            report.append(f"   Error Rate: {metric.error_rate:.2f}%")
            report.append(f"   Requests/sec: {metric.requests_per_second:.1f}")
            report.append(f"   Total Requests: {metric.total_requests}")
            report.append(f"   Successful: {metric.successful_requests}")

        # Performance Recommendations
        report.append("\nPERFORMANCE RECOMMENDATIONS")
        report.append("-" * 40)

        failed_endpoints = [v for v in validations if not v.passed]
        if failed_endpoints:
            report.append("Failed endpoints requiring attention:")
            for validation in failed_endpoints:
                if validation.actual_latency > validation.target_latency:
                    report.append(f"  - {validation.endpoint}: High latency ({validation.actual_latency:.1f}ms > {validation.target_latency:.1f}ms)")
                else:
                    report.append(f"  - {validation.endpoint}: High error rate")
        else:
            report.append("All endpoints meet performance targets!")

        # JSON export for CI/CD integration
        json_data = {
            "timestamp": time.time(),
            "service_url": self.base_url,
            "summary": {
                "total_endpoints": total_endpoints,
                "passed": passed_validations,
                "failed": failed_validations,
                "success_rate": (passed_validations / total_endpoints) * 100 if total_endpoints > 0 else 0
            },
            "results": [
                {
                    "endpoint": m.endpoint,
                    "method": m.method,
                    "passed": v.passed,
                    "target_latency": v.target_latency,
                    "actual_latency": v.actual_latency,
                    "error_rate": m.error_rate,
                    "requests_per_second": m.requests_per_second
                }
                for m, v in zip(metrics, validations)
            ]
        }

        # Save JSON report
        json_file = Path("performance-report.json")
        with open(json_file, 'w') as f:
            json.dump(json_data, f, indent=2)

        report.append(f"\nJSON Report saved: {json_file}")

        return "\n".join(report)

async def validate_support_sla_service(base_url: str = "http://localhost:8080") -> str:
    """Validate Support SLA Service performance"""

    endpoints = [
        {
            "endpoint": "/api/v1/sla/health",
            "method": "GET",
            "target_latency": 10.0,  # Health checks should be fast
            "description": "Health Check Endpoint"
        },
        {
            "endpoint": "/api/v1/sla/ticket/test-123/status",
            "method": "GET",
            "target_latency": 50.0,
            "description": "Ticket SLA Status"
        },
        {
            "endpoint": "/api/v1/sla/policies",
            "method": "GET",
            "target_latency": 50.0,
            "description": "SLA Policies List"
        },
        {
            "endpoint": "/api/v1/sla/analytics/summary",
            "method": "GET",
            "target_latency": 100.0,  # Analytics can be slower
            "description": "SLA Analytics Summary"
        },
        {
            "endpoint": "/api/v1/sla/alerts/active",
            "method": "GET",
            "target_latency": 50.0,
            "description": "Active SLA Alerts"
        }
    ]

    async with OgenPerformanceValidator(base_url) as validator:
        metrics = []
        validations = []

        logger.info("Starting Support SLA Service performance validation...")

        for endpoint_config in endpoints:
            logger.info(f"Testing {endpoint_config['description']}: {endpoint_config['endpoint']}")

            metric = await validator.measure_endpoint(
                endpoint=endpoint_config["endpoint"],
                method=endpoint_config["method"],
                num_requests=100,
                concurrency=10
            )

            validation = validator.validate_performance_targets(
                metric,
                target_latency=endpoint_config["target_latency"]
            )

            metrics.append(metric)
            validations.append(validation)

            status = "PASS" if validation.passed else "FAIL"
            logger.info(".1f")

        report = validator.generate_report(metrics, validations)
        return report

async def validate_gameplay_service(base_url: str = "http://localhost:8080") -> str:
    """Validate Gameplay Service performance for MMOFPS requirements"""

    endpoints = [
        {
            "endpoint": "/api/v1/gameplay/health",
            "method": "GET",
            "target_latency": 10.0,  # Critical health checks
            "description": "Gameplay Health Check"
        },
        {
            "endpoint": "/api/v1/gameplay/player/test-player/stats",
            "method": "GET",
            "target_latency": 20.0,  # Player stats must be fast
            "description": "Player Stats Retrieval"
        },
        {
            "endpoint": "/api/v1/gameplay/guilds/active",
            "method": "GET",
            "target_latency": 30.0,  # Guild queries need to be responsive
            "description": "Active Guilds List"
        },
        {
            "endpoint": "/api/v1/gameplay/quests/available",
            "method": "GET",
            "target_latency": 25.0,  # Quest availability checks
            "description": "Available Quests"
        },
        {
            "endpoint": "/api/v1/gameplay/effects/active",
            "method": "GET",
            "target_latency": 15.0,  # Real-time effects must be instant
            "description": "Active Effects Check"
        },
        {
            "endpoint": "/api/v1/gameplay/combat/session/test-session/status",
            "method": "GET",
            "target_latency": 10.0,  # Combat status critical for FPS
            "description": "Combat Session Status"
        },
        {
            "endpoint": "/api/v1/gameplay/leaderboards/top-players",
            "method": "GET",
            "target_latency": 50.0,  # Leaderboards can be slightly slower
            "description": "Top Players Leaderboard"
        },
        {
            "endpoint": "/api/v1/gameplay/achievements/recent",
            "method": "GET",
            "target_latency": 30.0,  # Achievement queries
            "description": "Recent Achievements"
        }
    ]

    async with OgenPerformanceValidator(base_url) as validator:
        metrics = []
        validations = []

        logger.info("Starting Gameplay Service performance validation...")
        logger.info("MMOFPS requirements: P95 latency must be <50ms for critical endpoints")

        for endpoint_config in endpoints:
            logger.info(f"Testing {endpoint_config['description']}: {endpoint_config['endpoint']}")

            # Use higher concurrency for gameplay service (more users)
            metric = await validator.measure_endpoint(
                endpoint=endpoint_config["endpoint"],
                method=endpoint_config["method"],
                num_requests=200,  # Higher load for MMOFPS
                concurrency=20     # Higher concurrency
            )

            validation = validator.validate_performance_targets(
                metric,
                target_latency=endpoint_config["target_latency"],
                max_error_rate=1.0  # 1% max error rate for gameplay
            )

            metrics.append(metric)
            validations.append(validation)

            status = "PASS" if validation.passed else "FAIL"
            logger.info(".1f")

        report = validator.generate_report(metrics, validations)
        return report

async def validate_all_services(base_url: str = "http://localhost:8080") -> str:
    """Validate all services performance"""

    services = ["support-sla", "gameplay"]
    all_metrics = []
    all_validations = []

    for service in services:
        logger.info(f"Validating {service} service...")

        if service == "support-sla":
            report = await validate_support_sla_service(base_url)
        elif service == "gameplay":
            report = await validate_gameplay_service(base_url)
        else:
            continue

        # Parse report to extract metrics (simplified - in real implementation
        # would parse the report string or modify to return structured data)
        logger.info(f"{service} validation completed")

    # Generate combined report
    if all_metrics and all_validations:
        validator = OgenPerformanceValidator(base_url)
        combined_report = validator.generate_report(all_metrics, all_validations)
        return f"Combined Services Validation Report:\n{combined_report}"
    else:
        return "Multi-service validation completed - check individual service logs above"

async def main():
    parser = argparse.ArgumentParser(description="Ogen Service Performance Validator")
    parser.add_argument("--url", default="http://localhost:8080",
                       help="Base URL of the service to test")
    parser.add_argument("--service", default="support-sla",
                       choices=["support-sla", "gameplay", "all"],
                       help="Service to validate (all includes support-sla + gameplay)")

    args = parser.parse_args()

    try:
        if args.service == "support-sla":
            report = await validate_support_sla_service(args.url)
        elif args.service == "gameplay":
            report = await validate_gameplay_service(args.url)
        elif args.service == "all":
            report = await validate_all_services(args.url)
        else:
            report = f"Unknown service: {args.service}"

        print(report)

        # Exit with error code if validation failed
        if "Failed Validations: 0" not in report:
            sys.exit(1)

    except Exception as e:
        logger.error(f"Validation failed: {e}")
        sys.exit(1)

if __name__ == "__main__":
    asyncio.run(main())
