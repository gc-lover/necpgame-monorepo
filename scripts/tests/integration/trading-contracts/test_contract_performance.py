#!/usr/bin/env python3
"""
Performance Tests for Trading Contracts System

Tests system performance under various load conditions:
- Response time benchmarks
- Throughput testing
- Memory usage monitoring
- Database query performance
- Redis caching effectiveness

Issue: #2202 - Тестирование системы контрактов и сделок
"""

import pytest
import requests
import time
import statistics
import psutil
import threading
from concurrent.futures import ThreadPoolExecutor, as_completed
from typing import List, Dict, Tuple
import json
import os

# Test Configuration
TEST_SERVICE_URL = "http://localhost:8088"
TEST_DURATION = 60  # seconds
CONCURRENT_USERS = 100
REQUESTS_PER_SECOND = 500

class PerformanceMetrics:
    """Performance metrics collector"""

    def __init__(self):
        self.response_times: List[float] = []
        self.errors: int = 0
        self.requests_sent: int = 0
        self.requests_completed: int = 0
        self.memory_usage: List[float] = []
        self.cpu_usage: List[float] = []
        self.start_time = time.time()

    def add_response_time(self, response_time: float):
        self.response_times.append(response_time)
        self.requests_completed += 1

    def add_error(self):
        self.errors += 1

    def record_system_metrics(self):
        """Record current system resource usage"""
        process = psutil.Process()
        self.memory_usage.append(process.memory_info().rss / 1024 / 1024)  # MB
        self.cpu_usage.append(psutil.cpu_percent(interval=None))

    def get_summary(self) -> Dict:
        """Get performance summary"""
        total_time = time.time() - self.start_time

        if not self.response_times:
            return {"error": "No requests completed"}

        return {
            "total_requests_sent": self.requests_sent,
            "total_requests_completed": self.requests_completed,
            "total_errors": self.errors,
            "total_time_seconds": total_time,
            "requests_per_second": self.requests_completed / total_time,
            "error_rate": self.errors / max(self.requests_sent, 1),
            "avg_response_time_ms": statistics.mean(self.response_times) * 1000,
            "median_response_time_ms": statistics.median(self.response_times) * 1000,
            "p95_response_time_ms": self._percentile(self.response_times, 95) * 1000,
            "p99_response_time_ms": self._percentile(self.response_times, 99) * 1000,
            "min_response_time_ms": min(self.response_times) * 1000,
            "max_response_time_ms": max(self.response_times) * 1000,
            "avg_memory_usage_mb": statistics.mean(self.memory_usage) if self.memory_usage else 0,
            "avg_cpu_usage_percent": statistics.mean(self.cpu_usage) if self.cpu_usage else 0,
        }

    def _percentile(self, data: List[float], percentile: float) -> float:
        """Calculate percentile from data"""
        if not data:
            return 0.0
        data_sorted = sorted(data)
        index = int(len(data_sorted) * percentile / 100)
        return data_sorted[min(index, len(data_sorted) - 1)]

