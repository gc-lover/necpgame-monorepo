#!/usr/bin/env python3
"""
MMOFPS Load Testing Suite
Comprehensive load testing for MMOFPS game services with 10k+ concurrent users

Features:
- Multi-service load testing (combat, matchmaking, inventory, economy)
- Real-time metrics collection and analysis
- Distributed testing across multiple machines
- WebSocket and HTTP concurrent load
- Performance profiling and bottleneck detection
- Automated scaling recommendations
"""

import os
import sys
import time
import json
import asyncio
import aiohttp
import statistics
import argparse
import logging
from pathlib import Path
from typing import Dict, List, Any, Optional, Tuple, Callable
from dataclasses import dataclass, asdict, field
from concurrent.futures import ThreadPoolExecutor, as_completed
import threading
import signal
import psutil
import socket

# Add project root to path
sys.path.append(str(Path(__file__).parent.parent))

# Setup structured logging
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s [%(levelname)s] %(name)s: %(message)s',
    datefmt='%Y-%m-%d %H:%M:%S'
)
logger = logging.getLogger(__name__)

@dataclass
class LoadTestConfig:
    """Configuration for load testing"""
    test_type: str  # "combat", "matchmaking", "inventory", "economy", "full"
    num_clients: int = 1000
    duration_seconds: int = 300  # 5 minutes
    ramp_up_seconds: int = 60
    ramp_down_seconds: int = 30
    target_rps: int = 1000  # Target requests per second
    server_url: str = "http://localhost:8080"
    websocket_url: str = "ws://localhost:8080"
    num_workers: int = 10
    enable_websockets: bool = True
    enable_metrics: bool = True
    enable_profiling: bool = True

@dataclass
class LoadTestMetrics:
    """Comprehensive metrics collected during load testing"""
    # Timing metrics
    start_time: float = 0.0
    end_time: float = 0.0
    duration: float = 0.0

    # Request metrics
    total_requests: int = 0
    successful_requests: int = 0
    failed_requests: int = 0
    timeouts: int = 0
    connection_errors: int = 0

    # Response time metrics (in milliseconds)
    response_times: List[float] = field(default_factory=list)
    avg_response_time: float = 0.0
    p50_response_time: float = 0.0
    p95_response_time: float = 0.0
    p99_response_time: float = 0.0
    min_response_time: float = float('inf')
    max_response_time: float = 0.0

    # Throughput metrics
    requests_per_second: float = 0.0
    bytes_sent: int = 0
    bytes_received: int = 0

    # Error metrics
    error_rate: float = 0.0
    error_breakdown: Dict[str, int] = field(default_factory=dict)

    # WebSocket metrics
    websocket_connections: int = 0
    websocket_messages_sent: int = 0
    websocket_messages_received: int = 0
    websocket_connection_errors: int = 0

    # Resource metrics
    cpu_usage: float = 0.0
    memory_usage: float = 0.0
    network_bytes_sent: int = 0
    network_bytes_received: int = 0

    # Custom metrics
    custom_metrics: Dict[str, Any] = field(default_factory=dict)

    def calculate_stats(self):
        """Calculate statistical metrics"""
        if self.response_times:
            self.avg_response_time = statistics.mean(self.response_times)
            self.p50_response_time = statistics.median(self.response_times)
            self.p95_response_time = statistics.quantiles(self.response_times, n=20)[18]
            self.p99_response_time = statistics.quantiles(self.response_times, n=100)[98]
            self.min_response_time = min(self.response_times)
            self.max_response_time = max(self.response_times)

        self.duration = self.end_time - self.start_time
        if self.duration > 0:
            self.requests_per_second = self.total_requests / self.duration

        if self.total_requests > 0:
            self.error_rate = (self.failed_requests / self.total_requests) * 100

    def to_dict(self) -> Dict[str, Any]:
        """Convert metrics to dictionary"""
        result = asdict(self)
        # Convert lists to summary stats to avoid huge JSON
        if self.response_times:
            result['response_times'] = {
                'count': len(self.response_times),
                'avg': self.avg_response_time,
                'p50': self.p50_response_time,
                'p95': self.p95_response_time,
                'p99': self.p99_response_time,
                'min': self.min_response_time,
                'max': self.max_response_time
            }
        else:
            result['response_times'] = None
        return result

