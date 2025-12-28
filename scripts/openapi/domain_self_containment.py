#!/usr/bin/env python3
"""
Domain Self-Containment Tool
Решение проблемы: External references через self-contained домены

Цель: Сделать каждый домен автономным, скопировав необходимые BASE-ENTITY
схемы локально. Это решает проблемы bundling и external refs.

Использование:
    python scripts/openapi/domain_self_containment.py companion-domain --embed-base-entity
"""

import os
import yaml
import argparse
from pathlib import Path
from typing import Dict, Any, Set, List, Optional
import shutil

class DomainSelfContainment:
    """Инструмент для создания self-contained доменов."""

    def __init__(self, common_schemas_path: Optional[str] = None):
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
                    print(f"[INIT] Found common-schemas.yaml at: {path}")
                    break
            else:
                self.common_schemas_path = script_dir / "proto" / "openapi" / "common-schemas.yaml"
                print(f"[INIT] Using default path for common-schemas.yaml: {self.common_schemas_path}")

        self.base_entity_map = self._load_base_entities()

    def _load_base_entities(self) -> Dict[str, Any]:
        """Загружаем BASE-ENTITY определения."""
        if not self.common_schemas_path.exists():
            print(f"[WARNING] common-schemas.yaml not found: {self.common_schemas_path}")
            return {}

        with open(self.common_schemas_path, 'r', encoding='utf-8') as f:
            content = yaml.safe_load(f)

        return content.get('components', {}).get('schemas', {})

    def make_domain_self_contained(self, domain_path: Path, embed_base_entity: bool = True) -> bool:
        """Делаем домен self-contained."""
        print(f"[SELF-CONTAIN] Processing domain: {domain_path.name}")

        main_yaml = domain_path / "main.yaml"
        if not main_yaml.exists():
            print(f"[ERROR] main.yaml not found in {domain_path}")
            return False

        # Анализируем какие BASE-ENTITY используются
        used_entities = self._analyze_used_entities(main_yaml)

        if not used_entities:
            print("[INFO] No BASE-ENTITY references found")
            return True

        if embed_base_entity:
            # Встраиваем BASE-ENTITY в домен
            return self._embed_base_entities(domain_path, main_yaml, used_entities)
        else:
            # Создаем локальную копию common-schemas
            return self._create_local_common_schemas(domain_path, used_entities)

    def _analyze_used_entities(self, main_yaml: Path) -> Set[str]:
        """Анализируем какие BASE-ENTITY используются в домене."""
        used_entities = set()

        with open(main_yaml, 'r', encoding='utf-8') as f:
            content = yaml.safe_load(f)

        # Ищем все $ref ссылки на common-schemas
        self._find_common_refs(content, used_entities)

        print(f"[ANALYZE] Found {len(used_entities)} external references: {used_entities}")
        return used_entities

    def _find_common_refs(self, obj: Any, used_entities: Set[str]) -> None:
        """Рекурсивно ищем ссылки на common-schemas и анализируем транзитивные зависимости."""
        if isinstance(obj, dict):
            for key, value in obj.items():
                if key == '$ref' and isinstance(value, str):
                    # Ищем ссылки на common-schemas.yaml
                    if 'common-schemas.yaml' in value:
                        # Извлекаем имя схемы из ссылки
                        parts = value.split('#/components/schemas/')
                        if len(parts) == 2:
                            schema_name = parts[1]
                            used_entities.add(schema_name)
                            # Добавляем транзитивные зависимости
                            self._add_transitive_dependencies(schema_name, used_entities)
                            print(f"[REF] Found external ref: {schema_name} in {value}")
                    # Также проверяем локальные ссылки на BASE-ENTITY (для уже мигрированных доменов)
                    elif value.startswith('#/components/schemas/'):
                        schema_name = value.split('#/components/schemas/')[1]
                        if schema_name in self.base_entity_map:
                            used_entities.add(schema_name)
                            # Добавляем транзитивные зависимости
                            self._add_transitive_dependencies(schema_name, used_entities)
                            print(f"[REF] Found local BASE-ENTITY ref: {schema_name}")
                elif key == 'allOf' and isinstance(value, list):
                    # Анализируем allOf конструкции для поиска зависимостей
                    for item in value:
                        if isinstance(item, dict) and '$ref' in item:
                            ref = item['$ref']
                            if 'common-schemas.yaml' in ref:
                                parts = ref.split('#/components/schemas/')
                                if len(parts) == 2:
                                    schema_name = parts[1]
                                    used_entities.add(schema_name)
                                    self._add_transitive_dependencies(schema_name, used_entities)
                                    print(f"[REF] Found external ref in allOf: {schema_name} in {ref}")
                            elif ref.startswith('#/components/schemas/'):
                                schema_name = ref.split('#/components/schemas/')[1]
                                if schema_name in self.base_entity_map:
                                    used_entities.add(schema_name)
                                    self._add_transitive_dependencies(schema_name, used_entities)
                                    print(f"[REF] Found local BASE-ENTITY ref in allOf: {schema_name}")
                else:
                    self._find_common_refs(value, used_entities)
        elif isinstance(obj, list):
            for item in obj:
                self._find_common_refs(item, used_entities)

    def _add_transitive_dependencies(self, schema_name: str, used_entities: Set[str]) -> None:
        """Добавляем транзитивные зависимости для BASE-ENTITY схемы."""
        if schema_name not in self.base_entity_map:
            return

        schema_def = self.base_entity_map[schema_name]

        # Анализируем allOf для поиска зависимостей
        if isinstance(schema_def, dict) and 'allOf' in schema_def:
            for item in schema_def['allOf']:
                if isinstance(item, dict) and '$ref' in item:
                    ref = item['$ref']
                    if ref.startswith('#/components/schemas/'):
                        dep_name = ref.split('#/components/schemas/')[1]
                        if dep_name in self.base_entity_map and dep_name not in used_entities:
                            used_entities.add(dep_name)
                            # Рекурсивно добавляем зависимости зависимостей
                            self._add_transitive_dependencies(dep_name, used_entities)

    def _embed_base_entities(self, domain_path: Path, main_yaml: Path, used_entities: Set[str]) -> bool:
        """Встраиваем BASE-ENTITY непосредственно в main.yaml домена."""
        print(f"[EMBED] Embedding {len(used_entities)} BASE-ENTITY schemas: {used_entities}")

        # Загружаем текущий main.yaml
        with open(main_yaml, 'r', encoding='utf-8') as f:
            content = yaml.safe_load(f)

        # Убеждаемся, что components.schemas существует
        if 'components' not in content:
            content['components'] = {}
        if 'schemas' not in content['components']:
            content['components']['schemas'] = {}

        existing_schemas = set(content['components']['schemas'].keys())
        print(f"[EMBED] Existing schemas: {len(existing_schemas)}")

        # Добавляем используемые BASE-ENTITY
        schemas_added = 0
        for entity_name in used_entities:
            if entity_name in self.base_entity_map:
                if entity_name not in content['components']['schemas']:
                    content['components']['schemas'][entity_name] = self.base_entity_map[entity_name]
                    schemas_added += 1
                    print(f"[EMBED] Added schema: {entity_name}")
                else:
                    print(f"[EMBED] Schema already exists: {entity_name}")

        print(f"[EMBED] Total schemas added: {schemas_added}")

        # Всегда обновляем ссылки, независимо от того, добавлены ли новые схемы
        if schemas_added > 0:
            # Сохраняем файл с новыми схемами
            with open(main_yaml, 'w', encoding='utf-8') as f:
                yaml.dump(content, f, default_flow_style=False, allow_unicode=True)

        # Обновляем ссылки простым текстовым поиском
        print(f"[EMBED] Updating {len(used_entities)} references: {used_entities}")
        self._update_refs_in_file(main_yaml, used_entities)

        if schemas_added > 0:
            print(f"[EMBED] Added {schemas_added} schemas to {main_yaml}")

        return True

    def _update_refs_in_file(self, main_yaml: Path, used_entities: Set[str]) -> None:
        """Обновляем ссылки в файле простым текстовым поиском."""
        with open(main_yaml, 'r', encoding='utf-8') as f:
            content = f.read()

        original_content = content

        # Заменяем каждую ссылку
        for entity_name in used_entities:
            old_ref = f'../../common-schemas.yaml#/components/schemas/{entity_name}'
            new_ref = f'#/components/schemas/{entity_name}'
            content = content.replace(old_ref, new_ref)

        if content != original_content:
            with open(main_yaml, 'w', encoding='utf-8') as f:
                f.write(content)
            print(f"[UPDATE] Updated references in {main_yaml}")
        else:
            print("[UPDATE] No references to update")

    def _create_local_common_schemas(self, domain_path: Path, used_entities: Set[str]) -> bool:
        """Создаем локальную копию common-schemas.yaml в домене."""
        local_common = domain_path / "common-schemas.yaml"

        # Создаем подмножество common-schemas с только используемыми сущностями
        local_content = {
            'openapi': '3.0.3',
            'info': {
                'title': f'{domain_path.name} Common Schemas',
                'version': '1.0.0',
                'description': 'Local copy of BASE-ENTITY schemas for this domain'
            },
            'components': {
                'schemas': {}
            }
        }

        # Добавляем только используемые схемы
        for entity_name in used_entities:
            if entity_name in self.base_entity_map:
                local_content['components']['schemas'][entity_name] = self.base_entity_map[entity_name]

        # Сохраняем локальную копию
        with open(local_common, 'w', encoding='utf-8') as f:
            yaml.dump(local_content, f, default_flow_style=False, allow_unicode=True)

        print(f"[LOCAL] Created local common-schemas.yaml with {len(used_entities)} schemas")

        # Обновляем ссылки в main.yaml на локальную копию
        return self._update_refs_to_local(domain_path / "main.yaml", used_entities)

    def _update_refs_to_local(self, main_yaml: Path, used_entities: Set[str]) -> bool:
        """Обновляем $ref ссылки на локальную common-schemas.yaml."""
        with open(main_yaml, 'r', encoding='utf-8') as f:
            content = f.read()

        # Заменяем ссылки
        for entity_name in used_entities:
            old_ref = f'../../common-schemas.yaml#/components/schemas/{entity_name}'
            new_ref = f'common-schemas.yaml#/components/schemas/{entity_name}'

            content = content.replace(old_ref, new_ref)

        # Сохраняем обновленный файл
        with open(main_yaml, 'w', encoding='utf-8') as f:
            f.write(content)

        print(f"[UPDATE] Updated references to local common-schemas.yaml")
        return True

    def validate_self_containment(self, domain_path: Path) -> bool:
        """Проверяем, что домен стал self-contained."""
        main_yaml = domain_path / "main.yaml"

        with open(main_yaml, 'r', encoding='utf-8') as f:
            content = yaml.safe_load(f)

        # Проверяем, что нет external ссылок на common-schemas
        external_refs = []
        self._find_external_refs(content, external_refs)

        if external_refs:
            print(f"[VALIDATE] Still has {len(external_refs)} external references:")
            for ref in external_refs[:5]:  # Показываем первые 5
                print(f"  - {ref}")
            return False

        print("[VALIDATE] Domain is now self-contained")
        return True

    def _find_external_refs(self, obj: Any, external_refs: List[str]) -> None:
        """Ищем external ссылки на common-schemas."""
        if isinstance(obj, dict):
            for key, value in obj.items():
                if key == '$ref' and isinstance(value, str):
                    if value.startswith('../') and 'common-schemas.yaml' in value:
                        external_refs.append(value)
                else:
                    self._find_external_refs(value, external_refs)
        elif isinstance(obj, list):
            for item in obj:
                self._find_external_refs(item, external_refs)


