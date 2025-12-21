#!/usr/bin/env python3
"""
NECPGAME Fix All OpenAPI Warnings
Исправляет ВСЕ OpenAPI warnings массово

SOLID Architecture:
- Single Responsibility: Only fixes OpenAPI warnings
- Open/Closed: Easy to add new warning fixes
- Dependency Injection: Uses shared OpenAPI manager
"""

from pathlib import Path
from scripts.core.base_script import BaseScript
from scripts.openapi.openapi_manager import OpenAPIManager


class FixAllOpenAPIWarnings(BaseScript):
    """
    Fixes all OpenAPI warnings across all specification files.
    Single Responsibility: Identify and fix common OpenAPI warnings.
    """

    def __init__(self):
        super().__init__(
            "fix-openapi-warnings",
            "Fix all OpenAPI warnings across all specification files"
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
        self.parser.add_argument(
            '--fix-responses',
            action='store_true',
            default=True,
            help='Add missing 4XX responses (default: True)'
        )

    def run(self):
        """Main fixing logic"""
        args = self.parse_args()

        # Find all main/service OpenAPI files
        openapi_dir = self.config.get_openapi_dir()
        yaml_files = []
        yaml_files.extend(openapi_dir.rglob("*main.yaml"))
        yaml_files.extend(openapi_dir.rglob("*service.yaml"))

        total_files = len(yaml_files)
        fixed_files = 0

        self.logger.info(f"Found {total_files} OpenAPI files to process")

        for i, file_path in enumerate(yaml_files, 1):
            self.logger.info(f"[{i}/{total_files}] Processing: {file_path.name}")

            try:
                spec = self.openapi_manager.load_spec(file_path)
                has_changes = False

                # Fix license
                if args.fix_license:
                    if self.openapi_manager.add_license(spec):
                        self.logger.info("  ✓ Added license")
                        has_changes = True

                # Fix common references
                if args.fix_refs:
                    if self.openapi_manager.fix_common_refs(spec):
                        self.logger.info("  ✓ Fixed common references")
                        has_changes = True

                # Add 4XX responses
                if args.fix_responses:
                    if self.openapi_manager.add_4xx_responses(spec):
                        self.logger.info("  ✓ Added 4XX responses")
                        has_changes = True

                # Save if changes were made
                if has_changes and not args.dry_run:
                    self.openapi_manager.save_spec(file_path, spec)
                    fixed_files += 1

            except Exception as e:
                self.logger.error(f"  ✗ Failed to process {file_path.name}: {e}")
                continue

        self._print_summary(total_files, fixed_files)

    def _print_summary(self, total_files: int, fixed_files: int):
        """Print fixing summary"""
        print("\n" + "=" * 60)
        print("FIXING SUMMARY")
        print("=" * 60)
        print(f"Total files processed: {total_files}")
        print(f"Files fixed: {fixed_files}")
        print("\nNotes:")
        print("- Some warnings (localhost servers) are left as-is for dev environments")
        print("- Manual review may be needed for complex cases")


def main():
    script = FixAllOpenAPIWarnings()
    script.main()


if __name__ == '__main__':
    main()
