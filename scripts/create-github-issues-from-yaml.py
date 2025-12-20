#!/usr/bin/env python3
"""
Скрипт для создания GitHub Issues из github-issues-pending.yaml

Создает Issues по категориям с правильными метками и описаниями.
"""

import json
import re
from pathlib import Path
from typing import Dict, List, Tuple


def parse_yaml_sections(content: str) -> Dict[str, Dict]:
    """Распарсить секции из YAML файла"""
    sections = {}

    # Найти все секции
    section_pattern = r'- id: (\w+)\s+title: ([^\n]+).*?body:\s*\|.*?(\n.*?)(?=- id:|\Z)'
    matches = re.finditer(section_pattern, content, re.DOTALL)

    for match in matches:
        section_id = match.group(1)
        title = match.group(2).strip()
        body = match.group(3)

        sections[section_id] = {
            'title': title,
            'body': body,
            'issues': parse_issues_from_body(body)
        }

    return sections


def parse_issues_from_body(body: str) -> List[Dict]:
    """Распарсить issues из тела секции"""
    issues = []

    # Разделить на отдельные issues
    issue_blocks = re.split(r'## Issue:', body)

    for block in issue_blocks[1:]:  # Пропустить первый пустой блок
        lines = block.strip().split('\n')
        if not lines:
            continue

        # Первая строка - заголовок
        title = lines[0].strip()

        # Найти файлы и метаданные
        files = []
        priority = ""
        labels = ""

        for line in lines[1:]:
            line = line.strip()
            if line.startswith('- knowledge/'):
                files.append(line[2:].strip())
            elif 'Приоритет:' in line or 'Priority:' in line:
                priority = line.split(':', 1)[1].strip()
            elif 'Метки:' in line or 'Labels:' in line:
                labels = line.split(':', 1)[1].strip()

        if files:  # Только если есть файлы
            issues.append({
                'title': title,
                'files': files,
                'priority': priority,
                'labels': labels
            })

    return issues


def create_issue_body(issue_data: Dict, section_title: str) -> str:
    """Создать тело Issue"""
    title = issue_data['title']
    files = issue_data['files']

    body = f"""## {title}

**Категория:** {section_title}

### Файлы для обработки:
"""

    for file_path in files:
        body += f"- [ ] {file_path}\n"

    body += """
### Требования:
- [ ] Проанализировать все файлы в списке
- [ ] Определить зависимости и потенциальные blockers
- [ ] Создать план реализации
- [ ] Назначить ответственного агента согласно workflow

### Критерии готовности:
- [ ] Все файлы проанализированы
- [ ] Зависимости идентифицированы
- [ ] План реализации создан
- [ ] Задача передана следующему агенту

### Следующие шаги:
1. **Content Writer** или **Architect** - анализ контента
2. **API Designer** - если нужны API изменения
3. **Backend** - импорт в базу данных
4. **QA** - тестирование
5. **GameBalance** - балансировка (если применимо)

---
*Автоматически создано из github-issues-pending.yaml*
"""

    return body


def get_github_labels(labels_str: str, priority: str) -> List[str]:
    """Получить список меток для GitHub"""
    labels = []

    if labels_str:
        # Очистить от "Метки:" префикса
        clean_labels = labels_str.replace('Метки:', '').replace('Labels:', '').strip()
        # Разделить по запятой и очистить
        labels.extend([label.strip() for label in clean_labels.split(',')])

    if priority and priority not in [label.strip() for label in labels]:
        labels.append(priority)

    # Добавить стандартные метки
    labels.extend(['automation', 'task-creation'])

    return [label for label in labels if label]


def simulate_github_issue_creation(title: str, body: str, labels: List[str]) -> Dict:
    """Симулировать создание GitHub Issue"""
    issue_data = {
        'title': title,
        'body': body,
        'labels': labels,
        'state': 'open'
    }

    print(f"\n=== Issue: {title} ===")
    print(f"Labels: {', '.join(labels)}")
    print(f"Body length: {len(body)} characters")
    print(f"Files to process: {len(re.findall(r'- \\[ \\]', body))}")

    return issue_data


def main():
    print("Создание GitHub Issues из github-issues-pending.yaml")
    print("=" * 60)

    # Прочитать файл
    issues_file = Path(__file__).parent.parent / "knowledge/analysis/tasks/github-issues-pending.yaml"

    if not issues_file.exists():
        print(f"Файл {issues_file} не найден")
        return

    with open(issues_file, 'r', encoding='utf-8') as f:
        content = f.read()

    # Распарсить секции
    sections = parse_yaml_sections(content)

    print(f"Найдено {len(sections)} секций")

    total_issues = 0
    all_issues = []

    # Обработать каждую секцию
    for section_id, section_data in sections.items():
        section_title = section_data['title']
        issues = section_data['issues']

        print(f"\n{section_title} ({len(issues)} issues):")

        for issue_data in issues:
            title = issue_data['title']
            labels = get_github_labels(issue_data['labels'], issue_data['priority'])
            body = create_issue_body(issue_data, section_title)

            # Симулировать создание
            issue = simulate_github_issue_creation(title, body, labels)
            all_issues.append(issue)
            total_issues += 1

    print(f"\n{'=' * 60}")
    print(f"Итого создано: {total_issues} Issues")

    # Сохранить в JSON для отладки
    output_file = Path(__file__).parent / "github-issues-created.json"
    with open(output_file, 'w', encoding='utf-8') as f:
        json.dump(all_issues, f, ensure_ascii=False, indent=2)

    print(f"Результаты сохранены в: {output_file}")


if __name__ == '__main__':
    main()
