#!/usr/bin/env python3
"""
Скрипт для выворачивания доменной структуры в плоскую.
Преобразует domain/services/service/ → service-service/
"""

import os
import shutil
from pathlib import Path
from typing import List, Tuple

class DomainFlattener:
    def __init__(self, openapi_root: Path):
        self.openapi_root = openapi_root
        self.stats = {
            'services_moved': 0,
            'domains_processed': 0,
            'errors': []
        }

    def get_all_domains(self) -> List[Path]:
        """Получить все домены кроме backup'ов и example"""
        domains = []
        for item in self.openapi_root.iterdir():
            if item.is_dir() and item.name.endswith('-domain'):
                if 'backup' not in item.name and item.name != 'example-domain':
                    domains.append(item)
        return sorted(domains)

    def get_domain_services(self, domain_path: Path) -> List[Path]:
        """Получить все сервисы домена"""
        services_dir = domain_path / 'services'
        if not services_dir.exists():
            return []
        return [s for s in services_dir.iterdir() if s.is_dir()]

    def flatten_domain(self, domain_path: Path) -> None:
        """Вывернуть один домен"""
        print(f"[FLATTEN] Processing domain: {domain_path.name}")

        services = self.get_domain_services(domain_path)
        if not services:
            print(f"[SKIP] No services in {domain_path.name}")
            return

        for service_path in services:
            new_name = f"{service_path.name}-service"
            target_path = self.openapi_root / new_name

            print(f"[MOVE] {service_path.name} -> {new_name}")

            try:
                # Переместить сервис
                shutil.move(str(service_path), str(target_path))
                self.stats['services_moved'] += 1
            except Exception as e:
                error_msg = f"Failed to move {service_path.name}: {e}"
                print(f"[ERROR] {error_msg}")
                self.stats['errors'].append(error_msg)

        self.stats['domains_processed'] += 1

        # Проверить, остались ли файлы в домене
        remaining_files = list(domain_path.rglob('*'))
        remaining_files = [f for f in remaining_files if f.is_file()]

        if remaining_files:
            print(f"[INFO] Domain {domain_path.name} still contains {len(remaining_files)} files")
        else:
            print(f"[CLEANUP] Domain {domain_path.name} is now empty")

    def cleanup_empty_domains(self) -> None:
        """Удалить пустые домены"""
        print("[CLEANUP] Removing empty domains...")

        for domain_path in self.get_all_domains():
            # Проверить, есть ли файлы
            all_files = list(domain_path.rglob('*'))
            has_files = any(f.is_file() for f in all_files)

            if not has_files:
                try:
                    shutil.rmtree(domain_path)
                    print(f"[REMOVED] Empty domain: {domain_path.name}")
                except Exception as e:
                    print(f"[ERROR] Failed to remove {domain_path.name}: {e}")

    def execute_flattening(self) -> bool:
        """Выполнить полное выворачивание"""
        print("="*50)
        print("DOMAIN FLATTENING STARTED")
        print("="*50)

        domains = self.get_all_domains()
        print(f"[INFO] Found {len(domains)} domains to process")

        for domain in domains:
            self.flatten_domain(domain)

        self.cleanup_empty_domains()

        print("="*50)
        print("FLATTENING RESULTS:")
        print(f"  Domains processed: {self.stats['domains_processed']}")
        print(f"  Services moved: {self.stats['services_moved']}")
        print(f"  Errors: {len(self.stats['errors'])}")

        if self.stats['errors']:
            print("ERRORS:")
            for error in self.stats['errors']:
                print(f"  - {error}")

        print("="*50)

        return len(self.stats['errors']) == 0

def main():
    script_dir = Path(__file__).parent.parent.parent
    openapi_root = script_dir / 'proto' / 'openapi'

    if not openapi_root.exists():
        print(f"OpenAPI root not found: {openapi_root}")
        return 1

    flattener = DomainFlattener(openapi_root)

    success = flattener.execute_flattening()

    return 0 if success else 1

if __name__ == '__main__':
    exit(main())
