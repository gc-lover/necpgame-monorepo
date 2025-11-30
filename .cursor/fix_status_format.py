#!/usr/bin/env python3
"""Обновить формат обновления статуса во всех командах"""
import re
import os
from pathlib import Path

commands_dir = Path('commands')

# Паттерн для поиска
pattern = r"id: '239690516'"
replacement = "id: 239690516  // число"

# Паттерн для value с сохранением названия статуса
value_pattern = r"value: '([^']+)'"
value_replacement = r"value: '{option_id}'  // id опции '\1' из list_project_fields"

files_updated = 0

for file_path in commands_dir.rglob('*.md'):
    try:
        content = file_path.read_text(encoding='utf-8')
        original = content
        
        # Заменить id
        content = re.sub(pattern, replacement, content)
        
        # Заменить value в контексте update_project_item (многострочный режим)
        if 'mcp_github_update_project_item' in content:
            # Найти блоки update_project_item (включая вложенные скобки)
            def replace_in_block(match):
                block = match.group(0)
                # Заменить value в этом блоке, сохраняя название статуса
                new_block = re.sub(
                    r"value: '([^']+)'",
                    r"value: '{option_id}'  // id опции '\1' из list_project_fields",
                    block
                )
                return new_block
            
            # Паттерн для блока update_project_item с учетом вложенных скобок
            # Ищем от mcp_github_update_project_item до закрывающей скобки
            lines = content.split('\n')
            new_lines = []
            in_block = False
            block_start = -1
            
            for i, line in enumerate(lines):
                if 'mcp_github_update_project_item' in line:
                    in_block = True
                    block_start = i
                    new_lines.append(line)
                elif in_block:
                    new_lines.append(line)
                    if line.strip().endswith('});') or (line.strip() == ');' and i > block_start + 5):
                        # Конец блока - обработать
                        block = '\n'.join(new_lines[block_start:])
                        processed = replace_in_block(re.match(r'.*', block, re.DOTALL))
                        new_lines[block_start:] = processed.split('\n')
                        in_block = False
                else:
                    new_lines.append(line)
            
            if in_block:
                # Обработать последний блок
                block = '\n'.join(new_lines[block_start:])
                processed = replace_in_block(re.match(r'.*', block, re.DOTALL))
                new_lines[block_start:] = processed.split('\n')
            
            content = '\n'.join(new_lines)
        
        if content != original:
            file_path.write_text(content, encoding='utf-8')
            files_updated += 1
            print(f"Updated: {file_path.name}")
    except Exception as e:
        print(f"Error in {file_path}: {e}")

print(f"\nTotal files updated: {files_updated}")

