#!/usr/bin/env python3
"""
Скрипт для автоматического разбиения GitHub Issues с большим количеством файлов
на подзадачи по 3-5 файлов каждая.
"""

import os
import re
from typing import List, Tuple

def parse_file_list(issue_body: str) -> List[str]:
    """Извлекает список файлов из тела Issue."""
    files = []
    in_list = False
    
    for line in issue_body.split('\n'):
        if '## Список файлов' in line or '### Список файлов' in line:
            in_list = True
            continue
        if in_list and line.strip().startswith('- '):
            file_match = re.search(r'- (.+\.yaml)', line)
            if file_match:
                files.append(file_match.group(1))
        elif in_list and line.strip() and not line.strip().startswith('-'):
            if files:  # Если уже есть файлы, значит список закончился
                break
    
    return files

def split_files_into_chunks(files: List[str], chunk_size: int = 4) -> List[List[str]]:
    """Разбивает список файлов на чанки по 3-5 файлов."""
    chunks = []
    for i in range(0, len(files), chunk_size):
        chunk = files[i:i+chunk_size]
        if len(chunk) >= 3:  # Минимум 3 файла в чанке
            chunks.append(chunk)
        else:
            # Если осталось меньше 3 файлов, добавляем к последнему чанку
            if chunks:
                chunks[-1].extend(chunk)
            else:
                chunks.append(chunk)
    
    return chunks

def generate_issue_body(parent_issue: dict, files: List[str], part_num: int, total_parts: int) -> str:
    """Генерирует тело Issue для подзадачи."""
    files_list = '\n'.join([f'- {f}' for f in files])
    
    return f"""## Описание задачи

Обработать файлы {parent_issue['title']} Part {part_num} в `{parent_issue.get('path', '')}` без `github_issue`.

## Список файлов

{files_list}

## Приоритет

P2 (Средний)

## Компоненты

- [x] Lore
- [x] Canon
- [x] Timeline
- [x] Quests

## Этап разработки

canon

## Метки

- `agent:idea-writer`
- `stage:idea`
- `lore`
- `canon`
- `timeline`
- `quests`

## Связанные Issues

Родительская задача: {parent_issue.get('parent_numbers', '')}
"""

if __name__ == '__main__':
    print("Скрипт для разбиения Issues готов к использованию.")
    print("Используйте GitHub API для получения Issues и их разбиения.")

