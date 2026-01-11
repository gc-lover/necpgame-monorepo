#!/usr/bin/env python3
"""
Скрипт валидации результатов миграции OpenAPI спецификаций.

Проверяет:
- Корректность $ref ссылок
- Валидность YAML синтаксиса
- Генерацию Go кода через ogen
- Соответствие стандартам структуры
- DRY compliance метрики

Использование:
    python scripts/openapi/validate-migration.py proto/openapi/companion-domain/
    python scripts/openapi/validate-migration.py proto/openapi/ --full-validation
"""

import os
import yaml
import json
import subprocess
import argparse
from pathlib import Path
from typing import Dict, List, Set, Tuple, Optional
from collections import defaultdict
import re

class MigrationValidator:
    """Валидатор результатов миграции."""

    def __init__(self, base_path: str = "proto/openapi"):
        self.base_path = Path(base_path)
        self.results = {
            'yaml_syntax_errors': [],
            'broken_refs': [],
            'missing_files': [],
            'structure_issues': [],
            'generation_errors': [],
            'dry_compliance': {},
            'performance_metrics': {}
        }

    def validate_domain_structure(self, domain_path: str) -> Dict[str, any]:
        """Полная валидация домена."""
        domain_path = Path(domain_path)

        print(f"[VALIDATE] Validating domain: {domain_path.name}")

        # 1. Проверка структуры директорий
        self._validate_directory_structure(domain_path)

        # 2. Проверка YAML синтаксиса
        self._validate_yaml_syntax(domain_path)

        # 3. Проверка $ref ссылок
        self._validate_references(domain_path)

        # 4. Проверка BASE-ENTITY использования
        self._validate_base_entity_usage(domain_path)

        # 5. Проверка генерации кода (опционально)
        self._validate_code_generation(domain_path)

        return self.results

    def _validate_directory_structure(self, domain_path: Path) -> None:
        """Проверка соответствия структуры стандарту."""
        expected_structure = {
            'services': domain_path / 'services',
            'schemas': domain_path / 'schemas',
            'entities': domain_path / 'schemas' / 'entities',
            'common': domain_path / 'schemas' / 'common',
            'enums': domain_path / 'schemas' / 'enums',
            'main_yaml': domain_path / 'main.yaml'
        }

        issues = []

        # Проверка обязательных директорий
        if not expected_structure['services'].exists():
            issues.append("Отсутствует директория services/")
        if not expected_structure['schemas'].exists():
            issues.append("Отсутствует директория schemas/")
        if not expected_structure['main_yaml'].exists():
            issues.append("Отсутствует main.yaml")

        # Проверка рекомендуемых директорий
        recommended = ['entities', 'common', 'enums']
        for rec_dir in recommended:
            if not expected_structure[rec_dir].exists():
                issues.append(f"Рекомендуется создать директорию schemas/{rec_dir}/")

        if issues:
            self.results['structure_issues'].extend(issues)

    def _validate_yaml_syntax(self, domain_path: Path) -> None:
        """Проверка YAML синтаксиса всех файлов."""
        for yaml_file in domain_path.rglob('*.yaml'):
            try:
                with open(yaml_file, 'r', encoding='utf-8') as f:
                    yaml.safe_load(f)
            except yaml.YAMLError as e:
                self.results['yaml_syntax_errors'].append(f"{yaml_file}: {e}")
            except Exception as e:
                self.results['yaml_syntax_errors'].append(f"{yaml_file}: {e}")

    def _validate_references(self, domain_path: Path) -> None:
        """Проверка корректности $ref ссылок."""
        for yaml_file in domain_path.rglob('*.yaml'):
            try:
                with open(yaml_file, 'r', encoding='utf-8') as f:
                    content = f.read()

                # Находим все $ref ссылки
                refs = re.findall(r'\$ref:\s*[\'"]([^\'"]+)[\'"]', content)

                for ref in refs:
                    if not self._validate_single_ref(ref, yaml_file, domain_path):
                        self.results['broken_refs'].append(f"{yaml_file}: {ref}")

            except Exception as e:
                self.results['broken_refs'].append(f"Ошибка обработки {yaml_file}: {e}")

    def _validate_single_ref(self, ref: str, source_file: Path, domain_path: Path) -> bool:
        """Проверка одной $ref ссылки."""
        # Убираем fragment (#/...)
        ref_path = ref.split('#')[0]

        if not ref_path:
            return True  # Локальная ссылка в том же файле

        # Преобразуем относительный путь в абсолютный
        if ref_path.startswith('../') or ref_path.startswith('./'):
            # Относительный путь - разрешаем относительно директории файла
            try:
                resolved_path = (source_file.parent / ref_path).resolve()
            except (OSError, RuntimeError):
                # Если resolve() не сработал, попробуем вручную
                resolved_path = Path(source_file).parent
                for part in ref_path.split('/'):
                    if part == '..':
                        resolved_path = resolved_path.parent
                    elif part and part != '.':
                        resolved_path = resolved_path / part
                resolved_path = resolved_path.resolve()
        elif ref_path.startswith('proto/openapi/'):
            # Абсолютный путь от корня проекта
            resolved_path = (self.base_path.parent.parent / ref_path).resolve()
        else:
            # Неизвестный формат
            return False

        # Проверяем существование файла
        if not resolved_path.exists():
            return False

        # Для YAML файлов проверяем, что это действительно YAML
        if resolved_path.suffix.lower() == '.yaml':
            try:
                with open(resolved_path, 'r', encoding='utf-8') as f:
                    yaml.safe_load(f)
            except:
                return False

        return True

    def _validate_base_entity_usage(self, domain_path: Path) -> None:
        """Проверка использования BASE-ENTITY."""
        dry_stats = {
            'total_entities': 0,
            'entities_using_base': 0,
            'avg_fields_per_entity': 0,
            'duplication_rate': 0
        }

        total_fields = 0
        entity_count = 0

        for yaml_file in domain_path.rglob('*.yaml'):
            try:
                with open(yaml_file, 'r', encoding='utf-8') as f:
                    content = yaml.safe_load(f)

                schemas = content.get('components', {}).get('schemas', {})
                for schema_name, schema_def in schemas.items():
                    if isinstance(schema_def, dict):
                        entity_count += 1

                        # Проверяем использование allOf с BASE-ENTITY
                        if 'allOf' in schema_def:
                            for item in schema_def['allOf']:
                                if isinstance(item, dict) and '$ref' in item:
                                    ref = item['$ref']
                                    if 'common-schemas.yaml' in ref:
                                        dry_stats['entities_using_base'] += 1
                                        break

                        # Считаем поля
                        if 'properties' in schema_def:
                            total_fields += len(schema_def['properties'])

            except Exception:
                continue

        if entity_count > 0:
            dry_stats['total_entities'] = entity_count
            dry_stats['avg_fields_per_entity'] = total_fields / entity_count

        self.results['dry_compliance'] = dry_stats

    def _validate_code_generation(self, domain_path: Path, run_generation: bool = False) -> None:
        """Проверка генерации Go кода."""
        if not run_generation:
            return

        # Создаем временную директорию для тестов
        test_gen_dir = self.base_path / 'test-gen'
        test_gen_dir.mkdir(exist_ok=True)

        try:
            # Пытаемся сгенерировать код для main.yaml домена
            main_yaml = domain_path / 'main.yaml'
            if main_yaml.exists():
                cmd = [
                    'ogen',
                    '--target', str(test_gen_dir),
                    '--package', 'api',
                    '--clean',
                    str(main_yaml)
                ]

                result = subprocess.run(cmd, capture_output=True, text=True, cwd=self.base_path)

                if result.returncode != 0:
                    self.results['generation_errors'].append(f"Оген генерация для {domain_path.name}: {result.stderr}")

        except FileNotFoundError:
            self.results['generation_errors'].append("ogen не найден. Установите: go install github.com/ogen-go/ogen/cmd/ogen@latest")
        except Exception as e:
            self.results['generation_errors'].append(f"Ошибка генерации кода: {e}")

    def run_full_validation(self, run_generation: bool = False) -> Dict[str, any]:
        """Полная валидация всех доменов."""
        print("[START] Starting full OpenAPI validation...")

        domains_path = self.base_path
        domain_results = {}

        # Находим все домены
        for item in domains_path.iterdir():
            if item.is_dir() and not item.name.startswith('.') and item.name != 'tools':
                domain_results[item.name] = self.validate_domain_structure(item)

                # Сбрасываем результаты для следующего домена
                self.results = {k: [] if isinstance(v, list) else {} for k, v in self.results.items()}

        # Агрегируем результаты
        aggregated = self._aggregate_results(domain_results)

        if run_generation:
            print("🔧 Проверка генерации Go кода...")
            for domain_name, domain_path in [(d, domains_path / d) for d in domain_results.keys()]:
                self._validate_code_generation(domain_path, True)

        return aggregated

    def _aggregate_results(self, domain_results: Dict[str, Dict]) -> Dict[str, any]:
        """Агрегация результатов по всем доменам."""
        aggregated = {
            'total_domains': len(domain_results),
            'domains_with_errors': 0,
            'total_yaml_errors': 0,
            'total_broken_refs': 0,
            'total_structure_issues': 0,
            'dry_compliance_summary': {},
            'domain_details': domain_results
        }

        for domain_name, results in domain_results.items():
            has_errors = any(len(v) > 0 for v in results.values() if isinstance(v, list))

            if has_errors:
                aggregated['domains_with_errors'] += 1

            aggregated['total_yaml_errors'] += len(results.get('yaml_syntax_errors', []))
            aggregated['total_broken_refs'] += len(results.get('broken_refs', []))
            aggregated['total_structure_issues'] += len(results.get('structure_issues', []))

        return aggregated

    def generate_report(self, results: Dict[str, any], output_file: str = 'validation-report.md') -> str:
        """Генерация отчета валидации."""
        report = []
        report.append("# 📊 ОТЧЕТ ВАЛИДАЦИИ МИГРАЦИИ OPENAPI")
        report.append("")

        if 'total_domains' in results:
            # Отчет по всем доменам
            report.append("## 🌐 СВОДНЫЙ ОТЧЕТ ПО ВСЕМ ДОМЕНАМ")
            report.append("")
            report.append(f"- **Всего доменов:** {results['total_domains']}")
            report.append(f"- **Доменов с ошибками:** {results['domains_with_errors']}")
            report.append(f"- **YAML ошибок:** {results['total_yaml_errors']}")
            report.append(f"- **Сломанных ссылок:** {results['total_broken_refs']}")
            report.append(f"- **Проблем структуры:** {results['total_structure_issues']}")
            report.append("")

            # Детали по доменам
            for domain_name, domain_results in results.get('domain_details', {}).items():
                report.append(f"### 🔍 Домен: {domain_name}")
                report.append("")

                # DRY compliance
                dry = domain_results.get('dry_compliance', {})
                if dry:
                    report.append("**DRY Compliance:**")
                    report.append(f"- Сущностей: {dry.get('total_entities', 0)}")
                    report.append(f"- Используют BASE-ENTITY: {dry.get('entities_using_base', 0)}")
                    report.append(".1f")
                    report.append("")

                # Ошибки
                errors = []
                for error_type, error_list in domain_results.items():
                    if isinstance(error_list, list) and error_list:
                        errors.extend([f"{error_type.upper()}: {e}" for e in error_list[:5]])  # Первые 5

                if errors:
                    report.append("**Ошибки:**")
                    for error in errors:
                        report.append(f"- {error}")
                    report.append("")

        else:
            # Отчет по одному домену
            report.append("## 🔍 РЕЗУЛЬТАТЫ ВАЛИДАЦИИ")
            report.append("")

            # Статистика
            report.append("### 📈 СТАТИСТИКА")
            report.append("")
            report.append(f"- **YAML синтаксис ошибок:** {len(results.get('yaml_syntax_errors', []))}")
            report.append(f"- **Сломанных ссылок:** {len(results.get('broken_refs', []))}")
            report.append(f"- **Проблем структуры:** {len(results.get('structure_issues', []))}")
            report.append("")

            # DRY compliance
            dry = results.get('dry_compliance', {})
            if dry:
                report.append("### 💧 DRY COMPLIANCE")
                report.append("")
                report.append(f"- **Всего сущностей:** {dry.get('total_entities', 0)}")
                report.append(f"- **Используют BASE-ENTITY:** {dry.get('entities_using_base', 0)}")
                report.append(".1f")
                report.append("")

            # Детальные ошибки
            error_sections = [
                ('yaml_syntax_errors', 'YAML СИНТАКСИС'),
                ('broken_refs', 'СЛОМАННЫЕ ССЫЛКИ'),
                ('structure_issues', 'ПРОБЛЕМЫ СТРУКТУРЫ'),
                ('generation_errors', 'ОШИБКИ ГЕНЕРАЦИИ')
            ]

            for error_key, section_title in error_sections:
                errors = results.get(error_key, [])
                if errors:
                    report.append(f"### ❌ {section_title}")
                    report.append("")
                    for error in errors[:20]:  # Показываем первые 20
                        report.append(f"- {error}")
                    if len(errors) > 20:
                        report.append(f"- ... и еще {len(errors) - 20} ошибок")
                    report.append("")

        # Рекомендации
        report.append("## 💡 РЕКОМЕНДАЦИИ")
        report.append("")

        total_errors = sum(len(v) for v in results.values() if isinstance(v, list))

        if total_errors == 0:
            report.append("✅ **Отлично!** Миграция прошла успешно. Все проверки пройдены.")
            report.append("")
            report.append("Следующие шаги:")
            report.append("1. Запустите генерацию кода для финальной проверки")
            report.append("2. Обновите документацию")
            report.append("3. Проведите интеграционное тестирование")
        else:
            report.append("⚠️  **Найдены проблемы.** Исправьте ошибки перед продолжением:")
            report.append("")
            report.append("1. **Исправьте YAML синтаксис** в указанных файлах")
            report.append("2. **Почините $ref ссылки** - проверьте пути")
            report.append("3. **Дополните структуру** доменов при необходимости")
            report.append("4. **Перезапустите валидацию** после исправлений")

        # Сохраняем отчет
        with open(output_file, 'w', encoding='utf-8') as f:
            f.write('\n'.join(report))

        print(f"[REPORT] Report saved to: {output_file}")
        return '\n'.join(report)


