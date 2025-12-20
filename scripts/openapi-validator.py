#!/usr/bin/env python3
# Issue: #1858
# Automated OpenAPI specification validator

import argparse
import json
import os
import subprocess
import sys
import yaml
from pathlib import Path
from typing import Dict, List, Tuple


class OpenAPIValidator:
    """Automated OpenAPI specification validation"""

    def __init__(self, project_root: str):
        self.project_root = Path(project_root)
        self.errors: List[str] = []
        self.warnings: List[str] = []

    def validate_all_specs(self) -> bool:
        """Validate all OpenAPI specifications in the project"""
        print("[INFO] Starting OpenAPI specification validation...")

        openapi_files = list(self.project_root.glob('**/proto/openapi/*.yaml'))
        if not openapi_files:
            print("[WARNING] No OpenAPI files found in proto/openapi/")
            return True

        print(f"[INFO] Found {len(openapi_files)} OpenAPI specification files")

        all_valid = True
        for spec_file in openapi_files:
            if not self._validate_single_spec(spec_file):
                all_valid = False

        self._check_cross_references(openapi_files)
        return self._report_results() and all_valid

    def _validate_single_spec(self, spec_file: Path) -> bool:
        """Validate a single OpenAPI specification"""
        print(f"[INFO] Validating {spec_file.name}...")

        try:
            # Load and parse YAML
            with open(spec_file, 'r', encoding='utf-8') as f:
                spec = yaml.safe_load(f)

            # Basic structure validation
            if not self._validate_basic_structure(spec, spec_file):
                return False

            # Semantic validation
            self._validate_semantic_rules(spec, spec_file)

            # Run redocly lint if available
            if not self._run_redocly_lint(spec_file):
                return False

            print(f"OK {spec_file.name} validation passed")
            return True

        except yaml.YAMLError as e:
            self.errors.append(f"YAML parsing error in {spec_file.name}: {e}")
            return False
        except Exception as e:
            self.errors.append(f"Validation error in {spec_file.name}: {e}")
            return False

    def _validate_basic_structure(self, spec: dict, spec_file: Path) -> bool:
        """Validate basic OpenAPI structure"""
        valid = True

        # Check required fields
        required_fields = ['openapi', 'info', 'paths']
        for field in required_fields:
            if field not in spec:
                self.errors.append(f"Missing required field '{field}' in {spec_file.name}")
                valid = False

        # Check OpenAPI version
        if 'openapi' in spec:
            version = spec['openapi']
            if not version.startswith('3.0'):
                self.warnings.append(f"OpenAPI version {version} in {spec_file.name} may not be fully supported")

        # Check info section
        if 'info' in spec:
            info = spec['info']
            required_info = ['title', 'version']
            for field in required_info:
                if field not in info:
                    self.errors.append(f"Missing info.{field} in {spec_file.name}")
                    valid = False

        # Check paths section
        if 'paths' in spec:
            paths = spec['paths']
            if not isinstance(paths, dict) or len(paths) == 0:
                self.warnings.append(f"No paths defined in {spec_file.name}")

            # Check path structure
            for path, methods in paths.items():
                if not isinstance(methods, dict):
                    self.errors.append(f"Invalid path structure for {path} in {spec_file.name}")
                    valid = False
                    continue

                for method, operation in methods.items():
                    if method not in ['get', 'post', 'put', 'delete', 'patch', 'options', 'head']:
                        continue

                    if not isinstance(operation, dict):
                        self.errors.append(f"Invalid operation structure for {method} {path} in {spec_file.name}")
                        valid = False
                        continue

                    # Check operation structure
                    if 'operationId' not in operation:
                        self.warnings.append(f"Missing operationId for {method} {path} in {spec_file.name}")

                    if 'responses' not in operation:
                        self.errors.append(f"Missing responses for {method} {path} in {spec_file.name}")
                        valid = False

        return valid

    def _validate_semantic_rules(self, spec: dict, spec_file: Path):
        """Validate semantic rules specific to NECPGAME"""
        # Check for BearerAuth security scheme
        if 'components' in spec and 'securitySchemes' in spec['components']:
            schemes = spec['components']['securitySchemes']
            if 'BearerAuth' not in schemes:
                self.warnings.append(f"Missing BearerAuth security scheme in {spec_file.name}")

            # Check BearerAuth structure
            elif 'BearerAuth' in schemes:
                bearer_auth = schemes['BearerAuth']
                if bearer_auth.get('type') != 'http' or bearer_auth.get('scheme') != 'bearer':
                    self.warnings.append(f"BearerAuth scheme not properly configured in {spec_file.name}")

        # Check for proper error responses
        if 'paths' in spec:
            for path, methods in spec['paths'].items():
                for method, operation in methods.items():
                    if isinstance(operation, dict) and 'responses' in operation:
                        responses = operation['responses']

                        # Check for common error codes
                        error_codes = ['400', '401', '403', '404', '500']
                        for code in error_codes:
                            if code not in responses:
                                self.warnings.append(f"Missing {code} response for {method} {path} in {spec_file.name}")

                        # Check response structure
                        for code, response in responses.items():
                            if isinstance(response, dict):
                                if 'description' not in response:
                                    self.errors.append(
                                        f"Missing description for {code} response in {method} {path}, {spec_file.name}")

    def _run_redocly_lint(self, spec_file: Path) -> bool:
        """Run redocly lint on the specification"""
        try:
            result = subprocess.run(
                ['redocly', 'lint', str(spec_file)],
                capture_output=True,
                text=True,
                cwd=self.project_root,
                timeout=30
            )

            if result.returncode != 0:
                # Parse errors and warnings
                output_lines = result.stdout.split('\n')
                for line in output_lines:
                    if 'error' in line.lower():
                        self.errors.append(f"Redocly error in {spec_file.name}: {line}")
                    elif 'warning' in line.lower():
                        self.warnings.append(f"Redocly warning in {spec_file.name}: {line}")

                if result.returncode == 1:  # Errors found
                    return False

            return True

        except subprocess.TimeoutExpired:
            self.errors.append(f"Redocly lint timeout for {spec_file.name}")
            return False
        except FileNotFoundError:
            self.warnings.append("redocly not installed, skipping lint check")
            return True
        except Exception as e:
            self.warnings.append(f"Could not run redocly lint on {spec_file.name}: {e}")
            return True

    def _check_cross_references(self, spec_files: List[Path]):
        """Check for cross-references between specifications"""
        print("[INFO] Checking cross-references...")

        # This could be extended to check for consistency between related specs
        # For now, just check that related services have compatible schemas
        service_names = []
        for spec_file in spec_files:
            try:
                with open(spec_file, 'r', encoding='utf-8') as f:
                    spec = yaml.safe_load(f)

                if 'info' in spec and 'title' in spec['info']:
                    title = spec['info']['title']
                    service_names.append(title)
            except Exception:
                continue

        # Check for duplicate service names
        if len(service_names) != len(set(service_names)):
            duplicates = [name for name in service_names if service_names.count(name) > 1]
            for duplicate in set(duplicates):
                self.warnings.append(f"Duplicate service name found: {duplicate}")

    def _report_results(self) -> bool:
        """Report validation results"""
        total_errors = len(self.errors)
        total_warnings = len(self.warnings)

        print("\n[RESULTS] OpenAPI Validation Results:")
        print(f"   Errors: {total_errors}")
        print(f"   Warnings: {total_warnings}")

        if total_errors > 0:
            print("\n[ERROR] ERRORS:")
            for error in self.errors[:10]:
                print(f"   - {error}")
            if len(self.errors) > 10:
                print(f"   ... and {len(self.errors) - 10} more")

        if total_warnings > 0:
            print("\n[WARNING] WARNINGS:")
            for warning in self.warnings[:10]:
                print(f"   - {warning}")
            if len(self.warnings) > 10:
                print(f"   ... and {len(self.warnings) - 10} more")

        if total_errors == 0:
            print("\n[SUCCESS] All OpenAPI specifications are valid!")
            return True
        else:
            print(f"\n[ERROR] {total_errors} OpenAPI validation errors found.")
            return False


def main():
    parser = argparse.ArgumentParser(description='NECPGAME OpenAPI Validator')
    parser.add_argument('--project-root', default='.', help='Project root directory')
    parser.add_argument('--spec', help='Validate only specific spec file')
    parser.add_argument('--strict', action='store_true', help='Fail on warnings')

    args = parser.parse_args()

    validator = OpenAPIValidator(args.project_root)

    if args.spec:
        spec_path = Path(args.project_root) / args.spec
        success = validator._validate_single_spec(spec_path)
    else:
        success = validator.validate_all_specs()

    if not success or (args.strict and len(validator.warnings) > 0):
        return 1

    return 0


if __name__ == '__main__':
    sys.exit(main())
