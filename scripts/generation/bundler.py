#!/usr/bin/env python3
"""
OpenAPI Bundler Component
SOLID: Single Responsibility - bundles OpenAPI specifications
"""

from pathlib import Path
from typing import Optional

import logging

from core.command_runner import CommandRunner


class OpenAPIBundler:
    """
    Bundles OpenAPI specifications using redocly.
    Single Responsibility: Bundle OpenAPI specs.
    """

    def __init__(self, command_runner: CommandRunner, logger: logging.Logger):
        self.command_runner = command_runner
        self.logger = logger

    def bundle(self, domain: str, service_dir: Path, openapi_dir: Path, dry_run: bool) -> Optional[Path]:
        """Bundle OpenAPI spec using redocly in service directory"""
        print(f"[BUNDLER] Bundling OpenAPI spec for {domain}")
        main_yaml = openapi_dir / domain / "main.yaml"
        print(f"[BUNDLER] Looking for main.yaml at: {main_yaml}")

        if not main_yaml.exists():
            raise FileNotFoundError(f"Main YAML not found: {main_yaml}")

        # PERFORMANCE: Create bundled file in service directory, not project root
        bundled_file = service_dir / "openapi-bundled.yaml"
        print(f"[BUNDLER] Will create bundled file at: {bundled_file}")

        if not dry_run:
            # Use redocly to bundle the spec
            try:
                self.command_runner.run([
                    'npx', '--yes', '@redocly/cli', 'bundle',
                    str(main_yaml), '-o', str(bundled_file)
                ])
                self.logger.info(f"Bundled OpenAPI spec: {bundled_file}")
            except Exception as e:
                self.logger.error(f"Failed to bundle OpenAPI spec: {e}")
                raise

        return bundled_file
