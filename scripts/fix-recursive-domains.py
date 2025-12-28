#!/usr/bin/env python3
"""
OpenAPI Recursive Domains Fixer

Исправляет проблему рекурсивных доменов в system-domain.
Извлекает сервисы из неправильных локаций в правильные домены.

Использование:
    python scripts/fix-recursive-domains.py [--dry-run] [--domain-mapping FILE]

Аргументы:
    --dry-run         : Только показать план исправления
    --domain-mapping  : JSON файл с маппингом сервисов по доменам

Примеры:
    python scripts/fix-recursive-domains.py --dry-run
    python scripts/fix-recursive-domains.py
"""

import os
import sys
import json
import shutil
import argparse
from pathlib import Path
from typing import Dict, List, Any, Set
from dataclasses import dataclass


@dataclass
class DomainFixPlan:
    """План исправления рекурсивных доменов"""
    actions: List[Dict[str, Any]]
    warnings: List[str]
    errors: List[str]
    domain_mapping: Dict[str, List[str]]


class RecursiveDomainsFixer:
    """Исправляет рекурсивные домены"""

    def __init__(self, openapi_root: str = "proto/openapi"):
        self.openapi_root = Path(openapi_root)

    def fix_recursive_domains(self, dry_run: bool = True, domain_mapping_file: str = None) -> DomainFixPlan:
        """Исправляет рекурсивные домены"""

        plan = DomainFixPlan(
            actions=[],
            warnings=[],
            errors=[],
            domain_mapping=self._load_domain_mapping(domain_mapping_file)
        )

        # Находим все рекурсивные домены
        recursive_domains = self._find_recursive_domains()

        for recursive_domain in recursive_domains:
            self._fix_single_recursive_domain(recursive_domain, plan)

        # Выполняем план
        if not dry_run:
            self._execute_fix_plan(plan)

        return plan

    def _load_domain_mapping(self, mapping_file: str = None) -> Dict[str, List[str]]:
        """Загружает маппинг доменов"""
        if mapping_file and Path(mapping_file).exists():
            with open(mapping_file, 'r', encoding='utf-8') as f:
                return json.load(f)

        # Дефолтный маппинг на основе анализа
        return {
            "auth-domain": ["accounts", "security", "core"],
            "combat-domain": ["abilities", "acrobatics", "analytics", "implants", "sessions"],
            "communication-domain": ["realtime", "session", "voice"],
            "content-domain": ["achievements", "announcements", "import", "narrative", "quests"],
            "economy-domain": ["analytics", "investments", "market", "production"],
            "gameplay-domain": ["achievements", "combat", "freerun", "progression", "quests"],
            "inventory-domain": ["companion", "crafting"],
            "security-domain": ["anti-cheat", "character", "kafka"],
            "social-domain": ["chat", "guilds", "mail", "relationships", "romance"],
            "system-domain": ["admin", "messaging", "support", "sync"],
            "tournament-domain": ["clan", "leaderboard", "league"],
            "world-domain": ["housing", "interactions", "transport"]
        }

    def _find_recursive_domains(self) -> List[Path]:
        """Находит рекурсивные домены"""
        recursive_domains = []

        # Проверяем system-domain/ai/
        ai_domain_path = self.openapi_root / "system-domain" / "ai"
        if ai_domain_path.exists():
            for item in ai_domain_path.iterdir():
                if item.is_dir() and item.name.endswith("-domain"):
                    recursive_domains.append(item)

        return recursive_domains

    def _fix_single_recursive_domain(self, recursive_domain_path: Path, plan: DomainFixPlan) -> None:
        """Исправляет один рекурсивный домен"""

        domain_name = recursive_domain_path.name  # например "auth-domain"
        target_domain_name = domain_name  # куда перемещать
        target_domain_path = self.openapi_root / target_domain_name

        # Создаем целевой домен если не существует
        if not target_domain_path.exists():
            plan.actions.append({
                "type": "create_domain",
                "domain": target_domain_name
            })

        # Перемещаем сервисы
        for service_dir in recursive_domain_path.iterdir():
            if service_dir.is_dir():
                service_name = service_dir.name

                # Определяем правильное место для сервиса
                target_service_path = self._determine_service_target_location(
                    service_name, target_domain_name, plan.domain_mapping
                )

                if target_service_path:
                    plan.actions.append({
                        "type": "move_service",
                        "from": str(service_dir.relative_to(self.openapi_root)),
                        "to": str(target_service_path.relative_to(self.openapi_root))
                    })

        # Удаляем пустой рекурсивный домен
        plan.actions.append({
            "type": "remove_empty_domain",
            "path": str(recursive_domain_path.relative_to(self.openapi_root))
        })

    def _determine_service_target_location(self, service_name: str, target_domain: str, domain_mapping: Dict[str, List[str]]) -> Path:
        """Определяет правильное место для сервиса"""

        # Используем маппинг для определения правильного домена
        for domain, services in domain_mapping.items():
            if service_name in services or any(s in service_name for s in services):
                if domain == target_domain:
                    # Сервис уже в правильном домене
                    return self.openapi_root / domain / "services" / service_name
                else:
                    # Сервис нужно переместить в другой домен
                    return self.openapi_root / domain / "services" / service_name

        # Если не нашли в маппинге, оставляем в текущем домене
        return self.openapi_root / target_domain / "services" / service_name

    def _execute_fix_plan(self, plan: DomainFixPlan) -> None:
        """Выполняет план исправления"""

        for action in plan.actions:
            try:
                if action["type"] == "create_domain":
                    domain_path = self.openapi_root / action["domain"]
                    domain_path.mkdir(exist_ok=True)

                    # Создаем стандартную структуру
                    (domain_path / "services").mkdir(exist_ok=True)
                    (domain_path / "schemas").mkdir(exist_ok=True)
                    (domain_path / "schemas" / "entities").mkdir(exist_ok=True)
                    (domain_path / "schemas" / "common").mkdir(exist_ok=True)
                    (domain_path / "schemas" / "enums").mkdir(exist_ok=True)

                elif action["type"] == "move_service":
                    from_path = self.openapi_root / action["from"]
                    to_path = self.openapi_root / action["to"]

                    if from_path.exists():
                        to_path.parent.mkdir(parents=True, exist_ok=True)
                        if to_path.exists():
                            # Объединяем директории если целевая существует
                            self._merge_directories(from_path, to_path)
                            shutil.rmtree(from_path)
                        else:
                            shutil.move(str(from_path), str(to_path))

                elif action["type"] == "remove_empty_domain":
                    domain_path = self.openapi_root / action["path"]
                    if domain_path.exists() and not any(domain_path.iterdir()):
                        domain_path.rmdir()

            except Exception as e:
                plan.errors.append(f"Failed to execute action {action}: {e}")

    def _merge_directories(self, source: Path, target: Path) -> None:
        """Объединяет две директории"""
        for item in source.rglob("*"):
            if item.is_file():
                rel_path = item.relative_to(source)
                target_file = target / rel_path
                target_file.parent.mkdir(parents=True, exist_ok=True)
                shutil.copy2(item, target_file)


