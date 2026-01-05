#!/usr/bin/env python3
"""
NECPGAME Problematic Services Generator
Generates only failed/problematic services from OpenAPI specifications individually

This script helps generate services that failed in bulk generation, allowing:
- Generate single problematic services
- Generate all missing services
- Detailed error logging per service
- Skip already generated services
"""

import sys
import os
import subprocess
import traceback
from pathlib import Path
from typing import List, Tuple, Dict, Any, Optional
from datetime import datetime
import json
import logging

class ProblematicServicesGenerator:
    """Generator for problematic/missing services"""

    def __init__(self, logger: logging.Logger):
        self.logger = logger
        self.project_root = Path(__file__).parent.parent
        self.proto_dir = self.project_root / "proto" / "openapi"
        self.services_dir = self.project_root / "services"

    def find_all_services(self) -> List[Tuple[str, Path]]:
        """Find all services with main.yaml in proto/openapi"""
        services = []

        if not self.proto_dir.exists():
            self.logger.error(f"proto/openapi directory not found: {self.proto_dir.absolute()}")
            return services

        for item in self.proto_dir.iterdir():
            if item.is_dir() and item.name.endswith("-service") and item.name != "common-service":
                spec_path = item / "main.yaml"
                if spec_path.exists():
                    # Validate YAML syntax
                    try:
                        import yaml
                        with open(spec_path, 'r', encoding='utf-8') as f:
                            yaml.safe_load(f)
                        services.append((item.name, spec_path))
                        self.logger.debug(f"Found valid service: {item.name} at {spec_path}")
                    except Exception as e:
                        error_msg = f"YAML validation failed for {item.name} at {spec_path}: {e}"
                        self.logger.error(error_msg)
                        continue
                else:
                    self.logger.warning(f"No main.yaml found in {item.name} at {spec_path}")

        return services

    def check_service_exists(self, service_name: str) -> bool:
        """Check if service is already generated (has Go files)"""
        service_path = self.services_dir / f"{service_name}-go"
        if not service_path.exists():
            return False

        # Check if there are any .go files in the service directory
        go_files = list(service_path.glob('*.go'))
        return len(go_files) > 0

    def generate_single_service(self, service_name: str, spec_path: Path, dry_run: bool = False) -> Tuple[bool, Optional[Dict[str, Any]]]:
        """Generate a single service with detailed error reporting"""
        service_output_dir = self.services_dir / f"{service_name}-go"

        try:
            self.logger.info(f"Starting generation of {service_name}")

            # Fix OpenAPI spec issues before generation
            self.fix_openapi_spec(spec_path)

            # Create service directory
            service_output_dir.mkdir(parents=True, exist_ok=True)
            self.logger.debug(f"Created directory: {service_output_dir}")

            # Try direct generation first (for specs without external refs)
            cmd = [
                "ogen",
                "--target", str(service_output_dir),
                "--package", "api",
                "--clean",
                str(spec_path.resolve())
            ]

            if dry_run:
                self.logger.info(f"[DRY RUN] Would run: {' '.join(cmd)}")
                return True, None

            self.logger.info(f"Running ogen for {service_name}...")
            self.logger.debug(f"Command: {' '.join(cmd)}")

            result = subprocess.run(cmd, capture_output=True, text=True, timeout=120)

            if result.returncode == 0:
                self.logger.info(f"[SUCCESS] {service_name} generated successfully")
                return True, None
            else:
                self.logger.warning(f"Direct generation failed for {service_name}, trying bundling...")

                # If direct generation failed, try bundling first
                bundled_spec_path = service_output_dir / "bundled.yaml"

                try:
                    self.logger.debug(f"Attempting to bundle {spec_path} -> {bundled_spec_path}")
                    from bundle_openapi import bundle_openapi_spec
                    bundle_openapi_spec(spec_path, bundled_spec_path)
                    bundle_result = subprocess.CompletedProcess(args=[], returncode=0, stdout="", stderr="")
                    self.logger.debug(f"Bundling successful for {service_name}")
                except Exception as e:
                    error_msg = f"Python bundling failed for {service_name} at {spec_path}: {e}"
                    self.logger.error(error_msg)
                    bundle_result = subprocess.CompletedProcess(args=[], returncode=1, stdout="", stderr=str(e))

                if bundle_result.returncode == 0:
                    # Create ogen.yml to ignore array defaults
                    ogen_config_path = service_output_dir / "ogen.yml"
                    ogen_config = {
                        "generator": {
                            "ignore_not_implemented": ["all"]
                        }
                    }
                    try:
                        import yaml
                        with open(ogen_config_path, 'w', encoding='utf-8') as f:
                            yaml.dump(ogen_config, f, default_flow_style=False)
                        self.logger.debug(f"Created ogen.yml at {ogen_config_path}")
                    except Exception as e:
                        self.logger.warning(f"Failed to create ogen.yml: {e}")

                    # Try generation with bundled spec
                    cmd = [
                        "ogen",
                        "--target", str(service_output_dir),
                        "--package", "api",
                        "--clean",
                        "--config", str(ogen_config_path),
                        str(bundled_spec_path)
                    ]

                    self.logger.info(f"Retrying ogen with bundled spec for {service_name}")
                    result = subprocess.run(cmd, capture_output=True, text=True, timeout=120)

                    if result.returncode == 0:
                        self.logger.info(f"[SUCCESS] {service_name} generated with bundling")
                        return True, None

                # Parse error details from ogen output
                error_info = self.parse_ogen_error(result.stderr)

                # Collect detailed error information
                error_details = {
                    'service': service_name,
                    'spec_path': str(spec_path),
                    'output_dir': str(service_output_dir),
                    'error_file': error_info['file'],
                    'error_line': error_info['line'],
                    'error_message': error_info['error'],
                    'error_context': error_info['context'],
                    'full_stderr': result.stderr,
                    'bundling_stderr': bundle_result.stderr if bundle_result.returncode != 0 else None,
                    'bundling_stdout': bundle_result.stdout if bundle_result.returncode != 0 else None,
                    'timestamp': datetime.now().isoformat()
                }

                # Enhanced error reporting
                error_msg = f"[ERROR] {service_name} failed generation"
                self.logger.error(error_msg)
                self.logger.error(f"  File: {error_info['file']}:{error_info['line']}")
                self.logger.error(f"  Error: {error_info['error']}")

                if error_info['context']:
                    self.logger.error("  Context:")
                    for ctx_line in error_info['context']:
                        self.logger.error(f"    {ctx_line}")

                return False, error_details

        except subprocess.TimeoutExpired:
            error_msg = f"[TIMEOUT] {service_name} generation timed out"
            self.logger.error(error_msg)
            return False, {
                'service': service_name,
                'error': 'timeout',
                'timeout_seconds': 120,
                'timestamp': datetime.now().isoformat()
            }

        except Exception as e:
            error_msg = f"[EXCEPTION] {service_name}: {e}"
            self.logger.error(error_msg)
            self.logger.debug(traceback.format_exc())
            return False, {
                'service': service_name,
                'error': str(e),
                'traceback': traceback.format_exc(),
                'timestamp': datetime.now().isoformat()
            }

    def parse_ogen_error(self, stderr: str) -> Dict[str, Any]:
        """Parse ogen error output to extract file, line, and error details"""
        lines = stderr.strip().split('\n')
        error_info = {
            'file': 'unknown',
            'line': 'unknown',
            'error': stderr,
            'context': []
        }

        for i, line in enumerate(lines):
            # Look for error patterns like "bundled.yaml:123:45 -> error message"
            if '.yaml:' in line and '->' in line:
                parts = line.split('->')
                if len(parts) >= 2:
                    location_part = parts[0].strip()
                    error_part = parts[1].strip()

                    # Extract file and line info
                    if ':' in location_part:
                        # Handle cases like "bundled.yaml:856:13" or "- bundled.yaml:856:13"
                        clean_location = location_part.lstrip('- ').strip()
                        file_info = clean_location.split(':')
                        if len(file_info) >= 2:
                            error_info['file'] = file_info[0]
                            if len(file_info) > 1:
                                error_info['line'] = file_info[1]
                            if len(file_info) > 2:
                                error_info['column'] = file_info[2]

                    error_info['error'] = error_part

                    # Add context lines
                    start_idx = max(0, i - 2)
                    end_idx = min(len(lines), i + 3)
                    error_info['context'] = lines[start_idx:end_idx]

                    break
            # Look for "at bundled.yaml:123:45" patterns
            elif 'at ' in line and '.yaml:' in line:
                parts = line.split('at ')
                if len(parts) >= 2:
                    location = parts[1].strip()
                    if ':' in location:
                        loc_parts = location.split(':')
                        error_info['file'] = loc_parts[0]
                        if len(loc_parts) > 1:
                            error_info['line'] = loc_parts[1]
                        if len(loc_parts) > 2:
                            error_info['column'] = loc_parts[2]

        return error_info

    def fix_openapi_spec(self, spec_path: Path) -> bool:
        """Fix common OpenAPI issues that prevent code generation"""
        try:
            import yaml

            with open(spec_path, 'r', encoding='utf-8') as f:
                data = yaml.safe_load(f)

            modified = False

            # Fix operations without default responses
            if 'paths' in data:
                for path, path_item in data['paths'].items():
                    if isinstance(path_item, dict):
                        for method, operation in path_item.items():
                            if isinstance(operation, dict) and 'responses' in operation:
                                if 'default' not in operation['responses']:
                                    operation['responses']['default'] = {
                                        'description': 'Unexpected error',
                                        'content': {
                                            'application/json': {
                                                'schema': {
                                                    '$ref': '../common-service/schemas/error.yaml#/Error'
                                                }
                                            }
                                        }
                                    }
                                    modified = True
                                    self.logger.debug(f"Added default response to {method.upper()} {path}")

            # Fix headers with schema: null
            def fix_headers(obj):
                if isinstance(obj, dict):
                    if 'headers' in obj:
                        for header_name, header_spec in obj['headers'].items():
                            if isinstance(header_spec, dict) and header_spec.get('schema') is None:
                                # Replace schema: null with proper string schema
                                header_spec['schema'] = {
                                    'type': 'string',
                                    'example': f'"{header_name.lower()}-value"',
                                    'description': f'{header_name} header value'
                                }
                                nonlocal modified
                                modified = True
                                self.logger.debug(f"Fixed null schema in header {header_name}")

                    # Recursively check nested objects
                    for key, value in obj.items():
                        if key != 'headers':  # Avoid infinite recursion
                            fix_headers(value)
                elif isinstance(obj, list):
                    for item in obj:
                        fix_headers(item)

            fix_headers(data)

            # Save if modified
            if modified:
                with open(spec_path, 'w', encoding='utf-8') as f:
                    yaml.dump(data, f, default_flow_style=False, allow_unicode=True, sort_keys=False)
                self.logger.info(f"Fixed OpenAPI spec: {spec_path}")
                return True

            return False

        except Exception as e:
            self.logger.error(f"Failed to fix OpenAPI spec {spec_path}: {e}")
            return False

    def analyze_generation_status(self) -> Dict[str, Any]:
        """Analyze which services exist and which don't"""
        services = self.find_all_services()
        status = {
            'total_services': len(services),
            'existing_services': [],
            'missing_services': [],
            'services': {}
        }

        for service_name, spec_path in services:
            exists = self.check_service_exists(service_name)
            service_info = {
                'name': service_name,
                'spec_path': str(spec_path),
                'exists': exists,
                'service_path': str(self.services_dir / f"{service_name}-go")
            }

            if exists:
                status['existing_services'].append(service_name)
            else:
                status['missing_services'].append(service_name)

            status['services'][service_name] = service_info

        return status

    def generate_problematic_services(self, specific_service: Optional[str] = None,
                                    failed_only: bool = True, dry_run: bool = False) -> Dict[str, Any]:
        """Generate problematic/missing services"""
        status = self.analyze_generation_status()

        if specific_service:
            if specific_service not in status['services']:
                self.logger.error(f"Service {specific_service} not found in proto/openapi")
                return {'error': f'Service {specific_service} not found'}

            services_to_generate = [(specific_service, Path(status['services'][specific_service]['spec_path']))]
        else:
            if failed_only:
                services_to_generate = [
                    (name, Path(info['spec_path']))
                    for name, info in status['services'].items()
                    if not info['exists']
                ]
            else:
                services_to_generate = [
                    (name, Path(info['spec_path']))
                    for name, info in status['services'].items()
                ]

        self.logger.info(f"Found {len(services_to_generate)} services to generate")

        results = {
            'total_attempted': len(services_to_generate),
            'successful': [],
            'failed': [],
            'errors': []
        }

        for service_name, spec_path in services_to_generate:
            self.logger.info(f"Processing {service_name}...")

            success, error_details = self.generate_single_service(service_name, spec_path, dry_run)

            if success:
                results['successful'].append(service_name)
                self.logger.info(f"[OK] {service_name} generated successfully")
            else:
                results['failed'].append(service_name)
                results['errors'].append(error_details)
                self.logger.error(f"[FAIL] {service_name} failed")

        return results

