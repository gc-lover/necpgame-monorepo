#!/usr/bin/env python3
"""
Скрипт миграции сущностей на BASE-ENTITY систему.

Цель: Заменить дублированные поля в сущностях на allOf композицию с BASE-ENTITY.

Использование:
    python scripts/openapi/migrate-to-base-entity.py proto/openapi/social-domain/schemas/entities/guild.yaml --dry-run
    python scripts/openapi/migrate-to-base-entity.py proto/openapi/social-domain/ --all-entities --execute
"""

import os
import yaml
import argparse
from pathlib import Path
from typing import Dict, List, Set, Tuple, Optional
from collections import defaultdict
import re

class BaseEntityMigrator:
    """Мигратор сущностей на BASE-ENTITY систему."""

    # BASE-ENTITY определения будут загружаться из common-schemas.yaml

    def __init__(self, common_schemas_path: Optional[str] = None, dry_run: bool = True):
        if common_schemas_path:
            self.common_schemas_path = Path(common_schemas_path)
        else:
            # Ищем common-schemas.yaml относительно корня проекта
            script_dir = Path(__file__).parent.parent.parent  # scripts/openapi/ -> scripts/ -> root
            possible_paths = [
                script_dir / "proto" / "openapi" / "common-schemas.yaml",
                script_dir / "common-schemas.yaml",
                Path("proto/openapi/common-schemas.yaml"),
                Path("common-schemas.yaml"),
            ]
            for path in possible_paths:
                if path.exists():
                    self.common_schemas_path = path
                    break
            else:
                self.common_schemas_path = script_dir / "proto" / "openapi" / "common-schemas.yaml"

        self.dry_run = dry_run
        self.stats = {
            'entities_processed': 0,
            'entities_migrated': 0,
            'fields_removed': 0,
            'errors': []
        }

        # Загрузка BASE-ENTITY определений
        self.base_entities = self._load_base_entities()

    def _load_base_entities(self) -> Dict[str, Set[str]]:
        """Загрузка определений BASE-ENTITY из common-schemas.yaml."""
        if not self.common_schemas_path.exists():
            print(f"[WARNING] common-schemas.yaml not found: {self.common_schemas_path}")
            return {}  # Возвращаем пустой словарь вместо жестко заданных

        try:
            with open(self.common_schemas_path, 'r', encoding='utf-8') as f:
                content = yaml.safe_load(f)

            schemas = content.get('components', {}).get('schemas', {})
            loaded_entities = {}

            for schema_name, schema_def in schemas.items():
                if isinstance(schema_def, dict):
                    # Собираем все поля из схемы, включая вложенные allOf
                    all_fields = set()
                    self._extract_all_fields(schema_def, schemas, all_fields)
                    if all_fields:  # Только если есть поля
                        loaded_entities[schema_name] = all_fields

            print(f"[OK] Loaded {len(loaded_entities)} BASE-ENTITY definitions from {self.common_schemas_path}")
            return loaded_entities

        except Exception as e:
            print(f"[WARNING] Error loading BASE-ENTITY: {e}")
            return {}

    def _extract_all_fields(self, schema_def: dict, all_schemas: dict, fields: set, visited: set = None) -> None:
        """Рекурсивно извлекает все поля из схемы, включая allOf композицию."""
        if visited is None:
            visited = set()

        # Избегаем циклических ссылок
        schema_id = id(schema_def)
        if schema_id in visited:
            return
        visited.add(schema_id)

        # Прямые properties
        if 'properties' in schema_def:
            fields.update(schema_def['properties'].keys())

        # allOf композиция
        if 'allOf' in schema_def:
            for item in schema_def['allOf']:
                if isinstance(item, dict):
                    if '$ref' in item:
                        # Разрешаем ссылку
                        ref = item['$ref']
                        if ref.startswith('#/components/schemas/'):
                            ref_name = ref.split('#/components/schemas/')[1]
                            if ref_name in all_schemas:
                                self._extract_all_fields(all_schemas[ref_name], all_schemas, fields, visited)
                    else:
                        # Встроенная схема
                        self._extract_all_fields(item, all_schemas, fields, visited)

    def migrate_entity_file(self, file_path: str) -> Optional[Dict]:
        """Миграция одного файла сущности."""
        file_path = Path(file_path)

        if not file_path.exists():
            raise FileNotFoundError(f"Файл не найден: {file_path}")

        try:
            with open(file_path, 'r', encoding='utf-8') as f:
                content = yaml.safe_load(f)

            # Находим все схемы в файле
            migrated_schemas = {}

            if 'components' in content and 'schemas' in content['components']:
                for schema_name, schema_def in content['components']['schemas'].items():
                    migrated_schema = self._migrate_single_entity(schema_name, schema_def)
                    if migrated_schema:
                        migrated_schemas[schema_name] = migrated_schema
                        self.stats['entities_migrated'] += 1

            self.stats['entities_processed'] += len(content.get('components', {}).get('schemas', {}))

            # Создание нового содержимого файла
            if migrated_schemas:
                new_content = content.copy()
                new_content['components']['schemas'] = migrated_schemas

                if not self.dry_run:
                    with open(file_path, 'w', encoding='utf-8') as f:
                        yaml.dump(new_content, f, default_flow_style=False, allow_unicode=True)

                return {
                    'file': str(file_path),
                    'migrated_entities': list(migrated_schemas.keys()),
                    'total_entities': len(content.get('components', {}).get('schemas', {}))
                }

        except Exception as e:
            self.stats['errors'].append(f"Ошибка обработки {file_path}: {e}")
            return None

    def _migrate_single_entity(self, entity_name: str, schema_def: Dict) -> Optional[Dict]:
        """Миграция одной сущности на BASE-ENTITY."""
        if not isinstance(schema_def, dict) or 'properties' not in schema_def:
            return None

        entity_fields = set(schema_def['properties'].keys())

        # Находим лучший BASE-ENTITY для этой сущности
        best_base_entity, matching_fields = self._find_best_base_entity(entity_fields)

        if not best_base_entity or not matching_fields:
            return None  # Не найдено подходящего BASE-ENTITY

        # Вычисляем поля, которые останутся в сущности
        remaining_fields = entity_fields - matching_fields

        if not remaining_fields:
            return None  # Все поля покрыты BASE-ENTITY, но должна остаться хотя бы 1 уникальное поле

        # Создаем новую схему с allOf
        new_schema = schema_def.copy()

        # Добавляем allOf композицию
        new_schema['allOf'] = [
            {
                '$ref': f'../../common-schemas.yaml#/components/schemas/{best_base_entity}'
            },
            {
                'type': 'object',
                'properties': {field: schema_def['properties'][field] for field in remaining_fields}
            }
        ]

        # Удаляем старые properties (они теперь в allOf)
        del new_schema['properties']

        # Обновляем required поля (убираем те, что теперь в BASE-ENTITY)
        if 'required' in new_schema:
            base_entity_fields = self.base_entities.get(best_base_entity, set())
            new_required = [field for field in new_schema['required'] if field not in base_entity_fields]
            if new_required:
                new_schema['required'] = new_required
            else:
                del new_schema['required']

        # Обновляем статистику
        self.stats['fields_removed'] += len(matching_fields)

        return new_schema

    def _find_best_base_entity(self, entity_fields: Set[str]) -> Tuple[Optional[str], Optional[Set[str]]]:
        """Поиск лучшего BASE-ENTITY для набора полей."""
        best_match = None
        best_matching_fields = set()
        max_matching_count = 0

        for base_entity, base_fields in self.base_entities.items():
            matching_fields = entity_fields.intersection(base_fields)

            # Лучший матч - максимальное количество совпадающих полей
            # Но только если совпадает больше 50% полей BASE-ENTITY
            if len(matching_fields) > max_matching_count and len(matching_fields) >= len(base_fields) * 0.5:
                max_matching_count = len(matching_fields)
                best_match = base_entity
                best_matching_fields = matching_fields

        return best_match, best_matching_fields

    def migrate_domain_entities(self, domain_path: str) -> List[Dict]:
        """Миграция всех сущностей в домене."""
        domain_path = Path(domain_path)
        results = []

        # Ищем все файлы сущностей
        entity_patterns = [
            '**/schemas/entities/*.yaml',
            '**/schemas/*.yaml',
            '**/*entity*.yaml',
            '**/*entities*.yaml'
        ]

        migrated_files = set()

        for pattern in entity_patterns:
            for entity_file in domain_path.glob(pattern):
                if entity_file in migrated_files:
                    continue

                result = self.migrate_entity_file(str(entity_file))
                if result:
                    results.append(result)
                    migrated_files.add(entity_file)

        return results

    def generate_report(self, results: List[Dict]) -> str:
        """Генерация отчета о миграции."""
        report = []
        report.append("# 📊 ОТЧЕТ МИГРАЦИИ НА BASE-ENTITY")
        report.append("")
        report.append(f"**Режим:** {'DRY RUN' if self.dry_run else 'EXECUTE'}")
        report.append("")

        report.append("## 📈 СТАТИСТИКА МИГРАЦИИ")
        report.append("")
        report.append(f"- **Сущностей обработано:** {self.stats['entities_processed']}")
        report.append(f"- **Сущностей мигрировано:** {self.stats['entities_migrated']}")
        report.append(f"- **Полей удалено:** {self.stats['fields_removed']}")
        report.append(f"- **Файлов обработано:** {len(results)}")
        report.append(f"- **Ошибок:** {len(self.stats['errors'])}")
        report.append("")

        if results:
            report.append("## 📁 МИГРИРОВАННЫЕ ФАЙЛЫ")
            report.append("")
            for result in results:
                report.append(f"### {result['file']}")
                report.append(f"- **Мигрировано сущностей:** {len(result['migrated_entities'])}")
                report.append(f"- **Всего сущностей:** {result['total_entities']}")
                if result['migrated_entities']:
                    report.append(f"- **Сущности:** {', '.join(result['migrated_entities'])}")
                report.append("")

        if self.stats['errors']:
            report.append("## ❌ ОШИБКИ")
            report.append("")
            for error in self.stats['errors'][:10]:
                report.append(f"- {error}")
            if len(self.stats['errors']) > 10:
                report.append(f"- ... и еще {len(self.stats['errors']) - 10} ошибок")
            report.append("")

        # Эффективность DRY
        if self.stats['entities_processed'] > 0:
            migration_rate = (self.stats['entities_migrated'] / self.stats['entities_processed']) * 100
            avg_fields_removed = self.stats['fields_removed'] / self.stats['entities_migrated'] if self.stats['entities_migrated'] > 0 else 0

            report.append("## 📊 ЭФФЕКТИВНОСТЬ DRY")
            report.append("")
            report.append(".1f")
            report.append(".1f")
            report.append("")

        report.append("## 💡 РЕКОМЕНДАЦИИ")
        report.append("")
        if self.dry_run:
            report.append("1. **Проверьте изменения** в DRY RUN режиме")
            report.append("2. **Исправьте ошибки** в списке выше")
            report.append("3. **Запустите с --execute** для применения изменений")
        else:
            report.append("1. **Запустите валидацию:** `npx @redocly/cli lint`")
            report.append("2. **Проверьте генерацию кода:** `ogen --target test-gen`")
            report.append("3. **Обновите тесты** для измененных схем")
            report.append("4. **Создайте backup** для отката при необходимости")

        return "\n".join(report)


