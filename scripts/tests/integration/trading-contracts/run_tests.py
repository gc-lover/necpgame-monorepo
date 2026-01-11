#!/usr/bin/env python3
"""
Test Runner for Trading Contracts System Integration Tests

Runs all integration tests and generates comprehensive test report.

Issue: #2202 - Тестирование системы контрактов и сделок
"""

import subprocess
import sys
import os
import json
import time
from datetime import datetime
from typing import Dict, List, Any
import argparse

class TestRunner:
    """Test runner for trading contracts integration tests"""

    def __init__(self):
        self.test_results = {}
        self.start_time = None
        self.end_time = None

    def run_test_file(self, test_file: str, test_name: str) -> Dict[str, Any]:
        """Run a single test file"""
        print(f"\n🏃 Running {test_name}...")
        print("-" * 50)

        start_time = time.time()

        try:
            # Run the test file
            result = subprocess.run([
                sys.executable, test_file
            ], capture_output=True, text=True, timeout=300)  # 5 minute timeout

            end_time = time.time()
            duration = end_time - start_time

            test_result = {
                "test_name": test_name,
                "file": test_file,
                "passed": result.returncode == 0,
                "return_code": result.returncode,
                "duration_seconds": duration,
                "stdout": result.stdout,
                "stderr": result.stderr,
                "timestamp": datetime.now().isoformat()
            }

            if test_result["passed"]:
                print(f"✅ {test_name} PASSED ({duration:.2f}s)")
            else:
                print(f"❌ {test_name} FAILED ({duration:.2f}s)")
                if result.stderr:
                    print(f"Error output:\n{result.stderr}")

            return test_result

        except subprocess.TimeoutExpired:
            end_time = time.time()
            duration = end_time - start_time
            print(f"⏰ {test_name} TIMEOUT ({duration:.2f}s)")
            return {
                "test_name": test_name,
                "file": test_file,
                "passed": False,
                "return_code": -1,
                "duration_seconds": duration,
                "stdout": "",
                "stderr": "Test timed out after 300 seconds",
                "timestamp": datetime.now().isoformat(),
                "error": "timeout"
            }

        except Exception as e:
            end_time = time.time()
            duration = end_time - start_time
            print(f"💥 {test_name} ERROR ({duration:.2f}s): {e}")
            return {
                "test_name": test_name,
                "file": test_file,
                "passed": False,
                "return_code": -1,
                "duration_seconds": duration,
                "stdout": "",
                "stderr": str(e),
                "timestamp": datetime.now().isoformat(),
                "error": str(e)
            }

    def run_all_tests(self) -> Dict[str, Any]:
        """Run all integration tests"""
        self.start_time = time.time()

        # Define test files to run
        test_files = [
            ("test_contract_trading_integration.py", "Trading Contracts Integration Tests"),
            ("test_contract_performance.py", "Trading Contracts Performance Tests"),
            ("test_contract_api_spec.py", "Trading Contracts API Specification Tests"),
            ("test_contract_integration.py", "Trading Contracts External Integration Tests")
        ]

        results = {}
        passed_tests = 0
        total_tests = len(test_files)

        print("🚀 Starting Trading Contracts Integration Test Suite")
        print("=" * 80)
        print(f"Test Environment: {os.environ.get('TEST_ENV', 'development')}")
        print(f"Service URL: {os.environ.get('TEST_SERVICE_URL', 'http://localhost:8088')}")
        print(f"Total Tests: {total_tests}")
        print("=" * 80)

        for test_file, test_name in test_files:
            test_path = os.path.join(os.path.dirname(__file__), test_file)

            if not os.path.exists(test_path):
                print(f"⚠️  Test file not found: {test_path}")
                results[test_name] = {
                    "test_name": test_name,
                    "file": test_file,
                    "passed": False,
                    "error": "file_not_found"
                }
                continue

            result = self.run_test_file(test_path, test_name)
            results[test_name] = result

            if result["passed"]:
                passed_tests += 1

        self.end_time = time.time()
        total_duration = self.end_time - self.start_time

        # Generate summary
        summary = {
            "test_suite": "Trading Contracts Integration Tests",
            "timestamp": datetime.now().isoformat(),
            "total_tests": total_tests,
            "passed_tests": passed_tests,
            "failed_tests": total_tests - passed_tests,
            "pass_rate": passed_tests / total_tests if total_tests > 0 else 0,
            "total_duration_seconds": total_duration,
            "average_test_duration": total_duration / total_tests if total_tests > 0 else 0,
            "results": results
        }

        return summary

    def generate_report(self, summary: Dict[str, Any]) -> str:
        """Generate test report"""
        report = []
        report.append("📊 Trading Contracts Integration Test Report")
        report.append("=" * 80)
        report.append(f"Test Suite: {summary['test_suite']}")
        report.append(f"Timestamp: {summary['timestamp']}")
        report.append("")
        report.append("📈 Summary:")
        report.append(f"  Total Tests: {summary['total_tests']}")
        report.append(f"  Passed: {summary['passed_tests']}")
        report.append(f"  Failed: {summary['failed_tests']}")
        report.append(".1%")
        report.append(".2f")
        report.append(".2f")
        report.append("")
        report.append("📋 Detailed Results:")
        report.append("-" * 80)

        for test_name, result in summary['results'].items():
            status_icon = "✅" if result.get('passed', False) else "❌"
            duration = result.get('duration_seconds', 0)
            report.append(f"{status_icon} {test_name}")
            report.append(".2f")

            if not result.get('passed', False):
                error = result.get('error') or result.get('stderr', 'Unknown error')
                if error and len(error) > 100:
                    error = error[:100] + "..."
                report.append(f"    Error: {error}")

            report.append("")

        # Performance assessment
        pass_rate = summary['pass_rate']
        if pass_rate >= 0.95:
            assessment = "EXCELLENT - All systems operational"
        elif pass_rate >= 0.80:
            assessment = "GOOD - Minor issues detected"
        elif pass_rate >= 0.60:
            assessment = "FAIR - Significant issues require attention"
        else:
            assessment = "CRITICAL - Major failures detected"

        report.append("🎯 Assessment:")
        report.append(f"  {assessment}")
        report.append("")

        # Recommendations
        report.append("💡 Recommendations:")
        failed_tests = [name for name, result in summary['results'].items() if not result.get('passed', False)]
        if failed_tests:
            report.append("  Failed tests require investigation:")
            for test in failed_tests:
                report.append(f"    - {test}")
        else:
            report.append("  All tests passed - system ready for production")

        report.append("")
        report.append("=" * 80)

        return "\n".join(report)

    def save_report(self, summary: Dict[str, Any], output_file: str = None):
        """Save test results to file"""
        if not output_file:
            timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")
            output_file = f"integration_test_results_{timestamp}.json"

        # Save JSON results
        with open(output_file, 'w', encoding='utf-8') as f:
            json.dump(summary, f, indent=2, ensure_ascii=False)

        # Save text report
        text_report_file = output_file.replace('.json', '.txt')
        text_report = self.generate_report(summary)

        with open(text_report_file, 'w', encoding='utf-8') as f:
            f.write(text_report)

        print(f"📄 Results saved to: {output_file}")
        print(f"📄 Report saved to: {text_report_file}")

        return output_file, text_report_file

