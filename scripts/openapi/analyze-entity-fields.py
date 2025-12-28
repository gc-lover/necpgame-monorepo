#!/usr/bin/env python3
"""
Анализатор полей сущностей в OpenAPI спецификациях.

Цель: Найти дублированные поля для миграции на BASE-ENTITY систему.

Использование:
    python scripts/openapi/analyze-entity-fields.py proto/openapi/social-domain/
    python scripts/openapi/analyze-entity-fields.py proto/openapi/ --all-domains
"""

import os
import yaml
import json
import argparse
from collections import defaultdict, Counter
from pathlib import Path
from typing import Dict, List, Set, Tuple

class EntityFieldAnalyzer:
    """Анализатор полей сущностей для DRY compliance."""

    def __init__(self):
        self.field_usage = defaultdict(lambda: defaultdict(int))  # field -> entity -> count
        self.entity_fields = defaultdict(set)  # entity -> set of fields
        self.common_fields = set()
        self.duplicate_patterns = defaultdict(list)

    def analyze_file(self, file_path: str) -> None:
        """Анализ одного YAML файла."""
        try:
            with open(file_path, 'r', encoding='utf-8') as f:
                content = yaml.safe_load(f)

            self._extract_schemas(content, file_path)

        except Exception as e:
            print(f"Ошибка обработки {file_path}: {e}")

    def _extract_schemas(self, content: dict, file_path: str) -> None:
        """Извлечение схем из OpenAPI файла."""
        if not isinstance(content, dict):
            return

        # Ищем components.schemas
        components = content.get('components', {})
        schemas = components.get('schemas', {})

        for schema_name, schema_def in schemas.items():
            if isinstance(schema_def, dict) and 'properties' in schema_def:
                self._analyze_entity_properties(schema_name, schema_def, file_path)

    def _analyze_entity_properties(self, entity_name: str, schema: dict, file_path: str) -> None:
        """Анализ свойств сущности."""
        properties = schema.get('properties', {})

        for field_name, field_def in properties.items():
            self.field_usage[field_name][entity_name] += 1
            self.entity_fields[entity_name].add(field_name)

    def find_common_fields(self, min_occurrences: int = 3) -> Set[str]:
        """Найти часто повторяющиеся поля."""
        common_fields = set()

        for field, entities in self.field_usage.items():
            if len(entities) >= min_occurrences:
                common_fields.add(field)

        self.common_fields = common_fields
        return common_fields

    def detect_duplicate_patterns(self) -> Dict[str, List[str]]:
        """Найти паттерны дублирования."""
        patterns = defaultdict(list)

        # Группировка по набору полей
        field_sets = defaultdict(list)
        for entity, fields in self.entity_fields.items():
            field_key = frozenset(fields)
            field_sets[field_key].append(entity)

        # Паттерны с повторяющимися наборами полей
        for field_set, entities in field_sets.items():
            if len(entities) > 1 and len(field_set) > 3:  # Минимум 3 поля и 2 сущности
                pattern_key = f"pattern_{len(field_set)}_fields"
                patterns[pattern_key] = entities

        self.duplicate_patterns = patterns
        return patterns

    def calculate_dry_metrics(self) -> Dict[str, float]:
        """Расчет метрик DRY compliance."""
        total_fields = sum(len(fields) for fields in self.entity_fields.values())
        unique_fields = len(set().union(*self.entity_fields.values()))
        duplicate_fields = total_fields - unique_fields

        # Процент дублирования
        duplication_rate = (duplicate_fields / total_fields * 100) if total_fields > 0 else 0

        # Среднее количество полей на сущность
        avg_fields_per_entity = total_fields / len(self.entity_fields) if self.entity_fields else 0

        return {
            'total_fields': total_fields,
            'unique_fields': unique_fields,
            'duplicate_fields': duplicate_fields,
            'duplication_rate_percent': duplication_rate,
            'avg_fields_per_entity': avg_fields_per_entity,
            'entities_count': len(self.entity_fields),
            'common_fields_count': len(self.common_fields)
        }

    def generate_report(self) -> str:
        """Генерация отчета анализа."""
        metrics = self.calculate_dry_metrics()
        self.find_common_fields()
        self.detect_duplicate_patterns()

        report = []
        report.append("# 📊 АНАЛИЗ ПОЛЕЙ СУЩНОСТЕЙ - DRY COMPLIANCE REPORT")
        report.append("")
        report.append("## 📈 МЕТРИКИ ДУБЛИРОВАНИЯ")
        report.append("")
        report.append(f"- **Всего полей:** {metrics['total_fields']}")
        report.append(f"- **Уникальных полей:** {metrics['unique_fields']}")
        report.append(f"- **Дублированных полей:** {metrics['duplicate_fields']}")
        report.append(".1f")
        report.append(".1f")
        report.append(f"- **Количество сущностей:** {metrics['entities_count']}")
        report.append(f"- **Общих полей (3+ использования):** {metrics['common_fields_count']}")
        report.append("")

        if self.common_fields:
            report.append("## 🔍 НАИБОЛЕЕ ЧАСТО ИСПОЛЬЗУЕМЫЕ ПОЛЯ")
            report.append("")
            field_counts = [(field, len(entities)) for field, entities in self.field_usage.items()]
            field_counts.sort(key=lambda x: x[1], reverse=True)

            for field, count in field_counts[:20]:  # Топ 20
                entities = list(self.field_usage[field].keys())
                report.append(f"- `{field}`: используется в {count} сущностях")
                if len(entities) <= 5:
                    report.append(f"  - Сущности: {', '.join(entities)}")
                else:
                    report.append(f"  - Сущности: {', '.join(entities[:3])}, ... (+{len(entities)-3})")
            report.append("")

        if self.duplicate_patterns:
            report.append("## 🎯 ПАТТЕРНЫ ДУБЛИРОВАНИЯ")
            report.append("")
            for pattern, entities in list(self.duplicate_patterns.items())[:10]:  # Топ 10 паттернов
                report.append(f"- **{pattern}**: {len(entities)} сущностей")
                report.append(f"  - Сущности: {', '.join(entities)}")
            report.append("")

        report.append("## 💡 РЕКОМЕНДАЦИИ ДЛЯ BASE-ENTITY")
        report.append("")
        report.append("### Предлагаемые BASE-ENTITY компоненты:")
        report.append("")
        base_entity_candidates = self._analyze_base_entity_candidates()
        if base_entity_candidates:
            for candidate in base_entity_candidates[:5]:
                report.append(f"- **{candidate['name']}**: {candidate['entities']} сущностей")
                report.append(f"  - Поля: {', '.join(list(candidate['fields'])[:10])}")
                if len(candidate['fields']) > 10:
                    report.append(f"    ... (+{len(candidate['fields'])-10} полей)")
        report.append("")
        report.append("### Следующие шаги:")
        report.append("1. Создать BASE-ENTITY компоненты на основе анализа")
        report.append("2. Миграция сущностей на allOf композицию")
        report.append("3. Обновление $ref ссылок")
        report.append("4. Валидация через ogen")

        return "\n".join(report)

    def _analyze_base_entity_candidates(self) -> List[Dict]:
        """Анализ кандидатов для BASE-ENTITY."""
        candidates = []

        # Группировка по общим полям
        field_combinations = defaultdict(set)

        for entity, fields in self.entity_fields.items():
            # Находим комбинации полей, которые встречаются часто
            common_entity_fields = fields.intersection(self.common_fields)
            if len(common_entity_fields) >= 3:  # Минимум 3 общих поля
                key = frozenset(common_entity_fields)
                field_combinations[key].add(entity)

        # Формируем кандидатов
        for field_set, entities in field_combinations.items():
            if len(entities) >= 2:  # Минимум 2 сущности
                candidates.append({
                    'name': f"BaseEntity_{len(field_set)}_fields",
                    'fields': field_set,
                    'entities': list(entities),
                    'entity_count': len(entities),
                    'field_count': len(field_set)
                })

        # Сортировка по количеству сущностей и полей
        candidates.sort(key=lambda x: (x['entity_count'], x['field_count']), reverse=True)

        return candidates


