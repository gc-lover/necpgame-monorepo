#!/usr/bin/env python3
"""
Generate specialized domain service using basic generator
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
from openapi.openapi_manager import OpenAPIManager
from generation.go_service_generator import GoServiceGenerator

def main():
    print("Generating specialized domain service...")

    try:
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

        print("Starting generation...")
        # Generate specialized domain service
        generator.generate_domain_service("specialized-domain", skip_bundle=False, skip_test=True, dry_run=False)
        print("Specialized domain service generated successfully!")

    except Exception as e:
        print(f"Error generating service: {e}")
        import traceback
        traceback.print_exc()

if __name__ == '__main__':
    main()
