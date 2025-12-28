#!/usr/bin/env python3
"""
NECPGAME Go Service Generator
Generates Go microservices from OpenAPI specifications

SOLID: Single Responsibility - orchestrates service generation
DRY: Uses composition of specialized components
"""

import sys
from pathlib import Path
from typing import Optional

# Add scripts directory to Python path for imports
script_path = Path(__file__).resolve()
scripts_dir = script_path.parent.parent  # scripts directory
project_root = scripts_dir.parent  # project root

print(f"[INIT] Script path: {script_path}")
print(f"[INIT] Scripts dir: {scripts_dir}")
print(f"[INIT] Project root: {project_root}")

# Add both scripts and project root to path
sys.path.insert(0, str(scripts_dir))
sys.path.insert(0, str(project_root))

print("[INIT] Importing modules...")
try:
    from core.command_runner import CommandRunner
    from core.config import ConfigManager
    from core.file_manager import FileManager
    from core.logger import Logger
    from openapi.openapi_manager import OpenAPIManager

    # Import specialized components
    from generation.bundler import OpenAPIBundler
    from generation.code_generator import GoCodeGenerator
    from generation.structure_creator import ServiceStructureCreator
    from generation.module_initializer import GoModuleInitializer
    from generation.tester import CompilationTester

    print("[INIT] Imports successful")
except ImportError as e:
    print(f"[ERROR] Import failed: {e}")
    print(f"[ERROR] Python path: {sys.path}")
    sys.exit(1)


class GoServiceGenerator:
    """
    Orchestrates Go service generation using composition of specialized components.
    SOLID: Single Responsibility - coordinates service generation
    DRY: Delegates to focused components
    """

    def __init__(self, config: ConfigManager, openapi_manager: OpenAPIManager,
                 file_manager: FileManager, command_runner: CommandRunner, logger_manager: Logger):
        print("[INIT] Initializing GoServiceGenerator")
        self.config = config
        self.openapi = openapi_manager
        self.file_manager = file_manager
        self.command_runner = command_runner
        self.logger = logger_manager.get_logger("GoServiceGenerator")

        # Compose specialized components (Dependency Injection)
        self.bundler = OpenAPIBundler(command_runner, logger_manager.get_logger("Bundler"))
        self.code_generator = GoCodeGenerator(command_runner, logger_manager.get_logger("CodeGenerator"))
        self.structure_creator = ServiceStructureCreator(logger_manager.get_logger("StructureCreator"))
        self.module_initializer = GoModuleInitializer(command_runner, logger_manager.get_logger("ModuleInitializer"))
        self.tester = CompilationTester(command_runner, logger_manager.get_logger("Tester"))

        print("[INIT] GoServiceGenerator initialized with components")

    def generate_domain_service(self, domain: str, skip_bundle: bool = False,
                                skip_test: bool = False, dry_run: bool = False) -> None:
        """
        Generate complete Go service for a domain using composition of components.
        DRY: Delegates to specialized components
        """
        try:
            # Get paths with error checking
            openapi_dir = self.config.get_openapi_dir()
            services_dir = self.config.get_services_dir()

            domain_dir = openapi_dir / domain
            if not domain_dir.exists():
                raise FileNotFoundError(f"Domain directory not found: {domain_dir}")

            service_name = f"{domain}-service-go"
            service_dir = services_dir / service_name

            if dry_run:
                print(f"[DRY RUN] Would generate service {service_name} in {service_dir}")
                print(f"[DRY RUN] Domain directory: {domain_dir}")
            else:
                self.logger.info(f"Generating service {service_name} in {service_dir}")

            if not dry_run:
                service_dir.mkdir(parents=True, exist_ok=True)
                self.logger.info(f"Created service directory: {service_dir}")
            elif dry_run:
                print(f"[DRY RUN] Would create service directory: {service_dir}")

            # PERFORMANCE: Bundle OpenAPI spec (delegated to specialized component)
            bundled_spec = None
            if not skip_bundle:
                bundled_spec = self.bundler.bundle(domain, service_dir, openapi_dir, dry_run)

            # PERFORMANCE: Generate Go code (delegated to specialized component)
            self.code_generator.generate(service_dir, bundled_spec, domain, dry_run)

            # PERFORMANCE: Create service structure (delegated to specialized component)
            self.structure_creator.create_structure(service_dir, domain, dry_run)

            # PERFORMANCE: Initialize Go module (delegated to specialized component)
            self.module_initializer.initialize(service_dir, service_name, dry_run)

            # PERFORMANCE: Test compilation (delegated to specialized component)
            if not skip_test and not dry_run:
                self.tester.test_compilation(service_dir, service_name)
            elif skip_test and dry_run:
                print(f"[DRY RUN] Would skip compilation test (--skip-test)")
            elif dry_run:
                print(f"[DRY RUN] Would test compilation of {service_name}")

            if dry_run:
                print(f"[DRY RUN] Successfully simulated generation of {service_name}")
            else:
                self.logger.info(f"Successfully generated {service_name}")

        except Exception as e:
            if dry_run:
                print(f"[DRY RUN ERROR] Failed to simulate generation: {e}")
            else:
                self.logger.error(f"Failed to generate service for domain {domain}: {e}")
            raise


def main():
    """Main entry point for the script"""
    import argparse

    parser = argparse.ArgumentParser(description="Generate Go service from OpenAPI spec")
    parser.add_argument("domain", help="Domain name (e.g., companion-domain)")
    parser.add_argument("--skip-bundle", action="store_true", help="Skip OpenAPI bundling")
    parser.add_argument("--skip-test", action="store_true", help="Skip compilation test")
    parser.add_argument("--dry-run", action="store_true", help="Dry run - show what would be done")

    args = parser.parse_args()

    if args.dry_run:
        print(f"[MAIN] Starting dry run simulation for domain: {args.domain}")
        print(f"[MAIN] Options: skip_bundle={args.skip_bundle}, skip_test={args.skip_test}")

    # Initialize components
    print("[MAIN] Initializing components...")
    config = ConfigManager()
    logger_manager = Logger(config)
    logger = logger_manager.get_logger("Main")
    file_manager = FileManager(logger_manager)
    command_runner = CommandRunner(logger_manager.get_logger("CommandRunner"))
    openapi_manager = OpenAPIManager(file_manager, command_runner, logger_manager)

    # Create generator
    generator = GoServiceGenerator(config, openapi_manager, file_manager, command_runner, logger_manager)

    try:
        print(f"[MAIN] Starting generation for domain: {args.domain}")
        generator.generate_domain_service(
            args.domain,
            skip_bundle=args.skip_bundle,
            skip_test=args.skip_test,
            dry_run=args.dry_run
        )
        if args.dry_run:
            print(f"[MAIN] Dry run completed successfully for domain: {args.domain}")
        else:
            logger.info(f"Successfully generated service for domain: {args.domain}")
    except Exception as e:
        print(f"[MAIN ERROR] Failed to generate service: {e}")
        import traceback
        traceback.print_exc()
        return 1

    return 0


if __name__ == '__main__':
    exit(main())