def main():
    import argparse

    parser = argparse.ArgumentParser(description="Generate problematic/missing services from OpenAPI specs")
    parser.add_argument("--service", help="Generate specific service only (e.g., pet-service)")
    parser.add_argument("--all", action="store_true", help="Generate all services (not just missing ones)")
    parser.add_argument("--dry-run", action="store_true", help="Show what would be done without actually generating")
    parser.add_argument("--status", action="store_true", help="Show generation status without generating")
    parser.add_argument("--verbose", "-v", action="store_true", help="Verbose logging")
    parser.add_argument("--log-file", help="Save detailed logs to file")

    args = parser.parse_args()

    # Setup logging
    logger = logging.getLogger("ProblematicServicesGenerator")
    logger.setLevel(logging.DEBUG if args.verbose else logging.INFO)

    # Console handler
    console_handler = logging.StreamHandler(sys.stdout)
    console_handler.setLevel(logging.INFO)
    console_formatter = logging.Formatter('[%(levelname)s] %(message)s')
    console_handler.setFormatter(console_formatter)
    logger.addHandler(console_handler)

    # File handler if requested
    if args.log_file:
        file_handler = logging.FileHandler(args.log_file)
        file_handler.setLevel(logging.DEBUG)
        file_formatter = logging.Formatter('%(asctime)s [%(levelname)s] %(message)s')
        file_handler.setFormatter(file_formatter)
        logger.addHandler(file_handler)

    # Change to project root
    os.chdir(Path(__file__).parent.parent)

    generator = ProblematicServicesGenerator(logger)

    if args.status:
        # Show status only
        status = generator.analyze_generation_status()
        print(f"\n{'='*60}")
        print("SERVICE GENERATION STATUS")
        print(f"{'='*60}")
        print(f"Total services in proto/openapi: {status['total_services']}")
        print(f"Already generated: {len(status['existing_services'])}")
        print(f"Missing/not generated: {len(status['missing_services'])}")

        if status['existing_services']:
            print(f"\n[EXISTING] Generated services:")
            for service in sorted(status['existing_services']):
                print(f"  [OK] {service}")

        if status['missing_services']:
            print(f"\n[MISSING] Services to generate:")
            for service in sorted(status['missing_services']):
                print(f"  [TODO] {service}")

        return

    # Generate services
    results = generator.generate_problematic_services(
        specific_service=args.service,
        failed_only=not args.all,
        dry_run=args.dry_run
    )

    # Print results
    print(f"\n{'='*60}")
    print("GENERATION RESULTS")
    print(f"{'='*60}")

    if 'error' in results:
        print(f"[ERROR] {results['error']}")
        return 1

    print(f"Total attempted: {results['total_attempted']}")
    print(f"Successful: {len(results['successful'])}")
    print(f"Failed: {len(results['failed'])}")

    if results['successful']:
        print(f"\n[SUCCESS] Generated services:")
        for service in sorted(results['successful']):
            print(f"  [OK] {service}")

    if results['failed']:
        print(f"\n[FAILED] Services that failed:")
        for service_name, error_details in zip(sorted(results['failed']), results['errors']):
            print(f"  [FAIL] {service_name}")
            if isinstance(error_details, dict):
                if 'error_file' in error_details and 'error_line' in error_details:
                    print(f"         Location: {error_details['error_file']}:{error_details['error_line']}")
                if 'error_message' in error_details:
                    print(f"         Error: {error_details['error_message']}")
                if 'error_context' in error_details and error_details['error_context']:
                    print(f"         Context:")
                    for ctx_line in error_details['error_context'][:3]:  # Show first 3 context lines
                        print(f"           {ctx_line}")
                if 'spec_path' in error_details:
                    print(f"         Spec: {error_details['spec_path']}")

        if args.log_file:
            print(f"\nDetailed error logs saved to: {args.log_file}")

    success_rate = len(results['successful']) / results['total_attempted'] * 100 if results['total_attempted'] > 0 else 0
    print(f"\nSuccess rate: {success_rate:.1f}%")

    if results['failed']:
        print(f"\nTo retry failed services:")
        print(f"  python scripts/generate-problematic-services.py --service <service-name>")
        print(f"  python scripts/generate-problematic-services.py  # Generate all missing")

        return 1 if not args.dry_run else 0
    else:
        return 0

if __name__ == "__main__":
    sys.exit(main())
