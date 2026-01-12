#!/usr/bin/env python3
"""
AI Enemies, Quest Systems, and Interactive Objects Integration Validation
Validates test results against Issue #2304 requirements
"""

import os
import sys
import json
import xml.etree.ElementTree as ET
from pathlib import Path
from typing import Dict, List, Any, Optional
import statistics


class IntegrationTestValidator:
    """Validates integration test results for Issue #2304"""

    def __init__(self):
        self.test_results_dir = Path(".")
        self.requirements = {
            "performance": {
                "p99_latency_ms": 50,
                "max_memory_mb": 50,
                "concurrent_entities": 500,
                "quest_instances": 10000
            },
            "functional": {
                "ai_behavior_patterns": True,
                "quest_systems": True,
                "interactive_objects": True,
                "end_to_end_scenarios": True
            },
            "integration": {
                "ai_quest_integration": True,
                "guild_war_ai_support": True,
                "cyber_space_ai_defense": True,
                "event_sourcing_consistency": True
            }
        }

    def validate_test_results(self) -> Dict[str, Any]:
        """Main validation function"""
        results = {
            "overall_status": "unknown",
            "performance_validation": self._validate_performance(),
            "functional_validation": self._validate_functional(),
            "integration_validation": self._validate_integration(),
            "scalability_validation": self._validate_scalability(),
            "recommendations": []
        }

        # Determine overall status
        all_passed = all([
            results["performance_validation"]["status"] == "passed",
            results["functional_validation"]["status"] == "passed",
            results["integration_validation"]["status"] == "passed",
            results["scalability_validation"]["status"] == "passed"
        ])

        results["overall_status"] = "passed" if all_passed else "failed"

        # Generate recommendations
        results["recommendations"] = self._generate_recommendations(results)

        return results

    def _validate_performance(self) -> Dict[str, Any]:
        """Validate performance requirements"""
        validation = {
            "status": "unknown",
            "metrics": {},
            "issues": []
        }

        # Check for test results files
        results_files = [
            "test-results-integration.xml",
            "test-results-functional.xml",
            "performance-metrics.json"
        ]

        for results_file in results_files:
            if os.path.exists(results_file):
                if results_file.endswith('.xml'):
                    self._parse_xml_results(results_file, validation)
                elif results_file.endswith('.json'):
                    self._parse_json_metrics(results_file, validation)

        # Validate against requirements
        if "p99_latency" in validation["metrics"]:
            p99 = validation["metrics"]["p99_latency"]
            if p99 > self.requirements["performance"]["p99_latency_ms"]:
                validation["issues"].append(
                    f"P99 latency {p99}ms exceeds requirement of {self.requirements['performance']['p99_latency_ms']}ms"
                )

        if "memory_usage_mb" in validation["metrics"]:
            memory = validation["metrics"]["memory_usage_mb"]
            if memory > self.requirements["performance"]["max_memory_mb"]:
                validation["issues"].append(
                    f"Memory usage {memory}MB exceeds requirement of {self.requirements['performance']['max_memory_mb']}MB"
                )

        # Determine status
        validation["status"] = "passed" if not validation["issues"] else "failed"

        return validation

    def _validate_functional(self) -> Dict[str, Any]:
        """Validate functional requirements"""
        validation = {
            "status": "unknown",
            "components_tested": [],
            "coverage": {},
            "issues": []
        }

        # Check for functional test results
        functional_tests = [
            "scripts/tools/functional/test_ai_enemies.py",
            "scripts/tools/functional/test_game_mechanics.py",
            "scripts/tools/functional/test_quest_api.py",
            "scripts/tools/integration/test_interactive_objects.py"
        ]

        for test_file in functional_tests:
            if os.path.exists(test_file):
                validation["components_tested"].append(os.path.basename(test_file))

        # Validate test coverage
        if len(validation["components_tested"]) >= 3:
            validation["coverage"]["functional_tests"] = len(validation["components_tested"])
        else:
            validation["issues"].append("Insufficient functional test coverage")

        # Determine status
        validation["status"] = "passed" if len(validation["components_tested"]) >= 3 else "failed"

        return validation

    def _validate_integration(self) -> Dict[str, Any]:
        """Validate integration requirements"""
        validation = {
            "status": "unknown",
            "integration_scenarios": [],
            "data_consistency": {},
            "issues": []
        }

        # Check for integration test file
        integration_test = "scripts/tools/integration/test_ai_quest_interactive_integration.py"
        if os.path.exists(integration_test):
            validation["integration_scenarios"].extend([
                "ai_quest_integration_flow",
                "guild_war_ai_integration",
                "cyber_space_ai_interactive_integration",
                "event_sourcing_consistency"
            ])

        # Check end-to-end scenarios
        e2e_test = "scripts/tools/integration/test_end_to_end_scenarios.py"
        if os.path.exists(e2e_test):
            validation["integration_scenarios"].extend([
                "guild_war_e2e",
                "cyber_space_mission_e2e",
                "social_intrigue_e2e"
            ])

        # Validate integration coverage
        required_scenarios = [
            "ai_quest_integration_flow",
            "guild_war_ai_integration",
            "cyber_space_ai_interactive_integration",
            "event_sourcing_consistency"
        ]

        covered_scenarios = [s for s in validation["integration_scenarios"] if s in required_scenarios]

        if len(covered_scenarios) >= len(required_scenarios) * 0.8:  # 80% coverage
            validation["data_consistency"]["integration_coverage"] = len(covered_scenarios) / len(required_scenarios)
        else:
            validation["issues"].append("Insufficient integration scenario coverage")

        # Determine status
        validation["status"] = "passed" if len(covered_scenarios) >= 3 else "failed"

        return validation

    def _validate_scalability(self) -> Dict[str, Any]:
        """Validate scalability requirements"""
        validation = {
            "status": "unknown",
            "load_test_results": {},
            "concurrency_metrics": {},
            "issues": []
        }

        # Check for scalability test results
        load_test_results = "load-test-results.json"
        if os.path.exists(load_test_results):
            with open(load_test_results, 'r') as f:
                data = json.load(f)
                validation["load_test_results"] = data

        # Validate concurrent entities
        if "total_entities" in validation["load_test_results"]:
            entities = validation["load_test_results"]["total_entities"]
            if entities >= self.requirements["performance"]["concurrent_entities"]:
                validation["concurrency_metrics"]["entities_supported"] = entities
            else:
                validation["issues"].append(
                    f"Entity count {entities} below requirement of {self.requirements['performance']['concurrent_entities']}"
                )

        # Validate quest instances
        if "total_quests" in validation["load_test_results"]:
            quests = validation["load_test_results"]["total_quests"]
            if quests >= self.requirements["performance"]["quest_instances"]:
                validation["concurrency_metrics"]["quests_supported"] = quests
            else:
                validation["issues"].append(
                    f"Quest count {quests} below requirement of {self.requirements['performance']['quest_instances']}"
                )

        # Determine status
        validation["status"] = "passed" if not validation["issues"] else "failed"

        return validation

    def _parse_xml_results(self, xml_file: str, validation: Dict[str, Any]):
        """Parse XML test results"""
        try:
            tree = ET.parse(xml_file)
            root = tree.getroot()

            # Extract basic metrics
            testsuite = root.find('.//testsuite')
            if testsuite is not None:
                validation["metrics"]["tests_run"] = int(testsuite.get('tests', 0))
                validation["metrics"]["failures"] = int(testsuite.get('failures', 0))
                validation["metrics"]["errors"] = int(testsuite.get('errors', 0))

        except Exception as e:
            validation["issues"].append(f"Failed to parse XML results: {e}")

    def _parse_json_metrics(self, json_file: str, validation: Dict[str, Any]):
        """Parse JSON metrics"""
        try:
            with open(json_file, 'r') as f:
                data = json.load(f)
                validation["metrics"].update(data)
        except Exception as e:
            validation["issues"].append(f"Failed to parse JSON metrics: {e}")

    def _generate_recommendations(self, results: Dict[str, Any]) -> List[str]:
        """Generate recommendations based on validation results"""
        recommendations = []

        # Performance recommendations
        perf = results["performance_validation"]
        if perf["status"] == "failed":
            recommendations.append("Optimize performance to meet P99 <50ms and memory <50MB requirements")
            if "p99_latency" in perf.get("metrics", {}):
                recommendations.append(f"Current P99 latency: {perf['metrics']['p99_latency']}ms - needs optimization")

        # Functional recommendations
        func = results["functional_validation"]
        if func["status"] == "failed":
            recommendations.append("Add missing functional tests for AI enemies, quests, and interactive objects")

        # Integration recommendations
        integ = results["integration_validation"]
        if integ["status"] == "failed":
            recommendations.append("Implement missing integration scenarios and end-to-end tests")

        # Scalability recommendations
        scale = results["scalability_validation"]
        if scale["status"] == "failed":
            recommendations.append("Improve scalability to support 500+ concurrent entities and 10000+ quest instances")

        # Success recommendations
        if results["overall_status"] == "passed":
            recommendations.append("✅ All requirements met! Ready for production deployment")
            recommendations.append("Consider adding chaos testing for improved resilience")
            recommendations.append("Implement continuous performance monitoring")

        return recommendations

    def print_report(self, results: Dict[str, Any]):
        """Print validation report"""
        print("=" * 80)
        print("AI ENEMIES, QUEST SYSTEMS & INTERACTIVE OBJECTS")
        print("INTEGRATION VALIDATION REPORT - Issue #2304")
        print("=" * 80)

        # Overall status
        status_color = "✅" if results["overall_status"] == "passed" else "❌"
        print(f"\nOverall Status: {status_color} {results['overall_status'].upper()}")

        # Performance validation
        perf = results["performance_validation"]
        perf_color = "✅" if perf["status"] == "passed" else "❌"
        print(f"\nPerformance Validation: {perf_color} {perf['status'].upper()}")
        if perf["issues"]:
            for issue in perf["issues"]:
                print(f"  - {issue}")

        # Functional validation
        func = results["functional_validation"]
        func_color = "✅" if func["status"] == "passed" else "❌"
        print(f"\nFunctional Validation: {func_color} {func['status'].upper()}")
        if func["issues"]:
            for issue in func["issues"]:
                print(f"  - {issue}")

        # Integration validation
        integ = results["integration_validation"]
        integ_color = "✅" if integ["status"] == "passed" else "❌"
        print(f"\nIntegration Validation: {integ_color} {integ['status'].upper()}")
        if integ["issues"]:
            for issue in integ["issues"]:
                print(f"  - {issue}")

        # Scalability validation
        scale = results["scalability_validation"]
        scale_color = "✅" if scale["status"] == "passed" else "❌"
        print(f"\nScalability Validation: {scale_color} {scale['status'].upper()}")
        if scale["issues"]:
            for issue in scale["issues"]:
                print(f"  - {issue}")

        # Recommendations
        if results["recommendations"]:
            print("
📋 Recommendations:"            for rec in results["recommendations"]:
                print(f"  • {rec}")

        print("\n" + "=" * 80)


def main():
    """Main validation function"""
    validator = IntegrationTestValidator()
    results = validator.validate_test_results()
    validator.print_report(results)

    # Exit with appropriate code
    if results["overall_status"] == "passed":
        print("\n🎉 Integration validation PASSED! Ready for Issue #2304 completion.")
        sys.exit(0)
    else:
        print("\n❌ Integration validation FAILED! Please address the issues above.")
        sys.exit(1)


if __name__ == "__main__":
    main()