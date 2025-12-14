#!/usr/bin/env python3
"""
Простой скрипт для анализа github-issues-pending.yaml

Использование:
    python scripts/create-github-issues-simple.py
"""

import re
from pathlib import Path

def analyze_pending_issues():
    """Проанализировать документ с pending issues"""
    issues_file = Path(__file__).parent.parent / "knowledge/analysis/tasks/github-issues-pending.yaml"

    if not issues_file.exists():
        print(f"Файл {issues_file} не найден")
        return

    print("Анализ файла github-issues-pending.yaml...")
    print("=" * 50)

    with open(issues_file, 'r', encoding='utf-8') as f:
        content = f.read()

    # Найти все секции
    sections = re.findall(r'- id: (\w+)\s+title: ([^\n]+)', content)

    print(f"Найдено {len(sections)} секций:")
    for section_id, title in sections:
        print(f"  - {section_id}: {title}")

    # Подсчитать количество issues в каждой секции
    issue_counts = {}
    for section_id, _ in sections:
        # Найти все ## Issue: в секции
        section_pattern = rf'- id: {section_id}[^#]*(.*?)(?=- id: |\Z)'
        section_match = re.search(section_pattern, content, re.DOTALL)

        if section_match:
            section_content = section_match.group(1)
            issue_count = len(re.findall(r'## Issue:', section_content))
            issue_counts[section_id] = issue_count

    print("\nКоличество Issues по секциям:")
    total_issues = 0
    for section_id, count in issue_counts.items():
        section_title = next(title for sid, title in sections if sid == section_id)
        print(f"  - {section_title}: {count} issues")
        total_issues += count

    print(f"\nВсего Issues для создания: {total_issues}")

    # Показать примеры issues
    print("\nПримеры Issues:")
    issue_matches = re.findall(r'## Issue: ([^\n]+)', content)
    for i, issue in enumerate(issue_matches[:5]):  # Показать первые 5
        print(f"  {i+1}. {issue.strip()}")

    if len(issue_matches) > 5:
        print(f"  ... и ещё {len(issue_matches) - 5} issues")

def main():
    analyze_pending_issues()

if __name__ == '__main__':
    main()