#!/usr/bin/env python3
"""
Ogen Migration Performance Benchmark Suite

Comprehensive benchmarking tool for measuring performance improvements
when migrating from oapi-codegen to ogen.
"""

import asyncio
import json
import logging
import statistics
import subprocess
import sys
import time
from dataclasses import dataclass, field
from datetime import datetime
from pathlib import Path
from typing import Dict, List, Optional, Tuple

import aiohttp


@dataclass
class BenchmarkResult:
    """Result of a single benchmark run."""
    service_name: str
    operation: str
    generator: str  # "oapi-codegen" or "ogen"
    duration_ms: float
    memory_usage_mb: float
    cpu_usage_percent: float
    timestamp: datetime = field(default_factory=datetime.now)


@dataclass
class PerformanceComparison:
    """Comparison between oapi-codegen and ogen performance."""
    service_name: str
    operation: str
    oapi_codegen_result: Optional[BenchmarkResult]
    ogen_result: Optional[BenchmarkResult]
    improvement_percentage: Optional[float] = None
    memory_savings_mb: Optional[float] = None


class BenchmarkSuite:
    """Comprehensive benchmark suite for ogen migration."""

    def __init__(self, base_path: Path):
        self.base_path = base_path
        self.results: List[BenchmarkResult] = []
        self.comparisons: List[PerformanceComparison] = []
        self.logger = logging.getLogger(__name__)

        # Benchmark configuration
        self.config = {
            "iterations": 100,
            "warmup_iterations": 10,
            "concurrent_requests": 50,
            "test_duration_seconds": 60,
            "memory_sampling_interval": 0.1,
            "cpu_sampling_interval": 0.1
        }

    def discover_services(self) -> List[str]:
        """Discover services available for benchmarking."""
        services_path = self.base_path / "services"
        services = []

        if services_path.exists():
            for service_dir in services_path.iterdir():
                if service_dir.is_dir() and (service_dir / "go.mod").exists():
                    # Check if service has OpenAPI spec
                    spec_path = self.base_path / "proto" / "openapi" / service_dir.name / "main.yaml"
                    if spec_path.exists():
                        services.append(service_dir.name)

        self.logger.info(f"Discovered {len(services)} services for benchmarking")
        return services

    async def run_full_benchmark_suite(self, services: Optional[List[str]] = None) -> None:
        """Run complete benchmark suite for all or selected services."""
        if services is None:
            services = self.discover_services()

        self.logger.info(f"Starting full benchmark suite for {len(services)} services")

        for service_name in services:
            try:
                self.logger.info(f"Benchmarking service: {service_name}")
                await self.benchmark_service(service_name)
            except Exception as e:
                self.logger.error(f"Failed to benchmark {service_name}: {e}")
                continue

        # Generate comparison report
        self.generate_comparison_report()
        self.logger.info("Benchmark suite completed")

    async def benchmark_service(self, service_name: str) -> None:
        """Run benchmarks for a specific service."""
        service_path = self.base_path / "services" / service_name

        # Benchmark oapi-codegen version (if exists)
        oapi_codegen_path = service_path / "benchmark_oapi_codegen"
        if oapi_codegen_path.exists():
            self.logger.info(f"Benchmarking oapi-codegen version of {service_name}")
            results = await self.run_service_benchmarks(service_name, "oapi-codegen", oapi_codegen_path)
            self.results.extend(results)

        # Benchmark ogen version (if exists)
        ogen_path = service_path / "benchmark_ogen"
        if ogen_path.exists():
            self.logger.info(f"Benchmarking ogen version of {service_name}")
            results = await self.run_service_benchmarks(service_name, "ogen", ogen_path)
            self.results.extend(results)

    async def run_service_benchmarks(self, service_name: str, generator: str, service_path: Path) -> List[BenchmarkResult]:
        """Run benchmarks for a service with specific generator."""
        results = []

        # Build service
        if not await self.build_service(service_path):
            self.logger.error(f"Failed to build {generator} version of {service_name}")
            return results

        # Start service
        process = await self.start_service(service_path)
        if process is None:
            self.logger.error(f"Failed to start {generator} version of {service_name}")
            return results

        try:
            # Wait for service to be ready
            await self.wait_for_service_ready(service_path)

            # Run performance benchmarks
            benchmark_results = await self.run_performance_benchmarks(service_name, generator, service_path)
            results.extend(benchmark_results)

        finally:
            # Stop service
            await self.stop_service(process)

        return results

    async def build_service(self, service_path: Path) -> bool:
        """Build service binary."""
        try:
            process = await asyncio.create_subprocess_exec(
                "go", "build", "-o", "service", ".",
                cwd=service_path,
                stdout=asyncio.subprocess.PIPE,
                stderr=asyncio.subprocess.PIPE
            )

            stdout, stderr = await process.communicate()

            if process.returncode == 0:
                self.logger.info(f"Successfully built service at {service_path}")
                return True
            else:
                self.logger.error(f"Build failed: {stderr.decode()}")
                return False

        except Exception as e:
            self.logger.error(f"Build error: {e}")
            return False

    async def start_service(self, service_path: Path) -> Optional[asyncio.subprocess.Process]:
        """Start service process."""
        try:
            # Find available port
            port = await self.find_available_port()

            process = await asyncio.create_subprocess_exec(
                "./service",
                env={**os.environ, "HTTP_ADDR": f":{port}"},
                cwd=service_path,
                stdout=asyncio.subprocess.PIPE,
                stderr=asyncio.subprocess.PIPE
            )

            # Store port for later use
            (service_path / ".port").write_text(str(port))

            self.logger.info(f"Started service on port {port}")
            return process

        except Exception as e:
            self.logger.error(f"Failed to start service: {e}")
            return None

    async def wait_for_service_ready(self, service_path: Path) -> None:
        """Wait for service to be ready."""
        port_file = service_path / ".port"
        if not port_file.exists():
            raise Exception("Port file not found")

        port = port_file.read_text().strip()
        url = f"http://localhost:{port}/health"

        # Try for up to 30 seconds
        for _ in range(30):
            try:
                async with aiohttp.ClientSession() as session:
                    async with session.get(url) as response:
                        if response.status == 200:
                            self.logger.info(f"Service ready on port {port}")
                            return
            except:
                pass

            await asyncio.sleep(1)

        raise Exception(f"Service not ready after 30 seconds on port {port}")

    async def run_performance_benchmarks(self, service_name: str, generator: str, service_path: Path) -> List[BenchmarkResult]:
        """Run performance benchmarks against running service."""
        port_file = service_path / ".port"
        port = port_file.read_text().strip()
        base_url = f"http://localhost:{port}"

        results = []

        # HTTP request latency benchmark
        latency_result = await self.benchmark_http_latency(service_name, generator, base_url)
        if latency_result:
            results.append(latency_result)

        # Memory usage benchmark
        memory_result = await self.benchmark_memory_usage(service_name, generator, service_path)
        if memory_result:
            results.append(memory_result)

        # CPU usage benchmark
        cpu_result = await self.benchmark_cpu_usage(service_name, generator, service_path)
        if cpu_result:
            results.append(cpu_result)

        # Concurrent request handling benchmark
        concurrent_result = await self.benchmark_concurrent_requests(service_name, generator, base_url)
        if concurrent_result:
            results.append(concurrent_result)

        return results

    async def benchmark_http_latency(self, service_name: str, generator: str, base_url: str) -> Optional[BenchmarkResult]:
        """Benchmark HTTP request latency."""
        latencies = []

        async with aiohttp.ClientSession() as session:
            for _ in range(self.config["iterations"]):
                start_time = time.time()

                try:
                    async with session.get(f"{base_url}/health") as response:
                        if response.status == 200:
                            latency = (time.time() - start_time) * 1000  # Convert to ms
                            latencies.append(latency)
                except Exception as e:
                    self.logger.warning(f"Request failed: {e}")
                    continue

        if latencies:
            avg_latency = statistics.mean(latencies)
            return BenchmarkResult(
                service_name=service_name,
                operation="http_latency",
                generator=generator,
                duration_ms=avg_latency,
                memory_usage_mb=0.0,  # Not measured here
                cpu_usage_percent=0.0   # Not measured here
            )

        return None

    async def benchmark_memory_usage(self, service_name: str, generator: str, service_path: Path) -> Optional[BenchmarkResult]:
        """Benchmark memory usage during load."""
        # This is a simplified implementation
        # Real implementation would use psutil or similar
        try:
            result = subprocess.run(
                ["ps", "aux", "--no-headers", "-o", "pmem,cmd"],
                capture_output=True,
                text=True
            )

            # Find our service process
            for line in result.stdout.split('\n'):
                if './service' in line:
                    # Extract memory percentage (simplified)
                    parts = line.split()
                    if len(parts) >= 1:
                        mem_percent = float(parts[0])
                        # Estimate MB (simplified calculation)
                        mem_mb = mem_percent * 10  # Rough estimate

                        return BenchmarkResult(
                            service_name=service_name,
                            operation="memory_usage",
                            generator=generator,
                            duration_ms=0.0,
                            memory_usage_mb=mem_mb,
                            cpu_usage_percent=0.0
                        )

        except Exception as e:
            self.logger.warning(f"Memory benchmark failed: {e}")

        return None

    async def benchmark_cpu_usage(self, service_name: str, generator: str, service_path: Path) -> Optional[BenchmarkResult]:
        """Benchmark CPU usage during load."""
        # Simplified implementation
        try:
            result = subprocess.run(
                ["ps", "aux", "--no-headers", "-o", "pcpu,cmd"],
                capture_output=True,
                text=True
            )

            for line in result.stdout.split('\n'):
                if './service' in line:
                    parts = line.split()
                    if len(parts) >= 1:
                        cpu_percent = float(parts[0])

                        return BenchmarkResult(
                            service_name=service_name,
                            operation="cpu_usage",
                            generator=generator,
                            duration_ms=0.0,
                            memory_usage_mb=0.0,
                            cpu_usage_percent=cpu_percent
                        )

        except Exception as e:
            self.logger.warning(f"CPU benchmark failed: {e}")

        return None

    async def benchmark_concurrent_requests(self, service_name: str, generator: str, base_url: str) -> Optional[BenchmarkResult]:
        """Benchmark concurrent request handling."""
        async def make_request(session: aiohttp.ClientSession, request_id: int):
            try:
                start_time = time.time()
                async with session.get(f"{base_url}/health") as response:
                    if response.status == 200:
                        return (time.time() - start_time) * 1000
            except Exception as e:
                self.logger.warning(f"Concurrent request {request_id} failed: {e}")
            return None

        start_time = time.time()

        async with aiohttp.ClientSession() as session:
            tasks = []
            for i in range(self.config["concurrent_requests"]):
                tasks.append(make_request(session, i))

            results = await asyncio.gather(*tasks, return_exceptions=True)

        total_time = (time.time() - start_time) * 1000
        successful_requests = sum(1 for r in results if r is not None and not isinstance(r, Exception))

        if successful_requests > 0:
            avg_time_per_request = total_time / successful_requests
            return BenchmarkResult(
                service_name=service_name,
                operation="concurrent_requests",
                generator=generator,
                duration_ms=avg_time_per_request,
                memory_usage_mb=0.0,
                cpu_usage_percent=0.0
            )

        return None

    async def stop_service(self, process: asyncio.subprocess.Process) -> None:
        """Stop service process."""
        try:
            process.terminate()
            await asyncio.wait_for(process.wait(), timeout=10.0)
            self.logger.info("Service stopped successfully")
        except asyncio.TimeoutError:
            self.logger.warning("Service didn't stop gracefully, killing...")
            process.kill()
            await process.wait()
        except Exception as e:
            self.logger.error(f"Error stopping service: {e}")

    async def find_available_port(self) -> int:
        """Find an available port."""
        import socket

        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
            s.bind(('', 0))
            s.listen(1)
            port = s.getsockname()[1]

        return port

    def generate_comparison_report(self) -> None:
        """Generate comparison report between oapi-codegen and ogen."""
        # Group results by service and operation
        comparisons_by_key = {}

        for result in self.results:
            key = (result.service_name, result.operation)
            if key not in comparisons_by_key:
                comparisons_by_key[key] = {}

            comparisons_by_key[key][result.generator] = result

        # Create comparisons
        for (service_name, operation), generators in comparisons_by_key.items():
            comparison = PerformanceComparison(
                service_name=service_name,
                operation=operation,
                oapi_codegen_result=generators.get("oapi-codegen"),
                ogen_result=generators.get("ogen")
            )

            # Calculate improvements
            if comparison.oapi_codegen_result and comparison.ogen_result:
                if operation in ["http_latency", "memory_usage"]:
                    # Lower is better
                    old_value = comparison.oapi_codegen_result.duration_ms or comparison.oapi_codegen_result.memory_usage_mb
                    new_value = comparison.ogen_result.duration_ms or comparison.ogen_result.memory_usage_mb

                    if old_value > 0:
                        comparison.improvement_percentage = ((old_value - new_value) / old_value) * 100
                elif operation == "cpu_usage":
                    # Lower is better
                    old_value = comparison.oapi_codegen_result.cpu_usage_percent
                    new_value = comparison.ogen_result.cpu_usage_percent

                    if old_value > 0:
                        comparison.improvement_percentage = ((old_value - new_value) / old_value) * 100

                # Memory savings
                if comparison.oapi_codegen_result.memory_usage_mb > 0 and comparison.ogen_result.memory_usage_mb > 0:
                    comparison.memory_savings_mb = comparison.oapi_codegen_result.memory_usage_mb - comparison.ogen_result.memory_usage_mb

            self.comparisons.append(comparison)

    def save_results(self, output_path: Optional[Path] = None) -> None:
        """Save benchmark results to file."""
        if output_path is None:
            timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")
            output_path = self.base_path / "scripts" / "ogen-migration" / f"benchmark_results_{timestamp}.json"

        results_data = {
            "timestamp": datetime.now().isoformat(),
            "config": self.config,
            "results": [
                {
                    "service_name": r.service_name,
                    "operation": r.operation,
                    "generator": r.generator,
                    "duration_ms": r.duration_ms,
                    "memory_usage_mb": r.memory_usage_mb,
                    "cpu_usage_percent": r.cpu_usage_percent,
                    "timestamp": r.timestamp.isoformat()
                }
                for r in self.results
            ],
            "comparisons": [
                {
                    "service_name": c.service_name,
                    "operation": c.operation,
                    "improvement_percentage": c.improvement_percentage,
                    "memory_savings_mb": c.memory_savings_mb
                }
                for c in self.comparisons
            ]
        }

        output_path.parent.mkdir(parents=True, exist_ok=True)

        with open(output_path, 'w', encoding='utf-8') as f:
            json.dump(results_data, f, indent=2, ensure_ascii=False)

        self.logger.info(f"Benchmark results saved to {output_path}")

    def print_summary(self) -> None:
        """Print benchmark summary."""
        print("\n=== Ogen Migration Benchmark Summary ===")
        print(f"Total benchmark runs: {len(self.results)}")
        print(f"Services compared: {len(set(c.service_name for c in self.comparisons))}")

        # Summary statistics
        improvements = [c.improvement_percentage for c in self.comparisons if c.improvement_percentage is not None]

        if improvements:
            avg_improvement = statistics.mean(improvements)
            print(".1f")
            print(".1f")
            print(".1f")

        # Per-operation breakdown
        operations = {}
        for comp in self.comparisons:
            if comp.operation not in operations:
                operations[comp.operation] = []
            if comp.improvement_percentage is not None:
                operations[comp.operation].append(comp.improvement_percentage)

        print("\nPer-operation improvements:")
        for op, improvements in operations.items():
            if improvements:
                avg = statistics.mean(improvements)
                print(f"  {op}: {avg:+.1f}%")


async def main():
    """Main entry point."""
    logging.basicConfig(level=logging.INFO)

    # Parse arguments
    import argparse
    parser = argparse.ArgumentParser(description="Ogen Migration Benchmark Suite")
    parser.add_argument("--services", nargs="*", help="Specific services to benchmark")
    parser.add_argument("--output", type=Path, help="Output file for results")

    args = parser.parse_args()

    # Initialize benchmark suite
    base_path = Path(__file__).parent.parent.parent
    suite = BenchmarkSuite(base_path)

    try:
        # Run benchmarks
        await suite.run_full_benchmark_suite(args.services)

        # Save results
        suite.save_results(args.output)

        # Print summary
        suite.print_summary()

    except KeyboardInterrupt:
        print("\nBenchmark interrupted by user")
    except Exception as e:
        logging.error(f"Benchmark failed: {e}")
        sys.exit(1)


if __name__ == "__main__":
    asyncio.run(main())
