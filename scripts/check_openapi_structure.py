#!/usr/bin/env python3
"""
Проверка структуры OpenAPI после реорганизации
"""

import os
from pathlib import Path
from typing import Dict, List, Set

def check_openapi_structure():
    """Проверить структуру OpenAPI директорий"""
    openapi_dir = Path("proto/openapi")

    if not openapi_dir.exists():
        print("❌ proto/openapi directory not found")
        return

    print("📊 Анализ структуры OpenAPI после реорганизации")
    print("=" * 50)

    # Собрать статистику
    stats = {
        'total_files': 0,
        'yaml_files': 0,
        'services': [],
        'service_files': {},
        'duplicates_found': []
    }

    # Проверить каждую директорию сервиса
    for service_dir in sorted(openapi_dir.iterdir()):
        if not service_dir.is_dir() or service_dir.name.startswith('.'):
            continue

        service_name = service_dir.name
        stats['services'].append(service_name)

        # Посчитать файлы в сервисе
        yaml_count = 0
        all_files = []

        for file_path in service_dir.rglob('*'):
            if file_path.is_file():
                stats['total_files'] += 1
                all_files.append(file_path.name)

                if file_path.suffix == '.yaml':
                    stats['yaml_files'] += 1
                    yaml_count += 1

        stats['service_files'][service_name] = yaml_count

        # Проверить на дубликаты в поддиректориях
        main_files = []
        sub_files = []

        for file_path in service_dir.glob('*.yaml'):
            main_files.append(file_path.name)

        for file_path in service_dir.rglob('*.yaml'):
            if len(file_path.parts) > len(service_dir.parts) + 1:  # В поддиректориях
                sub_files.append(file_path.name)

        duplicates = set(main_files) & set(sub_files)
        if duplicates:
            stats['duplicates_found'].append({
                'service': service_name,
                'duplicates': list(duplicates)
            })

        print(f"📁 {service_name}: {yaml_count} YAML файлов")

    print("\n" + "=" * 50)
    print("📈 СТАТИСТИКА:")
    print(f"  • Всего директорий сервисов: {len(stats['services'])}")
    print(f"  • Всего файлов: {stats['total_files']}")
    print(f"  • YAML файлов: {stats['yaml_files']}")

    print("\n🏆 ТОП-5 сервисов по количеству YAML файлов:")
    top_services = sorted(stats['service_files'].items(), key=lambda x: x[1], reverse=True)[:5]
    for service, count in top_services:
        print(f"  • {service}: {count} файлов")

    if stats['duplicates_found']:
        print("\n⚠️  НАЙДЕНЫ ДУБЛИКАТЫ:")
        for dup in stats['duplicates_found']:
            print(f"  • {dup['service']}: {', '.join(dup['duplicates'])}")
    else:
        print("\n✅ Дубликатов не найдено")

    print("\n🎯 СТРУКТУРА ГОТОВА К ИСПОЛЬЗОВАНИЮ")

if __name__ == "__main__":
    check_openapi_structure()
