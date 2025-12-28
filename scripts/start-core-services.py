#!/usr/bin/env python3
"""
Core Services Startup Script
Starts essential NECPGAME microservices for testing and validation

Usage:
    python scripts/start-core-services.py [--services SERVICE_LIST] [--check-only]
"""

import os
import sys
import time
import subprocess
import argparse
import requests
from pathlib import Path
from typing import List, Dict, Any

# Add project root to path
sys.path.append(str(Path(__file__).parent.parent))

class ServiceManager:
    """Manages NECPGAME microservices startup and health checks"""

    def __init__(self):
        self.services = {
            'world-regions-service': {
                'port': 8080,
                'path': 'services/world-regions-service-go',
                'health_endpoint': '/health',
                'startup_time': 5
            },
            'world-cities-service': {
                'port': 8081,
                'path': 'services/world-cities-service-go',
                'health_endpoint': '/health',
                'startup_time': 5
            },
            'api-gateway': {
                'port': 8080,  # API Gateway uses same port as world-regions
                'path': 'services/api-gateway-service-go',
                'health_endpoint': '/health',
                'startup_time': 3
            }
        }

    def start_service(self, service_name: str) -> bool:
        """Start a specific service"""
        if service_name not in self.services:
            print(f"[ERROR] Service {service_name} not found")
            return False

        service = self.services[service_name]
        service_path = service['path']

        try:
            print(f"[INFO] Starting {service_name}...")
            os.chdir(service_path)

            # Build if needed
            if os.path.exists('go.mod'):
                subprocess.run(['go', 'build', '-o', service_name, '.'], check=True, capture_output=True)

            # Start service
            process = subprocess.Popen(['./' + service_name],
                                     stdout=subprocess.PIPE,
                                     stderr=subprocess.PIPE,
                                     env={**os.environ, 'PORT': str(service['port'])})

            print(f"[OK] {service_name} started (PID: {process.pid})")

            # Wait for startup
            time.sleep(service['startup_time'])

            return True

        except Exception as e:
            print(f"[ERROR] Failed to start {service_name}: {e}")
            return False

    def check_service_health(self, service_name: str) -> Dict[str, Any]:
        """Check service health"""
        if service_name not in self.services:
            return {'status': 'unknown', 'error': 'Service not configured'}

        service = self.services[service_name]
        url = f"http://localhost:{service['port']}{service['health_endpoint']}"

        try:
            response = requests.get(url, timeout=5)
            if response.status_code in [200, 401]:  # 401 is acceptable (service running, auth required)
                return {'status': 'healthy', 'response_time': response.elapsed.total_seconds() * 1000, 'status_code': response.status_code}
            else:
                return {'status': 'unhealthy', 'status_code': response.status_code}
        except requests.exceptions.RequestException as e:
            return {'status': 'unreachable', 'error': str(e)}

    def start_all_services(self, services_to_start: List[str] = None) -> Dict[str, bool]:
        """Start all or specified services"""
        if services_to_start is None:
            services_to_start = list(self.services.keys())

        results = {}
        for service_name in services_to_start:
            if service_name in self.services:
                results[service_name] = self.start_service(service_name)

        return results

    def check_all_services(self, services_to_check: List[str] = None) -> Dict[str, Dict[str, Any]]:
        """Check health of all or specified services"""
        if services_to_check is None:
            services_to_check = list(self.services.keys())

        results = {}
        for service_name in services_to_check:
            results[service_name] = self.check_service_health(service_name)

        return results

    def print_status_report(self, health_results: Dict[str, Dict[str, Any]]):
        """Print comprehensive status report"""
        print("\n" + "="*60)
        print("NECPGAME CORE SERVICES STATUS REPORT")
        print("="*60)

        healthy_count = 0
        total_count = len(health_results)

        for service_name, health in health_results.items():
            status = health['status']
            if status == 'healthy':
                healthy_count += 1
                print(f"[OK] {service_name:<25} HEALTHY ({health.get('response_time', 0):.1f}ms)")
            elif status == 'unhealthy':
                print(f"[WARN] {service_name:<25} UNHEALTHY (HTTP {health.get('status_code', '???')})")
            else:
                print(f"[ERROR] {service_name:<25} {status.upper()}")

        print("="*60)
        print(f"SUMMARY: {healthy_count}/{total_count} services healthy")
        print("="*60)

        return healthy_count == total_count

def main():
    parser = argparse.ArgumentParser(description='NECPGAME Core Services Manager')
    parser.add_argument('--services', nargs='*', help='Specific services to manage')
    parser.add_argument('--check-only', action='store_true', help='Only check status, do not start')
    parser.add_argument('--start-only', action='store_true', help='Only start services, do not check')

    args = parser.parse_args()

    manager = ServiceManager()

    if args.services:
        services = args.services
    else:
        services = list(manager.services.keys())

    if not args.check_only:
        print("[INFO] Starting core services...")
        start_results = manager.start_all_services(services)
        print(f"[INFO] Started {sum(start_results.values())}/{len(start_results)} services")

    if not args.start_only:
        print("[INFO] Checking service health...")
        health_results = manager.check_all_services(services)
        all_healthy = manager.print_status_report(health_results)

        if all_healthy:
            print("[SUCCESS] All core services are healthy and ready!")
            return 0
        else:
            print("[WARNING] Some services are not healthy")
            return 1

    return 0

if __name__ == '__main__':
    sys.exit(main())
