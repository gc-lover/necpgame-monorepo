#!/usr/bin/env python3
"""
NECPGAME Fix Common References
Исправляет ссылки на общие схемы в OpenAPI спецификациях

SOLID Architecture:
- Single Responsibility: Only fixes common schema references
- Open/Closed: Easy to add new reference patterns
- Dependency Injection: Uses shared file manager
"""

from pathlib import Path
from scripts.core.base_script import BaseScript
from scripts.openapi.openapi_manager import OpenAPIManager


class FixCommonRefs(BaseScript):
    """
    Fixes common schema references in OpenAPI specifications.
    Single Responsibility: Update $ref paths to point to centralized common schemas.
    """

    def __init__(self):
        super().__init__(
            "fix-common-refs",
            "Fix common schema references in OpenAPI specifications"
        )
        self.openapi_manager = OpenAPIManager(
            self.file_manager,
            self.command_runner,
            self.logger
        )

    def add_script_args(self):
        """Add script-specific arguments"""
        self.parser.add_argument(
            '--old-pattern',
            default='../../misc-domain/common/common.yaml',
            help='Old reference pattern to replace'
        )
        self.parser.add_argument(
            '--new-pattern',
            default='../../common-schemas.yaml',
            help='New reference pattern'
        )

    def run(self):
        """Main fixing logic"""
        args = self.parse_args()

        openapi_dir = self.config.get_openapi_dir()
        yaml_files = list(openapi_dir.rglob("*.yaml"))

        self.logger.info(f"Found {len(yaml_files)} YAML files to check")

        fixed_files = 0

        for file_path in yaml_files:
            try:
                spec = self.openapi_manager.load_spec(file_path)
                changed = self.openapi_manager.fix_common_refs(spec)

                if changed and not args.dry_run:
                    self.openapi_manager.save_spec(file_path, spec)
                    self.logger.info(f"✓ Fixed: {file_path.relative_to(self.config.get_project_root())}")
                    fixed_files += 1
                elif changed and args.dry_run:
                    self.logger.info(f"Would fix: {file_path.relative_to(self.config.get_project_root())}")

            except Exception as e:
                self.logger.error(f"✗ Failed to process {file_path.name}: {e}")
                continue

        self._print_summary(len(yaml_files), fixed_files)

    def _print_summary(self, total_files: int, fixed_files: int):
        """Print fixing summary"""
        print("\n" + "=" * 60)
        print("REFERENCE FIXING SUMMARY")
        print("=" * 60)
        print(f"Total files checked: {total_files}")
        print(f"Files fixed: {fixed_files}")


def main():
    script = FixCommonRefs()
    script.main()


if __name__ == '__main__':
    main()
