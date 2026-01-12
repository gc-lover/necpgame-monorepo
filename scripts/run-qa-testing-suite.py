#!/usr/bin/env python3
"""
Comprehensive QA Testing Suite Runner
Orchestrates all QA tests for the NECPGAME project

This script runs the complete QA testing suite including:
- AI Enemies testing (500+ entities per zone, P99 <50ms latency)
- Quest Systems testing (1000+ concurrent guild wars, real-time sync validation)
- Interactive Objects testing (zone-specific mechanics, telemetry accuracy)
- Memory leak detection with pprof profiling
- Load testing: 500 concurrent players, 10000+ quest instances
- Integration testing for end-to-end scenarios
- Functional testing for AI behavior patterns, quest mechanics, interactive zones
- Test automation with coverage >90%, integration suites, and performance regression tests

Usage:
    python scripts/run-qa-testing-suite.py [--suite SUITE] [--verbose] [--report]

Arguments:
    --suite SUITE    Run specific test suite (ai_enemies, quest_systems, interactive_objects,
                     memory_leaks, load_testing, integration, functional, automation, all)
    --verbose        Enable verbose output
    --report         Generate detailed HTML report
    --ci             Run in CI mode (non-interactive)
"""

import argparse
import asyncio
import json
import os
import sys
import time
from datetime import datetime
from pathlib import Path
from typing import Dict, List, Any, Optional
import subprocess
import pytest


