#!/usr/bin/env python3
"""
Load Testing Scenarios for ogen Services
Issue: #143576311 - [QA] ogen Migration: Comprehensive Testing Strategy & Performance Validation Framework
"""

import asyncio
import aiohttp
import json
import time
import statistics
from typing import Dict, List, Any, Optional
from dataclasses import dataclass, asdict
from pathlib import Path
import argparse
import logging

logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')
logger = logging.getLogger(__name__)

@dataclass
class LoadTestResult:
    """Result of a load test scenario"""
    scenario_name: str
    total_requests: int
    successful_requests: int
    failed_requests: int
    total_time: float
    requests_per_second: float
    avg_response_time: float
    median_response_time: float
    p95_response_time: float
    p99_response_time: float
    min_response_time: float
    max_response_time: float
    error_rate: float
    timestamp: float

    # Advanced performance metrics
    throughput_stability: float = 0.0  # Coefficient of variation in response times
    cpu_usage_avg: float = 0.0         # Average CPU usage during test
    memory_usage_avg: float = 0.0      # Average memory usage during test
    network_io_mb: float = 0.0         # Network I/O in MB during test
    connection_pool_size: int = 0      # Final connection pool size
    goroutines_count: int = 0          # Estimated goroutine count

    # Reliability metrics
    consecutive_failures: int = 0      # Max consecutive failed requests
    recovery_time_ms: float = 0.0      # Time to recover from failures
    circuit_breaker_trips: int = 0     # Circuit breaker activations

    # Business metrics
    user_experience_score: float = 0.0 # UX score based on response times
    sla_compliance_pct: float = 0.0    # Percentage meeting SLA requirements
    business_impact_score: float = 0.0 # Business impact assessment

    def to_dict(self) -> Dict[str, Any]:
        return asdict(self)

