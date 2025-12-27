#!/usr/bin/env python3
"""
Generate economy domain service
"""

import sys
from pathlib import Path

# Add scripts directory to Python path for imports
scripts_dir = Path(__file__).parent / 'scripts'
sys.path.insert(0, str(scripts_dir))

from core.command_runner import CommandRunner
from core.config import ConfigManager
from core.file_manager import FileManager
from core.logger import Logger
from generation.go_service_generator import GoServiceGenerator
from openapi.openapi_manager import OpenAPIManager

def main():
    print("Generating economy domain service...")

    # Initialize components
    config = ConfigManager()
    logger = Logger()
    command_runner = CommandRunner(logger.get_logger("CommandRunner"))
    file_manager = FileManager(logger.get_logger("FileManager"))

    # Initialize OpenAPI manager
    openapi_manager = OpenAPIManager(config, file_manager, logger.get_logger("OpenAPIManager"))

    # Initialize service generator
    generator = GoServiceGenerator(
        config=config,
        openapi_manager=openapi_manager,
        file_manager=file_manager,
        command_runner=command_runner,
        logger=logger
    )

    # Generate economy domain service
    try:
        generator.generate_domain_service("economy-domain", skip_bundle=False, skip_test=False, dry_run=False)
        print("Economy domain service generated successfully!")
    except Exception as e:
        print(f"Error generating service: {e}")
        import traceback
        traceback.print_exc()

if __name__ == '__main__':
    main()
