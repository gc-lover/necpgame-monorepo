#!/usr/bin/env python3
# Issue: #1858
# Architecture validation tool for NECPGAME

import argparse
import fnmatch
import json
import os
import re
import subprocess
import sys
import yaml
from pathlib import Path
from typing import Dict, List, Set, Tuple


class ArchitectureValidator:
    """Comprehensive architecture validation for NECPGAME"""

    def __init__(self, project_root: str):
        self.project_root = Path(project_root)
        self.errors: List[str] = []
        self.warnings: Set[str] = set()  # Use set to deduplicate warnings
        self.violations: Dict[str, List[str]] = {
            'solid': [],
            'performance': [],
            'security': [],
            'structure': [],
            'files': []
        }
        self.exempted_files = self._load_exemptions()

    def validate_all(self) -> bool:
        """Run all validation checks"""
        print("Starting comprehensive architecture validation...")
        print("=" * 60)

        try:
            print("[1/7] Checking file sizes...")
            self._validate_file_sizes()

            print("[2/7] Checking code structure...")
            self._validate_code_structure()

            print("[3/7] Checking SOLID principles...")
            self._validate_solid_principles()

            print("[4/7] Checking performance requirements...")
            self._validate_performance_requirements()

            print("[5/7] Checking security compliance...")
            self._validate_security_compliance()

            print("[6/7] Checking OpenAPI specifications...")
            self._validate_openapi_specs()

            print("[7/7] Checking dependencies...")
            self._validate_dependencies()

            print("Generating validation report...")
            return self._report_results()

        except Exception as e:
            print(f"\nVALIDATION CRASHED: {e}")
            import traceback
            traceback.print_exc()
            return False

    def _validate_file_sizes(self):
        """Check file size limits"""
        print("[INFO] Checking file sizes...")

        limits = {
            'go': 1000,
            'py': 1000,
            'yaml': 1000,
            'md': 1000,
            'sql': 1000,
            # Generated files have higher limits
            'gen.go': 1200,
            'pb.go': 2000,  # Protocol buffer files can be very large
            'bundled.yaml': 1200,
            'changelog-content.yaml': 2000,
            # OpenAPI specs can be large
            'openapi': 1500,
            'api': 1500,
            'spec': 1500,
            # Analysis files
            'analysis': 1200,
        }

        # Special handling for generated files
        generated_patterns = [
            '.gen.go',
            '.pb.go',
            'bundled.yaml',
            'changelog-content.yaml',
            'api.gen.go',
            'oas_'
        ]

        for ext, max_lines in limits.items():
            for file_path in self.project_root.rglob(f'*.{ext}'):
                if self._should_check_file(file_path):
                    try:
                        with open(file_path, 'r', encoding='utf-8') as f:
                            lines = len(f.readlines())

                        # Skip generated files entirely
                        file_name = str(file_path)
                        is_generated = any(pattern in file_name for pattern in [
                            '_gen.go', '.pb.go', 'bundled.yaml', 'changelog-content.yaml',
                            'oas_', 'housing-service-paths.yaml', 'clan-war-service-paths.yaml',
                            'api.gen.go', 'generated', 'docker-compose.yml',
                            '.bundled.yaml', 'openapi-bundled.yaml', 'tournament-service-bundled.yaml'
                        ])
                        if is_generated:
                            continue

                        if lines > max_lines:
                            self.violations['files'].append(
                                f"File {file_path.relative_to(self.project_root)} "
                                f"exceeds {max_lines} lines ({lines} lines)"
                            )
                    except Exception as e:
                        self.warnings.add(f"Could not check {file_path}: {e}")
                # Debug: uncomment to see what files are being checked
                # elif 'oas_' in str(file_path) or '_gen.go' in str(file_path):
                #     pass  # Skip debug output for generated files

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
                        f"HTTP handler in {file_path.relative_to(self.project_root)} missing context timeout"
                    )

            # Check for proper error handling
            if 'func ' in content and 'error' in content:
                if 'if err != nil' not in content:
                    self.warnings.add(f"Function in {file_path.relative_to(self.project_root)} may lack error handling")

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
                            f"Struct in {file_path.relative_to(self.project_root)} may have suboptimal field alignment"
                        )

        except Exception as e:
            self.warnings.add(f"Could not validate {file_path}: {e}")

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
                                f"Function in {go_file.relative_to(self.project_root)} exceeds 50 lines ({lines} lines)"
                            )

                    # Check for large structs (>15 fields)
                    struct_matches = re.findall(r'type\s+\w+\s+struct\s*{([^}]*)}', content, re.DOTALL)
                    for struct_def in struct_matches:
                        fields = len([line for line in struct_def.split('\n') if ':' in line])
                        if fields > 15:
                            self.violations['solid'].append(
                                f"Struct in {go_file.relative_to(self.project_root)} has too many fields ({fields})"
                            )

                except Exception as e:
                    self.warnings.add(f"Could not check SOLID for {go_file}: {e}")

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
                                f"Potential goroutine leak in {go_file.relative_to(self.project_root)}"
                            )

                    # Check for database connection pooling
                    if 'database/sql' in content:
                        if 'SetMaxOpenConns' not in content and 'SetMaxIdleConns' not in content:
                            self.warnings.add(
                                f"Database in {go_file.relative_to(self.project_root)} may lack connection pooling"
                            )

                except Exception as e:
                    self.warnings.add(f"Could not check performance for {go_file}: {e}")

    def _validate_security_compliance(self):
        """Check security compliance"""
        print("[INFO] Checking security compliance...")

        for go_file in self.project_root.rglob('*.go'):
            if self._should_check_file(go_file):
                try:
                    with open(go_file, 'r', encoding='utf-8') as f:
                        content = f.read()

                    # Check for SQL injection vulnerabilities
                    # Only check files that actually contain database method calls
                    db_method_pattern = r'\.Query\(|QueryRow\(|Exec\(|QueryContext\(|ExecContext\('
                    if re.search(db_method_pattern, content):
                        # Look for string concatenation in SQL queries (dangerous pattern)
                        dangerous_patterns = [
                            r'\.Query\([^,)]*\s*\+\s*',  # .Query("SELECT..." + var)
                            r'\.Exec\([^,)]*\s*\+\s*',  # .Exec("INSERT..." + var)
                            r'QueryRow\([^,)]*\s*\+\s*',  # QueryRow("SELECT..." + var)
                            r'QueryContext\([^,)]*\s*\+\s*',  # QueryContext("SELECT..." + var)
                            r'ExecContext\([^,)]*\s*\+\s*',  # ExecContext("SELECT..." + var)
                            r'fmt\.Sprintf.*\.Query',  # fmt.Sprintf("SELECT %s", var) with .Query
                            r'fmt\.Sprintf.*\.Exec',  # fmt.Sprintf("SELECT %s", var) with .Exec
                        ]

                        has_dangerous_pattern = any(
                            re.search(pattern, content, re.IGNORECASE) for pattern in dangerous_patterns)

                        # Only flag if no parameterized placeholders AND has dangerous patterns
                        has_placeholders = '$' in content or '?' in content or 'args...' in content
                        if not has_placeholders and has_dangerous_pattern:
                            self.violations['security'].append(
                                f"Potential SQL injection in {go_file.relative_to(self.project_root)}"
                            )

                    # Check for proper input validation
                    if 'http.' in content:
                        if 'validate' not in content.lower() and 'sanitize' not in content.lower():
                            self.warnings.add(
                                f"HTTP handler in {go_file.relative_to(self.project_root)} may lack input validation"
                            )

                except Exception as e:
                    self.warnings.add(f"Could not check security for {go_file}: {e}")

    def _validate_openapi_specs(self):
        """Validate OpenAPI specifications"""
        print("[INFO] Checking OpenAPI specifications...")

        # Check if redocly is available once, not for each file
        redocly_available = self._check_redocly_available()

        if not redocly_available:
            yaml_files = list(self.project_root.rglob('**/proto/openapi/*.yaml'))
            self.warnings.add(f"redocly not found, skipping OpenAPI validation for {len(yaml_files)} spec files")
            return

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
                        f"OpenAPI spec {yaml_file.relative_to(self.project_root)} has validation errors: {result.stdout.strip()[:200]}..."
                    )

            except Exception as e:
                self.warnings.add(f"Could not validate {yaml_file.relative_to(self.project_root)}: {e}")

    def _check_redocly_available(self):
        """Check if redocly CLI tool is available"""
        try:
            result = subprocess.run(
                ['redocly', '--version'],
                capture_output=True,
                text=True,
                cwd=self.project_root
            )
            return result.returncode == 0
        except (FileNotFoundError, OSError):
            return False

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
                    self.warnings.add("go.mod contains replace directives")

            except Exception as e:
                self.warnings.add(f"Could not check go.mod: {e}")

    def _validate_concern_separation(self):
        """Check separation of concerns"""
        print("[INFO] Checking separation of concerns...")

        # Check that business logic is separate from handlers
        for go_file in self.project_root.rglob('services/**/*.go'):
            if 'handler' in go_file.name.lower() and 'service' in go_file.name.lower():
                self.warnings.add(
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
            'node_modules/',
            'bundled.yaml',
            'changelog-content.yaml',
            'docker-compose.yml',
            'openapi-bundled.yaml',
            '-bundled.yaml'
        ]

        file_str = str(file_path)
        relative_path = file_path.relative_to(self.project_root)

        # Check skip patterns
        for pattern in skip_patterns:
            if pattern in file_str:
                return False

        # Check exempted files from pre-commit-exemptions.txt using glob matching
        for exemption in self.exempted_files:
            # Convert exemption pattern to be relative to project root
            if fnmatch.fnmatch(str(relative_path), exemption):
                return False

        return True

    def _load_exemptions(self) -> Set[str]:
        """Load exempted file patterns from pre-commit-exemptions.txt"""
        exemptions_file = self.project_root / '.githooks' / 'pre-commit-exemptions.txt'
        exempted = set()

        if exemptions_file.exists():
            try:
                with open(exemptions_file, 'r', encoding='utf-8') as f:
                    for line in f:
                        line = line.strip()
                        # Skip empty lines and comments
                        if line and not line.startswith('#'):
                            exempted.add(line)
            except Exception as e:
                print(f"Warning: Could not load exemptions file: {e}")

        return exempted

    def _report_results(self) -> bool:
        """Report validation results"""
        total_violations = sum(len(v) for v in self.violations.values())
        total_warnings = len(self.warnings)

        print("\n" + "=" * 80)
        print("ARCHITECTURE VALIDATION RESULTS")
        print("=" * 80)
        print(f"Violations: {total_violations}")
        print(f"Warnings: {total_warnings}")
        print()

        if total_violations > 0:
            print("VIOLATIONS FOUND:")
            print("-" * 50)
            for category, violations in self.violations.items():
                if violations:
                    print(f"\n{category.upper()} ISSUES:")
                    for violation in violations:  # Show all violations
                        print(f"   - {violation}")
                    print(f"   Total: {len(violations)} {category} violations found")

        if total_warnings > 0:
            print("\nWARNINGS:")
            print("-" * 30)
            for warning in sorted(self.warnings):  # Show all warnings, sorted
                print(f"   - {warning}")
            print(f"   Total: {len(self.warnings)} unique warnings found")

        print("\n" + "=" * 80)

        if total_violations == 0:
            print("SUCCESS: All architecture checks passed!")
            return True
        else:
            print(f"VALIDATION FAILED: {total_violations} violations must be fixed")
            print("\nREQUIRED ACTIONS:")
            print("- Fix all violations before committing")
            print("- Address critical warnings when possible")
            print("- Run: python scripts/architecture-validator.py --help for options")
            return False


def main():
    parser = argparse.ArgumentParser(description='NECPGAME Architecture Validator')
    parser.add_argument('--project-root', default='.', help='Project root directory')
    parser.add_argument('--strict', action='store_true', help='Fail on warnings too')
    parser.add_argument('--category', choices=['solid', 'performance', 'security', 'structure', 'files'],
                        help='Check only specific category')

    args = parser.parse_args()

    try:
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
                    print(f"[ERROR] Unknown category: {args.category}")
                    return 1
        else:
            # Run all checks
            if not validator.validate_all():
                return 1

        return 0

    except Exception as e:
        print(f"[ERROR] Architecture validation failed with exception: {e}")
        import traceback
        traceback.print_exc()
        return 1


if __name__ == '__main__':
    sys.exit(main())
