#!/usr/bin/env python3
"""
Synchronization Performance Testing Script
Automated testing of MMOFPS synchronization performance

Usage:
    python scripts/test_synchronization_performance.py --type position --clients 100 --duration 60
    python scripts/test_synchronization_performance.py --type combat --clients 50 --duration 120
    python scripts/test_synchronization_performance.py --suite full --clients 1000 --duration 300
"""

import os
import sys
import time
import json
import asyncio
import aiohttp
import statistics
import argparse
from pathlib import Path
from typing import Dict, List, Any, Optional, Tuple
from dataclasses import dataclass, asdict
import logging
from concurrent.futures import ThreadPoolExecutor

# Add project root to path
sys.path.append(str(Path(__file__).parent.parent))

# Setup logging
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s [%(levelname)s] %(name)s: %(message)s',
    datefmt='%Y-%m-%d %H:%M:%S'
)
logger = logging.getLogger(__name__)

@dataclass
class SyncMetrics:
    """Metrics collected during sync testing"""
    total_messages: int = 0
    successful_syncs: int = 0
    failed_syncs: int = 0
    latencies: List[float] = None
    message_loss_rate: float = 0.0
    avg_latency: float = 0.0
    p50_latency: float = 0.0
    p95_latency: float = 0.0
    p99_latency: float = 0.0
    throughput_msgs_per_sec: float = 0.0
    test_duration: float = 0.0
    start_time: float = 0.0
    end_time: float = 0.0

    def __post_init__(self):
        if self.latencies is None:
            self.latencies = []

    def calculate_stats(self):
        """Calculate statistical metrics"""
        if not self.latencies:
            return

        self.avg_latency = statistics.mean(self.latencies)
        self.p50_latency = statistics.median(self.latencies)
        self.p95_latency = statistics.quantiles(self.latencies, n=20)[18]  # 95th percentile
        self.p99_latency = statistics.quantiles(self.latencies, n=100)[98]  # 99th percentile
        self.message_loss_rate = (self.failed_syncs / max(1, self.total_messages)) * 100
        self.throughput_msgs_per_sec = self.successful_syncs / max(1, self.test_duration)

@dataclass
class SyncTestConfig:
    """Configuration for sync testing"""
    sync_type: str
    num_clients: int
    duration_seconds: int
    server_url: str = "http://localhost:8080"
    message_interval: float = 0.05  # 20Hz default
    timeout: float = 5.0
    warmup_seconds: int = 10

