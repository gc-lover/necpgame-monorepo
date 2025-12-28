#!/usr/bin/env python3
"""
OpenAPI Domain Structure Analyzer

Анализирует структуру доменов и предлагает план миграции к стандарту.

Использование:
    python scripts/analyze-domain-structure.py [domain-name] [--all] [--output FILE]

Аргументы:
    domain-name    : Анализировать конкретный домен
    --all         : Анализировать все домены
    --output      : Сохранить отчет в файл

Примеры:
    python scripts/analyze-domain-structure.py social-domain
    python scripts/analyze-domain-structure.py --all --output domain-analysis.json
"""

import os
import sys
import json
import argparse
from pathlib import Path
from typing import Dict, List, Any, Optional
from dataclasses import dataclass, asdict


@dataclass
class DomainAnalysis:
    """Результат анализа домена"""
    domain_name: str
    total_files: int
    yaml_files: int
    structure_score: int  # 0-100, насколько соответствует стандарту
    issues: List[str]
    recommendations: List[str]
    current_structure: Dict[str, Any]
    target_structure: Dict[str, Any]
    migration_complexity: str  # "low", "medium", "high"


class DomainStructureAnalyzer:
    """Анализатор структуры доменов"""

    def __init__(self, openapi_root: str = "proto/openapi"):
        self.openapi_root = Path(openapi_root)
        self.standard_structure = {
            "main.yaml": "required",
            "domain-config.yaml": "required",
            "README.md": "required",
            "services/": {
                "level": 2,
                "content": "service directories"
            },
            "schemas/": {
                "level": 2,
                "subdirs": ["entities/", "common/", "enums/"]
            }
        }

    def analyze_domain(self, domain_name: str) -> DomainAnalysis:
        """Анализирует структуру конкретного домена"""
        domain_path = self.openapi_root / domain_name

        if not domain_path.exists():
            raise ValueError(f"Domain {domain_name} does not exist")

        analysis = DomainAnalysis(
            domain_name=domain_name,
            total_files=0,
            yaml_files=0,
            structure_score=0,
            issues=[],
            recommendations=[],
            current_structure={},
            target_structure={},
            migration_complexity="low"
        )

        # Сканируем структуру
        self._scan_directory_structure(domain_path, analysis)

        # Оцениваем соответствие стандарту
        self._evaluate_structure_compliance(analysis)

        # Генерируем рекомендации
        self._generate_recommendations(analysis)

        return analysis

    def _scan_directory_structure(self, domain_path: Path, analysis: DomainAnalysis) -> None:
        """Сканирует структуру директории домена"""
        structure = {}

        for root, dirs, files in os.walk(domain_path):
            root_path = Path(root)
            rel_path = root_path.relative_to(domain_path)

            # Считаем файлы
            analysis.total_files += len(files)
            analysis.yaml_files += len([f for f in files if f.endswith('.yaml') or f.endswith('.yml')])

            # Анализируем структуру
            level = len(rel_path.parts) if rel_path != Path('.') else 0
            if level not in structure:
                structure[level] = []

            dir_info = {
                "path": str(rel_path),
                "dirs": sorted(dirs),
                "files": sorted([f for f in files if f.endswith(('.yaml', '.yml', '.md'))]),
                "other_files": sorted([f for f in files if not f.endswith(('.yaml', '.yml', '.md'))])
            }

            structure[level].append(dir_info)

        analysis.current_structure = structure

    def _evaluate_structure_compliance(self, analysis: DomainAnalysis) -> None:
        """Оценивает соответствие структуры стандарту"""
        structure = analysis.current_structure
        issues = analysis.issues

        score = 100  # Начальный балл

        # Проверяем наличие обязательных файлов
        root_files = []
        for level_items in structure.values():
            for item in level_items:
                if item["path"] == ".":
                    root_files = item["files"]
                    break

        required_files = ["main.yaml", "README.md"]
        for req_file in required_files:
            if req_file not in root_files:
                issues.append(f"Missing required file: {req_file}")
                score -= 20

        # Проверяем наличие services/ и schemas/
        has_services = any("services/" in item["path"] for level_items in structure.values() for item in level_items)
        has_schemas = any("schemas/" in item["path"] for level_items in structure.values() for item in level_items)

        if not has_services:
            issues.append("Missing services/ directory")
            score -= 15

        if not has_schemas:
            issues.append("Missing schemas/ directory")
            score -= 15

        # Проверяем уровень вложенности
        max_level = max(structure.keys()) if structure else 0
        if max_level > 4:
            issues.append(f"Too deep nesting: {max_level} levels (max recommended: 4)")
            score -= 10

        # Проверяем несогласованную структуру
        if len(structure.get(1, [])) > 2:
            issues.append("Inconsistent directory structure at level 1")
            score -= 10

        # Определяем сложность миграции
        if score < 50:
            analysis.migration_complexity = "high"
        elif score < 80:
            analysis.migration_complexity = "medium"
        else:
            analysis.migration_complexity = "low"

        analysis.structure_score = max(0, score)

    def _generate_recommendations(self, analysis: DomainAnalysis) -> None:
        """Генерирует рекомендации по миграции"""
        recommendations = analysis.recommendations

        if analysis.structure_score < 70:
            recommendations.append("Complete restructure required - follow DOMAIN_STRUCTURE_STANDARD.md")

        if not any("services/" in item["path"] for level_items in analysis.current_structure.values() for item in level_items):
            recommendations.append("Create services/ directory with service subdirectories")

        if not any("schemas/" in item["path"] for level_items in analysis.current_structure.values() for item in level_items):
            recommendations.append("Create schemas/ directory with entities/, common/, enums/ subdirectories")

        missing_files = []
        root_files = []
        for level_items in analysis.current_structure.values():
            for item in level_items:
                if item["path"] == ".":
                    root_files = item["files"]
                    break

        if "main.yaml" not in root_files:
            missing_files.append("main.yaml")
        if "README.md" not in root_files:
            missing_files.append("README.md")

        if missing_files:
            recommendations.append(f"Create missing required files: {', '.join(missing_files)}")

        if analysis.migration_complexity == "high":
            recommendations.append("Consider starting with a clean domain structure using example-domain as template")

    def analyze_all_domains(self) -> List[DomainAnalysis]:
        """Анализирует все домены"""
        analyses = []

        if not self.openapi_root.exists():
            raise ValueError(f"OpenAPI root does not exist: {self.openapi_root}")

        for item in self.openapi_root.iterdir():
            if item.is_dir() and not item.name.startswith('.'):
                try:
                    analysis = self.analyze_domain(item.name)
                    analyses.append(analysis)
                except Exception as e:
                    print(f"Error analyzing domain {item.name}: {e}")
                    continue

        return analyses

    def generate_report(self, analyses: List[DomainAnalysis]) -> Dict[str, Any]:
        """Генерирует общий отчет"""
        total_domains = len(analyses)
        avg_score = sum(a.structure_score for a in analyses) / total_domains if total_domains > 0 else 0

        complexity_counts = {
            "low": len([a for a in analyses if a.migration_complexity == "low"]),
            "medium": len([a for a in analyses if a.migration_complexity == "medium"]),
            "high": len([a for a in analyses if a.migration_complexity == "high"])
        }

        return {
            "summary": {
                "total_domains": total_domains,
                "average_structure_score": round(avg_score, 1),
                "migration_complexity_distribution": complexity_counts
            },
            "domains": [asdict(analysis) for analysis in analyses]
        }