def main():
    parser = argparse.ArgumentParser(description='Миграция сущностей на BASE-ENTITY систему')
    parser.add_argument('path', help='Путь к файлу сущности или домену')
    parser.add_argument('--all-entities', action='store_true', help='Мигрировать все сущности в домене')
    parser.add_argument('--dry-run', action='store_true', help='Только анализ, без изменений')
    parser.add_argument('--execute', action='store_true', help='Выполнить миграцию')
    parser.add_argument('--common-schemas', help='Путь к common-schemas.yaml')
    parser.add_argument('--output', '-o', default='scripts/reports/base-entity-migration-report.md', help='Файл для отчета')

    args = parser.parse_args()

    if not (args.dry_run or args.execute):
        args.dry_run = True  # По умолчанию dry-run

    migrator = BaseEntityMigrator(args.common_schemas, dry_run=args.dry_run)

    results = []

    if args.all_entities:
        # Миграция всех сущностей в домене
        results = migrator.migrate_domain_entities(args.path)
    else:
        # Миграция одного файла
        result = migrator.migrate_entity_file(args.path)
        if result:
            results = [result]

    # Генерация и сохранение отчета
    report = migrator.generate_report(results)
    with open(args.output, 'w', encoding='utf-8') as f:
        f.write(report)

    print(f"[REPORT] Report saved to: {args.output}")

    # Вывод краткой статистики
    print("\n[STATS] SUMMARY:")
    print(f"   Entities processed: {migrator.stats['entities_processed']}")
    print(f"   Entities migrated: {migrator.stats['entities_migrated']}")
    print(f"   Fields removed: {migrator.stats['fields_removed']}")
    if migrator.stats['errors']:
        print(f"   Errors: {len(migrator.stats['errors'])}")


if __name__ == '__main__':
    main()
