#!/usr/bin/env python3
"""
Универсальный скрипт миграции структуры доменов на стандартизированную архитектуру.

Анализирует структуру любого домена и автоматически организует файлы по сервисам.
"""

import os
import sys
import shutil
from pathlib import Path
from typing import Dict, List, Set
import re

class SocialDomainMigrator:
    """Мигратор структуры social-domain."""

    def __init__(self, dry_run: bool = True):
        self.dry_run = dry_run
        self.project_root = Path(__file__).parent.parent.parent
        self.domain_path = self.project_root / "proto" / "openapi" / "social-domain"
        print(f"[INIT] Project root: {self.project_root}")
        print(f"[INIT] Domain path: {self.domain_path}")
        print(f"[INIT] Domain exists: {self.domain_path.exists()}")
        self.stats = {
            'files_moved': 0,
            'dirs_created': 0,
            'services_created': 0,
            'errors': []
        }

        # Маппинг файлов по сервисам
        # Маппинг файлов по сервисам (на основе реальной структуры)
        self.service_mapping = {
            'guilds': [
                'guild-service-go',  # папка guild-service-go/main.yaml
                'guilds',            # папка guilds/main.yaml
                'guild-service'      # подпапка guilds/guild-service/main.yaml
            ],
            'notifications': [
                'notifications',     # папка notifications/main.yaml
                'notification-service'  # подпапка notifications/notification-service/main.yaml
            ],
            'chat': [
                'chat'               # папка chat/main.yaml
            ],
            'mail': [
                'mail'               # папка mail/main.yaml
            ],
            'legend-templates': [
                'legend-templates/legend-templates-service-go'  # полный путь к подпапке
            ],
            'lobby': [
                'lobby/ws-lobby-go'        # полный путь к подпапке
            ],
            'voice-chat': [
                'voice-chat'         # папка voice-chat/main.yaml
            ],
            'dialogue-management': [
                'dialogue-management'  # папка dialogue-management/main.yaml
            ],
            'content-management': [
                'content-management/dialogues',         # полный путь к подпапке
                'content-management/lore',              # полный путь к подпапке
                'content-management/npcs',              # полный путь к подпапке
                'content-management/quests'             # полный путь к подпапке
            ]
        }

    def execute_migration(self):
        """Выполнить миграцию."""
        print(f"[MIGRATION] Starting social-domain structure migration")
        print(f"[MIGRATION] Mode: {'DRY RUN' if self.dry_run else 'EXECUTE'}")
        print(f"[MIGRATION] Domain: {self.domain_path}")
        print(f"[MIGRATION] Domain exists: {self.domain_path.exists()}")

        if not self.domain_path.exists():
            print(f"[ERROR] Domain not found: {self.domain_path}")
            return False

        print(f"[MIGRATION] Starting migration process...")

        try:
            # Создать целевую структуру
            print("[STEP 1/4] Creating target directory structure...")
            self._create_target_structure()
            print(f"[STEP 1/4] Directory structure created. {self.stats['dirs_created']} directories.")

            # Миграция сервисов
            print("[STEP 2/4] Migrating services...")
            self._migrate_services()
            print(f"[STEP 2/4] Services migrated. {self.stats['services_created']} services created.")

            # Перемещение схем
            print("[STEP 3/4] Migrating schemas...")
            self._migrate_schemas()
            print(f"[STEP 3/4] Schemas migrated. {self.stats['files_moved']} files moved.")

            # Генерация отчета
            print("[STEP 4/4] Generating migration report...")
            self._generate_report()
            print(f"[STEP 4/4] Report generated.")

            print(f"[SUCCESS] Migration completed successfully")
            print(f"[SUMMARY] Total actions: dirs={self.stats['dirs_created']}, services={self.stats['services_created']}, files={self.stats['files_moved']}")
            return True

        except Exception as e:
            print(f"[ERROR] Migration failed: {e}")
            import traceback
            traceback.print_exc()
            return False

    def _create_target_structure(self):
        """Создать целевую структуру директорий."""
        print("[STRUCTURE] Creating target directory structure...")

        target_dirs = [
            'services',
            'schemas/entities',
            'schemas/common',
            'schemas/enums'
        ]

        for service_name in self.service_mapping.keys():
            target_dirs.extend([
                f'services/{service_name}',
                f'services/{service_name}/schemas',
                f'services/{service_name}/schemas/requests',
                f'services/{service_name}/schemas/responses',
                f'services/{service_name}/schemas/models'
            ])

        for dir_path in target_dirs:
            full_path = self.domain_path / dir_path
            if not full_path.exists():
                if not self.dry_run:
                    full_path.mkdir(parents=True, exist_ok=True)
                self.stats['dirs_created'] += 1
                print(f"[CREATE] Directory created: {dir_path}")
            else:
                print(f"[SKIP] Directory already exists: {dir_path}")

        print(f"[STRUCTURE] Directory creation completed. Total: {self.stats['dirs_created']} created")

    def _migrate_services(self):
        """Миграция файлов сервисов."""
        print("[SERVICES] Migrating service files...")

        services_dir = self.domain_path / 'services'

        for service_name, file_patterns in self.service_mapping.items():
            print(f"[SERVICE] Processing service '{service_name}' with {len(file_patterns)} file patterns")

            service_dir = self.domain_path / 'services' / service_name
            moved_files = 0

            for pattern in file_patterns:
                print(f"[SEARCH] Looking for: {pattern}")
                folder_found = False
                source_dir = None

                # Ищем папку по полному пути относительно domain
                candidate_path = self.domain_path / pattern
                if candidate_path.exists() and candidate_path.is_dir():
                    source_dir = candidate_path
                    print(f"[SEARCH] Found folder: {pattern}")
                    folder_found = True
                elif '/' in pattern:
                    # Если не нашли, попробуем найти как подпапку
                    alt_path = self.domain_path / pattern.split('/')[0] / pattern.split('/')[1]
                    if alt_path.exists() and alt_path.is_dir():
                        source_dir = alt_path
                        print(f"[SEARCH] Found folder via alt path: {pattern}")
                        folder_found = True
                else:
                    # Ищем YAML файл прямо в корне домена
                    yaml_file = self.domain_path / f"{pattern}.yaml"
                    if yaml_file.exists() and yaml_file.is_file():
                        print(f"[SEARCH] Found file: {pattern}.yaml")
                        # Создаем временную "папку" для обработки файла
                        source_dir = self.domain_path / f"temp_{pattern}"
                        source_dir.mkdir(exist_ok=True)
                        # Копируем файл в временную папку как main.yaml
                        import shutil
                        shutil.copy2(str(yaml_file), str(source_dir / 'main.yaml'))
                        # Удаляем оригинальный файл
                        yaml_file.unlink()
                        print(f"[SEARCH] Converted file to folder structure: {pattern}.yaml -> temp_{pattern}/main.yaml")
                        folder_found = True

                if not folder_found:
                    print(f"[SEARCH] Not found: {pattern}")
                    continue

                    # Ищем main.yaml в этой папке
                    main_yaml = source_dir / 'main.yaml'
                    if main_yaml.exists():
                        target_path = service_dir / 'main.yaml'
                        print(f"[MOVE] Main service file: {main_yaml.relative_to(self.domain_path)} -> {target_path.relative_to(self.domain_path)}")

                        if not self.dry_run:
                            target_path.parent.mkdir(parents=True, exist_ok=True)
                            shutil.move(str(main_yaml), str(target_path))
                            moved_files += 1
                            self.stats['files_moved'] += 1
                        else:
                            print(f"[DRY-RUN] Would move: {main_yaml.relative_to(self.domain_path)} -> {target_path.relative_to(self.domain_path)}")

                        # Также ищем другие YAML файлы в этой папке (schemas и т.д.)
                        for yaml_file in source_dir.glob('*.yaml'):
                            if yaml_file.name != 'main.yaml':
                                # Определяем категорию по имени файла
                                if 'request' in yaml_file.name.lower():
                                    target_path = service_dir / 'schemas' / 'requests' / yaml_file.name
                                    category = "requests"
                                elif 'response' in yaml_file.name.lower():
                                    target_path = service_dir / 'schemas' / 'responses' / yaml_file.name
                                    category = "responses"
                                else:
                                    target_path = service_dir / 'schemas' / 'models' / yaml_file.name
                                    category = "models"

                                print(f"[MOVE] Schema file ({category}): {yaml_file.relative_to(self.domain_path)} -> {target_path.relative_to(self.domain_path)}")

                                if not self.dry_run:
                                    target_path.parent.mkdir(parents=True, exist_ok=True)
                                    shutil.move(str(yaml_file), str(target_path))
                                    moved_files += 1
                                    self.stats['files_moved'] += 1
                                else:
                                    print(f"[DRY-RUN] Would move: {yaml_file.relative_to(self.domain_path)} -> {target_path.relative_to(self.domain_path)}")

                        # Рекурсивно ищем YAML файлы в подпапках
                        for yaml_file in source_dir.rglob('*.yaml'):
                            if yaml_file.name == 'main.yaml' and yaml_file.parent != source_dir:
                                # Это main.yaml в подпапке - перемещаем в соответствующую категорию
                                subfolder_name = yaml_file.parent.name
                                if 'request' in subfolder_name.lower():
                                    target_path = service_dir / 'schemas' / 'requests' / f"{subfolder_name}.yaml"
                                    category = "requests"
                                elif 'response' in subfolder_name.lower():
                                    target_path = service_dir / 'schemas' / 'responses' / f"{subfolder_name}.yaml"
                                    category = "responses"
                                else:
                                    target_path = service_dir / 'schemas' / 'models' / f"{subfolder_name}.yaml"
                                    category = "models"

                                print(f"[MOVE] Subfolder main file ({category}): {yaml_file.relative_to(self.domain_path)} -> {target_path.relative_to(self.domain_path)}")

                                if not self.dry_run:
                                    target_path.parent.mkdir(parents=True, exist_ok=True)
                                    shutil.move(str(yaml_file), str(target_path))
                                    moved_files += 1
                                    self.stats['files_moved'] += 1
                                else:
                                    print(f"[DRY-RUN] Would move: {yaml_file.relative_to(self.domain_path)} -> {target_path.relative_to(self.domain_path)}")
                    else:
                        print(f"[SEARCH] No main.yaml found in folder: {pattern}")

                # Обрабатываем найденную папку (если folder_found = True)
                if folder_found:
                    # Ищем main.yaml в этой папке
                    main_yaml = source_dir / 'main.yaml'
                    if main_yaml.exists():
                        target_path = service_dir / 'main.yaml'
                        print(f"[MOVE] Main service file: {main_yaml.relative_to(self.domain_path)} -> {target_path.relative_to(self.domain_path)}")

                        if not self.dry_run:
                            target_path.parent.mkdir(parents=True, exist_ok=True)
                            shutil.move(str(main_yaml), str(target_path))
                            moved_files += 1
                            self.stats['files_moved'] += 1
                        else:
                            print(f"[DRY-RUN] Would move: {main_yaml.relative_to(self.domain_path)} -> {target_path.relative_to(self.domain_path)}")

                        # Также ищем другие YAML файлы в этой папке (schemas и т.д.)
                        for yaml_file in source_dir.glob('*.yaml'):
                            if yaml_file.name != 'main.yaml':
                                # Определяем категорию по имени файла
                                if 'request' in yaml_file.name.lower():
                                    target_path = service_dir / 'schemas' / 'requests' / yaml_file.name
                                    category = "requests"
                                elif 'response' in yaml_file.name.lower():
                                    target_path = service_dir / 'schemas' / 'responses' / yaml_file.name
                                    category = "responses"
                                else:
                                    target_path = service_dir / 'schemas' / 'models' / yaml_file.name
                                    category = "models"
                                print(f"[MOVE] Schema file ({category}): {yaml_file.relative_to(self.domain_path)} -> {target_path.relative_to(self.domain_path)}")

                                if not self.dry_run:
                                    target_path.parent.mkdir(parents=True, exist_ok=True)
                                    shutil.move(str(yaml_file), str(target_path))
                                    moved_files += 1
                                    self.stats['files_moved'] += 1
                                else:
                                    print(f"[DRY-RUN] Would move: {yaml_file.relative_to(self.domain_path)} -> {target_path.relative_to(self.domain_path)}")

                        # Рекурсивно ищем YAML файлы в подпапках
                        for yaml_file in source_dir.rglob('*.yaml'):
                            if yaml_file.name == 'main.yaml' and yaml_file.parent != source_dir:
                                # Это main.yaml в подпапке - перемещаем в соответствующую категорию
                                subfolder_name = yaml_file.parent.name
                                if 'request' in subfolder_name.lower():
                                    target_path = service_dir / 'schemas' / 'requests' / f"{subfolder_name}.yaml"
                                    category = "requests"
                                elif 'response' in subfolder_name.lower():
                                    target_path = service_dir / 'schemas' / 'responses' / f"{subfolder_name}.yaml"
                                    category = "responses"
                                else:
                                    target_path = service_dir / 'schemas' / 'models' / f"{subfolder_name}.yaml"
                                    category = "models"

                                print(f"[MOVE] Subfolder main file ({category}): {yaml_file.relative_to(self.domain_path)} -> {target_path.relative_to(self.domain_path)}")

                                if not self.dry_run:
                                    target_path.parent.mkdir(parents=True, exist_ok=True)
                                    shutil.move(str(yaml_file), str(target_path))
                                    moved_files += 1
                                    self.stats['files_moved'] += 1
                                else:
                                    print(f"[DRY-RUN] Would move: {yaml_file.relative_to(self.domain_path)} -> {target_path.relative_to(self.domain_path)}")
                    else:
                        print(f"[SEARCH] No main.yaml found in folder: {pattern}")

            if moved_files > 0:
                self.stats['services_created'] += 1
                print(f"[SERVICE] {service_name}: {moved_files} files migrated successfully")
            else:
                print(f"[SERVICE] {service_name}: No files found to migrate")

        # Удалить пустые директории в services (кроме новых сервисов)
        if not self.dry_run:
            self._cleanup_empty_dirs(services_dir)

    def _migrate_schemas(self):
        """Миграция файлов схем."""
        print("[SCHEMAS] Migrating schema files...")

        # Обработка оставшихся schema файлов в директориях сервисов
        service_dirs = ['guilds', 'notifications']

        for service_name in service_dirs:
            service_path = self.domain_path / service_name
            if service_path.exists():
                schemas_path = service_path / 'schemas'
                if schemas_path.exists():
                    schema_files = list(schemas_path.glob('*.yaml'))
                    if schema_files:
                        print(f"[SCHEMAS] Found {len(schema_files)} remaining schema files in {service_name}/schemas/")

                        for yaml_file in schema_files:
                            target_path = self.domain_path / 'services' / service_name / 'schemas' / 'models' / yaml_file.name
                            if not self.dry_run:
                                target_path.parent.mkdir(parents=True, exist_ok=True)
                                shutil.move(str(yaml_file), str(target_path))
                                self.stats['files_moved'] += 1
                                print(f"[MOVE] Remaining schema moved: {yaml_file.relative_to(self.domain_path)} -> services/{service_name}/schemas/models/{yaml_file.name}")
                            else:
                                print(f"[DRY-RUN] Would move remaining schema: {yaml_file.relative_to(self.domain_path)} -> services/{service_name}/schemas/models/{yaml_file.name}")

        # Удаление пустых директорий сервисов после перемещения файлов
        for service_name in service_dirs:
            service_path = self.domain_path / service_name
            if service_path.exists():
                # Рекурсивно удаляем пустые поддиректории
                self._cleanup_empty_dirs_in_path(service_path)

        print("[SCHEMAS] Schema migration completed")

    def _cleanup_empty_dirs(self, base_dir: Path):
        """Удалить пустые директории."""
        print("[CLEANUP] Cleaning up empty directories...")
        removed_count = 0

        for dir_path in sorted(base_dir.rglob('*'), key=lambda x: len(str(x)), reverse=True):
            if dir_path.is_dir() and not any(dir_path.iterdir()):
                try:
                    dir_path.rmdir()
                    removed_count += 1
                    print(f"[CLEANUP] Removed empty directory: {dir_path.relative_to(self.domain_path)}")
                except Exception as e:
                    print(f"[CLEANUP] Failed to remove {dir_path.relative_to(self.domain_path)}: {e}")

        print(f"[CLEANUP] Cleanup completed. {removed_count} empty directories removed")

    def _cleanup_empty_dirs_in_path(self, base_dir: Path):
        """Удалить пустые директории в указанном пути."""
        removed_count = 0

        for dir_path in sorted(base_dir.rglob('*'), key=lambda x: len(str(x)), reverse=True):
            if dir_path.is_dir() and not any(dir_path.iterdir()):
                try:
                    dir_path.rmdir()
                    removed_count += 1
                    print(f"[CLEANUP] Removed empty directory in service: {dir_path.relative_to(self.domain_path)}")
                except Exception as e:
                    print(f"[CLEANUP] Failed to remove {dir_path.relative_to(self.domain_path)}: {e}")

        if removed_count > 0:
            print(f"[CLEANUP] Cleaned up {removed_count} empty directories in {base_dir.name}")

    def _generate_report(self):
        """Генерация отчета о миграции."""
        report = []
        report.append("# 📊 ОТЧЕТ МИГРАЦИИ СТРУКТУРЫ SOCIAL-DOMAIN")
        report.append("")
        report.append(f"**Режим:** {'DRY RUN' if self.dry_run else 'EXECUTE'}")
        report.append(f"**Время:** {__import__('datetime').datetime.now().strftime('%Y-%m-%d %H:%M:%S')}")
        report.append("")

        report.append("## 📈 СТАТИСТИКА")
        report.append("")
        report.append(f"- **Директорий создано:** {self.stats['dirs_created']}")
        report.append(f"- **Файлов перемещено:** {self.stats['files_moved']}")
        report.append(f"- **Сервисов создано:** {self.stats['services_created']}")
        report.append("")

        report.append("## 🏗️ СОЗДАННАЯ СТРУКТУРА")
        report.append("")
        report.append("```")
        report.append("social-domain/")
        report.append("├── services/")
        for service_name in sorted(self.service_mapping.keys()):
            report.append(f"│   ├── {service_name}/")
            report.append("│   │   ├── main.yaml")
            report.append("│   │   └── schemas/")
            report.append("│   │       ├── requests/")
            report.append("│   │       ├── responses/")
            report.append("│   │       └── models/")
        report.append("├── schemas/")
        report.append("│   ├── entities/")
        report.append("│   ├── common/")
        report.append("│   └── enums/")
        report.append("└── main.yaml")
        report.append("```")
        report.append("")

        if self.stats['errors']:
            report.append("## ❌ ОШИБКИ")
            report.append("")
            for error in self.stats['errors']:
                report.append(f"- {error}")
            report.append("")

        # Сохранить отчет (всегда, даже в dry-run режиме)
        report_path = self.project_root / "scripts" / "reports" / "social-domain-structure-migration.md"
        report_path.parent.mkdir(parents=True, exist_ok=True)
        with open(report_path, 'w', encoding='utf-8') as f:
            f.write('\n'.join(report))

        print(f"[REPORT] Report saved to: {report_path}")