def print_analysis_report(analysis: DomainAnalysis) -> None:
    """Печатает отчет анализа домена"""
    print(f"\n[DOMAIN] {analysis.domain_name}")
    print(f"Files: {analysis.total_files} total, {analysis.yaml_files} YAML")
    print(f"Structure Score: {analysis.structure_score}/100")
    print(f"Migration Complexity: {analysis.migration_complexity.upper()}")

    if analysis.issues:
        print(f"\n[ISSUES] ({len(analysis.issues)})")
        for issue in analysis.issues:
            print(f"  - {issue}")

    if analysis.recommendations:
        print(f"\n[RECOMMENDATIONS] ({len(analysis.recommendations)})")
        for rec in analysis.recommendations:
            print(f"  - {rec}")


def print_summary_report(report: Dict[str, Any]) -> None:
    """Печатает сводный отчет"""
    summary = report["summary"]
    print(f"\n{'='*60}")
    print(f"[SUMMARY REPORT]")
    print(f"{'='*60}")
    print(f"Total domains analyzed: {summary['total_domains']}")
    print(f"Average structure score: {summary['average_structure_score']}/100")

    complexity = summary["migration_complexity_distribution"]
    print(f"Migration complexity:")
    print(f"  Low: {complexity['low']}")
    print(f"  Medium: {complexity['medium']}")
    print(f"  High: {complexity['high']}")


def main():
    parser = argparse.ArgumentParser(
        description="OpenAPI Domain Structure Analyzer",
        formatter_class=argparse.RawDescriptionHelpFormatter,
        epilog="""
Examples:
  python scripts/analyze-domain-structure.py social-domain
  python scripts/analyze-domain-structure.py --all --output report.json
        """
    )

    parser.add_argument(
        "domain",
        nargs="?",
        help="Domain name to analyze"
    )

    parser.add_argument(
        "--all",
        action="store_true",
        help="Analyze all domains"
    )

    parser.add_argument(
        "--output",
        type=str,
        help="Save report to JSON file"
    )

    args = parser.parse_args()

    analyzer = DomainStructureAnalyzer()

    if args.all:
        # Анализируем все домены
        analyses = analyzer.analyze_all_domains()
        report = analyzer.generate_report(analyses)

        print_summary_report(report)

        for analysis in analyses:
            print_analysis_report(analysis)

        if args.output:
            with open(args.output, 'w', encoding='utf-8') as f:
                json.dump(report, f, indent=2, ensure_ascii=False)
            print(f"\nReport saved to: {args.output}")

    elif args.domain:
        # Анализируем конкретный домен
        try:
            analysis = analyzer.analyze_domain(args.domain)
            print_analysis_report(analysis)

            if args.output:
                with open(args.output, 'w', encoding='utf-8') as f:
                    json.dump(asdict(analysis), f, indent=2, ensure_ascii=False)
                print(f"\nReport saved to: {args.output}")

        except ValueError as e:
            print(f"[ERROR] {e}")
            sys.exit(1)

    else:
        parser.print_help()


if __name__ == "__main__":
    main()