class TradingContractsPerformanceTest:
    """Performance test suite"""

    def setup_method(self):
        """Setup test environment"""
        self.service_url = TEST_SERVICE_URL
        self.session = requests.Session()
        self.metrics = PerformanceMetrics()

        # Create test user for performance tests
        self.test_user_id = f"perf_test_user_{int(time.time())}"

    def measure_response_time(self, operation: str, func, *args, **kwargs) -> float:
        """Measure response time for an operation"""
        start_time = time.time()
        try:
            result = func(*args, **kwargs)
            response_time = time.time() - start_time

            if isinstance(result, requests.Response):
                if result.status_code >= 400:
                    self.metrics.add_error()
                    return response_time

            self.metrics.add_response_time(response_time)
            return response_time

        except Exception as e:
            self.metrics.add_error()
            return time.time() - start_time

    def create_test_contract(self, user_id: str, symbol: str = "PERF_TEST") -> Dict:
        """Create a test contract for performance testing"""
        contract_data = {
            "client_order_id": f"perf_{int(time.time() * 1000000)}",
            "symbol": symbol,
            "contract_type": "SPOT",
            "order_type": "LIMIT",
            "side": "BUY",
            "quantity": 1,
            "price": 100.0,
            "user_id": user_id
        }

        url = f"{self.service_url}/contracts"
        response = self.session.post(url, json=contract_data)

        if response.status_code == 201:
            return response.json()
        else:
            raise Exception(f"Failed to create contract: {response.text}")

    def get_contract(self, contract_id: str) -> requests.Response:
        """Get contract details"""
        url = f"{self.service_url}/contracts/{contract_id}"
        return self.session.get(url)

    def get_order_book(self, symbol: str) -> requests.Response:
        """Get order book"""
        url = f"{self.service_url}/orderbook/{symbol}"
        return self.session.get(url)

    def get_user_contracts(self, user_id: str) -> requests.Response:
        """Get user contracts"""
        url = f"{self.service_url}/contracts"
        params = {"limit": 50}
        return self.session.get(url, params=params)

    def health_check(self) -> requests.Response:
        """Health check"""
        url = f"{self.service_url}/health"
        return self.session.get(url)

    def test_response_time_baselines(self):
        """Test baseline response times for key operations"""
        print("Testing baseline response times...")

        # Health check baseline
        health_times = []
        for i in range(10):
            response_time = self.measure_response_time(
                "health_check",
                self.health_check
            )
            health_times.append(response_time)

        avg_health_time = statistics.mean(health_times) * 1000
        print(".2f"
        # Create contract baseline
        contract_times = []
        for i in range(5):
            try:
                response_time = self.measure_response_time(
                    "create_contract",
                    self.create_test_contract,
                    self.test_user_id,
                    f"BASE_TEST_{i}"
                )
                contract_times.append(response_time)
            except:
                pass

        if contract_times:
            avg_contract_time = statistics.mean(contract_times) * 1000
            print(".2f"
        # Get contract baseline
        if contract_times:  # If we created contracts
            get_times = []
            # Create one contract to get
            try:
                contract = self.create_test_contract(self.test_user_id, "GET_TEST")
                contract_id = contract["contract_id"]

                for i in range(10):
                    response_time = self.measure_response_time(
                        "get_contract",
                        self.get_contract,
                        contract_id
                    )
                    get_times.append(response_time)

                avg_get_time = statistics.mean(get_times) * 1000
                print(".2f"
            except Exception as e:
                print(f"Failed to test get_contract: {e}")

    def test_concurrent_load(self):
        """Test system under concurrent load"""
        print("Testing concurrent load...")

        def worker_load_test(worker_id: int):
            """Worker function for load testing"""
            user_id = f"load_user_{worker_id}"

            # Create several contracts per worker
            for i in range(5):
                try:
                    self.measure_response_time(
                        "create_contract_concurrent",
                        self.create_test_contract,
                        user_id,
                        f"LOAD_TEST_{worker_id}_{i}"
                    )
                except:
                    pass

                # Small delay to avoid overwhelming
                time.sleep(0.01)

        # Run concurrent workers
        num_workers = 20
        with ThreadPoolExecutor(max_workers=num_workers) as executor:
            futures = [executor.submit(worker_load_test, i) for i in range(num_workers)]
            start_time = time.time()

            # Wait for completion
            for future in as_completed(futures):
                future.result()

            total_time = time.time() - start_time

        print(f"Concurrent load test completed in {total_time:.2f} seconds")
        print(f"Requests per second: {self.metrics.requests_completed / total_time:.2f}")

    def test_sustained_load(self):
        """Test sustained load over time"""
        print("Testing sustained load...")

        duration = 30  # seconds
        start_time = time.time()
        request_count = 0

        while time.time() - start_time < duration:
            try:
                self.measure_response_time(
                    "health_check_sustained",
                    self.health_check
                )
                request_count += 1

                # Small delay to avoid overwhelming
                time.sleep(0.01)

            except KeyboardInterrupt:
                break

        total_time = time.time() - start_time
        rps = request_count / total_time

        print(f"Sustained load test: {request_count} requests in {total_time:.2f} seconds")
        print(".2f"
    def test_memory_usage_under_load(self):
        """Test memory usage patterns under load"""
        print("Testing memory usage under load...")

        # Baseline memory
        initial_memory = psutil.Process().memory_info().rss / 1024 / 1024  # MB
        print(".2f"
        # Create burst of contracts
        burst_size = 100
        for i in range(burst_size):
            try:
                self.measure_response_time(
                    "create_contract_burst",
                    self.create_test_contract,
                    self.test_user_id,
                    f"MEM_TEST_{i}"
                )
            except:
                pass

        # Check memory after burst
        after_burst_memory = psutil.Process().memory_info().rss / 1024 / 1024  # MB
        memory_increase = after_burst_memory - initial_memory

        print(".2f"
        print(".2f"
        # Wait a bit for garbage collection
        time.sleep(2)
        final_memory = psutil.Process().memory_info().rss / 1024 / 1024  # MB
        memory_after_gc = final_memory - initial_memory

        print(".2f"
    def test_database_performance(self):
        """Test database query performance"""
        print("Testing database performance...")

        # Create a batch of contracts for testing
        batch_size = 50
        contract_ids = []

        for i in range(batch_size):
            try:
                contract = self.create_test_contract(self.test_user_id, f"DB_TEST_{i}")
                contract_ids.append(contract["contract_id"])
            except:
                pass

        # Test individual contract retrieval
        get_times = []
        for contract_id in contract_ids[:10]:  # Test first 10
            response_time = self.measure_response_time(
                "get_contract_db",
                self.get_contract,
                contract_id
            )
            get_times.append(response_time)

        if get_times:
            avg_get_time = statistics.mean(get_times) * 1000
            print(".2f"
        # Test order book retrieval (aggregates multiple contracts)
        order_book_times = []
        for i in range(10):
            response_time = self.measure_response_time(
                "get_order_book_db",
                self.get_order_book,
                "DB_TEST_0"  # Test symbol
            )
            order_book_times.append(response_time)

        if order_book_times:
            avg_ob_time = statistics.mean(order_book_times) * 1000
            print(".2f"
    def test_caching_effectiveness(self):
        """Test Redis caching effectiveness"""
        print("Testing caching effectiveness...")

        # Create a contract
        try:
            contract = self.create_test_contract(self.test_user_id, "CACHE_TEST")
            contract_id = contract["contract_id"]

            # First request (cache miss)
            start_time = time.time()
            response1 = self.get_contract(contract_id)
            first_request_time = time.time() - start_time

            # Second request (should be cache hit)
            start_time = time.time()
            response2 = self.get_contract(contract_id)
            second_request_time = time.time() - start_time

            if response1.status_code == 200 and response2.status_code == 200:
                speedup = first_request_time / max(second_request_time, 0.001)
                print(".4f")
                print(".2f")
                print(".2f")

                if speedup > 2.0:
                    print("✅ Caching is effective")
                else:
                    print("⚠️  Caching effectiveness is limited")
        except Exception as e:
            print(f"Failed to test caching: {e}")

    def test_error_handling_performance(self):
        """Test performance during error conditions"""
        print("Testing error handling performance...")

        # Test invalid contract creation
        error_times = []
        for i in range(10):
            invalid_contract = {
                "client_order_id": f"error_test_{i}",
                "symbol": "",  # Invalid: empty symbol
                "contract_type": "INVALID_TYPE",
                "user_id": self.test_user_id
            }

            try:
                url = f"{self.service_url}/contracts"
                start_time = time.time()
                response = self.session.post(url, json=invalid_contract)
                response_time = time.time() - start_time

                self.metrics.add_response_time(response_time)
                error_times.append(response_time)

                # Should get error response
                if response.status_code >= 400:
                    self.metrics.add_error()

            except Exception as e:
                self.metrics.add_error()

        if error_times:
            avg_error_time = statistics.mean(error_times) * 1000
            print(".2f"
    def test_scalability_metrics(self):
        """Test system scalability metrics"""
        print("Testing scalability metrics...")

        # Test with increasing concurrent users
        user_counts = [1, 5, 10, 20]
        results = {}

        for user_count in user_counts:
            print(f"Testing with {user_count} concurrent users...")

            def user_simulation(user_id: int):
                """Simulate user behavior"""
                user_specific_id = f"scale_user_{user_id}"

                # Each user creates a few contracts
                for i in range(3):
                    try:
                        self.measure_response_time(
                            "create_contract_scale",
                            self.create_test_contract,
                            user_specific_id,
                            f"SCALE_TEST_{user_id}_{i}"
                        )
                    except:
                        pass

                # Each user checks their contracts
                try:
                    self.measure_response_time(
                        "get_user_contracts_scale",
                        self.get_user_contracts,
                        user_specific_id
                    )
                except:
                    pass

            # Run concurrent users
            start_time = time.time()
            with ThreadPoolExecutor(max_workers=user_count) as executor:
                futures = [executor.submit(user_simulation, i) for i in range(user_count)]
                for future in as_completed(futures):
                    future.result()

            total_time = time.time() - start_time
            rps = self.metrics.requests_completed / max(total_time, 0.001)

            results[user_count] = {
                "total_time": total_time,
                "requests_per_second": rps,
                "completed_requests": self.metrics.requests_completed,
                "errors": self.metrics.errors
            }

            print(".2f")

        # Analyze scalability
        for user_count, result in results.items():
            print(f"Users: {user_count}, RPS: {result['requests_per_second']:.2f}, Errors: {result['errors']}")

    def run_full_performance_test(self):
        """Run complete performance test suite"""
        print("🚀 Starting Trading Contracts Performance Test Suite")
        print("=" * 60)

        # Run all performance tests
        self.test_response_time_baselines()
        print()

        self.test_concurrent_load()
        print()

        self.test_sustained_load()
        print()

        self.test_memory_usage_under_load()
        print()

        self.test_database_performance()
        print()

        self.test_caching_effectiveness()
        print()

        self.test_error_handling_performance()
        print()

        self.test_scalability_metrics()
        print()

        # Final summary
        print("=" * 60)
        summary = self.metrics.get_summary()

        print("📊 PERFORMANCE TEST SUMMARY")
        print(".2f"        print(".2f"        print(".2f"        print(".2f"        print(".2f"        print(".2f"        print(".2f"        print(".2f"        print(".2f"        print(".2f"        print(".2f"        print(".2f"
        # Performance assessment
        if summary["p95_response_time_ms"] < 100:  # 100ms P95
            performance_rating = "EXCELLENT"
        elif summary["p95_response_time_ms"] < 500:  # 500ms P95
            performance_rating = "GOOD"
        elif summary["p95_response_time_ms"] < 1000:  # 1s P95
            performance_rating = "ACCEPTABLE"
        else:
            performance_rating = "NEEDS_IMPROVEMENT"

        print(f"🎯 Performance Rating: {performance_rating}")

        if summary["error_rate"] < 0.01:  # < 1% error rate
            reliability_rating = "HIGH"
        elif summary["error_rate"] < 0.05:  # < 5% error rate
            reliability_rating = "MEDIUM"
        else:
            reliability_rating = "LOW"

        print(f"🛡️  Reliability Rating: {reliability_rating}")

        if summary["requests_per_second"] > 100:
            throughput_rating = "HIGH"
        elif summary["requests_per_second"] > 50:
            throughput_rating = "MEDIUM"
        else:
            throughput_rating = "LOW"

        print(f"⚡ Throughput Rating: {throughput_rating}")

        print("\n✅ Performance testing completed successfully!")

        return summary

# Test execution
if __name__ == "__main__":
    tester = TradingContractsPerformanceTest()
    tester.setup_method()

    try:
        summary = tester.run_full_performance_test()

        # Save results to file
        with open("performance_test_results.json", "w") as f:
            json.dump(summary, f, indent=2)

        print("📄 Results saved to performance_test_results.json")

    except Exception as e:
        print(f"❌ Performance test failed: {e}")
        raise