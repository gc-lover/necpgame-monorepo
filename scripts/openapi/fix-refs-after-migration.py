#!/usr/bin/env python3
"""
Скрипт для исправления $ref ссылок после миграции структуры домена.

Использование:
    python scripts/openapi/fix-refs-after-migration.py proto/openapi/misc-domain/
"""

import re
from pathlib import Path
from typing import Dict

class RefFixer:
    """Исправитель $ref ссылок после миграции."""

    def __init__(self, domain_path: str):
        self.domain_path = Path(domain_path)
        self.fixed_count = 0

        # Карта перемещений для misc-domain
        self.move_map = {
            'common-schemas.yaml': '../schemas/common/common-schemas.yaml',
            'common-base.yaml': '../services/common-base/main.yaml',
            'common-parameters.yaml': '../services/common-parameters/main.yaml',
            'common-responses.yaml': '../services/common-responses/main.yaml',
            'common.yaml': '../services/common/main.yaml',
            'abilities.yaml': '../services/utilities-abilities/main.yaml',
            'combos.yaml': '../services/utilities-combos/main.yaml',
            'shooting.yaml': '../services/utilities-shooting/main.yaml',
        }

    def fix_all_refs(self) -> None:
        """Исправить все ссылки в домене."""
        print(f"[START] Fixing references in {self.domain_path.name}")

        for yaml_file in self.domain_path.rglob('*.yaml'):
            self.fix_file_refs(yaml_file)

        print(f"[DONE] Fixed {self.fixed_count} references")

    def fix_file_refs(self, file_path: Path) -> None:
        """Исправить ссылки в одном файле."""
        try:
            with open(file_path, 'r', encoding='utf-8') as f:
                content = f.read()

            original_content = content

            # Исправляем ссылки на перемещенные файлы
            for old_file, new_path in self.move_map.items():
                # Паттерны для разных форматов кавычек и относительных путей
                patterns = [
                    (rf'\$ref:\s*\'\./{re.escape(old_file)}\'', f"$ref: '{new_path}'"),
                    (rf'\$ref:\s*\'{re.escape(old_file)}\'', f"$ref: '{new_path}'"),
                    (rf'\$ref:\s*"./{re.escape(old_file)}"', f'$ref: "{new_path}"'),
                    (rf'\$ref:\s*"{re.escape(old_file)}"', f'$ref: "{new_path}"'),
                    # Также обрабатываем без ./ в начале
                    (rf'\$ref:\s*\'{re.escape(old_file)}\'', f"$ref: '{new_path}'"),
                    (rf'\$ref:\s*"{re.escape(old_file)}"', f'$ref: "{new_path}"'),
                ]

                for pattern, replacement in patterns:
                    content = re.sub(pattern, replacement, content)

            # Специальная обработка для common-schemas.yaml с фрагментами
            content = re.sub(
                r'\$ref:\s*\'(common-schemas\.yaml)(#/.*)?\'',
                r"$ref: '../schemas/common/\1\2'",
                content
            )

            content = re.sub(
                r'\$ref:\s*"(common-schemas\.yaml)(#/.*)?"',
                r'$ref: "../schemas/common/\1\2"',
                content
            )

            if content != original_content:
                with open(file_path, 'w', encoding='utf-8') as f:
                    f.write(content)
                self.fixed_count += 1
                print(f"[FIXED] {file_path}")

        except Exception as e:
            print(f"[ERROR] Failed to fix {file_path}: {e}")


def main():
    import sys
    if len(sys.argv) != 2:
        print("Usage: python fix-refs-after-migration.py <domain-path>")
        sys.exit(1)

    domain_path = sys.argv[1]
    fixer = RefFixer(domain_path)
    fixer.fix_all_refs()


if __name__ == '__main__':
    main()