class OgenLoadTester:
    """Load testing framework for ogen services"""

    def __init__(self, base_url: str = "http://localhost:8080"):
        self.base_url = base_url.rstrip('/')
        self.session: Optional[aiohttp.ClientSession] = None

    async def __aenter__(self):
        self.session = aiohttp.ClientSession()
        return self

    async def __aexit__(self, exc_type, exc_val, exc_tb):
        if self.session:
            await self.session.close()

    def collect_system_metrics(self) -> Dict[str, float]:
        """Collect basic system metrics (simplified implementation)"""
        # In a real implementation, this would use psutil or similar
        # For now, return mock data
        return {
            "cpu_usage_percent": 45.2,
            "memory_usage_mb": 256.8,
            "network_io_mb": 12.3,
            "connection_pool_size": 25,
            "goroutines_count": 150,
        }

    def calculate_ux_score(self, avg_response_time: float, p95_response_time: float, error_rate: float) -> float:
        """Calculate user experience score based on performance metrics"""
        # UX score from 0-100, where 100 is perfect
        score = 100.0

        # Penalize slow average response time (>200ms = -20 points, >500ms = -50 points)
        if avg_response_time > 500:
            score -= 50
        elif avg_response_time > 200:
            score -= 20

        # Penalize slow P95 response time (>1000ms = -15 points, >2000ms = -35 points)
        if p95_response_time > 2000:
            score -= 35
        elif p95_response_time > 1000:
            score -= 15

        # Penalize high error rate (>5% = -25 points, >10% = -50 points)
        if error_rate > 10:
            score -= 50
        elif error_rate > 5:
            score -= 25

        return max(0.0, min(100.0, score))

    def calculate_sla_compliance(self, avg_response_time: float, p95_response_time: float) -> float:
        """Calculate SLA compliance percentage"""
        # Assume SLA: avg < 300ms, P95 < 1000ms
        sla_met = avg_response_time <= 300 and p95_response_time <= 1000
        return 100.0 if sla_met else 0.0

    def calculate_business_impact(self, avg_response_time: float, error_rate: float) -> float:
        """Calculate business impact score"""
        # Business impact from 0-100, where 100 is no impact
        impact = 100.0

        # High response times reduce user engagement
        if avg_response_time > 1000:
            impact -= 40
        elif avg_response_time > 500:
            impact -= 20

        # Errors cause user abandonment
        if error_rate > 5:
            impact -= 30
        elif error_rate > 1:
            impact -= 10

        return max(0.0, impact)

    async def run_gradual_load_scenario(self, scenario_name: str, endpoint: str, method: str,
                                      total_requests: int, concurrency_levels: List[int],
                                      description: str = "") -> LoadTestResult:
        """Run a gradual load increase scenario"""
        logger.info(f"Starting gradual load scenario '{scenario_name}': {total_requests} total requests")

        all_response_times = []
        all_results = []
        total_time = 0

        requests_per_level = total_requests // len(concurrency_levels)

        for i, concurrency in enumerate(concurrency_levels):
            logger.info(f"Level {i+1}/{len(concurrency_levels)}: {concurrency} concurrent connections")

            level_result = await self.run_scenario(
                scenario_name=f"{scenario_name}_level_{i+1}",
                endpoint=endpoint,
                method=method,
                requests=requests_per_level,
                concurrency=concurrency,
                warm_up=False,
                description=f"{description} - Level {i+1} ({concurrency} concurrent)"
            )

            all_response_times.extend([t/1000 for t in [level_result.avg_response_time] * level_result.total_requests])  # Convert back to seconds
            all_results.extend([True] * level_result.successful_requests + [False] * level_result.failed_requests)
            total_time += level_result.total_time

        # Calculate aggregated metrics
        successful_requests = sum(1 for r in all_results if r)
        failed_requests = len(all_results) - successful_requests

        if all_response_times:
            sorted_times = sorted(all_response_times)
            result = LoadTestResult(
                scenario_name=scenario_name,
                total_requests=total_requests,
                successful_requests=successful_requests,
                failed_requests=failed_requests,
                total_time=total_time,
                requests_per_second=total_requests / total_time if total_time > 0 else 0,
                avg_response_time=statistics.mean(all_response_times) * 1000,
                median_response_time=statistics.median(all_response_times) * 1000,
                p95_response_time=sorted_times[int(len(sorted_times) * 0.95)] * 1000,
                p99_response_time=sorted_times[int(len(sorted_times) * 0.99)] * 1000,
                min_response_time=min(all_response_times) * 1000,
                max_response_time=max(all_response_times) * 1000,
                error_rate=(failed_requests / total_requests) * 100,
                timestamp=time.time(),
                throughput_stability=statistics.stdev(all_response_times) / statistics.mean(all_response_times) if len(all_response_times) > 1 else 0.0,
            )
        else:
            result = LoadTestResult(
                scenario_name=scenario_name,
                total_requests=total_requests,
                successful_requests=0,
                failed_requests=total_requests,
                total_time=total_time,
                requests_per_second=0,
                avg_response_time=0,
                median_response_time=0,
                p95_response_time=0,
                p99_response_time=0,
                min_response_time=0,
                max_response_time=0,
                error_rate=100,
                timestamp=time.time()
            )

        return result

    async def run_scenario(self, scenario: Dict[str, Any]) -> LoadTestResult:
        """Run a specific load testing scenario"""

        name = scenario["name"]
        endpoint = scenario["endpoint"]
        method = scenario.get("method", "GET")
        num_requests = scenario["requests"]
        concurrency = scenario["concurrency"]
        payload = scenario.get("payload")

        url = f"{self.base_url}{endpoint}"
        response_times = []
        errors = []

        logger.info(f"Starting scenario '{name}': {num_requests} requests, {concurrency} concurrent")

        async def single_request():
            try:
                start_time = time.time()

                if method.upper() == "GET":
                    async with self.session.get(url) as response:
                        await response.text()
                        end_time = time.time()
                        response_times.append(end_time - start_time)
                        return response.status == 200

                elif method.upper() == "POST":
                    headers = {'Content-Type': 'application/json'}
                    async with self.session.post(url, json=payload, headers=headers) as response:
                        await response.text()
                        end_time = time.time()
                        response_times.append(end_time - start_time)
                        return response.status == 200

                else:
                    raise ValueError(f"Unsupported method: {method}")

            except Exception as e:
                end_time = time.time()
                response_times.append(end_time - start_time)
                errors.append(str(e))
                return False

        # Warm-up phase if specified
        if scenario.get("warm_up", False):
            logger.info("Running warm-up phase...")
            warm_up_tasks = [single_request() for _ in range(min(50, num_requests // 10))]
            await asyncio.gather(*warm_up_tasks)
            await asyncio.sleep(1)  # Brief pause

        # Main test phase
        start_time = time.time()

        semaphore = asyncio.Semaphore(concurrency)
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
            avg_time = statistics.mean(response_times) * 1000  # Convert to ms
            p95_time = sorted_times[int(len(sorted_times) * 0.95)] * 1000

            # Calculate advanced metrics
            throughput_stability = statistics.stdev(response_times) / statistics.mean(response_times) if len(response_times) > 1 else 0.0

            # Collect system metrics
            system_metrics = self.collect_system_metrics()

            # Calculate business metrics
            user_experience_score = self.calculate_ux_score(avg_time, p95_time, error_rate)
            sla_compliance_pct = self.calculate_sla_compliance(avg_time, p95_time)

            result = LoadTestResult(
                scenario_name=name,
                total_requests=num_requests,
                successful_requests=successful_requests,
                failed_requests=failed_requests,
                total_time=total_time,
                requests_per_second=num_requests / total_time,
                avg_response_time=avg_time,
                median_response_time=statistics.median(response_times) * 1000,
                p95_response_time=p95_time,
                p99_response_time=sorted_times[int(len(sorted_times) * 0.99)] * 1000,
                min_response_time=min(response_times) * 1000,
                max_response_time=max(response_times) * 1000,
                error_rate=(failed_requests / num_requests) * 100,
                timestamp=time.time(),

                # Advanced metrics
                throughput_stability=throughput_stability,
                cpu_usage_avg=system_metrics.get("cpu_usage_percent", 0.0),
                memory_usage_avg=system_metrics.get("memory_usage_mb", 0.0),
                network_io_mb=system_metrics.get("network_io_mb", 0.0),
                connection_pool_size=int(system_metrics.get("connection_pool_size", 0)),
                goroutines_count=int(system_metrics.get("goroutines_count", 0)),

                # Business metrics
                user_experience_score=user_experience_score,
                sla_compliance_pct=sla_compliance_pct,
                business_impact_score=self.calculate_business_impact(avg_time, error_rate),
            )
        else:
            result = LoadTestResult(
                scenario_name=name,
                total_requests=num_requests,
                successful_requests=0,
                failed_requests=num_requests,
                total_time=total_time,
                requests_per_second=0,
                avg_response_time=0,
                median_response_time=0,
                p95_response_time=0,
                p99_response_time=0,
                min_response_time=0,
                max_response_time=0,
                error_rate=100,
                timestamp=time.time()
            )

        logger.info(".2f")
        return result

async def run_stress_test_scenarios(base_url: str = "http://localhost:8080") -> List[LoadTestResult]:
    """Run extreme stress testing scenarios to find system breaking points"""

    scenarios = [
        {
            "name": "extreme_concurrency_burst",
            "endpoint": "/health",
            "method": "GET",
            "requests": 10000,
            "concurrency": 500,
            "warm_up": False,
            "description": "Extreme concurrency to test connection limits and resource exhaustion"
        },
        {
            "name": "memory_pressure_test",
            "endpoint": "/api/v1/gameplay/affixes/active",
            "method": "GET",
            "requests": 50000,
            "concurrency": 200,
            "warm_up": True,
            "description": "High memory pressure test with large concurrent dataset retrieval"
        },
        {
            "name": "network_saturation_test",
            "endpoint": "/api/v1/analytics/affixes/combinations",
            "method": "GET",
            "requests": 20000,
            "concurrency": 150,
            "warm_up": True,
            "description": "Network saturation with large response payloads"
        },
        {
            "name": "database_connection_exhaustion",
            "endpoint": "/api/v1/gameplay/affixes",
            "method": "GET",
            "params": {"limit": "100", "offset": "0"},
            "requests": 30000,
            "concurrency": 100,
            "warm_up": True,
            "description": "Test database connection pool exhaustion under heavy load"
        },
        {
            "name": "circuit_breaker_stress",
            "endpoint": "/api/v1/gameplay/affixes/NONEXISTENT",
            "method": "GET",
            "requests": 15000,
            "concurrency": 300,
            "warm_up": False,
            "description": "Circuit breaker activation stress test with 404 responses"
        },
        {
            "name": "gradual_load_increase",
            "endpoint": "/health",
            "method": "GET",
            "requests": 25000,
            "concurrency": [10, 25, 50, 100, 200, 300],  # Gradual increase
            "warm_up": False,
            "description": "Gradual load increase to find performance degradation points"
        },
        {
            "name": "mixed_workload_stress",
            "endpoint": "/api/v1/gameplay/affixes",
            "method": "GET",
            "requests": 15000,
            "concurrency": 100,
            "warm_up": True,
            "data": {
                "name": "Stress Test Affix",
                "description": "Created under extreme load conditions",
                "effects": [
                    {"type": "damage_modifier", "value": 2.0, "target": "all_damage"},
                    {"type": "health_modifier", "value": 1.5, "target": "max_health"},
                    {"type": "speed_modifier", "value": 1.3, "target": "movement_speed"}
                ],
                "rarity": "legendary",
                "tier": 5
            },
            "description": "Mixed read/write workload under stress conditions"
        },
        {
            "name": "timeout_boundary_test",
            "endpoint": "/api/v1/gameplay/affixes",
            "method": "POST",
            "requests": 5000,
            "concurrency": 50,
            "warm_up": True,
            "data": {
                "name": "Timeout Test Affix",
                "description": "Testing timeout boundaries under load",
                "effects": [{"type": "complex_calculation", "value": 999999, "target": "performance_test"}],
                "rarity": "epic",
                "tier": 3
            },
            "description": "Test timeout handling and boundary conditions"
        }
    ]

    tester = OgenLoadTester(base_url)
    results = []

    async with tester:
        for scenario in scenarios:
            logger.info(f"Running stress test scenario: {scenario['name']}")
            try:
                # Handle gradual load increase scenario specially
                if scenario["name"] == "gradual_load_increase":
                    result = await tester.run_gradual_load_scenario(
                        scenario_name=scenario["name"],
                        endpoint=scenario["endpoint"],
                        method=scenario["method"],
                        total_requests=scenario["requests"],
                        concurrency_levels=scenario["concurrency"],
                        description=scenario.get("description", "")
                    )
                else:
                    result = await tester.run_scenario(
                        scenario_name=scenario["name"],
                        endpoint=scenario["endpoint"],
                        method=scenario["method"],
                        requests=scenario["requests"],
                        concurrency=scenario["concurrency"],
                        warm_up=scenario.get("warm_up", True),
                        data=scenario.get("data"),
                        params=scenario.get("params"),
                        description=scenario.get("description", "")
                    )
                results.append(result)
                logger.info(".2f")
            except Exception as e:
                logger.error(f"Stress test scenario {scenario['name']} failed: {e}")
                results.append(LoadTestResult(
                    scenario_name=scenario["name"],
                    total_requests=scenario["requests"],
                    successful_requests=0,
                    failed_requests=scenario["requests"],
                    total_time=0,
                    requests_per_second=0,
                    avg_response_time=0,
                    median_response_time=0,
                    p95_response_time=0,
                    p99_response_time=0,
                    min_response_time=0,
                    max_response_time=0,
                    error_rate=100,
                    timestamp=time.time()
                ))

    return results

async def run_all_services_scenarios(base_url: str = "http://localhost:8080") -> List[LoadTestResult]:
    """Run load testing scenarios for all available services"""

    all_results = []

    # Test gameplay service
    logger.info("Starting gameplay service load testing...")
    try:
        gameplay_results = await run_gameplay_scenarios(base_url.replace(":8080", ":8081"))  # Assuming gameplay runs on 8081
        all_results.extend(gameplay_results)
        logger.info(f"Gameplay service testing completed: {len(gameplay_results)} scenarios")
    except Exception as e:
        logger.error(f"Gameplay service testing failed: {e}")

    # Test support SLA service
    logger.info("Starting support SLA service load testing...")
    try:
        sla_results = await run_support_sla_scenarios(base_url)
        all_results.extend(sla_results)
        logger.info(f"Support SLA service testing completed: {len(sla_results)} scenarios")
    except Exception as e:
        logger.error(f"Support SLA service testing failed: {e}")

    # Could add more services here as they become available
    # - auction service
    # - trading service
    # - tournament service
    # - guild service
    # etc.

    logger.info(f"All services testing completed: {len(all_results)} total scenarios")
    return all_results

async def run_gameplay_scenarios(base_url: str = "http://localhost:8080") -> List[LoadTestResult]:
    """Run all Gameplay service load testing scenarios"""

    scenarios = [
        {
            "name": "health_check_warm_up",
            "endpoint": "/health",
            "method": "GET",
            "requests": 100,
            "concurrency": 5,
            "warm_up": False,
            "description": "Warm-up health checks for gameplay service"
        },
        {
            "name": "health_check_normal_load",
            "endpoint": "/health",
            "method": "GET",
            "requests": 1000,
            "concurrency": 20,
            "warm_up": True,
            "description": "Normal load health checks"
        },
        {
            "name": "active_affixes_retrieval",
            "endpoint": "/api/v1/gameplay/affixes/active",
            "method": "GET",
            "requests": 2000,
            "concurrency": 25,
            "warm_up": True,
            "description": "Retrieve active affixes under load"
        },
        {
            "name": "affix_details_lookup",
            "endpoint": "/api/v1/gameplay/affixes/AFFIX-001",
            "method": "GET",
            "requests": 1500,
            "concurrency": 20,
            "warm_up": True,
            "description": "Individual affix details lookup"
        },
        {
            "name": "rotation_current_view",
            "endpoint": "/api/v1/gameplay/affixes/rotation/current",
            "method": "GET",
            "requests": 1000,
            "concurrency": 15,
            "warm_up": True,
            "description": "Current rotation information retrieval"
        },
        {
            "name": "rotation_history_load",
            "endpoint": "/api/v1/gameplay/affixes/rotation/history",
            "method": "GET",
            "params": {"limit": "10", "offset": "0"},
            "requests": 800,
            "concurrency": 12,
            "warm_up": True,
            "description": "Rotation history with pagination"
        },
        {
            "name": "instance_affixes_load",
            "endpoint": "/api/v1/gameplay/instances/INSTANCE-123/affixes",
            "method": "GET",
            "requests": 1200,
            "concurrency": 18,
            "warm_up": True,
            "description": "Instance-specific affix retrieval"
        },
        {
            "name": "affix_popularity_analytics",
            "endpoint": "/api/v1/analytics/affixes/popularity",
            "method": "GET",
            "requests": 300,
            "concurrency": 8,
            "warm_up": True,
            "description": "Affix popularity analytics under load"
        },
        {
            "name": "difficulty_analytics_load",
            "endpoint": "/api/v1/analytics/affixes/difficulty",
            "method": "GET",
            "requests": 250,
            "concurrency": 6,
            "warm_up": True,
            "description": "Difficulty analytics retrieval"
        },
        {
            "name": "combination_analytics",
            "endpoint": "/api/v1/analytics/affixes/combinations",
            "method": "GET",
            "requests": 200,
            "concurrency": 5,
            "warm_up": True,
            "description": "Affix combination analytics"
        },
        {
            "name": "affix_creation_burst",
            "endpoint": "/api/v1/gameplay/affixes",
            "method": "POST",
            "data": {
                "name": "Load Test Affix",
                "description": "Temporary affix for load testing",
                "effects": [
                    {
                        "type": "damage_modifier",
                        "value": 1.1,
                        "target": "player_damage"
                    }
                ],
                "rarity": "common",
                "tier": 1
            },
            "requests": 100,
            "concurrency": 3,
            "warm_up": True,
            "description": "Affix creation under controlled load"
        },
        {
            "name": "affix_update_operations",
            "endpoint": "/api/v1/gameplay/affixes/AFFIX-001",
            "method": "PUT",
            "data": {
                "name": "Updated Load Test Affix",
                "description": "Updated description for load testing"
            },
            "requests": 150,
            "concurrency": 4,
            "warm_up": True,
            "description": "Affix update operations"
        },
        {
            "name": "affixes_listing_pagination",
            "endpoint": "/api/v1/gameplay/affixes",
            "method": "GET",
            "params": {"limit": "20", "offset": "0", "rarity": "common"},
            "requests": 600,
            "concurrency": 10,
            "warm_up": True,
            "description": "Affixes listing with filtering and pagination"
        },
        {
            "name": "peak_load_health_check",
            "endpoint": "/health",
            "method": "GET",
            "requests": 5000,
            "concurrency": 100,
            "warm_up": True,
            "description": "Peak load health checks for stress testing"
        },
        {
            "name": "mixed_operations_burst",
            "endpoint": "/api/v1/gameplay/affixes/active",
            "method": "GET",
            "requests": 3000,
            "concurrency": 30,
            "warm_up": True,
            "description": "Mixed read operations under high concurrency"
        }
    ]

    tester = OgenLoadTester(base_url)
    results = []

    async with tester:
        for scenario in scenarios:
            logger.info(f"Running scenario: {scenario['name']}")
            try:
                result = await tester.run_scenario(
                    scenario_name=scenario["name"],
                    endpoint=scenario["endpoint"],
                    method=scenario["method"],
                    requests=scenario["requests"],
                    concurrency=scenario["concurrency"],
                    warm_up=scenario.get("warm_up", True),
                    data=scenario.get("data"),
                    params=scenario.get("params"),
                    description=scenario.get("description", "")
                )
                results.append(result)
                logger.info(".2f")
            except Exception as e:
                logger.error(f"Scenario {scenario['name']} failed: {e}")
                # Add failed result
                results.append(LoadTestResult(
                    scenario_name=scenario["name"],
                    total_requests=scenario["requests"],
                    successful_requests=0,
                    failed_requests=scenario["requests"],
                    total_time=0,
                    requests_per_second=0,
                    avg_response_time=0,
                    median_response_time=0,
                    p95_response_time=0,
                    p99_response_time=0,
                    min_response_time=0,
                    max_response_time=0,
                    error_rate=100,
                    timestamp=time.time()
                ))

    return results

async def run_support_sla_scenarios(base_url: str = "http://localhost:8080") -> List[LoadTestResult]:
    """Run all Support SLA service load testing scenarios"""

    scenarios = [
        {
            "name": "health_check_warm_up",
            "endpoint": "/api/v1/sla/health",
            "method": "GET",
            "requests": 100,
            "concurrency": 5,
            "warm_up": False,
            "description": "Warm-up health checks"
        },
        {
            "name": "health_check_normal_load",
            "endpoint": "/api/v1/sla/health",
            "method": "GET",
            "requests": 1000,
            "concurrency": 20,
            "warm_up": True,
            "description": "Normal load health checks"
        },
        {
            "name": "health_check_peak_load",
            "endpoint": "/api/v1/sla/health",
            "method": "GET",
            "requests": 5000,
            "concurrency": 50,
            "warm_up": True,
            "description": "Peak load health checks"
        },
        {
            "name": "ticket_sla_status_load",
            "endpoint": "/api/v1/sla/ticket/TICKET-123/status",
            "method": "GET",
            "requests": 500,
            "concurrency": 10,
            "warm_up": True,
            "description": "Ticket SLA status retrieval under load"
        },
        {
            "name": "sla_policies_load",
            "endpoint": "/api/v1/sla/policies",
            "method": "GET",
            "requests": 300,
            "concurrency": 15,
            "warm_up": True,
            "description": "SLA policies listing under load"
        },
        {
            "name": "analytics_summary_load",
            "endpoint": "/api/v1/sla/analytics/summary?period=weekly",
            "method": "GET",
            "requests": 200,
            "concurrency": 8,
            "warm_up": True,
            "description": "Analytics summary under load"
        },
        {
            "name": "mixed_endpoints_load",
            "endpoint": "/api/v1/sla/health",  # Will be overridden in test
            "method": "GET",
            "requests": 2000,
            "concurrency": 30,
            "warm_up": True,
            "mixed_endpoints": True,
            "description": "Mixed endpoints load test"
        }
    ]

    results = []

    async with OgenLoadTester(base_url) as tester:
        for scenario in scenarios:
            try:
                if scenario.get("mixed_endpoints"):
                    # Special handling for mixed endpoints
                    result = await run_mixed_endpoints_test(tester, scenario)
                else:
                    result = await tester.run_scenario(scenario)

                results.append(result)

                # Brief pause between scenarios
                await asyncio.sleep(2)

            except Exception as e:
                logger.error(f"Scenario '{scenario['name']}' failed: {e}")
                # Create failed result
                failed_result = LoadTestResult(
                    scenario_name=scenario["name"],
                    total_requests=scenario["requests"],
                    successful_requests=0,
                    failed_requests=scenario["requests"],
                    total_time=0,
                    requests_per_second=0,
                    avg_response_time=0,
                    median_response_time=0,
                    p95_response_time=0,
                    p99_response_time=0,
                    min_response_time=0,
                    max_response_time=0,
                    error_rate=100,
                    timestamp=time.time()
                )
                results.append(failed_result)

    return results

async def run_mixed_endpoints_test(tester: OgenLoadTester, scenario: Dict[str, Any]) -> LoadTestResult:
    """Run mixed endpoints load test"""

    endpoints = [
        "/api/v1/sla/health",
        "/api/v1/sla/policies",
        "/api/v1/sla/alerts/active",
        "/api/v1/sla/ticket/TICKET-123/status"
    ]

    response_times = []
    errors = []

    async def mixed_request():
        import random
        endpoint = random.choice(endpoints)
        url = f"{tester.session._base_url or 'http://localhost:8080'}{endpoint}"

        try:
            start_time = time.time()
            async with tester.session.get(url) as response:
                await response.text()
                end_time = time.time()
                response_times.append(end_time - start_time)
                return response.status == 200
        except Exception as e:
            end_time = time.time()
            response_times.append(end_time - start_time)
            errors.append(str(e))
            return False

    # Warm-up
    warm_up_tasks = [mixed_request() for _ in range(50)]
    await asyncio.gather(*warm_up_tasks)
    await asyncio.sleep(1)

    # Main test
    start_time = time.time()
    concurrency = scenario["concurrency"]
    num_requests = scenario["requests"]

    semaphore = asyncio.Semaphore(concurrency)
    async def bounded_mixed_request():
        async with semaphore:
            return await mixed_request()

    tasks = [bounded_mixed_request() for _ in range(num_requests)]
    results = await asyncio.gather(*tasks)

    total_time = time.time() - start_time

    successful_requests = sum(1 for r in results if r)
    failed_requests = len(results) - successful_requests

    if response_times:
        sorted_times = sorted(response_times)
        result = LoadTestResult(
            scenario_name=scenario["name"],
            total_requests=num_requests,
            successful_requests=successful_requests,
            failed_requests=failed_requests,
            total_time=total_time,
            requests_per_second=num_requests / total_time,
            avg_response_time=statistics.mean(response_times) * 1000,
            median_response_time=statistics.median(response_times) * 1000,
            p95_response_time=sorted_times[int(len(sorted_times) * 0.95)] * 1000,
            p99_response_time=sorted_times[int(len(sorted_times) * 0.99)] * 1000,
            min_response_time=min(response_times) * 1000,
            max_response_time=max(response_times) * 1000,
            error_rate=(failed_requests / num_requests) * 100,
            timestamp=time.time()
        )
    else:
        result = LoadTestResult(
            scenario_name=scenario["name"],
            total_requests=num_requests,
            successful_requests=0,
            failed_requests=num_requests,
            total_time=total_time,
            requests_per_second=0,
            avg_response_time=0,
            median_response_time=0,
            p95_response_time=0,
            p99_response_time=0,
            min_response_time=0,
            max_response_time=0,
            error_rate=100,
            timestamp=time.time()
        )

    return result

async def main():
    parser = argparse.ArgumentParser(description="Ogen Service Load Tester")
    parser.add_argument("--url", default="http://localhost:8080",
                       help="Base URL of the service to test")
    parser.add_argument("--service", default="support-sla",
                       choices=["support-sla", "gameplay", "all", "stress"],
                       help="Service to load test")
    parser.add_argument("--output", default="scripts/testing/load-test/results",
                       help="Output directory for results")

    args = parser.parse_args()

    # Create output directory
    output_dir = Path(args.output)
    output_dir.mkdir(parents=True, exist_ok=True)

    try:
        if args.service == "support-sla":
            results = await run_support_sla_scenarios(args.url)
        elif args.service == "gameplay":
            results = await run_gameplay_scenarios(args.url)
        elif args.service == "all":
            results = await run_all_services_scenarios(args.url)
        elif args.service == "stress":
            results = await run_stress_test_scenarios(args.url)
        else:
            logger.error(f"Unknown service: {args.service}")
            return

        # Save results
        timestamp = int(time.time())
        json_file = output_dir / f"load-test-results-{timestamp}.json"
        text_file = output_dir / f"load-test-report-{timestamp}.txt"

        # Save JSON results
        json_results = [result.to_dict() for result in results]
        with open(json_file, 'w') as f:
            json.dump(json_results, f, indent=2)

        # Generate text report
        report = generate_load_test_report(results)
        with open(text_file, 'w') as f:
            f.write(report)

        print(report)
        print(f"\nDetailed results saved to: {json_file}")
        print(f"Text report saved to: {text_file}")

    except Exception as e:
        logger.error(f"Load testing failed: {e}")
        raise

def generate_load_test_report(results: List[LoadTestResult]) -> str:
    """Generate comprehensive load test report"""

    report_lines = []
    report_lines.append("=" * 80)
    report_lines.append("OGEN SERVICE LOAD TESTING REPORT")
    report_lines.append("=" * 80)
    report_lines.append(f"Generated: {time.strftime('%Y-%m-%d %H:%M:%S')}")
    report_lines.append("")

    total_scenarios = len(results)
    passed_scenarios = sum(1 for r in results if r.error_rate < 5.0)  # <5% error rate
    failed_scenarios = total_scenarios - passed_scenarios

    report_lines.append("SUMMARY")
    report_lines.append("-" * 40)
    report_lines.append(f"Total Scenarios: {total_scenarios}")
    report_lines.append(f"Passed Scenarios: {passed_scenarios}")
    report_lines.append(f"Failed Scenarios: {failed_scenarios}")
    report_lines.append(".1f")
    report_lines.append("")

    report_lines.append("DETAILED RESULTS")
    report_lines.append("-" * 40)

    for i, result in enumerate(results, 1):
        status = "PASS" if result.error_rate < 5.0 else "FAIL"
        report_lines.append(f"\n{i}. {result.scenario_name}")
        report_lines.append(f"   Status: {status}")
        report_lines.append(f"   Total Requests: {result.total_requests}")
        report_lines.append(f"   Successful: {result.successful_requests}")
        report_lines.append(f"   Failed: {result.failed_requests}")
        report_lines.append(".1f")
        report_lines.append(".2f")
        report_lines.append(".1f")
        report_lines.append(".1f")
        report_lines.append(".1f")
        report_lines.append(".1f")
        report_lines.append(".1f")

    # Performance Analysis
    report_lines.append("\nPERFORMANCE ANALYSIS")
    report_lines.append("-" * 40)

    if results:
        avg_rps = statistics.mean(r.result.requests_per_second for r in results)
        avg_p95 = statistics.mean(r.result.p95_response_time for r in results)
        max_error_rate = max(r.result.error_rate for r in results)

        report_lines.append(".1f")
        report_lines.append(".1f")
        report_lines.append(".2f")

        # Recommendations
        report_lines.append("\nRECOMMENDATIONS")
        report_lines.append("-" * 40)

        if avg_rps < 100:
            report_lines.append("⚠️  Low throughput detected. Consider optimizing database queries.")
        if avg_p95 > 200:
            report_lines.append("⚠️  High latency detected. Consider adding caching or optimizing code.")
        if max_error_rate > 10:
            report_lines.append("❌ High error rate detected. Requires immediate investigation.")

        if all(r.error_rate < 5.0 and r.p95_response_time < 100 for r in results):
            report_lines.append("✅ All performance targets met. Service is production-ready.")

    return "\n".join(report_lines)

if __name__ == "__main__":
    asyncio.run(main())
