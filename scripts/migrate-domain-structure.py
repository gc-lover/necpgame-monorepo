#!/usr/bin/env python3
"""
OpenAPI Domain Structure Migrator

Автоматически мигрирует домен к стандартной структуре.

Использование:
    python scripts/migrate-domain-structure.py <domain-name> [--dry-run] [--backup]

Аргументы:
    domain-name    : Имя домена для миграции
    --dry-run     : Только показать план миграции
    --backup      : Создать backup перед миграцией

Примеры:
    python scripts/migrate-domain-structure.py social-domain --dry-run
    python scripts/migrate-domain-structure.py social-domain --backup
"""

import os
import sys
import shutil
import argparse
from pathlib import Path
from typing import Dict, List, Any, Tuple
from dataclasses import dataclass


@dataclass
class MigrationPlan:
    """План миграции домена"""
    domain_name: str
    actions: List[Dict[str, Any]]
    warnings: List[str]
    errors: List[str]


class DomainMigrator:
    """Мигрирует домен к стандартной структуре"""

    def __init__(self, openapi_root: str = "proto/openapi"):
        self.openapi_root = Path(openapi_root)

    def migrate_domain(self, domain_name: str, dry_run: bool = True, create_backup: bool = True) -> MigrationPlan:
        """Мигрирует домен к стандартной структуре"""
        domain_path = self.openapi_root / domain_name

        if not domain_path.exists():
            raise ValueError(f"Domain {domain_name} does not exist")

        plan = MigrationPlan(
            domain_name=domain_name,
            actions=[],
            warnings=[],
            errors=[]
        )

        # Создаем backup если требуется
        if create_backup and not dry_run:
            self._create_backup(domain_path, plan)

        # Анализируем текущую структуру
        current_structure = self._analyze_current_structure(domain_path)

        # Создаем план миграции
        self._create_migration_plan(domain_path, current_structure, plan)

        # Выполняем миграцию
        if not dry_run:
            self._execute_migration_plan(plan)

        return plan

    def _create_backup(self, domain_path: Path, plan: MigrationPlan) -> None:
        """Создает backup домена"""
        backup_path = domain_path.parent / f"{domain_path.name}_backup"

        if backup_path.exists():
            shutil.rmtree(backup_path)

        shutil.copytree(domain_path, backup_path)
        plan.actions.append({
            "type": "backup_created",
            "source": str(domain_path),
            "destination": str(backup_path)
        })

    def _analyze_current_structure(self, domain_path: Path) -> Dict[str, Any]:
        """Анализирует текущую структуру домена"""
        structure = {
            "files": {},
            "dirs": {},
            "yaml_files": [],
            "other_files": []
        }

        for root, dirs, files in os.walk(domain_path):
            root_path = Path(root)
            rel_path = root_path.relative_to(domain_path)

            for file in files:
                file_path = root_path / file
                rel_file_path = file_path.relative_to(domain_path)

                if file.endswith(('.yaml', '.yml')):
                    structure["yaml_files"].append(str(rel_file_path))
                else:
                    structure["other_files"].append(str(rel_file_path))

                structure["files"][str(rel_file_path)] = {
                    "path": str(rel_file_path),
                    "is_yaml": file.endswith(('.yaml', '.yml'))
                }

            for dir_name in dirs:
                dir_path = root_path / dir_name
                rel_dir_path = dir_path.relative_to(domain_path)
                structure["dirs"][str(rel_dir_path)] = {
                    "path": str(rel_dir_path),
                    "level": len(rel_dir_path.parts)
                }

        return structure

    def _create_migration_plan(self, domain_path: Path, current_structure: Dict[str, Any], plan: MigrationPlan) -> None:
        """Создает план миграции"""

        # Проверяем наличие обязательных файлов
        self._plan_required_files(domain_path, current_structure, plan)

        # Планируем структуру директорий
        self._plan_directory_structure(domain_path, current_structure, plan)

        # Планируем перемещение файлов
        self._plan_file_moves(domain_path, current_structure, plan)

    def _plan_required_files(self, domain_path: Path, current_structure: Dict[str, Any], plan: MigrationPlan) -> None:
        """Планирует создание обязательных файлов"""
        root_yaml_files = [f for f in current_structure["yaml_files"] if not "/" in f]

        # main.yaml
        if "main.yaml" not in root_yaml_files:
            plan.actions.append({
                "type": "create_file",
                "path": "main.yaml",
                "template": "domain_main_template"
            })

        # README.md
        if not (domain_path / "README.md").exists():
            plan.actions.append({
                "type": "create_file",
                "path": "README.md",
                "template": "domain_readme_template"
            })

        # domain-config.yaml
        if "domain-config.yaml" not in root_yaml_files:
            plan.actions.append({
                "type": "create_file",
                "path": "domain-config.yaml",
                "template": "domain_config_template"
            })

    def _plan_directory_structure(self, domain_path: Path, current_structure: Dict[str, Any], plan: MigrationPlan) -> None:
        """Планирует создание стандартной структуры директорий"""

        required_dirs = [
            "services",
            "schemas/entities",
            "schemas/common",
            "schemas/enums"
        ]

        for dir_path in required_dirs:
            full_path = domain_path / dir_path
            if not full_path.exists():
                plan.actions.append({
                    "type": "create_directory",
                    "path": dir_path
                })

    def _plan_file_moves(self, domain_path: Path, current_structure: Dict[str, Any], plan: MigrationPlan) -> None:
        """Планирует перемещение файлов в новую структуру"""

        for yaml_file in current_structure["yaml_files"]:
            file_path = Path(yaml_file)

            # Определяем новую локацию файла
            new_path = self._determine_new_file_location(file_path, current_structure)

            if new_path and str(new_path) != yaml_file:
                plan.actions.append({
                    "type": "move_file",
                    "from": yaml_file,
                    "to": str(new_path)
                })

    def _determine_new_file_location(self, file_path: Path, current_structure: Dict[str, Any]) -> Path:
        """Определяет новую локацию для файла"""

        # Логика определения новой локации основана на содержимом файла
        # и текущей структуре домена

        # Примеры правил:
        if "guild" in file_path.name.lower():
            if "schema" in file_path.name.lower():
                return Path("schemas/entities") / file_path.name
            else:
                return Path("services/guilds") / file_path.name

        # Другие правила для разных типов файлов...

        return None  # Оставляем на месте если не знаем куда переместить

    def _execute_migration_plan(self, plan: MigrationPlan) -> None:
        """Выполняет план миграции"""
        domain_path = self.openapi_root / plan.domain_name

        for action in plan.actions:
            try:
                if action["type"] == "create_directory":
                    dir_path = domain_path / action["path"]
                    dir_path.mkdir(parents=True, exist_ok=True)

                elif action["type"] == "move_file":
                    from_path = domain_path / action["from"]
                    to_path = domain_path / action["to"]

                    if from_path.exists():
                        to_path.parent.mkdir(parents=True, exist_ok=True)
                        shutil.move(str(from_path), str(to_path))

                elif action["type"] == "create_file":
                    file_path = domain_path / action["path"]

                    if action["template"] == "domain_main_template":
                        self._create_domain_main_file(file_path, plan.domain_name)
                    elif action["template"] == "domain_readme_template":
                        self._create_domain_readme_file(file_path, plan.domain_name)
                    elif action["template"] == "domain_config_template":
                        self._create_domain_config_file(file_path, plan.domain_name)

            except Exception as e:
                plan.errors.append(f"Failed to execute action {action}: {e}")

    def _create_domain_main_file(self, file_path: Path, domain_name: str) -> None:
        """Создает main.yaml файл домена"""
        content = f"""openapi: 3.0.3
info:
  title: "{domain_name.replace('-', ' ').title()} API"
  description: "Enterprise-grade {domain_name.replace('-', ' ')} API for NECPGAME"
  version: "1.0.0"
servers:
  - url: https://api.necpgame.com/v1/{domain_name}
    description: Production server
paths: {{}}  # Paths are defined in service files
components:
  schemas: {{}}  # Schemas are defined in service files
"""
        file_path.write_text(content)

    def _create_domain_readme_file(self, file_path: Path, domain_name: str) -> None:
        """Создает README.md файл домена"""
        content = f"""# {domain_name.replace('-', ' ').title()} Domain

## Overview

Enterprise-grade {domain_name.replace('-', ' ')} API for NECPGAME project.

## Services

- TODO: Add service descriptions

## Schemas

- TODO: Add schema descriptions

## Development

Follow the [Domain Structure Standard](../../DOMAIN_STRUCTURE_STANDARD.md) for consistency.
"""
        file_path.write_text(content)

    def _create_domain_config_file(self, file_path: Path, domain_name: str) -> None:
        """Создает domain-config.yaml файл"""
        content = f"""domain:
  name: {domain_name}
  version: 1.0.0
  services: []
  dependencies:
    - common-schemas
  tags:
    - {domain_name.replace('-domain', '').replace('-', '_')}
"""
        file_path.write_text(content)