def main():
    parser = argparse.ArgumentParser(description='Make OpenAPI domains self-contained')
    parser.add_argument('domain', help='Domain name (e.g., companion-domain)')
    parser.add_argument('--embed-base-entity', action='store_true',
                       help='Embed BASE-ENTITY directly into main.yaml')
    parser.add_argument('--create-local', action='store_true',
                       help='Create local common-schemas.yaml')
    parser.add_argument('--validate', action='store_true',
                       help='Validate self-containment after processing')

    args = parser.parse_args()

    domain_path = Path("proto/openapi") / args.domain

    if not domain_path.exists():
        print(f"[ERROR] Domain not found: {domain_path}")
        return 1

    tool = DomainSelfContainment()

    # Выбираем стратегию
    embed = args.embed_base_entity
    if not embed and not args.create_local:
        # По умолчанию embed
        embed = True

    try:
        success = tool.make_domain_self_contained(domain_path, embed_base_entity=embed)

        if success and args.validate:
            success = tool.validate_self_containment(domain_path)

        if success:
            print(f"[SUCCESS] Domain {args.domain} is now self-contained")
            return 0
        else:
            print(f"[ERROR] Failed to make domain {args.domain} self-contained")
            return 1

    except Exception as e:
        print(f"[ERROR] {e}")
        return 1


if __name__ == '__main__':
    exit(main())