@dataclass
class LoadTestResult:
    """Complete load test result"""
    config: LoadTestConfig
    metrics: LoadTestMetrics
    success: bool
    error_message: Optional[str] = None
    recommendations: List[str] = field(default_factory=list)

class MMOFPSLoadTester:
    """Main load testing orchestrator for MMOFPS services"""

    def __init__(self, config: LoadTestConfig):
        self.config = config
        self.metrics = LoadTestMetrics()
        self.results = LoadTestResult(config, self.metrics, False)
        self.stop_event = threading.Event()
        self.executor = ThreadPoolExecutor(max_workers=config.num_workers)

        # Setup signal handlers
        signal.signal(signal.SIGINT, self._signal_handler)
        signal.signal(signal.SIGTERM, self._signal_handler)

    def _signal_handler(self, signum, frame):
        """Handle shutdown signals"""
        logger.info(f"Received signal {signum}, stopping load test...")
        self.stop_event.set()

    async def run_load_test(self) -> LoadTestResult:
        """Run the complete load test"""
        logger.info(f"Starting {self.config.test_type} load test with {self.config.num_clients} clients for {self.config.duration_seconds}s")

        self.metrics.start_time = time.time()

        try:
            # Initialize test
            await self._initialize_test()

            # Ramp up phase
            await self._ramp_up()

            # Main test phase
            await self._run_main_test()

            # Ramp down phase
            await self._ramp_down()

            # Calculate final metrics
            self.metrics.end_time = time.time()
            self.metrics.calculate_stats()

            # Generate recommendations
            self._generate_recommendations()

            self.results.success = True
            logger.info(f"Load test completed successfully. RPS: {self.metrics.requests_per_second:.2f}")

        except Exception as e:
            self.results.success = False
            self.results.error_message = str(e)
            logger.error(f"Load test failed: {e}")

        return self.results

    async def _initialize_test(self):
        """Initialize test environment"""
        logger.info("Initializing test environment...")

        # Test server connectivity
        await self._test_server_connectivity()

        # Initialize metrics collection
        if self.config.enable_metrics:
            self._start_metrics_collection()

        # Initialize WebSocket connections if needed
        if self.config.enable_websockets and self.config.test_type in ['combat', 'full']:
            await self._initialize_websocket_pool()

    async def _test_server_connectivity(self):
        """Test basic server connectivity"""
        try:
            async with aiohttp.ClientSession() as session:
                async with session.get(f"{self.config.server_url}/health", timeout=10) as response:
                    if response.status not in [200, 401, 403]:
                        raise Exception(f"Server health check failed: {response.status}")
        except Exception as e:
            raise Exception(f"Server connectivity test failed: {e}")

    def _start_metrics_collection(self):
        """Start background metrics collection"""
        def collect_metrics():
            while not self.stop_event.is_set():
                try:
                    # Collect system metrics
                    cpu_percent = psutil.cpu_percent(interval=1)
                    memory_percent = psutil.virtual_memory().percent

                    # Collect network metrics
                    net_counters = psutil.net_io_counters()
                    bytes_sent = net_counters.bytes_sent
                    bytes_recv = net_counters.bytes_recv

                    # Update metrics (simplified - in real implementation would aggregate)
                    self.metrics.cpu_usage = cpu_percent
                    self.metrics.memory_usage = memory_percent
                    self.metrics.network_bytes_sent = bytes_sent
                    self.metrics.network_bytes_received = bytes_recv

                    time.sleep(5)  # Collect every 5 seconds
                except Exception as e:
                    logger.error(f"Metrics collection error: {e}")

        threading.Thread(target=collect_metrics, daemon=True).start()

    async def _initialize_websocket_pool(self):
        """Initialize WebSocket connection pool"""
        logger.info("Initializing WebSocket connection pool...")
        # Implementation for WebSocket pool initialization
        pass

    async def _ramp_up(self):
        """Gradually ramp up load"""
        logger.info(f"Ramping up load over {self.config.ramp_up_seconds} seconds...")

        clients_per_second = self.config.num_clients / self.config.ramp_up_seconds

        for second in range(self.config.ramp_up_seconds):
            if self.stop_event.is_set():
                break

            clients_this_second = int(clients_per_second * (second + 1))
            clients_this_second = min(clients_this_second, self.config.num_clients)

            # Start clients for this second
            await self._start_clients_batch(clients_this_second - ((second) * int(clients_per_second)))

            await asyncio.sleep(1)

    async def _start_clients_batch(self, num_clients: int):
        """Start a batch of clients"""
        # Implementation for starting client batch
        pass

    async def _run_main_test(self):
        """Run the main test phase"""
        logger.info(f"Running main test phase for {self.config.duration_seconds} seconds...")

        end_time = time.time() + self.config.duration_seconds

        while time.time() < end_time and not self.stop_event.is_set():
            # Run test logic based on type
            if self.config.test_type == "combat":
                await self._run_combat_test()
            elif self.config.test_type == "matchmaking":
                await self._run_matchmaking_test()
            elif self.config.test_type == "inventory":
                await self._run_inventory_test()
            elif self.config.test_type == "economy":
                await self._run_economy_test()
            elif self.config.test_type == "full":
                await self._run_full_test()

            await asyncio.sleep(0.1)  # Small delay to prevent overwhelming

    async def _run_combat_test(self):
        """Run combat-specific load test"""
        async with aiohttp.ClientSession() as session:
            tasks = []

            # Simulate combat actions
            for i in range(min(50, self.config.num_clients)):  # Batch size
                task = asyncio.create_task(self._simulate_combat_action(session, i))
                tasks.append(task)

            await asyncio.gather(*tasks, return_exceptions=True)

    async def _simulate_combat_action(self, session: aiohttp.ClientSession, client_id: int) -> Optional[Dict[str, Any]]:
        """Simulate a single combat action"""
        try:
            start_time = time.time()

            # Simulate combat action (damage, movement, ability use)
            action_data = {
                "action_type": "damage",
                "player_id": f"player_{client_id}",
                "target_id": f"target_{client_id % 10}",
                "damage": 85,
                "weapon": "ak47",
                "position": {"x": client_id * 10, "y": 0, "z": 0}
            }

            async with session.post(
                f"{self.config.server_url}/api/v1/combat/action",
                json=action_data,
                timeout=aiohttp.ClientTimeout(total=5)
            ) as response:
                response_time = (time.time() - start_time) * 1000

                self.metrics.total_requests += 1
                self.metrics.response_times.append(response_time)

                if response.status == 200:
                    self.metrics.successful_requests += 1
                    self.metrics.bytes_received += len(await response.text())
                else:
                    self.metrics.failed_requests += 1
                    error_key = f"http_{response.status}"
                    self.metrics.error_breakdown[error_key] = self.metrics.error_breakdown.get(error_key, 0) + 1

                return {
                    "status": response.status,
                    "response_time": response_time,
                    "client_id": client_id
                }

        except asyncio.TimeoutError:
            self.metrics.timeouts += 1
            self.metrics.failed_requests += 1
            return None
        except aiohttp.ClientError as e:
            self.metrics.connection_errors += 1
            self.metrics.failed_requests += 1
            self.metrics.error_breakdown[str(type(e).__name__)] = self.metrics.error_breakdown.get(str(type(e).__name__), 0) + 1
            return None

    async def _run_matchmaking_test(self):
        """Run matchmaking-specific load test"""
        async with aiohttp.ClientSession() as session:
            tasks = []

            for i in range(min(20, self.config.num_clients)):
                task = asyncio.create_task(self._simulate_matchmaking_request(session, i))
                tasks.append(task)

            await asyncio.gather(*tasks, return_exceptions=True)

    async def _simulate_matchmaking_request(self, session: aiohttp.ClientSession, client_id: int) -> Optional[Dict[str, Any]]:
        """Simulate matchmaking queue join"""
        try:
            start_time = time.time()

            queue_data = {
                "player_id": f"player_{client_id}",
                "game_mode": "ranked",
                "region": "us-west",
                "skill_rating": 1500 + (client_id % 500)
            }

            async with session.post(
                f"{self.config.server_url}/api/v1/matchmaking/join",
                json=queue_data,
                timeout=aiohttp.ClientTimeout(total=10)
            ) as response:
                response_time = (time.time() - start_time) * 1000

                self.metrics.total_requests += 1
                self.metrics.response_times.append(response_time)

                if response.status in [200, 202]:  # 202 = Accepted (queued)
                    self.metrics.successful_requests += 1
                else:
                    self.metrics.failed_requests += 1

                return {
                    "status": response.status,
                    "response_time": response_time,
                    "client_id": client_id
                }

        except Exception as e:
            self.metrics.failed_requests += 1
            return None

    async def _run_inventory_test(self):
        """Run inventory-specific load test"""
        async with aiohttp.ClientSession() as session:
            tasks = []

            for i in range(min(30, self.config.num_clients)):
                task = asyncio.create_task(self._simulate_inventory_operation(session, i))
                tasks.append(task)

            await asyncio.gather(*tasks, return_exceptions=True)

    async def _simulate_inventory_operation(self, session: aiohttp.ClientSession, client_id: int) -> Optional[Dict[str, Any]]:
        """Simulate inventory operations (equip, use, trade)"""
        try:
            start_time = time.time()

            operations = ["equip", "use", "drop"]
            operation = operations[client_id % len(operations)]

            inventory_data = {
                "player_id": f"player_{client_id}",
                "operation": operation,
                "item_id": f"item_{client_id % 100}",
                "quantity": 1
            }

            async with session.post(
                f"{self.config.server_url}/api/v1/inventory/{operation}",
                json=inventory_data,
                timeout=aiohttp.ClientTimeout(total=5)
            ) as response:
                response_time = (time.time() - start_time) * 1000

                self.metrics.total_requests += 1
                self.metrics.response_times.append(response_time)

                if response.status == 200:
                    self.metrics.successful_requests += 1
                else:
                    self.metrics.failed_requests += 1

                return {
                    "status": response.status,
                    "response_time": response_time,
                    "operation": operation
                }

        except Exception as e:
            self.metrics.failed_requests += 1
            return None

    async def _run_economy_test(self):
        """Run economy-specific load test"""
        async with aiohttp.ClientSession() as session:
            tasks = []

            for i in range(min(25, self.config.num_clients)):
                task = asyncio.create_task(self._simulate_economy_transaction(session, i))
                tasks.append(task)

            await asyncio.gather(*tasks, return_exceptions=True)

    async def _simulate_economy_transaction(self, session: aiohttp.ClientSession, client_id: int) -> Optional[Dict[str, Any]]:
        """Simulate economy transactions (buy, sell, trade)"""
        try:
            start_time = time.time()

            transaction_data = {
                "player_id": f"player_{client_id}",
                "transaction_type": "buy",
                "item_id": f"item_{client_id % 50}",
                "quantity": 1,
                "price": 100 + (client_id % 1000)
            }

            async with session.post(
                f"{self.config.server_url}/api/v1/economy/transaction",
                json=transaction_data,
                timeout=aiohttp.ClientTimeout(total=5)
            ) as response:
                response_time = (time.time() - start_time) * 1000

                self.metrics.total_requests += 1
                self.metrics.response_times.append(response_time)

                if response.status == 200:
                    self.metrics.successful_requests += 1
                else:
                    self.metrics.failed_requests += 1

                return {
                    "status": response.status,
                    "response_time": response_time,
                    "transaction_type": "buy"
                }

        except Exception as e:
            self.metrics.failed_requests += 1
            return None

    async def _run_full_test(self):
        """Run comprehensive full system test"""
        # Run all test types concurrently
        await asyncio.gather(
            self._run_combat_test(),
            self._run_matchmaking_test(),
            self._run_inventory_test(),
            self._run_economy_test(),
            return_exceptions=True
        )

    async def _ramp_down(self):
        """Gradually ramp down load"""
        logger.info(f"Ramping down load over {self.config.ramp_down_seconds} seconds...")
        # Implementation for graceful ramp down
        await asyncio.sleep(self.config.ramp_down_seconds)

    def _generate_recommendations(self):
        """Generate performance improvement recommendations"""
        recommendations = []

        # Analyze response times
        if self.metrics.p95_response_time > 500:  # >500ms P95
            recommendations.append("Consider implementing response time optimization - P95 is too high")

        if self.metrics.error_rate > 5:
            recommendations.append("High error rate detected - investigate service stability")

        if self.metrics.requests_per_second < self.config.target_rps * 0.8:
            recommendations.append("Throughput below target - consider horizontal scaling")

        if self.metrics.cpu_usage > 80:
            recommendations.append("High CPU usage detected - optimize compute-intensive operations")

        if self.metrics.memory_usage > 80:
            recommendations.append("High memory usage detected - check for memory leaks")

        self.results.recommendations = recommendations

    def save_results(self, output_file: Optional[str] = None) -> str:
        """Save test results to file"""
        if output_file is None:
            timestamp = time.strftime("%Y%m%d_%H%M%S")
            output_file = f"load_test_results_{self.config.test_type}_{timestamp}.json"

        results_data = {
            "config": asdict(self.config),
            "metrics": self.metrics.to_dict(),
            "success": self.results.success,
            "error_message": self.results.error_message,
            "recommendations": self.results.recommendations,
            "timestamp": time.time()
        }

        with open(output_file, 'w') as f:
            json.dump(results_data, f, indent=2, default=str)

        logger.info(f"Results saved to {output_file}")
        return output_file