class SynchronizationTester:
    def __init__(self, config: SyncTestConfig):
        self.config = config
        self.metrics = SyncMetrics()
        self.executor = ThreadPoolExecutor(max_workers=min(config.num_clients, 100))

    async def test_position_sync(self, session: aiohttp.ClientSession, client_id: int) -> Dict[str, Any]:
        """Test position synchronization"""
        client_metrics = SyncMetrics()
        client_metrics.start_time = time.time()

        try:
            # Simulate player movement
            position = {"x": 0.0, "y": 0.0, "z": 0.0, "client_id": client_id}

            for i in range(int(self.config.duration_seconds / self.config.message_interval)):
                start_time = time.time()

                # Update position (simulate movement)
                position["x"] += 1.0
                position["y"] += 0.5
                position["timestamp"] = start_time

                try:
                    async with session.post(
                        f"{self.config.server_url}/sync/position",
                        json=position,
                        timeout=aiohttp.ClientTimeout(total=self.config.timeout)
                    ) as response:
                        latency = time.time() - start_time

                        if response.status == 200:
                            client_metrics.successful_syncs += 1
                            client_metrics.latencies.append(latency * 1000)  # Convert to ms
                        else:
                            client_metrics.failed_syncs += 1
                            logger.warning(f"Position sync failed: {response.status}")

                except asyncio.TimeoutError:
                    client_metrics.failed_syncs += 1
                    logger.warning(f"Position sync timeout for client {client_id}")
                except Exception as e:
                    client_metrics.failed_syncs += 1
                    logger.error(f"Position sync error for client {client_id}: {e}")

                client_metrics.total_messages += 1
                await asyncio.sleep(self.config.message_interval)

        except Exception as e:
            logger.error(f"Position sync test failed for client {client_id}: {e}")

        client_metrics.end_time = time.time()
        client_metrics.test_duration = client_metrics.end_time - client_metrics.start_time
        client_metrics.calculate_stats()

        return {"client_id": client_id, "metrics": asdict(client_metrics)}

    async def test_combat_sync(self, session: aiohttp.ClientSession, client_id: int) -> Dict[str, Any]:
        """Test combat state synchronization"""
        client_metrics = SyncMetrics()
        client_metrics.start_time = time.time()

        try:
            # Simulate combat state updates
            combat_state = {
                "player_id": f"player_{client_id}",
                "health": 100,
                "ammo": 30,
                "weapon": " pistol",
                "effects": [],
                "client_id": client_id
            }

            for i in range(int(self.config.duration_seconds / self.config.message_interval)):
                start_time = time.time()

                # Update combat state (simulate damage, shooting, etc)
                combat_state["health"] = max(0, combat_state["health"] - 1)  # Simulate damage
                combat_state["ammo"] = max(0, combat_state["ammo"] - 1)    # Simulate shooting
                combat_state["timestamp"] = start_time

                try:
                    async with session.post(
                        f"{self.config.server_url}/sync/combat",
                        json=combat_state,
                        timeout=aiohttp.ClientTimeout(total=self.config.timeout)
                    ) as response:
                        latency = time.time() - start_time

                        if response.status == 200:
                            client_metrics.successful_syncs += 1
                            client_metrics.latencies.append(latency * 1000)
                        else:
                            client_metrics.failed_syncs += 1
                            logger.warning(f"Combat sync failed: {response.status}")

                except asyncio.TimeoutError:
                    client_metrics.failed_syncs += 1
                    logger.warning(f"Combat sync timeout for client {client_id}")
                except Exception as e:
                    client_metrics.failed_syncs += 1
                    logger.error(f"Combat sync error for client {client_id}: {e}")

                client_metrics.total_messages += 1
                await asyncio.sleep(self.config.message_interval)

        except Exception as e:
            logger.error(f"Combat sync test failed for client {client_id}: {e}")

        client_metrics.end_time = time.time()
        client_metrics.test_duration = client_metrics.end_time - client_metrics.start_time
        client_metrics.calculate_stats()

        return {"client_id": client_id, "metrics": asdict(client_metrics)}

    async def test_world_sync(self, session: aiohttp.ClientSession, client_id: int) -> Dict[str, Any]:
        """Test world state synchronization"""
        client_metrics = SyncMetrics()
        client_metrics.start_time = time.time()

        try:
            # Simulate world state updates (doors, elevators, destructibles)
            world_objects = [
                {"id": "door_1", "type": "door", "state": "closed"},
                {"id": "elevator_1", "type": "elevator", "floor": 1},
                {"id": "breakable_wall_1", "type": "destructible", "health": 100}
            ]

            for i in range(int(self.config.duration_seconds / (self.config.message_interval * 5))):  # Slower updates
                start_time = time.time()

                # Update world state (simulate interactions)
                for obj in world_objects:
                    if obj["type"] == "door":
                        obj["state"] = "open" if obj["state"] == "closed" else "closed"
                    elif obj["type"] == "elevator":
                        obj["floor"] = (obj["floor"] % 10) + 1
                    elif obj["type"] == "destructible":
                        obj["health"] = max(0, obj["health"] - 5)

                world_update = {
                    "client_id": client_id,
                    "timestamp": start_time,
                    "objects": world_objects
                }

                try:
                    async with session.post(
                        f"{self.config.server_url}/sync/world",
                        json=world_update,
                        timeout=aiohttp.ClientTimeout(total=self.config.timeout)
                    ) as response:
                        latency = time.time() - start_time

                        if response.status == 200:
                            client_metrics.successful_syncs += 1
                            client_metrics.latencies.append(latency * 1000)
                        else:
                            client_metrics.failed_syncs += 1
                            logger.warning(f"World sync failed: {response.status}")

                except asyncio.TimeoutError:
                    client_metrics.failed_syncs += 1
                    logger.warning(f"World sync timeout for client {client_id}")
                except Exception as e:
                    client_metrics.failed_syncs += 1
                    logger.error(f"World sync error for client {client_id}: {e}")

                client_metrics.total_messages += 1
                await asyncio.sleep(self.config.message_interval * 5)  # Slower for world sync

        except Exception as e:
            logger.error(f"World sync test failed for client {client_id}: {e}")

        client_metrics.end_time = time.time()
        client_metrics.test_duration = client_metrics.end_time - client_metrics.start_time
        client_metrics.calculate_stats()

        return {"client_id": client_id, "metrics": asdict(client_metrics)}

    async def run_test(self) -> Dict[str, Any]:
        """Run synchronization test"""
        logger.info(f"Starting {self.config.sync_type} sync test with {self.config.num_clients} clients for {self.config.duration_seconds}s")

        # Warmup period
        logger.info(f"Warmup period: {self.config.warmup_seconds}s")
        await asyncio.sleep(self.config.warmup_seconds)

        self.metrics.start_time = time.time()

        async with aiohttp.ClientSession() as session:
            # Create test tasks based on sync type
            tasks = []

            for client_id in range(self.config.num_clients):
                if self.config.sync_type == "position":
                    task = self.test_position_sync(session, client_id)
                elif self.config.sync_type == "combat":
                    task = self.test_combat_sync(session, client_id)
                elif self.config.sync_type == "world":
                    task = self.test_world_sync(session, client_id)
                else:
                    raise ValueError(f"Unknown sync type: {self.config.sync_type}")

                tasks.append(task)

            # Run all client tests concurrently
            logger.info(f"Running {len(tasks)} concurrent client tests...")
            results = await asyncio.gather(*tasks, return_exceptions=True)

        # Aggregate results
        self.metrics.end_time = time.time()
        self.metrics.test_duration = self.metrics.end_time - self.metrics.start_time

        all_latencies = []
        for result in results:
            if isinstance(result, Exception):
                logger.error(f"Client test failed: {result}")
                continue

            client_data = result
            client_metrics = client_data["metrics"]

            self.metrics.total_messages += client_metrics["total_messages"]
            self.metrics.successful_syncs += client_metrics["successful_syncs"]
            self.metrics.failed_syncs += client_metrics["failed_syncs"]
            all_latencies.extend(client_metrics["latencies"])

        self.metrics.latencies = all_latencies
        self.metrics.calculate_stats()

        return asdict(self.metrics)

    def print_report(self, results: Dict[str, Any]):
        """Print test results report"""
        print("\n" + "="*80)
        print(f"SYNCHRONIZATION PERFORMANCE TEST REPORT - {self.config.sync_type.upper()}")
        print("="*80)
        print(f"Test Configuration:")
        print(f"  Sync Type: {self.config.sync_type}")
        print(f"  Clients: {self.config.num_clients}")
        print(f"  Duration: {self.config.duration_seconds}s")
        print(f"  Message Interval: {self.config.message_interval}s")
        print(f"  Server: {self.config.server_url}")
        print()
        print(f"Results:")
        print(f"  Total Messages: {results['total_messages']:,}")
        print(f"  Successful Syncs: {results['successful_syncs']:,}")
        print(f"  Failed Syncs: {results['failed_syncs']:,}")
        print(f"  Message Loss Rate: {results['message_loss_rate']:.2f}%")
        print(f"  Test Duration: {results['test_duration']:.2f}s")
        print(f"  Throughput: {results['throughput_msgs_per_sec']:.0f} msgs/sec")
        print()
        print(f"Latency Statistics:")
        print(f"  Average: {results['avg_latency']:.2f}ms")
        print(f"  Median (P50): {results['p50_latency']:.2f}ms")
        print(f"  P95: {results['p95_latency']:.2f}ms")
        print(f"  P99: {results['p99_latency']:.2f}ms")
        print("="*80)

