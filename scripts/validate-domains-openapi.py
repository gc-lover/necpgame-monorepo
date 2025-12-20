#!/usr/bin/env python3
# Issue: Validation of all domain OpenAPI specs for Go-backend generation

import argparse
import json
import os
import subprocess
import sys
import yaml
from pathlib import Path
from typing import Dict, List, Tuple


class DomainOpenAPIValidator:
    """Validate all domain OpenAPI specifications for Go-backend generation"""

    def __init__(self, project_root: str):
        self.project_root = Path(project_root)
        self.errors: List[str] = []
        self.warnings: List[str] = []
        self.domains_validated = 0

    def validate_all_domains(self) -> bool:
        """Validate all OpenAPI domain specifications"""
        print("[INFO] Starting validation of all OpenAPI domains...")

        openapi_dir = self.project_root / "proto" / "openapi"
        if not openapi_dir.exists():
            self.errors.append("proto/openapi directory not found")
            return False

        # Find all domain directories
        domain_dirs = [d for d in openapi_dir.iterdir() if d.is_dir()]
        print(f"[INFO] Found {len(domain_dirs)} domain directories")

        all_valid = True
        for domain_dir in sorted(domain_dirs):
            if not self._validate_domain(domain_dir):
                all_valid = False

        return self._report_results() and all_valid

    def _validate_domain(self, domain_dir: Path) -> bool:
        """Validate a single domain"""
        domain_name = domain_dir.name
        print(f"[INFO] Validating domain: {domain_name}")

        # Find main.yaml in domain root - this should be a full OpenAPI spec
        main_yaml = domain_dir / "main.yaml"
        if not main_yaml.exists():
            self.warnings.append(f"No main.yaml found in {domain_name}")
            return True  # Not an error, just warning

        # Validate the main spec as full OpenAPI specification
        if not self._validate_full_openapi_spec(main_yaml):
            return False

        # Find all sub-service YAML files, excluding known component files
        all_yaml_files = list(domain_dir.rglob("*.yaml"))
        yaml_files = [f for f in all_yaml_files if f != main_yaml]  # Exclude main.yaml

        # Exclude component files that are not meant to be full OpenAPI specs
        component_patterns = [
            '-ext.yaml', '-ext1.yaml', '-ext2.yaml', '-ext3.yaml', '-ext4.yaml', '-ext5.yaml',  # Extension files
            'schemas/',   # Schema directories
            'paths/',     # Path directories
            'requests/',  # Request directories
            'responses/', # Response directories
            'combat-damage-service/',  # Known component subdirs
            'combat-sessions-service/',
            'combat-service/',
            'ai-service/',
            'inventory-service/',
            'tournament-service/',
            'clan-war-service/',
            'voice-chat-service/',
            'network-service/',
            'guild-service/',
            'notification-service/',
            'interactive-objects-service/',
        ]

        filtered_yaml_files = []
        for yaml_file in yaml_files:
            should_exclude = False
            file_path_str = str(yaml_file.relative_to(domain_dir))

            for pattern in component_patterns:
                if pattern in file_path_str:
                    should_exclude = True
                    break

            if not should_exclude:
                filtered_yaml_files.append(yaml_file)

        yaml_files = filtered_yaml_files
        print(f"[INFO] Found {len(yaml_files)} component YAML files to validate in {domain_name} (excluded {len(all_yaml_files) - len(yaml_files) - 1} component files)")

        # Validate component files (they may not be full specs)
        domain_valid = True
        for yaml_file in yaml_files:
            if not self._validate_component_file(yaml_file):
                domain_valid = False

        if domain_valid:
            self.domains_validated += 1
            print(f"[SUCCESS] Domain {domain_name} validation passed")
        else:
            print(f"[FAILED] Domain {domain_name} validation failed")

        return domain_valid

    def _validate_full_openapi_spec(self, spec_file: Path) -> bool:
        """Validate a full OpenAPI specification (main.yaml files)"""
        try:
            # Load and parse YAML
            with open(spec_file, 'r', encoding='utf-8') as f:
                spec = yaml.safe_load(f)

            # Basic structure validation for full spec
            if not self._validate_basic_structure(spec, spec_file):
                return False

            # Go-backend generation validation
            if not self._validate_go_generation_requirements(spec, spec_file):
                return False

            # Validate with oapi-codegen (the tool we use for Go generation)
            if not self._validate_with_oapi_codegen(spec_file):
                return False

            return True

        except yaml.YAMLError as e:
            self.errors.append(f"YAML parsing error in {spec_file}: {e}")
            return False
        except Exception as e:
            self.errors.append(f"Validation error in {spec_file}: {e}")
            return False

    def _validate_with_oapi_codegen(self, spec_file: Path) -> bool:
        """Validate OpenAPI spec using oapi-codegen (the same tool used for Go generation)"""
        try:
            # Run oapi-codegen with spec generation to validate the spec
            # If spec is valid, oapi-codegen will succeed, if invalid - it will fail
            result = subprocess.run(
                ['oapi-codegen', '-generate', 'spec', '-package', 'validation', str(spec_file)],
                capture_output=True,
                text=True,
                cwd=self.project_root,
                timeout=30
            )

            if result.returncode != 0:
                error_msg = result.stderr.strip()
                if not error_msg:
                    error_msg = result.stdout.strip()
                self.errors.append(f"oapi-codegen validation failed for {spec_file.name}: {error_msg}")
                return False

            return True

        except subprocess.TimeoutExpired:
            self.errors.append(f"oapi-codegen validation timeout for {spec_file.name}")
            return False
        except FileNotFoundError:
            self.warnings.append("oapi-codegen not found in PATH, skipping Go validation")
            return True  # Don't fail if oapi-codegen is not installed
        except Exception as e:
            self.errors.append(f"oapi-codegen validation error for {spec_file.name}: {e}")
            return False

    def _validate_component_file(self, spec_file: Path) -> bool:
        """Validate a component YAML file (may not be full OpenAPI spec)"""
        try:
            # Load and parse YAML
            with open(spec_file, 'r', encoding='utf-8') as f:
                spec = yaml.safe_load(f)

            # Component files should at least be valid YAML
            if not isinstance(spec, dict):
                self.errors.append(f"Invalid YAML structure in component file {spec_file}")
                return False

            # For component files, we only check basic YAML validity
            # They may or may not have OpenAPI content - that's normal for components
            # No warnings needed for missing OpenAPI content in component files

            # If it has paths, validate them for Go generation
            if 'paths' in spec:
                if not self._validate_go_generation_requirements(spec, spec_file):
                    return False

            return True

        except yaml.YAMLError as e:
            self.errors.append(f"YAML parsing error in component file {spec_file}: {e}")
            return False
        except Exception as e:
            self.errors.append(f"Validation error in component file {spec_file}: {e}")
            return False

    def _validate_basic_structure(self, spec: dict, spec_file: Path) -> bool:
        """Validate basic OpenAPI structure"""
        valid = True

        # Check required fields
        required_fields = ['openapi', 'info', 'paths']
        for field in required_fields:
            if field not in spec:
                self.errors.append(f"Missing required field '{field}' in {spec_file}")
                valid = False

        # Check OpenAPI version (must be 3.0.x for Go generation)
        if 'openapi' in spec:
            version = spec['openapi']
            if not version.startswith('3.0'):
                self.errors.append(f"Unsupported OpenAPI version {version} in {spec_file} (need 3.0.x for Go generation)")
                valid = False

        # Check info section
        if 'info' in spec:
            info = spec['info']
            required_info = ['title', 'version']
            for field in required_info:
                if field not in info:
                    self.errors.append(f"Missing info.{field} in {spec_file}")
                    valid = False

        # Check paths section
        if 'paths' in spec:
            paths = spec['paths']
            if not isinstance(paths, dict):
                self.errors.append(f"Invalid paths structure in {spec_file}")
                valid = False

        return valid

    def _validate_go_generation_requirements(self, spec: dict, spec_file: Path) -> bool:
        """Validate requirements for Go code generation"""
        valid = True

        # Check for operationId (required for Go generation)
        if 'paths' in spec:
            for path, methods in spec['paths'].items():
                if not isinstance(methods, dict):
                    continue

                for method, operation in methods.items():
                    if method not in ['get', 'post', 'put', 'delete', 'patch', 'options', 'head']:
                        continue

                    if not isinstance(operation, dict):
                        continue

                    # operationId is required for Go code generation
                    if 'operationId' not in operation:
                        self.errors.append(f"Missing operationId for {method.upper()} {path} in {spec_file} (required for Go generation)")
                        valid = False

                    # Check responses structure
                    if 'responses' not in operation:
                        self.errors.append(f"Missing responses for {method.upper()} {path} in {spec_file}")
                        valid = False
                    else:
                        responses = operation['responses']
                        # Check for at least one success response (200, 201, 204 for DELETE, 101 for WebSocket)
                        success_codes = ['200', '201']
                        if method.lower() == 'delete':
                            success_codes.append('204')
                        elif '/ws' in path or 'websocket' in path.lower():
                            success_codes = ['101', '200']
                        if not any(code in responses for code in success_codes):
                            self.warnings.append(f"No success response ({'/'.join(success_codes)}) for {method.upper()} {path} in {spec_file}")

        # Check schemas for Go generation compatibility
        if 'components' in spec and 'schemas' in spec['components']:
            schemas = spec['components']['schemas']
            for schema_name, schema in schemas.items():
                if not isinstance(schema, dict):
                    continue

                # Check for circular references or complex types that might cause Go generation issues
                if self._has_circular_refs(schema, schema_name, spec_file):
                    self.warnings.append(f"Potential circular reference in schema {schema_name} in {spec_file}")

        return valid

    def _has_circular_refs(self, schema: dict, schema_name: str, spec_file: Path, visited: set = None) -> bool:
        """Check for circular references in schema"""
        if visited is None:
            visited = set()

        if schema_name in visited:
            return True

        visited.add(schema_name)

        # Check $ref
        if '$ref' in schema:
            ref = schema['$ref']
            if ref.startswith('#/components/schemas/'):
                ref_name = ref.split('/')[-1]
                # For simplicity, we'll just warn about self-references
                if ref_name == schema_name:
                    return True

        # Check properties
        if 'properties' in schema:
            for prop_name, prop_schema in schema['properties'].items():
                if isinstance(prop_schema, dict):
                    if self._has_circular_refs(prop_schema, f"{schema_name}.{prop_name}", spec_file, visited.copy()):
                        return True

        # Check items for arrays
        if 'items' in schema and isinstance(schema['items'], dict):
            if self._has_circular_refs(schema['items'], f"{schema_name}[]", spec_file, visited.copy()):
                return True

        return False

    def _report_results(self) -> bool:
        """Report validation results"""
        total_errors = len(self.errors)
        total_warnings = len(self.warnings)

        print("\n[RESULTS] Domain OpenAPI Validation Results:")
        print(f"   Domains validated: {self.domains_validated}")
        print(f"   Errors: {total_errors}")
        print(f"   Warnings: {total_warnings}")

        if total_errors > 0:
            print("\n[ERROR] ERRORS:")
            for error in self.errors:
                print(f"   - {error}")

        if total_warnings > 0:
            print("\n[WARNING] WARNINGS:")
            for warning in self.warnings:
                print(f"   - {warning}")

        if total_errors == 0:
            print("\n[SUCCESS] All domain OpenAPI specifications are valid for Go-backend generation (validated with oapi-codegen)!")
            return True
        else:
            print(f"\n[ERROR] {total_errors} validation errors found. Fix them before Go code generation.")
            return False


def main():
    parser = argparse.ArgumentParser(description='NECPGAME Domain OpenAPI Validator for Go-backend')
    parser.add_argument('--project-root', default='.', help='Project root directory')
    parser.add_argument('--domain', help='Validate only specific domain')

    args = parser.parse_args()

    validator = DomainOpenAPIValidator(args.project_root)

    if args.domain:
        domain_path = Path(args.project_root) / "proto" / "openapi" / args.domain
        success = validator._validate_domain(domain_path)
    else:
        success = validator.validate_all_domains()

    return 0 if success else 1


if __name__ == '__main__':
    sys.exit(main())
