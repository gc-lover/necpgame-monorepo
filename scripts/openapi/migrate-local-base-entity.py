#!/usr/bin/env python3
"""
Скрипт замены локальных BASE-ENTITY на ссылки на common-schemas.yaml.

Цель: Убрать дублирование BASE-ENTITY схем в доменах и использовать глобальные.

Использование:
    python scripts/openapi/migrate-local-base-entity.py proto/openapi/companion-domain/main.yaml --dry-run
    python scripts/openapi/migrate-local-base-entity.py proto/openapi/companion-domain/main.yaml --execute
"""

import os
import yaml
import argparse
from pathlib import Path
from typing import Dict, List, Set, Tuple, Optional
import re

class LocalBaseEntityMigrator:
    """Мигратор локальных BASE-ENTITY на глобальные ссылки."""

    # Локальные BASE-ENTITY, которые нужно заменить на глобальные
    LOCAL_BASE_ENTITIES = {
        'NamedEntity': '../../common-schemas.yaml#/components/schemas/NamedEntity',
        'Error': '../../common-schemas.yaml#/components/schemas/Error',
        'BearerAuth': '../../common-schemas.yaml#/components/schemas/BearerAuth',
        'BaseEntityWithTimestamps': '../../common-schemas.yaml#/components/schemas/BaseEntityWithTimestamps',
    }

    def __init__(self, file_path: str, dry_run: bool = True):
        self.file_path = Path(file_path)
        self.dry_run = dry_run
        self.stats = {
            'refs_updated': 0,
            'schemas_removed': 0,
            'errors': []
        }

    def migrate(self) -> bool:
        """Выполнить полную миграцию домена."""
        print(f"[START] Full domain migration: {self.file_path.name}")
        print(f"Mode: {'DRY RUN' if self.dry_run else 'EXECUTE'}")
        print()

        try:
            # Загрузить файл
            with open(self.file_path, 'r', encoding='utf-8') as f:
                content = f.read()

            original_content = content

            # 1. Анализировать все ссылки в файле
            all_refs = self._analyze_all_refs(content)
            print(f"[ANALYZE] Found {len(all_refs)} total references")

            # 2. Определить локальные BASE-ENTITY для замены
            local_base_entities = self._identify_local_base_entities(content)
            print(f"[IDENTIFY] Found {len(local_base_entities)} local BASE-ENTITY to replace")

            # 3. Найти transitively зависимые схемы
            dependent_schemas = self._find_dependent_schemas(content, local_base_entities)
            print(f"[DEPENDENCY] Found {len(dependent_schemas)} dependent schemas to embed")

            # 4. Заменить ссылки на локальные BASE-ENTITY
            content = self._replace_local_references(content)

            # 5. Добавить недостающие BASE-ENTITY схемы
            content = self._embed_missing_base_entities(content, dependent_schemas)

            # 6. Удалить определения локальных BASE-ENTITY
            content = self._remove_local_definitions(content)

            # 7. Сохранить изменения
            if content != original_content:
                if not self.dry_run:
                    with open(self.file_path, 'w', encoding='utf-8') as f:
                        f.write(content)
                print(f"[SUCCESS] Fully migrated {self.file_path}")
                print(f"[STATS] Refs updated: {self.stats['refs_updated']}, Schemas embedded: {len(dependent_schemas)}, Local removed: {len(local_base_entities)}")
                return True
            else:
                print(f"[INFO] No changes needed for {self.file_path}")
                return True

        except Exception as e:
            error_msg = f"Error migrating {self.file_path}: {e}"
            print(f"[ERROR] {error_msg}")
            self.stats['errors'].append(error_msg)
            return False

    def _replace_local_references(self, content: str) -> str:
        """Заменить ссылки на локальные BASE-ENTITY на глобальные."""
        for local_entity, global_ref in self.LOCAL_BASE_ENTITIES.items():
            # Паттерны для разных форматов ссылок (с кавычками и без)
            patterns = [
                (rf'\$ref:\s*\'#/components/schemas/{re.escape(local_entity)}\'', f"$ref: '{global_ref}'"),
                (rf'\$ref:\s*"#/components/schemas/{re.escape(local_entity)}"', f'$ref: "{global_ref}"'),
                (rf'\$ref:\s*#?/components/schemas/{re.escape(local_entity)}', f'$ref: "{global_ref}"'),
            ]

            for pattern, replacement in patterns:
                matches = re.findall(pattern, content)
                if matches:
                    content = re.sub(pattern, replacement, content)
                    self.stats['refs_updated'] += len(matches)
                    print(f"[REF] Replaced {len(matches)} references to {local_entity} using pattern: {pattern}")

        return content

    def _analyze_all_refs(self, content: str) -> Set[str]:
        """Найти все ссылки на схемы в контенте."""
        refs = set()
        # Найти все $ref на компоненты
        ref_pattern = r'\$ref:\s*[\'"](#/components/schemas/[^\'"]+)[\'"]'
        matches = re.findall(ref_pattern, content)
        for match in matches:
            refs.add(match)
        return refs

    def _identify_local_base_entities(self, content: str) -> Set[str]:
        """Определить локальные BASE-ENTITY схемы."""
        try:
            spec = yaml.safe_load(content)
            if 'components' not in spec or 'schemas' not in spec['components']:
                return set()

            local_entities = set()
            schemas = spec['components']['schemas']

            for schema_name in self.LOCAL_BASE_ENTITIES.keys():
                if schema_name in schemas:
                    local_entities.add(schema_name)

            return local_entities
        except yaml.YAMLError:
            return set()

    def _find_dependent_schemas(self, content: str, local_entities: Set[str]) -> Set[str]:
        """Найти все схемы, которые transitively зависят от локальных BASE-ENTITY."""
        dependent_schemas = set()

        # Загрузить common-schemas.yaml для анализа зависимостей
        try:
            common_schemas_path = self.file_path.parent.parent / "common-schemas.yaml"
            with open(common_schemas_path, 'r', encoding='utf-8') as f:
                common_spec = yaml.safe_load(f)

            common_schemas = common_spec.get('components', {}).get('schemas', {})

            # Найти все схемы, которые ссылаются на локальные BASE-ENTITY
            for schema_name, schema_def in common_schemas.items():
                if isinstance(schema_def, dict):
                    schema_yaml = yaml.dump(schema_def)
                    for local_entity in local_entities:
                        if f'#/components/schemas/{local_entity}' in schema_yaml:
                            dependent_schemas.add(schema_name)
                            break

        except Exception as e:
            print(f"[WARNING] Could not analyze common-schemas.yaml dependencies: {e}")

        # Добавить сами локальные BASE-ENTITY для замены
        dependent_schemas.update(local_entities)

        return dependent_schemas

    def _embed_missing_base_entities(self, content: str, schemas_to_embed: Set[str]) -> str:
        """Встроить недостающие BASE-ENTITY схемы из common-schemas.yaml."""
        if not schemas_to_embed:
            return content

        try:
            spec = yaml.safe_load(content)
        except yaml.YAMLError:
            return content

        if 'components' not in spec:
            spec['components'] = {}
        if 'schemas' not in spec['components']:
            spec['components']['schemas'] = {}

        # Загрузить common-schemas.yaml
        try:
            common_schemas_path = self.file_path.parent.parent / "common-schemas.yaml"
            with open(common_schemas_path, 'r', encoding='utf-8') as f:
                common_spec = yaml.safe_load(f)

            common_schemas = common_spec.get('components', {}).get('schemas', {})

            existing_schemas = set(spec['components']['schemas'].keys())

            # Добавить недостающие схемы
            for schema_name in schemas_to_embed:
                if schema_name in common_schemas and schema_name not in existing_schemas:
                    spec['components']['schemas'][schema_name] = common_schemas[schema_name]
                    print(f"[EMBED] Added BASE-ENTITY schema: {schema_name}")

            # Конвертировать обратно в YAML
            return yaml.dump(spec, default_flow_style=False, allow_unicode=True, sort_keys=False)

        except Exception as e:
            print(f"[ERROR] Failed to embed BASE-ENTITY schemas: {e}")
            return content

    def _remove_local_definitions(self, content: str) -> str:
        """Удалить определения локальных BASE-ENTITY схем."""
        # Загрузить как YAML для точного удаления
        try:
            spec = yaml.safe_load(content)
        except yaml.YAMLError:
            return content  # Если не YAML, пропустить

        if 'components' not in spec or 'schemas' not in spec['components']:
            return content

        schemas = spec['components']['schemas']
        original_count = len(schemas)

        # Удалить локальные BASE-ENTITY
        for local_entity in self.LOCAL_BASE_ENTITIES.keys():
            if local_entity in schemas:
                del schemas[local_entity]
                self.stats['schemas_removed'] += 1
                print(f"[REMOVE] Removed local definition of {local_entity}")

        if len(schemas) < original_count:
            # Конвертировать обратно в YAML
            return yaml.dump(spec, default_flow_style=False, allow_unicode=True, sort_keys=False)
        else:
            return content

    def generate_report(self) -> str:
        """Генерация отчета о миграции."""
        report = []
        report.append("# 📊 ОТЧЕТ МИГРАЦИИ ЛОКАЛЬНЫХ BASE-ENTITY")
        report.append("")
        report.append(f"**Файл:** {self.file_path}")
        report.append(f"**Режим:** {'DRY RUN' if self.dry_run else 'EXECUTE'}")
        report.append("")

        report.append("## 📈 СТАТИСТИКА")
        report.append("")
        report.append(f"- **Ссылок обновлено:** {self.stats['refs_updated']}")
        report.append(f"- **Схем удалено:** {self.stats['schemas_removed']}")
        report.append(f"- **Ошибок:** {len(self.stats['errors'])}")
        report.append("")

        if self.stats['errors']:
            report.append("## ❌ ОШИБКИ")
            report.append("")
            for error in self.stats['errors']:
                report.append(f"- {error}")
            report.append("")

        report.append("## ✅ РЕЗУЛЬТАТ")
        report.append("")
        if self.dry_run:
            report.append("1. **Проверьте изменения** в dry-run режиме")
            report.append("2. **Запустите с --execute** для применения")
        else:
            report.append("1. **Запустите валидацию:** `python scripts/openapi/validate-migration.py`")
            report.append("2. **Создайте self-contained версию:** `python scripts/openapi/domain_self_containment.py`")
            report.append("3. **Сгенерируйте Go код:** `python scripts/generation/go_service_generator.py`")

        return "\n".join(report)


def main():
    parser = argparse.ArgumentParser(description='Миграция локальных BASE-ENTITY на глобальные')
    parser.add_argument('file_path', help='Путь к YAML файлу для миграции')
    parser.add_argument('--dry-run', action='store_true', help='Только анализ, без изменений')
    parser.add_argument('--execute', action='store_true', help='Выполнить миграцию')
    parser.add_argument('--output', '-o', default='base-entity-migration-report.md', help='Файл для отчета')

    args = parser.parse_args()

    if not (args.dry_run or args.execute):
        args.dry_run = True  # По умолчанию dry-run

    migrator = LocalBaseEntityMigrator(args.file_path, dry_run=args.dry_run)
    success = migrator.migrate()

    # Генерация и сохранение отчета
    report = migrator.generate_report()
    with open(args.output, 'w', encoding='utf-8') as f:
        f.write(report)

    print(f"[REPORT] Report saved to: {args.output}")

    if not success:
        exit(1)


if __name__ == '__main__':
    main()