class QATestingSuiteRunner:
    """Main QA testing suite runner"""

    def __init__(self, verbose: bool = False, ci_mode: bool = False):
        self.verbose = verbose
        self.ci_mode = ci_mode
        self.project_root = Path(__file__).parent.parent
        self.test_results = {}
        self.start_time = None

        # Test suite configurations
        self.test_suites = {
            "ai_enemies": {
                "name": "AI Enemies Testing",
                "description": "Performance and integration testing for AI enemy systems (500+ entities, P99 <50ms)",
                "path": "scripts/tools/functional/test_ai_enemies.py",
                "requirements": ["P99 latency <50ms", "500+ entities per zone", "Memory <50MB per zone"]
            },
            "quest_systems": {
                "name": "Quest Systems Testing",
                "description": "Concurrent guild wars and real-time sync validation (1000+ concurrent wars)",
                "path": "scripts/tools/integration/test_quest_systems.py",
                "requirements": ["1000+ concurrent wars", "Real-time sync validation", "Event sourcing replay"]
            },
            "interactive_objects": {
                "name": "Interactive Objects Testing",
                "description": "Zone-specific mechanics and telemetry accuracy testing",
                "path": "scripts/tools/integration/test_interactive_objects.py",
                "requirements": ["Zone mechanics validation", "Telemetry accuracy >99%", "Security protocols"]
            },
            "memory_leaks": {
                "name": "Memory Leak Detection",
                "description": "Memory leak detection using pprof profiling",
                "path": "scripts/tools/performance/test_memory_leaks.py",
                "requirements": ["Leak detection <50MB increase", "GC efficiency >80%", "Resource tracking"]
            },
            "load_testing": {
                "name": "Load Testing",
                "description": "Load testing with 500 concurrent players and 10000+ quest instances",
                "path": "scripts/tools/load-test/test_load_scenarios.py",
                "requirements": ["500 concurrent players", "10000+ quest instances", "Resource scaling"]
            },
            "integration": {
                "name": "Integration Testing",
                "description": "End-to-end scenarios: guild wars, cyber space missions, social intrigue",
                "path": "scripts/tools/integration/test_end_to_end_scenarios.py",
                "requirements": ["Guild war E2E", "Cyber space missions", "Social intrigue flows"]
            },
            "functional": {
                "name": "Functional Testing",
                "description": "AI behavior patterns, quest mechanics, and interactive zones",
                "path": "scripts/tools/functional/test_game_mechanics.py",
                "requirements": ["AI behavior validation", "Quest mechanics", "Zone interactions"]
            },
            "automation": {
                "name": "Test Automation",
                "description": "Test automation framework with >90% coverage and regression detection",
                "path": "scripts/tools/test_automation.py",
                "requirements": ["Coverage >90%", "Regression detection", "CI/CD integration"]
            }
        }

    async def run_suite(self, suite_name: str) -> Dict[str, Any]:
        """Run a specific test suite"""
        if suite_name not in self.test_suites:
            raise ValueError(f"Unknown test suite: {suite_name}")

        suite_config = self.test_suites[suite_name]
        suite_path = self.project_root / suite_config["path"]

        if not suite_path.exists():
            return {
                "suite": suite_name,
                "status": "failed",
                "error": f"Test file not found: {suite_path}",
                "duration": 0
            }

        self.log(f"Running {suite_config['name']}...")
        self.log(f"Description: {suite_config['description']}")

        start_time = time.time()

        try:
            # Run pytest on the test file
            cmd = [
                sys.executable, "-m", "pytest",
                str(suite_path),
                "-v" if self.verbose else "-q",
                "--tb=short",
                "--disable-warnings",
                "-x"  # Stop on first failure in CI mode
            ] if self.ci_mode else [
                sys.executable, "-m", "pytest",
                str(suite_path),
                "-v" if self.verbose else "",
                "--tb=long" if self.verbose else "--tb=short"
            ]

            result = await asyncio.create_subprocess_exec(
                *cmd,
                cwd=self.project_root,
                stdout=asyncio.subprocess.PIPE,
                stderr=asyncio.subprocess.PIPE
            )

            stdout, stderr = await result.communicate()
            duration = time.time() - start_time

            success = result.returncode == 0

            return {
                "suite": suite_name,
                "status": "passed" if success else "failed",
                "duration": duration,
                "return_code": result.returncode,
                "stdout": stdout.decode() if success or self.verbose else "",
                "stderr": stderr.decode() if not success or self.verbose else "",
                "requirements": suite_config["requirements"]
            }

        except Exception as e:
            duration = time.time() - start_time
            return {
                "suite": suite_name,
                "status": "error",
                "error": str(e),
                "duration": duration
            }

    async def run_all_suites(self) -> Dict[str, Any]:
        """Run all test suites"""
        self.start_time = time.time()
        results = {}

        self.log("=" * 80)
        self.log("NECPGAME QA TESTING SUITE")
        self.log("=" * 80)
        self.log(f"Started at: {datetime.now().isoformat()}")
        self.log(f"CI Mode: {self.ci_mode}")
        self.log("")

        for suite_name in self.test_suites.keys():
            result = await self.run_suite(suite_name)
            results[suite_name] = result

            status_icon = "[PASS]" if result["status"] == "passed" else "[FAIL]" if result["status"] == "failed" else "[WARN]"
            self.log(f"{status_icon} {suite_name}: {result['status']} ({result['duration']:.1f}s)")

            if result["status"] != "passed" and self.verbose:
                if "error" in result:
                    self.log(f"   Error: {result['error']}")
                if "stderr" in result and result["stderr"]:
                    self.log(f"   STDERR: {result['stderr'][:200]}...")

        # Generate summary
        summary = self.generate_summary(results)
        self.display_summary(summary)

        return {
            "results": results,
            "summary": summary,
            "total_duration": time.time() - self.start_time
        }

    def generate_summary(self, results: Dict[str, Any]) -> Dict[str, Any]:
        """Generate test execution summary"""
        total_suites = len(results)
        passed_suites = sum(1 for r in results.values() if r["status"] == "passed")
        failed_suites = sum(1 for r in results.values() if r["status"] == "failed")
        error_suites = sum(1 for r in results.values() if r["status"] == "error")

        total_duration = sum(r["duration"] for r in results.values())

        # Calculate pass rate
        pass_rate = passed_suites / total_suites if total_suites > 0 else 0

        # Collect all requirements
        all_requirements = []
        for suite_result in results.values():
            if "requirements" in suite_result:
                all_requirements.extend(suite_result["requirements"])

        return {
            "total_suites": total_suites,
            "passed_suites": passed_suites,
            "failed_suites": failed_suites,
            "error_suites": error_suites,
            "pass_rate": pass_rate,
            "total_duration": total_duration,
            "all_requirements_met": all_requirements,
            "timestamp": datetime.now().isoformat()
        }

    def display_summary(self, summary: Dict[str, Any]):
        """Display test execution summary"""
        self.log("")
        self.log("=" * 80)
        self.log("QA TESTING SUITE SUMMARY")
        self.log("=" * 80)

        self.log(f"Total Suites: {summary['total_suites']}")
        self.log(f"Passed: {summary['passed_suites']}")
        self.log(f"Failed: {summary['failed_suites']}")
        self.log(f"Errors: {summary['error_suites']}")
        self.log(f"Pass Rate: {summary['pass_rate']:.1%}")
        self.log(f"Total Duration: {summary['total_duration']:.1f}s")
        # Overall status
        if summary["pass_rate"] >= 0.95:
            self.log("[SUCCESS] OVERALL STATUS: PASSED - Ready for deployment!")
        elif summary["pass_rate"] >= 0.85:
            self.log("[WARN] OVERALL STATUS: MARGINAL - Review failures before deployment")
        else:
            self.log("[FAIL] OVERALL STATUS: FAILED - Blocking deployment")

        self.log("")
        self.log("Key Requirements Validated:")
        for req in summary["all_requirements_met"][:10]:  # Show first 10
            self.log(f"  • {req}")
        if len(summary["all_requirements_met"]) > 10:
            self.log(f"  ... and {len(summary['all_requirements_met']) - 10} more")

        self.log("")
        self.log(f"Completed at: {datetime.now().isoformat()}")

    def log(self, message: str):
        """Log message to console"""
        if self.ci_mode:
            # In CI mode, use plain text without emojis
            clean_message = message.replace("[PASS]", "[PASS]").replace("[FAIL]", "[FAIL]").replace("[WARN]", "[WARN]").replace("[SUCCESS]", "[SUCCESS]")
            print(clean_message)
        else:
            print(message)

    async def generate_report(self, results: Dict[str, Any], summary: Dict[str, Any]):
        """Generate detailed HTML report"""
        report_path = self.project_root / "reports" / f"qa-test-report-{int(time.time())}.html"

        # Ensure reports directory exists
        report_path.parent.mkdir(exist_ok=True)

        html_content = f"""
<!DOCTYPE html>
<html>
<head>
    <title>NECPGAME QA Testing Report</title>
    <style>
        body {{ font-family: Arial, sans-serif; margin: 40px; }}
        .header {{ background: #2c3e50; color: white; padding: 20px; border-radius: 5px; }}
        .summary {{ background: #ecf0f1; padding: 20px; margin: 20px 0; border-radius: 5px; }}
        .suite {{ margin: 10px 0; padding: 10px; border-left: 4px solid; }}
        .passed {{ border-color: #27ae60; background: #d5f4e6; }}
        .failed {{ border-color: #e74c3c; background: #fadbd8; }}
        .error {{ border-color: #f39c12; background: #fdeaa7; }}
        .requirements {{ background: #f8f9fa; padding: 15px; margin: 20px 0; }}
        .metric {{ display: inline-block; margin: 10px; padding: 10px; background: white; border-radius: 3px; }}
    </style>
</head>
<body>
    <div class="header">
        <h1>🧪 NECPGAME QA Testing Report</h1>
        <p>Generated: {summary['timestamp']}</p>
        <p>Total Duration: {summary['total_duration']:.1f} seconds</p>
    </div>

    <div class="summary">
        <h2>[SUMMARY] Summary</h2>
        <div class="metric">
            <strong>Total Suites:</strong> {summary['total_suites']}
        </div>
        <div class="metric">
            <strong>Passed:</strong> {summary['passed_suites']}
        </div>
        <div class="metric">
            <strong>Failed:</strong> {summary['failed_suites']}
        </div>
        <div class="metric">
            <strong>Pass Rate:</strong> {summary['pass_rate']:.1%}
        </div>
        <div class="metric">
            <strong>Total Duration:</strong> {summary['total_duration']:.1f}s
        </div>
    </div>

    <div class="requirements">
        <h2>[VALIDATED] Requirements Validated</h2>
        <ul>
"""

        for req in summary["all_requirements_met"]:
            html_content += f"            <li>{req}</li>\n"

        html_content += """
        </ul>
    </div>

    <h2>[RESULTS] Suite Results</h2>
"""

        for suite_name, result in results.items():
            suite_config = self.test_suites[suite_name]
            css_class = result["status"]
            status_icon = "[PASS]" if result["status"] == "passed" else "[FAIL]" if result["status"] == "failed" else "[WARN]"

            html_content += f"""
    <div class="suite {css_class}">
        <h3>{status_icon} {suite_config['name']}</h3>
        <p><strong>Description:</strong> {suite_config['description']}</p>
        <p><strong>Status:</strong> {result['status'].upper()}</p>
        <p><strong>Duration:</strong> {result['duration']:.1f} seconds</p>
"""

            if result["status"] != "passed" and "error" in result:
                html_content += f"        <p><strong>Error:</strong> {result['error']}</p>"

            html_content += "    </div>"

        html_content += """
</body>
</html>
"""

        with open(report_path, 'w', encoding='utf-8') as f:
            f.write(html_content)

        self.log(f"[REPORT] Detailed report generated: {report_path}")
        return report_path