def main():
    import argparse

    parser = argparse.ArgumentParser(description='Миграция структуры social-domain на стандартизированную архитектуру')
    parser.add_argument('--dry-run', action='store_true', help='Только анализ, без изменений')
    parser.add_argument('--execute', action='store_true', help='Выполнить миграцию')

    args = parser.parse_args()

    if not (args.dry_run or args.execute):
        args.dry_run = True

    print("[START] Social-domain structure migration script")
    print(f"[CONFIG] Mode: {'DRY RUN' if args.dry_run else 'EXECUTE'}")
    print(f"[CONFIG] Domain: social-domain")

    migrator = SocialDomainMigrator(dry_run=args.dry_run)
    success = migrator.execute_migration()

    if success:
        print(f"\n[SUCCESS] Social-domain structure migration completed successfully")
        print(f"[SUMMARY] Migration stats:")
        print(f"  - Directories created: {migrator.stats['dirs_created']}")
        print(f"  - Services migrated: {migrator.stats['services_created']}")
        print(f"  - Files moved: {migrator.stats['files_moved']}")
        if migrator.stats['errors']:
            print(f"  - Errors encountered: {len(migrator.stats['errors'])}")
        return 0
    else:
        print(f"\n[FAILED] Social-domain structure migration failed")
        if migrator.stats['errors']:
            print(f"[ERRORS] Encountered {len(migrator.stats['errors'])} errors:")
            for i, error in enumerate(migrator.stats['errors'][:5], 1):  # Show first 5 errors
                print(f"  {i}. {error}")
        return 1


if __name__ == '__main__':
    exit(main())