def main():
    """Main test runner function"""
    parser = argparse.ArgumentParser(description='Run Trading Contracts Integration Tests')
    parser.add_argument('--output', '-o', help='Output file for test results')
    parser.add_argument('--verbose', '-v', action='store_true', help='Verbose output')
    parser.add_argument('--single-test', help='Run only specified test file')

    args = parser.parse_args()

    # Set environment variables for tests
    os.environ.setdefault('TEST_SERVICE_URL', 'http://localhost:8088')
    os.environ.setdefault('TEST_DATABASE_URL', 'postgresql://test:test@localhost:5432/test_trading')
    os.environ.setdefault('TEST_REDIS_URL', 'redis://localhost:6379')
    os.environ.setdefault('TEST_ENV', 'integration_test')

    runner = TestRunner()

    if args.single_test:
        # Run single test
        test_path = os.path.join(os.path.dirname(__file__), args.single_test)
        result = runner.run_test_file(test_path, args.single_test)

        if result['passed']:
            print(f"\n✅ Single test {args.single_test} PASSED")
            sys.exit(0)
        else:
            print(f"\n❌ Single test {args.single_test} FAILED")
            if args.verbose and result.get('stderr'):
                print(f"Error details:\n{result['stderr']}")
            sys.exit(1)

    else:
        # Run all tests
        summary = runner.run_all_tests()

        # Print summary to console
        report = runner.generate_report(summary)
        print("\n" + report)

        # Save results
        output_file = args.output or f"integration_test_results_{int(time.time())}.json"
        json_file, txt_file = runner.save_report(summary, output_file)

        # Exit with appropriate code
        if summary['pass_rate'] >= 0.80:  # 80% pass rate required
            print("✅ Integration tests PASSED")
            sys.exit(0)
        else:
            print("❌ Integration tests FAILED")
            sys.exit(1)

if __name__ == "__main__":
    main()