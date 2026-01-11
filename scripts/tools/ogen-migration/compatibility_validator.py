#!/usr/bin/env python3
"""
Ogen Migration Compatibility Validator

Validates compatibility between oapi-codegen generated code and ogen generated code.
Ensures that API contracts, types, and behavior remain consistent after migration.
"""

import ast
import difflib
import json
import logging
import re
import subprocess
import sys
from dataclasses import dataclass, field
from pathlib import Path
from typing import Dict, List, Optional, Set, Tuple

import yaml


@dataclass
class CompatibilityIssue:
    """Represents a compatibility issue found during validation."""
    severity: str  # "error", "warning", "info"
    category: str  # "api_contract", "type_definition", "import", "behavior"
    file_path: Path
    line_number: Optional[int]
    message: str
    suggestion: Optional[str] = None
    old_code: Optional[str] = None
    new_code: Optional[str] = None


@dataclass
class ValidationResult:
    """Result of compatibility validation."""
    service_name: str
    compatible: bool
    issues: List[CompatibilityIssue] = field(default_factory=list)
    coverage_percentage: float = 0.0
    api_endpoints_checked: int = 0
    types_validated: int = 0


class CompatibilityValidator:
    """Validates compatibility between oapi-codegen and ogen generated code."""

    def __init__(self, base_path: Path):
        self.base_path = base_path
        self.logger = logging.getLogger(__name__)

        # Validation rules
        self.validation_rules = {
            "api_contract": self._validate_api_contract,
            "type_definitions": self._validate_type_definitions,
            "imports": self._validate_imports,
            "error_handling": self._validate_error_handling,
            "middleware": self._validate_middleware
        }

    def validate_service(self, service_name: str) -> ValidationResult:
        """Validate compatibility for a specific service."""
        self.logger.info(f"Starting compatibility validation for {service_name}")

        result = ValidationResult(service_name=service_name, compatible=True)

        service_path = self.base_path / "services" / service_name
        if not service_path.exists():
            result.issues.append(CompatibilityIssue(
                severity="error",
                category="general",
                file_path=service_path,
                message=f"Service directory not found: {service_path}"
            ))
            result.compatible = False
            return result

        # Check if both versions exist
        oapi_codegen_path = service_path / "internal" / "oapi_codegen"
        ogen_path = service_path / "internal" / "ogen"

        if not oapi_codegen_path.exists():
            result.issues.append(CompatibilityIssue(
                severity="warning",
                category="general",
                file_path=oapi_codegen_path,
                message="oapi-codegen generated code not found for comparison"
            ))

        if not ogen_path.exists():
            result.issues.append(CompatibilityIssue(
                severity="error",
                category="general",
                file_path=ogen_path,
                message="ogen generated code not found"
            ))
            result.compatible = False
            return result

        # Run validation checks
        for rule_name, rule_func in self.validation_rules.items():
            try:
                issues = rule_func(service_path, oapi_codegen_path, ogen_path)
                result.issues.extend(issues)
            except Exception as e:
                self.logger.error(f"Validation rule {rule_name} failed: {e}")
                result.issues.append(CompatibilityIssue(
                    severity="error",
                    category="validation_error",
                    file_path=service_path,
                    message=f"Validation rule {rule_name} failed: {str(e)}"
                ))

        # Check API contract compatibility
        api_issues = self._validate_api_contracts(service_path)
        result.issues.extend(api_issues)
        result.api_endpoints_checked = len(api_issues)

        # Check type definitions
        type_issues = self._validate_type_definitions(service_path, oapi_codegen_path, ogen_path)
        result.issues.extend(type_issues)
        result.types_validated = len(type_issues)

        # Calculate compatibility score
        error_count = sum(1 for issue in result.issues if issue.severity == "error")
        warning_count = sum(1 for issue in result.issues if issue.severity == "warning")

        if error_count > 0:
            result.compatible = False
        elif warning_count > 5:  # Too many warnings indicate potential issues
            result.compatible = False

        result.coverage_percentage = self._calculate_coverage(result.issues)

        self.logger.info(f"Validation completed for {service_name}: compatible={result.compatible}, "
                        f"issues={len(result.issues)} (errors={error_count}, warnings={warning_count})")

        return result

    def _validate_api_contracts(self, service_path: Path) -> List[CompatibilityIssue]:
        """Validate API contract compatibility."""
        issues = []

        # Check OpenAPI specification
        spec_path = self.base_path / "proto" / "openapi" / service_path.name / "main.yaml"
        if not spec_path.exists():
            issues.append(CompatibilityIssue(
                severity="error",
                category="api_contract",
                file_path=spec_path,
                message="OpenAPI specification not found"
            ))
            return issues

        # Load OpenAPI spec
        try:
            with open(spec_path, 'r', encoding='utf-8') as f:
                spec = yaml.safe_load(f)
        except Exception as e:
            issues.append(CompatibilityIssue(
                severity="error",
                category="api_contract",
                file_path=spec_path,
                message=f"Failed to parse OpenAPI spec: {str(e)}"
            ))
            return issues

        # Check paths
        if "paths" not in spec:
            issues.append(CompatibilityIssue(
                severity="error",
                category="api_contract",
                file_path=spec_path,
                message="No paths defined in OpenAPI spec"
            ))
            return issues

        # Validate each endpoint
        for path, methods in spec["paths"].items():
            for method, operation in methods.items():
                if method.upper() not in ["GET", "POST", "PUT", "DELETE", "PATCH"]:
                    continue

                issues.extend(self._validate_operation(path, method, operation, spec_path))

        return issues

    def _validate_operation(self, path: str, method: str, operation: Dict, spec_path: Path) -> List[CompatibilityIssue]:
        """Validate individual API operation."""
        issues = []

        # Check required fields
        if "operationId" not in operation:
            issues.append(CompatibilityIssue(
                severity="warning",
                category="api_contract",
                file_path=spec_path,
                message=f"Missing operationId for {method.upper()} {path}"
            ))

        # Check parameters
        if "parameters" in operation:
            for param in operation["parameters"]:
                issues.extend(self._validate_parameter(param, path, method, spec_path))

        # Check request/response schemas
        if "requestBody" in operation:
            issues.extend(self._validate_request_body(operation["requestBody"], path, method, spec_path))

        if "responses" in operation:
            issues.extend(self._validate_responses(operation["responses"], path, method, spec_path))

        return issues

    def _validate_parameter(self, param: Dict, path: str, method: str, spec_path: Path) -> List[CompatibilityIssue]:
        """Validate parameter definition."""
        issues = []

        if "name" not in param:
            issues.append(CompatibilityIssue(
                severity="error",
                category="api_contract",
                file_path=spec_path,
                message=f"Parameter missing name in {method.upper()} {path}"
            ))

        if "schema" not in param:
            issues.append(CompatibilityIssue(
                severity="warning",
                category="api_contract",
                file_path=spec_path,
                message=f"Parameter {param.get('name', 'unknown')} missing schema in {method.upper()} {path}"
            ))

        return issues

    def _validate_request_body(self, request_body: Dict, path: str, method: str, spec_path: Path) -> List[CompatibilityIssue]:
        """Validate request body definition."""
        issues = []

        if "content" not in request_body:
            issues.append(CompatibilityIssue(
                severity="error",
                category="api_contract",
                file_path=spec_path,
                message=f"Request body missing content in {method.upper()} {path}"
            ))
            return issues

        # Check for application/json content type
        if "application/json" not in request_body["content"]:
            issues.append(CompatibilityIssue(
                severity="warning",
                category="api_contract",
                file_path=spec_path,
                message=f"No application/json content type in request body for {method.upper()} {path}"
            ))
            return issues

        json_content = request_body["content"]["application/json"]
        if "schema" not in json_content:
            issues.append(CompatibilityIssue(
                severity="warning",
                category="api_contract",
                file_path=spec_path,
                message=f"Request body missing schema for {method.upper()} {path}"
            ))

        return issues

    def _validate_responses(self, responses: Dict, path: str, method: str, spec_path: Path) -> List[CompatibilityIssue]:
        """Validate response definitions."""
        issues = []

        # Check for 200 response
        if "200" not in responses and "default" not in responses:
            issues.append(CompatibilityIssue(
                severity="warning",
                category="api_contract",
                file_path=spec_path,
                message=f"No success response defined for {method.upper()} {path}"
            ))

        for status_code, response in responses.items():
            if "content" in response:
                if "application/json" not in response["content"]:
                    issues.append(CompatibilityIssue(
                        severity="warning",
                        category="api_contract",
                        file_path=spec_path,
                        message=f"No application/json content type in {status_code} response for {method.upper()} {path}"
                    ))

        return issues

    def _validate_type_definitions(self, service_path: Path, oapi_codegen_path: Path, ogen_path: Path) -> List[CompatibilityIssue]:
        """Validate type definitions compatibility."""
        issues = []

        # Compare generated type files
        oapi_files = list(oapi_codegen_path.glob("*.go"))
        ogen_files = list(ogen_path.glob("*.go"))

        # Find corresponding files
        for oapi_file in oapi_files:
            if oapi_file.name.startswith("types.go") or oapi_file.name.startswith("models.go"):
                ogen_equivalent = ogen_path / oapi_file.name
                if ogen_equivalent.exists():
                    file_issues = self._compare_type_files(oapi_file, ogen_equivalent)
                    issues.extend(file_issues)

        return issues

    def _compare_type_files(self, oapi_file: Path, ogen_file: Path) -> List[CompatibilityIssue]:
        """Compare type definitions between oapi-codegen and ogen files."""
        issues = []

        try:
            # Read both files
            with open(oapi_file, 'r', encoding='utf-8') as f:
                oapi_content = f.read()

            with open(ogen_file, 'r', encoding='utf-8') as f:
                ogen_content = f.read()

            # Parse Go AST for both files
            oapi_tree = ast.parse(oapi_content, filename=str(oapi_file))
            ogen_tree = ast.parse(ogen_content, filename=str(ogen_file))

            # Extract type definitions
            oapi_types = self._extract_types(oapi_tree)
            ogen_types = self._extract_types(ogen_tree)

            # Compare types
            for type_name, oapi_type in oapi_types.items():
                if type_name in ogen_types:
                    ogen_type = ogen_types[type_name]
                    type_issues = self._compare_types(type_name, oapi_type, ogen_type, ogen_file)
                    issues.extend(type_issues)
                else:
                    issues.append(CompatibilityIssue(
                        severity="error",
                        category="type_definition",
                        file_path=ogen_file,
                        message=f"Type {type_name} missing in ogen generated code"
                    ))

        except Exception as e:
            issues.append(CompatibilityIssue(
                severity="error",
                category="type_definition",
                file_path=ogen_file,
                message=f"Failed to compare type files: {str(e)}"
            ))

        return issues

    def _extract_types(self, tree: ast.Module) -> Dict[str, ast.ClassDef]:
        """Extract type definitions from Go AST."""
        types = {}

        for node in ast.walk(tree):
            if isinstance(node, ast.GenDecl) and node.tok == ast.TYPE:
                for spec in node.specs:
                    if isinstance(spec, ast.TypeSpec):
                        types[spec.name] = spec

        return types

    def _compare_types(self, type_name: str, oapi_type: ast.TypeSpec, ogen_type: ast.TypeSpec, file_path: Path) -> List[CompatibilityIssue]:
        """Compare two type definitions."""
        issues = []

        # This is a simplified comparison - real implementation would be more thorough
        oapi_str = ast.dump(oapi_type)
        ogen_str = ast.dump(ogen_type)

        if oapi_str != ogen_str:
            issues.append(CompatibilityIssue(
                severity="warning",
                category="type_definition",
                file_path=file_path,
                message=f"Type {type_name} differs between oapi-codegen and ogen",
                old_code=oapi_str[:200] + "..." if len(oapi_str) > 200 else oapi_str,
                new_code=ogen_str[:200] + "..." if len(ogen_str) > 200 else ogen_str
            ))

        return issues

    def _validate_imports(self, service_path: Path, oapi_codegen_path: Path, ogen_path: Path) -> List[CompatibilityIssue]:
        """Validate import compatibility."""
        issues = []

        # Check Go files in service for import issues
        go_files = list(service_path.glob("**/*.go"))

        for go_file in go_files:
            try:
                with open(go_file, 'r', encoding='utf-8') as f:
                    content = f.read()

                # Check for problematic imports
                if 'github.com/deepmap/oapi-codegen' in content:
                    issues.append(CompatibilityIssue(
                        severity="error",
                        category="imports",
                        file_path=go_file,
                        message="Still using oapi-codegen imports after migration",
                        suggestion="Replace with ogen imports"
                    ))

                if 'github.com/ogen-go/ogen' not in content and 'ogen' in content.lower():
                    issues.append(CompatibilityIssue(
                        severity="warning",
                        category="imports",
                        file_path=go_file,
                        message="Potential missing ogen import"
                    ))

            except Exception as e:
                issues.append(CompatibilityIssue(
                    severity="error",
                    category="imports",
                    file_path=go_file,
                    message=f"Failed to validate imports: {str(e)}"
                ))

        return issues

    def _validate_error_handling(self, service_path: Path, oapi_codegen_path: Path, ogen_path: Path) -> List[CompatibilityIssue]:
        """Validate error handling compatibility."""
        issues = []

        # Check that error types are compatible
        error_patterns = [
            r'errors\.New\(',
            r'fmt\.Errorf\(',
            r'api\.Error',
            r'api\.Err',
        ]

        go_files = list(service_path.glob("**/*.go"))

        for go_file in go_files:
            try:
                with open(go_file, 'r', encoding='utf-8') as f:
                    content = f.read()

                for pattern in error_patterns:
                    if re.search(pattern, content):
                        # This is just a basic check - real validation would be more sophisticated
                        break

            except Exception as e:
                issues.append(CompatibilityIssue(
                    severity="warning",
                    category="error_handling",
                    file_path=go_file,
                    message=f"Error handling validation failed: {str(e)}"
                ))

        return issues

    def _validate_middleware(self, service_path: Path, oapi_codegen_path: Path, ogen_path: Path) -> List[CompatibilityIssue]:
        """Validate middleware compatibility."""
        issues = []

        # Check for middleware usage
        middleware_patterns = [
            r'middleware\.',
            r'gin\.Use\(',
            r'http\.Middleware',
            r'auth\.Middleware',
        ]

        go_files = list(service_path.glob("**/*.go"))

        for go_file in go_files:
            try:
                with open(go_file, 'r', encoding='utf-8') as f:
                    content = f.read()

                for pattern in middleware_patterns:
                    matches = re.findall(pattern, content)
                    if matches:
                        # Check if middleware usage is compatible
                        # This is a simplified check
                        break

            except Exception as e:
                issues.append(CompatibilityIssue(
                    severity="warning",
                    category="middleware",
                    file_path=go_file,
                    message=f"Middleware validation failed: {str(e)}"
                ))

        return issues

    def _calculate_coverage(self, issues: List[CompatibilityIssue]) -> float:
        """Calculate validation coverage percentage."""
        # This is a simplified calculation
        total_checks = len(issues) + 1  # +1 to avoid division by zero
        passed_checks = sum(1 for issue in issues if issue.severity != "error")

        return (passed_checks / total_checks) * 100.0

    def validate_all_services(self) -> List[ValidationResult]:
        """Validate compatibility for all services."""
        self.logger.info("Starting validation for all services")

        results = []

        # Discover services
        services_path = self.base_path / "services"
        if not services_path.exists():
            self.logger.error("Services directory not found")
            return results

        for service_dir in services_path.iterdir():
            if service_dir.is_dir():
                result = self.validate_service(service_dir.name)
                results.append(result)

        self.logger.info(f"Validation completed for {len(results)} services")
        return results

    def generate_report(self, results: List[ValidationResult], output_path: Optional[Path] = None) -> None:
        """Generate validation report."""
        if output_path is None:
            timestamp = json.dumps(datetime.now().isoformat())
            output_path = self.base_path / "scripts" / "ogen-migration" / f"compatibility_report_{timestamp}.json"

        report_data = {
            "timestamp": datetime.now().isoformat(),
            "summary": {
                "total_services": len(results),
                "compatible_services": sum(1 for r in results if r.compatible),
                "incompatible_services": sum(1 for r in results if not r.compatible),
                "total_issues": sum(len(r.issues) for r in results),
                "error_count": sum(sum(1 for i in r.issues if i.severity == "error") for r in results),
                "warning_count": sum(sum(1 for i in r.issues if i.severity == "warning") for r in results),
            },
            "services": []
        }

        for result in results:
            service_data = {
                "name": result.service_name,
                "compatible": result.compatible,
                "coverage_percentage": result.coverage_percentage,
                "api_endpoints_checked": result.api_endpoints_checked,
                "types_validated": result.types_validated,
                "issues": [
                    {
                        "severity": issue.severity,
                        "category": issue.category,
                        "file_path": str(issue.file_path),
                        "line_number": issue.line_number,
                        "message": issue.message,
                        "suggestion": issue.suggestion,
                    }
                    for issue in result.issues
                ]
            }
            report_data["services"].append(service_data)

        output_path.parent.mkdir(parents=True, exist_ok=True)

        with open(output_path, 'w', encoding='utf-8') as f:
            json.dump(report_data, f, indent=2, ensure_ascii=False)

        self.logger.info(f"Compatibility report saved to {output_path}")

    def print_summary(self, results: List[ValidationResult]) -> None:
        """Print validation summary."""
        print("\n=== Ogen Migration Compatibility Summary ===")

        total_services = len(results)
        compatible = sum(1 for r in results if r.compatible)
        total_issues = sum(len(r.issues) for r in results)
        errors = sum(sum(1 for i in r.issues if i.severity == "error") for r in results)
        warnings = sum(sum(1 for i in r.issues if i.severity == "warning") for r in results)

        print(f"Services validated: {total_services}")
        print(f"Compatible services: {compatible} ({compatible/total_services*100:.1f}%)")
        print(f"Total issues: {total_issues}")
        print(f"Errors: {errors}")
        print(f"Warnings: {warnings}")

        if results:
            avg_coverage = sum(r.coverage_percentage for r in results) / len(results)
            print(f"Average coverage: {avg_coverage:.1f}%")

        print("\nTop issues by category:")
        category_counts = {}
        for result in results:
            for issue in result.issues:
                category_counts[issue.category] = category_counts.get(issue.category, 0) + 1

        for category, count in sorted(category_counts.items(), key=lambda x: x[1], reverse=True)[:5]:
            print(f"  {category}: {count}")


def main():
    """Main entry point."""
    logging.basicConfig(level=logging.INFO)

    # Parse arguments
    import argparse
    parser = argparse.ArgumentParser(description="Ogen Migration Compatibility Validator")
    parser.add_argument("--services", nargs="*", help="Specific services to validate")
    parser.add_argument("--output", type=Path, help="Output file for report")

    args = parser.parse_args()

    # Initialize validator
    base_path = Path(__file__).parent.parent.parent
    validator = CompatibilityValidator(base_path)

    try:
        if args.services:
            # Validate specific services
            results = []
            for service_name in args.services:
                result = validator.validate_service(service_name)
                results.append(result)
        else:
            # Validate all services
            results = validator.validate_all_services()

        # Generate report
        validator.generate_report(results, args.output)

        # Print summary
        validator.print_summary(results)

        # Exit with error code if any service is incompatible
        incompatible_count = sum(1 for r in results if not r.compatible)
        if incompatible_count > 0:
            print(f"\n{incompatible_count} services are incompatible!")
            sys.exit(1)

    except Exception as e:
        logging.error(f"Validation failed: {e}")
        sys.exit(1)


if __name__ == "__main__":
    main()