async def run_sync_test(sync_type: str, num_clients: int, duration: int, server_url: str) -> Dict[str, Any]:
    """Run a synchronization test"""
    config = SyncTestConfig(
        sync_type=sync_type,
        num_clients=num_clients,
        duration_seconds=duration,
        server_url=server_url
    )

    tester = SynchronizationTester(config)
    results = await tester.run_test()
    tester.print_report(results)

    return results

async def run_full_test_suite(num_clients: int, duration: int, server_url: str) -> Dict[str, Any]:
    """Run full test suite for all sync types"""
    logger.info("Running full synchronization test suite")

    results = {}

    # Test each sync type
    sync_types = ["position", "combat", "world"]

    for sync_type in sync_types:
        logger.info(f"\n{'='*60}")
        logger.info(f"Testing {sync_type} synchronization")
        logger.info(f"{'='*60}")

        config = SyncTestConfig(
            sync_type=sync_type,
            num_clients=num_clients,
            duration_seconds=duration,
            server_url=server_url
        )

        tester = SynchronizationTester(config)
        type_results = await tester.run_test()
        tester.print_report(type_results)

        results[sync_type] = type_results

        # Brief pause between tests
        await asyncio.sleep(5)

    return results

def save_results(results: Dict[str, Any], filename: str):
    """Save test results to file"""
    output_file = Path(f"reports/sync_test_{int(time.time())}.json")

    with open(output_file, 'w') as f:
        json.dump({
            "timestamp": time.time(),
            "config": {},
            "results": results
        }, f, indent=2)

    logger.info(f"Results saved to {output_file}")

def main():
    parser = argparse.ArgumentParser(description='Synchronization Performance Testing')
    parser.add_argument('--type', choices=['position', 'combat', 'world'],
                       help='Type of synchronization to test')
    parser.add_argument('--suite', choices=['full'], help='Run full test suite')
    parser.add_argument('--clients', type=int, default=10,
                       help='Number of concurrent clients (default: 10)')
    parser.add_argument('--duration', type=int, default=30,
                       help='Test duration in seconds (default: 30)')
    parser.add_argument('--server', default='http://localhost:8080',
                       help='Server URL (default: http://localhost:8080)')
    parser.add_argument('--save-results', action='store_true',
                       help='Save results to file')

    args = parser.parse_args()

    if not args.type and not args.suite:
        parser.error("Must specify either --type or --suite")

    # Run the test
    if args.suite == 'full':
        results = asyncio.run(run_full_test_suite(args.clients, args.duration, args.server))
    else:
        results = asyncio.run(run_sync_test(args.type, args.clients, args.duration, args.server))

    if args.save_results:
        save_results(results, f"sync_test_{args.type or 'full'}_{int(time.time())}")

if __name__ == '__main__':
    main()
