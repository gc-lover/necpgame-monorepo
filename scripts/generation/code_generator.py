#!/usr/bin/env python3
"""
Go Code Generator Component
SOLID: Single Responsibility - generates Go code from OpenAPI specs
"""

from pathlib import Path
from typing import Optional

import logging

from core.command_runner import CommandRunner


class GoCodeGenerator:
    """
    Generates Go code using ogen from bundled OpenAPI specifications.
    Single Responsibility: Generate Go API code.
    """

    def __init__(self, command_runner: CommandRunner, logger: logging.Logger):
        self.command_runner = command_runner
        self.logger = logger

    def generate(self, service_dir: Path, bundled_spec: Optional[Path],
                 domain: str, dry_run: bool) -> None:
        """Generate Go code using ogen"""
        print(f"[CODEGEN] Generating Go code for {domain}")

        if not bundled_spec or not bundled_spec.exists():
            print(f"[CODEGEN] Bundled spec not found, skipping Go code generation")
            return

        pkg_dir = service_dir / "pkg" / "api"
        print(f"[CODEGEN] Target directory: {pkg_dir}")

        if not dry_run:
            pkg_dir.mkdir(parents=True, exist_ok=True)

        if not dry_run:
            try:
                # Try to use ogen from PATH first
                self.command_runner.run([
                    'ogen', '--target', str(pkg_dir),
                    '--package', 'api', '--clean', str(bundled_spec)
                ])
                self.logger.info(f"Generated Go API code in: {pkg_dir}")
            except Exception as e:
                # Try to install ogen if not found
                self.logger.warning(f"ogen not found, trying to install: {e}")
                try:
                    self.command_runner.run(['go', 'install', 'github.com/ogen-go/ogen/cmd/ogen@latest'])
                    self.command_runner.run([
                        'ogen', '--target', str(pkg_dir),
                        '--package', 'api', '--clean', str(bundled_spec)
                    ])
                    self.logger.info(f"Generated Go API code after installing ogen: {pkg_dir}")
                except Exception as e2:
                    self.logger.error(f"Failed to generate Go code even after installing ogen: {e2}")
                    raise