def print_migration_plan(plan: MigrationPlan) -> None:
    """Печатает план миграции"""
    print(f"\n[MIGRATION PLAN] {plan.domain_name}")
    print(f"Actions: {len(plan.actions)}")

    if plan.actions:
        print(f"\nActions:")
        for i, action in enumerate(plan.actions, 1):
            print(f"  {i}. {action['type']}: {action.get('path', action.get('from', 'unknown'))}")
            if 'to' in action:
                print(f"     -> {action['to']}")

    if plan.warnings:
        print(f"\n[WARNINGS] ({len(plan.warnings)})")
        for warning in plan.warnings:
            print(f"  - {warning}")

    if plan.errors:
        print(f"\n[ERRORS] ({len(plan.errors)})")
        for error in plan.errors:
            print(f"  - {error}")


def main():
    parser = argparse.ArgumentParser(
        description="OpenAPI Domain Structure Migrator",
        formatter_class=argparse.RawDescriptionHelpFormatter,
        epilog="""
Examples:
  python scripts/migrate-domain-structure.py social-domain --dry-run
  python scripts/migrate-domain-structure.py social-domain --backup
        """
    )

    parser.add_argument(
        "domain",
        help="Domain name to migrate"
    )

    parser.add_argument(
        "--dry-run",
        action="store_true",
        help="Show migration plan without executing"
    )

    parser.add_argument(
        "--backup",
        action="store_true",
        default=True,
        help="Create backup before migration (default: True)"
    )

    args = parser.parse_args()

    migrator = DomainMigrator()

    try:
        plan = migrator.migrate_domain(args.domain, dry_run=args.dry_run, create_backup=args.backup)
        print_migration_plan(plan)

        if args.dry_run:
            print(f"\n[INFO] This was a dry run. Use without --dry-run to execute migration.")
        else:
            if plan.errors:
                print(f"\n[ERROR] Migration completed with {len(plan.errors)} errors.")
                sys.exit(1)
            else:
                print(f"\n[SUCCESS] Migration completed successfully!")

    except Exception as e:
        print(f"[ERROR] Migration failed: {e}")
        sys.exit(1)


if __name__ == "__main__":
    main()