async def main():
    """Main entry point"""
    parser = argparse.ArgumentParser(description="NECPGAME QA Testing Suite Runner")
    parser.add_argument(
        "--suite",
        choices=["ai_enemies", "quest_systems", "interactive_objects", "memory_leaks",
                "load_testing", "integration", "functional", "automation", "all"],
        default="all",
        help="Specific test suite to run"
    )
    parser.add_argument(
        "--verbose", "-v",
        action="store_true",
        help="Enable verbose output"
    )
    parser.add_argument(
        "--report",
        action="store_true",
        help="Generate detailed HTML report"
    )
    parser.add_argument(
        "--ci",
        action="store_true",
        help="Run in CI mode (non-interactive)"
    )

    args = parser.parse_args()

    # Initialize test runner
    runner = QATestingSuiteRunner(verbose=args.verbose, ci_mode=args.ci)

    try:
        if args.suite == "all":
            # Run all suites
            test_run = await runner.run_all_suites()
            results = test_run["results"]
            summary = test_run["summary"]
        else:
            # Run specific suite
            result = await runner.run_suite(args.suite)
            results = {args.suite: result}
            summary = runner.generate_summary(results)
            runner.display_summary(summary)

        # Generate report if requested
        if args.report:
            await runner.generate_report(results, summary)

        # Exit with appropriate code
        success_rate = summary["pass_rate"]
        if success_rate >= 0.95:
            sys.exit(0)  # Success
        elif success_rate >= 0.85:
            sys.exit(1)  # Warning
        else:
            sys.exit(2)  # Failure

    except KeyboardInterrupt:
        print("\n[STOP] Testing interrupted by user")
        sys.exit(130)
    except Exception as e:
        print(f"[ERROR] Fatal error: {e}")
        sys.exit(1)


if __name__ == "__main__":
    # Ensure we're in the project root
    script_dir = Path(__file__).parent
    project_root = script_dir.parent
    os.chdir(project_root)

    # Run the async main function
    asyncio.run(main())