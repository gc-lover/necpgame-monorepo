#!/usr/bin/env python3
"""
Performance Monitoring System for NECPGAME Services
Monitors Go services performance metrics for MMOFPS optimization
"""

import psutil
import requests
import time
import json
import logging
from datetime import datetime
from typing import Dict, List, Optional
import os
import sys
from pathlib import Path

# Add scripts directory to path for imports
sys.path.append(str(Path(__file__).parent))

try:
    from core.config import DatabaseConfig
    from core.logger import setup_logger
except ImportError:
    # Fallback if core modules not available
    class DatabaseConfig:
        pass

    def setup_logger(name):
        logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(name)s - %(levelname)s - %(message)s')
        return logging.getLogger(name)

class PerformanceMonitor:
    """Monitors performance metrics for Go services"""

    def __init__(self, services_config: Dict[str, str]):
        self.services = services_config
        self.metrics_history = []
        self.logger = setup_logger("performance_monitor")

        # Database connection for storing metrics
        self.db_config = DatabaseConfig()

    def get_service_metrics(self, service_name: str, port: int) -> Optional[Dict]:
        """Get performance metrics from a service"""
        try:
            # Health check endpoint
            health_url = f"http://localhost:{port}/health"
            response = requests.get(health_url, timeout=5)

            if response.status_code == 200:
                health_data = response.json()

                # Get system metrics for the service process
                process_metrics = self.get_process_metrics(service_name)

                return {
                    "service": service_name,
                    "port": port,
                    "status": "healthy",
                    "timestamp": datetime.now().isoformat(),
                    "health": health_data,
                    "process": process_metrics,
                    "response_time_ms": response.elapsed.total_seconds() * 1000
                }
            else:
                return {
                    "service": service_name,
                    "port": port,
                    "status": "unhealthy",
                    "timestamp": datetime.now().isoformat(),
                    "error": f"HTTP {response.status_code}",
                    "response_time_ms": response.elapsed.total_seconds() * 1000
                }

        except requests.exceptions.RequestException as e:
            return {
                "service": service_name,
                "port": port,
                "status": "unreachable",
                "timestamp": datetime.now().isoformat(),
                "error": str(e)
            }
        except Exception as e:
            self.logger.error(f"Error monitoring {service_name}: {e}")
            return None

    def get_process_metrics(self, service_name: str) -> Optional[Dict]:
        """Get system metrics for service process"""
        try:
            # Find process by name pattern
            for proc in psutil.process_iter(['pid', 'name', 'cpu_percent', 'memory_info', 'num_threads']):
                try:
                    if service_name.lower() in proc.info['name'].lower():
                        memory_info = proc.info['memory_info']

                        return {
                            "pid": proc.info['pid'],
                            "cpu_percent": proc.info['cpu_percent'],
                            "memory_mb": memory_info.rss / 1024 / 1024 if memory_info else 0,
                            "threads": proc.info['num_threads']
                        }
                except (psutil.NoSuchProcess, psutil.AccessDenied):
                    continue

            return None
        except Exception as e:
            self.logger.error(f"Error getting process metrics for {service_name}: {e}")
            return None

    def get_system_metrics(self) -> Dict:
        """Get overall system performance metrics"""
        try:
            cpu_percent = psutil.cpu_percent(interval=1)
            memory = psutil.virtual_memory()
            disk = psutil.disk_usage('/')

            return {
                "cpu_percent": cpu_percent,
                "memory_percent": memory.percent,
                "memory_used_gb": memory.used / 1024 / 1024 / 1024,
                "memory_total_gb": memory.total / 1024 / 1024 / 1024,
                "disk_percent": disk.percent,
                "disk_used_gb": disk.used / 1024 / 1024 / 1024,
                "disk_total_gb": disk.total / 1024 / 1024 / 1024,
                "timestamp": datetime.now().isoformat()
            }
        except Exception as e:
            self.logger.error(f"Error getting system metrics: {e}")
            return {}

    def monitor_all_services(self) -> List[Dict]:
        """Monitor all configured services"""
        results = []

        self.logger.info("Starting performance monitoring cycle")

        for service_name, port_str in self.services.items():
            try:
                port = int(port_str)
                metrics = self.get_service_metrics(service_name, port)
                if metrics:
                    results.append(metrics)
                    self.logger.info(f"Monitored {service_name}: {metrics['status']}")
                else:
                    self.logger.warning(f"Failed to monitor {service_name}")
            except ValueError as e:
                self.logger.error(f"Invalid port for {service_name}: {port_str}")
            except Exception as e:
                self.logger.error(f"Error monitoring {service_name}: {e}")

        # Add system metrics
        system_metrics = self.get_system_metrics()
        if system_metrics:
            results.append({
                "service": "system",
                "status": "monitoring",
                "system_metrics": system_metrics
            })

        return results

    def save_metrics_to_db(self, metrics: List[Dict]) -> bool:
        """Save metrics to database"""
        try:
            # This would save to performance_metrics table
            # For now, just log the metrics
            self.logger.info(f"Saving {len(metrics)} metric records to database")

            # In production, this would insert into database
            # self.db.insert_performance_metrics(metrics)

            return True
        except Exception as e:
            self.logger.error(f"Error saving metrics to DB: {e}")
            return False

    def generate_report(self, metrics: List[Dict]) -> Dict:
        """Generate performance report"""
        healthy_services = 0
        total_response_time = 0
        response_count = 0

        service_status = {}

        for metric in metrics:
            if metric.get('service') == 'system':
                continue

            service_name = metric['service']
            status = metric['status']

            service_status[service_name] = {
                "status": status,
                "response_time_ms": metric.get('response_time_ms', 0),
                "timestamp": metric.get('timestamp')
            }

            if status == 'healthy':
                healthy_services += 1

            if 'response_time_ms' in metric:
                total_response_time += metric['response_time_ms']
                response_count += 1

        avg_response_time = total_response_time / response_count if response_count > 0 else 0

        return {
            "timestamp": datetime.now().isoformat(),
            "total_services": len([m for m in metrics if m.get('service') != 'system']),
            "healthy_services": healthy_services,
            "average_response_time_ms": avg_response_time,
            "service_status": service_status,
            "system_metrics": next((m.get('system_metrics') for m in metrics if m.get('service') == 'system'), {})
        }

    def run_monitoring_cycle(self) -> Dict:
        """Run a complete monitoring cycle"""
        try:
            self.logger.info("Starting monitoring cycle")

            # Monitor all services
            metrics = self.monitor_all_services()

            # Save to database
            self.save_metrics_to_db(metrics)

            # Generate report
            report = self.generate_report(metrics)

            # Store in history
            self.metrics_history.append({
                "timestamp": datetime.now().isoformat(),
                "metrics": metrics,
                "report": report
            })

            # Keep only last 100 cycles
            if len(self.metrics_history) > 100:
                self.metrics_history = self.metrics_history[-100:]

            self.logger.info(f"Monitoring cycle completed. Healthy services: {report['healthy_services']}/{report['total_services']}")

            return report

        except Exception as e:
            self.logger.error(f"Error in monitoring cycle: {e}")
            return {"error": str(e)}

