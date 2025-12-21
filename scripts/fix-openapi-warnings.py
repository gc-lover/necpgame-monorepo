#!/usr/bin/env python3
"""
NECPGAME Fix OpenAPI Warnings
Исправляет основные OpenAPI warnings массово

SOLID Architecture:
- Single Responsibility: Fix basic OpenAPI warnings
- Open/Closed: Easy to add new warning types
- Dependency Injection: Uses shared components
"""

from pathlib import Path
from scripts.core.base_script import BaseScript
from scripts.openapi.openapi_manager import OpenAPIManager


class FixOpenAPIWarnings(BaseScript):
    """
    Fixes basic OpenAPI warnings across all specification files.
    Single Responsibility: Identify and fix common warning types.
    """

    def __init__(self):
        super().__init__(
            "fix-openapi-warnings",
            "Fix basic OpenAPI warnings across all specification files"
        )
        self.openapi_manager = OpenAPIManager(
            self.file_manager,
            self.command_runner,
            self.logger
        )

    def add_script_args(self):
        """Add script-specific arguments"""
        self.parser.add_argument(
            '--fix-license',
            action='store_true',
            default=True,
            help='Add missing license fields (default: True)'
        )
        self.parser.add_argument(
            '--fix-refs',
            action='store_true',
            default=True,
            help='Fix common schema references (default: True)'
        )

    def run(self):
        """Main fixing logic"""
        args = self.parse_args()

        openapi_dir = self.config.get_openapi_dir()
        yaml_files = list(openapi_dir.rglob("*.yaml"))

        self.logger.info(f"Found {len(yaml_files)} YAML files to check")

        license_count = 0
        path_count = 0

        for file_path in yaml_files:
            try:
                spec = self.openapi_manager.load_spec(file_path)
                has_changes = False

                # Add license
                if args.fix_license:
                    if self.openapi_manager.add_license(spec):
                        self.logger.info(f"✓ Added license: {file_path.name}")
                        license_count += 1
                        has_changes = True

                # Fix common paths
                if args.fix_refs:
                    if self.openapi_manager.fix_common_refs(spec):
                        self.logger.info(f"✓ Fixed refs: {file_path.name}")
                        path_count += 1
                        has_changes = True

                # Save if changes were made
                if has_changes and not args.dry_run:
                    self.openapi_manager.save_spec(file_path, spec)

            except Exception as e:
                self.logger.error(f"✗ Failed to process {file_path.name}: {e}")
                continue

        self._print_summary(license_count, path_count)

    def _print_summary(self, license_count: int, path_count: int):
        """Print fixing summary"""
        print("\n" + "=" * 60)
        print("WARNINGS FIXING SUMMARY")
        print("=" * 60)
        print(f"License fields added: {license_count}")
        print(f"Reference paths fixed: {path_count}")
        print("\nNote: Localhost server warnings are left as-is (normal for dev)")


def main():
    script = FixOpenAPIWarnings()
    script.main()


if __name__ == '__main__':
    main()