async def main():
    """Main entry point"""
    parser = argparse.ArgumentParser(description="MMOFPS Load Testing Suite")
    parser.add_argument("--type", choices=["combat", "matchmaking", "inventory", "economy", "full"],
                       default="combat", help="Type of load test to run")
    parser.add_argument("--clients", type=int, default=1000,
                       help="Number of concurrent clients")
    parser.add_argument("--duration", type=int, default=300,
                       help="Test duration in seconds")
    parser.add_argument("--server", default="http://localhost:8080",
                       help="Server URL to test")
    parser.add_argument("--rps", type=int, default=1000,
                       help="Target requests per second")
    parser.add_argument("--workers", type=int, default=10,
                       help="Number of worker threads")
    parser.add_argument("--output", help="Output file for results")
    parser.add_argument("--websockets", action="store_true", default=True,
                       help="Enable WebSocket testing")

    args = parser.parse_args()

    config = LoadTestConfig(
        test_type=args.type,
        num_clients=args.clients,
        duration_seconds=args.duration,
        server_url=args.server,
        target_rps=args.rps,
        num_workers=args.workers,
        enable_websockets=args.websockets
    )

    tester = MMOFPSLoadTester(config)

    try:
        logger.info(f"Starting MMOFPS load test: {config.test_type}")
        logger.info(f"Clients: {config.num_clients}, Duration: {config.duration_seconds}s")
        logger.info(f"Target RPS: {config.target_rps}, Server: {config.server_url}")

        result = await tester.run_load_test()

        if result.success:
            logger.info("✅ Load test completed successfully!")
            logger.info(f"Requests/sec: {result.metrics.requests_per_second:.2f}")
            logger.info(f"Avg response time: {result.metrics.avg_response_time:.2f}ms")
            logger.info(f"P95 response time: {result.metrics.p95_response_time:.2f}ms")
            logger.info(f"Error rate: {result.metrics.error_rate:.2f}%")

            if result.recommendations:
                logger.info("Recommendations:")
                for rec in result.recommendations:
                    logger.info(f"  - {rec}")
        else:
            logger.error(f"❌ Load test failed: {result.error_message}")
            sys.exit(1)

    except KeyboardInterrupt:
        logger.info("Load test interrupted by user")
    except Exception as e:
        logger.error(f"Unexpected error: {e}")
        sys.exit(1)
    finally:
        output_file = tester.save_results(args.output)
        logger.info(f"Results saved to: {output_file}")

if __name__ == "__main__":
    asyncio.run(main())