def main():
    """Main monitoring function"""
    # Configure services to monitor
    services_config = {
        "world-cities-service": "8080",
        "world-regions-service": "8081",
        "notification-system-service": "8082",
        "achievement-system-service": "8083",
        "mail-system-service": "8084",
        "session-management-service": "8085",
        "clan-war-service": "8086",
        "quest-engine-service": "8087",
        # Add more services as needed
    }

    # Initialize monitor
    monitor = PerformanceMonitor(services_config)

    print("[INFO] Starting Performance Monitoring System for NECPGAME")
    print("[INFO] Monitoring services:", list(services_config.keys()))

    try:
        while True:
            report = monitor.run_monitoring_cycle()

            # Print summary
            healthy = report.get('healthy_services', 0)
            total = report.get('total_services', 0)
            avg_response = report.get('average_response_time_ms', 0)

            print(f"[OK] Monitoring cycle completed - {healthy}/{total} services healthy, avg response: {avg_response:.2f}ms")

            # Print service status
            for service, status in report.get('service_status', {}).items():
                status_icon = "✓" if status['status'] == 'healthy' else "✗"
                response_time = status.get('response_time_ms', 0)
                print(f"  {status_icon} {service}: {status['status']} ({response_time:.2f}ms)")

            # Print system metrics
            sys_metrics = report.get('system_metrics', {})
            if sys_metrics:
                cpu = sys_metrics.get('cpu_percent', 0)
                mem = sys_metrics.get('memory_percent', 0)
                print(f"  📊 System: CPU {cpu:.1f}%, Memory {mem:.1f}%")

            # Wait before next cycle (30 seconds)
            time.sleep(30)

    except KeyboardInterrupt:
        print("\n[INFO] Monitoring stopped by user")
    except Exception as e:
        print(f"[ERROR] Monitoring failed: {e}")
        sys.exit(1)

if __name__ == "__main__":
    main()
