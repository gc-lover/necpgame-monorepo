#!/usr/bin/env python3
"""
NECPGAME Logger
SOLID: Single Responsibility - manages logging
"""

import logging
import sys
from typing import Optional
from scripts.core.config import ConfigManager


class Logger:
    """
    Manages logging for all scripts.
    Single Responsibility: Configure and provide loggers.
    """

    def __init__(self, config_manager: ConfigManager):
        self.config = config_manager
        self._configured = False

    def configure(self) -> None:
        """Configure logging based on project config"""
        if self._configured:
            return

        log_config = self.config.get('logging') or {}

        level_name = log_config.get('level', 'INFO').upper()
        level = getattr(logging, level_name, logging.INFO)

        format_str = log_config.get('format', '%(asctime)s [%(levelname)s] %(name)s: %(message)s')
        date_format = log_config.get('date_format', '%Y-%m-%d %H:%M:%S')

        logging.basicConfig(
            level=level,
            format=format_str,
            datefmt=date_format,
            stream=sys.stdout
        )

        self._configured = True

    def get_logger(self, name: str) -> logging.Logger:
        """Get configured logger"""
        self.configure()
        return logging.getLogger(name)

    def create_script_logger(self, script_name: str) -> logging.Logger:
        """Create logger for script with proper name"""
        return self.get_logger(f"necpgame.{script_name}")
