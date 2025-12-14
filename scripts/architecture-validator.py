#!/usr/bin/env python3
# Issue: #1858
# Architecture validation tool for NECPGAME

import os
import re
import yaml
import json
import subprocess
import sys
from pathlib import Path
from typing import Dict, List, Set, Tuple
import argparse

class ArchitectureValidator:
    """Comprehensive architecture validation for NECPGAME"""

    def __init__(self, project_root: str):
        self.project_root = Path(project_root)
        self.errors: List[str] = []
        self.warnings: List[str] = []
        self.violations: Dict[str, List[str]] = {
            'solid': [],
            'performance': [],
            'security': [],
            'structure': [],
            'files': []
        }

    def validate_all(self) -> bool:
        """Run all validation checks"""
        print("[INFO] Starting comprehensive architecture validation...")

        self._validate_file_sizes()
        self._validate_code_structure()
        self._validate_solid_principles()
        self._validate_performance_requirements()
        self._validate_security_compliance()
        self._validate_openapi_specs()
        self._validate_dependencies()

        return self._report_results()

    def _validate_file_sizes(self):
        """Check file size limits"""
        print("[INFO] Checking file sizes...")

        limits = {
            'go': 500,
            'py': 500,
            'yaml': 500,
            'md': 1000,
            'sql': 800
        }

        for ext, max_lines in limits.items():
            for file_path in self.project_root.rglob(f'*.{ext}'):
                if self._should_check_file(file_path):
                    try:
                        with open(file_path, 'r', encoding='utf-8') as f:
                            lines = len(f.readlines())

                        if lines > max_lines:
                            self.violations['files'].append(
                                f"File {file_path.relative_to(self.project_root)} "
                                f"exceeds {max_lines} lines ({lines} lines)"
                            )
                    except Exception as e:
                        self.warnings.append(f"Could not check {file_path}: {e}")

    def _validate_code_structure(self):
        """Validate code structure and organization"""
        print("[INFO] Checking code structure...")

        # Check Go files for proper package structure
        for go_file in self.project_root.rglob('*.go'):
            if self._should_check_file(go_file):
                self._validate_go_file_structure(go_file)

        # Check for proper separation of concerns
        self._validate_concern_separation()

    def _validate_go_file_structure(self, file_path: Path):
        """Validate Go file structure"""
        try:
            with open(file_path, 'r', encoding='utf-8') as f:
                content = f.read()

            # Check for context timeouts in HTTP handlers
            if 'http.' in content and 'HandleFunc' in content:
                if 'context.WithTimeout' not in content and 'ctx,' not in content:
                    self.violations['performance'].append(
                        f"HTTP handler in {file_path.name} missing context timeout"
                    )

            # Check for proper error handling
            if 'func ' in content and 'error' in content:
                if 'if err != nil' not in content:
                    self.warnings.append(f"Function in {file_path.name} may lack error handling")

            # Check struct alignment (large fields first)
            struct_matches = re.findall(r'type\s+\w+\s+struct\s*{([^}]*)}', content, re.DOTALL)
            for struct_def in struct_matches:
                lines = [line.strip() for line in struct_def.split('\n') if line.strip()]
                if len(lines) > 5:  # Only check larger structs
                    large_fields = []
                    small_fields = []
                    for line in lines:
                        if ':' in line:
                            field_type = line.split(':')[1].strip()
                            if any(t in field_type for t in ['[]', 'map[', 'string']):
                                large_fields.append(line)
                            else:
                                small_fields.append(line)

                    if large_fields and small_fields and large_fields[0] != lines[0]:
                        self.warnings.append(
                            f"Struct in {file_path.name} may have suboptimal field alignment"
                        )

        except Exception as e:
            self.warnings.append(f"Could not validate {file_path}: {e}")

    def _validate_solid_principles(self):
        """Check SOLID principles compliance"""
        print("[INFO] Checking SOLID principles...")

        for go_file in self.project_root.rglob('*.go'):
            if self._should_check_file(go_file):
                try:
                    with open(go_file, 'r', encoding='utf-8') as f:
                        content = f.read()

                    # Check for large functions (>50 lines)
                    functions = re.findall(r'func\s+\w+.*{([^}]*)}', content, re.DOTALL)
                    for func in functions:
                        lines = len([line for line in func.split('\n') if line.strip()])
                        if lines > 50:
                            self.violations['solid'].append(
                                f"Function in {go_file.name} exceeds 50 lines ({lines} lines)"
                            )

                    # Check for large structs (>15 fields)
                    struct_matches = re.findall(r'type\s+\w+\s+struct\s*{([^}]*)}', content, re.DOTALL)
                    for struct_def in struct_matches:
                        fields = len([line for line in struct_def.split('\n') if ':' in line])
                        if fields > 15:
                            self.violations['solid'].append(
                                f"Struct in {go_file.name} has too many fields ({fields})"
                            )

                except Exception as e:
                    self.warnings.append(f"Could not check SOLID for {go_file}: {e}")

    def _validate_performance_requirements(self):
        """Check performance requirements"""
        print("[INFO] Checking performance requirements...")

        for go_file in self.project_root.rglob('*.go'):
            if self._should_check_file(go_file):
                try:
                    with open(go_file, 'r', encoding='utf-8') as f:
                        content = f.read()

                    # Check for goroutine leaks
                    if 'go func' in content:
                        if 'defer' not in content and 'wg.Wait()' not in content:
                            self.violations['performance'].append(
                                f"Potential goroutine leak in {go_file.name}"
                            )

                    # Check for database connection pooling
                    if 'database/sql' in content:
                        if 'SetMaxOpenConns' not in content and 'SetMaxIdleConns' not in content:
                            self.warnings.append(
                                f"Database in {go_file.name} may lack connection pooling"
                            )

                except Exception as e:
                    self.warnings.append(f"Could not check performance for {go_file}: {e}")

    def _validate_security_compliance(self):
        """Check security compliance"""
        print("[INFO] Checking security compliance...")

        for go_file in self.project_root.rglob('*.go'):
            if self._should_check_file(go_file):
                try:
                    with open(go_file, 'r', encoding='utf-8') as f:
                        content = f.read()

                    # Check for SQL injection vulnerabilities
                    if 'Query' in content or 'Exec' in content:
                        if '$' not in content and '?' not in content:
                            self.violations['security'].append(
                                f"Potential SQL injection in {go_file.name}"
                            )

                    # Check for proper input validation
                    if 'http.' in content:
                        if 'validate' not in content.lower() and 'sanitize' not in content.lower():
                            self.warnings.append(
                                f"HTTP handler in {go_file.name} may lack input validation"
                            )

                except Exception as e:
                    self.warnings.append(f"Could not check security for {go_file}: {e}")

    def _validate_openapi_specs(self):
        """Validate OpenAPI specifications"""
        print("[INFO] Checking OpenAPI specifications...")

        for yaml_file in self.project_root.rglob('**/proto/openapi/*.yaml'):
            try:
                # Run redocly lint if available
                result = subprocess.run(
                    ['redocly', 'lint', str(yaml_file)],
                    capture_output=True,
                    text=True,
                    cwd=self.project_root
                )

                if result.returncode != 0:
                    self.violations['structure'].append(
                        f"OpenAPI spec {yaml_file.name} has validation errors: {result.stdout}"
                    )

            except FileNotFoundError:
                self.warnings.append("redocly not found, skipping OpenAPI validation")
            except Exception as e:
                self.warnings.append(f"Could not validate {yaml_file}: {e}")

    def _validate_dependencies(self):
        """Check for proper dependency management"""
        print("[INFO] Checking dependencies...")

        go_mod = self.project_root / 'go.mod'
        if go_mod.exists():
            try:
                with open(go_mod, 'r', encoding='utf-8') as f:
                    content = f.read()

                # Check for replace directives (local deps)
                if 'replace' in content:
                    self.warnings.append("go.mod contains replace directives")

            except Exception as e:
                self.warnings.append(f"Could not check go.mod: {e}")

    def _validate_concern_separation(self):
        """Check separation of concerns"""
        print("[INFO] Checking separation of concerns...")

        # Check that business logic is separate from handlers
        for go_file in self.project_root.rglob('services/**/*.go'):
            if 'handler' in go_file.name.lower() and 'service' in go_file.name.lower():
                self.warnings.append(
                    f"File {go_file.name} may mix handler and service concerns"
                )

    def _should_check_file(self, file_path: Path) -> bool:
        """Check if file should be validated"""
        # Skip generated files, test files, vendor directories
        skip_patterns = [
            'vendor/',
            '_test.go',
            'generated/',
            'migrations/',
            '.git/',
            'node_modules/'
        ]

        for pattern in skip_patterns:
            if pattern in str(file_path):
                return False

        return True

    def _report_results(self) -> bool:
        """Report validation results"""
        total_violations = sum(len(v) for v in self.violations.values())
        total_warnings = len(self.warnings)

        print("\n[RESULTS] Validation Results:")
        print(f"   Violations: {total_violations}")
        print(f"   Warnings: {total_warnings}")

        if total_violations > 0:
            print("\n[ERROR] VIOLATIONS:")
            for category, violations in self.violations.items():
                if violations:
                    print(f"\n[{category.upper()}]:")
                    for violation in violations[:10]:  # Limit output
                        print(f"   - {violation}")
                    if len(violations) > 10:
                        print(f"   ... and {len(violations) - 10} more")

        if total_warnings > 0 and total_warnings < 20:
            print("\n[WARNING] WARNINGS:")
            for warning in self.warnings[:10]:
                print(f"   - {warning}")

        if total_violations == 0:
            print("\n[SUCCESS] All architecture checks passed!")
            return True
        else:
            print(f"\n[ERROR] {total_violations} violations found. Please fix before proceeding.")
            return False


def main():
    parser = argparse.ArgumentParser(description='NECPGAME Architecture Validator')
    parser.add_argument('--project-root', default='.', help='Project root directory')
    parser.add_argument('--strict', action='store_true', help='Fail on warnings too')
    parser.add_argument('--category', choices=['solid', 'performance', 'security', 'structure', 'files'],
                       help='Check only specific category')

    args = parser.parse_args()

    validator = ArchitectureValidator(args.project_root)

    if args.category:
        # Run specific category check
        method_name = f'_validate_{args.category}_requirements'
        if hasattr(validator, method_name):
            getattr(validator, method_name)()
        else:
            method_name = f'_validate_{args.category}'
            if hasattr(validator, method_name):
                getattr(validator, method_name)()
            else:
                print(f"Unknown category: {args.category}")
                return 1
    else:
        # Run all checks
        if not validator.validate_all():
            return 1

    return 0


if __name__ == '__main__':
    sys.exit(main())