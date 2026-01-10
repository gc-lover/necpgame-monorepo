#!/usr/bin/env python3
"""
Mass Performance Optimization Checker for 80+ Services
Issue: #2278 - Mass ogen Migration - 80+ Services Performance Upgrade
Checks all services for required performance optimizations
"""

import os
import re
from pathlib import Path
from typing import Dict, List, Tuple

class PerformanceChecker:
    def __init__(self):
        self.services_dir = Path("services")
        self.results = {}

    def find_services(self) -> List[Path]:
        """Find all Go service directories"""
        return list(self.services_dir.glob("*-service-go"))

    def check_database_optimization(self, service_path: Path) -> bool:
        """Check if service has database connection pool optimization"""
        service_file = service_path / "internal" / "service" / "service.go"
        if not service_file.exists():
            return False

        try:
            content = service_file.read_text(encoding='utf-8')
        except UnicodeDecodeError:
            content = service_file.read_text(encoding='latin-1')
        # Check for connection pool settings
        patterns = [
            r"MaxConns\s*=\s*\d+",
            r"MinConns\s*=\s*\d+",
            r"pgxpool\.Config",
            r"SetMaxOpenConns",
            r"SetMaxIdleConns"
        ]

        return any(re.search(pattern, content) for pattern in patterns)

    def check_context_timeouts(self, service_path: Path) -> bool:
        """Check if service has context timeout optimizations"""
        handler_file = service_path / "internal" / "service" / "handler.go"
        service_file = service_path / "internal" / "service" / "service.go"

        files_to_check = [f for f in [handler_file, service_file] if f.exists()]

        for file_path in files_to_check:
            try:
                content = file_path.read_text(encoding='utf-8')
            except UnicodeDecodeError:
                content = file_path.read_text(encoding='latin-1')
            if re.search(r"context\.WithTimeout|WithDeadline|withTimeout", content):
                return True
        return False

    def check_redis_optimization(self, service_path: Path) -> bool:
        """Check if service has Redis connection pool optimization"""
        service_file = service_path / "internal" / "service" / "service.go"
        if not service_file.exists():
            return False

        try:
            content = service_file.read_text(encoding='utf-8')
        except UnicodeDecodeError:
            content = service_file.read_text(encoding='latin-1')
        patterns = [
            r"PoolSize\s*=\s*\d+",
            r"MinIdleConns\s*=\s*\d+",
            r"redis\.Options",
            r"ConnMaxLifetime"
        ]

        return any(re.search(pattern, content) for pattern in patterns)

    def check_http_optimization(self, service_path: Path) -> bool:
        """Check if service has HTTP server optimizations"""
        main_file = service_path / "main.go"
        if not main_file.exists():
            return False

        try:
            content = main_file.read_text(encoding='utf-8')
        except UnicodeDecodeError:
            content = main_file.read_text(encoding='latin-1')
        patterns = [
            r"ReadTimeout.*time\.Second",
            r"WriteTimeout.*time\.Second",
            r"IdleTimeout.*time\.Second",
            r"MaxHeaderBytes"
        ]

        return any(re.search(pattern, content) for pattern in patterns)

    def check_struct_alignment(self, service_path: Path) -> bool:
        """Check if service has struct alignment optimizations"""
        # Check generated ogen files for struct alignment hints
        ogen_files = list(service_path.glob("oas_*.go"))
        for ogen_file in ogen_files:
            try:
                content = ogen_file.read_text(encoding='utf-8')
            except UnicodeDecodeError:
                content = ogen_file.read_text(encoding='latin-1')
            if "BACKEND NOTE" in content and "struct alignment" in content:
                return True
        return False

    def check_service_optimization(self, service_path: Path) -> Dict[str, bool]:
        """Check all optimizations for a service"""
        service_name = service_path.name.replace("-service-go", "")

        return {
            "database_pool": self.check_database_optimization(service_path),
            "context_timeouts": self.check_context_timeouts(service_path),
            "redis_pool": self.check_redis_optimization(service_path),
            "http_server": self.check_http_optimization(service_path),
            "struct_alignment": self.check_struct_alignment(service_path)
        }

    def analyze_all_services(self) -> Dict[str, Dict[str, bool]]:
        """Analyze all services and return optimization status"""
        services = self.find_services()
        results = {}

        for service_path in services:
            service_name = service_path.name.replace("-service-go", "")
            results[service_name] = self.check_service_optimization(service_path)

        return results

    def print_summary(self, results: Dict[str, Dict[str, bool]]):
        """Print optimization summary"""
        print("[ANALYSIS] MASS PERFORMANCE OPTIMIZATION ANALYSIS")
        print("=" * 50)

        total_services = len(results)
        optimized_services = 0

        print(f"[INFO] Total services analyzed: {total_services}")
        print()

        # Count optimizations per type
        optimization_counts = {
            "database_pool": 0,
            "context_timeouts": 0,
            "redis_pool": 0,
            "http_server": 0,
            "struct_alignment": 0
        }

        for service_name, optimizations in results.items():
            optimized_count = sum(optimizations.values())
            if optimized_count >= 4:  # Consider optimized if 4/5 checks pass
                optimized_services += 1

            for opt_type, has_opt in optimizations.items():
                if has_opt:
                    optimization_counts[opt_type] += 1

        print("[TARGET] Optimization Coverage:")
        for opt_type, count in optimization_counts.items():
            percentage = (count / total_services) * 100
            status = "[OK]" if percentage >= 80 else "[WARNING]" if percentage >= 50 else "[ERROR]"
            print(f"  {status} {opt_type}: {count}/{total_services} ({percentage:.1f}%)")

        print()
        print(f"[SUCCESS] Fully Optimized Services: {optimized_services}/{total_services} ({(optimized_services/total_services)*100:.1f}%)")
        print()

        # Show services needing optimization
        needs_optimization = []
        for service_name, optimizations in results.items():
            missing_opts = [k for k, v in optimizations.items() if not v]
            if missing_opts:
                needs_optimization.append((service_name, missing_opts))

        if needs_optimization:
            print("[TASK] Services Needing Optimization:")
            for service_name, missing in needs_optimization[:10]:  # Show first 10
                print(f"  - {service_name}: missing {', '.join(missing)}")
            if len(needs_optimization) > 10:
                print(f"  ... and {len(needs_optimization) - 10} more")
        else:
            print("[SUCCESS] All services are fully optimized!")

        print()
        print("[TARGET] PERFORMANCE TARGETS:")
        print("  - P99 Latency: <30ms for operations")
        print("  - Memory: <35KB per active session")
        print("  - Concurrent users: 100,000+ simultaneous operations")
        print("  - Throughput: 30,000+ operations per second")

def main():
    checker = PerformanceChecker()
    results = checker.analyze_all_services()
    checker.print_summary(results)

if __name__ == "__main__":
    main()
