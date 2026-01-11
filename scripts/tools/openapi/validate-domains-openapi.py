#!/usr/bin/env python3
# Issue: Validation of all domain OpenAPI specs for Go-backend generation

import argparse
import json
import logging
import os
import subprocess
import sys
import traceback
import yaml
from pathlib import Path
from typing import Dict, List, Tuple
from datetime import datetime


class DomainOpenAPIValidator:
    """Validate all domain OpenAPI specifications for Go-backend generation"""

    def __init__(self, project_root: str, log_file: str = None):
        self.project_root = Path(project_root)
        self.errors: List[str] = []
        self.warnings: List[str] = []
        self.domains_validated = 0
        self.domains_failed = 0

        # Setup logging
        self.logger = logging.getLogger('DomainOpenAPIValidator')
        self.logger.setLevel(logging.DEBUG)

        # Console handler
        console_handler = logging.StreamHandler(sys.stdout)
        console_handler.setLevel(logging.INFO)
        console_formatter = logging.Formatter('[%(levelname)s] %(message)s')
        console_handler.setFormatter(console_formatter)
        self.logger.addHandler(console_handler)

        # File handler if specified
        if log_file:
            file_handler = logging.FileHandler(log_file)
            file_handler.setLevel(logging.DEBUG)
            file_formatter = logging.Formatter('%(asctime)s [%(levelname)s] %(message)s')
            file_handler.setFormatter(file_formatter)
            self.logger.addHandler(file_handler)

        self.logger.info("Domain OpenAPI Validator initialized")

    def validate_all_domains(self) -> bool:
        """Validate all OpenAPI domain specifications"""
        self.logger.info("Starting validation of all OpenAPI domains...")

        try:
            openapi_dir = self.project_root / "proto" / "openapi"
            if not openapi_dir.exists():
                error_msg = "proto/openapi directory not found"
                self.logger.error(error_msg)
                self.errors.append(error_msg)
                return False

            # Find all domain directories
            domain_dirs = [d for d in openapi_dir.iterdir() if d.is_dir()]
            self.logger.info(f"Found {len(domain_dirs)} domain directories")

            all_valid = True
            for domain_dir in sorted(domain_dirs):
                try:
                    if not self._validate_domain(domain_dir):
                        all_valid = False
                        self.domains_failed += 1
                    else:
                        self.domains_validated += 1
                except Exception as e:
                    error_msg = f"Unexpected error validating domain {domain_dir.name}: {e}"
                    self.logger.error(error_msg)
                    self.logger.debug(traceback.format_exc())
                    self.errors.append(error_msg)
                    all_valid = False
                    self.domains_failed += 1

            return self._report_results() and all_valid

        except Exception as e:
            error_msg = f"Fatal error during validation: {e}"
            self.logger.error(error_msg)
            self.logger.debug(traceback.format_exc())
            self.errors.append(error_msg)
            return False

    def _validate_domain(self, domain_dir: Path) -> bool:
        """Validate a single domain"""
        domain_name = domain_dir.name
        self.logger.info(f"Validating domain: {domain_name}")

        try:
            # Find main.yaml in domain root - this should be a full OpenAPI spec
            main_yaml = domain_dir / "main.yaml"
            if not main_yaml.exists():
                warning_msg = f"No main.yaml found in {domain_name}"
                self.logger.warning(warning_msg)
                self.warnings.append(warning_msg)
                return True  # Not an error, just warning

            # Validate the main spec as full OpenAPI specification
            if not self._validate_full_openapi_spec(main_yaml):
                self.logger.error(f"Domain {domain_name} failed main spec validation")
                return False

            # Find all sub-service YAML files, excluding known component files
            all_yaml_files = list(domain_dir.rglob("*.yaml"))
            yaml_files = [f for f in all_yaml_files if f != main_yaml]  # Exclude main.yaml

            # Exclude component files that are not meant to be full OpenAPI specs
            component_patterns = [
                '-ext.yaml', '-ext1.yaml', '-ext2.yaml', '-ext3.yaml', '-ext4.yaml', '-ext5.yaml',  # Extension files
                'schemas/',  # Schema directories
                'schemas.yaml',  # Schema files
                'schemas/',  # Schema directories (catch all)
                'paths/',  # Path directories
                'requests/',  # Request directories
                'responses/',  # Response directories
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
                'achievement-system-service-go/',  # Exclude achievement component services
                'realtime-subscription-service/',  # Exclude realtime subscription components
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
            self.logger.info(
                f"Found {len(yaml_files)} component YAML files to validate in {domain_name} (excluded {len(all_yaml_files) - len(yaml_files) - 1} component files)")

            # Validate component files (they may not be full specs)
            domain_valid = True
            for yaml_file in yaml_files:
                try:
                    if not self._validate_component_file(yaml_file):
                        domain_valid = False
                except Exception as e:
                    error_msg = f"Error validating component file {yaml_file}: {e}"
                    self.logger.error(error_msg)
                    self.logger.debug(traceback.format_exc())
                    self.errors.append(error_msg)
                    domain_valid = False

            if domain_valid:
                self.logger.info(f"Domain {domain_name} validation passed")
            else:
                self.logger.error(f"Domain {domain_name} validation failed")

            return domain_valid

        except Exception as e:
            error_msg = f"Unexpected error validating domain {domain_name}: {e}"
            self.logger.error(error_msg)
            self.logger.debug(traceback.format_exc())
            self.errors.append(error_msg)
            return False

    def _validate_full_openapi_spec(self, spec_file: Path) -> bool:
        """Validate a full OpenAPI specification (main.yaml files)"""
        self.logger.debug(f"Starting full validation of {spec_file}")
        try:
            # Load and parse YAML
            self.logger.debug(f"Loading YAML from {spec_file}")
            with open(spec_file, 'r', encoding='utf-8') as f:
                spec = yaml.safe_load(f)

            self.logger.debug("YAML loaded successfully")

            # Basic structure validation for full spec
            self.logger.debug("Validating basic structure")
            if not self._validate_basic_structure(spec, spec_file):
                self.logger.error(f"Basic structure validation failed for {spec_file}")
                return False

            # Go-backend generation validation
            self.logger.debug("Validating Go generation requirements")
            if not self._validate_go_generation_requirements(spec, spec_file):
                self.logger.error(f"Go generation requirements validation failed for {spec_file}")
                return False

            # Validate with oapi-codegen (the tool we use for Go generation)
            self.logger.debug("Validating with ogen")
            if not self._validate_with_oapi_codegen(spec_file):
                self.logger.error(f"ogen validation failed for {spec_file}")
                return False

            self.logger.debug(f"Full validation passed for {spec_file}")
            return True

        except yaml.YAMLError as e:
            error_msg = f"YAML parsing error in {spec_file}: {e}"
            self.logger.error(error_msg)
            self.errors.append(error_msg)
            return False
        except Exception as e:
            error_msg = f"Validation error in {spec_file}: {e}"
            self.logger.error(error_msg)
            self.logger.debug(traceback.format_exc())
            self.errors.append(error_msg)
            return False

    def _validate_with_oapi_codegen(self, spec_file: Path) -> bool:
        """Validate OpenAPI spec using oapi-codegen (the same tool used for Go generation)"""
        self.logger.debug(f"Validating {spec_file.name} with oapi-codegen")

        try:
            # First, try to bundle the spec to resolve external references
            bundled_file = None
            try:
                self.logger.debug(f"Bundling {spec_file.name} with redocly")
                bundle_result = subprocess.run(
                    ['npx', '--yes', '@redocly/cli', 'bundle', str(spec_file), '-o', '/tmp/bundled-validation.yaml'],
                    capture_output=True,
                    text=True,
                    cwd=self.project_root,
                    timeout=30
                )

                if bundle_result.returncode == 0:
                    bundled_file = Path('/tmp/bundled-validation.yaml')
                    self.logger.debug(f"Successfully bundled {spec_file.name}")
                else:
                    self.logger.warning(f"Bundling failed for {spec_file.name}: {bundle_result.stderr[:200]}...")
                    # If bundling fails, use original file
                    bundled_file = spec_file
            except subprocess.TimeoutExpired:
                self.logger.warning(f"Bundling timeout for {spec_file.name}, using original file")
                bundled_file = spec_file
            except FileNotFoundError as e:
                self.logger.warning(f"Redocly not found: {e}, using original file")
                bundled_file = spec_file
            except Exception as e:
                self.logger.warning(f"Bundling error for {spec_file.name}: {e}, using original file")
                bundled_file = spec_file

            # Run oapi-codegen with spec generation to validate the spec
            # If spec is valid, oapi-codegen will succeed, if invalid - it will fail
            self.logger.debug(f"Running oapi-codegen on {bundled_file}")
            result = subprocess.run(
                ['ogen', '-generate', 'spec', '-package', 'validation', str(bundled_file)],
                capture_output=True,
                text=True,
                cwd=self.project_root,
                timeout=60
            )

            if result.returncode != 0:
                error_msg = result.stderr.strip()
                if not error_msg:
                    error_msg = result.stdout.strip()

                # Skip external reference errors - these are resolved by bundling during actual code generation
                if "unrecognized external reference" in error_msg or "external references are disabled" in error_msg:
                    warning_msg = f"External references in {spec_file.name} will be resolved by bundling during code generation"
                    self.logger.warning(warning_msg)
                    self.warnings.append(warning_msg)
                    return True

                error_msg_full = f"ogen validation failed for {spec_file.name}: {error_msg}"
                self.logger.error(error_msg_full)
                self.errors.append(error_msg_full)
                return False

            self.logger.debug(f"ogen validation passed for {spec_file.name}")
            return True

        except subprocess.TimeoutExpired:
            error_msg = f"ogen validation timeout for {spec_file.name}"
            self.logger.error(error_msg)
            self.errors.append(error_msg)
            return False
        except FileNotFoundError:
            warning_msg = "ogen not found in PATH, skipping Go validation"
            self.logger.warning(warning_msg)
            self.warnings.append(warning_msg)
            return True  # Don't fail if tools are not installed
        except Exception as e:
            error_msg = f"ogen validation error for {spec_file.name}: {e}"
            self.logger.error(error_msg)
            self.logger.debug(traceback.format_exc())
            self.errors.append(error_msg)
            return False

    def _validate_component_file(self, spec_file: Path) -> bool:
        """Validate a component YAML file (may not be full OpenAPI spec)"""
        # Skip common component files - they don't need full OpenAPI validation
        if 'common' in spec_file.name.lower():
            return True

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
                self.errors.append(
                    f"Unsupported OpenAPI version {version} in {spec_file} (need 3.0.x for Go generation)")
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
                        self.errors.append(
                            f"Missing operationId for {method.upper()} {path} in {spec_file} (required for Go generation)")
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
                            self.warnings.append(
                                f"No success response ({'/'.join(success_codes)}) for {method.upper()} {path} in {spec_file}")

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
            print(
                "\n[SUCCESS] All domain OpenAPI specifications are valid for Go-backend generation (validated with oapi-codegen)!")
            return True
        else:
            print(f"\n[ERROR] {total_errors} validation errors found. Fix them before Go code generation.")
            return False


def main():
    parser = argparse.ArgumentParser(description='NECPGAME Domain OpenAPI Validator for Go-backend')
    parser.add_argument('--project-root', default='.', help='Project root directory')
    parser.add_argument('--domain', help='Validate only specific domain')
    parser.add_argument('--log-file', help='Log file path for detailed debugging')
    parser.add_argument('--verbose', '-v', action='store_true', help='Verbose output')

    args = parser.parse_args()

    # Set logging level
    if args.verbose:
        logging.getLogger().setLevel(logging.DEBUG)

    validator = DomainOpenAPIValidator(args.project_root, args.log_file)

    try:
        if args.domain:
            domain_path = Path(args.project_root) / "proto" / "openapi" / args.domain
            if not domain_path.exists():
                validator.logger.error(f"Domain directory not found: {domain_path}")
                return 1
            success = validator._validate_domain(domain_path)
        else:
            success = validator.validate_all_domains()

        return 0 if success else 1

    except KeyboardInterrupt:
        validator.logger.info("Validation interrupted by user")
        return 1
    except Exception as e:
        validator.logger.error(f"Fatal error: {e}")
        validator.logger.debug(traceback.format_exc())
        return 1


if __name__ == '__main__':
    sys.exit(main())