def main():
    parser = argparse.ArgumentParser(description='Анализ полей сущностей OpenAPI для DRY compliance')
    parser.add_argument('path', help='Путь к домену или директории с YAML файлами')
    parser.add_argument('--all-domains', action='store_true', help='Анализировать все домены')
    parser.add_argument('--output', '-o', default='scripts/reports/entity-analysis-report.md', help='Файл для отчета')
    parser.add_argument('--min-occurrences', type=int, default=3, help='Минимум вхождений для common fields')

    args = parser.parse_args()

    analyzer = EntityFieldAnalyzer()

    if args.all_domains:
        # Анализ всех доменов
        domains_path = Path(args.path)
        if domains_path.exists():
            for domain_dir in domains_path.iterdir():
                if domain_dir.is_dir() and not domain_dir.name.startswith('.'):
                    print(f"Анализ домена: {domain_dir.name}")
                    for yaml_file in domain_dir.rglob('*.yaml'):
                        analyzer.analyze_file(str(yaml_file))
    else:
        # Анализ конкретного пути
        path = Path(args.path)
        if path.is_dir():
            for yaml_file in path.rglob('*.yaml'):
                analyzer.analyze_file(str(yaml_file))
        elif path.is_file():
            analyzer.analyze_file(str(path))

    # Генерация отчета
    report = analyzer.generate_report()

    # Сохранение отчета
    with open(args.output, 'w', encoding='utf-8') as f:
        f.write(report)

    print(f"[OK] Analysis completed. Report saved to: {args.output}")

    # Вывод основных метрик в консоль
    metrics = analyzer.calculate_dry_metrics()
    print("\n[INFO] MAIN METRICS:")
    print(f"   Total fields: {metrics['total_fields']}")
    print(".1f")
    print(f"   Unique fields: {metrics['unique_fields']}")
    print(f"   Duplicate fields: {metrics['duplicate_fields']}")
    print(f"   Entities: {metrics['entities_count']}")


if __name__ == '__main__':
    main()
