#!/usr/bin/env python3
"""
Скрипт для создания GitHub Issues из документа github-issues-pending.yaml

Использование:
    python scripts/create-github-issues.py [--dry-run] [--category CATEGORY]

Аргументы:
    --dry-run    - показать что будет создано без создания
    --category   - создать только указанную категорию (america_quests, europe_quests, etc.)
"""

import argparse
import sys
import yaml
from pathlib import Path
from typing import Dict, List, Any


def load_pending_issues() -> Dict[str, Any]:
    """Загрузить документ с pending issues"""
    issues_file = Path(__file__).parent.parent / "knowledge/analysis/tasks/github-issues-pending.yaml"

    if not issues_file.exists():
        print(f"Файл {issues_file} не найден")
        sys.exit(1)

    with open(issues_file, 'r', encoding='utf-8') as f:
        return yaml.safe_load(f)


def parse_issue_groups(data: Dict[str, Any]) -> Dict[str, Dict[str, Any]]:
    """Распарсить группы issues из документа"""
    groups = {}

    for section in data.get('content', {}).get('sections', []):
        section_id = section.get('id')
        title = section.get('title')
        body = section.get('body', '')

        groups[section_id] = {
            'title': title,
            'body': body,
            'issues': parse_issues_from_body(body)
        }

    return groups


def parse_issues_from_body(body: str) -> List[Dict[str, Any]]:
    """Распарсить issues из тела секции"""
    issues = []
    lines = body.split('\n')

    current_issue = None
    current_files = []
    current_priority = ""
    current_labels = ""

    for line in lines:
        line = line.strip()
        if not line:
            continue

        # Начало нового issue
        if line.startswith('## Issue:'):
            # Сохранить предыдущий issue если есть
            if current_issue:
                issues.append({
                    'title': current_issue,
                    'files': current_files,
                    'priority': current_priority,
                    'labels': current_labels
                })

            # Начать новый issue
            current_issue = line.replace('## Issue:', '').strip()
            current_files = []
            current_priority = ""
            current_labels = ""

        # Файлы issue
        elif line.startswith('- knowledge/'):
            current_files.append(line[2:].strip())

        # Приоритет и метки
        elif line.startswith('Приоритет:') or line.startswith('Priority:'):
            current_priority = line.split(':', 1)[1].strip()

        elif line.startswith('Метки:') or line.startswith('Labels:'):
            current_labels = line.split(':', 1)[1].strip()

    # Сохранить последний issue
    if current_issue:
        issues.append({
            'title': current_issue,
            'files': current_files,
            'priority': current_priority,
            'labels': current_labels
        })

    return issues


def create_github_issue(title: str, body: str, labels: List[str] = None) -> bool:
    """Создать GitHub Issue (заглушка для dry-run)"""
    print(f"Создание Issue: {title}")
    if labels:
        print(f"   Labels: {', '.join(labels)}")
    print(f"   Body: {body[:100]}...")
    print()
    return True


def format_issue_body(issue_data: Dict[str, Any], section_title: str) -> str:
    """Форматировать тело Issue"""
    title = issue_data['title']
    files = issue_data['files']

    body = f"""## {title}

**Категория:** {section_title}

### Задачи:
"""

    for file_path in files:
        body += f"- [ ] {file_path}\n"

    body += """
### Требования:
- [ ] Проанализировать все файлы в списке
- [ ] Определить зависимости и blockers
- [ ] Создать план реализации
- [ ] Назначить ответственного агента

### Критерии готовности:
- [ ] Все файлы обработаны
- [ ] Зависимости разрешены
- [ ] План реализации согласован
- [ ] Задача передана следующему агенту

---
*Автоматически создано из github-issues-pending.yaml*
"""

    return body


def get_labels_from_string(labels_str: str) -> List[str]:
    """Преобразовать строку меток в список"""
    if not labels_str:
        return []

    # Разделить по запятой и очистить
    labels = [label.strip() for label in labels_str.split(',')]
    return [label for label in labels if label]


def main():
    parser = argparse.ArgumentParser(description='Создать GitHub Issues из pending документа')
    parser.add_argument('--dry-run', action='store_true', help='Показать что будет создано без создания')
    parser.add_argument('--category', help='Создать только указанную категорию')

    args = parser.parse_args()

    print("Загрузка документа github-issues-pending.yaml...")

    # Загрузить данные
    data = load_pending_issues()
    groups = parse_issue_groups(data)

    print(f"Найдено {len(groups)} категорий задач")

    # Фильтровать по категории если указано
    if args.category:
        if args.category not in groups:
            print(f"Категория '{args.category}' не найдена. Доступные: {', '.join(groups.keys())}")
            sys.exit(1)
        groups = {args.category: groups[args.category]}

    total_issues = sum(len(group['issues']) for group in groups.values())
    print(f"Всего Issues для создания: {total_issues}")

    if args.dry_run:
        print("\nDRY RUN - показываю что будет создано:\n")

    created_count = 0

    # Создать issues по категориям
    for section_id, section_data in groups.items():
        section_title = section_data['title']
        issues = section_data['issues']

        print(f"\nКатегория: {section_title} ({len(issues)} issues)")

        for issue_data in issues:
            title = issue_data['title']
            labels_str = issue_data.get('labels', '')
            labels = get_labels_from_string(labels_str)

            # Добавить приоритет в метки
            priority = issue_data.get('priority', '')
            if priority and priority not in labels:
                labels.append(priority)

            body = format_issue_body(issue_data, section_title)

            if args.dry_run:
                print(f"  Issue: {title}")
                if labels:
                    print(f"     Labels: {', '.join(labels)}")
            else:
                if create_github_issue(title, body, labels):
                    created_count += 1

    if args.dry_run:
        print("\nDry run завершен. Используйте без --dry-run для реального создания.")
    else:
        print(f"\nСоздано {created_count} Issues")


if __name__ == '__main__':
    main()
