#!/usr/bin/env python3
"""
NECPGAME Base Script
SOLID: Base class for all scripts with common functionality
"""

import sys
import argparse
from pathlib import Path
from typing import Optional
from scripts.core.config import ConfigManager
from scripts.core.file_manager import FileManager
from scripts.core.logger import Logger
from scripts.core.command_runner import CommandRunner


class BaseScript:
    """
    Base class for all NECPGAME scripts.
    Provides common functionality: config, logging, file operations, commands.
    """

    def __init__(self, name: str, description: str):
        self.name = name
        self.description = description

        # Core components
        self.config = ConfigManager()
        self.file_manager = FileManager(self.config)
        self.logger_manager = Logger(self.config)
        self.logger = self.logger_manager.create_script_logger(name)
        self.command_runner = CommandRunner(self.logger, self.config.get_project_root())

        # Argument parser
        self.parser = argparse.ArgumentParser(
            prog=name,
            description=description,
            formatter_class=argparse.RawDescriptionHelpFormatter
        )

        # Add common arguments
        self._add_common_args()

        # Add script-specific arguments
        self.add_script_args()

    def _add_common_args(self):
        """Add common arguments to all scripts"""
        self.parser.add_argument(
            '--verbose', '-v',
            action='store_true',
            help='Enable verbose logging'
        )
        self.parser.add_argument(
            '--dry-run',
            action='store_true',
            help='Show what would be done without making changes'
        )
        self.parser.add_argument(
            '--config',
            type=str,
            help='Path to config file (default: project-config.yaml)'
        )

    def add_script_args(self):
        """Override in subclasses to add script-specific arguments"""
        pass

    def parse_args(self):
        """Parse command line arguments"""
        return self.parser.parse_args()

    def validate_environment(self) -> bool:
        """Validate that script can run in current environment"""
        # Check if we're in project root
        project_root = self.config.get_project_root()
        if not (project_root / ".git").exists():
            self.logger.error(f"Not in project root: {project_root}")
            return False

        # Check Python version
        python_min = self.config.get('tools', 'python_version_min')
        if python_min and sys.version_info < tuple(map(int, python_min.split('.'))):
            self.logger.error(f"Python {python_min}+ required")
            return False

        return True

    def run(self):
        """Main script logic - override in subclasses"""
        raise NotImplementedError("Subclasses must implement run() method")

    def main(self):
        """Main entry point"""
        try:
            args = self.parse_args()

            # Override config path if specified
            if args.config:
                self.config = ConfigManager(args.config)
                # Reinitialize components with new config
                self.file_manager = FileManager(self.config)
                self.logger_manager = Logger(self.config)
                self.logger = self.logger_manager.create_script_logger(self.name)
                self.command_runner = CommandRunner(self.logger, self.config.get_project_root())

            if args.verbose:
                # Set logging to DEBUG level
                import logging
                logging.getLogger().setLevel(logging.DEBUG)

            if not self.validate_environment():
                sys.exit(1)

            self.logger.info(f"Starting {self.name}")

            if args.dry_run:
                self.logger.info("DRY RUN mode - no changes will be made")

            self.run()

            self.logger.info(f"Completed {self.name}")

        except KeyboardInterrupt:
            self.logger.info("Interrupted by user")
            sys.exit(1)
        except Exception as e:
            self.logger.error(f"Script failed: {e}", exc_info=True)
            sys.exit(1)
