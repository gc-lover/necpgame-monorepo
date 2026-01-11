#!/usr/bin/env python3
"""
Generate all enterprise-grade services from OpenAPI specifications
"""

import logging
import os
import sys
import subprocess
import traceback
from pathlib import Path
from concurrent.futures import ThreadPoolExecutor, as_completed
from datetime import datetime

def generate_service(service_name, spec_path, output_dir, logger):
    """Generate a single service using ogen"""
    service_output_dir = output_dir / f"{service_name}-go"

    try:
        logger.info(f"Starting generation of {service_name}")

        # Create service directory
        service_output_dir.mkdir(parents=True, exist_ok=True)
        logger.debug(f"Created directory: {service_output_dir}")

        # Try direct generation first (for specs without external refs)
        cmd = [
            "ogen",
            "--target", str(service_output_dir),
            "--package", "api",
            "--clean",
            str(spec_path.resolve())
        ]

        logger.info(f"Running ogen for {service_name}...")
        logger.debug(f"Command: {' '.join(cmd)}")

        result = subprocess.run(cmd, capture_output=True, text=True, timeout=120)

        if result.returncode == 0:
            logger.info(f"[SUCCESS] {service_name} generated successfully")
            return True, None
        else:
            logger.warning(f"Direct generation failed for {service_name}, trying bundling...")

            # If direct generation failed, try bundling first
            bundled_spec_path = service_output_dir / "bundled.yaml"

            try:
                logger.debug(f"Attempting to bundle {spec_path} -> {bundled_spec_path}")
                bundle_cmd = [
                    sys.executable,
                    str(Path(__file__).parent / "bundle_openapi.py"),
                    str(spec_path),
                    str(bundled_spec_path)
                ]
                bundle_result = subprocess.run(bundle_cmd, capture_output=True, text=True, timeout=60)
                bundle_result = subprocess.CompletedProcess(args=[], returncode=0, stdout="", stderr="")
                logger.debug(f"Bundling successful for {service_name}")
            except Exception as e:
                error_msg = f"Python bundling failed for {service_name} at {spec_path}: {e}"
                logger.error(error_msg)
                bundle_result = subprocess.CompletedProcess(args=[], returncode=1, stdout="", stderr=str(e))

            if bundle_result.returncode == 0:
                # Try generation with bundled spec
                cmd = [
                    "ogen",
                    "--target", str(service_output_dir),
                    "--package", "api",
                    "--clean",
                    str(bundled_spec_path)
                ]

                logger.info(f"Retrying ogen with bundled spec for {service_name}")
                result = subprocess.run(cmd, capture_output=True, text=True, timeout=120)

                if result.returncode == 0:
                    logger.info(f"[SUCCESS] {service_name} generated with bundling")
                    return True, None

            # If all attempts failed, collect detailed error information
            error_details = {
                'service': service_name,
                'spec_path': str(spec_path),
                'output_dir': str(service_output_dir),
                'direct_stderr': result.stderr,
                'bundling_stderr': bundle_result.stderr if bundle_result.returncode != 0 else None,
                'bundling_stdout': bundle_result.stdout if bundle_result.returncode != 0 else None
            }

            error_msg = f"[ERROR] {service_name} failed generation at {spec_path}"
            logger.error(error_msg)
            if result.stderr:
                logger.error(f"Direct generation STDERR: {result.stderr[:500]}...")
            if bundle_result.returncode != 0 and bundle_result.stderr:
                logger.error(f"Bundling STDERR: {bundle_result.stderr[:500]}...")

            return False, error_details

    except subprocess.TimeoutExpired:
        error_msg = f"[TIMEOUT] {service_name} generation timed out"
        logger.error(error_msg)
        return False, {'service': service_name, 'error': 'timeout', 'timeout_seconds': 120}

    except Exception as e:
        error_msg = f"[EXCEPTION] {service_name}: {e}"
        logger.error(error_msg)
        logger.debug(traceback.format_exc())
        return False, {'service': service_name, 'error': str(e), 'traceback': traceback.format_exc()}

def setup_logging(log_file=None, verbose=False):
    """Setup logging configuration"""
    logger = logging.getLogger('ServiceGenerator')
    logger.setLevel(logging.DEBUG if verbose else logging.INFO)

    # Console handler
    console_handler = logging.StreamHandler(sys.stdout)
    console_handler.setLevel(logging.INFO)
    console_formatter = logging.Formatter('[%(levelname)s] %(message)s')
    console_handler.setFormatter(console_formatter)
    logger.addHandler(console_handler)

    # File handler
    if log_file:
        file_handler = logging.FileHandler(log_file)
        file_handler.setLevel(logging.DEBUG)
        file_formatter = logging.Formatter('%(asctime)s [%(levelname)s] %(message)s')
        file_handler.setFormatter(file_formatter)
        logger.addHandler(file_handler)

    return logger