def main():
    parser = argparse.ArgumentParser(description='Валидация результатов миграции OpenAPI спецификаций')
    parser.add_argument('path', help='Путь к домену или директории для валидации')
    parser.add_argument('--full-validation', action='store_true', help='Валидация всех доменов')
    parser.add_argument('--run-generation', action='store_true', help='Запустить проверку генерации Go кода')
    parser.add_argument('--output', '-o', default='scripts/reports/validation-report.md', help='Файл для отчета')

    args = parser.parse_args()

    validator = MigrationValidator()

    if args.full_validation:
        results = validator.run_full_validation(args.run_generation)
    else:
        domain_path = args.path
        results = validator.validate_domain_structure(domain_path)

        if args.run_generation:
            validator._validate_code_generation(Path(domain_path), True)

    # Генерация отчета
    report = validator.generate_report(results, args.output)

    # Вывод краткой статистики в консоль
    print("\n[STATS] SUMMARY:")
    if 'total_domains' in results:
        print(f"   Доменов проверено: {results['total_domains']}")
        print(f"   Доменов с ошибками: {results['domains_with_errors']}")
        print(f"   Всего ошибок: {results['total_yaml_errors'] + results['total_broken_refs'] + results['total_structure_issues']}")
    else:
        total_errors = sum(len(v) for v in results.values() if isinstance(v, list))
        print(f"   Всего ошибок: {total_errors}")


if __name__ == '__main__':
    main()
