#!/usr/bin/env python3
"""
Go Module Initializer Component
SOLID: Single Responsibility - initializes Go modules
"""

from pathlib import Path

import logging

from core.command_runner import CommandRunner


class GoModuleInitializer:
    """
    Initializes Go modules and dependencies.
    Single Responsibility: Initialize Go modules.
    """

    def __init__(self, command_runner: CommandRunner, logger: logging.Logger):
        self.command_runner = command_runner
        self.logger = logger

    def initialize(self, service_dir: Path, service_name: str, dry_run: bool) -> None:
        """Initialize Go module and dependencies"""
        print(f"[MODULES] Initializing Go modules for {service_name}")
        if dry_run:
            return

        try:
            # Initialize go mod
            old_cwd = self.command_runner.cwd
            self.command_runner.cwd = service_dir
            try:
                self.command_runner.run(['go', 'mod', 'init', service_name])
                self.command_runner.run(['go', 'mod', 'tidy'])
            finally:
                self.command_runner.cwd = old_cwd

        except Exception as e:
            raise RuntimeError(f"Go module initialization failed: {e}")