def main():
    # Parse arguments manually for backward compatibility
    if len(sys.argv) < 2:
        print("Usage: python scripts/generate-all-services.py <output_dir> [--parallel N] [--log-file FILE] [--verbose] [--continue-on-error]")
        print("Example: python scripts/generate-all-services.py services --parallel 3 --log-file generation.log --verbose --continue-on-error")
        sys.exit(1)

    output_dir = sys.argv[1]
    parallel = 1
    log_file = None
    verbose = False
    continue_on_error = False

    i = 2
    while i < len(sys.argv):
        if sys.argv[i] == "--parallel" and i + 1 < len(sys.argv):
            try:
                parallel = int(sys.argv[i + 1])
                i += 2
            except ValueError:
                parallel = 1
                i += 1
        elif sys.argv[i] == "--log-file" and i + 1 < len(sys.argv):
            log_file = sys.argv[i + 1]
            i += 2
        elif sys.argv[i] in ["--verbose", "-v"]:
            verbose = True
            i += 1
        elif sys.argv[i] == "--continue-on-error":
            continue_on_error = True
            i += 1
        else:
            i += 1

    # Setup logging first
    logger = setup_logging(log_file, verbose)

    # Change to project root directory
    project_root = Path(__file__).parent.parent.parent.parent
    logger.info(f"Changing to project root: {project_root}")
    os.chdir(project_root)
    logger.info(f"Changed to project root: {os.getcwd()}")

    output_dir_path = Path(output_dir)
    logger.info(f"Starting service generation to {output_dir_path}")

    # Find all service specifications
    proto_dir = Path("proto/openapi")
    logger.info(f"Looking for proto_dir at: {proto_dir.absolute()}")
    if not proto_dir.exists():
        logger.error(f"proto/openapi directory not found: {proto_dir.absolute()}")
        return 1

    services = []
    skipped_services = []

    for item in proto_dir.iterdir():
        if item.is_dir() and item.name.endswith("-service"):
            spec_path = item / "main.yaml"
            if spec_path.exists():
                # Validate YAML syntax
                try:
                    import yaml
                    with open(spec_path, 'r', encoding='utf-8') as f:
                        yaml.safe_load(f)
                    services.append((item.name, spec_path))
                    logger.debug(f"Found valid service: {item.name} at {spec_path}")
                except Exception as e:
                    error_msg = f"YAML validation failed for {item.name} at {spec_path}: {e}"
                    logger.error(error_msg)
                    skipped_services.append((item.name, str(e), str(spec_path)))
                    continue
            else:
                logger.warning(f"No main.yaml found in {item.name} at {spec_path}")

    logger.info(f"Found {len(services)} services to generate, {len(skipped_services)} skipped due to YAML errors")

    if not services:
        logger.error("No valid services found to generate")
        return 1

    # Collect generation results
    successful_services = []
    failed_services = []

    if parallel == 1:
        # Sequential generation
        logger.info("Starting sequential generation...")
        for service_name, spec_path in services:
            success, error_details = generate_service(service_name, spec_path, output_dir_path, logger)
            if success:
                successful_services.append(service_name)
            else:
                failed_services.append((service_name, error_details))
                if not continue_on_error:
                    logger.error("Stopping due to error (use --continue-on-error to continue)")
                    break
    else:
        # Parallel generation
        logger.info(f"Starting parallel generation with {parallel} workers...")

        with ThreadPoolExecutor(max_workers=parallel) as executor:
            futures = {}
            for service_name, spec_path in services:
                future = executor.submit(generate_service, service_name, spec_path, output_dir_path, logger)
                futures[future] = service_name

            for future in as_completed(futures):
                service_name = futures[future]
                try:
                    success, error_details = future.result()
                    if success:
                        successful_services.append(service_name)
                    else:
                        failed_services.append((service_name, error_details))
                        if not continue_on_error:
                            logger.error("Stopping due to error (use --continue-on-error to continue)")
                            executor.shutdown(wait=False)
                            break
                except Exception as e:
                    error_msg = f"Unexpected error in {service_name}: {e}"
                    logger.error(error_msg)
                    failed_services.append((service_name, {'error': str(e), 'service_path': str(futures[future])}))

    # Report results
    success_count = len(successful_services)
    total_count = len(services)

    print(f"\n{'='*60}")
    print("GENERATION RESULTS SUMMARY")
    print(f"{'='*60}")
    print(f"Total services found: {len(services) + len(skipped_services)}")
    print(f"Services with YAML errors: {len(skipped_services)}")
    print(f"Services processed: {total_count}")
    print(f"Successfully generated: {success_count}")
    print(f"Failed generation: {len(failed_services)}")
    print(f"Success rate: {success_count/total_count*100:.1f}%" if total_count > 0 else "Success rate: N/A")
    print(f"{'='*60}")

    if successful_services:
        print("\n[SUCCESS] Generated services:")
        for service in sorted(successful_services):
            print(f"  [OK] {service}")

    if failed_services:
        print("\n[FAILED] Services that failed generation:")
        for service_name, error_details in failed_services:
            print(f"  [FAIL] {service_name}")
            if isinstance(error_details, dict):
                if 'spec_path' in error_details:
                    print(f"         Spec: {error_details['spec_path']}")
                if 'output_dir' in error_details:
                    print(f"         Output: {error_details['output_dir']}")
                if 'error' in error_details:
                    print(f"         Error: {error_details['error']}")
                if 'service_path' in error_details:
                    print(f"         Path: {error_details['service_path']}")

    if skipped_services:
        print("\n[SKIPPED] Services skipped due to YAML errors:")
        for service_name, error, spec_path in skipped_services:
            print(f"  [SKIP] {service_name}: {error}")
            print(f"         File: {spec_path}")

    if (failed_services or skipped_services) and log_file:
        print(f"\nDetailed error logs saved to: {log_file}")

    # Return appropriate exit code
    if failed_services and not continue_on_error:
        logger.error("Some services failed generation")
        return 1
    elif success_count == 0:
        logger.error("No services were generated successfully")
        return 1
    else:
        logger.info("Service generation completed")
        return 0

if __name__ == "__main__":
    main()
