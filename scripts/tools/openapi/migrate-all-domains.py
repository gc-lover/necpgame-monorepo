#!/usr/bin/env python3
"""
Массовый скрипт миграции всех доменов на стандартизированную архитектуру.

Автоматически находит все домены и применяет миграцию к каждому.
"""

import os
import sys
from pathlib import Path

# Добавляем текущую директорию в путь для импорта
sys.path.insert(0, os.path.dirname(__file__))

from migrate_domain_structure import DomainStructureMigrator

def get_all_domains():
    """Получить список всех доменов."""
    project_root = Path(__file__).parent.parent.parent
    openapi_dir = project_root / "proto" / "openapi"

    domains = []
    for item in openapi_dir.iterdir():
        if item.is_dir() and not item.name.startswith('.') and item.name != 'test-gen':
            # Проверяем, что это домен (содержит main.yaml)
            main_yaml = item / 'main.yaml'
            if main_yaml.exists():
                domains.append(item.name)

    # Исключаем уже мигрированные домены
    migrated = ['social-domain', 'analysis-domain', 'arena-domain', 'auth-expansion-domain']
    domains = [d for d in domains if d not in migrated]

    return sorted(domains)

def main():
    print("[BULK] Starting bulk domain migration...")

    domains = get_all_domains()
    print(f"[BULK] Found {len(domains)} domains to migrate:")
    for domain in domains:
        print(f"  - {domain}")

    if not domains:
        print("[BULK] No domains to migrate")
        return 0

    # Запрашиваем подтверждение
    response = input(f"\n[BULK] Migrate {len(domains)} domains? (y/N): ").strip().lower()
    if response != 'y':
        print("[BULK] Migration cancelled")
        return 0

    success_count = 0
    failed_domains = []

    for i, domain in enumerate(domains, 1):
        print(f"\n[BULK] [{i}/{len(domains)}] Migrating {domain}...")

        try:
            migrator = DomainStructureMigrator(domain, dry_run=False)
            success = migrator.execute_migration()

            if success:
                success_count += 1
                print(f"[BULK] ✅ {domain} migrated successfully")
            else:
                failed_domains.append(domain)
                print(f"[BULK] ❌ {domain} migration failed")

        except Exception as e:
            failed_domains.append(domain)
            print(f"[BULK] ❌ {domain} migration error: {e}")

    # Итоговый отчет
    print(f"\n{'='*60}")
    print(f"[BULK] MIGRATION COMPLETE")
    print(f"{'='*60}")
    print(f"Total domains: {len(domains)}")
    print(f"Successful: {success_count}")
    print(f"Failed: {len(failed_domains)}")

    if failed_domains:
        print(f"\nFailed domains:")
        for domain in failed_domains:
            print(f"  - {domain}")

    return 0 if not failed_domains else 1

if __name__ == '__main__':
    exit(main())