def print_fix_plan(plan: DomainFixPlan) -> None:
    """Печатает план исправления"""
    print(f"\n[RECURSIVE DOMAINS FIX PLAN]")
    print(f"Actions: {len(plan.actions)}")

    if plan.actions:
        print(f"\nActions:")
        for i, action in enumerate(plan.actions, 1):
            action_type = action['type']
            if action_type == "create_domain":
                print(f"  {i}. CREATE DOMAIN: {action['domain']}")
            elif action_type == "move_service":
                print(f"  {i}. MOVE SERVICE: {action['from']} -> {action['to']}")
            elif action_type == "remove_empty_domain":
                print(f"  {i}. REMOVE EMPTY: {action['path']}")

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
        description="OpenAPI Recursive Domains Fixer",
        formatter_class=argparse.RawDescriptionHelpFormatter,
        epilog="""
Examples:
  python scripts/fix-recursive-domains.py --dry-run
  python scripts/fix-recursive-domains.py --domain-mapping custom-mapping.json
        """
    )

    parser.add_argument(
        "--dry-run",
        action="store_true",
        help="Show fix plan without executing"
    )

    parser.add_argument(
        "--domain-mapping",
        type=str,
        help="JSON file with domain-to-service mapping"
    )

    args = parser.parse_args()

    fixer = RecursiveDomainsFixer()

    try:
        plan = fixer.fix_recursive_domains(dry_run=args.dry_run, domain_mapping_file=args.domain_mapping)
        print_fix_plan(plan)

        if args.dry_run:
            print(f"\n[INFO] This was a dry run. Use without --dry-run to execute fixes.")
        else:
            if plan.errors:
                print(f"\n[ERROR] Fix completed with {len(plan.errors)} errors.")
                sys.exit(1)
            else:
                print(f"\n[SUCCESS] Recursive domains fixed successfully!")

    except Exception as e:
        print(f"[ERROR] Fix failed: {e}")
        sys.exit(1)


if __name__ == "__main__":
    main()
